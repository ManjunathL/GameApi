define([
  'jquery',
  'lodash',
  'backbone',
  'bootstrap',
  'text!templates/dashboard/listing_details.html',
  'collections/design_details',
  'collections/tagproducts'
], function($, _, Backbone, Bootstrap, dashboardPageTemplate, DesignDetails, Tagproducts){
  var DashboardPage = Backbone.View.extend({
    el: '.page',
    design_details: null,
    tag_products: null,
    initialize: function() {
        this.design_details = new DesignDetails();
        this.tag_products = new Tagproducts();
        this.listenTo(Backbone);
        _.bindAll(this, 'render','fetchDesignsAndRender');
    },
    render: function () {
        var designId = this.model.id;
        var that = this;
        console.log("I m herer ");
        var getDesignsPromise = that.getDesigns(designId);
        var getTagProductsPromise = that.getTagProducts(designId);

        Promise.all([getDesignsPromise, getTagProductsPromise]).then(function() {
            console.log("@@@@@@@@@@@@@ In side Promise @@@@@@@@@@@@@@@@@@");
            that.fetchDesignsAndRender();
        });
    },
    getDesigns: function(designId) {
     console.log("###########################");
        var that = this;
        return new Promise(function(resolve, reject) {
            if (!that.design_details.get('id')) {
                that.design_details.fetch({
                    data:{id: designId},
                    success: function() {
                        console.log("design details fetch successfully- ");
                        resolve();
                    },
                    error: function(model, response, options) {
                        console.log("error from design details fetch - " + response);
                        reject();
                    }
                });
            } else{
                resolve();
            }
        });
    },
    getTagProducts: function(designId) {
    console.log("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        var that = this;
        return new Promise(function(resolve, reject) {
            if (!that.tag_products.get('id')) {
                that.tag_products.fetch({
                    data:{id: designId},
                    success: function() {
                        console.log("tag products fetch successfully- ");
                        resolve();
                    },
                    error: function(model, response, options) {
                        console.log("error from tag products fetch - " + response);
                        reject();
                    }
                });
            } else{
                resolve();
            }
        });
    },
    fetchDesignsAndRender: function() {
        var that = this;
        var design_details = that.design_details;
        var tag_products = that.tag_products;
        console.log("@@@@@@@@@@@@@@ tag_products @@@@@@@@@@@@@@@@@");

         $(this.el).html(_.template(dashboardPageTemplate)({
            'designsdetails':design_details.toJSON(),
            'tagdetails':tag_products.toJSON()
        }));
    }
  });
  return DashboardPage;
});
