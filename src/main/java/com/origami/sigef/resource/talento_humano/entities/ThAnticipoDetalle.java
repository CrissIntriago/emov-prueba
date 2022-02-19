/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.talento_humano.entities;

import com.origami.sigef.common.entities.CatalogoItem;
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

/**
 *
 * @author Criss Intriago
 * @author Jonathan Choez
 */
@Entity
@Table(name = "th_anticipo_detalle", schema = "talento_humano")
@NamedQueries({
    @NamedQuery(name = "ThAnticipoDetalle.findAll", query = "SELECT t FROM ThAnticipoDetalle t"),
    @NamedQuery(name = "ThAnticipoDetalle.findById", query = "SELECT t FROM ThAnticipoDetalle t WHERE t.id = :id"),
    @NamedQuery(name = "ThAnticipoDetalle.findByIdMes", query = "SELECT t FROM ThAnticipoDetalle t WHERE t.idMes = :idMes"),
    @NamedQuery(name = "ThAnticipoDetalle.findByValorCuota", query = "SELECT t FROM ThAnticipoDetalle t WHERE t.valorCuota = :valorCuota"),
    @NamedQuery(name = "ThAnticipoDetalle.findByEstado", query = "SELECT t FROM ThAnticipoDetalle t WHERE t.estado = :estado"),
    @NamedQuery(name = "ThAnticipoDetalle.findByDescontado", query = "SELECT t FROM ThAnticipoDetalle t WHERE t.descontado = :descontado"),
    @NamedQuery(name = "ThAnticipoDetalle.findByAnticipo", query = "SELECT t FROM ThAnticipoDetalle t INNER JOIN t.idMes m WHERE t.idAnticipo = ?1 AND t.estado=true ORDER BY m.orden ASC"),
    @NamedQuery(name = "ThAnticipoDetalle.findByFechaDescuento", query = "SELECT t FROM ThAnticipoDetalle t WHERE t.fechaDescuento = :fechaDescuento")})
public class ThAnticipoDetalle implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @JoinColumn(name = "id_mes", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem idMes;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor_cuota")
    private BigDecimal valorCuota;
    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "descontado")
    private Boolean descontado;
    @Column(name = "fecha_descuento")
    @Temporal(TemporalType.DATE)
    private Date fechaDescuento;
    @JoinColumn(name = "id_anticipo", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ThAnticipo idAnticipo;
    @JoinColumn(name = "id_tipo_rol", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ThTipoRol idTipoRol;

    public ThAnticipoDetalle() {
        this.descontado = Boolean.FALSE;
        this.estado = Boolean.TRUE;
        this.valorCuota = BigDecimal.ZERO;
    }

    public ThAnticipoDetalle(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CatalogoItem getIdMes() {
        return idMes;
    }

    public void setIdMes(CatalogoItem idMes) {
        this.idMes = idMes;
    }

    public BigDecimal getValorCuota() {
        return valorCuota;
    }

    public void setValorCuota(BigDecimal valorCuota) {
        this.valorCuota = valorCuota;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Boolean getDescontado() {
        return descontado;
    }

    public void setDescontado(Boolean descontado) {
        this.descontado = descontado;
    }

    public Date getFechaDescuento() {
        return fechaDescuento;
    }

    public void setFechaDescuento(Date fechaDescuento) {
        this.fechaDescuento = fechaDescuento;
    }

    public ThAnticipo getIdAnticipo() {
        return idAnticipo;
    }

    public void setIdAnticipo(ThAnticipo idAnticipo) {
        this.idAnticipo = idAnticipo;
    }

    public ThTipoRol getIdTipoRol() {
        return idTipoRol;
    }

    public void setIdTipoRol(ThTipoRol idTipoRol) {
        this.idTipoRol = idTipoRol;
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
        if (!(object instanceof ThAnticipoDetalle)) {
            return false;
        }
        ThAnticipoDetalle other = (ThAnticipoDetalle) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.resource.commons.controllers.ThAnticipoDetalle[ id=" + id + " ]";
    }

}
