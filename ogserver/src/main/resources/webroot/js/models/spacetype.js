define([
    'jquery',
    'backbone'
], function($, Backbone){
    var SpaceType = Backbone.Model.extend({
        urlRoot: baseApiUrl + '/gapi/workbench/conceptmaster/spacetype',
        defaults: {
           spaceType: ''
        }
    });
    return SpaceType;
});
