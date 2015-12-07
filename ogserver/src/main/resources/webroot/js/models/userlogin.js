define([
    'jquery',
    'backbone'
], function($, Backbone) {
    var UserLogin = Backbone.Model.extend({
        urlRoot: restBase + '/api/user.login',
        setAndLogin: function (name, email, imgUrl, provider) {
            this.set({
                name: name,
                email: email,
                imgUrl: imgUrl,
                provider: provider
            });
/*
            this.save({
                success: function(model, response, options) {
                }, error: function(model, response, options) {
                }
            });
*/
        },
        isLoggedIn: function() {
            return this.get('name');
        },
        isProviderFb: function() {
            return this.get('provider') === "facebook";
        },
        isProviderGoogle: function() {
            return this.get('provider') === "google";
        },
        isProviderNative: function() {
            return this.get('provider') === "native";
        }
    });
    return UserLogin;
});
