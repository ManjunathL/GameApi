define([
  'jquery',
  'underscore',
  'backbone',
  'models/add_blankboard'
], function($, _, Backbone, AddBlankboard){
    var AddBlankboards = Backbone.Collection.extend({
        model: AddBlankboard,
        url: baseRestApiUrl + 'MyGubbiApi/conceptboard/create/blank/',
        getaddBlankBoardTemplate: function(userId,userMindboardId, options) {
            var urllnk = baseRestApiUrl + 'MyGubbiApi/conceptboard/create/blank/';
            this.url = urllnk + userId + '/' + userMindboardId;
            return this.fetch(options);
        },
        initialize: function(models) {
          _.each(models, function (add_blankboard){
            add_blankboard = new ConceptBoard(add_blankboard);
          });
        }
    });
  return AddBlankboards;
});