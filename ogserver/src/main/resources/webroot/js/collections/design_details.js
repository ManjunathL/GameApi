define([
  'jquery',
  'underscore',
  'backbone',
  'models/design_detail'
], function($, _, Backbone, DesignDetail){
    var DesignDetails = Backbone.Collection.extend({
        model: DesignDetail,
        url: baseApiUrl + '/gapi/workbench/designmaster/id',
        initialize: function(models) {
          _.each(models, function (design_detail){
            design_detail = new DesignDetail(design_detail);
          });
        }
    });
  return DesignDetails;
});
