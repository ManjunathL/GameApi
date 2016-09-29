/**
 * Created by mygubbi on 11/05/16.
 */
define([
    'jquery',
    'underscore',
    'backbone',
    'text!/templates/landing_pages/kitchenlanding_page.html',
    'cloudinary_jquery',
    '/js/mgfirebase.js',
    '/js/consultutil.js',
    '/js/analytics.js'
], function($, _, Backbone, kitchenlandingPageTemplate, CloudinaryJquery, MGF, ConsultUtil, Analytics) {
    var KitchenLandingPageVIew = Backbone.View.extend({
         el: '.page',
                ref: null,
                refAuth: null,
                renderWithUserProfCallback: function(userProfData) {
                    $(this.el).html(_.template(kitchenlandingPageTemplate)({
                        'userProfile': userProfData
                    }));
                    $.cloudinary.responsive();
                },
                render: function() {
                    var authData = this.refAuth.currentUser;
                    MGF.getUserProfile(authData, this.renderWithUserProfCallback);
                },
                initialize: function() {
                    Analytics.apply(Analytics.TYPE_GENERAL);
                    this.ref = MGF.rootRef;
                    this.refAuth = MGF.refAuth;
                    $.cloudinary.config({ cloud_name: 'mygubbi', api_key: '492523411154281'});
                    _.bindAll(this, 'renderWithUserProfCallback');
                },
                /*submit: function(e) {
                    if (e.isDefaultPrevented()) return;
                    e.preventDefault();

                    var name = $('#contact_full_name1').val();
                    var email = $('#contact_email_id1').val();
                    var phone = $('#contact_contact_num1').val();

                    var query = $('#contact_city').val();

                    ConsultUtil.submit(name, email, phone, query, null, null, null, null);

                    window.App.router.navigate('/thankyou-consult-page', {
                        trigger: true
                    });

                },
                events: {
                    "submit": "submit"
                }*/
            });
    return KitchenLandingPageVIew;
});