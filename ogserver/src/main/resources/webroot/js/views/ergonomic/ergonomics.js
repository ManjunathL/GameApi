define([
    'jquery',
    'underscore',
    'backbone',
    'text!templates/ergonomics/ergonomics.html'
], function($, _, Backbone, ergonomicsTemplate) {
    var ErgonomicView = Backbone.View.extend({
        el: '.page',
        render: function() {
            $(this.el).html(_.template(ergonomicsTemplate));
        }
    });
    return ErgonomicView;
});