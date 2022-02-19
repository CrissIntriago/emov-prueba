package com.origami.sigef.tesoreria.comprobantelectronico.sri.logic;

import com.origami.sigef.common.config.CONFIG;

public class ComprobantesCode {

    public static String FACTURA = "01";
    public static String NOTACREDITO = "04";
    public static String NOTADEBITO = "05";
    public static String GUIAREMISION = "06";
    public static String COMPPROBANTERETENCION = "07";
    public static String AMBIENTE = CONFIG.FACTURA_AMBIENTE == null ? "1" : CONFIG.FACTURA_AMBIENTE;
    public static Boolean ONLINE = Boolean.FALSE;

}
