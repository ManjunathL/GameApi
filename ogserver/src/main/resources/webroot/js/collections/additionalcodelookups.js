define([
  'jquery',
  'underscore',
  'backbone',
  'models/additionalcodelookup'
], function($, _, Backbone, AdditionalCodeLookup){
    var AdditionalCodeLookups = Backbone.Collection.extend({
        model: AdditionalCodeLookup,
        url: baseApiUrl + '/gapi/workbench/additionalcodelookup',
        initialize: function(models) {
          _.each(models, function (additionalcodelookup){
            additionalcodelookup = new AdditionalCodeLookup(additionalcodelookup);
          });
        }
    });
  return AdditionalCodeLookups;
});
