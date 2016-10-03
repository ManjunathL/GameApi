define([
    'jquery',
    'underscore',
    'backbone',
    'bootstrap',
    'jqueryeasing',
    'text!/templates/product/details.html',
    '/js/views/product/details_helper.js',
    '/js/models/product.js',
    '/js/models/custom_product.js',
    'text!/templates/product/accessory.html',
    'text!/templates/product/appliance.html',
    'text!/templates/product/finish.html',
    'text!/templates/product/colors.html',
    '/js/collections/relatedproducts.js',
    'text!/templates/product/relatedproduct.html',
    '/js/slyutil.js',
    '/js/mgfirebase.js',
    '/js/consultutil.js',
    '/js/analytics.js',
    '/js/collections/appliances.js',
    '/js/views/view_manager.js'
], function($, _, Backbone, Bootstrap, JqueryEasing, productPageTemplate, DetailsHelper, ProductModel, CustomProduct, AccessoryTemplate, applianceTemplate, finishTemplate, colorsTemplate, RelatedProductCollection, relatedproductTemplate, SlyUtil, MGF, ConsultUtil, Analytics, ApplianceCollection, VM) {
    var ProductPage = Backbone.View.extend({
        el: '.page',
        ref: MGF.rootRef,
        refAuth: MGF.refAuth,
        appliancelst: '#applianceList',
        product: null,
        custom_product: null,
        RelatedProducts: null,
        initialize: function() {
            Analytics.apply(Analytics.TYPE_GENERAL);
            this.product = new ProductModel();
            window.product = this.product;
            this.custom_product = new CustomProduct();
            this.RelatedProducts = new RelatedProductCollection();
            this.Appliances = new ApplianceCollection();
            this.custom_product.on('change', this.render, this);
            this.listenTo(Backbone, 'user.change', this.handleUserChange);
            _.bindAll(this, 'render', 'markShortlisted', 'respond', 'setProductDetails');
        },
        render: function() {
            var that = this;
            window.custom_product = that.custom_product;

            var authData = this.refAuth.currentUser;
            MGF.getUserProfile(authData, this.setProductDetails);

        },
        setProductDetails: function(userProfData) {
            var that = this;
            if (!this.product.get('productId')) {
                this.product.set('id', this.model.id);
                this.product.set('productId', this.model.id);
                this.product.fetch({
                    success: function(response) {
                        that.markShortlisted();
                        that.respond(userProfData);
                    }
                });
            } else {
                this.respond(userProfData);
            }
        },
        markShortlisted: function() {
            var shortlisted = MGF.getShortListed(this.product.get('productId'));
            if (shortlisted) {
                this.product.set({
                    user_shortlisted: true
                }, {
                    silent: true
                });
                this.custom_product = new CustomProduct(shortlisted.custom_selections);
            } else {
                this.product.set({
                    user_shortlisted: false
                }, {
                    silent: true
                });
            }
        },
        handleUserChange: function() {
            if (VM.activeView === VM.PRODUCT_DETAILS) {
                this.markShortlisted();
                this.render();
            }
        },
        respond: function(userProfData) {
            var that = this;

            var selectedSubCategory = that.product.get('subcategory');

            var applianceType = new Array();
            var selectedCategory = that.product.get('categoryId');
            that.getAppliances(selectedCategory).then(function() {
                applianceType = that.Appliances.getApplianceType();
            });

            that.getRelatedProducts(selectedSubCategory).then(function() {

                if (!that.custom_product.get('basePrice')) {
                    that.custom_product.set({
                        'basePrice': that.product.get('defaultPrice')
                    }, {
                        silent: true
                    });
                }
                if (!that.custom_product.get('selectedMaterial')) {
                    that.custom_product.set({
                        'selectedMaterial': that.product.get('defaultMaterial')
                    }, {
                        silent: true
                    });
                }
                if (!that.custom_product.get('selectedFinish')) {
                    that.custom_product.set({
                        'selectedFinish': that.product.get('defaultFinish')
                    }, {
                        silent: true
                    });
                }
                if (!that.custom_product.get('finishes')) {
                    var finishes = new Array();
                    var finishobj = {};
                    _.map(that.product.get('mf'), function(model) {
                        if (model.material == that.product.get('defaultMaterial')) {
                            finishes.push(model.finish);
                            finishobj[model.finish] = model.basePrice;
                        } else {
                            return false;
                        }
                    });
                    that.custom_product.set({
                        'finishes': _.uniq(finishes)
                    }, {
                        silent: true
                    });
                    if (!that.custom_product.get('finishobj')) {
                        that.custom_product.set({
                            'finishobj': finishobj
                        }, {
                            silent: true
                        });
                    }
                }
                if (!that.custom_product.get('selectedAccessories')) {
                    var accessoryobj = {};
                    _.each(that.product.get('accessories'), function(acc) {
                        accessoryobj[acc.accessoryName] = acc.accessoryPrice;
                    });
                    that.custom_product.set({
                        'accessoryobj': accessoryobj
                    }, {
                        silent: true
                    });
                    that.custom_product.set({
                        'selectedAccessories': that.product.get('accessories')
                    }, {
                        silent: true
                    });
                }
                if (!that.custom_product.get('selectedAppliances')) {
                    var appliances = new Array();
                    that.custom_product.set({
                        'selectedAppliances': appliances
                    }, {
                        silent: true
                    });
                }
                if (!that.custom_product.get('colors')) {
                    that.changeColor(that.product.get('defaultFinish'), colorsTemplate);
                }

                var compiledTemplate = _.template(productPageTemplate);
                $(that.el).html(compiledTemplate({
                    "product": that.product.toJSON(),
                    "materials": _.uniq(_.pluck(that.product.get('mf'), 'material')),
                    "applianceTypes": _.uniq(applianceType),
                    "selectedColor": that.custom_product.get('colors'),
                    "selectedfinishes": that.custom_product.get('finishes'),
                    "selectedFinish": that.custom_product.get('selectedFinish'),
                    "selectedAccessories": _.uniq(that.custom_product.get('selectedAccessories')),
                    "relatedProducts": _.uniq(that.relatedProducts),
                    "custom_product": that.custom_product,
                    "appliances": that.Appliances.toJSON(),
                    'userProfile': userProfData
                }));

                DetailsHelper.ready(that);

            });

        },
        getRelatedProducts: function(selectedSubCategory) {
            var that = this;
            return new Promise(function(resolve, reject) {
                var selectedCategory = that.product.get('category');
                var selectedStyleId = that.product.get('styleId');
                var productId = that.product.get('productId');
                if (that.RelatedProducts.isEmpty()) {
                    that.RelatedProducts.fetch({
                        data: {
                            "category": selectedCategory,
                            "styleId": selectedStyleId,
                            "productId": productId
                        },
                        success: function(response) {
                            that.relatedProducts = response.toJSON();
                            resolve();
                        },
                        error: function(model, response, options) {
                            console.log("error from products fetch - " + response);
                        }
                    });
                } else {
                    resolve();
                }
            });

        },
        material: function(mf) {
            return mf.material;
        },
        finish: function(mf) {
            return mf.finish;
        },
        events: {
            "click .material": "changeMaterial",
            "click .finish": "changeFinish",
            "click .alt-accessory": "changeAccessory",
            "click .appliance": "changeAppliance",
            "click #product_consult": "openConsultPopup",
            "click #close-consult-pop": "closeModal",
            "click .dwf": "slideDelivery",
            "click .shortlistable-product": "toggleShortListProduct",
            "click .appliance-img": 'toggleColorNext',
            "click .appliance-mark": 'toggleColor'
        },
        toggleColorNext: function(e) {
            DetailsHelper.toggleColorNext(e);
        },
        toggleColor: function(e) {
            DetailsHelper.toggleColor(e);
        },
        toggleShortListProduct: function(e) {
            e.preventDefault();
            var currentTarget = $(e.currentTarget);
            var productId = this.product.get('productId');
            var alreadyShortlisted = this.product.get("user_shortlisted");
            var that = this;

            currentTarget.children('.product-heart').toggleClass('fa-heart-o');
            currentTarget.children('.product-heart').toggleClass('fa-heart');
            currentTarget.children('.product-heart-text').html('shortlist' + (alreadyShortlisted ? '' : 'ed'));

            if (alreadyShortlisted) {
                MGF.removeShortlistProduct(productId).then(function() {
                    that.product.set({
                        user_shortlisted: false
                    }, {
                        silent: true
                    });
                });
            } else {
                var productJsonObj = this.product.toJSON();
                productJsonObj['custom_selections'] = this.custom_product.toJSON();

                MGF.addShortlistProduct(productJsonObj).then(function() {
                    that.product.set({
                        user_shortlisted: true
                    }, {
                        silent: true
                    });
                });
            }
        },
        changeMaterial: function(ev) {
            $('.material').removeClass('active');
            $(ev.currentTarget).addClass('active');

            var selectedmaterial = $(ev.currentTarget).data('material');
            if (this.custom_product.get('selectedMaterial') !== 'undefined') {
                this.custom_product.set({
                    'selectedMaterial': selectedmaterial
                }, {
                    silent: true
                });
            }

            var finishes = new Array();
            var basePriceArr = new Array();
            var finishobj = {};
            _.map(this.product.get('mf'), function(model) {
                if (model.material == selectedmaterial) {
                    finishes.push(model.finish);
                    basePriceArr.push(model.basePrice);
                    finishobj[model.finish] = model.basePrice;
                } else {
                    return false;
                }
            });

            if (this.custom_product.get('finishobj') !== 'undefined') {
                this.custom_product.set({
                    'finishobj': finishobj
                }, {
                    silent: true
                });
            }

            if (this.custom_product.get('finishes') !== 'undefined') {
                this.custom_product.set({
                    'finishes': _.uniq(finishes)
                }, {
                    silent: true
                });
            }

            if (this.custom_product.get('basePrice') !== 'undefined') {
                this.custom_product.set({
                    'basePrice': basePriceArr[0]
                }, {
                    silent: true
                });
            }

            if (this.custom_product.get('selectedFinish') !== 'undefined') {
                this.custom_product.set({
                    'selectedFinish': finishes[0]
                }, {
                    silent: true
                });
            }

            if (this.custom_product.get('basePrice') !== '0') {
                var basePricetxt = this.custom_product.get('basePrice');
            } else {
                var basePricetxt = "Consult for Price";
            }
            $('#defaultbaseprice').html(basePricetxt);

            var nwfinishTemplate = _.template(finishTemplate);

            $('#finishList').html(nwfinishTemplate({
                "selectedfinishes": _.uniq(this.custom_product.get('finishes')),
                "selectedFinish": this.custom_product.get('selectedFinish')
            }));

            this.changeColor(finishes[0], colorsTemplate);
        },
        changeFinish: function(e) {
            $('.finish').removeClass('active');
            $(e.currentTarget).addClass('active');

            var selectedFinish = $(e.currentTarget).data('finish');
            if (this.custom_product.get('selectedFinish') !== 'undefined') {
                this.custom_product.set({
                    'selectedFinish': selectedFinish
                }, {
                    silent: true
                });
            }

            if (this.custom_product.get('basePrice') !== 'undefined') {
                this.custom_product.set({
                    'basePrice': this.custom_product.get('finishobj')[selectedFinish]
                }, {
                    silent: true
                });
            }

            this.changeColor(selectedFinish, colorsTemplate);
        },
        changeColor: function(selectedFinish, colorsTemplate) {
            var colors = {};
            _.map(this.product.get('fc'), function(result) {
                if (result.finish == selectedFinish) {
                    colors = result.color;
                } else {
                    return false;
                }
            });
            if (this.custom_product.get('colors') !== 'undefined') {
                this.custom_product.set({
                    'colors': colors
                }, {
                    silent: true
                });
            }


            if (this.custom_product.get('basePrice') !== '0') {
                var basePricetxt = this.custom_product.get('basePrice');
            } else {
                var basePricetxt = "Consult for Price";
            }
            $('#defaultbaseprice').html(basePricetxt);

            var colorsTemplate = _.template(colorsTemplate);

            $('#colorList').html(colorsTemplate({
                "selectedColor": this.custom_product.get('colors')
            }));

            return this;
        },
        changeAccessory: function(event) {

            $('.alt-accessory').removeClass('active');
            $(event.currentTarget).addClass('active');

            var selectedDefaultAccessoryId = $(event.currentTarget).data('daccessoryid');
            var selectedDefaultAccessory = $(event.currentTarget).data('daccessory');
            var selectedAltAccessory = $(event.currentTarget).data('altaccessory');
            var selectedAltAccessoryPrice = $(event.currentTarget).data('altaccessoryprice');
            var selectedAltAccessoryImg = $(event.currentTarget).data('altaccessoryimg');

            var defaultAccessoryPrice = 0;
            if (this.custom_product.get('accessoryobj') !== 'undefined') {
                defaultAccessoryPrice = this.custom_product.get('accessoryobj')[selectedDefaultAccessory];
            }

            var differencePrice = parseInt(selectedAltAccessoryPrice) - parseInt(defaultAccessoryPrice);

            if (this.custom_product.get('basePrice') !== 'undefined') {
                var defaultBaseprice = this.custom_product.get('basePrice');
                if (defaultBaseprice.indexOf(',') > -1) {
                    defaultBaseprice = defaultBaseprice.replace(/\,/g, '');
                } else {
                    defaultBaseprice = defaultBaseprice;
                }
                var basePrice = parseInt(defaultBaseprice) + parseInt(differencePrice);
                this.custom_product.set({
                    'basePrice': basePrice.toLocaleString()
                }, {
                    silent: true
                });
            }

            if (this.custom_product.get('selectedAccessories') !== 'undefined') {
                var accessoryobj = {};
                var accessoryList = this.custom_product.get('selectedAccessories');
                var k = 0;
                _.map(this.custom_product.get('selectedAccessories'), function(acc) {
                    if (acc.accessoryName == selectedDefaultAccessory) {
                        accessoryobj[selectedAltAccessory] = selectedAltAccessoryPrice;
                        accessoryList[k]['accessoryName'] = selectedAltAccessory;
                        accessoryList[k]['accessoryPrice'] = selectedAltAccessoryPrice;
                        accessoryList[k]['accessoryImg'] = selectedAltAccessoryImg;
                        accessoryList[k]['alternatives'] = _.uniq(acc.alternatives);
                    } else {
                        accessoryobj[accessoryList[k]['accessoryName']] = accessoryList[k]['accessoryPrice'];
                    }
                    k++;
                });
                this.custom_product.set({
                    'selectedAccessories': accessoryList
                }, {
                    silent: true
                });
                this.custom_product.set({
                    'accessoryobj': accessoryobj
                }, {
                    silent: true
                });

                if (this.custom_product.get('basePrice') !== '0') {
                    var basePricetxt = this.custom_product.get('basePrice');
                } else {
                    var basePricetxt = "Consult for Price";
                }
                $('#defaultbaseprice').html(basePricetxt);


                var compiledaccessoryTemplate = _.template(AccessoryTemplate);

                $('#accessoryList').html(compiledaccessoryTemplate({
                    "product": this.product.toJSON(),
                    "selectedAccessories": _.uniq(this.custom_product.get('selectedAccessories'))
                }));

                $('#accessory-image' + selectedDefaultAccessoryId).fadeOut(600, function() {
                    $('#accessory-image' + selectedDefaultAccessoryId).attr("src", imgBase + 'c_fit,w_206,h_124/' + selectedAltAccessoryImg);
                    $('#accessory-image' + selectedDefaultAccessoryId).fadeIn(200);
                });

                return this;
            }
        },
        changeAppliance: function(ev) {
            var selectedappliance = $(ev.currentTarget).data('appliance');
            var selectedapplianceId = $(ev.currentTarget).data('applianceid');
            var selectedapplianceType = $(ev.currentTarget).data('appliancetype');
            var selectedappliancePrice = $(ev.currentTarget).data('applianceprice');

            var appliances = this.custom_product.get('selectedAppliances');


            _.map(this.Appliances.toJSON(), function(model) {

                if (model.id == selectedapplianceId) {
                    if ($.inArray(selectedapplianceId, appliances) == -1) {
                        appliances.push(selectedapplianceId);

                        if(this.custom_product.get('basePrice') !== 'undefined'){
                            var defaultBaseprice = this.custom_product.get('basePrice');
                            if ((typeof(defaultBaseprice) === 'string') && defaultBaseprice.indexOf(',') > -1) {
                                defaultBaseprice=defaultBaseprice.replace(/\,/g,'');
                            }else{
                                defaultBaseprice=defaultBaseprice;
                            }
                            if((typeof(selectedappliancePrice) === 'string') && selectedappliancePrice.indexOf(',') > -1){
                                selectedappliancePrice=selectedappliancePrice.replace(/\,/g,'');
                            }else{
                                selectedappliancePrice=selectedappliancePrice;
                            }
                            var basePrice = parseInt(defaultBaseprice) + parseInt(selectedappliancePrice);
                            this.custom_product.set({'basePrice':basePrice.toLocaleString()},{silent: true});
                        }
                    } else {
                        appliances.splice($.inArray(selectedapplianceId, appliances), 1);
                        if(this.custom_product.get('basePrice') !== 'undefined'){
                            var defaultBaseprice = this.custom_product.get('basePrice');
                            if ((typeof(defaultBaseprice) === 'string') && defaultBaseprice.indexOf(',') > -1) {
                                defaultBaseprice=defaultBaseprice.replace(/\,/g,'');
                            }else{
                                defaultBaseprice=defaultBaseprice;
                            }
                            if((typeof(selectedappliancePrice) === 'string') && selectedappliancePrice.indexOf(',') > -1){
                                selectedappliancePrice=selectedappliancePrice.replace(/\,/g,'');
                            }else{
                                selectedappliancePrice=selectedappliancePrice;
                            }
                            var basePrice = parseInt(defaultBaseprice) - parseInt(selectedappliancePrice);
                            this.custom_product.set({'basePrice':basePrice.toLocaleString()},{silent: true});

                        }
                    }
                }
            });
            if (this.custom_product.get('basePrice') !== '0') {
                var basePricetxt = this.custom_product.get('basePrice');
            } else {
                var basePricetxt = "Consult for Price";
            }
            $('#defaultbaseprice').html(basePricetxt);
//            $('#defaultbaseprice').html(this.custom_product.get('basePrice'));
            if (this.custom_product.get('selectedAppliances') !== 'undefined') {
                this.custom_product.set({
                    'selectedAppliances': appliances
                }, {
                    silent: true
                });
            }
            return this;
        },
        closeModal: function(ev) {
            var id = $(ev.currentTarget).data('element');
            $(id).modal('toggle');
        },
        openConsultPopup: function() {
            $('#consultpop').modal('show');
        },
        slideDelivery: function() {
            if ($("#sldown").is(':visible')) {
                $("#sldown").hide();
                $("#slup").show();
            } else {
                $("#slup").hide();
                $("#sldown").show();
            }
            $('.dwf-desc').slideToggle();
        },
        getAppliances: function(selectedCategoryId) {
            var that = this;
            return new Promise(function(resolve, reject) {
                if (that.Appliances.isEmpty()) {
                    that.Appliances.fetch({
                        data: {
                            "category": selectedCategoryId
                        },
                        success: function(response) {
                            console.log("Successfully fetch appliances - ");
                            that.Appliances = response;
                            resolve();
                        },
                        error: function(model, response, options) {
                            console.log("error from appliances fetch - " + response);
                        }
                    });
                } else {
                    resolve();
                }
            });

        }
    });
    return ProductPage;
});