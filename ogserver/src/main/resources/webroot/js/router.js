define([
  'jquery',
  'underscore',
  'backbone',
  'bootstrap'
], function ($, _, Backbone, Bootstrap) {
  var AppRouter = Backbone.Router.extend({
    routes: {
        '': 'dashboard',
        'products/:categories/:subcategories': 'products',
        'product/:id': 'product',
        'user_profile': 'user_profile',
        'consult': 'consult',
        'stories': 'stories',
        'story/:id': 'story'
    }

  });

  var initialize = function(options){
    var appView = options.appView;
    var router = new AppRouter(options);
    router.on('route:dashboard', function (actions) {
      require(['views/dashboard/page'], function (DashboardPage) {
        new DashboardPage().render();
      });
    });
    router.on('route:products', function (categories, subcategories, searchTerm, sortBy, sortDir, layout) {
      require(['views/product/page'], function (ProductPage) {
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
        new ProductPage(options).render();
      });
    });
    router.on('route:product', function (productId) {
      require(['views/product/details'], function (ProductDetailPage) {

        var options = {
            model: {
                "id": productId
            }
        };
        new ProductDetailPage(options).render();
      });
    });
    router.on('route:user_profile', function (actions) {
      require(['views/user_profile/user_profile'], function (UserProfilePage) {
          new UserProfilePage().render();
      });
    });
    router.on('route:consult', function (actions) {
      require(['views/consult/consult'], function (ConsultPage) {
          new ConsultPage().render();
      });
    });
      router.on('route:stories', function (actions) {
      require(['views/story/stories'], function (StoriesPage) {
          new StoriesPage().render();
      });
    });
      router.on('route:story', function (storyId) {
          require(['views/story/full_story'], function (FullStoryPage) {

              var options = {
                  model: {
                      "id": storyId
                  }
              };
              new FullStoryPage(options).render();
          });
      });
    Backbone.history.start();
  };
  return {
      initialize: initialize
  };
});
