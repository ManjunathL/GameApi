/**
 * Created by mygubbi on 11/07/17.
 */
define([
    'jquery',
    'underscore',
    'backbone',
    'text!templates/landing_pages/double-bonanza.html',
    'cloudinary_jquery',
    'mgfirebase',
    'consultutil',
    'analytics'
], function($, _, Backbone, DoubleBonanzaPageTemplate, CloudinaryJquery, MGF, ConsultUtil, Analytics) {
    var DoubleBonanzaPageVIew = Backbone.View.extend({
        el: '.page',
        ref: null,
        refAuth: null,
        renderWithUserProfCallback: function(userProfData) {
            var catId = this.model.id;

            $(this.el).html(_.template(DoubleBonanzaPageTemplate)({
                'userProfile': userProfData,
                'catId':catId
            }));
            $.cloudinary.responsive();
        },
        render: function() {
            var authData = this.refAuth.currentUser;
            document.getElementById("canlink").href = window.location.href;
            MGF.getUserProfile(authData, this.renderWithUserProfCallback);
        },
        initialize: function() {
            this.ref = MGF.rootRef;
            this.refAuth = MGF.refAuth;
            Analytics.apply(Analytics.TYPE_GENERAL);
            $.cloudinary.config({ cloud_name: 'mygubbi', api_key: '492523411154281'});
            _.bindAll(this, 'renderWithUserProfCallback');
        }
    });
    return DoubleBonanzaPageVIew;
});