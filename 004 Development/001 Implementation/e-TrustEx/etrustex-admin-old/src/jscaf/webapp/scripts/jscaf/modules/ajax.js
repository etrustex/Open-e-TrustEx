/*global syze:true,jQuery:true,$:true,_:true,megaHoverOver:true,megaHoverOut:true,require:true,setTimeout,window,localStorage,document,navigator,screen,console,moment,p,self*/



/* ======================================================= */
/* ===           JSCAF AJAX MODULE                      == */
/* ======================================================= */


(function ($) {

    "use strict";

    $.extend({

        //DECLARATION
        //-----------
        __AJAX: {

            initAjaxEvents: function() {

                $._log('AJAX.initAjaxEvents');

                //AJAX EVENTS
                //-----------
                $._$document.ajaxStart(function () {
                    $._log('AJAX.ajaxStart');
                    $.__AJAX.ajaxCallRunning = true;
                    if ($._isBlockerPage) {
                        $._blockUI();
                    }
                    jQuery.noticeRemoveAll($('.notice-item-wrapper'));
                });

                $._$document.ajaxStop(function () {
                    $._log('AJAX.ajaxStop');

                    $.__AJAX.ajaxCallRunning = false;

                    if ($._settings.isInitDisplayOnAjaxStop) {
                        $._initDisplay();
                    }
                    if ($._isBlockerPage) {
                        $._unblockUI();
                    }
                    $._settings.isInitDisplayOnAjaxStop = true;
                });

                $._$document.ajaxError(function (e, x) {
                    $._log('AJAX.ajaxError');
                    $.__AJAX.ajaxCallRunning = false;

                    if ($.__AJAX.handleRedirect(x)) {
                        return;
                    }

                    var message = '';
                    var statusErrorMap = {
                        '400': "Server understood the request but request content was invalid.",
                        '401': "Unauthorised access.",
                        '403': "Forbidden resource can't be accessed",
                        '404': "Requested action url not found",
                        '500': "Internal Server Error",
                        '503': "Service Unavailable"
                    };

                    if (x.status !== undefined && x.status !== null && x.status !== 200) {
                        if ((x.status === 0 || x.status === 303 || x.status === 12017) && x.responseText === '' && x.statusText === 'error') {
                            message = 'EXCEPTION:EcasRedirectFailed';
                        } else {
                            message = x.status + ' [ ' + statusErrorMap[x.status] + ' ]';
                        }
                    } else if (x.status === 200 && x.statusText === 'parsererror') {
                        message = "Error.Parsing JSON Request failed.";
                    } else if (e === 'timeout') {
                        message = "Request Time out";
                    } else if (e === 'abort') {
                        message = "Request was aborted by the server";
                    } else if (x.status === 200 && x.statusText === 'OK') { //for preventing ajax error on JSON returning empty array
                        message = null;
                    } else {
                        message = "Unknown Error";
                    }

                    $.__AJAX.ajaxErrorMessage = message;
                    $.__AJAX.ajaxErrorResponse = x;

                    $._forceLog('AJAX.ajaxError : ' + $.__AJAX.ajaxErrorMessage);
                });

            },

            loadInnerFragment: function () {
                $._log('AJAX.loadInnerFragment');

                $.__AJAX.ajax({
                    id: $._settings.innerFragmentName,
                    call: $._settings.innerFragmentLoadActionName,
                    isBlockerActive: false,
                    fnPostCall: function() {
                        if ($._settings.koActive) {
                            $.__initKnockoutJS();
                        }
                        $.__initPostCommon();
                    }
                });
            },

            handleRedirect: function (x) {
                if (x && x.getResponseHeader('X-JSCAF-REDIRECT-TO')) {

                    window.location.href = x.getResponseHeader('X-JSCAF-REDIRECT-TO');
                    return true; // prevent further handling of ajax result
                }

                return false;
            },


            // INTERFACE TO jQuery AJAX LOW-LEVEL FUNCTION
            // -------------------------------------------

            isPrototypeMode: false,

            ajaxXhr: null,
            ajaxErrorMessage: null,
            ajaxErrorResponse: null,
            ajaxCallRunning: false,
            ajaxJsonArray: null,
            ajaxData: null,

            ajaxSettings: {},

            ajax: function (opt) {

                //get defaults settings and subclass by options if provided

                var settings = $.extend({

                    callType: 'POST',
                    isDataTypeHtml: true,
                    isDataTypeJson: false,
                    isDataTypeXml: false,
                    pageForm: $._getPageForm(),
                    pageUrl: $._getPageUrl(opt.callOverride),
                    id: null,
                    fragmentIdToRefresh: null,
                    isFragmentRefreshed: true,
                    call: null,
                    callOverride: null,
                    dispatchValue: null,
                    action: null,
                    paramValue: null,
                    isBlockerActive: true,
                    fnPreSuccess: null,
                    fnPreCall: null,
                    fnBeforeSend: $._settings.fnDefaultAjaxBeforeSend,
                    fnPostCall: null,
                    isDisplayAjaxCallError: true,
                    isAsyncCall: true,
                    isTemplateUsed: false,
                    parentTemplateIdToRefresh: null,
                    templateId: null,
                    isJsonArrayRefreshed: false,
                    isJsonArrayPosted: false,
                    jsonArrayPosted: null,
                    cache: true,
                    successNotificationMessage: null,
                    fnServerValidationSuccess: null,
                    fnServerValidationError: null,
                    isDialogRefreshed: false

                }, opt);


                if ($.__AJAX.ajaxCallRunning && !$._settings.isPrototypeMode) {
                    return;
                }

                $._log('AJAX.__ajax');

                if (settings.pageUrl === null && !$._settings.isPrototypeMode) {
                    $._log('====>WARNING : pageUrl cannot be NULL !');
                    return;
                }

                //storing the settings used for later usage
                $.__AJAX.ajaxSettings = settings;


                //use new id declaration, fragmentIdToRefresh replacement
                var fragmentIdToRefresh = null;
                if (settings.id !== null) {
                    fragmentIdToRefresh = settings.id;
                } else {
                    fragmentIdToRefresh = settings.fragmentIdToRefresh;
                }

                var dispatchValue = null;
                if (settings.call !== null) {
                    dispatchValue = settings.call;
                } else {
                    if (settings.callOverride !== null) {
                        dispatchValue = settings.callOverride;
                    } else {
                        dispatchValue = settings.dispatchValue;
                    }
                }


                //check first if it's a prototype ajax call (locally loaded file)
                //setting up special ajax call for prototype action
                if (settings.pageUrl === 'PROTOTYPE' || $._settings.isPrototypeMode) {

                    //setting the blocker as inactive as local calls are faster,
                    //settings.isBlockerActive = false;

                    //setting the cache to false for local page prototyping ajax call
                    settings.cache = false;

                    if (dispatchValue === $._settings.innerFragmentLoadActionName) {
                        //inner load fragment html file name can be provided in the page app settings
                        if ($._settings.prototypeInnerFragmentPageUrl !== null) {
                            settings.pageUrl = $._settings.prototypeInnerFragmentPageUrl;
                        }
                    } else {
                        settings.pageUrl = dispatchValue;
                    }

                    //need to change the url to prevent browser cache of locally called files which normally needs a refresh cache
                    settings.pageUrl += '?v=' + moment().unix();

                    //setting the prototype mode flag for further usages
                    $.__AJAX.isPrototypeMode = true;


                //normal ajax execution, finding the url
                } else {
                    if (dispatchValue !== null) {
                        var $dispatchValue = $($._getById('dispatchValue'));
                        if ($dispatchValue.length) {
                            //option #1 dispatch value is set on every form in a special hidden field
                            $dispatchValue.val(dispatchValue);
                        } else {
                            if ($._settings.isFormParamDispatchValueUsed) {
                                $._setParamValue('dispatchValue', dispatchValue);
                            } else {
                                if (settings.pageUrl !== null) {
                                    if (settings.paramValue !== null) {
                                        //option #2 action if set on the form (example : form action="/person"), the dispatchValue will be the method of the mapping for example : "/add", to match the complete request mapping : "/person/add"
                                        if (settings.pageUrl.indexOf(dispatchValue) < 0) {
                                            settings.pageUrl = settings.pageUrl.replace('.do','') + '/' + dispatchValue + '/' + settings.paramValue + '.do';
                                        } else {
                                            settings.pageUrl = '/' + dispatchValue + '/' + settings.paramValue + '.do';
                                        }

                                    } else {
                                        //option #3 action if set on the form (example : form action="/person"), the dispatchValue will be the method of the mapping for example : "/add", to match the complete request mapping : "/person/add"
                                        if (settings.pageUrl.indexOf(dispatchValue) < 0) {
                                            settings.pageUrl = settings.pageUrl.replace('.do','') + '/' + dispatchValue + '.do';
                                        } else {
                                            settings.pageUrl = '/' + dispatchValue + '.do';
                                        }
                                    }
                                } else {
                                    //option #4 in case no form action="/anAction" is specified, the full request mapping is set in the dispatchValue
                                    settings.pageUrl = $._getContextPath() + dispatchValue;
                                }
                            }
                        }
                    }
                }

                if (settings.action !== null) {
                    var $action = $($._getById('action'));
                    if ($action.length) {
                        $action.val(settings.action);
                    } else {
                        if ($._settings.isFormParamActionUsed) {
                            $._setParamValue('action', settings.action);
                        }
                    }
                    $action = null;
                }

                //setup the ajax default blocker : force it to be displayed when it's null (by default)
                if (settings.isBlockerActive === null) {
                    $._isBlockerPage = true;
                } else {
                    $._isBlockerPage = settings.isBlockerActive;
                }

                //checking data type
                //elseif is not used here, because the default is always isDataTypeHtml = true if not explicitly set to false
                var dataType;
                if (settings.isDataTypeHtml) {
                    dataType = 'html';
                }
                if (settings.isDataTypeJson) {
                    dataType = 'json';
                    settings.cache = false;
                }
                if (settings.isDataTypeXml) {
                    dataType = 'xml';
                    settings.cache = false;
                }

                //checking if a template is used
                if (settings.isTemplateUsed) {
                    if (!settings.isDataTypeJson || settings.templateId === null) {
                        $._log('ERROR : when using template, isDataTypeJson must be TRUE AND templateId must not be NULL');
                        return;
                    }
                }


                //reset error message and error response
                $.__AJAX.ajaxErrorMessage = null;
                $.__AJAX.ajaxErrorResponse = null;

                //reset refreshed json array
                $.__AJAX.ajaxJsonArray = null;

                //reset ajax data
                $.__AJAX.ajaxData = null;

                if (settings.fnPreCall !== null) {
                    settings.fnPreCall();
                }

                if (settings.isDialogRefreshed) {
                    $._log('AJAX.Dialog injection');
                    $._injectDialog();
                }

                //put a special param for AJAX calls to be known server-side
                if ($._settings.isFormDispatchCallTypeUsed) {
                    $._setParamValue('dispatchCallType', 'AJAX');
                }

                //checking page parameters and do injection
                $._injectPageParams();

                //checking jsonArray transferred
                var transferredData = null;
                var contentType = null;
                if (settings.isJsonArrayPosted) {
                    if ($._settings.isFormParamDispatchValueUsed) {
                        transferredData = 'dispatchValue=' + dispatchValue +
                            '&paramArray=' + JSON.stringify(settings.jsonArrayPosted);
                    } else {
                        transferredData = JSON.stringify(settings.jsonArrayPosted);
                    }                  
                    contentType = 'application/json';
                } else {
                    if (settings.pageForm !== null) {
                        transferredData = $($._getById(settings.pageForm)).serialize();
                    }
                    contentType = 'application/x-www-form-urlencoded; charset=UTF-8'; //jQuery default as of the doc
                }

                //log important settings
                $._log('AJAX.ajax.settings.callType=' + settings.callType);
                $._log('AJAX.ajax.settings.pageUrl=' + settings.pageUrl);
                $._log('AJAX.ajax.settings.pageForm=' + settings.pageForm);
                $._log('AJAX.ajax.dataType=' + dataType);
                $._log('AJAX.ajax.fragmentIdToRefresh=' + fragmentIdToRefresh);
                $._log('AJAX.ajax.transferredData=' + transferredData);

                //making the low-level ajax call
                $.__AJAX.ajaxXhr = $.ajax({
                    type: settings.callType,
                    cache: settings.cache,
                    url: settings.pageUrl,
                    data: transferredData,
                    dataType: dataType,
                    contentType: contentType,
                    async: settings.isAsyncCall,
                    beforeSend: settings.fnBeforeSend,
                    success: function (data, textStatus, x) {
                        $._log('AJAX.ajax.SUCCESS');

                        if ($.__AJAX.handleRedirect(x)) {
                            return;
                        }

                        if (settings.fnPreSuccess !== null) {
                            settings.fnPreSuccess();
                        }

                        if (settings.isDialogRefreshed) {
                            $._log('AJAX.re-appending refreshed dialog content:' + fragmentIdToRefresh + ' to dialog container:' + $.__currentDialogId);
                            $('#'+fragmentIdToRefresh).appendTo('#'+$.__currentDialogId);
                        }

                        //HTML/text injection
                        if (settings.isDataTypeHtml) {
                            if ($._isIE9() && $._settings.isCleanIE9AjaxHtmlTags) {
                                data = data.replace(/>\s+(?=<\/?(t|c)[hardfob])/gm, '>');
                            }


                            if ($._settings.isPrototypeMode) {

                                var cOutCnt, cOutPosition,i, cOutString, trailingQuotesOffset, valueObject, valueDefArray, pageFormObject;

                                cOutCnt = data.split('<c:out').length-1;

                                for (i=0; i<cOutCnt; i++) {

                                    cOutPosition = data.indexOf('<c:out');

                                    cOutString = data.substr(cOutPosition, data.substr(cOutPosition).indexOf('/>')+2);
                                    trailingQuotesOffset = 2;
                                    if (data.indexOf('"<c:out') !== -1) {
                                        trailingQuotesOffset = 2;
                                    }
                                    valueObject = cOutString.substr(cOutString.indexOf('${')+2, cOutString.indexOf('}') - cOutString.indexOf('${')-trailingQuotesOffset);
                                    valueDefArray = valueObject.split('.');
                                    pageFormObject = JSON.parse(localStorage.getItem(valueDefArray[0]));

                                    data = data.replace(cOutString, pageFormObject[valueDefArray[1]]);
                                }

                                cOutCnt = cOutPosition = i = cOutString = trailingQuotesOffset = valueObject = valueDefArray = pageFormObject = null;

                            }

                            if (settings.isFragmentRefreshed) {
                                $._addClass($._html,'hidden');
                                $($._getById(fragmentIdToRefresh)).html(data);
                                $._removeClass($._html,'hidden');
                            }

                            //JSON injection native or using a template
                        } else {
                            if (settings.isTemplateUsed) {

                                var refreshedObject,
                                    $template = $($._getById(settings.templateId));

                                if (settings.parentTemplateIdToRefresh !== null) {
                                    //if explicitly provided
                                    refreshedObject = $($._getById(settings.parentTemplateIdToRefresh));
                                } else {
                                    //by default, the container of the template is always the previous element in the DOM
                                    refreshedObject = $template.prev();
                                }

                                refreshedObject.html($template.render(data));

                                refreshedObject = $template = null;
                            }

                            //refreshing also the jsonArray object if needed
                            if (settings.isJsonArrayRefreshed) {
                                $.__AJAX.ajaxJsonArray = data;
                                $._ajaxJsonArray = data;
                            } else {
                                $.__AJAX.ajaxData = data;
                            }
                        }
                    },


                    complete: function () {
                        $._log('AJAX.ajax.COMPLETE');


                        //detecting if error has been detected during ajax call catched with .ajaxError global event
                        if ($.__AJAX.ajaxErrorMessage !== null) {

                            var errorMessageNotification = null,
                                isNotifyError = false;

                            if (settings.fnPreSuccess !== null) {
                                settings.fnPreSuccess();
                            }

                            //just a safety to be sure the overlay block is totally destroyed.
                            $._unblockUI();
                            $('.blockUI').remove();

                            //Reset the call stack ajax calls flag when an error occurs to prevent other ajax calls
                            //in the queue to be executed after
                            if ($.__isCallStackSyncRunning) {
                                $.__isCallStackSyncRunning = false;
                            }

                            //prototype mode is detected before the ajax call for local HTML calls, in that case, only simple notification are displayed
                            if ($.__AJAX.isPrototypeMode) {

                                isNotifyError = true;

                                if ( $.__AJAX.ajaxErrorResponse.status === 404) {
                                    errorMessageNotification = '<u>' + $.__AJAX.ajaxSettings.pageUrl + '</u> NOT FOUND';
                                    if ($.__modules.BROWSER && $._isChrome()) {
                                        errorMessageNotification += '<br>launch CHROME with command line parameter <span class="text-color-red big">--allow-file-access-from-files</span>';
                                    } else {
                                        errorMessageNotification += '<br>IF using CHROME, launch it with command line parameter <span class="text-color-red big">--allow-file-access-from-files</span>';
                                    }
                                }

                            } else {

                                if (settings.isDisplayAjaxCallError && $._settings.isAjaxErrorDisplay) {
                                    if ($._settings.isAjaxErrorNotificationDisplayOnly) {
                                        isNotifyError = true;
                                    } else {
                                        if ($._settings.fnDisplayErrorDialogOverride === null) {
                                            if ($.__modules.UI_DIALOG) {
                                                $.__UI_DIALOG.displayErrorDialog($.__AJAX.ajaxErrorResponse.responseText, $.__AJAX.ajaxErrorMessage, false);
                                            } else {
                                                isNotifyError = true;
                                            }
                                        } else {
                                            //in case the error is handled by the application itself,
                                            //then jSCAF postCommon must be called before
                                            $.__initPostCommon(true);

                                            //calling the override error display function
                                            $._settings.fnDisplayErrorDialogOverride($.__AJAX.ajaxErrorResponse.responseText);
                                        }
                                    }
                                }
                            }

                            //if no error dialog is display and no override occurs, then the normal notification is executed
                            if (isNotifyError) {
                                if (errorMessageNotification === null) {
                                    errorMessageNotification = 'ERROR <br>' + $.__AJAX.ajaxErrorMessage;
                                }
                                $._notifyError(errorMessageNotification);

                                //the init post common must be called if case a notification is displayed,
                                //the default if an error dialog is used using the displayErrorDialog function handle this call
                                $.__initPostCommon(true);
                            }


                        //callback if the fnPostCall has been provided
                        } else {

                            $.__AJAX.ajaxCallRunning = false;

                            if (settings.fnPostCall !== null) {
                                $._execFn(settings.fnPostCall,true);
                            }

                            //display the success notification if a message has been provided
                            if (settings.successNotificationMessage !== null) {
                                $._notifySuccess(settings.successNotificationMessage);
                            }


                            //Specific functions calls to handle server-side SpringMVC bean validation on the refreshed fragment
                            if (settings.fnServerValidationSuccess !== null && settings.fnServerValidationError !== null) {

                                if ($('#' + fragmentIdToRefresh).find('.error').length) {
                                    settings.fnServerValidationError();
                                } else {
                                    settings.fnServerValidationSuccess();
                                }

                            }
                        }

                        //remove paramsContainer and empty parameters array
                        $($._getById('paramsContainer')).remove();

                        //reset timeoutDialog counter
                        if (!$._isEmpty($._settings.timeoutDialogProperties)) {
                            $._settings.timeoutDialogProperties.currentInterval = 0;
                        }

                        $.__pageParams = null;

                    }


                });

                // return the json array if the call is a JSON call (ajaxJsonGet, ajaxJsonPost or ajaxJsonRefreshTemplate)
                if (settings.isJsonArrayRefreshed) {
                    return $.__AJAX.ajaxJsonArray;
                }
            },

        },




        //SHORTHANDS
        //----------
        _AR: function (opt, fnPostCall) {
            $._ajaxRefresh(opt, fnPostCall);
        },

        _AC: function (opt, fnPostCall) {
            $._ajaxCall(opt, fnPostCall);
        },

        _ARO: function (opt, fnPostCall) {
            $._ajaxRefreshOnly(opt, fnPostCall);
        },

        _AJG: function(opt) {
            return $._ajaxJsonGet(opt);
        },

        _AJP: function(opt) {
            return $._ajaxJsonPost(opt);
        },

        _AJRT: function(opt) {
            return $._ajaxJsonRefreshTemplate(opt);
        },




        /**
         * @see AJAX.JS
         * @class .
         * @memberOf AJAX.....$
         * @name _ajaxJsonArray
         * @description todo
         * @example todo
         */
        _ajaxJsonArray: null,




        // SYNCHRONOUS CALL STACK IMPLEMENTATION
        // -------------------------------------

        __callStackSyncIdx: 0,
        __callStackSyncFunctionsStack: [],
        __isCallStackSyncRunning: false,


        _callStackSyncNext: function () {
            $._log('AJAX.callStackSyncNext ==> stack function # ' + $.__callStackSyncIdx);

            if ($.__callStackSyncIdx === $.__callStackSyncFunctionsStack.length - 1) {
                $._log('AJAX.callStackSyncNext ==> END of stack reached');
                $.__isCallStackSyncRunning = false;
            }
            var action = $.__callStackSyncFunctionsStack[ $.__callStackSyncIdx++ ];
            $._execFn(action);
        },


        /**
         * @see AJAX.JS
         * @class .
         * @memberOf AJAX.....$
         * @name _callStackSync
         * @description todo
         * @example todo
         */
        _callStackSync: function (functionsStack) {
            $._log('AJAX.callStackSync');

            if (!$.__isCallStackSyncRunning) {
                //important to avoid multiple call to initDisplay at each ajax function call
                $._settings.isInitDisplayOnAjaxStop = false;

                //to prevent multiple queues of running at the same time
                $.__isCallStackSyncRunning = true;
                $.__callStackSyncIdx = 0;
                $.__callStackSyncFunctionsStack = functionsStack;

                $._callStackSyncNext();
            }
        },



        /**
         * @see AJAX.JS
         * @class .
         * @memberOf AJAX.....$
         * @name _ajaxRefreshOnly
         * @description todo
         * @example todo
         */
        _ajaxRefreshOnly: function (opt, fnPostCall) {
            $._log('AJAX.ajaxRefreshOnly : ' + opt.id);

            var settings = $._extend({
                id: null,
                call: 'refreshFragment',
                isBlockerActive: false,
                fnPostCall: fnPostCall
            },opt);

            $.__AJAX.ajax({
                id: settings.id,
                call: settings.call,
                action: settings.id,
                isBlockerActive: settings.isBlockerActive,
                fnPreCall: settings.fnPreCall,
                fnPostCall: settings.fnPostCall
            });
        },


        /**
         * @see AJAX.JS
         * @class .
         * @memberOf AJAX.....$
         * @name _ajaxRefresh
         * @description todo
         * @example todo
         */
        _ajaxRefresh: function (opt, fnPostCall) {

            if (fnPostCall === undefined) {
                fnPostCall = null;
            }

            var settings = $.extend({
                callType: 'POST',
                isDataTypeHtml: true,
                isDataTypeJson: false,
                id: null,
                fragmentIdToRefresh: null,
                isFragmentRefreshed: true,
                pageUrl: null,
                pageForm: null,
                dispatchValue: null,
                paramValue: null,
                call: null,
                callOverride: null,
                action: null,
                isBlockerActive: true,
                fnPreSuccess: null,
                fnBeforeSend: $._settings.fnDefaultAjaxBeforeSend,
                fnPreCall: null,
                fnPostCall: fnPostCall,
                isTemplateUsed: false,
                parentTemplateIdToRefresh: null,
                templateId: null,
                isJsonArrayRefreshed: false,
                isJsonSerialized: false,
                isAsyncCall: true,
                fnServerValidationSuccess: null,
                fnServerValidationError: null,
                isDialogRefreshed: false
            }, opt);

            $._log('AJAX.ajaxRefresh : fragment=[' + settings.fragmentIdToRefresh + '|' + settings.id + '] - dispatchValue=[' + settings.dispatchValue + '|' + settings.call + '] - action=[' + settings.action + ']');

            if (settings.fragmentIdToRefresh === null && settings.id === null && settings.isFragmentRefreshed && !settings.isTemplateUsed && !settings.isJsonArrayRefreshed) {
                $._log('====>WARNING : At least a fragment id to be refreshed must be provided if template is not used and if not a direct json injection');
                return;
            }

            if (settings.pageUrl === null) { settings.pageUrl = undefined; }
            if (settings.pageForm === null) { settings.pageForm = undefined; }

            $.__AJAX.ajax({
                callType: settings.callType,
                isDataTypeHtml: settings.isDataTypeHtml,
                isDataTypeJson: settings.isDataTypeJson,
                fragmentIdToRefresh: settings.fragmentIdToRefresh,
                id: settings.id,
                isFragmentRefreshed: settings.isFragmentRefreshed,
                pageForm: settings.pageForm,
                pageUrl: settings.pageUrl,
                dispatchValue: settings.dispatchValue,
                paramValue: settings.paramValue,
                call: settings.call,
                callOverride: settings.callOverride,
                action: settings.action,
                isBlockerActive: settings.isBlockerActive,
                fnPostCall: settings.fnPostCall,
                fnBeforeSend: settings.fnBeforeSend,
                fnPreCall: settings.fnPreCall,
                fnPreSuccess: settings.fnPreSuccess,
                isTemplateUsed: settings.isTemplateUsed,
                parentTemplateIdToRefresh: settings.parentTemplateIdToRefresh,
                templateId: settings.templateId,
                isJsonArrayRefreshed: settings.isJsonArrayRefreshed,
                isAsyncCall: settings.isAsyncCall,
                fnServerValidationSuccess: settings.fnServerValidationSuccess,
                fnServerValidationError: settings.fnServerValidationError,
                isDialogRefreshed: settings.isDialogRefreshed
            });
        },


        /**
         * @see AJAX.JS
         * @class .
         * @memberOf AJAX.....$
         * @name _ajaxCall
         * @description todo
         * @example todo
         */
        _ajaxCall: function (opt) {

            var settings = $.extend({
                pageForm: null,
                pageUrl: null,
                call: null,
                callOverride: null,
                dispatchValue: null,
                action: null,
                paramValue: null,
                isBlockerActive: false,
                fnPreSuccess: null,
                fnPreCall: null,
                fnPostCall: null
            }, opt);

            if (settings.pageUrl === null) { settings.pageUrl = undefined; }
            if (settings.pageForm === null) { settings.pageForm = undefined; }

            $.__AJAX.ajax({
                isFragmentRefreshed: false,
                pageForm: settings.pageForm,
                pageUrl: settings.pageUrl,
                call: settings.call,
                callOverride: settings.callOverride,
                dispatchValue: settings.dispatchValue,
                action: settings.action,
                paramValue: settings.paramValue,
                isBlockerActive: settings.isBlockerActive,
                fnPostCall: settings.fnPostCall,
                fnPreCall: settings.fnPreCall,
                fnPreSuccess: settings.fnPreSuccess
            });

        },

        /**
         * @see AJAX.JS
         * @class .
         * @memberOf AJAX.....$
         * @name _ajaxJsonPost
         * @description todo
         * @example todo
         */
        _ajaxJsonPost: function (opt) {

            var settings = $.extend({
                isFragmentRefreshed: true,
                fragmentIdToRefresh: null,
                id: null,
                pageForm: null,
                pageUrl: null,
                dispatchValue: null,
                call: null,
                callOverride: null,
                action: null,
                paramValue: null,
                isBlockerActive: true,
                fnPreSuccess: null,
                fnPreCall: null,
                fnPostCall: null,
                jsonArrayPosted: null,
                isJsonInHtml: false
            }, opt);

            if (settings.jsonArrayPosted === null) {
                $._log('AJAX._ajaxJsonPost : ERROR : jsonArrayPosted cannot be null');
                return;
            }
            if (settings.call === null && settings.callOverride === null && settings.dispatchValue === null && settings.pageUrl === null) {
                $._log('ERROR : either call or dispatchValue or pageUrl must be provided');
                return;
            }

            if (settings.pageUrl === null) { settings.pageUrl = undefined; }
            if (settings.pageForm === null) { settings.pageForm = undefined; }


            var isDataTypeJson, isDataTypeHtml, isJsonArrayRefreshed;

            if (settings.fragmentIdToRefresh !== null || settings.id !== null || settings.isJsonInHtml) {
                isDataTypeHtml = true;
                isDataTypeJson = false;
                isJsonArrayRefreshed = false;
            } else {
                isDataTypeJson = true,
                isDataTypeHtml = false,
                isJsonArrayRefreshed = true;
                $._settings.isInitDisplayOnAjaxStop = false;
            }


            return $.__AJAX.ajax({
                        isFragmentRefreshed: settings.isFragmentRefreshed,
                        fragmentIdToRefresh: settings.fragmentIdToRefresh,
                        id: settings.id,
                        pageForm: settings.pageForm,
                        pageUrl: settings.pageUrl,
                        dispatchValue: settings.dispatchValue,
                        call: settings.call,
                        callOverride: settings.callOverride,
                        action: settings.action,
                        paramValue: settings.paramValue,
                        isBlockerActive: settings.isBlockerActive,
                        fnPostCall: settings.fnPostCall,
                        fnPreCall: settings.fnPreCall,
                        fnPreSuccess: settings.fnPreSuccess,
                        isJsonArrayPosted: true,
                        jsonArrayPosted: settings.jsonArrayPosted,
                        isDataTypeJson: isDataTypeJson,
                        isDataTypeHtml: isDataTypeHtml,
                        isJsonArrayRefreshed: isJsonArrayRefreshed
                    });

        },


        /**
         * @see AJAX.JS
         * @class .
         * @memberOf AJAX.....$
         * @name _ajaxJsonGet
         * @description todo
         * @example todo
         */
        _ajaxJsonGet: function (opt) {

            var settings = $.extend({
                pageForm: null,
                pageUrl: null,
                dispatchValue: null,
                call: null,
                callOverride: null,
                action: null,
                paramValue: null,
                isBlockerActive: false,
                isAsyncCall:false,
                fnPreSuccess: null,
                fnPreCall: null,
                fnPostCall: null
            }, opt);

            if (settings.call === null && settings.callOverride === null && settings.dispatchValue === null && settings.pageUrl === null) {
                $._log('ERROR : either call or dispatchValue or pageUrl must be provided');
                return;
            }

            //$._settings.isInitDisplayOnAjaxStop = false;

            if (settings.pageUrl === null) { settings.pageUrl = undefined; }
            if (settings.pageForm === null) { settings.pageForm = undefined; }

            return $.__AJAX.ajax({
                        callType: 'GET',
                        isFragmentRefreshed: false,
                        fragmentIdToRefresh: null,
                        pageForm: settings.pageForm,
                        pageUrl: settings.pageUrl,
                        dispatchValue: settings.dispatchValue,
                        call: settings.call,
                        callOverride: settings.callOverride,
                        action: settings.action,
                        paramValue: settings.paramValue,
                        isBlockerActive: settings.isBlockerActive,
                        fnPostCall: settings.fnPostCall,
                        fnPreCall: settings.fnPreCall,
                        fnPreSuccess: settings.fnPreSuccess,
                        isAsyncCall: settings.isAsyncCall,
                        isDataTypeJson: true,
                        isDataTypeHtml: false,
                        isJsonArrayRefreshed: true
                    });

        },


        /**
         * @see AJAX.JS
         * @class .
         * @memberOf AJAX.....$
         * @name _checkNotifyJsonMessages
         * @description todo
         * @example todo
         */
        _checkNotifyJsonMessages: function(jsonArrayMessages, fnSuccess) {

            if ( jsonArrayMessages.length !== 0 ) {
                _.each(_.pluck(jsonArrayMessages,'messageText'), function(messageText) {
                    $._notifyError( messageText);
                });
            } else {
                $._execFn(fnSuccess);
            }

        },



        /**
         * @see AJAX.JS
         * @class .
         * @memberOf AJAX.....$
         * @name _ajaxJsonRefreshTemplate
         * @description todo
         * @example todo
         */
        _ajaxJsonRefreshTemplate: function (opt) {

            var settings = $.extend({
                pageForm: null,
                pageUrl: null,
                dispatchValue: null,
                call: null,
                callOverride: null,
                action: null,
                paramValue: null,
                isBlockerActive: false,
                isAsyncCall:false,
                fnPreSuccess: null,
                fnPreCall: null,
                fnPostCall: null,
                templateId: null
            }, opt);

            if (settings.call === null && settings.callOverride === null && settings.dispatchValue === null && settings.pageUrl === null) {
                $._log('ERROR : either call or dispatchValue or pageUrl must be provided');
                return;
            }

            if (settings.templateId === null) {
                $._log('ERROR : a template id must be provided');
            }

            if (settings.pageUrl === null) { settings.pageUrl = undefined; }
            if (settings.pageForm === null) { settings.pageForm = undefined; }

            return $.__AJAX.ajax({
                        callType: 'GET',
                        isFragmentRefreshed: false,
                        fragmentIdToRefresh: null,
                        pageForm: settings.pageForm,
                        pageUrl: settings.pageUrl,
                        dispatchValue: settings.dispatchValue,
                        call: settings.call,
                        callOverride: settings.callOverride,
                        action: settings.action,
                        paramValue: settings.paramValue,
                        isBlockerActive: settings.isBlockerActive,
                        fnPostCall: settings.fnPostCall,
                        fnPreCall: settings.fnPreCall,
                        fnPreSuccess: settings.fnPreSuccess,
                        isAsyncCall: settings.isAsyncCall,
                        isDataTypeJson: true,
                        isDataTypeHtml: false,
                        isJsonArrayRefreshed: true,
                        templateId: settings.templateId,
                        isTemplateUsed: true
                    });

        },



        /* ======================================================= */
        /* ===            INLINE EDITION FUNCTIONS              == */
        /* ======================================================= */

        __inlineIsEdit: false,
        __inlineEditMode: null,
        __$inlineEditCallerRow: null,
        __$inlineEditCallerTable: null,

        /**
         * @see AJAX.JS
         * @class .
         * @memberOf AJAX.....$
         * @name _inlineAddNew
         * @description todo
         * @example todo
         */
        _inlineAddNew: function (opt) {
            var settings = $.extend({
                $caller: null,
                fixedFragmentId: 'add_new_fragment',
                dispatchValue: null,
                fnPreCall: null,
                isAutoScrollToId: true,
                editModeParamName: 'editMode'
            }, opt);

            if ($.__inlineIsEdit) {
                return;
            }
            if (settings.$caller === null || settings.dispatchValue === null) {
                return;
            }


            if (settings.fixedFragmentId === 'add_new_fragment') {
                settings.$caller.closest('.box').find('.content').prepend('<div id="add_new_fragment" class="hidden"></div>');
            }

            $.__inlineIsEdit = true;
            $.__inlineEditMode = 'INSERT';

            $.__AJAX.ajax({
                id: settings.fixedFragmentId,
                call: settings.dispatchValue,
                isBlockerActive: false,
                fnPreCall: function () {
                    $._setParamValue(settings.editModeParamName, $.__inlineEditMode);
                    if (settings.fnPreCall !== null && typeof settings.fnPreCall === 'function') {
                        settings.fnPreCall();
                    }
                },
                fnPostCall: function () {
                    var $addNewFragment = $('#' + settings.fixedFragmentId),
                        $title = $addNewFragment.find('.inner-box').find('h1');
                    $title.text($title.text().replace('$EDIT_TITLE$', "Add new"));
                    $addNewFragment.removeClass('hidden');
                    if (settings.isAutoScrollToId) {
                        $._scrollToId(settings.fixedFragmentId);
                    }
                    $('#action_add_new').hide();
                }
            });

        },

        /**
         * @see AJAX.JS
         * @class .
         * @memberOf AJAX.....$
         * @name _inlineEdit
         * @description todo
         * @example todo
         */
        _inlineEdit: function (opt) {

            var settings = $.extend({
                $caller: null,
                fixedFragmentId: 'edit_fragment',
                dispatchValue: null,
                call: null,
                paramValue: null,
                fnPreCall: null,
                isAutoScrollToId: true,
                editBoxTitle: 'EDIT',
                editModeParamName: 'editMode',
                actionSaveId: 'action_edit_inline_save',
                actionSaveTitle: 'SAVE',
                actionCancelId: 'action_edit_inline_cancel',
                actionCancelTitle: 'cancel'
            }, opt);

            if ($.__inlineIsEdit) {
                return;
            }

            $.__inlineIsEdit = true;
            $.__inlineEditMode = 'EDIT';
            $.__$inlineEditCallerRow = settings.$caller.closest('tr');
            $.__$inlineEditCallerTable = settings.$caller.closest('table');

            if (settings.fixedFragmentId === 'edit_fragment') {
                $.__$inlineEditCallerRow.after('<tr id="edit_container" class="sub-row hidden"><td id="edit_fragment" colspan="' + settings.$caller.closest('table').find('th').length +'"></td></tr>');
            }

            $.__AJAX.ajax({
                id: settings.fixedFragmentId,
                dispatchValue: settings.dispatchValue,
                call: settings.call,
                paramValue: settings.paramValue,
                isBlockerActive: false,
                fnPreCall: function () {
                    $._setParamValue(settings.editModeParamName, $.__inlineEditMode);
                    if (settings.fnPreCall !== null && typeof settings.fnPreCall === 'function') {
                        settings.fnPreCall();
                    }
                },
                fnPostCall: function () {
                    var $fixedFragment = $('#' + settings.fixedFragmentId);
                    var $innerBox = $fixedFragment.find('.inner-box');
                    if (!$innerBox.length) {
                        $innerBox = $fixedFragment.children().wrap('<div class="inner-box blue clear"></div>');
                        $fixedFragment.children().prepend('<h1>' + settings.editBoxTitle + '</h1>');
                    }
                    $fixedFragment.append('<a id="' + settings.actionCancelId + '" class="button-link cr-pointer fr"><span class="button alternate gray_button no-icon"><span><span><em>' + settings.actionCancelTitle + '</em></span></span></span></a>');
                    $fixedFragment.append('<a id="' + settings.actionSaveId + '" class="button-link cr-pointer fr"><span class="button alternate blue_button"><span><span><b class="icon-button icon-bw-save"></b><em>' + settings.actionSaveTitle + '</em></span></span></span></a>');

                    $.__$inlineEditCallerTable.find('td.action a').addClass('hidden');
                    $('#edit_container').removeClass('hidden');
                    if (settings.isAutoScrollToId) {
                        $._scrollToId(settings.fixedFragmentId);
                    }
                }
            });

        },

        /**
         * @see AJAX.JS
         * @class .
         * @memberOf AJAX.....$
         * @name _inlineRemoveEditFragment
         * @description todo
         * @example todo
         */
        _inlineRemoveEditFragment: function () {
            if ($.__inlineEditMode === 'INSERT') {
                $('#add_new_fragment').remove();
            } else {
                $('#edit_container').remove();
            }

            $.__$inlineEditCallerTable.find('td.action a').removeClass('hidden');
            $('#action_add_new').show();

            $.__inlineIsEdit = false;
            $.__inlineEditMode = null;
            $.__$inlineEditCallerRow = null;
            $.__$inlineEditCallerTable = null;

        }




    });
}(jQuery));










