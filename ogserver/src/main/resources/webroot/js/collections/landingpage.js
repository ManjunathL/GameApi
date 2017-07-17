define([
  'jquery',
  'underscore',
  'backbone',
  'models/landing'
], function($, _, Backbone, Landing){
    var Landing = Backbone.Collection.extend({
        model: Landing,
        url: restBase + '/api/landingPage',
        initialize: function(models) {
          _.each(models, function (landing){
            landing = new Landing(landing);
          });
        }
    });
  return Landing;
});