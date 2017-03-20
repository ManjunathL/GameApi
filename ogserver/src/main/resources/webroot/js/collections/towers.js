define([
    'jquery',
    'backbone',
    'underscore'
], function($, Backbone, _) {
    var Towers = Backbone.Collection.extend({
        url: '/api/partner/towerDetails'
    });
    return Towers;
});