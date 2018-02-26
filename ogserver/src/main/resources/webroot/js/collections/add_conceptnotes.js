define([
  'jquery',
  'underscore',
  'backbone',
  'models/add_conceptnote'
], function($, _, Backbone, AddConceptnote){
    var AddConceptnotes = Backbone.Collection.extend({
        model: AddConceptnote,
        url: baseRestApiUrl + 'MyGubbiApi/conceptboardConcept/add/notes/',
        getaddConceptNote: function(userId,conceptboardConceptId, options) {
            var urllnk = baseRestApiUrl + 'MyGubbiApi/conceptboardConcept/add/notes/';
            console.log("@@@@@@@@@@@@@@@@ My Note @@@@@@@@@@@@@@@@@@");
            console.log(urllnk);
            this.url = urllnk + userId + '/' + conceptboardConceptId;
            return this.fetch(options);
        },
        initialize: function(models) {
          _.each(models, function (add_conceptnote){
            add_conceptnote = new Conceptnote(add_conceptnote);
          });
        }
    });
  return AddConceptnotes;
});