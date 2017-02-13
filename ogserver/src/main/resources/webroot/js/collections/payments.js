define([
    'jquery',
    'backbone',
    'underscore',
    'models/payment'
], function($, Backbone, _, Payment) {
    var MyPayment = Backbone.Collection.extend({
        model: Payment,
        url: 'https://test.payu.in/_payment'
    });
    return MyPayment;
});