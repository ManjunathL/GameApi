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
    'text!templates/my_account/my_account.html',
    'views/view_manager'
], function ($, jqueryui, _, Backbone, Bootstrap, BootstrapValidator, MGF, Analytics, MyAccount, MyAccountTemplate, VM) {
    var UserProfileView = Backbone.View.extend({
        el: '.page',
        ref: MGF.rootRef,
        refAuth: MGF.refAuth,
        myaccount: null,
        renderWithUserProfCallback: function (userProfData,mynestitems, providerId) {
            var that = this;
            that.myaccount.set({
                'userProfData': userProfData
            }, {
                silent: true
            });

            $(this.el).html(_.template(MyAccountTemplate)({
                'userProfile': userProfData,
                'myNest':mynestitems,
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
            _.bindAll(this, 'renderWithUserProfCallback', 'render', 'submit');
            this.myaccount.on('change', this.render, this);
            this.listenTo(Backbone, 'user.change', this.handleUserChange);
        },
        handleUserChange: function () {
            console.log('handle user change');
            if (VM.activeView === VM.MYACCOUNTPAGE) {
                window.location = '/';
            }
        },
        submit: function (e) {
            if (e.isDefaultPrevented()) return;
            e.preventDefault();

            var displayName = $('#user_display_name').val();
            var emailId = $('#user_email_id').val();
            var phone = $('#user_phone').val();

            $("#saveSuccss").hide();

            if((displayName.trim() == "") || (emailId.trim() == "") || (phone.trim() == "")){
                console.log('---not done---');
                $("#saveErr").show();
                return false;
            }else{
                $("#saveErr").hide();
                var formData = {
                    "displayName": displayName,
                    "profileImage": $('#user_profile_image').attr('src'),
                    "email": emailId,
                    "phone": phone,
                    "altPhone": $('#user_alt_phone').val(),
                    "address": $('#user_address').val(),
                    "occupation": $('#occupation').val(),
                    "hobbies": $('#hobbies').val(),
                    "interest": $('#interest').val(),
                    "crmId": $('#crmId').val(),
                    "city": $('#user_city').val(),
                    "state": $('#user_state').val(),
                    "pinCode": $('#user_pin_code').val()
                };

                var that = this;
                MGF.updateProfile(formData).then(function () {
                    $('.edit_mode').hide();
                    $('.view_mode').show();
                    $('.edit_icon').show();
                    $("#saveSuccss").show();
                    that.render();
                });
            }
        },
        events: {
            "click #save_details": "submit"
        }
    });
    return UserProfileView;
});
