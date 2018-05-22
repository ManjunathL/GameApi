define([
  'jquery',
  'lodash',
  'backbone',
  'bootstrap',
  'pinterest_grid',
  'owlcarousel',
  'text!templates/project/listing_project.html'
], function($, _, Backbone, Bootstrap, pinterest_grid, owlCarousel, listingProjectPageTemplate){
  var ListingProjectPage = Backbone.View.extend({
    el: '.page',
    initialize: function() {
        this.listenTo(Backbone);
        _.bindAll(this, 'render');
    },
    render: function () {
        $(this.el).html(_.template(listingProjectPageTemplate));
    }
  });
  return ListingProjectPage;
});
