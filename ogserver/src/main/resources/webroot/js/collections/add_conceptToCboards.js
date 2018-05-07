define([
  'jquery',
  'underscore',
  'backbone',
  'models/add_conceptToCboard'
], function($, _, Backbone, AddConceptToCboard){
    var AddConceptToCboards = Backbone.Collection.extend({
        model: AddConceptToCboard,
        url: baseRestApiUrl + 'MyGubbiApi/conceptboardConcept/web/add/',
        getaddConceptToCBoard: function(conceptboardId,conceptCode,spaceElementCode,options) {
            var urllnk = baseRestApiUrl + 'MyGubbiApi/conceptboardConcept/web/add';
            this.url = urllnk + "?conceptboardId=" + conceptboardId + "&conceptCode=" + conceptCode + "&spaceElementCode=" + spaceElementCode;
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

/*
http://45.112.138.146:8080/MyGubbiApi/conceptboardConcept/web/add?conceptboardId=3595&conceptCode=C-00129&spaceElementCode=SE-BASECABINET
"http://45.112.138.146:8080/MyGubbiApi/conceptboardConcept/web/add?conceptboardId=3727&conceptCode=C-00148&spaceElementCode=SE-BASECABINET"*/
