define(['firebase', 'underscore'], function(firebase, _) {

    var rootUrl = "https://sweltering-fire-6356.firebaseio.com/";
    var rootRef = new Firebase(rootUrl);
    return {
        'rootRef': rootRef,
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
        }
    };
});