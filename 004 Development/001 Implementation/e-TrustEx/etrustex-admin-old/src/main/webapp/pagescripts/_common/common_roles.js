define(function () {
		 //bind the components events
		$._ADE('click', '#dialog_transaction_action_cancel', function () {
			$._L('TRANSACTION_VIEW: Close button clicked');
			transactionView.close();
		});
		    
		// function definitions
		return roles = {
				parentForm: null,
				controllerPath: null,

                reloadRoles: function(profileId){
                    $._blockUIMessageInfo = $._getData('ica.reloading.roles');

                    $.ajax({
                        type: 'GET',
                        url: $._getContextPath() + "/role/forProfile.do",
                        data: {'profileId': profileId},
                        success: function (data) {
                            if(data.message) {
                                CIPADMIN.showBusinessError(data.message);
                            } else {
                                var rolesSelects = $('.roleSelect');
                                rolesSelects.empty();

                                rolesSelects.append($('<option/>').val('-1').text($._getData('choose.please')));
                                $.each(data, function (i, role) {
                                    rolesSelects.append($('<option/>').val(role.id).text(role.name));
                                });
                            }
                        }
                    });
                }
		}
});