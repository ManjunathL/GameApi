define([
    'jquery',
    'backbone'
], function($, Backbone){
    var CheckNameForMoreRoomList = Backbone.Model.extend({
        urlRoot:baseRestApiUrl + '/MyGubbiApi/conceptboard/check/name/'
    });
    return CheckNameForMoreRoomList;
});
