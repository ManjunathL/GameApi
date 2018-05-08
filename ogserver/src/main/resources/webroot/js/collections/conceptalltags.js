define([
  'jquery',
  'underscore',
  'backbone',
  'models/concepallttag'
], function($, _, Backbone, ConceptAllTag){
    var ConceptAllTags = Backbone.Collection.extend({
        model: ConceptAllTag,
        url: baseRestApiUrl + '/MyGubbiApi//conceptboard/get/all/conceptBoard/tagList/',
        getConceptAllTagList: function(mindboardId, options) {
            var urllnk = baseRestApiUrl + '/MyGubbiApi//conceptboard/get/all/conceptBoard/tagList/';
            this.url = urllnk + mindboardId;
            return this.fetch(options);
        },
        initialize: function(models) {
          _.each(models, function (concepallttag){
            concepallttag = new ConceptTag(concepallttag);
          });
        }
    });
  return ConceptAllTags;
});