define([
    'jquery',
    'backbone'
], function($, Backbone){
    var UserPreference = Backbone.Model.extend({
      urlRoot:baseRestApiUrl + 'MyGubbiApi/questionnaire/get/ques/list/'
    });
    return UserPreference;
});
