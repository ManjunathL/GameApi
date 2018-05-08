define([
  'jquery',
  'underscore',
  'backbone',
  'models/roomlayout'
], function($, _, Backbone, RoomLayout){
    var RoomLayouts = Backbone.Collection.extend({
        model: RoomLayout,
        url: baseRestApiUrl + 'MyGubbiApi/roomLayout/get/',
        getRoomLayoutList: function(spacetypeCode, options) {
            var urllnk = baseRestApiUrl + 'MyGubbiApi/roomLayout/get/';
            this.url = urllnk + '/' + spacetypeCode;
            return this.fetch(options);
        },
        initialize: function(models) {
          _.each(models, function (roomlayout){
            roomlayout = new RoomLayout(roomlayout);
          });
        }
    });
  return RoomLayouts;
});
