
/* global PrimeFaces */

function validateIntegerValueAndFocusNext(event, next) {
    var keyCode = event.which || event.keyCode;
    
    if (keyCode === 13) {
        next = PrimeFaces.escapeClientId(next);
        var element = $(next);
        if (element)
        {
            element.focus();
            element.select();

        }
    }

    if (keyCode < 48) {
        if (keyCode !== 8 && keyCode !== 9 && keyCode !== 37 && keyCode !== 39) {
            event.preventDefault();
            return false;
        }

    }
    if (keyCode > 57) {
        event.preventDefault();
        return false;
    }

    return true;
}
function validateFloatValueAndFocusNext(event, next) {
    var keyCode = event.which || event.keyCode;
    if (keyCode === 13) {
        next = PrimeFaces.escapeClientId(next);
        var element = $(next);
        if (element)
        {
            element.focus();
            element.select();
        }
    }
    if (keyCode > 57) {
        event.preventDefault();
        return false;
    }
    if (keyCode < 48) {
        /*. 46  ,44*/
        if (keyCode !== 8 && keyCode !== 46 && keyCode !== 9 && keyCode !== 37 && keyCode !== 39 && keyCode !== 44) {
            event.preventDefault();
            return false;
        }
    }
    return true;
}

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
        }
    }
    return true;
}


function wizardTransform() {
    $('ul.ui-wizard-step-titles>li.ui-wizard-step-title').each(function (e) {

        $(this).removeClass('ui-wizard-step-title ui-corner-all ui-state-default');
        $(this).addClass(' origami-wizard');

        $(this).html('<a href="#">' + $(this).html() + '</a>');

    });
}

$(document).ready(function () {

    $(document).on('blur', 'div.compContainer input.compInput', function () {

        var value = $(this).val();
        var id = $(this).attr("id");
        var field = $(this).attr("data-field");
        var defaultValue = $(this).attr("data-default-value");
        var allValues = $(this).attr("data-all-values");
        var valorActualInput = $(this).attr("data-id");

        if (typeof valorActualInput !== "undefined" || value) {
            var idOrden = allValues.split('-');
            var idItem = -1;
            var orden = -1;

            for (var i = 0; i < idOrden.length; i++) {
                var temp = idOrden[i].split(";");
                if (temp[1] === value) {
                    idItem = temp[0];
                    orden = temp[1];
                    break;
                }
            }
            if (orden === -1) {
                value = defaultValue;
                for (var i = 0; i < idOrden.length; i++) {
                    var temp = idOrden[i].split(";");
                    if (temp[1] === value) {
                        idItem = temp[0];
                        orden = temp[1];
                        break;
                    }
                }
            } else {
                value = orden;
            }
            $(this).val(parseInt(value));

            var arr = id.split(':');
            var prefIdSelect = '';

            for (var i = 0; i < arr.length - 1; i++) {
                prefIdSelect += arr[i] + '\\:';
            }
            var idSelect = prefIdSelect + field + '-select_input';
            var idLabel = prefIdSelect + field + '-select_label';


            $("#" + idSelect).val('com.origami.sgm.entities.CtlgItem:' + idItem + ':java.lang.Long');
            $("#" + idLabel).html($("#" + idSelect).find('option:selected').text());
        } else {

            if (value) {
                var idOrden = allValues.split('-');
                var idItem = -1;
                var orden = -1;

                for (var i = 0; i < idOrden.length; i++) {
                    var temp = idOrden[i].split(";");
                    if (temp[1] === value) {
                        idItem = temp[0];
                        orden = temp[1];
                        break;
                    }
                }
                if (orden === -1) {
                    value = defaultValue;
                    for (var i = 0; i < idOrden.length; i++) {
                        var temp = idOrden[i].split(";");
                        if (temp[1] === value) {
                            idItem = temp[0];
                            orden = temp[1];
                            break;
                        }
                    }
                } else {

                    value = orden;
                }
                $(this).val(parseInt(value));

                var arr = id.split(':');
                var prefIdSelect = '';

                for (var i = 0; i < arr.length - 1; i++) {
                    prefIdSelect += arr[i] + '\\:';
                }
                var idSelect = prefIdSelect + field + '-select_input';
                var idLabel = prefIdSelect + field + '-select_label';

                $("#" + idSelect).val('com.origami.sgm.entities.CtlgItem:' + idItem + ':java.lang.Long');
                $("#" + idLabel).html($("#" + idSelect).find('option:selected').text());

            }

        }

    });

    $(document).on('change', 'div.compContainer select', function () {
        var value = $(this).val();
        var id = $(this).attr("id");
        var selector = document.getElementById(id.replace('_input', ''));
        var idInput = document.getElementById(id.replace('select_input', 'input'));
        var allValues = selector.getAttribute('data-all-values');
        var idsOrdenItems = allValues.split('-');
        var idItemArray = value.split(':');
        var idItemSeleccionado = idItemArray[1];
        var orden = -1;

        for (var i = 0; i < idsOrdenItems.length; i++) {
            var temp = idsOrdenItems[i].split(";");
            if (temp[0] === idItemSeleccionado) {
                orden = temp[1];
                break;
            }
        }
        idInput.value = (orden === -1 ? '' : parseInt(orden));
    });
});


