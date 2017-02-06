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
    'analytics',
    'text!templates/user_profile/user_profile.html',
    'views/view_manager'
], function ($, _, Backbone, Bootstrap, BootstrapValidator, MGF, Analytics, UserProfileTemplate, VM) {
    var UserProfileView = Backbone.View.extend({
        el: '.page',
        ref: MGF.rootRef,
        refAuth: MGF.refAuth,
        renderWithUserProfCallback: function (userProfData, provider) {

            $(this.el).html(_.template(UserProfileTemplate)({
                'userProfile': userProfData,
                'provider': provider
            }));
            document.title = userProfData.displayName + ' | mygubbi';
        },
        render: function () {
            var authData = this.refAuth.currentUser;
            MGF.getUserProfile(authData, this.renderWithUserProfCallback);
        },
        initialize: function () {
            Analytics.apply(Analytics.TYPE_GENERAL);
            this.listenTo(Backbone, 'user.change', this.handleUserChange);
            _.bindAll(this, 'renderWithUserProfCallback', 'render', 'submit');
        },
        handleUserChange: function () {
            if (VM.activeView === VM.USER_PROFILE) {
                window.location = '/';
            }
        },
        submit: function (e) {
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
            var that = this;
            MGF.updateProfile(formData).then(function () {
                that.render()
            });

        },
        changeUserPassword: function (e) {
            if (e.isDefaultPrevented()) return;
            e.preventDefault();

            var authData = firebase.auth().currentUser;
            var email = authData.email;

            this.ref.changePassword({
                email: email,
                oldPassword: $('#old_password').val(),
                newPassword: $('#new_password').val()
            }, function (error) {
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
                    $('#chng_passwd_form').css("display","none");
                }
            });
        },
        deactivateUserAccount: function (e) {

            if (e.isDefaultPrevented()) return;
            e.preventDefault();

            var authData = this.refAuth.currentUser;
            var that = this;
            var email = MGF.getEmail(authData);

            this.ref.removeUser({
                email: email,
                password: $('#deactivate_password').val()
            }, function (error) {
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
                    setTimeout(function () {
                        that.ref.unauth();
                        window.location = '#';
                    }, 3000);
                    console.log("User account deleted successfully!");
                    $('#dactivate_account_form').css("display","none");

                }
            });
        },
        events: {
            "click #save_details": "submit",
            "submit #changeUserPasswordForm": "changeUserPassword",
            "submit #deactivateUserForm": "deactivateUserAccount"
        }

    });
    return UserProfileView;
});
