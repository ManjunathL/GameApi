define([
    'jquery',
    'backbone',
    'models/autosuggest_product',
    'underscore'
], function($, Backbone, AutoSuggestProduct, _) {
    var AutoSuggestProducts = Backbone.Collection.extend({
        model: AutoSuggestProduct,
        url: restBase + '/api/es/suggest'
    });
    return AutoSuggestProducts;
});
