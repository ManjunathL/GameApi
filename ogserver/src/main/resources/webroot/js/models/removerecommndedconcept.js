define([
    'jquery',
    'backbone'
], function($, Backbone){
    var RemovereCommndedConcept = Backbone.Model.extend({
        urlRoot:baseRestApiUrl + '/MyGubbiApi/recommendation/add/dismissed/concept/'
    });
    return RemovereCommndedConcept;
});
