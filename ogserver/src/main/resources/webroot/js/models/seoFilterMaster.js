/**
 * Created by Mehbub on 24/10/16.
 */

define([
    'jquery',
    'backbone'
], function($, Backbone) {
    var SEOFilterMaster = Backbone.Model.extend({
        //urlRoot:restBase + '/api/seo',
        urlRoot:restBase + '/api/seo',
        defaults: {
          category: '',
          subCategory: '',
          location: ''
        }
    });
    return SEOFilterMaster;
});