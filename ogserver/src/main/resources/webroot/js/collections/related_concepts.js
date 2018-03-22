define([
  'jquery',
  'underscore',
  'backbone',
  'models/related_concept'
], function($, _, Backbone, RelatedConcept){
    var RelatedConcepts = Backbone.Collection.extend({
        model: RelatedConcept,
        url: baseRestApiUrl + 'MyGubbiApi/conceptboardConcept/get/related/conceptlist/',
        getRelatedConceptsList: function(conceptboardId, conceptCode, spaceTypeCode, pageno, itemPerPage, options) {
            var urllnk = baseRestApiUrl + 'MyGubbiApi/conceptboardConcept/get/related/conceptlist/';
            this.url = urllnk + conceptboardId + '/' + conceptCode+ '/' + spaceTypeCode+ '/' + pageno + '/' + itemPerPage;
            return this.fetch(options);
        },
        initialize: function(models) {
          _.each(models, function (related_concept){
            related_concept = new RelatedConcept(related_concept);
          });
        }
    });
  return RelatedConcepts;
});