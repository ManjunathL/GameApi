define([
    'jquery',
    'underscore',
    'backbone',
    'bootstrap',
    'text!templates/seo_page/kitchen-slp.html',
    'cloudinary_jquery',
    'slyutil',
    'mgfirebase',
    'analytics'
], function($, _, Backbone, Bootstrap, KitchenSLpPageTemplate, CloudinaryJquery, SlyUtil, MGF, Analytics) {
    var KitchenSLpPageVIew = Backbone.View.extend({
        el: '.page',
        ref: MGF.rootRef,
        refAuth: MGF.refAuth,

        renderWithUserProfCallback: function(userProfData) {
            $(this.el).html(_.template(KitchenSLpPageTemplate)({
                'userProfile': userProfData
            }));
            $.cloudinary.responsive();
        },

        render: function() {
            var authData = this.refAuth.currentUser;
            document.getElementById("canlink").href = window.location.href;
            MGF.getUserProfile(authData, this.renderWithUserProfCallback);
        },

        initialize: function() {
            Analytics.apply(Analytics.TYPE_GENERAL);
            $.cloudinary.config({ cloud_name: 'mygubbi', api_key: '492523411154281'});
            _.bindAll(this, 'renderWithUserProfCallback');
        }
    });
    return KitchenSLpPageVIew;
});