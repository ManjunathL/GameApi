define([
    'jquery',
    'backbone'
], function($, Backbone){
    var AddConceptboard = Backbone.Model.extend({
        urlRoot:baseRestApiUrl + '/MyGubbiApi/conceptboard/add/template/'
    });
    return AddConceptboard;
});
