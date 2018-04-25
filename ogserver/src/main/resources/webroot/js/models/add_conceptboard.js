define([
    'jquery',
    'backbone'
], function($, Backbone){
    var AddConceptboard = Backbone.Model.extend({
        urlRoot:baseRestApiUrl + '/MyGubbiApi/conceptboard/web/add/template/'
    });
    return AddConceptboard;
});
