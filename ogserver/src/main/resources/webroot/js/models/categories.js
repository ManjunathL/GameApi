define([
    'jquery',
    'backbone'
], function($, Backbone) {
    var Categories = Backbone.Model.extend({
        urlRoot: restBase + '/api/categories'
    });
    return Categories;
});
