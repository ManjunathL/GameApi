define([
    'jquery',
    'backbone',
    'underscore'
], function($, Backbone, _) {
    var Developer = Backbone.Collection.extend({
        url: '/api/partner/developerDetails'
    });
    return Developer;
});