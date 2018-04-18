define([
  'jquery',
  'lodash',
  'backbone',
  'bootstrap',
  'mgfirebase',
  'text!templates/my_account/user_profile.html'
], function($, _, Backbone, Bootstrap,MGF, UserProfileTemplate){
  var UserProfilePage = Backbone.View.extend({
    el: '.page',
    initialize: function() {
        this.listenTo(Backbone);
        _.bindAll(this, 'render');
    },
    render: function () {
       var that = this;
       $(this.el).html(_.template(UserProfileTemplate));
    },
     events: {
                "click #save_details": "submit"
            },
    submit: function (e) {
                          if (e.isDefaultPrevented()) return;
                          e.preventDefault();

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
