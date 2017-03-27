define([
  'jquery',
  'underscore',
  'backbone',
  'models/addon'
], function($, _, Backbone, Addon){
    var Addon = Backbone.Collection.extend({
        model: Addon,
        url: restBase + '/api/addon',
        initialize: function(models) {
          _.each(models, function (addon){
            addon = new Addon(addon);
          });
        },
        getCategoriesList: function (addonsobj) {
            var result = new Array();
            var temp=null;
            this.each(function (model) {
              var categoryCode = model.get('categoryCode');

              if (categoryCode!=temp) {

               result.push(categoryCode);
              }
              temp=categoryCode;
            });
            return result;
        },
        getSubcategoriesList: function(selectedCategory) {
            var resultSubcat = new Array();
            var temp=null;
            this.each(function (model) {
            var catcode = model.get('categoryCode');
              if (catcode === selectedCategory.trim()) {
                var xx = model.get('productTypeCode');
                if(xx!=temp)
                resultSubcat.push(xx);
              }
              temp=xx;
            });
            return resultSubcat;
        },
         getProductSubtypeList: function(productSubtype) {
            var resultProSubtype = new Array();
            var temp=null;
            this.each(function (model) {
            var protypecode = model.get('productTypeCode');
              if (protypecode === productSubtype.trim()) {
                var xx = model.get('productSubtypeCode');
                if(xx!=temp)
                resultProSubtype.push(xx);
              }
              temp=xx;
            });
            return resultProSubtype;
        },
        getBrandList: function(brand) {
            var resultProBrand = new Array();
            var temp=null;
            this.each(function (model) {
            var prosubtypecode = model.get('productSubtypeCode');
              if (prosubtypecode === brand.trim()) {
                var xx = model.get('brandCode');
                if(xx!=temp)
                resultProBrand.push(xx);
              }
              temp=xx;
            });
            return resultProBrand;
        },
        filterCategorywise: function(selCat){
            var that = this;
            return _.map(this.filter(function(model){
                return model.get('categoryCode') === selCat;
            }), function (model) {return model.toJSON();});
        },
        filterProductTypewise: function(resObj,seProTyp,selCat){
            var that = this;
            return _.map(this.filter(function(resObj){
                return resObj.get('categoryCode') === selCat && resObj.get('productTypeCode') === seProTyp;
            }), function (resObj) {return resObj.toJSON();});
        },
        filterProductSubtypewise: function(resObj,seProSubtyp,seProTyp,selCat){
          var that = this;
          return _.map(this.filter(function(resObj){
              return resObj.get('categoryCode') === selCat && resObj.get('productTypeCode') === seProTyp && resObj.get('productSubtypeCode') === seProSubtyp;
          }), function (resObj) {return resObj.toJSON();});
        },
        filterBrandwise: function(resObjj,seBrand,seProSubtyp,seProTyp,selCat){
          var that = this;
          return _.map(this.filter(function(resObjj){
              return resObjj.get('categoryCode') === selCat && resObjj.get('productTypeCode') === seProTyp && resObjj.get('productSubtypeCode') === seProSubtyp && resObjj.get('brandCode') === seBrand;
          }), function (resObjj) {return resObjj.toJSON();});
        }

    });
  return Addon;
});