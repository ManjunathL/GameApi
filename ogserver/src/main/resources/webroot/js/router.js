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

            'shoe-rack-online(/)': 'shoeRackLD',
            'crockery-unit-cabinet(/)': 'crockeryUnitLD',
            'tv-unit-cabinet(/)': 'tvUnitLD',
            'sideboard(/)': 'sideboardLD',
            'foyer-design(/)': 'foyerLD',
            'products/Living & Dining/Foyer(/)': 'foyerLD',
            'l-shaped-kitchen-design(/)': 'lshapedK',
            'u-shaped-kitchen-design(/)': 'ushapedK',
            'straight-kitchen-design(/)': 'straightK',
            'parallel-kitchen-design(/)': 'parallelK',
            'wardrobe-designs-online(/)': 'wardrobeB',
            'study-table-designs-online(/)': 'studyTableB',
            'side-table-design-online(/)': 'sideTableB',
            'book-shelf(/)': 'bookShelfB',

            'kitchen-cabinet-design(/)': 'kitchens',
            'living-and-dining-room-designs(/)': 'livingDining',
            'bedroom-interior-design(/)': 'bedroom',

            'products/:categories(/:subcategories)(/)': 'products',
            'product_search-:searchTerm(/)': 'products-search',
            'product-:id(/)': 'product',
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
            'story-:name(/)': 'story',
            'thankyou(/)':'thankyou'
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
        },
        thankyou: function() {
            document.title = 'Thank you for contacting mygubbi';
        },
        careers: function() {
            document.title = 'Careers | mygubbi';
        },
        about: function() {
            document.title = 'About | mygubbi';
        },
        faq: function() {
            document.title = 'FAQs | mygubbi';
        },
        terms: function() {
            document.title = 'T&C | mygubbi';
        },
        privacypolicy: function() {
            document.title = 'Privacy Policy | mygubbi';
        },
        mygubbidiff: function() {
            document.title = 'mygubbi difference | mygubbi';
        },
        studyTableB: function () {
            document.title = 'Study Table Designs - Order Study Table Online from mygubbi';
        },
        tvUnitLD: function() {
            document.title = 'Buy entertainment units online and tv unit designs from mygubbi';
        },
        shoeRackLD: function() {
            document.title = 'Buy Shoe Stands and Shoe Racks Online from mygubbi';
        },
        crockeryUnitLD: function() {
            document.title = 'Crockery unit designs - Kitchen and Dining crockery Unit designs from mygubbi';
        },
        lshapedK: function() {
            document.title = 'L-Shaped Kitchen - L-Shaped Modular Kitchen Designs from mygubbi';
        },
        ushapedK: function () {
            document.title = 'U-Shaped Kitchen Design - U-Shaped Kitchen from mygubbi';
        },
        straightK: function () {
            document.title = 'Straight Kitchen Design - Straight Modular Kitchen Designs from mygubbi';
        },
        parallelK: function() {
            document.title = 'Parallel Kitchen Design - Parallel Kitchen Cabinets from mygubbi';
        },
        wardrobeB: function() {
            document.title = 'Bedroom wardrobe Design -Buy Wardrobes Online from mygubbi';
        },
        sideTableB: function() {
            document.title = 'Side Table - Bedside tables Online from mygubbi';
        },
        bookShelfB: function() {
            document.title = 'Book Rack - Book shelves and Book Organizers from mygubbi';
        },
        bedroom: function () {
            document.title = 'Bedroom Designs - Bedroom Furniture and interiors from mygubbi';
        },
        kitchens: function() {
            document.title = 'Modular Kitchen - Range of modular Kitchen Designs from mygubbi';
        },
        livingDining: function () {
            document.title = 'Living room designs and interiors from mygubbi';
        }
    });

    var routeSubCategory = function(subCategory, category) {
        setTimeout($('.page').append("<i class='page-tran fa fa-spinner fa-spin'></i>"), 0);
        require(['/js/views/product/page.js'], function(ProductPage) {
            var options = {
                model: {
                    "selectedCategories": category,
                    "selectedSubCategories": subCategory
                }
            };
            VM.create(VM.PRODUCT_LISTING, ProductPage, options).render();
        });
    };

    var routeCategory = function(category) {
        setTimeout($('.page').append("<i class='page-tran fa fa-spinner fa-spin'></i>"), 0);
        require(['/js/views/product/page.js'], function(ProductPage) {
            var options = {
                model: {
                    "selectedCategories": category
                }
            };
            VM.create(VM.PRODUCT_LISTING, ProductPage, options).render();
        });
    };

    var initialize = function(options) {
        var appView = options.appView;
        var router = new AppRouter(options);
        window.App = window.App || {};
        window.App.router = router;
        router.on('shoeRackLD', function(actions){
            routeSubCategory('Shoe Rack', 'living & dining');
        });
        router.on('route:crockeryUnitLD', function(actions){
            routeSubCategory('Crockery Unit', 'living & dining');
        });
        router.on('route:tvUnitLD', function(actions){
            routeSubCategory('Entertainment Unit', 'living & dining');
        });
        router.on('route:sideboardLD', function(actions){
            routeSubCategory('Sideboard', 'living & dining');
        });
        router.on('route:foyerLD', function(actions){
            routeSubCategory('Foyer Unit', 'living & dining');
        });
        router.on('route:lshapedK', function(actions){
            routeSubCategory('L Shaped Kitchen', 'kitchen');
        });
        router.on('route:ushapedK', function(actions){
            routeSubCategory('U Shaped Kitchen', 'kitchen');
        });
        router.on('route:straightK', function(actions){
            routeSubCategory('Straight Kitchen', 'kitchen');
        });
        router.on('route:parallelK', function(actions){
            routeSubCategory('Parallel Kitchen', 'kitchen');
        });
        router.on('route:wardrobeB', function(actions){
            routeSubCategory('Wardrobe', 'bedroom');
        });
        router.on('route:studyTableB', function(actions){
            routeSubCategory('Study Table', 'bedroom');
        });
        router.on('route:sideTableB', function(actions){
            routeSubCategory('Side Table', 'bedroom');
        });
        router.on('route:bookShelfB', function(actions){
            routeSubCategory('Book Rack', 'bedroom');
        });
        router.on('route:kitchens', function(actions){
            routeCategory('kitchen');
        });
        router.on('route:livingDining', function(actions){
            routeCategory('living & dining');
        });
        router.on('route:bedroom', function(actions){
            routeCategory('bedroom');
        });
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
        router.on('route:thankyou', function(actions) {
            setTimeout($('.page').append("<i class='page-tran fa fa-spinner fa-spin'></i>"), 0);
            require(['/js/views/thankyou/thankyou.js'], function(ThankYouPage) {
                VM.create(VM.THANKYOU, ThankYouPage).render();
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