define([
    'jquery',
    'underscore',
    'backbone',
    'bootstrap',
    'bootstrapvalidator',
    'firebase',
    'text!templates/header/menu.html',
    'collections/categories',
    'collections/users',
    'models/userShortList',
    'models/autoSearch',
    'models/preSearch'
], function($, _, Backbone, Bootstrap, BootstrapValidator, Firebase, headerMenuTemplate, Categories, Users, UserSL, AutoSearch, PreSearch) {
    var HeaderMenuView = Backbone.View.extend({
        users: new Users(),
        categories: new Categories(),
        auto_search: new AutoSearch([
                                      {
                                        "resultName": "sofa cum bed",
                                        "query": "/api/products/search/sofa+cum+bed"
                                      },
                                      {
                                        "resultName": "sofas by material",
                                        "query": "/api/products/search/sofa+cum+bed"
                                      },
                                      {
                                        "resultName": "sofa set",
                                        "query": "/api/products/search/sofa+cum+bed"
                                      },
                                      {
                                        "resultName": "fabric sofa sets",
                                        "query": "/api/products/search/sofa+cum+bed"
                                      },
                                      {
                                        "resultName": "sofa cum bed with storage",
                                        "query": "/api/products/search/sofa+cum+bed"
                                      },
                                      {
                                        "resultName": "sofas by speciality",
                                        "query": "/api/products/search/sofa+cum+bed"
                                      },
                                      {
                                        "resultName": "sofa cover",
                                        "query": "/api/products/search/sofa+cum+bed"
                                      }
                                    ]),
        pre_search: new PreSearch(),
        el: '.main-menu-container',
        render: function() {
            var that = this;
            window.users = this.users;
            this.categories.fetch({
                success: function() {
                    that.pre_search.fetch({
                        success: function() {
                            var compiledTemplate = _.template(headerMenuTemplate);
                            $(that.el).html(compiledTemplate({
                                "categories": that.categories,
                                "users": that.users,
                                "p_srch": that.pre_search.toJSON(),
                                "a_srch": that.auto_search.toJSON()
                            }));
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
        initialize: function() {
            this.users.on("add", this.render, this);
            this.users.on("reset", this.render, this);
        }
    })
    return HeaderMenuView;
});
