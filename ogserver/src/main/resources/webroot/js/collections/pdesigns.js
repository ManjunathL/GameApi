define([
  'jquery',
  'underscore',
  'backbone',
  'models/pdesign'
], function($, _, Backbone, Pdesign){
    var Pdesigns = Backbone.Collection.extend({
        model: Pdesign,
        url: baseApiUrl + '/gapi/workbench/designmaster/id',
        initialize: function(models) {
          _.each(models, function (pdesign){
            pdesign = new Product(pdesign);
          });
        }
    });
  return Pdesigns;
});
