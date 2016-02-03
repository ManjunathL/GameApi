define([
  'jquery',
  'backbone',
  '/js/models/user.js'
], function($, Backbone, User){
    var Users = Backbone.Collection.extend({
        model: User,
        url: restBase + '/users'
  });
  return Users;
});
