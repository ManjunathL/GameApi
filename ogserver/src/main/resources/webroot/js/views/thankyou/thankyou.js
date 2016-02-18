/**
 * Created by mygubbi on 17/2/16.
 */
define([
    'jquery',
    'underscore',
    'backbone',
    'text!/templates/thankyou/thankyou.html'
], function($, _, Backbone, thankyouTemplate) {
    var ThankyouView = Backbone.View.extend({
        el: '.page',
        render: function() {
            $(this.el).html(_.template(thankyouTemplate));
        }
    });
    return ThankyouView;
});