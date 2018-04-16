define([
    'jquery',
    'backbone'
], function($, Backbone){
    var Everything = Backbone.Model.extend({
        urlRoot: baseApiUrl + 'conceptMaster/get/concept-list/'
    });
    return Everything;
});
