/*
 * Created by mygubbi on 14/09/16.
 */
define([
    'jquery',
    'underscore',
    'backbone',
    'text!templates/kitchenAppliance/kitchen_appliance.html',
    'cloudinary_jquery',
    'slyutil',
    'mgfirebase',
    'consultutil',
    'analytics'
], function($, _, Backbone, kitchenAppliancePageTemplate, CloudinaryJquery, SlyUtil, MGF, ConsultUtil, Analytics) {
    var kitchenAppliancePageVIew = Backbone.View.extend({
        el: '.page',
        ref: null,
        renderWithUserProfCallback: function(userProfData){
            $(this.el).html(_.template(kitchenAppliancePageTemplate)({
                'userProfile': userProfData
            }));
            $.cloudinary.responsive();
        },
        render: function(){
            var authData = this.ref.getAuth();
            MGF.getUserProfile(authData, this.renderWithUserProfCallback);
        },
        initialize: function(){
            this.ref = MGF.rootRef;
            Analytics.apply(Analytics.TYPE_GENERAL);
            $.cloudinary.config({ cloud_name: 'mygubbi', api_key: '492523411154281'});
            _.bindAll(this, 'renderWithUserProfCallback');
        },
        events: {
            "click .hob-lnk": "changehobtab",
            "click .chimney-lnk": "changechimneytab",
            "click .apl-lnk": "changeapltab",
            "click .sink-lnk": "changesinktab",
            "click .faucet-lnk": "changefaucettab"
        },
        changehobtab: function(e){
            e.preventDefault();
            var currentTarget = $(e.currentTarget);
            var id = $(e.currentTarget).attr('id');
            $(".hob-img").removeClass('active');
             $(".hob-cnt").removeClass('active');
                $("#"+id+"-img").addClass('active');
            $("#"+id+"-content").addClass('active');

            console.log(id);
            return this;
        },
        changechimneytab: function(e){
            e.preventDefault();
            var currentTarget = $(e.currentTarget);
            var id = $(e.currentTarget).attr('id');
            $(".chimney-img").removeClass('active');
             $(".chimney-cnt").removeClass('active');
                $("#"+id+"-img").addClass('active');
            $("#"+id+"-content").addClass('active');

            console.log(id);
            return this;
        },
        changeapltab: function(e){
            e.preventDefault();
            var currentTarget = $(e.currentTarget);
            var id = $(e.currentTarget).attr('id');
            $(".apl-img").removeClass('active');
             $(".apl-cnt").removeClass('active');
                $("#"+id+"-img").addClass('active');
            $("#"+id+"-content").addClass('active');

            console.log(id);
            return this;
        },
        changesinktab: function(e){
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
        changefaucettab: function(e){
            e.preventDefault();
            var currentTarget = $(e.currentTarget);
            var id = $(e.currentTarget).attr('id');
            $(".faucet-img").removeClass('active');
             $(".faucet-cnt").removeClass('active');
                $("#"+id+"-img").addClass('active');
            $("#"+id+"-content").addClass('active');

            console.log(id);
            return this;
        }
    });
    return kitchenAppliancePageVIew;
});