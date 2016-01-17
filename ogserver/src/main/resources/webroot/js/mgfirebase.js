define(['firebase', 'underscore'], function(firebase, _) {

    var rootUrl = "https://sweltering-fire-6356.firebaseio.com/";
    var rootRef = new Firebase(rootUrl);
    return {
        'rootRef': rootRef,
        TYPE_CONSULT: "consult",
        TYPE_USER_ADD: "user.add",
        getUserProfile: function(authData, someFunc) {

            if (authData && authData.provider !== 'anonymous') {
                var userProfileRef = this.rootRef.child("user-profiles/" + authData.uid);
                var userProfile = null;
                var that = this;

                userProfileRef.once("value", function(snapshot) {
                    if (snapshot.exists()) {
                        that.userProfile = snapshot.val();
                    }
                    someFunc(that.userProfile);
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
        addConsultData: function(authData, formData) {
            this.rootRef.child("consults/" + authData.uid + "/" + Date.now()).set(formData,
                function(error) {
                    if (error) {
                        console.log("problem in inserting consult data", error);
                    } else {
                        console.log("successfully inserted consult data");
                    }
                });
            this.pushEvent(authData.uid, formData, this.TYPE_CONSULT);
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
        getName: function(authData, userProfile) {
            if (userProfile) {
                return userProfile.displayName;
            } else {
                switch (authData.provider) {
                    case 'password':
                        return authData.password.email.replace(/@.*/, '');
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
        }
    };
});