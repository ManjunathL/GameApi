define([
  'jquery',
  'underscore',
  'backbone',
  'models/edit_conceptboard'
], function($, _, Backbone, EditConceptboard){
    var EditConceptboards = Backbone.Collection.extend({
        model: EditConceptboard,
        url: baseRestApiUrl + 'MyGubbiApi/conceptboard/update/',
        geteditConceptBoard: function(options) {
            var urllnk = baseRestApiUrl + 'MyGubbiApi/conceptboard/update/';
            this.url = urllnk;
            return this.fetch(options);
        },
        initialize: function(models) {
          _.each(models, function (edit_conceptboard){
            edit_conceptboard = new ConceptBoard(edit_conceptboard);
          });
        }
    });
  return EditConceptboards;
});