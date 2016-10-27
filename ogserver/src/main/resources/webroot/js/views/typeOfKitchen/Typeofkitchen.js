/**
 * Created by mygubbi on 14/09/16.
 */
define([
    'jquery',
    'underscore',
    'backbone',
    'text!templates/typeofkitchen/typeofkitchen.html',
    'cloudinary_jquery',
    'slyutil',
    'mgfirebase',
    'consultutil',
    'analytics'
], function($, _, Backbone, TypeOfKitchenPageTemplate, CloudinaryJquery, SlyUtil, MGF, ConsultUtil, Analytics) {
    var TypeOfKitchenPageVIew = Backbone.View.extend({
        el: '.page',
        ref: null,
        renderWithUserProfCallback: function(userProfData) {
            $(this.el).html(_.template(TypeOfKitchenPageTemplate)({
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
            "click .base-lnk": "changebasetab",
            "click .wall-lnk": "changewalltab",
            "click .tall-lnk": "changetalltab"
        },
        changebasetab: function(e) {
            e.preventDefault();
            var currentTarget = $(e.currentTarget);
            var id = $(e.currentTarget).attr('id');
            $(".base-img").removeClass('active');
             $(".base-cnt").removeClass('active');
                $("#"+id+"-img").addClass('active');
            $("#"+id+"-content").addClass('active');

            return this;
        },
        changewalltab: function(e) {
            e.preventDefault();
            var currentTarget = $(e.currentTarget);
            var id = $(e.currentTarget).attr('id');
            $(".wall-img").removeClass('active');
             $(".wall-cnt").removeClass('active');
                $("#"+id+"-img").addClass('active');
            $("#"+id+"-content").addClass('active');

            return this;
        },
        changetalltab: function(e) {
            e.preventDefault();
            var currentTarget = $(e.currentTarget);
            var id = $(e.currentTarget).attr('id');
            $(".tall-img").removeClass('active');
             $(".tall-cnt").removeClass('active');
                $("#"+id+"-img").addClass('active');
            $("#"+id+"-content").addClass('active');

            console.log(id);
            return this;
        }
    });
    return TypeOfKitchenPageVIew;
});