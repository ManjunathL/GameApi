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
                renderWithUserProfCallback: function(userProfData) {
                    $(this.el).html(_.template(ebookPageTemplate)({
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

                    var name = $('#FirstName').val();
                    var email = $('#EmailAddress').val();

                    if((name.trim() != '') && (email.trim() != '')){
                        var url='http://res.cloudinary.com/mygubbi/raw/upload/v1466509270/E-Book.pdf';
                        window.open(url,'Download');

                        window.App.router.navigate('/thankyou-ebook-page', {
                            trigger: true
                        });
                    }else{
                        alert('Please fill required fields!!!');
                    }
                },
                events: {
                    "submit": "submit"
                }
            });
    return EbookPageVIew;
});