define([
  'jquery',
  'lodash',
  'backbone',
  'bootstrap',
  'text!templates/my_account/user_preference.html',
  'text!templates/my_account/home_preference.html',
  'text!templates/my_account/family_preference.html',
  'text!templates/my_account/member_preference.html',
  'text!templates/my_account/edit_member.html',
  'collections/user_preferences',
  'collections/user_members',
  'collections/saveuser_preferences',
  'collections/saveuser_members',
  'models/filter_preference',
  'models/filter',
  'collections/family_preferences',
  'collections/editfamilymembers',
  'collections/deletefamilymembers'
], function($, _, Backbone, Bootstrap, UserPreferenceTemplate, HomePreferenceTemplate, FamilyPreferenceTemplate, MemberPreferenceTemplate, editMemberPageTemplate,UserPreferences,UserMembers,SaveUserPreferences,SaveUserMembers,FilterPreference,Filter, FamilyPreferences, EditFamilyMembers, DeleteFamilyMembers){
  var UserProfilePage = Backbone.View.extend({
    el: '.page',
    user_home_preferences: null,
    user_family_preferences: null,
    user_members: null,
    saveuser_preferences:null,
    saveuser_members:null,
    filter_preference: null,
    filter: null,
    family_preference:null,
    editfamilymembers:null,
    deletefamilymembers:null,
    initialize: function() {
        this.filter_preference = new FilterPreference();
        this.user_home_preferences = new UserPreferences();
        this.user_family_preferences = new UserPreferences();
        this.user_members = new UserMembers();
        this.saveuser_preferences = new SaveUserPreferences();
        this.saveuser_members = new SaveUserMembers();
        this.filter = new Filter();
        this.filter_preference.on('change', this.render, this);
        this.filter.on('change', 'getSelectedQuestionFamily', this);
        this.family_preference = new FamilyPreferences();
        this.editfamilymembers = new EditFamilyMembers();
        this.deletefamilymembers = new DeleteFamilyMembers();
        this.listenTo(Backbone);
        _.bindAll(this, 'render');
    },
    render: function () {
       var that = this;

       window.filter_preference = that.filter_preference;
       window.filter = that.filter;

        if(typeof(that.filter_preference.get('savedMemberId')) !== 'undefined'){
            var memberId = that.filter_preference.get('savedMemberId');
        }else{
            var memberId = 0;
        }

        var getUserHomePreferencesPromise = that.getUserHomePreferences();
        var getUserFamilyPreferencesPromise = that.getUserFamilyPreferences();
        var getUserMemberPreferencesPromise = that.getUserMemberPreferences(memberId);
        var getUserFamilyPreferencesPromise = that.getAllFamilyPreferences();

        Promise.all([getUserHomePreferencesPromise,getUserFamilyPreferencesPromise,getUserMemberPreferencesPromise,getUserFamilyPreferencesPromise]).then(function() {
            console.log("@@@@@@@@@@@@@ In side Promise @@@@@@@@@@@@@@@@@@");
            that.fetchUserPreferencesAndRender();
        });
    },
    getUserHomePreferences: function(){
        var that = this;
        var category = 1;
        var userId = sessionStorage.userId;
        //var userId = "2ZBLKQ4vGMRSuN7k8AH8nf7InG43";
        return new Promise(function(resolve, reject) {
            if(userId){
           that.user_home_preferences.getUserPreferences(category, userId, {
               async: true,
               crossDomain: true,
               method: "GET",
               headers:{
                   "authorization": "Bearer "+ sessionStorage.authtoken
               },
               success:function(data) {

                   console.log(" +++++++++++++++ getUserHomePreferences ++++++++++++++++++ ");
                   console.log(data);
                   resolve();
               },
               error:function(response) {
                   //console.log(" +++++++++++++++ Errrorr ++++++++++++++++++ ");
                   //console.log(response);
                   reject();
               }
           });
           }else{
                         resolve();
                      }
      });
    },
    getUserFamilyPreferences: function(){
        var that = this;
        var category = 2;
        var userId = sessionStorage.userId;
        //var userId = "2ZBLKQ4vGMRSuN7k8AH8nf7InG43";
        return new Promise(function(resolve, reject) {
           if(userId){
               that.user_family_preferences.getUserPreferences(category, userId, {
                   async: true,
                   crossDomain: true,
                   method: "GET",
                   headers:{
                       "authorization": "Bearer "+ sessionStorage.authtoken
                   },
                   success:function(data) {

                       console.log(" +++++++++++++++ getUserFamilyPreferences ++++++++++++++++++ ");
                       console.log(data);
                       resolve();
                   },
                   error:function(response) {
                       //console.log(" +++++++++++++++ Errrorr ++++++++++++++++++ ");
                       //console.log(response);
                       reject();
                   }
               });
           }else{
              resolve();
           }
      });
    },
    getUserMemberPreferences: function(memberId){
        var that = this;

        var userId = sessionStorage.userId;
        //var memberId = 0;
        //var userId = "2ZBLKQ4vGMRSuN7k8AH8nf7InG43";

        return new Promise(function(resolve, reject) {
            if(userId){
               that.user_members.getUserMembers( userId, memberId, {
                   async: true,
                   crossDomain: true,
                   method: "GET",
                   headers:{
                       "authorization": "Bearer "+ sessionStorage.authtoken
                   },
                   success:function(data) {

                       console.log(" +++++++++++++++ getUserMemberPreferences ++++++++++++++++++ ");
                       console.log(data);
                       resolve();
                   },
                   error:function(response) {
                       //console.log(" +++++++++++++++ Errrorr ++++++++++++++++++ ");
                       //console.log(response);
                       reject();
                   }
               });
           }else{
            resolve();
           }
      });

    },
    getAllFamilyPreferences: function(){
            var that = this;

            var userId = sessionStorage.userId;
            //var memberId = 0;
            //var userId = "2ZBLKQ4vGMRSuN7k8AH8nf7InG43";

            return new Promise(function(resolve, reject) {
                if(userId){
                   that.family_preference.getFamilyPreferences(userId, {
                       async: true,
                       crossDomain: true,
                       method: "GET",
                       headers:{
                           "authorization": "Bearer "+ sessionStorage.authtoken
                       },
                       success:function(data) {

                           console.log(" +++++++++++++++ getAllFamilyPreferences ++++++++++++++++++ ");
                           console.log(data);
                           resolve();
                       },
                       error:function(response) {
                           //console.log(" +++++++++++++++ Errrorr ++++++++++++++++++ ");
                           //console.log(response);
                           reject();
                       }
                   });
               }else{
                resolve();
               }
          });

        },

    fetchUserPreferencesAndRender: function(){
        var that = this;
        var user_home_preferences = that.user_home_preferences;
        var user_family_preferences = that.user_family_preferences;
        var user_members = that.user_members;
        var family_preference = that.family_preference;
        //var editfamilymembers = that.editfamilymembers;

        console.log("@@@@@@@@@@@@@@@@@@ family_preference @@@@@@@@@@@@@@@@@@2222");
        console.log(family_preference);

        console.log("@@@@@@@@@@@@@@@@@@ user_members @@@@@@@@@@@@@@@@@@2222");
        console.log(user_members);

        $(this.el).html(_.template(UserPreferenceTemplate)({
           'userHomePreferencesDtls':user_home_preferences.toJSON(),
           'userFamilyPreferencesDtls':user_family_preferences.toJSON(),
           'userMemebersPreferencesDtls':user_members.toJSON(),
           'allFamilyPreference':family_preference.toJSON()
       }));

        $("#home").html(_.template(HomePreferenceTemplate)({
           'userHomePreferencesDtls':user_home_preferences.toJSON()
       }));
       $("#family").html(_.template(FamilyPreferenceTemplate)({
           'userFamilyPreferencesDtls':user_family_preferences.toJSON()
       }));

       $("#members").html(_.template(MemberPreferenceTemplate)({
           'userMemebersPreferencesDtls':user_members.toJSON(),
            'allFamilyPreference':family_preference.toJSON()
       }));
    },
    events: {
        "click .userques": "getSelectedQuestion",
        "click #save_homeId":"saveHomePreferences",
        "click .userquesfamily":"getSelectedQuestionFamily",
        "click #save_familyId":"saveFamilyPreferences",
        "click .userMember":"getSelectedMember",
        "click #save_familyMemebr":"saveFamilyMembers",
        "click #update_familyMemebr":"updateFamilyMembers",
        "click #edit_familyMemebr":"editFamilyMembers",
        "click #delete_familyMemebr":"deleteFamilyMembers",
        "click #HomeTab":"ViewHomeTabPanel",
        "click #FamilyTab":"ViewFamilyTabPanel",
        "click #MemberTab":"ViewMemberTabPanel"

    },
    ViewHomeTabPanel:function(){

        var that = this;
        var user_home_preferences = that.user_home_preferences;

        console.log("@@@@@@@@@@@@ Inside Home Panel @@@@@@@@@@@@@@@");

        $("#home").html('');

        $("#home").html(_.template(HomePreferenceTemplate)({
           'userHomePreferencesDtls':user_home_preferences.toJSON()
       }));
    },
    ViewFamilyTabPanel:function(){

        var that = this;
        var user_family_preferences = that.user_family_preferences;

        console.log("@@@@@@@@@@@@ Inside Family Panel @@@@@@@@@@@@@@@");

        $("#family").html('');
        $("#family").html(_.template(FamilyPreferenceTemplate)({
           'userFamilyPreferencesDtls':user_family_preferences.toJSON()
       }));
    },
    ViewMemberTabPanel:function(){

        var that = this;
        var user_members = that.user_members;
        var family_preference = that.family_preference;

        console.log("@@@@@@@@@@@@ Inside Member Panel @@@@@@@@@@@@@@@");

        $("#members").html('');
        $("#members").html(_.template(MemberPreferenceTemplate)({
           'userMemebersPreferencesDtls':user_members.toJSON(),
            'allFamilyPreference':family_preference.toJSON()
       }));
    },
    editFamilyMembers: function(evt){
              var that = this;
              evt.preventDefault();
              var currentTarget = $(evt.currentTarget);
              var userId = sessionStorage.userId;
              var memberId = currentTarget.data('element');
             that.editfamilymembers.editFamilyMembers(userId,memberId, {
                 async: true,
                 crossDomain: true,
                 method: "GET",
                 headers:{
                     "authorization": "Bearer "+ sessionStorage.authtoken
                 },
                 success:function(data) {

                     console.log(" +++++++++++++++ editFamilyMembers ++++++++++++++++++ ");
                     console.log(data);
                      $('#editMember-modal').modal('show');
                     that.fetchEditAndRender();
                 },
                 error:function(response) {
                     //console.log(" +++++++++++++++ Errrorr ++++++++++++++++++ ");
                     //console.log(response);
                 }
             });

    },
    deleteFamilyMembers: function(evt){
         var that = this;
         if(confirm("Are you sure you want to delete this project ?") == false){
                         return false;
                 }
         evt.preventDefault();
         var currentTarget = $(evt.currentTarget);
         var memberId = currentTarget.data('element');
         that.deletefamilymembers.deleteMembers(memberId,{
             async: true,
             crossDomain: true,
             method: "POST",
             headers:{
                 "authorization": "Bearer "+ sessionStorage.authtoken
             },
             success:function(data) {

                 console.log(" +++++++++++++++ deleteFamilyMembers ++++++++++++++++++ ");
                 console.log(data);
                 $("#snackbar").html("Successfully removed this member selection... ");
                 var x = document.getElementById("snackbar")
                 x.className = "show";
                 setTimeout(function(){ x.className = x.className.replace("show", ""); }, 3000);
                 that.render();
             },
             error:function(response) {
                 console.log(" +++++++++++++++  deleteFamilyMembers- Errrorr ++++++++++++++++++ ");
                 console.log(JSON.stringify(response));
             }
         });

    },
    fetchEditAndRender: function(){
         var that = this;
         var editfamilymembers = that.editfamilymembers;

         $('#editMember-dtls').html(_.template(editMemberPageTemplate)({
             "editfamilymembers": editfamilymembers.toJSON()
         }));

        },
    saveHomePreferences:function(e){
        var that = this;


        if (e.isDefaultPrevented()) return;
                e.preventDefault();
                 //var userId = "2ZBLKQ4vGMRSuN7k8AH8nf7InG43";
                 var userId = sessionStorage.userId;
                 var formData = that.filter_preference.get("selectedHomePreference");

                var category = 1;
                console.log("++++++++++++++++++++++++++ formData ++++++++++++++++++++++++++++++");
                console.log(JSON.stringify(formData));


                that.saveuser_preferences.saveUserPreferences(category,userId,{
                   async: true,
                   crossDomain: true,
                   method: "POST",
                   headers:{
                       "authorization": "Bearer "+ sessionStorage.authtoken,
                       "Content-Type": "application/json"
                   },
                   data: JSON.stringify(formData),
                   success:function(response) {
                      console.log("Successfully saved Home Preferences");
                      console.log(response);

                      $("#snackbar").html("Successfully saved Home Preferences");
                      var x = document.getElementById("snackbar")
                      x.className = "show";
                      setTimeout(function(){ x.className = x.className.replace("show", ""); }, 3000);
                      that.render();
                      return;

                  },
                  error:function(model, response, options) {

                      console.log(" +++++++++++++++ Home Preferences save- Errrorr ++++++++++++++++++ ");
                      console.log(JSON.stringify(response));


                       $("#snackbar").html(response.responseJSON.errorMessage);
                       var x = document.getElementById("snackbar")
                       x.className = "show";
                       setTimeout(function(){ x.className = x.className.replace("show", ""); }, 3000);
                       return;

                  }
              });
    },
    getSelectedMember: function(evt){
        evt.preventDefault();

        var that = this;




        var currentTarget = $(evt.currentTarget);
        var quesId = currentTarget.data('element');
        var quesOptionid = currentTarget.data('element1');
        var quesAns = currentTarget.data('element2');

        var age_group = [{
            'optionId':quesOptionid,
            'optionType':1,
            'quesAnsId':0,
            'quesId':quesId,
            'selectionStatus':1,
            'userOptionDesc':""
        }];


        that.filter_preference.set({
            'selectedAgegroup':age_group
        }, {
            silent: true
        });

        that.filter_preference.set({
            'selectedAge':quesAns
        }, {
            silent: true
        });


        console.log("@@@@@@@@@@@@ Age Group @@@@@@@@@@@@@@@");
        console.log(quesAns);
        console.log(that.filter_preference.get("selectedAgegroup"));

        return false;

    },
    getSelectedQuestion: function(evt){
        evt.preventDefault();

        var that = this;

        var currentTarget = $(evt.currentTarget);
        var quesId = currentTarget.data('element');
        var quesOptionid = currentTarget.data('element1');
        var quesName = currentTarget.data('element2');

        var interiors_budget = {};
        var dwelling_type = {};
        var room = {};
        var size = {};
        var usage = {};

        if(typeof(quesName) !== 'undefined' && quesName == "Interiors Budget"){
            $(".Interiors").css('background-color','');
            $(".Interiors").css('color','#000');
            $("#"+quesOptionid).css('background-color','#634e42');
            $("#"+quesOptionid).css('color','#fff');

            interiors_budget['optionId'] = quesOptionid;
            interiors_budget['optionType'] = 1;
            interiors_budget['quesAnsId'] = quesOptionid;
            interiors_budget['quesId'] = quesId;
            interiors_budget['selectionStatus'] = 1;
            interiors_budget['userOptionDesc'] = "";


            that.filter_preference.set({
                'selectedInteriorBudget':interiors_budget
            }, {
                silent: true
            });

        }

         if(typeof(quesName) !== 'undefined' && quesName == "Dwelling type"){
            $(".Dwelling").css('background-color','');
            $(".Dwelling").css('color','#000');
            $("#"+quesOptionid).css('background-color','#634e42');
            $("#"+quesOptionid).css('color','#fff');

            dwelling_type['optionId'] = quesOptionid;
            dwelling_type['optionType'] = 1;
            dwelling_type['quesAnsId'] = quesOptionid;
            dwelling_type['quesId'] = quesId;
            dwelling_type['selectionStatus'] = 1;
            dwelling_type['userOptionDesc'] = "";

            that.filter_preference.set({
                'selectedDwellingType':dwelling_type
            }, {
                silent: true
            });

        }

        if(typeof(quesName) !== 'undefined' && quesName == "Room"){
            $(".Room").css('background-color','');
            $(".Room").css('color','#000');
            $("#"+quesOptionid).css('background-color','#634e42');
            $("#"+quesOptionid).css('color','#fff');

            room['optionId'] = quesOptionid;
            room['optionType'] = 1;
            room['quesAnsId'] = quesOptionid;
            room['quesId'] = quesId;
            room['selectionStatus'] = 1;
            room['userOptionDesc'] = "";

            that.filter_preference.set({
                'selectedRoom':room
            }, {
                silent: true
            });

        }

        if(typeof(quesName) !== 'undefined' && quesName == "Size"){
            $(".Size").css('background-color','');
            $(".Size").css('color','#000');
            $("#"+quesOptionid).css('background-color','#634e42');
            $("#"+quesOptionid).css('color','#fff');

            size['optionId'] = quesOptionid;
            size['optionType'] = 1;
            size['quesAnsId'] = quesOptionid;
            size['quesId'] = quesId;
            size['selectionStatus'] = 1;
            size['userOptionDesc'] = "";

            that.filter_preference.set({
                'selectedSize':size
            }, {
                silent: true
            });

        }

        if(typeof(quesName) !== 'undefined' && quesName == "Usage"){
            $(".Usage").css('background-color','');
            $("#"+quesOptionid).css('background-color','#634e42');
            $("#"+quesOptionid).css('color','#fff');

            usage['optionId'] = quesOptionid;
            usage['optionType'] = 1;
            usage['quesAnsId'] = quesOptionid;
            usage['quesId'] = quesId;
            usage['selectionStatus'] = 1;
            usage['userOptionDesc'] = "";

            that.filter_preference.set({
                'selectedUsage':usage
            }, {
                silent: true
            });

        }

        console.log(" +++++++++++++++ quesId ++++++++++++++++++ ");
        console.log(quesName);
        console.log(quesId+"======================"+quesOptionid);

        console.log("@@@@@@@@@@@@@@@@ interiors_budget @@@@@@@@@@@@@@@@@");
        console.log(that.filter_preference.get("selectedInteriorBudget"));

        console.log("@@@@@@@@@@@@@@ dwelling_type @@@@@@@@@@@@@@@@@@@");
        console.log(that.filter_preference.get("selectedDwellingType"));

        console.log("@@@@@@@@@@@@@@ room @@@@@@@@@@@@@@@@@@@");
        console.log(that.filter_preference.get("selectedRoom"));

        console.log("@@@@@@@@@@@@@@ Size @@@@@@@@@@@@@@@@@@@");
        console.log(that.filter_preference.get("selectedSize"));

        console.log("@@@@@@@@@@@@@@ Usage @@@@@@@@@@@@@@@@@@@");
        console.log(that.filter_preference.get("selectedUsage"));


        var  user_home_preferences = that.user_home_preferences;
        user_home_preferences = user_home_preferences.toJSON();


         console.log("@@@@@@@@@@@@@@ user_home_preferences @@@@@@@@@@@@@@@@@@@");
         console.log(user_home_preferences);

         _.each(user_home_preferences[0].userQues, function(userQuesdtl, j){
                if(j == 0){
                    if(typeof(that.filter_preference.get("selectedDwellingType")) !== 'undefined'){
                        userQuesdtl.userSelectedOption[0] = that.filter_preference.get("selectedDwellingType");
                    }
                }
                if(j == 1){
                    if(typeof(that.filter_preference.get("selectedUsage")) !== 'undefined'){
                        userQuesdtl.userSelectedOption[0] = that.filter_preference.get("selectedUsage");
                    }

                }
                if(j == 2){
                    if(typeof(that.filter_preference.get("selectedInteriorBudget")) !== 'undefined'){
                        userQuesdtl.userSelectedOption[0] = that.filter_preference.get("selectedInteriorBudget");
                    }
                }
                if(j == 3){
                    if(typeof(that.filter_preference.get("selectedSize")) !== 'undefined'){
                        userQuesdtl.userSelectedOption[0] = that.filter_preference.get("selectedSize");
                    }
                }
                if(j == 4){
                    if(typeof(that.filter_preference.get("selectedRoom")) !== 'undefined'){
                        userQuesdtl.userSelectedOption[0]= that.filter_preference.get("selectedRoom");
                    }
                }
         });

         var user_homepref_for_saving = {};
         user_homepref_for_saving = user_home_preferences[0].userQues;

         console.log("@@@@@@@@@@@@@@ Final user_home_preferences object to save @@@@@@@@@@@@@@@@@@@");
         console.log(user_home_preferences);

          that.filter_preference.set({
                         'selectedHomePreference':user_homepref_for_saving
                     }, {
                         silent: true
                     });

         that.filter_preference.set({
             'selectedFinalObj':user_home_preferences
         }, {
             silent: true
         });

        return false;
    },

    saveFamilyPreferences:function(e){
            var that = this;


            if (e.isDefaultPrevented()) return;
                    e.preventDefault();
                     //var userId = "2ZBLKQ4vGMRSuN7k8AH8nf7InG43";
                     var userId = sessionStorage.userId;
                     var formData = that.filter_preference.get("selectedFamilyPreference");

                    var category = 1;
                    console.log("++++++++++++++++++++++++++family  formData ++++++++++++++++++++++++++++++");
                    console.log(JSON.stringify(formData));


                    that.saveuser_preferences.saveUserPreferences(category,userId,{
                       async: true,
                       crossDomain: true,
                       method: "POST",
                       headers:{
                           "authorization": "Bearer "+ sessionStorage.authtoken,
                           "Content-Type": "application/json"
                       },
                       data: JSON.stringify(formData),
                       success:function(response) {
                          console.log("Successfully saved Family Preferences");
                          console.log(response);

                          $("#snackbar").html("Successfully saved Family Preferences");
                          var x = document.getElementById("snackbar")
                          x.className = "show";
                          setTimeout(function(){ x.className = x.className.replace("show", ""); }, 3000);
                          that.render();
                          return;

                      },
                      error:function(model, response, options) {

                          console.log(" +++++++++++++++ Home Preferences save- Errrorr ++++++++++++++++++ ");
                          console.log(JSON.stringify(response));


                           $("#snackbar").html(response.responseJSON.errorMessage);
                           var x = document.getElementById("snackbar")
                           x.className = "show";
                           setTimeout(function(){ x.className = x.className.replace("show", ""); }, 3000);
                           return;

                      }
                  });
        },
    getSelectedQuestionFamily: function(evt){
            evt.preventDefault();
            var filterSelectedImage = {};
            var weAllImage={};
            var that = this;



            var currentTarget = $(evt.currentTarget);
            var quesId = currentTarget.data('element');
            var quesOptionid = currentTarget.data('element1');
            var quesName = currentTarget.data('element2');




            var adults = {};
            var teenagers  = {};
            var kids = {};
            var seniorCitizens = {};
            var weWouldLikeOurHomeToBe = {};
            var weLoveTo={};
            var weUsuallyDo={};
            var arrr ={};

           if (typeof(that.filter.get('selectedArr')) != 'undefined') {
                filterSelectedImage = that.filter.get('selectedArr');
                 that.filter_preference.set({
                    'selectedWeWouldLikeOurHomeToBe':filterSelectedImage
                    }, {
                        silent: true
                    });
           }

          if (typeof(that.filter.get('selectedweusallyDo')) != 'undefined') {
              weUsuallyDo = that.filter.get('selectedweusallyDo');
               that.filter_preference.set({
                    'selectedWeUsuallyDo':weUsuallyDo
                }, {
                    silent: true
                });

          }

         if (typeof(that.filter.get('selectedweLoveTO')) != 'undefined') {
             weLoveTo = that.filter.get('selectedweLoveTO');
             that.filter_preference.set({
                   'selectedWeLoveTo':weLoveTo
               }, {
                   silent: true
               });

         }

            if(typeof(quesName) !== 'undefined' && quesName == "Adults"){

                $(".Adults").css('background-color','');
                $(".Adults").css('color','#000');
                $("#butt"+quesOptionid).css('background-color','#634e42');
                $("#butt"+quesOptionid).css('color','#fff');

                adults['optionId'] = quesOptionid;
                adults['optionType'] = 2;
                adults['quesAnsId'] = quesOptionid;
                adults['quesId'] = quesId;
                adults['selectionStatus'] = 1;
                adults['userOptionDesc'] = "";


                that.filter_preference.set({
                    'selectedAdults':adults
                }, {
                    silent: true
                });

            }

             if(typeof(quesName) !== 'undefined' && quesName == "Teenagers / Young Adults"){

                $(".Teenagers").css('background-color','');
                $(".Teenagers").css('color','#000');
                $("#butt"+quesOptionid).css('background-color','#634e42');
                $("#butt"+quesOptionid).css('color','#fff');

                teenagers['optionId'] = quesOptionid;
                teenagers['optionType'] = 2;
                teenagers['quesAnsId'] = quesOptionid;
                teenagers['quesId'] = quesId;
                teenagers['selectionStatus'] = 1;
                teenagers['userOptionDesc'] = "";

                that.filter_preference.set({
                    'selectedTeenagers':teenagers
                }, {
                    silent: true
                });

            }

            if(typeof(quesName) !== 'undefined' && quesName == "Kids"){
                $(".Kids").css('background-color','');
                $(".Kids").css('color','#000');
                $("#butt"+quesOptionid).css('background-color','#634e42');
                $("#butt"+quesOptionid).css('color','#fff');
                kids['optionId'] = quesOptionid;
                kids['optionType'] = 2;
                kids['quesAnsId'] = quesOptionid;
                kids['quesId'] = quesId;
                kids['selectionStatus'] = 1;
                kids['userOptionDesc'] = "";

                that.filter_preference.set({
                    'selectedKids':kids
                }, {
                    silent: true
                });

            }

            if(typeof(quesName) !== 'undefined' && quesName == "Senior Citizens"){
                $(".Senior").css('background-color','');
                $(".Senior").css('color','#000');
                $("#butt"+quesOptionid).css('background-color','#634e42');
                $("#butt"+quesOptionid).css('color','#fff');
                seniorCitizens['optionId'] = quesOptionid;
                seniorCitizens['optionType'] = 2;
                seniorCitizens['quesAnsId'] = quesOptionid;
                seniorCitizens['quesId'] = quesId;
                seniorCitizens['selectionStatus'] = 1;
                seniorCitizens['userOptionDesc'] = "";

                that.filter_preference.set({
                    'selectedSeniorCitizens':seniorCitizens
                }, {
                    silent: true
                });

            }



             /*if(typeof(quesName) !== 'undefined' && quesName == "We love to"){
                weLoveTo['optionId'] = quesOptionid;
                weLoveTo['optionType'] = 2;
                weLoveTo['quesAnsId'] = quesOptionid;
                weLoveTo['quesId'] = quesId;
                weLoveTo['selectionStatus'] = 1;
                weLoveTo['userOptionDesc'] = "";

                that.filter_preference.set({
                    'selectedWeLoveTo':weLoveTo
                }, {
                    silent: true
                });

             }
             if(typeof(quesName) !== 'undefined' && quesName == "We usually do"){
                  weUsuallyDo['optionId'] = quesOptionid;
                  weUsuallyDo['optionType'] = 2;
                  weUsuallyDo['quesAnsId'] = quesOptionid;
                  weUsuallyDo['quesId'] = quesId;
                  weUsuallyDo['selectionStatus'] = 1;
                  weUsuallyDo['userOptionDesc'] = "";

                  that.filter_preference.set({
                      'selectedWeUsuallyDo':weUsuallyDo
                  }, {
                      silent: true
                  });

             }*/

            console.log(" +++++++++++++++ quesId ++++++++++++++++++ ");
            console.log(quesName);
            console.log(quesId+"======================"+quesOptionid);

            console.log("@@@@@@@@@@@@@@@@ Adult @@@@@@@@@@@@@@@@@");
            console.log(that.filter_preference.get("selectedAdults"));

            console.log("@@@@@@@@@@@@@@ Teenagers @@@@@@@@@@@@@@@@@@@");
            console.log(that.filter_preference.get("selectedTeenagers"));

            console.log("@@@@@@@@@@@@@@ Kids @@@@@@@@@@@@@@@@@@@");
            console.log(that.filter_preference.get("selectedKids"));

            console.log("@@@@@@@@@@@@@@ SeniorCitizens @@@@@@@@@@@@@@@@@@@");
            console.log(that.filter_preference.get("selectedSeniorCitizens"));

            console.log("@@@@@@@@@@@@@@ We Would Like Our Home To Be @@@@@@@@@@@@@@@@@@@");
            console.log(that.filter_preference.get("selectedWeWouldLikeOurHomeToBe"));

            console.log("@@@@@@@@@@@@@@ We Love To @@@@@@@@@@@@@@@@@@@");
            console.log(that.filter_preference.get("selectedWeLoveTo"));

            console.log("@@@@@@@@@@@@@@ We Usually Do @@@@@@@@@@@@@@@@@@@");
            console.log(that.filter_preference.get("selectedWeUsuallyDo"));


            var  user_family_preferences = that.user_family_preferences;
            user_family_preferences = user_family_preferences.toJSON();

             console.log("@@@@@@@@@@@@@@ user_family_preferences @@@@@@@@@@@@@@@@@@@");
             console.log(user_family_preferences);

             _.each(user_family_preferences[0].userQues, function(userQuesdtl, j){
                    if(j == 0){
                        if(typeof(that.filter_preference.get("selectedAdults")) !== 'undefined'){
                            userQuesdtl.userSelectedOption[0] = that.filter_preference.get("selectedAdults");
                        }
                    }
                    if(j == 1){
                        if(typeof(that.filter_preference.get("selectedTeenagers")) !== 'undefined'){
                            userQuesdtl.userSelectedOption[0] = that.filter_preference.get("selectedTeenagers");
                        }
                    }
                    if(j == 2){
                        if(typeof(that.filter_preference.get("selectedKids")) !== 'undefined'){
                            userQuesdtl.userSelectedOption[0]= that.filter_preference.get("selectedKids");
                        }
                    }
                    if(j == 3){
                        if(typeof(that.filter_preference.get("selectedSeniorCitizens")) !== 'undefined'){
                            userQuesdtl.userSelectedOption[0] = that.filter_preference.get("selectedSeniorCitizens");
                        }
                    }
                    if(j == 4){
                        if(typeof(that.filter_preference.get("selectedWeWouldLikeOurHomeToBe")) !== 'undefined'){
                            userQuesdtl.userSelectedOption = that.filter_preference.get("selectedWeWouldLikeOurHomeToBe");
                        }
                    }
                     if(j == 5){
                        if(typeof(that.filter_preference.get("selectedWeLoveTo")) !== 'undefined'){
                            userQuesdtl.userSelectedOption = that.filter_preference.get("selectedWeLoveTo");
                        }
                    }
                    if(j == 6){
                         if(typeof(that.filter_preference.get("selectedWeUsuallyDo")) !== 'undefined'){
                             userQuesdtl.userSelectedOption = that.filter_preference.get("selectedWeUsuallyDo");
                         }
                     }

             });

             var user_familypref_for_saving = {};
             user_familypref_for_saving = user_family_preferences[0].userQues;

             console.log("_____________Tushar LINE______________");
             console.log(user_familypref_for_saving);

             console.log("@@@@@@@@@@@@@@ Final user_family_preferences object to save @@@@@@@@@@@@@@@@@@@");
             console.log(user_family_preferences);

              that.filter_preference.set({
                             'selectedFamilyPreference':user_familypref_for_saving
                         }, {
                             silent: true
                         });

             that.filter_preference.set({
                 'selectedFinalObjFamily':user_family_preferences
             }, {
                 silent: true
             });

            return false;
        },
    saveFamilyMembers:function(e){
        var that = this;
        if (e.isDefaultPrevented()) return;

        var name = $("#inputname").val();
        var gender = $("input[name='gender']:checked").val();

         var memberId = $("#memberId").val();
        var workcategory = $("#inputcategory").val();


        var workoccupation = $("#inputoccupation").val();
        var workoccupationquesId = $("#inputoccupation").find(':selected').data('element');
        var workoccupationquesOptId = $("#inputoccupation").find(':selected').data('element1');

        var wobj=[{
           "optionId": workoccupationquesOptId,
           "optionType": 1,
           "quesAnsId": 0,
           "quesId": workoccupationquesId,
           "selectionStatus": 1,
           "userOptionDesc": ""
       }];

        var hobby1category = $("#inputhobby1category").val();
        var hobby1 = $("#inputhobby1").val();
        var hobby1quesId = $("#inputhobby1").find(':selected').data('element');
        var hobby1quesOptId = $("#inputhobby1").find(':selected').data('element1');

        var hobby2category = $("#inputhobby2category").val();
        var hobby2 = $("#inputhobby2").val();
        var hobby2quesId = $("#inputhobby2").find(':selected').data('element');
        var hobby2quesOptId = $("#inputhobby2").find(':selected').data('element1');

        var hobby3category = $("#inputhobby3category").val();
        var hobby3 = $("#inputhobby3").val();
        var hobby3quesId = $("#inputhobby3").find(':selected').data('element');
        var hobby3quesOptId = $("#inputhobby3").find(':selected').data('element1');

        var hobbiesArrObj = [
            {
              "optionId": hobby1quesOptId,
              "optionType": 1,
              "quesAnsId": 0,
              "quesId": hobby1quesId,
              "selectionStatus": 1,
              "userOptionDesc": ""
            },
            {
              "optionId": hobby2quesOptId,
              "optionType": 1,
              "quesAnsId": 0,
              "quesId": hobby2quesId,
              "selectionStatus": 1,
              "userOptionDesc": ""
            },
            {
              "optionId": hobby3quesOptId,
              "optionType": 1,
              "quesAnsId": 0,
              "quesId": hobby3quesId,
              "selectionStatus": 1,
              "userOptionDesc": ""
            }
          ];

        console.log(" @@@@@@@@@@@@@@ hobbiesArrObj @@@@@@@@@@@@@@@@@@@@@@ ");
        console.log(hobbiesArrObj);


        console.log(" @@@@@@@@@@@@@@ selectedAgegroup @@@@@@@@@@@@@@@@@@@@@@ ");
        console.log(that.filter_preference.get("selectedAgegroup"));


        var familyper =
        {
            "age": that.filter_preference.get("selectedAge"),
            "gender": gender,
            "imageUrl": "",
            "memberId": 0,
            "memberName": name
          };


        var  user_members = that.user_members;
        user_members = user_members.toJSON();

        console.log("@@@@@@@@@@@@@@ user_members @@@@@@@@@@@@@@@@@@@");
        console.log(user_members);

        if(typeof(user_members) !== 'undefined'){
            user_members[0].userQuesAns.familyPerson = familyper;

            _.each(user_members[0].userQuesAns.memberQuestionnaireList, function(userQuesdtl, j){
                if(userQuesdtl.name == "Occupation"){
                    userQuesdtl.memberSelectedAnswer = wobj;
                }

                if(userQuesdtl.name == "hobbies"){
                    userQuesdtl.memberSelectedAnswer = hobbiesArrObj;
                }

                if(userQuesdtl.name == "age"){
                    userQuesdtl.memberSelectedAnswer = that.filter_preference.get("selectedAgegroup");
                }
            });


            console.log("@@@@@@@@@@@@@@ Final user_members data for saving@@@@@@@@@@@@@@@@@@@");


            var formData = user_members[0].userQuesAns;
            console.log(JSON.stringify(formData));

            var userId = sessionStorage.userId;
            that.saveuser_members.saveUserMembers(userId,{
               async: true,
               crossDomain: true,
               method: "POST",
               headers:{
                   "authorization": "Bearer "+ sessionStorage.authtoken,
                   "Content-Type": "application/json"
               },
               data: JSON.stringify(formData),
               success:function(response) {
                  console.log("Successfully saved Family Members");
                  console.log(response);

                  var memberList = response.toJSON();

                  var smemberId = memberList[0].memberId;

                  that.filter_preference.set({
                       'savedMemberId':smemberId
                   }, {
                       silent: true
                   });

                  $("#addMember-modal").modal('hide');

                  /*$("#snackbar").html("Successfully saved Family Members");
                  var x = document.getElementById("snackbar")
                  x.className = "show";
                  setTimeout(function(){ x.className = x.className.replace("show", ""); }, 3000);
                  that.render();
                  return;
*/
              },
              error:function(model, response, options) {

                  console.log(" +++++++++++++++ User Members save- Errrorr ++++++++++++++++++ ");
                  console.log(JSON.stringify(response));

                  $("#addMember-modal").modal('hide');


                  /* $("#snackbar").html(response.responseJSON.errorMessage);
                   var x = document.getElementById("snackbar")
                   x.className = "show";
                   setTimeout(function(){ x.className = x.className.replace("show", ""); }, 3000);
                   return;*/

              }
          });

        }
        //return;

    },
    updateFamilyMembers:function(e){
            var that = this;
            if (e.isDefaultPrevented()) return;

            var name = $("#edit_inputname").val();
            var gender = $("input[name='edit_gender']:checked").val();

             var memberId = $("#edit_memberId").val();
            var workcategory = $("#edit_inputcategory").val();


            var workoccupation = $("#edit_inputoccupation").val();
            var workoccupationquesId = $("#edit_inputoccupation").find(':selected').data('element');
            var workoccupationquesOptId = $("#edit_inputoccupation").find(':selected').data('element1');

            var wobj=[{
               "optionId": workoccupationquesOptId,
               "optionType": 1,
               "quesAnsId": 0,
               "quesId": workoccupationquesId,
               "selectionStatus": 1,
               "userOptionDesc": ""
           }];

            var hobby1category = $("#edit_inputhobby1category").val();
            var hobby1 = $("#edit_inputhobby1").val();
            var hobby1quesId = $("#edit_inputhobby1").find(':selected').data('element');
            var hobby1quesOptId = $("#edit_inputhobby1").find(':selected').data('element1');

            var hobby2category = $("#edit_inputhobby2category").val();
            var hobby2 = $("#edit_inputhobby2").val();
            var hobby2quesId = $("#edit_inputhobby2").find(':selected').data('element');
            var hobby2quesOptId = $("#edit_inputhobby2").find(':selected').data('element1');

            var hobby3category = $("#edit_inputhobby3category").val();
            var hobby3 = $("#edit_inputhobby3").val();
            var hobby3quesId = $("#edit_inputhobby3").find(':selected').data('element');
            var hobby3quesOptId = $("#edit_inputhobby3").find(':selected').data('element1');

            var hobbiesArrObj = [
                {
                  "optionId": hobby1quesOptId,
                  "optionType": 1,
                  "quesAnsId": 0,
                  "quesId": hobby1quesId,
                  "selectionStatus": 1,
                  "userOptionDesc": ""
                },
                {
                  "optionId": hobby2quesOptId,
                  "optionType": 1,
                  "quesAnsId": 0,
                  "quesId": hobby2quesId,
                  "selectionStatus": 1,
                  "userOptionDesc": ""
                },
                {
                  "optionId": hobby3quesOptId,
                  "optionType": 1,
                  "quesAnsId": 0,
                  "quesId": hobby3quesId,
                  "selectionStatus": 1,
                  "userOptionDesc": ""
                }
              ];

            console.log(" @@@@@@@@@@@@@@ hobbiesArrObj @@@@@@@@@@@@@@@@@@@@@@ ");
            console.log(hobbiesArrObj);


            console.log(" @@@@@@@@@@@@@@ selectedAgegroup @@@@@@@@@@@@@@@@@@@@@@ ");
            console.log(that.filter_preference.get("selectedAgegroup"));


            var familyper =
            {
                "age": that.filter_preference.get("selectedAge"),
                "gender": parseInt(gender),
                "imageUrl": "",
                "memberId": parseInt(memberId),
                "memberName": name
              };


            var  user_members = that.user_members;
            user_members = user_members.toJSON();

            console.log("@@@@@@@@@@@@@@ user_members @@@@@@@@@@@@@@@@@@@");
            console.log(user_members);

            if(typeof(user_members) !== 'undefined'){
                user_members[0].userQuesAns.familyPerson = familyper;

                _.each(user_members[0].userQuesAns.memberQuestionnaireList, function(userQuesdtl, j){
                    if(userQuesdtl.name == "Occupation"){
                        userQuesdtl.memberSelectedAnswer = wobj;
                    }

                    if(userQuesdtl.name == "hobbies"){
                        userQuesdtl.memberSelectedAnswer = hobbiesArrObj;
                    }

                    if(userQuesdtl.name == "age"){
                        userQuesdtl.memberSelectedAnswer = that.filter_preference.get("selectedAgegroup");
                    }
                });


                console.log("@@@@@@@@@@@@@@ Final user_members data for saving@@@@@@@@@@@@@@@@@@@");


                var formData = user_members[0].userQuesAns;
                console.log(JSON.stringify(formData));

                var userId = sessionStorage.userId;
                that.saveuser_members.saveUserMembers(userId,{
                   async: true,
                   crossDomain: true,
                   method: "POST",
                   headers:{
                       "authorization": "Bearer "+ sessionStorage.authtoken,
                       "Content-Type": "application/json"
                   },
                   data: JSON.stringify(formData),
                   success:function(response) {
                      console.log("Successfully saved Family Members");
                      console.log(response);

                      var memberList = response.toJSON();

                      var smemberId = memberList[0].memberId;

                      that.filter_preference.set({
                           'savedMemberId':smemberId
                       }, {
                           silent: true
                       });

                      $("#addMember-modal").modal('hide');

                      /*$("#snackbar").html("Successfully saved Family Members");
                      var x = document.getElementById("snackbar")
                      x.className = "show";
                      setTimeout(function(){ x.className = x.className.replace("show", ""); }, 3000);
                      that.render();
                      return;
    */
                  },
                  error:function(model, response, options) {

                      console.log(" +++++++++++++++ User Members save- Errrorr ++++++++++++++++++ ");
                      console.log(JSON.stringify(response));

                      $("#addMember-modal").modal('hide');


                      /* $("#snackbar").html(response.responseJSON.errorMessage);
                       var x = document.getElementById("snackbar")
                       x.className = "show";
                       setTimeout(function(){ x.className = x.className.replace("show", ""); }, 3000);
                       return;*/

                  }
              });

            }
            //return;

        }
  });
  return UserProfilePage;
});
