/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.talentohumano.UtilsTH;

import com.origami.sigef.common.entities.TipoRol;
import com.origami.sigef.common.util.Utils;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 *
 * @author ORIGAMI2
 */
public class TalentoHumano {

    public static Integer diasCalendarioLaboral = 30;
    public static Integer HORAS_LABORABLES = 8;
    public static Integer DIAS_LABORABLES = 5;
    public static Integer DIAS_SEMANA = 7;
    public static Integer horasExtrasLOSEP = 60;
    public static Integer horasExtrasCT = 120;
    public static Integer horasSuplementariasLOSEP = 60;
    public static Integer horasSuplementariasCT = 48;
    public static Integer maximoHorasExtras = 168;
    public static Integer ANIOS_MAYOR_EDAD = 65;
    public static String HORAS_SIN_DESCANSO = "9:10";
    public static String HORAS_DESCANSO = "8:10";

    public static Integer getUltimoDiaMes(TipoRol rol) {
        int mesNum, diaMax;
        switch (rol.getMes().getCodigo().toUpperCase()) {
            case "ENERO":
                mesNum = 0;
                diaMax = obtenerUltimoDiaMes(rol.getAnio().intValue(), mesNum);
                break;
            case "FEBRERO":
                mesNum = 1;
                diaMax = obtenerUltimoDiaMes(rol.getAnio().intValue(), mesNum);
                break;
            case "MARZO":
                mesNum = 2;
                diaMax = obtenerUltimoDiaMes(rol.getAnio().intValue(), mesNum);
                break;
            case "ABRIL":
                mesNum = 3;
                diaMax = obtenerUltimoDiaMes(rol.getAnio().intValue(), mesNum);
                break;
            case "MAYO":
                mesNum = 4;
                diaMax = obtenerUltimoDiaMes(rol.getAnio().intValue(), mesNum);
                break;
            case "JUNIO":
                mesNum = 5;
                diaMax = obtenerUltimoDiaMes(rol.getAnio().intValue(), mesNum);
                break;
            case "JULIO":
                mesNum = 6;
                diaMax = obtenerUltimoDiaMes(rol.getAnio().intValue(), mesNum);
                break;
            case "AGOSTO":
                mesNum = 7;
                diaMax = obtenerUltimoDiaMes(rol.getAnio().intValue(), mesNum);
                break;
            case "SEPTIEMBRE":
                mesNum = 8;
                diaMax = obtenerUltimoDiaMes(rol.getAnio().intValue(), mesNum);
                break;
            case "OCTUBRE":
                mesNum = 9;
                diaMax = obtenerUltimoDiaMes(rol.getAnio().intValue(), mesNum);
                break;
            case "NOVIEMBRE":
                mesNum = 10;
                diaMax = obtenerUltimoDiaMes(rol.getAnio().intValue(), mesNum);
                break;
            default:
                mesNum = 11;
                diaMax = obtenerUltimoDiaMes(rol.getAnio().intValue(), mesNum);
                break;
        }
        return diaMax;
    }

    public static String getMesXInt(int mes) {
        String mesString;
        switch (mes) {
            case 1:
                mesString = "Enero";
                break;
            case 2:
                mesString = "Febrero";
                break;
            case 3:
                mesString = "Marzo";
                break;
            case 4:
                mesString = "Abril";
                break;
            case 5:
                mesString = "Mayo";
                break;
            case 6:
                mesString = "Junio";
                break;
            case 7:
                mesString = "Julio";
                break;
            case 8:
                mesString = "Agosto";
                break;
            case 9:
                mesString = "Septiembre";
                break;
            case 10:
                mesString = "Octubre";
                break;
            case 11:
                mesString = "Noviembre";
                break;
            default:
                mesString = "Diciembre";
                break;

        }
        return mesString.toUpperCase();
    }

