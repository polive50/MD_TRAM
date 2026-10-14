/**
 * Librería JavaScript de atlas.
 *
 * Script autoejecutado en cada llamada
 */
const ENTER_KEY_CODE = 13;

function noEnter() {
    return !(window.event && window.event.keyCode === ENTER_KEY_CODE);
}

/**
 * Código del antiguo fichero utilForms
 */
function trapEnter(evt, id, isLink) {
    var keycode;

    if (evt)
        ;
    else if (window.event)
        evt = window.event;
    else if (event)
        evt = event;
    else
        return true;

    if (evt.charCode)
        keycode = evt.charCode;
    else if (evt.keyCode)
        keycode = evt.keyCode;
    else if (evt.which)
        keycode = evt.which;
    else
        keycode = 0;

    if (keycode == 13) {
        evt.preventDefault ? evt.preventDefault() : evt.returnValue = false;
        if(isLink == true) {
            document.getElementById(id).onclick();
        } else {
            document.getElementById(id).submit();
        }
        return false;
    }
    else
        return true;
}