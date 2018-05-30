define([
    'jquery',
    'backbone'
], function($, Backbone){
    var UpdateDefaultRoom = Backbone.Model.extend({
        urlRoot:baseRestApiUrl + '/MyGubbiApi/conceptboard/update/'
    });
    return UpdateDefaultRoom;
});
