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
    'models/filterMaster'
], function($, jqueryui, _, Backbone, Bootstrap, productPageTemplate, productPageSmallGridTemplate, filterTemplate, Products, Categories, Filter, FilterMaster) {
    var ProductPage = Backbone.View.extend({
        el: '.page',
        products: new Products(),
        filter: new Filter(),
        filterMaster: new FilterMaster(),
        categories: new Categories(),
        initialize: function() {
            this.filter.on('change', this.render, this);
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
                                'selectedSubCategoriesName': subCategorynames
                            }, {
                                silent: true
                            });
                            var subCategoryIds = new Array();
                            subCategoryIds.push(subCategory.toJSON().id);
                            that.filter.set({
                                'subcategoryIds': subCategoryIds
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

            var selectedsubCategoryIds = that.filter.get('subcategoryIds');

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

            var filteredProducts = that.products.filterByPrice(minPrice, maxPrice, selectedsubCategoryIds);
            if (Object.keys(filteredProducts).length == 0) {
                console.log('here');
                filteredProducts = that.products.toJSON();
            }
            var compiledTemplate = _.template(productPageTemplate);
            $(that.el).html(compiledTemplate({
                "collection": filteredProducts,
                "filter": that.filter.toJSON(),
                "subcategoriesList": selectedSubCategoriesList,
                "filterMaster": that.filterMaster
            }));

            var filteredTemplate = _.template(filterTemplate);

            $('.listing').append(filteredTemplate({
                "collection": filteredProducts
            }));
            return that;
        }
    });
    return ProductPage;
});
