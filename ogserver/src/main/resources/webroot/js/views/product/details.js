define([
    'jquery',
    'underscore',
    'cloudinary',
    'backbone',
    'bootstrap',
    'text!templates/product/details.html',
    'collections/products',
    'collections/categories'
], function($, _, cloudinary, Backbone, Bootstrap, productPageTemplate, Products, Categories) {
    var ProductPage = Backbone.View.extend({
        el: '.page',
        render: function() {
            that = this;
            var products = new Products();
            var categories = new Categories();
            categories.fetch({
                success: function(model, responce, options) {
                    products.fetch({
                        data: {
                            "product": that.model.selectedProduct
                        },
                        success: function(model, response, options) {
                        console.log('ssssssssssssssssssssssssss'+response);
                            var compiledTemplate = _.template(productPageTemplate);
                            $(that.el).append(compiledTemplate({
                                "collection": products.models[0].toJSON()
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
