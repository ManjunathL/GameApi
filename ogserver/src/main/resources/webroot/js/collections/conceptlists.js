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
        },
        filterByTags:function (conceptlist,selectedTag){
            var that = this;
            return _.map(conceptlist.filter(function(concept){
                return that.concepWithTags(concept, selectedTag);
            }), function (concept) {return concept });
        },
        concepWithTags: function (conceptObj, selectedTag) {
            var tagArrObj = conceptObj.conceptTag;
            var tagArr = tagArrObj.split(",");
            for (var i=0; i < tagArr.length; i++) {
                if (tagArr[i] == selectedTag)
                    return true;
            }
        }
    });
  return ConceptLists;
});