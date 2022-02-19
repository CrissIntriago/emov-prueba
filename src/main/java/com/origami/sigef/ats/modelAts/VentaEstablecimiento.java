/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.ats.modelAts;

import com.origami.sigef.contabilidad.model.VentaEst;
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
@XmlType(name = "ventasEstablecimiento", propOrder = {"ventaEst"})
@XmlRootElement
public class VentaEstablecimiento {

    @XmlElement(name = "ventaEst")
    private List<VentaEst> ventaEst;

    public VentaEstablecimiento() {
    }

    public VentaEstablecimiento(List<VentaEst> ventaEst) {
        this.ventaEst = ventaEst;
    }

   //<editor-fold defaultstate="collapsed" desc="Getter and Setter">
    public List<VentaEst> getVentaEst() {
        return ventaEst;
    }

    public void setVentaEst(List<VentaEst> ventaEst) {
        this.ventaEst = ventaEst;
    }
//</editor-fold>

}
