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
    'libs/select2.full.min',
    'upload',
    'libs/app.min',
    'text!templates/dashboard/edit_design.html',
    'text!templates/dashboard/furniture.html',
    'text!templates/dashboard/decor.html',
    'collections/pdesigns',
    'collections/designs',
    'collections/products',
    'collections/codelookups',
    'collections/additionalcodelookups',
    'collections/updatedesigns',
    'collections/createtagproducts',
    'models/filter'
], function($, _, Backbone, Bootstrap, chosen, bootstrapDualListbox, tagsinput, nouislider, slick, select2, upload, appmin, editPageTemplate, furniturePageTemplate, decorPageTemplate, Pdesigns, Designs, Products, CodeLookups, AdditionalCodeLookups, UpdateDesigns, CreateTagProducts, Filter){
  var DashboardPage = Backbone.View.extend({
    el: '.page',
    pdesigns: null,
    products: null,
    designs: null,
    codelookups: null,
    additionalcodelookups: null,
    updatedesigns: null,
    createtagproducts: null,
    filter: null,
    initialize: function() {
        this.pdesigns = new Pdesigns();
        this.designs = new Designs();
        this.products = new Products();
        this.codelookups = new CodeLookups();
        this.additionalcodelookups = new AdditionalCodeLookups();
        this.updatedesigns = new UpdateDesigns();
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
        var pdesigns = that.pdesigns;
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

        $(this.el).html(_.template(editPageTemplate)({
            'designsdetails':pdesigns.toJSON(),
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
        that.ready();
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
     GetSelectedTextValue: function(catId){
         var that = this;
         if(typeof(catId) !== 'undefined' && catId != ""){
            var selectedValue = catId;
         }else{
            var selectedValue = $("#spacesel").val();
         }
         console.log("@@@@@@@@@@@@@@@ I m here @@@@@@@@@@@@@@@");
         console.log(selectedValue);

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
                     that.filter.set({
                         'allFurnitureList':response.toJSON()
                     }, {
                         silent: true
                     });

                     $('#furnituresel').html(_.template(furniturePageTemplate)({
                         "furnituressss": response.toJSON()
                     }));
                     that.setFurnitureHideVal();
                 }
                 if(ltype == 'decor'){
                     that.filter.set({
                         'allDecorList':response.toJSON()
                     }, {
                         silent: true
                     });
                     $('#decorsel').html(_.template(decorPageTemplate)({
                         "ddecorssss": response.toJSON()
                     }));
                     that.setDecorHideVal();
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

         var spaceName = $("#designspacename").val(),
            webDisplayName = $("#designwebname").val(),
            dFileLocation = $("#designdfile").val(),
            primaryImageLocation = $("#primaryimagepath").val(),
            secondaryImageLocationJson = $("#secondaryimagepath").val(),
            l0Location = $("#doc1").val(),
            moodBoardLocation = $("#designmoodboard1").val(),
            conceptNote = $("#designconceptnote").val(),

            spaceType = $('#select2-spacesel-container').text(),
            spaceLayout = $("#spaceLayout1").val(),
            spaceSize = $("#select2-sizesel-container").text(),
            propertyType = $("#designproperty").val(),
            spaceDesignStyle = $("#select2-designsel-container").text(),
            spaceColorStyle = $("#select2-designcolorstylesel-container").text(),
            spaceFeature = $("#featureselhidden").val(),
            furnitureJson = $("#furselected").val(),
            decorJson = $("#decorhidden").val(),

            wallTreatmentType = $("#walltypehidden").val(),
            wallMaterial = $("#designmaterialselhidden").val(),
            wallFinishMaterial = $("#designfinishhidden").val(),
            wallFinishSwatch = $("#swatchesimagepath").val(),

            ceilingType = $("#select2-ceiltypesel-container").text(),
            ceilingMaterial = $("#designceilmaterialhidden").val(),
            ceilingFinishMaterial = $("#designceilfinishhidden").val(),
            ceilingFinishSwatch = $("#ceilswatchesimagepath").val(),

            flooringMaterial = $("#designfloormaterialhidden").val(),
            flooringFinish = $("#designfloorfinishhidden").val(),
            flooringFinishSwatch = $("#flooringimagepath").val(),

            lighting = $("#select2-lightsel-container").text(),
            designElement = $("#designelement").val(),

            user1 = $("#select2-userpersonasel-container").text(),
            hobby1 = $("#select2-hobbypersonasel-container").text(),
            profession1 = $("#select2-professionpersonasel-container").text(),
            user2 = $("#select2-userpersonatwo-container").text(),
            hobby2 = $("#select2-hobbypersonatwo-container").text(),
            profession2 = $("#select2-professionpersonatwo-container").text(),
            ageGroup = $("#select2-agepersona-container").text(),
            religion = $("#select2-religionpersona-container").text(),
            community = $("#select2-communitypersona-container").text(),
            designerstory = $("#designerstory").val();

            console.log("@@@@@@@@@@@@@@@@@@@@@@@   Updated Data @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");

            console.log("spaceName------"+spaceName+"---------webDisplayName------"+webDisplayName+"------------dFileLocation------"+dFileLocation+"----------primaryImageLocation------"+primaryImageLocation+"---------secondaryImageLocationJson------"+secondaryImageLocationJson+"---------l0Location------"+l0Location+"---------moodBoardLocation------"+moodBoardLocation+"---------conceptNote------"+conceptNote);

            console.log("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");

            console.log("spaceType------"+spaceType+"---spaceLayout---"+spaceLayout+"---spaceSize---"+spaceSize+"---propertyType---"+propertyType+"---spaceDesignStyle---"+spaceDesignStyle+"---spaceColorStyle---"+spaceColorStyle+"---spaceFeature---"+spaceFeature+"---furnitureJson---"+furnitureJson+"---decorJson---"+decorJson);

            console.log("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");

            console.log("wallTreatmentType------"+wallTreatmentType+"---wallMaterial---"+wallMaterial+"---wallFinishMaterial---"+wallFinishMaterial+"---wallFinishSwatch---"+wallFinishSwatch);

            console.log("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");

            console.log("ceilingType------"+ceilingType+"---ceilingMaterial---"+ceilingMaterial+"---ceilingFinishMaterial---"+ceilingFinishMaterial+"---ceilingFinishSwatch---"+ceilingFinishSwatch);

            console.log("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");

            console.log("flooringMaterial------"+flooringMaterial+"---flooringFinish---"+flooringFinish+"---flooringFinishSwatch---"+flooringFinishSwatch);

            console.log("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");

            console.log("lighting------"+lighting+"---designElement---"+designElement);

            console.log("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");

            console.log("lighting------"+lighting+"---designElement---"+designElement);

            console.log("@@@@@@@@@@@@@@@@@@@@@@@   Updated Data END @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");

         that.updatedesigns.fetch({
             data : {
                   id: that.filter.get('designId'),
                  spaceName: spaceName,
                  webDisplayName: webDisplayName,
                  dFileLocation: dFileLocation,
                  primaryImageLocation: primaryImageLocation,
                  secondaryImageLocationJson: secondaryImageLocationJson,
                  l0Location: l0Location,
                  moodBoardLocation:moodBoardLocation,
                  conceptNote: conceptNote,
                  spaceType: spaceType,
                  spaceLayout: spaceLayout,
                  spaceSize: spaceSize,
                  propertyType: propertyType,
                  spaceDesignStyle: spaceDesignStyle,
                  spaceColorStyle: spaceColorStyle,
                  spaceFeature:  spaceFeature,
                  furnitureJson: furnitureJson,
                  decorJson: decorJson,
                  wallTreatmentType: wallTreatmentType,
                  wallMaterial: wallMaterial,
                  wallFinishMaterial: wallFinishMaterial,
                  wallFinishSwatch: wallFinishSwatch,
                  ceilingType: ceilingType,
                  ceilingMaterial: ceilingMaterial,
                  ceilingFinishMaterial: ceilingFinishMaterial,
                  ceilingFinishSwatch: ceilingFinishSwatch,
                  flooringMaterial: flooringMaterial,
                  flooringFinish: flooringFinish,
                  flooringFinishSwatch: flooringFinishSwatch,
                  lighting: lighting,
                  designElement: designElement,
                  user1: user1,
                  hobby1: hobby1,
                  profession1: profession1,
                  user2: user2,
                  hobby2: hobby2,
                  profession2: profession2,
                  ageGroup: ageGroup,
                  religion: religion,
                  community: community,
                  designerstory: designerstory
             },
             success: function(response) {
                 console.log("Successfully updated design");
                 console.log(response);
             },
             error: function(model, response, options) {
                 console.log("error while updating design - " + response);
                 console.log(response);
             }
         });
         that.render();
     },
     ready: function(){
        var that = this;
        var designId = this.model.id;

        that.filter.set({
             'designId':designId
         }, {
             silent: true
         });

        this.pdesigns.fetch({
          data: {
            id: designId
          },
          success: function(response) {
            var response = response.toJSON();

            console.log("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
            console.log(response);
            console.log("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
            $('#select2-spacesel-container').text(response[0].spaceType);
            $('#select2-sizesel-container').text(response[0].spaceSize);
            $('#select2-designsel-container').text(response[0].spaceDesignStyle);
            $('#select2-designcolorstylesel-container').text(response[0].spaceColorStyle);
            $('#select2-lightsel-container').text(response[0].lighting);
            $('#designelement').val(response[0].designElement);
            $('#select2-userpersonasel-container').text(response[0].user1);
            $('#select2-hobbypersonasel-container').text(response[0].hobby1);
            $('#select2-professionpersonasel-container').text(response[0].profession1);
            $('#select2-userpersonatwo-container').text(response[0].user2);
            $('#select2-hobbypersonatwo-container').text(response[0].hobby2);
            $('#select2-professionpersonatwo-container').text(response[0].profession2);
            $('#select2-agepersona-container').text(response[0].ageGroup);
            $('#select2-religionpersona-container').text(response[0].religion);
            $('#select2-communitypersona-container').text(response[0].community);
            $('#designerstory').val(response[0].designerstory);

             $('#designproperty').val(response[0].propertyType);

            $("#designspacename").val(response[0].spaceName);
            $("#designwebname").val(response[0].webDisplayName);
            $("#designdfile").val(response[0].dFileLocation);
            $("#designconceptnote").val(response[0].conceptNote);
            $("#primimage").attr('src', response[0].primaryImageLocation);
            var secondaryImageLocationJson = JSON.parse(response[0].secondaryImageLocationJson);
            var secondaryImageLoc = secondaryImageLocationJson.secondaryimage;
            $("#secondimage").attr('src', secondaryImageLoc);
            $("#l0LocationImg").attr('src', response[0].l0Location);
            $("#moodBoardLocation").attr('src', response[0].moodBoardLocation);
            $("#primaryimagepath").val(response[0].primaryImageLocation);
            $("#secondary_block").attr('src', response[0].secondaryImageLocationJson);
            $("#secondaryimagepath").val(response[0].secondaryImageLocationJson);

            $("#doc1").val(response[0].l0Location);
            $("#designmoodboard1").val(response[0].moodBoardLocation);

            if(typeof(response[0].spaceLayout) !== 'undefined' && response[0].spaceLayout !=""){
                $("#spaceLayout1").val(response[0].spaceLayout);
                var spaceLayoutJson = JSON.parse(response[0].spaceLayout);
                var spacelayoutImageLoc = spaceLayoutJson.spacelayout;
                $("#spaceLayoutImg").attr('src', spacelayoutImageLoc);
            }

            if(typeof(response[0].wallFinishSwatch) !== 'undefined' && response[0].wallFinishSwatch !=""){
                $("#swatchesimagepath").val(response[0].wallFinishSwatch);
                var swatchesimageJson = JSON.parse(response[0].wallFinishSwatch);
                var swatchesimageImageLoc = swatchesimageJson.swatchesimage;
                $("#FinishswatchesImg").attr('src', swatchesimageImageLoc);
            }


            if(typeof(response[0].ceilingFinishSwatch) !== 'undefined' && response[0].ceilingFinishSwatch !=""){
                 $("#ceilswatchesimagepath").val(response[0].ceilingFinishSwatch);
                var ceilingFinishSwatchJson = JSON.parse(response[0].ceilingFinishSwatch);
                var ceilingFinishSwatchImageLoc = ceilingFinishSwatchJson.ceilswatchesimage;
                $("#ceilFinishswatchesImg").attr('src', ceilingFinishSwatchImageLoc);
            }

            if(typeof(response[0].flooringFinishSwatch) !== 'undefined' && response[0].flooringFinishSwatch !=""){
                 $("#flooringimagepath").val(response[0].flooringFinishSwatch);
                var flooringFinishSwatchJson = JSON.parse(response[0].flooringFinishSwatch);
                var flooringFinishSwatchImageLoc = flooringFinishSwatchJson.floorswatchimage;
                $("#flooringswatchesImg").attr('src', flooringFinishSwatchImageLoc);
            }

            if(typeof(response[0].spaceType) !== 'undefined' && response[0].spaceType !=""){
                $("#spaceselinput").val(response[0].spaceType);
                $("#spacesel").val(response[0].spaceType);
                //$("#spacesel").attr('selected',true);
            }

            /*$("#featuresel").select2({
                data: [{
                    id: 0,
                    text: '2015'
                }, {
                    id: 1,
                    text: '2014'
                }],
                val: ["0"]
            }).select2('val', [0,1]);*/


            /* Space Feature Start */
            if(typeof(response[0].spaceFeature) !== 'undefined' && response[0].spaceFeature !=""){
                var spaceFeature = JSON.parse(response[0].spaceFeature);
                var spaceFeatureArr = spaceFeature.SelectedFeature;

                var spaceFeatureTxtArr = [];
                var fListobj = that.filter.get('selectedSpacefeature');
                var dobj = [];
                _.each(fListobj, function(featurec, i){
                    var innerObj = {};
                    innerObj['id'] = fListobj[i].id;
                    innerObj['text'] = fListobj[i].title;
                    _.each(spaceFeatureArr, function(sft, jj){
                        if(spaceFeatureArr[jj].text.trim() == innerObj['text'].trim()){
                            spaceFeatureTxtArr.push(innerObj['id']);
                        }
                    });
                    dobj.push(innerObj);
                 });

                $("#featuresel").select2({
                    data: dobj,
                    val: ["2536"]
                }).select2('val', spaceFeatureTxtArr);
            }
            /* Space Feature End */

            /* Space Element Furniture Start */
            if(typeof(response[0].furnitureJson) !== 'undefined' && response[0].furnitureJson !=""){
                var furnitureJson = JSON.parse(response[0].furnitureJson);
                var sFurnitureObj = furnitureJson.SelectedFurniture;
                $("#furselectedHiddenList").val(JSON.stringify(sFurnitureObj));
                that.filter.set({
                     'selectedFurnitureList':sFurnitureObj
                 }, {
                     silent: true
                 });
            }
            /* Space Element Furniture End */

            /* Space Element Decor Start */
            if(typeof(response[0].decorJson) !== 'undefined' && response[0].decorJson !=""){
                var decorJson = JSON.parse(response[0].decorJson);
                var ddecorObj = decorJson.SelectedDecor;
                $("#decorselectedHiddenList").val(JSON.stringify(ddecorObj));
                that.filter.set({
                    'selectedDecorList':ddecorObj
                }, {
                    silent: true
                });
            }
            /* Space Element Decor End */

            /* Wall Treatment Type Start */
            if(typeof(response[0].wallTreatmentType) !== 'undefined' && response[0].wallTreatmentType !=""){
                var wallTreatmentTypeJson = JSON.parse(response[0].wallTreatmentType);
                var wallTreatmentTypeArr = wallTreatmentTypeJson.SelectedWall;
                $("#walltypehidden").val(JSON.stringify(wallTreatmentTypeArr));

                var walltypeListobj = that.filter.get('selectedWalltype');

                var dwalltypeObj = [];
                var wallTreatTxtArr = [];
                _.each(walltypeListobj, function(walltype, i){
                    var innerwalltypeObj = {};
                    innerwalltypeObj['id'] = walltypeListobj[i].id;
                    innerwalltypeObj['text'] = walltypeListobj[i].title;

                    _.each(wallTreatmentTypeArr, function(sft, jj){
                        if(wallTreatmentTypeArr[jj].text.toLowerCase().trim() == innerwalltypeObj['text'].toLowerCase().trim()){
                            wallTreatTxtArr.push(innerwalltypeObj['id']);
                        }
                     });
                    dwalltypeObj.push(innerwalltypeObj);
                 });

                $("#walltypesel").select2({
                    data: dwalltypeObj,
                    val:[1111]
                }).select2('val', wallTreatTxtArr);
            }
            /* Wall Treatment Type End */

            /* Wall Treatment Material Start */
            if(typeof(response[0].wallMaterial) !== 'undefined' && response[0].wallMaterial !=""){
                var wallMaterialJson = JSON.parse(response[0].wallMaterial);
                var wallMaterialArr = wallMaterialJson.wallmaterial;
                $("#designmaterialselhidden").val(JSON.stringify(wallMaterialArr));

                var walltypeListobj = that.filter.get('selectedDesignMaterial');

                var  selobj = that.getSelJsonValue(walltypeListobj, wallMaterialArr);

                $("#designmaterialsel").select2({
                    data: selobj[0],
                    val:[1111]
                }).select2('val', selobj[1]);
            }
            /* Wall Treatment Material End */

            /* Wall Treatment Finish Start */
            if(typeof(response[0].wallFinishMaterial) !== 'undefined' && response[0].wallFinishMaterial !=""){
                var wallFinishMaterialJson = JSON.parse(response[0].wallFinishMaterial);
                var wallFinishMaterialArr = wallFinishMaterialJson.wallfinish;
                $("#designfinishhidden").val(JSON.stringify(wallFinishMaterialArr));

                var designfinishListobj = that.filter.get('selectedDesignFinishType');
                var  wallFinishselobj = that.getSelJsonValue(designfinishListobj, wallFinishMaterialArr);

                $("#designfinish").select2({
                    data: wallFinishselobj[0],
                    val:[1111]
                }).select2('val', wallFinishselobj[1]);
            }
            /* Wall Treatment Finish End */

            /* Ceiling Treatment Type Start */
            if(typeof(response[0].ceilingType) !== 'undefined' && response[0].ceilingType != ""){
                var ceilingType = response[0].ceilingType;
                var ceiltypeselListobj = that.filter.get('selectedCeilingType');

                var ceiltypeObj = [];
                var ceiltypeTxtArr = [];
                var ceilTxtNmArr = [];
                _.each(ceiltypeselListobj, function(ceiltype, i){
                    var innerceiltypeObj = {};
                    innerceiltypeObj['id'] = ceiltypeselListobj[i].id;
                    innerceiltypeObj['text'] = ceiltypeselListobj[i].title;
                    var tempvl=null;
                    if(ceilingType == innerceiltypeObj['text']){
                        var xx = ceilingType;
                        if(($.inArray( xx, ceilTxtNmArr ) == -1) && (xx != tempvl)){
                            ceiltypeTxtArr.push(innerceiltypeObj['id']);
                            ceilTxtNmArr.push(xx);
                        }
                    }
                    tempvl=xx;
                    ceiltypeObj.push(innerceiltypeObj);
                 });

                $("#ceiltypesel").select2({
                    data: ceiltypeObj,
                    val:[1111]
                }).select2('val', ceiltypeTxtArr);
            }
            /* Ceiling Treatment Type End */

            /* Ceiling Treatment Material Start */
            if(typeof(response[0].ceilingMaterial) !== 'undefined' && response[0].ceilingMaterial !=""){
                var ceilingMaterialJson = JSON.parse(response[0].ceilingMaterial);
                var ceilingMaterialArr = ceilingMaterialJson.ceilingmaterial;

                var designceilmaterialListobj = that.filter.get('selectedDesignMaterial');
                var ceilingMaterialobj = that.getSelJsonValue(designceilmaterialListobj, ceilingMaterialArr);

                $("#designceilmaterial").select2({
                    data: ceilingMaterialobj[0],
                    val:[1111]
                }).select2('val', ceilingMaterialobj[1]);
            }
            /* Ceiling Treatment Material End */

            /* Ceiling Treatment Finish Start */
            if(typeof(response[0].ceilingFinishMaterial) !== 'undefined' && response[0].ceilingFinishMaterial != ""){
                var ceilingFinishMaterialJson = JSON.parse(response[0].ceilingFinishMaterial);
                var ceilingFinishMaterialArr = ceilingFinishMaterialJson.ceilingfinish;

                var designceilfinishListobj = that.filter.get('selectedDesignFinishType');
                var ceilingfinishobj = that.getSelJsonValue(designceilfinishListobj, ceilingFinishMaterialArr);

                $("#designceilfinish").select2({
                    data: ceilingfinishobj[0],
                    val:[1111]
                }).select2('val', ceilingfinishobj[1]);
            }
            /* Ceiling Treatment Finish End */

            /* Flooring Material Start */
            if(typeof(response[0].flooringMaterial) !== 'undefined' && response[0].flooringMaterial != ""){
                var flooringMaterialJson = JSON.parse(response[0].flooringMaterial);
                var flooringMaterialArr = flooringMaterialJson.floormaterial;

                var designfloormaterialListobj = that.filter.get('selectedDesignMaterial');
                var floormaterialobj = that.getSelJsonValue(designfloormaterialListobj, flooringMaterialArr);

                $("#designfloormaterial").select2({
                    data: floormaterialobj[0],
                    val:[1111]
                }).select2('val', floormaterialobj[1]);
            }
            /* Flooring Material End */

            /* Flooring Finish Start */
            if(typeof(response[0].flooringFinish) !== 'undefined' && response[0].flooringFinish != ""){
                var flooringFinishJson = JSON.parse(response[0].flooringFinish);
                var flooringFinishArr = flooringFinishJson.floorfinish;

                var designfloorfinishListobj = that.filter.get('selectedDesignFinishMaterial');
                var  floorfinishobj = that.getSelJsonValue(designfloorfinishListobj, flooringFinishArr);

                $("#designfloorfinish").select2({
                    data: floorfinishobj[0],
                    val:[1111]
                }).select2('val', floorfinishobj[1]);
            }

            that.getPredefinedVal();
          },
          error: function(model, response, options) {
              console.log("couldn't fetch product data - " + response);
          }
        });


     },
     getSelJsonValue: function(selObj, selArr){
        var dselObj = [];
        var selTxtArr = [];
        var selTxtNmArr = [];

        _.each(selObj, function(seltype, i){
            var innerObj = {};
            innerObj['id'] = selObj[i].id;
            innerObj['text'] = selObj[i].title.trim();
                var tempvl=null;
            _.each(selArr, function(sft, jj){
                if(selArr[jj].text.toLowerCase().trim() == innerObj['text'].toLowerCase()){
                    var xx = selArr[jj].text.toLowerCase().trim();
                    if(($.inArray( xx, selTxtNmArr ) == -1) && (xx != tempvl)){
                        selTxtArr.push(innerObj['id']);
                        selTxtNmArr.push(xx);
                    }
                }
                tempvl=xx;
             });
            dselObj.push(innerObj);
         });

         return [dselObj, selTxtArr];
     },
     getPredefinedVal: function(){
        var that = this;
        var selCat = $('#select2-spacesel-container').text();
        var CategoryJson = that.filter.get('selectedSpaceType');

        console.log("@@@@@@@@@@@ CategoryJson @@@@@@@@@@@");
        console.log(CategoryJson);
        console.log("@@@@@@@@@@@@@@@@@@@@@@");

        var catId = that.findBySpecField(CategoryJson, 'title', selCat, 'code');
        console.log("@@@@@@@@@@@ catId @@@@@@@@@@@");
        console.log(catId);
        console.log("@@@@@@@@@@@@@@@@@@@@@@");
        that.GetSelectedTextValue(catId);
     },
     findBySpecField: function(data, reqField, value, resField){
        for (var i = 0; i < data.length; i++) {
            if (data[i][reqField] == value) {
                return(data[i][resField]);
            }
        }
        return '';
     },
     setFurnitureHideVal: function(){
        var that = this;
        var furListobj = that.filter.get('allFurnitureList');
        var sFurnitureobj = that.filter.get('selectedFurnitureList');

        console.log("@@@@@@@@@@@ Furnitureobj @@@@@@@@@@@");
        console.log(furListobj);
        console.log("@@@@@@@@@@@@@@@@@@@@@@");


        console.log("@@@@@@@@@@@ sFurnitureobj @@@@@@@@@@@");
        console.log(sFurnitureobj);
        console.log("@@@@@@@@@@@@@@@@@@@@@@");

        var dfurobj = [];
        var sFurnitureTxtArr = [];

        /*var furListobj = {};
        if(typeof(furList) !== 'undefined' && furList != ""){
            var furListobj = $.parseJSON(furList);
        }*/
        _.each(furListobj, function(fur, i){
            var innerfurObj = {};
            innerfurObj['id'] = furListobj[i].id;
            innerfurObj['text'] = furListobj[i].title;

            _.each(sFurnitureobj, function(sft, jj){
                if(typeof(sFurnitureobj[jj].text) != "undefined" && sFurnitureobj[jj].text == furListobj[i].title){
                    sFurnitureTxtArr.push(furListobj[i].id);
                }
             });
            dfurobj.push(innerfurObj);
         });

        $("#furnituresel").select2({
            data: dfurobj,
            val:[1869]
        }).select2('val', sFurnitureTxtArr);
     },
     setDecorHideVal: function(){
        var that = this;
        var decorListobj = that.filter.get('allDecorList');
        var ddecorobj = that.filter.get('selectedDecorList');

                console.log("@@@@@@@@@@@ decorListobj @@@@@@@@@@@");
                console.log(decorListobj);
                console.log("@@@@@@@@@@@@@@@@@@@@@@");


                console.log("@@@@@@@@@@@ ddecorobj @@@@@@@@@@@");
                console.log(ddecorobj);
                console.log("@@@@@@@@@@@@@@@@@@@@@@");

        var ddecobj = [];
        var decorListTxtArr = [];
        if(typeof(decorListobj) !== 'undefined'){

        _.each(decorListobj, function(dec, i){
            var innerdecObj = {};
            innerdecObj['id'] = decorListobj[i].id;
            innerdecObj['text'] = decorListobj[i].title;
            _.each(ddecorobj, function(sft, jj){
                if(ddecorobj[jj].text.toLowerCase().trim() == innerdecObj['text'].toLowerCase().trim()){
                    decorListTxtArr.push(decorListobj[i].id);
                }
             });
            ddecobj.push(innerdecObj);
         });
        }

        $("#decorsel").select2({
            data: ddecobj,
            val:[1869]
        }).select2('val', decorListTxtArr);
     }
  });
  return DashboardPage;
});
