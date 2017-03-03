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
    'text!templates/my_account/online_payment.html',
    'analytics',
    'views/view_manager',
    'collections/payments'
], function ($, jqueryui, _, sha512, Backbone, Bootstrap, BootstrapValidator, MGF, CloudinaryJquery, OnlinePaymentTemplate, Analytics, VM, MyPayments) {
    var OnlinePaymentView = Backbone.View.extend({
        el: '.page',
        ref: MGF.rootRef,
        refAuth: MGF.refAuth,
        payments:null,
        renderWithUserProfCallback: function(userProfData) {
            $(this.el).html(_.template(OnlinePaymentTemplate)({
                'userProfile': userProfData
            }));
            $.cloudinary.responsive();
        },
        render: function () {
            var authData = this.refAuth.currentUser;
            document.getElementById("canlink").href = window.location.href;
            MGF.getUserProfile(authData, this.renderWithUserProfCallback);
        },
        events: {
            "click #send_payu": "makePayment"
        },
        makePayment: function(e){
            e.preventDefault();
            if($("#payamount").val() == '' || $("#payamount").val().trim() == ''){
                console.log('---------g----------------');
                $("#payamount").focus();
                $("#payamountlbl").text('Please enter a amount.');
                return false;
            }

            var regexp = /^(\d*([.,](?=\d{1,2}))?\d+)+((?!\2)[.,]\d\d)?$/;

            if (!regexp.test($("#payamount").val())) {
               $("#payamount").focus();
               $("#payamountlbl").text('Please enter a valid amount.');
            }

            $("#payamountlbl").text('');

            var authData = this.refAuth.currentUser;
            var uid = authData.uid;
            var email = authData.email;

            if(email !== null){
                var firstname = $("#firstname").val();

                var phone = $("#phone").val();
                var OpportunityId = $("#udf1").val();
                var trnxId = $("#trnxid").val();
                var productinfo = "Mygubbi Modular Furniture";
                var amount = $("#payamount").val();
                var salesExcmail = $("#salesExcmail").val();

                var paymentData = {
                   "key": merchantKey,
                   "txnid": trnxId,
                   "amount": amount,
                   "productinfo": productinfo,
                   "firstname": firstname,
                   "email": email,
                   "phone": phone,
                   "udf1": OpportunityId,
                   "udf2": uid,
                   "udf3": salesExcmail,
                   "surl": successbaseUrl,
                   "furl": failurebaseUrl,
                   "hash": "",
                   "service_provider": "payu"
               };

               $("payerror").hide();

                var hashKey = this.generateHashkey(paymentData);

                if(hashKey){
                    $("#udf2").val(uid);
                    $("#udf3").val(salesExcmail);
                    $("#vamount").val(amount);
                    $("#hashkey").val(hashKey);
                    $("#payuform").submit();
                    return false;
                }
            }else{
                window.location = '/';
            }
        },
        generateHashkey: function(paymentData){
            var hashSequence ="key|txnid|amount|productinfo|firstname|email|udf1|udf2|udf3|udf4|udf5|udf6|udf7|udf8|udf9|udf10";
            var hashVarsSeq = hashSequence.split('|');
            var hash_string = new Array();

            for(var i=0; i < hashVarsSeq.length; i++) {
                var ss = hashVarsSeq[i];
                hash_string += paymentData[ss] ? paymentData[ss] : '';
                hash_string += '|';
            }
            hash_string += SALT;

            var hash = sha512(hash_string);
                hash = hash.toLowerCase();
            return hash;
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
    return OnlinePaymentView;
});
