define([
  'jquery',
  'underscore',
  'backbone',
  'models/finishmapping'
], function($, _, Backbone, FinishMapping){
    var FinishMappings = Backbone.Collection.extend({
        model: FinishMapping,
        url: baseApiUrl + '/gapi/workbench/finishmapping/material',
        initialize: function(models) {
          _.each(models, function (finishmapping){
            finishmapping = new FinishMapping(finishmapping);
          });
        }
    });
  return FinishMappings;
});
