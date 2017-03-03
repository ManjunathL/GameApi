/**
 * Created by Smruti on 29/08/16.
 */
define([
    'jquery',
    'jqueryui',
    'underscore',
    'backbone',
    'bootstrap',
    'bootstrapvalidator',
    'mgfirebase',
    'analytics',
    'models/myAccount',
    'text!templates/my_account/my_settings.html',
    'views/view_manager'
], function ($, jqueryui, _, Backbone, Bootstrap, BootstrapValidator, MGF, Analytics, MyAccount, MySettingsTemplate, VM) {
    var UserSettingsView = Backbone.View.extend({
        el: '.page',
        ref: MGF.rootRef,
        refAuth: MGF.refAuth,
        myaccount: null,
        renderWithUserProfCallback: function (userProfData, providerId) {
            $(this.el).html(_.template(MySettingsTemplate)({
                'userProfile': userProfData,
                'providerId': providerId
            }));
            document.title = userProfData.displayName + ' | mygubbi';
        },
        render: function () {
            var authData = this.refAuth.currentUser;
            document.getElementById("canlink").href = window.location.href;
            MGF.getUserProfile(authData, this.renderWithUserProfCallback);
        },
        initialize: function () {
            this.myaccount = new MyAccount();
            Analytics.apply(Analytics.TYPE_GENERAL);
            _.bindAll(this, 'renderWithUserProfCallback', 'render');
            this.myaccount.on('change', this.render, this);
            this.listenTo(Backbone, 'user.change', this.handleUserChange);
        },
        handleUserChange: function () {
            console.log('handle user change');
            if (VM.activeView === VM.MYSETTINGSPAGE) {
                window.location = '/';
            }
        },
        changeUserPassword: function (e) {
            if (e.isDefaultPrevented()) return;
            e.preventDefault();

            var authData = this.refAuth.currentUser;
            var email = MGF.getEmail(authData);
            var oldpassword = $('#old_password').val();
            var newPassword = $('#new_password').val();

            if((newPassword != null) && (oldpassword != newPassword)){
               authData.reauthenticate(firebase.auth.EmailAuthProvider.credential(email, oldpassword)).then(function() {
                 authData.updatePassword(newPassword).then(function() {
                        $('#success').html("User password changed successfully!");
                        $('#success_row').css("display", "block");
                        $("#success_row").fadeOut(10000);
                        console.log("User password changed successfully!");
                    }, function(error) {
                        $('#error').html("Error changing password:", error);
                        $('#error_row').css("display", "block");
                        $("#error_row").fadeOut(10000);
                        console.log("Error changing password:", error);
                 });
               }, function(error) {
                    $('#error').html("The specified user account password is incorrect.");
                    $('#error_row').css("display", "block");
                    $("#error_row").fadeOut(10000);
                    console.log("The specified user account password is incorrect.");
               });
            }else{
                $('#error').html("Error changing password:", error);
                $('#error_row').css("display", "block");
                $("#error_row").fadeOut(10000);
                console.log("Error changing password:", error);
            }
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
            "submit #changeUserPasswordForm": "changeUserPassword",
            "submit #deactivateUserForm": "deactivateUserAccount"

        }
    });
    return UserSettingsView;
});
