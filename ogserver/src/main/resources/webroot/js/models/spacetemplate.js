define([
    'jquery',
    'backbone'
], function($, Backbone){
    var SpaceTemplate = Backbone.Model.extend({
        urlRoot:baseRestApiUrl + '/MyGubbiApi/spacetemplate/get/'
    });
    return SpaceTemplate;
});
