require.config({
    paths: {
        jquery: 'libs/jquery-2.1.1',
        jqueryui: 'libs/jquery-ui/jquery-ui',
        underscore: 'libs/underscore/underscore-min',
        lodash: 'libs/lodash/lodash.min',
        backbone: 'libs/backbone/backbone-min',
        firebase: 'libs/firebase/firebase',
        select2: 'libs/select2.full.min',
        chosenjquery: 'libs/chosen.jquery',
        upload: 'libs/upload',
        text: 'libs/text/text',
        bootstrap: 'libs/bootstrap/dist/js/bootstrap.min',
        bootstrapvalidator: 'libs/bootstrap-validator/dist/validator.min',
        imagesloaded: 'libs/imagesloaded/imagesloaded.pkgd',
        wookmark: 'libs/wookmark/wookmark',
        pinterest_grid: 'libs/pinterest_grid',
        mixitup: 'libs/jquery.mixitup.min',
        owlcarousel: 'libs/owl.carousel/dist/owl.carousel.min',
        templates: '../templates'
    },
    shim: {
       'bootstrap': {
           deps: ['jquery', 'jqueryui'],
           exports: 'Bootstrap'
       },
       'bootstrapvalidator': {
           deps: ['bootstrap', 'jquery'],
           exports: 'Bootstrapvalidator'
       },
       'backbone': {
           deps: ['underscore', 'jquery'],
           exports: function () {
               return Backbone.noConflict();
           }
       },
       'firebase': {
           deps: ['jquery'],
           exports: 'firebase'
       }
    },
    map: {
       '*': {
           'css': 'libs/require-css/css'
       }
    }
    });
require([
  'views/app',
  'router',
  'vm'
], function(AppView, Router, Vm){
  var appView = Vm.create({}, 'AppView', AppView);
  appView.render();
  Router.initialize({appView: appView});
});

//var baseApiUrl = "https://localhost:1443";
//var baseApiUrl = "http://localhost:1442";
var baseApiUrl = "https://gameuat.mygubbi.com:1443";

var authbaseRestApiUrl = "http://45.112.138.146:8080/";
var baseRestApiUrl = "http://45.112.138.146:8080/";


//var authbaseRestApiUrl = "http://172.20.20.76:9000/";
//var baseRestApiUrl = "http://172.20.20.76:9100/";