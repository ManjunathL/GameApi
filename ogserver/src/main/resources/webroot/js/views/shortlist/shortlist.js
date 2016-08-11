define([
    'jquery',
    'underscore',
    'backbone',
    'bootstrap',
    'text!/templates/shortlist/shortlist.html',
    '/js/mgfirebase.js',
    '/js/consultutil.js',
    '/js/analytics.js',
    '/js/views/view_manager.js',
    'css!/css/shortlist.css'
], function($, _, Backbone, Bootstrap, shortlistTemplate, MGF, ConsultUtil, Analytics, VM) {
    var ShortlistView = Backbone.View.extend({
        el: '.page',
        initialize: function() {
            Analytics.apply(Analytics.TYPE_GENERAL);
            this.ref = MGF.rootRef;
            this.listenTo(Backbone, 'user.change', this.render);
            _.bindAll(this, 'render', 'renderWithUserProfCallback');
        },
        renderWithUserProfCallback: function(userProfData) {
            $(this.el).html(_.template(shortlistTemplate)({
                'shortlistedItems': MGF.getShortListedItems(),
                'userProfile': userProfData
            }));
            if(typeof(userProfData) !== 'undefined' && userProfData !== null && typeof(userProfData.displayName) !== 'undefined' && userProfData.displayName !== null){
                document.title = userProfData.displayName + '\'s Shortlist | mygubbi';
            }else{
                document.title = 'Shortlisted products for easy and fast access | mygubbi';
            }

        },
        render: function() {
            if (VM.activeView === VM.SHORTLIST) {
                var authData = this.ref.getAuth();
                MGF.getUserProfile(authData, this.renderWithUserProfCallback);
            }
        },
        events: {
            "click .shortlistable": "removeShortlistItem",
            "click .shortlist_consult": "openConsultPopup",
            "click .close-consult-pop": "closeModal",
            /*"click .consult-form-explore": "closeModal",
            "click #consult-submit-btn": "submitConsultButton",
            "submit .consultForm": "submitConsultForm",*/
            "click .shortgridshare": "toggleGridShareIcons"
        },
        removeShortlistItem: function(e) {
            e.preventDefault();

            var currentTarget = $(e.currentTarget);
            var productId = currentTarget.data('element');
            var that = this;

            MGF.removeShortlistProduct(productId).then(function() {
                currentTarget.children('.list-heart').toggleClass('fa-heart-o');
                currentTarget.children('.list-heart').toggleClass('fa-heart');
                currentTarget.children('.list-txt').html('shortlist');
                that.render();
            });
        },
        closeModal: function(ev) {
            var id = $(ev.currentTarget).data('element');
            $(id).modal('toggle');
        },
        openConsultPopup: function(e) {
            var id = $(e.currentTarget).attr('id');
            var popupid = id.replace('shortlist_consult', '');
            //alert(popupid);
            $('#consultpop' + popupid).modal('show');
        },
        /*submitConsultButton: function() {
            window.consultSubmitButton = this;
        },
        submitConsultForm: function(e) {
            if (e.isDefaultPrevented()) return;
            e.preventDefault();

            var id = $(e.currentTarget).attr('id');
            var formid = id.replace('consultForm', '');
            //alert(formid);
            $('#consult_error' + formid).html('');
            $('#consult_error_row' + formid).css("display", "none");
            this.consultSubmit(formid);
        },
        consultSubmit: function(formid) {

            var productName = $('#consult_product_name' + formid).val();
            var name = $('#consult_full_name' + formid).val();
            var email = $('#consult_email_id' + formid).val();
            var phone = $('#consult_contact_num' + formid).val();
            var propertyName = $('#consult_property_name' + formid).val();
            var query = $('#consult_product_name' + formid).val() + " :: " + $('#consult_requirement' + formid).val();
            var floorplan = $("#consult_floorplan" + formid).prop('files')[0];
            ConsultUtil.submit(name, email, phone, query, floorplan, propertyName, null, null);

            $('#consultpop' + formid).modal('hide');
            $('body').removeClass('modal-open');
            $('.modal-backdrop').remove();

            window.App.router.navigate('/thankyou-shortlist-product', {
                trigger: true
            });

        },*/
         toggleGridShareIcons: function(e){
             e.preventDefault();

             var currentTarget = $(e.currentTarget);
             var shareicoId = currentTarget.attr('id');
             var productId = shareicoId.replace('shortshare-grid-ico','');
             $('#shortgrid-share-txt'+productId).toggle();
         }
    });
    return ShortlistView;
});