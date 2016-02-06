define([
    'jquery',
    'underscore',
    'backbone',
    'bootstrap',
    '/js/views/view_manager.js'
], function($, _, Backbone, Bootstrap, VM) {
    var AppRouter = Backbone.Router.extend({
        routes: {
            '': 'dashboard',
            'products/:categories(/:subcategories)(/)': 'products',
            'product_search/:searchTerm(/)': 'products-search',
            'product/:id(/)': 'product',
            'user_profile(/)': 'user_profile',
            'consult(/)': 'consult',
            'shortlist(/)': 'shortlist',
            'stories(/)': 'stories',
            'story/:id(/)': 'story'
        },
        dashboard: function() {
            document.title = 'Home Decor, Modular Kitchen, Wardrobe Designs & Renovation Ideas | mygubbi';
        },
        consult: function() {
            document.title = 'Consult our experts | mygubbi';
        },
        shortlist: function() {
            document.title = 'Shortlisted products for easy and fast access | mygubbi';
        },
        stories: function() {
            document.title = 'Latest Tips from our Experts | mygubbi';
        }

    });

    var initialize = function(options) {
        var appView = options.appView;
        var router = new AppRouter(options);
        window.App = window.App || {};
        window.App.router = router;
        router.on('route:dashboard', function(actions) {
            require(['/js/views/dashboard/page.js'], function(DashboardPage) {
                VM.create("dashboard", DashboardPage).render();
            });
        });
        router.on('route:products', function(categories, subcategories, searchTerm, sortBy, sortDir, layout) {
            require(['/js/views/product/page.js'], function(ProductPage) {
                var options = {
                    model: {
                        "selectedCategories": categories,
                        "selectedSubCategories": subcategories,
                        "searchTerm": searchTerm,
                        "sortBy": sortBy,
                        "sortDir": sortDir,
                        "layout": layout
                    }
                };
                VM.create("productListing", ProductPage, options).render();
            });
        });
        router.on('route:products-search', function(searchTerm) {
            require(['/js/views/product/page.js'], function(ProductPage) {
                console.log('searchTerm');
                console.log(searchTerm);
                var options = {
                    model: {
                        "searchTerm": searchTerm
                    }
                };
                VM.create("productListing", ProductPage, options).render();
            });
        });
        router.on('route:product', function(productId) {
            require(['/js/views/product/details.js'], function(ProductDetailPage) {
                var options = {
                    model: {
                        "id": productId
                    }
                };
                VM.create("productDetails", ProductDetailPage, options).render();
            });
        });
        router.on('route:user_profile', function(actions) {
            require(['/js/views/user_profile/user_profile.js'], function(UserProfilePage) {
                VM.create("userProfile", UserProfilePage).render();
            });
        });
        router.on('route:consult', function(actions) {
            require(['/js/views/consult/consult.js'], function(ConsultPage) {
                VM.create("consult", ConsultPage).render();
            });
        });
        router.on('route:shortlist', function(actions) {
            require(['/js/views/shortlist/shortlist.js'], function(ShortlistPage) {
                VM.create("shortlist", ShortlistPage).render();
            });
        });
        router.on('route:stories', function(actions) {
            require(['/js/views/story/stories.js'], function(StoriesPage) {
                VM.create("stories", StoriesPage).render();
            });
        });
        router.on('route:story', function(storyId) {
            require(['/js/views/story/full_story.js'], function(FullStoryPage) {

                var options = {
                    model: {
                        "id": storyId
                    }
                };
                VM.create("story", FullStoryPage, options).render();
            });
        });
        router.on('route', function () {
            $("html,body").scrollTop(0);
        });
        Backbone.history.start({
            pushState: true,
            root: "/"
        });
    };
    return {
        initialize: initialize
    };
});