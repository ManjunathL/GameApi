define(['firebase', 'underscore', 'backbone', '/js/local_storage.js'], function(firebase, _, backbone, LS) {

    // Initialize Firebase
     var config = {
       apiKey: "AIzaSyCoKs1sqeE8GmMKysYpkOdO9KkZXcmhDQ0",
       authDomain: "mygubbi-uat.firebaseapp.com",
       databaseURL: "https://mygubbi-uat.firebaseio.com/",
       storageBucket: "mygubbi-uat.appspot.com",
       messagingSenderId: "106892346704"
     };
     firebase.initializeApp(config);

     var rootRef = firebase.database().ref();
     var refAuth = firebase.auth();

    return {
        'rootRef': rootRef,
        'refAuth': refAuth,
        shortlistedItems: null,
        TYPE_CONSULT: "consult",
        TYPE_SUBSCRIBE: "subscribe",
        TYPE_USER_ADD: "user.add",
        TYPE_USER_UPDATE: "user.updateProfile",
        TYPE_USER_REMOVE: "user.remove",
        TYPE_SHORTLIST_PRODUCT_ADD: "shortlist.product.add",
        TYPE_SHORTLIST_PRODUCT_REMOVE: "shortlist.product.remove",
        handleSignUp: function(email, password) {
           // Sign in with email and pass.
          this.refAuth.createUserWithEmailAndPassword(email, password).then(function(userData) {
            console.log("Successfully created user!!!");
            console.log(userData);
          }, function(error) {
            // An error happened.
            console.log('Error'+error);
          });
        },
        getUserProfile: function(authData, someFunc) {

            var that = this;
            // Listening for auth state changes.
          // [START authstatelistener]
          this.refAuth.onAuthStateChanged(function(user) {
            if (user) {
              // User is signed in.
              var displayName = user.displayName;
              var email = user.email;
              var emailVerified = user.emailVerified;
              var photoURL = user.photoURL;
              var isAnonymous = user.isAnonymous;
              var uid = user.uid;
              if(typeof(user.providerData) !== 'undefined' && user.providerData.length > 0){
                var providerId = user.providerData[0].providerId;
              }else{
                var providerId = user.providerData;
              }


              console.log('provider data ===== ');
              console.log(providerId);

                var userProfileRef = firebase.database().ref().child("user-profiles/" + uid);

                var userProfile = null;
                var that = this;

                userProfileRef.on("value", function(snapshot) {
                    if (snapshot.exists()) {
                        that.userProfile = snapshot.val();
                    }
                    someFunc(that.userProfile,null, providerId);
                   });

            } else {
                someFunc(null);
            }
          });
          // [END authstatelistener]
        },
        createProfile: function(userData, profileData, next) {
            this.rootRef.child('user-profiles').child(userData.uid).set(
                profileData,
                function(error) {
                    if (error) {
                        console.log("password profile data could not be saved." + error);
                    } else {
                        console.log("data saved successfully.");
                    }
                    if (next) next();
                }
            );
            this.pushEvent(userData.uid, profileData, this.TYPE_USER_ADD);
        },
        updateProfile: function(profileData) {

            var that = this;
            var uid = this.refAuth.currentUser.uid;

            return new Promise(function(resolve, reject){
                that.rootRef.child('user-profiles').child(uid).set(
                    profileData,
                    function(error) {
                        if (error) {
                            console.log("password profile data could not be saved." + error);
                            reject && reject();
                        } else {
                            console.log("data saved successfully.");
                            that.pushEvent(uid, profileData, that.TYPE_USER_UPDATE);
                            resolve();
                        }
                    }
                );
            });
        },
        getName: function(authData, userProfile) {
            if (userProfile) {
                return userProfile.displayName;
            } else {
            console.log('get name');
                var providerData = '', providerId='';
                if(typeof(authData.providerData) !== 'undefined'){
                    var providerData = authData.providerData[0];
                    var providerId = providerData.providerId;
                }
                switch (providerId) {
                    case 'password':
                        return providerData.email.replace(/@.*/, '');
                    case 'google.com':
                        return providerData.displayName;
                    case 'facebook.com':
                        return providerData.displayName;
                        //                    case 'twitter':
                        //                        return authData.twitter.displayName;
                }
            }
        },
        getImage: function(authData, userProfile) {
            if (userProfile) {
                return userProfile.profileImage;
            } else {
                console.log('get image');
                console.log(authData);
                var providerData = '', providerId='';
                if(typeof(authData.providerData) !== 'undefined'){
                    providerData = authData.providerData[0];
                    providerId = providerData.providerId;
                }
                switch (providerId) {
                    case 'password':
                        return providerData.photoURL;
                    case 'google.com':
                        return providerData.photoURL;
                    case 'facebook.com':
                        return providerData.photoURL;
                        //                    case 'twitter':
                        //                        return authData.twitter.profileImageURL;
                }
            }
        },
        getEmail: function(authData) {
            var providerData = '', providerId='';
            if(typeof(authData.providerData) !== 'undefined' && authData.providerData.length > 0){
                providerData = authData.providerData[0];
                providerId = providerData.providerId;
            }
            switch (providerId) {
                case 'password':
                    return providerData.email;
                case 'google.com':
                    return providerData.email;
                case 'facebook.com':
                    return providerData.email;
                    //                case 'twitter':
                    //                    return authData.twitter.email;
            }

        },
        pushEvent: function(uid, data, type) {
            var eventData = {
                "data": data,
                "type": type
            };
            this.rootRef.child("events").push().child(uid).set(eventData, function(error) {
                if (error) {
                    console.log("not able to push event data", error);
                } else {
                    console.log("successfully pushed event data");
                }
            });
        },
        removeShortlistProduct: function(productId) {
            var that = this;
            var authData = this.refAuth.currentUser;
            return new Promise(function(resolve, reject) {
                that.rootRef.child("shortlists").child(authData.uid).child(productId).remove(function(error) {
                    if (error) {
                        reject();
                    } else {
                        resolve();
                        that.pushEvent(authData.uid, {
                            productId: productId
                        }, that.TYPE_SHORTLIST_PRODUCT_REMOVE);
                    }
                });
            });
        },
        addShortlistProduct: function(product) {
            var that = this;
            var productId = product.productId;
            var authData = this.refAuth.currentUser;
            return new Promise(function(resolve, reject) {
                that.rootRef.child("shortlists").child(authData.uid).child(productId).set(
                    product,
                    function(error) {
                        if (error) {
                            console.log("not able to add shortlist data", error);
                            reject();
                        } else {
                            console.log("successfully added shortlist data");
                            resolve();
                            var email = that.getEmail(authData);
                            var data = {
                                product: product,
                                email: email ? email : ''
                            };
                            that.pushEvent(authData.uid, data, that.TYPE_SHORTLIST_PRODUCT_ADD);
                        }
                    });
            });
        },
        doAnonymousAuth: function() {
             var that = this;
             var existingAuthData = that.refAuth.currentUser;
             if (!existingAuthData) {
                 that.refAuth.signInAnonymously().catch(function(error) {
                      if (error) {
                          console.log("error in anonymous auth", error);
                      }
                  });
             }
        },
        getShortListedItems: function() {
         return this.shortlistedItems;
        },
        getShortListed: function(id) {
         return _.findWhere(this.shortlistedItems, {
             productId: id
         });
        },
        stopListeningForShortlistChanges: function(uid) {
         uid && this.rootRef.child("shortlists").child(uid).off("value");
        },
        listenForShortlistChanges: function() {
         var authData = this.refAuth.currentUser;
         var that = this;
         this.stopListeningForShortlistChanges(this.previousUid);
         this.stopListeningForShortlistChanges(authData.uid);
         this.previousUid = authData.uid;
         this.transferShortlistData(authData);
         var first = true;
         this.rootRef.child("shortlists").child(authData.uid).on("value", function(snapshot) {
             if (snapshot.exists()) {
                 that.shortlistedItems = snapshot.val();
             } else {
                 that.shortlistedItems = null;
             }
             Backbone.trigger('shortlist.change');
         }, function(error) {
             console.log("couldn't start listening to shortlist changes", error);
         });
        },
       transferShortlistData: function(authData) {
                if ((typeof(authData.provider) !== "undefined") && (authData.provider[0].providerId !== 'anonymous')) { //don't transfer shortlist when a person is logging out
                    var that = this;
                    _.each(that.shortlistedItems, function(shortlistedItem) {
                        that.addShortlistProduct(shortlistedItem).then(function() {});
                    });
                }
               },
        mynest: function(authData, someFunc) {
            console.log('------------authData in my nest----------------');
            console.log(authData);
            var mynestitems = null;
            //var authData = this.refAuth.currentUser;
             var projRef = firebase.database().ref().child("projects/" + authData.uid+"/myNest");
             var projectDetails = null;
             var that = this;

             projRef.on("value", function(snapshot) {
                 if (snapshot.exists()) {
                 console.log("--------project details--------");
                 console.log(authData);
                     that.projectDetails = snapshot.val();
                     var userProfileRef = firebase.database().ref().child("user-profiles/" + authData.uid);

                     var userProfile = null;

                     userProfileRef.on("value", function(snapshots) {
                         if (snapshots.exists()) {
                             that.userProfile = snapshots.val();
                         }
                         var providerId = authData.providerData[0].providerId;
                         someFunc(that.userProfile,that.projectDetails, providerId);
                        });
                 }else{
                    console.log("--------project details not exists--------");
                    var userProfileRef = firebase.database().ref().child("user-profiles/" + authData.uid);
                    var userProfile = null;
                    var providerId = authData.providerData[0].providerId;

                    userProfileRef.on("value", function(snapshot) {
                        if (snapshot.exists()) {
                            that.userProfile = snapshot.val();
                        }
                        someFunc(that.userProfile,null, providerId);
                       });

                 }
             });

        }
    };
});