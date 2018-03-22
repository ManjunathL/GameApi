define([
  'jquery',
  'lodash',
  'backbone',
  'bootstrap',
  'pinterest_grid',
   'text!templates/dashboard/search_concept.html',
   'text!templates/dashboard/searchConceptdetails.html',
   'models/filter',
  'collections/search_concepts',
  'collections/conceptboards',
  'collections/add_conceptToCboards'
], function($, _, Backbone, Bootstrap, pinterest_grid, SearchConceptTemplate,searchConceptdetailsPageTemplate, Filter,SearchConcepts,ConceptBoards,AddConceptToCboards){
  var SearchConceptPage = Backbone.View.extend({
    el: '.page',
    search_concepts:null,
    conceptboards: null,
     filter: null,
    initialize: function() {
        this.search_concepts = new SearchConcepts();
        this.filter = new Filter();
        this.conceptboards = new ConceptBoards();
        this.add_conceptToCboards = new AddConceptToCboards();
        this.listenTo(Backbone);
        this.filter.on('change', this.render, this);
        _.bindAll(this, 'render');
    },
    render: function () {
        var that = this;
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
                    $("#snackbar").html("Successfully searched ...");
                      var x = document.getElementById("snackbar")
                      x.className = "show";
                      setTimeout(function(){ x.className = x.className.replace("show", ""); }, 3000);
                    resolve();
               },
               error:function(response) {
                    console.log(" +++++++++++++++ Search- Errrorr ++++++++++++++++++ ");
                    console.log(JSON.stringify(response));
                    console.log("%%%%%%%%% response%%%%%%%%%%%%%%%%");
                    console.log(response.responseJSON.errorMessage);

                    $("#snackbar").html(response.responseJSON.errorMessage);
                    var x = document.getElementById("snackbar")
                    x.className = "show";
                    setTimeout(function(){ x.className = x.className.replace("show", ""); }, 3000);
                    reject();
               }
            });
        }else{
            reject();
        }
        });
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
        $('#concept-dtls').html(_.template(searchConceptdetailsPageTemplate));
        that.ready();

    },
    ready: function(){
        $(function() {
            //$(".simg").load(function() {
            //alert("++++++++++++ I m here +++++++++++++++++++");
               $("#searchpinBoot").pinterest_grid({
                    no_columns: 5,
                    padding_x: 20,
                    padding_y: 20,
                    margin_bottom: 50,
                    single_column_breakpoint: 700
                });
            //});
        });
     },
     events: {
          "click .boardlst": "addConcept2Cboard",
          "click .conceptImg": "getConceptDetails"

     },
     getConceptDetails: function(evt){
          var currentTarget = $(evt.currentTarget);
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
  }
    });

  return SearchConceptPage;
});
