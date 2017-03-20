define([
    'jquery',
    'backbone',
    'underscore'
], function($, Backbone, _) {
    var Projects = Backbone.Collection.extend({
        url: '/api/partner/designGalleryDetails'
    });
    return Projects;
});