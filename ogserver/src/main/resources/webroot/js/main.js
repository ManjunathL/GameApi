require.config({
  paths: {
    jquery: 'libs/jquery/jquery-min',
    underscore: 'libs/underscore/underscore-min',
    lodash: 'libs/lodash/lodash',
    backbone: 'libs/backbone/backbone-min',
    text: 'libs/text/text',
    bootstrap: 'libs/bootstrap/bootstrap',
    shim: {
        'bootstrap' : ['jquery']
    },
    templates: '../templates'
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
