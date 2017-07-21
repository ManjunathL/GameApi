define([
  'jquery',
  'underscore',
  'backbone',
  'models/diy'
], function($, _, Backbone, Diy){
    var Diy = Backbone.Collection.extend({
        model: Diy,
        url: restBase + '/api/diy/allDiy',
        initialize: function(models) {
          _.each(models, function (diy){
            diy = new Diy(diy);
          });
        }
    });
  return Diy;
});