define([
  'jquery',
  'underscore',
  'backbone',
  'models/authsetting'
], function($, _, Backbone, Authsetting){
    var Authsettings = Backbone.Collection.extend({
        model: Authsetting,
        url: 'http://45.112.138.146:8080/MyGubbiAuth/oauth/token',
        initialize: function(models) {
          _.each(models, function (authsetting){
            authsetting = new Authsetting(authsetting);
          });
        }
    });
  return Authsettings;
});
