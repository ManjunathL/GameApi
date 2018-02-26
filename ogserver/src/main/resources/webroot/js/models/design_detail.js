define([
    'jquery',
    'backbone'
], function($, Backbone){
    var DesignDetail = Backbone.Model.extend({
        urlRoot: baseApiUrl + '/gapi/workbench/designmaster/id',
        defaults: {
              id: ''
          }
    });
    return DesignDetail;
});
