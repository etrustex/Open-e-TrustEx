/*global syze:true,jQuery:true,$:true,_:true,megaHoverOver:true,megaHoverOut:true,require:true,setTimeout,window,document,mod,navigator,screen,console,moment,p,self,unescape,escape*/
/**
 *  @fileOverview jSCAF plugin
 *  @author ROELS Gregory - DIGIT.B.3
 *  @version 1.6-beta - 02.09.2013
 *
 */


/* ======================================================= */
/* ===                 JSCAF SPA INIT                   == */
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

            $._log('SPA-INIT.startInit <= this is where all begins');

            var init = mod.__init();

            var paths = {
                    //base, names must stay as it is !!
                    base: 'app/app_base',
                    baseEvents: 'app/app_base_events'
            };


            if (init.controllers !== undefined) {
                paths = $._extend( paths, init.controllers );
            }

            if (init.services !== undefined) {
                paths = $._extend( paths, init.services );
            }

            var config = {
                baseUrl: init.baseUrl,

                //just for local file for the script not being cached => replaced by token at build time
                urlArgs: "v=" + new Date().getTime(),

                //name to path values
                paths: paths
            };

            require.config( config );

            require(['base','baseEvents'], function(app, appEvents) {
                var settings = $._extend({

                    fnInitPostCall: 'app.init',
                    fnPageBindEventsCallback: 'appEvents.bindEvents'

                }, init.settings);

                $._init( settings );
            });

        },





        /* ======================================================= */
        /* ===             MAIN INIT FUNCTION                   == */
        /* ======================================================= */

        _init: function (opt, fn) {
            $._log('SPA-INIT.init');

            //extending the jSCAF settings objects with override options
            $._settings = $._extend($._settings, opt);


            //init pre call function
            if ($._settings.fnInitPreCall !== null) {
                $._execFn($._settings.fnInitPreCall);
            }

            if (!$.__modules.SPA) {
                $._log('====>ERROR : in order to use the SPA mode, SPA module should be explicitely loaded');
                return;
            } else {

                $.__SPA.display({url:'/home'});

                $.__SPA.bindEvents();

                //when in SPA mode, then every components should be explicitely declared
                $._settings.isLightInitialisationActive = true;

                //initialisation common events
                if ($.__modules.AJAX) {
                    $.__AJAX.initAjaxEvents();
                }

                if ($.__modules.DISPLAY) {
                    $.__DISPLAY.init();
                }

                //set the browser class on top html tag for targeting special browser in css declarations
                if ($.__modules.BROWSER) {
                    $._addClass($._body,$.__BROWSER.getBrowserShortName().toLowerCase());
                }

                //components init
                if ($.__modules.COMPONENTS) {
                    //define the components array
                    $.__COMPONENTS.define();
                    //initialises event handlers for common JS components
                    $.__COMPONENTS.bindEvents();
                }

                //reveal the body
                $._removeClass($._main,'hidden');
                $._unblockUI();

                //dynamic initPage function, with load innerFragment or override function if provided
                $._initDisplay();

                $._execFn($._settings.fnPageBindEventsCallback);

            }

            //calling callback function if provided
            if ($._settings.fnInitPostCall !== null) {
                $._execFn($._settings.fnInitPostCall, true);
            }

        }


    });
}(jQuery));




