define([
    'jquery',
    'backbone'
], function($, Backbone) {
    var Proposal = Backbone.Model.extend({
        urlRoot:'https://gameuat.mygubbi.com:1444' + '/gapi/outboundCrm/sendApprovalEmail',
        defaults: {
          emailId: '',
          status:'',
          customerName:'',
          customerPhone:''
        }
    });
    return Proposal;
});