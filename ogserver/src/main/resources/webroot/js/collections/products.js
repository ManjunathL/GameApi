define([
    'jquery',
    'backbone',
    'models/product',
    'underscore'
], function($, Backbone, Product, _) {
    var Products = Backbone.Collection.extend({
        model: Product,
        url: restBase + '/api/products',
        initialize: function() {
            this.sortKey = 'relevance';
            this.sortDir = 'desc';
        },
        comparator: function(a, b) {
            
            var sortMultiplier = 1;

            if (this.sortDir === 'desc') {
        		sortMultiplier = -1;
            }

            var aVal = a.get(this.sortKey);
            var bVal = b.get(this.sortKey);

    		switch (this.sortKey) {
    			case "date":
    			    aVal = new Date(aVal);
    			    bVal = new Date(bVal);
    			    break;
				case "defaultPrice":
                   // aVal = a.get('mf').sort(function(x,y){return x.aggregatedPrice-y.aggregatedPrice;});
				   // bVal = b.get('mf').sort(function(x,y){return x.aggregatedPrice-y.aggregatedPrice;});
                    break;
    			case "popularity": break;
				case "relevance": break;
				default:
				    return 0;
    		}

    		return (aVal > bVal ? 1 : aVal < bVal ? -1 : 0) * sortMultiplier;
        },
        sortBy: function(sortAttribute, sortDir) {
			this.sortKey = sortAttribute;
			this.sortDir = sortDir;
    		this.sort();
		},
		getProduct: function (id) {
		    return this.find(function(product){ return product.get('id') === id; });
		},
		filterByPrice:function (minPrice,maxPrice,filterIds){
		    var that = this;
		    if(!minPrice && !maxPrice){
                return this.toJSON();
		    }else{
		        return _.map(this.filter(function(product){

                                return product.get('defaultPrice') >= minPrice && product.get('defaultPrice') <= maxPrice && that.productInFilters(product, filterIds);
                            }), function (product) {return product.toJSON();});
		    }

		},
		productInFilters: function (product, filterIds) {
		    for (var i=0; i < filterIds.length; i++) {
		        if ((product.get("subCategId") == filterIds[i]) || (product.get("price_range_id") == filterIds[i]) || (product.get("style_id") == filterIds[i]))
		            return true;
		    }


		}
    });
    return Products;
});
