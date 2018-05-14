define([
  'jquery',
  'underscore',
  'backbone',
  'models/editfamilymember'
], function($, _, Backbone, EditFamilyMember){
    var EditFamilyMembers = Backbone.Collection.extend({
        model: EditFamilyMember,
        url: baseRestApiUrl + 'MyGubbiApi/questionnaire/get/members/ques/list/',
        editFamilyMembers: function(userId,memberId, options) {
            var urllnk = baseRestApiUrl + 'MyGubbiApi/questionnaire/get/members/ques/list/';
            this.url = urllnk +userId +'/'+memberId;
            return this.fetch(options);
        },
        initialize: function(models) {
          _.each(models, function (editfamilymembers){
            editfamilymembers = new EditFamilyMember(editfamilymembers);
          });
        }
    });
  return EditFamilyMembers;
});