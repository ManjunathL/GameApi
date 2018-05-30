define([
    'jquery',
    'backbone'
], function($, Backbone){
    var AddMoreRoomList = Backbone.Model.extend({
        urlRoot:baseRestApiUrl + '/MyGubbiApi/conceptboard/create/blank/'
    });
    return AddMoreRoomList;
});
