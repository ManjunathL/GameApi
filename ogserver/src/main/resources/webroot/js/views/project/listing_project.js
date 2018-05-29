define([
  'jquery',
  'lodash',
  'backbone',
  'bootstrap',
  'pinterest_grid',
  'owlcarousel',
  'text!templates/project/listing_project.html',
  'collections/getprojects',
  'collections/createprojects',
  'collections/spacetypelists'
], function($, _, Backbone, Bootstrap, pinterest_grid, owlCarousel, listingProjectPageTemplate, GetProjects, CreateProjects, SpaceTypeLists){
  var ListingProjectPage = Backbone.View.extend({
    el: '.page',
    getprojects: null,
    spacetypelists: null,
    initialize: function() {
        this.getprojects = new GetProjects();
        this.createprojects = new CreateProjects();
        this.spacetypelists = new SpaceTypeLists();
        this.listenTo(Backbone);
        _.bindAll(this, 'render');
    },
    render: function () {
        var that = this;
      //  $(this.el).html(_.template(listingProjectPageTemplate));

        var getProjectPromise = that.viewProjects();
        var getSpaceTypeListPromise = that.getSpaceTypeList();
        Promise.all([getProjectPromise,getSpaceTypeListPromise]).then(function() {
            console.log("@@@@@@@@@@@@@ In side Promise @@@@@@@@@@@@@@@@@@");
            that.fetchViewProjectRender();

        });
    },
    events: {
            "click #createProject": "createNewProject",
            "click #save_conceptfileupload": "saveConceptFileupload",
           // "click #addSpaceElement": "getSpaceTypeList"
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

                $(".err").html('');

                var userId = sessionStorage.userId;
                var pageno = 0;
                var itemPerPage = 10;
                var projectName = $('#projectName').val();
                    $('#projectName').focus();
                    if(projectName.length == 0){
                        $("#projectNama-error").html("Please Enter the Project Name ");
                     return false;
                    }
                var builder = $('#inputbuilder').val();
                 $('#inputbuilder').focus();
                    if(builder.length == 0){
                        $("#inputbuilder-error").html("Please Enter the Builder Name ");
                     return false;
                    }
                var projecttower= $('#inputprojecttower').val();
                $('#inputprojecttower').focus();
                    if(projecttower.length == 0){
                        $("#inputprojecttower-error").html("Please Enter the Project Tower ");
                     return false;
                }
                var ProjectAddress= $('#inputProjectAddress').val();
                $('#inputProjectAddress').focus();
                    if(ProjectAddress.length == 0){
                        $("#inputProjectAddress-error").html("Please Enter the Project Address ");
                     return false;
                }
                var city = $('#inputProjectCity').val();
                $('#inputProjectCity').focus();
                    if(city.length == 0){
                        $("#inputProjectCity-error").html("Please Enter the City ");
                     return false;
                }
                var state = $('#inputProjectState').val();
                 $('#inputProjectState').focus();
                 if(state.length == 0){
                        $("#inputProjectState-error").html("Please Enter the State ");
                     return false;
                 }
                 var pincode = $('#inputpincode').val();
                 $('#inputpincode').focus();
                  if(pincode.length == 0){
                         $("#inputpincode-error").html("Please Enter the Pin Code ");
                      return false;
                  }
                 var plan = $('#inputplan').val();
                 $('#inputplan').focus();
                   if(plan.length == 0){
                          $("#inputpincode-error").html("Please Enter the Plan ");
                       return false;
                 }

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
                                $('#createProject-modal').modal('hide');
                                $('#addRoomConcept').modal('show');

                                 console.log("Successfully create new project- ");
                                 console.log(body);

                                 $("#snackbar").html("Successfully create new project...");
                                var x = document.getElementById("snackbar")
                                x.className = "show";
                                setTimeout(function(){ x.className = x.className.replace("show", ""); }, 3000);
                                  that.render();
                                $(e.currentTarget).closest('article').remove();
                                 return;
                             },
                             error:function(response) {
                                 console.log(" +++++++++++++++create new project- Errrorr ++++++++++++++++++ ");
                                 console.log(response);
                             }
                      });

        },
    getSpaceTypeList: function(){
                var that = this;
                return new Promise(function(resolve, reject) {
                     if (!that.spacetypelists.get('id')) {
                        that.spacetypelists.fetch({
                            async: true,
                            crossDomain: true,
                            method: "GET",
                            headers:{
                             "authorization": "Bearer "+ sessionStorage.authtoken
                            },
                            success: function(response) {
                                console.log(" +++++++++++++++ Space Type Lists++++++++++++++++++ ");
                                console.log(response);
                                resolve();
                            },
                            error: function(model, response, options) {
                              reject();
                            }
                        });
                     }else{
                        resolve();
                     }
                });
            },
    fetchViewProjectRender: function(){
            var that = this;
            var getprojects = this.getprojects;
            var spacetypelists = this.spacetypelists;

            $(this.el).html(_.template(listingProjectPageTemplate)({
               "getprojects": getprojects.toJSON(),
                "spacetypelists": spacetypelists.toJSON()
           }));
        }

  });
    return ListingProjectPage;
});
