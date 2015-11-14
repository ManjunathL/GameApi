define([
  'jquery',
  'underscore',
  'backbone',
  'bootstrap',
  'vm'
], function ($, _, Backbone, Bootstrap, Vm) {
  var AppRouter = Backbone.Router.extend({
    routes: {
      '': 'dashboard',
      'products/:category': 'products',
      'products/:category/:subcategory': 'products'
    }
  });

  var initialize = function(options){
    var appView = options.appView;
    var router = new AppRouter(options);
    router.on('route:dashboard', function (actions) {
      require(['views/dashboard/page'], function (DashboardPage) {
        var dashboardPage = Vm.create(appView, 'DashboardPage', DashboardPage);
        dashboardPage.render();
      });
    });
    router.on('route:products', function (category, subcategory) {
      require(['views/product/page'], function (ProductPage) {
        var productPage = Vm.create(appView, 'ProductPage', ProductPage);
        productPage.render(category, subcategory);
      });
    });
    Backbone.history.start();
  };
  return {
    initialize: initialize
  };
});
