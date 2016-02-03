define(function() {
    return {
        Views: {},
        create: function(name, View, options) {
            if (typeof this.Views[name] !== 'undefined') {
                this.Views[name].undelegateEvents();
                if (typeof this.Views[name].clean === 'function') {
                    this.Views[name].clean();
                }
            }
            var view = options ? new View(options) : new View();
            this.Views[name] = view;
            return view;
        }
    };
});