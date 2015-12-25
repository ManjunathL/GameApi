define([
  'jquery',
  'underscore',
  'backbone',
  'bootstrap',
  'text!templates/consult/consult.html'
], function($, _, Backbone, Bootstrap, consultTemplate){
  var ConsultView = Backbone.View.extend({
    el: '.page',
    render: function () {
      $(this.el).html(consultTemplate);
    }
  });
  return ConsultView;
});
