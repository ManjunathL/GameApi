/**
 * Created by Mehbub on 21/11/16.
 */

define([
    'jquery',
    'backbone'
], function($, Backbone) {
    var MyNest = Backbone.Model.extend({
        urlRoot:gamecrmbaseUrl + '/gapi/outboundCrm/getOpportunityDetails',
        defaults: {
          emailId: ''
        }
    });
    return MyNest;
});