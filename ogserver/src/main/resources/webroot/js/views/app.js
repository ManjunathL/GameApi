define([
  'jquery',
  'jqueryui',
  'underscore',
  'cloudinary',
  'backbone',
  'vm',
  'text!templates/layout.html'
], function($, jqueryui, _, cloudinary, Backbone, Vm, layoutTemplate){
    var AppView = Backbone.View.extend({
        el: '.container-fluid',
        initialize: function () {

          var NestedView2 = Backbone.View.extend({});
          var NestedView1 = Backbone.View.extend({
            initialize: function () {
              var nestedView2 = Vm.create(this, 'Nested View 2', NestedView2);
            }
          });
          var nestedView1 = Vm.create(this, 'Nested View 1', NestedView1);

        },
        render: function () {
          var that = this;
          $(this.el).html(layoutTemplate);
          require(['views/header/menu'], function (HeaderMenuView) {
            var headerMenuView = Vm.create(that, 'HeaderMenuView', HeaderMenuView);
            headerMenuView.render();
          });
          require(['views/footer/footer'], function (FooterView) {
            var footerView = Vm.create(that, 'FooterView', FooterView, {appView: that});
            footerView.render();
          });

        }
  });
  return AppView;
});
