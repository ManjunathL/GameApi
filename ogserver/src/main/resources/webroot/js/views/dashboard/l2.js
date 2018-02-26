define([
  'jquery',
  'lodash',
  'backbone',
  'bootstrap',
  'imagesloaded',
  'wookmark',
  'text!templates/dashboard/l2.html',
  'text!templates/dashboard/kitchen.html',
  'text!templates/dashboard/living.html',
  'collections/designs',
  'collections/spacetypes',
  'collections/concepts'
], function($, _, Backbone, Bootstrap, ImagesLoaded, wookmark, dashboardPageTemplate, kitchenPageTemplate, livingPageTemplate, Designs, SpaceTypes, Concepts){
  var DashboardPage = Backbone.View.extend({
    el: '.page',
    concepts: null,
    designs: null,
    spacetypes: null,
    initialize: function() {
        this.concepts = new Concepts();
        this.designs = new Designs();
        this.spacetypes = new SpaceTypes();
        this.listenTo(Backbone);
        _.bindAll(this, 'render','fetchConceptsAndRender');
    },
    render: function () {
        var that = this;

        var getConceptsPromise = that.getConcepts();
        var getDesignsPromise = that.getDesigns();

        Promise.all([getConceptsPromise, getDesignsPromise]).then(function() {
            console.log("@@@@@@@@@@@@@ In side Promise @@@@@@@@@@@@@@@@@@");
            that.fetchConceptsAndRender();
        });
    },
    getConcepts: function(){
        var that = this;
        return new Promise(function(resolve, reject) {
             if (!that.concepts.get('id')) {
                that.concepts.fetch({
                  success: function(response) {
                    resolve();
                  },
                  error: function(model, response, options) {
                      reject();
                  }
                });
             }else{
                resolve();
             }
        });
    },
    getDesigns: function(){
        var that = this;
        return new Promise(function(resolve, reject) {
             if (!that.designs.get('id')) {
                that.designs.fetch({
                  success: function(response) {
                    resolve();
                  },
                  error: function(model, response, options) {
                      reject();
                  }
                });
            } else{
                resolve();
            }
        });
    },
    fetchConceptsAndRender: function() {
        var that = this;
        var designs = that.designs;
        var concepts = that.concepts;
        $(this.el).html(_.template(dashboardPageTemplate)({
            'designdetail':designs.toJSON(),
            'conceptdetails':concepts.toJSON()
        }));
    },
    events: {
        "click .selTabs": "getSelectedConcepts"
    },
    getSelectedConcepts: function(evt){
        var currentTarget = $(evt.currentTarget);
        var currTab = currentTarget.data('element');

        console.log(" Current Tab");
        console.log(currTab);

        var that = this;
        that.spacetypes.fetch({
             data: {
                 spaceType: currTab
             },
            success: function(response) {
                console.log("Successfully fetch Kitchen Concepts - ");
                if(currTab == 'Living'){
                    $('#'+currTab).html(_.template(livingPageTemplate)({
                        "conceptdetails": response.toJSON()
                    }));
                }else{
                    $('#'+currTab).html(_.template(kitchenPageTemplate)({
                        "conceptdetails": response.toJSON()
                    }));
                }
            },
            error: function(model, response, options) {
                console.log("error from Kitchen Concepts fetch - " + response);
            }
        });
        return;
    }
  });
  return DashboardPage;
});
