define([
  'jquery',
  'lodash',
  'backbone',
  'bootstrap',
  'chosenjquery',
  'libs/jquery.bootstrap-duallistbox',
  'libs/bootstrap-tagsinput',
  'libs/nouislider.min',
  'libs/slick.min',
  'select2',
  'upload',
  'libs/app.min',
  'text!templates/product/edit_product.html',
  'text!templates/product/subcat.html',
  'text!templates/product/component.html',
  'collections/designs',
  'collections/products',
  'collections/product_details',
  'collections/codelookups',
  'collections/additionalcodelookups',
  'collections/materialcomponents',
  'collections/materialfinishmappings',
  'collections/finishmappings',
  'collections/updateproducts',
  'models/filter'
], function($, _, Backbone, Bootstrap, chosen, bootstrapDualListbox, tagsinput, nouislider, slick, select2, upload, appmin, l3formPageTemplate, subcategoryPageTemplate, componentPageTemplate, Designs, Products, ProductDetails, CodeLookups, AdditionalCodeLookups, MaterialComponents, MaterialFinishMappings, FinishMappings, UpdateProducts, Filter){
  var L3FormPage = Backbone.View.extend({
    el: '.page',
    designs: null,
    product_details: null,
    codelookups: null,
    additionalcodelookups: null,
    materialcomponents: null,
    materialfinishmappings: null,
    finishmappings: null,
    updateproducts: null,
    filter: null,
    initialize: function() {
        this.designs = new Designs();
        this.products = new Products();
        this.product_details = new ProductDetails();
        this.codelookups = new CodeLookups();
        this.additionalcodelookups = new AdditionalCodeLookups();
        this.materialcomponents = new MaterialComponents();
        this.materialfinishmappings = new MaterialFinishMappings();
        this.finishmappings = new FinishMappings();
        this.updateproducts = new UpdateProducts();
        this.filter = new Filter();
        this.filter.on('change', this.render, this);
        this.listenTo(Backbone);
        _.bindAll(this, 'render','fetchProductsAndRender');
    },
    render: function () {
        var that = this;
        window.filter = that.filter;

        var getCategoryPromise = that.getSpaceTypes('wcategory');

        //var getSubCategoryPromise = that.getAdditionalTypes('wsubcategory','type');

        var getSpacePromise = that.getSpaceTypes('space');

        var getDesignStylePromise = that.getSpaceTypes('designstyle');

        var getColorStylePromise = that.getSpaceTypes('wcolorstyle');

        var getScalePromise = that.getSpaceTypes('Sourcingscale');



        Promise.all([getCategoryPromise, getDesignStylePromise, getSpacePromise, getColorStylePromise, getScalePromise ]).then(function() {
            that.fetchProductsAndRender();
        });
    },
    fetchProductsAndRender: function() {
        var that = this;

        var product_details = that.product_details;

        var category = that.filter.get('selectedCategory');
        var spaceType = that.filter.get('selectedSpaceType');
        var designstyle = that.filter.get('selectedDesignstyle');
        var colorstyle = that.filter.get('selectedColorstyle');
        var scale = that.filter.get('selectedScale');


        $(this.el).html(_.template(l3formPageTemplate)({
            'productdetails':product_details.toJSON(),
            'category': category,
            'spaceType': spaceType,
            'designstyle': designstyle,
            'otherdesignstyle': designstyle,
            'colorstyle': colorstyle,
            'othercolorstyle': colorstyle,
            'scale': scale

        }));

        that.ready();

        //L1FormHelper.getSelect2(that);
    },

    getSpaceTypes: function(selSpace) {
            var that = this;
            return new Promise(function(resolve, reject) {
                if (that.codelookups.isEmpty()) {
                    that.codelookups.fetch({
                        data: {
                            "lookupType": selSpace
                        },
                        success: function(response) {
                            console.log("Successfully fetch spaceType - ");
                            if(selSpace == 'wcategory'){
                                that.filter.set({
                                    'selectedCategory':response.toJSON()
                                }, {
                                    silent: true
                                });
                            }
                            if(selSpace == 'space'){
                                that.filter.set({
                                    'selectedSpaceType':response.toJSON()
                                }, {
                                    silent: true
                                });
                            }
                            if(selSpace == 'designstyle'){
                                that.filter.set({
                                    'selectedDesignstyle':response.toJSON()
                                }, {
                                    silent: true
                                });
                            }
                            if(selSpace == 'wcolorstyle'){
                                that.filter.set({
                                    'selectedColorstyle':response.toJSON()
                                }, {
                                    silent: true
                                });
                            }
                            if(selSpace == 'Sourcingscale'){
                                that.filter.set({
                                    'selectedScale':response.toJSON()
                                }, {
                                    silent: true
                                });
                            }
                            resolve();
                        },
                        error: function(model, response, options) {
                            console.log("error from spaceType fetch - " + response);
                        }
                    });
                } else {
                    resolve();
                }
            });

        },

    getAdditionalTypes: function(lookupType, additionalType) {
        var that = this;
        return new Promise(function(resolve, reject) {
            if (that.additionalcodelookups.isEmpty()) {
                that.additionalcodelookups.fetch({
                    data: {
                        lookupType: lookupType,
                        additionalType: additionalType
                    },
                    success: function(response) {
                        console.log("Successfully fetch Additional Type - ");
                        if(additionalType == 'type'){
                            that.filter.set({
                                'selectedUserpersona':response.toJSON()
                            }, {
                                silent: true
                            });
                        }
                        if(additionalType == 'Hobby'){
                            that.filter.set({
                                'selectedHobbypersona':response.toJSON()
                            }, {
                                silent: true
                            });
                        }
                        if(additionalType == 'Profession'){
                            that.filter.set({
                                'selectedProfessionpersona':response.toJSON()
                            }, {
                                silent: true
                            });
                        }
                        if(additionalType == 'Age'){
                            that.filter.set({
                                'selectedAgepersona':response.toJSON()
                            }, {
                                silent: true
                            });
                        }
                        if(additionalType == 'Religion'){
                            that.filter.set({
                                'selectedReligionpersona':response.toJSON()
                            }, {
                                silent: true
                            });
                        }
                        if(additionalType == 'Community'){
                            that.filter.set({
                                'selectedCommunitypersona':response.toJSON()
                            }, {
                                silent: true
                            });
                        }
                        resolve();
                    },
                    error: function(model, response, options) {
                        console.log("error from Additional Type fetch - " + response);
                    }
                });
            } else {
                resolve();
            }
        });

    },




    events: {
        "change #categorysel": "GetSelectedTextValue",
        "change #subcategorysel": "GetSelectedCompValue",
        "change .materialSelCls": "GetMaterialId",
        "change .finishSelCls": "GetfinishId",
        "change #images": "handleFileSelect",
        "change #img_secondary": "handleFileSelect",
        "change #spacesel": "spaceselected",
        "change #compcolorsel": "colorselected",
        "change #compdesignsel": "designStyleselected",
        "click .tagprodChk": "handleChange",
        "click #submitbtn": "submitproductdata"

    },

    handleFileSelect: function(evt){
        console.log("@@@@@@@@@@@@@@@ I M Here @@@@@@@@@@@@@@@");

        var currentTarget = $(evt.currentTarget);
        var currId = currentTarget.attr('id');

        console.log(" @@@@@@@@@@@@@@@ "+currId+" @@@@@@@@@@@@@@@ ");

        var files = evt.target.files; // FileList object

        // Loop through the FileList and render image files as thumbnails.
        for (var i = 0, f; f = files[i]; i++) {

            // Only process image files.
            if (!f.type.match('image.*')) {
            continue;
            }

            var reader = new FileReader();

            if(currId == "images"){
                // Closure to capture the file information.
                reader.onload = (function(theFile) {
                    return function(e) {
                        // Render thumbnail.
                        var span = document.createElement('span');
                        span.innerHTML = "<a href='" + e.target.result + "'><img class='thumb1 a-thumbnail' src='" + e.target.result + "'/></a>"
                        document.getElementById('list1').insertBefore(span, null);
                    };
                })(f);
            }
            if(currId == "img_secondary"){
                // Closure to capture the file information.
                reader.onload = (function(theFile) {
                return function(e) {
                // Render thumbnail.
                var span = document.createElement('span');
                span.innerHTML = "<a href='" + e.target.result + "'><img class='thumb' src='" + e.target.result + "'/></a>"
                document.getElementById('secondary_block').insertBefore(span, null);
                };
                })(f);
            }


            // Read in the image file as a data URL.
            reader.readAsDataURL(f);
        }

        var formData = new FormData();

        for ( var ff in files)
        {
            console.log("file:");
            console.log(files[ff]);
            console.log(files[ff].name)
            formData.append('file', files[ff]);
        }

        console.log(formData);

        $.ajax({
        url: baseApiUrl + "/gapi/fileupload/file",
        type: "POST",
        data : formData,
        processData: false,
        contentType: false,
        success: function (res)
        {
            console.log(res);
            if(currId == "images"){
                var splits = res;
                var imagesHtml = "";
                for ( var img in splits)
                {
                    var newImgUrl = splits[img].trim();
                    console.log(newImgUrl);
                    imagesHtml = imagesHtml +  "<span><a href='" + newImgUrl + "'><img class='thumb1 a-thumbnail' src='" + newImgUrl + "'/></a></span>"
                }
                document.getElementById("list1").innerHTML = imagesHtml;
                document.getElementById("primaryimagepath").value = newImgUrl;
            }else if(currId == "img_secondary"){
                var newjsonobject = {
                    "secondaryimage" : res
                }
                document.getElementById("secondaryimagepath").value = JSON.stringify(newjsonobject);
                var splits = res;
                var imagesHtml = "";
                for ( var img in splits)
                {
                    var newImgUrl = splits[img].trim();
                    console.log(newImgUrl);
                    imagesHtml = imagesHtml +  "<span><a href='" + newImgUrl + "'><img class='thumb a-thumbnail' src='" + newImgUrl + "'/></a></span>"
                }
                document.getElementById("secondary_block").innerHTML = imagesHtml;
            }else{
                document.getElementById(currId+'1').value = res;
            }
        }
      });

    },

    spaceselected: function(){
        var selectedValues = $("#spacesel").select2("data");
        var newjsonobject = {
         "SelectedSpaces" : selectedValues
        }
        console.log(newjsonobject);
        document.getElementById("spaceselhidden").value = JSON.stringify(newjsonobject);
    },

    designStyleselected: function(){
        var selectedValues = $("#compdesignsel").select2("data");
        var newjsonobject = {
            "otherDesignStyle" : selectedValues
        }
        console.log(newjsonobject);
        document.getElementById("compdesignselhidden").value = JSON.stringify(newjsonobject);
    },

    colorselected: function(){
        var selectedValues = $("#compcolorsel").select2("data");
        var newjsonobject = {
            "productCompatibilityColor" : selectedValues
        }
        console.log(newjsonobject);
        document.getElementById("compcolorselhidden").value = JSON.stringify(newjsonobject);
    },

    GetSelectedTextValue: function(catId){
        var that = this;
        if(typeof(catId) !== 'undefined' && catId != ""){
            var selectedValue = catId;
        }else{
            var selectedValue = $("#categorysel").val();
        }

        $("#subcategoryselhidden").val(selectedValue);
        that.getSubcategoryData('wsubcategory', selectedValue);
    },
    getSubcategoryData: function(ltype, Selval){
        var that = this;
        that.additionalcodelookups.fetch({
            data: {
                lookupType: ltype,
                additionalType: Selval
            },
            success: function(response) {
                console.log("Successfully fetch Sub category - ");
                if(ltype == 'wsubcategory'){
                    that.filter.set({
                        'selectedSubCategory':response.toJSON()
                    }, {
                        silent: true
                    });
                    $('#subcategorysel').html(_.template(subcategoryPageTemplate)({
                        "subcategory": response.toJSON()
                    }));
                }
                that.getPredefinedSubcatVal();
            },
            error: function(model, response, options) {
                console.log("error from Sub category fetch - " + response);
            }
        });
        return;
    },
    GetSelectedCompValue: function(subcatId){
        var that = this;
        if(typeof(subcatId) !== 'undefined' && subcatId !== ""){
            var selectedValue = subcatId;
        }else{
            var selectedValue = $("#subcategorysel").val();
        }
        that.getFurnitureComponentData('component', selectedValue);
    },
    getFurnitureComponentData: function(ltype, Selval){
        var that = this;
        that.materialcomponents.fetch({
            data: {
                lookupType: ltype,
                additionalType: Selval
            },
            success: function(response) {
                console.log("Successfully fetch Additional furniture - ");
                if(ltype == 'component'){
                    $('#componentsel').html(_.template(componentPageTemplate)({
                        "component": response.toJSON()
                    }));
                    that.setFurnitureVal();
                }
            },
            error: function(model, response, options) {
                console.log("error from Additional furniture fetch - " + response);
            }
        });
        return;
    },
    GetMaterialId: function(evt){
        var currentTarget = $(evt.currentTarget);
        var uqid = currentTarget.data('element');
        var selectedTextq = $("#materialsel"+uqid).prop('selectedIndex');
        var selectedText = $('.materialSelCls option:selected').val();
        $("#materialhidden" + uqid).val(selectedText);

        var that = this;
        that.materialfinishmappings.fetch({
            data: {
                element: uqid,
                materialName: selectedText
            },
            success: function(response) {
                console.log("Successfully fetch Material - ");

                var materialFinish = response.toJSON();
                 console.log(materialFinish);
                var optionHtml = [];
                optionHtml.push('<option value="">Select</option>' );
                for ( var e in materialFinish)
                {
                    var obj = materialFinish[e];
                    optionHtml.push('<option value="' + obj["finishName"] + '">' + obj["finishName"] + '</option>' );
                }

                var selectedOption = optionHtml.join('');
                console.log(selectedOption);
                var selectedElem = "finishmaterial" + uqid;
                console.log(selectedElem);
                //$("#"  + uqid).html(selectedOption);
                $("#finishmaterial"+uqid).html(selectedOption);
            },
            error: function(model, response, options) {
                console.log("error from Material fetch - " + response);
            }
        });
        return;

    },
    GetfinishId: function(evt){
        var currentTarget = $(evt.currentTarget);
        var uqid = currentTarget.data('element');
        var selectedvalue = $('.finishSelCls option:selected').val();

        console.log("@@@@@@@@@@@ selectedText @@@@@@@@@@@");
        console.log(selectedvalue);
        console.log("@@@@@@@@@@@@@@@@@@@@@@");

        var that = this;
        that.finishmappings.fetch({
            data: {
                finishMaterial:selectedvalue
            },
            success: function(response) {
                console.log("Successfully fetch Finish Type - ");
                var FinishType = response.toJSON();

                var optionHtml = [];
                optionHtml.push('<option value="">Select</option>' );
                for ( var e in FinishType)
                {
                    var obj = FinishType[e];
                    optionHtml.push('<option value="' + obj["finishType"] + '">' + obj["finishType"] + '</option>' );
                }

                var selectedOption = optionHtml.join('');
                console.log(selectedOption);
                var selectedElem = "finishtype" + uqid;
                console.log(selectedElem);
                $("#finishtype"+uqid).html(selectedOption);
            },
            error: function(model, response, options) {
                console.log("error from Finish Type fetch - " + response);
            }
        });
        return;

    },

    submitproductdata: function(e){
        console.log("submit process");
        if (e.isDefaultPrevented()) return;
        e.preventDefault();

        var furncompnm = $("#furncomponenthidden").val();
        var furncompnmArr = furncompnm.split(",");

        var fcmArr = [];
         $.each(furncompnmArr, function(i, fc){
            var fcc = fc.replace(" & ","");
            var fcd = fcc.replace(" ","");
            var fcmJson = {};
            fcmJson['component'] = fc;
            var cmp = fcd;
            fcmJson['material'] =$("#materialhidden"+cmp).val();
            fcmJson['finishMaterial'] =$("#finishmaterial"+cmp).val();
            fcmJson['finishType'] =$("#finishtype"+cmp).val();
            fcmArr.push(fcmJson);
         });
        console.log("++++++++++++++++++++++++++++++++++++++++++++++");
        console.log(fcmArr);
        console.log("++++++++++++++++++++++++++++++++++++++++++++++");




        var that = this;
        that.updateproducts.fetch({
            data : {
                  id: that.filter.get('productId'),
                  productName: $("#productname").val(),
                  webDisplayName: $("#productwebname").val(),
                  dFileLocation: $("#productdfile").val(),
                  primaryImageLocation: $("#primaryimagepath").val(),
                  secondaryImageLocationJson: $("#secondaryimagepath").val(),
                  category: $("#select2-categorysel-container").text(),
                  subcategory: $("#select2-subcategorysel-container").text(),
                  usp: $("#productusp").val(),
                  designAttribute: $("#productattribute").val(),
                  spaceJSon: $("#spaceselhidden").val(),
                  productStyle: $("#designsel option:selected").text(),
                  otherDesignStyle: $("#compdesignselhidden").val(),
                  productColor: $("#colorsel option:selected").text(),
                  productCompatibilityColor: $("#compcolorselhidden").val(),
                  furnitureComponentMaterialJson: JSON.stringify(fcmArr),
                  sourcingScale: $("#scalesel option:selected").text(),
                  designerstory: $("#productdesignerstory").val()
            },
            success: function(response) {
                console.log("Successfully updated product");
                console.log(response);
            },
            error: function(model, response, options) {
                console.log("error while updating product - " + response);
                console.log(response);
            }
        });
        that.render();
    },
    ready: function(){
        var that = this;
        var productId = this.model.id;

        that.filter.set({
             'productId':productId
         }, {
             silent: true
         });

        this.product_details.fetch({
          data: {
            id: productId
          },
          success: function(response) {
            var response = response.toJSON();

            console.log("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
            console.log(response);
            console.log("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");

           $('#productname').val(response[0].productName);
           $('#productwebname').val(response[0].webDisplayName);
           $('#productdfile').val(response[0].dFileLocation);
           $('#productname').val(response[0].productName);
           $("#images").attr('src', response[0].primaryImageLocation);
           $("#primimage").attr('src', response[0].primaryImageLocation);
           $("#primaryimagepath").val(response[0].primaryImageLocation);

           var secondaryImageLocationJson = JSON.parse(response[0].secondaryImageLocationJson);
           var secondaryImageLoc = secondaryImageLocationJson.secondaryimage;
           $("#secondimage").attr('src', secondaryImageLoc);
           $("#secondaryimagepath").val(response[0].secondaryImageLocationJson);

           $('#select2-categorysel-container').text(response[0].category);

           $('#select2-subcategorysel-container').text(response[0].subcategory);
           $('#select2-designsel-container').text(response[0].productStyle);

           $('#select2-colorsel-container').text(response[0].productColor);

           /* Space Feature Start */
           if(typeof(response[0].spaceJSon) !== 'undefined' && response[0].spaceJSon !=""){
                var spaceJSon = JSON.parse(response[0].spaceJSon);
                var spaceJSonArr = spaceJSon.SelectedSpaces;

                var sListobj = that.filter.get('selectedSpaceType');
                var selobj = that.getSelJsonValue(sListobj, spaceJSonArr);

                $("#spacesel").select2({
                    data: selobj[0],
                    val:[1111]
                }).select2('val', selobj[1]);

           }
           /* Space Feature End */

           /* productCompatibilityColor Start */
           if(typeof(response[0].productCompatibilityColor) !== 'undefined' && response[0].productCompatibilityColor !=""){

               try {
                    var pcolorJSon = JSON.parse(response[0].productCompatibilityColor);
                    var pcolorJSonArr = pcolorJSon.productCompatibilityColor;
                    $("#compcolorselhidden").val(pcolorJSonArr);
               } catch (e) {
                    var productCompatibilityColor = response[0].productCompatibilityColor;
                    $("#compcolorselhidden").val(productCompatibilityColor);
               }
               if(typeof(pcolorJSon) !== 'undefined'){
                    var pcListobj = that.filter.get('selectedColorstyle');
                    var  pcobj = that.getSelJsonValue(pcListobj, pcolorJSonArr);

                    console.log("$$$$$$$$$$ productCompatibilityColor $$$$$$$$$$$$$$");
                    console.log(pcobj);
                    console.log("$$$$$$$$$$$$$$$$$$$$$$$$");

                    $("#compcolorsel").select2({
                    data: pcobj[0],
                    val:[1111]
                    }).select2('val', pcobj[1]);
               }

               if(typeof(productCompatibilityColor) !== 'undefined' && productCompatibilityColor != ""){

                var productCompatibilityColorListobj = that.filter.get('selectedColorstyle');
                var productCompatibilityColorObj = [];
                var productCompatibilityColorTxtArr = [];
                var productCompatibilityColorTxtNmArr = [];
                _.each(productCompatibilityColorListobj, function(ceiltype, i){
                    var innerceiltypeObj = {};
                    innerceiltypeObj['id'] = productCompatibilityColorListobj[i].id;
                    innerceiltypeObj['text'] = productCompatibilityColorListobj[i].title;
                    var tempvl=null;
                    if(productCompatibilityColor == innerceiltypeObj['text']){
                        var xx = productCompatibilityColor;
                        if(($.inArray( xx, productCompatibilityColorTxtArr ) == -1) && (xx != tempvl)){
                            productCompatibilityColorTxtArr.push(innerceiltypeObj['id']);
                            productCompatibilityColorTxtNmArr.push(xx);
                        }
                    }
                    tempvl=xx;
                    productCompatibilityColorObj.push(innerceiltypeObj);
                 });

                $("#compcolorsel").select2({
                    data: productCompatibilityColorObj,
                    val:[1111]
                }).select2('val', productCompatibilityColorTxtArr);
            }
           }
           /* productCompatibilityColor End */

           /* otherDesignStyle Start */
           if(typeof(response[0].otherDesignStyle) !== 'undefined' && response[0].otherDesignStyle !=""){

               try {
                var odsJSon = JSON.parse(response[0].otherDesignStyle);
                var odsJSonArr = odsJSon.otherDesignStyle;
                $("#compdesignselhidden").val(odsJSonArr);
            } catch (e) {
                var ods = response[0].otherDesignStyle;
                $("#compdesignselhidden").val(ods);
            }

               if(typeof(odsJSon) !== 'undefined'){
                var odsListobj = that.filter.get('selectedDesignstyle');

                console.log("$$$$$$$$$$$$$$$$$$$$$$$$");
                console.log(odsJSonArr);
                console.log("$$$$$$$$$$$$$$$$$$$$$$$$");



                //var  odsobj = that.+getSelJsonValue(odsListobj, odsJSonArr);

                var odsObj = [];
                var odsTxtArr = [];
                var odsTxtNmArr = [];
                _.each(odsListobj, function(ceiltype, i){
                    var innerodsObj = {};
                    innerodsObj['id'] = odsListobj[i].id;
                    innerodsObj['text'] = odsListobj[i].title;
                    _.each(odsJSonArr, function(sft, jj){
                        if(odsJSonArr[jj].text.toLowerCase() == innerodsObj['text'].toLowerCase()){
                            odsTxtArr.push(odsListobj[i].id);
                        }
                     });
                    odsObj.push(innerodsObj);
                 });

                console.log("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
                console.log(odsTxtArr);
                console.log("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");

                $("#compdesignsel").select2({
                    data: odsObj,
                    val:[111]
                }).select2('val', odsTxtArr);
            }

               if(typeof(ods) !== 'undefined' && ods != ""){

                var odsListobj = that.filter.get('selectedDesignstyle');
                var odsObj = [];
                var odsTxtArr = [];
                var odsTxtNmArr = [];
                _.each(odsListobj, function(ceiltype, i){
                    var innerodsObj = {};
                    innerodsObj['id'] = odsListobj[i].id;
                    innerodsObj['text'] = odsListobj[i].title;
                    var tempvl=null;
                    if(ods == innerodsObj['text']){
                        var xx = ods;
                        if(($.inArray( xx, odsTxtArr ) == -1) && (xx != tempvl)){
                            odsTxtArr.push(innerodsObj['id']);
                            odsTxtNmArr.push(xx);
                        }
                    }
                    tempvl=xx;
                    odsObj.push(innerodsObj);
                 });

                console.log("********************************");
                console.log(odsTxtArr);
                console.log("********************************");

                $("#compdesignsel").select2({
                    data: odsObj,
                    val:[1111]
                }).select2('val', odsTxtArr);
            }
           }
           /* otherDesignStyle End */

           $("#productname").val(response[0].productName);
           $("#productwebname").val(response[0].webDisplayName);
           $("#productdfile").val(response[0].dFileLocation);
           $("#categorysel").val(response[0].category);
           $("#subcategorysel").val(response[0].subcategory);
           $("#productusp").val(response[0].usp);
           $("#productattribute").val(response[0].designAttribute);
           $("#productdesignerstory").val(response[0].designerstory);
           $("#select2-scalesel-container").text(response[0].sourcingScale);
           $("#furnitureComponenthid").val(response[0].furnitureComponentMaterialJson);

           that.getPredefinedVal();

          },
          error: function(model, response, options) {
              console.log("couldn't fetch product data - " + response);
          }
        });
     },
    getSelJsonValue: function(sListobj, spaceJSonArr){
        var spaceJSonTxtArr = [];
        var dobj = [];
        _.each(sListobj, function(subc, i){
            var innerObj = {};
            innerObj['id'] = sListobj[i].id;
            innerObj['text'] = sListobj[i].title;
            _.each(spaceJSonArr, function(sft, jj){
                if(spaceJSonArr[jj].text == innerObj['text']){
                    spaceJSonTxtArr.push(sListobj[i].id);
                }
             });
            dobj.push(innerObj);
         });

         return [dobj, spaceJSonTxtArr];
    },
    getPredefinedVal: function(){
        var that = this;
        var selCat = $('#select2-categorysel-container').text();
        var CategoryJson = that.filter.get('selectedCategory');
        var catId = that.findBySpecField(CategoryJson, 'title', selCat, 'code');
        that.GetSelectedTextValue(catId);
    },
    getPredefinedSubcatVal: function(){
        var that = this;
        var selSubCat = $('#select2-subcategorysel-container').text();
        var SubCategoryJson = that.filter.get('selectedSubCategory');

        console.log("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        console.log(SubCategoryJson +"=============="+ selSubCat);
        console.log("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        var subcatId = that.findBySpecField(SubCategoryJson, 'title', selSubCat, 'code');
        console.log('Result is  '+ subcatId);
        that.GetSelectedCompValue(subcatId);
    },
    findBySpecField: function(data, reqField, value, resField){
     for (var i = 0; i < data.length; i++) {
         if (data[i][reqField] == value) {
             return(data[i][resField]);
         }
     }
     return '';
    },
    setFurnitureVal: function(){
        var that = this;
        var furncompnm = $("#furnitureComponenthid").val();
        console.log("@@@@@@@@@@@ furncompnm @@@@@@@@@@@");
        console.log(furncompnm);
        console.log("@@@@@@@@@@@@@@@@@@@@@@");
        if(typeof(furncompnm) !== 'undefined' && furncompnm != ""){
             var furncompnmArr = JSON.parse(furncompnm);
             console.log("@@@@@@@@@@@ furncompnm @@@@@@@@@@@");
             console.log(furncompnmArr);
             console.log("@@@@@@@@@@@@@@@@@@@@@@");
             var fcmArr = [];
            $.each(furncompnmArr, function(i, fc){
                var comp = furncompnmArr[i].component;
                comp = comp.replace(" & ","");
                comp = comp.replace(" ","");
                var cmp = comp;
                if(furncompnmArr[i].material != "" && furncompnmArr[i].material != "Select"){
                    $("#materialhidden"+cmp).val(furncompnmArr[i].material);
                    $("#materialsel"+cmp).val(furncompnmArr[i].material);
                    $("#materialsel"+cmp).data('element',cmp);
                    var selMaterial = furncompnmArr[i].material;

                    if(furncompnmArr[i].finishMaterial != "" && furncompnmArr[i].finishMaterial != "Select"){
                        $("#finishmaterial"+cmp).val(furncompnmArr[i].finishMaterial);
                        var selFinishMaterial = furncompnmArr[i].finishMaterial;
                        var selFinishType = "";
                        if(furncompnmArr[i].finishType != "" && furncompnmArr[i].finishType != "Select"){
                            $("#finishtype"+cmp).val(furncompnmArr[i].finishType);
                            var selFinishType = furncompnmArr[i].finishType;
                        }

                        console.log("..... &&&&&&&&&&&&&&&&&&&&&&&&&..............");
                        console.log(cmp);
                        console.log(selMaterial);
                        console.log(selFinishMaterial);
                        console.log(selFinishType);
                        console.log(".......&&&&&&&&&&&&&&&&&&&&&&&&&&&&...........");

                       that.GetSelectedMaterialId(cmp, selMaterial, selFinishMaterial, selFinishType);
                    }
                }
             });
            //console.log("++++++++++++++++++++++++++++++++++++++++++++++");
            //console.log(furncompnm);
            //console.log("++++++++++++++++++++++++++++++++++++++++++++++");
        }
    },
    GetSelectedMaterialId: function(component, selMaterial, selFinishMaterial, selFinishType){
            if(typeof(component) !== "undefined" && component != ""){
                var uqid = component;
                var selectedText = selMaterial;
                $("#materialhidden" + uqid).val(selectedText);
            }


            console.log("@@@@@@@@@@@ selectedText  inside GetMaterialId @@@@@@@@@@@");
            console.log("uqid  ===" + uqid);
            console.log("selectedText  ==" +selectedText);
            console.log("@@@@@@@@@@@@@@@@@@@@@@");


            $("#materialhidden" + uqid).val(selectedText);

            var that = this;
            that.materialfinishmappings.fetch({
                data: {
                    element: uqid,
                    materialName: selectedText
                },
                success: function(response) {
                    console.log("Successfully fetch Material - ");

                    var materialFinish = response.toJSON();
                     console.log(materialFinish);
                    var optionHtml = [];
                    optionHtml.push('<option value="">Select</option>' );
                    for ( var e in materialFinish)
                    {
                        var obj = materialFinish[e];
                        if(selFinishMaterial.toLowerCase().trim() == obj["finishName"].toLowerCase().trim()){
                            optionHtml.push('<option value="' + obj["finishName"] + '" selected>' + obj["finishName"] + '</option>' );
                        }else{
                            optionHtml.push('<option value="' + obj["finishName"] + '">' + obj["finishName"] + '</option>' );
                        }
                    }

                    var selectedOption = optionHtml.join('');
                    console.log(selectedOption);
                    var selectedElem = "finishmaterial" + uqid;
                    console.log(selectedElem);
                    //$("#"  + uqid).html(selectedOption);
                    $("#finishmaterial"+uqid).html(selectedOption);
                    that.GetSelectedfinishId(component, selFinishMaterial, selFinishType);
                },
                error: function(model, response, options) {
                    console.log("error from Material fetch - " + response);
                }
            });
            return;

        },
    GetSelectedfinishId: function(component, finishMaterial, selFinishType){
            var uqid = component;
            var selectedvalue = finishMaterial;
            console.log(selectedvalue);

            console.log("@@@@@@@@@@@ selectedText @@@@@@@@@@@");
            console.log(selectedvalue);
            console.log("@@@@@@@@@@@@@@@@@@@@@@");

            var that = this;
            that.finishmappings.fetch({
                data: {
                    finishMaterial:selectedvalue
                },
                success: function(response) {
                    console.log("Successfully fetch Finish Type - ");
                    var FinishType = response.toJSON();

                    var optionHtml = [];
                    optionHtml.push('<option value="">Select</option>' );
                    for ( var e in FinishType)
                    {
                        var obj = FinishType[e];
                        if(selFinishType.toLowerCase().trim() == obj["finishType"].toLowerCase().trim()){
                            optionHtml.push('<option value="' + obj["finishType"] + '" selected>' + obj["finishType"] + '</option>' );
                        }else{
                            optionHtml.push('<option value="' + obj["finishType"] + '">' + obj["finishType"] + '</option>' );
                        }
                    }

                    var selectedOption = optionHtml.join('');
                    console.log(selectedOption);
                    var selectedElem = "finishtype" + uqid;
                    console.log(selectedElem);
                    $("#finishtype"+uqid).html(selectedOption);
                },
                error: function(model, response, options) {
                    console.log("error from Finish Type fetch - " + response);
                }
            });
            return;

        }
  });
  return L3FormPage;
});