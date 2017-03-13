define([
    'jquery',
    'backbone',
    'underscore'
], function($, Backbone, _) {
    var Towers = Backbone.Collection.extend({
        url: 'https://192.168.104.88/api/partner/towerDetails'
    });
    return Towers;
});