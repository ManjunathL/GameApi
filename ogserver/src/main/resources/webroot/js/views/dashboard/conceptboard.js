define([
  'jquery',
  'lodash',
  'backbone',
  'bootstrap',
  'pinterest_grid',
  'text!templates/dashboard/conceptboard.html',
  'text!templates/dashboard/kitchen.html',
  'text!templates/dashboard/concepts.html',
  'text!templates/dashboard/conceptdetails.html',
  'text!templates/dashboard/similarconcepts.html',
  'text!templates/dashboard/relatedconcepts.html',
  'text!templates/dashboard/living.html',
  //'collections/designs',
  'collections/spacetypes',
  //'collections/concepts',
  'collections/createconceptboards',
  'collections/conceptboards',
  'collections/conceptlists',
  'collections/concepttags',
  'collections/similar_concepts',
  'collections/related_concepts',
  'collections/add_conceptboards',
  'views/dashboard/add_conceptboard',
  'collections/add_conceptToCboards',
  'collections/remove_conceptBoards'
], function($, _, Backbone, Bootstrap, pinterest_grid, dashboardPageTemplate, kitchenPageTemplate, conceptsPageTemplate, conceptdetailsPageTemplate, similarconceptPageTemplate, relatedconceptPageTemplate, livingPageTemplate, SpaceTypes, CreateConceptBoards, ConceptBoards, ConceptLists, ConceptTags, SimilarConcepts, RelatedConcepts, AddConceptboards, AddConceptboard, AddConceptToCboards, RemoveConceptBoards){
  var ConceptboardPage = Backbone.View.extend({
    el: '.page',
    //concepts: null,
    //designs: null,
    spacetypes: null,
    createconceptboards: null,
    conceptboards: null,
    conceptlists: null,
    concepttags: null,
    similar_concepts: null,
    related_concepts: null,
    add_conceptboards: null,
    add_concept2boards: null,
    remove_conceptBoards:null,
    initialize: function() {
        //this.concepts = new Concepts();
        //this.designs = new Designs();
        this.spacetypes = new SpaceTypes();
        this.createconceptboards = new CreateConceptBoards();
        this.conceptboards = new ConceptBoards();
        this.conceptlists = new ConceptLists();
        this.concepttags = new ConceptTags();
        this.similar_concepts = new SimilarConcepts();
        this.related_concepts = new RelatedConcepts();
        this.add_conceptboards = new AddConceptboards();
        this.add_conceptToCboards = new AddConceptToCboards();
        this.remove_conceptBoards = new RemoveConceptBoards();
        this.listenTo(Backbone);
        _.bindAll(this, 'render','fetchConceptsAndRender');
    },
    render: function () {
        var that = this;

        var createConceptBoardPromise = that.createConceptBoard();
        var getConceptBoardsPromise = that.getConceptBoards();
        //var getConceptsPromise = that.getConcepts();
        //var getDesignsPromise = that.getDesigns();

        Promise.all([createConceptBoardPromise,getConceptBoardsPromise]).then(function() {
            console.log("@@@@@@@@@@@@@ In side Promise @@@@@@@@@@@@@@@@@@");
            that.fetchConceptsAndRender();
        });
    },
    createConceptBoard: function(){
        var that = this;
        //var userId = sessionStorage.userId;
        var userId = "user1234600";
        return new Promise(function(resolve, reject) {
            that.createconceptboards.getUserIdAuth(userId, {
                async: true,
                crossDomain: true,
                method: "GET",
                headers:{
                    "authorization": "Bearer "+ sessionStorage.authtoken
                },
                success:function(data) {
                    console.log(" +++++++++++++++ I m here ++++++++++++++++++ ");

                    var resDefConceptboards = data.toJSON();

                     console.log(resDefConceptboards);

                    console.log(resDefConceptboards[0].defaultMindboardId);

                    if(typeof(resDefConceptboards[0].defaultMindboardId) != 'undefined'){
                        sessionStorage.defaultMindboardId = resDefConceptboards[0].defaultMindboardId;
                    }else{
                        sessionStorage.defaultMindboardId = "";
                    }
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
    getConceptBoards: function(){
      var that = this;
      var userId = "user1234600";
      var userMindboardId = sessionStorage.defaultMindboardId;
      var pageno = 0;
      var itemPerPage = 20;
      return new Promise(function(resolve, reject) {
           that.conceptboards.getConceptBoardList(userId, userMindboardId, pageno, itemPerPage, {
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
    getConcepts: function(){
        var that = this;
        return new Promise(function(resolve, reject) {
             if (!that.concepts.get('id')) {
                that.concepts.fetch({
                  success: function(response) {
                    resolve();
                  },
                  error: function(model, response, options) {
                      reject();
                  }
                });
             }else{
                resolve();
             }
        });
    },
    getDesigns: function(){
        var that = this;
        return new Promise(function(resolve, reject) {
             if (!that.designs.get('id')) {
                that.designs.fetch({
                  success: function(response) {
                    resolve();
                  },
                  error: function(model, response, options) {
                      reject();
                  }
                });
            } else{
                resolve();
            }
        });
    },
    fetchConceptsAndRender: function() {
        var that = this;
       // var designs = that.designs;
       // var concepts = that.concepts;
        var conceptboards = that.conceptboards;


        $(this.el).html(_.template(dashboardPageTemplate)({
            //'designdetail':designs.toJSON(),
            //'conceptdetails':concepts.toJSON(),
            'conceptboardsDtls':conceptboards.toJSON()
        }));
    },
    events: {
       //"click .selTabs": "getSelectedConcepts",
       // "click .conceptImg": "getConceptDetails",
        "click #addCBoard": "viewAddCboard",
        "click .spacetypecls": "getSelectedTemplatess",
        "click #save_Cboard": "submitBoard",
        "click .remove-cboard":"removeConceptBoard"
       // "click .boardlst": "addConcept2Cboard"
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
    removeConceptBoard: function (e){
        if (e.isDefaultPrevented()) return;
            e.preventDefault();

        var userId = "user1234600";
        var userMindboardId = sessionStorage.defaultMindboardId;

        var currentTarget = $(e.currentTarget);
        var conceptBoardId = currentTarget.data('element');

        var formData = {
                    "conceptboardId": conceptBoardId,
                    "userId": userId
               };
        var that = this;

        that.remove_conceptBoards.getremoveConceptBoard(conceptBoardId, {
           async: true,
           crossDomain: true,
           method: "POST",
           headers:{
               "authorization": "Bearer "+ sessionStorage.authtoken,
               "Content-Type": "application/json"
           },
           data: JSON.stringify(formData),
           success:function(response) {
              console.log("Successfully removed Concept board ... ");
              console.log(response);
              $("#snackbar").html("Successfully save Concept board through template selection... ");
              var x = document.getElementById("snackbar")
              x.className = "show";
              setTimeout(function(){ x.className = x.className.replace("show", ""); }, 3000);
              return;

          },
          error:function(model, response, options) {
          alert("Shilpa respone :: ");
              console.log(" +++++++++++++++ save Concept to Concept board- Errrorr ++++++++++++++++++ ");
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
    submitBoard: function (e) {
        if (e.isDefaultPrevented()) return;
        e.preventDefault();

        //var userId = sessionStorage.userId;
        var userId = "user1234600";
        var userMindboardId = sessionStorage.defaultMindboardId;

        //var formData = new FormData();
       /*var formData = JSON.stringify({
         "userId": "user1234600",
         "name": "test concept board through template1234",
         "description": "ddddddd",
         "templateCode": "TC1001",
         "userMindboardId": "62"
       });*/
       var formData = {
            "id": 0,
            "userId": userId,
            "name": $('#cboardnameTxt').val(),
            "spaceTypeCode":$('#spaceTypeCodeTxt').val(),
            "description": $('#cboarddescTxt').val(),
            "templateCode": $('#templateCodeTxt').val(),
            "userMindboardId": userMindboardId,
            "imgUrl":$('#cboardImgUrl').val(),
            "noOfConceptsAdded": 1,
            "userNote": $('#cboarddescTxt').val()
       };
        var that = this;
        console.log("++++++++++++++++++++++++++ formData ++++++++++++++++++++++++++++++");
        console.log(formData);

        that.add_conceptboards.fetch({
           async: true,
           crossDomain: true,
           method: "POST",
           headers:{
               "authorization": "Bearer "+ sessionStorage.authtoken,
               "Content-Type": "application/json"
           },
           data: JSON.stringify(formData),
           success:function(response) {
              console.log("Successfully save Concept board through template selection... ");
              console.log(response);
              $("#addcboard-modal").modal('hide');

                $("#snackbar").html("Successfully save Concept board through template selection... ");
                var x = document.getElementById("snackbar")
                x.className = "show";
                setTimeout(function(){ x.className = x.className.replace("show", ""); }, 3000);
                return;

          },
          error:function(model, response, options) {
          alert("Shilpa respone :: ");
              console.log(" +++++++++++++++ save Concept to Concept board- Errrorr ++++++++++++++++++ ");
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
    getSelectedTemplatess: function(evt){
        evt.preventDefault();
        var currentTarget = $(evt.currentTarget);
        var spaceTypeCode = currentTarget.data('element');
        currentTarget.css({'border':'1px solid red'});
        //var spaceTypeCode = 'STBEDROOM';
        console.log(" +++++++++++++++ SpacType Templates++++++++++++++++++ ");
        console.log(spaceTypeCode);

        AddConceptboard.viewSpaceTemplates(spaceTypeCode);
        return;
    },
    getSelectedConcepts: function(evt){
        var currentTarget = $(evt.currentTarget);
        var currTab = currentTarget.data('element');
        var conceptboardId = currentTarget.data('element1');
        var spaceTypeCode = currentTarget.data('element2');
        var pageno = 0;
        var itemPerPage = 20;

        console.log(" Current Tab");
        console.log(currTab);
        console.log(conceptboardId);

        var that = this;
        that.conceptlists.getConceptList(conceptboardId, pageno, itemPerPage, {
           async: true,
           crossDomain: true,
           method: "GET",
           headers:{
               "authorization": "Bearer "+ sessionStorage.authtoken
           },
           success:function(response) {
               //console.log("Successfully fetch "+ currTab  +" Concepts - ");
               //console.log(response);
               console.log("I m here   ++++++++++++++ "+conceptboardId);

               if(typeof(spaceTypeCode) != 'undefined'){
                   sessionStorage.spaceTypeCode = spaceTypeCode;
               }else{
                   sessionStorage.spaceTypeCode = "";
               }

               if(typeof(conceptboardId) != 'undefined'){
                   sessionStorage.conceptboardId = conceptboardId;
               }else{
                   sessionStorage.conceptboardId = "";
               }

               that.getConceptTags(conceptboardId);
           },
           error:function(response) {
               console.log(" +++++++++++++++ Errrorr ++++++++++++++++++ ");
               console.log(response);
           }
       });
    },
    getConceptDetails: function(evt){
        var currentTarget = $(evt.currentTarget);
        var imgnm = currentTarget.data('element');
        var concDes = currentTarget.data('element1');
        var cnpnm = currentTarget.data('element2');
        var cconceptCode = currentTarget.data('element3');
        var cconceptId = currentTarget.data('element4');
        var pageno = 0;
        var itemPerPage = 20;

        var conceptboardId = sessionStorage.conceptboardId;
        var vconceptCode = cconceptCode;
        var relconceptCode = cconceptCode;

        var sspaceTypeCode = sessionStorage.spaceTypeCode ? sessionStorage.spaceTypeCode : '';

        $("#concNmm").text(concDes);
        $("#concDes").text(cnpnm);
        $("#conId").text(cconceptId);
        $("#mastImgg").attr("src",imgnm);
        $('#details-modal').modal('show');

        var that = this;

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
    getConceptTags: function(conceptboardId){
        var that = this;
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
               that.fetchConceptListsAndRender(conceptboardId);
           },
           error:function(response) {
               console.log(" +++++++++++++++ Errrorr Tags ++++++++++++++++++ ");
               console.log(response);
           }
       });
    },
    fetchConceptListsAndRender: function(conceptboardId){
        var that = this;
        var conceptlists = that.conceptlists;
        var concepttags = that.concepttags;

        $(".main-conceptboard-sec").html(_.template(conceptsPageTemplate)({
            "conceptdetails": conceptlists.toJSON(),
            "concepttags": concepttags.toJSON(),
            "conceptboardId": conceptboardId
        }));

         $('#concept-dtls').html(_.template(conceptdetailsPageTemplate));

         that.ready(conceptboardId);
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
    viewAddCboard: function(){
        $('#addcboard-modal').modal('show');
        AddConceptboard.apply();
    },
    ready: function(conceptboardId){
           $("#pinBoot"+conceptboardId).pinterest_grid({
                no_columns: 5,
                padding_x: 20,
                padding_y: 20,
                margin_bottom: 50,
                single_column_breakpoint: 700
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
    }
  });
  return ConceptboardPage;
});
