define([
  'jquery',
  'underscore',
  'backbone',
  'models/add_concepttag'
], function($, _, Backbone, AddConceptTag){
    var AddConcepttags = Backbone.Collection.extend({
        model: AddConceptTag,
        url: baseRestApiUrl + 'MyGubbiApi/conceptboardConcept/add/tags/',
        getaddConceptTag: function(userId,conceptboardConceptId, options) {
            var urllnk = baseRestApiUrl + 'MyGubbiApi/conceptboardConcept/add/tags/';
            this.url = urllnk + userId + '/' + conceptboardConceptId;
            return this.fetch(options);
        },
        initialize: function(models) {
          _.each(models, function (add_concepttag){
            add_concepttag = new ConceptTag(add_concepttag);
          });
        }
    });
  return AddConcepttags;
});