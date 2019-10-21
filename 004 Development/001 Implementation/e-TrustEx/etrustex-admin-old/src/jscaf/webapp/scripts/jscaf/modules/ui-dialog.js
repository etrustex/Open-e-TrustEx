/*global syze:true,jQuery:true,$:true,_:true,megaHoverOver:true,megaHoverOut:true,require:true,setTimeout,window,document,navigator,screen,console,moment,p,self*/



/* ======================================================= */
/* ===           JSCAF UI DIALOG MODULE                 == */
/* ======================================================= */


(function ($) {

    "use strict";

    $.extend({

        //DECLARATION
        //-----------
        __UI_DIALOG: {

            mainWidth: null,
            windowWidth: null,
            windowHeight: null,

            calculateDimensions: function() {
                var $window = $(window);

                $.__UI_DIALOG.mainWidth = $._$main.width();
                $.__UI_DIALOG.windowWidth = $window.width();
                $.__UI_DIALOG.windowHeight = $window.height();

                $window = null;

            },

            initTimeoutDialog: function() {

                $._log('UI_DIALOG.initTimeoutDialog');

                //checking properties
                if ($._settings.timeoutDialogProperties.refreshDelayS === undefined) {
                    $._log('====>WARNING : cannot init timeoutDialog a refreshDelayS property should be provided');
                    return;
                }

                if ($._settings.timeoutDialogProperties.timeoutDelayM === undefined) {
                    $._log('====>WARNING : cannot init timeoutDialog a timeoutDelayM property should be provided');
                    return;
                }

                if ($._settings.timeoutDialogProperties.timeoutForwardUrl === undefined) {
                    $._log('====>WARNING : cannot init timeoutDialog a timeoutForwardUrl property should be provided');
                    return;
                }

                if ($._settings.timeoutDialogProperties.fnKeepAliveCalled === undefined) {
                    $._log('====>WARNING : cannot init timeoutDialog a fnKeepAliveCalled property should be provided');
                    return;
                }

                if ($._settings.timeoutDialogProperties.alertBeforeTimeoutDelayS === undefined) {
                    $._log('====>WARNING : cannot init timeoutDialog a alertBeforeTimeoutDelayS property should be provided');
                    return;
                }


                //enabling interval
                $._settings.timeoutDialogProperties.currentInterval = 0;

                $._settings.timeoutDialogProperties.interval = self.setInterval(function() {

                    var curInt = $._settings.timeoutDialogProperties.currentInterval;
                    var triggeringDelayS = $._settings.timeoutDialogProperties.timeoutDelayM*60 - $._settings.timeoutDialogProperties.alertBeforeTimeoutDelayS;
                    var timeoutExpireDelayS = triggeringDelayS + $._settings.timeoutDialogProperties.alertBeforeTimeoutDelayS - curInt;

                    curInt += $._settings.timeoutDialogProperties.refreshDelayS;

                    /*
                    $._log('====>timeoutDialog details');
                    $._log('----------current interval : ' + curInt);
                    $._log('----------timeout delay : ' + $._settings.timeoutDialogProperties.timeoutDelayM*60);
                    $._log('----------alert before timeout delay : ' + $._settings.timeoutDialogProperties.alertBeforeTimeoutDelayS);
                    $._log('----------triggering delay : ' + triggeringDelayS);
                    $._log('----------timeout expires delay : ' + timeoutExpireDelayS);
                    */

                    //checking if interval is approaching the timeout delay
                    if (curInt >= triggeringDelayS && timeoutExpireDelayS > 0) {

                        //create the first time, if it doesn't exist yet
                        if (!$('#timeoutDialog').length) {
                            //make the browser blink on the task bar if not focussed (only work on IE8+)
                            window.focus();

                            //display the countdown dialog
                            $._msgbox({
                                dialogId: 'timeoutDialog',
                                title: $._getData('jscaf_timeout_dialog_title'),
                                subTitle: $._getData('jscaf_timeout_dialog_subTitle'),
                                text: $._getData('jscaf_timeout_dialog_text').replace('@COUNTDOWN@',timeoutExpireDelayS),
                                msgboxType: 'yes_no',
                                fnCallback: function(r) {
                                    if (r) {
                                        $._execFn($._settings.timeoutDialogProperties.fnKeepAliveCalled);
                                        $._settings.timeoutDialogProperties.currentInterval = 0;
                                    } else {
                                        $._navigateTo({url: $._settings.timeoutDialogProperties.timeoutForwardUrl});
                                    }
                                }
                            });
                        } else {
                            //refreshing the countdown on msgbox dialog
                            $('#timeoutDialog_delay').text(timeoutExpireDelayS);
                        }

                    //if expire timeout reached, then forward to the provided url
                    } else if (timeoutExpireDelayS === 0) {
                        $._closeDialog({dialogId: 'timeoutDialog'});
                        $._navigateTo({url: $._settings.timeoutDialogProperties.timeoutForwardUrl});
                    }

                    $._settings.timeoutDialogProperties.currentInterval = curInt;


                }, $._settings.timeoutDialogProperties.refreshDelayS*1000);

            },


            displayErrorDialog: function (htmlData, message, full) {
                $._log('UI_DIALOG.displayErrorDialog');


                $.__UI_DIALOG.calculateDimensions();

                var closeOnEscape = true;
                var closeFn = function () {
                    $('#globalErrorDialog').dialog("destroy").remove();
                };

                //init screen and reveal the body in case of error
                $.__initPostCommon(true);


                if (htmlData !== undefined) {

                    //in case a back to home page button is provided on the error page, the dialog is not closable,
                    //the back to home button is responsible for the forward.
                    if (htmlData.indexOf('back2homePageButton') >= 0) {
                        closeOnEscape = false;
                        closeFn = function () {};
                    }


                    if (htmlData.indexOf('EXCEPTION:') >= 0 || message.indexOf('EXCEPTION:') >= 0) {

                        if (message === 'EXCEPTION:EcasRedirectFailed') {

                            window.location.href = $._getContextPath() + $._settings.ecasRedirectFailedUrl;

                        } else {

                            var exceptionAction = '';
                            if (htmlData.indexOf('EXCEPTION:OUT_OF_SESSION') > 0) {
                                exceptionAction = 'outOfSessionException';
                            } else if (htmlData.indexOf('EXCEPTION:INVALID_SESSION') > 0) {
                                exceptionAction = 'invalidSessionException';
                            } else if (htmlData.indexOf('EXCEPTION:SECURITY') > 0) {
                                exceptionAction = 'securityException';
                            }

                            $._openDialog({
                                dialogId: 'exception_dialogFragment',
                                dialogTitle: 'APPLICATION ERROR',
                                closeOnEscape: false,
                                isAutoGenerated: true,
                                dialogWidth: 600,
                                isFragmentRefreshedBeforeOpen: true,
                                refreshedFragmentDispatchValue: 'showExceptionDialog',
                                refreshedFragmentAction: exceptionAction,
                                fnDialogOpen: function () {
                                    $('#exception_dialogFragment').prev().addClass('red');
                                }
                            });

                        }

                    } else {

                        $('<div id="globalErrorDialog"></div>').dialog({
                            autoOpen: false,
                            closeOnEscape: closeOnEscape,
                            resizable: false,
                            draggable: false,
                            width: $.__UI_DIALOG.windowWidth - 60,
                            height: $.__UI_DIALOG.windowHeight - 30,
                            modal: true,
                            close: closeFn,
                            open: function () {
                                var $globalErrorDialog = $('#globalErrorDialog');
                                $globalErrorDialog.prev().addClass('red');
                                if (!closeOnEscape) {
                                    $globalErrorDialog.parent().children().find('.ui-dialog-titlebar-close').hide();
                                }
                            }
                        });

                        var arrayToDisplay = [];
                        var idx = 0;
                        var i = 0;

                        if (full === undefined) {
                            full = true;
                        }

                        if (full) {
                            for (i = 0; i < $._logStack.length; i++) {
                                arrayToDisplay[idx] = $._logStack[i];
                                idx++;
                            }
                        } else {
                            for (i = 0; i < $._logStack.length; i++) {
                                if (i >= $._logStack.length - 100) {
                                    arrayToDisplay[idx] = $._logStack[i];
                                    idx++;
                                }
                            }
                        }

                        var $globalErrorDialog = $('#globalErrorDialog');

                        //set the content of the dialog the html received (the [error].jsp page)
                        $globalErrorDialog.html(htmlData);

                        var $errorContent = $('#error-content');

                        //extracting the content of the error (taking off the head and main body structure).
                        if ($errorContent.length) {
                            $globalErrorDialog.html($errorContent);

                            $('#globalErrorMessage_JS_stacktrace').html(arrayToDisplay.join('<br>'));

                            //fill javascript data here, cause script inside page are not interpreted inside dialog
                            $('#globalErrorMessage_JS_url').html(window.document.location.toString());
                            $('#globalErrorMessage_JS_navigator_userAgent').html(navigator.userAgent);
                            $('#globalErrorMessage_JS_navigator_platform').html(navigator.platform);

                        } else {

                            //by default or in case of a pure javascript error, javascript stack trace is displayed

                            var htmlOutput = '<div class="title"><span class="icon icon-warning fl"></span><span class="fl" style="width:90%;">ERROR</span></span><span class="icon icon-warning fr"></span></div>';

                            htmlOutput += '<table class="default fixed no-hover">';
                            htmlOutput += '    <caption><h5>Technical information</h5></caption>';
                            htmlOutput += '<thead>';
                            htmlOutput += '    <tr>';
                            htmlOutput += '        <th width="150px">INFO TYPE</th>';
                            htmlOutput += '        <th>VALUE</th>';
                            htmlOutput += '    </tr>';
                            htmlOutput += '</thead>';
                            htmlOutput += '    <tbody>';
                            htmlOutput += '    <tr>';
                            htmlOutput += '    <td class="center">JAVASCRIPT ERROR STACKTRACE</td>';
                            htmlOutput += '    <td class="text-color-blue">';

                            htmlOutput += arrayToDisplay.join('<br>');

                            htmlOutput += '    </td>';
                            htmlOutput += '    </tr>';
                            htmlOutput += '    </tbody>';
                            htmlOutput += '</table>';

                            $globalErrorDialog.html(htmlOutput);
                        }


                        //open the dialog
                        $globalErrorDialog.dialog("open");

                    }

                }


            }


        },


        //SHORTHANDS
        //----------
        _OD: function (opt) {
            $._openDialog(opt);
        },
        _OAD: function (opt) {
            $._openAjaxDialog(opt);
        },
        _OIFD: function (opt) {
            $._openIframeDialog(opt);
        },
        _CD: function (opt) {
            $._closeDialog(opt);
        },
        _MB: function (opt) {
            $._msgbox(opt);
        },



        /* ======================================================= */
        /* ===              UI-DIALOG FUNCTIONS                 == */
        /* ======================================================= */

        __currentDialogId: null,
        _isDialogAutoGenerated: false,
        __isFirstDialogShowCloseButton: false,


        /**
         * @see UI-DIALOG.JS
         * @class .
         * @memberOf UI-DIALOG.....$
         * @name _openAjaxDialog
         * @param opt the object literal parameter <br><em>MANDATORY</em>
         * @param opt.dialogId {string} the id of the dialog that will be created <br><em>MANDATORY</em> <br><i>default : null</i>
         * @param opt.refreshedFragmentPageUrl {string} the server url used for the ajax call <br><em>MANDATORY</em> <br><i>default : null</i>
         * @param opt.refreshedFragmentPageForm {string} the server dispatchValue called <br><em>MANDATORY</em> <br><i>default : null</i>
         * @param opt.refreshedFragmentDispatchValue {string} the server dispatchValue called <br><em>MANDATORY</em> <br><i>default : null</i>
         * @param opt.refreshedFragmentAction {string} the server action parameter transferred <br><i>default : null</i>
         * @param opt.dialogWidth {number} the dialog width <br><i>default : 800</i>
         * @param opt.dialogHeight {string} the dialog height <br><i>default : 'auto'</i>
         * @param opt.dialogTitle {string} the dialog header title <br><i>default : 'DIALOG'</i>
         * @param opt.isAjaxBlockerActive {boolean} If a blocker please wait message must be shown when the dialog is displayed <br><i>default : true</i>
         * @param opt.isShowButtons {boolean} if the buttons declared using buttonPrimary/buttonSecondary (see next params) must be created on the dialog <br><i>default : true</i>
         * @param opt.isShowCloseButton {boolean} if the close button up right must be visible  <br><i>default : false</i>
         * @param opt.isAsyncCall {boolean} if the ajax call should asynchronous  <br><i>default : true</i>
         * @param opt.isOneButtonOnly {boolean} if true, only the primary button and action will be displayed/active.  <br><i>default : false</i>
         * @param opt.buttonPrimaryTitle {string} The title of the primary button  <br><i>default : 'OK'</i>
         * @param opt.buttonPrimaryFn {function} The function called when the primary button is clicked  <br><em>MANDATORY</em> <br><i>default : null</i>
         * @param opt.buttonSecondaryTitle {string} The title of the secondary button  <br><i>default : 'cancel'</i>
         * @param opt.buttonSecondaryFn {function} The function called when the secondary button is clicked  <br><i>default : function() { $._closeDialog(); }</i>
         * @param opt.fnPreCall {function} called during $.__AJAX.ajax() fnPreCall step  <br><i>default : null</i>
         * @param opt.fnAfterCreatePostCall {function} called during $.__AJAX.ajax() fnAfterCreatePostCall step  <br><i>default : null</i>
         * @param opt.fnDialogClose {function} called during $._openDialog() fnDialogClose step  <br><i>default : null</i>
         * @description
         * Facade for $._openDialog() function when an AJAX call must be executed before the dialog is displayed and the content of this dialog is auto-generated when a server-side fragment is refreshed.
         * @example
         * //basic call
         * $._openAjaxDialog({
         *      dialogId: 'myDialogId',
         *      refreshedFragmentDispatchValue: 'aServerAction',
         *      buttonPrimaryFn: function() {
         *          //primary action function
         *      }
         * });
         */
        _openAjaxDialog: function (opt) {
            $._log('UI_DIALOG.openAjaxDialog');

            //get defaults settings and subclass by options if provided
            var settings = $.extend({

                dialogId: null,
                id:null,
                refreshedFragmentPageUrl: null,
                refreshedFragmentPageForm: null,
                refreshedFragmentDispatchValue: null,
                refreshedFragmentDispatchValueOverride: null,
                call:null,
                callOverride:null,
                paramValue: null,
                refreshedFragmentAction: null,
                dialogWidth: 800,
                dialogHeight: 'auto',
                dialogTitle: 'DIALOG',
                dialogDraggable: true,
                isAjaxBlockerActive: true,
                isShowButtons: true,
                isShowCloseButton: false,
                isAsyncCall: true,
                isOneButtonOnly: false,
                buttonPrimaryTitle: 'OK',
                buttonPrimaryFn: null,
                buttonSecondaryTitle: 'cancel',
                buttonSecondaryFn: function () {
                    $._closeDialog({});
                },
                fnPreCall: null,
                fnAfterCreatePostCall: null,
                fnDialogClose: null,
                isKeepCurrentDialogId: true,
                isCloseOnEscape: false,
                isAppSpecificDialogButtons: false,
                isSecondModalDialog: false,
                isDisableFirstDialog: false,
                firstDialogId: null

            }, opt);

            //replacing call and id by future deprecated values
            if (settings.dialogId === null && settings.id !== null) {
                settings.dialogId = settings.id;
            }

            if (settings.refreshedFragmentDispatchValue === null && settings.call !== null) {
                settings.refreshedFragmentDispatchValue = settings.call;
            }

            if (settings.refreshedFragmentDispatchValueOverride === null && settings.callOverride !== null) {
                settings.refreshedFragmentDispatchValueOverride = settings.callOverride;
            }



            //checking mandatory fields
            if (settings.dialogId === null || (settings.refreshedFragmentDispatchValue === null && settings.refreshedFragmentDispatchValueOverride === null && settings.refreshedFragmentPageUrl === null)) {
                $._log('ERROR:dialogId OR (refreshedFragmentDispatchValue AND refreshedFragmentDispatchValueOverride AND refreshedFragmentPageUrl) are MANDATORY');
                return;
            }

            if (settings.refreshedFragmentPageUrl === null) { settings.refreshedFragmentPageUrl = undefined; }
            if (settings.refreshedFragmentPageForm === null) { settings.refreshedFragmentPageForm = undefined; }


            var buttons = [];

            if (settings.isShowButtons) {
                if (settings.isOneButtonOnly || settings.buttonPrimaryFn === null) {

                    if (!settings.isShowCloseButton) {
                        buttons = [
                            {
                                text: settings.buttonSecondaryTitle,
                                click: settings.buttonSecondaryFn
                            }
                        ];
                    }

                } else {
                    buttons = [
                        {
                            text: settings.buttonSecondaryTitle,
                            click: settings.buttonSecondaryFn
                        },
                        {
                            text: settings.buttonPrimaryTitle,
                            click: settings.buttonPrimaryFn
                        }
                    ];
                }
            } else {
                settings.isShowCloseButton = true;
                buttons = null;
            }

            $._openDialog({
                dialogId: settings.dialogId,
                isAutoGenerated: true,
                dialogWidth: settings.dialogWidth,
                dialogHeight: settings.dialogHeight,
                dialogTitle: settings.dialogTitle,
                dialogDraggable: settings.dialogDraggable,
                isFragmentRefreshedBeforeOpen: true,
                refreshedFragmentPageUrl: settings.refreshedFragmentPageUrl,
                refreshedFragmentPageForm: settings.refreshedFragmentPageForm,
                refreshedFragmentDispatchValue: settings.refreshedFragmentDispatchValue,
                refreshedFragmentDispatchValueOverride: settings.refreshedFragmentDispatchValueOverride,
                refreshedFragmentAction: settings.refreshedFragmentAction,
                isAjaxBlockerActive: settings.isAjaxBlockerActive,
                isAsyncCall: settings.isAsyncCall,
                dialogButtons: buttons,
                isShowCloseButton: settings.isShowCloseButton,
                fnPreCall: settings.fnPreCall,
                fnAfterCreatePostCall: settings.fnAfterCreatePostCall,
                fnDialogClose: settings.fnDialogClose,
                isKeepCurrentDialogId: settings.isKeepCurrentDialogId,
                isCloseOnEscape: settings.isCloseOnEscape,
                isAppSpecificDialogButtons: settings.isAppSpecificDialogButtons,
                isSecondModalDialog: settings.isSecondModalDialog,
                isDisableFirstDialog: settings.isDisableFirstDialog,
                firstDialogId: settings.firstDialogId,
                paramValue: settings.paramValue
            });

        },


        /**
         * @see UI-DIALOG.JS
         * @class .
         * @memberOf UI-DIALOG.....$
         * @name _openDialog
         * @description
         * Facade function jQueryUI open dialog on an element.
         * @param opt the object literal parameter <br><em>MANDATORY</em>
         * @param opt.dialogId {string} the id of the dialog that will be created, if the isAutogenerated parameter is FALSE, this element must exist on the page before the dialog can be created <br><em>MANDATORY</em> <br><i>default : null</i>
         * @param opt.dialogContainerId {string} the dialog container that will be used when the dialog content will be re-injected into the form using the $._injectDialog() function, this element must exist before the dialog can be injected <br><i>default : opt.dialogId + '_container'</i>
         * @param opt.isAutoGenerated {boolean} if TRUE, the elements dialogId provided and the containerId provided will be created dynamically on the page.
         * @param opt.dialogWidth {number} the dialog width <br><i>default : 800</i>
         * @param opt.dialogHeight {string} the dialog height <br><i>default : 'auto'</i>
         * @param opt.dialogTitle {string} the dialog header title <br><i>default : 'DIALOG'</i>
         * @param opt.dialogDraggable {boolean} if the dialog should be draggable <br><i>default : true</i>
         * @param opt.dialogModal (boolean) if the dialog should be modal <br><i>default : true</i>
         * @param opt.dialogPosition (string) A string representing the position within the viewport. <br><i>Possible values: "center", "left", "right", "top", "bottom".</i>
         * @param opt.dialogResizable (boolean) if the dialog should be resisable <br><i>default : false</i>
         * @param opt.dialogAutoOpen {boolean} if the dialog should be opened right after this call, if FALSE, the dialog will be pre-created and an additional open call will be necessary to display it <br><i>default : true</i>
         * @param opt.dialogButtons {array} the buttons array to be displayed in the dialog footer for actions <br><i>default : null</i>
         * @param opt.dialogButtonsIcons {array} the buttons icons array corresponding to the buttons array defined above <br><i>default : null</i>
         * @param opt.isCloseOnEscape {boolean} if true, the dialog can be closed using ESC keypress <br><i>default : false</i>
         * @param opt.isDialogButtonsMsgBox {boolean} if true, the dialog will react as a msgbox <br><i>default : false</i>
         * @param opt.isDialogButtonsShowIcons {boolean} if true, icons will be shown on dialog actions buttons <br><i>default : true</i>
         * @param opt.isKeepCurrentDialogId {boolean} if true, the dialogId will be stored in $.__currentDialogId var for later be reused in $._closeDialog(), $._centerDialog(), $._emptyDialogContainer() and $._injectDialog() <br><i>default : true</i>
         * @param opt.isShowCloseButton {boolean} if a close button upper right of the dialog must be displayed allowing to close the dialog without having a close footer action button <br><i>default : false</i>
         * @param opt.isDialogOverflowAllowed {boolean} If false, no scrollbars will appear on dialog overflow in height <br><i>default : true</i>
         * @param opt.isBodyOverflowHiddenOnCreate {boolean} Block the scrollbar on parent document body to force only the scrolling on the dialog <br><i>default : true</i>
         * @param opt.isBodyOverflowScrollOnClose {boolean} Same as above but reset on close <br><i>default : true</i>
         * @param opt.isFragmentRefreshedBeforeOpen {boolean} If an ajax call must be done to refresh the fragment container of the dialog before the dialog is effectively displayed and created  <br><i>default : false</i>
         * @param opt.refreshedFragmentDispatchValue {string} the server dispatchValue called <br><i>default : null</i>
         * @param opt.refreshedFragmentAction {string} the server action parameter transferred <br><i>default : null</i>
         * @param opt.isAjaxBlockerActive {boolean} If a blocker please wait message must be shown when the dialog is displayed <br><i>default : true</i>
         * @param opt.fnDialogOpen {function} provided function to be executed when dialog open event is triggered <br><i>default : null</i>
         * @param opt.fnAfterCreatePostCall {function} provided function to be executed when the dialog has been created <br><i>default : null</i>
         *
         * @example
         * //basic call - auto-generated - no action buttons
         * $._openDialog({
         *      dialogId: 'dialog_basic',
         *      isAutoGenerated: true
         * });
         */
        _openDialog: function (opt) {
            $._log('UI_DIALOG.openDialog');

            $.__UI_DIALOG.calculateDimensions();

            //get defaults settings and subclass by options if provided
            var settings = $.extend({

                dialogId: null,
                dialogContainerId: opt.dialogId + '_container',
                dialogContent: null,
                isAutoGenerated: false,
                dialogWidth: 800,
                dialogHeight: 'auto',
                dialogTitle: 'DIALOG',
                dialogDraggable: true,
                dialogModal: true,
                dialogResizable: false,
                dialogPosition: 'center',
                dialogAutoOpen: true,
                dialogButtons: null,
                dialogButtonsIcons: null,
                isCloseOnEscape: false,
                isDialogButtonsMsgbox: false,
                isDialogButtonsShowIcons: true,
                isKeepCurrentDialogId: true,
                isShowCloseButton: null,
                isDialogOverflowAllowed: true,
                isFragmentRefreshedBeforeOpen: false,
                refreshedFragmentPageUrl: null,
                refreshedFragmentPageForm: null,
                refreshedFragmentDispatchValue: null,
                refreshedFragmentDispatchValueOverride: null,
                refreshedFragmentAction: null,
                isAjaxBlockerActive: true,
                isAsyncCall: true,
                isCenterAfterCreate: false,
                fnDialogOpen: null,
                fnDialogClose: null,
                fnPreCall: null,
                fnAfterCreatePostCall: null,
                isEmptyOnClose: true,
                hasHeaderAction: false,
                headerActionClass: null,
                headerActionIconClass: 'icon-download',
                headerActionText: $._getData('jscaf_common_download_file'),
                headerActionUrl: null,
                notificationText: null,
                isAppSpecificDialogButtons: false,
                isSecondModalDialog: false,
                isDisableFirstDialog: false,
                firstDialogId: null,
                paramValue: null

            }, opt);

            var $dialogContainer = $('#' + settings.dialogContainerId);

            //checking parameters
            if (settings.dialogId === null) {
                $._log('====>ERROR : dialogId must be provided');
                return;
            }

            if (settings.isFragmentRefreshedBeforeOpen && (settings.refreshedFragmentDispatchValue === null && settings.refreshedFragmentDispatchValueOverride === null && settings.refreshedFragmentPageUrl === null)) {
                $._log('====>ERROR : if isFragmentRefreshedBeforeOpen is TRUE, refreshFragmentDispatchValue OR refreshedFragmentPageUrl cannot be NULL');
                return;
            }

            if (settings.refreshedFragmentPageUrl === null) { settings.refreshedFragmentPageUrl = undefined; }
            if (settings.refreshedFragmentPageForm === null) { settings.refreshedFragmentPageForm = undefined; }

            if (settings.isSecondModalDialog && settings.isDisableFirstDialog && settings.firstDialogId === null) {
                $._log('====>ERROR : if isSecondModalDialog is TRUE and isDisableFirstDialog is TRUE then a firstDialogId cannont be NULL');
            }



            //checking and emptying container if exists
            if ($dialogContainer.length) {
                $dialogContainer.empty();
            }

            //checking if the dialogId exists and create it if not
            if (settings.dialogId !== null && settings.dialogContent === null) {
                if (!$('#' + settings.dialogId).length && settings.isAutoGenerated) {
                    $._$body.append('<div id="' + settings.dialogId + '" class="hidden"></div>');
                }
            } else if (settings.dialogId !== null && settings.dialogContent !== null) {
                $._$body.append(settings.dialogContent);
            }

            //checking if the dialog container exists and create if it must be generated dynamically
            if (!$dialogContainer.length && settings.isAutoGenerated) {
                $._$form.append('<div id="' + settings.dialogContainerId + '" class="hidden"></div>');
            }

            //storing autogenerated value for close function
            $._isDialogAutoGenerated = settings.isAutoGenerated;


            //HD resolution transforming dialog auto width
            if (settings.dialogWidth > $.__UI_DIALOG.mainWidth) {
                if ($.__modules.DISPLAY) {
                    if ($.__DISPLAY.hdMarginWidth !== null) {
                        settings.dialogWidth -= 2 * $.__DISPLAY.hdMarginWidth;
                    }
                } else {
                    settings.dialogWidth -= 150;
                }
            }

            //Setup transition and disable for IE
            var show = {effect:'fade', duration: 200};
            if ($._isIE7() || $._isIE8()) {
                show = null;
            }

            //setup dialog creation function
            var fn = function (postCall) {

                //creating and opening the dialog
                $('#' + settings.dialogId).dialog({
                    autoOpen: settings.dialogAutoOpen,
                    closeOnEscape: settings.isCloseOnEscape,
                    resizable: settings.dialogResizable,
                    draggable: settings.dialogDraggable,
                    width: settings.dialogWidth,
                    height: settings.dialogHeight,
                    modal: settings.dialogModal,
                    title: settings.dialogTitle,
                    show: null,
                    create: function () {
                        var $this = $(this);

                        $._logStart('DIALOG.create : ' + this.id);

                        if (!settings.isDialogOverflowAllowed) {
                            this.style.overflow = 'hidden';
                        }
                        //removing the hidden class of the dialogId
                        $._removeClass(this,'hidden');

                        //fixing the dialog so it won't be affected by user scroll on window
                        $._addClass($this.parent()[0],'pos-fixed');

                        $this = null;

                        $._logEnd();
                    },
                    open: function () {
                        var $this = $(this);

                        $._logStart('DIALOG.open');

                        //if buttons array is empty then show close button by default
                        if (settings.dialogButtons === null && settings.isShowCloseButton === null) {
                            settings.isShowCloseButton = true;
                        }

                        if (settings.dialogButtons !== null && settings.isShowCloseButton === null) {
                            settings.isShowCloseButton = false;
                        }

                        //hide close button
                        if (!settings.isShowCloseButton) {
                            $this.parent().children().find('.ui-dialog-titlebar-close').hide();
                        }

                        //put buttons collection in cache
                        var buttons = $($this.parent().find('.ui-dialog-buttonset').children('button'));

                        //focus on the last button (to prevent activate with double-click)
                        if (buttons.length > 1) {
                            $(buttons[buttons.length - 1]).focus();
                        }

                        //updating button priority display classes

                        //if more than 1 button, the last button is always set as a primary button
                        if (!settings.isDialogButtonsMsgbox) {
                            if (buttons.length > 1) {
                                $(buttons[buttons.length - 1]).addClass('ui-primary-button');
                                $(buttons[0]).addClass('ui-secondary-button');

                                //if only one button is present, then this button is set as a primary button
                            } else if (buttons.length === 1) {
                                $(buttons[0]).addClass('ui-primary-button');
                            }
                            //special display for dialog msgbox dialog
                        } else {
                            //if yes, no, cancel buttons, cancel is set as a secondary action, yes and no as primary action
                            if (buttons.length === 3) {
                                $(buttons).addClass('ui-primary-button');
                                $(buttons[2]).addClass('ui-secondary-button');
                            } else {
                                //if one or two buttons (yes/no), all are set as primary action
                                $(buttons).addClass('ui-primary-button');
                            }
                        }


                        //when bootstrap is active, no icon are allowed on the dialog buttons to keep the flat theme straight
                        if ($._settings.isBootstrapActive) {
                            settings.dialogButtonsIcons = null;
                        }


                        //setting buttons icons if provided
                        if (settings.dialogButtonsIcons !== null) {
                            var i;
                            for (i = 0; i < settings.dialogButtonsIcons.length; i++) {
                                if (settings.dialogButtonsIcons[i] !== undefined) {
                                    $(buttons[i])
                                        .removeClass('ui-button-text-only')
                                        .addClass('ui-button-text-icon-primary ui-button-text-icon')
                                        .prepend('<span class="ui-button-icon-primary ui-icon ' + settings.dialogButtonsIcons[i] + '"></span>');
                                }
                            }

                            //if not provided explicetly, then the last button receive by default an ui-icon-check icon
                        } else {
                            if (!$._settings.isBootstrapActive) { // when bootstrap is active, no icon is set on the dialog buttons
                                if (settings.isDialogButtonsShowIcons) {
                                    if (buttons.length > 1) {
                                        $(buttons[buttons.length - 1])
                                            .removeClass('ui-button-text-only')
                                            .addClass('ui-button-text-icon-primary ui-button-text-icon')
                                            .prepend('<span class="ui-button-icon-primary ui-icon ui-icon-check"></span>');
                                    }
                                }
                            }
                        }

                        //checking the height and constrain it if more than visible window height
                        if ($this.height() > $.__UI_DIALOG.windowHeight - 150) {
                            $this.height($.__UI_DIALOG.windowHeight - 150);
                        }

                        //checking if a action button should be displayed on the header
                        if (settings.hasHeaderAction) {
                            $('.ui-dialog-titlebar').append('<span class="ui-dialog-titlebar-header-action ' + settings.headerActionClass + '"><a href="' + settings.headerActionUrl + '" target="_blank"><span class="icon-inline-text ' + settings.headerActionIconClass + '"></span>' + settings.headerActionText + '</a></span>');
                        }

                        //checking if open function provided
                        if (settings.fnDialogOpen !== null) {
                            $._execFn(settings.fnDialogOpen);
                        }

                        //checking if a notification must be displayed
                        if (settings.notificationText !== null) {
                            jQuery.noticeAdd({type: 'warning', text: settings.notificationText});
                        }

                        //force centering the dialog
                        $this.dialog("option", "position", settings.dialogPosition);

                        //force z-index in case of multiple dialogs opening
                        if (settings.isSecondModalDialog) {
                            $('#' + settings.dialogId).parent().css({'z-index':'1000'});

                            //check if the first dialog should be disabled
                            if (settings.isDisableFirstDialog) {
                               var $firstDialog = $('#'+settings.firstDialogId);
                               $._elementBlock($firstDialog.parent(),false);
                               $firstDialog.parent().children().find('.ui-dialog-titlebar-close').hide();
                            }

                        }

                        $._logEnd();

                    },
                    close: function () {
                        $._logStart('DIALOG.close : ' + this.id);

                        //checking if open function provided
                        if (settings.fnDialogClose !== null) {
                            $._execFn(settings.fnDialogClose);
                        } else {
                            if (settings.dialogButtons === null && !settings.isAppSpecificDialogButtons) {
                                $._closeDialog({dialogId: settings.dialogId, isEmptyOnClose: settings.isEmptyOnClose});
                            }
                        }

                        if (settings.notificationText !== null) {
                            jQuery.noticeRemoveAll();
                        }

                        //if it's a second dialog and the first dialog has been disabled reactivate it
                        if (settings.isSecondModalDialog) {

                            //check if the first dialog should be disabled
                            if (settings.isDisableFirstDialog) {
                               var $firstDialog = $('#'+settings.firstDialogId);
                               $._elementUnblock($firstDialog.parent());

                               if ($.__isFirstDialogShowCloseButton) {
                                   $firstDialog.parent().children().find('.ui-dialog-titlebar-close').show();
                               }
                            }

                        }

                        $._logEnd();

                        return false;
                    },
                    buttons: settings.dialogButtons
                });


                if (settings.isCenterAfterCreate) {
                    $._execFn(function () {
                        $('#' + settings.dialogId).dialog("option", "position", 'center');
                    });
                }

                $._execFn(settings.fnAfterCreatePostCall, true);

                $._execFn(postCall, true);

            };


            if (settings.isFragmentRefreshedBeforeOpen) {

                if ($.__modules.AJAX) {
                    $.__AJAX.ajax({
                        pageUrl: settings.refreshedFragmentPageUrl,
                        pageForm: settings.refreshedFragmentPageForm,
                        id: settings.dialogId,
                        call: settings.refreshedFragmentDispatchValue,
                        callOverride: settings.refreshedFragmentDispatchValueOverride,
                        paramValue: settings.paramValue,
                        action: settings.refreshedFragmentAction,
                        isBlockerActive: settings.isAjaxBlockerActive,
                        fnPreCall: settings.fnPreCall,
                        fnPostCall: function () {
                            $._execFn(fn);
                        }
                    });
                } else {
                    $._log('ERROR: AJAX module not loaded, unable to perform ajax call before opening the dialog' );
                }

            } else {
                $._execFn(fn);
            }

            //storing current opened dialog for further reuse (in close / center / empty functions)
            if (settings.isKeepCurrentDialogId && !settings.isSecondModalDialog) {
                $.__currentDialogId = settings.dialogId;
            }

            //storing the first dialog close button state
            if (!settings.isSecondModalDialog) {
                $.__isFirstDialogShowCloseButton = settings.isShowCloseButton;
            }
        },

        /**
         * @see UI-DIALOG.JS
         * @class .
         * @memberOf UI-DIALOG.....$
         * @name _closeDialog
         * @description
         * Close a previously opened dialog
         * @param opt the object literal parameter <br><em>MANDATORY</em>
         * @param opt.dialogId {string} the id of the dialog that will be closed, if not provided the $.__currentDialogId will be taken if exists <br><i>default : null</i>
         * @param opt.dialogContainerId {string} the dialog container that will be emptied (if the dialog has not been auto-generated) OR removed (if the dialog has been previously auto-generated <br><i>default : null</i>
         * @param opt.isBodyOverflowScrollOnClose {boolean} Reset scrolling of parent body element on close <br><i>default : true</i>
         *
         */
        _closeDialog: function (opt, fn) {
            $._log('UI_DIALOG.closeDialog');

            //get defaults settings and subclass by     options if provided
            var settings = $.extend({

                dialogId: null,
                dialogContainerId: null,
                isBodyOverflowScrollOnClose: true,
                isEmptyOnClose: true

            }, opt);

            //checking parameters
            if (settings.dialogId === null && $.__currentDialogId === null) {
                $._log('====>ERROR : dialogId cannot be NULL');
                return;
            }

            //if a current dialog id has already been stored in the openDialog function, use it
            if (settings.dialogId === null && $.__currentDialogId !== null) {
                settings.dialogId = $.__currentDialogId;
                settings.dialogContainerId = $.__currentDialogId + '_container';
            }

            var $dialogContainer = $('#' + settings.dialogContainerId),
                $dialog = $('#' + settings.dialogId);

            //checking and emptying container if exists
            if ($dialogContainer.length && $._isDialogAutoGenerated) {
                $dialogContainer.remove();
            } else {
                if (settings.isEmptyOnClose) {
                    $dialogContainer.empty();
                }
            }

            //closing and destroying dialog DOM created elements
            $dialog.dialog("close").dialog("destroy");

            //checking and emptying the dialog content
            if ($dialog.length && $._isDialogAutoGenerated) {
                $dialog.remove();
            } else {
                if (settings.isEmptyOnClose) {
                    $dialog.empty();
                }
            }

            //destroying all poshytip that remains when validation occurs
            $('.JS_live-validation').poshytip('destroy');

            //just to be sure, back to the default value to avoid further highlighted validation error  if changed inside the previous process
            $._settings.isFieldValidationUpdateDisplay = false;

            //resetting the current dialog opened if it's the one kept (for multiple stacked dialogs)
            if (settings.dialogId === $.__currentDialogId) {
                $.__currentDialogId = null;
            }

            $._execFn(fn);

        },

        /**
         * @see UI-DIALOG.JS
         * @class .
         * @memberOf UI-DIALOG.....$
         * @name _centerDialog
         * @description
         * Center a previously opened dialog
         * @param dialogId {string} the id of the dialog that will be centered, if not provided the $.__currentDialogId will be taken if exists <br><i>default : undefined</i>
         *
         */
        _centerDialog: function (dialogId) {

            $.__UI_DIALOG.calculateDimensions();

            //checking parameters
            if (dialogId === undefined && $.__currentDialogId === null) {
                $._log('====>ERROR : dialogId must be provided');
                return;
            }

            //if a current dialog id has already been stored in the openDialog function, use it
            if (dialogId === undefined && $.__currentDialogId !== null) {
                dialogId = $.__currentDialogId;
            }

            var $dialog = $('#' + dialogId);

            //checking if dialog height is greater than window height-150px, if yes contrain it
            if ($dialog.height() > $.__UI_DIALOG.windowHeight - 150) {
                $dialog.height($.__UI_DIALOG.windowHeight - 150);
            }

            $dialog.dialog("option", "position", 'center');

        },

        /**
         * @see UI-DIALOG.JS
         * @class .
         * @memberOf UI-DIALOG.....$
         * @name _emptyDialogContainer
         * @description
         * Empty a previously injected dialog container
         * @param dialogContainerId {string} the id of the container used for the injection, if not provided the $.__currentDialogId + '_container' will be taken if exists <br><i>default : undefined</i>
         *
         */
        _emptyDialogContainer: function (dialogContainerId) {

            //checking parameters
            if (dialogContainerId === undefined && $.__currentDialogId === null) {
                $._log('====>ERROR : dialogContainerId must be provided');
                return;
            }

            //if a current dialog id has already been stored in the openDialog function, use it
            if (dialogContainerId === undefined && $.__currentDialogId !== null) {
                dialogContainerId = $.__currentDialogId + '_container';
            }

            $('#' + dialogContainerId).empty();
        },

        /**
         * @see UI-DIALOG.JS
         * @class .
         * @memberOf UI-DIALOG.....$
         * @name _injectDialog
         * @description
         * Inject the dialog into its form container (default) or provided container
         * @param dialogContentId {string} the id of the container of the dialog that will be injected into the form container <br><em>MANDATORY</em> <br><i>default : undefined</i>
         * @param dialogContainerId {string} the id of the container within the form where the dialog content will be injected, the $.__currentDialogId+'_container' will be taken in place if exists and no dialogContainerId is provided <br><i>default : undefined</i>
         * @param cloneDialog {boolean} if true, the dialog will be cloned into the container not moved (by default)  <br><i>default : undefined</i>
         *
         */
        _injectDialog: function (dialogContentId, dialogContainerId, cloneDialog) {
            //checking parameters
            if ((dialogContentId === undefined || dialogContentId === null) && $.__currentDialogId === null) {
                $._log('====>ERROR : a dialog content id must be provided for the injection');
                return;
            }
            if ((dialogContainerId === undefined || dialogContainerId === null) && $.__currentDialogId === null) {
                $._log('====>ERROR : dialogContainerId must be provided');
                return;
            }

            var $dialogContent;

            //in case no dialogContentId is provided, the content is always the closest child of the dialog itself
            if ($.__currentDialogId !== null && (dialogContentId === undefined || dialogContentId === null)) {
                $dialogContent = $('#' + $.__currentDialogId).children().first();
            } else {
                $dialogContent = $('#' + dialogContentId);
            }

            //if a current dialog id has already been stored in the openDialog function, use it for inject the dialog content into this auto-generated container
            if ($.__currentDialogId !== null && (dialogContainerId === undefined || dialogContainerId === null)) {
                dialogContainerId = $.__currentDialogId + '_container';
            }


            if (cloneDialog === undefined || !cloneDialog) {
                //by default the dialog form is moved to the container
                $dialogContent.appendTo('#' + dialogContainerId);
            } else {
                $dialogContent.clone().appendTo('#' + dialogContainerId);
            }
        },


        /**
         * @see UI-DIALOG.JS
         * @class .
         * @memberOf UI-DIALOG.....$
         * @name _injectDialogClone
         * @description todo
         * @example todo
         */
        _injectDialogClone: function(dialogContentId, dialogContainerId) {

            $._injectDialog(dialogContentId, dialogContainerId, true);

        },




        _openUploadDialog: function( opt ) {

            var url = $._getContextPath() + '/' + opt.url + '.do';
            var submitUrl = opt.url;

            var settings = $.extend({

                dialogId:'upload_document_dialogContent',
                iFrameObject: true,
                dialogWidth: 550,
                dialogHeight: 250,
                dialogTitle: 'UPLOAD FILE DIALOG',
                isDialogOverflowAllowed: false,
                documentsArray: [{documentUrl:url}],
                iFrameHeight: 200,
                iFrameScrolling: 'no',
                dialogButtons: [
                    {
                        text:'cancel',
                        click:function () {
                            $._closeDialog({dialogId:'upload_document_dialogContent'})
                        }
                    } ,
                    {
                        text:'UPLOAD',
                        click:function () {
                            $._SF({
                                call: submitUrl,
                                formId: opt.submitFormId,
                                isFormInIframe: true,
                                isIframeInDialog: true,
                                fnPostCall:function() {
                                    if ($('iframe').contents().find('#uploadError').length) {
                                        $._elementUnblock($('.ui-dialog'));
                                    }
                                }
                            });
                        }
                    }
                ]

            }, opt);


            $._openIframeDialog( settings );


        },


        _closeUploadDialog: function() {

            $._closeDialog({dialogId:'upload_document_dialogContent'});
            $._elementUnblock($('#dialog').parent());

        },




        /**
         * @see UI-DIALOG.JS
         * @class .
         * @memberOf UI-DIALOG.....$
         * @name _openIframeDialog
         * @description
         * Open a dialog containing a iFrame url location to another page / multi-url locations
         * @param opt {object} the object literal parameters <br><em>MANDATORY</em>
         * @param opt.dialogId {string} the id of the generated dialog <br><em>MANDATORY</em> <br><i>default : null</i>
         * @param opt.dialogTitle {string} the title of the dialog <br><i>default : ''</i>
         * @param opt.dialogWidth {number} the width of the dialog, if not provided, window width -100px will be taken instead <br><i>default : null</i>
         * @param opt.dialogHeight {number} the height of the dialog, if not provided, window height -100px will be taken instead <br><i>default : null</i>
         * @param opt.dialogButtons {array} the buttons array that will be included in the dialog footers actions <br><i>default : null</i>
         * @param opt.isDialogOverflowAllowed {boolean} if the dialog can have a size greater than the viewport, then scrolling is allowed <br><i>default : true</i>
         * @param opt.isShowCloseButton {boolean} if the close top right button must be visible, if true this invalidate the button actions array <br><i>default : null</i>
         * @param opt.fnDialogOpen {function} the function that will be called on dialog open event <br><i>default : null</i>
         * @param opt.fnDialogClose {function} the function that will be called on dialog close event <br><i>default : null</i>
         * @param opt.documentsArray {array} the array of document object (documentId-documentFileName-documentDescription-documentUrl), can be a single entry, that'll be displayed in the dialog <br><em>ANDATORY</em> <br><i>default : []</i>
         * @param opt.selectedDocumentId {number} the selected documentId which will be visually selected if more than one documents are provided inside the array, if not provided, the first element in the array will be selected by default. <br><i>default : null</i>
         * @param opt.iFrameHeight {number} the height of the iframe inside the dialog must be specified for IE (not allowing 100%). <br><i>default : null</i>
         * @param opt.iFrameScrolling {string} specify if the iFrame must be scrollable, must be specified for IE. <br><i>default : 'yes'</i>
         * @param opt.iFrameObject (boolean) specity if the iFrame tag should be used instead of object tag. <br><i>default : false</i>
         *
         */
        _openIframeDialog: function (opt) {

            $.__UI_DIALOG.calculateDimensions();

            var settings = $.extend({

                dialogId: null,
                dialogTitle: '',
                iFrameObject: false,
                dialogWidth: null,
                dialogHeight: null,
                dialogButtons: null,
                isFullWidthOnHD: false,
                isDialogOverflowAllowed: true,
                isShowCloseButton: null,
                fnDialogOpen: null,
                fnDialogClose: null,
                documentsArray: [],
                selectedDocumentId: null,
                iFrameHeight: null,
                iFrameScrolling: 'yes',
                hasHeaderAction: false,
                headerActionClass: null,
                headerActionIconClass: 'icon-download',
                headerActionText: $._getData('jscaf_common_download_file'),
                headerActionUrl: null,
                notificationText: null,
                isPdfContent: false

            }, opt);

            var dialogContent, i, dialogHeight, dialogWidth, iFrameHeight, documentType, selectedArrayIdx = 0;


            //$._blockUI();


            //calculating dialog height for dimensioning manually the iframe (problem on IE when at 100%)
            if (settings.dialogHeight !== null) {
                dialogHeight = settings.dialogHeight;
            } else {
                dialogHeight = $.__UI_DIALOG.windowHeight - 50;
            }

            //calculating dialog width
            if (settings.dialogWidth === null) {
                if (!$.__modules.DISPLAY) {
                    dialogWidth = $.__UI_DIALOG.windowWidth - 100;
                } else {
                    if ($.__DISPLAY.hdMarginWidth === null) {
                        dialogWidth = $.__UI_DIALOG.windowWidth - 100;
                    } else {
                        if (settings.isFullWidthOnHD) {
                            dialogWidth = $.__UI_DIALOG.windowWidth;
                        } else {
                            dialogWidth = $.__UI_DIALOG.windowWidth - $.__DISPLAY.hdMarginWidth;
                        }
                    }
                }
            } else {
                dialogWidth = settings.dialogWidth;
            }

            //setting the iFrame height
            if (settings.iFrameHeight === null) {
                if (settings.documentsArray.length === 1) {
                    iFrameHeight = '99%';
                } else {
                    iFrameHeight = (dialogHeight - 50) + 'px';
                }

            } else {
                iFrameHeight = settings.iFrameHeight + 'px';
            }


            //constructing the dialog content
            dialogContent = '<div id="' + settings.dialogId + '" class="hidden">';

            //checking mandatory parameters
            if (settings.documentsArray.length === 0) {
                $._log('ERROR:provided documentsArray cannot be empty');
                return;
            }

            if (settings.documentsArray.length === 1) {
                if (settings.iFrameObject === true) {
                    //IMPORTANT when loading an iFrame, always first set the SRC as about:blank, otherwise this will fail in IE9,
                    //the effective SRC (the documentUrl) is set on fnAfterCreatePostCall of the openDialog function call
                    dialogContent += '<iframe style="width:100%;height:' + iFrameHeight + ';" src="about:blank" frameBorder="0" seamless="seamless" scrolling="' + settings.iFrameScrolling + '"></iframe>';
                } else {
                    //IMPORTANT IE8 requires the param with the SRC enclosed inside the object tag !!
                    if ($._isIE8()) {
                        documentType = '';
                        if (settings.isPdfContent) {
                            documentType = 'type="application/pdf"';
                        }
                        dialogContent += '<object style="width:100%;height:' + iFrameHeight + ';" data="' + settings.documentsArray[0].documentUrl + '" ' + documentType + ' frameBorder="0" seamless="seamless" scrolling="' + settings.iFrameScrolling + '"><param name="src" value="'+settings.documentsArray[0].documentUrl+'"></object>';
                    } else {
                        dialogContent += '<object style="width:100%;height:' + iFrameHeight + ';" data="' + settings.documentsArray[0].documentUrl + '" frameBorder="0" seamless="seamless" scrolling="' + settings.iFrameScrolling + '"></object>';
                    }
                }
            } else {
                dialogContent += '<div class="columns">';
                dialogContent += '  <div class="col-20" style="margin-right: 3px;">';
                dialogContent += '  <ul class="list-counter-small light with-arrow">';

                for (i = 0; i < settings.documentsArray.length; i++) {
                    if ((settings.documentsArray[i].documentId === settings.selectedDocumentId && settings.selectedDocumentId !== null) || (i === 0 && settings.selectedDocumentId === null)) {
                        dialogContent += '    <li id="' + i + '" class="iframe_selector parent selected">';
                        selectedArrayIdx = i;
                    } else {
                        dialogContent += '    <li id="' + i + '" class="iframe_selector parent">';
                    }
                    dialogContent += '        <div class="title" style="font-size:11px">' + settings.documentsArray[i].documentFileName + '</div>';
                    dialogContent += '    </li>';
                }

                dialogContent += '  </ul>';
                dialogContent += '  </div>';

                dialogContent += '<div class="col-80" id="pdf-preview-holder">';
                dialogContent += '<object id="pdf-preview" data="'+settings.documentsArray[selectedArrayIdx].documentUrl+'" type="application/pdf" width="100%" height="'+iFrameHeight+'" standby="Loading pdf..."><param name="src" value="'+settings.documentsArray[selectedArrayIdx].documentUrl+'"></object>';
                dialogContent += '</div>';

                dialogContent += '  <div class="cl"></div>';
                dialogContent += '</div>';
            }

            dialogContent += '</div>';


            //Creating the dialog
            $._openDialog({
                dialogId: settings.dialogId,
                dialogContent: dialogContent,
                isAutoGenerated: true,
                resizable: false,
                draggable: false,
                dialogHeight: dialogHeight,
                dialogWidth: dialogWidth,
                dialogTitle: settings.dialogTitle,
                dialogButtons: settings.dialogButtons,
                isDialogOverflowAllowed: settings.isDialogOverflowAllowed,
                isShowCloseButton: settings.isShowCloseButton,
                fnDialogOpen: function () {
                    $._log('openIframeDialog:fnDialogOpen');
                    if (settings.documentsArray.length !== 1) {
                        //bind the iframe selector event
                        $._$document.on('click', '.iframe_selector', function () {
                                $('#pdf-preview-holder').find('object').remove();
                                $('<object id="pdf-preview" data="'+settings.documentsArray[this.id].documentUrl+'" type="application/pdf" width="100%" height="'+iFrameHeight+'" standby="Loading pdf..." ><param name="src" value="'+settings.documentsArray[this.id].documentUrl+'"></param></object>').appendTo('#pdf-preview-holder');

                        });
                    }
                    $('object').contents().find('body').css({'overflow': 'hidden'});
                    $('object').contents().find('html').css({'overflow': 'hidden'});
                    $._execFn(settings.fnDialogOpen);
                },
                fnDialogClose: function () {
                    $._log('openIframeDialog:fnDialogClose');
                    if (settings.documentsArray.length !== 1) {
                        //unbind the iframe selector event
                        $._$document.off('click', '.iframe_selector');
                    }
                    $._execFn(settings.fnDialogClose);
                },
                fnAfterCreatePostCall: function () {
                    $._log('openIframeDialog:fnAfterCreatePostCall');

                    $._initDisplay();
                    $._unblockUI();
                    $._enableButtons();

                    //Important as if the src is set when declared, this break in IE9
                    if (!$._isIE8()) {
                       if (settings.documentsArray.length===1 && settings.iFrameObject) {
                           $('iframe').attr('src',settings.documentsArray[0].documentUrl);
                       }
                    }
                },
                hasHeaderAction: settings.hasHeaderAction,
                headerActionClass: settings.headerActionClass,
                headerActionIconClass: settings.headerActionIconClass,
                headerActionText: settings.headerActionText,
                headerActionUrl: settings.headerActionUrl,
                notificationText: settings.notificationText,
                isKeepCurrentDialogId: false
            });


        },


        _openDocumentDialog: function(opt) {

                var settings = $.extend({

                    documentArray: [], //to do implement multi content types documents display (image+pdf in the same viewer)
                    documentId: null,
                    documentDescription: null,
                    documentFilename: null,
                    documentUrl: null,
                    documentContentType: null

                }, opt);

                var documentsArray = [];

                if (settings.documentContentType === 'application/pdf') {

                    documentsArray.push({
                            documentId: settings.documentId,
                            documentFileName: settings.documentFilename,
                            documentDescription: settings.documentDescription,
                            documentUrl: $._getContextPath() + settings.documentUrl
                    });

                    $._openIframeDialog({
                        dialogId:'pdfDialog',
                        dialogTitle:'PDF - ' + settings.documentFilename,
                        documentsArray:documentsArray,
                        selectedDocumentId:null,
                        isPdfContent:true
                    });

                } else if (settings.documentContentType.indexOf('image') === 0) {


                      var $dialog = $('<div id="imageDialog"><img id="image" src=""/></div>');
                      $._$body.append($dialog);

                      var $image = $('#image');

                      $image.attr('src', $._getContextPath() + settings.documentUrl);

                      $image.load(function(){

                        $dialog.dialog({
                          modal: true,
                          title: 'IMAGE - ' + settings.documentFilename,
                          resizable: false,
                          draggable: false,
                          width: 'auto',
                          close: function(){
                             $(this).dialog("destroy");
                             $dialog.remove();
                          }
                        });
                      });

                }



        },






        /* ======================================================= */
        /* ===              MESSAGE BOXES                       == */
        /* ======================================================= */




        /**
         * @see UI-DIALOG.JS
         * @class .
         * @memberOf UI-DIALOG.....$
         * @name _msgbox
         * @description
         * Open a jqueryUI dialog as an message box / alert box with different possible styles/behaviours
         * @param opt {object} the object literal parameter <br><em>MANDATORY</em>
         * @param opt.text {string} the message text of the alert box <br><em>MANDATORY</em> <br><i>default : null</i>
         * @param opt.title {string} either the title of the dialog (for prompt and email) or a sub-title displayed on top of the text message for other types <br><i>default : null</i>
         * @param opt.msgboxType {string} the content type of alert box to be displayed : 'ok' - 'yes_no' - 'yes_no_cancel' - 'prompt' - 'email' <br><i>default : 'ok'</i>
         * @param opt.alertType {string} the display type of alert box to be displayed : 'warning' - 'info' - 'error' - 'question' - 'confirm' <br><i>default : 'warning'</i>
         * @param opt.promptFieldLabel {string} in case of a alertType='prompt', this label will be displayed for the input field <br><i>default : null</i>
         * @param opt.isPromptFieldRequired {boolean} in case of alerType='prompt', if TRUE, the input field will be validated before success leaving the msgbox <br><i>default : false</i>
         * @param opt.fnCallback {function} the callback function that'll executed upon user action, if provided : when YES=>sends TRUE, when NO=>sends FALSE, when CANCEL=>sends null, when alertType='prompt'=>sends the input field result <br><i>default : null</i>
         *
         */
        _msgbox: function (opt) {

            //get defaults settings and subclass by options if provided

            var settings = $.extend({

                dialogId: 'msgbox_dialog',
                text: null,
                title: null,
                subTitle: null,
                msgboxType: 'ok',
                alertType: 'warning',
                promptFieldLabel: null,
                isPromptFieldRequired: false,
                fnCallback: null,
                isEnterKeyOnPrimaryActionEnabled: false

            }, opt);


            var dialogContent = '';

            dialogContent += '<div id="' + settings.dialogId + '" class="hidden">';
            dialogContent += '<div class="msgbox-' + settings.alertType + '"></div>';

            if (settings.msgboxType === 'prompt' || settings.msgboxType === 'email') {

                if (settings.msgboxType === 'email') {
                    dialogContent += '<div class="field required">';
                    dialogContent += '<div class="field-label" style="float:none; width:auto;"><h6 style="text-align:left;" class="text-color-red">' + $._getData('jscaf_email_subject') + '</h6></div>';
                    dialogContent += '<input type="text" id="msgbox_input_subject" class="field-value" style="width:350px;"></textarea><span class="required-toggle required-required"></span>';
                    dialogContent += '</div>';

                    dialogContent += '<div class="field required">';
                    dialogContent += '<div class="field-label" style="float:none; width:auto;"><h6 style="text-align:left;" class="text-color-red">' + $._getData('jscaf_email_content') + '</h6></div>';
                    dialogContent += '<textarea id="msgbox_input_content" class="field-value" rows="6" style="width:350px;"></textarea><span class="required-toggle required-required"></span>';
                    dialogContent += '</div>';

                } else {
                    if (!settings.isPromptFieldRequired) {
                        dialogContent += '<div class="field">';
                        dialogContent += '<div class="field-label" style="float:none; width:auto;"><h6 style="text-align:left;">' + settings.promptFieldLabel + '</h6></div>';
                    } else {
                        dialogContent += '<div class="field required">';
                        dialogContent += '<div class="field-label" style="float:none; width:auto;"><h6 style="text-align:left;" class="text-color-red">' + settings.promptFieldLabel + '</h6></span></div>';
                    }
                    dialogContent += '<textarea id="msgbox_input" class="field-value" rows="3" style="width:250px;"></textarea><span class="required-toggle required-required"></span>';
                    dialogContent += '</div>';
                }
            } else {
                dialogContent += '<div class="fl" style="width:260px">';
                if (settings.subTitle === null) {
                    dialogContent += '<h4 class="msgbox-text-' + settings.alertType + '">' + settings.text + '</h4>';
                } else {
                    dialogContent += '<h4 class="msgbox-text-' + settings.alertType + '">' + settings.subTitle + '</h4>';
                    dialogContent += '<p>' + settings.text + '</p>';
                }
                dialogContent += '</div>';
            }

            dialogContent += '</div>';


            //setting the msgbox title according to the alert type
            if (settings.title === null) {
                settings.title = $._getData('jscaf_common_msgbox_' + settings.alertType);
            }


            //setting the buttons according to the msgbox type
            var buttons = [];

            var fnAfterCreatePostCall = null;

            if (settings.msgboxType === 'ok') {   //default settings
                buttons = [
                    {
                        text: 'OK',
                        click: function () {
                            $('#'+settings.dialogId).dialog("close");
                            if (settings.fnCallback !== null) {
                                settings.fnCallback(true);
                            }
                        },
                        tabindex:1,
                        id:'ui_dialog_primary_action'
                    }
                ];
            }
            if (settings.msgboxType === 'yes_no') {
                buttons = [
                    {
                        text: $._getData('jscaf_common_YES'),
                        click: function () {
                            $('#'+settings.dialogId).dialog("close");
                            if (settings.fnCallback !== null) {
                                settings.fnCallback(true);
                            }
                        },
                        tabindex:1,
                        id:'ui_dialog_primary_action'
                    },
                    {
                        text: $._getData('jscaf_common_NO'),
                        click: function () {
                            $('#'+settings.dialogId).dialog("close");
                            if (settings.fnCallback !== null) {
                                settings.fnCallback(false);
                            }
                        },
                        tabindex:2

                    }
                ];

                settings.isDialogButtonsMsgbox = true;  //disable show primary and secondary actions as default buttons dialog

            }
            if (settings.msgboxType === 'yes_no_cancel') {
                buttons = [
                    {
                        text: $._getData('jscaf_common_YES'),
                        click: function () {
                            $('#'+settings.dialogId).dialog("close");
                            if (settings.fnCallback !== null) {
                                settings.fnCallback(true);
                            }
                        },
                        tabindex:1,
                        id:'ui_dialog_primary_action'
                    },
                    {
                        text: $._getData('jscaf_common_NO'),
                        click: function () {
                            $('#'+settings.dialogId).dialog("close");
                            if (settings.fnCallback !== null) {
                                settings.fnCallback(false);
                            }
                        },
                        tabindex:2
                    },
                    {
                        text: $._getData('jscaf_common_cancel'),
                        click: function () {
                            $('#'+settings.dialogId).dialog("close");
                            if (settings.fnCallback !== null) {
                                settings.fnCallback(null);
                            }
                        },
                        tabindex:3
                    }
                ];

                settings.isDialogButtonsMsgbox = true;  //disable show primary and secondary actions as default buttons dialog
            }
            if (settings.msgboxType === 'prompt' || settings.msgboxType === 'email') {

                var primaryActionText = 'OK';

                if (settings.msgboxType === 'email') {
                    primaryActionText = $._getData('jscaf_email_send');
                }

                buttons = [
                    {
                        text: 'cancel',
                        click: function () {
                            $('#'+settings.dialogId).dialog("close");
                            if (settings.fnCallback !== null) {
                                settings.fnCallback(null);
                            }
                        },
                        tabindex:1
                    },
                    {
                        text: primaryActionText,
                        click: function () {

                            var $dialog = $('#' + settings.dialogId);

                            if (settings.msgboxType === 'prompt') {

                                var $msgBoxInput = $('#msgbox_input');
                                var inputValue = $msgBoxInput.val();

                                if (settings.isPromptFieldRequired) {
                                    if ($.trim(inputValue) === '') {
                                        jQuery.noticeAdd({text: 'Field is required', type: 'error'});
                                        $msgBoxInput.focus();
                                    } else {
                                        $('#'+settings.dialogId).dialog("close");
                                        if (settings.fnCallback !== null) {
                                            settings.fnCallback(inputValue);
                                        }
                                    }
                                } else {
                                    $('#'+settings.dialogId).dialog("close");
                                    if (settings.fnCallback !== null) {
                                        settings.fnCallback(inputValue);
                                    }
                                }

                            } else {

                                var $msgBoxSubject = $('#msgbox_input_subject');
                                var $msgBoxContent = $('#msgbox_input_content');

                                if ($.trim($msgBoxSubject.val()) === '' || $.trim($msgBoxContent.val()) === '') {
                                    jQuery.noticeAdd({text: 'Please fill all the mandatory fields', type: 'error'});
                                    if ($.trim($msgBoxSubject.val()) === '') {
                                        $msgBoxSubject.focus();
                                    } else {
                                        $msgBoxContent.focus();
                                    }
                                } else {
                                    $('#'+settings.dialogId).dialog("close");
                                    if (settings.fnCallback !== null) {
                                        settings.fnCallback({subject: $msgBoxSubject.val(), content: $msgBoxContent.val()});
                                    }
                                }


                            }

                        },
                        tabindex:2,
                        id:'ui_dialog_primary_action'
                    }
                ];

                fnAfterCreatePostCall = function () {
                    if (settings.msgboxType === 'prompt') {
                        $('#msgbox_input').focus();
                    } else {
                        $('#msgbox_input_subject').focus();
                    }
                };
            }


            $._$body.append(dialogContent);

            var dialogWidth = 400;
            if (settings.msgboxType === 'email') {
                dialogWidth = 500;
            }


            $._openDialog({
                dialogId: settings.dialogId,
                dialogTitle: settings.title,
                dialogWidth: dialogWidth,
                isDialogOverflowAllowed: false,
                isBodyOverflowHiddenOnCreate: false,
                fnDialogOpen: function () {
                    var $dialog = $('#' + settings.dialogId);
                    $dialog.closest('.ui-dialog').addClass(settings.alertType);
                    $dialog.parent().find('.ui-dialog-buttonpane').addClass(settings.alertType);
                    $dialog.parent().find('.ui-dialog-title').addClass(settings.alertType);
                },
                fnDialogClose: function() {
                    jQuery.noticeRemoveAll();
                    $._closeDialog({dialogId: settings.dialogId});
                    $('#' + settings.dialogId).remove();
                },
                dialogButtons: buttons,
                isDialogButtonsMsgbox: settings.isDialogButtonsMsgbox,
                isDialogButtonsShowIcons: false,
                fnAfterCreatePostCall: fnAfterCreatePostCall,
                isKeepCurrentDialogId: false,
                isCloseOnEscape: true
            });


            //enabling keyboard action event on primary action button
            if (settings.isEnterKeyOnPrimaryActionEnabled) {
                $(document).one('keydown', function(e) {
                    var $this = $(this);
                    if (e.keyCode === $.ui.keyCode.ENTER) {
                        $('#ui_dialog_primary_action').trigger('click');
                        e.stopPropagation();
                    }
                });
            }




        }


    });




}(jQuery));










