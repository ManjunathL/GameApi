/**
 * Created by mygubbi on 26/1/16.
 */
define([
    'jquery',
    'underscore',
    'backbone',
    'highlight',
    'collections/stories',
    'models/story',
    'analytics',
    'text!templates/story/full_story.html',
    'views/story/full_story_helper'
], function($, _, Backbone, Highlight, Stories, Story, Analytics, fullStoryTemplate, FullStoryHelper) {
    var FullStoryView = Backbone.View.extend({
        el: '.page',
        story: new Story(),
        stories: null,
        initialize: function() {
            Analytics.apply(Analytics.TYPE_GENERAL);
            this.stories = new Stories();
        },
        render: function() {

            var that = this;

            var blog_name = that.model.name;

            if(typeof(blog_name) !== 'undefined'){
                blog_name = blog_name.replace(/-/g, ' ');
                blog_name = blog_name.replace(/_/, '-');
            }

            this.stories.fetch({
                success: function() {
                    that.fetchStoryAndRender(blog_name);
                },
                error: function(model, response, options) {
                    console.log("couldn't fetch story data - " + response);
                }
            });

        },
        fetchStoryAndRender: function(name) {

            var that = this;
            var stories = that.stories;
            stories = stories.toJSON();
            var full_story = {};

            delete stories.id;

            _.find(stories, function(item, index) {
                if (item.blogId.indexOf(name) !== -1) {
                    full_story = item;
                }
            });

            //console.log(_(stories).pluck('date_of_publish'));
            stories = _(stories).sortBy(function(story) {
                return Date.parse(story.date_of_publish);
            }).reverse();
            //console.log(_(stories).pluck('date_of_publish'));

            var rec_stories = [];
            $.each(stories.slice(1,4), function(i, data) {
                rec_stories.push(data);
            });

            var fullTemp = _.template(fullStoryTemplate);

            $(this.el).html(fullTemp({
                'rec_stories': rec_stories,
                'full_story': full_story
            }));

            FullStoryHelper.ready(that);

        }
    });
    return FullStoryView;
});