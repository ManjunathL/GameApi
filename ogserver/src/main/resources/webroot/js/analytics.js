define([
    'jquery',
    'underscore',
    'text!/templates/analytics/general.html',
    'text!/templates/analytics/thankyou.html'
], function($, _, General, ThankYou) {
    return {
        TYPE_GENERAL: 'general',
        TYPE_THANKYOU: 'thankyou',
        el: '.analytics',
        apply: function(analyticsType) {
            if (applyAnalytics) {
                if (analyticsType === this.TYPE_THANKYOU) {
                    $(this.el).html(ThankYou);
                } else if (analyticsType === this.TYPE_GENERAL) {
                    $(this.el).html(General);
                } else {
                    console.log("ERROR: analytics type NOT configured.");
                }
            }
        }
    };
});