    public static Integer getUltimoDiaMesString(String codigo, int anio) {
        int mesNum, diaMax;
        switch (codigo.toUpperCase()) {
            case "ENERO":
                mesNum = 0;
                diaMax = obtenerUltimoDiaMes(anio, mesNum);
                break;
            case "FEBRERO":
                mesNum = 1;
                diaMax = obtenerUltimoDiaMes(anio, mesNum);
                break;
            case "MARZO":
                mesNum = 2;
                diaMax = obtenerUltimoDiaMes(anio, mesNum);
                break;
            case "ABRIL":
                mesNum = 3;
                diaMax = obtenerUltimoDiaMes(anio, mesNum);
                break;
            case "MAYO":
                mesNum = 4;
                diaMax = obtenerUltimoDiaMes(anio, mesNum);
                break;
            case "JUNIO":
                mesNum = 5;
                diaMax = obtenerUltimoDiaMes(anio, mesNum);
                break;
            case "JULIO":
                mesNum = 6;
                diaMax = obtenerUltimoDiaMes(anio, mesNum);
                break;
            case "AGOSTO":
                mesNum = 7;
                diaMax = obtenerUltimoDiaMes(anio, mesNum);
                break;
            case "SEPTIEMBRE":
                mesNum = 8;
                diaMax = obtenerUltimoDiaMes(anio, mesNum);
                break;
            case "OCTUBRE":
                mesNum = 9;
                diaMax = obtenerUltimoDiaMes(anio, mesNum);
                break;
            case "NOVIEMBRE":
                mesNum = 10;
                diaMax = obtenerUltimoDiaMes(anio, mesNum);
                break;
            default:
                mesNum = 11;
                diaMax = obtenerUltimoDiaMes(anio, mesNum);
                break;
        }
        return diaMax;
    }

    public static Integer convertirMesLetraNum(String mes) {
        int mesNum;
        switch (mes.toUpperCase()) {
            case "ENERO":
                mesNum = 0;
                break;
            case "FEBRERO":
                mesNum = 1;
                break;
            case "MARZO":
                mesNum = 2;
                break;
            case "ABRIL":
                mesNum = 3;
                break;
            case "MAYO":
                mesNum = 4;
                break;
            case "JUNIO":
                mesNum = 5;
                break;
            case "JULIO":
                mesNum = 6;
                break;
            case "AGOSTO":
                mesNum = 7;
                break;
            case "SEPTIEMBRE":
                mesNum = 8;
                break;
            case "OCTUBRE":
                mesNum = 9;
                break;
            case "NOVIEMBRE":
                mesNum = 10;
                break;
            default:
                mesNum = 11;
                break;
        }
        return mesNum;
    }

    public static String convertirMesByNumString(String mes) {
        String mesNum;
        switch (mes.toUpperCase()) {
            case "ENERO":
                mesNum = "01";
                break;
            case "FEBRERO":
                mesNum = "02";
                break;
            case "MARZO":
                mesNum = "03";
                break;
            case "ABRIL":
                mesNum = "04";
                break;
            case "MAYO":
                mesNum = "05";
                break;
            case "JUNIO":
                mesNum = "06";
                break;
            case "JULIO":
                mesNum = "07";
                break;
            case "AGOSTO":
                mesNum = "08";
                break;
            case "SEPTIEMBRE":
                mesNum = "09";
                break;
            case "OCTUBRE":
                mesNum = "10";
                break;
            case "NOVIEMBRE":
                mesNum = "11";
                break;
            default:
                mesNum = "12";
                break;
        }
        return mesNum;
    }

