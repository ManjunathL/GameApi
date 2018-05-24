define([
    'jquery',
    'backbone'
], function($, Backbone){
    var CreateProject = Backbone.Model.extend({
      urlRoot:baseRestApiUrl + '/MyGubbiApi/userproject/create'
    });
    return CreateProject;
});
