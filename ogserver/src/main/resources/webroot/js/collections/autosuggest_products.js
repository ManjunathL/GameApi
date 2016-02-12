define([
    'jquery',
    'backbone',
    '/js/models/autosuggest_product.js',
    'underscore'
], function($, Backbone, AutoSuggestProduct, _) {
    var AutoSuggestProducts = Backbone.Collection.extend({
        model: AutoSuggestProduct,
        url: restBase + '/api/es/suggest'
    });
    return AutoSuggestProducts;
});
