define([
  'jquery',
  'underscore',
  'backbone',
  'models/codelookup'
], function($, _, Backbone, CodeLookup){
    var CodeLookups = Backbone.Collection.extend({
        model: CodeLookup,
        url: baseApiUrl + '/gapi/workbench/codelookup',
        initialize: function(models) {
          _.each(models, function (codelookup){
            codelookup = new CodeLookup(codelookup);
          });
        },
        getLookupType: function () {
             var result = new Array();
             this.each(function (model) {
               var lookupType = model.get('title');
               if (lookupType) {
                   result.push(lookupType);
               }
             });
            return result;
        }
    });
  return CodeLookups;
});
