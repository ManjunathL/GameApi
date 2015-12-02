	if (window.$ind === undefined) window.$ind = {};

		// Fixing hovers for devices with both mouse and touch screen
		jQuery.isMobile = /Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(navigator.userAgent);
		jQuery('html').toggleClass('no-touch', ! jQuery.isMobile);

		/**
		 * CSS-analog of jQuery slideDown/slideUp/fadeIn/fadeOut functions (for better rendering)
		 */
		!function(){

			/**
			 * Remove the passed inline CSS attributes.
			 *
			 * Usage: $elm.resetInlineCSS('height', 'width');
			 */
			jQuery.fn.resetInlineCSS = function(){
				for (var index = 0; index < arguments.length; index++){
					var name = arguments[index],
						value = '';
					this.css(name, value);
				}
				return this;
			};

			jQuery.fn.clearPreviousTransitions = function(){
				// Stopping previous events, if there were any
				var prevTimers = (this.data('animation-timers') || '').split(',');
				if (prevTimers.length >= 2){
					this.resetInlineCSS('transition', '-webkit-transition');
					prevTimers.map(clearTimeout);
					this.removeData('animation-timers');
				}
				return this;
			};
			/**
			 *
			 * @param {Object} css key-value pairs of animated css
			 * @param {Number} duration in milliseconds
			 * @param {Function} onFinish
			 * @param {String} easing CSS easing name
			 * @param {Number} delay in milliseconds
			 */
			jQuery.fn.performCSSTransition = function(css, duration, onFinish, easing, delay){
				duration = duration || 250;
				delay = delay || 25;
				easing = easing || 'ease-in-out';
				var $this = this,
					transition = [];

				this.clearPreviousTransitions();

				for (var attr in css){
					if ( ! css.hasOwnProperty(attr)) continue;
					transition.push(attr+' '+(duration/1000)+'s '+easing);
				}
				transition = transition.join(', ');
				$this.css({
					transition: transition,
					'-webkit-transition': transition
				});

				// Starting the transition with a slight delay for the proper application of CSS transition properties
				var timer1 = setTimeout(function(){
					$this.css(css);
				}, delay);

				var timer2 = setTimeout(function(){
					if (typeof onFinish == 'function') onFinish();
					$this.resetInlineCSS('transition', '-webkit-transition');
				}, duration + delay);

				this.data('animation-timers', timer1+','+timer2);
			};
			// Height animations
			jQuery.fn.slideDownCSS = function(duration, onFinish, easing, delay){
				if (this.length == 0) return;
				var $this = this;
				this.clearPreviousTransitions();
				// Grabbing paddings
				this.resetInlineCSS('padding-top', 'padding-bottom');
				var timer1 = setTimeout(function(){
					var paddingTop = parseInt($this.css('padding-top')),
						paddingBottom = parseInt($this.css('padding-bottom'));
					// Grabbing the "auto" height in px
					$this.css({
						visibility: 'hidden',
						position: 'absolute',
						height: 'auto',
						'padding-top': 0,
						'padding-bottom': 0,
						display: 'block'
					});
					var height = $this.height();
					$this.css({
						overflow: 'hidden',
						height: '0px',
						visibility: '',
						position: '',
						opacity: 0
					});
					$this.performCSSTransition({
						height: height + paddingTop + paddingBottom,
						opacity: 1,
						'padding-top': paddingTop,
						'padding-bottom': paddingBottom
					}, duration, function(){
						$this.resetInlineCSS('overflow').css('height', 'auto');
						if (typeof onFinish == 'function') onFinish();
					}, easing, delay);
				}, 25);
				this.data('animation-timers', timer1+',null');
			};
			jQuery.fn.slideUpCSS = function(duration, onFinish, easing, delay){
				if (this.length == 0) return;
				this.clearPreviousTransitions();
				this.css({
					height: this.outerHeight(),
					overflow: 'hidden',
					'padding-top': this.css('padding-top'),
					'padding-bottom': this.css('padding-bottom'),
					opacity: 1
				});
				var $this = this;
				this.performCSSTransition({
					height: 0,
					'padding-top': 0,
					'padding-bottom': 0,
					opacity: 0
				}, duration, function(){
					$this.resetInlineCSS('overflow', 'padding-top', 'padding-bottom').css({
						display: 'none'
					});
					if (typeof onFinish == 'function') onFinish();
				}, easing, delay);
			};
			// Opacity animations
			jQuery.fn.fadeInCSS = function(duration, onFinish, easing, delay){
				if (this.length == 0) return;
				this.clearPreviousTransitions();
				this.css({
					opacity: 0,
					display: 'block'
				});
				this.performCSSTransition({
					opacity: 1
				}, duration, onFinish, easing, delay);
			};
			jQuery.fn.fadeOutCSS = function(duration, onFinish, easing, delay){
				if (this.length == 0) return;
				var $this = this;
				this.performCSSTransition({
					opacity: 0
				}, duration, function(){
					$this.css('display', 'none');
					if (typeof onFinish == 'function') onFinish();
				}, easing, delay);
			};
			// Material design animations
			jQuery.fn.showMD = function(duration, onFinish, easing, delay){
				if (this.length == 0) return;
				this.clearPreviousTransitions();
				// Grabbing paddings
				this.resetInlineCSS('padding-top', 'padding-bottom');
				var paddingTop = parseInt(this.css('padding-top')),
					paddingBottom = parseInt(this.css('padding-bottom'));
				// Grabbing the "auto" height in px
				this.css({
					visibility: 'hidden',
					position: 'absolute',
					height: 'auto',
					'padding-top': 0,
					'padding-bottom': 0,
					'margin-top': -20,
					opacity: '',
					display: 'block'
				});
				var height = this.height();
				this.css({
					overflow: 'hidden',
					height: '0px'
				}).resetInlineCSS('visibility', 'position');
				var $this = this;
				this.performCSSTransition({
					height: height + paddingTop + paddingBottom,
					'margin-top': 0,
					'padding-top': paddingTop,
					'padding-bottom': paddingBottom
				}, duration || 350, function(){
					$this.resetInlineCSS('overflow', 'margin-top', 'padding-top', 'padding-bottom').css('height', 'auto');
					if (typeof onFinish == 'function') onFinish();
				}, easing || 'cubic-bezier(.23,1,.32,1)', delay || 150);
			};
			jQuery.fn.hideMD = function(duration, onFinish, easing, delay){
				if (this.length == 0) return;
				this.clearPreviousTransitions();
				var $this = this;
				this.resetInlineCSS('margin-top');
				this.performCSSTransition({
					opacity: 0
				}, duration || 100, function(){
					$this.css({
						display: 'none'
					}).resetInlineCSS('opacity');
					if (typeof onFinish == 'function') onFinish();
				}, easing, delay);
			};
			// Slide element left / right
			var slideIn = function($this, from){
					if ($this.length == 0) return;
					$this.clearPreviousTransitions();
					$this.css({width: 'auto', height: 'auto'});
					var width = $this.width(),
						height = $this.height();
					$this.css({
						width: width,
						height: height,
						position: 'relative',
						left: (from == 'right') ? '100%' : '-100%',
						opacity: 0,
						display: 'block'
					});
					$this.performCSSTransition({
						left: '0%',
						opacity: 1
					}, arguments[0] || 250, function(){
						$this.resetInlineCSS('position', 'left', 'opacity', 'display').css({width: 'auto', height: 'auto'});
					});
				},
				slideOut = function($this, to){
					if ($this.length == 0) return;
					$this.clearPreviousTransitions();
					$this.css({
						position: 'relative',
						left: 0,
						opacity: 1
					});
					$this.performCSSTransition({
						left: (to == 'left') ? '-100%' : '100%',
						opacity: 0
					}, arguments[0] || 250, function(){
						$this.css({
							display: 'none'
						}).resetInlineCSS('position', 'left', 'opacity');
					});
				};
			jQuery.fn.slideOutLeft = function(){ slideOut(this, 'left'); };
			jQuery.fn.slideOutRight = function(){ slideOut(this, 'right'); };
			jQuery.fn.slideInLeft = function(){ slideIn(this, 'left'); };
			jQuery.fn.slideInRight = function(){ slideIn(this, 'right'); };
		}();
