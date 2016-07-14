define([
    'jquery',
    'underscore',
    'backbone',
    'text!templates/footer/faq.html'
], function($, _, Backbone, faqTemplate) {
    var FaqView = Backbone.View.extend({
        el: '.page',
        render: function() {
            $(this.el).html(faqTemplate);
        }
    });
    return FaqView;
});