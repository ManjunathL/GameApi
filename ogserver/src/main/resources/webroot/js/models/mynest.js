/**
 * Created by Mehbub on 21/11/16.
 */

define([
    'jquery',
    'backbone'
], function($, Backbone) {
    var MyNest = Backbone.Model.extend({
        urlRoot:'https://game.mygubbi.com:1446' + '/gapi/outboundCrm/getOpportunityDetails',
        defaults: {
          emailId: ''
        }
    });
    return MyNest;
});