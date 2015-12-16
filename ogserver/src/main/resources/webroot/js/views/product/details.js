define([
    'jquery',
    'underscore',
    'backbone',
    'bootstrap',
    'text!templates/product/details.html',
    'models/product'
], function($, _, Backbone, Bootstrap, productPageTemplate, ProductModel) {
    var ProductPage = Backbone.View.extend({
        el: '.page',
        render: function() {
            var that = this;

            var product = new ProductModel({id: '123'});
           // var product = new ProductModel({id: that.model.id});
            //var product = this.model.getProduct(that.model.id);
            console.log(product);
            product.fetch({
                success: function (response) {
                    var compiledTemplate = _.template(productPageTemplate);
                    $(that.el).html(compiledTemplate({
                        "product": response.toJSON()
                    }));
                }
            });

        }
    });
    return ProductPage;
});
