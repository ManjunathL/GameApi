define([
  'jquery',
  'underscore',
  'backbone',
  'models/concept'
], function($, _, Backbone, Concept){
    var Concepts = Backbone.Collection.extend({
        model: Concept,
        url: baseApiUrl + '/gapi/workbench/conceptmaster/select',
        initialize: function(models) {
          _.each(models, function (concept){
            concept = new Concept(concept);
          });
        }
    });
  return Concepts;
});
