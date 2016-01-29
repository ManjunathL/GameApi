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
		    return this.find(function(product){ return product.get('productId') === id; });
		},
		filterByPrice:function (minPrice,maxPrice){
		    var that = this;
		    if(!minPrice && !maxPrice){
                return this.toJSON();
		    }else{
		        return _.map(this.filter(function(product){

					return product.get('defaultPrice') >= minPrice &&
					product.get('defaultPrice') <= maxPrice;
				}), function (product) {return product.toJSON();});
		    }

		},

		filterBySubcat:function (selectedSubcatIds){
			var that = this;
			return _.map(this.filter(function(product){
				return that.productInSubCategs(product, selectedSubcatIds);
			}), function (product) {return product.toJSON();});

		},
			filterByPriceRange:function (filteredProducts,selectedPriceRangeIds){
				var that = this;
				return _.map(filteredProducts.filter(function(product){
					return that.productInPriceRanges(product, selectedPriceRangeIds);
				}), function (product) {return product;});
		},
			filterByStyle:function (filteredProducts,selectedStyleIds){
				var that = this;
				return _.map(filteredProducts.filter(function(product){
					return that.productInStyles(product, selectedStyleIds);
				}), function (product) {return product;});
		},

		productInSubCategs: function (product, subCategoryIds) {
			for (var i=0; i < subCategoryIds.length; i++) {
				if (product.get("subcategory") == subCategoryIds[i])
					return true;
			}

		},
		productInPriceRanges: function (product, priceRangeIds) {
			for (var i=0; i < priceRangeIds.length; i++) {
				if (product.priceId == priceRangeIds[i])
					return true;
			}

		},
		productInStyles: function (product, styleIds) {
			for (var i=0; i < styleIds.length; i++) {
				if (product.styleId == styleIds[i])
					return true;
			}

		},
        getRelatedProducts: function (subcatId) {
         var that = this;
         return _.map(this.filter(function(product){
             return product.get("subcategory") == subcatId;
         }), function (product) {return product.toJSON();});
        }
    });
    return Products;
});
