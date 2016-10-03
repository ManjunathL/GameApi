/**
 * Created by mygubbi on 11/05/16.
 */
define([
    'jquery',
    'underscore',
    'backbone',
    'text!/templates/ebook/ebook.html',
    'cloudinary_jquery',
    '/js/mgfirebase.js',
    '/js/consultutil.js',
    '/js/analytics.js'
], function($, _, Backbone, ebookPageTemplate, CloudinaryJquery, MGF, ConsultUtil, Analytics) {
    var EbookPageVIew = Backbone.View.extend({
         el: '.page',
                ref: null,
                refAuth: null,
                renderWithUserProfCallback: function(userProfData) {
                    $(this.el).html(_.template(ebookPageTemplate)({
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
                submit: function(e) {
                    if (e.isDefaultPrevented()) return;
                    e.preventDefault();

                    var name = $('#FirstName').val();
                    var email = $('#EmailAddress').val();

                    //var url='http://res.cloudinary.com/mygubbi/raw/upload/v1466509270/E-Book.pdf';
                    var url='http://res.cloudinary.com/mygubbi/raw/upload/v1467111480/E-Book_updated.pdf';
                    window.open(url,'Download');

                    window.App.router.navigate('/thankyou-ebook-page', {
                        trigger: true
                    });
                },
                events: {
                    "submit": "submit"
                }
            });
    return EbookPageVIew;
});