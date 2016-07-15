/**
 * Created by mygubbi on 15/7/16 By Smruti
 */
define([
    'jquery',
    'underscore',
    'backbone',
    'text!/templates/product/new-details.html',
    'cloudinary_jquery',
    '/js/slyutil.js',
    '/js/mgfirebase.js',
    '/js/analytics.js'
], function($, _, Backbone, detailsPageTemplate, CloudinaryJquery, SlyUtil, MGF, Analytics) {
    var DetailsPageVIew = Backbone.View.extend({
        el: '.page',
        ref: null,
        render: function() {
            $(this.el).html(_.template(detailsPageTemplate));
            $.cloudinary.responsive();
        },
        initialize: function() {
            this.ref = MGF.rootRef;
            Analytics.apply(Analytics.TYPE_GENERAL);
            $.cloudinary.config({ cloud_name: 'mygubbi', api_key: '492523411154281'});
        }
    });
    return DetailsPageVIew;
});