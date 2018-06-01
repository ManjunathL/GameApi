define([
  'jquery',
  'underscore',
  'backbone',
  'models/deleteroomlist'
], function($, _, Backbone, DeleteRoomList){
    var DeleteRoomLists = Backbone.Collection.extend({
        model: DeleteRoomList,
        url: baseRestApiUrl + 'MyGubbiApi/conceptboard/delete/',
        removeRoomLists: function(conceptboardId, options){
            var urllnk = baseRestApiUrl + 'MyGubbiApi/conceptboard/delete/';
            this.url = urllnk + conceptboardId ;
            return this.fetch(options);
        },
        initialize: function(models) {
          _.each(models, function (deleteroomlist){
            deleteroomlist = new DeleteProject(deleteroomlist);
          });
        }
    });
  return DeleteRoomLists;
});