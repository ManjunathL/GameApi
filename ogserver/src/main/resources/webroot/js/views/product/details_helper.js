define([
    'jquery',
    '/js/slyutil.js',
    'jqueryeasing'
], function($, SlyUtil, JqueryEasing) {
    return {

        showMaterialsubmmenu: function() {
            $("#material-sub-menu").toggle();
        },

        showFinishsubmmenu: function() {
            $("#finish-sub-menu").toggle();
        },

        ready: function(parent) {

            _.bindAll(this, 'toggleColor', 'toggleColorNext');

/*
            var events = {
                "click .appliance-img": this.toggleColorNext,
                "click .appliance-mark": this.toggleColor
            };

            parent.delegateEvents(events);
*/

            var alt1LastIndex = 0;
            var down = true;
            var that = this;

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
                SlyUtil.create($wrap, '#forcecentered', '.next', '.prev', that.mainSliderActive).init();

                if ($('#alt1-frame').length > 0) {
                    var $alt1_frame = $('#alt1-frame');
                    var $alt1_wrap = $alt1_frame.parent().parent();
                    SlyUtil.create($alt1_wrap, '#alt1-frame', '.alt1-next', '.alt1-prev').init();
                }

                if ($('.accessory-frame').length > 0) {
                    $('.accessory-frame').each(function() {
                        var accessoryId = this.id;
                        var $accessory_frame = $('#' + accessoryId);
                        var $accessory_wrap = $accessory_frame.parent().parent();

                        SlyUtil.create($accessory_wrap, '#' + accessoryId, '.accessory-next', '.accessory-prev').init();
                    });
                }

                if ($('.appliance-frame').length > 0) {

                    var $appliance_frame = $('#appliance-Chimney');
                    var $appliance_wrap = $appliance_frame.parent().parent();
                    SlyUtil.create($appliance_wrap, '#appliance-Chimney', '.appliance-Chimney-next', '.appliance-Chimney-prev').init();

                    var $appliancehb_frame = $('#appliance-Hob');
                    var $appliancehb_wrap = $appliancehb_frame.parent().parent();
                    SlyUtil.create($appliancehb_wrap, '#appliance-Hob', '.appliance-Hob-next', '.appliance-Hob-prev').init();

                    /*$('.appliance-frame').each(function() {
                        var applianceId = 'appliance-Chimney';
                        alert(applianceId);
                        var $appliance_frame = $('#' + applianceId);
                        var $appliance_wrap = $appliance_frame.parent().parent();
alert($appliance_wrap);
                        SlyUtil.create($appliance_wrap, '#' + applianceId, '.appliance-next', '.appliance-prev').init();
                    });*/
                }


            });
        },

        mainSliderActive: function(eventName, itemIndex) {

            var images = window.product.get('images');
            var mainImg = document.getElementById('mainImg');

            if (!mainImg.src.match(images[itemIndex])) {

                $('#mainImg').fadeOut(600, function() {
                    $('#mainImg').attr("src", imgBase + images[itemIndex]);
                    $('#mainImg').fadeIn(200);
                });
            }
        },
        toggleColor: function(e) {
            this.toggleColorFor($(e.target));
        },
        toggleColorNext: function(e) {
            this.toggleColorFor($(e.target).next());
        },
        toggleColorFor: function(element) {
            var currentColor = $(element).css('color');
            if (currentColor === "rgb(204, 204, 204)") {
                $(element).css('color', 'rgb(0, 0 ,0)');
                $(element).css('background', 'rgb(240, 240, 240)');
            } else {
                $(element).css('color', 'rgb(204, 204, 204)');
                $(element).css('background', 'rgb(255, 255, 255)');
            }
        }
    };
});