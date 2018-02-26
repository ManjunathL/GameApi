define([
    'jquery',
    'backbone'
], function($, Backbone){
    var MaterialComponent = Backbone.Model.extend({
        urlRoot: baseApiUrl + '/gapi/workbench/materialcomponent/select',
        defaults: {
           lookupType:'',
           additionalType:''
        }
    });
    return MaterialComponent;
});
