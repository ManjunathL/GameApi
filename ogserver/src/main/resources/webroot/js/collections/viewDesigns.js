define([
  'jquery',
  'underscore',
  'backbone',
  'models/view_Design'
], function($, _, Backbone, ViewDesign){
    var ViewDesigns = Backbone.Collection.extend({
        model: ViewDesign,
        url: baseRestApiUrl + 'MyGubbiApi/designs/getdesignsperspace/',
        getDesigns: function(spaceTypeCode, options) {
            var urllnk = baseRestApiUrl + 'MyGubbiApi/designs/getdesignsperspace/';
            this.url = urllnk + spaceTypeCode +'?filteredStyles=&createdByFilter=1&myFavFilter=false';
            return this.fetch(options);
        },
        initialize: function(models) {
          _.each(models, function (view_Design){
            view_Design = new ViewDesign(view_Design);
          });
        },
        filterByStyleName:function (designlist,selectedStylenm){
            var that = this;
            return _.map(designlist.filter(function(design){
                return that.designWithStyles(design, selectedStylenm);
            }), function (design) {return design });
        },
        designWithStyles: function (designObj, selectedStylenm) {
            var designStyleArrObj = designObj.designStyle;
            var designStyleArr = designStyleArrObj.split(",");
            for (var i=0; i < designStyleArr.length; i++) {
                if (designStyleArr[i] == selectedStylenm)
                    return true;
            }
        }
    });
  return ViewDesigns;
});