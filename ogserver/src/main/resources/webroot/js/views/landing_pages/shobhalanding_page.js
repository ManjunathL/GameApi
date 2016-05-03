define([
    'jquery',
    'underscore',
    'backbone',
    'text!/templates/landing_pages/shobhalanding_page.html',
    'cloudinary_jquery',
    '/js/slyutil.js',
    '/js/mgfirebase.js',
    '/js/consultutil.js',
    '/js/analytics.js'
], function($, _, Backbone, shobhalandingPageTemplate, CloudinaryJquery, SlyUtil, MGF, ConsultUtil, Analytics) {
    var ShobhaLandingPageVIew = Backbone.View.extend({
        el: '.page',
        ref: null,
        renderWithUserProfCallback: function(userProfData) {
            $(this.el).html(_.template(shobhalandingPageTemplate)({
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
        }
    });
    return ShobhaLandingPageVIew;
});