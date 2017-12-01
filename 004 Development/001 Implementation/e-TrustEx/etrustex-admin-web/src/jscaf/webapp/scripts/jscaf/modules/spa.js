/*global syze:true,jQuery:true,$:true,_:true,megaHoverOver:true,megaHoverOut:true,require:true,setTimeout,window,document,navigator,screen,console,moment,p,self*/



/* ======================================================= */
/* ===                 JSCAF SPA MODULE                 == */
/* ======================================================= */


(function ($) {

    "use strict";

    $.extend({

        //DECLARATION
        //-----------
        __SPA: {

            /* ======================================================= */
            /* ===                NAVIGATION                        == */
            /* ======================================================= */

            bindEvents: function() {

                /*
                 $(window).bind('hashchange', function(){

                 $.__SPA.display({url:location.hash});
                 });
                 */

                $._$document.on('click', 'li.spa-nav', function () {
                    var $this = $(this),
                        data = $._extend({
                            isShowSelected:true
                        }, $this.data());

                    if ( data.isShowSelected ) {
                        $this.siblings().removeClass('selected');
                        $this.addClass('selected');
                    }

                    $.__SPA.display( data );
                });

            },

            displayNav: function() {

                $._AR({
                    id: 'spa-sidebar-wrapper',
                    call: 'pages/_nav/nav.html'
                });


            },


            display: function (data, fnCallback) {
                if ( data === null ) {
                    return;
                }

                if ( data.url !== undefined ) {
                    //setting the location hash
                    //var url = data.url.replace('#','');

                    //location.hash = url;

                    var url;

                    if ($._getPageUrl() === 'PROTOTYPE') {
                        url = 'pages' + data.url + '.html';
                    } else {
                        url = data.url.replace('/','');
                    }


                    var getPage = function() {
                        $._AR({
                            id: 'spa-page-container',
                            call: 'sub/' + url,
                            fnPostCall: function () {
                                var $page = $('#spa-page'),
                                    data = $page.data();

                                if (data.pageModule !== undefined) {
                                    $._require([data.pageModule], function() {
                                        if (window[data.pageModule].init !== undefined) {
                                            window[data.pageModule].init(); 
                                        }    
                                        if (window[data.pageModule].bindEvents !== undefined) {
                                            window[data.pageModule].bindEvents(); 
                                        }
                                    });
                                }

                                if (data.fnCallback !== undefined) {
                                    $._execFn(fnCallback);
                                }

                                //activating the element in nav sidbar
                                var $selectedNav = $('li.spa-nav[data-url="' + url + '"]');

                                $selectedNav.siblings().removeClass('selected');
                                $selectedNav.addClass('selected');

                            }
                        });
                    };


                    if ($('#spa-page').length) {
                        $('#spa-page').animate({opacity:0},
                            {
                                duration: 150,
                                easing: 'swing',
                                complete: function() { getPage(); },
                                queue: false
                            });
                    } else {
                        getPage();
                    }


                }

                if ( data.fnInit !== undefined ) {

                    $._execFn(data.fnInit);

                }

                if ( data.contentType === 'pdf' ) {

                    if (data.pdfFileName === undefined) {
                        $._log('====>ERROR : provide a data-pdf-file-name attribute for this PDF file to be loaded');
                        return;
                    }

                    $._openIframeDialog({
                        dialogId: 'pdfDialog',
                        dialogTitle: data.dialogTitle,
                        documentsArray: [{
                            documentFileName: data.pdfFileName,
                            documentUrl: $._settings.onlineHelpRootPath + "/pdf/" + data.pdfFileName
                        }],
                        iFrameScrolling: 'no'
                    });


                }
            }




        }








    });
}(jQuery));















