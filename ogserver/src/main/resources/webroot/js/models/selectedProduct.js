define([
  'jquery',
  'backbone'
], function($, Backbone){
  var SelectedProduct = Backbone.Model.extend({
    calculatePrice: function() {

        //this.model.originalProduct
    }
  });
  return SelectedProduct;
});
