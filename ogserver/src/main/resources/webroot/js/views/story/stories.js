/**
 * Created by mygubbi on 25/1/16.
 */
define([
    'jquery',
    'underscore',
    'backbone',
    'models/story',
    'text!templates/story/stories.html'
], function($, _, Backbone, Story, storiesTemplate) {
    var StoriesView = Backbone.View.extend({
        el: '.page',
        story: new Story(),
        render: function() {

            var that = this;

            this.story.fetch({
                success: function() {
                    that.fetchStoriesAndRender();
                    //this.ready();
                },
                error: function(model, response, options) {
                    console.log("couldn't fetch story data - " + response);
                }
            });
        },
        fetchStoriesAndRender: function() {
            var that = this;
            var stories = that.story;

            $(this.el).html(_.template(storiesTemplate)({'stories': stories.toJSON()}));
            //console.log('stories : ' + JSON.stringify(stories));
        }
    });
    return StoriesView;
});

