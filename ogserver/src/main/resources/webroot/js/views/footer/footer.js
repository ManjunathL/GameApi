define([
  'jquery',
  'lodash',
  'backbone',
  'text!templates/footer/footer.html'
], function($, _, Backbone, footerTemplate){
  var FooterView = Backbone.View.extend({
    el: '.footer',
    render: function () {
      $(this.el).html(footerTemplate);
    }
  });
  return FooterView;
});
