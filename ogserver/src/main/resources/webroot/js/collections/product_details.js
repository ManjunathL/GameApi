define([
  'jquery',
  'underscore',
  'backbone',
  'models/product_detail'
], function($, _, Backbone, ProductDetail){
    var ProductDetails = Backbone.Collection.extend({
        model: ProductDetail,
        url: baseApiUrl + '/gapi/workbench/productmaster/id',
        initialize: function(models) {
          _.each(models, function (product_detail){
            product_detail = new ProductDetail(product_detail);
          });
        }
    });
  return ProductDetails;
});
