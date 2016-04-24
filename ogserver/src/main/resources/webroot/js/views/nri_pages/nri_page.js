/**
 * Created by sakshi on 18/4/16.
 */

define([
    'jquery',
    'underscore',
    'backbone',
    'bootstrap',
    'text!/templates/nri_pages/nri_page.html',
    'cloudinary_jquery',
    '/js/mgfirebase.js',
    '/js/consultutil.js',
    '/js/analytics.js'
], function($, _, Backbone, Bootstrap, nriPageTemplate, CloudinaryJquery, MGF, ConsultUtil, Analytics) {
    var NriPageVIew = Backbone.View.extend({
        el: '.page',
        ref: MGF.rootRef,

        renderWithUserProfCallback: function(userProfData) {
            $(this.el).html(_.template(nriPageTemplate)({
                'userProfile': userProfData
            }));
            $.cloudinary.responsive();
        },

        render: function() {
            var authData = this.ref.getAuth();
            MGF.getUserProfile(authData, this.renderWithUserProfCallback);
            this.ready();
        },

        initialize: function() {
            Analytics.apply(Analytics.TYPE_GENERAL);
            $.cloudinary.config({ cloud_name: 'mygubbi', api_key: '492523411154281'});
            _.bindAll(this, 'renderWithUserProfCallback');
        },
        //events: {
        //    "click #online-tab": "toggleOnlineContent",
        //    "click #press-release-tab": "togglePressReleaseContent",
        //    "click #print-media-tab": "togglePrintMediaContent",
        //    "click .close-media-pop": "closeMediaModal"
        //},
        //toggleOnlineContent: function(e){
        //    $("#press-release-img").css('display','none');
        //    $("#print-media-img").css('display','none');
        //    $("#online-img").css('display','block');
        //
        //    $("#press-release-content").css('display','none');
        //    $("#print-media-content").css('display','none');
        //    $("#online-content").css('display','block');
        //
        //    $("#tab-bar").removeClass('press_media-box');
        //    $("#tab-bar").removeClass('print_media-box');
        //    $("#tab-bar").addClass('online_media-box');
        //},
        //togglePressReleaseContent: function(e){
        //    $("#online-img").css('display','none');
        //    $("#print-media-img").css('display','none');
        //    $("#press-release-img").css('display','block');
        //
        //    $("#online-content").css('display','none');
        //    $("#print-media-content").css('display','none');
        //    $("#press-release-content").css('display','block');
        //
        //    $("#tab-bar").removeClass('online_media-box');
        //    $("#tab-bar").removeClass('print_media-box');
        //    $("#tab-bar").addClass('press_media-box');
        //},
        //togglePrintMediaContent: function(e){
        //
        //    $("#press-release-img").css('display','none');
        //    $("#online-img").css('display','none');
        //    $("#print-media-img").css('display','block');
        //
        //
        //    $("#press-release-content").css('display','none');
        //    $("#online-content").css('display','none');
        //    $("#print-media-content").css('display','block');
        //
        //    $("#tab-bar").removeClass('press_media-box');
        //    $("#tab-bar").removeClass('online_media-box');
        //    $("#tab-bar").addClass('print_media-box');
        //},
        //closeMediaModal: function(ev) {
        //    var id = $(ev.currentTarget).data('element');
        //    $("#"+id).modal('toggle');
        //}
    });
    return NriPageVIew;
});