define([
  'jquery',
  'underscore',
  'backbone',
  'models/view_home_design'
], function($, _, Backbone, ViewHomeDesign){
    var ViewHomeDesigns = Backbone.Collection.extend({
        model: ViewHomeDesign,
        url: baseRestApiUrl + 'MyGubbiApi/looks/get/user-selected/looks/',
        getHomeDesigns: function(userId, projectId, options) {
            var urllnk = baseRestApiUrl + 'MyGubbiApi/looks/get/user-selected/looks/';
            this.url = urllnk + userId + "/" + projectId;
            return this.fetch(options);
        },
        initialize: function(models) {
          _.each(models, function (view_home_design){
            view_home_design = new ViewHomeDesigns(view_home_design);
          });
        },
    });
  return ViewHomeDesigns;
});