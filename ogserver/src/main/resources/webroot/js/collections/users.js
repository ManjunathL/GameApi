/**
 * Created by og on 4/12/15.
 */
define([
    'jquery',
    'backbone',
    'models/user'
], function($, Backbone, User){
    var Users = Backbone.Collection.extend({
        model: User,
        url: restBase + '/api/shortlist.short',
        getUserShortlist: function (id) {
            return this.find(function (model) {
                if (model.get('id') === id) {
                    return model;
                }
            });
        }
    });
    return Users;
});
