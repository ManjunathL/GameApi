define([
  'jquery',
  'lodash',
  'backbone',
  'bootstrap',
  'text!templates/my_account/user_preference.html',
  'collections/user_preferences',
  'collections/family_preferences',
  'collections/saveuser_preferences',
  'models/filter_preference',
  'models/filter'
], function($, _, Backbone, Bootstrap, UserProfileTemplate,UserPreferences,FamilyPreferences,SaveUserPreferences,FilterPreference,Filter){
  var UserProfilePage = Backbone.View.extend({
    el: '.page',
    user_home_preferences: null,
    user_family_preferences: null,
    user_members_preferences: null,
    saveuser_preferences:null,
    filter_preference: null,
    filter: null,
    initialize: function() {
        this.filter_preference = new FilterPreference();
        this.user_home_preferences = new UserPreferences();
        this.user_family_preferences = new UserPreferences();
        this.user_members_preferences = new FamilyPreferences();
        this.saveuser_preferences = new SaveUserPreferences();
        this.filter = new Filter();
        this.filter_preference.on('change', this.render, this);
        this.filter.on('change', 'getSelectedQuestionFamily', this);
        this.listenTo(Backbone);
        _.bindAll(this, 'render');
    },
    render: function () {
       var that = this;

       window.filter_preference = that.filter_preference;
       window.filter = that.filter;




        var getUserHomePreferencesPromise = that.getUserHomePreferences();
        var getUserFamilyPreferencesPromise = that.getUserFamilyPreferences();
        var getUserMemberPreferencesPromise = that.getUserMemberPreferences();

        Promise.all([getUserHomePreferencesPromise,getUserFamilyPreferencesPromise,getUserMemberPreferencesPromise]).then(function() {
            console.log("@@@@@@@@@@@@@ In side Promise @@@@@@@@@@@@@@@@@@");
            that.fetchUserPreferencesAndRender();
        });
    },
    getUserHomePreferences: function(){
        var that = this;
        var category = 1;
        //var userId = sessionStorage.userId;
        var userId = "2ZBLKQ4vGMRSuN7k8AH8nf7InG43";
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
        //var userId = sessionStorage.userId;
        var userId = "2ZBLKQ4vGMRSuN7k8AH8nf7InG43";
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
    getUserMemberPreferences: function(){
        var that = this;

        //var userId = sessionStorage.userId;
        var userId = "2ZBLKQ4vGMRSuN7k8AH8nf7InG43";

        return new Promise(function(resolve, reject) {
            if(userId){
               that.user_members_preferences.getFamilyPreferences( userId, {
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
    fetchUserPreferencesAndRender: function(){
        var that = this;
        var user_home_preferences = that.user_home_preferences;
        var user_family_preferences = that.user_family_preferences;
        var user_members_preferences = that.user_members_preferences;


        $(this.el).html(_.template(UserProfileTemplate)({
           'userHomePreferencesDtls':user_home_preferences.toJSON(),
           'userFamilyPreferencesDtls':user_family_preferences.toJSON(),
           'userMemebersPreferencesDtls':user_members_preferences.toJSON()
       }));
    },
    events: {
        "click .userques": "getSelectedQuestion",
        "click #save_homeId":"saveHomePreferences",
        "click .userquesfamily":"getSelectedQuestionFamily",
        "click #save_familyId":"saveFamilyPreferences"

    },
    saveHomePreferences:function(e){
        var that = this;


        if (e.isDefaultPrevented()) return;
                e.preventDefault();
                 var userId = "2ZBLKQ4vGMRSuN7k8AH8nf7InG43";
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

            interiors_budget['optionId'] = quesOptionid;
            interiors_budget['optionType'] = 1;
            interiors_budget['quesAnsId'] = quesOptionid;
            interiors_budget['quesId'] = quesId;
            interiors_budget['selectionStatus'] = 1;
            interiors_budget['userOptionDesc'] = "Added by shilpa";


            that.filter_preference.set({
                'selectedInteriorBudget':interiors_budget
            }, {
                silent: true
            });

        }

         if(typeof(quesName) !== 'undefined' && quesName == "Dwelling type"){

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


        var  user_home_preferences = that.user_family_preferences;
        user_home_preferences = user_home_preferences.toJSON();

         console.log("@@@@@@@@@@@@@@ user_home_preferences @@@@@@@@@@@@@@@@@@@");
         console.log(user_home_preferences);

         _.each(user_home_preferences[0].userQues, function(userQuesdtl, j){
                if(j == 0){
                    if(typeof(that.filter_preference.get("selectedInteriorBudget")) !== 'undefined'){
                        userQuesdtl.userSelectedOption[0] = that.filter_preference.get("selectedInteriorBudget");
                    }
                }
                if(j == 1){
                    if(typeof(that.filter_preference.get("selectedDwellingType")) !== 'undefined'){
                        userQuesdtl.userSelectedOption[0] = that.filter_preference.get("selectedDwellingType");
                    }
                }
                if(j == 2){
                    if(typeof(that.filter_preference.get("selectedRoom")) !== 'undefined'){
                        userQuesdtl.userSelectedOption[0]= that.filter_preference.get("selectedRoom");
                    }
                }
                if(j == 3){
                    if(typeof(that.filter_preference.get("selectedSize")) !== 'undefined'){
                        userQuesdtl.userSelectedOption[0] = that.filter_preference.get("selectedSize");
                    }
                }
                if(j == 4){
                    if(typeof(that.filter_preference.get("selectedUsage")) !== 'undefined'){
                        userQuesdtl.userSelectedOption[0] = that.filter_preference.get("selectedUsage");
                    }
                }
         });

         var user_pref_for_saving = {};
         user_pref_for_saving = user_home_preferences[0].userQues;

         console.log("_____________SHILPA LINE______________");
         console.log(user_pref_for_saving);

         console.log("@@@@@@@@@@@@@@ Final user_home_preferences object to save @@@@@@@@@@@@@@@@@@@");
         console.log(user_home_preferences);

          that.filter_preference.set({
                         'selectedHomePreference':user_pref_for_saving
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
                     var userId = "2ZBLKQ4vGMRSuN7k8AH8nf7InG43";
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

                adults['optionId'] = quesOptionid;
                adults['optionType'] = 2;
                adults['quesAnsId'] = quesOptionid;
                adults['quesId'] = quesId;
                adults['selectionStatus'] = 1;
                adults['userOptionDesc'] = "Added by tushar";


                that.filter_preference.set({
                    'selectedAdults':adults
                }, {
                    silent: true
                });

            }

             if(typeof(quesName) !== 'undefined' && quesName == "Teenagers / Young Adults"){

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

             var user_pref_for_saving = {};
             user_pref_for_saving = user_family_preferences[0].userQues;

             console.log("_____________Tushar LINE______________");
             console.log(user_pref_for_saving);

             console.log("@@@@@@@@@@@@@@ Final user_family_preferences object to save @@@@@@@@@@@@@@@@@@@");
             console.log(user_family_preferences);

              that.filter_preference.set({
                             'selectedFamilyPreference':user_pref_for_saving
                         }, {
                             silent: true
                         });

             that.filter_preference.set({
                 'selectedFinalObjFamily':user_family_preferences
             }, {
                 silent: true
             });

            return false;
        }
  });
  return UserProfilePage;
});
