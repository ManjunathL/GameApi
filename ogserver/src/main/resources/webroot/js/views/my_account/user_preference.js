define([
  'jquery',
  'lodash',
  'backbone',
  'bootstrap',
  'text!templates/my_account/user_preference.html',
  'collections/user_preferences'
], function($, _, Backbone, Bootstrap, UserProfileTemplate,UserPreferences){
  var UserProfilePage = Backbone.View.extend({
    el: '.page',
    user_preferences: null,
    initialize: function() {
        this.user_preferences = new UserPreferences();
        this.listenTo(Backbone);
        _.bindAll(this, 'render');
    },
    render: function () {
       var that = this;

        var getUserPreferencesPromise = that.getUserPreferences();

        Promise.all([getUserPreferencesPromise]).then(function() {
            console.log("@@@@@@@@@@@@@ In side Promise @@@@@@@@@@@@@@@@@@");
            that.fetchUserPreferencesAndRender();
        });
    },
    getUserPreferences: function(){
        var that = this;
        var category = 1;
        //var userId = sessionStorage.userId;
        var userId = "2ZBLKQ4vGMRSuN7k8AH8nf7InG43";
        return new Promise(function(resolve, reject) {
           that.user_preferences.getUserPreferences(category, userId, {
               async: true,
               crossDomain: true,
               method: "GET",
               headers:{
                   "authorization": "Bearer "+ sessionStorage.authtoken
               },
               success:function(data) {

                   console.log(" +++++++++++++++ getUserPreferences ++++++++++++++++++ ");
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
        var user_preferences = that.user_preferences;

        $(this.el).html(_.template(UserProfileTemplate)({
           'userpreferencesDtls':user_preferences.toJSON()
       }));
    }
  });
  return UserProfilePage;
});
