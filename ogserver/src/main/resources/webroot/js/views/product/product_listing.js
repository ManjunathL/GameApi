define([
  'jquery',
  'underscore',
  'backbone',
  'bootstrap',
  'text!templates/product/product_listing.html',
  'views/view_manager',
  'collections/products'
], function($, _, Backbone, Bootstrap, productListingPage, VM, Products){
  var ProductListingPage = Backbone.View.extend({
    el: '.page',
    products: null,
    initialize: function() {
        this.products = new Products();
        this.listenTo(Backbone);
        _.bindAll(this, 'render','fetchProductsAndRender');
    },
    render: function () {
        var that = this;
      this.products.fetch({
          success: function(response) {
            that.fetchProductsAndRender(response);
          },
          error: function(model, response, options) {
              console.log("couldn't fetch product data - " + response);
          }
      });

    },
    fetchProductsAndRender: function() {
        var that = this;
        var products = that.products;
        $(this.el).html(_.template(productListingPage)({
        'productdetails':products.toJSON()
        }));
    }
  });
  return ProductListingPage;
});
