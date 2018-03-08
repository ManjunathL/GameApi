define([
  'jquery',
  'lodash',
  'backbone',
  'bootstrap',
   'text!templates/my_account/user_profile.html'
], function($, _, Backbone, Bootstrap, UserProfileTemplate){
  var UserProfilePage = Backbone.View.extend({
    el: '.page',
    initialize: function() {
        this.listenTo(Backbone);
        _.bindAll(this, 'render');
    },
    render: function () {
       var that = this;
       $(this.el).html(_.template(UserProfileTemplate));
    }
  });
  return UserProfilePage;
});
