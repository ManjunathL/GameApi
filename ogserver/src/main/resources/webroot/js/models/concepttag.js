define([
    'jquery',
    'backbone'
], function($, Backbone){
    var ConceptTag = Backbone.Model.extend({
        urlRoot:baseRestApiUrl + '/MyGubbiApi/conceptboard/get/conceptBoard/tagList/'
    });
    return ConceptTag;
});
