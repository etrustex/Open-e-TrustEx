/*global syze:true,jQuery:true,$:true,_:true,megaHoverOver:true,megaHoverOut:true,require:true,setTimeout,window,document,navigator,screen,console,moment,p,self*/



/* ======================================================= */
/* ===                 JSCAF PLUGINS MODULE             == */
/* ======================================================= */


(function ($) {

    "use strict";

    $.extend({

        //DECLARATION
        //-----------
        __PLUGINS: {

            load: function() {
                $._log('PLUGINS.load');

                if ($._settings.JSplugins !== null) {

                    require.config({
                        //name to path values
                        paths: {
                            datatables: $._getContextPath() + '/scripts/optionals/datatables/dataTables-tableTools.min'
                        }
                    });


                    for (var prop in $.__pluginsDefs) {
                        if ($.__pluginsDefs.hasOwnProperty(prop)) {

                            if ($._settings.JSplugins[prop]) {
                                $._log('PLUGINS.load ====> ' + prop + ' loaded');

                                require([prop], function(Datatable) {
                                    $._log('PLUGINS.load ====> ' + prop + ' loaded');
                                });

                            }

                        }
                    }

                }

            }



        }








    });
}(jQuery));

