/**
 * Created by mygubbi on 25/1/16.
 */
define([
    'jquery',
    'underscore',
    'backbone',
    'jquerywaterfall',
    'highlight',
    'collections/stories',
    'collections/diys',
    'collections/latestblogs',
    'models/story',
    'analytics',
    'text!templates/story/new_stories.html'
], function($, _, Backbone, Waterfall, Highlight, Stories, Diys, Latestblogs, Story, Analytics, storiesTemplate) {
    var StoriesView = Backbone.View.extend({
        el: '.page',
        story: new Story(),
        stories: null,
        diys: null,
        latestblogs: null,
        initialize: function() {
            Analytics.apply(Analytics.TYPE_GENERAL);
            this.stories = new Stories();
            this.diys = new Diys();
            this.latestblogs = new Latestblogs();
        },
        render: function() {

            var that = this;
            document.getElementById("canlink").href = window.location.href;
            var selectedBlogsCategory = this.model.blogcategory;

            this.stories.fetch({
                 data: {
                     "tags": selectedBlogsCategory
                 },
                success: function(response) {
                    that.fetchDiysAndRender(selectedBlogsCategory);
                    that.fetchLatestBlogAndRender(selectedBlogsCategory);
                },
                error: function(model, response, options) {
                    console.log("couldn't fetch story data - " + response);
                }
            });

        },
        fetchDiysAndRender: function(selectedBlogsCategory) {
            var that = this;
            this.diys.fetch({
                 data: {
                     "id": "1"
                 },
                success: function(response) {
                    that.fetchStoriesAndRender(selectedBlogsCategory);
                },
                error: function(model, response, options) {
                    console.log("couldn't fetch diy data - " + response);
                    console.log(response);
                }
            });
        },
        fetchLatestBlogAndRender: function(selectedBlogsCategory) {
            var that = this;
            this.latestblogs.fetch({
                 data: {
                     "tags": selectedBlogsCategory
                 },
                success: function(response) {
                    that.fetchStoriesAndRender(selectedBlogsCategory);
                },
                error: function(model, response, options) {
                    console.log("couldn't fetch latest blogs data - " + response);
                    console.log(response);
                }
            });
        },
        fetchStoriesAndRender: function(selectedBlogsCategory) {
            var that = this;
            var nwdiys = that.diys;
            nwdiys = nwdiys.toJSON();
            var nwstories = that.stories;
            nwstories = nwstories.toJSON();

            var latestBlogs = that.latestblogs.toJSON();

            delete nwstories.id;

            nwdiys = _(nwdiys).sortBy(function(diyss) {
             return Date.parse(diyss.date_of_publish);
            }).reverse();

            //console.log(_(stories).pluck('date_of_publish'));
            nwstories = _(nwstories).sortBy(function(story) {
                return Date.parse(story.date_of_publish);
            }).reverse();


            var rec_stories = [];
            $.each(nwstories.slice(0,3), function(i, data) {
                rec_stories.push(data);
            });

            var latest_stories = [];
            $.each(latestBlogs.slice(0,3), function(i, data) {
                latest_stories.push(data);
            });

            var latest_diys = [];
            $.each(nwdiys.slice(0,3), function(i, data) {
                latest_diys.push(data);
            });

            //console.log("---------- Start Stories ------------");
            //console.log(latest_stories);
            //console.log("---------- End Stories ------------");

            $(this.el).html(_.template(storiesTemplate)({
                'selCat':selectedBlogsCategory,
                'stories': nwstories,
                'rec_stories': rec_stories,
                'latest_stories': latest_stories,
                'diys':latest_diys,
                'alldiys':nwdiys
            }));
            this.ready();
        },
        events: {
            "click #older-post-lnk": "openOlderBlogs",
            "click #older-diy-lnk": "openOlderDIYs"
        },
        openOlderDIYs: function(){
            $("#older_diys").toggle();
            var wtrsetting = {
                    top : false,
                    w : false,
                    col : false,
                    gap : 10,
                    gridWidth : [200,400,600],
                    refresh: 500,
                    timer : false,
                    scrollbottom : false
            };
            $('#box3').waterfall(wtrsetting);
        },
        openOlderBlogs: function(){
            $("#older_posts").toggle();
            var wtrsetting = {
                    top : false,
                    w : false,
                    col : false,
                    gap : 10,
                    gridWidth : [400,600],
                    refresh: 500,
                    timer : false,
                    scrollbottom : false
            };
            $('#box2').waterfall(wtrsetting);
        },
        ready: function(){
            var wtrsetting1 = {
                    top : false,
                    w : false,
                    col : false,
                    gap : 10,
                    gridWidth : [400,600],
                    refresh: 500,
                    timer : false,
                    scrollbottom : false
            };
            $('#box1').waterfall(wtrsetting1);
        }
    });
    return StoriesView;
});

