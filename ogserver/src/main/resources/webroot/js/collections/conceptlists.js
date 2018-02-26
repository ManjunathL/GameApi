define([
  'jquery',
  'underscore',
  'backbone',
  'models/conceptlist'
], function($, _, Backbone, ConceptList){
    var ConceptLists = Backbone.Collection.extend({
        model: ConceptList,
        url: baseRestApiUrl + 'MyGubbiApi/conceptboardConcept/get/concept-list/',
        getConceptList: function(conceptboardId,pageno,itemPerPage, options) {
            var urllnk = baseRestApiUrl + 'MyGubbiApi/conceptboardConcept/get/concept-list/';
            this.url = urllnk + conceptboardId + '/' + pageno + '/' + itemPerPage;
            return this.fetch(options);
        },
        initialize: function(models) {
          _.each(models, function (conceptlist){
            conceptlist = new ConceptList(conceptlist);
          });
        },
        getConcept: function (conceptlist, id) {
            var that = this;
            return _.map(conceptlist.filter(function(concept){
                return that.getSingleConcept(concept, id);
            }), function (concept) {return concept });
        },
        getSingleConcept: function (conceptObj, id) {
            if (conceptObj.id == id)
               return true;
        }
    });
  return ConceptLists;
});