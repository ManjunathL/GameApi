define([
  'jquery',
  'underscore',
  'backbone',
  'models/needconceptlist'
], function($, _, Backbone, NeedConceptList){
    var NeedConceptLists = Backbone.Collection.extend({
        model: NeedConceptList,
        url: baseRestApiUrl + 'MyGubbiApi/recommendation/get/concepts/you/need/',
        getNeedConceptList: function(conceptboardId,pageno,itemPerPage, options) {
            var urllnk = baseRestApiUrl + 'MyGubbiApi/recommendation/get/concepts/you/need/';
            this.url = urllnk + conceptboardId + '/' + pageno + '/' + itemPerPage;
            return this.fetch(options);
        },
        initialize: function(models) {
          _.each(models, function (needconceptlist){
            needconceptlist = new NeedConceptList(needconceptlist);
          });
        },
        getConcept: function (needconceptlist, conceptCode) {
            var that = this;
            return _.map(needconceptlist.filter(function(concept){
                return that.getSingleConcept(concept, conceptCode);
            }), function (concept) {return concept });
        },
        getSingleConcept: function (conceptObj, conceptCode) {
            if (conceptObj.conceptCode == conceptCode)
               return true;
        },
        filterByTags:function (needconceptlist,selectedTag){
            var that = this;
            return _.map(needconceptlist.filter(function(concept){
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
  return NeedConceptLists;
});