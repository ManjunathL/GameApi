    define([
  'jquery',
  'underscore',
  'backbone',
  'text!templates/layout.html',
  'views/header/menu',
  'views/footer/footer'
], function($, _, Backbone, layoutTemplate, HeaderMenuView, FooterView){
    var AppView = Backbone.View.extend({
        el: '.dw-container-fluid',
        render: function () {
          var that = this;
          console.log("@@@@@@@@@@@@@@ layoutTemplate @@@@@@@@@@@@@@");
          console.log(window.location.host);
          console.log(window.location.href);
          $(this.el).html(layoutTemplate);

          mainURLlnk = "http://"+window.location.host+"/";
          currURLlnk = window.location.href;
          //if(mainURLlnk !== currURLlnk){
            var headerMenuView = new HeaderMenuView();
              headerMenuView.render();
              var footerView = new FooterView({appView: that});
              footerView.render();

          //}

        }
  });
  return AppView;
});
