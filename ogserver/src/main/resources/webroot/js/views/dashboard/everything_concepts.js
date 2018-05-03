define([
  'jquery',
  'lodash',
  'backbone',
  'bootstrap',
  'pinterest_grid',
  'text!templates/dashboard/everything.html',
  'collections/everythings'
], function($, _, Backbone, Bootstrap, pinterest_grid, everythingPageTemplate,Everything){
  var EverythingConceptPage = Backbone.View.extend({
    el: '.page',
    everythings: null,
    initialize: function() {
        this.everythings = new Everything();
        this.listenTo(Backbone);
        _.bindAll(this, 'render');
    },
    render: function () {
        var that = this;
        console.log("****IN RENDER ***********")
        var spaceTypeCode = that.model.spaceTypeCode;
        var getEverythingConceptBoardPromise = that.getEverythingConceptBoard();

        Promise.all([getEverythingConceptBoardPromise]).then(function() {
            console.log("@@@@@@@@@@@@@ In side Promise @@@@@@@@@@@@@@@@@@");
            that.rendersub();
        });


    },
    getEverythingConceptBoard: function(){
        var that = this;

        var userId = sessionStorage.userId;
        var userMindboardId = sessionStorage.defaultMindboardId;
        var pageno = 0;
        var itemPerPage = 50;

        var formData = {
                      "tag": "",
                      "type": 0
                    };

       /*console.log("tagObj");
       console.log(typeof(tagObj));

        var formData = {
            "userId": userId,
            "userMindboardId": 17,
            "pageno": pageno,
            "itemPerPage":itemPerPage,
            "tagDTO":tagObj
        };*/


        console.log("formData");
        console.log(formData);
        /*return new Promise(function(resolve, reject) {
                           that.everything.fetch({
                              async: true,
                              crossDomain: true,
                              method: "POST",
                              headers:{
                                 "authorization": "Bearer "+ sessionStorage.authtoken,
                                 "Content-Type": "application/json"
                              },
                              data: JSON.stringify(formData),
                              success:function(response) {
                                  //console.log("Successfully fetch "+ currTab  +" Concepts - ");

                                  console.log(" everything response");
                                  console.log(response);

                                  resolve();
                              },
                              error:function(response) {
                                  console.log(" +++++++++++++++ Errrorr ++++++++++++++++++ ");
                                  console.log(response);
                                  reject();
                              }
                          });

                    });*/

        return new Promise(function(resolve, reject) {
         if(typeof(userId) !== 'undefined') {
           that.everythings.getEverythingList(userId, userMindboardId, pageno, itemPerPage,{
                async: true,
                crossDomain: true,
                method: "POST",
                headers:{
                   "authorization": "Bearer "+ sessionStorage.authtoken,
                   "Content-Type": "application/json"
                },
                data: JSON.stringify(formData),
              success:function(response) {
                  //console.log("Successfully fetch "+ currTab  +" Concepts - ");

                  console.log("everythings   ++++++++++++++ ");
                  console.log(response);

                  resolve();
              },
              error:function(response) {
                  console.log(" +++++++++++++++ Errrorr ++++++++++++++++++ ");
                  console.log(response);
                  reject();
              }
          });
         }else{
            resolve();
         }
    });
    },

    rendersub: function(){
        var that = this;
        var everythings = that.everythings;
        everythings = everythings.toJSON();


        $(this.el).html(_.template(everythingPageTemplate)({
         "listOfConcepts": everythings[0].listOfConcepts
        }));
        that.ready();
    },
    ready: function(){
        $(function() {
           $("#listOfConceptspinBoot").pinterest_grid({
                no_columns: 5,
                padding_x: 20,
                padding_y: 20,
                margin_bottom: 50,
                single_column_breakpoint: 700
            });
            //$(window).resize();
        });
     }
  });
  return EverythingConceptPage;
});
