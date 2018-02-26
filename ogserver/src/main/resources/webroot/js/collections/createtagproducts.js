define([
    'jquery',
    'backbone',
    'underscore'
], function($, Backbone, _) {
    var CreateTagProducts = Backbone.Collection.extend({
        url: baseApiUrl + '/gapi/workbench/tagproductmaster/create'
    });
    return CreateTagProducts;
});