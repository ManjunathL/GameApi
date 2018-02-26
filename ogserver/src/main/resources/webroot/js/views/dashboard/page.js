define([
  'jquery',
  'lodash',
  'backbone',
  'bootstrap',
  'text!templates/dashboard/page.html',
  'collections/designs'
], function($, _, Backbone, Bootstrap, dashboardPageTemplate, Designs){
  var DashboardPage = Backbone.View.extend({
    el: '.page',
    designs: null,
    initialize: function() {
        this.designs = new Designs();
        this.listenTo(Backbone);
        _.bindAll(this, 'render','fetchDesignsAndRender');
    },
    render: function () {
        var that = this;
        this.designs.fetch({
          success: function(response) {
            that.fetchDesignsAndRender(response);
          },
          error: function(model, response, options) {
              console.log("couldn't fetch product data - " + response);
          }
        });
    },
    fetchDesignsAndRender: function() {
        var that = this;
        var designs = that.designs;
        $(this.el).html(_.template(dashboardPageTemplate)({
            'designdetails':designs.toJSON()
        }));
    }
  });
  return DashboardPage;
});
