define([
    'jquery',
    'backbone'
], function($, Backbone){
    var SearchConcept = Backbone.Model.extend({
      urlRoot:baseRestApiUrl + 'MyGubbiApi/search/concept/'
    });
    return SearchConcept;
});
