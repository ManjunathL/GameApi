define([
    'jquery',
    'backbone',
    'underscore'
], function($, Backbone, _) {
    var SeoProducts = Backbone.Collection.extend({
        url: restBase + '/api/seoProducts'
    });
    return SeoProducts;
});