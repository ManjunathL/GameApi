define([
  'jquery',
  'underscore',
  'backbone',
  'models/remove_conceptFromCboard'
], function($, _, Backbone, RemoveConceptFromCboard){
    var RemoveConceptFromCboards = Backbone.Collection.extend({
        model: RemoveConceptFromCboard,
        url: baseRestApiUrl + 'MyGubbiApi/conceptboardConcept/delete/',
        getremoveConceptFromCBoard: function(conceptId, options) {
            var urllnk = baseRestApiUrl + 'MyGubbiApi/conceptboardConcept/delete/';
            this.url = urllnk + conceptId;
            return this.fetch(options);
        },
        initialize: function(models) {
          _.each(models, function (remove_conceptFromCboard){
            remove_conceptFromCboard = new AddConceptToCboard(remove_conceptFromCboard);
          });
        }
    });
  return RemoveConceptFromCboards;
});