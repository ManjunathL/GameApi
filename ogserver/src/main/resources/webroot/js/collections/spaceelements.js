define([
  'jquery',
  'underscore',
  'backbone',
  'models/spaceelement'
], function($, _, Backbone, SpaceElement){
    var SpaceElements = Backbone.Collection.extend({
        model: SpaceElement,
        url: baseRestApiUrl + 'MyGubbiApi/spaceelement/get',
         getSpaceElement: function(spaceTypeCode, options) {
                    var urllnk = baseRestApiUrl + 'MyGubbiApi/spaceelement/get/';
                    this.url = urllnk + spaceTypeCode;
                    return this.fetch(options);
                },
        initialize: function(models) {
          _.each(models, function (spaceelement){
            spaceelement = new SpaceElement(spaceelement);
          });
        }
    });
  return SpaceElements;
});
