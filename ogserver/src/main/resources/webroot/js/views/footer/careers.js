define([
    'jquery',
    'underscore',
    'backbone',
    'text!templates/footer/careers.html'
], function($, _, Backbone, careerTemplate) {
    var CareerView = Backbone.View.extend({
        el: '.page',
        render: function() {
            $(this.el).html(careerTemplate);
            document.getElementById("canlink").href = window.location.href;

        }
    });
    return CareerView;
});