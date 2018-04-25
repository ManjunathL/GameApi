define([
    'jquery',
    'backbone'
], function($, Backbone){
    var SaveShortListDesign = Backbone.Model.extend({
      urlRoot:baseRestApiUrl + 'MyGubbiApi/designs/save/userSelected/'
    });
    return SaveShortListDesign;
});
