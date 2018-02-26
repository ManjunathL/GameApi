define([
    'jquery',
    'backbone'
], function($, Backbone){
    var SimilarConcept = Backbone.Model.extend({
        urlRoot:baseRestApiUrl + 'MyGubbiApi/conceptboardConcept/get/similar/conceptlist/'
    });
    return SimilarConcept;
});
