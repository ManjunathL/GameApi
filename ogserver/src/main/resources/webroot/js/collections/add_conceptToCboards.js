define([
  'jquery',
  'underscore',
  'backbone',
  'models/add_conceptToCboard'
], function($, _, Backbone, AddConceptToCboard){
    var AddConceptToCboards = Backbone.Collection.extend({
        model: AddConceptToCboard,
        url: baseRestApiUrl + 'MyGubbiApi/conceptboardConcept/add/',
        getaddConceptToCBoard: function(conceptboardId,conceptCode, options) {
            var urllnk = baseRestApiUrl + 'MyGubbiApi/conceptboardConcept/add/';
            this.url = urllnk + conceptboardId + '/' + conceptCode;
            return this.fetch(options);
        },
        initialize: function(models) {
          _.each(models, function (add_conceptToCboard){
            add_conceptToCboard = new AddConceptToCboard(add_conceptToCboard);
          });
        }
    });
  return AddConceptToCboards;
});