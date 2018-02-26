define([
    'jquery',
    'backbone'
], function($, Backbone){
    var AddConceptTag = Backbone.Model.extend({
      urlRoot:baseRestApiUrl + '/MyGubbiApi/conceptboardConcept/add/tags/'
    });
    return AddConceptTag;
});
