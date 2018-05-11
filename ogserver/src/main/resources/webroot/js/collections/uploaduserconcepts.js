define([
  'jquery',
  'underscore',
  'backbone',
  'models/uploaduserconcept'
], function($, _, Backbone, UploadUserConcept){
    var UploadUserConcepts = Backbone.Collection.extend({
        model: UploadUserConcept,
        url: baseRestApiUrl +  'MyGubbiApi/upload/usercreatedconcept',
        uploadUserConceptBoard: function(userId,conceptboardId, options) {
            var urllnk = baseRestApiUrl + '/MyGubbiApi/upload/usercreatedconcept/';
            this.url = urllnk + userId + '/' + conceptboardId;
            return this.fetch(options);
        },
        initialize: function(models) {
          _.each(models, function (uploaduserconcept){
            uploaduserconcept = new ConceptBoard(uploaduserconcept);
          });
        }
    });
  return UploadUserConcepts;
});