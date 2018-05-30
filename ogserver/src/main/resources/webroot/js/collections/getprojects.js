define([
  'jquery',
  'underscore',
  'backbone',
  'models/getproject'
], function($, _, Backbone, GetProject){
    var GetProjects = Backbone.Collection.extend({
        model: GetProject,
        url: baseRestApiUrl + 'MyGubbiApi/userproject/get/list/',
        getProject: function(userId,pageno,itemPerPage,options){
            var urllnk = baseRestApiUrl + 'MyGubbiApi/userproject/get/list/';
            this.url = urllnk + userId + '/' + pageno + '/' + itemPerPage;
            return this.fetch(options);
        },
        getProjectDetails: function (getProjectlist, id) {
            var that = this;
             return _.map(getProjectlist.filter(function(project){
                 return that.getpeojectDetails(project, id);
             }), function (project) {return project });
        },
        getpeojectDetails:function(project, id){
             if (project.id == id)
                 return true;
        },
        initialize: function(models) {
          _.each(models, function (getproject){
            getproject = new GetProject(getproject);
          });
        }
    });
  return GetProjects;
});