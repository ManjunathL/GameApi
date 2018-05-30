define([
  'jquery',
  'underscore',
  'backbone',
  'models/deleteproject'
], function($, _, Backbone, DeleteProject){
    var DeleteProjects = Backbone.Collection.extend({
        model: DeleteProject,
        url: baseRestApiUrl + 'MyGubbiApi/userproject/delete/',
        removeProject: function(userId,projectId, options){
            var urllnk = baseRestApiUrl + 'MyGubbiApi/userproject/delete/';
            this.url = urllnk + userId + '/' + projectId ;
            return this.fetch(options);
        },
        initialize: function(models) {
          _.each(models, function (deleteproject){
            deleteproject = new DeleteProject(deleteproject);
          });
        }
    });
  return DeleteProjects;
});