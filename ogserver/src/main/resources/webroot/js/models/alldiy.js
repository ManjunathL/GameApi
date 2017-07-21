define([
    'jquery',
    'backbone'
], function($, Backbone){
    var Diy = Backbone.Model.extend({
        urlRoot:restBase + '/api/diy/allDiy',
        defaults: {
            id: '',
            tags:''
        }
    });
    return Diy;
});
