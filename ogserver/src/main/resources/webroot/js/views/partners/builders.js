/**
 * Created by Smruti on 10/03/2017.
 */
define([
    'jquery',
    'underscore',
    'backbone',
    'analytics',
    'text!templates/partners/builders.html',
    'text!templates/partners/speakers.html',
    'text!templates/partners/projects.html',
    'text!templates/partners/towers.html',
    'text!templates/partners/floorplans.html',
    'text!templates/partners/bpackages.html',
    'text!templates/partners/design_gallery.html',
    'models/partner',
    'collections/developers',
    'collections/projects',
    'collections/towers',
    'collections/floorplans',
    'collections/bpackages'
], function($, _, Backbone, Analytics, BuildersTemplate, SpeakersTemplate, ProjectsTemplate, TowersTemplate, FloorplansTemplate, BpackagesTemplate, DesigGalleryTemplate, Partner, Developers, Projects, Towers, Floorplans, Bpackages) {
    var BuildersView = Backbone.View.extend({
        el: '#page-top',
        developers: null,
        projects: null,
        towers: null,
        floorplans: null,
        bpackages: null,
        partner: null,
        initialize: function() {
            this.partner = new Partner();
            this.developers = new Developers();
            this.projects = new Projects();
            this.towers = new Towers();
            this.floorplans = new Floorplans();
            this.bpackages = new Bpackages();
            Analytics.apply(Analytics.TYPE_GENERAL);
        },
        render: function() {
            var buildersTemp = _.template(BuildersTemplate);
            $(this.el).html(buildersTemp);
            $('head').html('');
            this.ready();
        },
        ready: function(){
            var that = this;
            that.fetchDeveloperDetails();
        },
        fetchDeveloperDetails: function(){
            var that = this;
            this.developers.fetch({
                data: {
                    "developer_name": "ozone"
                },
                success: function(response) {
                    console.log(response);
                    var developers = response.toJSON();

                    that.partner.set({
                        'developerName':developers[0].developer_name
                    }, {
                        silent: true
                    });
                    var speakersTemp = _.template(SpeakersTemplate);
                    $("#speakers").html(speakersTemp({
                        'developers':developers
                    }));
                    that.fetchProjectDetails();

                },
                error: function(model, response, options) {
                    console.log("couldn't fetch developers data - " + response);
                }
            });
        },
        fetchProjectDetails: function(){
         var that = this;
         this.projects.fetch({
             data: {
                 "developer_name": that.partner.get('developerName')
             },
             success: function(response) {
                 console.log(response);
                 var projects = response.toJSON();

                 var prjTemp = _.template(ProjectsTemplate);
                 $("#prjctsel").html(prjTemp({
                     'projects':projects
                 }));

             },
             error: function(model, response, options) {
                 console.log("couldn't fetch projects data - " + response);
             }
         });
        },
        events: {
            "click .prjctselcls": "getTowerBlockNames",
            "click #twsubmt": "getFloorplanImg",
            "click .getpak": "getPackages",
            "click .getdsgn": "getDesignGallery"
        },
        getTowerBlockNames: function(e){
            e.preventDefault();
            var currentTarget = $(e.currentTarget);
            var projectName = currentTarget.data('element');

            console.log(' selected project name ');
            console.log(projectName);

            var that = this;
            that.partner.set({
                'projectName':projectName
            }, {
                silent: true
            });


            if(projectName != null){

                this.towers.fetch({
                     data: {
                         "developer_name": that.partner.get('developerName'),
                         "project_name": that.partner.get('projectName')
                     },
                     success: function(response) {
                         console.log(response);
                         var towers = response.toJSON();

                         var towerTemp = _.template(TowersTemplate);
                         $("#towersel").html(towerTemp({
                             'towers':towers
                         }));

                         window.location.href="#personalized";

                     },
                     error: function(model, response, options) {
                         console.log("couldn't fetch towers data - " + response);
                     }
                });
            }
        },
        getFloorplanImg: function(e){
         e.preventDefault();
         var currentTarget = $(e.currentTarget);
         var blockId = $("#towersel").val();
         var block_name = $("#towersel option:selected").text();
         var apartment_number = $("#apartment_number").val();

         console.log(blockId+" ------------------- "+block_name+" --------------------------- "+apartment_number);


         var that = this;
         this.floorplans.fetch({
              data: {
                  "blockId": blockId,
                  "block_name": block_name,
                  "apartment_number": apartment_number
              },
              success: function(response) {
                  console.log(response);
                  var floorplans = response.toJSON();

                  var flTemp = _.template(FloorplansTemplate);
                  $("#flrplnsel").html(flTemp({
                      'floorplans':floorplans
                  }));

              },
              error: function(model, response, options) {
                  console.log("couldn't fetch floor plan data - " + response);
              }
         });
        },
        getPackages: function(e){
         e.preventDefault();
         var currentTarget = $(e.currentTarget);
         var floorplanId = currentTarget.data('element');

         console.log('================floorPlanId=================');
         console.log(floorplanId);


         var that = this;
         this.bpackages.fetch({
              data: {
                  "floorPlanId": floorplanId
              },
              success: function(response) {
                  console.log(response);
                  var bpackages = response.toJSON();

                  that.partner.set({
                      'bpackages':bpackages
                  }, {
                      silent: true
                  });

                  var bpakTemp = _.template(BpackagesTemplate);
                  $("#ozon-pkag").html(bpakTemp({
                      'bpackages':bpackages
                  }));

              },
              error: function(model, response, options) {
                  console.log("couldn't fetch package data - " + response);
              }
         });
        },
        getDesignGallery: function(e){
            e.preventDefault();
            var currentTarget = $(e.currentTarget);
            var that = this;
            var packageId = currentTarget.data('element');
            var desgnTemp = _.template(DesigGalleryTemplate);
            $("#ozon-designs").html(desgnTemp({
              'bpackages':that.partner.get('bpackages'),
              'selpackageId':packageId
            }));
        }
    });
    return BuildersView;
});