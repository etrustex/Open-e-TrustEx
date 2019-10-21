/*global syze:true,jQuery:true,$:true,_:true,megaHoverOver:true,megaHoverOut:true,require:true,setTimeout,window,document,navigator,screen,console,moment,p,self*/



/* ======================================================= */
/* ===                 JSCAF RESOURCES MODULE           == */
/* ======================================================= */


(function ($) {

    "use strict";

    $.extend({

        //DECLARATION
        //-----------
        __RESOURCES: {

            load: function() {
                $._log('RESOURCES.load');

                // LOADING jSCAF COMMON TRANSLATION RESOURCES en/fr

                if ($._settings.language === 'fr') {
                    $.__globMessagesArray.push({key:'jscaf_common_home',value:'Accueil'});
                    $.__globMessagesArray.push({key:'jscaf_common_generated_in',value:'(Page g&eacute;n&eacute;r&eacute;e en '});
                    $.__globMessagesArray.push({key:'jscaf_common_cancel',value:'annuler'});
                    $.__globMessagesArray.push({key:'jscaf_common_close',value:'fermer'});
                    $.__globMessagesArray.push({key:'jscaf_common_YES',value:'OUI'});
                    $.__globMessagesArray.push({key:'jscaf_common_NO',value:'NON'});
                    $.__globMessagesArray.push({key:'jscaf_common_msgbox_warning',value:'ALERTE'});
                    $.__globMessagesArray.push({key:'jscaf_common_msgbox_error',value:'ERREUR'});
                    $.__globMessagesArray.push({key:'jscaf_common_msgbox_info',value:'INFORMATION'});
                    $.__globMessagesArray.push({key:'jscaf_common_msgbox_confirm',value:'CONFIRMATION'});
                    $.__globMessagesArray.push({key:'jscaf_common_msgbox_question',value:'QUESTION'});
                    $.__globMessagesArray.push({key:'jscaf_common_required_fields_missing',value:'Certains champs obligatoires (*) ne sont pas remplis ou sont invalides ...'});
                    $.__globMessagesArray.push({key:'jscaf_common_required',value:'Ce champ est obligatoire!'});
                    $.__globMessagesArray.push({key:'jscaf_common_email_invalid',value:'Adresse email invalide!'});
                    $.__globMessagesArray.push({key:'jscaf_common_date_invalid_format',value:'Format de date invalide (DD/MM/YYYY)!'});
                    $.__globMessagesArray.push({key:'jscaf_common_date_invalid',value:'Date invalide!'});
                    $.__globMessagesArray.push({key:'jscaf_common_period_invalid',value:'P&eacute;riode invalide!'});
                    $.__globMessagesArray.push({key:'jscaf_common_wait',value:'Attendez svp...chargement'});
                    $.__globMessagesArray.push({key:'jscaf_common_comp_invalid_equal',value:'Les champs doivent &ecirc;tre identiques!'});
                    $.__globMessagesArray.push({key:'jscaf_common_comp_invalid_datefromto',value:'P&eacute;riode invalide, la date de d&eacute;but doit &ecirc;tre inf&eacute;rieure &aacute; la date de fin!'});
                    $.__globMessagesArray.push({key:'jscaf_common_credit_card_invalid_type_not_matched',value:'La s&eacute;quence introduite ne correspond a aucun type de carte connu!'});
                    $.__globMessagesArray.push({key:'jscaf_common_credit_card_invalid_length',value:'La taille de la s&eacute;quence introduite est invalide!'});
                    $.__globMessagesArray.push({key:'jscaf_common_credit_card_invalid_number_sequence',value:'La validation de la séquence introduite est invalide!'});
                    $.__globMessagesArray.push({key:'jscaf_common_page_zoom_warning', value:"MESSAGE IMPORTANT : Vous utilisez un zoom sur la page autre que 100%, ceci peut engendrer des erreurs d'affichage et/ou des erreurs lors de la sauvegarde."});
                    $.__globMessagesArray.push({key:'jscaf_common_browser_warning', value:"MESSAGE IMPORTANT : La version de votre browser n'est pas optimale pour l'utilisation de l'application, elle peut provoquer des problèmes d'affichage."});
                    $.__globMessagesArray.push({key:'jscaf_common_onbeforeunload_message', value:"Vous avez essayé de quitter cette page. Si vous avez déjà modifié des données et n'avez pas sauvé celles-ci au moyen du bouton SAUVER, ces données seront perdues. Etes-vous sûr de vouloir quitter cette page?"});
                    $.__globMessagesArray.push({key:'jscaf_email_subject',value:'Sujet'});
                    $.__globMessagesArray.push({key:'jscaf_email_content',value:'Contenu'});
                    $.__globMessagesArray.push({key:'jscaf_email_send',value:'Envoyer'});
                    $.__globMessagesArray.push({key:'jscaf_common_download_file',value:'Télécharger ce fichier'});
                    $.__globMessagesArray.push({key:'jscaf_timeout_dialog_title',value:'Votre session va expirer !'});
                    $.__globMessagesArray.push({key:'jscaf_timeout_dialog_subTitle',value:'Voulez-vous rester connecté ?'});
                    $.__globMessagesArray.push({key:'jscaf_timeout_dialog_text',value:'Vous allez être déconnecté dans <span id="timeoutDialog_delay" class="text-color-blue very-big">@COUNTDOWN@</span> secondes.'});
                } else {
                    $.__globMessagesArray.push({key:'jscaf_common_home',value:'Home'});
                    $.__globMessagesArray.push({key:'jscaf_common_generated_in',value:'(Page generated in '});
                    $.__globMessagesArray.push({key:'jscaf_common_wait',value:'Please wait...loading'});
                    $.__globMessagesArray.push({key:'jscaf_common_cancel',value:'cancel'});
                    $.__globMessagesArray.push({key:'jscaf_common_close',value:'close'});
                    $.__globMessagesArray.push({key:'jscaf_common_YES',value:'YES'});
                    $.__globMessagesArray.push({key:'jscaf_common_NO',value:'NO'});
                    $.__globMessagesArray.push({key:'jscaf_common_msgbox_warning',value:'WARNING'});
                    $.__globMessagesArray.push({key:'jscaf_common_msgbox_error',value:'ERROR'});
                    $.__globMessagesArray.push({key:'jscaf_common_msgbox_info',value:'INFORMATION'});
                    $.__globMessagesArray.push({key:'jscaf_common_msgbox_confirm',value:'CONFIRMATION'});
                    $.__globMessagesArray.push({key:'jscaf_common_msgbox_question',value:'QUESTION'});
                    $.__globMessagesArray.push({key:'jscaf_common_required_fields_missing',value:'Some mandatory fields (*) are not filled or are invalid ...'});
                    $.__globMessagesArray.push({key:'jscaf_common_required',value:'This field is required!'});
                    $.__globMessagesArray.push({key:'jscaf_common_email_invalid',value:'This email address is invalid!'});
                    $.__globMessagesArray.push({key:'jscaf_common_date_invalid_format',value:'Invalid date format provided (DD/MM/YYYY)!'});
                    $.__globMessagesArray.push({key:'jscaf_common_date_invalid',value:'Invalid date!'});
                    $.__globMessagesArray.push({key:'jscaf_common_period_invalid',value:'Invalid period!'});
                    $.__globMessagesArray.push({key:'jscaf_common_comp_invalid_equal',value:'Fields must me equal!'});
                    $.__globMessagesArray.push({key:'jscaf_common_comp_invalid_datefromto',value:'Invalid period, date from must be before date to!!'});
                    $.__globMessagesArray.push({key:'jscaf_common_credit_card_invalid_type_not_matched',value:'Number sequence not matching any known credit card type!'});
                    $.__globMessagesArray.push({key:'jscaf_common_credit_card_invalid_length',value:'Number sequence length is not valid!'});
                    $.__globMessagesArray.push({key:'jscaf_common_credit_card_invalid_number_sequence',value:'Number sequence validation is not valid!'});
                    $.__globMessagesArray.push({key:'jscaf_common_page_zoom_warning', value:"IMPORTANT MESSAGE :  You're using a page zoom other than 100%, this might cause components to be incorrectly displayed and lead to errors during save operations."});
                    $.__globMessagesArray.push({key:'jscaf_common_browser_warning',value:'IMPORTANT MESSAGE : Your browser version is not optimal for the application usage, it might cause display problems.'});
                    $.__globMessagesArray.push({key:'jscaf_common_onbeforeunload_message',value:'You have attempted to leave this page.  If you have made any changes to the fields without clicking the SAVE button, your changes will be lost.  Are you sure you want to exit this page?'});
                    $.__globMessagesArray.push({key:'jscaf_email_subject',value:'Subject'});
                    $.__globMessagesArray.push({key:'jscaf_email_content',value:'Content'});
                    $.__globMessagesArray.push({key:'jscaf_email_send',value:'Send'});
                    $.__globMessagesArray.push({key:'jscaf_common_download_file',value:'Download this file'});
                    $.__globMessagesArray.push({key:'jscaf_timeout_dialog_title',value:'Your session is about to expire !'});
                    $.__globMessagesArray.push({key:'jscaf_timeout_dialog_subTitle',value:'Do you want to stay signed in ?'});
                    $.__globMessagesArray.push({key:'jscaf_timeout_dialog_text',value:'You will be logged out in <span id="timeoutDialog_delay" class="text-color-blue very-big">@COUNTDOWN@</span> seconds.'});
                }                


                //LOADING TRANSFERED APP MESSAGES
                
                var $appMessages = $('#jscaf_app_i18n');
                if ($appMessages.length) {
                    $appMessages.find('li').each(function() {
                        var $data = $(this).data();
                        $.__globMessagesArray.push({key:$data.key, value:$data.value});
                        $._log('====>app message added : ' + $data.key + '=>' + $data.value);
                    });
                } else {
                    $._log('====>no app message found');
                }


                //LOADING TRANSFERED PAGE MESSAGES

                var $pageMessages = $('#jscaf_page_i18n');
                if ($pageMessages.length) {
                    $pageMessages.find('li').each(function() {
                        var $data = $(this).data();
                        $.__globMessagesArray.push({key:$data.key, value:$data.value});
                        $._log('====>page message added : ' + $data.key + '=>' + $data.value);
                    });
                } else {
                    $._log('====>no page message found');
                }


                //LOADING EXTRA RESOURCES IF PROVIDED

                if ($._settings.fnExtraResourcesLoader !== null && typeof($._settings.fnExtraResourcesLoader) === 'function') {
                    $._execFn($._settings.fnExtraResourcesLoader);
                }


            }



        }








    });
}(jQuery));

