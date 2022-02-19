package com.origami.sigef.tesoreria.comprobantelectronico.sri.util;

public class Modulo11 {

    //public static String cadena;
    public Modulo11() {
    }

    private static String invertirCadena(String cadena) {
        String cadenaInvertida = "";
        for (int x = cadena.length() - 1; x >= 0; x--) {
            cadenaInvertida = cadenaInvertida + cadena.charAt(x);
        }
        return cadenaInvertida;
    }

    public Integer digitoVerificador(String cadena) {
        cadena = Modulo11.invertirCadena(cadena);
        int pivote = 2;
        int longitudCadena = cadena.length();
        Integer cantidadTotal = 0;
        int b = 1;
        for (int i = 0; i < longitudCadena; i++) {
            if (pivote == 8) {
                pivote = 2;
            }
            int temporal = Integer.parseInt(cadena.substring(i, b));
            b++;
            temporal *= pivote;
            pivote++;
            cantidadTotal += temporal;
        }
        cantidadTotal = 11 - cantidadTotal % 11;
        switch (cantidadTotal) {
            case 10:
                cantidadTotal = 1;
                break;
            case 11:
                cantidadTotal = 0;
                break;
        }
        return cantidadTotal;
    }

}
