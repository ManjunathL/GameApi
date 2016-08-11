/**
 * Created by mygubbi on 15/7/16 By Smruti
 */
define([
    'jquery',
    'underscore',
    'backbone',
    'bootstrap',
    'jqueryeasing',
    'text!/templates/product/new-details.html',
    '/js/views/product/details_helper.js',
    '/js/models/product.js',
    '/js/models/custom_product.js',
    'text!/templates/product/new-finish.html',
    '/js/collections/relatedproducts.js',
    'text!/templates/product/relatedproduct.html',
    '/js/slyutil.js',
    '/js/mgfirebase.js',
    '/js/analytics.js',
    '/js/views/view_manager.js'
], function($, _, Backbone, Bootstrap, JqueryEasing, productPageTemplate, DetailsHelper, ProductModel, CustomProduct, FinishTemplate, RelatedProductCollection, relatedproductTemplate, SlyUtil, MGF, Analytics, VM) {
    var ProductPage = Backbone.View.extend({
        el: '.page',
        ref: MGF.rootRef,
        product: null,
        initialize: function() {
            Analytics.apply(Analytics.TYPE_GENERAL);
            this.product = new ProductModel();
            window.product = this.product;
            this.custom_product = new CustomProduct();
            this.RelatedProducts = new RelatedProductCollection();
            this.custom_product.on('change', this.render, this);
            this.listenTo(Backbone, 'user.change', this.handleUserChange);
            _.bindAll(this, 'render', 'markShortlisted', 'respond', 'setProductDetails');
        },
        render: function() {
            var that = this;
            var authData = this.ref.getAuth();
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
            var selectedCategory = that.product.get('categoryId');

            var compiledTemplate = _.template(productPageTemplate);
            $(that.el).html(compiledTemplate({
                "product": that.product.toJSON(),
                'userProfile': userProfData
            }));

            DetailsHelper.ready(that);

           /* that.getRelatedProducts(selectedSubCategory).then(function() {

                if (!that.custom_product.get('selectedMaterial')) {
                    that.custom_product.set({
                        'selectedMaterial': that.product.get('defaultMaterial')
                    }, {
                        silent: true
                    });
                }

                if (!that.custom_product.get('material')) {
                    var materials = new Array();
                    var materialobj = {};
                    _.map(that.product.get('material'), function(model) {
                        materials.push(model.material);
                        if (model.material == that.product.get('defaultMaterial')) {
                            materialobj[model.material] = {"Desc":model.materialDescription,"Img":model.materialImage};
                        } else {
                            return false;
                        }
                    });



                    that.custom_product.set({
                        'materials': _.uniq(materials)
                    }, {
                        silent: true
                    });
                    if (!that.custom_product.get('materialobj')) {
                        that.custom_product.set({
                            'materialobj': materialobj
                        }, {
                            silent: true
                        });
                    }
                }

                if (!that.custom_product.get('selectedStyle')) {
                    that.custom_product.set({
                        'selectedStyle': that.product.get('styleName')
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

                if (!that.custom_product.get('finish')) {
                    var finishes = new Array();
                    var finishobj = {};
                    _.map(that.product.get('finish'), function(model) {
                        finishes.push(model.finishName);
                        if (model.finishName == that.product.get('defaultFinish')) {
                            var finishTypeArr = new Array();
                            var finishStyleObj = {};
                            _.map(model.finishes, function(finstype) {
                                finishTypeArr.push(finstype.finishType);
                                _.map(finstype.finishStyleType, function(finsStyletype) {
                                    if(finsStyletype.finishStyleId == that.product.get('styleId')){
                                        finishStyleObj[finstype.finishType] = finsStyletype.finishImage;
                                    }else{
                                        return false;
                                    }
                                });

                            });
                            finishobj[model.finishName] = {"Desc":model.finishDescription,"FinishType":finishTypeArr,"FinishStyleObj":finishStyleObj};
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


                var compiledTemplate = _.template(productPageTemplate);
                $(that.el).html(compiledTemplate({
                    "product": that.product.toJSON(),
                    "materialobj":materialobj,
                    "materials":materials,
                    "finishobj":finishobj,
                    "finishes":finishes,
                    "custom_product": that.custom_product,
                    "relatedProducts": _.uniq(that.relatedProducts),
                    'userProfile': userProfData
                }));

                DetailsHelper.ready(that);

            });*/

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
        events: {
            "click #product_consult": "openConsultPopup",
            "click #close-consult-pop": "closeModal",
            /*"click li.choose-material": "changeMaterial",
            "click li.choose-finish": "changeFinish",*/
            "click li.design-style": "changeDesignStyle",
            "click li.acc-img-cnt": "changeAccessory",
            "click li.tab_material": "changeMaterialTab"
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
        closeModal: function(ev) {
            var id = $(ev.currentTarget).data('element');
            $(id).modal('toggle');
        },
        openConsultPopup: function() {
            $('#consultpop').modal('show');
        },
        /*changeMaterial: function(event) {
            var that = this;

            var selectedMaterial = $(event.currentTarget).data("material");

            if (typeof(that.custom_product.get('selectedMaterial')) !== 'undefined') {
                that.custom_product.set({
                    'selectedMaterial': selectedMaterial
                }, {
                    silent: true
                });

                var materials = new Array();
                var materialobj = {};
                _.map(that.product.get('material'), function(model) {
                    materials.push(model.material);
                    if (model.material == selectedMaterial) {
                        console.log(model.materialDescription);
                        materialobj[model.material] = {"Desc":model.materialDescription,"Img":model.materialImage};
                    } else {
                        return false;
                    }
                });

                that.custom_product.set({
                    'materials': _.uniq(materials)
                }, {
                    silent: true
                });
                if (typeof(that.custom_product.get('materialobj')) !== 'undefined') {
                    that.custom_product.set({
                        'materialobj': materialobj
                    }, {
                        silent: true
                    });
                }
            }

            $("#material-img-content").attr("src", "https://res.cloudinary.com/mygubbi/image/upload/v1468317950/project_details/"+materialobj[selectedMaterial].Img)
            $("#material-tab-content").html('<div class="choose-active tab-pane active" id="'+selectedMaterial+'"><p>'+materialobj[selectedMaterial].Desc+'</p></div>');

            return this;
        },
        changeFinish: function(event) {
            var that = this;

            var selectedFinish = $(event.currentTarget).data("finish");

            if (typeof(that.custom_product.get('selectedFinish')) !== 'undefined') {
                that.custom_product.set({
                    'selectedFinish': selectedFinish
                }, {
                    silent: true
                });
            }
            var finishes = new Array();
            var finishobj = {};


            var finishStyleObj1 = {};
            var finishStyleObj = {};

            if (!that.custom_product.get('finish')) {
                _.map(that.product.get('finish'), function(model) {
                    finishes.push(model.finishName);
                    if (model.finishName == selectedFinish) {
                        var finishTypeArr = new Array();

                        _.map(model.finishes, function(finstype) {
                          var result =  _.filter( finstype.finishStyleType, function ( model ) {
                                return model.finishStyleId == "cmono";
                           });

                           _.map(result, function (value, key) {
                             finishStyleObj[finstype.finishType] = result[key].finishImage;
                             finishStyleObj1 = result[key].finishStyle;
                           });

                            finishTypeArr.push(finstype.finishType);

                        });

                        finishobj[model.finishName] = {"Desc":model.finishDescription,"FinishType":finishTypeArr,"finishStyleObj":finishStyleObj};
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

            $("#finish-tab-content").html('<div class="choose-active tab-pane active" id="'+selectedFinish+'"><p>'+finishobj[selectedFinish].Desc+'</p></div>');
            //$("#finishList").html('hiiiiiiiiiiiiiiiiiiiiii');

            var nwfinishTemplate = _.template(FinishTemplate);

            $('#finishList').html(nwfinishTemplate({
                "finishobj": finishobj,
                "finishStyleObj":finishStyleObj,
                "selectedFinish": this.custom_product.get('selectedFinish')
            }));


            return this;
        },*/
        changeAccessory: function(e) {
                                    e.preventDefault();
                                    var currentTarget = $(e.currentTarget);
                                    var id = $(e.currentTarget).attr('id');
                                    $(".acc-img").removeClass('active');
                                    $(".acc-cnt").removeClass('active');
                                    $(".acc-img-cnt").removeClass('active');

                                    $("#"+id+"-img").addClass('active');
                                    $("#"+id).addClass('active');
                                    $("#"+id+"-cnt").addClass('active');

                                    return this;
          },
        changeDesignStyle: function(e) {
             e.preventDefault();
             var currentTarget = $(e.currentTarget);
             var id = $(e.currentTarget).attr('id');
             $(".design-style-img").removeClass('active');
             $(".design-style-cnt").removeClass('active');
             $(".design-style").removeClass('active');

             $("#"+id+"-img").addClass('active');
             $("#"+id).addClass('active');
             $("#"+id+"-cnt").addClass('active');

             return this;
        },
         changeMaterialTab: function(ev) {
              ev.preventDefault();
              var currentTarget = $(ev.currentTarget);
              var id = $(ev.currentTarget).attr('id');
              $(".material-img").removeClass('active');
              $(".material-cnt").removeClass('active');
              $(".tab_material").removeClass('active');

              $("#"+id+"-img").addClass('active');
              $("#"+id).addClass('active');
              $("#"+id+"-cnt").addClass('active');

              return this;
         }
    });
    return ProductPage;
});