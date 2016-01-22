define([
    'jquery',
    'underscore',
    'backbone',
    'bootstrap',
    'sly',
    'jqueryeasing',
    'text!templates/product/details.html',
    'text!views/product/details_helper.js',
    'models/product',
    'models/custom_product',
    'text!templates/product/accessory.html',
    'text!templates/product/appliance.html',
    'text!templates/product/finish.html',
    'text!templates/product/colors.html'
], function($, _, Backbone, Bootstrap, Sly, JqueryEasing, productPageTemplate, helperJsTemplate, ProductModel, CustomProduct, AccessoryTemplate, applianceTemplate, finishTemplate, colorsTemplate) {
    var ProductPage = Backbone.View.extend({
        el: '.page',
        appliancelst: '#applianceList',
        product: null,
        custom_product: null,
        initialize: function() {
            this.product = new ProductModel();
            this.custom_product = new CustomProduct();
            this.custom_product.on('change',this.render,this);
        },
        render: function() {
            var that = this;
            window.custom_product = that.custom_product;


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

           if(!that.custom_product.get('basePrice')){
                that.custom_product.set({'basePrice':that.product.get('defaultPrice')},{silent: true});
           }
           if(!that.custom_product.get('selectedMaterial')){
               that.custom_product.set({'selectedMaterial':that.product.get('defaultMaterial')},{silent: true});
           }
           if(!that.custom_product.get('selectedFinish')){
               that.custom_product.set({'selectedFinish':that.product.get('defaultFinish')},{silent: true});
           }
           if(!that.custom_product.get('finishes')){
                   var finishes = new Array();
                   var finishobj = {};
                   _.map( that.product.get('mf'), function ( model ) {
                       if ( model.material == that.product.get('defaultMaterial') ){
                           finishes.push(model.finish);
                           finishobj[model.finish] = model.basePrice;
                       }else{
                           return false;
                       }
                   });
               that.custom_product.set({'finishes': _.uniq(finishes)},{silent: true});
               if(!that.custom_product.get('finishobj')){
                   that.custom_product.set({'finishobj':finishobj},{silent: true});
               }
           }
           if(!that.custom_product.get('selectedAccessories')){
              var accessoryobj = {};
                 _.each( that.product.get('accessories'), function ( acc ) {
                    accessoryobj[acc.accessoryName] = acc.accessoryPrice;
                 });
              that.custom_product.set({'accessoryobj':accessoryobj},{silent: true});
              that.custom_product.set({'selectedAccessories':that.product.get('accessories')},{silent: true});
           }
           if(!that.custom_product.get('selectedAppliances')){
                var appliances = new Array();
               that.custom_product.set({'selectedAppliances':appliances},{silent: true});
           }

            var compiledTemplate = _.template(productPageTemplate);
            $(this.el).html(compiledTemplate({
                "product": that.product.toJSON(),
                "materials": _.uniq(_.pluck(that.product.get('mf'), 'material')),
                "applianceTypes": _.uniq(_.pluck(that.product.get('appliances'), 'type')),
                "selectedColor": that.custom_product.get('colors'),
                "selectedfinishes": that.custom_product.get('finishes'),
                "selectedFinish": that.custom_product.get('selectedFinish'),
                "selectedAccessories": _.uniq(that.custom_product.get('selectedAccessories'))
            }));

            var compiledJsTemplate = _.template(helperJsTemplate);
            $(this.el).append(compiledJsTemplate({
                "product": that.product.toJSON()
            }));

            if(!that.custom_product.get('colors')){
                that.changeColor(that.product.get('defaultFinish'),colorsTemplate);
            }
        },
        material: function(mf) { return mf.material; },
        finish: function(mf) { return mf.finish; },
        events:{
            "click .material": "changeMaterial",
            "click .finish": "changeFinish",
            "click .alt-accessory": "changeAccessory",
            "click .appliance": "changeAppliance"

        },
        changeMaterial : function(ev) {
            $('.material').removeClass('active');
            $(ev.currentTarget).addClass('active');

            var selectedmaterial = $(ev.currentTarget).data('material');
            if(this.custom_product.get('selectedMaterial') !== 'undefined'){
                this.custom_product.set({'selectedMaterial':selectedmaterial},{silent: true});
            }

            var finishes = new Array();
            var basePriceArr = new Array();
            var finishobj = {};
            _.map( this.product.get('mf'), function ( model ) {
                if ( model.material == selectedmaterial ){
                    finishes.push(model.finish);
                    basePriceArr.push(model.basePrice);
                    finishobj[model.finish] = model.basePrice;
                }else{
                    return false;
                }
            });

            if(this.custom_product.get('finishobj') !== 'undefined'){
                this.custom_product.set({'finishobj':finishobj},{silent: true});
            }

            if(this.custom_product.get('finishes') !== 'undefined'){
                this.custom_product.set({'finishes':_.uniq(finishes)},{silent: true});
            }

            if(this.custom_product.get('basePrice') !== 'undefined'){
                this.custom_product.set({'basePrice':basePriceArr[0]},{silent: true});
            }

            if(this.custom_product.get('selectedFinish') !== 'undefined'){
                this.custom_product.set({'selectedFinish':finishes[0]},{silent: true});
            }

            $('#defaultbaseprice').html(this.custom_product.get('basePrice'));

            var nwfinishTemplate = _.template(finishTemplate);

             $('#finishList').html(nwfinishTemplate({
                 "selectedfinishes": _.uniq(this.custom_product.get('finishes')),
                 "selectedFinish": this.custom_product.get('selectedFinish')
             }));

            this.changeColor(finishes[0],colorsTemplate);
        },
        changeFinish : function(e) {
            $('.finish').removeClass('active');
            $(e.currentTarget).addClass('active');

            var selectedFinish = $(e.currentTarget).data('finish');
            if(this.custom_product.get('selectedFinish') !== 'undefined'){
                this.custom_product.set({'selectedFinish':selectedFinish},{silent: true});
            }

            if(this.custom_product.get('basePrice') !== 'undefined'){
               this.custom_product.set({'basePrice':this.custom_product.get('finishobj')[selectedFinish]},{silent: true});
           }

            this.changeColor(selectedFinish,colorsTemplate);
        },
        changeColor : function(selectedFinish,colorsTemplate){
            var colors = {};
            _.map( this.product.get('fc'), function ( result ) {
                if ( result.finish == selectedFinish ){
                    colors = result.color;
                }else{
                    return false;
                }
            });
            if(this.custom_product.get('colors') !== 'undefined'){
                this.custom_product.set({'colors':colors},{silent: true});
            }

            $('#defaultbaseprice').html(this.custom_product.get('basePrice'));

            var colorsTemplate = _.template(colorsTemplate);

             $('#colorList').html(colorsTemplate({
                 "selectedColor": this.custom_product.get('colors')
             }));

            return this;
        },
        changeAccessory : function(event){
        //debugger;
            $('.alt-accessory').removeClass('active');
            $(event.currentTarget).addClass('active');

            var selectedDefaultAccessory = $(event.currentTarget).data('daccessory');
            var selectedAltAccessory = $(event.currentTarget).data('altaccessory');
            var selectedAltAccessoryPrice = $(event.currentTarget).data('altaccessoryprice');
            var selectedAltAccessoryImg = $(event.currentTarget).data('altaccessoryimg');

            var defaultAccessoryPrice = 0;
            if(this.custom_product.get('accessoryobj') !== 'undefined'){
                defaultAccessoryPrice = this.custom_product.get('accessoryobj')[selectedDefaultAccessory];
            }

            var differencePrice = parseInt(selectedAltAccessoryPrice) - parseInt(defaultAccessoryPrice);

             if(this.custom_product.get('basePrice') !== 'undefined'){
                var defaultBaseprice = this.custom_product.get('basePrice');
                if (defaultBaseprice.indexOf(',') > -1) {
                    defaultBaseprice=defaultBaseprice.replace(/\,/g,'');
                }else{
                    defaultBaseprice = defaultBaseprice;
                }
                var basePrice = parseInt(defaultBaseprice) + parseInt(differencePrice);
                this.custom_product.set({'basePrice':basePrice.toLocaleString()},{silent: true});
            }

            if(this.custom_product.get('selectedAccessories') !== 'undefined'){
              var accessoryobj = {};
              var accessoryList = this.custom_product.get('selectedAccessories');
              var k = 0;
                 _.map(this.custom_product.get('selectedAccessories'), function ( acc ) {
                    if(acc.accessoryName == selectedDefaultAccessory){
                        accessoryobj[selectedAltAccessory] = selectedAltAccessoryPrice;
                        accessoryList[k]['accessoryName']= selectedAltAccessory;
                        accessoryList[k]['accessoryPrice']= selectedAltAccessoryPrice;
                        accessoryList[k]['accessoryImg']= selectedAltAccessoryImg;
                        accessoryList[k]['alternatives']= _.uniq(acc.alternatives);
                    }
                    else{
                        accessoryobj[accessoryList[k]['accessoryName']] = accessoryList[k]['accessoryPrice'];
                    }
                    k++;
                 });
              this.custom_product.set({'selectedAccessories':accessoryList},{silent: true});
              this.custom_product.set({'accessoryobj':accessoryobj},{silent: true});

             $('#defaultbaseprice').html(this.custom_product.get('basePrice'));

              var compiledaccessoryTemplate = _.template(AccessoryTemplate);

              $('#accessoryList').html(compiledaccessoryTemplate({
                  "product": this.product.toJSON(),
                  "selectedAccessories": _.uniq(this.custom_product.get('selectedAccessories'))
              }));
              return this;
           }
        },
       changeAppliance  : function(ev){
            var selectedappliance = $(ev.currentTarget).data('appliance');
            var selectedapplianceType = $(ev.currentTarget).data('appliancetype');
            var selectedappliancePrice = $(ev.currentTarget).data('applianceprice');
            console.log(selectedappliance+' ----- '+selectedapplianceType+' ----- '+selectedappliancePrice);

            var appliances = this.custom_product.get('selectedAppliances');
            _.map( this.product.get('appliances'), function ( model ) {
                if ( model.name == selectedappliance ){
                    if($.inArray( selectedappliance, appliances ) == -1){
                        appliances.push(selectedappliance);

                        if(this.custom_product.get('basePrice') !== 'undefined'){
                            var defaultBaseprice = this.custom_product.get('basePrice');
                            if (defaultBaseprice.indexOf(',') > -1) {
                                defaultBaseprice=defaultBaseprice.replace(/\,/g,'');
                            }
                            if(selectedappliancePrice.indexOf(',') > -1){
                                selectedappliancePrice=selectedappliancePrice.replace(/\,/g,'');
                            }
                            var basePrice = parseInt(defaultBaseprice) + parseInt(selectedappliancePrice);
                            this.custom_product.set({'basePrice':basePrice.toLocaleString()},{silent: true});
                        }
                    }else{
                        appliances.splice( $.inArray(selectedappliance, appliances), 1 );
                        if(this.custom_product.get('basePrice') !== 'undefined'){
                            var defaultBaseprice = this.custom_product.get('basePrice');
                            if (defaultBaseprice.indexOf(',') > -1) {
                                defaultBaseprice=defaultBaseprice.replace(/\,/g,'');
                            }
                            if(selectedappliancePrice.indexOf(',') > -1){
                                selectedappliancePrice=selectedappliancePrice.replace(/\,/g,'');
                            }
                            var basePrice = parseInt(defaultBaseprice) - parseInt(selectedappliancePrice);
                            this.custom_product.set({'basePrice':basePrice.toLocaleString()},{silent: true});

                        }
                    }
                }
            });
            $('#defaultbaseprice').html(this.custom_product.get('basePrice'));
            //console.log(appliances);
            if(this.custom_product.get('selectedAppliances') !== 'undefined'){
                this.custom_product.set({'selectedAppliances':appliances},{silent: true});
            }
            return this;
       }
    });
    return ProductPage;
});
