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
    'text!templates/partners/subprojects.html',
    'text!templates/partners/towers.html',
    'text!templates/partners/floorplans.html',
    'text!templates/partners/bpackages.html',
    'text!templates/partners/design_gallery.html',
    'models/partner',
    'collections/developers',
    'collections/projects',
    'collections/subprojects',
    'collections/towers',
    'collections/unitdetails',
    'collections/floorplans',
    'collections/bpackages',
    'collections/designgallerys'
], function($, _, Backbone, Analytics, BuildersTemplate, SpeakersTemplate, ProjectsTemplate, SubProjectsTemplate, TowersTemplate, FloorplansTemplate, BpackagesTemplate, DesigGalleryTemplate, Partner, Developers, Projects, Subprojects, Towers, Unitdetails, Floorplans, Bpackages, Designgallerys) {
    var BuildersView = Backbone.View.extend({
        el: '#page-top',
        developers: null,
        projects: null,
        subprojects: null,
        towers: null,
        unitdetails: null,
        floorplans: null,
        bpackages: null,
        designgallerys: null,
        partner: null,
        initialize: function() {
            this.partner = new Partner();
            this.developers = new Developers();
            this.projects = new Projects();
            this.subprojects = new Subprojects();
            this.towers = new Towers();
            this.unitdetails = new Unitdetails();
            this.floorplans = new Floorplans();
            this.bpackages = new Bpackages();
            this.designgallerys = new Designgallerys();
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
                that.fetchSubprojectDetails();
             },
             error: function(model, response, options) {
                 console.log("couldn't fetch projects data - " + response);
             }
         });
        },
        fetchSubprojectDetails: function(){
            var that = this;
            this.subprojects.fetch({
                data: {
                    "developer_name": "ozone"
                },
                success: function(response) {
                    console.log(response);
                    var subprojects = response.toJSON();

                    var subprjTemp = _.template(SubProjectsTemplate);
                    $("#subprjctsel").html(subprjTemp({
                      'subprojects':subprojects
                    }));

                },
                error: function(model, response, options) {
                    console.log("couldn't fetch developers data - " + response);
                }
            });
        },
        events: {
            "click .subprjctselcls": "getTowerBlockNames",
            "click #twsubmt": "getUnitDetails",
            "click .getpak": "getPackages",
            "click .getdsgn": "getDesignGallery"
        },
        getTowerBlockNames: function(e){
            e.preventDefault();
            var currentTarget = $(e.currentTarget);
            var projectName = currentTarget.data('element');
            var subprojectName = currentTarget.data('element1');

            console.log(' selected project name ');
            console.log(projectName);
            console.log(subprojectName);

            var that = this;
            that.partner.set({
                'projectName':projectName
            }, {
                silent: true
            });
            that.partner.set({
                'subprojectName':subprojectName
            }, {
                silent: true
            });


            if(projectName != null && subprojectName != null){

                this.towers.fetch({
                     data: {
                         "developer_name": that.partner.get('developerName'),
                         "project_name": that.partner.get('projectName'),
                         "sub_project_name": that.partner.get('subprojectName')
                     },
                     success: function(response) {
                         console.log(response);
                         var towers = response.toJSON();

                         var towerTemp = _.template(TowersTemplate);
                         $("#towersel").html(towerTemp({
                             'towers':towers
                         }));

                        $("#apartment_number").val('');
                        $("#flrplnsel").html('');
                        $("#ozon-pkag").html('');
                        $("#ozon-designs").html('');
                         window.location.href="#personalized";

                     },
                     error: function(model, response, options) {
                         console.log("couldn't fetch towers data - " + response);
                     }
                });
            }
        },
        getUnitDetails: function(e){
         e.preventDefault();
         var currentTarget = $(e.currentTarget);
         var groupId = $("#towersel").val();
         var grouping_name = $("#towersel option:selected").text();
         var apartment_number = $("#apartment_number").val();


         var that = this;
         if(grouping_name != null && apartment_number != null){
         this.unitdetails.fetch({
              data: {
                  "sub_project_name": that.partner.get('subprojectName'),
                  "group_code": grouping_name,
                  "unit_number": apartment_number
              },
              success: function(response) {
                  console.log("unit Master");

                  var unitDetails = response.toJSON();
                  console.log(unitDetails);
                  if(unitDetails.length != 0){
                      that.partner.set({
                          'floorPlanSetId':unitDetails[0].floor_plan_setId
                      }, {
                          silent: true
                      });

                      that.partner.set({
                          'subProjectId':unitDetails[0].subProjectId
                      }, {
                          silent: true
                      });
                  }else{
                    that.partner.set({
                      'floorPlanSetId':''
                  }, {
                      silent: true
                  });

                  that.partner.set({
                      'subProjectId':''
                  }, {
                      silent: true
                  });
                  }
                  that.getFloorplanImg();

              },
              error: function(model, response, options) {
                  console.log("couldn't fetch unit data - " + response);
              }
           });
            }
        },
        getFloorplanImg: function(){
            var that = this;
            this.floorplans.fetch({
                data: {
                  "subProjectId": that.partner.get('subProjectId')
                },
                success: function(response) {
                    console.log('floor plans');
                    console.log(response);
                    var floorplans = response.toJSON();
                       if(floorplans.length != 0){
                      that.partner.set({
                          'floorPlanSetId':floorplans[0].floor_plan_setId
                      }, {
                          silent: true
                      });
                        }
                     var flTemp = _.template(FloorplansTemplate);
                      $("#flrplnsel").html(flTemp({
                          'floorplans':floorplans
                      }));
                      that.getPackages();
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

         var that = this;
         if(!floorplanId){
           var floorplanId = that.partner.get('floorPlanSetId')
         }

         console.log('================floorPlanId=================');
         console.log(floorplanId);


         var that = this;
         this.bpackages.fetch({
              data: {
                  "floor_plan_setId": floorplanId
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

            this.designgallerys.fetch({
                data: {
                  "packageId": packageId
                },
                success: function(response) {
                    var designgallerys = response.toJSON();

                     var desgnTemp = _.template(DesigGalleryTemplate);
                     $("#ozon-designs").html(desgnTemp({
                       'designgallerys':designgallerys,
                       'selpackageId':packageId
                     }));
                },
                error: function(model, response, options) {
                  console.log("couldn't fetch floor plan data - " + response);
                }
          });

        }
    });
    return BuildersView;
});