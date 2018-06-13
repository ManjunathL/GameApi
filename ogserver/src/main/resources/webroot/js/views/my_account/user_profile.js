define([
  'jquery',
  'lodash',
  'backbone',
  'bootstrap',
  'bootstrapvalidator',
  'mgfirebase',
  'text!templates/my_account/user_profile.html'
], function($, _, Backbone, Bootstrap, BootstrapValidator,MGF, UserProfileTemplate){
  var UserProfilePage = Backbone.View.extend({
    el: '.page',
    ref: MGF.rootRef,
    refAuth: MGF.refAuth,
    initialize: function() {
        this.listenTo(Backbone, 'user.change', this.handleUserChange);
        _.bindAll(this, 'renderWithUserProfCallback', 'render', 'submit');
    },
    handleUserChange: function () {
        if (VM.activeView === VM.USER_PROFILE) {
            window.location = '/';
        }
    },
    renderWithUserProfCallback: function (userProfData, provider) {
        console.log("+++++++++++++ userProfData +++++++++++++++");
        console.log(userProfData);

        $(this.el).html(_.template(UserProfileTemplate)({
            'userProfile': userProfData,
            'provider': provider
        }));
    },
    render: function () {
        var authData = this.refAuth.currentUser;
        MGF.getUserProfile(authData, this.renderWithUserProfCallback);

        console.log("+++++++++++++ authData +++++++++++++++");
        console.log(authData);
    },
    events: {
        "click #save_details": "submit"
    },
    submit: function (e) {
          if (e.isDefaultPrevented()) return;
          e.preventDefault();

          if($('#inputname').val() == "" || $('#inputmobilenumber').val() == ""){
            $("#snackbar").html("Please fill the mandatory fields");
            var x = document.getElementById("snackbar")
            x.className = "show";
            setTimeout(function(){ x.className = x.className.replace("show", ""); }, 3000);
            return;
          }

          var communicationAddress={
            "houseNoStreet": $('#inputCommunicationAddress').val(),
            "city": $('#inputCommunicationCity').val(),
            "state": $('#inputCommunicationState').val(),
            "pincode": $('#inputCommunicationpincode').val()
           };

          var formData = {
              "additionalEmail": $('#inputemail').val(),
              "communicationAddress":communicationAddress,
              "dob": $('#inputdob').val(),
              "gender": $("input[name='gender']:checked").val(),
              "mobileNumber": $('#inputmobilenumber').val(),
              "name": $('#inputname').val(),
              "surName": $('#inputsurname').val()
          };

          console.log("User Profile form Data ");
          console.log(formData);

          var that = this;
          MGF.updateProfile(formData).then(function () {
          console.log("inside update Profile ");
            $("#snackbar").html("Successfully updated profile details... ");
            var x = document.getElementById("snackbar")
            x.className = "show";
            setTimeout(function(){ x.className = x.className.replace("show", ""); }, 3000);
            //return;
              that.render();
          });
      }

  });
  return UserProfilePage;
});
