/*global syze:true,jQuery:true,$:true,_:true,megaHoverOver:true,megaHoverOut:true,require:true,setTimeout,window,document,navigator,screen,console,moment,p,self*/



/* ======================================================= */
/* ===           JSCAF VALIDATION MODULE                == */
/* ======================================================= */


(function ($) {

    "use strict";

    $.extend({

        //Declaration
        __VALIDATION: {

            initValidationEvents: function() {

                //LIVE VALIDATION BOUND EVENTS
                //----------------------------

                //trigger on BLUR on INPUT fields
                $._$document
                    .on('blur', '.JS_live-validation',
                    function () {
                        var $this = $(this);
                        if ($this.get(0).nodeName !== 'SELECT') {
                            $.__liveValidation($this);
                        }
                    }
                );

                //trigger on CHANGE on SELECT fields
                $._$document
                    .on('change', '.JS_live-validation',
                    function () {
                        var $this = $(this);
                        if ($this.get(0).nodeName === 'SELECT' || $this.hasClass('JS_datetimepicker') || $this.hasClass('JS_datepicker')) {
                            $.__liveValidation($this);
                        }
                    }
                );

            },


            validateDatetimepicker: function(o) {

                $._log('VALIDATION.validateDateTimepicker');

                var $d = $('#' + o.attr('id') + '_d');
                var $h = $('#' + o.attr('id') + '_h');
                var $m = $('#' + o.attr('id') + '_m');

                if (o.hasClass('JS_dateonly')) {
                    $h.val('00');
                    $m.val('00');
                }

                if (!o.hasClass('JS_no-minutes-autofill')) {
                    if (($m.val() === '' || $m.val() === '__') && $h.val() !== '' && $h.val() !== '__') {
                        $m.val('00');
                    }
                }

                if ($d.val() === '__/__/____') {
                    $d.val('');
                }

                if ($d.val() !== '') {
                    var a = moment(
                        $d.val() + ' ' +
                            $h.val() + ':' +
                            $m.val(), 'DD/MM/YYYY HH:mm');

                    if (o.val() !== '' && a.format('YYYY') !== '0000') {
                        $d.val(a.format('DD/MM/YYYY'));
                        $h.val(a.format('HH'));
                        $m.val(a.format('mm'));
                    }

                    o.val(a.format('DD/MM/YYYY HH:mm'));

                    $._isFieldValid(o);

                    $._log('VALIDATION.validateDateTimePicker => trigger change event');
                    o.change();
                }

            }

        },



        /**
         * @see VALIDATION.JS
         * @class .
         * @memberOf VALIDATION.....$          
         * @name _isFieldValid
         * @description todo
         * @example todo
         */
        
        /*
         * @description
         * The general client-side validation function that can be called on demand and that will be called on every display refresh for field having the JS_live-validation class
         * @class .
         * @name _isFieldValid
         * @memberOf $
         * @param o {object} the jQuery element to be validated <br><i>default : undefined</i>
         * @param isUpdateDisplay {boolean} if the element should be visually marked invalid on the screen <br><i>default : undefined</i>
         * @param isValidForced {boolean} pass an outside condition into this parameters, this will bypass the validation of the _isFieldValid function itself but use the other actions (display invalidity) of the function <br><i>default : undefined</i>
         * @param o1 {object} if two elements must be validated against each other (this is the first element of the pair) <br><i>default : undefined</i>
         * @param o2 {object} if two elements must be validated against each other (this is the second element of the pair) <br><i>default : undefined</i>
         */
        _isFieldValid: function (o, isUpdateDisplay, isValidForced, o1, o2) {

            //check first if the display should be updated
            var updateDisplay = false;

            var isValid = false;

            // several options are possible :
            // 1. isUpdateDisplay parameter not specified => check for the global param
            if (isUpdateDisplay === undefined && $._settings.isFieldValidationUpdateDisplay) {
                updateDisplay = true;
            }
            // 2. isUpdateDisplay is TRUE, then this take the lead (used in the global validation on a form for example)
            if (isUpdateDisplay) {
                updateDisplay = true;
            }


            //field comparison validation
            if (o1 !== undefined && o2 !== undefined) {
                //options data are on the first field

                //first single field validity is checked
                var isValido1 = $._isFieldValidSingle(o1, updateDisplay);
                var isValido2 = $._isFieldValidSingle(o2, updateDisplay);

                var validationContentId = o1.attr('id') + '_' + o2.attr('id') + '_val_content';

                var $validationContent = $('#' + validationContentId);

                //then comparison validation occurs only if both fields passed their own validations
                if (!isValido1 || !isValido2) {

                    $('#' + validationContentId).remove();

                    //in case of one field or the other is invalid, invalidate both
                    o1.parent().find('span.required-toggle').removeClass('required-valid').addClass('required-required');
                    o2.parent().find('span.required-toggle').removeClass('required-valid').addClass('required-required');

                } else {

                    //comparison starts based on class provided ALWAYS ON FIELD1 !!
                    //checking equality
                    if (o1.hasClass('JS_comp-equal')) {
                        isValid = (o1.val() === o2.val());
                    }

                    //checking date period
                    if (o1.hasClass('JS_comp-datefromto') || o1.hasClass('JS_comp-datefromto-notequal')) {

                        var dateFrom;
                        var dateTo;

                        if (o1.hasClass('JS_datetimepicker')) {

                            dateFrom = $._getDate(o1.val());
                            dateTo = $._getDate(o2.val());

                        } else if (o1.hasClass('JS_datepicker')) {

                            dateFrom = $._getDate(o1.val());
                            dateTo = $._getDate(o2.val());

                        }

                        if (o1.hasClass('JS_comp-datefromto')) {
                            isValid = $._isPeriodValid(dateFrom, dateTo);
                        } else if (o1.hasClass('JS_comp-datefromto-notequal')) {
                            isValid = $._isPeriodValidNotEqual(dateFrom, dateTo);
                        }

                    }


                    //updating the required toggle icon
                    if (isValid) {
                        o1.parent().find('span.required-toggle').removeClass('required-required').addClass('required-valid');
                        o2.parent().find('span.required-toggle').removeClass('required-required').addClass('required-valid');
                    } else {
                        o1.parent().find('span.required-toggle').removeClass('required-valid').addClass('required-required');
                        o2.parent().find('span.required-toggle').removeClass('required-valid').addClass('required-required');
                    }


                    //updating the display
                    if (updateDisplay) {

                        //getting the validation message if exists
                        var validationMessage = '';
                        var $validationMessage = $('#' + o1.attr('id') + '_val_message');

                        if (o1.attr('alt') !== undefined) {
                            validationMessage = o1.attr('alt');
                        } else {
                            if ($validationMessage.length) {
                                validationMessage = $.trim($validationMessage.text());
                            } else {
                                if (o1.hasClass('JS_comp-equal')) {
                                    validationMessage = $._getData('jscaf_common_comp_invalid_equal');
                                }
                                if (o1.hasClass('JS_comp-datefromto')) {
                                    validationMessage = $._getData('jscaf_common_comp_invalid_datefromto');
                                }
                            }
                        }


                        //displaying the additional content
                        var validationContent = '';

                        if (o1.hasClass('JS_show_validation_error')) {

                            if (isValid) {
                                $('#' + validationContentId).remove();
                            } else {

                                if (!$('#' + validationContentId).length) {

                                    if (o2.hasClass('JS_error_bottom')) {
                                        validationContent += '<br>';
                                    }

                                    validationContent += '<div id="' + validationContentId + '" class="fl" style="margin-top:0;';

                                    if (o2.hasClass('JS_error_bottom')) {
                                        //finding top label width to left align
                                        var labelWidth = parseInt(o2.parent().find('.field-label').width(), 10);
                                        //adjust padding
                                        labelWidth += 2;
                                        validationContent += 'margin-bottom:10px; margin-left:' + labelWidth + 'px;';
                                    }
                                    validationContent += '">' +
                                        '   <div class="validation box">' +
                                        '     <p>' + validationMessage + '</p>' +
                                        '   </div>' +
                                        '</div>';

                                    if (o1.hasClass('JS_datetimepicker')) {
                                        o2.next().next().next().next().next().next().after(validationContent);
                                    } else {
                                        if (o1.hasClass('JS_datepicker')) {
                                            o2.next().next().after(validationContent);
                                        } else {
                                            o2.next().after(validationContent);
                                        }
                                    }

                                }
                            }

                        } else if (o1.hasClass('JS_show_validation_error_no_label')) {

                            if (isValid) {
                                $('#' + validationContentId).remove();
                            } else {

                                if (!$('#' + validationContentId).length) {

                                    validationContent = '<div id="' + validationContentId + '" title="' + validationMessage + '" class="fl cr-pointer" style="margin-top:0">' +
                                        '   <div class="validation">' +
                                        '     <p class="no-label">&nbsp;</p>' +
                                        '   </div>' +
                                        '</div>';

                                    if (o1.hasClass('JS_datetimepicker')) {
                                        o2.next().next().next().next().next().next().after(validationContent);
                                    } else {
                                        if (o1.hasClass('JS_datepicker')) {
                                            o2.next().next().after(validationContent);
                                        } else {
                                            o2.next().after(validationContent);
                                        }
                                    }

                                    $('#' + validationContentId).poshytip({
                                        className: 'tip-error',
                                        showOn: 'hover',
                                        alignTo: 'target',
                                        alignX: 'right',
                                        offsetX: -2,
                                        offsetY: -28
                                    });

                                }
                            }
                        }

                        //default display of a field : ui-state-error class added when not valid

                        if (isValid) {

                            if (o1.hasClass('JS_datetimepicker')) {
                                o1.parent().find('input').removeClass('ui-state-error');
                            } else {
                                o1.removeClass('ui-state-error');
                            }

                            if (o2.hasClass('JS_datetimepicker')) {
                                o2.parent().find('input').removeClass('ui-state-error');
                            } else {
                                o2.removeClass('ui-state-error');
                            }

                        } else {

                            if (o1.hasClass('JS_datetimepicker')) {
                                o1.parent().find('input').addClass('ui-state-error');
                            } else {
                                o1.addClass('ui-state-error');
                            }

                            if (o2.hasClass('JS_datetimepicker')) {
                                o2.parent().find('input').addClass('ui-state-error');
                            } else {
                                o2.addClass('ui-state-error');
                            }

                        }

                    }

                }

                //single field validation (default)
            } else {

                isValid = $._isFieldValidSingle(o, isUpdateDisplay, isValidForced);

            }

            return isValid;
        },


        _isFieldValidSingle: function (o, isUpdateDisplay, isValidForced) {

            var isSelect = false;
            var isDate = false;
            var isDateTime = false;
            var isSpan = false;
            var isApproveToggle = false;
            var isButtonSet = false;
            var isTextareaElastic = false;
            var isEmail = false;
            var isSelect2 = false;

            var isNumberField = false;
            var isZeroValidation = false;

            var isCreditCard = false;

            var isStarRating = false;

            var dateResult, creditCardResult;


            var isValid = false;

            //first check if the object exist
            if (!o.length) {
                //returning true to not break the client-side validation
                return true;
            }

            var nodeName = o.get(0).nodeName;

            if (nodeName === 'SELECT') {
                isSelect = true;
            } else {
                if (nodeName === 'INPUT') {
                    if (o.hasClass('JS_datepicker')) {
                        isDate = true;
                    }
                    if (o.hasClass('JS_datetimepicker')) {
                        isDateTime = true;
                    }
                    if (o.hasClass('JS_field-number') || o.hasClass('JS_field-number0to9')) {
                        isNumberField = true;
                        if (o.hasClass('JS_zero-validation')) {
                            isZeroValidation = true;
                        }
                    }
                    if (o.hasClass('JS_email')) {
                        isEmail = true;
                    }
                    if (o.hasClass('JS_field-credit-card-number')) {
                        isCreditCard = true;
                    }
                } else {
                    if (nodeName === 'SPAN') {
                        if (o.hasClass('JS_buttonSet')) {
                            isButtonSet = true;
                        } else if (o.hasClass('JS_approve-toggle')) {
                            isApproveToggle = true;
                        } else {
                            isSpan = true;
                        }
                    } else {
                        if (nodeName === 'TEXTAREA') {
                            if (o.hasClass('JS_elastic')) {
                                isTextareaElastic = true;
                            }
                        } else {
                            if (o.find('.ui-stars-star').length) {
                                isStarRating = true;
                            } else {
                                if (o.hasClass('select2-container')) {
                                    isSelect2 = true;
                                }
                            }
                        }
                    }
                }
            }


            if (isValidForced === undefined) {

                if (isSelect) {
                    if (o.val() === '-1' || o.val() === 'XXX' || o.val() === '' || o.val() === 'All' || o.val() === 'null / null') {
                        isValid = false;
                    } else {
                        isValid = o.children().length !== 0; //prevent validity of empty select (no options)
                    }
                } else {
                    if (isDate) {
                        dateResult = $._checkDate(o.val());
                        isValid = !(dateResult === -1 || dateResult === -2);
                    } else if (isDateTime) {
                        isValid = !($.trim(o.val()).length === 0 || o.val() === 'Invalid Date' || o.val() === 'NaN/NaN/NaN NaN:NaN' || o.val() === 'NaN');
                    } else {
                        if (isSpan) {
                            isValid = $.trim(o.text()) !== '';
                        } else if (isApproveToggle) {
                            isValid = !(o.hasClass('approve-toggle-disabled'));
                        } else if (isButtonSet) {
                            var buttonSetVal = $('[name="' + o.attr('id') + '"]:checked').attr('value');
                            isValid = buttonSetVal !== undefined;
                        } else {
                            if (isStarRating) {
                                isValid = o.find('input').val() !== '0';
                            } else {
                                if (isSelect2) {
                                    isValid = true; //forced to valid as the validation is done on the hidden linked select
                                } else {    
                                    if ($.trim(o.val()).length === 0) {
                                        if (isTextareaElastic) {
                                            isValid = false;
                                        } else {
                                            try {
                                                isValid = $.trim(o.html()).length !== 0;
                                            } catch (err) {
                                                isValid = false;
                                            }
                                        }
                                    } else {
                                        if (isEmail) {
                                            isValid = o.filter(
                                                function () {
                                                    return this.value.match('[a-zA-Z0-9-_]+[a-zA-Z0-9.-_]*@[a-zA-Z0-9-_]+.[a-zA-Z.-_]{1,}[a-zA-Z-_]+');
                                                }).length !== 0;
                                        } else {
                                            if (isCreditCard) {
                                                creditCardResult = $._checkCreditCard(o);
                                                isValid = (creditCardResult === 0);
                                            } else {
                                                isValid = true;
                                            }
                                        }
                                    }

                                    if (isValid) {
                                        if (isNumberField && isZeroValidation) {
                                            if (parseFloat(o.val()) === 0.0) {
                                                isValid = false;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                isValid = isValidForced;
            }


            //updating the required toggle icon
            if (!isSelect2) {
                if (isValid) {
                    o.closest('.field').find('span.required-toggle')
                        .removeClass('required-required')
                        .addClass('required-valid');
                } else {
                    o.closest('.field').find('span.required-toggle')
                        .removeClass('required-valid')
                        .addClass('required-required');
                }
            }

            //check first if the display should be updated
            var updateDisplay = false;

            // several options are possible :
            // 1. isUpdateDisplay parameter not specified => check for the global param
            if (isUpdateDisplay === undefined && $._settings.isFieldValidationUpdateDisplay) {
                updateDisplay = true;
            }
            // 2. isUpdateDisplay is TRUE, then this take the lead (used in the global validation on a form for example)
            if (isUpdateDisplay) {
                updateDisplay = true;
            }


            //updating the display - display the validation content close to the field
            if (updateDisplay) {

                var validationMessage = null;

                //setting validation message
                if (!isValid) {

                    if (o.attr('alt') !== undefined) {
                        validationMessage = o.attr('alt');
                    } else {

                        if ($('#' + o.attr('id') + '_val_message').length) {

                            validationMessage = $.trim($('#' + o.attr('id') + '_val_message').text());

                        } else {

                            if (!isDate && !isDateTime) {
                                if (isEmail) {
                                    validationMessage = $._getData('jscaf_common_email_invalid');
                                } else {
                                    if (isCreditCard && creditCardResult !== undefined) {
                                        if (creditCardResult === -1) {
                                            validationMessage = $._getData('jscaf_common_credit_card_invalid_type_not_matched');
                                        } else if (creditCardResult === -2) {
                                            validationMessage = $._getData('jscaf_common_credit_card_invalid_length');
                                        } else if (creditCardResult === -3) {
                                            validationMessage = $._getData('jscaf_common_credit_card_invalid_number_sequence');
                                        }
                                    } else {
                                        validationMessage = $._getData('jscaf_common_required');
                                    }
                                }
                            }
                            else {
                                if (isDate) {
                                    if (dateResult === -1) {
                                        validationMessage = $._getData('jscaf_common_date_invalid_format');
                                    } else {
                                        validationMessage = $._getData('jscaf_common_date_invalid');
                                    }
                                } else if (isDateTime) {
                                    validationMessage = $._getData('jscaf_common_date_invalid');
                                }
                            }
                        }
                    }

                }


                //VALIDATION DISPLAY set on extra content
                //---------------------------------------

                var validationContent = '';
                var validationContentId = o.attr('id') + '_val_content';
                var $validationContent = ('#' + validationContentId);

                if (o.hasClass('JS_show_validation_error')) {

                    if (isValid) {
                        $('#' + validationContentId).remove();
                    } else {

                        $('#' + validationContentId).remove();

                        if (!$('#' + validationContentId).length) {

                            if (o.hasClass('JS_error_bottom')) {
                                validationContent += '<br>';
                            }

                            validationContent += '<div id="' + validationContentId + '" class="fl" style="margin-top:0;';

                            if (o.hasClass('JS_error_bottom')) {
                                //finding top label width to left align
                                var labelWidth = parseInt(o.parent().find('.field-label').width(), 10);
                                //adjust padding
                                labelWidth += 2;
                                validationContent += 'margin-bottom:10px; margin-left:' + labelWidth + 'px;';
                            }
                            validationContent += '">' +
                                '   <div class="validation box">' +
                                '     <p>' + validationMessage + '</p>' +
                                '   </div>' +
                                '</div>';

                            if (isDateTime) {
                                o.next().next().next().next().next().next().after(validationContent);
                            } else {
                                if (isDate) {
                                    if (o.next().next().length) {
                                        o.next().next().after(validationContent);
                                    } else {
                                        o.next().after(validationContent);
                                    }
                                } else {
                                    o.next().after(validationContent);
                                }
                            }


                        }
                    }

                } else {

                    if (o.hasClass('JS_show_validation_error_no_label')) {

                        $('#' + validationContentId).remove();

                        if (isValid) {
                            $('#' + validationContentId).remove();

                        } else {

                            if (!$('#' + validationContentId).length) {

                                validationContent = '<div id="' + validationContentId + '" title="' + validationMessage + '" class="fl cr-pointer" style="margin-top:0">' +
                                    '   <div class="validation">' +
                                    '     <p class="no-label">&nbsp;</p>' +
                                    '   </div>' +
                                    '</div>';
                                if (isDateTime) {
                                    o.next().next().next().next().next().next().after(validationContent);
                                } else {
                                    if (isDate) {
                                        o.next().next().after(validationContent);
                                    } else {
                                        o.next().after(validationContent);
                                    }
                                }
                                $('#' + validationContentId).poshytip({
                                    className: 'tip-error',
                                    showOn: 'hover',
                                    alignTo: 'target',
                                    alignX: 'right',
                                    offsetX: -2,
                                    offsetY: -28
                                });
                            }
                        }

                    }
                }


                //default display of a field : ui-state-error class added when not valid

                if (isValid) {

                    if (o.hasClass('JS_datetimepicker')) {
                        o.parent().find('input').removeClass('ui-state-error');
                    } else {
                        if (o.next().hasClass('select2-container')) {
                            o.next().removeClass('ui-state-error');
                        } else {
                            if (!isSelect2) {
                                o.removeClass('ui-state-error');
                            }                                
                        }    
                    }

                } else {

                    if (o.hasClass('JS_datetimepicker')) {
                        o.parent().find('input').addClass('ui-state-error');
                    } else {
                        if (o.next().hasClass('select2-container')) {
                            o.next().addClass('ui-state-error');
                        } else {
                            if (!isSelect2) {
                                o.addClass('ui-state-error');
                            }    
                        }    
                    }

                }

            }

            return isValid;
        },


        /**
         * @see VALIDATION.JS
         * @class .
         * @memberOf VALIDATION.....$          
         * @name _checkCreditCard
         * @description todo
         * @example todo
         */
        _checkCreditCard: function (o) {
            var card_types,
                normalizedNumber,
                cardType;

            card_types = [
                {
                    name: 'amex',
                    pattern: /^3[47]/,
                    valid_length: [15]
                },
                {
                    name: 'diners_club_carte_blanche',
                    pattern: /^30[0-5]/,
                    valid_length: [14]
                },
                {
                    name: 'diners_club_international',
                    pattern: /^36/,
                    valid_length: [14]
                },
                {
                    name: 'jcb',
                    pattern: /^35(2[89]|[3-8][0-9])/,
                    valid_length: [16]
                },
                {
                    name: 'laser',
                    pattern: /^(6304|630[69]|6771)/,
                    valid_length: [16, 17, 18, 19]
                },
                {
                    name: 'visa_electron',
                    pattern: /^(4026|417500|4508|4844|491(3|7))/,
                    valid_length: [16]
                },
                {
                    name: 'visa',
                    pattern: /^4/,
                    valid_length: [16]
                },
                {
                    name: 'mastercard',
                    pattern: /^5[1-5]/,
                    valid_length: [16]
                },
                {
                    name: 'maestro',
                    pattern: /^(5018|5020|5038|6304|6759|676[1-3])/,
                    valid_length: [12, 13, 14, 15, 16, 17, 18, 19]
                },
                {
                    name: 'discover',
                    pattern: /^(6011|622(12[6-9]|1[3-9][0-9]|[2-8][0-9]{2}|9[0-1][0-9]|92[0-5]|64[4-9])|65)/,
                    valid_length: [16]
                }
            ];

            //normalize the input first
            normalizedNumber = o.val().replace(/[ \-]/g, '');

            //check credit card type
            var card_type, _i, _len;
            cardType = null;
            for (_i = 0, _len = card_types.length; _i < _len; _i++) {
                card_type = card_types[_i];
                if (normalizedNumber.match(card_type.pattern)) {
                    cardType = card_type;
                    break;
                }
            }
            if (cardType === null) {
                //credit card type cannot be matched
                return -1;
            }

            //checking number length
            var i, isLengthValid = false;

            for (i = 0; i < cardType.valid_length.length; i++) {
                if (cardType.valid_length[i] === normalizedNumber.length) {
                    isLengthValid = true;
                    break;
                }
            }

            if (!isLengthValid) {
                // length number not valid
                return -2;
            }


            //check credit card number validity
            var digit, n, sum, _ref;
            sum = 0;
            _ref = normalizedNumber.split('').reverse().join('');
            for (n = 0, _len = _ref.length; n < _len; n++) {
                digit = _ref[n];
                digit = +digit;
                if (n % 2) {
                    digit *= 2;
                    if (digit < 10) {
                        sum += digit;
                    } else {
                        sum += digit - 9;
                    }
                } else {
                    sum += digit;
                }
            }
            if (sum % 10 !== 0) {
                //invalid number
                return -3;
            }

            return 0;  //valid

        },



        /**
         * @see VALIDATION.JS
         * @class .
         * @memberOf VALIDATION.....$          
         * @name _validate
         * @description todo
         * @example todo
         */
        /*
         * @description
         * Validate all elements having the JS_live-validation class inside a provided container jQuery element
         * @class .
         * @name _validate
         * @memberOf $
         * @param $o {object} the jQuery element of the container <br><i>default : undefined</i>
         */
        _validate: function ($o) {

            var bValid = true;

            $._settings.isFieldValidationUpdateDisplay = true;

            $o.find('.JS_live-validation').each(function () {
                bValid = $._isFieldValid($(this), true) && bValid;
            });

            $._settings.isFieldValidationUpdateDisplay = false;

            return bValid;
        },





        __liveValidation: function ($o) {

            if ($o.attr('class').indexOf('JS_comp') > 0) {

                var linkedField;
                if ($o.hasClass('JS_comp-field1')) {
                    linkedField = $o.closest('.field').nextAll().filter('.field').find('.JS_comp-field2');
                    $._isFieldValid(null, null, null, $o, linkedField);

                } else if ($o.hasClass('JS_comp-field2')) {
                    linkedField = $o.closest('.field').prevAll().filter('.field').find('.JS_comp-field1');
                    $._isFieldValid(null, null, null, linkedField, $o);
                }

            } else {
                $._isFieldValid($o);
            }

        },



        /**
         * @see VALIDATION.JS
         * @class .
         * @memberOf VALIDATION.....$          
         * @name _checkDate
         * @description todo
         * @example todo
         */

        /*
         * @description
         * strictly checks if a String date is valid
         * @class .
         * @name _checkDate
         * @memberOf $
         * @param sDate {String} the date formatted as string to be checked <br><i>default : undefined</i>
         */
        _checkDate: function (sdate) {
            // basic regular expression : 1 or 2 digit / 1 or 2 digit / 1 to 4 digit to validate format
            var re = /^(\d{1,2}\/){2}\d{1,4}$/;
            if (!re.test(sdate)) {
                return(-1);
            }    // V?rification du format ? l'aide d'un masque de saisie
            var dateArray = sdate.split('/');
            if (dateArray.length !== 3) {
                return(-1);
            }
            var dd = parseInt(dateArray[0], 10); // extract day
            var mm = parseInt(dateArray[1], 10); // extract month
            var yyyy = parseInt(dateArray[2], 10); // extract year
            if (dd === 0 || mm === 0) {
                return(-2);
            } // day of month = 0 => bad format
            if (dd < 10 && dateArray[0].length === 1) {
                dateArray[0] = '0' + dateArray[0];
            }// day < 10 : concatenate with 0
            if (mm < 10 && dateArray[1].length === 1) {
                dateArray[1] = '0' + dateArray[1];
            }// month < 10 : concatenate with 0
            if (yyyy < 100) { // year <100
                yyyy += 2000;          // add 2000
                dateArray[2] = yyyy.toString(); // reformat input
            }
            if (yyyy < 2000 || yyyy > 9999 || mm < 1 || mm > 12 || dd < 1) {
                return(-2);
            } // irregular cases
            if (mm === 4 || mm === 6 || mm === 9 || mm === 11) {
                return (dd <= 30 ? 0 : -2);
            } // Mois de 30 jours
            if (mm === 1 || mm === 3 || mm === 5 || mm === 7 || mm === 8 || mm === 10 || mm === 12) {
                return (dd <= 31 ? 0 : -2);
            }  // Mois de 31 jours
            if ((yyyy % 4 !== 0) || ((yyyy % 4 === 0) && (yyyy % 400 === 0))) {
                return (dd <= 28 ? 0 : -2);
            }   // Mois de f?vrier, ann?e NON BISEXTILE
            return (dd <= 29 ? 0 : -2);   // Mois de f?vrier, ann?e BISEXTILE
        },


        /**
         * @see VALIDATION.JS
         * @class .
         * @memberOf VALIDATION.....$          
         * @name _isDateTimeValid
         * @description todo
         * @example todo
         */
        _isDateTimeValid: function (s) {

            //check the date only first
            if ($._checkDate(s) === 0) {
                return false;
            } else {

                var hours = s.substr(11, 2);
                var minutes = s.substr(14, 2);

                if (hours >= 0 && hours <= 23) {
                    return (minutes >= 0 && minutes <= 59);
                } else {
                    return false;
                }
            }
        }

    });  


}(jQuery));










