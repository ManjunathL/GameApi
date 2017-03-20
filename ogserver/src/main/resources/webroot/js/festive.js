$(document).ready(function() {
  var owl = $("#owl-testimonial");

    owl.owlCarousel({
      itemsCustom : [
        [0, 2],
        [450, 4],
        [600, 7],
        [700, 9],
        [1000, 10],
        [1200, 12],
        [1400, 13],
        [1600, 15]
      ],
      navigation : true,
      items: 2,
      responsive:{
            0:{
                items:1
            },
            600:{
                items:2
            },
            1000:{
                items:2
            }
        }
    });

});

$(document).ready(function() {
  var owl = $("#owl-gallery");
    owl.owlCarousel({
      navigation : true,
      items: 1,
      loop:true,
      autoplay:true,
      autoplayTimeout:3000
    });
});

jQuery(document).ready(function () {

    // wow jquery
    new WOW().init();

    // portfolio
    jQuery('.item .picframe').each(function () {

        jQuery(this).find("img").css("width", "100%");
        jQuery(this).find("img").css("height", "auto");

        jQuery(this).find("img").on('load', function () {
            var w = jQuery(this).css("width");
            var h = jQuery(this).css("height");
            //nh = (h.substring(0, h.length - 2)/2)-48;

            jQuery(this).parent().css("height", h);

        }).each(function () {
            if (this.complete) $(this).load();
        });
    });

    // --------------------------------------------------
    // owlCarousel
    // --------------------------------------------------

    jQuery("#gallery-carousel").owlCarousel({
        items: 4,
        navigation: false,
        pagination: false
    });

    jQuery(".carousel-gallery").owlCarousel({
        items: 4,
        navigation: false,
        pagination: false
    });

    jQuery("#blog-carousel").owlCarousel({
        items: 2,
        navigation: false,
        pagination: true
    });
});

jQuery(document).ready(function () {

    // --------------------------------------------------
    // filtering gallery
    // --------------------------------------------------

    var $container = jQuery('#gallery');
    $container.isotope({
        itemSelector: '.item',
        filter: '*'
    });
    jQuery('#filters a').on("click", function () {
        var $this = jQuery(this);
        if ($this.hasClass('selected')) {
            return false;
        }
        var $optionSet = $this.parents();
        $optionSet.find('.selected').removeClass('selected');
        $this.addClass('selected');

        var selector = jQuery(this).attr('data-filter');
        $container.isotope({
            filter: selector
        });
        return false;
    });
});

// --------------------------------------------------
// css animation
// --------------------------------------------------
var v_count = '0';

jQuery(window).load(function () {

    jQuery('.animated').fadeTo(0, 0);
    jQuery('.animated').each(function () {
        var imagePos = jQuery(this).offset().top;
        var timedelay = jQuery(this).attr('data-delay');

        var topOfWindow = jQuery(window).scrollTop();
        if (imagePos < topOfWindow + 300) {
            jQuery(this).fadeTo(1, 500);
            var $anim = jQuery(this).attr('data-animation');
        }
    });


    // btn arrow up
    jQuery(".arrow-up").on("click", function () {
        jQuery(".coming-soon .coming-soon-content").fadeOut("medium", function () {
            jQuery("#hide-content").fadeIn(600, function () {
                jQuery('.arrow-up').animate({ 'bottom': '-40px' }, "slow");
                jQuery('.arrow-down').animate({ 'top': '0' }, "slow");
            });
        });
    });

    // btn arrow down
    jQuery(".arrow-down").on("click", function () {
        jQuery("#hide-content").fadeOut("slow", function () {
            jQuery(".coming-soon .coming-soon-content").fadeIn(800, function () {
                jQuery('.arrow-up').animate({ 'bottom': '0px' }, "slow");
                jQuery('.arrow-down').animate({ 'top': '-40' }, "slow");
            });
        });
    });

    // isotope
    jQuery('#gallery').isotope('reLayout');

});



jQuery(window).scroll(function () {

    // counter
    jQuery('.timer').each(function () {
        var imagePos = jQuery(this).offset().top;

        var topOfWindow = jQuery(window).scrollTop();
        if (imagePos < topOfWindow + 500 && v_count == '0') {

            jQuery(function ($) {

                // start all the timers
                jQuery('.timer').each(count);


                function count(options) {
                    v_count = '1';
                    var $this = jQuery(this);
                    options = $.extend({}, options || {}, $this.data('countToOptions') || {});
                    $this.countTo(options);
                }
            });

        }
    });

    // progress bar
    jQuery('.de-progress').each(function () {
        var pos_y = jQuery(this).offset().top;
        var value = jQuery(this).find(".progress-bar").attr('data-value');

        var topOfWindow = jQuery(window).scrollTop();
        if (pos_y < topOfWindow + 500) {
            jQuery(this).find(".progress-bar").animate({ 'width': value }, "slow");
        }
    });

    // animated bar
    jQuery('.animated').each(function () {
        var imagePos = jQuery(this).offset().top;
        var timedelay = jQuery(this).attr('data-delay');

        var topOfWindow = jQuery(window).scrollTop();
        if (imagePos < topOfWindow + 500) {
            jQuery(this).delay(timedelay).queue(function () {
                jQuery(this).fadeTo(1, 500);
                var $anim = jQuery(this).attr('data-animation');
                jQuery(this).addClass($anim).clearQueue();
            });

        }
    });

});
