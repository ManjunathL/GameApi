/**
 * Created by og on 20/11/15.
 */
define([
    'jquery',
    'backbone'
], function($, Backbone) {
    var UserShortList = Backbone.Model.extend({
        urlRoot:restBase + '/api/shortlist.short'
    });
    return UserShortList;
});