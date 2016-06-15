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
            'crockery-unit-designs(/)': 'crockeryUnitLD',
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
            'thankyou-:page(/)':'thankyou',
            'lp(/)':'landingpage',
            'oglp(/)':'newlandingpage',
            'fblp(/)':'fblandingpage',
            'mangalore-lp(/)':'mangalorelandingpage',
            'email-lp(/)':'emaillandingpage',
            'nri-:cityName(/)' : 'nripage',
            'media(/)': 'mediapage',
            'shobha-lp(/)': 'shobhalandingpage',
            'pune-lp(/)': 'punelandingpage',
            'kitchen-lp(/)': 'litchenlandingpage',
            'holiday-lp(/)': 'holidaylandingpage',
             '*something': 'errorPage'


        },
        dashboard: function() {
            document.title = 'Home Decor, Modular Kitchen, Wardrobe Designs & Renovation Ideas | mygubbi';
            document.querySelector('meta[name="description"]').setAttribute("content", "Buy Customized Home D?cor Interiors online for living room furniture, kitchen furniture, side tables and TV furniture.Get Home d?cor ideas to design your Home");
            document.querySelector('meta[name="keywords"]').setAttribute("content", "home interior décor, home interior design, interior home décor, home interiors, custom made furniture, custom furniture, online customised furniture, modern furniture design, furniture designs");
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
            document.querySelector('meta[name="description"]').setAttribute("content", "Designs of Study Table which complement Your Room and Interiors.Get the latest study table design designed to meet your needs.");
            document.querySelector('meta[name="keywords"]').setAttribute("content", "study table, study table designs, study table online ,buy study table online, order study table online, latest study table design");
        },
        tvUnitLD: function() {
            document.title = 'Buy entertainment units online and tv unit designs from mygubbi';
            document.querySelector('meta[name="description"]').setAttribute("content", "Deck Your Home With the best of Décor with our range of TV Furniture and TV Cabinet Designs.Pick out luxury Home décor from MyGubbi");
            document.querySelector('meta[name="keywords"]').setAttribute("content", "tv unit, tv stand, tv cabinet, tv units, entertainment unit,entertainment unit designs");
        },
        shoeRackLD: function() {
            document.title = 'Buy Shoe Stands and Shoe Racks Online from mygubbi';
            document.querySelector('meta[name="description"]').setAttribute("content", "Give a neater and classy look to your Home by getting Your Shoe Rack Furniture Custom made by our designers who look into the minute details for better living");
            document.querySelector('meta[name="keywords"]').setAttribute("content", "shoe rack, shoe rack online, shoe rack designs, shoe racks, shoe racks online");
        },
        crockeryUnitLD: function() {
            document.title = 'Crockery unit designs - Kitchen and Dining crockery Unit designs from mygubbi';
            document.querySelector('meta[name="description"]').setAttribute("content", "Look for Crockery unit designs Online and get to select from a range of exclusive products from MyGubbi.");
            document.querySelector('meta[name="keywords"]').setAttribute("content", "crockery, crockery unit, crockery cabinet, crockery unit designs, crockery cabinet online,kitchen crockery cabinet designs, dining crockery unit designs, designs for crockery unit");
        },
        lshapedK: function() {
            document.title = 'L-Shaped Kitchen - L-Shaped Modular Kitchen Designs from mygubbi';
            document.querySelector('meta[name="description"]').setAttribute("content", "L shaped Kitchen Designs for Your Dream Modular Kitchen.Get a more spacious and modern kitchen which match up to the latest Décor.");
            document.querySelector('meta[name="keywords"]').setAttribute("content", "l shaped kitchen design, l shaped kitchen, l shaped kitchen designs");
        },
        ushapedK: function () {
            document.title = 'U-Shaped Kitchen Design - U-Shaped Kitchen from mygubbi';
            document.querySelector('meta[name="description"]').setAttribute("content", "U shaped Kitchen Designs for Your Dream Modular Kitchen.Get a more spacious and modern kitchen which match up to the latest Décor.");
            document.querySelector('meta[name="keywords"]').setAttribute("content", "u shaped kitchen, u shaped kitchen design,  u shaped kitchen designs");
        },
        straightK: function () {
            document.title = 'Straight Kitchen Design - Straight Modular Kitchen Designs from mygubbi';
            document.querySelector('meta[name="description"]').setAttribute("content", "Straight Kitchen Designs for Your Dream Modular Kitchen.Get a more spacious and modern kitchen which match up to the latest Décor.");
            document.querySelector('meta[name="keywords"]').setAttribute("content", "straight kitchen design, straight modular kitchen designs, straight modular kitchen");
        },
        parallelK: function() {
            document.title = 'Parallel Kitchen Design - Parallel Kitchen Cabinets from mygubbi';
            document.querySelector('meta[name="description"]').setAttribute("content", "Parallel Kitchen Designs for Your Dream Modular Kitchen.Get a more spacious and modern kitchen which match up to the latest Décor.");
            document.querySelector('meta[name="keywords"]').setAttribute("content", "parallel kitchen design, parallel kitchen, parallel kitchen designs,parallel modular kitchen designs");
        },
        wardrobeB: function() {
            document.title = 'Bedroom wardrobe Design -Buy Wardrobes Online from mygubbi';
            document.querySelector('meta[name="description"]').setAttribute("content", "Check out Our Exclusive range of Wardrobe designs which add a classy touch to your Home décor.MyGubbi offers a hassle free installation process and speedy delivery");
            document.querySelector('meta[name="keywords"]').setAttribute("content", "wardrobe, wardrobe designs, wardrobe design,wardrobe online, buy wardrobe online");
        },
        sideTableB: function() {
            document.title = 'Side Table - Bedside tables Online from mygubbi';
            document.querySelector('meta[name="description"]').setAttribute("content", "Bedside Table Designs that compliment Your Bedroom décor in ways like no other.Pick Out Luxury Home Décor from MyGubbi");
            document.querySelector('meta[name="keywords"]').setAttribute("content", "side table, side tables,study table designs, designs of study table, buy study table online, order study table online");
        },
        bookShelfB: function() {
            document.title = 'Book Rack - Book shelves and Book Organizers from mygubbi';
            document.querySelector('meta[name="description"]').setAttribute("content", "Custom Made Bookshelves from MyGubbi add to your range of luxury décor and add a touch of elegance to your living abode");
            document.querySelector('meta[name="keywords"]').setAttribute("content", "book shelf, bookshelf design, book rack, bookshelf designs");
        },
        bedroom: function () {
            document.title = 'Bedroom Designs - Bedroom Furniture and interiors from mygubbi';
            document.querySelector('meta[name="description"]').setAttribute("content", "MyGubbi offers an Exclusive collection of bedroom furniture to choose from.Get Your Bedroom interiors custom made to fit your Home and Décor.");
            document.querySelector('meta[name="keywords"]').setAttribute("content", "bedroom designs, bedroom furniture, bedroom interior, bedroom interior design");
        },
        kitchens: function() {
            document.title = 'Modular Kitchen - Range of modular Kitchen Designs from mygubbi';
            document.querySelector('meta[name="description"]').setAttribute("content", "Check out our Range of Kitchen Cabinets and choose the kitchen cabinet design of Your choice.");
            document.querySelector('meta[name="keywords"]').setAttribute("content", "modular kitchen, modular kitchen design, kitchen design, kitchen cabinets, kitchen, kitchen interior, kitchen cabinet, kitchen furniture,kitchen cabinet designs, kitchen cabinets designs");
        },
        livingDining: function () {
            document.title = 'Living room designs and interiors from mygubbi';
            document.querySelector('meta[name="description"]').setAttribute("content", "Buy Your Living Room furniture online from MyGubbi and jazz up your life with the latest Décor.Get Your Dining Room furniture custom made by picking out from a range of dining room designs");
            document.querySelector('meta[name="keywords"]').setAttribute("content", "living and dining room, living and dining room designs, living and dining room design ,living room furniture online,living room designs, dining room designs, dining room furniture,living room interior");
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
        router.on('route:shoeRackLD', function(actions){
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
        router.on('route:landingpage', function(actions) {
            setTimeout($('.page').append("<i class='page-tran fa fa-spinner fa-spin'></i>"), 0);
            require(['/js/views/landing_pages/landing_page.js'], function(LandingPage) {
                VM.create(VM.LANDINGPAGE, LandingPage).render();
            });
        });
        router.on('route:newlandingpage', function(actions) {
            setTimeout($('.page').append("<i class='page-tran fa fa-spinner fa-spin'></i>"), 0);
            require(['/js/views/landing_pages/newlanding_page.js'], function(NewLandingPage) {
                VM.create(VM.NEWLANDINGPAGE, NewLandingPage).render();
            });
        });
        router.on('route:fblandingpage', function(actions) {
            setTimeout($('.page').append("<i class='page-tran fa fa-spinner fa-spin'></i>"), 0);
            require(['/js/views/landing_pages/fblanding_page.js'], function(FbLandingPage) {
                VM.create(VM.FBLANDINGPAGE, FbLandingPage).render();
            });
        });
        router.on('route:mangalorelandingpage', function(actions) {
            setTimeout($('.page').append("<i class='page-tran fa fa-spinner fa-spin'></i>"), 0);
            require(['/js/views/landing_pages/mangalorelanding_page.js'], function(MangaloreLandingPage) {
                VM.create(VM.MANGALORELANDINGPAGE, MangaloreLandingPage).render();
            });
        });
        router.on('route:emaillandingpage', function(actions) {
            setTimeout($('.page').append("<i class='page-tran fa fa-spinner fa-spin'></i>"), 0);
            require(['/js/views/landing_pages/emaillanding_page.js'], function(EmailLandingPage) {
                VM.create(VM.EMAILLANDINGPAGE, EmailLandingPage).render();
            });
        });
        router.on('route:shobhalandingpage', function(actions) {
            setTimeout($('.page').append("<i class='page-tran fa fa-spinner fa-spin'></i>"), 0);
            require(['/js/views/landing_pages/shobhalanding_page.js'], function(ShobhaLandingPage) {
                VM.create(VM.SHOBHALANDINGPAGE, ShobhaLandingPage).render();
            });
        });

        router.on('route:punelandingpage', function(actions) {
            setTimeout($('.page').append("<i class='page-tran fa fa-spinner fa-spin'></i>"), 0);
            require(['/js/views/landing_pages/punelanding_page.js'], function(PuneLandingPage) {
                VM.create(VM.PUNELANDINGPAGE, PuneLandingPage).render();
            });
        });

        router.on('route:kitchenlandingpage', function(actions) {
                    setTimeout($('.page').append("<i class='page-tran fa fa-spinner fa-spin'></i>"), 0);
                    require(['/js/views/landing_pages/kitchenlanding_page.js'], function(KitchenLandingPage) {
                        VM.create(VM.KITCHENLANDINGPAGE, KitchenLandingPage).render();
                    });
                });

        router.on('route:errorPage', function(actions) {
                    setTimeout($('.page').append("<i class='page-tran fa fa-spinner fa-spin'></i>"), 0);
                    require(['/js/views/errorPage/errorPage.js'], function(ErrorPage) {
                        VM.create(VM.ERRORPAGE, ErrorPage).render();
                    });
                });
        router.on('route:holidaylandingpage', function(actions) {
            setTimeout($('.page').append("<i class='page-tran fa fa-spinner fa-spin'></i>"), 0);
            require(['/js/views/landing_pages/holidaylanding_page.js'], function(HolidayLandingPage) {
                VM.create(VM.HOLIDAYLANDINGPAGE, HolidayLandingPage).render();
            });
        });
        router.on('route:mediapage', function(actions) {
            setTimeout($('.page').append("<i class='page-tran fa fa-spinner fa-spin'></i>"), 0);
            require(['/js/views/media_pages/media_page.js'], function(MediaPage) {
                VM.create(VM.MEDIAPAGE, MediaPage).render();
            });
        });
        router.on('route:nripage', function(cityName) {
            setTimeout($('.page').append("<i class='page-tran fa fa-spinner fa-spin'></i>"), 0);
            require(['/js/views/nri_pages/nri_page.js'], function(NriPage) {
                var options = {
                    model: {
                        "cityName": cityName
                    }
                };
                VM.create(VM.NRIPAGE, NriPage, options).render();
            });
        });

        router.on('route', function () {
            $("html,body").scrollTop(0);
        });
        Backbone.history.start({
            pushState: true,
            root: "/"
        });
     if(Backbone.history.fragment === "*something"){
     Backbone.history.navigate("pune-lp");
     ContactManager.ContactsApp.List.Controller.listContacts();
     }

    };
    return {
        initialize: initialize
    };
});