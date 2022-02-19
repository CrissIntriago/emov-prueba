/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.ats.entities;

import com.origami.sigef.common.entities.Factura;
import com.origami.sigef.tesoreria.comprobantelectronico.entities.FormaPago;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author ORIGAMI
 */
@Entity
@Table(schema = "ats", name = "factura_formas_pago")
public class FacturaFormasPago implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @JoinColumn(name = "factura", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Factura factura;
    @JoinColumn(name = "formas_pago", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private FormaPago formaPago;

    public FacturaFormasPago() {
    }

    public FacturaFormasPago(Long id) {
        this.id = id;
    }

    public FacturaFormasPago(Factura factura, FormaPago formaPago) {
        this.factura = factura;
        this.formaPago = formaPago;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Factura getFactura() {
        return factura;
    }

    public void setFactura(Factura factura) {
        this.factura = factura;
    }

    public FormaPago getFormaPago() {
        return formaPago;
    }

    public void setFormaPago(FormaPago formaPago) {
        this.formaPago = formaPago;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.ats.entities.DetalleComprasFormasPago{" + "id=" + id + ","
                + " detalleCompras=" + factura + ", formaPago=" + formaPago + '}';
    }

}
