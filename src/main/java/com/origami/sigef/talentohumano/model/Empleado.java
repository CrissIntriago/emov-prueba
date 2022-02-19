/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.talentohumano.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author jesus
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "empleado", propOrder = {"benGalpg", "enfcatastro", "tipIdRet", "idRet", "apellidoTrab", "nombreTrab", "estab", "residenciaTrab", "paisResidencia",
    "aplicaConvenio", "tipoTrabajDiscap", "porcentajeDiscap", "tipIdDiscap", "idDiscap"})
public class Empleado {

    private String benGalpg;
    private String enfcatastro;
    private String tipIdRet;
    private String idRet;
    private String apellidoTrab;
    private String nombreTrab;
    private String estab;
    private String residenciaTrab;
    private String paisResidencia;
    private String aplicaConvenio;
    private String tipoTrabajDiscap;
    private Integer porcentajeDiscap;
    private String tipIdDiscap;
    private String idDiscap;

    public Empleado() {
    }

    public String getBenGalpg() {
        return benGalpg;
    }

    public void setBenGalpg(String benGalpg) {
        this.benGalpg = benGalpg;
    }

    public String getEnfcatastro() {
        return enfcatastro;
    }

    public void setEnfcatastro(String enfcatastro) {
        this.enfcatastro = enfcatastro;
    }

    public String getTipIdRet() {
        return tipIdRet;
    }

    public void setTipIdRet(String tipIdRet) {
        this.tipIdRet = tipIdRet;
    }

    public String getIdRet() {
        return idRet;
    }

    public void setIdRet(String idRet) {
        this.idRet = idRet;
    }

    public String getApellidoTrab() {
        return apellidoTrab;
    }

    public void setApellidoTrab(String apellidoTrab) {
        this.apellidoTrab = apellidoTrab;
    }

    public String getNombreTrab() {
        return nombreTrab;
    }

    public void setNombreTrab(String nombreTrab) {
        this.nombreTrab = nombreTrab;
    }

    public String getEstab() {
        return estab;
    }

    public void setEstab(String estab) {
        this.estab = estab;
    }

    public String getResidenciaTrab() {
        return residenciaTrab;
    }

    public void setResidenciaTrab(String residenciaTrab) {
        this.residenciaTrab = residenciaTrab;
    }

    public String getPaisResidencia() {
        return paisResidencia;
    }

    public void setPaisResidencia(String paisResidencia) {
        this.paisResidencia = paisResidencia;
    }

    public String getAplicaConvenio() {
        return aplicaConvenio;
    }

    public void setAplicaConvenio(String aplicaConvenio) {
        this.aplicaConvenio = aplicaConvenio;
    }

    public String getTipoTrabajDiscap() {
        return tipoTrabajDiscap;
    }

    public void setTipoTrabajDiscap(String tipoTrabajDiscap) {
        this.tipoTrabajDiscap = tipoTrabajDiscap;
    }

    public Integer getPorcentajeDiscap() {
        return porcentajeDiscap;
    }

    public void setPorcentajeDiscap(Integer porcentajeDiscap) {
        this.porcentajeDiscap = porcentajeDiscap;
    }

    public String getTipIdDiscap() {
        return tipIdDiscap;
    }

    public void setTipIdDiscap(String tipIdDiscap) {
        this.tipIdDiscap = tipIdDiscap;
    }

    public String getIdDiscap() {
        return idDiscap;
    }

    public void setIdDiscap(String idDiscap) {
        this.idDiscap = idDiscap;
    }

}
