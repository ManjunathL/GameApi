define([
  'jquery',
  'lodash',
  'backbone',
  'bootstrap',
  'text!templates/product/product_details.html',
  'collections/product_details'
], function($, _, Backbone, Bootstrap, productDetailsPageTemplate, ProductDetails){
  var ProductDetailsPage = Backbone.View.extend({
    el: '.page',
    product_details: null,
    initialize: function() {
        this.product_details = new ProductDetails();
        this.listenTo(Backbone);
        _.bindAll(this, 'render','fetchProductsAndRender');
    },
    render: function () {
        var productId = this.model.id;
        var that = this;
        console.log("I m here ");
        var getProductsPromise = that.getProducts(productId);

        Promise.all([getProductsPromise]).then(function() {
            that.fetchProductsAndRender();
        });
    },
    getProducts: function(productId) {
     console.log("###########################");
        var that = this;
        return new Promise(function(resolve, reject) {
            if (!that.product_details.get('id')) {
                that.product_details.fetch({
                    data:{id: productId},
                    success: function() {
                        console.log("product details fetch successfully- ");
                        resolve();
                    },
                    error: function(model, response, options) {
                        console.log("error from design details fetch - " + response);
                        reject();
                    }
                });
            } else{
                resolve();
            }
        });
    },

    fetchProductsAndRender: function() {
        var that = this;
        var product_details = that.product_details;
         $(this.el).html(_.template(productDetailsPageTemplate)({
            'productsdetails':product_details.toJSON(),
        }));
    }
  });
  return ProductDetailsPage;
});
