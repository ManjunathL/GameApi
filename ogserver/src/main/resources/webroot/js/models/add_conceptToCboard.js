define([
    'jquery',
    'backbone'
], function($, Backbone){
    var AddConceptToCboard = Backbone.Model.extend({
        urlRoot:baseRestApiUrl + '/MyGubbiApi/conceptboardConcept/web/add/'
    });
    return AddConceptToCboard;
});