    public static Integer obtenerUltimoDiaMes(int anio, int mes) {
        Calendar cal = Calendar.getInstance();
        cal.set(anio, mes, 1);
        return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    public static Date fechaInicio(Short anio, int dia, int mes) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date fecha = new Date();
        String aux = dia + "/" + mes + "/" + anio;
        try {
            fecha = sdf.parse(aux);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fecha;
    }

    public static Date fechaCierre(Short anio, int mes) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date fecha = new Date();
//        int anio = a.intValue() + 1;
        String aux = obtenerUltimoDiaMes(anio, mes - 1) + "/" + mes + "/" + anio;
        try {
            fecha = sdf.parse(aux);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return fecha;
    }

    public static boolean validarFechaInicio(Date fechaIni, TipoRol r) {
        int mes, anio, mesRol;
        mes = Utils.getMes(fechaIni) - 1;
        anio = Utils.getAnio(fechaIni);
        mesRol = Utils.convertirLetraAMes(r.getMes().getCodigo().toUpperCase()) - 1;
        if (anio < r.getAnio().intValue()) {
            return true;
        }
        return anio == r.getAnio().intValue() && mes <= mesRol;
    }

    public static boolean validarFechaInicio2(Date fechaIni, TipoRol r) {
        int mes, anio, mesRol;
        mes = Utils.getMes(fechaIni);
        anio = Utils.getAnio(fechaIni);
        mesRol = Utils.convertirLetraAMes(r.getMes().getCodigo().toUpperCase()) - 1;
        if (anio < r.getAnio().intValue()) {
            return true;
        }
        return anio == r.getAnio().intValue() && mes <= mesRol;
    }

    public static boolean validarFechaCuota(Date fechaIni, TipoRol r) {
        int mes, anio, mesRol;
        mes = Utils.getMes(fechaIni) - 1;
        anio = Utils.getAnio(fechaIni);
        mesRol = Utils.convertirLetraAMes(r.getMes().getCodigo().toUpperCase()) - 1;
        if (anio > r.getAnio().intValue()) {
            return false;
        }
        return (anio <= r.getAnio().intValue() && mes == mesRol);
    }

    public static boolean validarFechaFin(Date fechaFin, TipoRol r) {
        int mes, anio, mesRol;
        mes = Utils.getMes(fechaFin) - 1;
        anio = Utils.getAnio(fechaFin);
        mesRol = Utils.convertirLetraAMes(r.getMes().getCodigo().toUpperCase()) - 1;
        if (anio > r.getAnio().intValue()) {
            return true;
        }
        return anio == r.getAnio().intValue() && mes >= mesRol;
    }

    public static boolean validarFechaFin2(Date fechaFin, TipoRol r) {
        int mes, anio, mesRol;
        mes = Utils.getMes(fechaFin) + 1;
        anio = Utils.getAnio(fechaFin);
        mesRol = Utils.convertirLetraAMes(r.getMes().getCodigo().toUpperCase()) - 1;
        System.out.println("mes fecha --> " + mes + " mes rol ---> " + mesRol);
        System.out.println("anio fecha --> " + anio + " anio rol --> " + r.getAnio());
        if (anio > r.getAnio().intValue()) {
            return true;
        }
        return anio == r.getAnio().intValue() && mes >= mesRol;
    }

    /**
     * Método que calcula los meses que han pasado dese fecha inicio hasta fecha
     * fin.
     *
     * @param fechaInicio: fecha de inicio de comparación.
     * @param fechaFin: fecha de finalización de comparación.
     * @return 0 si no ha pasado un mes o si se presenta alguna exepción. > 0 si
     * han pasado almenos un mes.
     */
    public static int calcularMesesAFecha(Date fechaInicio, Date fechaFin) {
        try {
            //Fecha inicio en objeto Calendar
            Calendar startCalendar = Calendar.getInstance();
            startCalendar.setTime(fechaInicio);
            //Fecha finalización en objeto Calendar
            Calendar endCalendar = Calendar.getInstance();
            endCalendar.setTime(fechaFin);
            //Cálculo de meses para las fechas de inicio y finalización
            int startMes = (startCalendar.get(Calendar.YEAR) * 12) + startCalendar.get(Calendar.MONTH);
            int endMes = (endCalendar.get(Calendar.YEAR) * 12) + endCalendar.get(Calendar.MONTH);
            //Diferencia en meses entre las dos fechas
            int diffMonth = endMes - startMes;
            //Si la el día de la fecha de finalización es menor que el día de la fecha inicio
            //se resta un mes, puesto que no estaria cumpliendo otro periodo.
            //Para esto ocupo el metoddo ponerAnioMesActual
            Date aFecha = ponerAnioMesActual(fechaInicio, fechaFin).getTime();
            if (formatearDate(fechaFin, "dd/MM/yyyy").compareTo(
                    formatearDate(aFecha, "dd/MM/yyyy")) < 0) {
                diffMonth = diffMonth - 1;
            }
            //Si la fecha de finalización es menor que la fecha de inicio, retorno que los meses
            // transcurridos entre las dos fechas es 0
            if (diffMonth < 0) {
                diffMonth = 0;
            }
            return diffMonth;
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Método que formatea un afecha en base al formato pasado como parámetro.
     *
     * @param fecha: fecha a formatear
     * @param pattern: formato que se dará a la fecha.
     * @return Fecha formateada en base al formato de pattern. null si se
     * presenta alguna excepción
     */
    public static Date formatearDate(Date fecha, String pattern) {
        SimpleDateFormat formato = new SimpleDateFormat(pattern);
        Date fechaFormateada = null;
        try {
            fechaFormateada = formato.parse(formato.format(fecha));
            return fechaFormateada;
        } catch (Exception ex) {
            return fechaFormateada;
        }
    }

    public static Date ParseFecha(String fecha, String pattern) {
        SimpleDateFormat formato = new SimpleDateFormat(pattern);
        Date fechaDate = null;
        try {
            fechaDate = new Date(formato.parse(fecha).getTime());
            return fechaDate;
        } catch (ParseException ex) {
            System.out.println(ex);
            return null;
        }
    }

    public static Timestamp convertStringToTimestamp(String str_date, String pattern) {
        try {
            DateFormat formatter;
            formatter = new SimpleDateFormat(pattern);
            Date date = formatter.parse(str_date);
            java.sql.Timestamp timeStampDate = new Timestamp(date.getTime());
            return timeStampDate;
        } catch (ParseException e) {
            System.out.println("Exception :" + e);
            return null;
        }
    }

    /**
     * Método que remplaza el año y el mes de fecha y pone el año y mes de
     * fechaActual
     *
     * @param fecha: fecha a remplazar el mes y el año
     * @param fechaActual: fecha de la cual se tomara el mes y el año y se
     * colocara en fecha
     * @return Calendar con la nueva fecha calculada.
     */
    public static Calendar ponerAnioMesActual(Date fecha, Date fechaActual) {
        try {
            Calendar cActual = Calendar.getInstance();
            cActual.setTime(fechaActual);
            Calendar cfecha = Calendar.getInstance();
            cfecha.setTime(fecha);
            //la fecha nueva
            Calendar c1 = Calendar.getInstance();
            c1.set(cActual.get(Calendar.YEAR), cActual.get(Calendar.MONTH), cfecha.get(Calendar.DATE));
            return c1;
        } catch (Exception e) {
            return null;
        }
    }

    public static Integer diasDiferencia(Date fechaini, Date fechaFin) {
        int dias = (int) ((fechaFin.getTime() - fechaini.getTime()) / 86400000);
        return dias;
    }

//    public static Date ParseFecha(String fecha, String formatoFecha) {
//        SimpleDateFormat formato = new SimpleDateFormat(formatoFecha);
//        Date fechaDate = null;
//        try {
//            fechaDate = formato.parse(fecha);
//        } catch (ParseException ex) {
//            System.out.println(ex);
//        }
//        return fechaDate;
//    }
    public static boolean isBisiesto(short anio) {
        GregorianCalendar calendar = new GregorianCalendar();
        boolean esBisiesto = false;
        if (calendar.isLeapYear(anio)) {
            esBisiesto = true;
        }
        return esBisiesto;
    }

    public static int diasHabilesEntreFechas(Date startDate, Date endDate) {
        Calendar startCal = Calendar.getInstance();
        startCal.setTime(startDate);
        Calendar endCal = Calendar.getInstance();
        endCal.setTime(endDate);
        int workDays = 0;
        //Return 0 if start and end are the same
        if (startCal.getTimeInMillis() == endCal.getTimeInMillis()) {
            return 0;
        }
        if (startCal.getTimeInMillis() > endCal.getTimeInMillis()) {
            startCal.setTime(endDate);
            endCal.setTime(startDate);
        }
        do {
            //excluding start date
            startCal.add(Calendar.DAY_OF_MONTH, 1);
            if (startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                ++workDays;
            }
        } while (startCal.getTimeInMillis() < endCal.getTimeInMillis()); //excluding end date

        return workDays;
    }

    public static Integer getDayOfTheWeek(Date d) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(d);
        return cal.get(Calendar.DAY_OF_WEEK);
    }
}
