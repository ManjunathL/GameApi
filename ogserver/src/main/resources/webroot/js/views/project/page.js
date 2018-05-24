define([
  'jquery',
  'lodash',
  'backbone',
  'bootstrap',
  'pinterest_grid',
  'owlcarousel',
  'text!templates/project/page.html'
], function($, _, Backbone, Bootstrap, pinterest_grid, owlCarousel, PageTemplate){
  var ListingProjectPage = Backbone.View.extend({
    el: '.page',
    initialize: function() {
        this.listenTo(Backbone);
        _.bindAll(this, 'render');
    },
    render: function () {
        $(this.el).html(_.template(PageTemplate));
    }
  });
  return ListingProjectPage;
});
