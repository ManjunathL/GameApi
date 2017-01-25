require.config({
        baseUrl: "js",
        paths: {
        jquery: 'libs/jquery/dist/jquery.min',
        jqueryui: 'libs/jquery-ui/jquery-ui',
        underscore: 'libs/underscore/underscore-min',
        backbone: 'libs/backbone/backbone-min',
        text: 'libs/text/text',
        jqueryeasing: 'libs/jquery.easing/js/jquery.easing.min',
        sly: 'libs/sly/dist/sly',
        bxslider: 'libs/bxslider-4/jquery.bxslider.min',
        bootstrap: 'libs/bootstrap-custom/js/bootstrap.min',
        bootstrapvalidator: 'libs/bootstrap-validator/dist/validator.min',
        firebase: 'libs/firebase/firebase',
        cloudinary_jquery: 'libs/cloudinary-jquery.mg',
        unveil: 'libs/unveil/jquery.unveil.mg',
        zepto: 'libs/zepto-full/zepto.min',
        /*fresco: 'libs/frescojs-light/js/fresco/fresco',*/
        jquerywaterfall: 'libs/jquery.waterfall/waterfall-light',
        highlight: 'libs/highlight/highlight',
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
        },
        'cloudinary_jquery': {
            deps: ['jquery'],
            exports: 'cloudinary-jquery'
        },
        'unveil': {
            deps: ['jquery'],
            exports: 'unveil'
        },
        'zepto': {
            deps: ['jquery'],
            exports: 'zepto'
        }
/*        'fresco': {
            deps: ['jquery'],
            exports: 'fresco'
        }*/
    },
    map: {
        '*': {
            'css': 'libs/require-css/css'
        }
    }
});

require([
    'views/app',
    'router'
], function (AppView, Router) {
    var appView = new AppView();
    appView.render();
    Router.initialize({"appView": appView});
});

var imgBase = "https://res.cloudinary.com/mygubbi/image/upload/f_auto/";