define([
  'jquery',
  'backbone',
  'models/subcategory'
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
    initialize: function(models) {
      _.each(models, function (subcategory){
        subcategory = new SubCategory(subcategory);
      });
    }
  });
  return SubCategories;
});
