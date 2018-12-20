/*global syze:true,jQuery:true,$:true,_:true,megaHoverOver:true,megaHoverOut:true,require:true,setTimeout,window,document,navigator,screen,console,moment,p,self,unescape,escape*/




/* ======================================================= */
/* ===                 JSCAF INIT                       == */
/* ======================================================= */

(function ($) {

    "use strict";

    $.extend({

        __initModuleIdentifier: function() {
            return true;
        },

        /* ======================================================= */
        /* ===             INIT FUNCTIONS                       == */
        /* ======================================================= */


        __startInit: function() {

            $._log('INIT.startInit <= this is where all begins');

            var initObject = null;

            //by default we look for a page object

            if ( window.p !== undefined ) {
                initObject = window.p;
            }


            //in the case of a modules-based page controller, a mod object is wrapping the modules definition
            if ( window.mod !== undefined ) {
                initObject = window.mod;
            }



            if ( initObject === null && !$._settings.isPrototypeMode ) {

                //init only the mandatory stuffs for a basic page (logout/etc...)
                $.__initCommon();
                $.__initPostCommon();

            } else {    

                // we are in the prototype mode !!
                if ( initObject === null && $._settings.isPrototypeMode ) {

                    $.__PROTOTYPE.init();
                    $._init();

                } else {

                    if ( initObject.__init === undefined ) {

                        // the page controller is empty, the default _init() function is called
                        $._init();

                    } else {                    

                        var initResult = initObject.__init(),
                            initResultType = typeof initResult;

                        //the returned value of the p.__init function must be either 
                        
                        //a function

                        if ( initResultType === 'function' ) {

                            //the function is executed, the function is assuming the call to the $._init() function
                            initResult();


                        //or an settings object         

                        } else if ( initResultType === 'object' ) {

                            //the result of the settings subclassed in the p.__init are injected into the core $._init() function called    
                            $._init( initResult );

                        } else {
                            
                            throw new Error('jscaf::ERROR -- your page controller __init function must return a settings object OR a function  - cannot initialise the page');

                        }

                        initResult = initResultType = null;

                    }
                }    

            } 

            initObject = null;

        },





        /* ======================================================= */
        /* ===             MAIN INIT FUNCTION                   == */
        /* ======================================================= */


        // DEFAULT INIT & PARAMETERS
        // -------------------------
        /**
         * @description
         * The page initialisation function, must be called during document.ready() on page controller to initialize the current page. This will ensure callbacks to own p.initDisplay() and p.bindEvents() on the page controller.
         *
         * @name _init
         * @memberOf $
         * @class .
         * @param opt the object literal parameter {object} <br><i>default : $._settings</i>
         * @example
         * //basic call on page controller document.ready()
         * var p = {
         *   __init: function() { return {}; },  //optional  
         *   initDisplay: function() { ... $._initDisplay(); }, //optional
         *   bindEvents: function() { ... } // optional
         * };
         *
         *
         * //dynamic component example
         * var p = {
         *   __init: function() { 
         *       return {
         *          JScomponents: {
         *              JStooltip: true,
         *              JSbuttonSet: true
         *          }
         *   },      
         *   initDisplay: function() { ... } // optional
         *   bindEvents: function() { ... } // optional
         * };
         *
         */
        _init: function (opt, fn) {
            $._log('INIT.init');

            //extending the jSCAF settings objects with override options
            $._settings = $._extend($._settings, opt);

            //checking some settings values
            if ($._settings.jscafRootUrl === null) {
                $._settings.jscafRootUrl = $._getContextPath();
            }


            //init pre call function
            if ($._settings.fnInitPreCall !== null) {
                $._execFn($._settings.fnInitPreCall);
            }
            
            //branching on init type : light/full
            if ($._settings.isLightInitialisationActive) {

                $.__initLight();

            } else {

                $.__initFull();

            }
        },


        __initLight: function() {
            $._log('INIT.init LIGHT');

            $.__initCommon();

            //dynamic initPage function, with load innerFragment or override function if provided
            $.__initPageLight();

            //calling callback function if provided
            if ($._settings.fnInitPostCall !== null) {
                $._execFn($._settings.fnInitPostCall, true);
            }

        },


        __initFull: function() {
            $._log('INIT.init FULL');


            //Do init only if browser is supported
            if ($.__modules.BROWSER) {
                if (!$.__BROWSER.isCheckBrowserSupported) {
                    return;
                }
            }

            $.__initCommon();

            //dynamic initPage function, with load innerFragment or override function if provided
            if ($._settings.isPageDefaultInit) {
                //init page => normal way
                $.__initPageFull();
            } else {
                //init page => the page has it's own defined init function
                if (null !== $._settings.fnPageInitOverrideCallback) {
                    $._execFn($._settings.fnPageInitOverrideCallback);
                } else {
                    $._initDisplay();
                }
            }

            //check if the onBeforeUnload must be bound, warning the user if the browser/tab is closed before saving the actual page
            if ($._settings.isOnBeforeUnloadActive) {
                $._addOnBeforeUnloadEvent();
            }

            //enable timeoutDialog for alerting user when the sessions will soon expire
            if (!$._isEmpty($._settings.timeoutDialogProperties) && $.__modules.UI_DIALOG) {
                $.__UI_DIALOG.initTimeoutDialog();
            }


        },


        __initCommon: function() {
            $._log('INIT.initCommon');


            //initialisation common events
            if ($.__modules.AJAX) {
                $.__AJAX.initAjaxEvents();
            }

            //set the browser class on top html tag for targeting special browser in css declarations
            if ($.__modules.BROWSER) {
                $._addClass($._body,$.__BROWSER.getBrowserShortName().toLowerCase());
            }    

            if ($._settings.isFlatThemeActive) {
                $._addClass($._body,'flat');
            } else {
                if ($._$body.hasClass('flat')) {
                    $._settings.isFlatThemeActive = true;
                }
            }

            if ($._settings.isBootstrapActive) {
                $._addClass($._body,'bootstrap');
            }            

            //components init
            if ($.__modules.COMPONENTS) {
                //define the components array
                $.__COMPONENTS.define();                
                //initialises event handlers for common JS components
                $.__COMPONENTS.bindEvents();
            }     

            //plugins init
            //if ($.__modules.PLUGINS) {
                //load the plugins defined in $._settings.JSplugins on app settings OR page settings definitions
            //    $.__PLUGINS.load();
            //}                

            //try to execute the application controller init and bindEvents
            if (typeof window[$._settings.appName].__init === 'function') {
                $._execFn(window[$._settings.appName].__init);         
            }
            if (typeof window[$._settings.appName].bindEvents === 'function') {
                $._execFn(window[$._settings.appName].bindEvents);
                $.__initAppComponents();
            }

            //call initEvents : events not part of components but general to jSCAF
            $.__initGlobalEvents();

        },


        __initGlobalEvents: function() {

            //bind events for slidebars if active
            if ($._settings.isSlidebarsActive) {
                $._$document.on('click','.JS_slidebar-toggle', function() {
                    $._slidebars.slidebars.toggle('left');
                    if ($.__modules.DISPLAY) {
                        $.__DISPLAY.refreshResponsiveness();
                    }                    
                });
            }


        },


        __initPageLight: function() {
            $._log('INIT.initPage LIGHT');

            $.__initPostCommon();

            //$._initDisplay(); //fix Sysper bug : called twice during light init, already called in initPostCommon

            $._execFn($._settings.fnPageBindEventsCallback);
        },

        __initPageFull: function() {
            $._log('INIT.initPage FULL');

            //checking if prototype-mode
            if ($._settings.isPrototypeMode) {
            
                $.__PROTOTYPE.includePageContent();

                $.__initPostCommon();
            
            } else {

                //load inner fragment is needed
                if ($._settings.hasPageInnerFragment) {
                    //for the $._initDisplay not being called twice, this is set to false by default on the Ajax stop common event,
                    // p.initDisplay(), called on ajax complete will handle the call to $._initDisplay() by itself
                    $._settings.isInitDisplayOnAjaxStop = false;
                    if ($.__modules.AJAX) {
                        $.__AJAX.loadInnerFragment();
                    }    
                } else {
                    if ($._settings.koActive) {
                        $.__initKnockoutJS();
                    }
                    $.__initPostCommon();
                }

                //callback to the p.bindEvents() function to apply bindings on proper page elements
                $._execFn($._settings.fnPageBindEventsCallback);

            }




        },


        __initKnockoutJS: function() {

            //init knockout view model and bindings
            $._log('INIT.KnockoutJS: active');

            //retrieving model and applying the viewModel bindings

            if ($._settings.koViewModel === null) {

                $._log('INIT.KnockoutJS: p.ViewModel is not defined, unable to init KnockoutJS');

            } else {

                if ($._settings.koModelJsonGet !== null && $.__modules.AJAX) {

                    $._log('INIT.KnockoutJS.getting page model : ' + $._settings.koModelJsonGet.callOverride);
                    
                    $._ajaxJsonGet({
                        callOverride: $._settings.koModelJsonGet.callOverride,
                        fnPostCall: function() {
                            $.__koModel = $._ajaxJsonArray;    
                            $._log('INIT.KnockoutJS.page setting model in $.__koModel : ' + JSON.stringify($.__koModel));

                            ko.applyBindings(new $._settings.koViewModel());                                
                        }
                    });

                //applying the viewModel bindings, assuming the model json get is done manually    
                } else {

                    ko.applyBindings(new $._settings.koViewModel());                                

                }

            }
        },


        __initPostCommon: function(isCalledAfterAjaxError) {

            $._L('INIT.initPostCommon');


            //reset the init display flag for future ajax calls
            $._settings.isInitDisplayOnAjaxStop = true;

            //reveal the body
            $._removeClass($._main,'hidden');
            $._unblockUI();

            //Display module init

            if (!isCalledAfterAjaxError) {  // no need to re-initialise the base display after an ajax error
                if ($.__modules.DISPLAY) {
                    $.__DISPLAY.init();
                }
            }

            //callback the p.initDisplay() function of the page controller
            $._initDisplay();

            //load the translation messages if active
            if ($._settings.isI18nActive) {
                $.__initI18n();
            }

            //setting generation time in the footer if enabled
            if ($._settings.isPageGenerationTimeDisplayed) {
                if (!$._$footer.children().hasClass('generation_time')) {
                    $._$footer.append(
                        '<div class="generation_time center very-small text-color-grey">' +
                            $._getData('jscaf_common_generated_in') + $._getGenerationTime() + ')' +
                            '</div>');
                }
            }

            //calling callback function if provided
            if ($._settings.fnInitPostCall !== null && !isCalledAfterAjaxError) {
                $._execFn($._settings.fnInitPostCall, true);
            }




        },




        /* ======================================================= */
        /* ===             APP COMPONENTS INITIALISATION        == */
        /* ======================================================= */


        __initAppComponents: function () {
            $._log('INIT.initAppComponents');

            if ($._settings.appComponents === null) {
                return;
            }

            var cmpArray = [],
                prop;
            
            window[$._settings.appName]._cmp = {};
            for (prop in $._settings.appComponents) {
                if ($._settings.appComponents.hasOwnProperty(prop)) {
                    //replacing timestamp for force caching @deploy time
                    window[$._settings.appName]._cmp[prop] = window[$._settings.appName].__cmpDefs[prop]; //.replace('@pagescript.timestamp@', $._settings.appBuildTimestamp);
                    //keep only the components for later registering
                    cmpArray.push(prop);
                }
            }

            //require base configuration
            require.config({
                paths: window[$._settings.appName]._cmp
            });

            //requesting components and registering the components objects once loaded
            require(cmpArray, function () {
                var i, cmpArrayLength = cmpArray.length;
                
                for (i = 0; i < cmpArrayLength; i++) {
                    $._pageComponents.push(window[cmpArray[i]]);
                }
                $._pageComponentsBindEvents();
                
                i = cmpArrayLength = null;
            });


        }







    });
}(jQuery));




