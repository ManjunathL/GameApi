define([
    'jquery',
    'underscore',
    'backbone',
    'text!templates/footer/new-footer.html',
    'mgfirebase'
], function($, _, Backbone, footerTemplate, MGF) {
    var FooterView = Backbone.View.extend({
        el: '.footer',
        render: function() {
            $(this.el).html(_.template(footerTemplate));
            this.ready();
        },
        ready: function() {
            $(function() {
                var faded = true;
                $(window).scroll(function() {
                    var duration = 300;
                    if ($(this).scrollTop() > 400) {
                        if (faded) {
                            $('.back-to-top').fadeIn(duration);
                            faded = false;
                        }
                    } else {
                        if (!faded) {
                            $('.back-to-top').fadeOut(duration);
                            faded = true;
                        }
                    }
                });

                $('.back-to-top').click(function(event) {
                    event.preventDefault();
                    $('html, body').animate({
                        scrollTop: 0
                    }, 500);
                    return false;
                });

                $("#wish").click(function() {
                    if ($(this).hasClass('glyphicon-heart')) {
                        $(this).removeClass('glyphicon-heart').addClass('glyphicon-heart-empty');
                        $('.super-script').hide();
                    } else {
                        $(this).removeClass('glyphicon-heart-empty').addClass('glyphicon-heart');
                        $('.super-script').show();
                    }
                });

            });
        },
        subscribe: function() {
            var email = $('#subscribe-email').val();
            if (email) {
                var emailReg = /^([\w-\.]+@([\w-]+\.)+[\w-]{2,4})?$/;
                var check =  emailReg.test( email );
                if(check){
                    MGF.subscribeUser(email);
                    $('#subscribe-msg').show();
                    $('#subscribe-msg').html('<i>Thanks for subscribing.</i>');
                    $('#subscribe-email').val('');

                }else {
                    $('#subscribe-msg').show();
                    $('#subscribe-msg').html('<i><span class="error-class">Please enter valid email.</span></i>');
                }
            }else {
                $('#subscribe-msg').show();
                $('#subscribe-msg').html('<i><span class="error-class">Please enter your email.</span></i>');
            }
        },
        events: {
            "click #subscribe-btn": 'subscribe'
        }
    });
    return FooterView;
});