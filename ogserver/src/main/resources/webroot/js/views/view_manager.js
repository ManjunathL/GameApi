define(function() {
    return {
        HOMEPAGE:"HomePage",
        CONCEPTBOARDPAGE: "ConceptboardPage",
        PRODUCT_LISTING: "ProductListingPage",
        L1FORMPAGE: "L1formPage",
        EDITDESIGNPAGE: "EditDesignPage",
        LISTINGDETAILSPAGE: "ListingDetailsPage",
        L3FORMPAGE: "L3formPage",
        PRODUCTDETAILSPAGE: "ProductDetailsPage",
        EDITPRODUCTPAGE: "EditProductPage",
        LISTINGCONCEPTPAGE: "ListingConceptPage",
        SEARCHCONCEPTPAGE:"SearchConceptPage",
        USERPROFILEPAGE:"UserProfilePage",
        USERPREFERENCEPAGE:"UserPreferencePage",
        VIEWDESIGNSPAGE:"ViewDesignPage",
        EVERYTHINGCONCEPTPAGE:"EverythingConceptPage",
        LISTINGPROJECTPAGE:"ListingProjectPage",
        Views: {},
        activeView: "",
        create: function(name, View, options) {
            if (typeof this.Views[name] !== 'undefined') {
                this.Views[name].undelegateEvents();
                this.Views[name].stopListening();
                if (typeof this.Views[name].clean === 'function') {
                    this.Views[name].clean();
                }
            }
            var view = options ? new View(options) : new View();
            this.Views[name] = view;
            this.activeView = name;
            return view;
        }
    };
});