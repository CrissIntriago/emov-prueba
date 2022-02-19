/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.contabilidad.entities;

import com.origami.sigef.common.entities.CuentaContableRetencion;
import com.origami.sigef.common.entities.Factura;
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

/**
 *
 * @author Criss Intriago
 */
@Entity
@Table(name = "cont_factura_detalle", schema = "contabilidad")
@NamedQueries({
    @NamedQuery(name = "ContFacturaDetalle.findAll", query = "SELECT c FROM ContFacturaDetalle c"),
    @NamedQuery(name = "ContFacturaDetalle.findById", query = "SELECT c FROM ContFacturaDetalle c WHERE c.id = :id"),
    @NamedQuery(name = "ContFacturaDetalle.findByIdFactura", query = "SELECT c FROM ContFacturaDetalle c WHERE c.idFactura = ?1 AND c.estado = true"),
    @NamedQuery(name = "ContFacturaDetalle.findByIdConfRetencion", query = "SELECT c FROM ContFacturaDetalle c WHERE c.idConfRetencion = :idConfRetencion"),
    @NamedQuery(name = "ContFacturaDetalle.findByEstado", query = "SELECT c FROM ContFacturaDetalle c WHERE c.estado = :estado"),
    @NamedQuery(name = "ContFacturaDetalle.findByFechaCreacion", query = "SELECT c FROM ContFacturaDetalle c WHERE c.fechaCreacion = :fechaCreacion"),
    @NamedQuery(name = "ContFacturaDetalle.findByFechaModificacion", query = "SELECT c FROM ContFacturaDetalle c WHERE c.fechaModificacion = :fechaModificacion"),
    @NamedQuery(name = "ContFacturaDetalle.findByUsuarioCreacion", query = "SELECT c FROM ContFacturaDetalle c WHERE c.usuarioCreacion = :usuarioCreacion"),
    @NamedQuery(name = "ContFacturaDetalle.findByUsuarioModificacion", query = "SELECT c FROM ContFacturaDetalle c WHERE c.usuarioModificacion = :usuarioModificacion")})
public class ContFacturaDetalle implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @JoinColumn(name = "id_factura", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Factura idFactura;
    @JoinColumn(name = "id_conf_retencion", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CuentaContableRetencion idConfRetencion;
    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.DATE)
    private Date fechaCreacion;
    @Column(name = "fecha_modificacion")
    @Temporal(TemporalType.DATE)
    private Date fechaModificacion;
    @Size(max = 2147483647)
    @Column(name = "usuario_creacion")
    private String usuarioCreacion;
    @Size(max = 2147483647)
    @Column(name = "usuario_modificacion")
    private String usuarioModificacion;
    @Column(name = "valor_detalle")
    private BigDecimal valorDetalle;
    @Column(name = "valor_base")
    private BigDecimal valorBase;

    public ContFacturaDetalle() {
        this.estado = Boolean.TRUE;
        this.valorDetalle = BigDecimal.ZERO;
        this.valorBase = BigDecimal.ZERO;
    }

    public ContFacturaDetalle(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Factura getIdFactura() {
        return idFactura;
    }

    public void setIdFactura(Factura idFactura) {
        this.idFactura = idFactura;
    }

    public CuentaContableRetencion getIdConfRetencion() {
        return idConfRetencion;
    }

    public void setIdConfRetencion(CuentaContableRetencion idConfRetencion) {
        this.idConfRetencion = idConfRetencion;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Date getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Date fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public String getUsuarioCreacion() {
        return usuarioCreacion;
    }

    public void setUsuarioCreacion(String usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
    }

    public String getUsuarioModificacion() {
        return usuarioModificacion;
    }

    public void setUsuarioModificacion(String usuarioModificacion) {
        this.usuarioModificacion = usuarioModificacion;
    }

    public BigDecimal getValorDetalle() {
        return valorDetalle;
    }

    public BigDecimal getValorBase() {
        return valorBase;
    }

    public void setValorBase(BigDecimal valorBase) {
        this.valorBase = valorBase;
    }

    public void setValorDetalle(BigDecimal valorDetalle) {
        this.valorDetalle = valorDetalle;
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
        if (!(object instanceof ContFacturaDetalle)) {
            return false;
        }
        ContFacturaDetalle other = (ContFacturaDetalle) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.resource.contabilidad.entities.ContFacturaDetalle[ id=" + id + " ]";
    }

}
