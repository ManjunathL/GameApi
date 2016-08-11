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
        }
    });
    return ConsultView;
});