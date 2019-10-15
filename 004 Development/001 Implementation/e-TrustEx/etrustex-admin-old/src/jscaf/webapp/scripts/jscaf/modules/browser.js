/*global syze:true,jQuery:true,$:true,_:true,megaHoverOver:true,megaHoverOut:true,require:true,setTimeout,window,document,navigator,screen,console,moment,p,self*/



/* ======================================================= */
/* ===           JSCAF BROWSER MODULE                   == */
/* ======================================================= */


(function ($) {

    "use strict";

    $.extend({

        //DECLARATION
        //-----------
        __BROWSER: {


            isCheckBrowserSupported: function () {
                var browserVersion,
                    isSupported = true;

                if ($._settings.isBrowserDetectionEnabled) {

                    browserVersion = $.__BROWSER.getBrowserVersion();

                    if ($._isIE()) {
                        if (browserVersion < 8) {
                            isSupported = false;
                        }
                    }
                    if ($._isFirefox()) {
                        if (browserVersion < 5) {
                            isSupported = false;
                        }
                    }
                    if ($._isChrome()) {
                        if (browserVersion < 4) {
                            isSupported = false;
                        }
                    }
                    if ($._isOpera()) {
                        if (browserVersion < 9) {
                            isSupported = false;
                        }
                    }
                    if ($._isSafari()) {
                        if (browserVersion < 3) {
                            isSupported = false;
                        }
                    }

                }

                if (!isSupported) {
                    $._$main.addClass('hidden');
                    $._$header.addClass('hidden');
                    $._$footer.addClass('hidden');
                    if (!$('#unsupported_browser').length) {
                        var unsupportedContent = '';
                        unsupportedContent += '<div id="unsupported_browser" style="text-align:center;font-family:Arial,sans-serif;font-size:20px;font-weight:bold;"><br>';
                        unsupportedContent += 'Unsupported browser<br>';
                        unsupportedContent += '<span style="font-size:14px;font-weight: normal;">Your browser version is not supported</span><br><br>';
                        unsupportedContent += '</div>';
                        $._$body.html(unsupportedContent);
                    }
                }

                browserVersion = null;

                return isSupported;

            },


            checkBrowserWarning: function () {

                if (!$('#header-warning').length) {
                    $('#header').after('<div id="header-warning"></div>');
                }

                var browserVersion = $.__BROWSER.getBrowserVersion(),
                    isWarning = false,
                    $headerWarning = $('#header-warning'),
                    $browserWarning = $('#browser-warning');

                if ($._isIE()) {
                    if (browserVersion < 8) {
                        isWarning = true;
                    }
                }
                if ($._isFirefox()) {
                    if (browserVersion < 10) {
                        isWarning = true;
                    }
                }
                if ($._isChrome()) {
                    if (browserVersion < 10) {
                        isWarning = true;
                    }
                }
                if ($._isOpera()) {
                    isWarning = true;
                }
                if ($._isSafari()) {
                    if (browserVersion < 4) {
                        isWarning = true;
                    }
                }

                if (isWarning) {
                    if (!$browserWarning.length) {
                        $headerWarning.show().append('<li id="browser-warning">' + $._getData('jscaf_common_browser_warning') + '</li>');
                    }
                } else {
                    $browserWarning.remove();
                }

                if (!$headerWarning.children().length) {
                    $headerWarning.remove();
                }

                browserVersion = isWarning = $browserWarning = $headerWarning = null;

            },

            getBrowserName: function () {
                if ($._isIE()) {
                    return 'Internet Explorer';
                }
                if ($._isFirefox()) {
                    return 'Firefox';
                }
                if ($._isSafari()) {
                    return 'Safari';
                }
                if ($._isChrome()) {
                    return 'Chrome';
                }
                if ($._isOpera()) {
                    return 'Opera';
                }
                return 'Other';
            },

            getBrowserShortName: function () {
                if ($._isIE()) {
                    return 'ie';
                }
                if ($._isFirefox()) {
                    return 'ff';
                }
                if ($._isSafari()) {
                    return 'saf';
                }
                if ($._isChrome()) {
                    return 'chr';
                }
                if ($._isOpera()) {
                    return 'op';
                }
                return 'Other';
            },

            getBrowserVersion: function () {
                if ($._isIE()) {
                    var defaultVersion = parseInt(navigator.userAgent.substr(navigator.userAgent.indexOf('MSIE') + 5, 3), 10);
                    if (defaultVersion === 7) {
                        if (navigator.userAgent.indexOf('Trident/4.0') > 0) {
                            return 8;
                        }
                        else if (navigator.userAgent.indexOf('Trident/5.0') > 0) {
                            return 9;
                        }
                        else {
                            return 7;
                        }
                    } else {
                        return defaultVersion;
                    }
                }
                if ($._isFirefox()) {
                    return parseInt(navigator.userAgent.substr(navigator.userAgent.lastIndexOf('Firefox') + 8, 7), 10);
                }
                if ($._isSafari()) {
                    return parseInt(navigator.userAgent.substr(navigator.userAgent.lastIndexOf('Safari') + 7, 10), 10);
                }
                if ($._isChrome()) {
                    return parseInt(navigator.userAgent.substr(navigator.userAgent.lastIndexOf('Chrome') + 7, 8), 10);
                }
                if ($._isOpera()) {
                    return  parseInt(navigator.userAgent.substr(navigator.userAgent.lastIndexOf('Opera') + 6, 4), 10);
                }

                return null;
            },


            getPageZoom: function () {

                var returnValue = 'N/A';

                if ($._isIE()) {
                    var version = $.__BROWSER.getBrowserVersion(), b;

                    if (version === 7) {
                        b = document.body.getBoundingClientRect();
                        returnValue = ((b.right - b.left) / document.body.clientWidth).toFixed(2);
                    } else if (version === 8) {
                        returnValue = (screen.deviceXDPI / screen.systemXDPI).toFixed(2);
                        if (isNaN(returnValue)) {
                            //for IE7 detected as IE8, force IE7 detection
                            b = document.body.getBoundingClientRect();
                            returnValue = ((b.right - b.left) / document.body.clientWidth).toFixed(2);
                        }
                    }
                }

                return returnValue;

            },


            checkPageZoomWarning: function () {
                var $pageZoomWarning = $('#pagezoom-warning');

                if (!$('#header-warning').length) {
                    $('#header').after('<div id="header-warning"></div>');
                }

                var $headerWarning = $('#header-warning'),
                    pageZoom = $.__BROWSER.getPageZoom();

                if (pageZoom !== 'N/A' && $.trim(pageZoom) !== '1.00') {
                    if (!$pageZoomWarning.length) {
                        $headerWarning.append('<li id="pagezoom-warning">' + $._getData('jscaf_common_page_zoom_warning') + '</li>');
                    }
                } else {
                    $pageZoomWarning.remove();
                }

                if (!$headerWarning.children().length) {
                    $headerWarning.remove();
                }

                $headerWarning = pageZoom = null;
            }


    

        },


        /**
         * @see BROWSER.JS
         * @class .
         * @memberOf BROWSER.....$          
         * @name _isTouchDevice
         * @description todo
         * @example todo
         */
        _isTouchDevice: function () {
            try {
                document.createEvent("TouchEvent");
                return true;
            } catch (e) {
                return false;
            }
        },        

        /**
         * @see BROWSER.JS
         * @class .
         * @memberOf BROWSER.....$          
         * @name _isIE
         * @description todo
         * @example todo
         */
        _isIE: function () {
            return (navigator.userAgent.toUpperCase().indexOf('MSIE') > 0);
        },

        /**
         * @see BROWSER.JS
         * @class .
         * @memberOf BROWSER.....$          
         * @name _isIE7
         * @description todo
         * @example todo
         */
        _isIE7: function () {
            return ( $._isIE() && $.__BROWSER.getBrowserVersion() === 7 );
        },

        /**
         * @see BROWSER.JS
         * @class .
         * @memberOf BROWSER.....$          
         * @name _isIE8
         * @description todo
         * @example todo
         */
        _isIE8: function () {
            return ( $._isIE() && $.__BROWSER.getBrowserVersion() === 8 );
        },

        /**
         * @see BROWSER.JS
         * @class .
         * @memberOf BROWSER.....$          
         * @name _isIE9
         * @description todo
         * @example todo
         */
        _isIE9: function () {
            return ( $._isIE() && $.__BROWSER.getBrowserVersion() === 9 );
        },

        /**
         * @see BROWSER.JS
         * @class .
         * @memberOf BROWSER.....$          
         * @name _isFirefox
         * @description todo
         * @example todo
         */
        _isFirefox: function () {
            return (navigator.userAgent.toUpperCase().indexOf('FIREFOX') > 0);
        },

        /**
         * @see BROWSER.JS
         * @class .
         * @memberOf BROWSER.....$          
         * @name _isSafari
         * @description todo
         * @example todo
         */
        _isSafari: function () {
            return (navigator.userAgent.toUpperCase().indexOf('SAFARI') > 0 &&
                navigator.userAgent.toUpperCase().indexOf('CHROME') < 0);
        },

        /**
         * @see BROWSER.JS
         * @class .
         * @memberOf BROWSER.....$          
         * @name _isChrome
         * @description todo
         * @example todo
         */
        _isChrome: function () {
            return (navigator.userAgent.toUpperCase().indexOf('CHROME') > 0);
        },

        /**
         * @see BROWSER.JS
         * @class .
         * @memberOf BROWSER.....$          
         * @name _isOpera
         * @description todo
         * @example todo
         */
        _isOpera: function () {
            return (navigator.userAgent.toUpperCase().indexOf('OPERA') > 0);
        },

        /**
         * @see BROWSER.JS
         * @class .
         * @memberOf BROWSER.....$          
         * @name _isOther
         * @description todo
         * @example todo
         */
        _isOther: function () {
            return (!$._isIE() && !$._isFirefox() && !$._isSafari() && !$._isChrome() && !$._isOpera());
        },




        //old support of browser.msie and browser.mozilla functions dropped in jQuery 1.9
        browser: {
            msie: function () {
                return $._isIE();
            },
            mozilla: function () {
                return $._isFirefox();
            }
        }        


    });
}(jQuery));















