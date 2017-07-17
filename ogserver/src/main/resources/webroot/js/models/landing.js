/**
 * Created by mygubbi on 26/1/16.
 */
define([
    'jquery',
    'backbone'
], function($, Backbone){
    var Story = Backbone.Model.extend({
        urlRoot:restBase + '/api/landingPage',
        defaults: {
            page_url: ''
        }
    });
    return Story;
});
