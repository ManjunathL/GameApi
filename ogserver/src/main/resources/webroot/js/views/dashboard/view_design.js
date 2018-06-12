define([
  'jquery',
  'lodash',
  'backbone',
  'bootstrap',
  'pinterest_grid',
  'text!templates/dashboard/view_design.html',
  'collections/viewDesigns',
  'models/filter',
  'collections/save_shortlist_designs',
  'collections/roomlayouts',
  'collections/uploaduserdesigns'
], function($, _, Backbone, Bootstrap, pinterest_grid, viewDesignPageTemplate,viewDesigns, Filter, SaveShortListDesigns, RoomLayouts, UploadUserDesigns){
  var ViewDesignPage = Backbone.View.extend({
    el: '.page',
    viewDesigns: null,
    filter: null,
    save_shortlist_designs:null,
    roomlayouts:null,
    uploaduserdesigns:null,
    initialize: function() {
        this.viewDesigns = new viewDesigns();
        this.roomlayouts = new RoomLayouts();
        this.filter = new Filter();
        this.save_shortlist_designs=new SaveShortListDesigns();
        this.filter.on('change', this.render, this);
        this.uploaduserdesigns = new UploadUserDesigns();
        this.listenTo(Backbone);
        _.bindAll(this, 'render');
    },
    render: function () {
        var that = this;

        window.filter = that.filter;


        console.log("****IN RENDER ***********")
        var conceptboardId = that.model.id;
        var spaceTypeCode = that.model.spaceTypeCode;


        console.log("**** spaceTypeCode ***********")
        console.log(spaceTypeCode)


        //return false;

//        alert("that.model.name = "+that.model.spaceTypeCode);
//        var spaceTypeCode = "SP-LIVING";

        var getDesignsPromise = that.getDesigns(conceptboardId,spaceTypeCode);
        var getRoomLayoutsPromise = that.getRoomLayouts(spaceTypeCode);

        Promise.all([getDesignsPromise,getRoomLayoutsPromise]).then(function() {
            console.log("@@@@@@@@@@@@@ In side Promise @@@@@@@@@@@@@@@@@@");
            that.rendersub();
        });
    },
    events: {
        "click .shortListdesign": "saveShortListDesigns",
        "change #conceptfileupload": "getuploadedFileDtls",
         "click #showImg": "showuploadedFileDtls",
        "click #addCBoard": "viewAddDesign",
        "submit #userconceptfrmImage": "submitUploadConceptBoard",
        "click .lookImg": "viewFullImg",
        "click .close": "closeModal"

    },
    closeModal: function(e){
        document.getElementById('myLookImgModal').style.display = "none";
    },
    viewFullImg: function(e){
        if (e.isDefaultPrevented()) return;
        e.preventDefault();

        var currentTarget = $(e.currentTarget);
        var imgId = currentTarget.attr('id');

        // Get the modal
        var modal = document.getElementById('myLookImgModal');

        // Get the image and insert it inside the modal - use its "alt" text as a caption
        var img = document.getElementById(imgId);
        var modalImg = document.getElementById("img01");
        var captionText = document.getElementById("caption");

        modal.style.display = "block";
        modalImg.src = currentTarget.attr('src');
        captionText.innerHTML = currentTarget.attr('alt');

        return;

    },
    getDesigns: function(conceptboardId,spaceTypeCode){
        var that = this;

        var userId = sessionStorage.userId;

        return new Promise(function(resolve, reject) {
            if(!that.viewDesigns.get('id')){
                that.viewDesigns.getDesigns(userId, spaceTypeCode, conceptboardId, {
                   async: true,
                   crossDomain: true,
                   method: "GET",
                   headers:{
                       "authorization": "Bearer "+ sessionStorage.authtoken
                   },
                   success:function(response){
                        //console.log("Successfully Searched... ");
                        //console.log(response);
                        /*$("#snackbar").html("Successfully searched ...");
                          var x = document.getElementById("snackbar")
                          x.className = "show";
                          setTimeout(function(){ x.className = x.className.replace("show", ""); }, 3000);*/
                        resolve();
                   },
                   error:function(response) {
                        console.log(" +++++++++++++++ Search- Errrorr ++++++++++++++++++ ");
                        console.log(JSON.stringify(response));
                        console.log("%%%%%%%%% response%%%%%%%%%%%%%%%%");
                        console.log(response.responseJSON.errorMessage);

                        /*$("#snackbar").html(response.responseJSON.errorMessage);
                        var x = document.getElementById("snackbar")
                        x.className = "show";
                        setTimeout(function(){ x.className = x.className.replace("show", ""); }, 3000);*/
                        reject();
                   }
                });
            }else{
                reject();
            }
            });

    },
    getRoomLayouts: function(spaceTypeCode){
        var that = this;
        return new Promise(function(resolve, reject) {
            if(!that.roomlayouts.get('id')){
                that.roomlayouts.getRoomLayoutList(spaceTypeCode, {
                   async: true,
                   crossDomain: true,
                   method: "GET",
                   headers:{
                       "authorization": "Bearer "+ sessionStorage.authtoken
                   },
                   success:function(response){
                        //console.log("Successfully Searched... ");
                        //console.log(response);
                        /*$("#snackbar").html("Successfully searched ...");
                          var x = document.getElementById("snackbar")
                          x.className = "show";
                          setTimeout(function(){ x.className = x.className.replace("show", ""); }, 3000);*/
                        resolve();
                   },
                   error:function(response) {
                        console.log(" +++++++++++++++ Search- Errrorr ++++++++++++++++++ ");
                        console.log(JSON.stringify(response));
                        console.log("%%%%%%%%% response%%%%%%%%%%%%%%%%");
                        console.log(response.responseJSON.errorMessage);

                        /*$("#snackbar").html(response.responseJSON.errorMessage);
                        var x = document.getElementById("snackbar")
                        x.className = "show";
                        setTimeout(function(){ x.className = x.className.replace("show", ""); }, 3000);*/
                        reject();
                   }
                });
            }else{
                reject();
            }
            });

    },
    saveShortListDesigns:function(e){
        var that = this;
        var filterShortDesign={};

        if (e.isDefaultPrevented()) return;

         //var userId = "user1234600";
         var userId = sessionStorage.userId;
         var conceptboardId = that.model.id;
         var spaceTypeCode = that.model.spaceTypeCode;
         var formData = that.filter.get("shortedList");
            if (typeof(that.filter.get('shortedList')) != 'undefined') {
                          filterShortDesign = that.filter.get('shortedList');

             }

        console.log("++++++++++++++++++++++++++ formData ++++++++++++++++++++++++++++++");
        console.log(JSON.stringify(formData));


        that.save_shortlist_designs.saveShortListDesigns(conceptboardId,filterShortDesign,{
           async: true,
           crossDomain: true,
           method: "POST",
           headers:{
               "authorization": "Bearer "+ sessionStorage.authtoken,
               "Content-Type": "application/json"
           },
           success:function(response) {
              console.log("Successfully saved Home Preferences");
              console.log(response);


          },
          error:function(model, response, options) {

              console.log(" +++++++++++++++ Home Preferences save- Errrorr ++++++++++++++++++ ");
              console.log(JSON.stringify(response));

          }
        });
    },
    viewAddDesign: function(event){
            $('#addcboard-modalForImageDesign').modal('show');
           $(".modal-backdrop").css("z-index", "0");
            return;
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

           // var formData = new FormData();

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
    showuploadedFileDtls: function (e) {
                var that = this;
                var dataImage = that.filter.get('imgData');
                $('#imgtt').attr('src',dataImage);
                return false;
            },
    submitUploadConceptBoard: function (e) {

            if (e.isDefaultPrevented()) return;
                    e.preventDefault();

          var that = this;
          var userId = sessionStorage.userId;
          var conceptboardId = that.filter.get("selectedconceptboardId");
          var cboardnameTxt = $('#cboardcnameTxt').val();
          if(cboardnameTxt.length == 0){
              $('#cboardnameTxt').focus();
              $("#addUploadImage_error").html("Please Enter the Concept Name ");
              return false;
          }
          var cboarddescTxt = $('#cboarddescTxt').val();
          if(cboarddescTxt == 0){
              $('#cboarddescTxt').focus();
              $("#addUploadImagedesc_error").html("Please Enter the Concept Description ");
              return false;
            }
          var conceptfileupload= that.filter.get('imgData');
          if(conceptfileupload == undefined){
                $("#addUpload_error").html("Please Upload Image ");
                return false;
            }
          console.log("@@@@@@@ conceptfileupload @@@@@@");
          console.log(conceptfileupload);

          var formData = new FormData();

          formData.append("file",conceptfileupload);
          formData.append("name",cboardnameTxt);
          formData.append("description",cboarddescTxt);
          formData.append("userId",userId);
          formData.append("conceptboardId",conceptboardId);
          formData.append("spaceelementcode","");
            console.log("++++++++++++++++++++++++++ formData ++++++++++++++++++++++++++++++");
            console.log(formData);

            that.uploaduserdesigns.fetch({
               async: true,
               crossDomain: true,
               method: "POST",
               headers:{
                   "authorization": "Bearer "+ sessionStorage.authtoken,
               },
               processData: false,
               contentType: false,
               data: formData,
               success:function(response) {
                  console.log("Successfully save user Concept  .. ");
                  console.log(JSON.stringify(response));

                  $("#addcboard-modalForImage").modal('hide');



                   $('body').removeClass('modal-open');
                   $('.modal-backdrop').remove();

                  $("#snackbar").html("Successfully save user Concept ...");
                  var x = document.getElementById("snackbar")
                  x.className = "show";
                  setTimeout(function(){ x.className = x.className.replace("show", ""); }, 3000);
                  that.render();
                  //return;
                  //$(e.currentTarget).closest('article').remove();
                  //return false;

              },
              error:function(model, response, options) {
                  console.log(" +++++++++++++++ save save user Concept image- Errrorr ++++++++++++++++++ ");
                  console.log(JSON.stringify(response));

                  $("#addcboard-modalForImage").modal('hide');

                  $('body').removeClass('modal-open');
                  $('.modal-backdrop').remove();


                    $("#snackbar").html("Successfully save user Concept ...");
                    var x = document.getElementById("snackbar")
                    x.className = "show";
                    setTimeout(function(){ x.className = x.className.replace("show", ""); }, 3000);
                    that.render();
              }
          });
          return;
        },

    rendersub: function(){
        var that = this;
        var viewDesigns = that.viewDesigns;
        var roomlayouts = that.roomlayouts;
        viewDesigns = viewDesigns.toJSON();

         var conceptboardId = that.model.id;
            this.filter.set({
                'selectedconceptboardId':conceptboardId
            }, {
                silent: true
            });

        if (typeof(that.filter.get('selectedStyleName')) == 'undefined') {
            //var arrSt = new Array();

            that.filter.set({
                'selectedStyleName': ''
            }, {
                silent: true
            });
        }
        if (typeof(that.filter.get('selectedRoomLayout')) == 'undefined') {
            //var arrSt = new Array();

            that.filter.set({
                'selectedRoomLayout': ''
            }, {
                silent: true
            });
        }


        var filterStyleName = that.filter.get('selectedStyleName');
        var filterRoomLayout = that.filter.get('selectedRoomLayout');

        console.log("++++++++++++++ StyleName ++++++++++++++++++++");
        console.log(filterStyleName);

        console.log("++++++++++++++ RoomLayout ++++++++++++++++++++");
        console.log(filterRoomLayout);

        if(typeof(filterStyleName) !== 'undefined' && filterStyleName.length > 0){
            var filteredDesigns = that.viewDesigns.filterByStyleName(viewDesigns[0].designList, filterStyleName);

            console.log("@@@@@@@@@@ filteredConcepts @@@@@@@@");
            console.log(filteredDesigns);
            console.log("@@@@@@@@@@@@@@@@@@");
            viewDesigns[0].designList = filteredDesigns;

        }else{
            viewDesigns = viewDesigns;
        }

        if(typeof(filterRoomLayout) !== 'undefined' && filterRoomLayout.length > 0){
            var filteredDesigns = that.viewDesigns.filterByRoomLayout(viewDesigns[0].designList, filterRoomLayout);

            console.log("@@@@@@@@@@ filteredConcepts @@@@@@@@");
            console.log(filteredDesigns);
            console.log("@@@@@@@@@@@@@@@@@@");
            viewDesigns[0].designList = filteredDesigns;
        }else{
            viewDesigns = viewDesigns;
        }



        $(this.el).html(_.template(viewDesignPageTemplate)({
         "designList": viewDesigns[0].designList,
         "roomlayouts": roomlayouts.toJSON()
        }));
        that.ready();
    },
    ready: function(){
        $(function() {
           $("#searchpinBoot").pinterest_grid({
                no_columns: 3,
                padding_x: 20,
                padding_y: 20,
                margin_bottom: 50,
                single_column_breakpoint: 700
            });
            //$(window).resize();
        });
     }
  });
  return ViewDesignPage;
});
