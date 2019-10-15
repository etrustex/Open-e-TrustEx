/* ======================================================= */
/* ===                PAGE LOAD                         == */
/* ======================================================= */
var mod = {
    __init: function () {

        return function () {

			require.config({
		        //root of module (mandatory)
				baseUrl: $._getContextPath() + "/jsp",

		        //localisation of modules / map by names
				paths: {
		            //base - common set of functions - events
		            //---------------------------------------
					base: '[PAGE URL]/mod_base@pagescript.timestamp@',
					baseEvents: '[PAGE URL]/mod_base_events@pagescript.timestamp@'

				}
			});

			require(['base','baseEvents', function() {

		        $._init({
		            fnPageBindEventsCallback: 'e.bindEvents'
		        });

			});

        };
    }
};
