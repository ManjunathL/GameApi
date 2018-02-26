define([
  'jquery',
  'underscore',
  'backbone',
  'models/product'
], function($, _, Backbone, Product){
    var Products = Backbone.Collection.extend({
        model: Product,
        url: baseApiUrl + '/gapi/workbench/productmaster/select',
        initialize: function(models) {
          _.each(models, function (product){
            product = new Product(product);
          });
        }
    });
  return Products;
});