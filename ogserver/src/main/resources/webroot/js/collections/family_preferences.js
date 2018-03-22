define([
  'jquery',
  'underscore',
  'backbone',
  'models/family_preference'
], function($, _, Backbone, FamilyPreference){
    var FamilyPreferences = Backbone.Collection.extend({
        model: FamilyPreference,
        url: baseRestApiUrl + 'MyGubbiApi/familypersona/get/familymembers/list/',
        getFamilyPreferences: function(userId, options) {
            var urllnk = baseRestApiUrl + 'MyGubbiApi/familypersona/get/familymembers/list/';
            this.url = urllnk +userId;
            return this.fetch(options);
        },
        initialize: function(models) {
          _.each(models, function (family_preference){
            family_preference = new FamilyPreference(family_preference);
          });
        }
    });
  return FamilyPreferences;
});