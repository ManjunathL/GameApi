define([
  'jquery',
  'underscore',
  'cloudinary',
  'backbone',
  'bootstrap',
  'text!templates/product/page.html',
  'text!templates/product/filter.html',
  'collections/products'
], function($, _, cloudinary, Backbone, Bootstrap, productPageTemplate, filterTemplate, Products){
  var ProductPage = Backbone.View.extend({
    el: '.page',
    render: function (categ, subCateg) {
        var products = new Products();
        var that = this;
        $(this.el).html(_.template(filterTemplate));
        products.fetch({
            data: {
                category: categ,
                subCategory: subCateg
            },
            success: function() {
                var compiledTemplate = _.template(productPageTemplate);
                $(that.el).append(compiledTemplate({collection: products.models[0].toJSON()}));
            },
            error: function() {
                console.log("error from products fetch");
            }
        });
    }
  });
  return ProductPage;
});
