/**
 * Created by Smruti on 10/03/2017.
 */
define([
    'jquery',
    'underscore',
    'backbone',
    'analytics',
    'text!templates/partners/builders.html',
    'text!templates/partners/speakers.html'
], function($, _, Backbone, Analytics, BuildersTemplate, SpeakersTemplate) {
    var BuildersView = Backbone.View.extend({
        el: '#page-top',
        initialize: function() {
            Analytics.apply(Analytics.TYPE_GENERAL);
        },
        render: function() {

            var buildersTemp = _.template(BuildersTemplate);

            $(this.el).html(buildersTemp);
            $('head').html('');
            this.ready();
        },
        ready: function(){
            var speakersTemp = _.template(SpeakersTemplate);
            $("#speakers").html(speakersTemp);
        }
    });
    return BuildersView;
});