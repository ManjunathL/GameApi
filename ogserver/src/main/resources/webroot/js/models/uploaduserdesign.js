define([
    'jquery',
    'backbone'
], function($, Backbone){
    var UploadUserDesign = Backbone.Model.extend({
        urlRoot:baseRestApiUrl + '/MyGubbiApi/userupload/design/'
    });
    return UploadUserDesign;
});
