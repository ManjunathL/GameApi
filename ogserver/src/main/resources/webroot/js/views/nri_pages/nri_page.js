/**
 * Created by sakshi on 18/4/16.
 */

define([
    'jquery',
    'underscore',
    'backbone',
    'bootstrap',
    'text!/templates/nri_pages/nri_page.html',
    'cloudinary_jquery',
    '/js/mgfirebase.js',
    '/js/consultutil.js',
    '/js/analytics.js'
], function($, _, Backbone, Bootstrap, nriPageTemplate, CloudinaryJquery, MGF, ConsultUtil, Analytics) {
    var NriPageVIew = Backbone.View.extend({
        el: '.page',
        ref: MGF.rootRef,

        renderWithUserProfCallback: function(userProfData) {
            $(this.el).html(_.template(nriPageTemplate)({
                'userProfile': userProfData
            }));
            $.cloudinary.responsive();
        },

        render: function() {
            var authData = this.ref.getAuth();
            MGF.getUserProfile(authData, this.renderWithUserProfCallback);
            this.ready();
        },

        initialize: function() {
            Analytics.apply(Analytics.TYPE_GENERAL);
            $.cloudinary.config({ cloud_name: 'mygubbi', api_key: '492523411154281'});
            _.bindAll(this, 'renderWithUserProfCallback');
        },
        submit: function(e) {
            if (e.isDefaultPrevented()) return;
            e.preventDefault();

            var name = $('#nri_contact_full_name').val();
            var email = $('#nri_contact_email_id').val();
            var phone = $('#nri_contact_contact_num').val();

            ConsultUtil.submit(name, email, phone, null, null, null, null, null);

            window.App.router.navigate('/thankyou-contact-banner', {
                trigger: true
            });

        },
        events: {
            "submit #nri_contactForm": "submit"
        }
    });
    return NriPageVIew;
});