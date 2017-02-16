define([
    'jquery',
    'underscore',
    'backbone',
    'bootstrap',
    'bootstrapvalidator',
    'mgfirebase',
    'consultutil',
    'collections/autosuggest_products',
    'text!templates/header/suggest_results.html'
], function($, _, Backbone, Bootstrap, BootstrapValidator, MGF, ConsultUtil, AutoSuggestProducts, SuggestResultsPage) {
    return {
        ref: MGF.rootRef,
        refAuth: MGF.refAuth,

        createUser: function(userId, data, next) {
            var that = this;
            this.ref.child("users").child(userId).set(data, function(error) {
                if (error) {
                    console.log("Data could not be saved." + error);
                    if (next) next(false);
                } else {
                    console.log("Data saved successfully.");
                    if (next) next(true);
                }
            });
        },

        setUser: function(user) {
            users.add(user, {
                at: 0
            });
        },

        getUserProfileHandleAuth: function(uid, authData, handleAuth) {
            var userProfileRef = this.ref.child("user-profiles/" + uid);
            var userProfile = null;
            var that = this;
            userProfileRef.on("value", function(snapshot) {
                if (snapshot.exists()) {
                    userProfile = snapshot.val();
                }
                that.handleAuth(authData, userProfile);
                //console.log("handleAuth done");
            });
        },
        getUserProfileWithCB: function(next) {
            var authData = this.refAuth.currentUser;
            MGF.getUserProfile(authData, next);
        },
        onFAuth: function(authData) {
            if (authData) {
                if ((typeof(authData.providerData[0]) != 'undefined') && (authData.providerData[0].providerId !== 'anonymous')) { //don't do nothin on anonymous auths
                    $('#user-icon').toggleClass("glyphicon glyphicon-user fa fa-spinner fa-spin");
                    if (users.length === 0 || users.at(0).get('uid') !== authData.uid) {
                        this.getUserProfileHandleAuth(authData.uid, authData, this.handleAuth);
                        //console.log("user profile done");
                    }
                }
                MGF.listenForShortlistChanges();
            } else {
                MGF.doAnonymousAuth(); //.then(function(){/*do nothing here as onFAuth will get called again automatically*/});
            }
        },
        formUserData: function(authData, userProfile) {

            return {
                providerData: authData.providerData,
                email: MGF.getEmail(authData),
                displayName: MGF.getName(authData, userProfile),
                profileImage: MGF.getImage(authData, userProfile),
                uid: authData.uid
            };
        },
        handleAuth: function(authData, userProfile) {
            var email = MGF.getEmail(authData);
            if (!email) {
                console.log("email not provided, please try again and provide email id as it is mandatory.");
                $('#login_error').html("Please tick email, while providing Facebook access.");
                $('#login_error_row').css("display", "block");
                //this.ref.unauth();
                //MGF.refAuth.signOut();
                return;
            }

            var user = this.formUserData(authData, userProfile);

            if (authData.providerData !== 'password') {
                var userRef = this.ref.child("users/" + authData.uid);
                var that = this;
                userRef.on("value", function(snapshot) {
                    if (snapshot.exists()) {
                        that.setUser(user);
                        //console.log("user already exists in firebase");
                        window.fbButton && window.fbButton.button('reset');
                        //                        window.twitterButton && window.twitterButton.button('reset');
                        window.googleButton && window.googleButton.button('reset');
                        $('#user-icon').toggleClass("glyphicon glyphicon-user fa fa-spinner fa-spin");
                    } else {
                        console.log("user doesn't exist in firebase, creating..");

                        var setUserAndCreateProfile = function(result) {
                            if (result) {
                                that.setUser(user);
                                that.createProfile(authData, {
                                    displayName: user.displayName,
                                    email: user.email,
                                    phone: '',
                                    profileImage: user.profileImage
                                }, null);
                            }
                            window.fbButton && window.fbButton.button('reset');
                            //                            window.twitterButton && window.twitterButton.button('reset');
                            window.googleButton && window.googleButton.button('reset');
                            $('#user-icon').toggleClass("glyphicon glyphicon-user fa fa-spinner fa-spin");
                        };
                        that.createUser(authData.uid, user, setUserAndCreateProfile);
                    }
                });
            } else {
                this.setUser(user);
                $('#user-icon').toggleClass("glyphicon glyphicon-user fa fa-spinner fa-spin");
            }
        },

        authHandler: function(error, authData) {
            if (error) {
                console.log("Login Failed!", error);
                $('#login_error').html(error);
                $('#login_error_row').css("display", "block");
            } else {
                console.log("Authenticated successfully");
            }
            window.loginButton && window.loginButton.button('reset');
            window.googleButton && window.googleButton.button('reset');
            window.fbButton && window.fbButton.button('reset');
            //window.twitterButton && window.twitterButton.button('reset');
        },

        pwdLogin: function() {
            var that = this;
            var email = $('#emailId').val();
            var password = $('#pwd').val();

            this.refAuth.signInWithEmailAndPassword(email, password).then(function(authData) {
              // Sign-in successful.
              console.log('User data after login');
              console.log(authData);

              $('#user-icon').toggleClass("glyphicon glyphicon-user fa fa-spinner fa-spin");
              console.log('Sign-in successful');
            }, function(error) {
              // An error happened.
              $('#login_error').html("The password is invalid. Please enter a correct password.");
              $('#login_error_row').css("display", "block");
              console.log('Error'+error);
              window.loginButton && window.loginButton.button('reset');
            }, this.authHandler, {
                 remember: $('#remember').is(':checked') ? 'default' : 'sessionOnly'
             });
            // [END authwithemail]
        },

        resetPassword: function() {

            var emailAddress = $('#forgotEmail').val();

            this.refAuth.sendPasswordResetEmail(emailAddress).then(function() {
                console.log("reset successfully");
                $('.modal_success_msg').slideDown();
                $('#forgotBtn').hide();
                $('#forgotEmail').prop('disabled', true);
                window.forgotButton && window.forgotButton.button('reset');
            }, function(error) {
                  console.log("Reset Failed!", error);
                  $('.modal_error_msg').slideDown();
                  window.forgotButton && window.forgotButton.button('reset');
            });
        },

        signOutUser: function(ev) {
           // MGF.stopListeningForShortlistChanges(this.refAuth.currentUser.uid);
            users.reset();
            this.refAuth.signOut().then(function() {
               // Sign-out successful.
               console.log("Sign-out successful.");
               window.location = '/';
            }, function(error) {
              // An error happened.
              console.log("Sign-out failed due to some error." + error.errorMessage);
            });
        },

        closeModal: function(ev) {
            var id = $(ev.currentTarget).data('element');
            $(id).modal('toggle');
        },

        closeUserPopup: function(ev) {
            var id = $(ev.currentTarget).data('element');
            if (id == '.userpop') {
                $('.userpop').css('right', '-800px');
            } else {
                $(id).hide('slow');
            }
        },

       positionSideContact: function() {

            var windowHeight = $(window).height();
            var contactUsSideHeight = $('.contact-us-side').height();
            var contactUsSideWidth = $('.contact-us-side').width();
            var popHeight = $('.contact-us-pop').height() - contactUsSideHeight;
            var popHeightMore = popHeight > windowHeight;
            $('.contact-us-pop').css('top', popHeightMore ? 0 : (windowHeight / 2 - popHeight / 2) + 'px');
            $('.contact-us-side').css('top', ((popHeight / 2 > windowHeight) ? (-popHeight * 3 / 4 + contactUsSideWidth / 2) : (-popHeight / 2 + contactUsSideWidth / 2)) + 'px');

            var currLeft = $('.contact-us-pop').position().left;
            if (currLeft < 0) {
                $('.contact-us-pop').css('left', -$('.contact-us-pop').width() + 'px');
            }
        },

        closeContactForm: function(ev) {
            $('#contactuspop').modal('toggle');
        },

        getSuggestions: function(term) {
            return new Promise(function(resolve, reject) {
                var autoSuggestProducts = new AutoSuggestProducts();
                autoSuggestProducts.fetch({
                    data: {
                        "term": term
                    },
                    success: function() {
                        console.log("suggest fetch successful");
                        resolve(autoSuggestProducts);
                    },
                    error: function(model, response, options) {
                        console.log("error in suggest fetch - " + response);
                    }
                });
            });
        },

        signUp: function() {

            var that = this;

            var email = $('#reg_email_id').val();
            var password = $('#reg_password').val();
            var phoneNum = $('#reg_contact_num').val();

            //MGF.handleSignUp(email,password);

            this.refAuth.createUserWithEmailAndPassword(email, password).then(function(userData) {
                console.log("Successfully created user account with uid:", userData.uid);
                var photoURL = userData.photoURL ? userData.photoURL: 'https://res.cloudinary.com/mygubbi/image/upload/v1484131794/cep/user_new.png';


                var userData = {
                    providerData: "password",
                    email: $('#reg_email_id').val(),
                    displayName: $('#reg_full_name').val(),
                    profileImage: photoURL,
                    uid: userData.uid
                };

                console.log(userData);



                var profileJSONData = function(userData) {
                    if (userData) {
                        that.setUser(userData);
                        that.createProfile(userData, {
                            displayName: $('#reg_full_name').val(),
                            email: $('#reg_email_id').val(),
                            phone: phoneNum,
                            profileImage: photoURL,
                            crmId:""
                        }, null);
                    }
                };

                var profileData = {
                    displayName: $('#reg_full_name').val(),
                    email: $('#reg_email_id').val(),
                    phone: phoneNum,
                    profileImage: photoURL,
                    crmId:""
                };

                that.createUser(userData.uid, userData, profileJSONData);

                that.createProfile(userData, profileData, that.unAuthAfterProfile);

            }, function(error) {
                // An error happened.
                console.log("Error creating user:", error);
                $('#reg_error').html(error);
                $('#reg_error_row').css("display", "block");
                window.signupButton.button('reset');
            });
        },
        unAuthAfterProfile: function() {
            window.signupButton.button('reset');
            $('#reg_done_message').html("Thanks for registering with us. You now have access to our personalized service. Please <a href='#' id='goto-login'>Login</a> to proceed.");
            $('#signup').modal('hide');
            $('#notify').modal('show');

            this.refAuth.onAuthStateChanged(this.onFAuth);

            setTimeout(function() {
                window.location = '/';
            }, 1000);

        },
        gotoLogin: function() {
            $('#notify').modal('toggle');
            this.showUserPop();
        },

        toggleContactUsPop: function() {
            var currLeft = $('.contact-us-pop').position().left;
            if (currLeft < 0) {
                $('.contact-us-pop').css('left', '0px');
                $('.contact-us-pop').toggleClass('overflowHeight');

            } else {
                $('.contact-us-pop').css('left', -$('.contact-us-pop').width() + 'px');
                $('.contact-us-pop').toggleClass('overflowHeight');
            }
        },
        showUserPop: function() {
            $('#login_error').html('');
            $('#login_error_row').css("display", "none");
            $('.userpop').css('right', '0px');
        },

        createProfile: function(userData, profileData, next) {
            MGF.createProfile(userData, profileData, next);
        },
        debounce: function(fn, delay) {
            var timer = null;
            return function() {
                var context = this,
                    args = arguments;
                clearTimeout(timer);
                timer = setTimeout(function() {
                    fn.apply(context, args);
                }, delay);
            };
        },
        clearSearchedProduct: function(ev){
            ev.preventDefault();
            ev.stopPropagation();
            $('.sb-search-input').val("");
            $('.clr-search-lnk').css('display','none');
        },
        gotoSearchedProduct: function(ev) {
            ev.preventDefault();
            ev.stopPropagation();
            var target = $(ev.currentTarget).data('target');
            var srctxt = $(ev.currentTarget).text();

            $('.sb-search-input').val(srctxt);
            $('.sb-search_suggest').slideUp();
            $('.clr-search-lnk').css('display','block');

            //close the search bar start
            var isMobile = window.matchMedia("only screen and (max-width: 920px)");
            if (isMobile.matches) {
                $('#main-lg-ico').css("position", "relative");
                $('#sb-search-duplicate').toggle('slide', {
                    direction: 'Right'
                }, 0);
            } else {
                $('#bs-example-navbar-collapse-1').css("position", "relative");
                $('#sb-search').toggle('slide', {
                    direction: 'Right'
                }, 0);
            }
            //close the search bar end

            window.App.router.navigate(target, {
                trigger: true
            });
        },
        ready: function(parent) {

            //add any new functions to this list. This is essential as this class is only a helper, the functions are called from outside.
            _.bindAll(this, 'toggleContactUsPop', 'closeContactForm', 'createUser',
                'setUser', 'getUserProfileHandleAuth', 'getUserProfileWithCB', 'onFAuth', 'handleAuth', 'authHandler', 'pwdLogin', 'resetPassword', 'signOutUser',
                'closeModal', 'closeUserPopup', 'signUp', 'gotoLogin', 'showUserPop', 'createProfile',
                'unAuthAfterProfile');

            var events = {
                "click .signout_icon": this.signOutUser,
                "click #close-user-pop": this.closeUserPopup,
                "click #view-all-shortlist": this.closeUserPopup,
                "click #shortlist-bar-explore": this.closeUserPopup,
                "click .shortlist-side": this.closeUserPopup,
                "click #close-signup-pop": this.closeModal,
                "click #close-forgot-pop": this.closeModal,
                "click #close-contactus-pop": this.toggleContactUsPop,
                //"click #contact-form-explore": this.toggleContactUsPop,
                "click #goto-login": this.gotoLogin,
                "click .sb-search-txt": this.gotoSearchedProduct,
                "click .clr-search-lnk": this.clearSearchedProduct
            };

            parent.delegateEvents(events);

            //this.ref.onAuth(this.onFAuth);
            var that = this;
            //var auth = firebase.auth();
            this.refAuth.onAuthStateChanged(this.onFAuth);


            $(function() {

                 if(window.location.href.indexOf("faq-shipping") > -1 || window.location.toString().indexOf("faq-returns") > -1 || window.location.toString().indexOf("faq-warranty") > -1){
                    document.getElementById("canlink").href = "https://www.mygubbi.com/faq";
                    }
                    else{
                    document.getElementById("canlink").href = window.location.href;
                 }
                $('#nav').find('li.gubbi-list-desk a').click(function () {
                    $('#nav').find('li.gubbi-list-desk a').removeClass('active');
                    $(this).addClass('active');
                    $($(this).closest('li.gubbi-list-desk').children()[0]).addClass('active');
                });

                /*$('#nav').find('ul.dropdownMenu_lg a').click(function () {
                    $('#nav').find('ul.dropdownMenu_lg li').removeClass('active');
                    $($(this).parent('li')).addClass('active').removeClass('open');
                });*/

                $('#nav').find('ul.gubbi-list-mob-ul a').click(function () {
                    $('#nav').find('ul.gubbi-list-mob-ul li').removeClass('active');
                    $($(this).parent('li')).addClass('active');
                });

                /* Menu onhover code by Smruti */
                $( ".dropdown-submenu a" )
                .mouseenter(function() {
                    $(this).next('ul').addClass('viewMenuCls');
                })
                .mouseleave(function() {
                    $(this).next('ul').removeClass('viewMenuCls');
                });

                $('.user').click(function(e) {
                    e.stopPropagation();
                    that.showUserPop();
                });

                $('#new_reg').click(function() {
                    $('.userpop').css('right', '-800px');
                    $('#signup').modal('toggle');
                    $('#reg_error').html('');
                    $('#reg_error_row').css("display", "none");
                    $('#reg_password').val('');
                    $('#reg_confirm').val('');

                });

                $('#contact-us-side-btn').click(function(e) {
                    e.stopPropagation();
                    that.toggleContactUsPop();
                    $('#contact_error').html('');
                    $('#contact_error_row').css("display", 'none');
                });

                $("#registerForm").submit(function(e) {
                    e.preventDefault();
                    if ($('#reg_confirm').val() !== $('#reg_password').val()) {
                        $('#reg_confirm_error').html("Passwords don't match!");
                    } else {
                        window.signupButton.button('loading');
                        $('#reg_confirm_error').html('');
                        $('#reg_error').html('');
                        $('#reg_error_row').css("display", "none");
                        that.signUp();
                    }
                });

                $("#searchForm1").submit(function(e) {
                    e.preventDefault();
                    var term = $('#searchInput1').val();
                    if (!term) return;

                    //close the search bar start
                    var isMobile = window.matchMedia("only screen and (max-width: 920px)");
                    if (isMobile.matches) {
                        $('#main-lg-ico').css("position", "relative");
                        $('#sb-search-duplicate').toggle('slide', {
                            direction: 'Right'
                        }, 0);
                    } else {
                        $('#bs-example-navbar-collapse-1').css("position", "relative");
                        $('#sb-search').toggle('slide', {
                            direction: 'Right'
                        }, 0);
                    }
                    //close the search bar end

                    window.App.router.navigate("/product_search-" + term, {
                        trigger: true
                    });

                });

                $("#searchForm2").submit(function(e) {
                    e.preventDefault();
                    var term = $('#searchInput2').val();
                    if (!term) return;

                    //close the search bar start
                    var isMobile = window.matchMedia("only screen and (max-width: 920px)");
                    if (isMobile.matches) {
                        $('#main-lg-ico').css("position", "relative");
                        $('#sb-search-duplicate').toggle('slide', {
                            direction: 'Right'
                        }, 0);
                    } else {
                        $('#bs-example-navbar-collapse-1').css("position", "relative");
                        $('#sb-search').toggle('slide', {
                            direction: 'Right'
                        }, 0);
                    }
                    //close the search bar end

                    window.App.router.navigate("/product_search-" + term, {
                        trigger: true
                    });

                });

                $('#loginBtn').click(function() {
                    window.loginButton = $(this);
                });

                $('#forgotBtn').click(function() {
                    window.forgotButton = $(this);
                });

                $('#signup-btn').click(function() {
                    window.signupButton = $(this);
                });

                $("#loginForm").submit(function(e) {
                    e.preventDefault();
                    window.loginButton.button('loading');
                    $('#login_error').html('');
                    $('#login_error_row').css("display", "none");
                    that.pwdLogin();
                });

                $("#forgotForm").submit(function(e) {
                    e.preventDefault();
                    window.forgotButton.button('loading');
                    $('.modal_success_msg').css('display', 'none');
                    $('.modal_error_msg').css('display', 'none');
                    that.resetPassword();
                });

                $('#view_profile').click(function() {
                    $('.userpop').css('right', '-800px');
                });

                $('.search').click(function() {
                    $('.search_overlay').slideDown();
                    $('.search_input').focus();
                });

                $('.search_close').click(function() {
                    $('.search_overlay').slideUp();
                });

                $('.search_input').keyup(function() {
                    var char = $(this).val().length;
                    if (char >= 3) {
                        $('.search_suggest').slideDown();
                    } else {
                        $('.search_suggest').slideUp();
                    }
                });

                $('.search_overlay').click(function() {
                    $('.search_input').focus();
                    $('.search_suggest').slideUp();
                });

                /* Search on menu bar Start */
                $('.search-ico').click(function() {
                    var isMobile = window.matchMedia("only screen and (max-width: 920px)");
                    if (isMobile.matches) {
                        $('#main-lg-ico').css("position", "relative");
                        $('#sb-search-duplicate').toggle('slide', {
                            direction: 'Right'
                        }, 500);
                    } else {
                        $('#bs-example-navbar-collapse-1').css("position", "relative");
                        $('#sb-search').toggle('slide', {
                            direction: 'Right'
                        }, 500);
                    }
                });

                $('.sb-search-input').keyup(that.debounce(function(e) {

                    if (e.keyCode == 13) return;

                    var term = $(this).val();
                    if (term.length >= 3) {
                        that.getSuggestions(term).then(function(autoSuggestProducts) {
                            $('.sb-search_suggest').html(_.template(SuggestResultsPage)({
                                autoSuggestProducts: autoSuggestProducts.toJSON()
                            }));
                        });
                        $('.sb-search_suggest').slideDown();
                    } else {
                        $('.sb-search_suggest').slideUp();
                    }
                }, 250));


                /* Search on menu bar End  */

                $("#forgot_password").on('hidden.bs.modal', function() {
                    $('.modal_success_msg').css('display', 'none');
                    $('.modal_error_msg').css('display', 'none');
                });

                $("#forgot_pwd_btn").click(function() {
                    $('#forgot_password').modal('show');
                    $('#forgotBtn').show();
                    $('#forgotEmail').val('');
                    $('#forgotEmail').prop('disabled', false);

                });

                $('#fb-btn').click(function() {

                    window.fbButton = $(this);
                    window.fbButton.button('loading');
                    /*that.ref.authWithOAuthPopup("facebook", that.authHandler, {
                        scope: "email"
                    });*/

                    var provider = new firebase.auth.FacebookAuthProvider();

                    firebase.auth().signInWithPopup(provider).then(function(result) {
                      var token = result.credential.accessToken;
                      var user = result.user;
                      var uid = result.user.uid;
                      firebase.auth().signInWithRedirect(provider);
                      that.authHandler();
                      console.log('Successfully login using facebook'+uid);
                    }).catch(function(error) {
                      var errorCode = error.code;
                      var errorMessage = error.message;
                      var email = error.email;
                      var credential = error.credential;
                      console.log('login failed using facebook '+errorMessage);
                    });

                    /*this.refAuth.signInWithPopup(provider).then(function(result) {
                      // User signed in!
                      var uid = result.user.uid;
                      console.log('Successfully login using facebook'+uid);
                    }).catch(function(error) {
                      // An error occurred
                      console.log('login failed using facebook');
                    });*/
                });

                $('#google-btn').click(function() {
                    window.googleButton = $(this);
                    window.googleButton.button('loading');
                    /*that.ref.authWithOAuthPopup("google", that.authHandler, {
                        scope: "email"
                    });*/

                    var providerData = new firebase.auth.GoogleAuthProvider();
                    firebase.auth().signInWithPopup(providerData).then(function(result) {
                      var accessToken = result.credential.accessToken;
                      that.authHandler();
                      console.log('Successfully login using google plus'+accessToken);
                    });
                });

                $(function() {
                    var navMain = $("#bs-example-navbar-collapse-1");
                    var menuLi = $(".menu-li");
                    menuLi.on("click", "a", null, function() {
                        navMain.collapse('hide');
                    });
                });

                $(window).resize(that.positionSideContact);

                that.positionSideContact();

                $(document).on("click", null, function(e) {
                    var contactpopup = $("#contactuspop");
                    if (!$('#contact-us-side-btn').is(e.target) && !contactpopup.is(e.target) && contactpopup.has(e.target).length == 0) {
                        $("#contactuspop").css('left', -$("#contactuspop").width() + 'px');
                    }
                    var popup = $(".userpop");
                    if (!$('#user-icon').is(e.target) && !popup.is(e.target) && popup.has(e.target).length == 0) {
                        popup.css('right', '-800px');
                    }
                });

                $(document).on("click", "a[href^='/']", function(event) {
                    href = $(event.currentTarget).attr('href');
                    if (!event.altKey && !event.ctrlKey && !event.metaKey && !event.shiftKey) {
                        event.preventDefault();
                        url = href.replace("/^\//", '').replace('\#\!\/', '');
                        window.App.router.navigate(url, {
                            trigger: true
                        });
                    }
                });

            });
        }
    };
});