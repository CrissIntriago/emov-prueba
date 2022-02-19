/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.ats.modelAts;

import java.io.Serializable;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author ORIGAMI
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "formasDePago", propOrder = {"formaPago"})
@XmlRootElement
public class FormasPagoModel implements Serializable {

    @XmlElement
    private List<String> formaPago;

    public FormasPagoModel() {
    }

    public FormasPagoModel(List<String> formaPagoModel) {
        this.formaPago = formaPagoModel;
    }

//<editor-fold defaultstate="collapsed" desc="Get and Set">
    public List<String> getFormaPago() {
        return formaPago;
    }

    public void setFormaPago(List<String> formaPagoModel) {
        this.formaPago = formaPagoModel;
    }

    @Override
    public String toString() {
        return "FormasPagoModel{" + "formaPagoModel=" + formaPago + '}';
    }

//</editor-fold>
}
