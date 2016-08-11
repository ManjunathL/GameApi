define([
    'jquery',
    'underscore',
    'backbone',
    'text!/templates/footer/privacypolicy.html'
], function($, _, Backbone, privacypolicyTemplate) {
    var PrivacyPolicyView = Backbone.View.extend({
        el: '.page',
        render: function() {
            $(this.el).html(privacypolicyTemplate);
        }
    });
    return PrivacyPolicyView;
});