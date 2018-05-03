define([
    'jquery',
    'backbone'
], function($, Backbone){
    var CreateConceptBoard = Backbone.Model.extend({
        urlRoot:baseRestApiUrl + '/MyGubbiApi/mindboard/add/default/mindboard/'
    });
    return CreateConceptBoard;
});
