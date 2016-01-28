define([
    'jquery',
    'underscore',
    'backbone',
    'bootstrap',
    'text!templates/consult/consult.html',
    'mgfirebase',
    'consultutil',
    'css!../../../css/consult'
], function($, _, Backbone, Bootstrap, consultTemplate, MGF, ConsultUtil) {
    var ConsultView = Backbone.View.extend({
        el: '.page',
        ref: null,
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
            this.ref = MGF.rootRef;
            _.bindAll(this, 'renderWithUserProfCallback');
        },
        submit: function(e) {
            if (e.isDefaultPrevented()) return;
            e.preventDefault();

            var name = $('#contact_full_name1').val();
            var email = $('#contact_email_id1').val();
            var phone = $('#contact_contact_num1').val();
            var propertyName = $('#contact_property_name1').val();
            var query = $('#contact_requirement1').val();
            var floorplan = $("#contact_floorplan1").prop('files')[0];

            ConsultUtil.submit(name, email, phone, query, floorplan, propertyName);

            $('#form-heading').hide('slow');
            $('#contactForm1').hide('slow');
            $('#success-message').show('slow');
            $('#message-padding').show('slow');
        },
        events: {
            "submit": "submit"
        }
    });
    return ConsultView;
});