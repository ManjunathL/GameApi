define([
    'jquery',
    'backbone'
], function($, Backbone){
    var CreateConceptBoard = Backbone.Model.extend({
        urlRoot:baseRestApiUrl + '/MyGubbiApi/conceptboard/get/conceptboard/list/'
    });
    return CreateConceptBoard;
});
