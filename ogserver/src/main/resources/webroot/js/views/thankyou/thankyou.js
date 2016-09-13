define([
    'jquery',
    'underscore',
    'backbone',
    'analytics',
    'text!templates/thankyou/thankyou.html'
], function($, _, Backbone, Analytics, thankyouTemplate) {
    var ThankyouView = Backbone.View.extend({
        el: '.page',
        initialize: function() {
            Analytics.apply(Analytics.TYPE_THANKYOU);
        },
        render: function() {
            $(this.el).html(_.template(thankyouTemplate));
        }
    });
    return ThankyouView;
});