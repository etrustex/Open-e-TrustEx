;(function ( $, window, document, undefined ) {


	/* ================================================================= */
	/*     PLUGIN DEFINITION											 */
	/* ================================================================= */

	var pluginName = "JSinlineEdit";







    /* ================================================================= */
    /*     DEFAULT SETTINGS          	                			     */
	/* ================================================================= */

	var	defaults = {

		modelUrl: null,

		insertFragmentId: 'add_new_fragment',
		insertUrl: 'fragments/insert.html',
		insertButtonTitle: 'insert new record',
		insertFragmentTitle: 'INSERT NEW RECORD',

		editFragmentId : 'edit_fragment',
		editUrl: 'fragments/insert.html',
		editButtonTitle : 'edit',
		editFragmentTitle: 'EDIT CURRENT RECORD',

		deleteUrl: null,
		deleteButtonTitle: 'delete',
		fnDelete: null,

		fnCancel: null,
		fnSave: null,

		isValidationActive: true,
		validationMessage: 'Some mandatory fields are not filled in'

	};	







    /* ================================================================= */
    /*     CONSTRUCTOR		          	                			     */
	/* ================================================================= */

	function JSinlineEdit ( element, options ) {
		$._log('PLUGIN:JSinlineEdit.init => ' + element.id);
		
		// cache and defined parent element access for inner functions
		this.id = element.id;
		this.element = element;
		this.$element = $(element);
		this.$elementParent = this.$element.parent();

		// subclass the defaults if provided in declaration

		this.settings = $.extend( {}, defaults, options );

		// store properties for later reuse inside inner functions

		this._defaults = defaults;
		this._name = pluginName;



		// for disabling multi plugin instanciation

		$._addClass(element, pluginName);

		// states

		this.insertState = false;
		this.editState = false;
		this.currentRow = null;

		this.model = {};



		// call init functions

		if (this.settings.modelUrl === null) {
			this.bindEvents();
			this.initDisplay();			
		} else {
			this.renderContent();
		}	

	}




    /* ================================================================= */
    /*     PLUGIN METHODS	          	                			     */
	/* ================================================================= */
	
	JSinlineEdit.prototype = {


		renderContent: function() {

			var base = this;

            $._AJRT({
                call: base.settings.modelUrl,
                templateId: base.id + '_content_template',
                isBlockerActive: false,
                fnPostCall: function() {
                	base.model = $._ajaxJsonArray;
					base.bindEvents();
					base.initDisplay();			
                }
            });			

		},




	    /* ================================================================= */
	    /*     BIND EVENTS : 												 */
	    /*       - events binding for multiple elements        			     */
	    /*       - executed once when plugin is applied to element           */
		/*         during page init phase									 */
		/* ================================================================= */

		bindEvents: function () {
			$._log('PLUGIN:JSinlineEdit.bindEvents');

			var base = this;

			base.$element.closest('.box-wrapper').on('click','#action_insert_new_record', function() {
				base.actionInsertNewRecord($(this));
			});

			base.$elementParent.on('click','#' + base.id + ' .action_edit', function() {
				base.actionEdit($(this));				
			});

			base.$elementParent.on('click', '.action_save', function() {
				base.actionSave($(this));
			});

			base.$elementParent.on('click', '.action_cancel', function() {
				base.actionCancel($(this));
			});

	    },





	    /* ================================================================= */
	    /*     INIT DISPLAY : DOM RENDERING	                			     */
	    /*       - executed once when plugin is applied to element           */
		/*         during page init phase									 */
		/* ================================================================= */

	    initDisplay: function() {
			$._log('PLUGIN:JSinlineEdit.initDisplay');

			var base = this;

			base.$element.closest('.box-wrapper')
			     .find('.header')
			     .append('<div class="header-right mr"><button id="action_insert_new_record" type="button" class="btn btn-success btn-xs edit-buttons">' + base.settings.insertButtonTitle + '</button></div>');
			
			base.$element.find('thead tr')
				 .append('<th width="150px" class="center">ACTIONS</th>');
			
			base.$element.find('tbody tr')
				 .append('<td class="center"><p class="edit-buttons"><button type="button" class="btn btn-primary btn-xs action_edit">' + base.settings.editButtonTitle + '</button> <button type="button" class="btn btn-danger btn-xs action_delete">' + base.settings.deleteButtonTitle + '</button></p></td>');
			
	    },







	    /* ================================================================= */
	    /*     INNER FUNCTIONS          	                			     */
		/* ================================================================= */

	    actionInsertNewRecord: function($o) {

	    	var base = this;

			$o.closest('.box').find('.content').prepend('<div id="' + base.settings.insertFragmentId + '" class="hidden"></div>');	 

			$._ajaxRefresh({
				id: base.settings.insertFragmentId,
				call: base.settings.insertUrl,
				fnPostCall: function() {
					
					var $addNewFragment = $('#' + base.settings.insertFragmentId);

					base.decorate($addNewFragment);
					
					$addNewFragment.removeClass('hidden')
								   .find('h1').text(base.settings.insertFragmentTitle);
				
					base.insertState = true;

					$('.edit-buttons').addClass('hidden');

				}
			});
	    },


	    actionEdit: function($o) {
			var base = this,
				rowId = $o.closest('tr').attr('id');

			$('#'+rowId).after('<tr id="edit_container" class="sub-row hidden"><td id="' + base.settings.editFragmentId + '" colspan="' + $(this).closest('table').find('th').length + '"></td></tr>');		

			$._ajaxRefresh({
				id: 'edit_fragment',
				call: base.settings.editUrl,
				fnPostCall: function() {

					var $editFragment = $('#' + base.settings.editFragmentId);

					base.decorate($editFragment);	

					$('#edit_container').removeClass('hidden')
					$editFragment.find('h1').text(base.settings.editFragmentTitle);

					base.editState = true;
					base.currentRow = rowId;

					$('.edit-buttons').addClass('hidden');

				}
			});				

	    },


	    actionSave: function($o) {

	    	var base = this;

			if (base.settings.isValidationActive && !$._validate(base.$elementParent)) {

				$._notifyError(base.settings.validationMessage);

			} else {


				$._ajaxRefresh({
					id:'innerFragment',
					call:'index-inner.html',
					fnPreCall: function() {
						$._SPV('rowId',base.currentRow);
					},
					fnPostcall: function() {
						base.reset();
					}
				});

			}
	    },

	    actionCancel: function($o) {
	    	var base = this;

			if (base.insertState) {
				$('#add_new_fragment').remove();
			} 

			if (base.editState) {
				$('#edit_container').remove();
			}

			base.reset();
	    },



	    decorate: function($container) {
			$container.wrapInner('<div class="inner-box blue"></div>');
			$container.find('.inner-box').prepend('<h1></h1>');
			$container.find('.inner-box').append('<br><br><p class="fr"><button type="button" class="btn btn-primary btn-sm action_save">SAVE</button>  <button type="button" class="btn btn-default btn-sm action_cancel">cancel</button></p><br>');
	    },


	    reset: function() {
			//reset states
			var base = this;

			base.insertState = false;
			base.editState = false;
			base.currentRow = null;

			$('.edit-buttons').removeClass('hidden');

			$._removeNotifications();
	    }




	};





    /* ================================================================= */
    /*     APPLYING PLUGIN TO ELEMENT  	                			     */
	/* ================================================================= */

	$.fn[ pluginName ] = function ( options ) {
		return this.each(function() {
			
			// multi-initialisation prevention
			
			if (!$._hasClass(this,pluginName)) {
				new JSinlineEdit( this, $.extend( $(this).data(), options ) );
			}
		});
	};

})( jQuery, window, document );