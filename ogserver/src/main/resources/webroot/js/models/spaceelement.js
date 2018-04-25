define([
    'jquery',
    'backbone'
], function($, Backbone){
    var SpaceElement = Backbone.Model.extend({
        urlRoot: baseRestApiUrl + 'MyGubbiApi/spaceelement/get'
    });
    return SpaceElement;
});
