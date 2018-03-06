define([
    'jquery',
    'backbone'
], function($, Backbone){
    var EditConceptboard = Backbone.Model.extend({
        urlRoot:baseRestApiUrl + 'MyGubbiApi/conceptboard/update/'
    });
    return EditConceptboard;
});
