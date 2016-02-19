/**
 * Created by mygubbi on 18/2/16.
 */
define([
    'jquery'
], function($) {
    return {

        ready: function() {
            $('img').each(function() {
                if(!($(this).hasClass('img-responsive'))){
                    if(!($(this).hasClass('non_resp_img'))){
                        $(this).addClass('img-responsive');
                    }
                }
            });
        }
    };
});