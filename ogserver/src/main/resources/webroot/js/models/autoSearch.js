/**
 * Created by og on 10/12/15.
 */
define([
    'jquery',
    'backbone'
], function($, Backbone) {
    var Auto_Search = Backbone.Model.extend({
        urlRoot:restBase + '/api/auto.search'
    });
    return Auto_Search;
});