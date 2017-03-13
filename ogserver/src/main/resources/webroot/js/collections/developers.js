define([
    'jquery',
    'backbone',
    'underscore'
], function($, Backbone, _) {
    var Developer = Backbone.Collection.extend({
        url: 'https://192.168.104.88/api/partner/developerDetails'
    });
    return Developer;
});