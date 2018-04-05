define([
  'jquery',
  'lodash',
  'backbone',
  'bootstrap',
  'pinterest_grid',
  'text!templates/dashboard/view_design.html',
  'collections/viewDesigns'
], function($, _, Backbone, Bootstrap, pinterest_grid, viewDesignPageTemplate,viewDesigns){
  var ViewDesignPage = Backbone.View.extend({
    el: '.page',
    viewDesigns: null,
    initialize: function() {
        this.viewDesigns = new viewDesigns();
        this.listenTo(Backbone);
        _.bindAll(this, 'render');
    },
    render: function () {
        var that = this;
        console.log("****IN RENDER ***********")
        var spaceTypeCode = that.model.spaceTypeCode;
//        alert("that.model.name = "+that.model.spaceTypeCode);
//        var spaceTypeCode = "SP-LIVING";

        var getDesignsPromise = that.getDesigns(spaceTypeCode);

        Promise.all([getDesignsPromise]).then(function() {
            console.log("@@@@@@@@@@@@@ In side Promise @@@@@@@@@@@@@@@@@@");
            that.rendersub();
        });


    },
    getDesigns: function(spaceTypeCode){
        var that = this;
        return new Promise(function(resolve, reject) {
            if(!that.viewDesigns.get('id')){
                that.viewDesigns.getDesigns(spaceTypeCode, {
                   async: true,
                   crossDomain: true,
                   method: "GET",
                   headers:{
                       "authorization": "Bearer "+ sessionStorage.authtoken
                   },
                   success:function(response){
                        //console.log("Successfully Searched... ");
                        //console.log(response);
                        $("#snackbar").html("Successfully searched ...");
                          var x = document.getElementById("snackbar")
                          x.className = "show";
                          setTimeout(function(){ x.className = x.className.replace("show", ""); }, 3000);
                        resolve();
                   },
                   error:function(response) {
                        console.log(" +++++++++++++++ Search- Errrorr ++++++++++++++++++ ");
                        console.log(JSON.stringify(response));
                        console.log("%%%%%%%%% response%%%%%%%%%%%%%%%%");
                        console.log(response.responseJSON.errorMessage);

                        $("#snackbar").html(response.responseJSON.errorMessage);
                        var x = document.getElementById("snackbar")
                        x.className = "show";
                        setTimeout(function(){ x.className = x.className.replace("show", ""); }, 3000);
                        reject();
                   }
                });
            }else{
                reject();
            }
            });

    },
    rendersub: function(){
        var that = this;
        var viewDesigns = that.viewDesigns;
        viewDesigns = viewDesigns.toJSON();


        $(this.el).html(_.template(viewDesignPageTemplate)({
         "designList": viewDesigns[0].designList
        }));
        that.ready();
    },
    ready: function(){
        $(function() {
           $("#searchpinBoot").pinterest_grid({
                no_columns: 5,
                padding_x: 20,
                padding_y: 20,
                margin_bottom: 50,
                single_column_breakpoint: 700
            });
        });
     }
  });
  return ViewDesignPage;
});
