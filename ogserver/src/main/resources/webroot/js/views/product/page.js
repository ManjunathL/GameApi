define([
    'jquery',
    'underscore',
    'cloudinary',
    'backbone',
    'bootstrap',
    'text!templates/product/page.html',
    'text!templates/product/page_small_grid.html',
    'text!templates/product/filter.html',
    'collections/products',
    'collections/categories'
], function($, _, cloudinary, Backbone, Bootstrap, productPageTemplate, productPageSmallGridTemplate, filterTemplate, Products, Categories) {
    var ProductPage = Backbone.View.extend({
        el: '.page',
        render: function() {
            that = this;
            var products = new Products();
            var categories = new Categories();
            categories.fetch({
                success: function(model, responce, options) {

                    var filter = {
                        "allCategories": categories,
                        "selectedCategories": that.model.selectedCategories,
                        "selectedSubCategories": that.model.selectedSubCategories,
                        "searchTerm": that.model.searchTerm,
                        "sortBy": that.model.sortBy,
                        "sortDir": that.model.sortDir,
                        "layout": that.model.layout
                    };

                    $(that.el).html(_.template(filterTemplate)(filter));

                    products.fetch({
                        data: {
                            "categories": that.model.selectedCategories,
                            "subCategories": that.model.selectedSubCategories,
                            "searchTerm": that.model.searchTerm
                        },
                        success: function(model, response, options) {

                            if (that.model.sortBy) {
                                products.sortBy(that.model.sortBy, that.model.sortDir);
                            }

                            var compiledTemplate;
                            if (that.model.layout && that.model.layout === 'sg') {
                              compiledTemplate = _.template(productPageSmallGridTemplate);
                            } else {
                              compiledTemplate = _.template(productPageTemplate); 
                            }
                            $(that.el).append(compiledTemplate({
                                "collection": products.toJSON()
                            }));
                        },
                        error: function(model, response, options) {
                            console.log("error from products fetch - " + response);
                        }
                    });
                },
                error: function(model, response, options){
                  console.log("couldn't fetch categories - " + response);
                }
            });

        }/*,
        initialize: function() {
            _.bindAll(this, 'render');
        }*/

    });
    return ProductPage;
});
