define([
    'jquery',
    'backbone',
    'underscore'
], function($, Backbone, _) {
    var UpdateDesigns = Backbone.Collection.extend({
        url: baseApiUrl + '/gapi/workbench/designmaster/update'
    });
    return UpdateDesigns;
});