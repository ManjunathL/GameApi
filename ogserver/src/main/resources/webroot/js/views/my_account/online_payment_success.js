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
    'text!templates/my_account/online_payment.html',
    'analytics',
    'views/view_manager',
    'collections/payments'
], function ($, jqueryui, _, sha512, Backbone, Bootstrap, BootstrapValidator, MGF, OnlinePaymentTemplate, Analytics, VM, MyPayments) {
    var OnlinePaymentView = Backbone.View.extend({
        el: '.page',
        ref: MGF.rootRef,
        refAuth: MGF.refAuth,
        payments:null,
        render: function () {
            console.log('inside success page');
            console.log(this.model.msg);
        }
        initialize: function() {
            Analytics.apply(Analytics.TYPE_GENERAL);
            $.cloudinary.config({ cloud_name: 'mygubbi', api_key: '492523411154281'});
            _.bindAll(this, 'renderWithUserProfCallback');
        }
    });
    return OnlinePaymentView;
});
