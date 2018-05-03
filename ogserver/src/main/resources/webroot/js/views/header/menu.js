define([
  'jquery',
  'lodash',
  'backbone',
  'bootstrap',
  'text!templates/header/menu.html',
  'collections/authsettings',
  'views/header/menu_helper',
  'models/authsetup'
], function($, _, Backbone, Bootstrap, headerMenuTemplate, Authsettings, menuHelper, Authsetup){
  var HeaderMenuView = Backbone.View.extend({
    el: '.main-menu-container',
    authsettings: null,
    authsetup: null,
    initialize: function () {
        this.authsettings = new Authsettings();
        this.authsetup = new Authsetup();
       // this.getAuthentication();
       this.listenTo(Backbone);
       _.bindAll(this, 'renderSub');
    },
    render: function() {
        var that = this;
        that.renderSub();
        //$(this.el).html(_.template(headerMenuTemplate));

        //$('a[href="' + window.location.hash + '"]').addClass('active');
    },
    renderSub: function(){
        $(this.el).html(_.template(headerMenuTemplate));
        $('a[href="' + window.location.hash + '"]').addClass('active');
        menuHelper.ready(this);
    },
    events: {
    },
    getAuthentication: function(){
        var that = this;
        alert("sssssssssssssssssssss");
        var form = new FormData();
        form.append("username", "rajnish.kumar2291@gmail.com");
        form.append("password", "welcome");
        form.append("grant_type", "password");
        form.append("client_id", "clientIdPassword");

        var settings = {
          "async": true,
          "crossDomain": true,
          "url": authbaseRestApiUrl+"MyGubbiAuth/oauth/token",
          "method": "POST",
          "headers": {
            "authorization": "Basic Y2xpZW50SWRQYXNzd29yZDpzZWNyZXQ="
          },
          "processData": false,
          "contentType": false,
          "mimeType": "multipart/form-data",
          "data": form
        }

        $.ajax(settings).done(function (response) {
          that.fetchAuthrender(response);
        });
    },
    fetchAuthrender: function (authTokenObj) {
        var that = this;
        console.log("authTokenObj++++++++++++++++");
        var authTokenObj = $.parseJSON(authTokenObj);
        console.log(authTokenObj.access_token);

         sessionStorage.authtoken = "";

        if(typeof(authTokenObj.access_token) !== 'undefined'){
            sessionStorage.authtoken = authTokenObj.access_token;
        }else{
            sessionStorage.authtoken = "";
        }

        if(typeof(authTokenObj.userId) !== 'undefined'){
            sessionStorage.userId = authTokenObj.userId;
        }else{
            sessionStorage.userId = "user1234600";
        }
        sessionStorage.userId = "user1234600";

        //$("#accessToken").val(sessionStorage.authtoken);
    }
  })

  return HeaderMenuView;
});

