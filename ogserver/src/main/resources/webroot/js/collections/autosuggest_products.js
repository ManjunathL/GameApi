define([
    'jquery',
    'backbone',
    'models/autosuggest_product',
    'underscore'
], function($, Backbone, AutoSuggestProduct, _) {
    var AutoSuggestProducts = Backbone.Collection.extend({
        model: AutoSuggestProduct,
        url: baseRestApiUrl + 'MyGubbiApi/search/get/autocompletion-tags/'
    });
    return AutoSuggestProducts;
});
