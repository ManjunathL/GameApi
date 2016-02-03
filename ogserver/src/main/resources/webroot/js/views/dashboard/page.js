define([
    'jquery',
    'underscore',
    'backbone',
    'bootstrap',
    '/js/mgfirebase.js',
    '/js/consultutil.js',
    'text!/templates/dashboard/page.html'
], function($, _, Backbone, Bootstrap, MGF, ConsultUtil, dashboardPageTemplate){
    var DashboardPage = Backbone.View.extend({
        el: '.page',
        ref: MGF.rootRef,

        renderWithUserProfCallback: function(userProfData) {
            $(this.el).html(_.template(dashboardPageTemplate)({
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

            var name = $('#banner_contact_full_name').val();
            var email = $('#banner_contact_email_id').val();
            var phone = $('#banner_contact_contact_num').val();
            var propertyName = $('#banner_contact_property_name').val();
            var query = $('#banner_contact_requirement').val();
            var floorplan = $("#banner_contact_floorplan").prop('files')[0];

            ConsultUtil.submit(name, email, phone, query, floorplan, propertyName);

            $('#banner_contactForm').hide('slow');
            $('#banner_success-msg').show('slow');
            $('#banner_success-msg-padding').show('slow');
        },
        events: {
            "submit #banner_contactForm": "submit"
        }
    });
    return DashboardPage;
});
