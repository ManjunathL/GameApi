define([
  'jquery',
  'backbone'
], function($, Backbone){
  var AutoSuggestProduct = Backbone.Model.extend({
      defaults: {
          id: ''
      }
  });
  return AutoSuggestProduct;
});
