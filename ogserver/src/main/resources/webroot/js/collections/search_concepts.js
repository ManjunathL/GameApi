define([
  'jquery',
  'underscore',
  'backbone',
  'models/search_concept'
], function($, _, Backbone, SearchConcept){
    var SearchConcepts = Backbone.Collection.extend({
        model: SearchConcept,
        url: baseRestApiUrl + 'MyGubbiApi/search/concept/',
        getSearchConcept: function(userId, options) {
            var urllnk = baseRestApiUrl + 'MyGubbiApi/search/concept/';
            this.url = urllnk + userId;
            return this.fetch(options);
        },
        initialize: function(models) {
          _.each(models, function (search_concept){
            search_concept = new SearchConcept(search_concept);
          });
        },
        filterBySearchTags:function (searchconceptlist,selectedSearchTag){
            var that = this;
            return _.map(searchconceptlist.filter(function(concept){
                return that.searchconceptWithTags(concept, selectedSearchTag);
            }), function (concept) {return concept });
        },
        searchconceptWithTags: function (conceptObj, selectedSearchTag) {
            var tagArrObj = conceptObj.tags;
            var tagArr = tagArrObj.split(",");
            for (var i=0; i < tagArr.length; i++) {
                if (tagArr[i] == selectedSearchTag)
                    return true;
            }
        }
    });
  return SearchConcepts;
});