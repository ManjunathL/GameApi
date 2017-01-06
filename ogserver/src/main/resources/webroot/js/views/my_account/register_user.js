/**
 * Created by Smruti on 28/12/16.
 */
define([
    'jquery',
    'underscore',
    'backbone',
    '/js/mgfirebase.js',
    'text!/templates/my_account/register_user.html'
], function ($, _, Backbone, MGF, RegisterUserTemplate) {
    var UserProfileView = Backbone.View.extend({
        initialize: function() {
            console.log('hi Smruti');
            console.log(this.model.userdata);

            var queryString = this.model.userdata;
            if(queryString.indexOf('?') > -1){
                queryString = queryString.split('?')[1];
              }
              var pairs = queryString.split('&');
              var result = {};
              pairs.forEach(function(pair) {
                pair = pair.split('=');
                result[pair[0]] = decodeURIComponent(pair[1] || '');
              });

              //var res = JSON.parse(result.json);

              console.log(result);

              var name = result.name;
              var semail = decodeURIComponent(result.email);
              var email = semail.replace("|", ".");
              console.log(semail+ '----------------' + email);

              var phone = decodeURIComponent(result.phone);
              var password = result.password;

              var photoURL = result.photoURL ? result.photoURL : 'https://res.cloudinary.com/mygubbi/image/upload/f_auto/user.png';
              var crmId = result.crmId;

              var that = this;

              MGF.refAuth.createUserWithEmailAndPassword(email, password).then(function(userData) {
                  console.log("Successfully created user account with uid:", userData.uid);

                  var uid = userData.uid;
                  /*firebase.auth().signOut().then(function() {
                        console.log("Sign-out successful");
                        $("#testdv").text("Sign-out successful");
                  }, function(error) {
                    console.log("An error happened");
                    $("#testdv").text("An error happened");
                  });*/


                  console.log("uid: "+uid);

                  var userData = {
                      providerData: "password",
                      email: email,
                      displayName: name,
                      profileImage: photoURL,
                      uid: uid
                  };
                   that.createUser(userData);
                   var profileData = {
                      displayName: name,
                      email: email,
                      phone: phone,
                      profileImage: photoURL,
                      crmId:crmId
                  };
                  that.createProfile(userData,profileData);
              }, function(error) {
                  // An error happened.
                  console.log("Error creating user:", error);
              });

        },
        createUser: function(userData){
             MGF.rootRef.child("users").child(userData.uid).set(userData, function(error) {
                 if (error) {
                     console.log("Data could not be saved." + error);
                 } else {
                     console.log("Data saved successfully.");
                 }
             });
        },
        createProfile: function(userData,profileData){
            var that = this;
            MGF.rootRef.child('user-profiles').child(userData.uid).set(
                profileData,
                function(error) {
                    if (error) {
                        console.log("password profile data could not be saved." + error);
                    } else {
                        console.log("data saved successfully.");
                    }
                }
            );
            that.pushEvent(userData.uid, profileData, "user.add");

        },
        pushEvent: function(uid, data, type){
            var eventData = {
                "data": data,
                "type": type
            };
            MGF.rootRef.child("events").push().child(uid).set(eventData, function(error) {
                if (error) {
                    console.log("not able to push event data", error);
                } else {
                    console.log("successfully pushed event data");
                }
            });

        }
    });
    return UserProfileView;
});
