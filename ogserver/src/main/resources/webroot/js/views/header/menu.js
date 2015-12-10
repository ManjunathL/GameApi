define([
    'jquery',
    'underscore',
    'backbone',
    'bootstrap',
    'text!templates/header/menu.html',
    'collections/categories',
    'models/userlogin',
    'models/user',
    'models/userShortList'
], function($, _, Backbone, Bootstrap, headerMenuTemplate, Categories, UserLogin, UserModel, UserSL) {
    var HeaderMenuView = Backbone.View.extend({
        userLogin: new UserLogin(),
        el: '.main-menu-container',
        render: function() {
            var that = this;
            var categories = new Categories();
            var user = new UserModel({'id':'gaurav345'});
            var user_sl = new UserSL({'id':'gaurav12345345'});

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
                                    console.log(usersl.keys().length);
                                    var compiledTemplate = _.template(headerMenuTemplate);
                                    $(that.el).html(compiledTemplate({
                                        "categories": categories,
                                        "usercln": user.toJSON(),
                                        "usersl": usersl.toJSON()
                                    }));
                                    $('a[href="' + window.location.hash + '"]').addClass('active');
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