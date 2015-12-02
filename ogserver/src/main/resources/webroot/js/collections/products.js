define([
    'jquery',
    'backbone',
    'models/product'
], function($, Backbone, Product) {
    var Products = Backbone.Collection.extend({
        model: Product,
        url: restBase + '/api/products',
        initialize: function() {
            this.sortKey = 'popularity';
            this.sortDir = 'asc';
        },
        comparator: function(a, b) {
            
            var sortMultiplier = 1;

            if (this.sortDir === 'desc') {
        		sortMultiplier = -1;
            }

            var aVal = a.get(this.sortKey);
            var bVal = b.get(this.sortKey);

    		switch (this.sort_key) {
    			case "date":
    			    aVal = new Date(aVal);
    			    bVal = new Date(bVal);
    			    break;
				case "price":
                    aVal = a.get('mf').sort(function(x,y){return x.aggregatedPrice-y.aggregatedPrice;});
				    bVal = b.get('mf').sort(function(x,y){return x.aggregatedPrice-y.aggregatedPrice;});
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
		}
    });
    return Products;
});
