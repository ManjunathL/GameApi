/**
 * Created by og on 19/11/15.
 */
define([
    'jquery',
    'backbone'
], function($, Backbone) {
    var User = Backbone.Model.extend({
            urlRoot:restBase + '/api/user.short'
    });
    return User;
});