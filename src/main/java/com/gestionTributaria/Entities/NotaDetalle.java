/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Entities;

import com.asgard.Entity.FinaRenLiquidacion;
import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author DEVELOPER
 */
@Entity
@Table(name = "nota_detalle", schema = Utils.SCHEMA_SGM)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "NotaDetalle.findAll", query = "SELECT n FROM NotaDetalle n"),
    @NamedQuery(name = "NotaDetalle.findById", query = "SELECT n FROM NotaDetalle n WHERE n.id = :id"),
    @NamedQuery(name = "NotaDetalle.findByFechaIngreso", query = "SELECT n FROM NotaDetalle n WHERE n.fechaIngreso = :fechaIngreso"),
    @NamedQuery(name = "NotaDetalle.findByLiquidacion", query = "SELECT n FROM NotaDetalle n WHERE n.liquidacion = :liquidacion"),
    @NamedQuery(name = "NotaDetalle.findByUsuarioCreacion", query = "SELECT n FROM NotaDetalle n WHERE n.usuarioCreacion = :usuarioCreacion")})
public class NotaDetalle implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "fecha_ingreso")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaIngreso;
    @JoinColumn(name = "liquidacion", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private FinaRenLiquidacion liquidacion;
    @Size(max = 25)
    @Column(name = "usuario_creacion")
    private String usuarioCreacion;
    @JoinColumn(name = "id_nota", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private NotasCredito idNota;
    @Column(name = "comprobante")
    private String comprobante;
    @Column(name = "fecha_pago")
    private Date fechaPago;
    @Column(name = "valor")
    private BigDecimal valor;

    public NotaDetalle() {
    }

    public NotaDetalle(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public FinaRenLiquidacion getLiquidacion() {
        return liquidacion;
    }

    public void setLiquidacion(FinaRenLiquidacion liquidacion) {
        this.liquidacion = liquidacion;
    }
    
    
    public String getUsuarioCreacion() {
        return usuarioCreacion;
    }

    public void setUsuarioCreacion(String usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
    }

    public NotasCredito getIdNota() {
        return idNota;
    }

    public void setIdNota(NotasCredito idNota) {
        this.idNota = idNota;
    }

    public String getComprobante() {
        return comprobante;
    }

    public void setComprobante(String comprobante) {
        this.comprobante = comprobante;
    }

    public Date getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(Date fechaPago) {
        this.fechaPago = fechaPago;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NotaDetalle)) {
            return false;
        }
        NotaDetalle other = (NotaDetalle) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.educacion.Models.NotaDetalle[ id=" + id + " ]";
    }
    
}
