define([
    'jquery',
    'backbone'
], function($, Backbone){
    var UserMember = Backbone.Model.extend({
      urlRoot:baseRestApiUrl + 'MyGubbiApi/questionnaire/get/members/ques/list/'
    });
    return UserMember;
});
