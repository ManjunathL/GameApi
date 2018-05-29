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
        getProjectDetails: function (id) {
            return this.find(function(GetProject){ return GetProject.get('id') === id; });
        },
        initialize: function(models) {
          _.each(models, function (getproject){
            getproject = new ConceptBoard(getproject);
          });
        }
    });
  return GetProjects;
});