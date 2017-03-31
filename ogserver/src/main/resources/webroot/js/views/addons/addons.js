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
    'text!templates/addons/product_types.html',
    'text!templates/addons/product_subtypes.html',
    'text!templates/addons/brands.html',
    'text!templates/addons/addons_filter.html',
    'cloudinary_jquery',
    'slyutil',
    'mgfirebase',
    'consultutil',
    'analytics'
], function($, _, Backbone, Addons, FilterAddon, AddonsPageTemplate, ProductTypeTemplate, ProductSubtypeTemplate, BrandTemplate,AddonsFilterTemplate, CloudinaryJquery, SlyUtil, MGF, ConsultUtil, Analytics) {
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
                             console.log("in addons");
                             console.log(response);
                             var addonsdtls = response.toJSON();
                             that.renderWithAddons(addonsdtls);
                          },
                          error: function(model, response, options) {
                              console.log("couldn't fetch story data - " + response);
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
        getSubcatList: function(){
            var that = this;
            var selectedCategory = $('#filter-category option:selected').text();

            if(selectedCategory !== 'Category'){
                this.filterAddon.set({
                    'selectedCategory':selectedCategory
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

            $('#filter-product-subtype').html('<option> Product Subtype </option>');
            $('#filter-brands').html('<option>Brands</option>');
            that.addonsFilter();
        },
        getSubcatList1: function(){
            var that = this;
            var productType = $('#filter-product-type option:selected').text();
            if(productType !== 'Product Type'){
            this.filterAddon.set({
                                'productType':productType
                                      }, {
                                          silent: true
                                      }
                                 );
             }
            var resProductSubtype = that.addons.getProductSubtypeList(productType);
             $("#filter-product-subtype").html(_.template(ProductSubtypeTemplate)({
                'resProductSubtype': resProductSubtype
            }));
            $('#filter-brands').html('<option>Brands</option>');
            that.addonsFilter();
        },
        getSubcatList2: function(){
            var that = this;
            var productSubtype = $('#filter-product-subtype option:selected').text();
            this.filterAddon.set({
                        'productSubtype':productSubtype
                                 }, {
                                      silent: true
                                    }
                                 );
                                 $('#filter-brands').html('<option>Brands</option>');
            var resBrand = that.addons.getBrandList(productSubtype);
            console.log('++++++++Filtered product+++++++++++');
                        console.log(resBrand);

            $("#filter-brands").html(_.template(BrandTemplate)({
                             'resBrand': resBrand
                                     }));
             that.addonsFilter();

        },
        getSubcatList3: function(){
            var that = this;
            var brand = $('#filter-brands option:selected').text();
            this.filterAddon.set({
                      'brand':brand
                               }, {
                                    silent: true
                                  }
                               );

            that.addonsFilter();

        },
        addonsFilter: function(){
            var that = this;
            var x_selectedCategory= that.filterAddon.get('selectedCategory') ? that.filterAddon.get('selectedCategory') : '';
            var x_selectedproductType= that.filterAddon.get('productType') ? that.filterAddon.get('productType') : '';
            var x_selectedproductSubtype= that.filterAddon.get('productSubtype') ? that.filterAddon.get('productSubtype') : '' ;
            var x_selectedbrand= that.filterAddon.get('brand') ? that.filterAddon.get('brand') : '' ;
            console.log('=============X Category List===============');
            console.log(x_selectedCategory);
            console.log(x_selectedproductType);
            console.log(x_selectedproductSubtype);
            var resfil = {};
            if(x_selectedCategory != ''){
               resfil = that.addons.filterCategorywise(x_selectedCategory);
                console.log('=============Category Filter===============');
                console.log(resfil);
                console.log(resfil.length);
            }
            if(x_selectedproductType != ''){
              resfil = that.addons.filterProductTypewise(resfil,x_selectedproductType,x_selectedCategory);
              console.log('=============Product Type Filter===============');
              console.log(resfil);
              console.log(resfil.length);
            }
            if(x_selectedproductSubtype != ''){
              resfil = that.addons.filterProductSubtypewise(resfil,x_selectedproductSubtype,x_selectedproductType,x_selectedCategory);
              console.log('=============Product Subtype Filter===============');
              console.log(resfil);
              console.log(resfil.length);
             }
             if(x_selectedbrand != ''){
               resfil = that.addons.filterBrandwise(resfil,x_selectedbrand,x_selectedproductSubtype,x_selectedproductType,x_selectedCategory);
               console.log('=============Brand Filter===============');
               console.log(resfil);
               console.log(resfil.length);
              }


              if(resfil.length == 0){
                resfil = that.filterAddon.get('selectedaddonsdtls');
              }


              console.log('=============Final Filter===============');
                             console.log(resfil);
                             console.log(resfil.length);
             $("#allAddons").html(_.template(AddonsFilterTemplate)({
                 'addonsdtls': resfil
             }));
        }
    });
    return AddonsPageVIew;
});