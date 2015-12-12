define([
  'jquery',
  'backbone',
  'models/user'
], function($, Backbone, User){
    var Users = Backbone.Collection.extend({
        model: User,
        url: restBase + '/users'
  });
  return Users;
});
