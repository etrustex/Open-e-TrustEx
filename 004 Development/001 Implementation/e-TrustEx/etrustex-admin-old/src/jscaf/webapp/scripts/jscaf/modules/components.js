/*global syze:true,jQuery:true,$:true,_:true,megaHoverOver:true,megaHoverOut:true,require:true,setTimeout,window,document,navigator,screen,console,moment,p,self*/



/* ======================================================= */
/* ===           JSCAF COMPONENTS MODULE                == */
/* ======================================================= */

(function ($) {

    "use strict";

    $.extend({


        //DECLARATION
        //-----------
        __COMPONENTS: {

            define: function() {

                $._log('COMPONENTS.define');

                //in case a JScomponents object is provided, it takes the lead over default declaration,
                //and only the components defined in the JScomponents object will be initialised
                if ($._settings.JScomponents !== null) {

                    $.__cmp = $.extend($.__cmpDefs, $._settings.JScomponents);

                    //if the JScomponents is null, then all the components are initialised, except when jSCAF is run in LIGHT mode
                } else {

                    //in light version, every component used on a page should be explicitely declared
                    if ( !$._settings.isLightInitialisationActive ) {

                        var prop;

                        //in case the JScomponents is null, all components are loaded by default
                        for (prop in $.__cmpDefs) {
                            if ($.__cmpDefs.hasOwnProperty(prop)) {
                                $.__cmpDefs[prop] = true;
                            }
                        }

                        prop = null;
                    }

                    $.__cmp = $.__cmpDefs;
                }

            },


            bindEvents: function() {
                $._log('COMPONENTS.bindEvents');

                // global events
                // -------------

                //DISABLE ENTER KEY ON FORM AUTO-SUBMIT
                //-------------------------------------

                if ( $._settings.isDisableEnterKeyOnForm ) {

                    $._$document.on('keypress',
                        function (event) {
                            var key = $._getKey(event);

                            if (key === 13) { //enter
                                if ($('#' + event.target.id).hasClass('JS_enterkey_enabled')) {
                                    return true;
                                } else {
                                    return (event.target.nodeName === 'TEXTAREA');
                                }
                            }
                            return true;
                        }
                    );

                }


                //expandable events
                $._$document.on('click', '.JS_expandable', function () {
                    var $this = $(this),
                        $content = $this.siblings().filter('.content');


                    if ($content.length) {
                        var $expandButton = $this.find('.expand-button');
                        if ($expandButton.length) {
                            if ($content.hasClass('hidden')) {
                                $expandButton.removeClass('expanded').addClass('collapsed');
                            } else {
                                $expandButton.removeClass('collapsed').addClass('expanded');
                            }
                        }
                        $content.toggleClass('hidden');

                    } else {
                        $this.next().toggleClass('hidden');
                    }
                });

                $._$document
                    .on('click', '.JS_toggle-content, .toggle-trigger-content',
                    function () {
                        var $this = $(this);

                        $this.toggleClass("active");

                        if ($this.hasClass('JS_toggle-sub-row')) {
                            $this.closest('tr').next().toggleClass('hidden');
                        } else {
                            $('#content_' + $this.attr('id')).toggleClass('hidden');
                        }
                    }
                );


                if ($.__modules.VALIDATION) {
                    $.__VALIDATION.initValidationEvents();
                }



                //APPROVE TOGGLE BUTTON
                //---------------------
                if ($.__cmp.JSapproveToggle) {

                    $._$document
                        .on('click', 'span.JS_approve-toggle',
                        function () {
                            $._log('COMMON.JS_approve-toggle.CLICK');

                            var $this = $(this),
                                isDisabled = $this.hasClass('approve-toggle-disabled'),
                                $associatedContent = $('#' + $this.attr('id') + '_content'),
                                associatedPropertyId = $this.attr('associatedPropertyId'),
                                $associatedProperty = $('#' + associatedPropertyId),
                                associatedValueTrue = 'Y',
                                associatedValueFalse = 'N',
                                enableTableRowUpdate = $this.attr('enableTableRowUpdate');

                            if (isDisabled) {
                                $this.removeClass('approve-toggle-disabled').addClass('approve-toggle-enabled');
                            } else {
                                $this.removeClass('approve-toggle-enabled').addClass('approve-toggle-disabled');
                            }

                            if ($this.hasClass('JS_live-validation')) {
                                $._isFieldValid($this, null, isDisabled);
                            }

                            if ($this.attr('associatedValueTrue') === 'true') {
                                associatedValueTrue = 'true';
                                associatedValueFalse = 'false';
                            }

                            if ($associatedProperty.length) {
                                if (!isDisabled) {
                                    $associatedProperty.val(associatedValueFalse);
                                } else {
                                    $associatedProperty.val(associatedValueTrue);
                                }
                            }

                            if ($associatedContent.length) {
                                if (!isDisabled) {
                                    $associatedContent.addClass('hidden');
                                } else {
                                    $associatedContent.removeClass('hidden');
                                }
                            }

                            if (enableTableRowUpdate === undefined) {
                                enableTableRowUpdate = true;
                            }

                            if (enableTableRowUpdate) {

                                if (isDisabled) {
                                    $this.closest('tr').find('.note-error').removeClass('note-error').addClass('note-success').find('p').removeClass('text-color-red');
                                    $this.closest('tr').find("td:nth-child(2)").removeClass('bgcolor-red');
                                } else {
                                    $this.closest('tr').find('.note-success').removeClass('note-success').addClass('note-error').find('p').addClass('text-color-red');
                                    $this.closest('tr').find("td:nth-child(2)").addClass('bgcolor-red');
                                }

                            }

                        }
                    );
                }

                //BUTTON SET => hidden field val + associated content
                //---------------------------------------------------
                if ($.__cmp.JSbuttonSet) {

                    $._$document
                        .on('change', 'span.JS_buttonSet',
                        function () {
                            $._log('COMMON.JS_buttonSet.CHANGE');
                            var $this = $(this),
                                id = $this.attr('id'),
                                associatedPropertyId = $this.attr('associatedPropertyId'),
                                $associatedProperty = $('#' + associatedPropertyId),
                                checkedValue = $('[name="' + id + '"]:checked').attr('value'),
                                $associatedContent = $('#' + id + '_content');

                            if ($associatedProperty.length) {
                                $associatedProperty.val(checkedValue);
                            }

                            if ($associatedContent.length) {
                                var associatedContentDisplayValue = $this.attr('associatedContentDisplayValue');

                                if (associatedContentDisplayValue === '' || associatedContentDisplayValue === undefined) {
                                    if (checkedValue.toUpperCase() === 'TRUE' || checkedValue.toUpperCase() === 'Y') {
                                        $associatedContent.removeClass('hidden');
                                    } else {
                                        $associatedContent.addClass('hidden');
                                    }
                                } else {
                                    if (checkedValue === associatedContentDisplayValue) {
                                        $associatedContent.removeClass('hidden');
                                    } else {
                                        $associatedContent.addClass('hidden');
                                    }
                                }
                            } else {

                                var associatedContentDisplayBasedOnValue = $this.attr('associatedContentDisplayBasedOnValue');

                                if (associatedContentDisplayBasedOnValue !== '' && associatedContentDisplayBasedOnValue !== undefined) {
                                    if (associatedContentDisplayBasedOnValue.toUpperCase() === 'TRUE') {
                                        $('.' + id).addClass('hidden');
                                        $('#' + id + '_content_' + checkedValue).removeClass('hidden');
                                    }
                                }
                            }

                            //for blur for JS_live-validation trigger
                            if ($this.hasClass('JS_live-validation')) {
                                $._isFieldValid($this);
                            }
                        }
                    );
                }

                if ($.__cmp.JSfieldNumber) {

                    $._$document
                        .on('keypress', 'input.JS_field-number',
                        function (event) {
                            var key;
                            if (window.event) {
                                key = window.event.keyCode; //IE
                            } else {
                                key = event.which;
                            }

                            if (key > 31 && (key < 48 || key > 57)) {
                                return (key === 46 || key === 45 || key === 44); // decimal point & minus
                            }
                            return true;
                        }
                    );

                    $._$document
                        .on('blur', 'input.JS_field-number',
                        function () {
                            var $this = $(this),
                                value = $this.val();

                            if (value !== '') {
                                $this.val(parseFloat(value).toFixed(2));
                            }
                        }
                    );

                    $._$document
                        .on('keyup', 'input.JS_field-number',
                        function (event) {
                            var key;
                            if (window.event) {
                                key = window.event.keyCode; //IE
                            } else {
                                key = event.which;
                            }

                            //Convert "," (188 main keyboard, 110 numeric keyboard) by "."
                            if ( (key === 188) || (key === 110) ) {
                                var $this = $(this);
                                $this.val($this.val().replace(/[,]/g,"."));
                                return false;
                            }
                            return true;
                        }

                    );


                    $._$document
                        .on('keypress', 'input.JS_field-number0to9',
                        function (event) {
                            var key;
                            if (window.event) {
                                key = window.event.keyCode; //IE
                            } else {
                                key = event.which;
                            }
                            return !((key > 31 && key < 48) || key > 57);
                        }
                    );

                    //JS_field-number and JS_field-number0to9 auto select content
                    //-----------------------------------------------------------
                    $._$document
                        .on('focus', 'input.JS_field-number, input.JS_field-number0to9',
                        function () {
                            var t = $(this);

                            $._execFn(function () {
                                t.select();
                            }, true);
                        }
                    );
                }

                //JS_datepicker and JS_datetimepicker double click = today event
                //--------------------------------------------------------------
                if ($.__cmp.JSdatepicker) {

                    $._$document
                        .on('dblclick', 'input.JS_datepicker',
                        function () {
                            $(this).val(moment().format('DD/MM/YYYY'));
                        }
                    );


                    $._$document
                        .on('dblclick', 'input.JS_datetimepicker_d',
                        function () {
                            var d = moment(),
                                $this = $(this),
                                mainInputId = $this.attr('mainInputId'),
                                $d = $('#' + mainInputId + '_d'),
                                $h = $('#' + mainInputId + '_h'),
                                $m = $('#' + mainInputId + '_m');
                                $s = $('#' + mainInputId + '_s');

                            $d.val(d.format('DD/MM/YYYY'));
                            $h.val(d.format('HH'));
                            $m.val(d.format('mm'));
                            $s.val(d.format('ss'));

                            $('#' + mainInputId).val(d.format('DD/MM/YYYY HH:mm'));

                            if ($this.hasClass('JS_dateonly')) {
                                $h.val('00');
                                $m.val('00');
                                $s.val('00');
                            }

                        }
                    );

                    $._$document
                        .on('blur', 'input.JS_datetimepicker_d',
                        function () {
                            $._log('COMMON.JS_datetimepicker_d.BLUR');
                            var $this = $(this),
                                mainInputId = $this.attr('mainInputId');

                            $('#'+mainInputId).val($this.val());
                            if ($('#'+mainInputId).val() === '__/__/____') {
                                $('#'+mainInputId).val('');
                            }
                            if ($.__modules.VALIDATION) {
                                $.__VALIDATION.validateDatetimepicker($('#' + mainInputId));
                            }
                        }
                    );


                    $._$document
                        .on('blur', 'input.JS_datetimepicker_h',
                        function () {
                            $._log('COMMON.JS_datetimepicker_h.BLUR');
                            if ($.__modules.VALIDATION) {
                                $.__VALIDATION.validateDatetimepicker($('#' + $(this).attr('mainInputId')));
                            }
                        }
                    );

                    $._$document
                        .on('keyup', 'input.JS_datetimepicker_h',
                        function (event) {
                            var $this = $(this);

                            if (!$this.hasClass('JS_no-minutes-autofocus')) {

                                var key;
                                if (window.event) {
                                    key = window.event.keyCode; //IE
                                } else {
                                    key = event.which;
                                }

                                //this prevent to go directly to the minutes when using tab on the date
                                if (key !== 9 && key !== 16) {
                                    //focus directly on minutes when the hours are filled
                                    if ($this.val().split("_").length - 1 === 0) {
                                        $('#' + $this.attr('mainInputId') + '_m').focus();
                                    }
                                }

                            }
                        }
                    );


                    $._$document
                        .on('blur', 'input.JS_datetimepicker_s',
                        function () {
                            $._log('COMMON.JS_datetimepicker_s.BLUR');
                            if ($.__modules.VALIDATION) {
                                $.__VALIDATION.validateDatetimepicker($('#' + $(this).attr('mainInputId')));
                            }
                        }
                    );
                }

                //SLIDER KEEP FIXED ON SCROLL
                //---------------------------
                if ($.__cmp.JSslider) {

                    $(window).scroll(function () {
                        var $sliderTab = $('#slider_tab');
                        if ($sliderTab.length) {
                            $sliderTab.find('ul').css({'padding-top': $(window).height() / 4 + $(window).scrollTop() + 'px'});
                            $('#slider_content:not(.fixed)').css({'margin-top': $(window).scrollTop() + 'px'});
                        }
                    });
                }

                //JS_radio with content displayed on click
                //----------------------------------------
                if ($.__cmp.JSradio) {

                    $._$document.on('click', '.JS_radio', function () {
                        $('.JS_radio-content').addClass('hidden');
                        $('#' + this.id + '_content').removeClass('hidden');
                    });
                }

                //JS_checkbox with content displayed on click
                //----------------------------------------
                if ($.__cmp.JScheckbox) {

                    $._$document.on('click', '.JS_checkbox', function () {
                        if ($(this).is(':checked')) {
                            $('#' + this.id + '_content').removeClass('hidden');
                        } else {
                            $('#' + this.id + '_content').addClass('hidden');
                        }
                    });
                }

                //JS_autocomplete, prevent ajax blocker + initDisplay when remote ajax calls are done
                //-----------------------------------------------------------------------------------
                if ($.__cmp.JSautocomplete) {

                    $._$document.on('keydown', '.JS_autocomplete', function () {
                        $._isBlockerPage = false;
                        $._settings.isInitDisplayOnAjaxStop = false;
                    });

                    $._$document.on('keypress', '.JS_autocomplete', function () {
                        $._isBlockerPage = false;
                        $._settings.isInitDisplayOnAjaxStop = false;
                    });

                    $._$document.on('keyup', '.JS_autocomplete', function () {
                        var $this = $(this);
                        if ($this.val() === '' && $this.data().hasResultElement) {
                            var $result = $('#' + $this.attr('id') + '_result');
                            if ($result.get(0).nodeName === 'INPUT') {
                                $result.val('');
                            } else {
                                $result.html('');
                            }
                        }
                    });
                }

                //STAR RATING force blur of parent element for triggering live-validation
                //-----------------------------------------------------------------------
                if ($.__cmp.JSstars) {

                    $._$document.on('click',
                        '.ui-stars-star',
                        function () {
                            $(this).parent().blur();
                        });
                }

                //TOPNAV SELECT NAVIGATION FOR LOW-RESOLUTION DEVICES
                //---------------------------------------------------
                if ($.__cmp.JStopNav) {

                    $._$document.on('click',
                        '#topnav-menu',
                        function () {
                            $('#topnav-menu-content').toggle();
                        });
                    $._$document.on('click',
                        '#topnav-menu-content li',
                        function () {
                            var url = $(this).attr('linkUrl');
                            if (url !== 'undefined') {
                                window.location = url;
                            }
                        });
                }

                if ($.__cmp.JStopBar) {

                    $._$document.on('click',
                        '#topbar-menu',
                        function () {
                            $('#topbar-menu-content').slideToggle();
                        });
                    $._$document.on('click',
                        '#topbar-menu-content li',
                        function () {
                            var url = $(this).attr('linkUrl');
                            if (url !== 'undefined') {
                                window.location = url;
                            }
                        });
                }


                //HELP DIALOGS AND VIDEO CONTAINER
                //--------------------------------
                if ($.__cmp.JSopenDialogContent) {

                    $._$document
                        .on('click', '.JS_open-dialogContent',
                        function () {
                            var $this = $(this),
                                dialogContentId = this.id + '_dialogContent',
                                data = $.extend({
                                    contentId: dialogContentId,
                                    width:600,
                                    isEmptyOnClose:false,
                                    title: $this.attr('dialogTitle') //to ensure backward comp.
                                }, $this.data()),

                            //clone the dialogContent as it's always detroyed on save (since jSCAF 1.7)
                                $cloneDialogContent = $("#" + dialogContentId).clone();

                            $._openDialog({
                                dialogId: data.contentId,
                                isCloseOnEscape: true,
                                dialogWidth: data.width,
                                dialogTitle: data.title,
                                isEmptyOnClose:data.isEmptyOnClose,
                                isCenterAfterCreate: true,
                                fnDialogClose: function() {
                                    //put back the previously destroyed dialog content
                                    $this.after($cloneDialogContent);
                                }
                            });
                        }
                    );
                }



                if ($.__cmp.JSopenPdfDialog) {
                    $._$document
                        .on('click', '.JS_open-pdf-dialog',
                        function () {

                            var $this = $(this),
                                $data = $this.data(),
                                dialogId,
                                dialogTitle,
                                documentsArray = [],
                                i,
                                pdfFileNames,
                                pdfFileNamesPath,
                                hasHeaderAction = false,
                                headerActionUrl = null;

                            //creating the dialogId
                            dialogId = this.id + '_dialogContent';

                            //getting the fileNames array
                            if ($data.dialogPdfFilenames !== undefined) {
                                pdfFileNames = $data.dialogPdfFilenames;
                            } else {
                                pdfFileNames = $this.attr('pdfFileNames');
                            }

                            pdfFileNames = pdfFileNames.split(',');

                            //getting the path of the file
                            if ($data.dialogPdfFilenamesPath !== undefined) {
                                pdfFileNamesPath = $data.dialogPdfFilenamesPath;
                            } else {
                                pdfFileNamesPath = $._getContextPath() + $._settings.onlineHelpRootPath + "/pdf/";
                            }


                            //getting the title
                            if ($data.dialogTitle !== undefined) {
                                dialogTitle = $data.dialogTitle;
                            } else {
                                dialogTitle = $this.attr('dialogTitle');
                            }

                            //adding path to the files
                            for (i = 0; i < pdfFileNames.length; i++) {
                                documentsArray.push({
                                    documentFileName: pdfFileNames[i],
                                    documentUrl: pdfFileNamesPath + pdfFileNames[i]
                                });
                            }


                            if (pdfFileNames.length === 1 && $data.showDownloadLink) {
                                // Single document allow download
                                hasHeaderAction = true;
                                headerActionUrl = pdfFileNamesPath + pdfFileNames[0];
                            }

                            $._openIframeDialog({
                                dialogId: dialogId,
                                dialogTitle: dialogTitle,
                                documentsArray: documentsArray,
                                iFrameScrolling: 'no',
                                hasHeaderAction: hasHeaderAction,
                                headerActionUrl: headerActionUrl,
                                fnDialogClose: function() {
                                    $._closeDialog({dialogId: dialogId});
                                }
                            });

                        }
                    );

                }

                if ($.__cmp.JSopenVideoDialog) {

                    $._$document
                        .on('click', '.JS_open-video-dialog',
                        function () {
                            var $this = $(this);
                            var videoFileName = $._getContextPath() + $._settings.onlineHelpRootPath + "/videos/" + $this.attr('videoFileName') + '.m4v';

                            if ($._isIE7() || $._isIE8()) {

                                $._openIframeDialog({
                                    dialogId: 'testdialog',
                                    dialogTitle: 'HELP TUTORIAL : ' + $this.attr('videoFileName'),
                                    dialogWidth: 820,
                                    dialogHeight: 600,
                                    documentsArray: [
                                        {documentUrl: $._getContextPath() + $._settings.onlineHelpRootPath + '/videos/' + $this.attr('videoFileName') + '.html'}
                                    ],
                                    hasHeaderAction: true,
                                    headerActionUrl: videoFileName
                                });

                            } else {

                                var videoContent = '<video id="videoPlayer" class="video-js vjs-default-skin" controls autoplay preload="none" data-setup="{}">';
                                videoContent += '<source src="' + videoFileName + '" type="video/mp4" />';
                                videoContent += '</video>';

                                $._$body.append(videoContent);

                                $._openDialog({
                                    dialogId: 'videoPlayer',
                                    dialogTitle: 'VIDEO TUTORIAL : ' + $this.attr('videoFileName'),
                                    dialogWidth: 840,
                                    hasHeaderAction: true,
                                    headerActionUrl: videoFileName,
                                    fnAfterCreatePostCall: function () {
                                        $('#videoPlayer').css({'width': '800px'});
                                        $._centerDialog();
                                    },
                                    fnDialogClose: function () {
                                        $('#videoPlayer').remove();
                                    }
                                });

                            }
                        }
                    );
                }


                //TABLE SORTER on DEFAULT TABLE
                //-----------------------------
                if ($.__cmp.JStableSorter) {

                    $._$document
                        .on('sortEnd', 'table.default.tablesorter-table',
                        function () {
                            $.__COMPONENTS.initDefaultTable();
                        }
                    );

                }

                //TABLESORTER SIMULATION WITH SERVER-SIDE SORTING
                //------------------------------------------------
                if ($.__cmp.JStableSorterServer) {

                    $._$document
                        .on('click', 'table.tablesorter-table-server th',
                        function () {
                            var $this = $(this);
                            var tableId = $this.closest('table').attr('id');
                            var $table = $('#' + tableId);
                            //todo migrate those attributes into data- attributes
                            var $data = $this.data();
                            var $tableData = $table.data();
                            var sortedColumn = $this.attr('sortedColumn');
                            var sortDispatchValue = $table.attr('sortDispatchValue');
                            var sortAction = $table.attr('sortAction');
                            var associatedFragmentId = $table.attr('associatedFragmentId');
                            var fnPostCall = $table.attr('postCall');
                            var $sortOrder = $('#sortOrder');

                            if (sortedColumn === undefined) {
                                sortedColumn = $data.sortedColumn;
                            }
                            if (sortDispatchValue === undefined)  {
                                if ($tableData.sortDispatchValue !== undefined) {
                                    sortDispatchValue = $tableData.sortDispatchValue;
                                } else {
                                    sortDispatchValue = $tableData.sortCall;
                                }
                            }
                            if (sortAction === undefined) {
                                sortAction = $tableData.sortAction;
                            }
                            if (associatedFragmentId === undefined) {
                                associatedFragmentId = $tableData.associatedFragmentId;
                            }
                            if (fnPostCall === undefined) {
                                fnPostCall = $tableData.fnPostCall;
                            }


                            $._log('tablesorter-table-server th.CLICK => tableId=' + tableId + ' - sortedColumn=' + sortedColumn + ' - sortAction:' + sortAction + ' - associatedFragmentId=' + associatedFragmentId);

                            //displaying the header icon

                            if ($this.hasClass('sortable')) {
                                if ($this.hasClass('sortDown') || $this.hasClass('sortUp')) {
                                    if ($this.hasClass('sortDown')) {
                                        $sortOrder.val('ASC');
                                    }
                                    if ($this.hasClass('sortUp')) {
                                        $sortOrder.val('DESC');
                                    }
                                } else {
                                    $sortOrder.val('ASC');
                                }

                                //setting sortField
                                $('#sortedColumn').val(sortedColumn);


                                //refreshing the list according to the sorted options
                                if ($.__modules.AJAX) {
                                    $.__AJAX.ajax({
                                        id: associatedFragmentId,
                                        call: sortDispatchValue,
                                        action: sortAction,
                                        fnPostCall: function () {
                                            var $table = $('#'+tableId);

                                            $table.find('th.sortable').removeClass('sortDown').removeClass('sortUp');

                                            if ($('#sortOrder').val() === 'ASC') {
                                                $table.find('th.sortable[sortedColumn=' + sortedColumn + ']').addClass('sortUp');
                                                $table.find('th.sortable[data-sorted-column=' + sortedColumn + ']').addClass('sortUp');
                                            } else {
                                                $table.find('th.sortable[sortedColumn=' + sortedColumn + ']').addClass('sortDown');
                                                $table.find('th.sortable[data-sorted-column=' + sortedColumn + ']').addClass('sortDown');
                                            }

                                            if (fnPostCall !== undefined) {
                                                $._execFn(fnPostCall, false);
                                            }
                                        }
                                    });
                                }
                            }

                        });
                }


                // LIST COUNTER MENU
                // -----------------
                if ($.__cmp.JSlistCounterSmall) {

                    $._$document
                        .on('click', 'ul.list-counter-small li.parent',
                        function () {
                            var $this = $(this),
                                $ul = $this.parent(),
                                id = this.id;

                            if ($this.hasClass('empty') || $this.hasClass('selected')) {
                                return false;
                            }

                            $this.addClass('selected').siblings().removeClass('selected');

                            if ($ul.hasClass('JS_always-expanded')) {

                                $ul.find('li.child').removeClass('selected');

                            } else {

                                $('ul.list-counter-small span.child-wrapper').each(function () {
                                    var $this = $(this);

                                    if (this.id !== (id + '_children')) {

                                        if (!$this.hasClass('always-visible')) {
                                            $this.addClass('hidden');
                                        }

                                    }
                                });

                            }


                            var child = $('#' + id + '_children');

                            if (child.length) {
                                if (!child.find('li.child.selected').length) {
                                    if (child.hasClass('hidden')) {
                                        if (!$this.hasClass('unselectable')) {
                                            $this.addClass('selected');
                                        }
                                        child.removeClass('hidden');
                                    } else {
                                        if (!$ul.hasClass('JS_always-expanded')) {
                                            $this.removeClass('selected');
                                            child.addClass('hidden');
                                        }
                                    }
                                } else {
                                    child.find('li.child.selected').removeClass('selected');
                                    if (!$this.hasClass('unselectable')) {
                                        $this.addClass('selected');
                                    }
                                    child.removeClass('hidden');
                                }

                                if ($this.hasClass('unselectable')) {
                                    var $firstChild = child.find('li.child').eq(0);
                                    if ($this.hasClass('autoselect-first-child')) {
                                        $firstChild.addClass('selected').click();
                                    }
                                }
                            } else {
                                $this.addClass('selected');
                            }

                            if ($this.hasClass('selected') && $this.closest('ul').hasClass('with-arrow')) {
                                if (!$this.find('.arrow').length) {
                                    $this.append('<span class="arrow"></span>');
                                }
                            }

                            return true;
                        });

                    $._$document
                        .on('click', 'ul.list-counter-small li.child',
                        function () {
                            var $this = $(this),
                                $childWrapper = $this.closest('.child-wrapper'),
                                $ul = $this.closest('ul');

                            if ($this.hasClass('empty')) {
                                return;
                            }

                            if ($ul.hasClass('JS_always-expanded')) {
                                $ul.find('li.child').removeClass('selected');
                                $ul.find('li.parent').siblings().removeClass('selected');
                            }

                            $childWrapper.prev().removeClass('selected');
                            $childWrapper.find('li').removeClass('selected');
                            $this.addClass('selected');

                        });

                }


                //RADIO-LIST
                //----------
                if ($.__cmp.JSradioList) {

                    $._$document
                        .on('click', 'li.radio-list',
                        function () {
                            var $this = $(this);
                            $this.siblings().removeClass('selected');
                            $this.addClass('selected');
                        });

                }


                // COUNTER-BOX-BIG SELECTABLE
                // --------------------------
                if ($.__cmp.JScounterBox) {

                    $._$document
                        .on('click', '.counter-box-big-wrapper.selectable',
                        function () {
                            $('.counter-box-big-wrapper.selectable').removeClass('selected');
                            $(this).addClass('selected');
                        });
                }

                // PAGINATION EVENT
                // ----------------
                if ($.__cmp.JSpagination) {

                    $._$document
                        .on('click', '.action_pagination_goto_page',
                        function () {
                            var $this = $(this),
                                fragmentId = $this.closest('.pagination-wrapper').attr('id');

                            if ($.__modules.AJAX) {
                                $.__AJAX.ajax({
                                    id: fragmentId,
                                    call: 'pgGotoPage',
                                    action: fragmentId,
                                    fnPreCall: function() {
                                        $._setParamValue('pgSelectedPageIndex', $this.attr('pageIndex'));
                                    },
                                    fnPostCall: function () {
                                        $('html, body').animate({scrollTop: 0});
                                    }
                                });
                            }
                        });
                }

                if ($.__cmp.JStooltip) {

                    $._$document
                        .on('click', '.JS_tooltip',
                        function () {
                            $(this).poshytip('show').addClass('tooltip-active');
                        });


                    $._$document
                        .on('click', '.JS_tooltip-content-close',
                        function () {
                            var caller = $('#' + $(this).attr('data-linked-tooltip-id'));

                            //maintain backward comp.
                            if (!caller.length) {
                                caller = $('#' + $(this).attr('linkedtooltipid'));
                            }
                            if (caller.length) {
                                caller.poshytip('hide');
                                caller.removeClass('tooltip-active');
                            } else {
                                $('.tip-default').remove();
                                $('.tip-black').remove();
                                $('.tip-white').remove();
                            }
                        });
                }


                //TABLE FILTER EVENT
                //------------------
                if ($.__cmp.JStableFilter) {

                    $.expr[':'].containsIgnoreCase = function (n, i, m) {
                        return jQuery(n).text().toUpperCase().indexOf(m[3].toUpperCase()) >= 0;
                    };

                    $._$document.on('click', 'input.table-filter', function(e) {
                       e.stopPropagation();
                    });

                    $._$document.on('keyup',
                        'input.table-filter',
                        function () {
                            var tableIdToFilter =$(this).attr('tableIdToFilter'),
                                tableBody,
                                data = this.value,
                                jo, i, $jo;

                            if (tableIdToFilter==='*') {
                                tableBody = $('table').find('tbody');

                                if (data === '') {
                                    $('table').find('thead').show();
                                    $('.section-title-bullet').show();
                                    $('br').show();
                                } else {
                                    $('table').find('thead').hide();
                                    $('.section-title-bullet').hide();
                                    $('br').hide();
                                }

                            } else {
                                tableBody = $($('#' + tableIdToFilter).find('tbody')[0]);
                            }

                            tableBody.find('tr').hide().addClass('bg bgcolor-red');
                            jo = tableBody.find('tr');
                            for (i=0; i<jo.length; ++i) {
                                $jo = $(jo[i]);
                                if ($jo.find('*:containsIgnoreCase("'+data+'")').length) {
                                    $jo.show();
                                    if ($jo.hasClass('sub-row')) {
                                        $(jo[i-1]).show();
                                    } else {
                                        $jo = $(jo[i + 1]);
                                        if ($jo.hasClass('sub-row')) {
                                            $jo.show();
                                            ++i;
                                        }
                                    }
                                }
                            }
                        });


                    $._$document.on('click','.action_reset_table_filter',
                        function(e) {
                            var associatedFilterId= $(this).data().filterId;
                            $('#' + associatedFilterId).val('').keyup();
                            e.stopPropagation();
                        });


                }

                //INPUT FILE UPLOAD EVENT
                //-----------------------

                if ($.__cmp.JSfileInput) {

                    $._$document.on('change',
                        'input.file-input',
                        function () {
                            var value = $(this).val();
                            $(this).next().text(value.substr(value.lastIndexOf('\\') + 1, value.length));
                            $(this).next().addClass('value');
                        });
                }

                //HELP CONTENT TRIGGER
                //--------------------
                if ($.__cmp.JShelp) {

                    $._$document.on('focus',
                        'input.JS_help,select.JS_help',
                        function () {
                            var $this = $(this);

                            if ($._isInlineHelpActive) {
                                $this.poshytip({
                                    content: $('#' + $this.attr('id') + '_help').html(),
                                    showOn: 'focus',
                                    className: 'tip-black',
                                    offsetX: 10,
                                    allowTipHover: false,
                                    slide: false,
                                    alignTo: 'target',
                                    alignX: 'right',
                                    alignY: 'center'
                                });
                            } else {
                                $this.poshytip('destroy');
                            }
                        });
                }


                //TOGGLE BUTTON
                //-------------
                if ($.__cmp.JStoggleButton) {

                    $._$document.on('click',
                        'a.toggle-button',
                        function (e) {
                            var $this = $(this);
                            var $icon = $this.find('.icon');

                            $this.toggleClass('active');

                            if ($this.hasClass('active')) {
                                $icon.addClass('on').removeClass('off');
                            } else {
                                $icon.addClass('off').removeClass('on');
                            }

                            e.stopPropagation();
                        });
                }


                //RADIO BUTTONS : click on label = check the radio button
                //-------------------------------------------------------
                if ($.__cmp.JSradio) {
                    $._$document.on('click', '.radio-group > label', function () {
                        var $this = $(this),
                            data = $this.data();

                        if ( $this.prev().attr('disabled') !== 'disabled') {
                            $this.parent().find('input').prop('checked', false).removeClass('checked');
                            $this.prev().prop('checked', true).attr('checked', 'checked').addClass('checked');
                        }
                        if (data.associatedInputId !== undefined) {
                            $('#'+data.associatedInputId).val($this.find('input').val());
                        }
                    });
                }

                // ACCORDION BOX
                // -------------
                if ($.__cmp.JSaccordionBox) {
                    $._$document.on('click','.JS_accordion_box_auto_triggered',function() {
                        $._accordionPanelSwitch($(this).closest('.accordion-box').attr('id'));
                    });
                }


                // LISTS TRANSFER
                // --------------
                if ($.__cmp.JSlistsTransfer) {

                    $._$document.on('click','ul.JS_lists-transfer li', function(){
                        $(this).toggleClass('active');
                    });


                    $._$document.on('click','.JS_lists-transfer-actions button', function() {

                        var $this = $(this),
                            transferType = $this.data().transferType,
                            $lists = $('.JS_lists-transfer-actions').parent().find('ul.JS_lists-transfer'),
                            $listLeft = $($lists[0]),
                            $listRight = $($lists[1]);

                        if (transferType === 'LR_ALL') {
                            $listRight.append($listLeft.find('li'));
                        }
                        if (transferType === 'RL_ALL') {
                            $listLeft.append($listRight.find('li'));
                        }
                        if (transferType === 'LR_SELECTED') {
                            $listRight.append($listLeft.find('li.active').removeClass('active'));
                        }
                        if (transferType === 'RL_SELECTED') {
                            $listLeft.append($listRight.find('li.active').removeClass('active'));
                        }

                        return false;
                    });

                }


                // VIEW DOCUMENT
                // -------------
                if ($.__cmp.JSviewDocument) {

                    $._$document.on('click','.JS_view-document', function() {
                        var data = $(this).data();

                        $._openDocumentDialog({
                            documentId: data.id,
                            documentDescription: data.description,
                            documentFilename: data.filename,
                            documentUrl: data.url,
                            documentContentType: data.contentType
                        });

                    });

                }




                // initialising flat theme events
                if ($._settings.isFlatThemeActive) {

                    // Display/Hide Dashboard
                    // Handle dashboard height according  to window size

                    $._$document.on( 'click', '#toggle-tasks', function( event ) {
                        event.stopPropagation();
                        $( this ).toggleClass( 'open' );
                        $( '#header-tasks' ).toggle();
                        $( '.tasks-outer' ).css({ 'height': $( window ).height() - $( '#header' ).height() + 'px'});
                    });

                    $._$document.on( 'click', '#toggle-dashboard', function( event ) {
                        event.stopPropagation();
                        $( this ).toggleClass( 'open' );
                        $( '#header-page-dashboard' ).toggle(function(){
                            $(this).find('.JS_focus').focus();
                        });
                        $.__COMPONENTS.iaResizeDashboard();
                    });

                    //Call the resize dashboard if the window is resized
                    $( window ).on( 'resize', $.__COMPONENTS.iaResizeDashboard );

                    // Hide Dashboard when the user clicks outside of it
                    $._$document.on( 'click touchstart', 'html', function( event ) {
                        var $headerPageDashboard = $( '#header-page-dashboard' );
                        if ( $headerPageDashboard.is( ':visible' ) ) {
                            $headerPageDashboard.hide();
                            $( '#toggle-dashboard' ).removeClass( 'open' );
                        }
                    });

                    $._$document.on( 'click touchstart', '#header-page-dashboard', function( event ) {
                        event.stopPropagation();
                    });

                    // Dashboard Filtering: Display results list on first character entered into the search box (hides tabbed interface)
                    var dashboardVisible = false;
                    $._$document.on( 'keyup', '#dashboard-filter-input', function() {
                        var filterInputContent = $( '#dashboard-filter-input' ).val();

                        if ( filterInputContent !== '' && dashboardVisible !== true ) {
                            dashboardVisible = true;
                            $( '#dashboard-tabs' ).hide();
                            $( '#dashboard-filter-results' ).show();
                        } else if ( filterInputContent === '' && dashboardVisible === true ) {
                            dashboardVisible = false;
                            $( '#dashboard-tabs' ).show();
                            $( '#dashboard-filter-results' ).hide();
                        }
                    });

                    // Dashboard Filtering: Display tabbed interface when the user cancels the search (hides results list)
                    $._$document.on( 'click', '#dashboard-filter-cancel', function() {
                        dashboardVisible = false;
                        $( '#dashboard-filter-input' ).val('');
                        $( '#dashboard-tabs' ).show();
                        $( '#dashboard-filter-results' ).hide();
                    });


                    // Dashboard Filtering: Filter content
                    $._$document.on( 'keyup', '#dashboard-filter-input', function() {
                        // Filter the items
                        var filter = $(this).val();

                        $( '#dashboard-filter-results .item' ).each( function() {
                            var $this = $(this);

                            if ( $this.text().search( new RegExp( filter, 'i' ) ) < 0 ) {
                                $this.fadeOut( { duration: 140 });
                            } else {
                                $this.show();
                            }
                        } );
                    });


                }



            },



            iaResizeDashboard: function() {
                $( '.dashboard-outer' ).css({ 'height': $( window ).height() - $( '#header' ).height() + 'px'});
            },


            initDisplay: function() {

                $._log('COMPONENTS.initDisplay');

                if ($._settings.isPageZoomWarningEnabled && $.__modules.BROWSER) {
                    $.__BROWSER.checkPageZoomWarning();
                }

                //checking browser version (for warning - not blocking)
                if ($._settings.isBrowserWarningEnabled && $.__modules.BROWSER) {
                    $.__BROWSER.checkBrowserWarning();
                }

                //validation box field alignment
                $('.field > .validation.box').each(function () {
                    var $this = $(this);
                    $this.css({'margin-left': $this.parent().find('.field-label').width() + 8});
                    $this = null;
                });

                //dynamically center a block against its parent container
                $('.JS_center').each(function () {
                    var $this = $(this);
                    var marginLeft = (($this.closest('.content').width() - $this.width()) / 2) - 10;
                    if (marginLeft > 0) {
                        $this.css({'margin-left': marginLeft + 'px'});
                    }
                    $this = null;
                });

                //in case of a content slider is present, force it to be fixed positioned although floating
                if ($.__cmp.JSslider) {
                    var $sliderTab = $('#slider_tab');
                    if ($sliderTab.length) {
                        $('#slider_tab').find('ul').css({'padding-top': $(window).height() / 4 + $(window).scrollTop() + 'px'});
                        $('#slider_content:not(.fixed)').css({'margin-top': $(window).scrollTop() + 'px'});
                    }
                    $sliderTab = null;
                }

                //MENU WITH HOVERINTENT
                //---------------------
                if ($.__cmp.JStopNav) {

                    var $topnav = $('.top-nav');
                    if ($topnav.length) {
                        $topnav.find('.sub').css({'opacity': '0'});
                        $topnav.find('li').hoverIntent({
                            sensitivity: 1, // number = sensitivity threshold (must be 1 or higher)
                            interval: 0, // number = milliseconds for onMouseOver polling interval
                            over: megaHoverOver, // function = onMouseOver callback (REQUIRED)
                            timeout: 0, // number = milliseconds delay before onMouseOut
                            out: megaHoverOut // function = onMouseOut callback (REQUIRED)
                        });
                    }
                }



                //FIELD FOCUS on JS_focus class
                //-----------------------------
                $('input.JS_focus').focus();


                //DEFAULT TABLE
                //-------------
                if ($('table.default').length) {
                    $.__COMPONENTS.initDefaultTable();
                }


                //topbar contraction
                var $headerReadOnly = $('#header.read-only');
                if ($headerReadOnly.length) {
                    setTimeout(function () {
                        if ($headerReadOnly !== null) {

                            var $topBarLis = $headerReadOnly.find('.top-bar li:not(.fixed):not(.separator):not(.read-only-hidden)');
                            var $readOnlyBottomSwitch = $headerReadOnly.find('.read-only-bottom-switch');

                            if ($topBarLis.length && $readOnlyBottomSwitch.length) {
                                $topBarLis.css({'overflow': 'hidden'}).animate({width: 15}, 150);
                                $readOnlyBottomSwitch.css({'position': 'absolute'}).animate({'top': '30px', 'right': '0px'}, 150);
                            }

                            $topBarLis = $readOnlyBottomSwitch = null;
                        }
                    }, 750);
                }
                $headerReadOnly = null;

                var $topMessage = $('#top_message');
                if ($topMessage.length) {
                    //constraint the top-message to the body width, as it is position fixed
                    $('#top_message').css({'width': $._$body.width() + 'px'});
                }
                $topMessage = null;


                //EXPANDABLE BOX
                //--------------
                //show the expand icon on the box header : usage : <div class="box expandable">
                var $jsExpandable = $('.JS_expandable');

                $jsExpandable.each(function(){
                    var $this = $(this);

                    if ($this.hasClass('JS_show-icon')) {
                        if ($this.hasClass('header') && $this.parent().hasClass('box')) {
                            var $content = $this.siblings().filter('.content'),
                                $expandButton = $this.find('.expand-button'),
                                $headerRight = $this.find('.header-right');

                            if ($content.hasClass('hidden')) {
                                if ($expandButton.length) {
                                    $expandButton.removeClass('collapsed').addClass('expanded');
                                } else {
                                    if ($headerRight.length) {
                                        $headerRight.before('<span class="fr expand-button expanded"></span>');
                                    } else {
                                        $this.append('<span class="fr expand-button expanded"></span>');
                                    }
                                }
                            } else {
                                if ($expandButton.length) {
                                    $expandButton.removeClass('expanded').addClass('collapsed');
                                } else {
                                    if ($headerRight.length) {
                                        $headerRight.before('<span class="fr expand-button collapsed"></span>');
                                    } else {
                                        $this.append('<span class="fr expand-button collapsed"></span>');
                                    }
                                }
                            }
                            $content = null;
                            $expandButton = null;
                            $headerRight = null;
                        }
                    }
                });

                $jsExpandable = null;


                //PLACEHOLDER SUPPORT
                //-------------------
                if ($.placeholder !== undefined) {
                    $('input[placeholder], textarea[placeholder]').placeholder();
                }



                //JQUERY-UI DATEPICKER INIT
                //-------------------------
                if ($.__cmp.JSdatepicker) {

                    var datepickerOptions =
                    {
                        dateFormat: 'dd/mm/yy',
                        constrainInput: true,
                        changeMonth: true,
                        changeYear: true,
                        showOn: "button",
                        showAnim: '',
                        buttonImage: $._settings.jscafRootUrl + "/styles/images/common/datepicker-calendar.png",
                        buttonImageOnly: true,
                        buttonText: "",
                        showButtonPanel: true,
                        firstDay: 1,
                        isRTL: false,
                        minDate: null,
                        maxDate: null,
                        beforeShow: function (input) {
                            $(input).unbind('blur');
                        },
                        onSelect: function () {
                            $._log('COMMON.datepicker.ONSELECT');
                            var $this = $(this);
                            if ($this.hasClass('JS_datetimepicker_d')) {
                                var mainInputId = $('#' + $this.attr('mainInputId'));
                                if ($.__modules.VALIDATION) {
                                    $.__VALIDATION.validateDatetimepicker(mainInputId);
                                }
                            } else {
                                if ($.__modules.VALIDATION) {
                                    $._isFieldValid($this);
                                }
                                $this.change();
                            }
                        }
                    };

                    // datePicker localisation
                    var datePickerLocal;
                    if ($._settings.language === 'fr') {
                        datePickerLocal = {
                            monthNames: ['Janvier', 'F&eacute;vrier', 'Mars', 'Avril', 'Mai', 'Juin',
                                'Juillet', 'Ao&ucirc;t', 'Septembre', 'Octobre', 'Novembre', 'D&egrave;cembre'],
                            monthNamesShort: ['Jan', 'F&eacute;v', 'Mar', 'Avr', 'Mai', 'Jun',
                                'Jul', 'Ao&ucirc;', 'Sep', 'Oct', 'Nov', 'D&egrave;c'],
                            dayNames: ['Dimanche', 'Lundi', 'Mardi', 'Mercredi', 'Jeudi', 'Vendredi', 'Samedi'],
                            dayNamesShort: ['Dim', 'Lun', 'Mar', 'Mer', 'Jeu', 'Ven', 'Sam'],
                            dayNamesMin: ['Di', 'Lu', 'Ma', 'Me', 'Je', 'Ve', 'Sa'],
                            prevText: 'Mois pr&eacute;c&eacute;dent',
                            nextText: 'Mois suivant',
                            currentText: 'Aujourd\'hui',
                            closeText: 'Fermer'
                        };
                    } else {
                        datePickerLocal = {
                            monthNames: ['January', 'February', 'March', 'April', 'May', 'June',
                                'July', 'August', 'September', 'October', 'November', 'December'],
                            monthNamesShort: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun',
                                'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'],
                            dayNames: ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'],
                            dayNamesShort: ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'],
                            dayNamesMin: ['Su', 'Mo', 'Tu', 'We', 'Th', 'Fr', 'Sa'],
                            prevText: "Previous Month",
                            nextText: "Next Month",
                            currentText: 'today',
                            closeText: 'Done'
                        };
                    }
                    $.extend(datepickerOptions, datePickerLocal);


                    //Checking if a long date of years
                    var pickerRange = {yearRange: "c-1:c+2"};
                    var pickerRangeLong = {yearRange: "2007:c+5"};

                    $('input.JS_datepicker').each(function () {
                        var $this = $(this),
                            data = $this.data();

                        // YEAR RANGE

                        if (data.yearRange !== undefined) {
                            $.extend(datepickerOptions, {yearRange: data.yearRange});

                            //maintained for backward compatibility,
                        } else {
                            if ($this.hasClass('JS_dateLongRange')) {
                                $.extend(datepickerOptions, pickerRangeLong);
                            } else {
                                $.extend(datepickerOptions, pickerRange);
                            }
                        }

                        if (data.minDate !== undefined) {
                            $.extend(datepickerOptions, {minDate: data.minDate});
                        }

                        if (data.maxDate !== undefined) {
                            $.extend(datepickerOptions, {maxDate: data.maxDate});
                        }


                        // SHOW METHOD

                        if (data.showOn !== undefined) {
                            $.extend(datepickerOptions, {showOn: data.showOn});
                        }


                        // Enable input mask

                        $this.addClass('JS_maskedInput').attr('inputMask', '99/99/9999');

                        $this.datepicker(datepickerOptions);
                    });


                    //create the date picker
                    $("input.JS_datetimepicker").each(function () {
                        var $this = $(this);
                        var data = $this.data();
                        var id = $this.attr('id');
                        var id_d = id + '_d';
                        var id_h = id + '_h';
                        var id_m = id + '_m';
                        var id_s = id + '_s';

                        $this.addClass('hidden');

                        if ($this.parent().find('input').length === 1) {
                            $this.parent().append('<input id="' + id_d + '" mainInputId="' + id + '" type="text" class="JS_datepicker JS_datetimepicker_d JS_maskedInput field-value fl" inputMask="99/99/9999"/>');

                            if (data.yearRange !== undefined) {
                                $.extend(datepickerOptions, {yearRange: data.yearRange});

                                //maintained for backward compatibility
                            } else {
                                if ($this.hasClass('JS_dateLongRange')) {
                                    $.extend(datepickerOptions, pickerRangeLong);
                                } else {
                                    $.extend(datepickerOptions, pickerRange);
                                }
                            }

                            if (data.minDate !== undefined) {
                                $.extend(datepickerOptions, {minDate: data.minDate});
                            }

                            if (data.maxDate !== undefined) {
                                $.extend(datepickerOptions, {maxDate: data.maxDate});
                            }


                            // SHOW METHOD

                            if (data.showOn !== undefined) {
                                $.extend(datepickerOptions, {showOn: data.showOn});
                            }

                            $('#' + id_d).datepicker(datepickerOptions);
                            $this.parent().append('<input id="' + id_h + '" mainInputId="' + id + '" type="text" style="width:18px;margin-left:2px;" class="JS_datetimepicker_h JS_maskedInput field-value fl" inputMask="99"/>');
                            $this.parent().append('<input id="' + id_m + '" mainInputId="' + id + '" type="text" style="width:18px;margin-left:2px;" class="JS_datetimepicker_m JS_maskedInput field-value fl" inputMask="99"/>');
                            $this.parent().append('<input id="' + id_s + '" mainInputId="' + id + '" type="text" style="width:18px;margin-left:2px;" class="JS_datetimepicker_s JS_maskedInput field-value fl" inputMask="99"/>');
                            $this.closest('.field').addClass('datetimepicker');
                        }

                        var $d = $('#' + id_d);
                        var $h = $('#' + id_h);
                        var $m = $('#' + id_m);
                        var $s = $('#' + id_s);

                        if ($.trim($this.val()) !== '' && $.trim($this.val()) !== 'NaN/NaN/NaN NaN:NaN:NaN') {
                            var d = moment($this.val(), 'DD/MM/YYYY HH:mm:ss');
                            $d.val(d.format('DD/MM/YYYY'));
                            $h.val(d.format('HH'));
                            $m.val(d.format('mm'));
                            $s.val(d.format('ss'));
                        }

                        if ($this.hasClass('JS_dateonly')) {
                            $h.val('00').addClass('hidden');
                            $m.val('00').addClass('hidden');
                        }

                        //copying additional classes from the input field to the generated fields
                        var i, classes = $this.attr('class').split(' ');
                        for (i = 1; i < classes.length; i++) {
                            if (classes[i] !== 'JS_datetimepicker' &&
                                classes[i] !== 'JS_field' &&
                                classes[i] !== 'JS_live-validation' &&
                                classes[i] !== 'field-value' &&
                                classes[i] !== 'hidden') {
                                $d.addClass(classes[i]);
                                $h.addClass(classes[i]);
                                $m.addClass(classes[i]);
                                $s.addClass(classes[i]);
                            }
                        }


                    });

                }

                //TRANSFORM RADIO BUTTONS INTO JQUERY-UI BUTTON SETS
                //--------------------------------------------------
                if ($.__cmp.JSbuttonSet) {

                    $('span.JS_buttonSet').buttonset();

                }

                //INIT poshytip tooltip
                //---------------------
                if ($.__cmp.JStooltip) {

                    //Poshytip

                    $('.JS_tooltip').each(function () {

                        var $this = $(this),
                            $content,
                            settings = $.extend({
                                type: 'default',
                                triggerType: 'hover',
                                content: 'title',
                                contentId: null,
                                align: 'bottom',
                                theme: 'tip-black',
                                fade: false,
                                slide: false,
                                width: null,
                                showClose: true,
                                closePosition: 'top',
                                fixedTopPosition: null,
                                hintTitle: null
                            }, $this.data()),

                        //default tip parameters for bottom alignment
                            tipParams = {
                                alignY: 'bottom',
                                alignX: 'center',
                                offsetX: 0,
                                showOn: 'hover'
                            };


                        //LEFT align
                        if (settings.align === 'left') {
                            tipParams.alignY = 'center';
                            tipParams.alignX = 'left';
                            tipParams.offsetX = 5;
                        }

                        //RIGHT align
                        if (settings.align === 'right') {
                            tipParams.alignY = 'center';
                            tipParams.alignX = 'right';
                            tipParams.offsetX = 5;
                        }

                        //TOP align
                        if (settings.align === 'top') {
                            tipParams.alignY = 'top';
                        }

                        //Trigger type : normal OR onClick
                        if (settings.triggerType === 'onClick') {
                            tipParams.showOn = 'none';
                        }

                        //Trigger type : focus for input
                        if (settings.triggerType === 'focus') {
                            tipParams.showOn = 'focus';
                            settings.slide = true;
                            settings.fade = true;
                        }

                        //checking and getting the content element
                        if (settings.content === 'title' && settings.type !== 'hint' && settings.triggerType !== 'onClick') {
                            settings.content = $this.attr('title');
                        } else if (settings.content === 'html' || settings.type === 'hint' || settings.triggerType === 'onClick') {
                            $content = $('#' + this.id + '_content');
                            if (!$content.length) {
                                $content = $('#' + settings.contentId);
                            }

                            //Prepend/append the close button dynamically if not present
                            if (settings.triggerType === 'onClick' && settings.showClose) {
                                if (!$content.find('.JS_tooltip-content-close').length) {
                                    if (settings.closePosition === 'top') {
                                        if (settings.type === 'hint') {
                                            $content.prepend('<a class="JS_tooltip-content-close text-color-light-grey fr small" data-linked-tooltip-id="' + $this.attr('id') + '" style="position:absolute; top:10px; right: 10px;">' + $._getData('jscaf_common_close') + '<span class="icon-inline-text icon-close fr" style="margin-left:5px"></span></a>');
                                        } else {
                                            $content.prepend('<a class="JS_tooltip-content-close text-color-white fr small" data-linked-tooltip-id="' + $this.attr('id') + '">' + $._getData('jscaf_common_close') + '<span class="icon-inline-text icon-close fr" style="margin-left:5px"></span></a><br><br>');
                                        }
                                        //$content.prepend('<i class="JS_tooltip-content-close icon icon-ft-cancel-circle size16 red fr" data-linked-tooltip-id="' + $this.attr('id') + '"><br>');
                                    } else if (settings.closePosition === 'bottom') {
                                        $content.append('<br><a class="JS_tooltip-content-close text-color-white fr small" data-linked-tooltip-id="' + $this.attr('id') + '">' + $._getData('jscaf_common_close') + '<span class="icon-inline-text icon-close fr" style="margin-left:5px"></span></a>');
                                    }
                                }
                            }

                            settings.content = $content.html();
                        }

                        //subclassing types
                        if (settings.type === 'hint') {
                            settings.theme = 'tip-hint';
                            settings.fade = true;
                            settings.slide = true;

                            if (settings.hintTitle === null) {
                                settings.hintTitle = 'INFO';
                            }

                            settings.content = '<div class="tooltip-hint">' +
                            '<h6>' + settings.hintTitle + '</h6>' +
                            settings.content +
                            '</div>';
                        }

                        if (settings.type === 'error' || settings.type === 'input-error') {
                            settings.theme = 'tip-error';
                            tipParams.alignX = 'right';
                            tipParams.alignY = 'center';
                            tipParams.offsetX = 5;
                            settings.fade = true;

                            if (settings.type === 'input-error') {
                                settings.triggerType = 'focus';
                            }
                        }


                        $this.poshytip({
                            content: settings.content,
                            showOn: tipParams.showOn,
                            className: settings.theme,
                            showTimeout: 100,
                            offsetY: 5,
                            allowTipHover: false,
                            fade: settings.fade,
                            slide: settings.slide,
                            alignTo: 'target',
                            alignX: tipParams.alignX,
                            alignY: tipParams.alignY,
                            offsetX: tipParams.offsetX
                        });

                        $('.tip-black, .tip-white, .tip-hint').css({'max-width': settings.width});
                        if (settings.fixedTopPosition !== null) {
                            $('.tip-white').addClass('fixed-top-position');
                        }
                    });

                    //remove some .tip craps added to the dom
                    $('.tip-default').remove();
                    $('.tip-error').remove();
                    $('.tip-hint').remove();
                    $('.tip-black').remove();
                    $('.tip-white').remove();

                }


                //MASKED INPUT
                //------------
                if ($.__cmp.JSmaskedInput) {

                    $('input.JS_maskedInput').each(function () {
                        var $this = $(this);
                        $this.mask($this.attr('inputMask'));
                    });
                }

                //FIELD JS_field-number0to9 formating
                //-----------------------------------
                if ($.__cmp.JSfieldNumber) {

                    $('input.JS_field-number0to9').each(function () {
                        var $this = $(this);
                        if ($this.val() !== '') {
                            $this.val(parseFloat($this.val()).toFixed(0));
                        }
                    });

                    //FIELD JS_field-number formating
                    //-----------------------------------
                    $('input.JS_field-number').each(function () {
                        var $this = $(this);
                        if ($this.val() !== '') {
                            $this.val(parseFloat($this.val()).toFixed(2));
                        }
                    });
                }


                // BOOTSTRAP COMPONENTS GENERATION
                // -------------------------------
                if ($.__cmp.JSbsPopover) {
                    $('a.JS_bs-popover').popover();
                }


                //MAX LENGTH
                //----------
                if ($.__cmp.JSmaxLength) {

                    // Max Length Input
                    $('input[maxlength],textarea[maxlength]').each(function () {
                        var $this = $(this),
                            data = $this.data(),
                            $parent = $this.parent(),
                            maxCharLength = parseInt($this.attr("maxlength"), 10);

                        if (maxCharLength >= 0 || maxCharLength <= 4000) {
                            var charLength = $this.val().length;

                            if (data.showcounter || data.showcounter === undefined) {
                                if (!$parent.find('span.max-length-count').length) {
                                    $parent.append('<span class="max-length-count">' + (maxCharLength - charLength) + '</span>');
                                } else {
                                    $parent.find('span.max-length-count').html(maxCharLength - charLength);
                                }
                            }
                            $this.on('keypress keyup', function (e) {

                                var charLength = $(this).val().length;
                                var newLinesCount = $(this).val().split('\n').length - 1;
                                $parent.find('span.max-length-count').html(maxCharLength - charLength - newLinesCount);
                                if ((charLength + newLinesCount) === maxCharLength) {
                                    $parent.find('span.max-length-count').addClass("max");
                                } else {
                                    $parent.find('span.max-length-count').removeClass("max");
                                }
                                if (charLength >= maxCharLength) {
                                    $this.val($this.val().substring(0,maxCharLength));
                                }
                            });
                        }
                    });

                }



                if ($.__cmp.JSlistCounterSmall) {

                    $('ul.list-counter-small.with-arrow li.selected').each(function () {
                        var $this = $(this);
                        if (!$this.find('.arrow').length) {
                            $this.append('<span class="arrow"></span>');
                        }
                    });

                    $('ul.list-counter-small li.with-children').each(function () {
                        var $this = $(this);
                        if (!$this.find('.icon-expand').length) {
                            $this.append('<span class="icon icon-expand"></span>');
                        }
                    });
                }

                // SORTABLE LIST
                // -------------
                if ($.__cmp.JSsortable) {

                    $('.JS_sortable').sortable({
                        deactivate: function () {
                            $._log('serialize = [' + $(this).sortable("serialize", { key: "sort" }) + ']  ' + 'toArray = [' + $(this).sortable("toArray") + ']');
                        }
                    });
                }


                //apply checkedPolyfill on radios input for IE7/8 compat
                if ($.__cmp.JSradio) {
                    $('.JS_radio').checkedPolyfill();
                }

                if ($.__cmp.JScheckbox) {
                    $('.JS_checkbox').checkedPolyfill();
                }

                //select2 init
                if ($.__cmp.JSselect2) {

                    try {
                        $('select.JS_select2').select2({ width: 'resolve' });
                    } catch (e) {
                    }
                }

                // AUTOCOMPLETE FIELD
                // ------------------
                if ($.__cmp.JSautocomplete) {

                    $('.JS_autocomplete').each(function () {
                        var $this = $(this);

                        var opt = $this.data();
                        var source;

                        if (opt.sourceType === 'local') {
                            source = p[opt.sourceLocalArray];
                        } else if (opt.sourceType === 'remote') {
                            source = $._getContextPath() + opt.sourceRemoteUrl;
                        }

                        $this.autocomplete({
                            source: source,
                            minLength: 2,
                            response: function (event, ui) {
                                if (!ui.item) {
                                    if ((opt.hasResultElement) && (opt.allowFreeText)) {
                                        //Nothing has been selected => remove id for hasResultElement when free text is allowed
                                        var $result = $('#' + $this.attr('id') + '_result');
                                        if ($result.get(0).nodeName === 'INPUT') {
                                            $result.val('');
                                        } else {
                                            $result.html('');
                                        }
                                    }
                                }
                            },
                            select: function (event, ui) {
                                $this.data('item', ui.item);
                                if (opt.hasResultElement) {
                                    var $result = $('#' + $this.attr('id') + '_result');
                                    if ($result.get(0).nodeName === 'INPUT') {
                                        if (ui.item) {
                                            $result.val(ui.item[opt.resultPropertyName]);
                                        } else {
                                            $result.val('');
                                        }
                                    } else {
                                        if (ui.item) {
                                            $result.html(ui.item[opt.resultPropertyName]);
                                        } else {
                                            $result.html('');
                                        }
                                    }
                                }
                                $this.change();
                            },
                            close: function () {
                                var item = $this.data('item');
                                if (item) {
                                    if (opt.replacePropertyName !== undefined) {
                                        $this.val(item[opt.replacePropertyName]);
                                    }
                                } else {
                                    $this.val('');
                                }
                                $._isBlockerPage = true;
                                $._settings.isInitDisplayOnAjaxStop = true;
                            }
                        });

                        if (opt.listItemHtml !== undefined && opt.listItemPropertyNames !== undefined) {

                            $this.data("ui-autocomplete")._renderItem = function (ul, item) {

                                var idx,
                                    listItem = opt.listItemHtml,
                                    listItemNames = opt.listItemPropertyNames.split(',');

                                for (idx = 0; idx < listItemNames.length; idx++) {
                                    listItem = listItem.replace('$' + (idx + 1), item[listItemNames[idx]]);
                                }

                                return $("<li>")
                                    .append(listItem)
                                    .appendTo(ul);
                            };
                        }

                    });
                }



                // SUMMERNOTE initialisation

                if ($.__cmp.JSsummernote) {

                    $('.JS_summernote').each(function () {
                        var $this = $(this),
                            opt = $this.data();

                        if (opt.height === undefined) {
                            opt.height = 200;
                        }

                        $this.summernote({
                            height: opt.height,
                            //see http://summernote.org/#/features for additional features but AVOID fontnames if you don't want to have a messy generated code!!
                            toolbar: [
                                ['style', ['style', 'bold', 'italic', 'underline', 'clear']],
                                ['fontsize', ['fontsize']],
                                ['color', ['color']],
                                ['para', ['ul', 'ol', 'paragraph']],
                                ['height', ['height']]
                            ]
                        });

                    });




                }






                //FIELD REQUIRED
                //--------------
                //add the required icon on the field required : usage <div class="field required">
                $('div.field.required').each(function () {

                    var $this = $(this);


                    //dynamic insert of the required icon if not already present inside the required field
                    if (!$this.find('span.required-toggle').length) {

                        if ($this.find('br').length) {
                            $this.find('br').before('<span class="required-toggle required-required"></span>');
                        } else {

                            /* todo test in every cases

                             var $input = $this.find('div.label').next();
                             if (!$input.length) {
                             $input = $this.find('div.field-label').next();
                             }

                             if ($input.length) {
                             $input.after('<span class="required-toggle required-required"></span>');
                             } else {
                             */

                            $this.append('<span class="required-toggle required-required"></span>');

                            //}
                        }

                    }

                    if ($.__modules.VALIDATION) {
                        //checking the required field on init
                        //if the field have been initialised with some valid values for changing the required-toggle
                        //if the field contains more than one component to be validated, step through the returned array if not empty
                        $this.find('.JS_live-validation').each(function () {
                            $.__liveValidation($(this));
                        });
                    }

                    $this = null;
                });



                if ($._settings.isFlatThemeActive) {

                    $('.js-serialaccordion').serialaccordion({
                        getTarget: function( $trigger, $wrapper ) {
                            return $wrapper.find( '.js-serialaccordion-content' );
                        }
                    });

                    $('.header-user-navigation [data-serialexpand]').serialexpand({
                        position: 'bottom right'
                    });

                    $('.header-page-navigation [data-serialexpand]').serialexpand({
                        position: 'bottom left'
                    });

                    $('[data-serialtabs]').serialtabs({ target: 'data-serialtabs' });

                    $('[data-serialbox]').serialbox();

                    //$('.tasks .notification-count-inner').text( $('.homepage-widget-content .task-list li').not('.checked').length );
                    $('.header-user-navigation .messages .notification-count-inner').text( $('.header-user-messages .message' ).length );

                    $.__COMPONENTS.iaResizeDashboard();

                    $( 'body' ).css( 'overflow', 'auto' );

                }








                //jSCAF plugins initialisation


                if ($.__cmp.JSinlineEdit && $.fn.JSinlineEdit !== undefined) {

                    $('.JS_inlineEdit').JSinlineEdit();

                }


                if ($.__cmp.JSwizard && $.fn.JSwizard !== undefined) {

                    $('.JS_wizard').JSwizard();

                }





                //init display of each registered components

                $._execFn($._pageComponentsInitDisplay(), true);


            },


            initDefaultTable: function () {
                //applying zebra style
                var i = 1;

                $('table.default:not(.dataTable) > tbody > tr').each(function () {
                    var $this = $(this);

                    if (!$this.hasClass('bg') && // allow different bg color for row
                        !($this.closest('table').hasClass('sub-table')) && // content of hidden hierarchical table are not taken into account
                        !($this.hasClass('sub-row')) && // main hidden row or hierarchical table are not taken into account
                        !($this.closest('table').hasClass('no-zebra'))) {
                        if (i % 2 === 0) {
                            $this.removeClass('zebra2').addClass('zebra1');
                        } else {
                            $this.removeClass('zebra1').addClass('zebra2');
                        }
                        i++;
                    }
                });

            }




        },


        /* ======================================================= */
        /* ===            MANUAL TRIGGERS                       == */
        /* ======================================================= */



        /**
         * @see COMPONENTS.JS
         * @class .
         * @memberOf COMPONENTS.....$
         * @name _tabSwitch
         * @description todo
         */
        _tabSwitch: function (tabLinkId, tabsContainerId, tabContentClass) {
            if (tabContentClass !== undefined) {
                $('.' + tabContentClass).addClass('hidden');
            } else {
                $('.tabContent').addClass('hidden');
            }

            if (tabsContainerId !== undefined) {
                $('#' + tabsContainerId + ' li').removeClass('selected');
            } else {
                $('.tabLink').parent().removeClass('selected');
            }

            var $tabLink = $('#' + tabLinkId);

            $tabLink.parent().addClass('selected');
            $('#' + $tabLink.attr('id') + '_tabContent').removeClass('hidden');
        },


        /**
         * @see COMPONENTS.JS
         * @class .
         * @memberOf COMPONENTS.....$
         * @name _gridBoxSwitch
         * @description todo
         */
        _gridBoxSwitch: function($o) {
            $o.addClass('selected').siblings().removeClass('selected');
        },

        _activeWizardIndex: 0,


        /**
         * @see COMPONENTS.JS
         * @class .
         * @memberOf COMPONENTS.....$
         * @name _wizardStepSwitch
         * @description todo
         */
        _wizardStepSwitch: function (idx, step, isDynamicContent, wizardId) {

            var $wizardStepLi,
                $actionWizardNextPanel,
                $actionWizardPreviousPanel;

            if (wizardId === null || wizardId === undefined) {
                $wizardStepLi = $('.wizard-steps li');
                $actionWizardNextPanel = $('#action_wizard_next_panel');
                $actionWizardPreviousPanel = $('#action_wizard_previous_panel');
            } else {
                $wizardStepLi = $('#' + wizardId).find('li');
                $actionWizardNextPanel = $('#' + wizardId + '_action_wizard_next_panel');
                $actionWizardPreviousPanel = $('#' + wizardId + '_action_wizard_previous_panel');
            }

            if (step !== undefined && step !== null) {
                if ($._activeWizardIndex + step <= $wizardStepLi.length - 1 && $._activeWizardIndex + step >= 0) {
                    $._activeWizardIndex += step;
                }
            } else {
                $._activeWizardIndex = idx;
            }

            var wizardLink = $wizardStepLi.eq($._activeWizardIndex).find('a');

            $wizardStepLi.removeClass('active');
            wizardLink.parent().addClass('active');

            $._activeWizardIndex = $('.wizard-steps').find('.active').index();

            if ($._activeWizardIndex === $wizardStepLi.length - 1) {
                $actionWizardNextPanel.hide();
            } else {
                $actionWizardNextPanel.show();
            }

            if ($._activeWizardIndex === 0) {
                $actionWizardPreviousPanel.hide();
            } else {
                $actionWizardPreviousPanel.show();
            }

            if (!isDynamicContent || isDynamicContent === undefined) {
                $('.wizard-step-content').hide();
                $('#' + wizardLink.attr('id') + '_content').removeClass('hidden').fadeIn();
            }


        },


        /**
         * @see COMPONENTS.JS
         * @class .
         * @memberOf COMPONENTS.....$
         * @name _accordionPanelSwitch
         * @description todo
         */
        _accordionPanelSwitch: function (id) {
            var $this = $('#'+id);
            $this.removeClass('collapsed').addClass('expanded')
                .siblings().removeClass('expanded').addClass('collapsed');
        },





        /* ======================================================= */
        /* ===            ADDITIONAL VISUAL COMPONENTS          == */
        /* ======================================================= */

        /**
         * @see COMPONENTS.JS
         * @class .
         * @memberOf COMPONENTS.....$
         * @name _createTopMessage
         * @description display a top message over the header to emphasize a validation client-side or server-side
         * @param opt the object literal parameter <br><em>MANDATORY</em>
         * @param opt.type css class of the top message <br><i>DEFAULT : error</i>
         * @param opt.title title of the message
         * @param opt.subTitle subTitle of the message
         * @param opt.content content of the message in plain text
         * @param opt.contentArray if the content is null, then an array of message should be provided
         */

        _createTopMessage: function (opt) {
            var settings = $.extend({
                type: 'error',
                title: null,
                subTitle: null,
                content: null,
                contentArray: null,
                stay: true,
                displayTime: 6000
            }, opt);

            //Destroy top message first if already exists in the DOM
            $._destroyTopMessage();

            //Constructing the message content
            var content = '';
            content += '<div id="top_message" style="width:' + $._$body.width() + 'px;top:-200px;">';
            content += '<div class="close-message"></div>';
            content += '<div class="' + settings.type + ' message">';

            if (settings.title !== null) {
                content += '<h3>' + settings.title + '</h3>';
            }

            if (settings.subTitle !== null) {
                content += '<p>' + settings.subTitle;
            }

            if (settings.content !== null) {
                content += settings.content;
            } else if (settings.contentArray !== null) {
                var i;
                content += '<p>';
                for (i = 0; i < settings.contentArray.length; i++) {
                    content += '<li class="action_highlight_error cr-pointer">' + settings.contentArray[i].messageText + '</li>';
                }
                content += '</p';
            }
            content += '</div></div>';

            //pre-pending the top_message to the body : must be the first element (z-index...)
            $._$body.prepend(content);

            //do some funny things
            $('#top_message').animate({top: 0}, {duration: 750, easing: 'easeOutBounce'});

            //if displayTime provided as input param, the stay parameter is set to false,
            //this way only displayTime can be provided to force the message to disappear automatically
            if (opt.displayTime !== undefined) {
                settings.stay = false;
            }

            //remove the message after a displayTime period of time
            if (settings.displayTime !== null && !settings.stay) {
                setTimeout(function () {
                    $._removeTopMessage();
                }, settings.displayTime);
            }

            //bind the close event for direct closing the message
            $('.close-message').on('click', function () {
                $._removeTopMessage();
            });

        },


        /**
         * @see COMPONENTS.JS
         * @class .
         * @memberOf COMPONENTS.....$
         * @name _removeTopMessage
         * @description todo
         */
        _removeTopMessage: function () {
            //un-bind for safety
            $('.close-message').off('click');

            //fade and remove from DOM
            $('#top_message').fadeOut(500, function () {
                $('#top_message').remove();
            });
        },

        /**
         * @see COMPONENTS.JS
         * @class .
         * @memberOf COMPONENTS.....$
         * @name _destroyTopMessage
         * @description todo
         */
        _destroyTopMessage: function () {
            //un-bind for safety
            $('.close-message').off('click');

            //fade and remove from DOM
            $('#top_message').remove();
        },



        /**
         * @see COMPONENTS.JS
         * @class .
         * @memberOf COMPONENTS.....$
         * @name _highlightErrorOnScreen
         * @description todo
         */
        _highlightErrorOnScreen: function (errorId, errorsMap) {
            var tabId, errorTypeId, $parent, parentId, errorClass, scrollOffset, $topMessage;

            if (errorsMap[errorId] === null || errorsMap[errorId] === undefined) {
                return;
            }

            tabId = errorsMap[errorId].tabId;
            parentId = errorsMap[errorId].parentId;
            errorClass = errorsMap[errorId].errorClass;

            //todo auto switch tab and put highlight here under in callback fn

            $._log('DISPLAY.highlightErrorOnScreen : tabId:' + tabId + ' ' + 'errorTypeId:' + errorTypeId + ' element: parentId=' + parentId + ' errorClass=' + errorClass);

            //setting offset if top_message exists
            $topMessage = $('#top_message');
            if ($topMessage.length) {
                scrollOffset = $topMessage.height() - 60;
            } else {
                scrollOffset = 0;
            }

            if (parentId !== null) {

                $parent = $('#' + parentId);

                if (errorClass !== null) {
                    $._scrollToElement(
                        $parent.find('.validation_error').find('.' + errorClass), scrollOffset
                    ).effect("highlight", {color: "#ff0000"}, 1500);
                } else {
                    if ($parent.find('.validation_error').length) {
                        $._scrollToElement(
                            $parent.find('.validation_error'), scrollOffset
                        ).effect("highlight", {color: "#ff0000"}, 1500);
                    } else {
                        $._scrollToElement(
                            $parent, scrollOffset
                        ).effect("pulsate", { times: 2 }, 1000);
                    }
                }

            } else {

                $._scrollToElement(
                    $._$form.find('.' + errorClass), scrollOffset
                ).effect("highlight", {color: "#ff0000"}, 1500);

            }

        }















    });




}(jQuery));










