/*global syze:true,jQuery:true,$:true,_:true,megaHoverOver:true,megaHoverOut:true,require:true,setTimeout,window,document,navigator,screen,console,moment,p,self,unescape,escape*/


/* ======================================================= */
/* ===                 JSCAF CORE                       == */
/* ======================================================= */

(function ($) {

    "use strict";

    $.extend({

        __coreModuleIdentifier: function() {
            return true;
        },


        //loaded during main document.ready
        __modules: {},

        //jSCAF SETTINGS
        //--------------

        /**
         * @see CORE.JS
         * @class .
         * @memberOf $
         * @name _settings
         * @description todo
         * @example todo
         */

        _settings: {

            appName: 'APP',
            appVersion: null,
            appBuildDate: null,
            appDevelopmentBuild: true,
            contextPath: null,
            language: 'en',
            appBuildTimestamp: '',

            isLoggingEnabled: true, //TODO : DEPRECATE ! => replaced by delog ANT target at build time, to avoid calling the $._L / $._log helpers on PROD env.
            onlineHelpRootPath: '/help',

            dateFilterPrefix: null,
            dateFilterFormatName: null,
            isFormParamDispatchValueUsed: false,
            isFormParamActionUsed: false,

            fnDefaultAjaxBeforeSend: function (jqXHR, settings) {},
            fnDisplayErrorDialogOverride: null,
            isBrowserDetectionEnabled: true,
            isBrowserWarningEnabled: true,
            isPageGenerationTimeDisplayed: true,
            isPageZoomWarningEnabled: true,
            isFieldValidationUpdateDisplay: false,
            ecasRedirectFailedUrl: '/pages/outOfSession.html',
            isCleanIE9AjaxHtmlTags: false,
            isFormDispatchCallTypeUsed: false,
            isDisplaySettingsUsed: true,
            prototypeInnerFragmentPageUrl: null,
            hasPageInnerFragment: false,
            innerFragmentLoadActionName: 'load',
            innerFragmentName: 'innerFragment',
            isInitDisplayOnAjaxStop: false,
            isAjaxErrorDisplay: true,
            isAjaxErrorNotificationDisplayOnly: false,
            fnPageInitDisplayCallback: 'p.initDisplay',
            fnPageBindEventsCallback: 'p.bindEvents',
            isI18nActive: false,
            i18nResourcesLocation: null,
            i18nResourcesBundleName: null,
            isResponsiveTopBar: true,
            isPageDefaultInit: true,
            fnPageInitOverrideCallback: null,
            pageFormNameOverride: null,
            pageActionUrlOverride: null,
            isOnBeforeUnloadActive: false,
            timeoutDialogProperties: {},
            isLightInitialisationActive: false,
            fnInitPreCall: null,
            fnInitPostCall: null,
            isSinglePageApp: false,
            isFlatThemeActive: false,
            isPrototypeMode: false,
            isDisableEnterKeyOnForm: true,
            jscafRootUrl: null,
            isMinimalScreenWidthDetectionActive: true,
            isMinimalScreenWidthDetectionActiveForIE78Only: false,
            isKeepTopNavigationFixed: false,

            JScomponents: null,
            appComponents: null,

            isBootstrapActive: false,
            isBootstrapPrimaryActive: false,
            isSlidebarsActive: false,

            isBlockOnLoadActive: true,


            koActive: false,
            koModelJsonGet: null,
            koViewModel: null

        },


        //GLOBAL CACHED ELEMENTS
        //----------------------
        _$main: null,
        _main: null,
        _$mainContent: null,
        _$form: null,
        _$html: null,
        _html: null,
        _$body: null,
        _body:null,
        _$header: null,
        _$footer: null,
        _$document: null,
        _$slidebars: null,


        //LOCAL JSCAF VARIABLES
        //---------------------
        __pageStartTime: new Date(),
        __isInitPhase: true,


        //COMPONENTS DECLARATION
        //----------------------
        __cmp: $.__cmpDefs,

        __cmpDefs: {

            //bundled components

            JSapproveToggle: false,
            JSautocomplete: false,
            JSbuttonSet: false,
            JSfieldNumber: false,
            JSdatepicker: false,
            JStooltip: false,
            JSdialog: false,
            JSslider: false,
            JSradio: false,
            JScheckbox: false,
            JSstars: false,
            JStopNav: false,
            JStopBar: false,
            JStableSorter: false,
            JStableSorterServer: false,
            JSlistCounterSmall: false,
            JScounterBox: false,
            JSpagination: false,
            JStableFilter: false,
            JSfileInput: false,
            JShelp: false,
            JStoggleButton: false,
            JSselect2: false,
            JSsortable: false,
            JSmaxLength: false,
            JSmaskedInput: false,
            JSbsPopover: false,
            JSradioList: false,
            JSopenDialogContent: false,
            JSopenPdfDialog: false,
            JSopenVideoDialog: false,
            JSaccordionBox: false,
            JSsummernote: false,
            JSlistsTransfer: false,
            JSviewDocument: false,

            //plugins

            JSinlineEdit: false,
            JSwizard: false
        },



        /* ======================================================= */
        /* ===             PRE-INIT FUNCTIONS @doc.ready        == */
        /* ======================================================= */

        __initCachedElements: function () {
            $._log('CORE.initCachedElements');

            $._$main = $('#main');
            $._$mainContent = $('#main-content');

            //checking if the main and main-content are existing, if not create them and wrap the body content over
            if (!$._$main.length) {
                $._$body.children().wrapAll('<div id="main"></div>');
                $._$main = $('#main');
            }
            if (!$._$mainContent.length) {
                $._$main.children().wrapAll('<div id="main-content"></div>');
                $._$mainContent = $('#main-content');
            }


            //$._$form = $('form');
            $._$form = $._$mainContent.find('form');
            $._$html = $('html');
            $._$body = $('body');
            $._$document = $(document);
            $._$header = $('#header');
            $._$footer = $('#footer');

            $._html = $._$html[0];
            $._body = $._$body[0];
            $._main = $._$main[0];

        },


        __globMessagesArray: $.__globMessagesArray || [],

        __initGlobalMessages: function () {
            $._log('CORE.initGlobalMessages');




        },


        //loads an translate key value pairs on the page
        //an associated json file must be located right besides the page controller with the same root name
        //if page controller = page.js, the json file will be : page_en.json and page_fr.json by default
        //a complete resourceBundle can be provided using $._settings.i18nResourcesLocation + $._settings.i18nResourcesBundleName

        __i18nData: null,

        __initI18n: function () {

            var url;

            if ( $._settings.i18nResourcesLocation === null ) {
                var pageScriptFileName = $('#page_script').attr('src');
                pageScriptFileName = pageScriptFileName.substr(0, pageScriptFileName.indexOf('.'));
                url = pageScriptFileName + '_' + $._settings.language + '.json';
            } else {
                url = $._getContextPath() + $._settings.i18nResourcesLocation + '/' + $._settings.i18nResourcesBundleName + '_' + $._settings.language + '.json';
            }

            $._isBlockerPage = false;

            $.ajax({
                dataType: "json",
                url: url,
                cache: true,
                success: function (data) {
                    $.__i18nData = data;
                }
            });

            $._isBlockerPage = true;

        },

        // called on every initDisplay if active for the current page
        __refreshI18n: function() {
            if ( $.__i18nData !== null ) {
                $('.i18n').each(function () {
                    var $this = $(this),
                        attributes = $this.data();
                    $this.text($.__i18nData[attributes.key]);
                });
            }
        },


        /**
         * @see CORE.JS
         * @class .
         * @memberOf CORE.....$
         * @name _initDisplay
         * @type function
         * @default empty
         * @description todo
         * @example todo
         */
        _initDisplay: function (fn) {
            $._log('CORE.initDisplay');

            //init components elements defined in $document.ready of the page script
            if ($.__modules.COMPONENTS) {
                $.__COMPONENTS.initDisplay();
            }


            //calling application initDisplay function
            if ($._settings.fnAppInitDisplay !== null && !$._settings.isSinglePageApp) {

                if (typeof window[$._settings.appName].initDisplay === 'function') {
                    $._execFn(window[$._settings.appName].initDisplay);
                }

            }


            //calling page initDisplay
            if ($.__isFunctionExists($._settings.fnPageInitDisplayCallback) && !$._settings.isSinglePageApp) {

                //check if the page initDisplay function does not contain a call to the _initDisplay (to avoid infinite loop)

                var objLiteral = null,
                    fnString = $._settings.fnPageInitDisplayCallback,
                    sep = fnString.indexOf('.'),
                    initDisplayPos;

                fn = null;

                if (sep > 0) {
                    objLiteral = fnString.substring(0, sep);
                    fn = fnString.substring(sep + 1);

                    initDisplayPos = window[objLiteral][fn].toString().indexOf('$._initDisplay');

                } else {
                    fn = fnString;

                    initDisplayPos = window[fn].toString().indexOf('$._initDisplay');
                }


                if (initDisplayPos < 0) {
                    $._execFn($._settings.fnPageInitDisplayCallback, true);
                } else {
                    throw new Error('ERROR !!!! initDisplay CANNOT BE CALLED WITHIN your PAGE initDisplay function');
                }


                objLiteral = fn = fnString = sep = initDisplayPos = null;

            }


            //by default every time the ajaxStop global events is triggered, the _initDisplay() is called,
            //this can be set to false on the fnPreCall option of the ajax call (using $._ajax function) to avoid
            //this automatic initDisplay and if you want to handle this by yourself.
            $._settings.isInitDisplayOnAjaxStop = true;

            //fill client-height when not done at first init (because of the page was full filled)
            $.__fillClientHeight();

            //activate Syze plugin on page init
            if ($.__modules.DISPLAY) {
                $.__DISPLAY.syze();
            }

            //refresh i18n if active for the current page
            if ( $._settings.isI18nActive ) {
                $.__refreshI18n();
            }

            //init slidebars plugin if activated
            if ( $._settings.isSlidebarsActive ) {
                $._slidebars = new $.slidebars();
                if ( $._settings.isSlidebarsOpen ) {
                    $._slidebars.slidebars.open('left');
                }
            }

            //calling callback function if provided
            $._execFn(fn, true);

        },


        __fillClientHeight: function () {
            if (!$._settings.isBootstrapPrimaryActive) { //don't execute this if bootstrap is the primary lib

                if ($._body.offsetHeight < screen.height) {

                    var height;
                    if ($._hasClass($._html,'ismobile')) {
                       height = (window.innerHeight || document.documentElement.clientHeight) - $('#header').height();
                    } else {
                       height = (window.innerHeight || document.documentElement.clientHeight) - $('#header').height() - $('#footer').height();
                    }

                    if ( $._settings.isFlatThemeActive ) {
                        height -= 50;
                    }  else {
                        if (!$('#header').length) {
                            height -= 30;
                        } else {
                            height -= 140;
                        }
                    }

                    $._log('CORE.fillClientHeight => height:' + height);

                    //fill the client height to adjust same height on smaller height pages
                    if ($._$main[0] !== undefined && $._$mainContent[0] !== undefined) {
                        $._$main[0].style.minHeight = height + 'px';
                        $._$mainContent[0].style.minHeight = height + 'px';
                    }

                    if ($.__modules.SPA) {
                        var pageContainer = $._getById('spa-page-container');
                        if (pageContainer !== null) {
                            pageContainer.style.minHeight = height + 20 + 'px';
                        }
                    }

                    height = null;
                }
            }
        },





        /* ============================================================================================== */
        /* ===             BROWSER DETECTION in case BROWSER module is not declared                    == */
        /* ============================================================================================== */

        /**
         * @see CORE.JS
         * @class .
         * @memberOf CORE.....$
         * @name _isIE
         * @description todo
         * @example todo
         */
        _isIE: function() {
            return $._hasClass($._html,'ie');
        },

        /**
         * @see CORE.JS
         * @class .
         * @memberOf CORE.....$
         * @name _isIE7
         * @description todo
         * @example todo
         */
        _isIE7: function() {
            return $._hasClass($._html,'ie7');
        },

        /**
         * @see CORE.JS
         * @class .
         * @memberOf CORE.....$
         * @name _isIE8
         * @description todo
         * @example todo
         */
        _isIE8: function() {
            return $._hasClass($._html,'ie8');
        },

        /**
         * @see CORE.JS
         * @class .
         * @memberOf CORE.....$
         * @name _isIE9
         * @description todo
         * @example todo
         */
        _isIE9: function() {
            return $._hasClass($._html,'ie9');
        },






        /**
         * @see CORE.JS
         * @class .
         * @memberOf CORE.....$
         * @name _getContextPath
         * @description todo
         * @example todo
         */
        _getContextPath: function () {
            var returnContextPath = null;

            try {
                if ($._settings.contextPath === null) {
                    returnContextPath = '..';
                } else {
                    returnContextPath = $._settings.contextPath;
                }
            } catch (err) {
                returnContextPath = '..';
            }

            if (returnContextPath === null) {
                returnContextPath = '..';
            }
            return returnContextPath;
        },




        // BLOCKER DURING REQUEST
        // ----------------------

        _isBlockerPage: true,
        _blockUIMessageInfo: null,


        _getBlockUIMessage: function () {

            if ($._blockUIMessageInfo !== null) {
                return  '<div class="blocker-wrapper">' +
                    '<br><div class="loader"></div>' +
                    '<br>' +
                    '<span class="blocker">' +
                    $._getData('jscaf_common_wait') + '<br>' +
                    '<span class="blocker">' +
                    $._blockUIMessageInfo +
                    '</span><br>' +
                    '</span><br><br>' +
                    '</div>';
            } else {
                return  '<div class="blocker-wrapper">' +
                    '<br><div class="loader"></div>' +
                    '<br>' +
                    '<span class="blocker">' +
                    $._getData('jscaf_common_wait') +
                    '</span><br><br>' +
                    '</div>';
            }

        },

        /**
         * @see CORE.JS
         * @class .
         * @memberOf CORE.....$
         * @name _blockUI
         * @description todo
         * @example todo
         */
        _blockUI: function (postCall) {
            $._log('CORE.blockUI');

            $.blockUI({
                message: $._getBlockUIMessage(),
                overlayCSS: { backgroundColor: '#7c7c7d' },
                baseZ: 55000, // overlay popup
                fadeIn: 0,
                fadeOut: 0
            });

            // Remove UI message otherwise it will be reused for all blocker without messages.
            $._blockUIMessageInfo = null;

            $._execFn(postCall, true);
        },

        /**
         * @see CORE.JS
         * @class .
         * @memberOf CORE.....$
         * @name _unblockUI
         * @description todo
         * @example todo
         */
        _unblockUI: function (postCall) {
            $._log('CORE.unblockUI');

            document.body.style.cursor = "default";
            $.unblockUI();

            $._execFn(postCall, true);

        },

        /**
         * @see CORE.JS
         * @class .
         * @memberOf CORE.....$
         * @name _elementBlock
         * @description todo
         * @example todo
         */
        _elementBlock: function (o,isShowMessage) {

            if (isShowMessage === undefined) {
                isShowMessage = true;
            }

            if (isShowMessage) {
                o.block({
                    message: $._getBlockUIMessage(),
                    overlayCSS: { backgroundColor: '#7c7c7d' },
                    baseZ: 55000 // overlay popup
                });
            } else {
                o.block({
                    message: null,
                    overlayCSS: { backgroundColor: '#7c7c7d' },
                    baseZ: 55000 // overlay popup
                });
            }

            // Remove UI message otherwise it will be reused for all blocker without messages.
            $._blockUIMessageInfo = null;
        },

        /**
         * @see CORE.JS
         * @class .
         * @memberOf CORE.....$
         * @name _elementUnblock
         * @description todo
         * @example todo
         */
        _elementUnblock: function (o) {
            o.unblock();
        },



        /* ======================================================= */
        /* ===             COMMON PAGE FUNCTIONS                == */
        /* ======================================================= */


        /**
         * @see CORE.JS
         * @class .
         * @memberOf CORE.....$
         * @name _getPageForm
         * @description todo
         * @example todo
         */
        _getPageForm: function () {
            if ($._settings.pageFormNameOverride === null) {
                var f = $($._$form[0]).attr('id');

                if (f === undefined) {
                    return null;
                } else {
                    return f;
                }

            } else {
                return $._settings.pageFormNameOverride;
            }
        },

        /**
         * @see CORE.JS
         * @class .
         * @memberOf CORE.....$
         * @name _getPageUrl
         * @description todo
         * @example todo
         */
        _getPageUrl: function (formActionOverride) {
            $._log('CORE.getPageUrl');

            var pageUrl = null,
                formId = $._getPageForm();

            if ($._settings.pageActionUrlOverride === null) {
                if (formActionOverride === null || formActionOverride === undefined) {
                    if (formId !== null) {
                        pageUrl = $._getAttr($._getById(formId),'action').replace(',DanaInfo=www.cc.cec+', '');
                    } else {
                        if ($._$form[0] !== undefined) {
                            pageUrl = $._getAttr($._$form[0],'action').replace(',DanaInfo=www.cc.cec+', '');
                        }
                    }
                } else {
                    pageUrl = formActionOverride;
                }

                if (pageUrl !== undefined && pageUrl !== null) {
                    if ($('#dstb-id').length /*|| typeof(DSHost) != 'undefined'*/) {
                        pageUrl = pageUrl.replace('https://myremote.ec.europa.eu', '')
                            .replace(',DanaInfo=www.cc.cec+', '')
                            .replace(',DanaInfo=www.cc.cec,dom=1,CT=sxml+', '');
                    }
                } else {
                    return null;
                }

            } else {
                pageUrl = $._getContextPath() + $._settings.pageActionUrlOverride;
            }


            if (pageUrl.indexOf($._getContextPath()) < 0 && pageUrl !== 'PROTOTYPE') {
                if (pageUrl.indexOf('http://')>=0) {
                    pageUrl = $._getContextPath() + pageUrl.substr(7).substr(pageUrl.substr(7).indexOf('/'));
                } else {
                    if (pageUrl.indexOf('https://')>=0) {
                        pageUrl = $._getContextPath() + pageUrl.substr(8).substr(pageUrl.substr(8).indexOf('/'));
                    } else {
                        pageUrl = $._getContextPath() + pageUrl;
                    }
                }
            }


            return pageUrl;
        },




        //todo : TO BE DEPRECATED and replaced by _submitForm2 only (temporary)
        _submitForm: function (dispatchValue, action, isBlocker, fnPostCall, formId, isFormInIframe) {
            $._submitForm2({
                dispatchValue: dispatchValue,
                action: action,
                isBlocker: isBlocker,
                fnPostCall: fnPostCall,
                formId: formId,
                isFormInIframe: isFormInIframe
            });
        },

        /**
         * @see CORE.JS
         * @class .
         * @memberOf CORE.....$
         * @name _submitForm2
         * @description todo
         * @example todo
         */

        //todo: TO BE RENAMED TO submitForm when the older one will be removed
        _submitForm2: function (opt) {

            var settings = $.extend({
                call: null,
                dispatchValue: null,
                action: null,
                isBlocker: true,
                fnPostCall: null,
                formId: null,
                isFormInIframe: false,
                isFormInObject: false,
                isIframeInDialog: false,
                validateElementId: null
            }, opt);

            var dispatchValue = null;
            var $dispatchValue;

            if (settings.call !== null) {
                dispatchValue = settings.call;
            } else {
                dispatchValue = settings.dispatchValue;
            }

            $._log('CORE.submitForm : ' + $._getPageForm() + ' - dispatchValue:' + dispatchValue + ' - action:' + settings.action);

            if (settings.isBlocker) {
                if (settings.isIframeInDialog) {
                    $._elementBlock($('.ui-dialog'));
                } else {
                    $._blockUI();
                }
            }

            if (dispatchValue !== null) {

                if (settings.formId !== null) {

                    if (!settings.isFormInIframe && !settings.isFormInObject) {

                        if ($('iframe').length) {
                            settings.isFormInIframe = true;
                        }

                        if ($('object').length) {
                            settings.isFormInObject = true;
                        }
                    }

                    if (settings.isFormInIframe) {
                        $dispatchValue = $('iframe').contents().find('#' + settings.formId).find('#dispatchValue');
                    } else {
                        if (settings.isFormInObject) {
                            $dispatchValue = $($('object').get(0).contentDocument).find('#dispatchValue');
                        } else {
                            $dispatchValue = $('#' + settings.formId).find('#dispatchValue');
                        }
                    }


                } else {
                    $dispatchValue = $('#dispatchValue');
                }

                if ($dispatchValue.length) {
                    $dispatchValue.val(dispatchValue);
                } else {
                    if ($._settings.isFormParamDispatchValueUsed) {
                        $._setParamValue('dispatchValue', dispatchValue, false, settings.formId, settings.isFormInIframe);
                    }
                }
            }
            if (settings.action !== null) {
                var $action;

                if (settings.formId !== null) {
                    if (settings.isFormInIframe) {
                        $action = $('iframe').contents().find('#' + settings.formId).find('#action');
                    } else {
                        if (settings.isFormInObject) {
                            $dispatchValue = $($('object').get(0).contentDocument).find('#action');
                        } else {
                            $action = $('#' + settings.formId).find('#action');
                        }
                    }
                } else {
                    $action = $('#action');
                }


                if ($action.length) {
                    $action.val(settings.action);
                } else {
                    if ($._settings.isFormParamActionUsed) {
                        $._setParamValue('action', settings.action, false, settings.formId, settings.isFormInIframe);
                    }
                }
            }

            //set the submit call type explicitly to be known server-side
            if ($._settings.isFormDispatchCallTypeUsed) {
                $._setParamValue('dispatchCallType', 'SUBMIT', false, settings.formId, settings.isFormInIframe);
            }

            $._injectPageParams({
                formId: settings.formId,
                isFormInIframe: settings.isFormInIframe,
                isFormInObject: settings.isFormInObject
            });

            if (settings.formId !== null) {

                if (settings.isFormInIframe) {
                    $('iframe').contents().find('#' + settings.formId).submit();
                } else {
                    if (settings.isFormInObject) {
                        $($('object').get(0).contentDocument).find('#' + settings.formId).submit();
                    } else {
                        $('#' + settings.formId).submit();
                    }
                }
            } else {
                $._$form.submit();
            }


            if (settings.fnPostCall !== null) {
                var isValid = true;

                if (settings.validateElementId !== null) {
                    if (settings.isFormInIframe) {
                        isValid = $('iframe').contents().find('#' + settings.validateElementId).length === 0;
                    } else if (settings.isFormInObject) {
                        isValid = $($('object').get(0).contentDocument).find('#' + settings.validateElementId).length === 0;
                    }
                }

                if (isValid) {
                    setTimeout(settings.fnPostCall, 1000);
                } else {
                    $._unblockUI();
                }
            }
        },















        // COMPONENTS
        // ==========

        _pageComponents: [],

        _registerComponent: function (cmp) {
            $._pageComponents.push(cmp);
        },

        _pageComponentsInitDisplay: function () {
            $._log('CORE.pageComponentsInitDisplay');

            //executing initDisplay of each page components if exist
            if ($._pageComponents.length) {
                var c;
                for (c = 0; c < $._pageComponents.length; c++) {
                    if (typeof $._pageComponents[c].initDisplay === 'function') {
                        $._pageComponents[c].initDisplay();
                    }
                }
            }
        },

        _pageComponentsBindEvents: function () {
            $._log('CORE.pageComponentsBindEvents');

            //load the sub-components of the page if there are any
            if ($._pageComponents.length) {
                var c;
                for (c = 0; c < $._pageComponents.length; c++) {
                    if (typeof $._pageComponents[c].bindEvents === 'function') {
                        $._pageComponents[c].bindEvents();
                    }
                }
            }
        },



        /* ======================================================= */
        /* ===            SHORTHANDS TO PLUGINS                 == */
        /* ======================================================= */

        /**
         * @see CORE.JS
         * @class .
         * @memberOf CORE.....$
         * @name _notifyError
         * @description todo
         * @example todo
         */
        _notifyError: function(text) {

            jQuery.noticeAdd({type:'error',text:text});

        },

        /**
         * @see CORE.JS
         * @class .
         * @memberOf CORE.....$
         * @name _notifySuccess
         * @description todo
         * @example todo
         */
        _notifySuccess: function(text) {

            jQuery.noticeAdd({type:'success',text:text});

        },

        /**
         * @see CORE.JS
         * @class .
         * @memberOf CORE.....$
         * @name _removeNotifications
         * @description todo
         * @example todo
         */
        _removeNotifications: function() {
            jQuery.noticeRemoveAll($('.notice-item-wrapper'));
        },




        /* ======================================================= */
        /* ===            MINIMAL SHORTHANDS                    == */
        /* ======================================================= */

        _L: function (text, isForced) {
            $._log(text, isForced);
        },
        _I: function (opt) {
            $._init(opt);
        },
        _ID: function() {
            $._initDisplay();
        },


        _GD: function (key) {
            $._getData(key);
        },
        _SD: function (key, value) {
            $._setData(key, value);
        },

        _SF: function (opt) {
            $._submitForm2(opt);
        },

        _AE: function (eventType, selector, fn) {
            $._addEvent(eventType, selector, fn);
        },
        _AEB: function (eventType, selector, fn) {
            $._addEventButtonClick(selector, fn);
        },
        _AEE: function (selector, fn) {
            $._addEventOnEnter(selector, fn);
        },
        _ADE: function (eventType, selector, fn) {
            $._addDocumentEvent(eventType, selector, fn);
        },
        _ADEE: function (selector, fn) {
            $._addDocumentEventOnEnter(selector, fn);
        },
        _SPV: function (name, value, isDate, formId, isFormInIframe) {
            $._setParamValue(name, value, isDate, formId, isFormInIframe);
        },
        _EF: function (aFunction, isSetTimeout) {
            $._execFn(aFunction, isSetTimeout);
        }

    });
}(jQuery));




