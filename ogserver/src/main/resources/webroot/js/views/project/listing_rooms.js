define([
  'jquery',
  'lodash',
  'backbone',
  'bootstrap',
  'pinterest_grid',
  'owlcarousel',
  'text!templates/project/listing_rooms.html'
], function($, _, Backbone, Bootstrap, pinterest_grid, owlCarousel, listingRoomPageTemplate){
  var ListingRoomPage = Backbone.View.extend({
    el: '.page',
    initialize: function() {
        this.listenTo(Backbone);
        _.bindAll(this, 'render');
    },
    render: function () {
        $(this.el).html(_.template(listingRoomPageTemplate));
    }
  });
  return ListingRoomPage;
});
