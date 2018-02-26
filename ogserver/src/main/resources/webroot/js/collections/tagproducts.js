define([
  'jquery',
  'underscore',
  'backbone',
  'models/tagproduct'
], function($, _, Backbone, Tagproduct){
    var Tagproducts = Backbone.Collection.extend({
        model: Tagproduct,
        url: baseApiUrl + '/gapi/workbench/tagproductmaster/select',
        initialize: function(models) {
          _.each(models, function (tagproduct){
            tagproduct = new Tagproduct(tagproduct);
          });
        }
    });
  return Tagproducts;
});
