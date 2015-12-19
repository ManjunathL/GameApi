/**
 * Created by og on 15/12/15.
 */
define([
    'jquery',
    'underscore',
    'backbone',
    'bootstrap',
    'bootstrapvalidator',
    'firebase',
    'text!templates/user_profile/user_profile.html'
], function($, _, Backbone, Bootstrap, BootstrapValidator, Firebase, UserProfileTemplate ) {
    var UserProfileView = Backbone.View.extend({
        users: [],
        el: '.page',
        render: function() {
            var that = this;
            window.users = this.users;
            //console.log(this.users);
            var compiledTemplate = _.template(UserProfileTemplate);
            $(that.el).html(compiledTemplate({
                "users": that.users
            }));
        }
        //},
        //initialize: function() {
        //    this.users.on("add", this.render, this);
        //    this.users.on("reset", this.render, this);
        //}
    });
    return UserProfileView;
});
