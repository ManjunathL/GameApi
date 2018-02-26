define([
  'jquery',
  'underscore',
  'backbone',
  'models/concepttag'
], function($, _, Backbone, ConceptTag){
    var ConceptTags = Backbone.Collection.extend({
        model: ConceptTag,
        url: baseRestApiUrl + 'MyGubbiApi/conceptboard/get/conceptBoard/tagList/',
        getConceptTagList: function(conceptboardId, options) {
            var urllnk = baseRestApiUrl + 'MyGubbiApi/conceptboard/get/conceptBoard/tagList/';
            this.url = urllnk + conceptboardId;
            return this.fetch(options);
        },
        initialize: function(models) {
          _.each(models, function (concepttag){
            concepttag = new ConceptTag(concepttag);
          });
        }
    });
  return ConceptTags;
});