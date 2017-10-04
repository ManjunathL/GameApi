/**
 * Created by Smruti on 27/02/17.
 */
define([
    'jquery',
    'jqueryui',
    'timepicker',
    'underscore',
    'backbone',
    'bootstrap',
    'bootstrapvalidator',
    'mgfirebase',
    'analytics',
    'models/myAccount',
    'text!templates/my_account/my_nest.html',
    'text!templates/my_account/payment_history.html',
    'views/view_manager',
    'models/proposal',
    'collections/mynests'
], function ($, jqueryui, timepicker, _, Backbone, Bootstrap, BootstrapValidator, MGF, Analytics, MyAccount, MyNestTemplate, PaymentHistoryTemplate, VM, Proposal, MyNests) {
    var MyNestView = Backbone.View.extend({
        el: '.page',
        ref: MGF.rootRef,
        refAuth: MGF.refAuth,
        myaccount: null,
        proposal: null,
        mynests:null,
        renderWithUserProjectCallback: function (userProfData,mynestitems, providerId) {
            var that = this;
            var project_statusArr = new Array();
            if(typeof(mynestitems) !== 'undefined' && mynestitems !== null){
                if(typeof(mynestitems.paymentDetails) !== 'undefined' && mynestitems.paymentDetails !== null){

                    var project_status = mynestitems.paymentDetails[0].sales_stage;
                    var client_project_status_c = mynestitems.paymentDetails[0].client_project_status_c;

                    switch (project_status) {
                        case "PROSPECT":case "FLOOR_PLAN_UPLOADED":case "INITIAL_PROPOSAL_UPLOADED":case "INITIAL_PROPOSAL_SENT":case "COLLECT_BOOKING_AMOUNT":case "On_Hold":
                            project_statusArr = ["Proposal"];
                            break;
                        case "COLLECTED_BOOKING_AMOUNT":case "SITE_MEASUREMENT_UPLOADED":case "DETAILED_DESIGN_APPROVED":case "FINAL_PROPOSAL_UPLOADED":case "FINAL_PROPOSAL_SENT":case "COLLECT_ORDER_AMOUNT":case "PRODUCTION_DRAWING":
                            project_statusArr = ["Proposal","Booked"];
                            break;
                        case "Closed_Won":
                            project_statusArr = ["Proposal","Booked","Confirmed"];
                            break;
                        case "Closed_Lost":
                            project_statusArr = ["Proposal","Booked","Confirmed","Production","Installation","Closed"];
                            break;
                        case "PLANNING":case "PRODUCTION":
                            project_statusArr = ["Proposal","Booked","Confirmed","Production"];
                            break;
                        case "INSTALLATION":case "SNAGS":case "PROJECT_HANDOVER":
                            project_statusArr = ["Proposal","Booked","Confirmed","Production","Installation"];
                            break;
                        case "WARRANTY_HANDOVER":case "Closed":
                            project_statusArr = ["Proposal","Booked","Confirmed","Production","Installation","Closed"];
                            break;
                    }
                }
            }

            that.myaccount.set({
                'userProfData': userProfData
            }, {
                silent: true
            });

            that.myaccount.set({
                'projectStatusArr': project_statusArr
            }, {
                silent: true
            });

            that.myaccount.set({
                'mynest': mynestitems
            }, {
                silent: true
            });

            that.myaccount.set({
                'providerId': providerId
            }, {
                silent: true
            });

            $(this.el).html(_.template(MyNestTemplate)({
                'userProfile': that.myaccount.get("userProfData"),
                'myNest':that.myaccount.get("mynest"),
                'providerId': that.myaccount.get("providerId"),
                'projectStatusArr':that.myaccount.get("projectStatusArr")
            }));

            document.title = userProfData.displayName + ' | mygubbi';
        },
        render: function () {
            var authData = this.refAuth.currentUser;
            document.getElementById("canlink").href = window.location.href;

            MGF.mynest(authData,this.renderWithUserProjectCallback);

            MGF.getTransactionDetails(authData,this.renderTranscationDetails);

            /*setTimeout(
                $('.page').append("<img id='loadico' src='https://res.cloudinary.com/mygubbi/image/upload/v1470959542/home/new_design/mygubbi.gif' class='page-tran'>")
            , 2000);*/

            var that = this;
            var mynestProf = authData.email;
            this.mynests.fetch({
                 data: {
                     "emailId": mynestProf
                 },
                success: function(response) {
                   that.fetchMynestAndRender(mynestProf);
                },
                error: function(model, response, options) {
                    console.log("couldn't fetch getopportunity data - " + response);
                }
            });


        },
        renderTranscationDetails: function(transdetails){
            var that = this;
            if(transdetails) {
                transdetails = _(transdetails).sortBy(function(trnsc) {
                    return Date.parse(trnsc.addedon);
                }).reverse();

                that.myaccount.set({
                    'transdetails': transdetails
                }, {
                    silent: true
                });
            }
        },
        fetchMynestAndRender: function(mynestProf) {
            var that = this;
            var newProf = that.mynests;
            newProf = newProf.toJSON();

            //$("#loadico").hide();
        },
        initialize: function () {
            this.myaccount = new MyAccount();
            this.mynests = new MyNests();
            this.proposal = new Proposal();
            Analytics.apply(Analytics.TYPE_GENERAL);
            _.bindAll(this, 'renderWithUserProjectCallback', 'render','renderTranscationDetails');
            this.myaccount.on('change', this.render, this);
            this.listenTo(Backbone, 'user.change', this.handleUserChange);
        },
        handleUserChange: function () {
            console.log('handle user change');
            if (VM.activeView === VM.MYACCOUNTPAGE) {
                window.location = '/';
            }
        },
        events: {
            "click #approvebtn": "changeapprove",
            "click #callback": "requestcall",
            "click #onlinepay": "showPaymentHistory"

        },
        showPaymentHistory: function(e) {
            if (e.isDefaultPrevented()) return;
            e.preventDefault();
            var that = this;

            var paydtls = {};
            var paydtlsobj = [];
            if(that.myaccount.get("transdetails")){
                $.each(that.myaccount.get("transdetails"), function(i, data) {
                    var paydtls = {};
                    paydtls['payAmount'] = data.amount;
                    paydtls['payDate'] = data.addedon;
                    paydtls['payMode'] = data.mode;
                    paydtls['payTransId'] = data.txnid;
                    paydtls['status'] = data.status;
                    paydtlsobj.push(paydtls);
                });
            }
            var nesttt = that.myaccount.get("mynest");

            if(typeof(nesttt.paymentDetails[0].m1_amount_collected_c) !== 'undefined'){
                var nestpaydtls = {};
                nestpaydtls['payAmount'] = nesttt.paymentDetails[0].m1_amount_collected_c;
                nestpaydtls['payDate'] = nesttt.paymentDetails[0].m1_payment_date_c;
                nestpaydtls['payMode'] = nesttt.paymentDetails[0].m1_mode_of_payment_c;
                nestpaydtls['payTransId'] = nesttt.paymentDetails[0].payment_reference1_c;
                nestpaydtls['status'] = 'Success';
                paydtlsobj.push(nestpaydtls);
            }
            if(typeof(nesttt.paymentDetails[0].m2_amount_collected_c) !== 'undefined'){
                var nestpaydtls2 = {};
                nestpaydtls2['payAmount'] = nesttt.paymentDetails[0].m2_amount_collected_c;
                nestpaydtls2['payDate'] = nesttt.paymentDetails[0].m2_payment_date_c;
                nestpaydtls2['payMode'] = nesttt.paymentDetails[0].m2_mode_of_payment_c;
                nestpaydtls2['payTransId'] = nesttt.paymentDetails[0].payment_reference2_c;
                nestpaydtls2['status'] = 'Success';
                paydtlsobj.push(nestpaydtls2);
            }
            if(typeof(nesttt.paymentDetails[0].m3_amount_collected_c) !== 'undefined'){
                var nestpaydtls3 = {};
                nestpaydtls3['payAmount'] = nesttt.paymentDetails[0].m3_amount_collected_c;
                nestpaydtls3['payDate'] = nesttt.paymentDetails[0].m3_payment_date_c;
                nestpaydtls3['payMode'] = nesttt.paymentDetails[0].m3_mode_of_payment_c;
                nestpaydtls3['payTransId'] = nesttt.paymentDetails[0].payment_reference3_c;
                nestpaydtls3['status'] = 'Success';
                paydtlsobj.push(nestpaydtls3);
            }

            paydtlsobj = _(paydtlsobj).sortBy(function(payhisdtls) {
                return Date.parse(payhisdtls.payDate);
            }).reverse();

            var payHisTemp = _.template(PaymentHistoryTemplate);
            $("#payHistory").html(payHisTemp({
                'paydtlsobj':paydtlsobj
            }));
        },
        requestcall: function(e) {
            if (e.isDefaultPrevented()) return;
            e.preventDefault();

            $("#requestCB_error").text('');
            if(($('#user_date').val().trim() == "") && ($('#user_time').val().trim() == "")){
                   $("#requestCB_error").text("Please fill out required fields!!!");
                   return false;
            }else{
                var that = this;
                var authData = this.refAuth.currentUser;

                var salesExecEmail = $(e.currentTarget).data('element');
                var customerName = $(e.currentTarget).data('element1');
                var customerphone = $(e.currentTarget).data('element2');
                var designerEmail = $(e.currentTarget).data('element3');
                var crmId = $(e.currentTarget).data('element4');
                var taskType = $(e.currentTarget).data('element5');
                var scheduleTime = $('#user_date').val() + ' ' + $('#user_time').val();

                var eventData = {
                    proposal_status: "Request for call back",
                    scheduledDate: $('#user_date').val(),
                    scheduledTime: $('#user_time').val()
                };

                //console.log(eventData);
                //return false;

                that.proposal.fetch({
                    data: {
                     "emailId": salesExecEmail,
                     "status":"Deferred",
                     "customerName":customerName,
                     "customerPhone":customerphone,
                     "designerEmail":designerEmail,
                     "crmId":crmId,
                     "taskType":taskType,
                     "scheduleTime":scheduleTime
                    },
                    success: function(response) {
                        if(response){
                            $("#reqCallbk_successMsg").fadeIn();
                            $(".salesStage").fadeOut(1000);
                            $('#reqCallbk_successMsg').fadeOut(10000);
                        }
                    },
                    error: function(model, response, options) {
                        console.log("couldn't fetch data - " + response);
                    }
                });
                return false;
            }
        },
        changeapprove: function(e) {
            if (e.isDefaultPrevented()) return;
            e.preventDefault();

            $("#approve_successMsg").hide();

            var authData = this.refAuth.currentUser;
            console.log(authData);


            var that = this;
            var salesExecEmail = $(e.currentTarget).data('element');
            var customerName = $(e.currentTarget).data('element1');
            var customerphone = $(e.currentTarget).data('element2');
            var designerEmail = $(e.currentTarget).data('element3');
            var crmId = $(e.currentTarget).data('element4');
            var taskType = $(e.currentTarget).data('element5');

            var eventData = {
                proposal_status: "Approve"

            };

            that.proposal.fetch({
                data: {
                 "emailId": salesExecEmail,
                 "status":"Completed",
                 "customerName":customerName,
                 "customerPhone":customerphone,
                 "designerEmail":designerEmail,
                 "crmId":crmId,
                 "taskType":taskType
                },
                success: function(response) {
                    if(response){

                        $("#approve_successMsg").fadeIn();
                        $("#approvebtn").addClass('disabled');
                        $(".salesStage").fadeOut(1000);
                        $('#approve_successMsg').fadeOut(10000);
                    }
                },
                error: function(model, response, options) {
                    console.log("couldn't fetch data - " + response);
                }
            });
            return false;
        }
    });
    return MyNestView;
});
