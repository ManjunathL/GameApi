var firebaseFB = require("firebase");

var fbConfig = {
    apiKey: "AIzaSyCoKs1sqeE8GmMKysYpkOdO9KkZXcmhDQ0",
    authDomain: "mygubbi-uat.firebaseapp.com",
    databaseURL: "https://mygubbi-uat.firebaseio.com",
    storageBucket: "mygubbi-uat.appspot.com",
};

var resFbApp = firebaseFB.initializeApp(fbConfig);

var refFbAuth = resFbApp.auth();
var refFbDatabase = resFbApp.database().ref();

var url = require('url');

var fs = require('fs');
var https = require('https');
var options = {
    key: fs.readFileSync('key.pem'),
    cert: fs.readFileSync('cert.pem')
};

https.createServer(options, function(request, response) {

    console.log('Server running at https://127.0.0.1:8685/');

    var url_parts = url.parse(request.url, true);
    var query = url_parts.query;

    var name = query.name;
    var semail = decodeURIComponent(query.email);

    var email = semail.split('|').join(".");
    console.log(semail+ '----------------' + email);

    var phone = decodeURIComponent(query.phone);
    var password = query.password;

    var photoURL = query.photoURL ? query.photoURL : 'https://res.cloudinary.com/mygubbi/image/upload/v1484131794/cep/user_new.png';
    var crmId = query.crmId;

    //response.writeHead(200, {"Content-Type": "text/plain"});



    var message = {};
    var errorMessage = "";
    var successMessage = "";



    if(resFbApp){

        refFbAuth.createUserWithEmailAndPassword(email, password).then(function(userData) {
            console.log("Successfully created user account with uid:", userData.uid);

            var uid = userData.uid;

            console.log("uid: "+uid);


            var userData = {
                providerData: "password",
                email: email,
                displayName: name,
                profileImage: photoURL,
                uid: uid
            };
             refFbDatabase.child("users").child(userData.uid).set(userData, function(error) {
                if (error) {
                    console.error("Data could not be saved." + error);
                    response.writeHead(500, {"Content-Type": "application/json"});
                    response.write(JSON.stringify({"Error creating User":error.message}));
                    response.end();
                } else {
                    console.log("User created successfully");

                    var profileData = {
                        displayName: name,
                        email: email,
                        phone: phone,
                        profileImage: photoURL,
                        crmId:crmId
                    };

                    var data = profileData;
                    refFbDatabase.child('user-profiles').child(userData.uid).set(
                        data,
                        function(error) {
                            if (error) {
                                console.error("password profile data could not be saved." + error);
                                response.writeHead(500, {"Content-Type": "application/json"});
                                response.write(JSON.stringify({"Error creating User Profile":error.message}));
                                response.end();

                            } else {
                                console.log("User Profile created successfully.");
                                var eventData = {
                                    "data": data,
                                    "type": "user.add"
                                };
                                refFbDatabase.child("events").push().child(userData.uid).set(eventData, function(error) {
                                    if (error) {
                                        console.error("not able to push event data"+ error.message);
                                        response.writeHead(500, {"Content-Type": "application/json"});
                                        response.write(JSON.stringify({"not able to push event data":error.message}));
                                        response.end();
                                    } else {
                                        console.log("successfully pushed event data");
                                        response.writeHead(200, {"Content-Type": "application/json"});
                                        response.write(JSON.stringify({"successfully pushed event data":userData.uid}));
                                        response.end();
                                    }
                                });

                            }
                        }
                    );
                }
            });




        }, function(error) {
            console.error(error);
            response.writeHead(500, {"Content-Type": "application/json"});
            response.write(JSON.stringify({"Error creating Authentication":error.message}));
            response.end();
        });

    }else{
        console.error("Error in firebase");
        response.writeHead(500, {"Content-Type": "application/json"});
        response.write("Error in firebase");
        response.end();
    }

}).listen(8685);

