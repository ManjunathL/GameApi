define([
    'jquery',
    'underscore',
    'backbone',
    'text!/templates/landing_pages/emaillanding_page.html',
    'cloudinary_jquery',
    '/js/slyutil.js',
    '/js/mgfirebase.js',
    '/js/consultutil.js',
    '/js/analytics.js'
], function($, _, Backbone, emaillandingPageTemplate, CloudinaryJquery, SlyUtil, MGF, ConsultUtil, Analytics) {
    var EmailLandingPageVIew = Backbone.View.extend({
        el: '.page',
        ref: null,
        renderWithUserProfCallback: function(userProfData) {
            $(this.el).html(_.template(emaillandingPageTemplate)({
                'userProfile': userProfData
            }));
            $.cloudinary.responsive();
        },
        render: function() {
            var authData = this.ref.getAuth();
            MGF.getUserProfile(authData, this.renderWithUserProfCallback);
            this.ready();
        },
        ready: function() {
            if ($('#lpalt-frame').length > 0) {
                var $lpalt_frame = $('#lpalt-frame');
                var $lpalt_wrap = $lpalt_frame.parent().parent();
                SlyUtil.create($lpalt_wrap, '#lpalt-frame', '.lpalt-next', '.lpalt-prev').init();
            }
            if ($('#brand-frame').length > 0) {
                var $brand_frame = $('#brand-frame');
                var $brand_wrap = $brand_frame.parent().parent();
                SlyUtil.create($brand_wrap, '#brand-frame', '.brand-next', '.brand-prev').init();
            }
        },
        initialize: function() {
            this.ref = MGF.rootRef;
            Analytics.apply(Analytics.TYPE_GENERAL);
            $.cloudinary.config({ cloud_name: 'mygubbi', api_key: '492523411154281'});
            _.bindAll(this, 'renderWithUserProfCallback');
        },
        submit_form1: function(e) {
            if (e.isDefaultPrevented()) return;
            e.preventDefault();

            var name = $('#contact_full_name1').val();
            var email = $('#contact_email_id1').val();
            var phone = $('#contact_contact_num1').val();

            ConsultUtil.submit(name, email, phone, "", "", null, null, null);

            window.App.router.navigate('/thankyou-emaillp-page', {
                trigger: true
            });

        },
        submit_form2: function(e) {
            if (e.isDefaultPrevented()) return;
            e.preventDefault();

            var name = $('#contact_full_name2').val();
            var email = $('#contact_email_id2').val();
            var phone = $('#contact_contact_num2').val();
            var propertyName = $('#contact_property_name2').val();
            var query = $('#contact_requirement2').val();
            var floorplan = $("#contact_floorplan2").prop('files')[0];
            var projectName = $('#contact_project_name2').val();
            var cityName = $('#contact_city_name2').val();

            ConsultUtil.submit(name, email, phone, query, floorplan, propertyName, projectName, cityName);

            window.App.router.navigate('/thankyou-emaillp-page', {
                trigger: true
            });

        },
        events: {
            "submit #contactNwLpForm1": "submit_form1",
            "submit #contactNwLpForm2": "submit_form2"

        }
    });
    return EmailLandingPageVIew;
});