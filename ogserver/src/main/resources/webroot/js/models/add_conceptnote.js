define([
    'jquery',
    'backbone'
], function($, Backbone){
    var AddConceptNote = Backbone.Model.extend({
      urlRoot:baseRestApiUrl + '/MyGubbiApi/conceptboardConcept/add/notes/'
    });
    return AddConceptNote;
});
