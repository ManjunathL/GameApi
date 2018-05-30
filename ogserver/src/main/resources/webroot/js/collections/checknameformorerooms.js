define([
  'jquery',
  'underscore',
  'backbone',
  'models/checknameformoreroom'
], function($, _, Backbone, CheckNameForMoreRoomList){
    var CheckNameForMoreRoomLists = Backbone.Collection.extend({
        model: CheckNameForMoreRoomList,
        url: baseRestApiUrl +  'MyGubbiApi/conceptboard/check/name/',
        checkNameForMoreRoom: function(userId,userProjectId,options) {
            var urllnk = baseRestApiUrl + 'MyGubbiApi/conceptboard/check/name/';
            this.url = urllnk +'?userId='+ userId + '&userProjectId=' + userProjectId;
            return this.fetch(options);
        },
        initialize: function(models) {
          _.each(models, function (checknameformoreroom){
            checknameformoreroom = new ConceptBoard(checknameformoreroom);
          });
        }
    });
  return CheckNameForMoreRoomLists;
});