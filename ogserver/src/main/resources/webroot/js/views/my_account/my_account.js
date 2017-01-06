/**
 * Created by Smruti on 29/08/16.
 */
define([
    'jquery',
    'underscore',
    'backbone',
    'bootstrap',
    'bootstrapvalidator',
    '/js/mgfirebase.js',
    '/js/analytics.js',
    '/js/models/myAccount.js',
    'text!/templates/my_account/my_account.html',
    'text!/templates/my_account/my_nest.html',
    'text!/templates/my_account/my_profile.html',
    'text!/templates/my_account/my_settings.html',
    'text!/templates/my_account/my_message.html',
    '/js/views/view_manager.js',
    '/js/models/proposal.js',
    '/js/collections/mynests.js'
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
        if(typeof(mynestitems) !== 'undefined' && mynestitems !== null){

            var project_status = mynestitems.paymentDetails[0].sales_stage;
            var client_project_status_c = mynestitems.paymentDetails[0].client_project_status_c;
            var project_statusArr = new Array();
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

            console.log('-------------client_project_status_c---------------');
            console.log(client_project_status_c);
            if(typeof(client_project_status_c) != 'undefined' && client_project_status_c != null){

                switch (client_project_status_c) {
                    case "Project Initiated":case "Upload Scope Document - Conduct 'Kick off' meeting - completed":case "Upload Workshop Drawing - completed":case "Upload Prod drawing signed-off by customer - completed":case "Upload Pre-Installation checklist & list site work - completed":case "Complete Preinstallation site work - completed":case "Generate SO extract & update ERP - completed":
                        project_statusArr = ["initiated","proposal approved","order placed"];
                        break;
                    case "Generate PO extract & update ERP - completed":case "Update PO details in CRM & Confirm delivery dates - completed":case "Update Product readiness for inspection  - completed":case "Upload the QC report post vendor site inspection  - completed":case "Final Payment Collection - completed":case "Update GRNs against the POs - completed":case "Upload product photos to confirm readiness for Delivery - completed":case "Update DO completion status - Completed":case "Upload Invoices - Completed":case "Upload Packing list and Accessories list - Completed":case "Update Site delivery status - Completed":
                          project_statusArr = ["initiated","proposal approved","order placed","production started"];
                          break;
                    case "Update Site Installation status - Completed":case "Upload Snaglist and confirm QC completion - Completed":
                          project_statusArr = ["initiated","proposal approved","order placed","production started","installation"];
                          break;
                    case "Upload Handover document and update Project Closure status - Completed":
                          project_statusArr = ["initiated","proposal approved","order placed","production started","installation","handed over"];
                          break;
                }
            }
            console.log('-------- project_statusArr  -----------');
            console.log(project_statusArr);

        }



            if (!(that.myaccount.get('userProfData'))) {
                that.myaccount.set({
                    'userProfData': userProfData
                }, {
                    silent: true
                });
            }

            if (!(that.myaccount.get('projectStatusArr'))) {
                that.myaccount.set({
                    'projectStatusArr': project_statusArr
                }, {
                    silent: true
                });
            }
            if (!(that.myaccount.get('mynest'))) {
                that.myaccount.set({
                    'mynest': mynestitems
                }, {
                    silent: true
                });
            }

            if (!(that.myaccount.get('providerId'))) {
                that.myaccount.set({
                    'providerId': providerId
                }, {
                    silent: true
                });
            }

            $(this.el).html(_.template(MyAccountTemplate)({
                'userProfile': userProfData,
                'myNest':mynestitems,
                'providerId': providerId,
                'projectStatusArr':project_statusArr
            }));

            $("#profile").html(_.template(MyProfileTemplate)({
                'userProfile': userProfData,
                'myNest':mynestitems,
                'providerId': providerId,
                'projectStatusArr':project_statusArr
            }));




            document.title = userProfData.displayName + ' | mygubbi';
        },
        render: function () {
            var authData = this.refAuth.currentUser;
            //MGF.getUserProfile(authData, this.renderWithUserProjectCallback);
            MGF.mynest(authData,this.renderWithUserProjectCallback);

            setTimeout(
                $('.page').append("<img id='loadico' src='https://res.cloudinary.com/mygubbi/image/upload/v1470959542/home/new_design/mygubbi.gif' class='page-tran'>")
            , 2000);

            var that = this;
            var mynestProf = authData.email;
            //var mynestProf = this.model.email;
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
            this.listenTo(Backbone, 'user.change', this.handleUserChange);
            _.bindAll(this, 'renderWithUserProjectCallback', 'render', 'submit');
        },
        handleUserChange: function () {
            if (VM.activeView === VM.MYACCOUNTPAGE) {
                window.location = '/';
            }
        },
        submit: function (e) {
            if (e.isDefaultPrevented()) return;
            e.preventDefault();

            var formData = {
                "displayName": $('#user_display_name').val(),
                "profileImage": $('#user_profile_image').attr('src'),
                "email": $('#user_email_id').val(),
                //"dob": $('#user_dob').val(),
                "phone": $('#user_phone').val(),
                "altPhone": $('#user_alt_phone').val(),
                "address": $('#user_address').val(),
                "occupation": $('#occupation').val(),
                "hobbies": $('#hobbies').val(),
                "interest": $('#interest').val(),
                //"website": $('#website').val(),
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

            console.log(formData);
            //return false;


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

            console.log(email);
            console.log('---------------oldPassword---------------');
            console.log(oldpassword);
            console.log('---------------newPassword---------------');
            console.log(newPassword);

            if((newPassword != null) && (oldpassword != newPassword)){
               authData.reauthenticate(firebase.auth.EmailAuthProvider.credential(email, oldpassword));

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
                this.ref.child("projects").child(authData.uid).child("myNest").child("paymentDetails").child("initial_proposal_status").set(eventData, function(error) {
                    if (error) {
                        console.log("not able to push data", error);
                    } else {
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
                                    console.log("successfully pushed data");
                                    $("#reqCallbk_successMsg").fadeIn();
                                    $("#approvebtn").addClass('disabled');
                                    $('#reqCallbk_successMsg').fadeOut(5000);
                                }
                            },
                            error: function(model, response, options) {
                                console.log("couldn't fetch data - " + response);
                            }
                        });
                    }
                });
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
            this.ref.child("projects").child(authData.uid).child("myNest").child("paymentDetails").child("initial_proposal_status").set(eventData, function(error) {
                if (error) {
                    console.log("not able to push data", error);
                } else {

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
                                console.log("successfully pushed data");
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
                }
            });

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
            if(id == "message-lnk"){
                var that = this;
                $("#message").html(_.template(MyMessageTemplate)({
                    'userProfile': that.myaccount.get("userProfData"),
                    'myNest':that.myaccount.get("mynest"),
                    'providerId': that.myaccount.get("providerId"),
                    'projectStatusArr':that.myaccount.get("projectStatusArr")
                }));
            }
        }

    });
    return UserProfileView;
});
