define([
    'jquery',
    'underscore',
    'backbone',
    'text!/templates/footer/mygubbidiff.html'
], function($, _, Backbone, mygubbidiffTemplate) {
    var MyGubbiDiffView = Backbone.View.extend({
        el: '.page',
        render: function() {
            $(this.el).html(mygubbidiffTemplate);
        }
    });
    return MyGubbiDiffView;
});