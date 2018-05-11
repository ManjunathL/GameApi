define([
    'jquery',
    'backbone'
], function($, Backbone){
    var EditFamilyMember = Backbone.Model.extend({
      urlRoot:baseRestApiUrl + 'MyGubbiApi//questionnaire/get/members/ques/list/'
    });
    return EditFamilyMember;
});
