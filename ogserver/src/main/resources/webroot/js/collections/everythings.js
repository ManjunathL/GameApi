define([
  'jquery',
  'underscore',
  'backbone',
  'models/everything'
], function($, _, Backbone, Everything){
    var Everythings = Backbone.Collection.extend({
        model: Everything,
        url: baseRestApiUrl + 'MyGubbiApi/conceptMaster/get/concept-list/',
         getEverythingList: function(userId, userMindboardId, pageno, itemPerPage,options) {
                var urllnk = baseRestApiUrl + 'MyGubbiApi/conceptMaster/get/concept-list/';
                this.url = urllnk + userId + '/' + userMindboardId + '/' + pageno + '/' + itemPerPage ;
                return this.fetch(options);
        },
        initialize: function(models) {
          _.each(models, function (everything){
            everything = new Everything(everything);
          });
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
                 if (tagArr[i].toLowerCase() == selectedTag.toLowerCase())
                     return true;
             }
         }
    });
  return Everythings;
});
