/**
 * Created by mygubbi on 14/09/16.
 */
define([
    'jquery',
    'underscore',
    'backbone',
    'text!templates/payment/payment.html',
    'cloudinary_jquery',
    'slyutil',
    'mgfirebase',
    'consultutil',
    'analytics'
], function($, _, Backbone, PaymentPageTemplate, CloudinaryJquery, SlyUtil, MGF, ConsultUtil, Analytics) {
    var PaymentPageVIew = Backbone.View.extend({
        el: '.page',
        ref: null,
        renderWithUserProfCallback: function(userProfData) {
            $(this.el).html(_.template(PaymentPageTemplate)({
                'userProfile': userProfData
            }));
            $.cloudinary.responsive();
        },
        render: function() {
            var authData = this.ref.getAuth();
            MGF.getUserProfile(authData, this.renderWithUserProfCallback);
        },
        initialize: function() {
            this.ref = MGF.rootRef;
            Analytics.apply(Analytics.TYPE_GENERAL);
            $.cloudinary.config({ cloud_name: 'mygubbi', api_key: '492523411154281'});
            _.bindAll(this, 'renderWithUserProfCallback');
        }
    });
    return PaymentPageVIew;
});