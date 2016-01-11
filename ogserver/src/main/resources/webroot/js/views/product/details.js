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
    'models/custom_product'
], function($, _, Backbone, Bootstrap, Sly, JqueryEasing, productPageTemplate, helperJsTemplate, ProductModel, CustomProduct) {
    var ProductPage = Backbone.View.extend({
        el: '.page',
        product: new ProductModel(),
        custom_product: new CustomProduct(),
        initialize: function() {
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
           if(!that.custom_product.get('colors')){
            that.changeColor(that.product.get('defaultFinish'));
           }

           // console.log(that.product.get('accessories'));

           if(!that.custom_product.get('selectedAccessories')){
              var accessoryobj = {};
                 _.each( that.product.get('accessories'), function ( acc ) {
                    accessoryobj[acc.accessoryName] = acc.accessoryPrice;
                 });
console.log('First Time ---- ');
console.log(accessoryobj);

              that.custom_product.set({'accessoryobj':accessoryobj},{silent: true});
              that.custom_product.set({'selectedAccessories':that.product.get('accessories')},{silent: true});
           }

            var compiledTemplate = _.template(productPageTemplate);
            $(this.el).html(compiledTemplate({
                "product": that.product.toJSON(),
                "materials": _.uniq(_.pluck(that.product.get('mf'), 'material')),
                //"finishes": _.uniq(_.pluck(that.product.get('mf'), 'finish')),
                "applianceTypes": _.uniq(_.pluck(that.product.get('appliances'), 'type'))
            }));

            var compiledJsTemplate = _.template(helperJsTemplate);
            $(this.el).append(compiledJsTemplate({
                "product": that.product.toJSON()
            }));

        },
        material: function(mf) { return mf.material; },
        finish: function(mf) { return mf.finish; },
        events:{
            "click .material": "changeMaterial",
            "click .finish": "changeFinish",
            "click .alt-accessory": "changeAccessory"

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

            console.log(finishobj);

            if(this.custom_product.get('finishobj') !== 'undefined'){
                this.custom_product.set({'finishobj':finishobj},{silent: true});
            }



            if(this.custom_product.get('finishes') !== 'undefined'){
                this.custom_product.set({'finishes':_.uniq(finishes)},{silent: true});
            }
            if(this.custom_product.get('selectedFinish') !== 'undefined'){
                this.custom_product.set({'selectedFinish':finishes[0]},{silent: true});
                this.changeColor(finishes[0]);
            }
            if(this.custom_product.get('basePrice') !== 'undefined'){
                this.custom_product.set({'basePrice':basePriceArr[0]});
            }
        },
        changeFinish : function(e) {
            $('.finish').removeClass('active');
            $(e.currentTarget).addClass('active');

            var selectedFinish = $(e.currentTarget).data('finish');
            if(this.custom_product.get('selectedFinish') !== 'undefined'){
                this.custom_product.set({'selectedFinish':selectedFinish},{silent: true});
            }

            if(this.custom_product.get('basePrice') !== 'undefined'){
               this.custom_product.set({'basePrice':this.custom_product.get('finishobj')[selectedFinish]});
           }

            this.changeColor(selectedFinish);
        },
        changeColor : function(selectedFinish){
            var colors = {};
            _.map( this.product.get('fc'), function ( result ) {
                if ( result.finish == selectedFinish ){
                    colors = result.color;
                }else{
                    return false;
                }
            });
            if(this.custom_product.get('colors') !== 'undefined'){
                this.custom_product.set({'colors':colors});
            }
        },
        changeAccessory : function(event){
            $('.alt-accessory').removeClass('active');
            $(event.currentTarget).addClass('active');

            var selectedDefaultAccessory = $(event.currentTarget).data('daccessory');
            var selectedAltAccessory = $(event.currentTarget).data('altaccessory');
            var selectedAltAccessoryPrice = $(event.currentTarget).data('altaccessoryprice');
            var selectedAltAccessoryImg = $(event.currentTarget).data('altaccessoryimg');

            console.log(selectedDefaultAccessory+' ---------- '+selectedAltAccessory+' ----- '+selectedAltAccessoryPrice);

            var defaultAccessoryPrice = 0;
            if(this.custom_product.get('accessoryobj') !== 'undefined'){
                defaultAccessoryPrice = this.custom_product.get('accessoryobj')[selectedDefaultAccessory];
            }

            var differencePrice = parseInt(selectedAltAccessoryPrice) - parseInt(defaultAccessoryPrice);

             console.log(defaultAccessoryPrice+'*******'+selectedAltAccessoryPrice+'**********'+differencePrice);

             if(this.custom_product.get('basePrice') !== 'undefined'){
                var defaultBaseprice = this.custom_product.get('basePrice');
                if (defaultBaseprice.indexOf(',') > -1) {
                    defaultBaseprice=defaultBaseprice.replace(/\,/g,'');
                }
                var basePrice = parseInt(defaultBaseprice) + parseInt(differencePrice);

                console.log(defaultBaseprice+'--------'+differencePrice+' ---------- '+basePrice);
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
                        accessoryList[k]['alternatives']= acc.alternatives;
                    }
                    else{
                        accessoryobj[accessoryList[k]['accessoryName']] = accessoryList[k]['accessoryPrice'];
                    }
                    k++;
                 });
              this.custom_product.set({'selectedAccessories':accessoryList},{silent: true});
              this.custom_product.set({'accessoryobj':accessoryobj});

           }

        }
    });
    return ProductPage;
});
