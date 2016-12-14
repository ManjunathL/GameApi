define([
  'jquery',
  'underscore',
  'backbone',
  'models/latestblog'
], function($, _, Backbone, Latestblog){
    var Latestblog = Backbone.Collection.extend({
        model: Latestblog,
        url: restBase + '/api/blogs/latestTag',
        initialize: function(models) {
          _.each(models, function (latestblog){
            latestblog = new Latestblog(latestblog);
          });
        }
    });
  return Latestblog;
});