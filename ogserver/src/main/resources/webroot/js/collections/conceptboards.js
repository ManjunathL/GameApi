define([
  'jquery',
  'underscore',
  'backbone',
  'models/conceptboard'
], function($, _, Backbone, ConceptBoard){
    var ConceptBoards = Backbone.Collection.extend({
        model: ConceptBoard,
        url: baseRestApiUrl + 'MyGubbiApi/conceptboard/get/conceptboard/list/',
        getConceptBoardList: function(userId,userMindboardId,pageno,itemPerPage, options) {
            var urllnk = baseRestApiUrl + 'MyGubbiApi/conceptboard/get/conceptboard/list/';
            this.url = urllnk + userId + '/' + userMindboardId + '/' + pageno + '/' + itemPerPage;
            return this.fetch(options);
        },
        initialize: function(models) {
          _.each(models, function (conceptboard){
            conceptboard = new ConceptBoard(conceptboard);
          });
        }
    });
  return ConceptBoards;
});