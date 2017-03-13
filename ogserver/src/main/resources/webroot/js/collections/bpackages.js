define([
    'jquery',
    'backbone',
    'underscore'
], function($, Backbone, _) {
    var Bpackages = Backbone.Collection.extend({
        url: 'https://192.168.104.88/api/partner/packageDetails'
    });
    return Bpackages;
});