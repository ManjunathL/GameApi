/**
 * Created by mygubbi on 25/1/16.
 */
define([
    'jquery',
    'underscore',
    'backbone',
    'models/story',
    'analytics',
    'text!templates/story/new_stories.html'
], function($, _, Backbone, Story, Analytics, storiesTemplate) {
    var StoriesView = Backbone.View.extend({
        el: '.page',
        story: new Story(),
        initialize: function() {
            Analytics.apply(Analytics.TYPE_GENERAL);
        },
        render: function() {

            var that = this;
            var selectedBlogsCategory = this.model.blogcategory;
            console.log(selectedBlogsCategory);
            this.story.fetch({
                 data: {
                     "tags": selectedBlogsCategory
                 },
                success: function(response) {

                    console.log(response);
                    //return false;
                    that.fetchStoriesAndRender(selectedBlogsCategory);
                    //this.ready();
                },
                error: function(model, response, options) {
                    console.log("couldn't fetch story data - " + response);
                }
            });
        },
        fetchStoriesAndRender: function(selectedBlogsCategory) {
            var that = this;
            var stories = that.story;
            stories = stories.toJSON();

            delete stories.id;

            //console.log(_(stories).pluck('date_of_publish'));
            stories = _(stories).sortBy(function(story) {
                return Date.parse(story.date_of_publish);
            }).reverse();
            //console.log(_(stories).pluck('date_of_publish'));

            var rec_stories = [];
            $.each(stories.slice(1,4), function(i, data) {
                rec_stories.push(data);
            });

            $(this.el).html(_.template(storiesTemplate)({'selCat':selectedBlogsCategory,'stories': stories,'rec_stories': rec_stories}));
        }
    });
    return StoriesView;
});

