define([
    'jquery',
    'backbone'
], function($, Backbone){
    var RelatedConcept = Backbone.Model.extend({
        urlRoot:baseRestApiUrl + '/MyGubbiApi/conceptboardConcept/get/related/conceptlist/'
    });
    return RelatedConcept;
});
