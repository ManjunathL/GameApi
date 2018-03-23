define([
    'jquery',
    'backbone'
], function($, Backbone){
    var SaveUserPreference = Backbone.Model.extend({
      urlRoot:baseRestApiUrl + 'MyGubbiApi/questionnaire/save/user/answer/'
    });
    return SaveUserPreference;
});
