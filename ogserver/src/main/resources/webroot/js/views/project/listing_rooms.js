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
  'views/dashboard/add_conceptboard',
  'collections/add_conceptboards',
  'collections/deleteroomlists',
  'collections/updatedefaultrooms',
], function($, _, Backbone, Bootstrap, pinterest_grid, owlCarousel, listingRoomPageTemplate, GetProjects, ConceptBoards,AddConceptboard, AddConceptboards, DeleteRoomLists, UpdateDefaultRooms){
  var ListingRoomPage = Backbone.View.extend({
    el: '.page',
    project: null,
    conceptboards: null,
    add_conceptboards: null,
    deleteroomlists: null,
    updatedefaultrooms: null,
    initialize: function() {
        this.project = new GetProjects();
        this.conceptboards = new ConceptBoards();
        this.add_conceptboards = new AddConceptboards();
        this.deleteroomlists = new DeleteRoomLists();
        this.updatedefaultrooms = new UpdateDefaultRooms();

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
        "click .addCBoard1": "viewAddCboard",
        "click #save_Cboard": "submitBoard",
        "click #deleteroom": "removeRoomList",
        "click #editDesc": "editDescRoomList",
        "click #save_Roomdesc": "UpdateDescRoom"
    },
    submitBoard: function (e) {
            if (e.isDefaultPrevented()) return;
            e.preventDefault();

            var userId = sessionStorage.userId;
            var userMindboardId = sessionStorage.defaultMindboardId;
            var CheckConceptBoard = $('#templateCodeTxt').val();
             var conName=$('#cboardnameTxt').val()
            console.log($('#cboardnameTxt').val());
            if(conName.length == 0){
                $('#cboardnameTxt').focus();
                $("#addConcptBoardName_error").html("Please Enter the Concept Board Name ");

                return false;
            }
           if(CheckConceptBoard != "blank"){
           var formData = {
                "completionPercentage": 0,
                "description": "",
                "id": $('#spaceIdTxt').val(),
                "imgUrl": "",
                "name": $('#cboardnameTxt').val(),
                "noOfConceptsAdded": 0,
                "spaceTypeCategory": 0,
                "spaceTypeCode": $('#spaceTypeCodeTxt').val(),
                "templateCode": $('#templateCodeTxt').val(),
                "type": "",
                "userId": userId,
                "userMindboardId": 0,
                "userNote": "",
                "userProjectId": userMindboardId
           };


            var that = this;
            console.log("++++++++++++++++++++++++++ formData ++++++++++++++++++++++++++++++");
            console.log(formData);

            that.add_conceptboards.fetch({
               async: true,
               crossDomain: true,
               method: "POST",
               headers:{
                   "authorization": "Bearer "+ sessionStorage.authtoken,
                   "Content-Type": "application/json"
               },
               data: JSON.stringify(formData),
               success:function(response) {
                  console.log("Successfully save Concept board through template selection... ");
                  console.log(response);
                  console.log("++++ I m here +");

                  $("#addcboard-modal").modal('hide');
                  $('body').removeClass('modal-open');
                  $('.modal-backdrop').remove();

                  $("#snackbar").html("Successfully save Concept board through template selection... ");
                  var x = document.getElementById("snackbar")
                  x.className = "show";
                  setTimeout(function(){ x.className = x.className.replace("show", ""); }, 3000);
                  that.render();
                  //return;

              },
              error:function(model, response, options) {

                  console.log(" +++++++++++++++ save Concept to Concept board- Errrorr ++++++++++++++++++ ");
                  console.log(JSON.stringify(response));
                  console.log("%%%%%%%%% response%%%%%%%%%%%%%%%%");
                  console.log(response.responseJSON.errorMessage);
                    return;

              }
          });
          }else{
                 var formData = {
                          "id": 0,
                          "userId": userId,
                          "name": $('#cboardnameTxt').val(),
                          "spaceTypeCode":$('#spaceTypeCodeTxt').val(),
                          "description": 'blank',
                         /* "templateCode": $('#templateCodeTxt').val(),*/
                          "userMindboardId": userMindboardId,
                          /*"imgUrl":'none',
                          "noOfConceptsAdded": 1,*/
                          "userNote": 'blank'
                     };
                      var that = this;
                      console.log("++++++++++++++++++++++++++ formData ++++++++++++++++++++++++++++++");
                      console.log(formData);

                      that.add_blankboards.fetch({
                         async: true,
                         crossDomain: true,
                         method: "POST",
                         headers:{
                             "authorization": "Bearer "+ sessionStorage.authtoken,
                             "Content-Type": "application/json"
                         },
                         data: JSON.stringify(formData),
                         success:function(response) {
                            console.log("Successfully save Concept board through template selection... ");
                            console.log(response);

                             $("#addcboard-modal").modal('hide');

                             $('body').removeClass('modal-open');
                             $('.modal-backdrop').remove();

                              $("#snackbar").html("Successfully save Concept board through template selection... ");
                              var x = document.getElementById("snackbar")
                              x.className = "show";
                              setTimeout(function(){ x.className = x.className.replace("show", ""); }, 3000);
                              that.render();
                            return;

                        },
                        error:function(model, response, options) {

                            console.log(" +++++++++++++++ save Concept to Concept board- Errrorr ++++++++++++++++++ ");
                            console.log(JSON.stringify(response));
                            console.log("%%%%%%%%% response%%%%%%%%%%%%%%%%");
                            console.log(response.responseJSON.errorMessage);

                             $("#snackbar").html(response.responseJSON.errorMessage);
                             var x = document.getElementById("snackbar")
                             x.className = "show";
                             setTimeout(function(){ x.className = x.className.replace("show", ""); }, 3000);
                             return;

                        }
                    });
                }
        },
    viewAddCboard: function(evt){
        var that = this;

        evt.preventDefault();
        var currentTarget = $(evt.currentTarget);
        var spaceTypeCode = currentTarget.data('element');
        var spaceId = currentTarget.data('element1');

        $('#addcboard-modal').modal('show');
        AddConceptboard.apply(spaceTypeCode,spaceId);
    },
    UpdateDescRoom: function(body){
            var that = this;
            var roomId = $('#editroomid').val();
            var name = $('#editname').val();
            var description = $('#editdescription').val();
            var spaceTypeCode = $('#editspaceTypeCode').val();
            var body={
                "description": description,
                "id": roomId,
                "name": name,
                "spaceTypeCode": spaceTypeCode
            };
            var userId = sessionStorage.userId;
            that.updatedefaultrooms.addUserDefaultRoomList({
                async: true,
                crossDomain: true,
                method: "POST",
                headers:{
                "authorization": "Bearer "+ sessionStorage.authtoken,
                "Content-Type": "application/json"
                },
                data: JSON.stringify(body),
                success:function(response) {
                console.log("Successfully Update Room Description ... ");
                console.log(response);
                $("#snackbar").html("Successfully Update Room Description... ");
                var x = document.getElementById("snackbar")
                x.className = "show";
                setTimeout(function(){ x.className = x.className.replace("show", ""); }, 3000);
                that.render();
                $('#editDesc-modal').modal('hide');
                $('body').removeClass('modal-open');
                $('.modal-backdrop').remove();

                },
                error:function(response) {
                console.log(" +++++++++++++++ Error Update Room Description - Errrorr ++++++++++++++++++ ");
                console.log(JSON.stringify(response));
                }
            });
    },
    editDescRoomList: function(evt){
        var that = this;
        var currentTarget = $(evt.currentTarget);
        var id = currentTarget.data('element');
        var desc = currentTarget.data('element1');
        var name = currentTarget.data('element2');
        var spaceTypeCode = currentTarget.data('element3');


        $('#editroomid').val(id);
        $('#editdescription').text(desc);
        $('#editname').val(name);
        $('#editspaceTypeCode').val(spaceTypeCode);
        $('#editDesc-modal').modal('show');

        return false;

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
    removeRoomList: function(e){
        if (e.isDefaultPrevented()) return;
        e.preventDefault();
        var that = this;

        if(confirm("Are you sure you want to delete this room ?") == false){
                return false;
        }
        var currentTarget = $(e.currentTarget);
        var projectId = currentTarget.data('element');
        var userId = sessionStorage.userId;
        that.deleteroomlists.removeRoomLists(projectId, {
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
                $("#snackbar").html("Successfully Deleted Room selection... ");
                var x = document.getElementById("snackbar")
                x.className = "show";
                setTimeout(function(){ x.className = x.className.replace("show", ""); }, 3000);
                that.render();
                //              return;
            },
            error:function(response) {
                console.log(" +++++++++++++++  Deleted Room- Errrorr ++++++++++++++++++ ");
                console.log(JSON.stringify(response));
                $("#snackbar").html(response.responseJSON.errorMessage);
                var x = document.getElementById("snackbar")
                x.className = "show";
                setTimeout(function(){ x.className = x.className.replace("show", ""); }, 3000);
            }
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
