define(['sly'], function(sly) {

    var SlyUtil = {
        create: function($wrap, node_element, next, previous, active) {
            return new Sly(node_element, {
                horizontal: 1,
                itemNav: 'basic',
                smart: 1,
                activateMiddle: 0,
                activateOn: 'click',
                mouseDragging: 1,
                touchDragging: 1,
                releaseSwing: 1,
                startAt: 0,
                scrollBy: 1,
                speed: 300,
                elasticBounds: 1,
                easing: 'easeOutExpo',
                dragHandle: 1,
                dynamicHandle: 1,
                clickBar: 1,
                prevPage: $wrap.find(previous),
                nextPage: $wrap.find(next)
            }, {
                active: active
            });
        }
    };
    return SlyUtil;
});