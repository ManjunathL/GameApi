define([
  'jquery',
  'backbone',
  '/js/models/appliance.js',
  'underscore'
], function($, Backbone, Appliance, _){
  var Appliances = Backbone.Collection.extend({
    model: Appliance,
    url: restBase + '/api/appliances',
    getApplianceType: function () {
     var result = new Array();
     this.each(function (model) {
       var applianceType = model.get('type');
       if (applianceType) {
           result.push(applianceType);
       }
     });
     return result;
    }
    });
  return Appliances;
});