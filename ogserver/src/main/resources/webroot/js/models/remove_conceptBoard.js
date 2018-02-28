define([
    'jquery',
    'backbone'
], function($, Backbone){
    var RemoveConceptBoard = Backbone.Model.extend({
        urlRoot:baseRestApiUrl + '/MyGubbiApi/conceptboard/delete/'
    });
    return RemoveConceptBoard;
});
