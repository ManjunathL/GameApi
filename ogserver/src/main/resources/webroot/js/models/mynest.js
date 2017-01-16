/**
 * Created by Mehbub on 21/11/16.
 */

define([
    'jquery',
    'backbone'
], function($, Backbone) {
    var MyNest = Backbone.Model.extend({
        urlRoot:'https://gameuat.mygubbi.com:1444' + '/gapi/outboundCrm/getOpportunityDetails',
        defaults: {
          emailId: ''
        }
    });
    return MyNest;
});