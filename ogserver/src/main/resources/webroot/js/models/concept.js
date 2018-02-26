define([
    'jquery',
    'backbone'
], function($, Backbone){
    var Concept = Backbone.Model.extend({
        urlRoot: baseApiUrl + '/gapi/workbench/conceptmaster/select'
    });
    return Concept;
});
