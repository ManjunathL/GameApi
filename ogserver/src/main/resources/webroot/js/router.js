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
            /*':categories-:subcategories-products(/)': 'products',
            ':categories-products(/)': 'products',*/
            'product_search/:searchTerm(/)': 'products-search',
            'product/:id(/)': 'product',
            'user_profile(/)': 'user_profile',
            'consult(/)': 'consult',
            'shortlist(/)': 'shortlist',
            'careers(/)': 'careers',
            'about-mygubbi(/)': 'about',
            'faq(/)': 'faq',
            'terms(/)': 'terms',
            'privacy-policy(/)': 'privacypolicy',
            'mygubbi-difference(/)': 'mygubbidiff',
            'stories(/)': 'stories',
            'story/:name(/)': 'story',
            '*path': 'default'
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
                VM.create(VM.DASHBOARD, DashboardPage).render();
            });
        });
        router.on('route:products', function(categories, subcategories, searchTerm, sortBy, sortDir, layout) {
            setTimeout($('.page').append("<i class='page-tran fa fa-spinner fa-spin'></i>"), 0);
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
                VM.create(VM.PRODUCT_LISTING, ProductPage, options).render();
            });
        });
        router.on('route:products-search', function(searchTerm) {
            setTimeout($('.page').append("<i class='page-tran fa fa-spinner fa-spin'></i>"), 0);
            require(['/js/views/product/page.js'], function(ProductPage) {
                console.log('searchTerm');
                console.log(searchTerm);
                var options = {
                    model: {
                        "searchTerm": searchTerm
                    }
                };
                VM.create(VM.PRODUCT_LISTING, ProductPage, options).render();
            });
        });
        router.on('route:product', function(productId) {
            setTimeout($('.page').append("<i class='page-tran fa fa-spinner fa-spin'></i>"), 0);
            require(['/js/views/product/details.js'], function(ProductDetailPage) {
                var options = {
                    model: {
                        "id": productId
                    }
                };
                VM.create(VM.PRODUCT_DETAILS, ProductDetailPage, options).render();
            });
        });
        router.on('route:user_profile', function(actions) {
            setTimeout($('.page').append("<i class='page-tran fa fa-spinner fa-spin'></i>"), 0);
            require(['/js/views/user_profile/user_profile.js'], function(UserProfilePage) {
                VM.create(VM.USER_PROFILE, UserProfilePage).render();
            });
        });
        router.on('route:consult', function(actions) {
            setTimeout($('.page').append("<i class='page-tran fa fa-spinner fa-spin'></i>"), 0);
            require(['/js/views/consult/consult.js'], function(ConsultPage) {
                VM.create(VM.CONSULT, ConsultPage).render();
            });
        });
        router.on('route:shortlist', function(actions) {
            setTimeout($('.page').append("<i class='page-tran fa fa-spinner fa-spin'></i>"), 0);
            require(['/js/views/shortlist/shortlist.js'], function(ShortlistPage) {
                VM.create(VM.SHORTLIST, ShortlistPage).render();
            });
        });
        router.on('route:careers', function(actions) {
            setTimeout($('.page').append("<i class='page-tran fa fa-spinner fa-spin'></i>"), 0);
            require(['/js/views/footer/careers.js'], function(CareerPage) {
                VM.create(VM.CAREERS, CareerPage).render();
            });
        });
        router.on('route:about', function(actions) {
            setTimeout($('.page').append("<i class='page-tran fa fa-spinner fa-spin'></i>"), 0);
            require(['/js/views/footer/about.js'], function(AboutPage) {
                VM.create(VM.ABOUT, AboutPage).render();
            });
        });
        router.on('route:faq', function(actions) {
            setTimeout($('.page').append("<i class='page-tran fa fa-spinner fa-spin'></i>"), 0);
            require(['/js/views/footer/faq.js'], function(FaqPage) {
                VM.create(VM.FAQ, FaqPage).render();
            });
        });
        router.on('route:privacypolicy', function(actions) {
            setTimeout($('.page').append("<i class='page-tran fa fa-spinner fa-spin'></i>"), 0);
            require(['/js/views/footer/privacypolicy.js'], function(PrivacyPolicyPage) {
                VM.create(VM.PRIVACY_POLICY, PrivacyPolicyPage).render();
            });
        });
        router.on('route:terms', function(actions) {
            setTimeout($('.page').append("<i class='page-tran fa fa-spinner fa-spin'></i>"), 0);
            require(['/js/views/footer/terms.js'], function(TermsPage) {
                VM.create(VM.TERMS, TermsPage).render();
            });
        });
        router.on('route:mygubbidiff', function(actions) {
            setTimeout($('.page').append("<i class='page-tran fa fa-spinner fa-spin'></i>"), 0);
            require(['/js/views/footer/mygubbidiff.js'], function(MyGubbiDiffPage) {
                VM.create(VM.MGDIFF, MyGubbiDiffPage).render();
            });
        });
        router.on('route:stories', function(actions) {
            setTimeout($('.page').append("<i class='page-tran fa fa-spinner fa-spin'></i>"), 0);
            require(['/js/views/story/stories.js'], function(StoriesPage) {
                VM.create(VM.STORIES, StoriesPage).render();
            });
        });
        router.on('route:story', function(name) {
            setTimeout($('.page').append("<i class='page-tran fa fa-spinner fa-spin'></i>"), 0);
            require(['/js/views/story/full_story.js'], function(FullStoryPage) {

                var options = {
                    model: {
                        "name": name
                    }
                };
                VM.create(VM.STORY, FullStoryPage, options).render();
            });
        });
        router.on('route:default', function(path) {
            console.log("default route - " + path);
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