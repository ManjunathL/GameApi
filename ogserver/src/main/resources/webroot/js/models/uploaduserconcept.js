define([
    'jquery',
    'backbone'
], function($, Backbone){
    var UploadUserConcept = Backbone.Model.extend({
        urlRoot:baseRestApiUrl + '/MyGubbiApi/upload/web/usercreatedconcept/'
    });
    return UploadUserConcept;
});
