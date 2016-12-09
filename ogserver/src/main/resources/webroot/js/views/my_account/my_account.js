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
    '/js/views/view_manager.js'
], function ($, _, Backbone, Bootstrap, BootstrapValidator, MGF, Analytics, MyAccount, MyAccountTemplate, MyNestTemplate, MyProfileTemplate, MySettingsTemplate, MyMessageTemplate, VM) {
    var UserProfileView = Backbone.View.extend({
        el: '.page',
        ref: MGF.rootRef,
        refAuth: MGF.refAuth,
        myaccount: null,
        renderWithUserProjectCallback: function (userProfData,mynestitems, providerId) {
        var that = this;
        console.log("-----------mynestitems-------------")
        console.log(mynestitems);
        if(typeof(mynestitems) !== 'undefined'){

            var project_status = mynestitems.paymentDetails[0].sales_stage;
            var project_statusArr = new Array();
            switch (project_status) {
                case "FLOOR_PLAN":
                    project_statusArr = ["initiated"];
                    break;
                case "INITIAL_PROPOSAL":
                    project_statusArr = ["initiated"];
                    break;
                case "BOOKING_FORM":
                    project_statusArr = ["initiated","proposal approved"];
                    break;
                case "SITE_MEASUREMENT":
                    project_statusArr = ["initiated","proposal approved"];
                    break;
                case "DETAILED_DESIGN ":
                    project_statusArr = ["initiated","proposal approved"];
                    break;
                case "FINAL_PROPOSAL":
                    project_statusArr = ["initiated","proposal approved"];
                    break;
                case "WORKS_CONTRACT":
                    project_statusArr = ["initiated","proposal approved","order placed"];
                    break;
                case "SCOPE_DOCUMENT":
                    project_statusArr = ["initiated","proposal approved","order placed"];
                    break;
                case "WORKING_DRAWING":
                      project_statusArr = ["initiated","proposal approved","order placed"];
                      break;
                case "PRODUCTION_DRAWING":
                      project_statusArr = ["initiated","proposal approved","order placed"];
                      break;
                case "SITE_PRESINSTALLATION_CHECKLIST":
                      project_statusArr = ["initiated","proposal approved","order placed"];
                      break;
                case "PO_EXTRACT":
                      project_statusArr = ["initiated","proposal approved","order placed"];
                      break;
                case "QC_REPORT":
                      project_statusArr = ["initiated","proposal approved","order placed"];
                      break;
                case "PRODUCT_PHOTOS":
                      project_statusArr = ["initiated","proposal approved","order placed","production started"];
                      break;
                case "PACKING_LIST_AND_ACCESSORIES_LIST":
                      project_statusArr = ["initiated","proposal approved","order placed","production started","installation"];
                      break;
                case "INVOICES":
                      project_statusArr = ["initiated","proposal approved","order placed","production started","installation"];
                      break;
                case "SNAGLIST":
                      project_statusArr = ["initiated","proposal approved","order placed","production started","installation"];
                      break;
                case "CUSTOMER_HANDOVER":
                      project_statusArr = ["initiated","proposal approved","order placed","production started","installation","handed over"];
                      break;
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
        },
        initialize: function () {
            this.myaccount = new MyAccount();
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
            var newPassword = $('#new_password').val();

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

                                var authData = this.refAuth.currentUser;
                                 var eventData = {
                                                proposal_status: "Request for call back",
                                                scheduledDate: $('#user_date').val(),
                                                scheduledTime: $('#user_time').val()
                                            };
               this.ref.child("projects").child(authData.uid).child("myNest").child("paymentDetails").child("initial_proposal_status").set(eventData, function(error) {
                                if (error) {
                                    console.log("not able to push data", error);
                                } else {
                                    console.log("successfully pushed data");
                                }
                            });
        },
        changeapprove: function(e) {
            if (e.isDefaultPrevented()) return;
                        e.preventDefault();

                        var authData = this.refAuth.currentUser;
                         var eventData = {
                                        proposal_status: "Approve"

                                    };


       this.ref.child("projects").child(authData.uid).child("myNest").child("paymentDetails").child("initial_proposal_status").set(eventData, function(error) {
                        if (error) {
                            console.log("not able to push data", error);
                        } else {
                            console.log("successfully pushed data");
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
