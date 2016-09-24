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

        handleSignUp: function(email, password) {
           // Sign in with email and pass.
          // [START createwithemail]
          this.rootAuth.createUserWithEmailAndPassword(email, password).catch(function(error) {
            if(error){

                // Handle Errors here.
                var errorCode = error.code;
                var errorMessage = error.message;
                // [START_EXCLUDE]
                if (errorCode == 'auth/weak-password') {
                  alert('The password is too weak.');
                } else {
                  alert(errorMessage);
                }
                console.log(error);
                // [END_EXCLUDE]

            }else{
                console.log("Successfully created user!!!");
            }

          });
          // [END createwithemail]

        }

     };

 });
