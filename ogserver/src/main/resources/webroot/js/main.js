require.config({
  waitSeconds: 20,
  paths: {
    jquery: 'libs/jquery/dist/jquery.min',
    jqueryui: 'libs/jquery-ui/jquery-ui',
    underscore: 'libs/underscore/underscore-min',
    lodash: 'libs/lodash/lodash.min',
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
