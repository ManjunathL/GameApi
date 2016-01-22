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
    'models/filter',
    'models/filterMaster',
    'mgfirebase'
], function($, jqueryui, _, Backbone, Bootstrap, productPageTemplate, productPageSmallGridTemplate, filterTemplate, Products, Categories, Filter, FilterMaster, MGF) {
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
            this.filter.on('change', this.render, this);
            this.listenTo(Backbone, 'user.change', this.handleUserChange);
            _.bindAll(this, 'clearShortlisted', 'markShortlisted', 'render');
        },
        render: function() {

            var that = this;

            if (!this.filterMaster.get('price_ranges')) {
                this.filterMaster.fetch({
                    success: function() {
                        that.fetchCategoriesAndRender();
                    },
                    error: function(model, response, options) {
                        console.log("couldn't fetch filter master - " + response);
                    }
                });
            } else {
                this.fetchCategoriesAndRender();
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
                            'selectedSubCategoriesList': selectedCategory.toJSON().subCategories
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

                        subCategory = that.categories.getSubCategoryBySubCategoryCode(selectedSubCategories);

                        if (subCategory) {
                            var subCategorynames = new Array();
                            subCategorynames.push(subCategory.toJSON().name);
                            that.filter.set({
                                'selectedFiltersName': subCategorynames
                            }, {
                                silent: true
                            });
                            var fillterIds = new Array();
                            fillterIds.push(subCategory.toJSON().id);
                            that.filter.set({
                                'fillterIds': fillterIds
                            }, {
                                silent: true
                            });
                        }

                        if (that.products.isEmpty()) {
                            that.products.fetch({
                                data: {
                                    "categories": that.model.selectedCategories,
                                    "searchTerm": that.model.searchTerm
                                },
                                success: function() {
                                    that.markShortlisted();
                                    that.productFilter();
                                },
                                error: function(model, response, options) {
                                    console.log("error from products fetch - " + response);
                                }
                            });
                        }

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
                that.products.getProduct(shortlistedProduct.id).set('user_shortlisted', true);
            });
        },
        clearShortlisted: function() {
            this.products && this.products.each(function(product){
                product.set('user_shortlisted', false);
            });
        },
        productFilter: function() {

            var that = this;

            if (typeof(that.filter.get('selectedSubCategoriesList')) !== 'undefined') {
                var selectedSubCategoriesList = that.filter.get('selectedSubCategoriesList').toJSON();
                that.filter.set({
                    'filterflag': '1'
                }, {
                    silent: true
                });
            }

            var selectedfillterIds = that.filter.get('fillterIds');

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

            var filteredProducts = that.products.filterByPrice(minPrice, maxPrice, selectedfillterIds);
            if (Object.keys(filteredProducts).length == 0) {
                console.log('here');
                filteredProducts = that.products.toJSON();
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
            "click .shortlistable-item": "toggleShortListItem"
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
        }
    });
    return ProductPage;
});