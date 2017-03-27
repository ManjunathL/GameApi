define([
    'jquery',
    'backbone'
], function($, Backbone){
    var Addon = Backbone.Model.extend({
        urlRoot:restBase + '/api/addon',
        defaults: {
            id: ''
        }
    });
    return Addon;
});
