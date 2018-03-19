define([
  'jquery',
  'underscore',
  'backbone',
  'models/spacetemplate'
], function($, _, Backbone, SpaceTemplate){
    var SpaceTemplates = Backbone.Collection.extend({
        model: SpaceTemplate,
        url: baseRestApiUrl + 'MyGubbiApi/spacetemplate/get/',
        getSpaceTemplateList: function(spacetypeCode, options) {
            var urllnk = baseRestApiUrl + 'MyGubbiApi/spacetemplate/get/';
            this.url = urllnk + '?spaceTypeCode=' + spacetypeCode;
            return this.fetch(options);
        },
        initialize: function(models) {
          _.each(models, function (spacetemplate){
            spacetemplate = new SpaceTemplate(spacetemplate);
          });
        }
    });
  return SpaceTemplates;
});
