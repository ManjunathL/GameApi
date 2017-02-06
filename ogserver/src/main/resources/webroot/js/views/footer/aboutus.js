define([
    'jquery',
    'underscore',
    'backbone',
    'text!templates/footer/aboutus.html'
], function($, _, Backbone, aboutTemplate) {
    var AboutView = Backbone.View.extend({
        el: '.page',
        render: function() {
            $(this.el).html(_.template(aboutTemplate));
            document.getElementById("canlink").href = window.location.href;

        }
    });
    return AboutView;
});