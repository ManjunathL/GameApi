define([
  'jquery',
  'underscore',
  'backbone',
  'models/story'
], function($, _, Backbone, Story){
    var Story = Backbone.Collection.extend({
        model: Story,
        url: restBase + '/api/blogs',
        initialize: function(models) {
          _.each(models, function (story){
            story = new Story(story);
          });
        }
    });
  return Story;
});