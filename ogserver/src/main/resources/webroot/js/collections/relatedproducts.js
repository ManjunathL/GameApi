define([
    'jquery',
    'backbone',
    'underscore'
], function($, Backbone, _) {
    var RelatedProducts = Backbone.Collection.extend({
        url: restBase + '/api/relatedproducts'
    });
    return RelatedProducts;
});