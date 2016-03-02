define([
    'jquery',
    'underscore',
    'backbone',
    'bootstrap',
    'cloudinary_jquery',
    'text!/templates/consult/consult.html',
    '/js/mgfirebase.js',
    '/js/consultutil.js',
    '/js/analytics.js',
    'css!/css/consult.css'
], function($, _, Backbone, Bootstrap, CloudinaryJquery, consultTemplate, MGF, ConsultUtil, Analytics) {
    var ConsultView = Backbone.View.extend({
        el: '.page',
        ref: null,
        renderWithUserProfCallback: function(userProfData) {
            $(this.el).html(_.template(consultTemplate)({
                'userProfile': userProfData
            }));
            $.cloudinary.responsive();
        },
        render: function() {
            var authData = this.ref.getAuth();
            MGF.getUserProfile(authData, this.renderWithUserProfCallback);
        },
        initialize: function() {
            Analytics.apply(Analytics.TYPE_GENERAL);
            this.ref = MGF.rootRef;
            $.cloudinary.config({ cloud_name: 'mygubbi', api_key: '492523411154281'});
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

            window.App.router.navigate('/thankyou-consult-page', {
                trigger: true
            });

        },
        events: {
            "submit": "submit"
        }
    });
    return ConsultView;
});