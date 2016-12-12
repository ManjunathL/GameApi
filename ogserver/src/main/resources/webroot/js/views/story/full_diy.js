/**
 * Created by mygubbi on 08/12/16.
 */
define([
    'jquery',
    'underscore',
    'backbone',
    'collections/diys',
    'models/diy',
    'analytics',
    'text!templates/story/full_diy.html',
    'views/story/full_story_helper'
], function($, _, Backbone, Diys, Diy, Analytics, fullDiyTemplate, FullStoryHelper) {
    var FullDiyView = Backbone.View.extend({
        el: '.page',
        diy: new Diy(),
        diys: null,
        initialize: function() {
            Analytics.apply(Analytics.TYPE_GENERAL);
            this.diys = new Diys();
        },
        render: function() {

            var that = this;

            var diy_name = that.model.name;

           console.log(diy_name);

            this.diys.fetch({
                success: function() {
                    that.fetchDiyAndRender(diy_name);
                },
                error: function(model, response, options) {
                    console.log("couldn't fetch diy data - " + response);
                }
            });

        },
        fetchDiyAndRender: function(name) {

            var that = this;
            var diys = that.diys;
            diys = diys.toJSON();
            var full_diy = {};

            delete diys.id;

            _.find(diys, function(item, index) {
                if (item.diy_heading == name) {
                    full_diy = item;
                }
            });

            diys = _(diys).sortBy(function(diy) {
                return Date.parse(diy.date_of_publish);
            }).reverse();

            var rec_diys = [];
            $.each(diys.slice(0,3), function(i, data) {
                rec_diys.push(data);
            });

            console.log("rec_diys");
            console.log(rec_diys);


            var fullTemp = _.template(fullDiyTemplate);

            $(this.el).html(fullTemp({
                'rec_diys':rec_diys,
                'full_diy': full_diy
            }));

            FullStoryHelper.ready(that);

        }
    });
    return FullDiyView;
});