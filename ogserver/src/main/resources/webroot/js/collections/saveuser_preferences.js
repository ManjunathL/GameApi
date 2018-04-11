define([
  'jquery',
  'underscore',
  'backbone',
  'models/saveuser_preference'
], function($, _, Backbone, SaveUserPreference){
    var SaveUserPreferences = Backbone.Collection.extend({
        model: SaveUserPreference,
        url: baseRestApiUrl + 'MyGubbiApi/questionnaire/save/user/answer/',
        saveUserPreferences: function(category, userId, options) {
            var urllnk = baseRestApiUrl + 'MyGubbiApi/questionnaire/save/user/answer/';
            this.url = urllnk + category +"/" +userId;
            return this.fetch(options);
        },
        initialize: function(models) {
          _.each(models, function (saveuser_preference){
            saveuser_preference = new SaveUserPreference(saveuser_preference);
          });
        }
    });
  return SaveUserPreferences;
});