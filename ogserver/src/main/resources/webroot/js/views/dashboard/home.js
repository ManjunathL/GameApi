define([
  'jquery',
  'lodash',
  'backbone',
  'bootstrap',
  'text!templates/dashboard/home.html'
], function($, _, Backbone, Bootstrap, homePageTemplate){
  var HomePage = Backbone.View.extend({
    el: '.page',
    initialize: function() {
        this.listenTo(Backbone);
        _.bindAll(this, 'render');
    },
    render: function() {
        var that = this;
        $(this.el).html(_.template(homePageTemplate));
        $(document).on("click", "a[href^='/']", function(event) {
            href = $(event.currentTarget).attr('href');
            if (!event.altKey && !event.ctrlKey && !event.metaKey && !event.shiftKey) {
                event.preventDefault();
    //                        url = href.replace("/^\//", '').replace('\#\!\/', '');
                url = href;
                window.App.router.navigate(url, {
                    trigger: true
                });
            }
        });
    }
  });
  return HomePage;
});
