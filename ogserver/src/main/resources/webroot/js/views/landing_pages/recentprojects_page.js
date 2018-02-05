/**
 * Created by mygubbi on 01/02/18.
**/

define([
    'jquery',
    'underscore',
    'backbone',
    'text!templates/landing_pages/recentprojects_page.html',
    'cloudinary_jquery',
    'mgfirebase',
    'collections/recent_projects',
    'analytics',
    'libs/unveil/jquery.unveil.mg'
], function($, _, Backbone, recentprojectsPageTemplate, CloudinaryJquery, MGF, RecentProjects, Analytics, unveil) {
    var RecentProjectsPageVIew = Backbone.View.extend({
        el: '.page',
        ref: null,
        recent_projects: null,
        refAuth: null,
        renderWithUserProfCallback: function(userProfData) {
            var that = this;
            var nwprojects = that.recent_projects;
            nwprojects = nwprojects.toJSON();

            $(this.el).html(_.template(recentprojectsPageTemplate)({
                'userProfile': userProfData,
                'recentprojects': nwprojects
            }));

             $.cloudinary.responsive();
             $("img").unveil(200);
        },
        render: function() {
            var authData = this.refAuth.currentUser;
            MGF.getUserProfile(authData, this.renderWithUserProfCallback);
            document.getElementById("canlink").href = window.location.href;

            var that = this;
            that.recent_projects.fetch({
                 data: {
                 },
                success: function(response) {
                    that.renderWithUserProfCallback();
                },
                error: function(model, response, options) {
                    console.log("couldn't fetch recent projects data - " + response);
                }
            });
        },
        initialize: function() {
            this.ref = MGF.rootRef;
            this.refAuth = MGF.refAuth;
            this.recent_projects = new RecentProjects();
            Analytics.apply(Analytics.TYPE_GENERAL);
            $.cloudinary.config({ cloud_name: 'mygubbi', api_key: '492523411154281'});
            _.bindAll(this, 'renderWithUserProfCallback');
        }
    });
    return RecentProjectsPageVIew;
});