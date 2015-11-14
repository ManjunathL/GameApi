define([
  'jquery',
  'underscore',
  'backbone',
  'bootstrap',
  'text!templates/header/menu.html',
  'collections/categories'
], function($, _, Backbone, Bootstrap, headerMenuTemplate, Categories){
  var HeaderMenuView = Backbone.View.extend({
    el: '.main-menu-container',
    render: function () {
        that = this;
        var categories = new Categories();
        categories.fetch({
            success: function() {
                var compiledTemplate = _.template(headerMenuTemplate);
                $(that.el).html(compiledTemplate({"categories": categories.models[0].toJSON()}));
                $('a[href="' + window.location.hash + '"]').addClass('active');
            },
            error: function() {
                console.log("error in fetching categories data");
            }
        });
    },
    events: {
      'click a': 'highlightMenuItem'
    },
    highlightMenuItem: function (ev) {
      $('.active').removeClass('active');
      $(ev.currentTarget).addClass('active');
    }
  })
  return HeaderMenuView;
});
