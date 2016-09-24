define(['firebase', 'underscore', 'backbone', '/js/local_storage.js'], function(firebase, _, backbone, LS) {

    // Initialize Firebase
     var config = {
       apiKey: "AIzaSyALgwN8p4bsgNDskEP_F4IPEyJTeUaQqTo",
       authDomain: "mygubbi-cep.firebaseapp.com",
       databaseURL: "https://mygubbi-cep.firebaseio.com",
       storageBucket: "mygubbi-cep.appspot.com",
       messagingSenderId: "623623245186"
     };
     firebase.initializeApp(config);

     var rootRef = firebase.database().ref();
     var rootAuth = firebase.auth();

    return {
        'rootRef': rootRef,
        'rootAuth': rootAuth,
        shortlistedItems: null,
        TYPE_CONSULT: "consult",
        TYPE_SUBSCRIBE: "subscribe",
        TYPE_USER_ADD: "user.add",
        TYPE_USER_UPDATE: "user.update",
        TYPE_USER_REMOVE: "user.remove",
        TYPE_SHORTLIST_PRODUCT_ADD: "shortlist.product.add",
        TYPE_SHORTLIST_PRODUCT_REMOVE: "shortlist.product.remove",
        handleSignUp: function(email, password) {

        console.log(' ------------- here ------------------');
           // Sign in with email and pass.
          // [START createwithemail]
          this.rootAuth.createUserWithEmailAndPassword(email, password).then(function(userData) {
            console.log("Successfully created user!!!");
            console.log(userData);
          }, function(error) {
            // An error happened.
            console.log('Error'+error);
          });
          // [END createwithemail]

        },
        getUserProfile: function(authData, someFunc) {

            // Listening for auth state changes.
          // [START authstatelistener]
          this.rootAuth.onAuthStateChanged(function(user) {
            if (user) {
              // User is signed in.
              var displayName = user.displayName;
              var email = user.email;
              var emailVerified = user.emailVerified;
              var photoURL = user.photoURL;
              var isAnonymous = user.isAnonymous;
              var uid = user.uid;
              var providerData = user.providerData;

               someFunc(user, providerData);
            } else {
                someFunc(null);
            }
          });
          // [END authstatelistener]
        }
/*        getUserProfile: function(authData, someFunc) {

            if (authData && authData.provider !== 'anonymous') {
                var userProfileRef = this.rootRef.child("user-profiles/" + authData.uid);
                var userProfile = null;
                var that = this;

                userProfileRef.once("value", function(snapshot) {
                    if (snapshot.exists()) {
                        that.userProfile = snapshot.val();
                    }
                    someFunc(that.userProfile, authData.provider);
                });
            } else {
                someFunc(null);
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
        addConsultData: function(formData, userId) {
            var uid = userId ? userId : this.rootRef.getAuth().uid;
            var that = this;
            LS.addConsultData(uid, formData); //add to local storage as a backup option
            this.rootRef.child("consults/" + uid + "/" + Date.now()).set(formData,
                function(error) {
                    if (error) {
                        console.log("problem in inserting consult data", error);
                    } else {
                        console.log("successfully inserted consult data");
                        LS.removeConsultData(uid); //cleanup local storage as firebase has already submitted the data
                        that.pushEvent(uid, formData, that.TYPE_CONSULT);
                    }
                });
        },
        subscribeUser: function(email) {
            var uid = this.rootRef.getAuth().uid;
            var that = this;
            var formData = {
                email: email
            };
            this.rootRef.child("subscriptions/" + uid + "/" + Date.now()).set(formData,
                function(error) {
                    if (error) {
                        console.log("problem in inserting subscription data", error);
                    } else {
                        console.log("successfully inserted subscription data");
                        that.pushEvent(uid, formData, that.TYPE_SUBSCRIBE);
                    }
                });
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
            var uid = this.rootRef.getAuth().uid;
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
                switch (authData.provider) {
                    case 'password':
                        return authData.password.email.replace(/@.*//*, '');
                    case 'google':
                        return authData.google.displayName;
                    case 'facebook':
                        return authData.facebook.displayName;
                        //                    case 'twitter':
                        //                        return authData.twitter.displayName;
                }
            }
        },
        getImage: function(authData, userProfile) {
            if (userProfile) {
                return userProfile.profileImage;
            } else {
                switch (authData.provider) {
                    case 'password':
                        return authData.password.profileImageURL;
                    case 'google':
                        return authData.google.profileImageURL;
                    case 'facebook':
                        return authData.facebook.profileImageURL;
                        //                    case 'twitter':
                        //                        return authData.twitter.profileImageURL;
                }
            }
        },
        getEmail: function(authData) {
            switch (authData.provider) {
                case 'password':
                    return authData.password.email;
                case 'google':
                    return authData.google.email;
                case 'facebook':
                    return authData.facebook.email;
                    //                case 'twitter':
                    //                    return authData.twitter.email;
            }
        },
        removeShortlistProduct: function(productId) {
            var that = this;
            var authData = this.rootRef.getAuth();
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
            var authData = this.rootRef.getAuth();
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
                            var email = that.getEmail(that.rootRef.getAuth());
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
            var existingAuthData = that.rootRef.getAuth();
            if (!existingAuthData) {
                that.rootRef.authAnonymously(function(error, authData) {
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
            var authData = this.rootRef.getAuth();
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
                if (first) {
                    first = false;
                    LS.submitAllConsultData(_.bind(that.addConsultData, that));
                    Backbone.trigger('user.change');
                }
            }, function(error) {
                console.log("couldn't start listening to shortlist changes", error);
            });
        },
        transferShortlistData: function(authData) {
            if (authData.provider !== 'anonymous') { //don't transfer shortlist when a person is logging out
                var that = this;
                _.each(this.shortlistedItems, function(shortlistedItem) {
                    that.addShortlistProduct(shortlistedItem).then(function() {});
                });
            }
        }*/
    };
});