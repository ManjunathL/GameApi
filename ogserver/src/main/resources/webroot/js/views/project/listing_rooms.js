define([
  'jquery',
  'lodash',
  'backbone',
  'bootstrap',
  'pinterest_grid',
  'owlcarousel',
  'text!templates/project/listing_rooms.html',
  'views/dashboard/add_conceptboard'
], function($, _, Backbone, Bootstrap, pinterest_grid, owlCarousel, listingRoomPageTemplate,AddConceptboard){
  var ListingRoomPage = Backbone.View.extend({
    el: '.page',
    add_concept2boards: null,
    initialize: function() {

        this.listenTo(Backbone);
        _.bindAll(this, 'render');
    },
    render: function () {
        $(this.el).html(_.template(listingRoomPageTemplate));
    },
    events: {
            "click .addCBoard1": "viewAddCboard"
        },
        viewAddCboard: function(){
                $('#addcboard-modal').modal('show');
                AddConceptboard.apply();
            },
    getConceptBoards: function(){
        var that = this;
        var userId = sessionStorage.userId;
        var userProjectId = 170;
        var pageno = 0;
        var itemPerPage = 20;
        var filter = 1;
        that.conceptboards.getConceptBoardList(userId, userProjectId, pageno, itemPerPage,filter, {
           async: true,
           crossDomain: true,
           method: "GET",
           headers:{
               "authorization": "Bearer "+ sessionStorage.authtoken
           },
           success:function(data) {
               console.log(" +++++++++++++++ Concept Boards ++++++++++++++++++ ");
               console.log(data);
               return false;
           },
           error:function(response) {
               console.log(" +++++++++++++++ Errrorr ++++++++++++++++++ ");
               console.log(response);
               return false;
           }
        });
    }
  });
  return ListingRoomPage;
});
