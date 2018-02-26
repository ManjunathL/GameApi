define([
    'jquery',
    'backbone'
], function($, Backbone){
    var FinishMapping = Backbone.Model.extend({
        urlRoot: baseApiUrl + '/gapi/workbench/finishmapping/material',
        defaults: {
           finishMaterial:''
        }
    });
    return FinishMapping;
});
