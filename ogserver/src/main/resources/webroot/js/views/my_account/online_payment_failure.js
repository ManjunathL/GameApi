/**
 * Created by Smruti on 09/02/16.
 */
define([
    'jquery',
    'jqueryui',
    'underscore',
    'backbone',
    'bootstrap',
    'bootstrapvalidator',
    'mgfirebase',
    'cloudinary_jquery',
    'text!templates/my_account/online_payment_failure.html',
    'analytics',
    'views/view_manager'
], function ($, jqueryui, _, Backbone, Bootstrap, BootstrapValidator, MGF, CloudinaryJquery, OnlinePaymentFailureTemplate, Analytics, VM) {
    var OnlinePaymentFailureView = Backbone.View.extend({
        el: '.page',
        ref: MGF.rootRef,
        refAuth: MGF.refAuth,
        renderWithUserProfCallback: function(userProfData) {
            var txnid = this.model.txnId;
            $(this.el).html(_.template(OnlinePaymentFailureTemplate)({
                'userProfile': userProfData,
                'txnid':txnid
            }));
            $.cloudinary.responsive();
        },
        render: function () {
            var authData = this.refAuth.currentUser;
            document.getElementById("canlink").href = window.location.href;
            MGF.getUserProfile(authData, this.renderWithUserProfCallback);
        },
        initialize: function() {
            this.ref = MGF.rootRef;
            this.refAuth = MGF.refAuth;
            Analytics.apply(Analytics.TYPE_GENERAL);
            $.cloudinary.config({ cloud_name: 'mygubbi', api_key: '492523411154281'});
            _.bindAll(this, 'renderWithUserProfCallback');
        }
    });
    return OnlinePaymentFailureView;
});