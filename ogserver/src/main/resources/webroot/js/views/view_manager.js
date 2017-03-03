define(function() {
    return {
        DASHBOARD: "dashboard",
        PRODUCT_LISTING: "productListing",
        PRODUCT_DETAILS: "productDetails",
        USER_PROFILE: "userProfile",
        CONSULT: "consult",
        SHORTLIST: "shortlist",
        STORIES: "stories",
        STORY: "story",
        DIY: "diy",
        ABOUT: "about",
        TERMS: "terms",
        PRIVACY_POLICY: "privacy_policy",
        CAREERS: "careers",
        FAQ: "faq",
        MGDIFF: "mgdiff",
        THANKYOU: "thankyou",
        LANDINGPAGE: "landingpage",
        NEWLANDINGPAGE: "newlandingpage",
        FBLANDINGPAGE: "fblandingpage",
        MEDIAPAGE: "mediapage",
        NRIPAGE: "nripage",
        MANGALORELANDINGPAGE: "MangaloreLandingPage",
        EMAILLANDINGPAGE: "EmailLandingPage",
        SHOBHALANDINGPAGE: "shobhalandingpage",
        PUNELANDINGPAGE: "punelandingpage",
        HOLIDAYLANDINGPAGE: "holidaylandingpage",
        COMPLETEDPROJECTSPAGE: "completedprojectspage",
        NEWPRODUCT_DETAILSPAGE: "NewProductDetailsPage",
        MYACCOUNTPAGE: "MyAccountPage",
        MYNESTPAGE: "MyNestPage",
        MYSETTINGSPAGE: "MySettingsPage",
        REGISTER_USER: "RegisterUser",
        QUALITYCHECK: "QualityCheckPage",
        TYPEOFKITCHENPAGE: "typeofkitchen",
        ONLINEPAYMENTPAGE: "OnlinePaymentPage",
        ONLINEPAYMENTSUCCESSPAGE: "OnlinePaymentSuccessPage",
        ONLINEPAYMENTFAILUREPAGE: "OnlinePaymentFailurePage",
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