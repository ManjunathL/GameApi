define([
  'jquery',
  'underscore',
  'backbone',
  'models/materialfinishmapping'
], function($, _, Backbone, MaterialFinishMapping){
    var MaterialFinishMappings = Backbone.Collection.extend({
        model: MaterialFinishMapping,
        url: baseApiUrl + '/gapi/workbench/materialfinishmapping/id',
        initialize: function(models) {
          _.each(models, function (materialfinishmapping){
            materialfinishmapping = new MaterialFinishMapping(materialfinishmapping);
          });
        }
    });
  return MaterialFinishMappings;
});
