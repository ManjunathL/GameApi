/**
 * Created by mygubbi on 25/1/16.
 */
define([
    'jquery',
    'underscore',
    'backbone',
    'collections/landingpage',
    'models/landing',
    'analytics',
    'models/lpfilter',
    'text!templates/seo_page/landing-page.html'
], function($, _, Backbone, LandingPage, LandingPages, Analytics,LpFilter, landingPageTemplate) {
    var landingPageView = Backbone.View.extend({
        el: '.page',
        landingpages: null,
        lpfilter: null,
        initialize: function() {
            Analytics.apply(Analytics.TYPE_GENERAL);
            this.landingpages = new LandingPage();
            this.lpfilter = new LpFilter();


        },

    render: function() {

               var that = this;
               window.lpfilter = that.lpfilter;
                           document.getElementById("canlink").href = window.location.href;
                           var selectedPage= this.model.spageurl;
                           this.landingpages.fetch({
                                data: {
                                    "page_url": selectedPage
                                },
                               success: function(response) {
                           that.fetchDiyAndRender(selectedPage);
                       },
                       error: function(model, response, options) {
                           console.log("couldn't fetch diy data - " + model + response+ options);
                       }
                   });


               },
               fetchDiyAndRender: function(selectedPage) {
                console.log("i am in fetch");
                var that = this;
                var lp = that.landingpages;
                console.log("=-=-=");
                console.log(lp);
                that.lpfilter.set({
                    'seo': lp.toJSON()
                }, {
                    silent: true
                });
                var compiledTemplate = _.template(landingPageTemplate);
                    $(that.el).html(compiledTemplate({
                        'page': lp.toJSON(),
                "filter": that.lpfilter.toJSON()

                            }));
               }

           });

           return landingPageView;
});

