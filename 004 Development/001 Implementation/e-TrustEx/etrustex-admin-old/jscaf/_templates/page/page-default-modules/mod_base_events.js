define(function() {

    $._log('baseEvents:REQUIRED');

    return e = {

        bindEvents:function() {
            $._log('PAGE.bindEvents');

            $._addEvent('click','[A SELECTOR]',function () {
                p.functionCallByEvent($(this));
            });
        }

    };

});
