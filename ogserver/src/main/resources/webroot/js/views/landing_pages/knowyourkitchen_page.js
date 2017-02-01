define([
    'jquery',
    'underscore',
    'backbone',
    'text!templates/landing_pages/knowyourkitchen_page.html',
    'cloudinary_jquery',
    'slyutil',
    'mgfirebase',
    'consultutil',
    'analytics'
], function($, _, Backbone, knowyourkitchenPageTemplate, CloudinaryJquery, SlyUtil, MGF, ConsultUtil, Analytics) {
    var KnowYourKitchenPageVIew = Backbone.View.extend({
        el: '.page',
        ref: null,
        renderWithUserProfCallback: function(userProfData) {
            $(this.el).html(_.template(knowyourkitchenPageTemplate)({
                'userProfile': userProfData
            }));
            $.cloudinary.responsive();
        },
        render: function() {
            var authData = this.ref.getAuth();
            MGF.getUserProfile(authData, this.renderWithUserProfCallback);
            document.getElementById("canlink").href = window.location.href;
            this.ready();
        },
        ready: function() {
            if ($('#lpalt-frame').length > 0) {
                var $lpalt_frame = $('#lpalt-frame');
                var $lpalt_wrap = $lpalt_frame.parent().parent();
                SlyUtil.create($lpalt_wrap, '#lpalt-frame', '.lpalt-next', '.lpalt-prev').init();
            }
            if ($('#brand-frame').length > 0) {
                var $brand_frame = $('#brand-frame');
                var $brand_wrap = $brand_frame.parent().parent();
                SlyUtil.create($brand_wrap, '#brand-frame', '.brand-next', '.brand-prev').init();
            }
        },
        initialize: function() {
            this.ref = MGF.rootRef;
            Analytics.apply(Analytics.TYPE_GENERAL);
            $.cloudinary.config({ cloud_name: 'mygubbi', api_key: '492523411154281'});
            _.bindAll(this, 'renderWithUserProfCallback');
        },
        events: {
         "click .wardrobe-lnk": "changeWardHandletab",
        "click .material-lnk": "changeMaterialtab",
        "click .counter-lnk": "changeCountertab",
        "click .finishes-swatches": "changeSwatchestab",
        "click .typefinishes": "typefinishtab"
        },
        changeSwatchestab: function(e) {
            e.preventDefault();
            var currentTarget = $(e.currentTarget);
            var id = $(e.currentTarget).attr('id');
            $(".swatches-img").removeClass('active');
            $(".swatches-cnt").removeClass('active');

            $("#"+id+"-img").addClass('active');
            $("#"+id+"-cnt").addClass('active');

            return this;
        },
        changeCountertab: function(e) {
            e.preventDefault();
            var currentTarget = $(e.currentTarget);
            var id = $(e.currentTarget).attr('id');
            $(".counter-img").removeClass('active');
             $(".counter-cnt").removeClass('active');
                $("#"+id+"-img").addClass('active');
            $("#"+id+"-cnt").addClass('active');
            console.log(id);
            return this;
        },
        typefinishtab: function(e) {
            e.preventDefault();
            var currentTarget = $(e.currentTarget);
            var id = $(e.currentTarget).attr('id');
            $(".type-img").removeClass('active');
             $(".type-cnt").removeClass('active');
                $("#"+id+"-img").addClass('active');
            $("#"+id+"-cnt").addClass('active');
            console.log(id);
            return this;
        },
        changeWardHandletab: function(e) {
            e.preventDefault();
            var currentTarget = $(e.currentTarget);
            var id = $(e.currentTarget).attr('id');
            $(".wardrobe-img").removeClass('active');
             $(".wardrobe-cnt").removeClass('active');
                $("#"+id+"-img").addClass('active');
            $("#"+id+"-content").addClass('active');

            console.log(id);
            return this;
        },
        changeMaterialtab: function(e) {
            e.preventDefault();
            var currentTarget = $(e.currentTarget);
            var id = $(e.currentTarget).attr('id');
            $(".material-img").removeClass('active');
             $(".material-cnt").removeClass('active');
                $("#"+id+"-img").addClass('active');
            $("#"+id+"-content").addClass('active');

            console.log(id);
            return this;
         }
    });
    return KnowYourKitchenPageVIew;
});