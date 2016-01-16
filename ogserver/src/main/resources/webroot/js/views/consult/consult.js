define([
    'jquery',
    'underscore',
    'backbone',
    'bootstrap',
    'text!templates/consult/consult.html',
    'mgfirebase',
    'css!../../../css/consult'
], function($, _, Backbone, Bootstrap, consultTemplate, MGF) {
    var ConsultView = Backbone.View.extend({
        el: '.page',
        ref: MGF.rootRef,
        renderWithUserProfCallback: function(userProfData) {
            $(this.el).html(_.template(consultTemplate)({
                'userProfile': userProfData
            }));
        },
        render: function() {
            var authData = this.ref.getAuth();
            MGF.getUserProfile(authData, this.renderWithUserProfCallback);
        },
        initialize: function() {
            _.bindAll(this, 'renderWithUserProfCallback');
        },
        submit: function(e) {
            if (e.isDefaultPrevented()) return;
            e.preventDefault();

            var formData = {
                "fullName": $('#contact_full_name1').val(),
                "email": $('#contact_email_id1').val(),
                "contactNumber": $('#contact_contact_num1').val(),
                "requirements": $('#contact_requirement1').val(),
                "propertyName": $('#contact_property_name1').val()
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

            $('#form-heading').hide('slow');
            $('#contactForm1').hide('slow');
            $('#success-message').show('slow');
            $('#message-padding').show('slow');


        },
        setConsultData: function(authData, formData) {
            MGF.addConsultData(authData, formData);
            MGF.pushEvent(authData.uid, formData, MGF.TYPE_CONSULT);
        },
        events: {
            "submit": "submit"
        }
    });
    return ConsultView;
});