//PrimeFaces.locales['en_US'] = {
//    closeText: 'Cerrar',
//    prevText: 'Anterior',
//    nextText: 'Siguiente',
//    monthNames: ['Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio', 'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'],
//    monthNamesShort: ['Ene', 'Feb', 'Mar', 'Abr', 'May', 'Jun', 'Jul', 'Ago', 'Sep', 'Oct', 'Nov', 'Dic'],
//    dayNames: ['Domingo', 'Lunes', 'Martes', 'Miércoles', 'Jueves', 'Viernes', 'Sábado'],
//    dayNamesShort: ['Dom', 'Lun', 'Mar', 'Mie', 'Jue', 'Vie', 'Sab'],
//    dayNamesMin: ['D', 'L', 'M', 'X ', 'J', 'V ', 'S'],
//    weekHeader: 'Semana',
//    firstDay: 0,
//    isRTL: false,
//    showMonthAfterYear: false,
//    yearSuffix: '',
//    timeOnlyTitle: 'Solo hora',
//    timeText: 'Hora',
//    hourText: 'Hora',
//    minuteText: 'Minuto',
//    secondText: 'Segundo',
//    currentText: 'Fecha actual',
//    ampm: false,
//    month: 'Mes',
//    week: 'Semana',
//    day: 'Día',
//    allDayText: 'Día completo'
//};

//PrimeFaces.locales['es'] = {
//    closeText: 'Cerrar',
//    prevText: 'Anterior',
//    nextText: 'Siguiente',
//    monthNames: ['Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio', 'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'],
//    monthNamesShort: ['Ene', 'Feb', 'Mar', 'Abr', 'May', 'Jun', 'Jul', 'Ago', 'Sep', 'Oct', 'Nov', 'Dic'],
//    dayNames: ['Domingo', 'Lunes', 'Martes', 'Miércoles', 'Jueves', 'Viernes', 'Sábado'],
//    dayNamesShort: ['Dom', 'Lun', 'Mar', 'Mie', 'Jue', 'Vie', 'Sab'],
//    dayNamesMin: ['D', 'L', 'M', 'X', 'J', 'V', 'S'],
//    weekHeader: 'Semana',
//    firstDay: 1,
//    isRTL: false,
//    showMonthAfterYear: false,
//    yearSuffix: '',
//    timeOnlyTitle: 'Sólo hora',
//    timeText: 'Tiempo',
//    hourText: 'Hora',
//    minuteText: 'Minuto',
//    secondText: 'Segundo',
//    currentText: 'Fecha actual',
//    ampm: false,
//    month: 'Mes',
//    week: 'Semana',
//    day: 'Día',
//    allDayText: 'Todo el día'
//};

// Blue : #3E98D3  |  Red : #EF3F61  |  Green : #2BB673  |  Orange : #F15732
//
function skinChart() {
    this.cfg.shadow = false;
    this.cfg.title = '';
    this.cfg.seriesColors = ['#3E98D3', '#EF3F61', '#2BB673', '#F15732', '#5dd5de', '#f02480', '#9068be', '#6ed3cf'];
    this.cfg.grid = {
        background: '#ffffff',
        borderColor: '#ffffff',
        gridLineColor: '#F5F5F5',
        shadow: false
    };
    this.cfg.axesDefaults = {
        rendererOptions: {
            textColor: '#666F77'
        }
    };
    this.cfg.seriesDefaults = {
        shadow: false,
        lineWidth: 1,
        markerOptions: {
            shadow: false,
            size: 7,
            style: 'circle'
        }
    }
}

function skinBar() {
    this.cfg.shadow = false;
    this.cfg.title = '';
    this.cfg.seriesColors = ['#3E98D3', '#EF3F61', '#2BB673', '#F15732', '#5dd5de', '#f02480', '#9068be', '#6ed3cf'];
    this.cfg.grid = {
        background: '#ffffff',
        borderColor: '#ffffff',
        gridLineColor: '#F5F5F5',
        shadow: false
    };
    this.cfg.axesDefaults = {
        rendererOptions: {
            textColor: '#666F77'
        }
    };
    this.cfg.seriesDefaults = {
        shadow: false,
        lineWidth: 1,
        renderer: $.jqplot.BarRenderer,
        markerOptions: {
            shadow: false,
            size: 7,
            style: 'circle'
        }
    }
}

function skinArea() {
    this.cfg.shadow = false;
    this.cfg.title = '';
    this.cfg.seriesColors = ['#EF3F61', '#2BB673'];
    this.cfg.grid = {
        background: '#ffffff',
        borderColor: '#ffffff',
        gridLineColor: '#F5F5F5',
        shadow: false
    };
    this.cfg.axesDefaults = {
        rendererOptions: {
            textColor: '#666F77'
        }
    };
    this.cfg.seriesDefaults = {
        shadow: false,
        lineWidth: 1,
        markerOptions: {
            shadow: false,
            size: 7,
            style: 'circle'
        }
    }
}

