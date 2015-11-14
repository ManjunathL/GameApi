define([
  'jquery',
  'backbone'
], function($, Backbone){
  var Product = Backbone.Model.extend({
    urlRoot: restBase + '/api/products'
  });
  return Product;
});
