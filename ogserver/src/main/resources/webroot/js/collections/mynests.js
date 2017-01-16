define([
    'jquery',
    'backbone',
    'underscore'
], function($, Backbone, _) {
    var MyNest = Backbone.Collection.extend({
        url: 'https://gameuat.mygubbi.com:1444' + '/gapi/outboundCrm/getOpportunityDetails'
    });
    return MyNest;
});