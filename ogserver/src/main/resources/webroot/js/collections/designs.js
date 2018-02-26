define([
  'jquery',
  'underscore',
  'backbone',
  'models/design'
], function($, _, Backbone, Design){
    var Designs = Backbone.Collection.extend({
        model: Design,
        url: baseApiUrl + '/gapi/workbench/designmaster/select',
        initialize: function(models) {
          _.each(models, function (design){
            design = new Design(design);
          });
        }
    });
  return Designs;
});
