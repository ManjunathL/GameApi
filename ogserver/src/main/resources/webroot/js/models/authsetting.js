define([
    'jquery',
    'backbone'
], function($, Backbone){
    var Authsetting = Backbone.Model.extend({
        urlRoot: 'http://45.112.138.146:8080/MyGubbiAuth/oauth/token'
    });
    return Authsetting;
});
