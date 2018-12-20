;(function ( $, window, document, undefined ) {


	/* ================================================================= */
	/*     PLUGIN DEFINITION       												 */
	/* ================================================================= */

	var pluginName = "JSwizard";









    /* ================================================================= */
    /*     DEFAULT SETTINGS          	                			     */
	/* ================================================================= */

	var	defaults = {

		isModuleByStepActive: false,
		activeStepIndex: null,
		stepBaseTitle: 'STEP'

	};	







    /* ================================================================= */
    /*     CONSTRUCTOR		          	                			     */
	/* ================================================================= */

	function JSwizard ( element, options ) {
		$._log('PLUGIN:'+ pluginName +'.init => ' + element.id);
		
		// cache and defined parent element access for inner functions
		this.id = element.id;
		this.$element = $(element);
		this.$elementParent = this.$element.parent();
		this.$steps = this.$element.find('li');

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






    /* ================================================================= */
    /*     PLUGIN METHODS	          	                			     */
	/* ================================================================= */
	
	JSwizard.prototype = {

	    /* ================================================================= */
	    /*     BIND EVENTS : 												 */
	    /*       - events binding for multiple elements        			     */
	    /*       - executed once when plugin is applied to element           */
		/*         during page init phase									 */
		/* ================================================================= */

		bindEvents: function () {
			$._log('PLUGIN:' + pluginName + '.bindEvents');
			
			var base = this;

			base.$element.on('click','li', function() {
				base.actionActivateStep(this);
			});

			base.$elementParent.on('click','#' + this.id + '_action_wizard_next_panel', function() {
				base.actionActivateStepByIndex($._activeWizardIndex+1);
			});

			base.$elementParent.on('click','#' + this.id + '_action_wizard_previous_panel', function() {
				base.actionActivateStepByIndex($._activeWizardIndex-1);
			});

	    },







	    /* ================================================================= */
	    /*     INIT DISPLAY : DOM RENDERING	                			     */
	    /*       - executed once when plugin is applied to element           */
		/*         during page init phase									 */
		/* ================================================================= */

	    initDisplay: function() {
			$._log('PLUGIN:' + pluginName + '.initDisplay');

			var stepWidth = 100/this.$steps.length,
				activeStep,
				base = this;


			// decorating the wizard and steps

			base.$element.addClass('wizard-steps');

			base.$steps.each(function() {
				var $li = $(this), 
				    $data = $li.data(), 
				    idx = $li.index()+1;
				    
				$li.css({'width':stepWidth+'%'})
				   .append('<a class="selected"><span class="h2">' + base.settings.stepBaseTitle + ' ' + idx + '</span><span class="dot"><span></span></span><span class="label">' + $data.stepSubTitle + '</span></a>');
			});

			

			// appending step content

			base.$elementParent.append('<br><div id="' + base.id + '_step_content" class="wizard-step-content"></div>');
			


			// appending prev / next buttons

			base.$elementParent.append('<button id="' + base.id + '_action_wizard_previous_panel" type="button" class="fl btn btn-default btn-sm">PREVIOUS</button> <button id="' + this.id + '_action_wizard_next_panel" type="button" class="fr btn btn-primary btn-sm">NEXT</button>  ')


			// activate the first step by default

			activeStep = base.$steps.filter('.active').index();

			if (activeStep === -1) {
				if (base.settings.activeStepIndex !== null) {
					base.actionActivateStepByIndex(base.settings.activeStepIndex);
				} else {
					$._log('PLUGIN.JSwizard ===> ERROR : unable to find an active step, add an "active" class in the declaration OR provide an active step in the activeStepIndex options during initialisation (JS side)');
				}
			} else {
				base.actionActivateStepByIndex(activeStep);
			}

		
	    },







	    /* ================================================================= */
	    /*     INNER FUNCTIONS          	                			     */
		/* ================================================================= */

	    actionActivateStepByIndex: function(index) {
	    	var base = this;
	    	base.actionActivateStep(base.$steps[index]);
	    },


	    actionActivateStep: function(o) {
			var $o = $(o),
			    id = o.id,
			    base = this;

			$._ajaxRefresh({
				id: base.id + '_step_content',
				call: 'fragments/' + id + '.html',
				fnPostCall: function() {
				
					$._wizardStepSwitch($o.index(), null, true, base.id);	

					if (base.settings.isModuleByStepActive) {	
						$._require('mod_' + id, function() {
							window[id].init();
						});
					}
				
				}
			});	    	
	    }	    

	};





    /* ================================================================= */
    /*     APPLYING PLUGIN TO ELEMENT  	                			     */
	/* ================================================================= */

	$.fn[ pluginName ] = function ( options ) {
		return this.each(function() {
			
			// multi-initialisation prevention
			
			if (!$._hasClass(this,pluginName)) {
				new JSwizard( this, $.extend( $(this).data(), options ) );
			}
		});
	};

})( jQuery, window, document );