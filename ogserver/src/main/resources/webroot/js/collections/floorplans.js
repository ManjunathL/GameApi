define([
    'jquery',
    'backbone',
    'underscore'
], function($, Backbone, _) {
    var FloorPlans = Backbone.Collection.extend({
        url: '/api/partner/floorPlanDetails'
    });
    return FloorPlans;
});