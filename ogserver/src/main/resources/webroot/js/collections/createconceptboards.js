define([
  'jquery',
  'underscore',
  'backbone',
  'models/createconceptboard'
], function($, _, Backbone, CreateConceptBoard){
    var CreateConceptBoards = Backbone.Collection.extend({
        model: CreateConceptBoard,
        url: baseRestApiUrl + 'MyGubbiApi/conceptboard/create/default/conceptboard/',
        getUserIdAuth: function(userId, options) {
             var urllnk = baseRestApiUrl + 'MyGubbiApi/conceptboard/create/default/conceptboard/';
             this.url = urllnk + userId;
            return this.fetch(options);
        },
        initialize: function(models) {
          _.each(models, function (createconceptboard){
            createconceptboard = new CreateConceptBoard(createconceptboard);
          });
        }
    });
  return CreateConceptBoards;
});