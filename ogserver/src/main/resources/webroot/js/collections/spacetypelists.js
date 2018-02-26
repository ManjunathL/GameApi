define([
  'jquery',
  'underscore',
  'backbone',
  'models/spacetypelist'
], function($, _, Backbone, SpaceTypeList){
    var SpaceTypeLists = Backbone.Collection.extend({
        model: SpaceTypeList,
        url: baseRestApiUrl + 'MyGubbiApi/spacetype/get',
        initialize: function(models) {
          _.each(models, function (spacetypelist){
            spacetypelist = new SpaceTypeList(spacetypelist);
          });
        }
    });
  return SpaceTypeLists;
});
