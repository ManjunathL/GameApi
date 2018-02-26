define([
    'jquery',
    'backbone'
], function($, Backbone){
    var ConceptBoard = Backbone.Model.extend({
        urlRoot:baseRestApiUrl + '/MyGubbiApi/conceptboard/create/default/conceptboard/'
    });
    return ConceptBoard;
});
