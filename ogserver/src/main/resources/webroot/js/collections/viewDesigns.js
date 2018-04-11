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
            this.url = urllnk + spaceTypeCode +'?filteredStyles=ST-CONTEMPORARY&createdByFilter=1&myFavFilter=false';
            return this.fetch(options);
        },
        initialize: function(models) {
          _.each(models, function (view_Design){
            view_Design = new ViewDesign(view_Design);
          });
        }
    });
  return ViewDesigns;
});