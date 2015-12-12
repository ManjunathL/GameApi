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
    'models/userShortList'
], function($, _, Backbone, Bootstrap, BootstrapValidator, Firebase, headerMenuTemplate, Categories, Users, UserSL) {
    var HeaderMenuView = Backbone.View.extend({
        users: new Users(),
        el: '.main-menu-container',
        render: function() {
            var that = this;
            var categories = new Categories();

            window.users = this.users;
            categories.fetch({
                success: function() {
                    var compiledTemplate = _.template(headerMenuTemplate);
                    $(that.el).html(compiledTemplate({
                        "categories": categories,
                        "users": that.users
                    }));
                    $('a[href="' + window.location.hash + '"]').addClass('active');
/*
                    user.fetch({
                        success: function(user) {
                            user_sl.fetch({
                                success: function(usersl) {
                                    //console.log(usersl.keys().length);
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
*/
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
            this.users.on("add", this.render, this);
            this.users.on("reset", this.render, this);
        }

    })
    return HeaderMenuView;
});
