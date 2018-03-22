define([
  'jquery',
  'underscore',
  'backbone',
  'models/user_preference'
], function($, _, Backbone, UserPreference){
    var UserPreferences = Backbone.Collection.extend({
        model: UserPreference,
        url: baseRestApiUrl + 'MyGubbiApi/questionnaire/get/ques/list/',
        getUserPreferences: function(category, userId, options) {
            var urllnk = baseRestApiUrl + 'MyGubbiApi/questionnaire/get/ques/list/';
            this.url = urllnk + category +"/" +userId;
            return this.fetch(options);
        },
        initialize: function(models) {
          _.each(models, function (user_preference){
            user_preference = new UserPreference(user_preference);
          });
        }
    });
  return UserPreferences;
});