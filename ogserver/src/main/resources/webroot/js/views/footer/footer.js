define([
    'jquery',
    'underscore',
    'backbone',
    'text!templates/footer/footer.html'
], function($, _, Backbone, footerTemplate) {
    var FooterView = Backbone.View.extend({
        el: '.footer',
        render: function() {
            $(this.el).html(footerTemplate);
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
        }
    });
    return FooterView;
});