define([
  'jquery',
  'lodash',
  'backbone',
  'bootstrap',
  'text!templates/my_account/user_preference.html',
  'collections/user_preferences',
  'collections/family_preferences',
  'collections/saveuser_preferences',
  'models/filter_preference'
], function($, _, Backbone, Bootstrap, UserProfileTemplate,UserPreferences,FamilyPreferences,SaveUserPreferences,FilterPreference){
  var UserProfilePage = Backbone.View.extend({
    el: '.page',
    user_home_preferences: null,
    user_family_preferences: null,
    user_members_preferences: null,
    saveuser_preferences:null,
    filter_preference: null,
    initialize: function() {
        this.filter_preference = new FilterPreference();
        this.user_home_preferences = new UserPreferences();
        this.user_family_preferences = new UserPreferences();
        this.user_members_preferences = new FamilyPreferences();
        this.saveuser_preferences = new SaveUserPreferences();

        this.filter_preference.on('change', this.render, this);
        this.listenTo(Backbone);
        _.bindAll(this, 'render');
    },
    render: function () {
       var that = this;

       window.filter_preference = that.filter_preference;



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
    },getUserFamilyPreferences: function(){
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
        "click #save_homeId":"saveHomePreferences"
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


        var  user_home_preferences = that.user_home_preferences;
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
    }
  });
  return UserProfilePage;
});
