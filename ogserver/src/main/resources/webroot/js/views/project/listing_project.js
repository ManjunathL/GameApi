define([
  'jquery',
  'lodash',
  'backbone',
  'bootstrap',
  'pinterest_grid',
  'owlcarousel',
  'text!templates/project/listing_project.html',
  'text!templates/project/createroomlist.html',
  'collections/getprojects',
  'collections/createprojects',
  'collections/spacetypelists',
  'collections/deleteprojects',
  'collections/conceptboards',
  'collections/updatedefaultrooms',
  'collections/addmore_roomlists',
  'collections/checknameformorerooms'
], function($, _, Backbone, Bootstrap, pinterest_grid, owlCarousel, listingProjectPageTemplate, roomListTempPageTemplate, GetProjects, CreateProjects, SpaceTypeLists, DeleteProjects, ConceptBoards, UpdateDefaultRooms, AddMoreRoomLists, CheckNameForMoreRoomLists){
  var ListingProjectPage = Backbone.View.extend({
    el: '.page',
    getprojects: null,
    spacetypelists: null,
    updatedefaultroom: null,
    addmore_roomlists: null,
    checknameformorerooms: null,
    initialize: function() {
        this.getprojects = new GetProjects();
        this.createprojects = new CreateProjects();
        this.spacetypelists = new SpaceTypeLists();
        this.deleteprojects = new DeleteProjects();
        this.conceptboards = new ConceptBoards();
        this.updatedefaultroom = new UpdateDefaultRooms();
        this.addmore_roomlists = new AddMoreRoomLists();
        this.checknameformorerooms = new CheckNameForMoreRoomLists();
        this.listenTo(Backbone);
        _.bindAll(this, 'render','fetchViewProjectRender','fetchRoomsAndRender');
    },
    render: function () {
        var that = this;
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
            "click #deleteProject": "deleteProjects",
            "click #submitConceptDetail": "submitConceptDetail",
            "click .updatecreateRoom": "updateDefaultRoom",
            "click .addMoreRoom": "checkRoom"
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
    deleteProjects: function(e){
                          if (e.isDefaultPrevented()) return;
                          e.preventDefault();
                          var that = this;
                          var currentTarget = $(e.currentTarget);
                          var projectId = currentTarget.data('element');
                          var userId = sessionStorage.userId;
                                 that.deleteprojects.removeProject(userId,projectId, {
                                       async: true,
                                       crossDomain: true,
                                       method: "POST",
                                       headers:{
                                           "authorization": "Bearer "+ sessionStorage.authtoken,
                                           "Content-Type": "application/json"
                                       },
                                       success:function(response) {
                                          console.log("Successfully removed Project ... ");
                                          console.log(response);
                                          $("#snackbar").html("Successfully removed Project selection... ");
                                          var x = document.getElementById("snackbar")
                                          x.className = "show";
                                          setTimeout(function(){ x.className = x.className.replace("show", ""); }, 3000);
                                          that.render();
                            //              return;
                                 },
                                    error:function(response) {
                                         console.log(" +++++++++++++++ Delete Project- Errrorr ++++++++++++++++++ ");
                                         console.log(JSON.stringify(response));
                                         console.log("%%%%%%%%% response %%%%%%%%%%%%%%%%");
                                         console.log(response.responseJSON.errorMessage);

                                          $("#snackbar").html(response.responseJSON.errorMessage);
                                          var x = document.getElementById("snackbar")
                                          x.className = "show";
                                          setTimeout(function(){ x.className = x.className.replace("show", ""); }, 3000);
                                    }
                                });
                  },
    updateDefaultRoom: function(e){
         if (e.isDefaultPrevented()) return;
         e.preventDefault();
         var that = this;
         var currentTarget = $(e.currentTarget);
         var spaceTypeId = currentTarget.attr('id');
         var inputname = $('#inputname'+spaceTypeId).val();
         var description = $('#description'+spaceTypeId).val();
         var spaceTypeCode = $('#inputcategory'+spaceTypeId).val();
         var body={
           "description": description,
           "id": spaceTypeId,
           "name": inputname,
           "spaceTypeCode": spaceTypeCode
         }
         var userId = sessionStorage.userId;
                that.updatedefaultroom.addUserDefaultRoomList({
                      async: true,
                      crossDomain: true,
                      method: "POST",
                      headers:{
                          "authorization": "Bearer "+ sessionStorage.authtoken,
                          "Content-Type": "application/json"
                      },
                      data: JSON.stringify(body),
                      success:function(response) {
                         console.log("Successfully Add Room in  Project ... ");
                         console.log(response);
                         $("#snackbar").html("Successfully Add Room in Project selection... ");
                         var x = document.getElementById("snackbar")
                         x.className = "show";
                         setTimeout(function(){ x.className = x.className.replace("show", ""); }, 3000);
                         return false;
                },
                   error:function(response) {
                        console.log(" +++++++++++++++ Delete Add Room - Errrorr ++++++++++++++++++ ");
                        console.log(JSON.stringify(response));
                        console.log("%%%%%%%%% response %%%%%%%%%%%%%%%%");
                        console.log(response.responseJSON.errorMessage);

                         $("#snackbar").html(response.responseJSON.errorMessage);
                         var x = document.getElementById("snackbar")
                         x.className = "show";
                         setTimeout(function(){ x.className = x.className.replace("show", ""); }, 3000);
                   }
               });
    },
    checkRoom: function(e){
             if (e.isDefaultPrevented()) return;
             e.preventDefault();
             var that = this;
             var currentTarget = $(e.currentTarget);
             var id = currentTarget.attr('id');
             var currIdArr = id.replace("add","");
             console.log("currIdArr");
             console.log(currIdArr);

             var inputname = $('#inputname'+currIdArr).val();
             var spaceTypeId = $('#inputcategory'+currIdArr).val();
             var projectId = $('#projectId').val();
             var description = $('#description'+currIdArr).val();
             var userId = sessionStorage.userId;


             var body={
                "description": description,
                 "id": 0,
                 "name": inputname,
                 "spaceTypeCode": spaceTypeId,
                 "userId": userId,
                 "userMindboardId": 0,
                 "userNote": "null",
                 "userProjectId": projectId
             }
             nameExists={
             "name":inputname
             }
             that.checknameformorerooms.checkNameForMoreRoom(userId,projectId,{
                   async: true,
                   crossDomain: true,
                   method: "POST",
                   headers:{
                       "authorization": "Bearer "+ sessionStorage.authtoken,
                       "Content-Type": "application/json"
                   },
                   data:JSON.stringify(nameExists),
                   success:function(response) {
                      console.log("Successfully check Room in  Project ... ");
                      console.log(response);
                      var checkNameResonspe= response.toJSON();
                      var nameExists=checkNameResonspe[0].nameExists;
                      if(nameExists==false){
                       that.addmore_roomlists.addUserMoreRoomList({
                                async: true,
                                crossDomain: true,
                                method: "POST",
                                headers:{
                                    "authorization": "Bearer "+ sessionStorage.authtoken,
                                    "Content-Type": "application/json"
                                },
                                data:JSON.stringify(body),
                                success:function(response) {
                                   console.log("Successfully Add Room in  Project ... ");
                                   console.log(response);
                                   $("#snackbar").html("Successfully Add Room in Project selection... ");
                                   var x = document.getElementById("snackbar")
                                   x.className = "show";
                                   setTimeout(function(){ x.className = x.className.replace("show", ""); }, 3000);
                                   return false;
                          },
                             error:function(response) {
                                  console.log(" +++++++++++++++ Delete Add Room - Errrorr ++++++++++++++++++ ");
                                  console.log(JSON.stringify(response));
                                  return false;
                             }
                         });
                      }
             },
                error:function(response) {
                     console.log(" +++++++++++++++ check Room - Errrorr ++++++++++++++++++ ");
                     console.log(JSON.stringify(response));
                     console.log("%%%%%%%%% response %%%%%%%%%%%%%%%%");
                     console.log(response.responseJSON.errorMessage);
                }
            });

        },
    saveConceptFileupload: function(e){
        var that = this;
        if (e.isDefaultPrevented()) return;
        e.preventDefault();
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

        var formdata={
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
            data: JSON.stringify(formdata),
            success:function(response) {
                console.log("Successfully create new project- ");
                console.log(response);

                var resDefProjects = response.toJSON();
                var currentInsertedProjectId = resDefProjects[0].id ;
                console.log("currentInsertedProjectId");
                console.log(currentInsertedProjectId);
                $("#snackbar").html("Successfully create new project...");
                var x = document.getElementById("snackbar")
                x.className = "show";
                setTimeout(function(){ x.className = x.className.replace("show", ""); }, 3000);

                that.getConceptBoards(currentInsertedProjectId);

            },
            error:function(response) {
                console.log(" +++++++++++++++create new project- Errrorr ++++++++++++++++++ ");
                console.log(response);
                return;
            }
        });
        return false;
    },
    getConceptBoards: function(currentInsertedProjectId){
      var that = this;
      var userId = sessionStorage.userId;
      //var userId = "user1234600";
      var projectId = currentInsertedProjectId;
      var pageno = 0;
      var itemPerPage = 20;
      var filterId = 1;
      //return new Promise(function(resolve, reject) {
           that.conceptboards.getConceptBoardList(userId, projectId, pageno, itemPerPage,filterId, {
               async: true,
               crossDomain: true,
               method: "GET",
               headers:{
                   "authorization": "Bearer "+ sessionStorage.authtoken
               },
               success:function(data) {
                   console.log(" +++++++++++++++ Room List ++++++++++++++++++ ");
                   console.log(data);
                   that.fetchRoomsAndRender(projectId);
               },
               error:function(response) {
                   //console.log(" +++++++++++++++ Errrorr ++++++++++++++++++ ");
                   //console.log(response);
                   return false;
               }
           });
      //});
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
    },
    fetchRoomsAndRender: function(projectId) {
        var that = this;
        var conceptboards = that.conceptboards;
        var spacetypelists = that.spacetypelists;



        $("#createroomlist").html(_.template(roomListTempPageTemplate)({
           "roomListCreate":conceptboards.toJSON(),
           "spacetypelists": spacetypelists.toJSON(),
           "projectId": projectId
       }));
        $('#createProject-modal').modal('hide');
                $('#addRoomConcept').modal('show');
    }
  });
    return ListingProjectPage;
});
