/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.ats.modelAts;

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
@XmlType(name = "ventas", propOrder = {"detalleVentas"})
@XmlRootElement
public class Ventas {

    private List<DetalleVentas> detalleVentas;

    public Ventas() {
    }

    public Ventas(List<DetalleVentas> detalleVentas) {
        this.detalleVentas = detalleVentas;
    }

    public List<DetalleVentas> getDetalleVentas() {
        return detalleVentas;
    }

    public void setDetalleVentas(List<DetalleVentas> detalleVentas) {
        this.detalleVentas = detalleVentas;
    }

}
