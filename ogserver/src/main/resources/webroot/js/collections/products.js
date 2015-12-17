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
		filterByPrice:function (minPrice,maxPrice,subCategoryIds){
		    var that = this;
		    if(!minPrice && !maxPrice){
                return this.toJSON();
		    }else{
		        return _.map(this.filter(function(product){

                                return product.get('defaultPrice') >= minPrice && product.get('defaultPrice') <= maxPrice && that.productInSubCategs(product, subCategoryIds);
                            }), function (product) {return product.toJSON();});
		    }

		},
		productInSubCategs: function (product, subCategoryIds) {
		    for (var i=0; i < subCategoryIds.length; i++) {
		        if (product.get("subCategId") == subCategoryIds[i])
		            return true;
		    }

		}
    });
    return Products;
});
