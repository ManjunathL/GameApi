define([
  'jquery',
  'lodash',
  'backbone',
  'bootstrap',
  'pinterest_grid',
  'owlcarousel',
  'text!templates/dashboard/listing_concept.html',
  'text!templates/dashboard/conceptdetails.html',
  'text!templates/dashboard/similarconcepts.html',
  'text!templates/dashboard/relatedconcepts.html',
  'text!templates/dashboard/view_conceptDesc.html',
  'collections/conceptlists',
  'collections/needconceptlists',
  'models/filter',
  'collections/conceptboards',
  'collections/concepttags',
  'collections/similar_concepts',
  'collections/related_concepts',
  'collections/add_conceptboards',
  'views/dashboard/add_conceptboard',
  'collections/add_conceptToCboards',
  'collections/remove_conceptFromCboards',
  'collections/add_conceptnotes',
  'collections/add_concepttags'
], function($, _, Backbone, Bootstrap, pinterest_grid, owlCarousel, conceptsPageTemplate, conceptdetailsPageTemplate, similarconceptPageTemplate, relatedconceptPageTemplate,view_conceptDesc, ConceptLists, NeedConceptLists, Filter, ConceptBoards, ConceptTags, SimilarConcepts, RelatedConcepts, AddConceptboards, AddConceptboard, AddConceptToCboards, RemoveConceptFromCboards, AddConceptnotes, AddConcepttags){
  var ListingConceptPage = Backbone.View.extend({
    el: '.page',
    conceptlists: null,
    needconceptlists: null,
    filter: null,
    conceptboards: null,
    concepttags: null,
    similar_concepts: null,
    related_concepts: null,
    add_conceptboards: null,
    add_concept2boards: null,
    add_conceptnotes: null,
    add_concepttags:null,
    remove_conceptFromCboards:null,
    initialize: function() {
        this.conceptlists = new ConceptLists();
        this.needconceptlists = new NeedConceptLists();
        this.filter = new Filter();
        this.conceptboards = new ConceptBoards();
        this.concepttags = new ConceptTags();
        this.similar_concepts = new SimilarConcepts();
        this.related_concepts = new RelatedConcepts();
        this.add_conceptboards = new AddConceptboards();
        this.add_conceptToCboards = new AddConceptToCboards();
        this.add_conceptnotes = new AddConceptnotes();
        this.add_concepttags = new AddConcepttags();
        this.remove_conceptFromCboards = new RemoveConceptFromCboards();

        this.filter.on('change', this.render, this);
        this.listenTo(Backbone);
        _.bindAll(this, 'render','fetchConceptListsAndRender');
    },
    render: function () {
        var that = this;
        window.filter = that.filter;

        var conceptboardId = that.model.id;
        var spaceTypeCode = that.model.name;

        this.filter.set({
            'selectedspaceTypeCode':spaceTypeCode
        }, {
            silent: true
        });

        /*if(typeof(spaceTypeCode) != 'undefined'){
          sessionStorage.spaceTypeCode = spaceTypeCode;
        }else{
          sessionStorage.spaceTypeCode = "";
        }*/


        var getConceptsPromise = that.getConcepts(conceptboardId);
        var getNeedConceptsPromise = that.getNeedConcepts(conceptboardId);
        var getConceptTagsPromise = that.getConceptTags(conceptboardId);
        var getConceptBoardsPromise = that.getConceptBoards();

        Promise.all([getConceptsPromise,getNeedConceptsPromise,getConceptTagsPromise,getConceptBoardsPromise]).then(function() {
            console.log("@@@@@@@@@@@@@ In side Promise @@@@@@@@@@@@@@@@@@");
            that.fetchConceptListsAndRender(conceptboardId);
        });
    },
    getConcepts: function(conceptboardId){
        var that = this;
        var pageno = 0;
        var itemPerPage = 20;
        return new Promise(function(resolve, reject) {
             if(typeof(conceptboardId) !== 'undefined') {
               that.conceptlists.getConceptList(conceptboardId, pageno, itemPerPage, {
                  async: true,
                  crossDomain: true,
                  method: "GET",
                  headers:{
                      "authorization": "Bearer "+ sessionStorage.authtoken
                  },
                  success:function(response) {
                      //console.log("Successfully fetch "+ currTab  +" Concepts - ");

                      console.log("I m here   ++++++++++++++ "+conceptboardId);
                      console.log(response);

                      /*if(typeof(conceptboardId) != 'undefined'){
                          sessionStorage.conceptboardId = conceptboardId;
                      }else{
                          sessionStorage.conceptboardId = "";
                      }*/
                        if (!(that.filter.get('selectedconceptboardId'))) {
                          this.filter.set({
                              'selectedconceptboardId':conceptboardId
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
    getNeedConcepts: function(conceptboardId){
        var that = this;
        var pageno = 0;
        var itemPerPage = 20;
        var userId = sessionStorage.userId;

        var formData = {
            "conceptboardId": 1591,
            "spaceTypeCode": "SP-KITCHEN",
            "userId": userId
        };

        return new Promise(function(resolve, reject) {
             if(typeof(conceptboardId) !== 'undefined') {
               that.needconceptlists.fetch({
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

                      console.log("needconceptlist   ++++++++++++++ "+conceptboardId);
                      console.log(response);

                      /*if(typeof(conceptboardId) != 'undefined'){
                          sessionStorage.conceptboardId = conceptboardId;
                      }else{
                          sessionStorage.conceptboardId = "";
                      }*/
                        if (!(that.filter.get('selectedconceptboardId'))) {
                          this.filter.set({
                              'selectedconceptboardId':conceptboardId
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
     getConceptTags: function(conceptboardId){
        var that = this;
        return new Promise(function(resolve, reject) {
            that.concepttags.getConceptTagList(conceptboardId, {
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
       });
    },
    getConceptBoards: function(){
      var that = this;
      var userId = "user1234600";
      var userMindboardId = sessionStorage.defaultMindboardId;
      var pageno = 0;
      var itemPerPage = 20;
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
    fetchConceptListsAndRender: function(conceptboardId){
        var that = this;
        var conceptlists = that.conceptlists;
        var needconceptlists = that.needconceptlists;
        var concepttags = that.concepttags;
        var conceptboards = that.conceptboards;

        var cBoardList = conceptboards.toJSON();
        var conceptBoardName =  that.conceptboards.getConceptBoardName(cBoardList[0].listOfUserConceptBoard,conceptboardId);

        if (typeof(that.filter.get('noFilterApplied')) == 'undefined') {
            that.filter.set({
                'noFilterApplied': '0'
            }, {
                silent: true
            });
        }



        var filterApplied = that.filter.get('noFilterApplied');

        var filterTag = that.filter.get('selectedTag');

        /*if(filterTag){
            console.log(" ================ filterTag =================== ");
            console.log(filterTag);
            that.filter.set({
                'noFilterApplied': '1'
            }, {
                silent: true
            });
        }*/

        if ((typeof(filterTag) !== 'undefined') && (filterTag.length != 0)) {
            conceptlists = conceptlists.toJSON();
            var filteredConcepts = that.conceptlists.filterByTags(conceptlists[0].listOfConceptBoardConcept, filterTag);

            console.log("@@@@@@@@@@ filteredConcepts @@@@@@@@");
            console.log(filteredConcepts);
            console.log("@@@@@@@@@@@@@@@@@@");

            conceptlists[0].listOfConceptBoardConcept = filteredConcepts;

            that.filter.set({
                'noFilterApplied': '1'
            }, {
                silent: true
            });
        } else {
            var conceptlists = conceptlists.toJSON();
            that.filter.set({
                'noFilterApplied': '0'
            }, {
                silent: true
            });
        }


          /*if (filterApplied == '0') {
                var filteredConcepts = conceptlists.toJSON();
            }*/


        console.log("@@@@@@@@@@@@@@@@@@");
        console.log(filterTag);
        console.log(filteredConcepts);
        console.log("@@@@@@@@@@@@@@@@@@");


        $(this.el).html(_.template(conceptsPageTemplate)({
            "conceptdetails": conceptlists,
            "needconceptlisting": needconceptlists.toJSON(),
            "concepttags": concepttags.toJSON(),
            "conceptboardId": conceptboardId,
            'conceptboardsDtls':conceptboards.toJSON(),
            "conceptBoardName":conceptBoardName,
            "filterTag":filterTag
        }));

        $('#concept-dtls').html(_.template(conceptdetailsPageTemplate));

        that.ready(conceptboardId);
    },
    ready: function(conceptboardId){
        $(function() {
           $("#pinBoot"+conceptboardId).pinterest_grid({
                no_columns: 5,
                padding_x: 20,
                padding_y: 20,
                margin_bottom: 50,
                single_column_breakpoint: 700
            });
            $(window).resize();
        });

            $('#tab'+conceptboardId).on('shown.bs.tab', function (e) {
                if (e.isDefaultPrevented()) return;
                e.preventDefault();
                //var $this = $(this);
                //if($this.prop('disabled')) return;
                var target = $(e.target).attr("href") // activated tab
                //alert(target);
                var target = target.replace("#","");
                $("#pinBoot"+target).pinterest_grid({
                    no_columns: 5,
                    padding_x: 20,
                    padding_y: 20,
                    margin_bottom: 50,
                    single_column_breakpoint: 700
                });
            });

            /*$('.conceptTagCarousel').carousel({
                interval: false
            });*/


    },
    events: {
         "click .conceptImg": "getConceptDetails",
         "click .needconceptImg": "getNeedConceptDetails",
         "click .relatedconceptImg": "getRelatedConceptDetails",
         "click .similarconceptImg": "getSimilarConceptDetails",
         "click #addCBoard": "viewAddCboard",
         "click .boardlst": "addConcept2Cboard",
         "click #save_CNote": "submitConceptNote",
         "click #save_CTag": "submitConceptTag",
         "click .remove-pin": "removeConceptFromCboard",
         "click. #show_description":"viewDescription"

    },
    viewDescription:function(evt){
         var currentTarget = $(evt.currentTarget);
          var conceptId = currentTarget.data('element');
          var cnpnm = currentTarget.data('element1');


        $('#show_description-modal').modal('show');

        $('#show_description-dtls').html(_.template(view_conceptDesc)({
            "conceptId": conceptId,
            "conceptDesc": cnpnm

        }));

    },
    removeConceptFromCboard: function (e) {
        if (e.isDefaultPrevented()) return;
        e.preventDefault();

        var currentTarget = $(e.currentTarget);
        var conceptId = currentTarget.data('element');

        console.log("@@@@@@@ conceptId @@@@@@");
        console.log(conceptId);
        console.log("@@@@@@@@@@@@@");

       var that = this;
       if (confirm("Are you sure to delete the concept?")) {

        that.remove_conceptFromCboards.getremoveConceptFromCBoard(conceptId, {
           async: true,
           crossDomain: true,
           method: "POST",
           headers:{
               "authorization": "Bearer "+ sessionStorage.authtoken,
               "Content-Type": "application/json"
           },
           success:function(response) {
               console.log("Successfully deleted Concept From Concept board- ");
               console.log(response);

               $("#snackbar").html("Successfully deleted Concept From Concept board ...");
              var x = document.getElementById("snackbar")
              x.className = "show";
              setTimeout(function(){ x.className = x.className.replace("show", ""); }, 3000);
                that.render();
              //$(e.currentTarget).closest('article').remove();
               return;
           },
           error:function(response) {
               console.log(" +++++++++++++++ save Concept to Concept board- Errrorr ++++++++++++++++++ ");
               console.log(response);
           }
       });
       }
    },
    addConcept2Cboard: function (e) {
        if (e.isDefaultPrevented()) return;
        e.preventDefault();

        var currentTarget = $(e.currentTarget);
        var conceptboardId = currentTarget.data('element');

        //var userId = sessionStorage.userId;
        var userConceptCode = $('#ConcCod').val();

         var formData = {
             "conceptboardId": conceptboardId,
             "userConceptCode": userConceptCode
        };

        var that = this;

        that.add_conceptToCboards.getaddConceptToCBoard(conceptboardId, userConceptCode, {
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
    submitConceptTag: function (e) {
        if (e.isDefaultPrevented()) return;
        e.preventDefault();

        var ctag = $('#tagtxt').val();
        var conceptboardConceptId = $("#conId").val();

        //alert(ctag);

        //var userId = sessionStorage.userId;
        var userId = "user1234600";

        //var conceptboardConceptId = 329;
        //shilpa check what all need to be sent
       var formData = {
            "userTags": ctag
       };
        var that = this;

       that.add_concepttags.getaddConceptTag(userId,conceptboardConceptId, {
           async: true,
           crossDomain: true,
           method: "POST",
           headers:{
               "authorization": "Bearer "+ sessionStorage.authtoken,
               "Content-Type": "application/json"
           },
           data: JSON.stringify(formData),
           success:function(response) {
                 $("#details-modal").modal('hide');
                 $("#snackbar").html("Successfully save Concept Note");
                 var x = document.getElementById("snackbar")
                 x.className = "show";
                 setTimeout(function(){ x.className = x.className.replace("show", ""); }, 3000);
                 that.render();
                 return;
           },
           error:function(response) {
                console.log(" +++++++++++++++Error in saving concept tags ++++++++++++++++++ ");
                console.log(response.responseJSON.errorMessage);
                $("#snackbar").html(response.responseJSON.errorMessage);
                var x = document.getElementById("snackbar")
                x.className = "show";
                setTimeout(function(){ x.className = x.className.replace("show", ""); }, 3000);
                return;
           }
       });
    },
    submitConceptNote: function (e) {
        if (e.isDefaultPrevented()) return;
        e.preventDefault();

        var cnote = $('#conceptNotetxt').val();
        var conceptboardConceptId = $("#conId").val();

        //alert(cnote);

        //var userId = sessionStorage.userId;
        var userId = "user1234600";

        //var conceptboardConceptId = 329;
        //shilpa check what all need to be sent
       var formData = {
            "userNotes": cnote
       };
        var that = this;

       that.add_conceptnotes.getaddConceptNote(userId,conceptboardConceptId, {
           async: true,
           crossDomain: true,
           method: "POST",
           headers:{
               "authorization": "Bearer "+ sessionStorage.authtoken,
               "Content-Type": "application/json"
           },
           data: JSON.stringify(formData),
           success:function(response) {
               console.log("Successfully save Concept Note");
               console.log(response);
               // pin-modal
               $("#details-modal").modal('hide');
               $("#snackbar").html("Successfully save Concept Note");
               var x = document.getElementById("snackbar")
               x.className = "show";
               setTimeout(function(){ x.className = x.className.replace("show", ""); }, 3000);
               that.render();
               return;
           },
           error:function(response) {
                console.log(" +++++++++++++++Error in saving concept note ++++++++++++++++++ ");
                console.log(JSON.stringify(response));
                console.log("%%%%%%%%% response%%%%%%%%%%%%%%%%");
                console.log(response.responseJSON.errorMessage);

                $("#snackbar").html(response.responseJSON.errorMessage);
                var x = document.getElementById("snackbar")
                x.className = "show";
                setTimeout(function(){ x.className = x.className.replace("show", ""); }, 3000);
                return;
           }
       });
    },
    getConceptDetails: function(evt){
     var currentTarget = $(evt.currentTarget);

     var cconceptId = currentTarget.data('element4');

     var that = this;
     var conceptlists = that.conceptlists;
     conceptlists = conceptlists.toJSON();

     console.log("@@@@@@@@@@@@@@@@@ conceptlists @@@@@@@@@@@@@@@@@@@@");
     console.log(conceptlists);


       var conceptdtls = that.conceptlists.getConcept(conceptlists[0].listOfConceptBoardConcept,cconceptId);

    console.log("@@@@@@@@@@@@@@@@@ conceptdtls @@@@@@@@@@@@@@@@@@@@");
    console.log(conceptdtls);
    console.log("@@@@@@@@@@@@@@@@@ conceptCode @@@@@@@@@@@@@@@@@@@@");
    console.log(conceptdtls[0].conceptCode);

    var imgnm = '', concDes = '', cnpnm = '', cconceptCode = '', userNote = '', userTag = '';
    if(typeof(conceptdtls) !== 'undefined'){
        imgnm = conceptdtls[0].imgURL;

//        var consTitle = conceptdtls[0].conceptTitle;
//        var consTitle = "Chilika Lake is a brackish water lagoon, spread over the Puri, Khurda and Ganjam districts of Odisha state on the east coast of India, at the mouth of the Daya River, flowing into the Bay of Bengal, covering an area of over 1,100 km";
        var consTitle = conceptdtls[0].conceptDescription;
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
     $("#show_description").attr("data-element",cconceptId);
     $("#show_description").attr("data-element1",consTitle);


     $("#conId").val(cconceptId);
     $("#mastImgg").attr("src",imgnm);

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
    getNeedConceptDetails: function(evt){
     var currentTarget = $(evt.currentTarget);

     var cconceptId = currentTarget.data('element3');

     var that = this;
     var needconceptlists = that.needconceptlists;
     needconceptlists = needconceptlists.toJSON();

     console.log("@@@@@@@@@@@@@@@@@ needconceptlists @@@@@@@@@@@@@@@@@@@@");
     console.log(needconceptlists);


       var conceptdtls = that.needconceptlists.getConcept(needconceptlists[0].youMayNeedItConceptList,cconceptId);

    console.log("@@@@@@@@@@@@@@@@@ conceptdtls @@@@@@@@@@@@@@@@@@@@");
    console.log(conceptdtls);
    console.log("@@@@@@@@@@@@@@@@@ conceptCode @@@@@@@@@@@@@@@@@@@@");
    console.log(conceptdtls[0].conceptCode);

    var imgnm = '', concDes = '', cnpnm = '', cconceptCode = '', userNote = '', userTag = '';
    if(typeof(conceptdtls) !== 'undefined'){
        imgnm = conceptdtls[0].imgURL;

//        var consTitle = conceptdtls[0].conceptTitle;
//        var consTitle = "Chilika Lake is a brackish water lagoon, spread over the Puri, Khurda and Ganjam districts of Odisha state on the east coast of India, at the mouth of the Daya River, flowing into the Bay of Bengal, covering an area of over 1,100 km";
        var consTitle = conceptdtls[0].conceptTypeDescription;
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
     $("#show_description").attr("data-element",cconceptId);
     $("#show_description").attr("data-element1",consTitle);


     $("#conId").val(cconceptId);
     $("#mastImgg").attr("src",imgnm);

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
        var consTitle = conceptdtls[0].conceptDescription;
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
     $("#show_description").attr("data-element",cconceptId);
     $("#show_description").attr("data-element1",consTitle);


     $("#conId").val(cconceptId);
     $("#mastImgg").attr("src",imgnm);

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
        var consTitle = conceptdtls[0].conceptDescription;
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
     $("#show_description").attr("data-element",cconceptId);
     $("#show_description").attr("data-element1",consTitle);


     $("#conId").val(cconceptId);
     $("#mastImgg").attr("src",imgnm);

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
  return ListingConceptPage;
});
