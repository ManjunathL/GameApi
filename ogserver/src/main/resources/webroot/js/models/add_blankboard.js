define([
    'jquery',
    'backbone'
], function($, Backbone){
    var AddBlankboard = Backbone.Model.extend({
        urlRoot:baseRestApiUrl + '/MyGubbiApi/conceptboard/create/blank/'
    });
    return AddBlankboard;
});
