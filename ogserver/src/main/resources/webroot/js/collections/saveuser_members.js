define([
  'jquery',
  'underscore',
  'backbone',
  'models/saveuser_member'
], function($, _, Backbone, SaveUserMember){
    var SaveUserMembers = Backbone.Collection.extend({
        model: SaveUserMember,
        url: baseRestApiUrl + 'MyGubbiApi/questionnaire/save/member/answer/',
        saveUserMembers: function(userId, options) {
            var urllnk = baseRestApiUrl + 'MyGubbiApi/questionnaire/save/member/answer/';
            this.url = urllnk + userId;
            return this.fetch(options);
        },
        initialize: function(models) {
          _.each(models, function (saveuser_member){
            saveuser_member = new SaveUserMember(saveuser_member);
          });
        }
    });
  return SaveUserMembers;
});