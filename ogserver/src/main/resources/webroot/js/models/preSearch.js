/**
 * Created by og on 10/12/15.
 */
define([
    'jquery',
    'backbone'
], function($, Backbone) {
    var Pre_Search = Backbone.Model.extend({
        urlRoot:restBase + '/api/pre.search'
    });
    return Pre_Search;
});