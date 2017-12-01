"use strict";

// the page controller is by default, declare as "p" object literal

var p = {


    // ============================
    //           INIT 
    // ============================

    // the init is the trigger of the page rendering,
    // if not declared, the global default settings will be assumed instead,
    // the returned object allows to subclass every global settings you setup in your tiles/common_app_settings.jsp
    // if empty (={}), the global settings will be applied

    // jSCAF dynamic components can be defined here like this
    // return {
    //      JScomponents: {
    //          JStooltip: true
    //      }
    //}
    // 
    // note that if at least a component is defined, this component will be only rendered !!!
    // by default if not in LIGHT-mode, all components are rendered.
    // full list of components, see var $._cmpDefs inside .../scripts/jscaf/modules/core.js module.

    __init: function() {
        return {
            fnInitPostCall: function() {} // optional this function will be executed after the complete init phase
        }
    },



    // ============================
    //      PAGE INIT DISPLAY
    // ============================

    // init display function will be called during init phase AND also at every ajax STOP event triggered
    // used it therefore with caution, only plugin inits that are common to the page should be executed here

    initDisplay: function() {
        $._L('PAGE.initDisplay');

        //myPlugin.init();
    },



    // ============================
    //      PAGE EVENT BINDINGS
    // ============================

    // binds events proper to that page, application events can be subclassed by using an ".off"
    // before re-declaring the same events at page-level

    bindEvents: function() {
        $._L('PAGE.bindEvents');

        // use an $._AE (=$._addEvent) if the element is declared inside the page form
        //$._AE('[EVENT_TYPE]', '[SELECTOR]', function() {
            // handler code
        //});

        // use an $._ADE (=$._addDocumentEvent) if the element is declared outside the page form
        // this applies to dialogs, which lives inside the form BUT when rendered are transfered outside
        //$._ADE('[EVENT_TYPE]', '[SELECTOR]', function() {
            // handler code
        //});

    }
};
