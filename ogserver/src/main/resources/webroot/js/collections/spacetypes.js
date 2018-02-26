define([
  'jquery',
  'underscore',
  'backbone',
  'models/spacetype'
], function($, _, Backbone, SpaceType){
    var SpaceTypes = Backbone.Collection.extend({
        model: SpaceType,
        url: baseApiUrl + '/gapi/workbench/conceptmaster/spacetype',
        initialize: function(models) {
          _.each(models, function (spacetype){
            spacetype = new SpaceType(spacetype);
          });
        },
        getSpaceType: function () {
             var result = new Array();
             this.each(function (model) {
               var spacetype = model.get('space_type');
               if (spacetype) {
                   result.push(spacetype);
               }
             });
            return result;
        }
    });
  return SpaceTypes;
});
