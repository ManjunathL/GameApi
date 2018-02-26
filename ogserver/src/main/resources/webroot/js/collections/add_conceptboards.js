define([
  'jquery',
  'underscore',
  'backbone',
  'models/add_conceptboard'
], function($, _, Backbone, AddConceptboard){
    var AddConceptboards = Backbone.Collection.extend({
        model: AddConceptboard,
        url: baseRestApiUrl + 'MyGubbiApi/conceptboard/add/template/',
        getaddConceptBoardTemplate: function(userId,userMindboardId, options) {
            var urllnk = baseRestApiUrl + 'MyGubbiApi/conceptboard/add/template/';
            this.url = urllnk + userId + '/' + userMindboardId;
            return this.fetch(options);
        },
        initialize: function(models) {
          _.each(models, function (add_conceptboard){
            add_conceptboard = new ConceptBoard(add_conceptboard);
          });
        }
    });
  return AddConceptboards;
});