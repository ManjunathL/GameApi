define([
    'jquery',
    'backbone'
], function($, Backbone){
    var RemoveConceptFromCboard = Backbone.Model.extend({
        urlRoot:baseRestApiUrl + '/MyGubbiApi/conceptboardConcept/delete/'
    });
    return RemoveConceptFromCboard;
});
