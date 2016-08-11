define([
  'jquery',
  'underscore',
  'backbone',
  'text!/templates/layout.html'
], function($, _, Backbone, layoutTemplate){
    var AppView = Backbone.View.extend({
        el: '.container-fluid',
        render: function () {
          var that = this;
          $(this.el).html(layoutTemplate);
          require(['views/header/new-menu'], function (HeaderMenuView) {
            var headerMenuView = new HeaderMenuView();
            headerMenuView.render();
          });
          require(['views/footer/new-footer'], function (FooterView) {
            var footerView = new FooterView({appView: that});
            footerView.render();
          });

        }
  });
  return AppView;
});
