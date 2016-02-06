/**
 * Created by mygubbi on 26/1/16.
 */
define([
    'jquery',
    'underscore',
    'backbone',
    '/js/models/story.js',
    'text!/templates/story/full_story.html'
], function($, _, Backbone, Story, fullStoryTemplate) {
    var FullStoryView = Backbone.View.extend({
        el: '.page',
        story: new Story(),
        render: function() {

            var that = this;

            this.story.fetch({
                success: function() {
                    that.fetchStoryAndRender(that.model.id);
                },
                error: function(model, response, options) {
                    console.log("couldn't fetch story data - " + response);
                }
            });

        },
        fetchStoryAndRender: function(id) {

            var that = this;
            var stories = that.story;
            stories = stories.toJSON();

            delete stories.id;

            //console.log(_(stories).pluck('date_of_publish'));
            stories = _(stories).sortBy(function(story) {
                return Date.parse(story.date_of_publish);
            }).reverse();
            //console.log(_(stories).pluck('date_of_publish'));

            $(this.el).html(_.template(fullStoryTemplate)({
                'stories': stories,
                'story_id': id
            }));

        }
    });
    return FullStoryView;
});