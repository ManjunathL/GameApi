define([
    'jquery',
    'backbone'
], function($, Backbone){
    var UploadUserConcept = Backbone.Model.extend({
        urlRoot:baseRestApiUrl + '/MyGubbiApi/userupload/concept/'
    });
    return UploadUserConcept;
});
