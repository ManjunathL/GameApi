<script>

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

    var $frame = $('#forcecentered');
    var $wrap = $frame.parent().parent();
    SlyUtil.create($wrap, '#forcecentered', '.next', '.prev',mainSliderActive).init();



     if($('#alt1-frame').length > 0){
        var $alt1_frame = $('#alt1-frame');
        var $alt1_wrap = $alt1_frame.parent().parent();
        SlyUtil.create($alt1_wrap, '#alt1-frame', '.alt1-next', '.alt1-prev').init();
    }

    if($('.accessory-frame').length > 0){
        $('.accessory-frame').each(function () {
            var accessoryId = this.id;
            var $accessory_frame = $('#'+accessoryId);
            var $accessory_wrap = $accessory_frame.parent().parent();

            SlyUtil.create($accessory_wrap, '#'+accessoryId, '.accessory-next', '.accessory-prev').init();
        });
     }

});


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
</script>