define([
    'jquery',
    'backbone'
], function($, Backbone){
    var ConceptAllTag = Backbone.Model.extend({
        urlRoot:baseRestApiUrl + 'MyGubbiApi/conceptboard/get/all/conceptBoard/tagList/'
    });
    return ConceptAllTag;
});
