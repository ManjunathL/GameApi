define([
  'jquery',
  'backbone'
], function($, Backbone){
  var Category = Backbone.Model.extend({
    urlRoot: restBase + '/api/categories'
  });
  return Category;
});
