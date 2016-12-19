define([
 'jquery',
 'underscore',
 'backbone',
 'models/proposal'
], function($, _, Backbone, Proposal){
   var Proposal = Backbone.Collection.extend({
       model: Proposal,
        urlRoot:'https://gameuat.mygubbi.com:1444' + '/gapi/outboundCrm/sendApprovalEmail',
       initialize: function(models) {
         _.each(models, function (proposal){
           proposal = new Proposal(proposal);
         });
       }
   });
 return Proposal;
});