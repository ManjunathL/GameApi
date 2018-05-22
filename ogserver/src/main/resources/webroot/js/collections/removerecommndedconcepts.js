define([
  'jquery',
  'underscore',
  'backbone',
  'models/removerecommndedconcept'
], function($, _, Backbone, RemovereCommndedConcept){
    var RemovereCommndedConcepts = Backbone.Collection.extend({
        model: RemovereCommndedConcept,
        url: baseRestApiUrl + 'MyGubbiApi/recommendation/add/dismissed/concept/',
        getremoverecommndedconcept: function(conceptId, options) {
            var urllnk = baseRestApiUrl + 'MyGubbiApi/recommendation/add/dismissed/concept/';
            this.url = urllnk + conceptId;
            return this.fetch(options);
        },
        initialize: function(models) {
          _.each(models, function (removerecommndedconcept){
            removerecommndedconcept = new AddConceptToCboard(removerecommndedconcept);
          });
        }
    });
  return RemovereCommndedConcepts;
});