define(['firebase', 'underscore'], function(firebase, _) {

    var rootUrl = "https://sweltering-fire-6356.firebaseio.com/";
    var rootRef = new Firebase(rootUrl);
    return {
        'rootRef': rootRef,
        TYPE_CONSULT: "consult",
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
        }

    };
});