define([
           'jquery',
           'underscore',
           'backbone',
           'models/add_conceptboard'
         ], function($, _, Backbone, wishListConceptList){
             var ShortListDesigns = Backbone.Collection.extend({
                 model: shortListDesign,
                 url: baseRestApiUrl + 'MyGubbiApi/conceptboard/add/template/',
                 getShortListDesign: function(userId,userMindboardId, options) {
                     var urllnk = baseRestApiUrl + 'MyGubbiApi/conceptboard/add/template/';
                     this.url = urllnk + userId + '/' + userMindboardId;
                     return this.fetch(options);
                 },
                 initialize: function(models) {
                   _.each(models, function (short_ListDesign){
                     add_conceptboard = new ShortListDesign(short_ListDesign);
                   });
                 }
             });
           return ShortListDesigns;
         });