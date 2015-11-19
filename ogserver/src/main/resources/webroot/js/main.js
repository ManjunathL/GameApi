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
    templates: '../templates'
  },
  shim: {
    'bootstrap': {
        deps: ['jquery'],
        exports: 'Bootstrap'
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

var imgBase = "http://res.cloudinary.com/univermal/image/upload/"; //todo: change the collection name
var restBase = "http://localhost:8080"; //todo: make this empty for production
