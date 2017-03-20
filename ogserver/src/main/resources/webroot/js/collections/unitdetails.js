define([
    'jquery',
    'backbone',
    'underscore'
], function($, Backbone, _) {
    var FloorPlans = Backbone.Collection.extend({
        url: '/api/partner/unitDetails'
    });
    return FloorPlans;
});