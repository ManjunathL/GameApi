define([
  'jquery',
  'backbone',
  '/js/models/subcategory.js'
], function($, Backbone, SubCategory){
  var SubCategories = Backbone.Collection.extend({
    model: SubCategory,
    getByCode: function (code) {
        return this.find(function (model) {
            if (model.get('code') === code) {
                return model;
            }
        });
    },
     getBySubcategoryName: function (name) {
         return this.find(function (model) {
             if (model.get('name') === name) {
                 return model;
             }
         });
     },
    initialize: function(models) {
      _.each(models, function (subcategory){
        subcategory = new SubCategory(subcategory);
      });
    }
  });
  return SubCategories;
});
