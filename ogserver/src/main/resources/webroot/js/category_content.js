define([
    'jquery',
    'underscore',
    'collections/seoFilterMasters',
    'models/seoFilterMaster',
    'models/seofilter',
    'collections/seoproducts',
    'text!templates/category/content_seo.html'
], function($, _,SeoFilterMasters ,SEOFilterMaster, SEOFilter, SEOProduct, ContentSeoTemplate) {
    return {
        el: '.category-content',
        seoFilterMaster: new SEOFilterMaster(),
        seoFilterMasters: new SeoFilterMasters(),
        seoFilter: new SEOFilter(),
        seoProducts: new SEOProduct(),

        apply: function(category,subCategory,location,selPage) {
            var loc = location;
            if((loc == null) || (loc == "") ){
                loc = "website";
            }
            var that = this;

            var vcategory = '';
            var dcategory = '';

            if((typeof(subCategory) !== 'undefined') && (subCategory != '')){
                vcategory = category;
                dcategory = subCategory;
            }else if(category == "kitchen"){
                vcategory = "Kitchen";
                dcategory = vcategory;
            }else if(category == "bedroom"){
                vcategory = "Wardrobe";
                dcategory = vcategory;
            }else if(category == "living & dining"){
                vcategory = "Storage Solutions";
                dcategory = vcategory;
            }

            that.seoFilter.set({
             'selcategory':vcategory
            }, {
              silent: true
            });

            that.seoFilter.set({
             'selTitle':dcategory
            }, {
              silent: true
            });

            that.seoFilter.set({
             'selPage':selPage
            }, {
              silent: true
            });

            var getseoFilterMasterPromise = that.getseoFilterMaster(category,subCategory,loc);
            var getseoProductsPromise = that.getseoProducts(category);
            var getseoProductsPromise1 = that.getseoProducts1(category);

            Promise.all([getseoFilterMasterPromise,getseoProductsPromise,getseoProductsPromise1]).then(function() {
                 that.showContent();
            });
        },
        getseoFilterMaster: function(category,subCategory,loc){
            var that = this;
            return new Promise(function(resolve, reject) {
                that.seoFilterMasters.fetch({
                    data: {
                      "category": category,
                      "subcategory": subCategory,
                      "location": loc
                    },
                    success: function(response) {
                        console.log("+++++++++++SEO data++++++++++++++");
                        var res = response.toJSON();
                        console.log(response);
                        that.seoFilter.set({
                         'selectedSubCategories':res
                        }, {
                          silent: true
                        });
                        that.seoFilter.set({
                            'seoDesc':res[0].content
                        }, {
                             silent: true
                         });
                         that.seoFilter.set({
                            'H_tags':res[0].H_tags
                        }, {
                             silent: true
                         });
                         that.seoFilter.set({
                            'other_catg_url':res[0].other_catg_url
                        }, {
                             silent: true
                         });
                        resolve();
                    },
                    error: function(err){
                        console.log("unable to fetch seo data- ");
                        reject();
                    }
                });
            });
        },
        getseoProducts: function(category){
            var that = this;
            var selectedCategoryName = category;
            var othcat = "";
            console.log("+++++++++++++++category+++++++++++++++++");
            console.log(category);
            if(category === "kitchen"){
                othcat = "Bedroom";
            }else if(category === "bedroom"){
                othcat = "Kitchen";
            }else if(category === "living & dining"){
                othcat = "Kitchen";
            }else if(category === "livingndining"){
                othcat = "Kitchen";
            }
            console.log("+++++++++++++++++++++++");
            console.log(othcat);
            return new Promise(function(resolve, reject) {
                 that.seoProducts.fetch({
                    data: {
                      "category": othcat
                    },
                     success: function(response) {
                         console.log("+++++++++++++++SEO Products+++++++++++++++");
                         console.log(response);
                         that.seoFilter.set({
                             'selectedProducts':response.toJSON()
                         }, {
                              silent: true
                          });
                         resolve();
                     },
                     error: function(err){
                         console.log("unable to fetch categories data- ");
                         reject();
                     }
                 });
            });
        },
        getseoProducts1: function(category){
            var that = this;
            var selectedCategoryName = category;
            var othcat = "";
            console.log(category);
            if(category === "kitchen"){
                othcat = "Living & Dining";
            }else if(category === "bedroom"){
                othcat = "Living & Dining";
            }else if(category === "living & dining"){
                othcat = "Bedroom";
            }else if(category === "livingndining"){
                othcat = "Bedroom";
            }
            console.log("+++++++++++++++++++++++");
            console.log(othcat);
            return new Promise(function(resolve, reject) {
                 that.seoProducts.fetch({
                    data: {
                      "category": othcat,
                    },
                     success: function(response) {
                         console.log("+++++++++++++++SEO Products+++++++++++++++");
                         console.log(response);
                         that.seoFilter.set({
                             'selectedProducts1':response.toJSON()
                         }, {
                              silent: true
                          });
                         resolve();
                     },
                     error: function(err){
                         console.log("unable to fetch categories data- ");
                         reject();
                     }
                 });
            });
        },
        showContent: function(){
            var that = this;
            if(typeof(that.seoFilter.get('seoDesc')) !== 'undefined'){
                var ContentSeo = _.template(ContentSeoTemplate);
                $(".category-content").html(ContentSeo({
                      "selPage": that.seoFilter.get('selPage'),
                      "selectedcategory": that.seoFilter.get('selcategory'),
                      "selTitle": that.seoFilter.get('selTitle'),
                      "description": that.seoFilter.get('seoDesc'),
                      "subcategoryList": that.seoFilter.get('selectedSubCategories'),
                      "other_catg_url": that.seoFilter.get('other_catg_url'),
                      "H_tags": that.seoFilter.get('H_tags'),
                      "selectedProducts": that.seoFilter.get('selectedProducts'),
                      "selectedProducts1": that.seoFilter.get('selectedProducts1')
                 }));
             }
        }
    };
});