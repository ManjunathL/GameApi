define([
    'jquery',
    'backbone',
    'underscore'
], function($, Backbone, _) {
    var SeoProducts = Backbone.Collection.extend({
        url: 'https://192.168.104.88/api/seoProducts'
    });
    return SeoProducts;
});