/**
 * Created by mygubbi on 14/09/16.
 */
define([
    'jquery',
    'underscore',
    'backbone',
    'text!templates/mygubbiStudio/mygubbi-studio.html',
    'cloudinary_jquery',
    'slyutil',
    'mgfirebase',
    'consultutil',
    'analytics'
], function($, _, Backbone, MygubbiStudioPageTemplate, CloudinaryJquery, SlyUtil, MGF, ConsultUtil, Analytics) {
    var MygubbiStudioPageVIew = Backbone.View.extend({
        el: '.page',
        ref: MGF.rootRef,
        refAuth: MGF.refAuth,
        renderWithUserProfCallback: function(userProfData) {
            $(this.el).html(_.template(MygubbiStudioPageTemplate)({
                'userProfile': userProfData
            }));
            $.cloudinary.responsive();
        },
        render: function() {
            document.getElementById("canlink").href = window.location.href;
            var authData = this.refAuth.currentUser;
            MGF.getUserProfile(authData, this.renderWithUserProfCallback);
        },
        initialize: function() {
            this.ref = MGF.rootRef;
            Analytics.apply(Analytics.TYPE_GENERAL);
            $.cloudinary.config({ cloud_name: 'mygubbi', api_key: '492523411154281'});
            _.bindAll(this, 'renderWithUserProfCallback');
        }
    });
    return MygubbiStudioPageVIew;
});