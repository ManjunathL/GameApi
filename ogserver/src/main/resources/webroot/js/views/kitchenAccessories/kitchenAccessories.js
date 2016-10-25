/**
 * Created by mygubbi on 14/09/16.
 */
define([
    'jquery',
    'underscore',
    'backbone',
    'text!templates/kitchenAccessories/kitchenAccessories.html',
    'cloudinary_jquery',
    'slyutil',
    'mgfirebase',
    'consultutil',
    'analytics'
], function($, _, Backbone, kitchenAccessoriesPageTemplate, CloudinaryJquery, SlyUtil, MGF, ConsultUtil, Analytics) {
    var kitchenAccessoriesPageVIew = Backbone.View.extend({
        el: '.page',
        ref: null,
        renderWithUserProfCallback: function(userProfData) {
            $(this.el).html(_.template(kitchenAccessoriesPageTemplate)({
                'userProfile': userProfData
            }));
            $.cloudinary.responsive();
        },
        render: function() {
            var authData = this.ref.getAuth();
            MGF.getUserProfile(authData, this.renderWithUserProfCallback);
        },
        initialize: function() {
            this.ref = MGF.rootRef;
            Analytics.apply(Analytics.TYPE_GENERAL);
            $.cloudinary.config({ cloud_name: 'mygubbi', api_key: '492523411154281'});
            _.bindAll(this, 'renderWithUserProfCallback');
        },
        events: {
            "click .drawers-lnk": "changedrawerstab",
            "click .sink-lnk": "changesinktab",
            "click .splash-lnk": "changesplashtab"
        },
        changedrawerstab: function(e) {
            e.preventDefault();
            var currentTarget = $(e.currentTarget);
            var id = $(e.currentTarget).attr('id');
            $(".drawers-img").removeClass('active');
             $(".drawers-cnt").removeClass('active');
                $("#"+id+"-img").addClass('active');
            $("#"+id+"-content").addClass('active');

            console.log(id);
            return this;
        },
        changesinktab: function(e) {
            e.preventDefault();
            var currentTarget = $(e.currentTarget);
            var id = $(e.currentTarget).attr('id');
            $(".sink-img").removeClass('active');
             $(".sink-cnt").removeClass('active');
                $("#"+id+"-img").addClass('active');
            $("#"+id+"-content").addClass('active');

            console.log(id);
            return this;
        },
        changesplashtab: function(e) {
            e.preventDefault();
            var currentTarget = $(e.currentTarget);
            var id = $(e.currentTarget).attr('id');
            $(".splash-img").removeClass('active');
             $(".splash-cnt").removeClass('active');
                $("#"+id+"-img").addClass('active');
            $("#"+id+"-content").addClass('active');

            console.log(id);
            return this;
        }
    });
    return kitchenAccessoriesPageVIew;
});