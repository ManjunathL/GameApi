define([
    'jquery',
    'underscore',
    'backbone',
    'bootstrap',
    'bootstrapvalidator',
    'collections/autosuggest_products',
    'text!templates/header/suggest_results.html'
], function($, _, Backbone, Bootstrap, BootstrapValidator, AutoSuggestProducts, SuggestResultsPage) {
    return {
        getSuggestions: function(term) {
            return new Promise(function(resolve, reject) {
                var autoSuggestProducts = new AutoSuggestProducts();
                var that = this;

                var userId = sessionStorage.userId;
                var formData = {
                         "searchText": term,
                         "spaceTypeCode": ""
                };
                autoSuggestProducts.fetch({
                async: true,
                crossDomain: true,
                method: "POST",
                headers:{
                   "authorization": "Bearer "+ sessionStorage.authtoken,
                   "Content-Type": "application/json"
                },
                data: JSON.stringify(formData),
                success:function(response) {
                    console.log("Successfully Searched... ");
                    console.log(response);

//                    $("#snackbar").html("Successfully fetched in Concept board... ");
//                    var x = document.getElementById("snackbar")
//                    x.className = "show";
//                    setTimeout(function(){ x.className = x.className.replace("show", ""); }, 3000);
//                    return;
                    resolve(autoSuggestProducts);

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
                    return;

                }
                });
            });
        },
        debounce: function(fn, delay) {
            var timer = null;
            return function() {
                var context = this,
                    args = arguments;
                clearTimeout(timer);
                timer = setTimeout(function() {
                    fn.apply(context, args);
                }, delay);
            };
        },
        ready: function(parent) {

            _.bindAll(this, 'getSuggestions', 'debounce');

            var events = {
                "click .sb-search-txt": this.gotoSearchedProduct
            };

            parent.delegateEvents(events);

            //this.ref.onAuth(this.onFAuth);
            var that = this;
            $(function() {


//                $("#searchForm1").submit(function(e) {
//                    e.preventDefault();
//                    var term = $('#searchInput1').val();
//                    if (!term) return;
//
//                    window.App.router.navigate("/concept_search-" + term, {
//                        trigger: true
//                    });
//
//                });
                $('.search').click(function() {
                    $('.sb-search_suggest').slideDown();
                    $('#searchInput1').focus();
                });

                $('.search_close').click(function() {
                    $('.sb-search_suggest').slideUp();
                });

                $('#searchInput1').keyup(function() {

                    var char = $(this).val().length;
                    if (char >= 3) {
                        $('.search_suggest').slideDown();
                    } else {
                        $('.search_suggest').slideUp();
                    }
                });

                $('.search_overlay').click(function() {
                    $('.search_input').focus();
                    $('.search_suggest').slideUp();
                });

                $('#searchInput1').keyup(that.debounce(function(e) {

                    if (e.keyCode == 13) return;

                    var term = $(this).val();
                    if (term.length >= 3) {
                        that.getSuggestions(term).then(function(autoSuggestProducts) {
                            $('.sb-search_suggest').html(_.template(SuggestResultsPage)({
                                autoSuggestProducts: autoSuggestProducts.toJSON()
                            }));
                        });
                        $('.sb-search_suggest').slideDown();
                    } else {
                        $('.sb-search_suggest').slideUp();
                    }
                }, 250));

                $("#userProfile-dv").mouseleave(function(){
                    var hovered = $("#userProfile-dv").find(".dropdown-menu").length;
                    !!hovered && $('.dropdown-menu').hide();
                });

                /*$(document).on("click", null, function(e) {
                    var userProfiledv = $("#userProfile-dv");
                    if (!$('#userIcon').is(e.target) && !userProfiledv.is(e.target) && userProfiledv.has(e.target).length == 0) {
                        $("#userProfile-dv").hide();
                    }
                });*/


                /* Search on menu bar End  */
                $(document).on("click", "a[href^='/']", function(event) {
                    href = $(event.currentTarget).attr('href');
                    if (!event.altKey && !event.ctrlKey && !event.metaKey && !event.shiftKey) {
                        event.preventDefault();
//                        url = href.replace("/^\//", '').replace('\#\!\/', '');
                        url = href;
                        window.App.router.navigate(url, {
                            trigger: true
                        });
                    }
                });

            });
        }
    };
});