/*global syze:true,jQuery:true,$:true,_:true,megaHoverOver:true,megaHoverOut:true,require:true,setTimeout,window,document,navigator,screen,console,moment,p,self,unescape,escape*/

/**=======================================================
 * =======================================================
 * @fileOverview jSCAF Javascript API
 * @author ROELS Gregory - DIGIT.B.1
 * =======================================================
 * ======================================================= */


/* ======================================================= */
/* ===       INITIAL PAGE LOAD - EXECUTED ONCE !        == */
/* ======================================================= */

$(function() {

    //JSCAF VERSION
    //-------------
    $.__VERSION =  '1.10.05 - 07.04.2015';
    //-----------------------------------
    //-----------------------------------




    //console.profile(new Date());


    $._log('jSCAF:' +  $.__VERSION);
    $._log('-------------------------------------------------------------------------');



    $._log('MAIN.document.ready');


    //GETTING APP SETTINGS
    //--------------------
    var $appSettings = $('#jscaf_app_settings');
    $._log('APP.loading global settings');
    if ($appSettings.length) {
        $._log('====>APP settings exists, extending default $._settings');
        $._settings = $.extend( $._settings, $appSettings.data());
    } else {
        $._log('====>APP settings not found, taking default settings');
    }



    //MODULES DEFINITION
    //------------------
    $.__modules = {
        AJAX: $.__AJAX !== undefined,
        DISPLAY: $.__DISPLAY !== undefined,
        UI_DIALOG: $.__UI_DIALOG !== undefined,
        VALIDATION: $.__VALIDATION !== undefined,
        COMPONENTS: $.__COMPONENTS !== undefined,
        BROWSER: $.__BROWSER !== undefined,
        SPA: $.__SPA !== undefined,
        RESOURCES: $.__RESOURCES !== undefined,
    };

    
    // force to single page application detection if the SPA module has been loaded

    if ($.__modules.SPA) {
        $._settings.isSinglePageApp = true;
    }


    //CHECKING THE APP OBJECT DEFINITION
    //----------------------------------
    if (window[$._settings.appName] === undefined && !$._settings.isSinglePageApp) {

        alert('jscaf::ERROR -- MAIN.jSCAF-not-ready\n\nMESSAGE:\n' + $._settings.appName + ' object is not defined !');

        throw new Error('jscaf::ERROR -- MAIN.APP-not-defined');

    } 



    //checking modules definition
    if ( !$.__MAIN.is_jSCAF_ready() ) {

        alert('jscaf::ERROR -- MAIN.jSCAF-not-ready\n\nMESSAGE:\neither CORE or RESOURCES or INIT or UTILS modules are not defined !');

        throw new Error('jscaf::ERROR -- MAIN.jSCAF-not-ready');


    } else {

        //init jSCAF dictionnary
        $.__RESOURCES.load();

        //when the page is loaded, alert the user that the page is rendered
        if ($._settings.isBlockOnLoadActive) {
            $._blockUI();
        }

        //cache the most used dom elements first
        $.__initCachedElements();

        //===============================================================================
        $.__startInit();   // STARTING PAGE INITIALISATION
        //===============================================================================

    }






    //console.profileEnd();


 });




(function ($) {

    "use strict";

    $.extend({

        //DECLARATION
        //-----------
        __MAIN: {

            // MODULES HELPER FUNCTIONS
            // ------------------------

            is_jSCAF_ready: function() {
                //mandatory modules
                return ( $.__coreModuleIdentifier !== undefined &&
                         $.__initModuleIdentifier !== undefined &&
                         $.__utilsModuleIdentifier !== undefined &&
                         $.__modules.RESOURCES);

            }

        }


    });
}(jQuery));
