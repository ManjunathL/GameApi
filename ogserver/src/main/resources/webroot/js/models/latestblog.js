/**
 * Created by mygubbi on 11/12/16.
 */
define([
    'jquery',
    'backbone'
], function($, Backbone){
    var Latestblog = Backbone.Model.extend({
        urlRoot:restBase + '/api/blogs/latestTag',
        defaults: {
            tags: ''
        }
    });
    return Latestblog;
});
