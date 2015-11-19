define([
  'jquery',
  'underscore',
  'backbone',
  'bootstrap'
], function ($, _, Backbone, Bootstrap) {
  var AppRouter = Backbone.Router.extend({
    routes: {
      '': 'dashboard',
      'products/:categories/:subcategories(/q:searchTerm)(/s:sortBy)(/d:sortDir)(/l:layout)': 'products',
      'products/details/:productnm': 'product_details'
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
      console.log('smruti111111111111111111111');
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
    router.on('route:product_details', function (productnm) {
      require(['views/product/details'], function (ProductPage) {
          console.log('smruti');

        var options = {
            model: {
                "selectedProduct": productnm
            }
        };
        new ProductPage(options).render();
      });
    });
    Backbone.history.start();
  };
  return {
      initialize: initialize
  };
});