function skinBubble() {
    this.cfg.shadow = false;
    this.cfg.title = '';
    this.cfg.seriesColors = ['#2BB673', '#F15732'];
    this.cfg.grid = {
        background: '#ffffff',
        borderColor: '#ffffff',
        gridLineColor: '#F5F5F5',
        shadow: false
    };
    this.cfg.axesDefaults = {
        rendererOptions: {
            textColor: '#666F77'
        }
    };
    this.cfg.seriesDefaults = {
        shadow: false,
        lineWidth: 1,
        renderer: $.jqplot.BubbleRenderer,
        markerOptions: {
            shadow: false,
            size: 7,
            style: 'circle'
        }
    }
}

function skinZoom() {
    this.cfg.shadow = false;
    this.cfg.title = '';
    this.cfg.seriesColors = ['#2BB673', '#F15732'];
    this.cfg.grid = {
        background: '#ffffff',
        borderColor: '#ffffff',
        gridLineColor: '#F5F5F5',
        shadow: false
    };
    this.cfg.axesDefaults = {
        rendererOptions: {
            textColor: '#666F77'
        }
    };
    this.cfg.seriesDefaults = {
        shadow: false,
        lineWidth: 1,
        markerOptions: {
            shadow: false,
            size: 7,
            style: 'circle'
        }
    }
}
//
function skinPie() {
    this.cfg.shadow = false;
    this.cfg.title = '';
    this.cfg.seriesColors = ['#3E98D3', '#EF3F61', '#2BB673', '#F15732'];
    //this.cfg.seriesColors = ['#69D2E7', '#DD6161', '#D8DEC3','#CDC1CF','#A7DBD8','#83AF9B','#D68189'];
    this.cfg.grid = {
        background: '#ffffff',
        borderColor: '#ffffff',
        gridLineColor: '#F5F5F5',
        color: '#000000',
        shadow: false
    };
    this.cfg.axesDefaults = {
        rendererOptions: {
            textColor: '#666F77',
        }
    };
    this.cfg.seriesDefaults = {
        renderer: $.jqplot.PieRenderer,
        shadow: false,
        lineWidth: 1,
        markerOptions: {
            shadow: false,
            size: 7,
            style: 'circle'
        }
    }
}

function skinDonut() {
    this.cfg.shadow = false;
    this.cfg.title = '';
    this.cfg.seriesColors = ['#3E98D3', '#EF3F61', '#2BB673', '#F15732'];
    this.cfg.grid = {
        background: '#ffffff',
        borderColor: '#ffffff',
        gridLineColor: '#F5F5F5',
        shadow: false
    };
    this.cfg.axesDefaults = {
        rendererOptions: {
            textColor: '#666F77',
        }
    };
    this.cfg.seriesDefaults = {
        renderer: $.jqplot.DonutRenderer,
        shadow: false,
        lineWidth: 1,
        markerOptions: {
            shadow: false,
            size: 7,
            style: 'circle'
        }
    }
}

function skinBarAndLine() {
    this.cfg.shadow = false;
    this.cfg.title = '';
    this.cfg.seriesColors = ['#3E98D3', '#EF3F61'];
    this.cfg.grid = {
        background: '#ffffff',
        borderColor: '#ffffff',
        gridLineColor: '#F5F5F5',
        shadow: false
    };
    this.cfg.axesDefaults = {
        rendererOptions: {
            textColor: '#666F77'
        }
    };
    this.cfg.seriesDefaults = {
        shadow: false,
        lineWidth: 5,
        renderer: $.jqplot.BarRenderer,
        markerOptions: {
            shadow: false,
            size: 7,
            style: 'circle'
        }
    }
}

function skinMeterGauge() {
    this.cfg.title = '';
    this.cfg.seriesDefaults = {
        shadow: false,
        lineWidth: 5,
        renderer: $.jqplot.MeterGaugeRenderer,
        rendererOptions: {
            shadow: false,
            min: 100,
            max: 800,
            intervals: [200, 300, 400, 500, 600, 700, 800],
            intervalColors: ['#3E98D3', '#EF3F61', '#2BB673', '#F15732', '#3E98D3', '#EF3F61', '#2BB673']
        }
    }
}

function skinMultiAxis() {
    this.cfg.shadow = false;
    this.cfg.title = '';
    this.cfg.seriesColors = ['#2BB673', '#F15732'];
    this.cfg.grid = {
        background: '#ffffff',
        borderColor: '#ffffff',
        gridLineColor: '#F5F5F5',
        shadow: false
    };
    this.cfg.axesDefaults = {
        rendererOptions: {
            textColor: '#666F77'
        }
    };
    this.cfg.seriesDefaults = {
        shadow: false,
        lineWidth: 1,
        renderer: $.jqplot.BarRenderer,
        markerOptions: {
            shadow: false,
            size: 7,
            style: 'circle'
        }
    }
}