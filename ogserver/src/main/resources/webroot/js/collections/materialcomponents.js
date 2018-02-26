define([
  'jquery',
  'underscore',
  'backbone',
  'models/materialcomponent'
], function($, _, Backbone, MaterialComponent){
    var MaterialComponents = Backbone.Collection.extend({
        model: MaterialComponent,
        url: baseApiUrl + '/gapi/workbench/materialcomponent/select',
        initialize: function(models) {
          _.each(models, function (materialcomponent){
            materialcomponent = new MaterialComponent(materialcomponent);
          });
        }
    });
  return MaterialComponents;
});
