define([
    'jquery',
    'underscore',
    'backbone',
    'bootstrap',
    'cloudinary_jquery',
    '/js/slyutil.js',
    '/js/mgfirebase.js',
    '/js/consultutil.js',
    '/js/analytics.js',
    'bxslider',
    'text!/templates/dashboard/page.html'
], function($, _, Backbone, Bootstrap, CloudinaryJquery, SlyUtil, MGF, ConsultUtil, Analytics, BxSlider, dashboardPageTemplate){
    var DashboardPage = Backbone.View.extend({
        el: '.page',
        ref: MGF.rootRef,

        renderWithUserProfCallback: function(userProfData) {
            $(this.el).html(_.template(dashboardPageTemplate)({
              'userProfile': userProfData
            }));
            $.cloudinary.responsive();
        },

        render: function() {
            var authData = this.ref.getAuth();
            MGF.getUserProfile(authData, this.renderWithUserProfCallback);
            this.ready();
        },
        ready: function () {
            var that = this;
            (function() {
                var v = document.getElementsByClassName("youtube-player");
                for (var n = 0; n < v.length; n++) {
                    var p = document.createElement("div");
                    p.innerHTML = that.labnolThumb(v[n].dataset.id);
                    p.onclick = that.labnolIframe;
                    v[n].appendChild(p);
                }
            })();
            if ($('#hmalt-frame').length > 0) {
                var $hmalt_frame = $('#hmalt-frame');
                var $hmalt_wrap = $hmalt_frame.parent().parent();
                SlyUtil.create($hmalt_wrap, '#hmalt-frame', '.hmalt-next', '.hmalt-prev').init();
            }

            var gallerySlider = $('.bxslider').bxSlider({
                mode: 'fade',
                auto: 'true',
                pause: 8000,
                speed: 1000,
                nextSelector: '.slider_right',
                prevSelector: '.slider_left'
            });
            $('.slider-move').click(function (e) {
                e.preventDefault();
                var number = $(this).data('number');
                gallerySlider.goToSlide(number);
                return false;
            });
        },
        labnolThumb: function (id) {
            return '<img class="youtube-thumb" src="https://i.ytimg.com/vi/' + id + '/hqdefault.jpg"><div class="play-button"></div>';
        },
        labnolIframe: function () {
            var that = this;
            var iframe = document.createElement("iframe");
            iframe.setAttribute("src", "https://www.youtube.com/embed/" + that.parentNode.dataset.id + "?rel=0");
            iframe.setAttribute("frameborder", "0");
            iframe.setAttribute("id", "youtube-iframe");
            that.parentNode.replaceChild(iframe, that);
        },
        initialize: function() {
            Analytics.apply(Analytics.TYPE_GENERAL);
            $.cloudinary.config({ cloud_name: 'mygubbi', api_key: '492523411154281'});
            _.bindAll(this, 'renderWithUserProfCallback');
        },
        submit: function(e) {
            if (e.isDefaultPrevented()) return;
            e.preventDefault();

            var name = $('#banner_contact_full_name').val();
            var email = $('#banner_contact_email_id').val();
            var phone = $('#banner_contact_contact_num').val();
            var propertyName = $('#banner_contact_property_name').val();
            var query = $('#banner_contact_requirement').val();
            var floorplan = $("#banner_contact_floorplan").prop('files')[0];

            ConsultUtil.submit(name, email, phone, query, floorplan, propertyName, null, null);

            window.App.router.navigate('/thankyou-contact-banner', {
                trigger: true
            });

        },
        events: {
            "submit #banner_contactForm": "submit"
        }
    });
    return DashboardPage;
});
