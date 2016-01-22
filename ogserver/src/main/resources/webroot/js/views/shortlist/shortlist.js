define([
    'jquery',
    'underscore',
    'backbone',
    'bootstrap',
    'text!templates/shortlist/shortlist.html',
    'mgfirebase',
    'css!../../../css/shortlist'
], function($, _, Backbone, Bootstrap, shortlistTemplate, MGF) {
    var ShortlistView = Backbone.View.extend({
        el: '.page',
        initialize: function() {
            _.bindAll(this, 'render');
        },
        render: function() {
            $(this.el).html(_.template(shortlistTemplate)({
                'shortlistedItems': MGF.getShortListedItems()
            }));
        },
        events: {
            "click .shortlistable": "removeShortlistItem"
        },
        removeShortlistItem: function(e) {
            e.preventDefault();

            var currentTarget = $(e.currentTarget);
            var productId = currentTarget.data('element');
            var that = this;

            MGF.removeShortlistProduct(productId).then(function(){
                currentTarget.children('.list-heart').toggleClass('fa-heart-o');
                currentTarget.children('.list-heart').toggleClass('fa-heart');
                currentTarget.children('.list-txt').html('shortlist');
                that.render();
            });
        }
    });
    return ShortlistView;
});