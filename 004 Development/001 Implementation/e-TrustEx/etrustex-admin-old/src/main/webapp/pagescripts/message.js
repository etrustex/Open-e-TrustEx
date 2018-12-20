var p = {
		__init: function() {
			return {
		        pageFormNameOverride:'messageForm',
		        pageActionUrlOverride:$('#messageForm').attr('action'),
		        appComponents:{
		        	partyView: true,
					transactionView: true,
					icaView: true
	            }
			};
		},
		
		initDisplay: function(){
			$._L("MESSAGE." + $("#pageMode").val() + "page.initDisplay");
		},
		
		bindEvents: function() {
		    $._AE('click', '.action_view_party', function() {
				var divId = $(this).attr('id');
				partyView.openDialog("messageForm", "message", divId.substring(divId.lastIndexOf("_") + 1));
			});

			$._AE('click', '#action_view_ica', function() {
				icaView.openDialog("messageForm", "message");
			});

			$._AE('click', '#action_view_transaction', function() {
				transactionView.openDialog("messageForm", "message");
			});
			
            $._AE('click', '.action_show_message_stuff', function() {
                p.showMessageStuffDialog($(this).attr('id'));
            });

            $._AE('click', '#action_message_resubmit', function() {
                p.resubmitMessageConfirmation();
            });

            $._ADE('click', '.routing_action_redispatch', function() {
                p.redispatchConfirmation($(this).closest('a').attr('id'));
            });

            $._ADE('click', '#dialog_stuff_action_cancel', function() {
                p.closeMessageStuffDialog();
            });

            $._ADE('click','.select-endpoint', function() {
                p.showEndpointDialog($(this).closest('tr').attr('id'));
            });

            $._ADE('click','.select-message', function() {
                var relationship = $(this).hasClass('parent') ? 'parent' : 'child';
                p.showMsgDialog($(this).closest('tr').attr('id'), relationship);
            });

			$._AE('click', '#action_cancel', function() {
				$._L("MESSAGE: CANCEL button clicked");
				p.cancel();
			});
		},
		
		// function definitions

        showMessageStuffDialog: function(btnId) {
            var msgId = $("#messageId").val();
            var stuff = btnId.substring(btnId.lastIndexOf("_") + 1);
            var url = $._getContextPath() + "/message/" + msgId + "/show/" + stuff + ".do";
            var dialogTitle = $._getData('message.' + stuff + '.dialog.title') + " " + msgId;

            $._OAD({
                dialogId: 'showMessageStuffDialog',
                dialogTitle: dialogTitle,
                refreshedFragmentPageUrl: url,
                isOneButtonOnly: true,
                buttonSecondaryFn: function () { $._CD({dialogId: 'showMessageStuffDialog'}); },
                isShowCloseButton: true,
                dialogWidth: 900
            });
        },

        closeMessageStuffDialog: function() {
            $._CD({dialogId: 'showMessageStuffDialog'});
        },


        showEndpointDialog: function(endpointId) {
            $._OAD({
                dialogId: 'showEndpointDialog',
                dialogTitle: $._getData('endpoint.view.title'),
                refreshedFragmentPageUrl: $._getContextPath() + "/endpoint/" + endpointId + "/view/load.do?isViewDialog=true",
                isOneButtonOnly: true,
//                buttonSecondaryFn: function () { $._CD({dialogId: 'showEndpointDialog'}); },
                fnAfterCreatePostCall: function () {
                    $( "#showEndpointDialog #action_cancel" ).click(function() {
                      $._CD({dialogId: 'showEndpointDialog'});
                    });
                },
                isShowCloseButton: true,
                dialogWidth: 900
            });
        },

        showMsgDialog: function(messageId, relationship) {
           $._OAD({
               dialogId: 'showMsgDialog',
               dialogTitle: $._getData(relationship == 'parent' ? 'message.parent.view.title' : 'message.child.view.title'),
               refreshedFragmentPageUrl: $._getContextPath() + "/message/" + messageId + "/view/load.do?isViewDialog=true",
               isOneButtonOnly: true,
               fnAfterCreatePostCall: function () {
                   $( "#showMsgDialog #action_cancel" ).click(function() {
                     $._CD({dialogId: 'showMsgDialog'});
                   });
                   $("#showMsgDialog .button-link").not( "#action_cancel" ).hide();
               },
               isShowCloseButton: true,
               dialogWidth: 900
           });
        },

        redispatchConfirmation: function(msgRoutingId) {
            $._msgbox({
                dialogId:'confirmMsg',
                text: $._getData('message.redispatch.management.confirmation.message'),
                title: $._getData('common.management.confirmation.message.title'),
                msgboxType:'yes_no',
                alertType:'warning',
                fnCallback:function (r) {
                    if (r === true) {
                        $._blockUI();
                        p.redispatch(msgRoutingId);
                    }
                }
            });
        },

        redispatch: function(msgRoutingId){
            $._ajaxCall({
                pageUrl: $._getContextPath() + "/message/" + msgRoutingId + "/redispatch.do",
                pageForm: 'messageForm',
                fnPostCall: function () {
                    var ajaxResult = CIPADMIN.ajaxResult();

                    $._unblockUI();

                    if(ajaxResult.success) {
//                        CIPADMIN.showSuccessMessage(ajaxResult.message);

                        $._msgbox({
                            dialogId:'successMsg',
                            text:ajaxResult.message,
                            alertType:'info',
                            fnCallback:function (r) {
                                if (r === true) {
                                    p.closeMessageStuffDialog();
                                    p.showMessageStuffDialog('action_show_message_routing');
                                }
                            }
                        });
                    } else {
                        CIPADMIN.showBusinessError(ajaxResult.message);
                    }
                }
            });
        },

        resubmitMessageConfirmation: function() {
            $._msgbox({
                dialogId:'confirmMsg',
                text: $._getData('message.resubmit.management.confirmation.message'),
                title: $._getData('common.management.confirmation.message.title'),
                msgboxType:'yes_no',
                alertType:'warning',
                fnCallback:function (r) {
                    if (r === true) {
                        p.resubmitMessage();
                        $._blockUI();
                    }
                }
            });
        },

        resubmitMessage: function(){
            $._ajaxCall({
                pageUrl: $._getContextPath() + "/message/" + $("#messageId").val() + "/resubmit.do",
                pageForm: 'messageForm',
                fnPostCall: function () {
                    var ajaxResult = CIPADMIN.ajaxResult();
                    $._unblockUI();
                    if(ajaxResult.success) {
                        CIPADMIN.showSuccessMessage(ajaxResult.message);
                    } else {
                        CIPADMIN.showBusinessError(ajaxResult.message);
                    }
                }
            });
        },

		cancel: function() {
			$._navigateTo({url: "/message/search.do?fromView=true&" + $("#messageSearchForm").serialize()});
		}
};