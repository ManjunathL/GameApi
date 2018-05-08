define([
    'jquery',
    'backbone'
], function($, Backbone){
    var RoomLayout = Backbone.Model.extend({
        urlRoot:baseRestApiUrl + 'MyGubbiApi/roomLayout/get/'
    });
    return RoomLayout;
});
