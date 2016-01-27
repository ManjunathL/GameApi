/**
 * Created by og on 15/12/15.
 */
define([
    'jquery',
    'underscore',
    'backbone',
    'bootstrap',
    'bootstrapvalidator',
    'mgfirebase',
    'text!templates/user_profile/user_profile.html'
], function($, _, Backbone, Bootstrap, BootstrapValidator, MGF, UserProfileTemplate ) {
    var UserProfileView = Backbone.View.extend({
        users: [],
        el: '.page',
        ref: MGF.rootRef,
        renderWithUserProfCallback: function(userProfData,provider) {
            $(this.el).html(_.template(UserProfileTemplate)({
                'userProfile': userProfData,
                'provider': provider
            }));
        },
        render: function() {
            var authData = this.ref.getAuth();
            MGF.getUserProfile(authData, this.renderWithUserProfCallback);
        },
        initialize: function() {
            _.bindAll(this, 'renderWithUserProfCallback', 'render', 'submit', 'setuserProfileData');
        },
        submit: function(e) {
            if (e.isDefaultPrevented()) return;
            e.preventDefault();

            var formData = {
                "displayName": $('#user_display_name').val(),
                "profileImage": $('#user_profile_image').attr('src'),
                "email": $('#user_email_id').attr('data-value'),
                "dob": $('#user_dob').val(),
                "phone": $('#user_phone').val(),
                "address": $('#user_address').val(),
                "city": $('#user_city').val(),
                "state": $('#user_state').val(),
                "pinCode": $('#user_pin_code').val()
            };

            var authData = this.ref.getAuth();
            var that = this;
            if (!authData) {
                this.ref.authAnonymously(function(error, authData) {
                    if (error) {
                        console.log("Login Failed!", error);
                    } else {
                        that.setuserProfileData(authData, formData);
                    }
                });
            } else {
                this.setuserProfileData(authData, formData);
            }

        },
        setuserProfileData: function (authData, formData) {
            this.ref.child('user-profiles').child(authData.uid).set(formData, function(error){
                    if (error) {
                        console.log("problem in inserting user data", error);
                    } else {
                        console.log("successfully inserted user data");
                    }
                });
        },
        changeUserPassword: function () {
            var authData = this.ref.getAuth();
            var email = MGF.getEmail(authData);

            this.ref.changePassword({
                email: email,
                oldPassword: $('#old_password').val(),
                newPassword: $('#new_password').val()
            }, function(error) {
                if (error) {
                    switch (error.code) {
                        case "INVALID_PASSWORD":
                            $('#error').html("The specified user account password is incorrect.");
                            $('#error_row').css("display", "block");
                            console.log("The specified user account password is incorrect.");
                            break;
                        case "INVALID_USER":
                            $('#error').html("The specified user account does not exist.");
                            $('#error_row').css("display", "block");
                            console.log("The specified user account does not exist.");
                            break;
                        default:
                            $('#error').html("Error changing password:", error);
                            $('#error_row').css("display", "block");
                            console.log("Error changing password:", error);
                    }
                } else {
                    $('#success').html("User password changed successfully!");
                    $('#success_row').css("display", "block");
                    console.log("User password changed successfully!");
                }
            });
        },
        deactivateUserAccount: function () {
            var authData = this.ref.getAuth();
            var email = MGF.getEmail(authData);

            this.ref.removeUser({
                email: email,
                password: $('#deactivate_password').val()
            }, function(error) {
                if (error) {
                    switch (error.code) {
                        case "INVALID_USER":
                            $('#deactiate_error').html("The specified user account does not exist.");
                            $('#deactiate_error_row').css("display", "block");
                            console.log("The specified user account does not exist.");
                            break;
                        case "INVALID_PASSWORD":
                            $('#deactiate_error').html("The specified user account password is incorrect.");
                            $('#deactiate_error_row').css("display", "block");
                            console.log("The specified user account password is incorrect.");
                            break;
                        default:
                            $('#deactiate_error').html("Error removing user:", error);
                            $('#deactiate_error_row').css("display", "block");
                            console.log("Error removing user:", error);
                    }
                } else {
                    $('#deactiate_success').html("User account deleted successfully!");
                    $('#deactiate_success_row').css("display", "block");
                    console.log("User account deleted successfully!");
                }
            });
        },
        events: {
            "click #save_details": "submit",
            "click #set_password": "changeUserPassword",
            "click #confirm_deactivate": "deactivateUserAccount"
        }

    });
    return UserProfileView;
});
