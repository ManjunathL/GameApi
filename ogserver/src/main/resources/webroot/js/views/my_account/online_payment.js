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
        renderWithUserProfCallback: function(userProfData) {
            $(this.el).html(_.template(OnlinePaymentTemplate)({
                'userProfile': userProfData
            }));
            $.cloudinary.responsive();
        },
        render: function () {
            var authData = this.refAuth.currentUser;
            console.log('-------------authData in myaccount page--------------------');
            console.log(authData);
            document.getElementById("canlink").href = window.location.href;

            MGF.getUserProfile(authData, this.renderWithUserProfCallback);

        },
        events: {
            "click #send_payu": "makePayment"
        },
        makePayment: function(){
            var authData = this.refAuth.currentUser;
            var firstname = $("#firstname").val();
            var email = authData.email;
            var phone = $("#phone").val();
            var OpportunityId = $("#udf1").val();
            var trnxId = $("#trnxid").val();
            var productinfo = "test product";

            var paymentData = {
               "key": "gtKFFx",
               "txnid": trnxId,
               "amount": "100",
               "productinfo": productinfo,
               "firstname": firstname,
               "email": email,
               "phone": phone,
               "udf1": OpportunityId,
               "surl": "https://localhost:8787/",
               "furl": "https://localhost:8787/kitchen-cabinet-design",
               "hash": "",
               "service_provider": "payu"
           };

            var hashKey = this.generateHashkey(paymentData);

            console.log(firstname+' ----------- '+email+' ------ '+phone+' ------ '+productinfo+' ------ '+OpportunityId+' ------ '+trnxId);
            //return false;
            if(hashKey){
            //document.getElementById("hash").valu = hashKey;
            $("#hashkey").val(hashKey);
            $("#payuform").submit();
            return false;
            }
        },
        generateHashkey: function(paymentData){
        console.log('----------i m here--------------');
        console.log(paymentData);

            var hashSequence ="key|txnid|amount|productinfo|firstname|email|udf1|udf2|udf3|udf4|udf5|udf6|udf7|udf8|udf9|udf10";
            var SALT = "eCwWELxi";
            var hashVarsSeq = hashSequence.split('|');
            var hash_string = new Array();

            console.log(hashVarsSeq);
            console.log(" ---- hashVarsSeq ----");
            console.log(hashVarsSeq.length);

            for(var i=0; i < hashVarsSeq.length; i++) {
                console.log(hashVarsSeq[i]);
                var ss = hashVarsSeq[i];
                console.log('paymentData.ss - --------------- '+paymentData[ss]);
                hash_string += paymentData[ss] ? paymentData[ss] : '';
                hash_string += '|';
            }
            hash_string += SALT;

            console.log(hash_string);

            var hash = sha512(hash_string);
                hash = hash.toLowerCase();

            console.log(hash);
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
