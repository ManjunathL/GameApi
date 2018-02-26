<script type="text/javascript">
//Fetching space values 
    $(function(){

    //Backbone Model
    var Cat = Backbone.Model.extend({});

    // create a collection
    var CatCollection = Backbone.Collection.extend({
        model: Cat,
        url: 'http://gameuat.mygubbi.com:1447/gapi/workbench/codelookup?lookupType=space'

      
    });
    var catCollection = new CatCollection();
    catCollection.fetch(
    { 
    success: function(collection, response, option) {
            var spacetmp = _.template($("#spacenm").html());
            $("#spacesel").html(spacetmp({
            'subcat':response
            }));
         },
        error: function(model, response, options) {
            console.log("error from products fetch - " + response);
    
        }
    });
        
   });
//Fetcting size values
        $(function(){

    //Backbone Model
    var Size = Backbone.Model.extend({});

    // create a collection
    var SizeCollection = Backbone.Collection.extend({
        model: Size,
        url: 'http://gameuat.mygubbi.com:1447/gapi/workbench/codelookup?lookupType=size'

      
    });
    var sizeCollection = new SizeCollection();
    sizeCollection.fetch(
    { 
    success: function(collection, response, option) {
            var sizetmp = _.template($("#sizenm").html());
            $("#sizesel").html(sizetmp({
            'size':response
            }));
         },
        error: function(model, response, options) {
            console.log("error from products fetch - " + response);
    
        }
    });
        
   });
        
//Fetcting designstyle values
        $(function(){

    //Backbone Model
    var Designstyle = Backbone.Model.extend({});

    // create a collection
    var DesignstyleCollection = Backbone.Collection.extend({
        model: Designstyle,
        url: 'http://gameuat.mygubbi.com:1447/gapi/workbench/codelookup?lookupType=designstyle'

      
    });
    var designstyleCollection = new DesignstyleCollection();
    designstyleCollection.fetch(
    { 
    success: function(collection, response, option) {
            var designtmp = _.template($("#designnm").html());
            $("#designsel").html(designtmp({
            'design':response
            }));
         },
        error: function(model, response, options) {
            console.log("error from products fetch - " + response);
    
        }
    });
        
   });        
        
 </script>
    <--- template of space -->
    <script id="spacenm" type="text/template">
    <%= console.log(subcat) %>
         <% _.each(subcat, function(subc, i){ %>
           <option value=""><%= subcat[i].title %></option>
         <% }); %>
    </script>
        
        <--- template of size -->
<script id="sizenm" type="text/template">
    <%= console.log(size) %>
         <% _.each(size, function(sizec, i){ %>
           <option value=""><%= size[i].title %></option>
         <% }); %>
    </script>
            
<--- template of design style -->
<script id="designnm" type="text/template">
    <%= console.log(design) %>
         <% _.each(design, function(designc, i){ %>
           <option value=""><%= design[i].title %></option>
         <% }); %>
    </script>           