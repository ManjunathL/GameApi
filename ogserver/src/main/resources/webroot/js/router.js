define([
    'jquery',
    'underscore',
    'backbone',
    'bootstrap',
    'views/view_manager'
], function($, _, Backbone, Bootstrap, VM) {
    var AppRouter = Backbone.Router.extend({
        routes: {
            '': 'dashboard',
            'products/:categories/:subcategories': 'products',
            'product_search/:searchTerm': 'products-search',
            'product/:id': 'product',
            'user_profile': 'user_profile',
            'consult': 'consult',
            'shortlist': 'shortlist'
        }

    });

    var initialize = function(options) {
        var appView = options.appView;
        var router = new AppRouter(options);
        router.on('route:dashboard', function(actions) {
            require(['views/dashboard/page'], function(DashboardPage) {
                VM.create("dashboard", DashboardPage).render();
            });
        });
        router.on('route:products', function(categories, subcategories, searchTerm, sortBy, sortDir, layout) {
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
                VM.create("productListing", ProductPage, options).render();
            });
        });
        router.on('route:products-search', function(searchTerm) {
            require(['views/product/page'], function(ProductPage) {
                var options = {
                    model: {
                        "searchTerm": searchTerm
                    }
                };
                VM.create("productListing", ProductPage, options).render();
            });
        });
        router.on('route:product', function(productId) {
            require(['views/product/details'], function(ProductDetailPage) {
                var options = {
                    model: {
                        "id": productId
                    }
                };
                VM.create("productDetails", ProductDetailPage, options).render();
            });
        });
        router.on('route:user_profile', function(actions) {
            require(['views/user_profile/user_profile'], function(UserProfilePage) {
                VM.create("userProfile", UserProfilePage).render();
            });
        });
        router.on('route:consult', function(actions) {
            require(['views/consult/consult'], function(ConsultPage) {
                VM.create("consult", ConsultPage).render();
            });
        });
        router.on('route:shortlist', function(actions) {
            require(['views/shortlist/shortlist'], function(ShortlistPage) {
                VM.create("shortlist", ShortlistPage).render();
            });
        });
        Backbone.history.start();
    };
    return {
        initialize: initialize
    };
});