define([
    'jquery',
    'backbone'
], function($, Backbone){
    var SaveUserMember = Backbone.Model.extend({
      urlRoot:baseRestApiUrl + 'MyGubbiApi/questionnaire/save/member/answer/'
    });
    return SaveUserMember;
});
