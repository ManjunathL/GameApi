define([
    'jquery',
    'backbone'
], function($, Backbone){
    var Design = Backbone.Model.extend({
        urlRoot: baseApiUrl + '/gapi/workbench/designmaster/select'
    });
    return Design;
});
