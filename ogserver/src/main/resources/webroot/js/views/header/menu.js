define([
    'jquery',
    'underscore',
    'backbone',
    'bootstrap',
    'text!templates/header/menu.html',
    'collections/categories',
    'models/userlogin',
    'models/user',
    'models/userShortList',
    'models/autoSearch',
    'models/preSearch'
], function($, _, Backbone, Bootstrap, headerMenuTemplate, Categories, UserLogin, UserModel, UserSL, AutoSearch, PreSearch) {
    var HeaderMenuView = Backbone.View.extend({
        userLogin: new UserLogin(),
        el: '.main-menu-container',
        render: function() {
            var that = this;
            var categories = new Categories();
            var user = new UserModel({'id':'gaurav345'});
            var user_sl = new UserSL({'id':'gaurav345'});
            var auto_search = new AutoSearch();
            var pre_search = new PreSearch();

            window.userLogin = this.userLogin;
            categories.fetch({
                success: function() {
                    var compiledTemplate = _.template(headerMenuTemplate);
                    $(that.el).html(compiledTemplate({
                        "categories": categories,
                        "userLogin": that.userLogin
                    }));
                    $('a[href="' + window.location.hash + '"]').addClass('active');
                    user.fetch({
                        success: function(user) {
                            user_sl.fetch({
                                success: function(usersl) {
                                    auto_search.fetch({
                                        success: function(auto_search) {
                                            pre_search.fetch({
                                                success: function(pre_search) {
                                                        console.log(JSON.stringify(pre_search));
                                                        var compiledTemplate = _.template(headerMenuTemplate);
                                                        $(that.el).html(compiledTemplate({
                                                            "categories": categories,
                                                            "usercln": user.toJSON(),
                                                            "usersl": usersl.toJSON(),
                                                            "a_srch": auto_search.toJSON(),
                                                            "p_srch": pre_search.toJSON()

                                                        }));
                                                        $('a[href="' + window.location.hash + '"]').addClass('active');
                                                    },
                                                    error: function() {
                                                        console.log("error in fetching user pre_search data");
                                                    }
                                                });
                                            },
                                            error: function() {
                                            console.log("error in fetching user auto_search data");
                                        }
                                    });
                                },
                                error: function() {
                                    console.log("error in fetching user shortlist data");
                                }
                            });
                        },
                        error: function() {
                            console.log("error in fetching user data");
                        }
                    });
                },
                error: function() {
                    console.log("error in fetching categories data");
                }
            });
        },
        events: {
            'click a': 'highlightMenuItem'
        },
        highlightMenuItem: function(ev) {
            $('.active').removeClass('active');
            $(ev.currentTarget).addClass('active');
        },
        initialize: function() {
            this.userLogin.on("change", this.render, this);
        }

    })
    return HeaderMenuView;
});