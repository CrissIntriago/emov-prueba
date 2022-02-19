/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.config;

import java.math.BigInteger;

/**
 *
 * @author Dairon Freddy
 */
public class CONFIG {

    public static final String PERSISTENCE_UNIT = "persistence-sigef";
    public static final String PERSISTENCE = "persistence-olicontrol";
    public static final String PERSISTENCE_BIOTIME = "persistence-biotime";
    public static String SERVICE_USER;
    public static String SERVICE_PASS;
    public static String SERVICE_URL;
    public static String URL_APP;
    public static int IVA;
    public static BigInteger TIEMPO_EXPIRACION = BigInteger.ONE;
    public static String ESTADO_ACTIVO_REG = "ACT";
    public static String ESTADO_INACTIVO_REG = "IN";
    public static String ESTADO_PENDIENTE_REG = "PEN";
//    public static String appQrServiceDesarrollo = "http://localhost:8090/origamigt/api/ajax/";
//    public static String appAtlantisUrlDesarrollo = "http://186.101.220.187:8780/atlantis/api/";
    public static String FACTURA_AMBIENTE;
    public static String IP_TC_WEB = "http://186.101.220.187:8380";
    public static String IP_TC_WEB1 = "http://186.101.220.187:8380";
    public static String PARAMETER_TIPO = "tipoBeneficiario";
    public static String PARAMETER_RENDER = "renderSeleccionar";
    public static String ONE_TYPE = "one_type";
    public static String PLAN_CUENTA_CONTABLE = "plan_cuenta_contable";
    public static String PLAN_ITEM_PRESUPUESTARIO = "plan_item_presupuestario";
    public static String CODE_CARGO = "CODE_CARGO";
    public static String RESTRINGIR_DEBE = "RESTRINGIR_DEBE";
    public static String RESTRINGIR_HABER = "RESTRINGIR_HABER";
    public static String COD_DEVENGADO = "diario_general_devengado";
    public static String COD_EJECUTADO = "diario_general_ejecucion";
    public static String SIGLAS_INSTITUCION = "DURÁN";
    //CONFIGURACION DE LOS RUBROS DE TALENTO HUMANO
    public static String CONFIG_DECIMO_TERCERO = "CONFIG_DECIMO_TERCERO";
    public static String CONFIG_DECIMO_CUARTO = "CONFIG_DECIMO_CUARTO";
    public static String CONFIG_FONDOS_RESERVA = "CONFIG_FONDOS_RESERVA";
    public static String CONFIG_PRES_QUIROGRAFARIO = "CONFIG_PRES_QUIROGRAFARIO";
    public static String CONFIG_PRES_HIPOTECARIO = "CONFIG_PRES_HIPOTECARIO";
    public static String CONFIG_TH_DESCUENTO = "CONFIG_TH_DESCUENTO";
    public static String CONFIG_TH_ANTICIPO = "CONFIG_TH_ANTICIPO";
    public static String CONFIG_TH_IMPUESTO_RENTA = "CONFIG_TH_IMPUESTO_RENTA";
    public static String CONFIG_TH_CAUCIONES = "CONFIG_TH_CAUCIONES";
    public static String CONFIG_TH_BENEFICIOS_SINDICALES = "CONFIG_TH_BENEFICIOS_SINDICALES";
    public static String CONFIG_APORTE_IESS_LOSEP = "CONFIG_APORTE_IESS_LOSEP";
    public static String CONFIG_APORTE_IESS_CODIGO = "CONFIG_APORTE_IESS_CODIGO";
    public static String MAX_PORCENJATE_RETENCION = "MAX_PORCENJATE_RETENCION";
    public static String CONFIG_TH_HORAS_EXTRAS = "CONFIG_TH_HORAS_EXTRAS";
    public static String VALOR_STATIC_HORAS = "VALOR_STATIC_HORAS";
    public static String CONFIG_PH = "CONFIG_PH";
    public static String CONFIG_PQ = "CONFIG_PQ";

}
