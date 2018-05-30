define([
    'jquery',
    'backbone'
], function($, Backbone){
    var SaveShortListDesign = Backbone.Model.extend({
      urlRoot:baseRestApiUrl + 'MyGubbiApi/looks/save/userSelected/'
    });
    return SaveShortListDesign;
});
