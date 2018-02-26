define([
    'jquery',
    'backbone'
], function($, Backbone){
    var CodeLookup = Backbone.Model.extend({
        urlRoot: baseApiUrl + '/gapi/workbench/codelookup',
        defaults: {
           lookupType: ''
        }
    });
    return CodeLookup;
});
