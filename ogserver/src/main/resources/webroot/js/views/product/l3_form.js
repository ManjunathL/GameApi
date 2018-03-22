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
  'text!templates/product/l3_form.html',
  'text!templates/product/subcat.html',
  'text!templates/product/component.html',
  'collections/designs',
  'collections/products',
  'collections/codelookups',
  'collections/additionalcodelookups',
  'collections/materialcomponents',
  'collections/materialfinishmappings',
  'collections/finishmappings',
  'collections/createproducts',
  'models/filter'
], function($, _, Backbone, Bootstrap, chosen, bootstrapDualListbox, tagsinput, nouislider, slick, select2, upload, appmin, l3formPageTemplate, subcategoryPageTemplate, componentPageTemplate, Designs, Products, CodeLookups, AdditionalCodeLookups, MaterialComponents, MaterialFinishMappings, FinishMappings, CreateProducts, Filter){
  var L3FormPage = Backbone.View.extend({
    el: '.page',
    designs: null,
    codelookups: null,
    additionalcodelookups: null,
    materialcomponents: null,
    materialfinishmappings: null,
    finishmappings: null,
    createproducts: null,
    filter: null,
    initialize: function() {
        this.designs = new Designs();
        this.products = new Products();
        this.codelookups = new CodeLookups();
        this.additionalcodelookups = new AdditionalCodeLookups();
        this.materialcomponents = new MaterialComponents();
        this.materialfinishmappings = new MaterialFinishMappings();
        this.finishmappings = new FinishMappings();
        this.createproducts = new CreateProducts();
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


        var category = that.filter.get('selectedCategory');
        var spaceType = that.filter.get('selectedSpaceType');
        var designstyle = that.filter.get('selectedDesignstyle');
        var colorstyle = that.filter.get('selectedColorstyle');
        var scale = that.filter.get('selectedScale');


        $(this.el).html(_.template(l3formPageTemplate)({
            'category': category,
            'spaceType': spaceType,
            'designstyle': designstyle,
            'otherdesignstyle': designstyle,
            'colorstyle': colorstyle,
            'othercolorstyle': colorstyle,
            'scale': scale

        }));

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
        "change #compdesignsel": "designStyleselected",
        "change #compcolorsel": "colorselected",
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
    colorselected: function(){
        var selectedValues = $("#compcolorsel").select2("data");
        var newjsonobject = {
            "productCompatibilityColor" : selectedValues
        }
        console.log(newjsonobject);
        document.getElementById("compcolorselhidden").value = JSON.stringify(newjsonobject);
    },

    GetSelectedTextValue: function(){
        var that = this;
        var selectedValue = $("#categorysel").val();
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
                    $('#subcategorysel').html(_.template(subcategoryPageTemplate)({
                        "subcategory": response.toJSON()
                    }));
                }
            },
            error: function(model, response, options) {
                console.log("error from Sub category fetch - " + response);
            }
        });
        return;
    },
    GetSelectedCompValue: function(){
        var that = this;
        var selectedValue = $("#subcategorysel").val();
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
        console.log("@@@@@@@@@@@ selectedText @@@@@@@@@@@");
        console.log(uqid);
        console.log(selectedText);
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
        var that = this;


         var furncompnm = $("#furncomponenthidden").val();
         var furncompnmArr = furncompnm.split(",");
        //return false;
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


        that.createproducts.fetch({
            data : {
                  productName: $("#productname").val(),
                  webDisplayName: $("#productwebname").val(),
                  dFileLocation: $("#productdfile").val(),
                  primaryImageLocation: $("#primaryimagepath").val(),
                  secondaryImageLocationJson: $("#secondaryimagepath").val(),
                  category: $("#categorysel option:selected").text(),
                  subcategory: $("#subcategorysel option:selected").text(),
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
                console.log("Successfully added new product");
                console.log(response);
            },
            error: function(model, response, options) {
                console.log("error while adding new product - " + response);
                console.log(response);
            }
        });
        that.render();
    }
  });
  return L3FormPage;
});