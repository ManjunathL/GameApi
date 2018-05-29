define([
    'jquery',
    'backbone'
], function($, Backbone){
    var GetProject = Backbone.Model.extend({
      urlRoot:baseRestApiUrl + '/MyGubbiApi/userproject/get/list/'
    });
    return GetProject;
});
