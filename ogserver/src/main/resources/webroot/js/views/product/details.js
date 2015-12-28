define([
    'jquery',
    'underscore',
    'backbone',
    'bootstrap',
    'sly',
    'jqueryeasing',
    'text!templates/product/details.html',
    'models/product'
], function($, _, Backbone, Bootstrap, Sly, JqueryEasing, productPageTemplate, ProductModel) {
    var ProductPage = Backbone.View.extend({
        el: '.page',
        product: new ProductModel(),
        render: function() {
            var that = this;
            if (!this.product.get('id')) {
                this.product.set('id', this.model.id);
                this.product.fetch({
                    success: function (response) {
                        that.respond();
                    }
                });
            } else {
                this.respond();
            }
        },
        respond: function() {
            var that = this;
            var compiledTemplate = _.template(productPageTemplate);
            $(this.el).html(compiledTemplate({
                "product": that.product.toJSON(),
                "materials": _.uniq(_.pluck(that.product.get('mf'), 'material')),
                "finishes": _.uniq(_.pluck(that.product.get('mf'), 'finish')),
                "applianceTypes": _.uniq(_.pluck(that.product.get('appliances'), 'type'))
            }));
        },
        material: function(mf) { return mf.material; },
        finish: function(mf) { return mf.finish; }
    });
    return ProductPage;
});
