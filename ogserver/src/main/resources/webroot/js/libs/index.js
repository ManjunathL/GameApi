"use strict";

$(function(){
console.log("inside function");
    //Backbone Model
    var Designdetails = Backbone.Model.extend({});

    // create a collection
    var DesigndetailsCollection = Backbone.Collection.extend({
        model: Designdetails,
        url: 'http://localhost:1445/gapi/workbench/designmaster/select'
              
        

      
    });
    var designdetailsCollection = new DesigndetailsCollection();
    designdetailsCollection.fetch(
    { 
    success: function(collection, response, option) {
        console.log(response);
            var designdetailstmp = _.template($("#designdetailsnm").html());
            $("#designdetails").html(designdetailstmp({
            'designdetails':response
            }));
        
         },
        error: function(model, response, options) {
            console.log("error from products fetch - " + response);
    
        }
    });
        
   });


<span id="designdetailsnm" type="text/template">
   
         <% _.each(designdetails, function(designdetailsc, i){ %>
         
         <% var spnm=  designdetails[i].spaceType %>
         <% spnm = spnm.replace(/\s+/g, '-') %>
         
         <% var stnm=  designdetails[i].spaceDesignStyle %>
         <% stnm = stnm.replace(/\s+/g, '-') %>
         
         <% var sznm=  designdetails[i].spaceSize %>
         <% sznm = sznm.replace(/\s+/g, '-') %>
         
         <% var clnm=  designdetails[i].spaceColorStyle %>
         <% clnm = clnm.replace(/\s+/g, '-') %>
         
         <div class="design col-sm-6 col-md-4" data-category="<%= spnm %> <%= stnm %> <%= sznm %> <%= clnm %>">
          <div class="design listings-grid__item" data-category="<%= spnm %> <%= stnm %> <%= sznm %> <%= clnm %>">
       
									<a href="listing-detail.html?id=<%= designdetails[i].id %>">
										<div class="listings-grid__main">
											<img src="img/1.jpg" alt="">
										</div>

										<div class="listings-grid__body">
											<small><%= designdetails[i].spaceName %></small>
											<h5><%= designdetails[i].webDisplayName %></h5>
										</div>
                                        

										<ul class="listings-grid__attrs">
											<li><i class="zmdi zmdi-group"></i> 05 </li>
											<li><i class="zmdi zmdi-shape"></i> 20 </li>
										</ul>
									</a>

									<div class="actions listings-grid__favorite">
										<div class="actions__toggle">
											<input type="checkbox">
											<i class="zmdi zmdi-favorite-outline"></i>
											<i class="zmdi zmdi-favorite"></i>
										</div>
									</div>
								</div>
                                
                                </div>
           
           
         <% }); %>
         
    </span>