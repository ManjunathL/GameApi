define([
    'jquery',
    'underscore',
    'backbone',
    'bootstrap',
    'cloudinary_jquery',
    'slyutil',
    'mgfirebase',
    'consultutil',
    'analytics',
    'bxslider',
    'collections/stories',
    'text!templates/dashboard/new-page.html',
    'text!templates/story/home_story.html',
    'text!templates/story/home_interior_story.html',
    'text!templates/story/home_trends_story.html',
    'text!templates/story/home_architechture_story.html',
    'libs/unveil/jquery.unveil.mg'
], function($, _, Backbone, Bootstrap, CloudinaryJquery, SlyUtil, MGF, ConsultUtil, Analytics, BxSlider, Stories, dashboardPageTemplate, blogPageTemplate, homeInteriorBlog, homeTrendBlog, homeArchBlog){
    var DashboardPage = Backbone.View.extend({
        el: '.page',
        ref: MGF.rootRef,
        refAuth: MGF.refAuth,
        story: new Stories(),

        renderWithUserProfCallback: function(userProfData) {
            this.getStories();
            this.getStoriesInteriors();
            this.getStoriesTrends();
            this.getStoriesArch();


            $(this.el).html(_.template(dashboardPageTemplate)({
              'userProfile': userProfData
            }));
            $.cloudinary.responsive();
            $("img").unveil();

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
                    $.each(lateststories.slice(0,2), function(i, data) {
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

        getStoriesInteriors: function() {
                    var that = this;
                    that.story.fetch({
                         data: {
                             "tags": "interiors"
                         },
                        success: function(res) {
                            var lateststories3 = res.toJSON();
                            lateststories3 = _(lateststories3).sortBy(function(story) {
                                return Date.parse(story.date_of_publish);
                            }).reverse();

                            $("#latest_interior_blog_content").html(_.template(homeInteriorBlog)({
                              'lateststories3': lateststories3[0]
                            }));
                        },
                        error: function(model, response, options) {
                            console.log("couldn't fetch story data - " + response);
                        }
                    });
                },
        getStoriesTrends: function() {
                            var that = this;
                            that.story.fetch({
                                 data: {
                                     "tags": "trends"
                                 },
                                success: function(respo) {
                                    var lateststories1 = respo.toJSON();
                                    lateststories1 = _(lateststories1).sortBy(function(story) {
                                        return Date.parse(story.date_of_publish);
                                    }).reverse();
                                    $("#latest_interior_blog_content1").html(_.template(homeTrendBlog)({
                                      'lateststories1': lateststories1[0]
                                    }));
                                },
                                error: function(model, response, options) {
                                    console.log("couldn't fetch story data - " + response);
                                }
                            });
                    },
        getStoriesArch: function() {
                            var that = this;
                            that.story.fetch({
                                 data: {
                                     "tags": "architecture"
                                 },
                                success: function(result) {
                                    var lateststories2 = result.toJSON();
                                    lateststories2 = _(lateststories2).sortBy(function(story) {
                                        return Date.parse(story.date_of_publish);
                                    }).reverse();
                                    $("#latest_interior_blog_content2").html(_.template(homeArchBlog)({
                                      'lateststories2': lateststories2[0]
                                    }));
                                },
                                error: function(model, response, options) {
                                    console.log("couldn't fetch story data - " + response);
                                }
                            });
                    },
        render: function() {
            var authData = this.refAuth.currentUser;
            MGF.getUserProfile(authData, this.renderWithUserProfCallback);
            document.getElementById("canlink").href = window.location.href;
        },
        ready: function () {
            var that = this;
            //that.autoPlayYouTubeModal();

           /* (function() {
                var v = document.getElementsByClassName("youtube-player");
                for (var n = 0; n < v.length; n++) {
                    var p = document.createElement("div");
                    p.innerHTML = that.labnolThumb(v[n].dataset.id);
                    p.onclick = that.labnolIframe;
                    v[n].appendChild(p);
                }
            })();*/
            if ($('#hmalt-frame').length > 0) {
                var $hmalt_frame = $('#hmalt-frame');
                var $hmalt_wrap = $hmalt_frame.parent().parent();
                SlyUtil.create($hmalt_wrap, '#hmalt-frame', '.hmalt-next', '.hmalt-prev').init();
            }
            if ($('#brand-frame').length > 0) {
                var $brand_frame = $('#brand-frame');
                var $brand_wrap = $brand_frame.parent().parent();
                SlyUtil.create($brand_wrap, '#brand-frame', '.brand-next', '.brand-prev').init();
            }

            /*var gallerySlider = $('.bxslider').bxSlider({
                mode: 'fade',
                auto: 'true',
                pause: 8000,
                speed: 2000,
                nextSelector: '.slider_right',
                prevSelector: '.slider_left'
            });*/
            $('.slider-move').click(function (e) {
                e.preventDefault();
                var number = $(this).data('number');
                gallerySlider.goToSlide(number);
                return false;
            });
        },
/*
        autoPlayYouTubeModal: function (ev) {
          var that = this;
          var theModal = $(ev.currentTarget).data("target"),
          videoSRC = $(ev.currentTarget).attr("data-theVideo"),

          videoSRCauto = videoSRC + "?autoplay=1";

          $(theModal + ' iframe').attr('src', videoSRCauto);
          $(theModal + ' button.close').click(function () {
              $(theModal + ' iframe').attr('src', videoSRC);
          });
          $('.modal').click(function () {
              $(theModal + ' iframe').attr('src', videoSRC);
          });

        },

        labnolThumb: function (id) {
            return '<img class="youtube-thumb" src="https://i.ytimg.com/vi/' + id + '/hqdefault.jpg"><div class="play-button"></div>';
        },
        labnolIframe: function () {
            var that = this;
            var iframe = document.createElement("iframe");
            iframe.setAttribute("src", "https://www.youtube.com/embed/" + that.parentNode.dataset.id + "?rel=0");
            iframe.setAttribute("frameborder", "0");
            iframe.setAttribute("id", "youtube-iframe");
            that.parentNode.replaceChild(iframe, that);
        },*/
        initialize: function() {
            Analytics.apply(Analytics.TYPE_GENERAL);
            $.cloudinary.config({ cloud_name: 'mygubbi', api_key: '492523411154281'});
            this.getStories();
            this.getStoriesInteriors();
            this.getStoriesTrends();
            this.getStoriesArch();
            _.bindAll(this, 'renderWithUserProfCallback', 'getStories', 'getStoriesInteriors','getStoriesTrends',  'getStoriesArch');

        },
        events: {
           // "click #youtubelnk": "autoPlayYouTubeModal"
        }
    });
    return DashboardPage;
});
