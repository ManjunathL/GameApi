define([
    'jquery',
    'backbone',
    'underscore'
], function($, Backbone, _) {
    var CreateDesigns = Backbone.Collection.extend({
        url: baseApiUrl + '/gapi/workbench/designmaster/create'
    });
    return CreateDesigns;
});