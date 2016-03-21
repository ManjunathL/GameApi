/**
 * Created by mygubbi on 14/3/16.
 */
define([
    'jquery',
    'underscore',
    'backbone',
    'text!/templates/media_pages/media_page.html',
    'cloudinary_jquery',
    '/js/slyutil.js',
    '/js/mgfirebase.js',
    '/js/consultutil.js',
    '/js/analytics.js'
], function($, _, Backbone, mediaPageTemplate, CloudinaryJquery, SlyUtil, MGF, ConsultUtil, Analytics) {
    var MediaPageVIew = Backbone.View.extend({
        el: '.page',
        ref: null,
        render: function() {
            $(this.el).html(_.template(mediaPageTemplate));
            $.cloudinary.responsive();
        },
        initialize: function() {
            this.ref = MGF.rootRef;
            Analytics.apply(Analytics.TYPE_GENERAL);
            $.cloudinary.config({ cloud_name: 'mygubbi', api_key: '492523411154281'});
        },
        events: {
             "click #online-tab": "toggleOnlineContent",
             "click #press-release-tab": "togglePressReleaseContent",
             "click #print-media-tab": "togglePrintMediaContent"
        },
        toggleOnlineContent: function(e){
            $("#press-release-img").css('display','none');
            $("#print-media-img").css('display','none');
            $("#online-img").css('display','block');

            $("#press-release-content").css('display','none');
            $("#print-media-content").css('display','none');
            $("#online-content").css('display','block');

            $("#tab-bar").removeClass('press_media-box');
            $("#tab-bar").removeClass('print_media-box');
            $("#tab-bar").addClass('online_media-box');
        },
        togglePressReleaseContent: function(e){
            $("#online-img").css('display','none');
            $("#print-media-img").css('display','none');
            $("#press-release-img").css('display','block');

            $("#online-content").css('display','none');
            $("#print-media-content").css('display','none');
            $("#press-release-content").css('display','block');

            $("#tab-bar").removeClass('online_media-box');
            $("#tab-bar").removeClass('print_media-box');
            $("#tab-bar").addClass('press_media-box');
        },
        togglePrintMediaContent: function(e){

            $("#press-release-img").css('display','none');
            $("#online-img").css('display','none');
            $("#print-media-img").css('display','block');


            $("#press-release-content").css('display','none');
            $("#online-content").css('display','none');
            $("#print-media-content").css('display','block');

            $("#tab-bar").removeClass('press_media-box');
            $("#tab-bar").removeClass('online_media-box');
            $("#tab-bar").addClass('print_media-box');
        },
    });
    return MediaPageVIew;
});