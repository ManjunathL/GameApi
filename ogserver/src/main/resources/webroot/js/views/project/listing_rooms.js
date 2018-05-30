define([
  'jquery',
  'lodash',
  'backbone',
  'bootstrap',
  'pinterest_grid',
  'owlcarousel',
  'text!templates/project/listing_rooms.html',
  'collections/getprojects',
  'collections/conceptboards',
  'views/dashboard/add_conceptboard'
], function($, _, Backbone, Bootstrap, pinterest_grid, owlCarousel, listingRoomPageTemplate, GetProjects, ConceptBoards,AddConceptboard){
  var ListingRoomPage = Backbone.View.extend({
    el: '.page',
    project: null,
    conceptboards: null,
    initialize: function() {
        this.project = new GetProjects();
        this.conceptboards = new ConceptBoards();

        this.listenTo(Backbone);
        _.bindAll(this, 'render','fetchViewProjectRender');
    },
    render: function () {
        var that = this;

                var userprojectId = that.model.projectId;

                if(typeof(userprojectId) != 'undefined'){
                    sessionStorage.defaultMindboardId = userprojectId;
                }else{
                    sessionStorage.defaultMindboardId = "";
                }

                var getProjectPromise = that.viewProjects();
                var getConceptBoardsPromise = that.getConceptBoards(userprojectId);

                Promise.all([getProjectPromise,getConceptBoardsPromise]).then(function() {
                    console.log("@@@@@@@@@@@@@ In side Promise @@@@@@@@@@@@@@@@@@");
                    that.fetchViewProjectRender();

                });
    },
    events: {
            "click .addCBoard1": "viewAddCboard"
        },
        viewAddCboard: function(){
                $('#addcboard-modal').modal('show');
                AddConceptboard.apply();
            },
    viewProjects: function(){
        var that = this;
        var userId = sessionStorage.userId;
        var pageno = 0;
        var itemPerPage = 10;
        return new Promise(function(resolve, reject) {
               that.project.getProject(userId, pageno, itemPerPage, {
                  async: true,
                  crossDomain: true,
                  method: "GET",
                  headers:{
                      "authorization": "Bearer "+ sessionStorage.authtoken
                  },
                  success:function(response) {
                      console.log("I m here   ++++++++++++++ "+userId);
                      console.log(response);


                      resolve();
                  },
                  error:function(response) {
                      console.log(" +++++++++++++++ Errrorr ++++++++++++++++++ ");
                      console.log(response);
                      reject();
                  }
              });
        });
    },
    getConceptBoards: function(userprojectId){
        var that = this;
        var userId = sessionStorage.userId;
        var userProjectId = userprojectId;
        var pageno = 0;
        var itemPerPage = 20;
        var filter = 1;
        return new Promise(function(resolve, reject) {
        that.conceptboards.getConceptBoardList(userId, userProjectId, pageno, itemPerPage,filter, {
           async: true,
           crossDomain: true,
           method: "GET",
           headers:{
               "authorization": "Bearer "+ sessionStorage.authtoken
           },
           success:function(data) {
               console.log(" +++++++++++++++ Concept Boards ++++++++++++++++++ ");
               console.log(data);
               resolve();
           },
           error:function(response) {
               console.log(" +++++++++++++++ Errrorr ++++++++++++++++++ ");
               console.log(response);
               reject();
           }
        });
        });
    },
    fetchViewProjectRender: function(){
        var that = this;
        var project = that.project;
        project = project.toJSON();

        var conceptboards = that.conceptboards;

        var userprojectId = that.model.projectId;

        var projectDtls = that.project.getProjectDetails(project[0].userProjects,userprojectId);

        console.log(" @@@@@@@@@@@@@ Project Details @@@@@@@@@@@@@@@ ");
        console.log(projectDtls);

        $(this.el).html(_.template(listingRoomPageTemplate)({
              "projectDtls": projectDtls,
              'conceptboardsDtls':conceptboards.toJSON()
        }));
    }
  });
  return ListingRoomPage;
});
