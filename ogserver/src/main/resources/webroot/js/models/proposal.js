define([
    'jquery',
    'backbone'
], function($, Backbone) {
    var Proposal = Backbone.Model.extend({
        urlRoot: gamecrmbaseUrl + '/gapi/outboundCrm/sendApprovalEmail',
        defaults: {
          emailId: '',
          status:'',
          customerName:'',
          customerPhone:''
        }
    });
    return Proposal;
});