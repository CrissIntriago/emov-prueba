
/* global PrimeFaces */

function focusNextOnEnter(event, next) {
    var keyCode = event.which || event.keyCode;
    if (keyCode === 13) {
        next = PrimeFaces.escapeClientId(next);
        var element = $(next);
        if (element)
        {
            element.focus();
            element.select();
            event.preventDefault();
        } else {
            return false;
        }

    }
    return true;
}

function wizardTransform() {
    $('ul.ui-wizard-step-titles>li.ui-wizard-step-title').each(function (e) {
        $(this).removeClass('ui-wizard-step-title ui-corner-all ui-state-default');
        $(this).addClass(' sigef-wizard');
        $(this).html('<a href="#">' + $(this).html() + '</a>');
    });
}

function rightclick(nameEvent) {
    var rightclick;
    var e = window.event;
    console.log(nameEvent + ' Evento: ' + e + ' which ' + +e.which);
    if (e.which)
        rightclick = (e.which === 3);
    else if (e.button)
        rightclick = (e.button === 2);
    alert(rightclick);
}

function toUpperCase(input) {
    var keynum;
    var e = window.event;
    if (window.event) { // IE                    
        keynum = e.keyCode;
    } else if (e.which) { // Netscape/Firefox/Opera                   
        keynum = e.which;
    }
    if (keynum === 32 || keynum === 8) {
        return;
    }
    var value = input.value;
    if (isNaN(value)) {
        var caretPos = input.selectionStart;
        input.value = value.toUpperCase();
        input.selectionStart = caretPos;
        input.selectionEnd = caretPos;
        input.ad = caretPos;
    }
}