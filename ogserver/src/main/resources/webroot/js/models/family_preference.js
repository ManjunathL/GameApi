define([
    'jquery',
    'backbone'
], function($, Backbone){
    var FamilyPreference = Backbone.Model.extend({
      urlRoot:baseRestApiUrl + 'MyGubbiApi/familypersona/get/familymembers/list/'
    });
    return FamilyPreference;
});
