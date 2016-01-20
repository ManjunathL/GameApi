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
        renderWithUserProfCallback: function(userProfData) {
            $(this.el).html(_.template(UserProfileTemplate)({'userProfile': userProfData}));
            console.log(userProfData);
        },
        render: function() {
            var authData = this.ref.getAuth();
            MGF.getUserProfile(authData, this.renderWithUserProfCallback);
            console.log('simple render' + JSON.stringify(authData));
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
        events: {
            "click #save_details": "submit"
        }


        //},
        //initialize: function() {
        //    this.users.on("add", this.render, this);
        //    this.users.on("reset", this.render, this);
        //}
    });
    return UserProfileView;
});
