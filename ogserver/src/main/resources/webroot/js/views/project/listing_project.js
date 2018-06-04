define([
  'jquery',
  'lodash',
  'backbone',
  'bootstrap',
  'pinterest_grid',
  'owlcarousel',
  'text!templates/project/listing_project.html',
  'text!templates/project/createroomlist.html',
  'text!templates/project/editprojectandroom.html',
  'collections/getprojects',
  'collections/createprojects',
  'collections/spacetypelists',
  'collections/deleteprojects',
  'collections/conceptboards',
  'collections/updatedefaultrooms',
  'collections/addmore_roomlists',
  'collections/checknameformorerooms'
], function($, _, Backbone, Bootstrap, pinterest_grid, owlCarousel, listingProjectPageTemplate, roomListTempPageTemplate, editprojectandroomTempPageTemplate, GetProjects, CreateProjects, SpaceTypeLists, DeleteProjects, ConceptBoards, UpdateDefaultRooms, AddMoreRoomLists, CheckNameForMoreRoomLists){
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
        _.bindAll(this, 'render','fetchViewProjectRender','fetchRoomsAndRender', 'fetchEditProjectAndRommRender');
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
        "click #save_project": "editProjectAndRoomSaved",
        "click #deleteProject": "deleteProjects",
        "click #submitConceptDetail": "submitConceptDetail",
        "click .updatecreateRoom": "updateDefaultRoom",
        "click .addMoreRoom": "checkRoom",
        "click #save_roomConcept": "saveRoomConcept",
        "click #edit_roomConcept": "editRoomConcept",
        "click #edit_roomList": "editRoomList",
        "change #uploadHomelayout": "getuploadedFileDtls"
    },
    createNewProject: function(){
        $('#createProject-modal').modal('show');
    },
    saveRoomConcept: function(){
    var that = this;
    var common = $('.common').length;
    var userId = sessionStorage.userId;
    $( ".roomId" ).each(function( i ) {
        var roomId = $(this).val();
        console.log("roomId");
        console.log(roomId);
        if(roomId !== ""){
            var spaceTypeId = roomId;
            var inputname = $('#inputname'+spaceTypeId).val();
            var description = $('#description'+spaceTypeId).val();
            var spaceTypeCode = $('#inputcategory'+spaceTypeId).val();

            var body={
                "description": description,
                "id": spaceTypeId,
                "name": inputname,
                "spaceTypeCode": spaceTypeCode
            };
            if(inputname != ""){
                that.updateDefaultRoom(body);
            }
            console.log("@@@@@@@@@@@@@ spaceTypeId @@@@@@@@@@@@@@@"+spaceTypeId);
            console.log("inputname === "+inputname+"==== description ====="+description+"=========spaceTypeCode========="+spaceTypeCode);
            }else{
                var id = $(this).attr('id');
                var currIdArr = id.replace("roomID","");
                console.log("currIdArr");
                console.log(currIdArr);
                var inputname = $('#inputname'+currIdArr).val();
                var spaceTypeCode = $('#inputcategory'+currIdArr).val();
                var projectId = $('#projectId').val();
                var description = $('#description'+currIdArr).val();

                var body={
                    "description": description,
                    "id": 0,
                    "name": inputname,
                    "spaceTypeCode": spaceTypeCode,
                    "userId": userId,
                    "userMindboardId": 0,
                    "userNote": "null",
                    "userProjectId": projectId
                };

                if(inputname != ""){
                    that.checkRoom(body);
                }
                console.log("@@@@@@@@@@@@@ spaceTypeId @@@@@@@@@@@@@@@"+currIdArr);
                console.log("inputname === "+inputname+"==== description ====="+description+"=========spaceTypeCode========="+spaceTypeCode);

            }

    });

    return false;
  },
    editRoomConcept: function(){
        var that = this;
        var common = $('.editcommon').length;
        var userId = sessionStorage.userId;
        $( ".editroomID" ).each(function( i ) {
            var roomId = $(this).val();
            console.log("editroomID");
            console.log(roomId);
            if(roomId !== ""){
                var spaceTypeId = roomId;
                var inputname = $('#editinputname'+spaceTypeId).val();
                var description = $('#editdescription'+spaceTypeId).val();
                var spaceTypeCode = $('#editinputcategory'+spaceTypeId).val();

                var body={
                    "description": description,
                    "id": spaceTypeId,
                    "name": inputname,
                    "spaceTypeCode": spaceTypeCode
                };
                if(inputname != ""){
                    that.updateDefaultRoom(body);
                }
                console.log("@@@@@@@@@@@@@ spaceTypeId @@@@@@@@@@@@@@@"+spaceTypeId);
                console.log("inputname === "+inputname+"==== description ====="+description+"=========spaceTypeCode========="+spaceTypeCode);
                }else{
                    var id = $(this).attr('id');
                    var currIdArr = id.replace("editroomID","");
                    console.log("currIdArr");
                    console.log(currIdArr);
                    var inputname = $('#editinputname'+currIdArr).val();
                    var spaceTypeCode = $('#editinputcategory'+currIdArr).val();
                    var projectId = $('#editprojectId').val();
                    var description = $('#editdescription'+currIdArr).val();

                    var body={
                        "description": description,
                        "id": 0,
                        "name": inputname,
                        "spaceTypeCode": spaceTypeCode,
                        "userId": userId,
                        "userMindboardId": 0,
                        "userNote": "null",
                        "userProjectId": projectId
                    };

                    if(inputname != ""){
                        that.checkRoom(body);
                    }
                    console.log("@@@@@@@@@@@@@ spaceTypeId @@@@@@@@@@@@@@@"+currIdArr);
                    console.log("inputname === "+inputname+"==== description ====="+description+"=========spaceTypeCode========="+spaceTypeCode);

                }

        });

        return false;
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
    updateDefaultRoom: function(body){
        /*if (e.isDefaultPrevented()) return;
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
        }*/

        var that = this;
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
            console.log("Successfully Update Room in  Project ... ");
            console.log(response);
            },
            error:function(response) {
            console.log(" +++++++++++++++ Error Update Default Room - Errrorr ++++++++++++++++++ ");
            console.log(JSON.stringify(response));
            }
        });
        },
    checkRoom: function(body){
        /*if (e.isDefaultPrevented()) return;
        e.preventDefault();*/


        /*var currentTarget = $(e.currentTarget);
        var id = currentTarget.attr('id');
        var currIdArr = id.replace("add","");
        console.log("currIdArr");
        console.log(currIdArr);

        var inputname = $('#inputname'+currIdArr).val();
        var spaceTypeId = $('#inputcategory'+currIdArr).val();
        var projectId = $('#projectId').val();
        var description = $('#description'+currIdArr).val();
        var userId = sessionStorage.userId;*/

        var that = this;
        var userId = body.userId;
        var projectId = body.userProjectId;
        nameExists={
            "name":body.name
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
                            $("#snackbar").html("Successfully Add Room... ");
                            var x = document.getElementById("snackbar")
                            x.className = "show";
                            setTimeout(function(){ x.className = x.className.replace("show", ""); }, 3000);
                            return false;
                        },
                        error:function(response) {
                            console.log(" +++++++++++++++ Error by adding  Room - Errrorr ++++++++++++++++++ ");
                            console.log(JSON.stringify(response));
                            return false;
                        }
                    });
                }
            },
            error:function(response) {
                console.log(" +++++++++++++++ check Room - Errrorr ++++++++++++++++++ ");
                console.log(JSON.stringify(response));return false;
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
    editProjectAndRoomSaved: function(e){
            var that = this;
            if (e.isDefaultPrevented()) return;
            e.preventDefault();
            $(".err").html('');

            var userId = sessionStorage.userId;
            var pageno = 0;
            var itemPerPage = 10;
            var projectName = $('#editprojectName').val();
            $('#projectName').focus();
            if(projectName.length == 0){
                $("#projectNama-error").html("Please Enter the Project Name ");
                return false;
            }
            var builder = $('#editinputbuilder').val();
            $('#inputbuilder').focus();
            if(builder.length == 0){
                $("#inputbuilder-error").html("Please Enter the Builder Name ");
                return false;
            }
            var projecttower= $('#editinputprojecttower').val();
            $('#inputprojecttower').focus();
            if(projecttower.length == 0){
                $("#inputprojecttower-error").html("Please Enter the Project Tower ");
                return false;
            }
            var ProjectAddress= $('#editinputProjectAddress').val();
            $('#inputProjectAddress').focus();
            if(ProjectAddress.length == 0){
                $("#inputProjectAddress-error").html("Please Enter the Project Address ");
                return false;
            }
            var city = $('#editinputProjectCity').val();
            $('#inputProjectCity').focus();
            if(city.length == 0){
                $("#inputProjectCity-error").html("Please Enter the City ");
                return false;
            }
            var state = $('#editinputProjectState').val();
            $('#inputProjectState').focus();
            if(state.length == 0){
                $("#inputProjectState-error").html("Please Enter the State ");
                return false;
            }
            var pincode = $('#editinputpincode').val();
            $('#inputpincode').focus();
            if(pincode.length == 0){
                $("#inputpincode-error").html("Please Enter the Pin Code ");
                return false;
            }
            var plan = $('#editinputplan').val();
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
                },
                error:function(response) {
                    console.log(" +++++++++++++++create new project- Errrorr ++++++++++++++++++ ");
                    console.log(response);
                    return;
                }
            });
            return false;
        },
    getuploadedFileDtls: function (evt) {
        var that = this;

        var files = evt.target.files;

        for (var i = 0, f; f = files[i]; i++) {
            if (!f.type.match('image.*')) {
                continue;
            }
            var reader = new FileReader();
            reader.readAsDataURL(f);
        }
        console.log("file:");
        console.log(files[0]);
        console.log(files[0].name)
        //formData.append('file', files[ff]);
        var fileObj = {};
        //var fileMnObj = new File();
        if(files[0].name != ""){
            that.filter.set({
                'imgData': files[0]
            }, {
                silent: true
            });
        }
        console.log("++++++++++++ Image Data +++++++++++++++++++");
        console.log(that.filter.get('imgData'));
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
    editRoomList: function(e){
        var that = this;
        var userId = sessionStorage.userId;
        var currentTarget = $(e.currentTarget);
        var projectId = currentTarget.data('element');;
        //var projectId = currentInsertedProjectId;
        var pageno = 0;
        var itemPerPage = 20;
        var filterId = 1;
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
                that.fetchEditProjectAndRommRender(projectId);
            },
            error:function(response) {
                //console.log(" +++++++++++++++ Errrorr ++++++++++++++++++ ");
                //console.log(response);
                return false;
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
        $("#createroomlist").html(_.template(roomListTempPageTemplate)({
                  "roomListCreate":conceptboards.toJSON(),
                  "spacetypelists": spacetypelists.toJSON(),
                  "projectId": projectId
              }));
        $('#createProject-modal').modal('hide');
        $('#addRoomConcept').modal('show');
    },
    fetchEditProjectAndRommRender: function(projectId) {
            var that = this;
            var conceptboards = that.conceptboards;
            var getprojects = this.getprojects;
            getprojects = getprojects.toJSON();
            var spacetypelists = that.spacetypelists;
             var projectDtls = that.getprojects.getProjectDetails(getprojects[0].userProjects,projectId);

             console.log(" %%%%%%%%%%%%%% Project Details %%%%%%%%%%%%%% ");
                     console.log(projectDtls);
            $("#editRoomeroomlist").html(_.template(editprojectandroomTempPageTemplate)({
                      "roomListCreate":conceptboards.toJSON(),
                       "getprojects": projectDtls,
                       "spacetypelists": spacetypelists.toJSON(),
                       "projectId": projectId
                  }));
            $('#editRoomandProject').modal('show');
        }
    });
    return ListingProjectPage;
});
