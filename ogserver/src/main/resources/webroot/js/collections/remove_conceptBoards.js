define([
  'jquery',
  'underscore',
  'backbone',
  'models/remove_conceptBoard'
], function($, _, Backbone, RemoveConceptBoard){
    var RemoveConceptBoards = Backbone.Collection.extend({
        model: RemoveConceptBoard,
        url: baseRestApiUrl + 'MyGubbiApi/conceptboard/delete/',
        getremoveConceptBoard: function(conceptboardId, options) {
            var urllnk = baseRestApiUrl + 'MyGubbiApi/conceptboard/delete/';
            this.url = urllnk + conceptboardId;
            return this.fetch(options);
        },
        initialize: function(models) {
          _.each(models, function (remove_conceptBoard){
            remove_conceptBoard = new RemoveConceptBoard(remove_conceptBoard);
          });
        }
    });
  return RemoveConceptBoards;
});