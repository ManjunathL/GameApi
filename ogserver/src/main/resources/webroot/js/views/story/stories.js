/**
 * Created by mygubbi on 25/1/16.
 */
define([
    'jquery',
    'underscore',
    'backbone',
    'jquerywaterfall',
    'collections/stories',
    'models/story',
    'analytics',
    'text!templates/story/new_stories.html'
], function($, _, Backbone, Waterfall, Stories, Story, Analytics, storiesTemplate) {
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
            console.log(selectedBlogsCategory);
            this.stories.fetch({
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
            var nwstories = that.stories;
            nwstories = nwstories.toJSON();

            delete nwstories.id;

            //console.log(_(stories).pluck('date_of_publish'));
            nwstories = _(nwstories).sortBy(function(story) {
                return Date.parse(story.date_of_publish);
            }).reverse();
            //console.log(_(stories).pluck('date_of_publish'));

            var rec_stories = [];
            $.each(nwstories.slice(0,3), function(i, data) {
                rec_stories.push(data);
            });

            console.log("---------- Start Stories ------------");
            console.log(nwstories);
            console.log("---------- End Stories ------------");

            $(this.el).html(_.template(storiesTemplate)({'selCat':selectedBlogsCategory,'stories': nwstories,'rec_stories': rec_stories}));
            this.ready();
        },
        events: {
            "click #older-post-lnk": "openOlderBlogs"
        },
        openOlderBlogs: function(){
            $("#older_posts").toggle();
            var setting = {
                    // top offset
                    top : false,

                    // the container witdh
                    w : false,

                    // the amount of columns
                    col : false,

                    // the space bewteen boxes
                    gap : 10,

                    // breakpoints in px
                    // 0-400: 1 column
                    // 400-600: 2 columns
                    // 600-800: 3 columns
                    // 800-1000: 4 columns
                    gridWidth : [400,600],

                    // the interval to check the screen
                    refresh: 500,
                    timer : false,

                    // execute a function as the page is scrolled to the bottom
                    scrollbottom : false
            };
            $('#box2').waterfall(setting);
        },
        ready: function(){
            var setting = {
                    // top offset
                    top : false,

                    // the container witdh
                    w : false,

                    // the amount of columns
                    col : false,

                    // the space bewteen boxes
                    gap : 10,

                    // breakpoints in px
                    // 0-400: 1 column
                    // 400-600: 2 columns
                    // 600-800: 3 columns
                    // 800-1000: 4 columns
                    gridWidth : [400,600],

                    // the interval to check the screen
                    refresh: 500,
                    timer : false,

                    // execute a function as the page is scrolled to the bottom
                    scrollbottom : false
            };
            $('#box1').waterfall(setting);
        }
    });
    return StoriesView;
});

