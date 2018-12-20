/*global syze:true,jQuery:true,$:true,_:true,megaHoverOver:true,megaHoverOut:true,require:true,setTimeout,window,document,navigator,screen,console,moment,p,self,unescape,escape*/


/* ======================================================= */
/* ===                 JSCAF UTILS                      == */
/* ======================================================= */

(function ($) {

    "use strict";

    $.extend({


        __utilsModuleIdentifier: function() {
            return true;
        },

 

        /* ======================================================= */
        /* ===             LOGGING FUNCTIONS                    == */
        /* ======================================================= */


        _logStack: [],
        _logStackIdx: 0,
        _logTimerStart: 0,
        _logTimerText: '',

        _forceLog: function (text) {
            $._log(text, true);
        },

        /**
         * @see UTILS.JS
         * @class .
         * @memberOf UTILS.....$          
         * @name _log
         * @description todo
         * @example todo
         */

        /*
         * @description
         * general logging function, display a timestamp into the console and stores logs history into a logs-array : _logStack[]
         * @class .
         * @name _log
         * @memberOf $
         * @param text {String} the text to be logged <br><i>default : undefined</i>
         * @param isForced {boolean} if the log should bypass the general log switched : _isLoggingEnabled <br><i>default : undefined</i>
         */
        _log: function (text, isForced) {

            var logText = moment().format('DD/MM/YYYY HH:mm:ss...SSS') + ' => ' + text;

            //insert log text in stack
            $._logStack[$._logStackIdx] = text;
            $._logStackIdx++;

            if (typeof console !== "undefined") {
                console.log(logText);
            }

        },



        /**
         * @see UTILS.JS
         * @class .
         * @memberOf UTILS.....$          
         * @name _logStart
         * @description todo
         * @example todo
         */
        _logStart: function(text) {
            $._logTimerStart = new Date().getTime();
            $._log(text + ' => START');
            $._logTimerText = text;
        },


        /**
         * @see UTILS.JS
         * @class .
         * @memberOf UTILS.....$          
         * @name _logEnd
         * @description todo
         * @example todo
         */
        _logEnd: function() {
            var duration = new Date().getTime() - $._logTimerStart;
            $._log($._logTimerText + ' => END  ---------> ' + duration + 'ms');
        },





        /* ======================================================= */
        /* ===             STRING FUNCTIONS                     == */
        /* ======================================================= */


        /**
         * @see UTILS.JS
         * @class .
         * @memberOf UTILS.....$          
         * @name _pad
         * @description todo
         * @example todo
         */

        /*
         * @description
         * add 0 leading character length to a string, works like the lpad oracle function for formating number
         * @class .
         * @name _pad
         * @memberOf $
         * @param n {String} the input string <br><i>default : undefined</i>
         * @param len {Number} the length of the leading zeroes string <br><i>default : undefined</i>
         */
        _pad: function (n, len) {
            var s = n.toString();
            if (s.length < len) {
                s = ('0000000000' + s).slice(-len);
            }
            return s;
        },

        _escapeRegExp: function (string) {
            return string.replace(/([.*+?\^=!:${}()|\[\]\/\\])/g, "\\$1");
        },


        /**
         * @see UTILS.JS
         * @class .
         * @memberOf UTILS.....$          
         * @name _replaceAll
         * @description todo
         * @example todo
         */
        _replaceAll: function(string, find, replace) {
            return string.replace(new RegExp($._escapeRegExp(find), 'g'), replace);
        },


        /* ======================================================= */
        /* ===             DATE FUNCTIONS                       == */
        /* ======================================================= */


        /**
         * @see UTILS.JS
         * @class .
         * @memberOf UTILS.....$          
         * @name _getDate
         * @description todo
         * @example todo
         */

        /*
         * @description
         * String TO date converter, use the moment.js library for date manipulation
         * @class .
         * @name _getDate
         * @memberOf $
         * @param s {String} the input date string, accept 3 foramts : DD/MM/YY or DD/MM/YYYY or DD/MM/YYYY HH:mm <br><i>default : undefined</i>
         */
        _getDate: function (s) {
            var m = $._getMoment(s);

            if (m === null) {
                return m;
            }

            return m.toDate();

        },


        /**
         * @see UTILS.JS
         * @class .
         * @memberOf UTILS.....$          
         * @name _getMoment
         * @description todo
         * @example todo
         */


        /*
         * @description
         * String TO moment converter, use the moment.js library for date manipulation
         * @class .
         * @name _getMoment
         * @memberOf $
         * @param s {String} the input date string, accept 3 foramts : DD/MM/YY or DD/MM/YYYY or DD/MM/YYYY HH:mm <br><i>default : undefined</i>
         */
        _getMoment://string format is FR format : DD/MM/YY or DD/MM/YYYY or DD/MM/YYYY HH:MI
            function (s) {
                if (s === undefined || s === null) {
                    return null;
                }
                if (s.length === 8) { // format = DD/MM/YY
                    return moment(s, 'DD/MM/YY');
                } else {
                    if (s.length === 10) { // format = DD/MM/YYYY
                        return moment(s, 'DD/MM/YYYY');
                    } else {  // format = DD/MM/YYYY HH:MI
                        return moment(s, 'DD/MM/YYYY HH:mm');
                    }
                }
            },


        /**
         * @see UTILS.JS
         * @class .
         * @memberOf UTILS.....$          
         * @name _getDateRange
         * @description todo
         * @example todo
         */

        /*
         * @description
         * returns a dates array filled by dates between a start date and a end date
         * @class .
         * @name _getDateRange
         * @memberOf $
         * @param startDate {Date} the start date of the range <br><i>default : undefined</i>
         * @param endDate {Date} the end date of the range <br><i>default : undefined</i>
         */
        _getDateRange: function (startDate, endDate) {
            if (startDate === undefined || endDate === undefined) {
                return [];
            }

            var dateArray = [];
            var currentDate = new Date(startDate);
            var t;
            while (currentDate <= endDate) {
                t = new Date(currentDate);
                dateArray.push(t);
                currentDate.setDate(currentDate.getDate() + 1);
            }
            return dateArray;
        },


        /**
         * @see UTILS.JS
         * @class .
         * @memberOf UTILS.....$          
         * @name _isDateEqual
         * @description todo
         * @example todo
         */


        /*
         * @description
         * checks if two dates are equal
         * @class .
         * @name _isDateEqual
         * @memberOf $
         * @param d1 {Date} the first date to be compared <br><i>default : undefined</i>
         * @param d2 {Date} the second date to be compared <br><i>default : undefined</i>
         */
        _isDateEqual: function (d1, d2, isCheckDateOnly) {
            if (d1 === undefined || d2 === undefined) {
                return false;
            } else {
                if (isCheckDateOnly) {
                    return (moment(d1).format('DD/MM/YYYY') === moment(d2).format('DD/MM/YYYY'));
                } else {
                    return ((d1.getTime() - d2.getTime()) === 0);
                }
            }
        },


        /**
         * @see UTILS.JS
         * @class .
         * @memberOf UTILS.....$          
         * @name _isPeriodValid
         * @description todo
         * @example todo
         */

        /*
         * @description
         * checks if a period given the start and end dates is valid, start date <= end date
         * @class .
         * @name _isPeriodValid
         * @memberOf $
         * @param dateFrom {Date} the start date of the period to be checked <br><i>default : undefined</i>
         * @param dateTo {Date} the end date of the period to be checked <br><i>default : undefined</i>
         */
        _isPeriodValid: function (dateFrom, dateTo) {
            if (dateFrom === undefined || dateTo === undefined) {
                return false;
            } else {
                return (dateFrom.getTime() <= dateTo.getTime());
            }
        },


        /**
         * @see UTILS.JS
         * @class .
         * @memberOf UTILS.....$          
         * @name _isPeriodValidNotEqual
         * @description todo
         * @example todo
         */

        /*
         * @description
         * checks if a period given the start and end dates is valid and not equal, start date < end date
         * @class .
         * @name _isPeriodValidNotEqual
         * @memberOf $
         * @param dateFrom {Date} the start date of the period to be checked <br><i>default : undefined</i>
         * @param dateTo {Date} the end date of the period to be checked <br><i>default : undefined</i>
         */
        _isPeriodValidNotEqual: function (dateFrom, dateTo) {
            if (dateFrom === undefined || dateTo === undefined) {
                return false;
            } else {
                return (dateFrom.getTime() < dateTo.getTime());
            }
        },

        _getGenerationTime: function () {
            var t = new Date();
            return (t.getTime() - $.__pageStartTime.getTime()) / 1000 + 's';
        },




        /* ======================================================= */
        /* ===             MISC FUNCTIONS                       == */
        /* ======================================================= */



        /**
         * @see UTILS.JS
         * @class .
         * @memberOf UTILS.....$          
         * @name _scrollToId
         * @description todo
         * @example todo
         */
         _scrollToId: function (id, offset, isHighlighted) {
            var $el = $("#" + id);

            return $._scrollToElement($("#" + id),offset,isHighlighted);
        },


        /**
         * @see UTILS.JS
         * @class .
         * @memberOf UTILS.....$          
         * @name _scrollToElement
         * @description todo
         * @example todo
         */
        _scrollToElement: function ($el, offset, isHighlighted) {

            if (!$el.length) {
                return [];
            }

            if (offset === undefined) {
                offset = 0;
            }

            if (isHighlighted === undefined) {
                isHighlighted = false;
            }            

            $('html,body').animate({scrollTop: $el.offset().top - offset});

            if (isHighlighted) {
                $el.effect("highlight", {color: "#ff0000"}, 1500);
            }

            return $el;
        },


        /**
         * @see UTILS.JS
         * @class .
         * @memberOf UTILS.....$          
         * @name _scrollToTop
         * @description todo
         * @example todo
         */
        _scrollToTop: function() {
            $('html,body').animate({scrollTop: 0});
        },


        _enableButtons: function () {
            var $buttons = $('span.button');
            if ($buttons.length) {
                $buttons.each(function () {
                    $(this).removeClass('disabled');
                });
            }
        },

        _disableButtons: function () {
            var $buttons = $('span.button');
            if ($buttons.length) {
                $buttons.each(
                    function () {
                        
                        var $this = $(this);

                        if (!$this.hasClass('enabled')) {
                            $this.addClass('disabled');
                            $._$form.off('click','#' + this.id);
                        }
                    }
                );
            }
        },


        /**
         * @see UTILS.JS
         * @class .
         * @memberOf UTILS.....$          
         * @name _getData
         * @description todo
         * @example todo
         */
        _getData: function (key) {
            var i, globMessagesArrayLength = $.__globMessagesArray.length;
            
            for ( i=0; i<globMessagesArrayLength;i++) {
                if ($.__globMessagesArray[i].key === key) {
                    return $.__globMessagesArray[i].value;
                }
            }

            i = globMessagesArrayLength = null;
        },


        /**
         * @see UTILS.JS
         * @class .
         * @memberOf UTILS.....$          
         * @name _setData
         * @description todo
         * @example todo
         */
        _setData: function (key, value) {
            if ($._getData(key) === undefined) {
                $.__globMessagesArray.push({key:key,value:value});
            }
        },



        /**
         * @see UTILS.JS
         * @class .
         * @memberOf UTILS.....$          
         * @name _cancelBubble
         * @description todo
         * @example todo
         */
        _cancelBubble: function (event) {
            if ($._isIE()) {
                event.cancelBubble = true;
            } else {
                event.stopPropagation();
            }
        },



        /**
         * @see UTILS.JS
         * @class .
         * @memberOf UTILS.....$          
         * @name _defer
         * @description todo
         * @example todo
         */
        _defer: function (aFunction) {
            $._execFn(aFunction,true);
        },


        /**
         * @see UTILS.JS
         * @class .
         * @memberOf UTILS.....$          
         * @name _execFn
         * @description todo
         * @example todo
         */
        _execFn: function (aFunction, isSetTimeout) {

            if (aFunction === undefined || aFunction === null) {
                return;
            }

            if (isSetTimeout === undefined) {
                isSetTimeout = false;
            }

            if (typeof aFunction === 'function') {

                if (isSetTimeout) {
                    setTimeout(function () {
                        aFunction();
                    }, 0);
                } else {
                    aFunction();
                }

            } else {

                if (aFunction.indexOf("()") > 0) {
                    aFunction = aFunction.substr(0, aFunction.indexOf("()"));
                }

                var objLiteral = null,
                    fn = null,
                    sep = aFunction.indexOf('.');

                if (sep > 0) {
                    objLiteral = aFunction.substring(0, sep);
                    fn = aFunction.substring(sep + 1);
                } else {
                    fn = aFunction;
                }

                if (objLiteral === null) {
                    if (typeof window[fn] === 'function') {
                        if (isSetTimeout) {
                            setTimeout(function () {
                                window[fn]();
                            }, 0);
                        } else {
                            window[fn]();
                        }
                    }
                } else {
                    if (typeof window[objLiteral][fn] === 'function') {
                        if (isSetTimeout) {
                            setTimeout(function () {
                                window[objLiteral][fn]();
                            }, 0);
                        } else {
                            window[objLiteral][fn]();
                        }
                    }
                }
            }
        },







        /* ======================================================= */
        /* ===             COMMON PAGE FUNCTIONS                == */
        /* ======================================================= */

        __pageParams: null,


        _getParamValue: function (name) {
            if ($.__pageParams !== null) {
                var i, pageParamsLength = $.__pageParams.length;
                
                for (i = 0; i < pageParamsLength; i++) {
                    if ($.__pageParams[i].name === name) {
                        return $.__pageParams[i].value;
                    }
                }
                
                i = pageParamsLength = null;
            }
            return null;
        },



        /**
         * @see UTILS.JS
         * @class .
         * @memberOf UTILS.....$          
         * @name __setParamValue
         * @description todo
         * @example todo
         */

        _setParamValue: function (name, value, isDate, formId, isFormInIframe) {
            $.__setParamValue({
                name: name,
                value: value,
                isDate: isDate,
                formId: formId,
                isFormInIframe: isFormInIframe
            });
        },


        __setParamValue: function (opt) {

            var settings = $.extend({
                name: null,
                value: null,
                isDate: false,
                formId: null,
                isFormInIframe: false
            }, opt);

            var isParamExist = false;

            if ($.__pageParams === null) {
                $.__pageParams = [];
            } else {
                var i, pageParamsLength = $.__pageParams.length;
                
                for (i = 0; i < pageParamsLength; i++) {
                    if ($.__pageParams[i].name === settings.name) {
                        $.__pageParams[i].value = settings.value;
                        $.__pageParams[i].isDate = settings.isDate;
                        isParamExist = true;
                        break;
                    }
                }

                i = pageParamsLength = null;
            }

            if (!isParamExist) {
                //stay compatible with normal input field definition and avoid double injection
                var $name;

                if (settings.formId !== null) {
                    if (settings.isFormInIframe) {
                        $name = $('iframe').contents().find('#' + settings.formId).find('#' + settings.name);
                    } else {
                        $name = $('#' + settings.formId).find('#' + settings.name);
                    }
                } else {
                    $name = $('#' + settings.name);
                }


                if ($name.length) {
                    $name.val(settings.value);
                } else {
                    $.__pageParams.push({name: settings.name, value: settings.value, isDate: settings.isDate});
                }
            }

            settings = isParamExist = null;
        },

        _injectPageParams: function (opt) {

            var settings = $.extend({
                formId: null,
                isFormInIframe: false,
                isFormInObject: false
            }, opt);


            if ($.__pageParams !== null) {

                var paramsContainer = '<div id="paramsContainer" class="hidden">',
                    paramName,
                    isDateExists = false,
                    p,
                    pageParamsLength = $.__pageParams.length;

                for (p = 0; p < pageParamsLength; p++) {
                    if ($.__pageParams[p].isDate && $._settings.dateFilterPrefix !== null) {
                        paramName = $._settings.dateFilterPrefix + $.__pageParams[p].name;
                        isDateExists = true;
                    } else {
                        paramName = $.__pageParams[p].name;
                    }
                    paramsContainer += '<input type="hidden" name="' + paramName + '" value="' + $.__pageParams[p].value + '" id="' + $.__pageParams[p].name + '">';
                }

                if (isDateExists && $._settings.dateFilterFormatName !== null) {
                    paramsContainer += '<input type="hidden" name="' + $._settings.dateFilterFormatName + '" value="dd/MM/yyyy hh:mm">';
                }

                paramsContainer += '</div>';

                if (settings.formId === null) {
                    $($._$form[$._$form.length - 1]).append(paramsContainer);
                } else {
                    if (settings.isFormInIframe) {
                        $('iframe').contents().find('#' + settings.formId).append(paramsContainer);
                    } else {
                        if (settings.isFormInObject) {
                            $($('object').get(0).contentDocument).find('#' + settings.formId).append(paramsContainer);
                        }  else {
                            $('#' + settings.formId).append(paramsContainer);
                        }
                    }
                }

                paramsContainer = paramName = isDateExists = p = pageParamsLength = null;

            }

            settings = null;

        },



        __isFunctionExists: function (fnString) {
            var objLiteral = null,
                fn = null,
                sep = fnString.indexOf('.');

            if (sep > 0) {
                objLiteral = fnString.substring(0, sep);
                fn = fnString.substring(sep + 1);
            } else {
                fn = fnString;
            }

            if (objLiteral === null) {
                if (window[fn] !== undefined) {
                    return (typeof window[fn] === 'function');    
                } else {
                    return false;
                }
            } else {
                if (window[objLiteral] !== undefined) {
                    if (window[objLiteral][fn] !== undefined) {
                        return (typeof window[objLiteral][fn] === 'function');
                    } else {
                        return false;
                    }    
                } else {
                    return false;
                }    
            }
        },



        __externalWindow: null,
        __externalWindowOpened: false,



        /**
         * @see UTILS.JS
         * @class .
         * @memberOf UTILS.....$          
         * @name _navigateTo
         * @description todo
         * @example todo
         */
        _navigateTo: function (opt) {

            $._blockUI();

            var settings = $.extend({
                url: null,
                isOpenInExternalWindow: false,
                isCenterExternalWindow: true,
                isSameWindow: true,
                isKeepOnlyOneExternalWindow: true,
                externalWindowWidth: 800,
                externalWindowHeight: 600
            }, opt);

            var name;

            if (settings.isKeepOnlyOneExternalWindow) {
                name = "externalWindow";
            } else {
                name = "_blank";
            }

            if (settings.isOpenInExternalWindow) {
                $.__externalWindow = window.open(
                    $._getContextPath() + settings.url,
                    name,
                    "toolbar=no, location=no, directories=no, status=no, menubar=no, " +
                        "scrollbars=yes, resizable=yes, copyhistory=no, " +
                        "width=" + settings.externalWindowWidth + ",height=" + settings.externalWindowHeight
                );
                if (!$.__externalWindowOpened && settings.isCenterExternalWindow) {
                    $.__externalWindow.moveTo(screen.width / 2 - settings.externalWindowWidth / 2, screen.height / 2 - settings.externalWindowHeight / 2);
                }
                $.__externalWindowOpened = true;
            } else {
                if (settings.isSameWindow) {
                    window.location = $._getContextPath() + settings.url;
                } else {
                    window.open($._getContextPath() + settings.url, '_blank');
                }
            }

            $._unblockUI();
        },


        /**
         * @see UTILS.JS
         * @class .
         * @memberOf UTILS.....$          
         * @name _closeExternalWindow
         * @description todo
         * @example todo
         */
        _closeExternalWindow: function () {

            if ($.__externalWindow !== null) {
                $.__externalWindow.close();
            }

        },

        /**
         * @see UTILS.JS
         * @class .
         * @memberOf UTILS.....$          
         * @name _form2json
         * @description todo
         * @example todo
         */
        _form2json: function ($form) {
            var o = {};
            var a = $form.serializeArray();
            $.each(a, function() {
                if (o[this.name] !== undefined) {
                    if (!o[this.name].push) {
                        o[this.name] = [o[this.name]];
                    }
                    o[this.name].push(this.value || '');
                } else {
                    o[this.name] = this.value || '';
                }
            });
            return o;
        },


        /* ======================================================= */
        /* ===             EVENTS FUNCTIONS                     == */
        /* ======================================================= */

        /**
         * @see UTILS.JS
         * @class .
         * @memberOf UTILS.....$          
         * @name _addEvent
         * @description todo
         * @example todo
         */
        //default form registered event creation
        _addEvent: function (eventType, selector, fn) {
            $._log('CORE.addEvent : eventType:[' + eventType + '] selector:[' + selector + ']');

            //listen the event
            $._$form.on(
                eventType,
                selector,
                fn
            );
        },


        /**
         * @see UTILS.JS
         * @class .
         * @memberOf UTILS.....$          
         * @name _addEventButtonClick
         * @description todo
         * @example todo
         */

        //default form registered event creation
        _addEventButtonClick: function (selector, fn) {
            $._log('CORE.addEventButtonClick : selector:[' + selector + ']');

            //listen the event
            $._$form.onButtonClick(
                'click',
                selector,
                fn
            );
        },


        /**
         * @see UTILS.JS
         * @class .
         * @memberOf UTILS.....$          
         * @name _removeEvent
         * @description todo
         * @example todo
         */

        //default form registered event removal
        _removeEvent: function (eventType, selector) {
            $._log('CORE.removeEvent : feventType:[' + eventType + '] selector:[' + selector + ']');

            $._$form.off(
                eventType,
                selector
            );
        },


        /**
         * @see UTILS.JS
         * @class .
         * @memberOf UTILS.....$          
         * @name _addDocumentEvent
         * @description todo
         * @example todo
         */

        //default TOP registered event creation
        _addDocumentEvent: function (eventType, selector, fn) {
            $._log('CORE.addDocumentEvent : eventType:[' + eventType + '] selector:[' + selector + ']');

            $(document).on(
                eventType,
                selector,
                fn
            );
        },


        /**
         * @see UTILS.JS
         * @class .
         * @memberOf UTILS.....$          
         * @name _removeDocumentEvent
         * @description todo
         * @example todo
         */
        //default TOP registered event removal
        _removeDocumentEvent: function (eventType, selector) {
            $._log('CORE.removeEvent : eventType:[' + eventType + '] selector:[' + selector + ']');

            $(document).off(
                eventType,
                selector
            );
        },


        /**
         * @see UTILS.JS
         * @class .
         * @memberOf UTILS.....$          
         * @name _addDialogEvent
         * @description todo
         * @example todo
         */

        //default TOP registered event creation
        _addDialogEvent: function (dialogId, eventType, selector, fn) {
            $._log('CORE.addDocumentEvent : eventType:[' + eventType + '] selector:[' + selector + ']');

            $('#' + dialogId).on(
                eventType,
                selector,
                fn
            );
        },


        /**
         * @see UTILS.JS
         * @class .
         * @memberOf UTILS.....$          
         * @name _addEventOnEnter
         * @description todo
         * @example todo
         */

        //particular bound event on ENTER keydown on form element
        _addEventOnEnter: function (selector, fn) {
            $._log('CORE.addEventOnEnter : selector:[' + selector + ']');

            $._$form.on(
                'keypress',
                selector,
                function (event) {
                    var key;
                    if (window.event) {
                        key = window.event.keyCode; //IE
                    } else {
                        key = event.which;
                    }

                    if (key === 13) {
                        $._execFn(fn, true);
                        return false;
                    }

                    return true;
                }
            );
        },


        /**
         * @see UTILS.JS
         * @class .
         * @memberOf UTILS.....$          
         * @name _addDocumentEventOnEnter
         * @description todo
         * @example todo
         */        
        //particular bound event on ENTER keydown on form element to be used in dialogs
        _addDocumentEventOnEnter: function (selector, fn) {
            $._log('CORE.addEventOnEnter : selector:[' + selector + ']');

            $(document).on(
                'keypress',
                selector,
                function (event) {
                    var key;
                    if (window.event) {
                        key = window.event.keyCode; //IE
                    } else {
                        key = event.which;
                    }

                    if (key === 13) {
                        $._execFn(fn, true);
                        return false;
                    }

                    return true;
                }
            );
        },


        /**
         * @see UTILS.JS
         * @class .
         * @memberOf UTILS.....$          
         * @name _addOnBeforeUnloadEvent
         * @description todo
         * @example todo
         */
        _addOnBeforeUnloadEvent: function () {
            window.onbeforeunload = function () {
                return $._getData('jscaf_common_onbeforeunload_message');
            };
        },



        /**
         * @see UTILS.JS
         * @class .
         * @memberOf UTILS.....$          
         * @name _removeOnBeforeUnloadEvent
         * @description todo
         * @example todo
         */
        _removeOnBeforeUnloadEvent: function () {
            window.onbeforeunload = function () {
            };
        },





        /**
         * @see UTILS.JS
         * @class .
         * @memberOf UTILS.....$          
         * @name _getKey
         * @description todo
         * @example todo
         */
        _getKey: function(event) {
            var key;
            if (window.event) {
                key = window.event.keyCode; //IE
            } else {
                key = event.which;
            }
            return key;
        },







        /* ======================================================= */
        /* ===             MISC FUNCTIONS                       == */
        /* ======================================================= */


        /**
         * @see UTILS.JS
         * @class .
         * @memberOf UTILS.....$          
         * @name _isEmpty
         * @description todo
         * @example todo
         */
        _isEmpty: function(map) {
           var key;

           for(key in map) {
              if (map.hasOwnProperty(key)) {
                 return false;
              }
           }
           return true;
        },


        /**
         * @see UTILS.JS
         * @class .
         * @memberOf UTILS.....$          
         * @name _refreshJsonTemplate
         * @description todo
         * @example todo
         */
        _refreshJsonTemplate: function(templateId, jsonData, templateContainerId, fnPostCall) {

            var $template = $('#'+templateId), 
                $templateContainer = null;

            if (templateId === undefined || jsonData === undefined) {
                $._L('ERROR ===> templateId AND jsonData should be provided');
                return false;
            }

            if (!$template.length) {
                $._L('ERROR ===> template with id:[' + templateId +'] cannot be found ');
                return false;
            }


            //in case the container is undefined, the container is always the previous element to the template
            if (templateContainerId === undefined) {
                $templateContainer = $template.prev();
            } else {
                $templateContainer = $('#'+templateContainerId);
                if (!$templateContainer.length) {
                    $._L('ERROR ===> templateContainer with id:[' + templateContainerId +'] cannot be found ');
                    return false;
                } else {
                    $templateContainer = $('#'+templateContainerId);
                }
            }

            $templateContainer.html($template.render(jsonData));

            $template = $templateContainer = null;

            if (fnPostCall !== undefined) {
                fnPostCall();
            }

            return true;
        },






        /* ======================================================= */
        /* ===            REQUIRE.JS + COMPONENTS FUNCTIONS     == */
        /* ======================================================= */


        // REQUIRE.JS functions
        // ====================


        /**
         * @see UTILS.JS
         * @class .
         * @memberOf UTILS.....$          
         * @name _require
         * @description todo
         * @example todo
         */
        //interface to requireJS.require
        _require: function (moduleName, fn) {
            var modulesArray = [],
                i, isAllModulesExist = true,
                modulesArrayLength;

            if ($.isArray(moduleName)) {
                modulesArray = moduleName;
            } else {
                modulesArray.push(moduleName);
            }

            modulesArrayLength = modulesArray.length;

            for (i = 0; i < modulesArrayLength; i++) {
                if (window[modulesArray[i]] === undefined) {
                    isAllModulesExist = false;

                    if (i === modulesArray.length - 1) {
                        require([modulesArray[i]], fn);
                    } else {
                        require([modulesArray[i]]);
                    }
                }
            }

            if (isAllModulesExist && fn !== undefined) {
                fn();
            }

            modulesArray = i = isAllModulesExist = modulesArrayLength = moduleName = fn = null;

        },


        /* ======================================================= */
        /* ===     HELPERS FUNCTIONS FOR jQUERY SUBSTITUTES     == */
        /* ======================================================= */


        /**
         * @see UTILS.JS
         * @class .
         * @memberOf UTILS.....$          
         * @name _hasClass
         * @description todo
         * @example todo
         */
        _hasClass: function (el, name) {
           return new RegExp('(\\s|^)'+name+'(\\s|$)').test(el.className);
        },


        /**
         * @see UTILS.JS
         * @class .
         * @memberOf UTILS.....$          
         * @name _addClass
         * @description todo
         * @example todo
         */
        _addClass: function(el, name) {
           if (!$._hasClass(el, name)) { el.className += (el.className ? ' ' : '') +name; }
        },


        /**
         * @see UTILS.JS
         * @class .
         * @memberOf UTILS.....$          
         * @name _removeClass
         * @description todo
         * @example todo
         */
        _removeClass: function(el, name) {
           if ( el === null) {
              return;
           }
           if ($._hasClass(el, name)) {
              el.className=el.className.replace(new RegExp('(\\s|^)'+name+'(\\s|$)'),' ').replace(/^\s+|\s+$/g, '');
           }
        },


        /**
         * @see UTILS.JS
         * @class .
         * @memberOf UTILS.....$          
         * @name _extend
         * @description todo
         * @example todo
         */
        _extend: function(a,b) {
            var key;
            for(key in b) {
                if(b.hasOwnProperty(key)) {
                    a[key] = b[key];
                }
            }
            return a;
        },


        /**
         * @see UTILS.JS
         * @class .
         * @memberOf UTILS.....$          
         * @name _getById
         * @description todo
         * @example todo
         */
        _getById: function(id) {
            return document.getElementById(id);
        },


        /**
         * @see UTILS.JS
         * @class .
         * @memberOf UTILS.....$          
         * @name _getAttr
         * @description todo
         * @example todo
         */
        _getAttr: function(el,attr) {
            var value = el.getAttribute(attr);
            if (value === null) {
                return undefined;
            }
            return value;
        },


        /**
         * @see UTILS.JS
         * @class .
         * @memberOf UTILS.....$          
         * @name _setAttr
         * @description todo
         * @example todo
         */
        _setAttr: function(el,attr,value) {
            el.setAttribute(attr,value);
        },


        /**
         * @see UTILS.JS
         * @class .
         * @memberOf UTILS.....$          
         * @name _getId
         * @description todo
         * @example todo
         */
        _getId: function(el) {
            return el.getAttribute('id');
        },


        /**
         * @see UTILS.JS
         * @class .
         * @memberOf UTILS.....$          
         * @name _getCookie
         * @description todo
         * @example todo
         */
        _getCookie: function(c_name) {
            var i,x,y,ARRcookies=document.cookie.split(";");
            for (i=0;i<ARRcookies.length;i++) {
                x=ARRcookies[i].substr(0,ARRcookies[i].indexOf("="));
                y=ARRcookies[i].substr(ARRcookies[i].indexOf("=")+1);
                x=x.replace(/^\s+|\s+$/g,"");
                if (x===c_name) {
                    return unescape(y);
                }
            }
        },


        /**
         * @see UTILS.JS
         * @class .
         * @memberOf UTILS.....$          
         * @name _setCookie
         * @description todo
         * @example todo
         */
        _setCookie: function(c_name,value,exdays) {
            var exdate=new Date();
            exdate.setDate(exdate.getDate() + exdays);
            var c_value=escape(value) + ((exdays===null) ? "" : "; expires="+exdate.toUTCString());
            document.cookie=c_name + "=" + c_value;
        },


        /**
         * @see UTILS.JS
         * @class .
         * @memberOf UTILS.....$          
         * @name _remove
         * @description todo
         * @example todo
         */
        _remove: function($o) {
            var $parent = $o.parent();
            $o.remove();
            return $parent;
        },


        /**
         * @see UTILS.JS
         * @class .
         * @memberOf UTILS.....$          
         * @name _putBack
         * @description todo
         * @example todo
         */
        _putBack: function($parent,$o) {
            $parent.append($o);
        }






    });
}(jQuery));




