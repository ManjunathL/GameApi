define([
    'jquery',
    'underscore',
    'backbone',
    'bootstrap',
    'text!templates/header/menu.html',
    'collections/categories',
    'models/userlogin'
], function($, _, Backbone, Bootstrap, headerMenuTemplate, Categories, UserLogin) {
    var HeaderMenuView = Backbone.View.extend({
        userLogin: new UserLogin(),
        el: '.main-menu-container',
        render: function() {
            var that = this;
            var categories = new Categories();
            window.userLogin = this.userLogin;
            categories.fetch({
                success: function() {
                    var compiledTemplate = _.template(headerMenuTemplate);
                    $(that.el).html(compiledTemplate({
                        "categories": categories,
                        "userLogin": that.userLogin
                    }));
                    $('a[href="' + window.location.hash + '"]').addClass('active');
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
