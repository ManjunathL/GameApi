define([
  'jquery',
  'underscore',
  'backbone',
  'models/similar_concept'
], function($, _, Backbone, SimilarConcept){
    var SimilarConcepts = Backbone.Collection.extend({
        model: SimilarConcept,
        url: baseRestApiUrl + 'MyGubbiApi/conceptboardConcept/get/similar/conceptlist/',
        getSimilarConceptsList: function(conceptboardId, conceptCode, pageno, itemPerPage, options) {
            var urllnk = baseRestApiUrl + 'MyGubbiApi/conceptboardConcept/get/similar/conceptlist/';
            this.url = urllnk + conceptboardId + '/' + conceptCode+ '/' + pageno + '/' + itemPerPage;
            return this.fetch(options);
        },
        getSimilarConcept: function (conceptlist, conceptCode) {
            var that = this;
            return _.map(conceptlist.filter(function(concept){
                return that.getSingleSimilarConcept(concept, conceptCode);
            }), function (concept) {return concept });
        },
        getSingleSimilarConcept: function (conceptObj, conceptCode) {
            if (conceptObj.conceptCode == conceptCode)
               return true;
        },
        initialize: function(models) {
          _.each(models, function (similar_concept){
            similar_concept = new SimilarConcept(similar_concept);
          });
        }
    });
  return SimilarConcepts;
});