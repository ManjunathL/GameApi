define([
  'jquery',
  'underscore',
  'backbone',
  'models/recent_project'
], function($, _, Backbone, RecentProject){
    var RecentProjects = Backbone.Collection.extend({
        model: RecentProject,
        url: restBase + '/api/recentProject',
        initialize: function(models) {
          _.each(models, function (recent_project){
            recent_project = new RecentProject(recent_project);
          });
        }
    });
  return RecentProjects;
});