define([
    'jquery',
    'backbone'
], function($, Backbone) {
    var Proposal = Backbone.Model.extend({
        urlRoot:'https://game.mygubbi.com:1446' + '/gapi/outboundCrm/sendApprovalEmail',
        defaults: {
          emailId: '',
          status:'',
          customerName:'',
          customerPhone:''
        }
    });
    return Proposal;
});