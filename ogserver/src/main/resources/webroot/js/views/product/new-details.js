/**
 * Created by mygubbi on 15/7/16 By Smruti
 */
define([
    'jquery',
    'underscore',
    'backbone',
    'bootstrap',
    'jqueryeasing',
    'text!templates/product/new-details.html',
    'views/product/details_helper',
    'models/product',
    'models/custom_product',
    'text!templates/product/new-finish.html',
    'collections/relatedproducts',
    'text!templates/product/relatedproduct.html',
    'models/product_filter',
    'slyutil',
    'mgfirebase',
    'analytics',
    'category_content',
    'views/view_manager'
], function($, _, Backbone, Bootstrap, JqueryEasing, productPageTemplate, DetailsHelper, ProductModel, CustomProduct, FinishTemplate, RelatedProductCollection, relatedproductTemplate, Filter, SlyUtil, MGF, Analytics, CategoryContent, VM) {
    var ProductPage = Backbone.View.extend({
        el: '.page',
        ref: MGF.rootRef,
        refAuth: MGF.refAuth,
        product: null,
        initialize: function() {
            Analytics.apply(Analytics.TYPE_GENERAL);
            this.product = new ProductModel();
            this.productfilter = new Filter();
            window.product = this.product;
            this.custom_product = new CustomProduct();
            this.RelatedProducts = new RelatedProductCollection();
            this.custom_product.on('change', this.render, this);
            this.listenTo(Backbone, 'user.change', this.handleUserChange);
            _.bindAll(this, 'render', 'markShortlisted', 'respond', 'setProductDetails');
        },
        render: function() {
            var that = this;
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
            var selectedCategory = that.product.get('categoryId');
            var productName = that.product.get('name');

            CategoryContent.apply(selectedCategory,selectedSubCategory,'',productName);

            window.productfilter = that.productfilter;
             this.productfilter.set({
                 'selectedCategoryName':selectedCategory
             }, {
                 silent: true
             });
             this.productfilter.set({
                 'selectedSubCategoryName':selectedSubCategory
             }, {
                 silent: true
             });
             this.productfilter.set({
                 'productName':productName
             }, {
                 silent: true
             });
            var compiledTemplate = _.template(productPageTemplate);
            $(that.el).html(compiledTemplate({
                "product": that.product.toJSON(),
                'userProfile': userProfData
            }));

            DetailsHelper.ready(that);

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