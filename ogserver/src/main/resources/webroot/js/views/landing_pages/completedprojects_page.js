/**
 * Created by mygubbi on 01/07/16.
 */
define([
    'jquery',
    'underscore',
    'backbone',
    'text!templates/landing_pages/completedprojects_page.html',
    'cloudinary_jquery',
    'slyutil',
    'mgfirebase',
    'consultutil',
    'models/story',
    'text!templates/story/home_story.html',
    'analytics'
], function($, _, Backbone, completedprojectsPageTemplate, CloudinaryJquery, SlyUtil, MGF, ConsultUtil, Story, blogPageTemplate, Analytics) {
    var CompletedProjectsPageVIew = Backbone.View.extend({
        el: '.page',
        ref: null,
        story: new Story(),
        renderWithUserProfCallback: function(userProfData) {
            $(this.el).html(_.template(completedprojectsPageTemplate)({
                'userProfile': userProfData
            }));
            $.cloudinary.responsive();
        },
        render: function() {
            var authData = this.ref.getAuth();
            MGF.getUserProfile(authData, this.renderWithUserProfCallback);
            this.getStories();
            this.ready();
        },
        getStories: function() {
                    var that = this;
                    that.story.fetch({
                         data: {
                             "tags": 'all'
                         },
                        success: function(response) {
                            var lateststories = response.toJSON();
                            lateststories = _(lateststories).sortBy(function(story) {
                                return Date.parse(story.date_of_publish);
                            }).reverse();

                            var rec_stories = [];
                            $.each(lateststories.slice(1,3), function(i, data) {
                                rec_stories.push(data);
                            });
                            $("#latest_blog_content").html(_.template(blogPageTemplate)({
                              'lateststories': rec_stories
                            }));
                        },
                        error: function(model, response, options) {
                            console.log("couldn't fetch story data - " + response);
                        }
                    });
                },

        ready: function() {
            if ($('#lpalt-frame').length > 0) {
                var $lpalt_frame = $('#lpalt-frame');
                var $lpalt_wrap = $lpalt_frame.parent().parent();
                SlyUtil.create($lpalt_wrap, '#lpalt-frame', '.lpalt-next', '.lpalt-prev').init();
            }
            if ($('#brand-frame').length > 0) {
                var $brand_frame = $('#brand-frame');
                var $brand_wrap = $brand_frame.parent().parent();
                SlyUtil.create($brand_wrap, '#brand-frame', '.brand-next', '.brand-prev').init();
            }
        },
        initialize: function() {
            this.ref = MGF.rootRef;
            Analytics.apply(Analytics.TYPE_GENERAL);
            $.cloudinary.config({ cloud_name: 'mygubbi', api_key: '492523411154281'});
            _.bindAll(this, 'renderWithUserProfCallback');
        }
    });
    return CompletedProjectsPageVIew;
});