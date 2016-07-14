define([
  'jquery',
  'underscore',
  'backbone',
  'text!templates/layout.html',
  'views/header/menu',
  'views/footer/footer'
], function($, _, Backbone, layoutTemplate, HeaderMenuView, FooterView){
    var AppView = Backbone.View.extend({
        el: '.container-fluid',
        render: function () {
          var that = this;
          $(this.el).html(layoutTemplate);
          var headerMenuView = new HeaderMenuView();
          headerMenuView.render();
          var footerView = new FooterView({appView: that});
          footerView.render();
        }
  });
  return AppView;
});
