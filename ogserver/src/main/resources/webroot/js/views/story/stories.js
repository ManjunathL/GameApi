/**
 * Created by mygubbi on 25/1/16.
 */
define([
    'jquery',
    'underscore',
    'backbone',
    'models/story',
    'analytics',
    'text!templates/story/stories.html'
], function($, _, Backbone, Story, Analytics, storiesTemplate) {
    var StoriesView = Backbone.View.extend({
        el: '.page',
        story: new Story(),
        initialize: function() {
            Analytics.apply(Analytics.TYPE_GENERAL);
        },
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
            stories = stories.toJSON();

            delete stories.id;

            //console.log(_(stories).pluck('date_of_publish'));
            stories = _(stories).sortBy(function(story) {
                return Date.parse(story.date_of_publish);
            }).reverse();
            //console.log(_(stories).pluck('date_of_publish'));

            $(this.el).html(_.template(storiesTemplate)({'stories': stories}));
        }
    });
    return StoriesView;
});

