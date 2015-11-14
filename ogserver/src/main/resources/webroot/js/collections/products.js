define([
  'jquery',
  'backbone',
  'models/products'
], function($, Backbone, Product){
  var Products = Backbone.Collection.extend({
    model: Product,
    url: restBase + '/api/products'
  });
  return Products;
});
