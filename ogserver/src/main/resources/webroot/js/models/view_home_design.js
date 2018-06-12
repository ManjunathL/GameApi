define([
    'jquery',
    'backbone'
], function($, Backbone){
    var ViewHomeDesign = Backbone.Model.extend({
      urlRoot:baseRestApiUrl + 'MyGubbiApi/looks/get/user-selected/looks/'
    });
    return ViewHomeDesign;
});
