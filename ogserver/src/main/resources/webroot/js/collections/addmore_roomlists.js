define([
  'jquery',
  'underscore',
  'backbone',
  'models/addmore_roomlist'
], function($, _, Backbone, AddMoreRoomList){
    var AddMoreRoomLists = Backbone.Collection.extend({
        model: AddMoreRoomList,
        url: baseRestApiUrl +  'MyGubbiApi/conceptboard/create/',
        addUserMoreRoomList: function(options) {
            var urllnk = baseRestApiUrl + 'MyGubbiApi/conceptboard/create/';
            this.url = urllnk;
            return this.fetch(options);
        },
        initialize: function(models) {
          _.each(models, function (addmore_roomlist){
            addmore_roomlist = new ConceptBoard(addmore_roomlist);
          });
        }
    });
  return AddMoreRoomLists;
});