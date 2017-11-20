define([
    'jquery',
    'underscore',
    'backbone',
    'bootstrap',
    'views/view_manager'
], function($, _, Backbone, Bootstrap, VM) {
/*    var prod = 'products';
    prod = prod.replace(/\s/g,"-");*/
    var AppRouter = Backbone.Router.extend({
        routes: {
            '': 'dashboard',
            ':pageurl/online(/)':'seolp',
            'shoe-rack-online(/)': 'shoeRackLD',
            'crockery-unit-designs(/)': 'crockeryUnitLD',
            'tv-unit-cabinet(/)': 'tvUnitLD',
            'sideboard(/)': 'sideboardLD',
            'foyer-design(/)': 'foyerLD',
            'l-shaped-kitchen-design(/)': 'lshapedK',
            'u-shaped-kitchen-design(/)': 'ushapedK',
            'straight-kitchen-design(/)': 'straightK',
            'parallel-kitchen-design(/)': 'parallelK',
            'wardrobe-designs-online(/)': 'wardrobeB',
            'sliding-wardrobe-online(/)': 'wardrobeSlide',
            'swing-wardrobe-online(/)': 'wardrobeSwing',
            'study-table-designs-online(/)': 'studyTableB',
            'side-table-design-online(/)': 'sideTableB',
            'book-shelf(/)': 'bookShelfB',
            'nest-of-tables-online(/)': 'nestofTableB',
            'coffee-tables-online(/)': 'cofeeTableB',
            'buy-chest-of-drawers-online(/)': 'chestofDrawerB',
            'kitchen-cabinet-design(/)': 'kitchens',
            'storage-solutions(/)': 'livingDining',
            'console-tables(/)': 'consoletables',
            'dressers(/)': 'dressers',

//            'living-and-dining-room-designs(/)': 'livingDining',
//            'wardrobe-designs-online(/)': 'bedroom',
            /* Start Dynamic City Url SEO -- Mehbub */
//            'l-shaped-kitchen-design-:cityName(/)': 'lshapedK',
//            'shoe-rack-online-:cityName(/)': 'shoeRackLD',
//            'crockery-unit-designs-:cityName(/)': 'crockeryUnitLD',
//            'tv-unit-cabinet-:cityName(/)': 'tvUnitLD',
//            'sideboard-:cityName(/)': 'sideboardLD',
//            'foyer-design-:cityName(/)': 'foyerLD',
//            'u-shaped-kitchen-design-:cityName(/)': 'ushapedK',
//            'straight-kitchen-design-:cityName(/)': 'straightK',
//            'parallel-kitchen-design-:cityName(/)': 'parallelK',
//            'wardrobe-designs-online-:cityName(/)': 'wardrobeB',
//            'study-table-designs-online-:cityName(/)': 'studyTableB',
//            'side-table-design-online-:cityName(/)': 'sideTableB',
//            'book-shelf-:cityName(/)': 'bookShelfB',
//            'nest-of-tables-online-:cityName(/)': 'nestofTableB',
//            'coffee-tables-online-:cityName(/)': 'cofeeTableB',
//            'buy-chest-of-drawers-online-:cityName(/)': 'chestofDrawerB',
//            'kitchen-cabinet-design-:cityName(/)': 'kitchens',
//            'living-and-dining-room-designs-:cityName(/)': 'livingDining',
//            'wardrobe-designs-online-:cityName(/)': 'bedroom',

            /* Enhancement of SEO city URL's */

            'l-shaped-kitchen-:cityName(/)': 'lshapedK',
            'shoe-rack-:cityName(/)': 'shoeRackLD',
            'crockery-unit-:cityName(/)': 'crockeryUnitLD',
            'tv-unit-:cityName(/)': 'tvUnitLD',
            'side-board-:cityName(/)': 'sideboardLD',
            'foyer-design-:cityName(/)': 'foyerLD',
            'u-shaped-kitchen-:cityName(/)': 'ushapedK',
            'straight-kitchen-:cityName(/)': 'straightK',
            'parallel-kitchen-:cityName(/)': 'parallelK',
            'wardrobes-online-:cityName(/)': 'wardrobeB',
            'study-table-:cityName(/)': 'studyTableB',
            'side-table-:cityName(/)': 'sideTableB',
            'book-shelf-:cityName(/)': 'bookShelfB',
            'nest-of-tables-:cityName(/)': 'nestofTableB',
            'coffee-tables-:cityName(/)': 'cofeeTableB',
            'buy-chest-of-drawers-:cityName(/)': 'chestofDrawerB',
            'modular-kitchen-cabinet-:cityName(/)': 'kitchens',
            'storage-solutions-:cityName(/)': 'livingDining',
            'console-table-:cityName(/)': 'consoletables',
            'dressers-:cityName(/)': 'dressers',
            //'wardrobes-online-:cityName(/)': 'bedroom',
            /* End of enhancment of SEO City URL */
            /* End Dynamic City Url SEO -- Mehbub */

/*
            'products/:categories(/:subcategories)(/)': prod,
*/
            'product_search-:searchTerm(/)': 'products-search',
           // 'product-:id(/)': 'product',
            'user_profile(/)': 'user_profile',
            'consult(/)': 'consult',
            'shortlist(/)': 'shortlist',
            'careers(/)': 'careers',
            'about-mygubbi(/)': 'about',
            'faq(/)': 'faq',
            'faq-shipping(/)': 'faq',
            'faq-returns(/)': 'faq',
            'faq-warranty(/)': 'faq',
            'terms(/)': 'terms',
            'privacy-policy(/)': 'privacypolicy',
            'mygubbi-difference(/)': 'mygubbidiff',
            'stories-:blogcategory(/)': 'stories',
            'story-:name(/)': 'story',
            'diy-:name(/)': 'diy',
            'thankyou-:page(/)':'thankyou',
            'lp(/)':'landingpage',
            'oglp(/)':'newlandingpage',
            /*'fblp(/)':'fblandingpage',*/
            'mangalore-lp(/)':'mangalorelandingpage',
            /*'email-lp(/)':'emaillandingpage',*/
            'nri-services-:cityName(/)' : 'nripage',
            'media(/)': 'mediapage',
            'kitchen-slp(/)': 'kitchenslppage',
            /*'shobha-lp(/)': 'shobhalandingpage',*/
            'pune-lp(/)': 'punelandingpage',
            'kitchen-lp(/)': 'kitchenlandingpage',
            /*'holiday-lp(/)': 'holidaylandingpage',*/
            'e-book(/)': 'ebook',
            //'completed-projects(/)': 'completedprojectspage',
            'recent-projects(/)': 'completedprojectspage',
            //'newproduct-details-:id(/)': 'newdetailspage',
            //'product-:id(/)': 'newdetailspage',
            ':id/p/:seoId(/)':'newdetailspage',
            'know-your-wardrobe(/)': 'knowyourwardrobe',
            'know-your-kitchen(/)': 'knowyourkitchen',
            'remarketing-lp(/)': 'remarketinglp',
            'about-us(/)': 'aboutus',
            'quality-check(/)': 'qualitycheck',
            'addons(/)': 'addons',
            'mygubbi-studio(/)': 'mygubbistudio',
            'platinum-home-interiors(/)': 'platinumhomes',
            'kitchen-accessories(/)': 'kitchenAccessories',
            'kitchen-appliance(/)': 'kitchenAppliance',
            'type-of-kitchen(/)': 'typeofkitchen',
            'ergonomics(/)': 'ergonomics',
            'kitchen-material(/)': 'kitchenMaterial',
            'payment(/)': 'payment',
            'my_account(/)': 'my_account',
            'my_nest(/)': 'my_nest',
            'my_settings(/)': 'my_settings',
            'online-payment(/)': 'online_payment',
            'paysuccess-:txnId(/)': 'online_payment_success',
            'payfailure-:txnId(/)': 'online_payment_failure',
            '*something': 'errorPage'
        },
        dashboard: function() {
            document.title = 'Home Decor, Modular Kitchen, Wardrobe Designs & Renovation Ideas | MyGubbi';
            document.querySelector('meta[name="description"]').setAttribute("content", "Buy Customized Home D?cor Interiors online for living room furniture, kitchen furniture, side tables and TV furniture.Get Home d?cor ideas to design your Home");
            document.querySelector('meta[name="keywords"]').setAttribute("content", "home interior décor, home interior design, interior home décor, home interiors, custom made furniture, custom furniture, online customised furniture, modern furniture design, furniture designs");
        },
        consult: function() {
            document.title = 'Get in touch with us for th best advice on home decor and remodelling';
            document.querySelector('meta[name="description"]')
            .setAttribute("content", "Consult our experts to gets tips and advice on the best decor theme and furniture units for your home. Get in touch with us right away.");
            document.querySelector('meta[name="keywords"]')
            .setAttribute("content", "MyGubbi address, MyGubbi contac us");
        },
        shortlist: function() {
            document.title = 'Shortlisted products for easy and fast access | MyGubbi';
        },
        stories: function() {
            document.title = 'Latest Tips from our Experts | MyGubbi';
        },
        thankyou: function() {
            document.title = 'Thank you for contacting MyGubbi';
        },
        careers: function() {
            document.title = 'Work with us to explore the world of home decor';
            document.querySelector('meta[name="description"]').setAttribute("content", "Exhibit your talent in mastering the business of interior designing by working with us");
            document.querySelector('meta[name="keywords"]').setAttribute("content", "mygubbi careers");
        },
        about: function() {
            document.title = 'Know about our products and services for online home decor ';
            document.querySelector('meta[name="description"]').setAttribute("content", "We are experts in offering modular furnishing and home decor solutions");
            document.querySelector('meta[name="keywords"]').setAttribute("content", "mygubbi about us");
        },
        faq: function() {
            document.title = 'Browse through our FAQ section to understand the process better';
            document.querySelector('meta[name="description"]').setAttribute("content", "Our FAQs guides you through our process and what you can expect of our services and products. Do check out the FAQ section for guidance on using our website and availing the services");
            document.querySelector('meta[name="keywords"]').setAttribute("content", "MyGubbi FAQ ,MyGubbi warranty, MyGubbi shipping, MyGubbi returns");
        },
        terms: function() {
            document.title = 'Our terms and conditions offer you the benefit of hassle free experience ';
            document.querySelector('meta[name="description"]').setAttribute("content", "Refer the terms and conditions on our website to know more about the reserved rights, online conduct, registration and cancellation/refund policies ");
            document.querySelector('meta[name="keywords"]').setAttribute("content", "mygubbi terms and conditions");
        },
        privacypolicy: function() {
            document.title = 'We maintain confidentiality of your personal information';
                        document.querySelector('meta[name="description"]').setAttribute("content", "Our privacy policy ensures that your log in details and personal information shared on the website for communication purposes maintains the privacy standards.  ");
                        document.querySelector('meta[name="keywords"]').setAttribute("content", "mygubbi privacy policy");
        },
        mygubbidiff: function() {
            document.title = 'MyGubbi difference | MyGubbi';
        },
        studyTableB: function () {
            document.title = 'Study Table Designs - Order Study Table Online from MyGubbi';
            document.querySelector('meta[name="description"]').setAttribute("content", "Designs of Study Table which complement Your Room and Interiors.Get the latest study table design designed to meet your needs.");
            document.querySelector('meta[name="keywords"]').setAttribute("content", "study table, study table designs, study table online ,buy study table online, order study table online, latest study table design");
        },
        tvUnitLD: function() {
            document.title = 'Buy entertainment units online and tv unit designs from MyGubbi';
            document.querySelector('meta[name="description"]').setAttribute("content", "Deck Your Home With the best of Décor with our range of TV Furniture and TV Cabinet Designs.Pick out luxury Home décor from MyGubbi");
            document.querySelector('meta[name="keywords"]').setAttribute("content", "tv unit, tv stand, tv cabinet, tv units, entertainment unit,entertainment unit designs");
        },
        shoeRackLD: function() {
            document.title = 'Buy Shoe Stands and Shoe Racks Online from MyGubbi';
            document.querySelector('meta[name="description"]').setAttribute("content", "Give a neater and classy look to your Home by getting Your Shoe Rack Furniture Custom made by our designers who look into the minute details for better living");
            document.querySelector('meta[name="keywords"]').setAttribute("content", "shoe rack, shoe rack online, shoe rack designs, shoe racks, shoe racks online");
        },
        crockeryUnitLD: function() {
            document.title = 'Crockery unit designs - Kitchen and Dining crockery Unit designs from MyGubbi';
            document.querySelector('meta[name="description"]').setAttribute("content", "Look for Crockery unit designs Online and get to select from a range of exclusive products from MyGubbi.");
            document.querySelector('meta[name="keywords"]').setAttribute("content", "crockery, crockery unit, crockery cabinet, crockery unit designs, crockery cabinet online,kitchen crockery cabinet designs, dining crockery unit designs, designs for crockery unit");
        },
        lshapedK: function() {
            document.title = 'L-Shaped Kitchen - L-Shaped Modular Kitchen Designs from MyGubbi';
            document.querySelector('meta[name="description"]').setAttribute("content", "L shaped Kitchen Designs for Your Dream Modular Kitchen.Get a more spacious and modern kitchen which match up to the latest Décor.");
            document.querySelector('meta[name="keywords"]').setAttribute("content", "l shaped kitchen design, l shaped kitchen, l shaped kitchen designs");
        },
        ushapedK: function () {
            document.title = 'U-Shaped Kitchen Design - U-Shaped Kitchen from MyGubbi';
            document.querySelector('meta[name="description"]').setAttribute("content", "U shaped Kitchen Designs for Your Dream Modular Kitchen.Get a more spacious and modern kitchen which match up to the latest Décor.");
            document.querySelector('meta[name="keywords"]').setAttribute("content", "u shaped kitchen, u shaped kitchen design,  u shaped kitchen designs");
        },
        straightK: function () {
            document.title = 'Straight Kitchen Design - Straight Modular Kitchen Designs from MyGubbi';
            document.querySelector('meta[name="description"]').setAttribute("content", "Straight Kitchen Designs for Your Dream Modular Kitchen.Get a more spacious and modern kitchen which match up to the latest Décor.");
            document.querySelector('meta[name="keywords"]').setAttribute("content", "straight kitchen design, straight modular kitchen designs, straight modular kitchen");
        },
        parallelK: function() {
            document.title = 'Parallel Kitchen Design - Parallel Kitchen Cabinets from MyGubbi';
            document.querySelector('meta[name="description"]').setAttribute("content", "Parallel Kitchen Designs for Your Dream Modular Kitchen.Get a more spacious and modern kitchen which match up to the latest Décor.");
            document.querySelector('meta[name="keywords"]').setAttribute("content", "parallel kitchen design, parallel kitchen, parallel kitchen designs,parallel modular kitchen designs");
        },
        wardrobeB: function() {
            document.title = 'Bedroom wardrobe Design -Buy Wardrobes Online from MyGubbi';
            document.querySelector('meta[name="description"]').setAttribute("content", "Check out Our Exclusive range of Wardrobe designs which add a classy touch to your Home décor.MyGubbi offers a hassle free installation process and speedy delivery");
            document.querySelector('meta[name="keywords"]').setAttribute("content", "wardrobe, wardrobe designs, wardrobe design,wardrobe online, buy wardrobe online");
        },
        sideTableB: function() {
            document.title = 'Side Table - Bedside tables Online from MyGubbi';
            document.querySelector('meta[name="description"]').setAttribute("content", "Bedside Table Designs that compliment Your Bedroom décor in ways like no other.Pick Out Luxury Home Décor from MyGubbi");
            document.querySelector('meta[name="keywords"]').setAttribute("content", "side table, side tables,study table designs, designs of study table, buy study table online, order study table online");
        },
        bookShelfB: function() {
            document.title = 'Book Rack - Book shelves and Book Organizers from MyGubbi';
            document.querySelector('meta[name="description"]').setAttribute("content", "Custom Made Bookshelves from MyGubbi add to your range of luxury décor and add a touch of elegance to your living abode");
            document.querySelector('meta[name="keywords"]').setAttribute("content", "book shelf, bookshelf design, book rack, bookshelf designs");
        },
        nestofTableB: function() {
            document.title = 'Nest of Tables online Bangalore - MyGubbi';
            document.querySelector('meta[name="description"]').setAttribute("content", "Buy Nest of Tables online In Bangalore with MyGubbi . Shop online for Nest of Tables with trendy designs and customised Nest of Tables.");
            document.querySelector('meta[name="keywords"]').setAttribute("content", "Nest of Tables Bangalore, buy Nest of Tables, Nest Of Tables online, buy Nest Of Tables online Bangalore");
        },
        cofeeTableB: function() {
            document.title = 'Coffee Tables online in India - MyGubbi';
            document.querySelector('meta[name="description"]').setAttribute("content", "Buy Coffee Tables online at low prices in India. Shop from a wide range of Coffee Tables and­­ customised designs at MyGubbi with an easy EMI");
            document.querySelector('meta[name="keywords"]').setAttribute("content", "Coffee Tables, buy Coffee Tables, Coffee Tables online, buy Coffee Tables online india");
        },
        chestofDrawerB: function() {
            document.title = 'Buy Chest of Drawers Online in Bangalore – MyGubbi';
            document.querySelector('meta[name="description"]').setAttribute("content", "Buy Chest of Drawers online for your home in Bangalore, Pune only at MyGubbi.com. Exclusive customised designs of Chest of Drawers now available at 0% EMI.");
            document.querySelector('meta[name="keywords"]').setAttribute("content", "Chest of Drawers, buy Chest of Drawers, buy Chest of Drawers online, buy Chest of Drawers Bangalore");
        },
        bedroom: function () {
            document.title = 'Bedroom Designs - Bedroom Furniture and interiors from MyGubbi';
            document.querySelector('meta[name="description"]').setAttribute("content", "MyGubbi offers an Exclusive collection of bedroom furniture to choose from.Get Your Bedroom interiors custom made to fit your Home and Décor.");
            document.querySelector('meta[name="keywords"]').setAttribute("content", "bedroom designs, bedroom furniture, bedroom interior, bedroom interior design");
        },
        kitchens: function() {
            document.title = 'Modular Kitchen - Range of modular Kitchen Designs from MyGubbi';
            document.querySelector('meta[name="description"]').setAttribute("content", "Check out our Range of Kitchen Cabinets and choose the kitchen cabinet design of Your choice.");
            document.querySelector('meta[name="keywords"]').setAttribute("content", "modular kitchen, modular kitchen design, kitchen design, kitchen cabinets, kitchen, kitchen interior, kitchen cabinet, kitchen furniture,kitchen cabinet designs, kitchen cabinets designs");
        },
        livingDining: function () {
            document.title = 'Living room designs and interiors from MyGubbi';
            document.querySelector('meta[name="description"]').setAttribute("content", "Buy Your Living Room furniture online from MyGubbi and jazz up your life with the latest Décor.Get Your Dining Room furniture custom made by picking out from a range of dining room designs");
            document.querySelector('meta[name="keywords"]').setAttribute("content", "living and dining room, living and dining room designs, living and dining room design ,living room furniture online,living room designs, dining room designs, dining room furniture,living room interior");
        }
    });
    var routeSubCategory = function(subCategory, category, cityName) {
               setTimeout($('.page').append("<img src='https://res.cloudinary.com/mygubbi/image/upload/v1481115313/home/new_design/spinner.gif' class='page-tran'>"), 0);
               require(['views/product/page'], function(ProductPage) {
                   var options = {
                       model: {
                           "selectedCategories": category,
                           "selectedSubCategories": subCategory,
                           "cityName": cityName

                       }
                   };
                   VM.create(VM.PRODUCT_LISTING, ProductPage, options).render();
               });
           };

             var routeCategory = function(category,cityName) {
                    setTimeout($('.page').append("<img src='https://res.cloudinary.com/mygubbi/image/upload/v1481115313/home/new_design/spinner.gif' class='page-tran'>"), 0);
                    require(['views/product/page'], function(ProductPage) {
                        var options = {
                            model: {
                                "selectedCategories": category,
                                "cityName": cityName
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

            router.on('route:shoeRackLD', function(cityName){
             if((cityName == null) || (cityName == "") ){
                                    cityName = "website";
                                }
                routeSubCategory('Shoe Rack', 'living & dining', cityName );
            });
            router.on('route:crockeryUnitLD', function(cityName){
             if((cityName == null) || (cityName == "") ){
                                    cityName = "website";
                                }
                routeSubCategory('Crockery Unit', 'living & dining' , cityName);
            });
            router.on('route:tvUnitLD', function(cityName){
             if((cityName == null) || (cityName == "") ){
                                    cityName = "website";
                                }
                routeSubCategory('Entertainment Unit', 'living & dining', cityName);
            });
            router.on('route:sideboardLD', function(cityName){
             if((cityName == null) || (cityName == "") ){
                                    cityName = "website";
                                }
                routeSubCategory('Sideboard', 'living & dining', cityName);
            });
            router.on('route:foyerLD', function(cityName){
             if((cityName == null) || (cityName == "") ){
                                    cityName = "website";
                                }
                routeSubCategory('Foyer Unit', 'living & dining', cityName);
            });
            router.on('route:lshapedK', function(cityName){
            if((cityName == null) || (cityName == "") ){
                        cityName = "website";
                    }
                routeSubCategory('L Shaped Kitchen', 'kitchen', cityName);
            });
            router.on('route:ushapedK', function(cityName){
             if((cityName == null) || (cityName == "") ){
                                    cityName = "website";
                                }
                routeSubCategory('U Shaped Kitchen', 'kitchen', cityName);
            });
            router.on('route:straightK', function(cityName){
             if((cityName == null) || (cityName == "") ){
                                    cityName = "website";
                                }
                routeSubCategory('Straight Kitchen', 'kitchen', cityName);
            });
            router.on('route:parallelK', function(cityName){
             if((cityName == null) || (cityName == "") ){
                                    cityName = "website";
                                }
                routeSubCategory('Parallel Kitchen', 'kitchen', cityName);
            });
            router.on('route:wardrobeB', function(cityName){
             if((cityName == null) || (cityName == "") ){
                                    cityName = "website";
                                }
                routeSubCategory('Wardrobe', 'bedroom', cityName);
            });
            router.on('route:wardrobeSlide', function(cityName){
             if((cityName == null) || (cityName == "") ){
                                    cityName = "website";
                                }
                routeSubCategory('Sliding Wardrobe', 'bedroom', cityName);
            });
            router.on('route:wardrobeSwing', function(cityName){
             if((cityName == null) || (cityName == "") ){
                                    cityName = "website";
                                }
                routeSubCategory('Swing Wardrobe', 'bedroom', cityName);
            });
            router.on('route:studyTableB', function(cityName){
             if((cityName == null) || (cityName == "") ){
                                    cityName = "website";
                                }
                routeSubCategory('Study Table', 'living & dining', cityName);
            });
            router.on('route:sideTableB', function(cityName){
             if((cityName == null) || (cityName == "") ){
                                    cityName = "website";
                                }
                routeSubCategory('Side Table', 'living & dining', cityName);
            });
            router.on('route:bookShelfB', function(cityName){
             if((cityName == null) || (cityName == "") ){
                                    cityName = "website";
                                }
                routeSubCategory('Book Rack', 'living & dining', cityName);
            });
            router.on('route:nestofTableB', function(cityName){
             if((cityName == null) || (cityName == "") ){
                                    cityName = "website";
                                }
                routeSubCategory('Nest of Table', 'living & dining', cityName);
            });
            router.on('route:cofeeTableB', function(cityName){
             if((cityName == null) || (cityName == "") ){
                                    cityName = "website";
                                }
                routeSubCategory('Coffee Tables', 'living & dining', cityName);
            });
            router.on('route:chestofDrawerB', function(cityName){
             if((cityName == null) || (cityName == "") ){
                                    cityName = "website";
                                }
                routeSubCategory('Chest of Drawer', 'living & dining', cityName);
            });
            router.on('route:consoletables', function(cityName){
             if((cityName == null) || (cityName == "") ){
                                    cityName = "website";
                                }
                routeSubCategory('Console Table', 'living & dining', cityName);
            });
            router.on('route:dressers', function(cityName){
             if((cityName == null) || (cityName == "") ){
                                    cityName = "website";
                                }
                routeSubCategory('Dresser', 'living & dining', cityName);
            });
            router.on('route:kitchens', function(cityName){
             if((cityName == null) || (cityName == "") ){
                                    cityName = "website";
                                }
                routeCategory('kitchen',cityName);
            });
            router.on('route:livingDining', function(cityName){
             if((cityName == null) || (cityName == "") ){
                                    cityName = "website";
                                }
                routeCategory('living & dining', cityName);
            });
            router.on('route:bedroom', function(cityName){
             if((cityName == null) || (cityName == "") ){
                                    cityName = "website";
                                }
                routeCategory('bedroom', cityName);
            });

            /*router.on('route:dashboard', function(actions) {
                require(['views/dashboard/page'], function(DashboardPage) {
                    VM.create(VM.DASHBOARD, DashboardPage).render();
                });
            });*/
            router.on('route:dashboard', function(actions) {
              setTimeout($('.page').append("<img src='https://res.cloudinary.com/mygubbi/image/upload/v1481115313/home/new_design/spinner.gif' class='page-tran'>"), 0);
                require(['views/dashboard/new-page'], function(DashboardPage) {
                    VM.create(VM.DASHBOARD, DashboardPage).render();
                });
            });
            router.on('route:products', function(categories, subcategories, searchTerm, sortBy, sortDir, layout) {
                setTimeout($('.page').append("<img src='https://res.cloudinary.com/mygubbi/image/upload/v1481115313/home/new_design/spinner.gif' class='page-tran'>"), 0);
                require(['views/product/page'], function(ProductPage) {
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
                setTimeout($('.page').append("<img src='https://res.cloudinary.com/mygubbi/image/upload/v1481115313/home/new_design/spinner.gif' class='page-tran'>"), 0);
                require(['views/product/page'], function(ProductPage) {
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
                setTimeout($('.page').append("<img src='https://res.cloudinary.com/mygubbi/image/upload/v1481115313/home/new_design/spinner.gif' class='page-tran'>"), 0);
                require(['views/product/details'], function(ProductDetailPage) {
                    var options = {
                        model: {
                            "id": productId
                        }
                    };
                    VM.create(VM.PRODUCT_DETAILS, ProductDetailPage, options).render();
                });
            });
            router.on('route:user_profile', function(actions) {
                setTimeout($('.page').append("<img src='https://res.cloudinary.com/mygubbi/image/upload/v1481115313/home/new_design/spinner.gif' class='page-tran'>"), 0);
                require(['views/user_profile/user_profile'], function(UserProfilePage) {
                    VM.create(VM.USER_PROFILE, UserProfilePage).render();
                });
            });
            router.on('route:my_account', function(actions) {
                setTimeout($('.page').append("<img src='https://res.cloudinary.com/mygubbi/image/upload/v1481115313/home/new_design/spinner.gif' class='page-tran'>"), 0);
                require(['/js/views/my_account/my_account.js'], function(MyAccountPage) {
                    VM.create(VM.MYACCOUNTPAGE, MyAccountPage).render();
                });
            });
            router.on('route:my_nest', function(actions) {
                setTimeout($('.page').append("<img src='https://res.cloudinary.com/mygubbi/image/upload/v1481115313/home/new_design/spinner.gif' class='page-tran'>"), 0);
                require(['/js/views/my_account/my_nest.js'], function(MyNestPage) {
                    VM.create(VM.MYNESTPAGE, MyNestPage).render();
                });
            });
            router.on('route:my_settings', function(actions) {
                setTimeout($('.page').append("<img src='https://res.cloudinary.com/mygubbi/image/upload/v1481115313/home/new_design/spinner.gif' class='page-tran'>"), 0);
                require(['/js/views/my_account/my_settings.js'], function(MySettingsPage) {
                    VM.create(VM.MYSETTINGSPAGE, MySettingsPage).render();
                });
            });
            router.on('route:online_payment', function(actions) {
                setTimeout($('.page').append("<img src='https://res.cloudinary.com/mygubbi/image/upload/v1481115313/home/new_design/spinner.gif' class='page-tran'>"), 0);
                require(['/js/views/my_account/online_payment.js'], function(OnlinePaymentPage) {
                    VM.create(VM.ONLINEPAYMENTPAGE, OnlinePaymentPage).render();
                });
            });
            router.on('route:online_payment_success', function(txnId) {
                setTimeout($('.page').append("<img src='https://res.cloudinary.com/mygubbi/image/upload/v1481115313/home/new_design/spinner.gif' class='page-tran'>"), 0);
                require(['/js/views/my_account/online_payment_success.js'], function(OnlinePaymentSuccessPage) {
                    var options = {
                        model: {
                            "txnId": txnId
                        }
                    };
                    VM.create(VM.ONLINEPAYMENTSUCCESSPAGE, OnlinePaymentSuccessPage, options).render();
                });
            });
            router.on('route:online_payment_failure', function(txnId) {
                setTimeout($('.page').append("<img src='https://res.cloudinary.com/mygubbi/image/upload/v1481115313/home/new_design/spinner.gif' class='page-tran'>"), 0);
                require(['/js/views/my_account/online_payment_failure.js'], function(OnlinePaymentFailurePage) {
                    var options = {
                        model: {
                            "txnId": txnId
                        }
                    };
                    VM.create(VM.ONLINEPAYMENTFAILUREPAGE, OnlinePaymentFailurePage, options).render();
                });
            });
            router.on('route:consult', function(actions) {
                setTimeout($('.page').append("<img src='https://res.cloudinary.com/mygubbi/image/upload/v1481115313/home/new_design/spinner.gif' class='page-tran'>"), 0);
                require(['views/consult/consult'], function(ConsultPage) {
                    VM.create(VM.CONSULT, ConsultPage).render();
                });
            });
            router.on('route:shortlist', function(actions) {
                setTimeout($('.page').append("<img src='https://res.cloudinary.com/mygubbi/image/upload/v1481115313/home/new_design/spinner.gif' class='page-tran'>"), 0);
                require(['views/shortlist/shortlist'], function(ShortlistPage) {
                    VM.create(VM.SHORTLIST, ShortlistPage).render();
                });
            });
            router.on('route:careers', function(actions) {
                setTimeout($('.page').append("<img src='https://res.cloudinary.com/mygubbi/image/upload/v1481115313/home/new_design/spinner.gif' class='page-tran'>"), 0);
                require(['views/footer/careers'], function(CareerPage) {
                    VM.create(VM.CAREERS, CareerPage).render();
                });
            });
            router.on('route:about', function(actions) {
                setTimeout($('.page').append("<img src='https://res.cloudinary.com/mygubbi/image/upload/v1481115313/home/new_design/spinner.gif' class='page-tran'>"), 0);
                require(['views/footer/about'], function(AboutPage) {
                    VM.create(VM.ABOUT, AboutPage).render();
                });
            });

            router.on('route:qualitycheck', function(actions) {
                setTimeout($('.page').append("<img src='https://res.cloudinary.com/mygubbi/image/upload/v1481115313/home/new_design/spinner.gif' class='page-tran'>"), 0);
                require(['views/quality/quality-check'], function(QualityCheckPage) {
                    VM.create(VM.QUALITYCHECK, QualityCheckPage).render();
                });
            });

            router.on('route:addons', function(actions) {
                setTimeout($('.page').append("<img src='https://res.cloudinary.com/mygubbi/image/upload/v1481115313/home/new_design/spinner.gif' class='page-tran'>"), 0);
                require(['views/addons/addons'], function(addonsPage) {
                    VM.create(VM.ADDONS, addonsPage).render();
                });
            });
            router.on('route:mygubbistudio', function(actions) {
                document.title = 'Experience studio to give you the feel of our collection of elegant furniture units';
                document.querySelector('meta[name="description"]')
                .setAttribute("content", "Explore our collection of sophisticated furniture units at our experience studios. You can also consult our designers to get the best advice on the look you need for your home. ");
                document.querySelector('meta[name="keywords"]')
                .setAttribute("content", "mygubbi studio, mygubbi showroom , mygubbi experience center");
                setTimeout($('.page').append("<img src='https://res.cloudinary.com/mygubbi/image/upload/v1481115313/home/new_design/spinner.gif' class='page-tran'>"), 0);
                 require(['views/mygubbiStudio/mygubbi-studio'], function(MygubbiStudioPage) {
                    VM.create(VM.MYGUBBISTUDIO, MygubbiStudioPage).render();
                });
            });
            router.on('route:platinumhomes', function(actions) {
                document.title = 'MyGubbi: Home Interiors & Decor Online, Platinum Home Interior designs';
                document.querySelector('meta[name="description"]')
                .setAttribute("content", "Home interior design made easy with Platinum Home Interiors. Get excellent design ideas customised and implemented to suit your home needs. Beautiful Homes Interiors made easy.");
                document.querySelector('meta[name="keywords"]')
                .setAttribute("content", "Platinum home, Platinum home interior services, Platinum home interior, Platinum interior services, modular home interiors ");
                setTimeout($('.page').append("<img src='https://res.cloudinary.com/mygubbi/image/upload/v1481115313/home/new_design/spinner.gif' class='page-tran'>"), 0);
                require(['views/platinum_homes/platinum-homes'], function(PlatinumHomesPage) {
                VM.create(VM.PLATINUMHOMES, PlatinumHomesPage).render();
                 });
            });
            router.on('route:faq', function(actions) {
                setTimeout($('.page').append("<img src='https://res.cloudinary.com/mygubbi/image/upload/v1481115313/home/new_design/spinner.gif' class='page-tran'>"), 0);
                require(['views/footer/faq'], function(FaqPage) {
                    VM.create(VM.FAQ, FaqPage).render();
                });
            });
            router.on('route:privacypolicy', function(actions) {
                setTimeout($('.page').append("<img src='https://res.cloudinary.com/mygubbi/image/upload/v1481115313/home/new_design/spinner.gif' class='page-tran'>"), 0);
                require(['views/footer/privacypolicy'], function(PrivacyPolicyPage) {
                    VM.create(VM.PRIVACY_POLICY, PrivacyPolicyPage).render();
                });
            });
            router.on('route:terms', function(actions) {
                setTimeout($('.page').append("<img src='https://res.cloudinary.com/mygubbi/image/upload/v1481115313/home/new_design/spinner.gif' class='page-tran'>"), 0);
                require(['views/footer/terms'], function(TermsPage) {
                    VM.create(VM.TERMS, TermsPage).render();
                });
            });
            router.on('route:mygubbidiff', function(actions) {
                setTimeout($('.page').append("<img src='https://res.cloudinary.com/mygubbi/image/upload/v1481115313/home/new_design/spinner.gif' class='page-tran'>"), 0);
                require(['views/footer/mygubbidiff'], function(MyGubbiDiffPage) {
                    VM.create(VM.MGDIFF, MyGubbiDiffPage).render();
                });
            });
            router.on('route:stories', function(blogcategory) {

                setTimeout($('.page').append("<img src='https://res.cloudinary.com/mygubbi/image/upload/v1481115313/home/new_design/spinner.gif' class='page-tran'>"), 0);
                require(['/js/views/story/stories.js'], function(StoriesPage) {

                 var options = {
                    model: {
                        "blogcategory": blogcategory
                    }
                };
                VM.create(VM.STORIES, StoriesPage, options).render();
            });
        });
        router.on('route:story', function(name) {
            setTimeout($('.page').append("<img src='https://res.cloudinary.com/mygubbi/image/upload/v1481115313/home/new_design/spinner.gif' class='page-tran'>"), 0);
            require(['views/story/full_story'], function(FullStoryPage) {

                var options = {
                    model: {
                        "name": name
                    }
                };
                VM.create(VM.STORY, FullStoryPage, options).render();
            });
        });
        router.on('route:diy', function(name) {
            setTimeout($('.page').append("<img src='https://res.cloudinary.com/mygubbi/image/upload/v1481115313/home/new_design/spinner.gif' class='page-tran'>"), 0);
            require(['views/story/full_diy'], function(FullDiyPage) {

                var options = {
                    model: {
                        "name": name
                    }
                };
                VM.create(VM.DIY, FullDiyPage, options).render();
            });
        });
        router.on('route:thankyou', function(actions) {
            setTimeout($('.page').append("<img src='https://res.cloudinary.com/mygubbi/image/upload/v1481115313/home/new_design/spinner.gif' class='page-tran'>"), 0);
            require(['views/thankyou/thankyou'], function(ThankYouPage) {
                VM.create(VM.THANKYOU, ThankYouPage).render();
            });
        });
        router.on('route:landingpage', function(actions) {
            setTimeout($('.page').append("<img src='https://res.cloudinary.com/mygubbi/image/upload/v1481115313/home/new_design/spinner.gif' class='page-tran'>"), 0);
            require(['views/landing_pages/landing_page'], function(LandingPage) {
                VM.create(VM.LANDINGPAGE, LandingPage).render();
            });
        });
        router.on('route:newlandingpage', function(actions) {
            setTimeout($('.page').append("<img src='https://res.cloudinary.com/mygubbi/image/upload/v1481115313/home/new_design/spinner.gif' class='page-tran'>"), 0);
            require(['views/landing_pages/newlanding_page'], function(NewLandingPage) {
                VM.create(VM.NEWLANDINGPAGE, NewLandingPage).render();
            });
        });
        router.on('route:fblandingpage', function(actions) {
            setTimeout($('.page').append("<img src='https://res.cloudinary.com/mygubbi/image/upload/v1481115313/home/new_design/spinner.gif' class='page-tran'>"), 0);
            require(['views/landing_pages/fblanding_page'], function(FbLandingPage) {
                VM.create(VM.FBLANDINGPAGE, FbLandingPage).render();
            });
        });
        router.on('route:mangalorelandingpage', function(actions) {
            setTimeout($('.page').append("<img src='https://res.cloudinary.com/mygubbi/image/upload/v1481115313/home/new_design/spinner.gif' class='page-tran'>"), 0);
            require(['views/landing_pages/mangalorelanding_page'], function(MangaloreLandingPage) {
                VM.create(VM.MANGALORELANDINGPAGE, MangaloreLandingPage).render();
            });
        });
        router.on('route:emaillandingpage', function(actions) {
            setTimeout($('.page').append("<img src='https://res.cloudinary.com/mygubbi/image/upload/v1481115313/home/new_design/spinner.gif' class='page-tran'>"), 0);
            require(['views/landing_pages/emaillanding_page'], function(EmailLandingPage) {
                VM.create(VM.EMAILLANDINGPAGE, EmailLandingPage).render();
            });
        });
        router.on('route:shobhalandingpage', function(actions) {
            setTimeout($('.page').append("<img src='https://res.cloudinary.com/mygubbi/image/upload/v1481115313/home/new_design/spinner.gif' class='page-tran'>"), 0);
            require(['views/landing_pages/shobhalanding_page'], function(ShobhaLandingPage) {
                VM.create(VM.SHOBHALANDINGPAGE, ShobhaLandingPage).render();
            });
        });
        router.on('route:punelandingpage', function(actions) {
            setTimeout($('.page').append("<img src='https://res.cloudinary.com/mygubbi/image/upload/v1481115313/home/new_design/spinner.gif' class='page-tran'>"), 0);
            require(['views/landing_pages/punelanding_page'], function(PuneLandingPage) {
                VM.create(VM.PUNELANDINGPAGE, PuneLandingPage).render();
            });
        });
        router.on('route:kitchenlandingpage', function(actions) {
            setTimeout($('.page').append("<img src='https://res.cloudinary.com/mygubbi/image/upload/v1481115313/home/new_design/spinner.gif' class='page-tran'>"), 0);
            require(['views/landing_pages/kitchenlanding_page'], function(KitchenLandingPage) {
                VM.create(VM.KITCHENLANDINGPAGE, KitchenLandingPage).render();
            });
        });
        router.on('route:ebook', function(actions) {
            setTimeout($('.page').append("<img src='https://res.cloudinary.com/mygubbi/image/upload/v1481115313/home/new_design/spinner.gif' class='page-tran'>"), 0);
            require(['views/ebook/ebook'], function(EbookPage) {
                VM.create(VM.EBOOKPAGE, EbookPage).render();
            });
        });
        router.on('route:errorPage', function(actions) {
            setTimeout($('.page').append("<img src='https://res.cloudinary.com/mygubbi/image/upload/v1481115313/home/new_design/spinner.gif' class='page-tran'>"), 0);
            require(['views/errorPage/errorPage'], function(ErrorPage) {
                VM.create(VM.ERRORPAGE, ErrorPage).render();
            });
        });
        router.on('route:holidaylandingpage', function(actions) {
            setTimeout($('.page').append("<img src='https://res.cloudinary.com/mygubbi/image/upload/v1481115313/home/new_design/spinner.gif' class='page-tran'>"), 0);
            require(['views/landing_pages/holidaylanding_page'], function(HolidayLandingPage) {
                VM.create(VM.HOLIDAYLANDINGPAGE, HolidayLandingPage).render();
            });
        });
        router.on('route:completedprojectspage', function(actions) {
            document.title = 'Completed Projects with customised Home Interiors and design from MyGubbi';
            document.querySelector('meta[name="description"]')
            .setAttribute("content", "Browse through the completed projects customised and designed by home interior designers from MyGubbi. Complete home interiors solution providers.");
            document.querySelector('meta[name="keywords"]')
            .setAttribute("content", "mygubbi completed projects");
            setTimeout($('.page').append("<img src='https://res.cloudinary.com/mygubbi/image/upload/v1481115313/home/new_design/spinner.gif' class='page-tran'>"), 0);
            require(['views/landing_pages/completedprojects_page'], function(CompletedProjectsPage) {
                VM.create(VM.COMPLETEDPROJECTSPAGE, CompletedProjectsPage).render();
            });
        });
        router.on('route:knowyourwardrobe', function(actions) {
            document.title = 'Know Your Wardrobe | MyGubbi';
            setTimeout($('.page').append("<img src='https://res.cloudinary.com/mygubbi/image/upload/v1481115313/home/new_design/spinner.gif' class='page-tran'>"), 0);
            require(['views/landing_pages/knowyourwardrobe_page'], function(KnowYourWardrobe) {
                VM.create(VM.KNOWYOURWARDROBEPAGE, KnowYourWardrobe).render();
            });
        });
        router.on('route:knowyourkitchen', function(actions) {
            document.title = 'Know Your Kitchen | MyGubbi';
            setTimeout($('.page').append("<img src='https://res.cloudinary.com/mygubbi/image/upload/v1481115313/home/new_design/spinner.gif' class='page-tran'>"), 0);
            require(['views/landing_pages/knowyourkitchen_page'], function(KnowYourKitchen) {
                VM.create(VM.KNOWYOURKITCHENPAGE, KnowYourKitchen).render();
            });
        });
        router.on('route:kitchenAccessories', function(actions) {
            document.title = 'Kitchen Accessories | MyGubbi';
            setTimeout($('.page').append("<img src='https://res.cloudinary.com/mygubbi/image/upload/v1481115313/home/new_design/spinner.gif' class='page-tran'>"), 0);
            require(['views/kitchenAccessories/kitchenAccessories'], function(kitchenAccessories) {
                VM.create(VM.KITCHENACCESSORIESPAGE, kitchenAccessories).render();
            });
        });
        router.on('route:kitchenAppliance', function(actions) {
            document.title = 'Kitchen Appliance | MyGubbi';
            setTimeout($('.page').append("<img src='https://res.cloudinary.com/mygubbi/image/upload/v1481115313/home/new_design/spinner.gif' class='page-tran'>"), 0);
            require(['views/kitchenAppliance/kitchen_appliance'], function(kitchenAppliance) {
                VM.create(VM.KITCHENAPPLIANCEPAGE, kitchenAppliance).render();
            });
        });

        router.on('route:kitchenMaterial', function(actions) {
            document.title = 'Kitchen Material | MyGubbi';
            setTimeout($('.page').append("<img src='https://res.cloudinary.com/mygubbi/image/upload/v1481115313/home/new_design/spinner.gif' class='page-tran'>"), 0);
            require(['views/kitchenMaterial/kitchen-material'], function(kitchenMaterial) {
                VM.create(VM.KITCHENMATERIALPAGE, kitchenMaterial).render();
            });
        });

        router.on('route:payment', function(actions) {
            document.title = 'Our payment terms make your online interior design easy ';
                        document.querySelector('meta[name="description"]').setAttribute("content", "Check our payment terms to know about the available payment options");
                        document.querySelector('meta[name="keywords"]').setAttribute("content", "mygubbi payment");
            setTimeout($('.page').append("<img src='https://res.cloudinary.com/mygubbi/image/upload/v1481115313/home/new_design/spinner.gif' class='page-tran'>"), 0);
            require(['views/payment/payment'], function(payment) {
                VM.create(VM.PAYMENTPAGE, payment).render();
            });
        });

        router.on('route:typeofkitchen', function(actions) {
            document.title = 'Type Of Kitchen | MyGubbi';
            setTimeout($('.page').append("<img src='https://res.cloudinary.com/mygubbi/image/upload/v1481115313/home/new_design/spinner.gif' class='page-tran'>"), 0);
            require(['views/kitchentypes/typeofkitchen'], function(typeofkitchen) {
                VM.create(VM.TYPEOFKITCHENPAGE, typeofkitchen).render();
            });
        });
        router.on('route:ergonomics', function(actions) {
            document.title = 'Ergonomics | MyGubbi';
            setTimeout($('.page').append("<img src='https://res.cloudinary.com/mygubbi/image/upload/v1481115313/home/new_design/spinner.gif' class='page-tran'>"), 0);
            require(['views/ergonomics/ergonomics'], function(ergonomics) {
                VM.create(VM.ERGONOMICSPAGE, ergonomics).render();
            });
        });
        router.on('route:remarketinglp', function(actions) {
            setTimeout($('.page').append("<img src='https://res.cloudinary.com/mygubbi/image/upload/v1481115313/home/new_design/spinner.gif' class='page-tran'>"), 0);
            require(['views/landing_pages/remarketinglp_page'], function(RemarketingLp) {
                VM.create(VM.REMARKETINGLPSPAGE, RemarketingLp).render();
            });
        });
        router.on('route:mediapage', function(actions) {
            setTimeout($('.page').append("<img src='https://res.cloudinary.com/mygubbi/image/upload/v1481115313/home/new_design/spinner.gif' class='page-tran'>"), 0);
            require(['views/media_pages/media_page'], function(MediaPage) {
                VM.create(VM.MEDIAPAGE, MediaPage).render();
            });
        });
        router.on('route:kitchenslppage', function(actions) {
            setTimeout($('.page').append("<img src='https://res.cloudinary.com/mygubbi/image/upload/v1481115313/home/new_design/spinner.gif' class='page-tran'>"), 0);
            require(['views/seo_page/kitchen-slp'], function(KitchenSlpPage) {
                VM.create(VM.KITCHENSLPPAGE, KitchenSlpPage).render();
            });
        });
        router.on('route:newdetailspage', function(productId, seoId ) {
           setTimeout($('.page').append("<img src='https://res.cloudinary.com/mygubbi/image/upload/v1481115313/home/new_design/spinner.gif' class='page-tran'>"), 0);
           require(['../../js/views/product/new-details'], function(NewProductDetailsPage) {
                              var options = {
                   model: {
                       "id": productId
                   }
               };                            VM.create(VM.NEWPRODUCT_DETAILSPAGE, NewProductDetailsPage, options).render();
           });
       });
       router.on('route:seolp', function(pageurl) {

                setTimeout($('.page').append("<img src='https://res.cloudinary.com/mygubbi/image/upload/v1481115313/home/new_design/spinner.gif' class='page-tran'>"), 0);
                require(['views/seo_page/landing-page'], function(LandingPage) {

                var options = {
                model: {
                   "spageurl": pageurl
                }
                };
                VM.create(VM.LANDINGPAGE, LandingPage, options).render();
                });
                            });
        router.on('route:nripage', function(cityName) {
            document.title = 'Premium homes for premium living. Get end to end service for your dream home with us ';
            document.querySelector('meta[name="description"]')
            .setAttribute("content", "Our NRI service is a for the premium home segment focusing on the end to end solution for home decor. Our service follows the international standards to ensure the best in class quality.");
            document.querySelector('meta[name="keywords"]')
            .setAttribute("content", "MyGubbi NRI Services");
            setTimeout($('.page').append("<img src='https://res.cloudinary.com/mygubbi/image/upload/v1481115313/home/new_design/spinner.gif' class='page-tran'>"), 0);
            require(['views/nri_pages/nri_page'], function(NriPage) {
                var options = {
                    model: {
                        "cityName": cityName
                    }
                };
                VM.create(VM.NRIPAGE, NriPage, options).render();
            });
        });
        router.on('route:aboutus', function(actions) {
                    setTimeout($('.page').append("<img src='https://res.cloudinary.com/mygubbi/image/upload/v1481115313/home/new_design/spinner.gif' class='page-tran'>"), 0);
                    require(['views/footer/aboutus'], function(AboutUsPage) {
                        VM.create(VM.ABOUTUS, AboutUsPage).render();
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