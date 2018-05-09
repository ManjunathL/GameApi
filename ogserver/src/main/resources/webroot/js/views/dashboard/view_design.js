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
  'collections/roomlayouts'
], function($, _, Backbone, Bootstrap, pinterest_grid, viewDesignPageTemplate,viewDesigns, Filter, SaveShortListDesigns, RoomLayouts){
  var ViewDesignPage = Backbone.View.extend({
    el: '.page',
    viewDesigns: null,
    filter: null,
    save_shortlist_designs:null,
    roomlayouts:null,
    initialize: function() {
        this.viewDesigns = new viewDesigns();
        this.roomlayouts = new RoomLayouts();
        this.filter = new Filter();
        this.save_shortlist_designs=new SaveShortListDesigns();

        this.filter.on('change', this.render, this);
        this.listenTo(Backbone);
        _.bindAll(this, 'render');
    },
    render: function () {
        var that = this;

        window.filter = that.filter;


        console.log("****IN RENDER ***********")
        var conceptboardId = that.model.id;
        var spaceTypeCode = that.model.spaceTypeCode;

//        alert("that.model.name = "+that.model.spaceTypeCode);
//        var spaceTypeCode = "SP-LIVING";

        var getDesignsPromise = that.getDesigns(conceptboardId);
        var getRoomLayoutsPromise = that.getRoomLayouts(spaceTypeCode);

        Promise.all([getDesignsPromise,getRoomLayoutsPromise]).then(function() {
            console.log("@@@@@@@@@@@@@ In side Promise @@@@@@@@@@@@@@@@@@");
            that.rendersub();
        });
    },
    events: {
        "click .shortListdesign": "saveShortListDesigns",
    },
    getDesigns: function(conceptboardId){
        var that = this;
        return new Promise(function(resolve, reject) {
            if(!that.viewDesigns.get('id')){
                that.viewDesigns.getDesigns(conceptboardId, {
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
    rendersub: function(){
        var that = this;
        var viewDesigns = that.viewDesigns;
        var roomlayouts = that.roomlayouts;
        viewDesigns = viewDesigns.toJSON();


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
