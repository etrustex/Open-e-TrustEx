/*global syze:true,jQuery:true,$:true,_:true,megaHoverOver:true,megaHoverOut:true,require:true,setTimeout,window,document,navigator,screen,console,moment,p,self*/



/* ======================================================= */
/* ===           JSCAF DISPLAY MODULE                   == */
/* ======================================================= */


(function ($) {

    "use strict";

    $.extend({

        __DISPLAY: {

            hdMarginWidth: null,
            initPhase: false,

            __$topNav: null,
            __topNavPosition: null,


            init: function() {
                $._log('DISPLAY.init');
                

                // DISPLAY SETTINGS EVENTS
                // -----------------------
                if ($._settings.isDisplaySettingsUsed) {
                    $._$document.on('click',
                        '.JS_fontsize_switcher',
                        function () {
                            $.__DISPLAY.switchFontsize($(this).attr('fontsize'));
                            $('#display-settings-container').poshytip('hide').removeClass('tooltip-active');
                        });

                    $._$document.on('click',
                        '.JS_theme_switcher',
                        function () {
                            $.__DISPLAY.switchTheme($(this).attr('themeName'));
                            $('#display-settings-container').poshytip('hide').removeClass('tooltip-active');
                        });

                    $._$document.on('click',
                        '.JS_hd_margin_switcher',
                        function () {
                            $.__DISPLAY.switchHdMarginWidth($(this).attr('marginWidth'));
                            $.__DISPLAY.initPhase = false;
                            $.__DISPLAY.initResponsiveness();
                            $('#display-settings-container').poshytip('hide').removeClass('tooltip-active');
                        });
                }


                // GLOBAL SETTINGS EVENTS
                // ----------------------
                if ($._settings.isKeepTopNavigationFixed) {
                    $.__DISPLAY.__$topNav = $('ul.top-nav'); 
                    $.__DISPLAY.__topNavPosition = $.__DISPLAY.__$topNav.offset();

                    $(window).scroll(function(){
                        $.__DISPLAY.refreshTopNavigation();  
                    });
                }



                //SETTING THE RESPONSIVE ENABLER
                //------------------------------
                $.__DISPLAY.initPhase = true;
                $.__DISPLAY.initResponsiveness();
                $.__DISPLAY.initPhase = false;


                //set displaySettings hd margin width visibility according to screen dimension detected
                if ($._hasClass($._html,'ishd')) {
                    $._removeClass($._getById('display-settings-container-hd'),'hidden');
                    $.__DISPLAY.initHdMarginWidth();
                }                



                //init display settings
                $.__DISPLAY.initTheme();
                $.__DISPLAY.initFontsize();

            },
            

            initResponsiveness: function() {
                $._log('DISPLAY.initResponsiveness');

                if ($.__DISPLAY.initPhase) {
                    
                    //INIT SYZE DETECTION AND CALLBACK
                    //--------------------------------

                    if ($._settings.isMinimalScreenWidthDetectionActive) {
                        syze.sizes(0, 480, 960, 1400, 2500).from('browser').names({ 0: 'unsupported', 480: 'mobile', 960: 'desktop', 1400: 'hd', 2500: 'doublescreen'});
                    } else {
                        if ($._settings.isMinimalScreenWidthDetectionActiveForIE78Only && ($._isIE7() || $._isIE8())) {
                            syze.sizes(0, 480, 960, 1400, 2500).from('browser').names({ 0: 'unsupported', 480: 'mobile', 960: 'desktop', 1400: 'hd', 2500: 'doublescreen'});
                        } else {
                            syze.sizes(0, 960, 1400, 2500).from('browser').names({ 0: 'mobile', 960: 'desktop', 1400: 'hd', 2500: 'doublescreen'});
                        }
                    }

                    syze.debounceRate(250);

                } else {

                    $.__DISPLAY.refreshResponsiveness();
                }    

            },


            syze: function() {
                syze.callback(function () {
                    $.__DISPLAY.refreshResponsiveness();
                }); 
            },


            refreshResponsiveness: function () {

                $._log('DISPLAY.refreshResponsiveness : ENABLED');

                var isMobile = $._hasClass($._html,'ismobile'),
                    isHD = $._hasClass($._html,'ishd'),
                    isDoubleScreen = $._hasClass($._html,'isdoublescreen'),
                    isDesktop = $._hasClass($._html,'isdesktop'),
                    isUnsupported = $._hasClass($._html,'isunsupported'),
                    width = (window.innerWidth || document.documentElement.clientWidth),
                    toTop = $._getById('totop');


                $._$body.removeClass('mlr-0 mlr-sb-0 mlr-10 mlr-sb-10 mlr-20 mlr-sb-20 mlr-30 mlr-sb-30 no-margin width-sb-80');

                //HD resolution contraction : always keep 1280px width on higher resolution >1400px width, otherwise it's unreadable
                if (isHD && !isDoubleScreen) {
                    //add margins left and right of the body + adjust header elements position
                    var marginWidth = $.__DISPLAY.hdMarginWidth;

                    if (marginWidth === null) {
                        marginWidth = $.__DISPLAY.getHdMarginWidth();
                    }

                    if ($._settings.isSlidebarsActive && $._hasClass($._$html[0],'sb-active-left')) {

                        $._$body.addClass('hd mlr-sb-' + marginWidth);

                    } else {

                        $._$body.addClass('hd mlr-' + marginWidth);

                    }

                    $.__DISPLAY.hdMarginWidth = marginWidth;                    

                    if ($._settings.isSlidebarsActive) {
                        $('.sb-slidebar').attr('data-sb-width','15%');
                    }

                } else {
                    //go back to the 1280px width initial state
                    $._removeClass($._$body[0],'hd');

                    if ($._settings.isSlidebarsActive && $._hasClass($._$html[0],'sb-active-left')) {
                        $._addClass($._$body[0],'width-sb-80');
                    } else {
                        $._addClass($._$body[0],'no-margin');
                        $._removeClass($._$body[0],'width-sb-80');   
                    }

                    if ($._settings.isSlidebarsActive) {
                        $('.sb-slidebar').attr('data-sb-width','20%');
                    }

                    $.__DISPLAY.hdMarginWidth = null;
                }




                //check for mobile resolution or desktop between 960px and 1024px wide resolution
                if (isMobile || (isDesktop && width <= 1024)) {

                    //hide table column when resolution is less than minimum target (<1024px)
                    $('td.optional,th.optional').hide();
                    $('.table-menu.table-menu-hidden').find("input").trigger("updateCheck");

                    //transform tables td iconifiable elements
                    $('td.JS_iconifiable').each(function () {
                        var $this = $(this);
                        //setting the associated th width for icon display and remove column header text
                        if (!$this.find('span').length) {
                            $this.closest('table')
                                .find('th')
                                .eq($this.index())
                                .attr('width', '16px')
                                .text('');

                            //text content of td is put on the title attribute for tooltip display
                            $this.attr('title', $.trim($this.text()));

                            //text content is replaced by an icon
                            $this.html('<span class="icon-table icon-purpose"></span>');
                        }
                    });

                    //transform sub-rowable columns into alternate row display
                    $('td.JS_subrowable').each(function () {
                        var $this = $(this);

                        $._log($this);

                        var $th = $this.closest('table')
                            .find('th')
                            .eq($this.index())
                            .hide();

                        if (!$th.prev().find('.second-line').length) {
                            $th.prev()
                                .append('<span class="second-line">' + $th.text() + '</span>');
                        }

                        if (!$this.prev().find('.second-line').length) {
                            $this.hide().prev()
                                .append('<span class="second-line">' + $this.text() + '</span>');
                        }
                    });
                }


                //show table columns when resolution is normal target (> 1024px)
                if ((isDesktop && width > 1024) || isHD || isDoubleScreen) {
                    $('td.optional,th.optional').show();
                    $('.table-menu.table-menu-hidden').find("input").trigger("updateCheck");

                    //show column again
                    $('td.JS_subrowable').each(function () {
                        var $this = $(this);

                        $this.closest('table')
                            .find('th')
                            .eq($this.index())
                            .show()
                            .prev()
                            .find('.second-line')
                            .remove();

                        $this.show()
                            .prev()
                            .find('.second-line')
                            .remove();

                    });
                }

                //check for mobile resolutions : between 960px and less than 1024px wide
                if (isMobile) {

                    if ($._settings.isSlidebarsActive) {
                        $('.sb-slidebar').attr('data-sb-width','50%');
                    }                        


                    //normal floats for ul#dashboard-buttons if more than 400px width
                    if (width < 400) {
                        $('#dashboard-buttons').addClass('smallmobile');
                    } else {
                        $('#dashboard-buttons').removeClass('smallmobile');
                    }

                    //transform tabs into partial select box
                    var tabs = $('ul.tabs');

                    if (tabs.length) {


                        //check if a tab has been hidden due to the resize
                        var hiddenTab = false, prevOffsetLeft = 0;
                        tabs.find('li').each(function (idx) {
                            var $this = $(this),
                                thisOffsetLeft = $this.offset().left;
                            if (thisOffsetLeft < prevOffsetLeft) {
                                hiddenTab = true;
                            }
                            prevOffsetLeft = thisOffsetLeft;
                        });


                        //if at least a tab has been hidden, apply mobile transformation
                        if (hiddenTab) {

                            tabs.find('li:not(.selected)').addClass('hidden');

                            if (!$('#tab-previous').length) {
                                tabs.prepend('<i id="tab-previous" class="icon size24 icon-ft-arrow-left fl cr-pointer" style="margin-right:15px;"></i>');
                            }

                            if (!$('#tab-next').length) {
                                tabs.append('<i id="tab-next" class="icon size24 icon-ft-arrow-right cr-pointer" style="margin-left:10px;"></i>');
                            }

                        }

                    }

                } else {
                    //back to normal topnav
                    $('#topnav-menu').remove();
                    $('#topnav-menu-content').remove();
                    $('#topbar-menu').remove();
                    $('#topbar-menu-content').remove();
                    $('.top-nav').find('li').show();
                    $('.top-bar').find('li').show();

                    //reset all tabs
                    $('ul.tabs').find('li').removeClass('hidden');
                    $('#tab-previous').remove();
                    $('#tab-next').remove();

                }

                //check for less than highest mobile resolution supported : 600px
                var $unsupportedResolution = $('#unsupported_resolution');
                if (isUnsupported) {
                    $._$main.addClass('hidden');
                    $._$header.addClass('hidden');
                    $._$footer.addClass('hidden');
                    if (!$unsupportedResolution.length) {
                        var unsupportedContent = '';
                        unsupportedContent += '<div id="unsupported_resolution" style="text-align:center;font-family:Arial,sans-serif;font-size:20px;font-weight:bold;"><br>';
                        unsupportedContent += 'Unsupported resolution<br>';
                        unsupportedContent += '<span style="font-size:14px;font-weight: normal;">must be greater than 600px wide</span><br><br>';
                        unsupportedContent += '</div>';
                        $._$body.append(unsupportedContent);
                    }
                } else {
                    $unsupportedResolution.remove();
                    $._$main.removeClass('hidden');
                    $._$header.removeClass('hidden');
                    $._$footer.removeClass('hidden');
                }


                //filling empty space to avoid little screens reduced when no enough content to fill the screen height
                $.__fillClientHeight();        

                //when top-nav is fixed then refresh it it screen is resized
                if ($._settings.isKeepTopNavigationFixed) {
                    $.__DISPLAY.refreshTopNavigation();
                }


                //dashboard for flat design
                var $dashboardNav = $('#dashboard_nav');
                if ($dashboardNav.length) {
                    $( '.dashboard-outer' ).css({ 'height': $( window ).height() - $( '#header' ).height() + 'px'});
                }

            },


            refreshTopNavigation: function() {
                if($(window).scrollTop() > $.__DISPLAY.__topNavPosition.top){
                   $.__DISPLAY.__$topNav.css('position','fixed').css('top','0').css({'width':$('#header').width()-19}).addClass('light');
                } else {
                   $.__DISPLAY.__$topNav.css('position','absolute').css('top',$.__DISPLAY.__topNavPosition.top).removeClass('light');
                }  
            },


                   


            /* ======================================================= */
            /* ===        DISPLAY SETTINGS FUNCTIONS                == */
            /* ======================================================= */
            initTheme: function () {
                $._log('DISPLAY.initTheme');

                var theme = $._getCookie("appTheme");

                if (theme === undefined || theme === null || theme === 'color' || theme === 'default-min.css') {    //keep old cookie entry for backward compat. reasons
                    theme = 'color';
                } else {
                    theme = 'bw';
                }

                $.__DISPLAY.switchTheme(theme);
            },

            switchTheme: function (theme) {
                $._log('DISPLAY.switchTheme');

                $('.JS_theme_switcher').removeClass('selected');
                $('.JS_theme_switcher[themeName="' + theme + '"]').addClass('selected');

                if (theme === 'bw') {
                    $._addClass($._html,'bw');
                } else {
                    $._removeClass($._html,'bw');
                }

                $._setCookie('appTheme', theme, 365);

            },







            initFontsize: function () {
                $._log('DISPLAY.initFontSize');

                var fontsize = $._getCookie("appFontsize");

                if (fontsize === null || fontsize === undefined || fontsize === 'font_default' || fontsize === 'default-min.css') {    //keep old cookie entry for backward compat. reasons
                    fontsize = 'font_default';
                }

                $.__DISPLAY.switchFontsize(fontsize);

            },

            switchFontsize: function (fontsize) {
                $._log('DISPLAY.switchFontsize');

                $('.JS_fontsize_switcher').removeClass('selected');
                $('.JS_fontsize_switcher[fontsize="' + fontsize + '"]').addClass('selected');

                $._removeClass($._html,'font_default');
                $._removeClass($._html,'font_max');
                $._removeClass($._html,'font_min');
                $._addClass($._html,fontsize);

                $._setCookie('appFontsize', fontsize, 365);

            },








            getHdMarginWidth: function() {
                $._log('DISPLAY.getHdMarginWidth');

                var marginWidth = $._getCookie("appHdMarginWidth");

                if (marginWidth === null || marginWidth === undefined || (marginWidth !== '0' && marginWidth !== '10' && marginWidth !== '20' && marginWidth !== '30')) {
                    marginWidth = '30';
                }

                return marginWidth;
            },


            initHdMarginWidth: function () {
                $._log('DISPLAY.initHdMarginWidth');
                $.__DISPLAY.switchHdMarginWidth($.__DISPLAY.getHdMarginWidth());
            },

            switchHdMarginWidth: function (marginWidth) {
                $._log('DISPLAY.switchHdMarginWidth');

                $('.JS_hd_margin_switcher').removeClass('selected');
                $('.JS_hd_margin_switcher[marginWidth="' + marginWidth + '"]').addClass('selected');
                
                $.__DISPLAY.hdMarginWidth = marginWidth;

                $._setCookie('appHdMarginWidth', marginWidth, 365);

            }


        }







    });    
            
}(jQuery));










