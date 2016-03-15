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
        }
    });
    return MediaPageVIew;
});