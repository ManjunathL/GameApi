define([
  'jquery',
  'lodash',
  'backbone',
  'bootstrap',
  'pinterest_grid',
  'text!templates/dashboard/search_concept.html',
  'text!templates/dashboard/searchConceptdetails.html',
  'text!templates/dashboard/searchConceptlists.html',
  'text!templates/dashboard/similarconcepts.html',
  'text!templates/dashboard/relatedconcepts.html',
  'text!templates/dashboard/spaceelements.html',
  'collections/spaceelements',
  'models/filter',
  'collections/search_concepts',
  'collections/conceptboards',
  'collections/add_conceptToCboards',
  'collections/similar_concepts',
  'collections/related_concepts'
], function($, _, Backbone, Bootstrap, pinterest_grid, SearchConceptTemplate,searchConceptdetailsPageTemplate, searchConceptlitsPageTemplate, similarconceptPageTemplate, relatedconceptPageTemplate, spaceTempPageTemplate, SpaceElements, Filter, SearchConcepts, ConceptBoards, AddConceptToCboards, SimilarConcepts, RelatedConcepts){
  var SearchConceptPage = Backbone.View.extend({
    el: '.page',
    search_concepts:null,
    conceptboards: null,
    similar_concepts: null,
    related_concepts: null,
    filter: null,
    spaceelements:null,
    initialize: function() {
        this.search_concepts = new SearchConcepts();
        this.conceptboards = new ConceptBoards();
        this.add_conceptToCboards = new AddConceptToCboards();
        this.similar_concepts = new SimilarConcepts();
        this.related_concepts = new RelatedConcepts();
        this.filter = new Filter();
        this.spaceelements = new SpaceElements();
        this.listenTo(Backbone);
        this.filter.on('change', this.render, this);
        _.bindAll(this, 'render', 'fetchRelatedConceptAndRender');
    },
    render: function () {
        var that = this;

        $("#searchpinBoot111").html('');

        window.filter = that.filter;
        console.log("+++++++++++++ search term ++++++++++++++");
        console.log(that.model.searchTerm);


        var searchTerm = that.model.searchTerm;
        var spaceTypeCode = that.model.name;
        this.filter.set({
            'selectedspaceTypeCode':spaceTypeCode
        }, {
            silent: true
        });

         $("#searchInput1").val(searchTerm);
        var getsearchConceptsPromise = that.getsearchConcepts(searchTerm);
        var getConceptBoardsPromise = that.getConceptBoards();

        Promise.all([getsearchConceptsPromise,getConceptBoardsPromise]).then(function() {
            console.log("@@@@@@@@@@@@@ In side Promise @@@@@@@@@@@@@@@@@@");
            that.rendersub();
        });


    },
    getConceptBoards: function(){
          var that = this;
          //var userId = "user1234600";
          debugger
          var userId = sessionStorage.userId
          var userProjectId = sessionStorage.defaultMindboardId;
/*          var userProjectId = sessionStorage.userProjectId;*/
          var pageno = 0;
          var itemPerPage = 20;
          var filterId = 1;
          return new Promise(function(resolve, reject) {
               that.conceptboards.getConceptBoardList(userId, userProjectId, pageno, itemPerPage,filterId, {
                   async: true,
                   crossDomain: true,
                   method: "GET",
                   headers:{
                       "authorization": "Bearer "+ sessionStorage.authtoken
                   },
                   success:function(data) {
                       //console.log(" +++++++++++++++ Concept Boards ++++++++++++++++++ ");
                       //console.log(data);
                       $(".modal-open").css("overflow", "scroll");
                       $(".modal-backdrop.in").css("opacity", "0");

                       resolve();
                   },
                   error:function(response) {
                       //console.log(" +++++++++++++++ Errrorr ++++++++++++++++++ ");
                       //console.log(response);
                       reject();
                   }
               });
          });
        },
    addConcept2Cboard: function (e) {
            if (e.isDefaultPrevented()) return;
            e.preventDefault();

            var currentTarget = $(e.currentTarget);
            var conceptboardId = currentTarget.data('element');

            //var userId = sessionStorage.userId;
            var userConceptCode = $('#ConcCod').val();
            var spaceElementCode=$('#spaceElementCodeTxt').val();

             var formData = {
                 "conceptboardId": conceptboardId,
                 "userConceptCode": userConceptCode,
                 "spaceElementCode": spaceElementCode

            };

            var that = this;

            that.add_conceptToCboards.getaddConceptToCBoard(conceptboardId, userConceptCode, spaceElementCode,{
               async: true,
               crossDomain: true,
               method: "POST",
               headers:{
                   "authorization": "Bearer "+ sessionStorage.authtoken,
                   "Content-Type": "application/json"
               },
               success:function(response) {
                   console.log("Successfully save Concept to Concept board- ");
                   console.log(response);
                   $("#pin-modal").modal('hide');
                   return;
               },
               error:function(response) {
                   console.log(" +++++++++++++++ save Concept to Concept board- Errrorr ++++++++++++++++++ ");
                   console.log(response);
               }
           });
        },
    getsearchConcepts: function(searchTerm){
        var that = this;

        var userId = sessionStorage.userId;
        var formData = {
                "itemPerPage": 10,
                "pageNo": 0,
                "searchText": searchTerm,
                "searchType": 1,
                "spaceTypeCode": "",
                "userId": userId
        };


        console.log(formData);

        return new Promise(function(resolve, reject) {
        if(!that.search_concepts.get('id')){
            that.search_concepts.fetch({
               async: true,
               crossDomain: true,
               method: "POST",
               headers:{
                   "authorization": "Bearer "+ sessionStorage.authtoken,
                   "Content-Type": "application/json"
               },
               data: JSON.stringify(formData),
               success:function(response){
                    console.log("Successfully Searched... ");
                    console.log(response);
                    /*$("#snackbar").html("Successfully searched ...");
                      var x = document.getElementById("snackbar")
                      x.className = "show";
                      setTimeout(function(){ x.className = x.className.replace("show", ""); }, 3000);*/
                    resolve();
               },
               error:function(response) {
                    console.log(" +++++++++++++++ Search- Errrorr ++++++++++++++++++ ");
                    console.log(JSON.stringify(response));
                    console.log("%%%%%%%%% response%%%%%%%%%%%%%%%%");
                    console.log(response.responseJSON.errorMessage);

                    /*$("#snackbar").html(response.responseJSON.errorMessage);
                    var x = document.getElementById("snackbar")
                    x.className = "show";
                    setTimeout(function(){ x.className = x.className.replace("show", ""); }, 3000);*/
                    reject();
               }
            });
        }else{
            reject();
        }
        });
    },
    getRelatedConceptDetails: function(evt){
            var currentTarget = $(evt.currentTarget);

            var cconceptId = currentTarget.data('element4');

            var cconceptCode = currentTarget.data('element5');
            var that = this;

            var relatedConceptsListObj = that.related_concepts;
            relatedConceptsListObj = relatedConceptsListObj.toJSON();

            console.log("@@@@@@@@@@@@@@@@@ relatedConceptsList @@@@@@@@@@@@@@@@@@@@");
            console.log(relatedConceptsListObj);
            console.log("@@@@@@@@@@@@@@@@@ cconceptCode @@@@@@@@@@@@@@@@@@@@");
            console.log(cconceptCode);

            if(typeof(cconceptCode) !== 'undefined'){
            var conceptdtls = that.related_concepts.getRelatedConcept(relatedConceptsListObj[0].relatedConceptsList,cconceptCode);
            console.log("@@@@@@@@@@@@@@@@@ Inside Related Concept @@@@@@@@@@@@@@@@@@@@");
            }
            console.log("@@@@@@@@@@@@@@@@@ conceptdtls @@@@@@@@@@@@@@@@@@@@");
            console.log(conceptdtls);
            console.log("@@@@@@@@@@@@@@@@@ conceptCode @@@@@@@@@@@@@@@@@@@@");
            console.log(conceptdtls[0].conceptCode);

            var imgnm = '', concDes = '', cnpnm = '', cconceptCode = '', userNote = '', userTag = '';
            if(typeof(conceptdtls) !== 'undefined'){
            imgnm = conceptdtls[0].imgURL;

            //        var consTitle = conceptdtls[0].conceptTitle;
            //        var consTitle = "Chilika Lake is a brackish water lagoon, spread over the Puri, Khurda and Ganjam districts of Odisha state on the east coast of India, at the mouth of the Daya River, flowing into the Bay of Bengal, covering an area of over 1,100 km";
            var consTitle = conceptdtls[0].conceptDetails;
            var n = consTitle.length;
            if (n > 60){
            concDes = consTitle.slice(0, 60) +' <a href="javascript:void(0);" class="color-orange read_full sidebar-box black" ><a href="javascript:void(0);" onclick="showDesc()" class="button readMore" style="">Read More...</a></p></a>';

            } else {
            concDes = consTitle;
            }

            concFullDes = consTitle;

            cnpnm = conceptdtls[0].conceptTitle;
            //        cnpnm = conceptdtls[0].conceptTitle;
            cconceptCode = conceptdtls[0].conceptCode;
            userNote = conceptdtls[0].userNote;
            userTag = conceptdtls[0].userTag;
            }

            var pageno = 0;
            var itemPerPage = 50;

            var conceptboardId = that.filter.get('selectedconceptboardId');
            var sspaceTypeCode = that.filter.get('selectedspaceTypeCode');


            var vconceptCode = cconceptCode;
            var relconceptCode = cconceptCode;

            $("#concNmm").text(cnpnm);
            $("#concDes").html(concDes);
            $("#concFullDes").html(concFullDes);
            $("#ConcCod").val(cconceptCode);
            $("#show_description11").attr("data-element",cconceptId);
            $("#show_description11").attr("data-element1",consTitle);


            $("#conId").val(cconceptId);
            $("#mastImgg").attr("src",imgnm);
            $("#mastImg1").attr("src",imgnm);

            if(typeof(userNote) != 'undefined' && userNote != null){
            $("#conceptNotetxt").html(userNote);
            $("#save_CNote").text("Edit Notes");
            }else{
            $("#conceptNotetxt").html('');
            $("#save_CNote").text("Add Notes");
            }
            //     $("#conceptNotetxt").text(userNote);

            if(typeof(userTag) != 'undefined' && userTag != null){
            var arr = userTag.split(",");
            var taglists = '';
            for( var i=0; i< arr.length; i++ ){
                taglists += '<span class="tag label label-info">'+arr[i]+'<span data-role="remove"></span></span>';
            }
            $("#tagtxt").val(userTag);
            $("#taglists").html(taglists);
            }else{
            $("#tagtxt").html('');
            }

            $('#details-modal').modal('show');


            var similarConceptDtlsPromise = that.getsimilarConcepts(conceptboardId, vconceptCode, pageno, itemPerPage);
            var relatedConceptDtlsPromise = that.getrelateConcepts(conceptboardId, relconceptCode, sspaceTypeCode, pageno, itemPerPage);

            Promise.all([similarConceptDtlsPromise,relatedConceptDtlsPromise]).then(function() {
             console.log("@@@@@@@@@@@@@ In side Concept Promise @@@@@@@@@@@@@@@@@@");
             that.fetchRelatedConceptAndRender();
            });
        },
    getSimilarConceptDetails: function(evt){
        var currentTarget = $(evt.currentTarget);

        var cconceptId = currentTarget.data('element4');

        var cconceptCode = currentTarget.data('element5');
        var that = this;

        var similarConceptsListObj = {};

        var similarConceptsListObj = that.similar_concepts;
             similarConceptsListObj = similarConceptsListObj.toJSON();

             console.log("@@@@@@@@@@@@@@@@@ similarConceptsList @@@@@@@@@@@@@@@@@@@@");
                  console.log(similarConceptsListObj);

        /*if(typeof(cconceptCode) !== 'undefined' && relatedConceptsListObj != ""){
        var conceptdtls = that.related_concepts.getRelatedConcept(relatedConceptsListObj[0].relatedConceptsList,cconceptCode);
        console.log("@@@@@@@@@@@@@@@@@ Inside Related Concept @@@@@@@@@@@@@@@@@@@@");
        }else{
        var conceptdtls = that.conceptlists.getConcept(conceptlists[0].listOfConceptBoardConcept,cconceptId);
        }*/

        if(typeof(cconceptCode) !== 'undefined'){
        var conceptdtls = that.similar_concepts.getSimilarConcept(similarConceptsListObj[0].similarConceptsList,cconceptCode);
        console.log("@@@@@@@@@@@@@@@@@ Inside Similar Concept @@@@@@@@@@@@@@@@@@@@");
        }
        console.log("@@@@@@@@@@@@@@@@@ conceptdtls @@@@@@@@@@@@@@@@@@@@");
        console.log(conceptdtls);
        console.log("@@@@@@@@@@@@@@@@@ conceptCode @@@@@@@@@@@@@@@@@@@@");
        console.log(conceptdtls[0].conceptCode);

        var imgnm = '', concDes = '', cnpnm = '', cconceptCode = '', userNote = '', userTag = '';
        if(typeof(conceptdtls) !== 'undefined'){
        imgnm = conceptdtls[0].imgURL;

        //        var consTitle = conceptdtls[0].conceptTitle;
        //        var consTitle = "Chilika Lake is a brackish water lagoon, spread over the Puri, Khurda and Ganjam districts of Odisha state on the east coast of India, at the mouth of the Daya River, flowing into the Bay of Bengal, covering an area of over 1,100 km";
        var consTitle = conceptdtls[0].conceptDetails;
        var n = consTitle.length;
        if (n > 60){
        concDes = consTitle.slice(0, 60) +' <a href="javascript:void(0);" class="color-orange read_full sidebar-box black" ><a href="javascript:void(0);" onclick="showDesc()" class="button readMore" style="">Read More...</a></p></a>';

        } else {
        concDes = consTitle;
        }
        concFullDes = consTitle;

        cnpnm = conceptdtls[0].conceptTitle;
        //        cnpnm = conceptdtls[0].conceptTitle;
        cconceptCode = conceptdtls[0].conceptCode;
        userNote = conceptdtls[0].userNote;
        userTag = conceptdtls[0].userTag;
        }

        var pageno = 0;
        var itemPerPage = 20;

        var conceptboardId = that.filter.get('selectedconceptboardId');
        var sspaceTypeCode = that.filter.get('selectedspaceTypeCode');


        var vconceptCode = cconceptCode;
        var relconceptCode = cconceptCode;

        $("#concNmm").text(cnpnm);
        $("#concNm").text(cnpnm);
        $("#concNm1").text(cnpnm);
        $("#concDes").html(concDes);
        $("#concFullDes").html(concFullDes);
        $("#ConcCod").val(cconceptCode);
        $("#show_description11").attr("data-element",cconceptId);
        $("#show_description11").attr("data-element1",consTitle);


        $("#conId").val(cconceptId);
        $("#mastImgg").attr("src",imgnm);
        $("#mastImg1").attr("src",imgnm);

        if(typeof(userNote) != 'undefined' && userNote != null){
        $("#conceptNotetxt").html(userNote);
        $("#save_CNote").text("Edit Notes");
        }else{
        $("#conceptNotetxt").html('');
        $("#save_CNote").text("Add Notes");
        }
        //     $("#conceptNotetxt").text(userNote);

        if(typeof(userTag) != 'undefined' && userTag != null){
        var arr = userTag.split(",");
        var taglists = '';
        for( var i=0; i< arr.length; i++ ){
            taglists += '<span class="tag label label-info">'+arr[i]+'<span data-role="remove"></span></span>';
        }
        $("#tagtxt").val(userTag);
        $("#taglists").html(taglists);
        }else{
        $("#tagtxt").html('');
        }

        $('#details-modal').modal('show');


        var similarConceptDtlsPromise = that.getsimilarConcepts(conceptboardId, vconceptCode, pageno, itemPerPage);
        var relatedConceptDtlsPromise = that.getrelateConcepts(conceptboardId, relconceptCode, sspaceTypeCode, pageno, itemPerPage);

        Promise.all([similarConceptDtlsPromise,relatedConceptDtlsPromise]).then(function() {
         console.log("@@@@@@@@@@@@@ In side Concept Promise @@@@@@@@@@@@@@@@@@");
         that.fetchRelatedConceptAndRender();
        });
    },
    getsimilarConcepts: function(conceptboardId, vconceptCode, pageno, itemPerPage){
     var that = this;
     return new Promise(function(resolve, reject) {
          if (!that.similar_concepts.get('id')) {
             that.similar_concepts.getSimilarConceptsList(conceptboardId, vconceptCode, pageno, itemPerPage, {
                async: true,
                crossDomain: true,
                method: "GET",
                headers:{
                    "authorization": "Bearer "+ sessionStorage.authtoken
                },
                success:function(response) {
                    //console.log("Successfully fetch "+ currTab  +" Concepts - ");
                    //console.log(response);
                    console.log(" ============= Similar Concept =============== ");
                    console.log(response);
                    resolve();
                },
                error:function(response) {
                    console.log(" +++++++++++++++ Errrorr ++++++++++++++++++ ");
                    console.log(response);
                    reject();
                }
            });
          }else{
             resolve();
          }
     });
    },
    getrelateConcepts: function(conceptboardId, vconceptCode, sspaceTypeCode, pageno, itemPerPage){
     var that = this;
     return new Promise(function(resolve, reject) {
          if (!that.related_concepts.get('id')) {
              that.related_concepts.getRelatedConceptsList(conceptboardId, vconceptCode, sspaceTypeCode, pageno, itemPerPage, {
                 async: true,
                 crossDomain: true,
                 method: "GET",
                 headers:{
                     "authorization": "Bearer "+ sessionStorage.authtoken
                 },
                 success:function(response) {
                     //console.log("Successfully fetch "+ currTab  +" Concepts - ");
                     //console.log(response);
                     console.log(" ============= Related Concept =============== ");
                     console.log(response);
                     resolve();
                 },
                 error:function(response) {
                     console.log(" +++++++++++++++ Errrorr ++++++++++++++++++ ");
                     console.log(response);
                     reject();
                 }
             });
           }else{
              resolve();
           }
     });
    },
    viewSpaceTemplates: function(e){
        if (e.isDefaultPrevented()) return;
        e.preventDefault();
        var that = this;
        var currentTarget = $(e.currentTarget);
        var conceptboardId = currentTarget.data('element');
        var spaceTypeCode = currentTarget.data('element1');

        $("#owl-sptype1 .item").removeClass("active-Stypebox");
        $(currentTarget).addClass("active-Stypebox");

        $("#owl-sptype .item").removeClass("active-Stypebox");
        $(currentTarget).addClass("active-Stypebox");

        console.log(" +++++++++++++++ Space Type Templates++++++++++++++++++ ");
        console.log(spaceTypeCode);

        that.spaceelements.getSpaceElement(spaceTypeCode,{
            async: true,
            crossDomain: true,
            method: "GET",
            headers:{
                "authorization": "Bearer "+ sessionStorage.authtoken
            },
            success: function(response) {
                console.log(" +++++++++++++++ Space Type Templates++++++++++++++++++ ");
                console.log(response);
                that.fetchSpacetypesAndRender(conceptboardId);
                //resolve();
            },
            error: function(model, response, options) {
                //reject();
                console.log(" +++++++++++++++ Space Type Templates Error ++++++++++++++++++ ");
                console.log(response);
            }
        });

    },
    fetchSpacetypesAndRender: function(conceptboardId){
            var that = this;
            var spaceelements = that.spaceelements;

            $("#spaceTemp111").html(_.template(spaceTempPageTemplate)({
               "spacetemplates": spaceelements.toJSON(),
               "selectedconceptboardId": conceptboardId
           }));
           $("#spaceTemp11").html(_.template(spaceTempPageTemplate)({
              "spacetemplates": spaceelements.toJSON(),
              "selectedconceptboardId": conceptboardId
          }));
           $("#spaceTempUpload").html(_.template(spaceTempPageTemplate)({
                  "spacetemplates": spaceelements.toJSON(),
                  "selectedconceptboardId": conceptboardId
                 }));
        },

    rendersub: function(){
        var that = this;
         var search_concepts = that.search_concepts;

         var searchedConcepts = search_concepts.toJSON();
           var conceptboards = that.conceptboards;
           if (typeof(that.filter.get('noSearchFilterApplied')) == 'undefined') {
               that.filter.set({
                   'noSearchFilterApplied': '0'
               }, {
                   silent: true
               });
           }



           var filterApplied = that.filter.get('noSearchFilterApplied');

           var filterTag = that.filter.get('selectedSearchTag');

           var afterfilteredConcepts = {};

           if ((typeof(filterTag) !== 'undefined') && (filterTag.length != 0)) {

                var conceptObj = searchedConcepts[0].conceptSearchList;

               var filteredConcepts = that.search_concepts.filterBySearchTags(conceptObj.conceptList, filterTag);
                searchedConcepts[0].conceptSearchList.conceptList = filteredConcepts;
                searchedConcepts[0].conceptSearchList.tagList = conceptObj.tagList;

               that.filter.set({
                   'noSearchFilterApplied': '1'
               }, {
                   silent: true
               });
           } else {
               var filteredConcepts = searchedConcepts;
               that.filter.set({
                   'noSearchFilterApplied': '0'
               }, {
                   silent: true
               });
           }
        $(this.el).html(_.template(SearchConceptTemplate)({
             "conceptdetails": searchedConcepts[0].conceptSearchList,
             "conceptboardId": "1111",
             "conceptboardsDtls":conceptboards.toJSON(),
             "filterTag":filterTag
         }));
         $('#srchlist').html(_.template(searchConceptlitsPageTemplate)({
                      "conceptdetails": searchedConcepts[0].conceptSearchList,
                      "conceptboardId": "1111",
                      "conceptboardsDtls":conceptboards.toJSON(),
                      "filterTag":filterTag
                  }));
           $('#concept-dtls').html(_.template(searchConceptdetailsPageTemplate)({
                                "conceptboardsDtls":conceptboards.toJSON(),                                "filterTag":filterTag
                            }));
       // $('#concept-dtls').html(_.template(searchConceptdetailsPageTemplate));

        that.overlap();
    },
     overlap: function(){
          var that = this;
          var srcimages =  $(".simgaa2");
          var srcloadedImgNum = 0;
          srcimages.on('load', function(){
            srcloadedImgNum += 1;
            if (srcloadedImgNum == srcimages.length) {
              // here all images loaded, do your stuff
              console.log("here");
              that.ready();
            }
          });
     },

    ready: function(){
        $(function() {
           if($(".simgaa2").length > 0){
               $("#searchpinBoot222").pinterest_grid({
                    no_columns: 5,
                    padding_x: 20,
                    padding_y: 20,
                    margin_bottom: 50,
                    single_column_breakpoint: 700
                });
            }
        });

     },
     addConceptToCboard: function (e) {
             if (e.isDefaultPrevented()) return;
             e.preventDefault();

             var that = this;
             console.log(" +++++++++++++++ I am here  ++++++++++++++++++ ");
             var currentTarget = $(e.currentTarget);
             var conceptboardId = $('#conceptBoardIdTxt').val();

              //var conceptlists = that.conceptlists;
                  //conceptlists = conceptlists.toJSON();

             //var userId = sessionStorage.userId;
             var userConceptCode = $('#ConcCod').val();
             var spaceElementCode=$('#spaceElementCodeTxt').val();


              var formData = {
                  "conceptboardId": conceptboardId,
                  "userConceptCode": userConceptCode,
                  "spaceElementCode": spaceElementCode
             };
              console.log(" ++++++formData  ++++++++++++++++++ ");
              console.log(formData);
             that.add_conceptToCboards.getaddConceptToCBoard(conceptboardId,userConceptCode,spaceElementCode,{
                async: true,
                crossDomain: true,
                method: "POST",
                headers:{
                    "authorization": "Bearer "+ sessionStorage.authtoken,
                    "Content-Type": "application/json"
                },
                data: JSON.stringify(formData),
                success:function(response) {
                    console.log("Successfully save Concept to Concept board- ");
                    console.log(response);
                    //$("#pin-modal").modal('hide');
                    $("#pin-modal-conceptdtls").modal('hide');

                    $("#snackbar").html("Successfully save Concept Note");
                     var x = document.getElementById("snackbar")
                     x.className = "show";
                     setTimeout(function(){ x.className = x.className.replace("show", ""); }, 3000);
                     that.render();

                    //$('body').removeClass('modal-open');
                   //$('.modal-backdrop').remove();

                    //return;
                },
                error:function(response) {
                    console.log(" +++++++++++++++ save Concept to Concept board- Errrorr ++++++++++++++++++ ");
                    console.log(response);
                }
            });
         },
     events: {
          "click .boardlst": "addConcept2Cboard",
          "click .conceptImgg": "getConceptDetails",
          "click .relatedconceptImg": "getRelatedConceptDetails",
          "click .similarconceptImg": "getSimilarConceptDetails",
          "click .boardlst": "viewSpaceTemplates",
          "click #saveSpaveElement":"addConceptToCboard"

     },
     getConceptDetails: function(evt){
          var currentTarget = $(evt.currentTarget);
          var that = this;
          console.log(" ++++++++++++++++ I m here ++++++++++++++++++++++ ");

          console.log(currentTarget.data('element'));
          console.log(currentTarget.data('element1'));
          console.log(currentTarget.data('element2'));
          console.log(currentTarget.data('element3'));
          console.log(currentTarget.data('element4'));
          console.log(currentTarget.data('element5'));
          console.log(currentTarget.data('element6'));
          console.log(currentTarget.data('element7'));


          var imgnm = currentTarget.data('element');
          var concDes = currentTarget.data('element1');
          var conceptTitle = currentTarget.data('element2');
          var cconceptCode = currentTarget.data('element3');
          var conceptTypeCode = currentTarget.data('element4');
          var consTitle = currentTarget.data('element5');
          var spaceElementCodes = currentTarget.data('element6');
          var sspaceTypeCode = currentTarget.data('element7');

          var n = consTitle.length;
          if (n > 160){
              cnpnm = consTitle.slice(0, 160) +' <a id="show_description" href="javascript:void(0);" class="color-orange read_full" >Read More...</a>';
          } else {
               cnpnm = consTitle;
          }

          var pageno = 0;
          var itemPerPage = 20;
          var vconceptCode = cconceptCode;

            $("#concNmm").text(concDes);
            $("#concDes").html(cnpnm);
            $("#show_description").attr("data-element",vconceptCode);
            $("#show_description").attr("data-element1",consTitle);


            $("#conId").val(vconceptCode);
            $("#mastImgg").attr("src",imgnm);
          $('#details-modal').modal('show');
     var similarConceptDtlsPromise = that.getsimilarConcepts(0, vconceptCode, pageno, itemPerPage);

     var singlespaceTypeCode = sspaceTypeCode.split(',');

     for(var i = 0; i < singlespaceTypeCode.length; i++) {
        singlespaceTypeCode[i] = singlespaceTypeCode[i].replace(/^\s*/, "").replace(/\s*$/, "");

     var relatedConceptDtlsPromise = that.getrelateConcepts(0, cconceptCode, singlespaceTypeCode[i], pageno, itemPerPage);
       break;
     }
/*
      $(".modal-backdrop").css('display','none');
*/

     Promise.all([similarConceptDtlsPromise,relatedConceptDtlsPromise]).then(function() {
         console.log("@@@@@@@@@@@@@ In side Concept Promise @@@@@@@@@@@@@@@@@@");
         that.fetchRelatedConceptAndRender();
     });
    },
     fetchRelatedConceptAndRender: function(){
          var that = this;
          var related_concepts = that.related_concepts;
          var similar_concepts = that.similar_concepts;

          $('#similarconceptdtls').html(_.template(similarconceptPageTemplate)({
              "similarConcepts": similar_concepts.toJSON()
          }));

          $('#relatedconceptdtls').html(_.template(relatedconceptPageTemplate)({
              "relatedConcepts": related_concepts.toJSON()
          }));

     }
    });

  return SearchConceptPage;
});
