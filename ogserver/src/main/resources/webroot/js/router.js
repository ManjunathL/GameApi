define([
    'jquery',
    'underscore',
    'backbone',
    'bootstrap',
    'views/view_manager'
], function($, _, Backbone, Bootstrap, VM) {
    var AppRouter = Backbone.Router.extend({
        routes: {
            //'': 'dashboard',
            '': 'viewconceptboard',
            'concept-list-:id-:name(/)':'listing_concepts',
            'view(/)': 'dashboard',
            'l1_form(/)': 'l1_form',
            'edit_design-:id(/)': 'editdesign',
            'edit_product-:id(/)': 'editproduct',
            'listing-detail-:id(/)': 'listing_details',
            'product-list(/)': 'products',
            'l3_form(/)': 'l3_form',
            'product-detail-:id(/)': 'product_details',
            '*something': 'errorPage'
        }
    });

    var initialize = function(options) {
        var appView = options.appView;
        var router = new AppRouter(options);
        window.App = window.App || {};
        window.App.router = router;


        router.on('route:dashboard', function(actions) {
            require(['views/dashboard/mindboard'], function(DashboardPage) {
                VM.create(VM.DASHBOARD, DashboardPage).render();
            });
        });

        router.on('route:viewconceptboard', function(actions) {
            require(['views/dashboard/conceptboard'], function(ConceptboardPage) {
                VM.create(VM.CONCEPTBOARD, ConceptboardPage).render();
            });
        });
        router.on('route:listing_concepts', function(conceptboardId,spaceTypeCode) {
            console.log("@@@@@@@@@@@@@@@@@@@@@");
            console.log(conceptboardId);
            console.log(spaceTypeCode);
            console.log("@@@@@@@@@@@@@@@@@@@@@");
           require(['views/dashboard/listing_concept'], function(ListingConceptPage) {
                var options = {
                    model: {
                        "id": conceptboardId,
                        "name": spaceTypeCode
                    }
                };
                VM.create(VM.LISTINGCONCEPTPAGE, ListingConceptPage, options).render();
            });
        });



        /*router.on('route:listing_details', function(designID) {
            require(['views/dashboard/listing_details'], function(ListingDetailsPage) {
            console.log("@@@@@@@@@@@@@@@@@@@@@");
            console.log(designID);
            console.log("@@@@@@@@@@@@@@@@@@@@@");
                var options = {
                    model: {
                        "id": designID
                    }
                };
                VM.create(VM.LISTINGDETAILSPAGE, ListingDetailsPage, options).render();
            });
        });*/

        router.on('route', function () {
            $("html,body").scrollTop(0);
        });
        //Backbone.history.start();
        Backbone.history.start({
            pushState: true,
            root: "/"
        });
        if(Backbone.history.fragment === "*something"){
            ContactManager.ContactsApp.List.Controller.listContacts();
        }
    };
    return {
        initialize: initialize
    };
});
