define(function() {

    $._log('base:REQUIRED');

    return p = {

        /* ======================================================= */
        /* ===                INIT                              == */
        /* ======================================================= */

        initDisplay: function() {
            $._log('PAGE.initDisplay');
        },


        /* ======================================================= */
        /* ===                INNER FUNCTIONS                   == */
        /* ======================================================= */

        //$o is the $(this) object passed when call on event listener in e.bindEvents(),
        //this way it's already cached
        functionCallByEvent: function($o) {

        }

    };

});
