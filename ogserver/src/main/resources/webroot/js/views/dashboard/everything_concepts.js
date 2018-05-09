define([
  'jquery',
  'lodash',
  'backbone',
  'bootstrap',
  'pinterest_grid',
  'owlcarousel',
  'text!templates/dashboard/everything.html',
  'text!templates/dashboard/conceptdetails.html',
  'text!templates/dashboard/similarconcepts.html',
  'text!templates/dashboard/relatedconcepts.html',
  'text!templates/dashboard/spaceelements.html',
   'models/filter',
  'collections/everythings',
  'collections/conceptalltags',
  'collections/conceptlists',
  'collections/similar_concepts',
  'collections/related_concepts',
  'collections/conceptboards',
  'collections/spaceelements',
  'collections/add_conceptToCboards',
], function($, _, Backbone, Bootstrap, pinterest_grid, owlCarousel, everythingPageTemplate, conceptdetailsPageTemplate, similarconceptPageTemplate, relatedconceptPageTemplate, spaceTempPageTemplate, Filter, Everything, ConceptAllTags, ConceptLists, SimilarConcepts, RelatedConcepts, ConceptBoards, SpaceElements, AddConceptToCboards){
  var EverythingConceptPage = Backbone.View.extend({
    el: '.page',
    filter: null,
    everythings: null,
    conceptalltags: null,
    conceptlists: null,
    similar_concepts: null,
    related_concepts: null,
    conceptboards: null,
    spaceelements:null,
    add_concept2boards: null,
    initialize: function() {
        this.filter = new Filter();
        this.everythings = new Everything();
        this.conceptalltags = new ConceptAllTags();
        this.conceptlists = new ConceptLists();
        this.similar_concepts = new SimilarConcepts();
        this.related_concepts = new RelatedConcepts();
        this.conceptboards = new ConceptBoards();
        this.spaceelements = new SpaceElements();
        this.add_conceptToCboards = new AddConceptToCboards();
        this.filter.on('change', this.render, this);
        this.listenTo(Backbone);
        _.bindAll(this, 'render');
    },
    render: function () {
        var that = this;
        window.filter = that.filter;
        console.log("****IN RENDER ***********")
        var conceptboardId = that.model.id;
        var spaceTypeCode = that.model.spaceTypeCode;
        var getEverythingConceptBoardPromise = that.getEverythingConceptBoard();
        var getConceptAllTagsPromise = that.getConceptAllTags();
        var getConceptBoardsPromise = that.getConceptBoards();
        var getSpaceElementPromise = that.getSpaceElement(spaceTypeCode);

        Promise.all([getEverythingConceptBoardPromise, getConceptAllTagsPromise, getConceptBoardsPromise, getSpaceElementPromise]).then(function() {
            console.log("@@@@@@@@@@@@@ In side Promise @@@@@@@@@@@@@@@@@@");
            that.rendersub();
        });


    },
    viewSpaceTemplates: function(e){
            if (e.isDefaultPrevented()) return;
            e.preventDefault();

            var that = this;



            var currentTarget = $(e.currentTarget);
            var conceptboardId = currentTarget.data('element');
            var spaceTypeCode = currentTarget.data('element1');

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
    getSpaceElement: function(spaceTypeCode){
                var that = this;
                return new Promise(function(resolve, reject) {
                     if(typeof(spaceTypeCode) !== 'undefined') {
                       that.spaceelements.getSpaceElement(spaceTypeCode, {
                          async: true,
                          crossDomain: true,
                          method: "GET",
                          headers:{
                              "authorization": "Bearer "+ sessionStorage.authtoken
                          },
                          success:function(response) {

                              console.log("I m here   ++++++++++++++ "+spaceTypeCode);
                              console.log(response);
                                if (!(that.filter.get('selectedspaceelements'))) {
                                  this.filter.set({
                                      'selectedspaceelements':spaceTypeCode
                                  }, {
                                      silent: true
                                  });
                              }
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
    },
    getEverythingConceptBoard: function(){
        var that = this;

        var userId = sessionStorage.userId;
        var userMindboardId = sessionStorage.defaultMindboardId;
        var pageno = 0;
        var itemPerPage = 50;

        var formData = {
          "tag": "",
          "type": 0
        };
        console.log("formData");
        console.log(formData);
        return new Promise(function(resolve, reject) {
         if(typeof(userId) !== 'undefined') {
           that.everythings.getEverythingList(userId, userMindboardId, pageno, itemPerPage,{
                async: true,
                crossDomain: true,
                method: "POST",
                headers:{
                   "authorization": "Bearer "+ sessionStorage.authtoken,
                   "Content-Type": "application/json"
                },
                data: JSON.stringify(formData),
              success:function(response) {
                  //console.log("Successfully fetch "+ currTab  +" Concepts - ");

                  console.log("everythings   ++++++++++++++ ");
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
    getConceptAllTags: function(){
            var that = this;
            var userMindboardId = sessionStorage.defaultMindboardId;

            return new Promise(function(resolve, reject) {
             if(typeof(userMindboardId) !== 'undefined') {
               that.conceptalltags.getConceptAllTagList(userMindboardId, {
                  async: true,
                  crossDomain: true,
                  method: "GET",
                  headers:{
                      "authorization": "Bearer "+ sessionStorage.authtoken
                  },
                  success:function(response) {
                      console.log("Successfully fetch Concept Tags - ");
                      console.log(response);
                      resolve();
                  },
                  error:function(response) {
                      console.log(" +++++++++++++++ Errrorr Tags ++++++++++++++++++ ");
                      console.log(response);
                      reject();
                  }
              });
             }else{
                resolve();
             }
             });

        },

    events: {
         "click .conceptImg1": "getConceptDetails",
         "click .relatedconceptImg": "getRelatedConceptDetails",
         "click .similarconceptImg": "getSimilarConceptDetails",
         "click .boardlst": "viewSpaceTemplates",
         "click #saveSpaveElement":"addConcept2Cboard"
         },

     addConcept2Cboard: function (e) {
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
                   $("#pin-modal").modal('hide');
                   $("#pin-modal-conceptdtls").modal('hide');

                   return;
               },
               error:function(response) {
                   console.log(" +++++++++++++++ save Concept to Concept board- Errrorr ++++++++++++++++++ ");
                   console.log(response);
               }
           });
        },

     getConceptDetails: function(evt){
          var currentTarget = $(evt.currentTarget);

          var cconceptId = currentTarget.data('element4');
          var userConceptBoardId = currentTarget.data('element5');
          var spaceTypeCode = currentTarget.data('element6');


          var that = this;
          var everythings = that.everythings;
          everythings = everythings.toJSON();

          console.log("@@@@@@@@@@@@@@@@@ everythings @@@@@@@@@@@@@@@@@@@@");
          console.log(everythings);


            var conceptdtls = that.conceptlists.getConcept(everythings[0].listOfConcepts,cconceptId);

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
             concDes = consTitle.slice(0, 60) +' <a  href="javascript:void(0);" class="color-orange read_full sidebar-box black" ><a href="javascript:void(0);" onclick="showDesc()" class="button readMore" style="">Read More...</a></p></a>';

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
          $("#show_description").attr("data-element",cconceptId);
          $("#show_description").attr("data-element1",consTitle);


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

          $('#details-modal1').modal('show');


          var similarConceptDtlsPromise = that.getsimilarConcepts(userConceptBoardId, vconceptCode, pageno, itemPerPage);
          var relatedConceptDtlsPromise = that.getrelateConcepts(userConceptBoardId, relconceptCode, spaceTypeCode, pageno, itemPerPage);

          Promise.all([similarConceptDtlsPromise,relatedConceptDtlsPromise]).then(function() {
              console.log("@@@@@@@@@@@@@ In side Concept Promise @@@@@@@@@@@@@@@@@@");
              that.fetchRelatedConceptAndRender();
          });
         },
     getConceptBoards: function(){
               var that = this;
               var userId = sessionStorage.userId;
               //var userId = "user1234600";
               var userMindboardId = sessionStorage.defaultMindboardId;
               var pageno = 0;
               var itemPerPage = 50;
               var filterId = 1;
               return new Promise(function(resolve, reject) {
                    that.conceptboards.getConceptBoardList(userId, userMindboardId, pageno, itemPerPage,filterId, {
                        async: true,
                        crossDomain: true,
                        method: "GET",
                        headers:{
                            "authorization": "Bearer "+ sessionStorage.authtoken
                        },
                        success:function(data) {
                            //console.log(" +++++++++++++++ Concept Boards ++++++++++++++++++ ");
                            //console.log(data);
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



         /*if(typeof(cconceptCode) !== 'undefined' && relatedConceptsListObj != ""){
             var conceptdtls = that.related_concepts.getRelatedConcept(relatedConceptsListObj[0].relatedConceptsList,cconceptCode);
             console.log("@@@@@@@@@@@@@@@@@ Inside Related Concept @@@@@@@@@@@@@@@@@@@@");
         }else{
            var conceptdtls = that.conceptlists.getConcept(conceptlists[0].listOfConceptBoardConcept,cconceptId);
         }*/

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
             if (n > 160){
             concDes = consTitle.slice(0, 160) +' <a id="show_description" href="javascript:void(0);" class="color-orange read_full" >Read More...</a>';

             } else {
             concDes = consTitle;
             }


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
          $("#ConcCod").val(cconceptCode);
          $("#show_description").attr("data-element",cconceptId);
          $("#show_description").attr("data-element1",consTitle);


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

          $('#details-modal1').modal('show');


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
         if (n > 160){
         concDes = consTitle.slice(0, 160) +' <a id="show_description" href="javascript:void(0);" class="color-orange read_full" >Read More...</a>';

         } else {
         concDes = consTitle;
         }


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
      $("#concDes").html(concDes);
      $("#ConcCod").val(cconceptCode);
      $("#show_description").attr("data-element",cconceptId);
      $("#show_description").attr("data-element1",consTitle);


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

      $('#details-modal1').modal('show');


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

     },
     rendersub: function(){
        var that = this;
        var everythings = that.everythings;
        everythings = everythings.toJSON();
        var conceptboards = that.conceptboards;
        var conceptalltags = that.conceptalltags;
         var filterTag = that.filter.get('selectedTag');
        if ((typeof(filterTag) !== 'undefined') && (filterTag.length != 0)) {
         var filteredConcepts = that.everythings.filterByTags(everythings[0].listOfConcepts, filterTag);

            console.log("@@@@@@@@@@ filteredConcepts @@@@@@@@");
            console.log(filteredConcepts);
            console.log("@@@@@@@@@@@@@@@@@@");

                    everythings[0].listOfConcepts = filteredConcepts;
                    }
            console.log("@@@@@@@@@@ everythings[0].listOfConcepts  @@@@@@@@");
            console.log(everythings[0].listOfConcepts );
            console.log("@@@@@@@@@@@@@@@@@@");

        $(this.el).html(_.template(everythingPageTemplate)({
         "listOfConcepts": everythings[0].listOfConcepts,
         "conceptalltags": conceptalltags.toJSON(),
         'conceptboardsDtls':conceptboards.toJSON(),
          "filterTag":filterTag
        }));
        $('#concept-dtls').html(_.template(conceptdetailsPageTemplate)({
                    'conceptboardsDtls':conceptboards.toJSON()

                }));
        that.ready();
    },
     ready: function(){
        $(function() {
           $("#listOfConceptspinBoot").pinterest_grid({
                no_columns: 5,
                padding_x: 20,
                padding_y: 20,
                margin_bottom: 50,
                single_column_breakpoint: 700
            });
            //$(window).resize();
        });
     }
  });
  return EverythingConceptPage;
});
