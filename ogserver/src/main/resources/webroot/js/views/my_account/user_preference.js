define([
  'jquery',
  'lodash',
  'backbone',
  'bootstrap',
  'text!templates/my_account/user_preference.html',
  'collections/user_preferences',
  'collections/family_preferences'
], function($, _, Backbone, Bootstrap, UserProfileTemplate,UserPreferences,FamilyPreferences){
  var UserProfilePage = Backbone.View.extend({
    el: '.page',
    user_home_preferences: null,
    user_family_preferences: null,
    user_members_preferences: null,
    initialize: function() {
        this.user_home_preferences = new UserPreferences();
        this.user_family_preferences = new UserPreferences();
        this.user_members_preferences = new FamilyPreferences();

        this.listenTo(Backbone);
        _.bindAll(this, 'render');
    },
    render: function () {
       var that = this;

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
      });
    },getUserFamilyPreferences: function(){
        var that = this;
        var category = 2;
        //var userId = sessionStorage.userId;
        var userId = "2ZBLKQ4vGMRSuN7k8AH8nf7InG43";
        return new Promise(function(resolve, reject) {
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
      });
    },getUserMemberPreferences: function(){
        var that = this;

        //var userId = sessionStorage.userId;
        var userId = "2ZBLKQ4vGMRSuN7k8AH8nf7InG43";
        return new Promise(function(resolve, reject) {
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
    }
  });
  return UserProfilePage;
});
