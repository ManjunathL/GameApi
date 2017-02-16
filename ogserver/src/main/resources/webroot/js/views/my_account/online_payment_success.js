/**
 * Created by Smruti on 09/02/16.
 */
define([
    'jquery',
    'jqueryui',
    'underscore',
    'sha512',
    'backbone',
    'bootstrap',
    'bootstrapvalidator',
    'mgfirebase',
    'cloudinary_jquery',
    'text!templates/my_account/online_payment_success.html',
    'analytics',
    'views/view_manager',
    'collections/payments'
], function ($, jqueryui, _, sha512, Backbone, Bootstrap, BootstrapValidator, MGF, CloudinaryJquery, OnlinePaymentSuccessTemplate, Analytics, VM, MyPayments) {
    var OnlinePaymentSuccessView = Backbone.View.extend({
        el: '.page',
        ref: MGF.rootRef,
        refAuth: MGF.refAuth,
        payments:null,
        renderWithUserProfCallback: function(userProfData) {
            $(this.el).html(_.template(OnlinePaymentSuccessTemplate)({
                'userProfile': userProfData
            }));
            $.cloudinary.responsive();
        },
        render: function () {
            var authData = this.refAuth.currentUser;
            console.log('-------------authData in myaccount page--------------------');
            console.log(authData);
            if(authData.email !== null){
                document.getElementById("canlink").href = window.location.href;
                MGF.getUserProfile(authData, this.renderWithUserProfCallback);
            }else{
                window.location = '/';
            }
        },
        initialize: function() {
            this.ref = MGF.rootRef;
            this.refAuth = MGF.refAuth;
            this.payments = new MyPayments();
            Analytics.apply(Analytics.TYPE_GENERAL);
            $.cloudinary.config({ cloud_name: 'mygubbi', api_key: '492523411154281'});
            _.bindAll(this, 'renderWithUserProfCallback');
        }
    });
    return OnlinePaymentSuccessView;
});
