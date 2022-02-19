/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.talentohumano.model;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author jesus
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "retRelDep", propOrder = {"datRetRelDep"})
@XmlRootElement
public class RetRelDep {

    private List<DatRetRelDep> datRetRelDep;

    public RetRelDep() {
    }

    public List<DatRetRelDep> getDatRetRelDep() {
        return datRetRelDep;
    }

    public void setDatRetRelDep(List<DatRetRelDep> datRetRelDep) {
        this.datRetRelDep = datRetRelDep;
    }

}
