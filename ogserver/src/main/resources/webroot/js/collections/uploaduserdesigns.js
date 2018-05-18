define([
  'jquery',
  'underscore',
  'backbone',
  'models/uploaduserdesign'
], function($, _, Backbone, UploadUserDesign){
    var UploadUserDesigns = Backbone.Collection.extend({
        model: UploadUserDesign,
        url: baseRestApiUrl +  'MyGubbiApi/userupload/design',
        uploadUserDesignsImage: function(userId,conceptboardId, options) {
            var urllnk = baseRestApiUrl + '/MyGubbiApi/userupload/design/';
            this.url = urllnk + userId + '/' + conceptboardId;
            return this.fetch(options);
        },
        initialize: function(models) {
          _.each(models, function (uploaduserdesign){
            uploaduserdesign = new ConceptBoard(uploaduserdesign);
          });
        }
    });
  return UploadUserDesigns;
});