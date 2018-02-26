define([
    'jquery',
    'backbone'
], function($, Backbone){
    var ProductDetail = Backbone.Model.extend({
        urlRoot: baseApiUrl + '/gapi/workbench/productmaster/id',
        defaults: {
              id: ''
          }
    });
    return ProductDetail;
});
