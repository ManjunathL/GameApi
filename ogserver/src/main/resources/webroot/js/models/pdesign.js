define([
    'jquery',
    'backbone'
], function($, Backbone){
    var Product = Backbone.Model.extend({
        urlRoot: baseApiUrl + '/gapi/workbench/designmaster/id'
    });
    return Product;
});
