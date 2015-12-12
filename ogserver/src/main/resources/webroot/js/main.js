require.config({
  paths: {
    jquery: 'libs/jquery/dist/jquery.min',
    jqueryui: 'libs/jquery.ui/ui/widget',
    underscore: 'libs/underscore/underscore-min',
    lodash: 'libs/lodash/lodash.min',
    cloudinary: 'libs/cloudinary/js/jquery.cloudinary',
    backbone: 'libs/backbone/backbone-min',
    text: 'libs/text/text',
    bootstrap: 'libs/bootstrap/dist/js/bootstrap.min',
    bootstrapvalidator: 'libs/bootstrap-validator/dist/validator.min',
    firebase: 'libs/firebase/firebase',
    templates: '../templates'
  },
  shim: {
    'bootstrap': {
        deps: ['jquery'],
        exports: 'Bootstrap'
    },
    'bootstrapvalidator': {
            deps: ['bootstrap', 'jquery'],
            exports: 'Bootstrapvalidator'
    },
    'backbone': {
        deps: ['underscore', 'jquery'],
        exports: function() {
            return Backbone.noConflict();
        }
    },
    'cloudinary': {
        deps: ['jquery', 'jqueryui'],
        exports: 'cloudinary'
    },
    'firebase': {
        deps: ['jquery'],
        exports: 'firebase'
    }
  }
});

require([
  'views/app',
  'router'
], function(AppView, Router){
  var appView = new AppView();
  appView.render();
  Router.initialize({"appView": appView});
});

var imgBase = "https://res.cloudinary.com/univermal/image/upload/"; //todo: change the collection name
var restBase = "https://localhost:8080"; //todo: make this empty for production
var firebaseBase = "https://sweltering-fire-6356.firebaseio.com/";
