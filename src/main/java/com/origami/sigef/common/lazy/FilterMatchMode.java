/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.lazy;

/**
 *
 * @author Dairon Freddy
 */
public enum FilterMatchMode {

    /*
    lt: menos de
le: menor o igual que
eq: igual a igual a
ne: no es igual a
ge: mayor o igual que
gt: mayor que
     */
    STARTS_WITH("startsWith"),
    ENDS_WITH("endsWith"),
    CONTAINS("contains"),
    EXACT("exact"),
    LESS_THAN("lt"),
    LESS_THAN_EQUALS("lte"),
    GREATER_THAN("gt"),
    GREATER_THAN_EQUALS("gte"),
    EQUALS("equals"),
    NOT_EQUAL("ne"),
    IN("in");

    /**
     * Value of p:column's filterMatchMode attribute which corresponds to this
     * math mode
     */
    private final String uiParam;

    FilterMatchMode(String uiParam) {
        this.uiParam = uiParam;
    }

    /**
     * @param uiParam value of p:column's filterMatchMode attribute
     * @return MatchMode which corresponds to given UI parameter
     * @throws IllegalArgumentException if no MatchMode is corresponding to
     * given UI parameter
     */
    public static FilterMatchMode fromUiParam(String uiParam) {
        for (FilterMatchMode matchMode : values()) {
            if (matchMode.uiParam.equals(uiParam)) {
                return matchMode;
            }
        }
        throw new IllegalArgumentException("No MatchMode found for " + uiParam);
    }

}
