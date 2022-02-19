/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.model;

import java.io.Serializable;

/**
 *
 * @author ENRIQUE
 */
public class PlanLocalFormat implements Serializable{
    private String codSis;
    private String dscSistema;
    private String codObj;
    private String descObjetivo;
    private String codPol;
    private String dscPolitica;

    public PlanLocalFormat() {
    }

    public PlanLocalFormat(String codSis, String dscSistema, String codObj, String descObjetivo, String codPol, String dscPolitica) {
        this.codSis = codSis;
        this.dscSistema = dscSistema;
        this.codObj = codObj;
        this.descObjetivo = descObjetivo;
        this.codPol = codPol;
        this.dscPolitica = dscPolitica;
    }

    public String getCodSis() {
        return codSis;
    }
    
    public void setCodSis(String codSis) {
        this.codSis = codSis;
    }

    public String getDscSistema() {
        return dscSistema;
    }

    public void setDscSistema(String dscSistema) {
        this.dscSistema = dscSistema;
    }

    public String getCodObj() {
        return codObj;
    }

    public void setCodObj(String codObj) {
        this.codObj = codObj;
    }

    public String getDescObjetivo() {
        return descObjetivo;
    }

    public void setDescObjetivo(String descObjetivo) {
        this.descObjetivo = descObjetivo;
    }

    public String getCodPol() {
        return codPol;
    }

    public void setCodPol(String codPol) {
        this.codPol = codPol;
    }

    public String getDscPolitica() {
        return dscPolitica;
    }

    public void setDscPolitica(String dscPolitica) {
        this.dscPolitica = dscPolitica;
    }
    
}
