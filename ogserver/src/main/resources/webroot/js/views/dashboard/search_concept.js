define([
  'jquery',
  'lodash',
  'backbone',
  'bootstrap',
  'pinterest_grid',
   'text!templates/dashboard/search_concept.html',
   'text!templates/dashboard/conceptdetails.html',
   'models/filter',
  'collections/search_concepts',
  'collections/conceptboards',
  'collections/add_conceptToCboards'
], function($, _, Backbone, Bootstrap, pinterest_grid, SearchConceptTemplate,conceptdetailsPageTemplate, Filter,SearchConcepts,ConceptBoards,AddConceptToCboards){
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
                    //console.log("Successfully Searched... ");
                    //console.log(response);
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
             'conceptboardsDtls':conceptboards.toJSON()
         }));
        var conceptboardId = "1111";

        $('#concept-dtls').html(_.template(conceptdetailsPageTemplate));
        that.ready(conceptboardId);

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


        },
             events: {
                  "click .boardlst": "addConcept2Cboard"
             },
  });
  return SearchConceptPage;
});
