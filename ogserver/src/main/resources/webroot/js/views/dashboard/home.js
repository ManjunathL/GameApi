define([
  'jquery',
  'lodash',
  'backbone',
  'bootstrap',
  'text!templates/dashboard/home.html',
  'collections/authsettings',
  'models/authsetup',
  'mgfirebase'
], function($, _, Backbone, Bootstrap, homePageTemplate, Authsettings, Authsetup,MGF){
  var HomePage = Backbone.View.extend({
    el: '.page',
    ref: MGF.rootRef,
    refAuth: MGF.refAuth,
    authsettings: null,
    authsetup: null,
    initialize: function() {
        this.authsettings = new Authsettings();
        this.authsetup = new Authsetup();
        this.listenTo(Backbone);
        _.bindAll(this, 'render','getLogin');
    },
    render: function() {
        var that = this;
          $(this.el).html(_.template(homePageTemplate));


        $(document).on("click", "a[href^='/']", function(event) {
                    href = $(event.currentTarget).attr('href');
                    if (!event.altKey && !event.ctrlKey && !event.metaKey && !event.shiftKey) {
                        event.preventDefault();
                        url = href.replace("/^\//", '').replace('\#\!\/', '');
                        window.App.router.navigate(url, {
                            trigger: true
                        });
                    }
                });
    },
     events: {
         "click #loginBtn": "getLogin",
         "click #signuplnk": "signupOpen",
         "click #registerBtn": "registerUser"
     },
     registerUser: function(e){
        var that = this;

        var name = $('#reg_name').val();
        var email = $('#reg_email_id').val();
        var password = $('#reg_password').val();
        var phoneNum = $('#reg_mobile').val();

        console.log(name+" ============= "+email+" ============= "+password+" ============= "+phoneNum);


        this.refAuth.createUserWithEmailAndPassword(email, password).then(function(userData) {
            console.log("Successfully created user account with uid:", userData.uid);
            var photoURL = userData.photoURL ? userData.photoURL: 'https://res.cloudinary.com/mygubbi/image/upload/v1484131794/cep/user_new.png';


            var userData = {
                email: $('#reg_email_id').val(),
                name: $('#reg_name').val(),
                photo: photoURL,
                phoneNo: $('#reg_mobile').val(),
                uid: userData.uid
            };

            //console.log("@@@@@@@@@@@@ userData @@@@@@@@@@@@@@@@@@@");
            //console.log(userData);
            that.createUser(userData.uid, userData, that.unAuthAfterProfile);

        }, function(error) {
            // An error happened.
            console.log("Error creating user:", error);
            $('#reg_error').html(error);
            $('#reg_error_row').css("display", "block");
            window.signupButton.button('reset');
        });

        return false;

     },
     createUser: function(userId, data) {
         var that = this;
         this.ref.child("users").child(userId).set(data, function(error) {
             if (error) {
                 console.log("Data could not be saved." + error);
                 //if (next) next(false);
             } else {
                 console.log("Data saved successfully.");
                 //if (next) next(true);
             }
         });
     },
     unAuthAfterProfile: function() {
         window.signupButton.button('reset');
         $('#reg_done_message').html("Thanks for registering with us. You now have access to our personalized service. Please <a href='#' id='goto-login'>Login</a> to proceed.");
         $('#register-modal').modal('hide');
         $('#notify').modal('show');

         this.refAuth.onAuthStateChanged(this.onFAuth);

         setTimeout(function() {
             window.location = '/';
         }, 1000);

     },
     signupOpen: function(){
        $("#register-modal").modal('show');
     },
     getLogin: function(){
         var that = this;
         //alert("sssssssssssssssssssss");

         var emailId = $("#emailID").val();
         var pwd = $("#passwrd").val();

         //alert(emailId+" ================= "+pwd);

         firebase.auth().signInWithEmailAndPassword(emailId, pwd).then(function(authData) {
           // Sign-in successful.
           console.log('User data after login');
           console.log(authData);

           $('#user-icon').toggleClass("glyphicon glyphicon-user fa fa-spinner fa-spin");
           console.log('Sign-in successful');


           console.log(authData.uid);
               if(typeof(authData.uid) !== 'undefined'){
                    sessionStorage.fbId = authData.uid;

                    sessionStorage.fbtoken = authData._lat;

                    if(typeof(authData.uid) !== 'undefined'){
                     sessionStorage.userId = authData.uid;
                    }

                    //window.location = "/viewconceptboards";

                    window.App.router.navigate("/page", {
                        trigger: true
                    });
                    that.getAuthentication(emailId,pwd);

               }else{
                    sessionStorage.fbId = "";
               }
                return false;
            }, function(error) {
          // An error happened.
          $("#login_error").html("Invalid Email or Password");
          console.log('Error'+error);
          return false;
         });



         return false;
     },
     getAuthentication: function(emailId,pwd){
         var that = this;

         var form = new FormData();
         form.append("username", sessionStorage.fbtoken);
         form.append("password", "welcome");
         form.append("grant_type", "password");
         form.append("client_id", "clientIdPassword");

         /*form.append("username", "rajnish.kumar2291@gmail.com");
         form.append("password", "welcome");
         form.append("grant_type", "password");
         form.append("client_id", "clientIdPassword");*/

         var settings = {
           "async": true,
           "crossDomain": true,
           "url": authbaseRestApiUrl+"MyGubbiAuth/oauth/token",
           "method": "POST",
           "headers": {
             "authorization": "Basic Y2xpZW50SWRQYXNzd29yZDpzZWNyZXQ="
           },
           "processData": false,
           "contentType": false,
           "mimeType": "multipart/form-data",
           "data": form
         }

         $.ajax(settings).done(function (response) {
           that.fetchAuthrender(response);
         });
     },
     fetchAuthrender: function (authTokenObj) {
         var that = this;
         console.log("authTokenObj++++++++++++++++");
         var authTokenObj = $.parseJSON(authTokenObj);
         console.log(authTokenObj.access_token);

          sessionStorage.authtoken = "";

         if(typeof(authTokenObj.access_token) !== 'undefined'){
             sessionStorage.authtoken = authTokenObj.access_token;
         }else{
             sessionStorage.authtoken = "";
         }

         /*if(typeof(authTokenObj.userId) !== 'undefined'){
             sessionStorage.userId = authTokenObj.userId;
         }*///else{
             //sessionStorage.userId = "user1234600";
         //}
         //sessionStorage.userId = "user1234600";

         //$("#accessToken").val(sessionStorage.authtoken);
         //window.location.href = "/viewconceptboards";
         window.App.router.navigate("/page", {
             trigger: true
         });
     }
  });
  return HomePage;
});
