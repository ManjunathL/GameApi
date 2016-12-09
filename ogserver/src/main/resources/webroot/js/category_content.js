define([
    'jquery',
    'underscore',
    'models/seoFilterMaster',
    'text!templates/category/content_seo.html'
], function($, _,SEOFilterMaster, ContentSeoTemplate) {
    return {
        el: '.category-content',
        seoFilterMaster: new SEOFilterMaster(),

        apply: function(category,subCategory,location) {

        var loc = location;
        if((loc == null) || (loc == "") ){
            loc = "website";
        }
         /*console.log(" ----------------Category Content----------------- ");
         console.log(category+"  ================ "+subCategory+" ============ "+loc);*/
        var that = this;
        that.seoFilterMaster.fetch({
          data: {
              "category": category,
              "subcategory": subCategory,
              "location": loc
          },

            success: function(response) {
                console.log("SEO filterMaster fetch successfully- ");
                console.log(response);
                console.log(response.get(0).content);

                var ContentSeo = _.template(ContentSeoTemplate);
                $(".category-content").html(ContentSeo({
                      "description": response.get(0).content
                  }));

            },
            error: function(model, response, options) {
                console.log("error from SEO filterMaster fetch - " + response);
            }
         });


        }
    };
});