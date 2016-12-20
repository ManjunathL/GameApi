define([
    'jquery',
    'jqueryui',
    'underscore',
    'backbone',
    'bootstrap',
    'text!templates/product/page.html',
    'text!templates/product/page_small_grid.html',
    'text!templates/product/filter.html',
    'collections/products',
    'collections/categories',
    'collections/subcategories',
    'models/filter',
    'models/filterMaster',
    'models/seoFilterMaster',
    'mgfirebase',
    'analytics',
    'category_content',
    'views/view_manager'
], function($, jqueryui, _, Backbone, Bootstrap, productPageTemplate, productPageSmallGridTemplate, filterTemplate, Products, Categories, subCategories, Filter, FilterMaster, SEOFilterMaster, MGF, Analytics, CategoryContent, VM) {
    var ProductPage = Backbone.View.extend({
        el: '.page',
        products: null,
        filter: null,
        filterMaster: null,
        seoFilterMaster: null,
        categories: null,
        initialize: function() {
            Analytics.apply(Analytics.TYPE_GENERAL);
            this.products = new Products();
            this.filter = new Filter();
            this.filterMaster = new FilterMaster();
            this.seoFilterMaster = new SEOFilterMaster();
            this.categories = new Categories();
            this.subCategories = new subCategories();

            this.filter.on('change', this.render, this);
            this.listenTo(Backbone, 'user.change', this.handleUserChange);
            _.bindAll(this, 'clearShortlisted', 'markShortlisted', 'render');
        },
        render: function() {

            var that = this;

            window.filter = that.filter;

            var selectedCategories = this.model.selectedCategories;
            var selectedSubCategories = that.model.selectedSubCategories;
            var selectedCityName = this.model.cityName;



            this.filter.set({
                'selectedCategoryName':selectedCategories
            }, {
                silent: true
            });

            //if (typeof(that.model.searchTerm) !== 'undefined' && that.model.searchTerm != null) {
                that.filter.set({
                    'searchTerm':that.model.searchTerm
                }, {
                    silent: true
                });
            //}

            that.filter.set({
                'selectedSubCategoryName':that.model.selectedSubCategories
            }, {
                silent: true
            });

            if (!(that.filter.get('viewtype'))) {
                that.filter.set({
                    'viewtype': 'largegrid'
                }, {
                    silent: true
                });
            }
//debugger
            var subCategory = '';

            var selectedSubCategoriesList = {};

            var getFilterMasterPromise = that.getFilterMaster();
            var getCategoriesPromise = that.getCategories(selectedSubCategories);
            var getProductsPromise = that.getProducts();
            var getcityDetailsPromise = this.getCityDetails(selectedCityName);

            Promise.all([getFilterMasterPromise, getCategoriesPromise, getProductsPromise, getcityDetailsPromise]).then(function() {

                console.log(" ======= City Details =======");

                if(typeof(that.seoFilterMaster) !== 'undefined' && typeof(that.seoFilterMaster.get(0)) !== 'undefined'){
                 console.log("JSON TITLE");
                 console.log(JSON.stringify(that.seoFilterMaster.get(0).title));
                    if (!(that.filter.get('metaKeyword'))) {
                        that.filter.set({
                            'metaKeyword': that.seoFilterMaster.get(0).meta_keywords
                        }, {
                            silent: true
                        });
                    }
                    if (!(that.filter.get('location'))) {
                       that.filter.set({
                           'location': that.seoFilterMaster.get(0).location
                       }, {
                           silent: true
                       });
                   }
                     if (!(that.filter.get('metaTitle'))) {
                        that.filter.set({
                            'metaTitle': that.seoFilterMaster.get(0).title

                        }, {
                            silent: true
                        });
                    }
                    if (!(that.filter.get('metaDesc'))) {
                        that.filter.set({
                            'metaDesc': that.seoFilterMaster.get(0).description
                        }, {
                            silent: true
                        });
                    }
                }
                that.markShortlisted();
                that.productFilter();

                var categ = selectedSubCategories;
                var categ1 = selectedCategories;

                if (categ === undefined || categ === '' || categ === null) {
                    categ = selectedCategories;
                }
                console.log("selectedCategories");
                console.log(selectedCategories);
                console.log("categ");
                console.log(categ);
                console.log("selectedCityName");
                console.log(selectedCityName);
                CategoryContent.apply(selectedCategories,selectedSubCategories,selectedCityName);

            }).catch(function(err) {
            	console.log('Catch: ', err);
            });

        },
        getCityDetails: function(cityName){

            console.log('------------- cityName ------------');
            console.log(cityName);
            var that = this;
            var selectedCategoryName = that.filter.get('selectedCategoryName');
            var selectedSubCategoryName = that.filter.get('selectedSubCategoryName');

            console.log("selectedSubCategoryName");
            console.log(selectedSubCategoryName);

        return new Promise(function(resolve, reject) {
            if (!that.seoFilterMaster.get("location")) {
                that.seoFilterMaster.fetch({
                  data: {
                      "category": selectedCategoryName,
                      "subcategory": selectedSubCategoryName,
                      "location": cityName
                  },
                    success: function(response) {
                        console.log("SEO filterMaster fetch successfully- ");
                        console.log(response);
                        resolve();
                    },
                    error: function(model, response, options) {
                        console.log("error from SEO filterMaster fetch - " + response);
                        reject();
                    }
                 });
             } else{
                 resolve();
             }
         });
    },
        getFilterMaster: function() {
            var that = this;
            return new Promise(function(resolve, reject) {
                if (!that.filterMaster.get('price_ranges')) {
                    that.filterMaster.fetch({
                        success: function() {
                            console.log("filterMaster fetch successfully- ");
                            resolve();
                        },
                        error: function(model, response, options) {
                            console.log("error from filterMaster fetch - " + response);
                            reject();
                        }
                    });
                } else{
                    resolve();
                }
            });
        },
        getCategories: function(selectedSubCategories) {
            var that = this;
            return new Promise(function(resolve, reject) {
                if (that.categories.isEmpty()) {
                    that.categories.fetch({
                        success: function(response) {
                            console.log("categories fetch successfully- ");
                            var selectedCategory = that.categories.getByCode(that.model.selectedCategories);

                            if(typeof(selectedCategory) !== 'undefined'){
                                selectedSubCategoriesList = selectedCategory.toJSON().subCategories;

                                that.filter.set({
                                    'selectedSubCategoriesList':selectedSubCategoriesList.toJSON()
                                }, {
                                    silent: true
                                });
                                that.filter.set({
                                    'minPriceLimit': selectedCategory.toJSON().minPriceLimit
                                }, {
                                    silent: true
                                });
                                that.filter.set({
                                    'maxPriceLimit': selectedCategory.toJSON().maxPriceLimit
                                }, {
                                    silent: true
                                });
                            }

                            subCategory = that.categories.getSubCategoryBySubCategoryName(selectedSubCategories);

                            var subCategorynames = new Array();
                            subCategory && subCategorynames.push(subCategory.toJSON().name);
                            that.filter.set({
                                'selectedFiltersName': subCategorynames
                            }, {
                                silent: true
                            });
                            var filterIds = new Array();
                            subCategory && filterIds.push(subCategory.toJSON().name);
                            that.filter.set({
                                'filterIds': filterIds
                            }, {
                                silent: true
                            });

                            var subcatIds = new Array();
                            subCategory && subcatIds.push(subCategory.toJSON().id);

                            that.filter.set({
                                'subcatIds': subcatIds
                            }, {
                                silent: true
                            });

                            var priceRangeIds = new Array();
                            that.filter.set({
                                'priceRangeIds': priceRangeIds
                            }, {
                                silent: true
                            });

                            var priceRangeS = new Array();
                            priceRangeS.push(0);
                            that.filter.set({

                                'priceRangeS': priceRangeS
                            }, {
                                silent: true
                            });
                            var priceRangeS1 = new Array();
                               priceRangeS1.push(600000);
                            that.filter.set({

                                'priceRangeS1': priceRangeS1
                            }, {
                                silent: true
                            });

                            var styleIds = new Array();
                            that.filter.set({
                                'styleIds': styleIds
                            }, {
                                silent: true
                            });
                            resolve();
                        },
                        error: function(model, response, options) {
                            console.log("error from categories fetch - " + response);
                            reject();
                        }
                    });
                } else{
                    resolve();
                }
            });
        },
        getProducts: function() {
            var that = this;
            return new Promise(function(resolve, reject) {
                if (that.products.isEmpty()) {
                    if (that.model.searchTerm) {
                        that.products.url = restBase + '/api/es/search';
                    }
                    that.products.fetch({
                        data: {
                            "category": that.model.selectedCategories,
                            "term": that.model.searchTerm
                        },
                        success: function() {
                            console.log("products fetch successfully- ");
                            if (typeof(that.model.searchTerm) !== 'undefined' && that.model.searchTerm != null) {
                                that.getProductSubcategories();
                            }
                            resolve();
                        },
                        error: function(model, response, options) {
                            console.log("error from products fetch - " + response);
                            reject();
                        }
                    });
                } else{
                    resolve();
                }
            });
        },
        markShortlisted: function() {
            var shortlistedItems = MGF.getShortListedItems();
            var that = this;
            _.each(shortlistedItems, function(shortlistedProduct) {
                var shProducts = that.products.getProduct(shortlistedProduct.productId);
                shProducts && shProducts.set('user_shortlisted', true);
            });
        },
        clearShortlisted: function() {
            this.products && this.products.each(function(product) {
                product.set('user_shortlisted', false);
            });
        },
        getProductSubcategories: function() {
            var that = this;

            var subcatIds = new Array();
            var filterIds = new Array();
            var subCategorynames = new Array();

            var selectedSubCategoriesList = {};
            var subcategoryarr = [];
            that.products.each(function(product) {
                subcategoryarr.push({id: product.get('subcategoryId'), name: product.get('subcategory')});
                /*subcategoryarr.push({
                    name: product.get('subcategory')
                });*/
            });
            that.filter.set({
                'selectedSubCategoriesList': _.uniq(subcategoryarr, false, function(e) {
                    return e.name;
                })
            }, {
                silent: true
            });
            that.filter.set({
                'selectedFiltersName': subCategorynames
            }, {
                silent: true
            });

            that.filter.set({
                'filterIds': filterIds
            }, {
                silent: true
            });

            that.filter.set({
                'subcatIds': subcatIds
            }, {
                silent: true
            });

            var priceRangeIds = new Array();
            that.filter.set({
                'priceRangeIds': priceRangeIds
            }, {
                silent: true
            });
            var priceRangeS = new Array();
            that.filter.set({
                'priceRangeS': priceRangeS
            }, {
                silent: true
            });
             var priceRangeS1 = new Array();
                        that.filter.set({
                            'priceRangeS1': priceRangeS1
                        }, {
                            silent: true
                        });
            var styleIds = new Array();
            that.filter.set({
                'styleIds': styleIds
            }, {
                silent: true
            });
        },
        productFilter: function() {

            var that = this;

            if (typeof(that.filter.get('viewtype')) !== 'undefined') {
                that.filter.set({
                    'viewtype': that.filter.get('viewtype')
                }, {
                    silent: true
                });
            }
            if (typeof(that.filter.get('selectedSubCategoriesList')) !== 'undefined') {
                var selectedSubCategoriesList = that.filter.get('selectedSubCategoriesList');
                that.filter.set({
                    'filterflag': '1'
                }, {
                    silent: true
                });
            }

            if (typeof(that.filter.get('noFilterApplied')) == 'undefined') {
                that.filter.set({
                    'noFilterApplied': '0'
                }, {
                    silent: true
                });
            }

            var filterApplied = that.filter.get('noFilterApplied');

            var selectedfilterIds = that.filter.get('filterIds');
            var selectedSubcatIds = that.filter.get('subcatIds');
            var selectedPriceRangeIds = that.filter.get('priceRangeIds');
            var selectedPriceRangeS = that.filter.get('priceRangeS');
            var selectedPriceRangeS1 = that.filter.get('priceRangeS1');
            var selectedStyleIds = that.filter.get('styleIds');

            var minPrice = '';
            var maxPrice = '';
            if (typeof(that.filter.get('minPrice')) !== 'undefined' && typeof(that.filter.get('maxPrice')) !== 'undefined') {
                minPrice = that.filter.get('minPrice');
                maxPrice = that.filter.get('maxPrice');
                that.filter.set({
                    'filterflag': '1'
                }, {
                    silent: true
                });
            } else {
                minPrice = that.filter.get('minPriceLimit');
                maxPrice = that.filter.get('maxPriceLimit');
                that.filter.set({
                    'filterflag': '0'
                }, {
                    silent: true
                });
            }

            var sortDir = '';
            var sortBy = '';
            if (typeof(that.filter.get('sortDir')) !== 'undefined' && typeof(that.filter.get('sortBy')) !== 'undefined') {
                sortDir = that.filter.get('sortDir');
                sortBy = that.filter.get('sortBy');
                that.filter.set({
                    'filterflag': '1'
                }, {
                    silent: true
                });
            } else {
                that.filter.set({
                    'sortDir': 'desc'
                }, {
                    silent: true
                });
                that.filter.set({
                    'sortBy': 'relevance'
                }, {
                    silent: true
                });
                that.filter.set({
                    'filterflag': '0'
                }, {
                    silent: true
                });
                sortDir = 'desc';
                sortBy = 'relevance';
            }
            if (sortBy && sortDir) {
                that.products.sortBy(sortBy, sortDir);
            }


            if ((typeof(selectedSubcatIds) !== 'undefined') && (selectedSubcatIds.length != 0)) {
                var filteredProducts = that.products.filterBySubcat(selectedSubcatIds);
            } else {
                filteredProducts = that.products.toJSON();
            }


            if (selectedPriceRangeIds.length != 0) {
                if (typeof(filteredProducts) == 'undefined') {
                    filteredProducts = that.products.toJSON();
                    filteredProducts = that.products.filterByPriceRange(filteredProducts, selectedPriceRangeIds);
                } else {
                    filteredProducts = that.products.filterByPriceRange(filteredProducts, selectedPriceRangeIds);
                }
            }

            if (selectedPriceRangeS.length != 0) {
                if (typeof(filteredProducts) == 'undefined') {
                    filteredProducts = that.products.toJSON();
                    filteredProducts = that.products.filterByPriceRangeS(filteredProducts, selectedPriceRangeS);
                } else {
                    filteredProducts = that.products.filterByPriceRangeS(filteredProducts, selectedPriceRangeS);
                }

            }
            if (selectedPriceRangeS1.length != 0) {
                if (typeof(filteredProducts) == 'undefined') {
                    filteredProducts = that.products.toJSON();
                    filteredProducts = that.products.filterByPriceRangeS1(filteredProducts, selectedPriceRangeS1);
                } else {
                    filteredProducts = that.products.filterByPriceRangeS1(filteredProducts, selectedPriceRangeS1);
                }

            }

            if (selectedStyleIds.length != 0) {
                if (typeof(filteredProducts) == 'undefined') {
                    filteredProducts = that.products.toJSON();
                    filteredProducts = that.products.filterByStyle(filteredProducts, selectedStyleIds);
                } else {
                    filteredProducts = that.products.filterByStyle(filteredProducts, selectedStyleIds);
                }
            }

            if (filterApplied == '1') {
                filteredProducts = that.products.toJSON();
                that.filter.set({
                    'noFilterApplied': '0'
                }, {
                    silent: true
                });
            }

            var compiledTemplate = _.template(productPageTemplate);
            $(that.el).html(compiledTemplate({
                "collection": filteredProducts,
                "filter": that.filter.toJSON(),
                "subcategoriesList": selectedSubCategoriesList,
                "filterMaster": that.filterMaster.toJSON()
            }));


            var filteredTemplate = _.template(filterTemplate);

            $('.listing').append(filteredTemplate({
                "collection": filteredProducts
            }));
            return that;
        },
        events: {
            "click .shortlistable-item": "toggleShortListItem",
            "click .listshare": "toggleShareIcons",
            "click .gridshare": "toggleGridShareIcons",
            "click .glyphicon-th": "viewShortGridItem",
            "click .glyphicon-th-large": "viewLargeGridItem",
            "click .glyphicon-th-list": "viewListItem",
            "click #youtubelnk": "autoPlayYouTubeModal"

        },
        toggleShortListItem: function(e) {

            e.preventDefault();

            var currentTarget = $(e.currentTarget);
            var productId = currentTarget.data('element');
            var product = this.products.getProduct(productId);
            var alreadyShortlisted = product.get("user_shortlisted");
            var that = this;

            currentTarget.children('.list-heart').toggleClass('fa-heart-o');
            currentTarget.children('.list-heart').toggleClass('fa-heart');

            if (alreadyShortlisted) {
                MGF.removeShortlistProduct(productId).then(function() {
                    product.set('user_shortlisted', false);
                });
                $('#wishlist-ico').removeClass('active');
                $('#wishlist-ico1').removeClass('active');
            } else {
                MGF.addShortlistProduct(product.toJSON()).then(function() {
                    product.set('user_shortlisted', true);
                });
            }
            return false;
        },
        handleUserChange: function() {
            if (VM.activeView === VM.PRODUCT_LISTING) {
                this.clearShortlisted();
                this.markShortlisted();
                this.render();
            }
        },
        toggleShareIcons: function(e){
            e.preventDefault();

            var currentTarget = $(e.currentTarget);
            var shareicoId = currentTarget.attr('id');
            var productId = shareicoId.replace('share-ico','');
            $('#list-share-txt'+productId).toggle();
        },
        toggleGridShareIcons: function(e){
            e.preventDefault();

            var currentTarget = $(e.currentTarget);
            var shareicoId = currentTarget.attr('id');
            var productId = shareicoId.replace('share-grid-ico','');
            $('#grid-share-txt'+productId).toggle();
        },
        viewShortGridItem: function(e){
            var that = this;
            if (typeof(that.filter.get('viewtype')) !== 'undefined') {
                that.filter.set({
                    'viewtype': 'shortgrid'
                });
            }
            $(e.currentTarget).addClass('active');
            $('.glyphicon-th-large').removeClass('active');
            $('.glyphicon-th-list').removeClass('active');
            $('.glyphicon-th-full').removeClass('active');
            $('.list-heart').css('font-size', '22px');
            $('.listli').css('padding', '0px');
            $('.share-ico-txt').css('width', '85px');
            $('.share-ico-txt a').css('padding-left', '0px!important');
            $('h4').css('font-size', '15px').addClass('product-caption-overflow');
            $('.portfolio-caption').css('height', '82px');
            $('.portfolio-item').removeClass('col-md-6').addClass('col-md-4');
        },
        viewLargeGridItem: function(e){
            var that = this;
            if (typeof(that.filter.get('viewtype')) !== 'undefined') {
                that.filter.set({
                    'viewtype': 'largegrid'
                });
            }
            $(e.currentTarget).addClass('active');
            $('.glyphicon-th').removeClass('active');
            $('.glyphicon-th-list').removeClass('active');
            $('.glyphicon-th-full').removeClass('active');
            $('.listli').css('padding', '10px');
            $('h4').css('font-size', '20px').removeClass('product-caption-overflow');
            $('.portfolio-item').removeClass('col-md-4').addClass('col-md-6');
        },
        autoPlayYouTubeModal: function (ev) {
                  var that = this;
                  var theModal = $(ev.currentTarget).data("target"),
                  videoSRC = $(ev.currentTarget).attr("data-theVideo"),

                  videoSRCauto = videoSRC + "?autoplay=1";

                  $(theModal + ' iframe').attr('src', videoSRCauto);
                  $(theModal + ' button.close').click(function () {
                      $(theModal + ' iframe').attr('src', videoSRC);
                  });
                  $('.modal').click(function () {
                      $(theModal + ' iframe').attr('src', videoSRC);
                  });

                },
        viewListItem: function(e){
            var that = this;
            if (typeof(that.filter.get('viewtype')) !== 'undefined') {
                that.filter.set({
                    'viewtype': 'largelist'
                });
            }

            $(e.currentTarget).addClass('active');
            $('.glyphicon-th-large').removeClass('active');
            $('.glyphicon-th').removeClass('active');
            $('.glyphicon-th-full').removeClass('active');
            $('.col-7-img').removeClass('col-md-12').addClass('col-md-7');
            $('.col-5-txt').removeClass('col-md-12').addClass('col-md-5').css('padding-top', '0px');
            $('h2').css('font-size', '25px');
            $('h4').css('font-size', '20px').removeClass('product-caption-overflow');
            $('.dimsn').css('display', 'block');
        }

    });


    return ProductPage;
});