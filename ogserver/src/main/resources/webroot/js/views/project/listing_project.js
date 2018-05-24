define([
  'jquery',
  'lodash',
  'backbone',
  'bootstrap',
  'pinterest_grid',
  'owlcarousel',
  'text!templates/project/listing_project.html',
  'collections/getprojects',
  'collections/createprojects'
], function($, _, Backbone, Bootstrap, pinterest_grid, owlCarousel, listingProjectPageTemplate, GetProjects, CreateProjects){
  var ListingProjectPage = Backbone.View.extend({
    el: '.page',
    getprojects: null,
    initialize: function() {
        this.getprojects = new GetProjects();
        this.createprojects = new CreateProjects();
        this.listenTo(Backbone);
        _.bindAll(this, 'render');
    },
    render: function () {
        var that = this;
      //  $(this.el).html(_.template(listingProjectPageTemplate));

        var getProjectPromise = that.viewProjects();
        Promise.all([getProjectPromise]).then(function() {
            console.log("@@@@@@@@@@@@@ In side Promise @@@@@@@@@@@@@@@@@@");

        });
    },
    events: {
            "click #createProject": "createNewProject",
            "click #save_conceptfileupload": "saveConceptFileupload"
    },
    createNewProject: function(){
        $('#createProject-modal').modal('show');
    },
    viewProjects: function(){
            var that = this;
            var userId = sessionStorage.userId;
            var pageno = 0;
            var itemPerPage = 10;
            return new Promise(function(resolve, reject) {
                   that.getprojects.getProject(userId, pageno, itemPerPage, {
                      async: true,
                      crossDomain: true,
                      method: "GET",
                      headers:{
                          "authorization": "Bearer "+ sessionStorage.authtoken
                      },
                      success:function(response) {
                          console.log("I m here   ++++++++++++++ "+userId);
                          console.log(response);
                          that.fetchViewProjectRender();

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
    saveConceptFileupload: function(){
                var that = this;
                var userId = sessionStorage.userId;
                var pageno = 0;
                var itemPerPage = 10;
                var builder = $('#inputbuilder').val();
                var plan = $('#inputplan').val();
                var projecttower= $('#inputprojecttower').val();
                var ProjectAddress= $('#inputProjectAddress').val();
                var city = $('#inputProjectCity').val();
                var state = $('#inputProjectState').val();
                var pincode = $('#inputpincode').val();
                var projectName= $('#projectName').val();

                var body={
                    "builder": $('#inputbuilder').val(),
                    "city": $('#inputcity').val(),
                    "description": "",
                    "id": 0,
                    "planCode": $('#inputplan').val(),
                    "projectTower": $('#inputprojecttower').val(),
                    "projectAddress": $('#inputProjectAddress').val(),
                    "city": $('#inputProjectCity').val(),
                    "state": $('#inputProjectState').val(),
                    "pin": $('#inputpincode').val(),
                    "projectId": 0,
                    "projectName": $('#projectName').val(),
                    "refrencedUserId": 0,
                    "shareStatus": 0,
                    "state": "string",
                    "type": 0,
                    "userUploadedPlanId": 0
                }
                      that.createprojects.createNewProject(userId,{
                             async: true,
                             crossDomain: true,
                             method: "POST",
                             headers:{
                                 "authorization": "Bearer "+ sessionStorage.authtoken,
                                 "Content-Type": "application/json"
                             },
                             data: JSON.stringify(body),
                             success:function(response) {
                                 console.log("Successfully create new project- ");
                                 console.log(body);

                                 $("#snackbar").html("Successfully create new project...");
                                var x = document.getElementById("snackbar")
                                x.className = "show";
                                setTimeout(function(){ x.className = x.className.replace("show", ""); }, 3000);
                                  that.render();
                                //$(e.currentTarget).closest('article').remove();
                                // return;
                             },
                             error:function(response) {
                                 console.log(" +++++++++++++++create new project- Errrorr ++++++++++++++++++ ");
                                 console.log(response);
                             }
                      });

        },

    fetchViewProjectRender: function(){
            var that = this;
            var getprojects = this.getprojects;

            $(this.el).html(_.template(listingProjectPageTemplate)({
               "getprojects": getprojects.toJSON(),
           }));
        },

  });
    return ListingProjectPage;
});
