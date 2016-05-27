/**
 * Created by mygubbi on 26/1/16.
 */
define([
    'jquery',
    'underscore',
    'backbone',
    '/js/models/story.js',
    '/js/analytics.js',
    'text!/templates/story/full_story.html',
    '/js/views/story/full_story_helper.js'
], function($, _, Backbone, Story, Analytics, fullStoryTemplate, FullStoryHelper) {
    var FullStoryView = Backbone.View.extend({
        el: '.page',
        story: new Story(),
        initialize: function() {
            Analytics.apply(Analytics.TYPE_GENERAL);
        },
        render: function() {

            var that = this;

            this.story.fetch({
                success: function() {
                    that.fetchStoryAndRender(that.model.name);
                },
                error: function(model, response, options) {
                    console.log("couldn't fetch story data - " + response);
                }
            });

        },
        fetchStoryAndRender: function(name) {

            var that = this;
            var stories = that.story;
            stories = stories.toJSON();
            var full_story = {};

            delete stories.id;

            _.find(stories, function(item, index) {
                if (item.blog_url == name) {
                    full_story = item;
                }
            });

            //console.log(_(stories).pluck('date_of_publish'));
            stories = _(stories).sortBy(function(story) {
                return Date.parse(story.date_of_publish);
            }).reverse();
            //console.log(_(stories).pluck('date_of_publish'));

            var rec_stories = [];
            $.each(stories.slice(0,4), function(i, data) {
                rec_stories.push(data);
            });

            $(this.el).html(_.template(fullStoryTemplate)({
                'stories': rec_stories,
                'full_story': full_story
            }));

            FullStoryHelper.ready(that);

        }
    });
    return FullStoryView;
});