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
            '': 'dashboard',
            'viewconceptboards(/)': 'viewconceptboard',
            ':name-conceptlist-:id(/)':'listing_concepts',
            'l1_form(/)': 'l1_form',
            'edit_design-:id(/)': 'editdesign',
            'edit_product-:id(/)': 'editproduct',
            'listing-detail-:id(/)': 'listing_details',
            'product-list(/)': 'products',
            'l3_form(/)': 'l3_form',
            'product-detail-:id(/)': 'product_details',
            'concept_search-:term(/)':'concepts_search',
            ':name-viewdesign-:id(/)':'listing_designs',
            'user-profile(/)':'user-profile',
            'user_preference(/)':'user_preference',
            'listofconcepts(/)':'listing_everything',
            '*something': 'errorPage'
        }
    });

    var initialize = function(options) {
        var appView = options.appView;
        var router = new AppRouter(options);
        window.App = window.App || {};
        window.App.router = router;


        router.on('route:dashboard', function(actions) {
        //setTimeout($('.page').append('<img src="img/everything/rotating-balls-spinner.gif"'), 5000);
            require(['views/dashboard/home'], function(HomePage) {
                VM.create(VM.HomePage, HomePage).render();
            });
        });

        router.on('route:viewconceptboard', function(actions) {
            require(['views/dashboard/conceptboard'], function(ConceptboardPage) {
                VM.create(VM.CONCEPTBOARDPAGE, ConceptboardPage).render();
            });
        });

        router.on('route:concepts_search', function(srcterm) {
           srcterm = decodeURIComponent(srcterm);
           require(['views/dashboard/search_concept'], function(SearchConceptPage) {
                 var options = {
                    model: {
                        "searchTerm": srcterm
                    }
                };

                VM.create(VM.SEARCHCONCEPTPAGE, SearchConceptPage,options).render();
            });
        });

        router.on('route:listing_designs', function(spaceTypeCode,conceptboardId) {
                   require(['views/dashboard/view_design'], function(ViewDesignPage) {
                         var options = {
                            model: {
                                "id": conceptboardId,
                                "spaceTypeCode": spaceTypeCode
                            }
                        };

                        VM.create(VM.VIEWDESIGNSPAGE, ViewDesignPage,options).render();
                    });
                });

         router.on('route:listing_everything', function(spaceTypeCode) {
                           require(['views/dashboard/everything_concepts'], function(EverythingConceptPage) {
                                 var options = {
                                    model: {
                                        "spaceTypeCode": spaceTypeCode
                                    }
                                };

                                VM.create(VM.EVERYTHINGCONCEPTPAGE, EverythingConceptPage,options).render();
                            });
                        });


        router.on('route:user-profile', function(actions) {
            require(['views/my_account/user_profile'], function(UserProfilePage) {
                VM.create(VM.USERPROFILEPAGE, UserProfilePage).render();
            });
        });
        router.on('route:user_preference', function(actions) {
            require(['views/my_account/user_preference'], function(UserPreferencePage) {
                VM.create(VM.USERPREFERENCEPAGE, UserPreferencePage).render();
            });
        });
        router.on('route:listing_concepts', function(spaceTypeCode, conceptboardId) {
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
