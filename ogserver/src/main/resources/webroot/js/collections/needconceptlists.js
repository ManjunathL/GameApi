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
            return _.map(needconceptlist.filter(function(needconceptlist){
                return that.needconcepWithTags(needconceptlist, selectedTag);
            }), function (needconceptlist) {return needconceptlist });
        },
        needconcepWithTags: function (conceptObj, selectedTag) {
            if(typeof(conceptObj.tags) !== 'undefined' && conceptObj.tags !== null){
                var tagArrObj = conceptObj.tags;
                var tagArr = tagArrObj.split(",");
                for (var i=0; i < tagArr.length; i++) {
                    if (tagArr[i].toLowerCase() == selectedTag.toLowerCase())
                        return true;
                }
            }
        },
         filterByElement:function (conceptlist,selectedElement){
             var that = this;
             return _.map(conceptlist.filter(function(concept){
                 return that.needconcepWithElements(concept, selectedElement);
             }), function (concept) {return concept });
         },
         needconcepWithElements: function (conceptObj, selectedElement) {
             var spaceElementCode = conceptObj.spaceElementCode;

                 if (spaceElementCode == selectedElement)
                     return true;

         }
    });
  return NeedConceptLists;
});