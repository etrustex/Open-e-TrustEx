var p = {
		__init: function() {
			return {
		        pageFormNameOverride:'bulkRedispatchForm',
		        pageActionUrlOverride:'/bulkredispatch/search.do'
			};
		},
		
		initDisplay: function(){
			$._L("bulkRedispatchForm initiated");
			$('#csv_upload_action_ok').prop( "disabled", true );
			$('#csv_upload_action_ok').find(">:first-child").prop( "class", "button gray_button no-icon  no-icon  enabled" );
		},
		
		bindEvents: function(){
			$._AE('click', '#csv_upload_action_ok', function(){
				p.importCSV();
			});
			
			$._AE('change', '#file', function(){   	    	
				var files = $("#file")[0].files;
				if(files.length > 0) {
					$('#csv_upload_action_ok').prop( "disabled", false );
					$('#csv_upload_action_ok').find(">:first-child").prop( "class", "button alternate blue_button no-icon  no-icon  enabled" );
					$('#csv_filename').text($('#file')[0].files[0].name);
				}else{
					$('#csv_upload_action_ok').prop( "disabled", true );
					$('#csv_upload_action_ok').find(">:first-child").prop( "class", "button gray_button no-icon  no-icon  enabled" );
				}
			});
		},

		importCSV: function() {
	    	var bValid = true;   	    	
	    	var files = $("#file")[0].files;
	    	if(files.length == 0) {
	    		var bValid = false;
	    		jQuery.noticeAdd({text: $._getData('bulkredispatch.error.file.empty'), type: 'error'});
	    	} else if(files[0].size > 100000) {
	    		jQuery.noticeAdd({text: $._getData('bulkredispatch.error.file.size'), type: 'error'});
	    	}
	    	
	    	if(bValid){
	    		var formData = new FormData();
				formData.append('file', $('#file')[0].files[0]);
				$.ajax({
					type: "POST",
					url:  $._getContextPath() + '/bulkredispatch/upload.do',
					data : formData,
					enctype: 'multipart/form-data',
					cache: false,
					contentType: false,
					processData: false,
					success: function (response) {
						if(response == 'success'){
							$( "#inputBox" ).prop( "hidden", true );
							$( "#outputBox" ).prop( "hidden", false );
							$( "#container" ).text($('#file')[0].files[0].name);
						}else{
							jQuery.noticeAdd({text: response, type: 'error'});
						}
					}
				});   	
	    	} 
	    }
};