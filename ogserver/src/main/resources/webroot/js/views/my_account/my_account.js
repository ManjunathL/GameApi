/**
 * Created by Smruti on 29/08/16.
 */
define([
    'jquery',
    'underscore',
    'backbone',
    'bootstrap',
    'bootstrapvalidator',
    'mgfirebase',
    'analytics',
    'models/myAccount',
    'text!templates/my_account/my_account.html',
    'text!templates/my_account/my_nest.html',
    'text!templates/my_account/my_profile.html',
    'text!templates/my_account/my_settings.html',
    'text!templates/my_account/my_message.html',
    'views/view_manager',
    'models/proposal',
    'collections/mynests'
], function ($, _, Backbone, Bootstrap, BootstrapValidator, MGF, Analytics, MyAccount, MyAccountTemplate, MyNestTemplate, MyProfileTemplate, MySettingsTemplate, MyMessageTemplate, VM, Proposal, MyNests) {
    var UserProfileView = Backbone.View.extend({
        el: '.page',
        ref: MGF.rootRef,
        refAuth: MGF.refAuth,
        myaccount: null,
        proposal: null,
        mynests:null,
        renderWithUserProjectCallback: function (userProfData,mynestitems, providerId) {
            var that = this;
            console.log("-----------mynestitems-------------")
            console.log(mynestitems);
            var project_statusArr = new Array();
            if(typeof(mynestitems) !== 'undefined' && mynestitems !== null){
                if(typeof(mynestitems.paymentDetails) !== 'undefined' && mynestitems.paymentDetails !== null){

                    var project_status = mynestitems.paymentDetails[0].sales_stage;
                    var client_project_status_c = mynestitems.paymentDetails[0].client_project_status_c;

                    switch (project_status) {
                        case "PROSPECT":case "FLOOR_PLAN_UPLOADED":case "INITIAL_PROPOSAL_UPLOADED":case "INITIAL_PROPOSAL_SENT":
                            project_statusArr = ["initiated"];
                            break;
                        case "COLLECT_BOOKING_AMOUNT":case "COLLECTED_BOOKING_AMOUNT":case "SITE_MEASUREMENT_UPLOADED":case "DETAILED_DESIGN_APPROVED ":case "FINAL_PROPOSAL_UPLOADED":case "FINAL_PROPOSAL_SENT":case "COLLECT_ORDER_AMOUNT":
                            project_statusArr = ["initiated","proposal approved"];
                            break;
                        case "Closed_Won":
                            project_statusArr = ["initiated","proposal approved","order placed"];
                            break;
                    }
                    if(typeof(client_project_status_c) != 'undefined' && client_project_status_c != null){

                        switch (client_project_status_c) {
                            case "Project Initiated":case "Upload Scope Document - Conduct 'Kick off' meeting --Completed":case "Upload Workshop Drawing --Completed":case "Upload Prod drawing signed-off by customer --Completed":case "Upload Pre-Installation checklist & list site work --Completed":case "Complete Preinstallation site work --Completed":case "Generate SO extract & update ERP --Completed":
                                project_statusArr = ["initiated","proposal approved","order placed"];
                                break;
                            case "Generate PO extract & update ERP --Completed":case "Update PO details in CRM & Confirm delivery dates --Completed":case "Update Product readiness for inspection  --Completed":case "Upload the QC report post vendor site inspection  --Completed":case "Final Payment Collection - completed":case "Update GRNs against the POs --Completed":case "Upload product photos to confirm readiness for Delivery --Completed":case "Update DO completion status --Completed":case "Upload Invoices --Completed":case "Upload Packing list and Accessories list --Completed":case "Update Site delivery status --Completed":
                                  project_statusArr = ["initiated","proposal approved","order placed","production started"];
                                  break;
                            case "Update Site Installation status --Completed":case "Upload Snaglist and confirm QC completion  --Completed":
                                  project_statusArr = ["initiated","proposal approved","order placed","production started","installation"];
                                  break;
                            case "Upload Handover document and update Project Closure status --Completed":case "Project Completed":
                                  project_statusArr = ["initiated","proposal approved","order placed","production started","installation","handed over"];
                                  break;
                        }
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

            $(this.el).html(_.template(MyAccountTemplate)({
                'userProfile': userProfData,
                'myNest':mynestitems,
                'providerId': providerId
            }));

            $("#profile").html(_.template(MyProfileTemplate)({
                'userProfile': that.myaccount.get("userProfData"),
                'myNest':that.myaccount.get("mynest"),
                'providerId': that.myaccount.get("providerId")
            }));

            document.title = userProfData.displayName + ' | mygubbi';
        },
        render: function () {
            var authData = this.refAuth.currentUser;
            console.log('-------------authData in myaccount page--------------------');
            console.log(authData);

            MGF.mynest(authData,this.renderWithUserProjectCallback);

            setTimeout(
                $('.page').append("<img id='loadico' src='https://res.cloudinary.com/mygubbi/image/upload/v1470959542/home/new_design/mygubbi.gif' class='page-tran'>")
            , 2000);

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
        fetchMynestAndRender: function(mynestProf) {
            var that = this;
            var newProf = that.mynests;
            newProf = newProf.toJSON();

            $("#loadico").hide();
        },
        initialize: function () {
            this.myaccount = new MyAccount();
            this.mynests = new MyNests();
            this.proposal = new Proposal();
            Analytics.apply(Analytics.TYPE_GENERAL);
            _.bindAll(this, 'renderWithUserProjectCallback', 'render', 'submit');
            this.myaccount.on('change', this.render, this);
            this.listenTo(Backbone, 'user.change', this.handleUserChange);
        },
        handleUserChange: function () {
            console.log('handle user change');
            if (VM.activeView === VM.MYACCOUNTPAGE) {
                window.location = '/';
            }
        },
        submit: function (e) {
            if (e.isDefaultPrevented()) return;
            e.preventDefault();


            var formData = {
                "displayName": $('#user_display_name').val(),
                "profileImage": profileImage,
                "email": $('#user_email_id').val(),
                //"dob": $('#user_dob').val(),
                "phone": $('#user_phone').val(),
                "altPhone": $('#user_alt_phone').val(),
                "address": $('#user_address').val(),
                "occupation": $('#occupation').val(),
                "hobbies": $('#hobbies').val(),
                "interest": $('#interest').val(),
                "crmId": $('#crmId').val(),
                "city": $('#user_city').val(),
                "state": $('#user_state').val(),
                "pinCode": $('#user_pin_code').val()
            };
            console.log(formData);

            var that = this;
            MGF.updateProfile(formData).then(function () {
                that.render()
            });

        },
        submitPropertyDetails: function (e) {
            if (e.isDefaultPrevented()) return;
            e.preventDefault();

            var formData = {
                "property-name": $('#property-name').val(),
                "property-type": $('#property-type').val(),
                "blockno": $('#blockno').val(),
                "builder_name": $('#builder_name').val(),
                "flatno": $('#flatno').val()
            };

            var that = this;
            MGF.updatePropertyDetails(formData).then(function () {
                that.render()
            });

        },
        changeUserPassword: function (e) {
            if (e.isDefaultPrevented()) return;
            e.preventDefault();

            var authData = this.refAuth.currentUser;
            var email = MGF.getEmail(authData);
            var oldpassword = $('#old_password').val();
            var newPassword = $('#new_password').val();

            if((newPassword != null) && (oldpassword != newPassword)){
               //authData.reauthenticate(firebase.auth.EmailAuthProvider.credential(email, oldpassword));

               authData.updatePassword(newPassword).then(function() {
                    // Update successful.
                    $('#success').html("User password changed successfully!");
                    $('#success_row').css("display", "block");
                    console.log("User password changed successfully!");
                  }, function(error) {
                    // An error happened.
                    switch (error.code) {
                        case "INVALID_PASSWORD":
                            $('#error').html("The specified user account password is incorrect.");
                            $('#error_row').css("display", "block");
                            console.log("The specified user account password is incorrect.");
                            break;
                        case "INVALID_USER":
                            $('#error').html("The specified user account does not exist.");
                            $('#error_row').css("display", "block");
                            console.log("The specified user account does not exist.");
                            break;
                        default:
                            $('#error').html("Error changing password:", error);
                            $('#error_row').css("display", "block");
                            console.log("Error changing password:", error);
                    }
                  });
            }else{
                $('#error').html("Error changing password:", error);
                $('#error_row').css("display", "block");
                console.log("Error changing password:", error);
            }
        },
        deactivateUserAccount: function (e) {

            if (e.isDefaultPrevented()) return;
            e.preventDefault();

            var authData = this.refAuth.currentUser;
            var that = this;
            var email = MGF.getEmail(authData);

            this.ref.removeUser({
                email: email,
                password: $('#deactivate_password').val()
            }, function (error) {
                if (error) {
                    switch (error.code) {
                        case "INVALID_USER":
                            $('#deactiate_error').html("The specified user account does not exist.");
                            $('#deactiate_error_row').css("display", "block");
                            console.log("The specified user account does not exist.");
                            break;
                        case "INVALID_PASSWORD":
                            $('#deactiate_error').html("The specified user account password is incorrect.");
                            $('#deactiate_error_row').css("display", "block");
                            console.log("The specified user account password is incorrect.");
                            break;
                        default:
                            $('#deactiate_error').html("Error removing user:", error);
                            $('#deactiate_error_row').css("display", "block");
                            console.log("Error removing user:", error);
                    }
                } else {
                    $('#deactiate_success').html("User account deleted successfully!");
                    $('#deactiate_success_row').css("display", "block");
                    setTimeout(function () {
                        that.ref.unauth();
                        window.location = '#';
                    }, 3000);
                    console.log("User account deleted successfully!");
                    $('#dactivate_account_form').css("display","none");

                }
            });
        },
        events: {
            "click .myaccount-lnk a": "changeMyaccounttab",
            "click #save_details": "submit",
            "click #approvebtn": "changeapprove",
            "click #callback": "requestcall",
            "click #save_property_details": "submitPropertyDetails",
            "click #profile-file-input": "changeProfileImg",
            "submit #changeUserPasswordForm": "changeUserPassword",
            "submit #deactivateUserForm": "deactivateUserAccount"

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
                        console.log(" --------------- proposal response ----------------");
                        console.log(response);
                        if(response){
                            $("#reqCallbk_successMsg").fadeIn();
                            $(".salesStage").fadeOut(1000);
                            $('#reqCallbk_successMsg').fadeOut(5000);
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
                    console.log(" --------------- proposal response ----------------");
                    console.log(response);
                    if(response){

                        $("#approve_successMsg").fadeIn();
                        $("#approvebtn").addClass('disabled');
                        $(".salesStage").fadeOut(1000);
                        $('#approve_successMsg').fadeOut(5000);
                    }
                },
                error: function(model, response, options) {
                    console.log("couldn't fetch data - " + response);
                }
            });
            return false;
        },
        changeMyaccounttab: function(e) {
            e.preventDefault();
            var currentTarget = $(e.currentTarget);
            var id = $(e.currentTarget).attr('id');
            $(".myaccount-lnk").removeClass('active');
            $(".myaccount-lnk-content").css('color','#000');
            $(".myaccount-lnk-img").css('display','none');
            $("#"+id).addClass('active');
            $("#"+id+"-img").css('display','inline-block');
            $("#"+id+"-content").css('color','#f15a23');
            $(".myaccount-lnk-img_def").css('display','inline-block');
            $("#"+id+"-img_def").css('display','none');
            //return this;

            if(id == "profile-lnk"){
                var that = this;
                $("#profile").html(_.template(MyProfileTemplate)({
                    'userProfile': that.myaccount.get("userProfData"),
                    'myNest':that.myaccount.get("mynest"),
                    'providerId': that.myaccount.get("providerId")
                }));
            }
            if(id == "mynest-lnk"){
                var that = this;
                $("#mynest").html(_.template(MyNestTemplate)({
                    'userProfile': that.myaccount.get("userProfData"),
                    'myNest':that.myaccount.get("mynest"),
                    'providerId': that.myaccount.get("providerId"),
                    'projectStatusArr':that.myaccount.get("projectStatusArr")
                }));
            }
            if(id == "settings-lnk"){
                var that = this;
                $("#settings").html(_.template(MySettingsTemplate)({
                    'userProfile': that.myaccount.get("userProfData"),
                    'myNest':that.myaccount.get("mynest"),
                    'providerId': that.myaccount.get("providerId"),
                    'projectStatusArr':that.myaccount.get("projectStatusArr")
                }));
            }
            /*if(id == "message-lnk"){
                var that = this;
                $("#message").html(_.template(MyMessageTemplate)({
                    'userProfile': that.myaccount.get("userProfData"),
                    'myNest':that.myaccount.get("mynest"),
                    'providerId': that.myaccount.get("providerId"),
                    'projectStatusArr':that.myaccount.get("projectStatusArr")
                }));
            }*/
        }

    });
    return UserProfileView;
});
