define([
    'jquery',
    'backbone',
    'underscore'
], function($, Backbone, _) {
    var Projects = Backbone.Collection.extend({
        url: 'https://192.168.104.88/api/partner/projectDetails'
    });
    return Projects;
});