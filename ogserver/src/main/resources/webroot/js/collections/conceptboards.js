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
        },
        getConceptBoardName: function(cBoardList,cBoardId){

            console.log("Coming Here??");
//            console.log(cBoardList.toJSON());
            console.log(cBoardList);

            for (var i=0; i < cBoardList.length; i++) {
                if (cBoardList[i].id == cBoardId){
                console.log(cBoardList[i].name);
                    return cBoardList[i].name;
                    }
            }
        }
    });
  return ConceptBoards;
});