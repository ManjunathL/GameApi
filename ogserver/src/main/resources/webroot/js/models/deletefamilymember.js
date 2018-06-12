define([
    'jquery',
    'backbone'
], function($, Backbone){
    var DeleteFamilyMember = Backbone.Model.extend({
      urlRoot:baseRestApiUrl + 'MyGubbiApi/familypersona/delete/member/'
    });
    return DeleteFamilyMember;
});
