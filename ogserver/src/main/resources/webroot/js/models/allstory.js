/**
 * Created by mygubbi on 26/1/16.
 */
define([
    'jquery',
    'backbone'
], function($, Backbone){
    var Story = Backbone.Model.extend({
        urlRoot:restBase + '/api/blogs/allBlogs',
        defaults: {
            tags: ''
        }
    });
    return Story;
});
