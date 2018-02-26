define([
    'jquery',
    'backbone'
], function($, Backbone){
    var MaterialFinishMapping = Backbone.Model.extend({
        urlRoot: baseApiUrl + '/gapi/workbench/materialfinishmapping/id',
        defaults: {
           element:'',
           materialName:''
        }
    });
    return MaterialFinishMapping;
});
