/**
 * Created by mygubbi on 26/1/16.
 */
define([
    'jquery',
    'underscore',
    'backbone',
    'models/story',
    'text!templates/story/full_story.html'
], function($, _, Backbone, Story, fullStoryTemplate) {
    var FullStoryView = Backbone.View.extend({
        el: '.page',
        story: new Story(),
        render: function() {

            var that = this;

            _.map(this.story, function ( res ) {
                if ( res.id == that.model.id ){
                    story_obj = res;
                }else{
                    return false;
                }
            });

            var response = this.story.get(that.model.id);
            console.log('id : '+ that.model.id);
            if(response){
                that.fetchStoryAndRender();
            }else {
                console.log("couldn't fetch story data");
            }

        },
        fetchStoryAndRender: function() {
            var that = this;
            var story = that.story;

            $(this.el).html(_.template(fullStoryTemplate)({'stories': story.toJSON()}));
            //console.log('stories : ' + JSON.stringify(stories));
        }
    });
    return FullStoryView;
});