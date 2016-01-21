define([
    'jquery',
    'underscore',
    'backbone',
    'bootstrap',
    'mgfirebase',
    'text!templates/dashboard/page.html'
], function($, _, Backbone, Bootstrap, MGF, dashboardPageTemplate){
    var DashboardPage = Backbone.View.extend({
        el: '.page',
        ref: MGF.rootRef,

        renderWithUserProfCallback: function(userProfData) {
            $(this.el).html(_.template(dashboardPageTemplate)({
              'userProfile': userProfData
            }));
        },

        render: function() {
            $(this.el).html(dashboardPageTemplate);
            var authData = this.ref.getAuth();
            MGF.getUserProfile(authData, this.renderWithUserProfCallback);
            //console.log('simple render' + JSON.stringify(authData));
        },
        initialize: function() {
            _.bindAll(this, 'renderWithUserProfCallback');
        },
        submit: function(e) {
            if (e.isDefaultPrevented()) return;
            e.preventDefault();

            var formData = {
                "fullName": $('#banner_contact_full_name').val(),
                "email": $('#banner_contact_email_id').val(),
                "contactNumber": $('#banner_contact_contact_num').val(),
                "requirements": $('#banner_contact_requirement').val(),
                "propertyName": $('#banner_contact_property_name').val()
            };

            var authData = this.ref.getAuth();
            var that = this;
            if (!authData) {
                this.ref.authAnonymously(function(error, authData) {
                    if (error) {
                        console.log("Login Failed!", error);
                    } else {
                        that.setConsultData(authData, formData);
                    }
                });
            } else {
                this.setConsultData(authData, formData);
            }

            $('#banner_contactForm').hide('slow');
            $('#banner_success-msg').show('slow');
            $('#banner_success-msg-padding').show('slow');


        },
        setConsultData: function(authData, formData) {
            MGF.addConsultData(authData, formData);
            MGF.pushEvent(authData.uid, formData, MGF.TYPE_CONSULT);
        },
        events: {
            "submit": "submit"
        }
    });
    return DashboardPage;
});
