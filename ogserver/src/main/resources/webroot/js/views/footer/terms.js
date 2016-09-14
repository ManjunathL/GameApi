define([
    'jquery',
    'underscore',
    'backbone',
    'text!templates/footer/terms.html'
], function($, _, Backbone, termsTemplate) {
    var TermsView = Backbone.View.extend({
        el: '.page',
        render: function() {
            $(this.el).html(termsTemplate);
        }
    });
    return TermsView;
});