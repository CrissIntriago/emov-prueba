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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author ORIGAMI
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "compras", propOrder = {"detalleCompras"})
@XmlRootElement
public class Compras implements Serializable {

    private List<DetalleCompras> detalleCompras;

    public Compras() {
    }

    public Compras(List<DetalleCompras> detalleCompras) {
        this.detalleCompras = detalleCompras;
    }

    public List<DetalleCompras> getDetalleCompras() {
        return detalleCompras;
    }

    public void setDetalleCompras(List<DetalleCompras> detalleCompras) {
        this.detalleCompras = detalleCompras;
    }

}
