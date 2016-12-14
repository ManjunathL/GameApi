/**
 * Created by mygubbi on 25/1/16.
 */
define([
    'jquery',
    'underscore',
    'backbone',
    'collections/stories',
    'models/story',
    'analytics',
    'text!templates/story/stories.html'
], function($, _, Backbone, Stories, Story, Analytics, storiesTemplate) {
    var StoriesView = Backbone.View.extend({
        el: '.page',
        story: new Story(),
        stories: null,
        initialize: function() {
            Analytics.apply(Analytics.TYPE_GENERAL);
            this.stories = new Stories();
        },
        render: function() {

            var that = this;
            var selectedBlogsCategory = this.model.blogcategory;
            this.stories.fetch({
                 data: {
                     "tags": selectedBlogsCategory
                 },
                success: function(response) {

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
            var nwstories = that.stories;
            nwstories = nwstories.toJSON();

            delete nwstories.id;

            nwstories = _(nwstories).sortBy(function(story) {
                return Date.parse(story.date_of_publish);
            }).reverse();

            var rec_stories = [];
            $.each(nwstories.slice(1,4), function(i, data) {
                rec_stories.push(data);
            });
            $(this.el).html(_.template(storiesTemplate)({'selCat':selectedBlogsCategory,'stories': nwstories,'rec_stories': rec_stories}));
        }
    });
    return StoriesView;
});
