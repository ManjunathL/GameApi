define([
    'jquery',
    'backbone'
], function($, Backbone){
    var DeleteRoomList = Backbone.Model.extend({
        urlRoot:baseRestApiUrl + '/MyGubbiApi/conceptboard/delete/'
    });
    return DeleteRoomList;
});
