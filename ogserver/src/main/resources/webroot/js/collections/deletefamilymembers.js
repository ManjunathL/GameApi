define([
  'jquery',
  'underscore',
  'backbone',
  'models/deletefamilymember'
], function($, _, Backbone, DeleteFamilyMember){
    var DeleteFamilyMembers = Backbone.Collection.extend({
        model: DeleteFamilyMember,
        url: baseRestApiUrl + 'MyGubbiApi/familypersona/delete/member/',
        deleteMembers: function(memberId, options) {
            var urllnk = baseRestApiUrl + 'MyGubbiApi/familypersona/delete/member/';
            this.url = urllnk +memberId;
            return this.fetch(options);
        },
        initialize: function(models) {
          _.each(models, function (deletefamilymembers){
            deletefamilymembers = new DeleteFamilyMember(deletefamilymembers);
          });
        }
    });
  return DeleteFamilyMembers;
});