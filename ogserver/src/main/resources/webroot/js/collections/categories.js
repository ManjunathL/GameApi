define([
  'jquery',
  'backbone',
  'models/categories'
], function($, Backbone, Category){
  var Categories = Backbone.Collection.extend({
    model: Category,
    url: restBase + '/api/categories'
  });
  return Categories;
});
