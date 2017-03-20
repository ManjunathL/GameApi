define([
    'jquery',
    'backbone',
    'underscore'
], function($, Backbone, _) {
    var Bpackages = Backbone.Collection.extend({
        url: '/api/partner/packageDetails'
    });
    return Bpackages;
});