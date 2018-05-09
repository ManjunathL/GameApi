define([
  'jquery',
  'underscore',
  'backbone',
  'models/user_member'
], function($, _, Backbone, UserMember){
    var UserMembers = Backbone.Collection.extend({
        model: UserMember,
        url: baseRestApiUrl + 'MyGubbiApi/questionnaire/get/members/ques/list/',
        getUserMembers: function(userId, memberId, options) {
            var urllnk = baseRestApiUrl + 'MyGubbiApi/questionnaire/get/members/ques/list/';
            this.url = urllnk + userId +"/" +memberId;
            return this.fetch(options);
        },
        initialize: function(models) {
          _.each(models, function (user_member){
            user_member = new UserMember(user_member);
          });
        }
    });
  return UserMembers;
});