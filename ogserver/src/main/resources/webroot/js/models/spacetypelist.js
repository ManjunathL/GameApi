define([
    'jquery',
    'backbone'
], function($, Backbone){
    var SpaceTypeList = Backbone.Model.extend({
        urlRoot: baseRestApiUrl + 'MyGubbiApi/spacetype/get'
    });
    return SpaceTypeList;
});
