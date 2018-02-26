define([
    'jquery',
    'backbone',
    'underscore'
], function($, Backbone, _) {
    var CreateProducts = Backbone.Collection.extend({
        url: baseApiUrl + '/gapi/workbench/productmaster/create'
    });
    return CreateProducts;
});