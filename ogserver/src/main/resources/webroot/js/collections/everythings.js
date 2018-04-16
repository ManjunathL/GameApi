define([
  'jquery',
  'underscore',
  'backbone',
  'models/everything'
], function($, _, Backbone, Everything){
    var Everythings = Backbone.Collection.extend({
        model: Everything,
        url: baseRestApiUrl + 'MyGubbiApi/conceptMaster/get/concept-list/',
         getEverythingList: function(userId, userMindboardId, pageno, itemPerPage,options) {
                var urllnk = baseRestApiUrl + 'MyGubbiApi/conceptMaster/get/concept-list/';
                this.url = urllnk + userId + '/' + userMindboardId + '/' + pageno + '/' + itemPerPage ;
                return this.fetch(options);
        },
        initialize: function(models) {
          _.each(models, function (everything){
            everything = new Everything(everything);
          });
        }
    });
  return Everythings;
});
