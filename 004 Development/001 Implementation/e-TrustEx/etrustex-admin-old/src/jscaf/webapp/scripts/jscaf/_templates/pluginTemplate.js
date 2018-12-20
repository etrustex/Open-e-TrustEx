;(function ( $, window, document, undefined ) {

	// TO DO FIRST : replace every instance of [myPluginName] by your plugin object name


	/* ================================================================= */
	/*     PLUGIN DEFINITION       									     */
	/* ================================================================= */

	var pluginName = "[myPluginName]";







	// DEFAULT OPTIONS IF NOT SUBCLASSED IN DECLARATION
	// ------------------------------------------------

	var	defaults = {

		// defaults settings initialisation

	};	





	// CONSTRUCTOR
	// -----------

	function [myPluginName] ( element, options ) {
		$._log('PLUGIN:'+ pluginName +'.init => ' + element.id);
		
		// cache and defined parent element access for inner functions
		this.id = element.id;
		this.element = element;
		this.$element = $(element);
		this.$elementParent = this.$element.parent();

		// subclass the defaults if provided plugin init (JS side)

		this.settings = $.extend( {}, defaults, options );


		// store properties for later reuse inside inner functions

		this._defaults = defaults;
		this._name = pluginName;


		// for preventing multi plugin instanciation
		$._addClass(element, pluginName);


		// call init functions

		this.bindEvents();
		this.initDisplay();			



	}




	// PLUGIN METHODS
	// --------------
	
	[myPluginName].prototype = {

	    /* ================================================================= */
	    /*     BIND EVENTS : 												 */
	    /*       - events binding for multiple elements        			     */
	    /*       - executed once when plugin is applied to element           */
		/*         during page init phase									 */
		/* ================================================================= */

		bindEvents: function () {
			$._log('PLUGIN:' + pluginName + '.bindEvents');

			var base = this;

			// events inner to element

			base.$element.on('[EVENT]','[SELECTOR:action_[VERB]_[OBJECT]', function() {
				base.actionVerbObject(this);
			});



			// events at same level of element

			base.$elementParent.on('[EVENT]','[SELECTOR]:action_[VERB]_[OBJECT]', function() {
				base.actionVerbObject(this);
			});


	    },







	    /* ================================================================= */
	    /*     INIT DISPLAY : DOM RENDERING	                			     */
	    /*       - executed once when plugin is applied to element           */
		/*         during page init phase									 */
		/* ================================================================= */

	    initDisplay: function() {
			$._log('PLUGIN:' + pluginName + '.initDisplay');

			var base = this;


			// DOM rendering of plugin
	
	    },







	    /* ================================================================= */
	    /*     INNER FUNCTIONS          	                			     */
		/* ================================================================= */

	    actionVerbObject: function(o) {
	    	var id = o.id;

	    	// function handler of event
	    }


	};


	// PLUGIN 
	// ------

	$.fn[ pluginName ] = function ( options ) {
		return this.each(function() {
			
			// multi-initialisation prevention
			
			if (!$._hasClass(this,pluginName)) {
				new [myPluginName]( this, $.extend( $(this).data(), options ) );
			}
		});
	};

})( jQuery, window, document );