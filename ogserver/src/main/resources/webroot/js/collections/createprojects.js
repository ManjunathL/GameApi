define([
  'jquery',
  'underscore',
  'backbone',
  'models/createproject'
], function($, _, Backbone, CreateProject){
    var CreateProjects = Backbone.Collection.extend({
        model: CreateProject,
        url: baseRestApiUrl + 'MyGubbiApi/userproject/create/',
        createNewProject: function(userId,options){
            var urllnk = baseRestApiUrl + 'MyGubbiApi/userproject/create/';
            this.url = urllnk + userId;
            return this.fetch(options);
        },
        initialize: function(models) {
          _.each(models, function (createproject){
            createproject = new ConceptBoard(createproject);
          });
        }
    });
  return CreateProjects;
});