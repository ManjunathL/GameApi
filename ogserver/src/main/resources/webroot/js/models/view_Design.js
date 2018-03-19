define([
    'jquery',
    'backbone'
], function($, Backbone){
    var ViewDesign = Backbone.Model.extend({
      urlRoot:baseRestApiUrl + 'MyGubbiApi/designs/getdesignsperspace/'
    });
    return ViewDesign;
});
