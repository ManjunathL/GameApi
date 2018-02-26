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
  'text!templates/dashboard/l1_form.html',
  'text!templates/dashboard/furniture.html',
  'text!templates/dashboard/decor.html',
  'collections/designs',
  'collections/products',
  'collections/codelookups',
  'collections/additionalcodelookups',
  'collections/createdesigns',
  'collections/createtagproducts',
  'models/filter'
], function($, _, Backbone, Bootstrap, chosen, bootstrapDualListbox, tagsinput, nouislider, slick, select2, upload, appmin, l1formPageTemplate, furniturePageTemplate, decorPageTemplate, Designs, Products, CodeLookups, AdditionalCodeLookups, CreateDesigns, CreateTagProducts, Filter){
  var L1FormPage = Backbone.View.extend({
    el: '.page',
    designs: null,
    codelookups: null,
    additionalcodelookups: null,
    createdesigns: null,
    createtagproducts: null,
    filter: null,
    initialize: function() {
        this.designs = new Designs();
        this.products = new Products();
        this.codelookups = new CodeLookups();
        this.additionalcodelookups = new AdditionalCodeLookups();
        this.createdesigns = new CreateDesigns();
        this.createtagproducts = new CreateTagProducts();
        this.filter = new Filter();
        this.filter.on('change', this.render, this);
        this.listenTo(Backbone);
        _.bindAll(this, 'render','fetchProductsAndRender');
    },
    render: function () {
        var that = this;
        window.filter = that.filter;
        var getProductsPromise = that.getProducts();

        var getSpaceTypesPromise = that.getSpaceTypes('space');
        var getSizePromise = that.getSpaceTypes('size');
        var getDesignstylePromise = that.getSpaceTypes('designstyle');
        var getWcolorstylePromise = that.getSpaceTypes('wcolorstyle');
        var getSpacefeaturePromise = that.getSpaceTypes('Spacefeature');
        var getWalltypePromise = that.getSpaceTypes('wallTreatmentType');
        var getCeilingTypePromise = that.getSpaceTypes('ceilingType');
        var getLightingPromise = that.getSpaceTypes('Lighting');
        var getDesignmaterialPromise = that.getSpaceTypes('Material');
        var getDesignFinishTypePromise = that.getSpaceTypes('FinishType');
        var getDesignFinishMaterialPromise = that.getSpaceTypes('FinishMaterial');


        var getUserpersonaPromise = that.getAdditionalTypes('userpersona','type');
        var getHobbypersonaPromise = that.getAdditionalTypes('userpersona','Hobby');
        var getProfessionpersonaPromise = that.getAdditionalTypes('userpersona','Profession');

        var getAgepersonaPromise = that.getAdditionalTypes('userpersona','Age');
        var getReligionpersonaPromise = that.getAdditionalTypes('userpersona','Religion');
        var getCommunitypersonaPromise = that.getAdditionalTypes('userpersona','Community');

        Promise.all([getProductsPromise, getSpaceTypesPromise, getSizePromise, getDesignstylePromise
                    , getWcolorstylePromise, getSpacefeaturePromise, getWalltypePromise
                    , getCeilingTypePromise, getLightingPromise, getDesignmaterialPromise
                    , getDesignFinishTypePromise, getDesignFinishMaterialPromise
                    , getUserpersonaPromise, getHobbypersonaPromise, getProfessionpersonaPromise
                    , getAgepersonaPromise, getReligionpersonaPromise, getCommunitypersonaPromise
                    ]).then(function() {
            that.fetchProductsAndRender();
        });
    },
    fetchProductsAndRender: function() {
        var that = this;
        var productdetails = that.products.toJSON();
        var spaceType = that.filter.get('selectedSpaceType');
        var size = that.filter.get('selectedSize');
        var designstyle = that.filter.get('selectedDesignstyle');
        var designcolorstyle = that.filter.get('selectedWcolorstyle');
        var feature = that.filter.get('selectedSpacefeature');
        var walltype = that.filter.get('selectedWalltype');
        var ceiltype = that.filter.get('selectedCeilingType');
        var light = that.filter.get('selectedLighting');
        var desmat = that.filter.get('selectedDesignMaterial');
        var finmat = that.filter.get('selectedDesignFinishType');
        var dfinmat = that.filter.get('selectedDesignFinishMaterial');

        var userpersona = that.filter.get('selectedUserpersona');
        var hobbypersona = that.filter.get('selectedHobbypersona');
        var professionpersona = that.filter.get('selectedProfessionpersona');

        var agepersona = that.filter.get('selectedAgepersona');
        var religionpersona = that.filter.get('selectedReligionpersona');
        var communitypersona = that.filter.get('selectedCommunitypersona');

        $(this.el).html(_.template(l1formPageTemplate)({
            'productdetails': productdetails,
            'spaceType': spaceType,
            'size': size,
            'designstyle': designstyle,
            'designcolorstyle': designcolorstyle,
            'feature': feature,
            'walltype': walltype,
            'ceiltype': ceiltype,
            'light': light,
            'desmat': desmat,
            'finmat': finmat,
            'ceilmat': desmat,
            'ceilfinmat': finmat,
            'floormat': desmat,
            'floorfin': dfinmat,
            'userpersona': userpersona,
            'userpersonatwo': userpersona,
            'hobbypersona':hobbypersona,
            'hobbypersonatwo':hobbypersona,
            'professionpersona':professionpersona,
            'professionpersonatwo':professionpersona,
            'agepersona':agepersona,
            'religionpersona':religionpersona,
            'communitypersona':communitypersona
        }));

        //L1FormHelper.getSelect2(that);
    },
    getProducts: function(){
        var that = this;
        return new Promise(function(resolve, reject) {
            if (that.products.isEmpty()) {
                that.products.fetch({
                    success: function(response) {
                        resolve();
                    },
                    error: function(model, response, options) {
                        console.log("error from spaceType fetch - " + response);
                        reject();
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
                        if(selSpace == 'space'){
                            that.filter.set({
                                'selectedSpaceType':response.toJSON()
                            }, {
                                silent: true
                            });
                        }
                        if(selSpace == 'size'){
                            that.filter.set({
                                'selectedSize':response.toJSON()
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
                                'selectedWcolorstyle':response.toJSON()
                            }, {
                                silent: true
                            });
                        }
                        if(selSpace == 'Spacefeature'){
                            that.filter.set({
                                'selectedSpacefeature':response.toJSON()
                            }, {
                                silent: true
                            });
                        }
                        if(selSpace == 'wallTreatmentType'){
                            that.filter.set({
                                'selectedWalltype':response.toJSON()
                            }, {
                                silent: true
                            });
                        }
                        if(selSpace == 'ceilingType'){
                            that.filter.set({
                                'selectedCeilingType':response.toJSON()
                            }, {
                                silent: true
                            });
                        }
                        if(selSpace == 'Lighting'){
                            that.filter.set({
                                'selectedLighting':response.toJSON()
                            }, {
                                silent: true
                            });
                        }
                        if(selSpace == 'Material'){
                            that.filter.set({
                                'selectedDesignMaterial':response.toJSON()
                            }, {
                                silent: true
                            });
                        }
                        if(selSpace == 'FinishType'){
                            that.filter.set({
                                'selectedDesignFinishType':response.toJSON()
                            }, {
                                silent: true
                            });
                        }
                        if(selSpace == 'FinishMaterial'){
                            that.filter.set({
                                'selectedDesignFinishMaterial':response.toJSON()
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
    events: {
        "change #featuresel": "GetspaceValue",
        "change #furnituresel": "furntiureselected",
        "change #decorsel": "decorselected",
        "change #walltypesel": "wallselected",
        "change #designmaterialsel": "wallmaterilaselected",
        "change #designfinish": "wallfinishselected",
        "change #designceilmaterial": "ceilingmaterialselected",
        "change #designceilfinish": "ceilingfinishselected",
        "change #designfloorfinish": "floorfinishselected",
        "change #designfloormaterial": "floormaterialselected",
        "change #images": "handleFileSelect",
        "change #img_secondary": "handleFileSelect",
        "change #doc": "handleFileSelect",
        "change #designmoodboard": "handleFileSelect",
        "change #spaceLayout": "handleFileSelect",
        "change #swatches": "handleFileSelect",
        "change #ceilswatches": "handleFileSelect",
        "change #flooring_swatches": "handleFileSelect",
        "change #spacesel": "GetSelectedTextValue",
        "click .tagprodChk": "handleChange",
        "click #submitbtn": "submitdesigndata"

    },
    handleChange: function(e){
        if (e.isDefaultPrevented()) return;
        e.preventDefault();

        var currentTarget = $(e.currentTarget);
        var checkboxId = currentTarget.attr('id');



        console.log("#######################");
        console.log(checkboxId);

        var splits = checkboxId.split('chk');
        console.log("#######################");
        console.log(splits[1]);

        var that = this;
        that.createtagproducts.fetch({
            data : {
                 productName: $("#title"+splits[1]).text(),
                 imagePath: $("#tagimagePath"+splits[1]).attr("src"),
                 spaceName:$("#designspacename").val()
            },
            success: function(response) {
                console.log("Successfully added tag products");
                console.log(response);
                $("#chk"+splits[1]).prop('checked', true);
            },
            error: function(model, response, options) {
                console.log("error while adding tag products - " + response);
                console.log(response);
            }
        });
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
            if(currId == "spaceLayout"){
                // Closure to capture the file information.
                reader.onload = (function(theFile) {
                return function(e) {
                // Render thumbnail.
                var span = document.createElement('span');
                span.innerHTML = "<a href='" + e.target.result + "'><img class='thumb' src='" + e.target.result + "'/></a>"
                document.getElementById('space_block').insertBefore(span, null);
                };
                })(f);
            }
            if(currId == "swatches"){
                // Closure to capture the file information.
                reader.onload = (function(theFile) {
                return function(e) {
                // Render thumbnail.
                var span = document.createElement('span');
                span.innerHTML = "<a href='" + e.target.result + "'><img class='thumb_swatch' src='" + e.target.result + "'/></a>"
                document.getElementById('swatch_block').insertBefore(span, null);
                };
                })(f);
            }
            if(currId == "ceilswatches"){
                // Closure to capture the file information.
                reader.onload = (function(theFile) {
                return function(e) {
                // Render thumbnail.
                var span = document.createElement('span');
                span.innerHTML = "<a href='" + e.target.result + "'><img class='thumb_swatch' src='" + e.target.result + "'/></a>"
                document.getElementById('ceilswatch_block').insertBefore(span, null);
                };
                })(f);
            }
            if(currId == "flooring_swatches"){
                // Closure to capture the file information.
                reader.onload = (function(theFile) {
                return function(e) {
                // Render thumbnail.
                var span = document.createElement('span');
                span.innerHTML = "<a href='" + e.target.result + "'><img class='thumb_swatch' src='" + e.target.result + "'/></a>"
                document.getElementById('flooring_block').insertBefore(span, null);
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
            }else if(currId == "spaceLayout"){
                var newjsonobject = {
                    "spacelayout" : res
                }
                document.getElementById("spaceLayout1").value = JSON.stringify(newjsonobject);;
                var splits = res;

                var imagesHtml = "";
                for ( var img in splits)
                {
                    var newImgUrl = splits[img].trim();
                    console.log(newImgUrl);

                    imagesHtml = imagesHtml +  "<span> <a href='" + newImgUrl + "'><img class='thumb a-thumbnail' src='" + newImgUrl + "'/></a></span>"

                }
                document.getElementById("spaceLayout").innerHTML = imagesHtml;
            }else if(currId == "swatches"){
                var newjsonobject = {
                    "swatchesimage" : res
                }

                document.getElementById("swatchesimagepath").value = JSON.stringify(newjsonobject);
                var splits = res;

                var imagesHtml = "";
                for ( var img in splits)
                {
                    var newImgUrl = splits[img].trim();
                    console.log(newImgUrl);

                    imagesHtml = imagesHtml +  "<span> <a href='" + newImgUrl + "'><img class='thumb_swatch' src='" + newImgUrl + "'/></a></span>"

                }
                document.getElementById("swatch_block").innerHTML = imagesHtml;
            }else if(currId == "ceilswatches"){
                var newjsonobject = {
                    "ceilswatchesimage" : res
                }


                document.getElementById("ceilswatchesimagepath").value = JSON.stringify(newjsonobject);
                var splits = res;

                var imagesHtml = "";
                for ( var img in splits)
                {
                    var newImgUrl = splits[img].trim();
                    console.log(newImgUrl);

                    imagesHtml = imagesHtml +  "<span> <a href='" + newImgUrl + "'><img class='thumb_swatch' src='" + newImgUrl + "'/></a></span>"

                }
                document.getElementById("ceilswatch_block").innerHTML = imagesHtml;
            }else if(currId == "flooring_swatches"){
                var newjsonobject = {
                    "floorswatchimage" : res
                }


                document.getElementById("flooringimagepath").value = JSON.stringify(newjsonobject);
                var splits = res;

                var imagesHtml = "";
                for ( var img in splits)
                {
                    var newImgUrl = splits[img].trim();
                    console.log(newImgUrl);

                    imagesHtml = imagesHtml +  "<span> <a href='" + newImgUrl + "'><img class='thumb_swatch' src='" + newImgUrl + "'/></a></span>"

                }
                document.getElementById("flooring_block").innerHTML = imagesHtml;
            }else{
                document.getElementById(currId+'1').value = res;
            }
        }
      });

    },
    GetspaceValue: function(){
        var selectedValues = $("#featuresel").select2("data");
        var newjsonobject = {
            "SelectedFeature" : selectedValues

        }
        console.log(newjsonobject);
        document.getElementById("featureselhidden").value = JSON.stringify(newjsonobject);
    },
    furntiureselected: function(){
        var selectedValues = $("#furnituresel").select2("data");
        var newjsonobject = {
            "SelectedFurniture" : selectedValues
        }
        console.log(newjsonobject);
        document.getElementById("furselected").value = JSON.stringify(newjsonobject);
    },
    decorselected: function(){
        var selectedValues = $("#decorsel").select2("data");
        var newjsonobject = {
            "SelectedDecor" : selectedValues
        }
        console.log(newjsonobject);
        document.getElementById("decorhidden").value = JSON.stringify(newjsonobject);
    },
    wallselected: function(){
        var selectedValues = $("#walltypesel").select2("data");
        var newjsonobject = {
            "SelectedWall" : selectedValues
        }
        console.log(newjsonobject);
        document.getElementById("walltypehidden").value = JSON.stringify(newjsonobject);
    },
    wallmaterilaselected: function(){
        var selectedValues = $("#designmaterialsel").select2("data");
        var newjsonobject = {
            "wallmaterial" : selectedValues
        }
        console.log(newjsonobject);
        document.getElementById("designmaterialselhidden").value = JSON.stringify(newjsonobject);

    },
    wallfinishselected: function(){
        var selectedValues = $("#designfinish").select2("data");
        var newjsonobject = {
            "wallfinish" : selectedValues
        }
        console.log(newjsonobject);
        document.getElementById("designfinishhidden").value = JSON.stringify(newjsonobject);
    },
    ceilingmaterialselected: function(){
        var selectedValues = $("#designceilmaterial").select2("data");
        var newjsonobject = {
            "ceilingmaterial" : selectedValues
        }
        console.log(newjsonobject);
        document.getElementById("designceilmaterialhidden").value = JSON.stringify(newjsonobject);
    },
    ceilingfinishselected: function(){
        var selectedValues = $("#designceilfinish").select2("data");
        var newjsonobject = {
            "ceilingfinish" : selectedValues
        }
        console.log(newjsonobject);
        document.getElementById("designceilfinishhidden").value = JSON.stringify(newjsonobject);
    },
    floorfinishselected: function(){
        var selectedValues = $("#designfloorfinish").select2("data");
        var newjsonobject = {
            "floorfinish" : selectedValues
        }
        console.log(newjsonobject);
        document.getElementById("designfloorfinishhidden").value = JSON.stringify(newjsonobject);
    },
    floormaterialselected: function(){
        var selectedValues = $("#designfloormaterial").select2("data");
        var newjsonobject = {
            "floormaterial" : selectedValues
        }
        console.log(newjsonobject);
        document.getElementById("designfloormaterialhidden").value = JSON.stringify(newjsonobject);

    },
    GetSelectedTextValue: function(){
        var that = this;
        var selectedValue = $("#spacesel").val();
        $("#furnitureselhidden").val(selectedValue);
        that.getFurnitureDecoreData('furniture', selectedValue);
        that.getFurnitureDecoreData('decor', selectedValue);

    },
    getFurnitureDecoreData: function(ltype, Selval){
        var that = this;
        that.additionalcodelookups.fetch({
            data: {
                lookupType: ltype,
                additionalType: Selval
            },
            success: function(response) {
                console.log("Successfully fetch Additional furniture - ");
                if(ltype == 'furniture'){
                    $('#furnituresel').html(_.template(furniturePageTemplate)({
                        "furnituressss": response.toJSON()
                    }));
                }
                if(ltype == 'decor'){
                    $('#decorsel').html(_.template(decorPageTemplate)({
                        "ddecorssss": response.toJSON()
                    }));
                }

            },
            error: function(model, response, options) {
                console.log("error from Additional furniture fetch - " + response);
            }
        });
        return;
    },
    submitdesigndata: function(e){
        console.log("Hiiiiiiiiiiiiiii Smrutiiiiiiiiiiiiii");
        if (e.isDefaultPrevented()) return;
        e.preventDefault();
        var that = this;
        that.createdesigns.fetch({
            data : {
                  spaceName: $("#designspacename").val(),
                  webDisplayName: $("#designwebname").val(),
                  dFileLocation: $("#designdfile").val(),
                  primaryImageLocation: $("#primaryimagepath").val(),
                  secondaryImageLocationJson: $("#secondaryimagepath").val(),
                  l0Location: $("#doc1").val(),
                  moodBoardLocation:$("#designmoodboard1").val(),
                  conceptNote: $("#designconceptnote").val(),
                  spaceType: $("#spacesel option:selected").text(),
                  spaceLayout: $("#spaceLayout1").val(),
                  spaceSize: $("#sizesel option:selected").text(),
                  propertyType: $("#designproperty").val(),
                  spaceDesignStyle: $("#designstyle option:selected").text(),
                  spaceColorStyle: $("#designcolorstyle option:selected").text(),
                  spaceFeature:  $("#featureselhidden").val(),
                  furnitureJson: $("#furselected").val(),
                  decorJson: $("#decorhidden").val(),
                  wallTreatmentType: $("#walltypehidden").val(),
                  wallMaterial: $("#designmaterialselhidden").val(),
                  wallFinishMaterial: $("#designfinishhidden").val(),
                  wallFinishSwatch: $("#swatchesimagepath").text(),
                  ceilingType: $("#ceiltypesel option:selected").text(),
                  ceilingMaterial: $("#designceilmaterialhidden").val(),
                  ceilingFinishMaterial: $("#designceilfinishhidden").val(),
                  ceilingFinishSwatch: $("#ceilswatchesimagepath").val(),
                  flooringMaterial: $("#designfloormaterialhidden").val(),
                  flooringFinish: $("#designfloorfinishhidden").val(),
                  flooringFinishSwatch: $("#flooringimagepath").val(),
                  lighting: $("#lightsel option:selected").text(),
                  designElement: $("#designelement").val(),
                  user1: $("#userpersonasel option:selected").text(),
                  hobby1: $("#hobbypersonasel option:selected").text(),
                  profession1: $("#professionpersonasel option:selected").text(),
                  user2: $("#userpersonatwo option:selected").text(),
                  hobby2: $("#hobbypersonatwo option:selected").text(),
                  profession2: $("#professionpersonatwo option:selected").text(),
                  ageGroup: $("#agepersona option:selected").text(),
                  religion: $("#religionpersona option:selected").text(),
                  community: $("#communitypersona option:selected").text(),
                  designerstory: $("#designerstory").val()
            },
            success: function(response) {
                console.log("Successfully added new design");
                console.log(response);
            },
            error: function(model, response, options) {
                console.log("error while adding new design - " + response);
                console.log(response);
            }
        });
        that.render();
    }
  });
  return L1FormPage;
});
