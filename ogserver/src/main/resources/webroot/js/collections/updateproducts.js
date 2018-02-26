define([
    'jquery',
    'backbone',
    'underscore'
], function($, Backbone, _) {
    var UpdateProducts = Backbone.Collection.extend({
        url: baseApiUrl + '/gapi/workbench/productmaster/update'
    });
    return UpdateProducts;
});