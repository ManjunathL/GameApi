define([
  'jquery',
  'underscore',
  'backbone',
  'bootstrap',
  'text!templates/dashboard/page.html'
], function($, _, Backbone, Bootstrap, dashboardPageTemplate){
  var DashboardPage = Backbone.View.extend({
    el: '.page',
    render: function () {
        $(this.el).html(dashboardPageTemplate);
    }
  });
  return DashboardPage;
});
