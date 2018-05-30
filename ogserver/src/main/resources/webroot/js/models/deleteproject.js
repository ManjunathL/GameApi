define([
    'jquery',
    'backbone'
], function($, Backbone){
    var DeleteProject = Backbone.Model.extend({
        urlRoot:baseRestApiUrl + '/MyGubbiApi/userproject/delete/'
    });
    return DeleteProject;
});
