define([
  'jquery',
  'underscore',
  'backbone',
  'models/updatedefaultroom'
], function($, _, Backbone, UpdateDefaultRoom){
    var UpdateDefaultRooms = Backbone.Collection.extend({
        model: UpdateDefaultRoom,
        url: baseRestApiUrl +  'MyGubbiApi/conceptboard/update/',
        addUserDefaultRoomList: function(options) {
            var urllnk = baseRestApiUrl + 'MyGubbiApi/conceptboard/update';
            this.url = urllnk;
            return this.fetch(options);
        },
        initialize: function(models) {
          _.each(models, function (updatedefaultroom){
            updatedefaultroom = new ConceptBoard(updatedefaultroom);
          });
        }
    });
  return UpdateDefaultRooms;
});