// Filename: router.js
define([
  'jquery',
  'underscore',
  'backbone',
  'bootstrap',
  'vm'
], function ($, _, Backbone, Bootstrap, Vm) {
  var AppRouter = Backbone.Router.extend({
    routes: {
      // Default - catch all
      '*actions': 'defaultAction'
    }
  });

  var initialize = function(options){
    var appView = options.appView;
    var router = new AppRouter(options);
    router.on('route:defaultAction', function (actions) {
      require(['views/dashboard/page'], function (DashboardPage) {
        var dashboardPage = Vm.create(appView, 'DashboardPage', DashboardPage);
        dashboardPage.render();
      });
    });
    Backbone.history.start();
  };
  return {
    initialize: initialize
  };
});
