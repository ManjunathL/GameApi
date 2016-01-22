define([
    'jquery',
    'underscore',
    'backbone',
    'bootstrap',
    'bootstrapvalidator',
    'text!templates/header/menu.html',
    'collections/categories',
    'collections/users',
    'models/userShortList',
    'models/autoSearch',
    'models/preSearch',
    'views/header/menu_helper',
    'mgfirebase'
], function($, _, Backbone, Bootstrap, BootstrapValidator, headerMenuTemplate, Categories, Users, UserSL, AutoSearch, PreSearch, menuHelper, MGF) {
    var HeaderMenuView = Backbone.View.extend({
        users: null,
        categories: null,
        auto_search: null,
        pre_search: null,
        el: '.main-menu-container',
        render: function() {
            var that = this;
            window.users = this.users;
            this.categories.fetch({
                success: function() {
                    that.pre_search.fetch({
                        success: function() {
                            menuHelper.getUserProfileWithCB(that.renderSub);
                        },
                        error: function() {
                            console.log("error in fetching user pre_search data");
                        }
                    });
                },
                error: function() {
                    console.log("error in fetching user categories data");
                }
            });
        },
        renderSub: function(userProfile) {
            var compiledTemplate = _.template(headerMenuTemplate);
            $(this.el).html(compiledTemplate({
                "categories": this.categories,
                "users": this.users,
                "p_srch": this.pre_search.toJSON(),
                "a_srch": this.auto_search.toJSON(),
                "userProfile": userProfile
            }));
            menuHelper.ready(this);
        },
        events: {},
        initialize: function() {
            this.users = new Users();
            this.categories = new Categories();
            this.auto_search = new AutoSearch([{
                "resultName": "sofa cum bed",
                "query": "/api/products/search/sofa+cum+bed"
            }, {
                "resultName": "sofas by material",
                "query": "/api/products/search/sofa+cum+bed"
            }, {
                "resultName": "sofa set",
                "query": "/api/products/search/sofa+cum+bed"
            }, {
                "resultName": "fabric sofa sets",
                "query": "/api/products/search/sofa+cum+bed"
            }, {
                "resultName": "sofa cum bed with storage",
                "query": "/api/products/search/sofa+cum+bed"
            }, {
                "resultName": "sofas by speciality",
                "query": "/api/products/search/sofa+cum+bed"
            }, {
                "resultName": "sofa cover",
                "query": "/api/products/search/sofa+cum+bed"
            }]);
            this.pre_search = new PreSearch();
            this.users.on("add", this.render, this);
            this.users.on("reset", this.render, this);
            this.listenTo(Backbone, 'shortlist.change', this.handleShortlistChange);
            _.bindAll(this, 'renderSub');
        },
        handleShortlistChange: function() {
            var slItems = MGF.getShortListedItems();
            $('#shortlistSuperScript').html(slItems ? Object.keys(slItems).length : '');
        }
    })
    return HeaderMenuView;
});