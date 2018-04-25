define([
  'jquery',
  'underscore',
  'backbone',
  'models/save_shortlist_design'
], function($, _, Backbone, SaveShortListDesign){
    var SaveShortListDesigns = Backbone.Collection.extend({
        model: SaveShortListDesign,
        url: baseRestApiUrl + 'MyGubbiApi/designs/save/userSelected/',
        saveShortListDesigns: function(conceptboardId, designCode, options) {
            var urllnk = baseRestApiUrl + 'MyGubbiApi/designs/save/userSelected/';
            this.url = urllnk + conceptboardId +"/" +designCode;
            return this.fetch(options);
        },
        initialize: function(models) {
          _.each(models, function (save_shortlist_design){
            save_shortlist_design = new SaveShortListDesign(save_shortlist_design);
          });
        }
    });
  return SaveShortListDesigns;
});