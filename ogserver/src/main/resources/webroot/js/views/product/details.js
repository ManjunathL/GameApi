define([
    'jquery',
    'underscore',
    'cloudinary',
    'backbone',
    'bootstrap',
    'text!templates/product/details.html',
    'models/product',
    'models/selectedProduct'
], function($, _, cloudinary, Backbone, Bootstrap, productPageTemplate, ProductModel, SelectedProduct) {
    var ProductPage = Backbone.View.extend({
        el: '.page',
        render: function() {

            var product = this.model.products.getProduct(this.model.id);
            var selectedProduct = new SelectedProduct();
            selectedProduct.set('originalProduct', product);

            var compiledTemplate = _.template(productPageTemplate);
            $(this.el).html(compiledTemplate({
                "product": product.toJSON(),
                "selectedProduct": selectedProduct.toJSON()
            }));
        }
    });
    return ProductPage;
});
