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
        this.listenTo(Backbone);
        _.bindAll(this, 'renderWithUserProfCallback', 'render');
    },
    renderWithUserProfCallback: function (userProfData, provider) {
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
            return;
          }

          var communicationAddress={
                        "houseNoStreet": $('#inputCommunicationAddress').val(),
                        "city": $('#inputCommunicationCity').val(),
                        "state": $('#inputCommunicationState').val(),
                        "pincode": $('#inputCommunicationpincode').val()
          } ;

          var projectAddress={
                    "houseNoStreet": $('#inputProjectAddress').val(),
                    "city": $('#inputProjectCity').val(),
                    "state": $('#inputProjectState').val(),
                    "pincode": $('#inputpincode').val()
          };
          var projectDetails={
                    "builder": $('#inputbuilder').val(),
                    "city": $('#inputcity').val(),
                    "plan": $('#inputplan').val(),
                    "project": $('#inputproject').val(),
                    "projecttower": $('#inputprojecttower').val()
          };
          var formData = {
              "additionalEmail": $('#inputemail').val(),
              "communicataionAddress":communicationAddress,
              "dob": $('#inputdob').val(),
              /*"gender": $('#gender').val(),*/
              "gender": "female",
              "mobileNumber": $('#inputmobilenumber').val(),
              "name": $('#inputname').val(),
              "projectAddress":projectAddress,
              "projectDetail":projectDetails,
              "surName": $('#inputsurname').val()
          };
          console.log("form Data ");
          console.log(formData);
          var that = this;
          MGF.updateProfile(formData).then(function () {
          console.log("inside update Profile ");
              that.render()
          });
      }

  });
  return UserProfilePage;
});
