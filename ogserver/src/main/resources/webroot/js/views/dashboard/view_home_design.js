define([
  'jquery',
  'lodash',
  'backbone',
  'bootstrap',
  'pinterest_grid',
  'text!templates/dashboard/view_home_design.html',
  'collections/view_home_designs',
], function($, _, Backbone, Bootstrap, pinterest_grid, viewHomeDesignPageTemplate,ViewHomeDesigns){
  var ViewHomeDesignPage = Backbone.View.extend({
    el: '.page',
    view_home_designs: null,
    initialize: function() {
        this.view_home_designs = new ViewHomeDesigns();
        this.listenTo(Backbone);
        _.bindAll(this, 'render');
    },
    render: function () {
        var that = this;
        var projectId = that.model.id;
        var userId = sessionStorage.userId;

        var getHomeDesignsPromise = that.getHomeDesigns(userId,projectId);

        Promise.all([getHomeDesignsPromise]).then(function() {
            console.log("@@@@@@@@@@@@@ In side Promise @@@@@@@@@@@@@@@@@@");
            that.rendersub();
        });
    },

    getHomeDesigns: function(userId,projectId){
        var that = this;

        return new Promise(function(resolve, reject) {
            if(!that.view_home_designs.get('id')){
                that.view_home_designs.getHomeDesigns(userId, projectId, {
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
    rendersub: function(){
     var that=this;
     var view_home_designs=that.view_home_designs;
     view_home_designs= view_home_designs.toJSON();

       $(this.el).html(_.template(viewHomeDesignPageTemplate)({
         "designhomeList": view_home_designs[0].designList
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
  return ViewHomeDesignPage;
});
