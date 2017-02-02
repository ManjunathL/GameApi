define([
    'jquery',
    'underscore',
    'backbone',
    'text!templates/footer/faq.html'
], function($, _, Backbone, faqTemplate) {
    var FaqView = Backbone.View.extend({
        el: '.page',
        render: function() {
            $(this.el).html(faqTemplate);
            if(window.location.href.indexOf("faq-shipping") > -1 || window.location.toString().indexOf("faq-returns") > -1 || window.location.toString().indexOf("faq-warranty") > -1){
            document.getElementById("canlink").href = "https://www.mygubbi.com/faq";
            }
            else{
            document.getElementById("canlink").href = window.location.href;
            }
        }
    });
    return FaqView;
});