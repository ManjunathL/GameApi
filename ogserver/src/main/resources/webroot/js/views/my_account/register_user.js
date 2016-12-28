/**
 * Created by Smruti on 29/08/16.
 */
define([
    'jquery',
    'underscore',
    'backbone',
    '/js/mgfirebase.js'
], function ($, _, Backbone, MGF) {
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


              var name = result.name;
              var email = result.email;
              var phone = result.phone;
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



            console.log(result.name);
        },
        createUser: function(userData){
             MGF.rootRef.ref().child("users").child(userData.uid).set(userData, function(error) {
                 if (error) {
                     console.log("Data could not be saved." + error);
                 } else {
                     console.log("Data saved successfully.");
                 }
             });
        },
        createProfile: function(userData,profileData){
            MGF.rootRef.child('user-profiles').child(userData.uid).set(
                profileData,
                function(error) {
                    if (error) {
                        console.log("password profile data could not be saved." + error);
                        $("#testdv").text("password profile data could not be saved." + error);
                    } else {
                        console.log("data saved successfully.");
                        $("#testdv").text("data saved successfully.");
                    }
                }
            );
            pushEvent(userData.uid, profileData, "user.add");

        },
        pushEvent: function(uid, data, type){
            var eventData = {
                "data": data,
                "type": type
            };
            MGF.rootRef.child("events").push().child(uid).set(eventData, function(error) {
                if (error) {
                    console.log("not able to push event data", error);
                    $("#testdv").text("not able to push event data" + error);
                } else {
                    console.log("successfully pushed event data");
                    $("#testdv").text("successfully pushed event data");

                }
            });
        }
    });
    return UserProfileView;
});
