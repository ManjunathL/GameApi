define([
  'jquery',
  'backbone',
  'models/category',
  'collections/subcategories'
], function($, Backbone, Category, SubCategories){
  var Categories = Backbone.Collection.extend({
    model: Category,
    url: restBase + '/api/categories',
    getByCode: function (code) {
        return this.find(function (model) {
            if (model.get('code') === code) {
                return model;
            }
        });
    },
    getSubCategoryBySubCategoryCode: function (code) {
        var result = null;
        this.each(function (model) {
            var subCategory = model.get('subCategories').getByCode(code);
            if (subCategory) {
                result = subCategory;
            }
        });
        return result;
    },
    getSubCategoryBySubCategoryName: function (name) {
        var result = null;
        this.each(function (model) {
          var subCategory = model.get('subCategories').getBySubcategoryName(name);
          if (subCategory) {
              result = subCategory;
          }
        });
        return result;
    },
    parse: function(response) {
      _.each(response, function(category){
        category.subCategories = new SubCategories(category.subCategories);
      });
      return response;
    }
  });
  return Categories;
});
