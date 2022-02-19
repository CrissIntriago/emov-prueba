/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.catastro.Models;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class EstadosPredio {

    /**
     * PREDIO ACTIVOS
     */
    public static String ACTIVO = "A";

    /**
     * EL PREDIO PASA AL ESTADO H QUE ES CONSIDERADO HISTORICO
     */
    public static String HISTORICO = "H";

    /**
     * PREDIOS INACTIVOS POR ALGUN MOTIVOS EN ESPECIAL.
     */
    public static String INACTIVO = "I";

    /**
     * PREDIO EN PROCESO DE DIVISION, FUSION Y DIVISION.
     */
    public static String PENDIENTE = "P";

    /**
     * PREDIO EN PROCESO DE EDICION TEMPORAL.
     */
    public static String TEMPORAL = "X";

    /**
     * PREDIO EN PROCESO DE EDICION TEMPORAL Y ACTIVOS.
     */
    public static List<String> ACTIVOS_TEMPORALES = Arrays.asList(ACTIVO, PENDIENTE);
}
