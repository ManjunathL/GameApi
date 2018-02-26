define([
    'jquery',
    'backbone'
], function($, Backbone){
    var Tagproduct = Backbone.Model.extend({
        urlRoot: baseApiUrl + '/gapi/workbench/tagproductmaster/select',
        defaults: {
           id: ''
        }
    });
    return Tagproduct;
});
