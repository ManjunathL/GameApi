define([
    'jquery',
    'backbone',
    'underscore'
], function($, Backbone, _) {
    var MyNest = Backbone.Collection.extend({
        url: gamecrmbaseUrl + '/gapi/outboundCrm/getOpportunityDetails'
    });
    return MyNest;
});