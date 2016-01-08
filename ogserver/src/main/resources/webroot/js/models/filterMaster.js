/**
 * Created by og on 10/12/15.
 */
define([
    'jquery',
    'backbone'
], function($, Backbone) {
    var FilterMaster = Backbone.Model.extend({
        urlRoot:restBase + '/api/filter.master'
    });
    return FilterMaster;
});