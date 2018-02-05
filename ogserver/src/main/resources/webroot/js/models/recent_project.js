define([
    'jquery',
    'backbone'
], function($, Backbone){
    var RecentProject = Backbone.Model.extend({
        urlRoot: restBase + '/api/recentProject',
        defaults: {
            id: '',
            tags:''
        }
    });
    return RecentProject;
});
