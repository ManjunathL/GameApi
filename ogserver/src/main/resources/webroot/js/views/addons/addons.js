/**
 * Created by mygubbi on 14/09/16.
 */
define([
    'jquery',
    'underscore',
    'backbone',
    'collections/addons',
    'models/filterAddon',
    'text!templates/addons/addons.html',
    'text!templates/addons/addons_category.html',
    'text!templates/addons/product_types.html',
    'text!templates/addons/product_subtypes.html',
    'text!templates/addons/brands.html',
    'text!templates/addons/addons_filter.html',
    'cloudinary_jquery',
    'slyutil',
    'mgfirebase',
    'consultutil',
    'analytics'
], function($, _, Backbone, Addons, FilterAddon, AddonsPageTemplate, CategoriesTemplate, ProductTypeTemplate, ProductSubtypeTemplate, BrandTemplate,AddonsFilterTemplate, CloudinaryJquery, SlyUtil, MGF, ConsultUtil, Analytics) {
    var AddonsPageVIew = Backbone.View.extend({
        el: '.page',
        addons: null,
        FilterAddon: null,
        ref: MGF.rootRef,
        refAuth: MGF.refAuth,
        renderWithAddons: function(addonsdtls) {
            var resaddons = this.addons.getCategoriesList(addonsdtls);
            //console.log(resaddons);
            this.filterAddon.set({
                  'selectedaddonsdtls':addonsdtls
              }, {
                  silent: true
              }
                                      );

            $(this.el).html(_.template(AddonsPageTemplate)({
                'addonsdtls': addonsdtls,
                'resaddons': resaddons
            }));
            $.cloudinary.responsive();
        },

        render: function() {
            var that = this;
            this.addons.fetch({
               data: {
               },

              success: function(response) {
                 var addonsdtls = response.toJSON();
                 that.renderWithAddons(addonsdtls);
              },
              error: function(model, response, options) {
                  console.log("couldn't fetch addon data - " + response);
              }
            });
        },
        initialize: function() {
            this.ref = MGF.rootRef;
            this.addons = new Addons();
            this.filterAddon = new FilterAddon();
            Analytics.apply(Analytics.TYPE_GENERAL);
            $.cloudinary.config({ cloud_name: 'mygubbi', api_key: '492523411154281'});
            _.bindAll(this, 'renderWithAddons');
        },
        events: {
            "change #filter-category": "getSubcatList",
            "change #filter-product-type": "getSubcatList1",
            "change #filter-product-subtype": "getSubcatList2",
            "change #filter-brands": "getSubcatList3"
        },
        getCatList: function(addonsdtls){
            var resaddons = this.addons.getCategoriesList(addonsdtls);
            $("#filter-category").html(_.template(CategoriesTemplate)({
                'resaddons': resaddons
            }));
        },
        getSubcatList: function(){
            var that = this;
            var selectedCategory = $('#filter-category option:selected').text();

            this.filterAddon.set({
            'productType':''
              }, {
                  silent: true
              }
            );
            this.filterAddon.set({
            'productSubtype':''
            }, {
               silent: true
             }
            );
            this.filterAddon.set({
              'brand':''
                     }, {
                          silent: true
                        }
                     );

            if(selectedCategory.trim() !== 'Category'){
                this.filterAddon.set({
                    'selectedCategory':selectedCategory
                    }, {
                    silent: true
                    }
                );
            }else{
                this.filterAddon.set({
                    'selectedCategory':''
                    }, {
                    silent: true
                    }
                );
             }

            var addonsdtls = that.filterAddon.get('selectedaddonsdtls');
            var resProductType = that.addons.getSubcategoriesList(selectedCategory);

            $("#filter-product-type").html(_.template(ProductTypeTemplate)({
                'resProductType': resProductType
            }));

            $('#filter-product-subtype').html('<option value="">Product Type</option>');
            $('#filter-product-subtype').html('<option value="">Product Subtype</option>');
            $('#filter-brands').html('<option value="">Brands</option>');
            that.addonsFilter();
        },
        getSubcatList1: function(){
            var that = this;
            var productType = $('#filter-product-type option:selected').text();

            this.filterAddon.set({
             'productSubtype':''
              }, {
                   silent: true
                 }
              );
              this.filterAddon.set({
                              'brand':''
                                     }, {
                                          silent: true
                                        }
                                     );

            if(productType.trim() !== 'Product Type'){
                this.filterAddon.set({
                    'productType':productType
                      }, {
                          silent: true
                      }
                 );
             }else{
                this.filterAddon.set({
                    'productType':''
                      }, {
                          silent: true
                      }
                 );
             }
            var resProductSubtype = that.addons.getProductSubtypeList(productType);
             $("#filter-product-subtype").html(_.template(ProductSubtypeTemplate)({
                'resProductSubtype': resProductSubtype
            }));
            $('#filter-brands').html('<option value="">Brands</option>');
            that.addonsFilter();
        },
        getSubcatList2: function(){
            var that = this;
            var productSubtype = $('#filter-product-subtype option:selected').text();
            var productType = that.filterAddon.get('productType') ? that.filterAddon.get('productType') : '';

             this.filterAddon.set({
                                             'brand':''
                                                    }, {
                                                         silent: true
                                                       }
                                                    );

            if(productSubtype.trim() !== 'Product Subtype'){
                this.filterAddon.set({
                'productSubtype':productSubtype
                 }, {
                      silent: true
                    }
                 );
            }else{
                this.filterAddon.set({
                'productSubtype':''
                 }, {
                      silent: true
                    }
                 );

            }

            $('#filter-brands').html('<option value="">Brands</option>');
            var resBrand = that.addons.getBrandList(productType,productSubtype);

            $("#filter-brands").html(_.template(BrandTemplate)({
                 'resBrand': resBrand
             }));
             that.addonsFilter();
        },
        getSubcatList3: function(){
            var that = this;
            var brand = $('#filter-brands option:selected').text();

            if(brand.trim() !== 'Brands'){
                this.filterAddon.set({
                'brand':brand
                       }, {
                            silent: true
                          }
                       );
           }else{
                this.filterAddon.set({
                'brand':''
                       }, {
                            silent: true
                          }
                       );
           }

            that.addonsFilter();

        },
        addonsFilter: function(){
            var that = this;


            var x_selectedCategory= that.filterAddon.get('selectedCategory') ? that.filterAddon.get('selectedCategory') : '';
            var x_selectedproductType= that.filterAddon.get('productType') ? that.filterAddon.get('productType') : '';
            var x_selectedproductSubtype= that.filterAddon.get('productSubtype') ? that.filterAddon.get('productSubtype') : '' ;
            var x_selectedbrand= that.filterAddon.get('brand') ? that.filterAddon.get('brand') : '' ;


            console.log("+++++++++++Selected Vals ++++++++++++++++");
            console.log(x_selectedCategory+"========="+x_selectedproductType+"========="+x_selectedproductSubtype+"========="+x_selectedbrand);



            var resfil = {};

            if(x_selectedCategory != ''){
               resfil = that.addons.filterCategorywise(x_selectedCategory);
            }

            if(x_selectedproductType != ''){
              resfil = that.addons.filterProductTypewise(resfil,x_selectedproductType,x_selectedCategory);
            }

            if(x_selectedproductSubtype != ''){
              resfil = that.addons.filterProductSubtypewise(resfil,x_selectedproductSubtype,x_selectedproductType,x_selectedCategory);
            }

            if(x_selectedbrand != ''){
                resfil = that.addons.filterBrandwise(resfil,x_selectedbrand,x_selectedproductSubtype,x_selectedproductType,x_selectedCategory);
            }


            console.log("+++++++++++Filtered product by smruti ++++++++++++++++");
            console.log(resfil);
            console.log(Object.keys(resfil).length);


            if(Object.keys(resfil).length == 0){
                resfil = that.filterAddon.get('selectedaddonsdtls');
            }
             $("#allAddons").html(_.template(AddonsFilterTemplate)({
                 'addonsdtls': resfil
             }));
        }
    });
    return AddonsPageVIew;
});