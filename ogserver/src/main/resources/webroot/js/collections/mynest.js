define([
 'jquery',
 'underscore',
 'backbone',
 'models/mynest'
], function($, _, Backbone, MyNest){
   var MyNest = Backbone.Collection.extend({
       model: MyNest,
        urlRoot:'https://gameuat.mygubbi.com:1444' + '/gapi/outboundCrm//getOpportunityDetails',
       initialize: function(models) {
         _.each(models, function (mynest){
           mynest = new MyNest(mynest);
               console.log("collection");

         });
          console.log("=======mynest 445555555555555555555555555555555555555=======");
                     console.log(mynest);
       }
   });
 return MyNest;
});