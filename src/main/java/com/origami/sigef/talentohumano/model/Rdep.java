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
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "rdep", propOrder = {"numRuc", "anio", "retRelDep"})
@XmlRootElement
public class Rdep {

    private String numRuc;
    private Integer anio;
    private RetRelDep retRelDep;

    public Rdep() {
    }

    public String getNumRuc() {
        return numRuc;
    }

    public void setNumRuc(String numRuc) {
        this.numRuc = numRuc;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public RetRelDep getRetRelDep() {
        return retRelDep;
    }

    public void setRetRelDep(RetRelDep retRelDep) {
        this.retRelDep = retRelDep;
    }

}
