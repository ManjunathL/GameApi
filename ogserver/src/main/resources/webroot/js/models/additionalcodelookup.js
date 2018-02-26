define([
    'jquery',
    'backbone'
], function($, Backbone){
    var AdditionalCodeLookup = Backbone.Model.extend({
        urlRoot: baseApiUrl + '/gapi/workbench/additionalcodelookup',
        defaults: {
           lookupType:'',
           additionalType:''
        }
    });
    return AdditionalCodeLookup;
});
