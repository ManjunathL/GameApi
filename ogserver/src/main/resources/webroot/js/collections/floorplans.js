define([
    'jquery',
    'backbone',
    'underscore'
], function($, Backbone, _) {
    var FloorPlans = Backbone.Collection.extend({
        url: 'https://192.168.104.88/api/partner/floorPlanDetails'
    });
    return FloorPlans;
});