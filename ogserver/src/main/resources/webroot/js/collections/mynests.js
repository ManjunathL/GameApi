define([
    'jquery',
    'backbone',
    'underscore'
], function($, Backbone, _) {
    var MyNest = Backbone.Collection.extend({
        url: 'https://game.mygubbi.com:1446' + '/gapi/outboundCrm/getOpportunityDetails'
    });
    return MyNest;
});