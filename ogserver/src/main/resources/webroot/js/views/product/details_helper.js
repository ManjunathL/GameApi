
function showMaterialsubmmenu() {
    $("#material-sub-menu").toggle();
}

function showFinishsubmmenu() {
    $("#finish-sub-menu").toggle();
}


var alt1LastIndex = 0;
var down = true;

$(window).scroll(function() {

    var scrollTop = $(window).scrollTop();
    var headingBar = $("#heading-bar");

    if (scrollTop > 0) {
        if (down) {
            $("#heading-bar").fadeOut(300, function() {
                $("#heading-bar").css("position", "fixed");
                $("#heading-bar").css("top", "50px");
                $("#heading-bar").css("background-color", "#FFFFFF");
                $("#heading-bar").toggleClass("portfolio-box");
                $("#heading-bar").css("width", "100%");
                $("#heading-bar").css("left", "50%");
                $("#heading-bar").css("margin-left", "-50%");
                $("#prodName").css("font-size", "18px");
                $("#prodPrice").css("font-size", "18px");
                $("#heading-bar").fadeIn(600);
            });
            down = false;
        }
    } else {
        down = true;
        $("#heading-bar").fadeOut(10, function() {
            $("#heading-bar").css("position", "static");
            $("#heading-bar").css("background-color", "transparent");
            $("#heading-bar").css("width", "auto");
            $("#heading-bar").css("margin-left", "auto");
            $("#prodName").css("font-size", "24px");
            $("#prodPrice").css("font-size", "24px");
            $("#heading-bar").toggleClass("portfolio-box");
            $("#heading-bar").fadeIn(300);
        });
    }

});

jQuery(function($) {
    'use strict';

//    alert('hi');

    var $frame = $('#forcecentered');
    var $wrap = $frame.parent().parent();

    // Call Sly on frame
    var sly = new Sly('#forcecentered', {
        horizontal: 1,
        itemNav: 'basic',
        smart: 1,
        activateMiddle: 0,
        activateOn: 'click',
        mouseDragging: 1,
        touchDragging: 1,
        releaseSwing: 1,
        startAt: 0,
        /*scrollBar: $wrap.find('.scrollbar'),*/
        scrollBy: 1,
        speed: 300,
        elasticBounds: 1,
        easing: 'easeOutExpo',
        dragHandle: 1,
        dynamicHandle: 1,
        clickBar: 1,

        // Buttons
        prevPage: $wrap.find('.prev'),
        nextPage: $wrap.find('.next')
    }, {
        active: mainSliderActive
    }).init();

//    accessorySlide();
});

function accessorySlide(){

}
function mainSliderActive(eventName, itemIndex) {

    var images = "<%= product.images %>".split(',');
    var mainImg = document.getElementById('mainImg');

    if (!mainImg.src.match(images[itemIndex])) {

        $('#mainImg').fadeOut(600, function() {
            $('#mainImg').attr("src", imgBase + images[itemIndex]);
            $('#mainImg').fadeIn(200);
        });
    }
}



function toggleColor(element) {
    var currentColor = $(element).css('color');
    if (currentColor === "rgb(204, 204, 204)") {
        $(element).css('color', 'rgb(0, 0 ,0)');
        $(element).css('background', 'rgb(240, 240, 240)');
    } else {
        $(element).css('color', 'rgb(204, 204, 204)');
        $(element).css('background', 'rgb(255, 255, 255)');
    }
}

(function(d, s, id) {
  var js, fjs = d.getElementsByTagName(s)[0];
  if (d.getElementById(id)) return;
  js = d.createElement(s); js.id = id;
  js.src = "//connect.facebook.net/en_GB/sdk.js#xfbml=1&version=v2.5";
  fjs.parentNode.insertBefore(js, fjs);
}(document, 'script', 'facebook-jssdk'));

!function(d,s,id){var js,fjs=d.getElementsByTagName(s)[0],p=/^http:/.test(d.location)?'http':'https';if(!d.getElementById(id)){js=d.createElement(s);js.id=id;js.src=p+'://platform.twitter.com/widgets.js';fjs.parentNode.insertBefore(js,fjs);}}(document, 'script', 'twitter-wjs');
