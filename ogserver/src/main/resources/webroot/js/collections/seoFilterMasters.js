define([
    'jquery',
    'backbone',
    'underscore',
    'models/seoFilterMaster'
], function($, Backbone, _, SeoFilterMaster) {
    var SEOFilterMaster = Backbone.Collection.extend({
        model: SeoFilterMaster,
        url: 'https://192.168.104.88/api/seo',
        initialize: function(models) {
         _.each(models, function (seofilter){
           seofilter = new SeoFilterMaster(seofilter);
         });
       }
    });
    return SEOFilterMaster;
});