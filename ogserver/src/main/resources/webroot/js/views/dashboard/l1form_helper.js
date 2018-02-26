define([
    'jquery',
    'chosenjquery',
    'libs/jquery.bootstrap-duallistbox',
    'libs/bootstrap-tagsinput',
    'libs/nouislider.min',
    'libs/slick.min',
    'select2'
], function($, chosen, bootstrapDualListbox, tagsinput, nouislider, slick, select2) {
    return {
        getSelect2: function() {
            $(".select2_demo_1").select2({width: "100%"});
            $(".select2_demo_2").select2({width: "100%"});
            $(".select2_demo_3").select2({
                placeholder: "Select a state",
                allowClear: true
            });
        },
        ready: function(parent) {

            _.bindAll(this, 'getSelect2');

            jQuery(function($) {
                'use strict';

                alert("Hiiiiiiiiiii I am Here");

                $('.chosen-select').chosen({width: "100%"});

                $('.tagsinput').tagsinput({
                    tagClass: 'label label-primary'
                });
                $('.dual_select').bootstrapDualListbox({
                    selectorMinimalHeight: 160
                });

            });
        }
    };
});