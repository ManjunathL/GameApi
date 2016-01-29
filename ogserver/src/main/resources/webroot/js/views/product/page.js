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
    'mgfirebase'
], function($, jqueryui, _, Backbone, Bootstrap, productPageTemplate, productPageSmallGridTemplate, filterTemplate, Products, Categories, subCategories, Filter, FilterMaster, MGF) {
    var ProductPage = Backbone.View.extend({
        el: '.page',
        products: null,
        filter: null,
        filterMaster: null,
        categories: null,
        initialize: function() {
            this.products = new Products();
            this.filter = new Filter();
            this.filterMaster = new FilterMaster();
            this.categories = new Categories();
            this.subCategories = new subCategories();
            this.filter.on('change', this.render, this);
            this.listenTo(Backbone, 'user.change', this.handleUserChange);
            _.bindAll(this, 'clearShortlisted', 'markShortlisted', 'render');
        },
        render: function() {

            var that = this;
            if (!this.filterMaster.get('price_ranges')) {
                this.filterMaster.fetch({
                    success: function() {
                        if (typeof(that.model.searchTerm) !== 'undefined' && that.model.searchTerm != null) {
                            that.fetchProductAndRender();
                        } else {
                            that.fetchCategoriesAndRender();
                        }
                    },
                    error: function(model, response, options) {
                        console.log("couldn't fetch filter master - " + response);
                    }
                });
            } else {
                if (typeof(that.model.searchTerm) !== 'undefined' && that.model.searchTerm != null) {
                    that.fetchProductAndRender();
                } else {
                    this.fetchCategoriesAndRender();
                }
            }

        },
        fetchCategoriesAndRender: function() {
            var that = this;
            window.filter = that.filter;

            var selectedSubCategories = that.model.selectedSubCategories;

            var subCategory = '';

            var selectedSubCategoriesList = {};
            if (that.categories.isEmpty()) {
                that.categories.fetch({
                    success: function() {

                        var selectedCategory = that.categories.getByCode(that.model.selectedCategories);

                        selectedSubCategoriesList = selectedCategory.toJSON().subCategories;

                        that.filter.set({
                            'selectedSubCategoriesList': selectedCategory.toJSON().subCategories.toJSON()
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

                        /*
                                                subCategory = that.categories.getSubCategoryBySubCategoryCode(selectedSubCategories);
                        */
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

                        var subcatNames = new Array();
                        subCategory && subcatNames.push(subCategory.toJSON().name);
                        that.filter.set({
                            'subcatNames': subcatNames
                        }, {
                            silent: true
                        });

                        var priceRangeIds = new Array();
                        that.filter.set({
                            'priceRangeIds': priceRangeIds
                        }, {
                            silent: true
                        });

                        var styleIds = new Array();
                        that.filter.set({
                            'styleIds': styleIds
                        }, {
                            silent: true
                        });

                        that.fetchProductAndRender();
                    },
                    error: function(model, response, options) {
                        console.log("couldn't fetch categories - " + response);
                    }
                });
            } else {
                that.productFilter();
            }

        },
        markShortlisted: function() {
            var shortlistedItems = MGF.getShortListedItems();
            var that = this;


            _.each(shortlistedItems, function(shortlistedProduct) {
                that.products.getProduct(shortlistedProduct.productId).set('user_shortlisted', true);
            });
        },
        clearShortlisted: function() {
            this.products && this.products.each(function(product) {
                product.set('user_shortlisted', false);
            });
        },
        fetchProductAndRender: function() {
            var that = this;
            if (that.products.isEmpty()) {
                that.products.fetch({
                    data: {
                        "categories": that.model.selectedCategories,
                        "searchTerm": that.model.searchTerm
                    },
                    success: function() {
                        that.markShortlisted();
                        if (typeof(that.model.searchTerm) !== 'undefined' && that.model.searchTerm != null) {
                            that.getProductSubcategories();
                        }
                        that.productFilter();
                    },
                    error: function(model, response, options) {
                        console.log("error from products fetch - " + response);
                    }
                });
            } else {
                that.productFilter();
            }
        },
        getProductSubcategories: function() {
            var that = this;
            window.filter = that.filter;

            var subcatNames = new Array();
            var filterIds = new Array();
            var subCategorynames = new Array();

            var selectedSubCategoriesList = {};

            var subcategoryarr = [];
            that.products.each(function(product) {
                //subcategoryarr.push({id: product.get('subCategId'), name: product.get('subCateg')});
                subcategoryarr.push({
                    name: product.get('subcategory')
                });
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
                'subcatNames': subcatNames
            }, {
                silent: true
            });

            var priceRangeIds = new Array();
            that.filter.set({
                'priceRangeIds': priceRangeIds
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
            var selectedSubcatNames = that.filter.get('subcatNames');
            var selectedPriceRangeIds = that.filter.get('priceRangeIds');
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


            if ((typeof(selectedSubcatNames) !== 'undefined') && (selectedSubcatNames.length != 0)) {
                var filteredProducts = that.products.filterBySubcat(selectedSubcatNames);
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
            "click .fa-share": "toggleShareIcons"
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
            currentTarget.children('.list-txt').html('shortlist' + (alreadyShortlisted ? '' : 'ed'));

            if (alreadyShortlisted) {
                MGF.removeShortlistProduct(productId).then(function() {
                    product.set('user_shortlisted', false);
                });
            } else {
                console.log(product.toJSON());
                MGF.addShortlistProduct(product.toJSON()).then(function() {
                    product.set('user_shortlisted', true);
                });
            }
            return false;
        },
        handleUserChange: function() {
            this.clearShortlisted();
            this.markShortlisted();
            this.render();
        },
        toggleShareIcons: function(e){
            e.preventDefault();

            var currentTarget = $(e.currentTarget);
            var shareicoId = currentTarget.attr('id');
            var productId = shareicoId.replace('share-ico','');
            $('#list-share-txt'+productId).toggle();
        }
    });
    return ProductPage;
});