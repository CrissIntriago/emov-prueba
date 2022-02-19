/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

/**
 *
 * @author ORIGAMI2
 */
@Entity
@Table(name = "otro_descuento", schema = "talento_humano")
@NamedQueries({
    @NamedQuery(name = "OtroDescuento.findAll", query = "SELECT o FROM OtroDescuento o")
    , @NamedQuery(name = "OtroDescuento.findById", query = "SELECT o FROM OtroDescuento o WHERE o.id = :id")
    , @NamedQuery(name = "OtroDescuento.findByTotalDescuento", query = "SELECT o FROM OtroDescuento o WHERE o.totalDescuento = :totalDescuento")
    , @NamedQuery(name = "OtroDescuento.findByEstado", query = "SELECT o FROM OtroDescuento o WHERE o.estado = :estado")
    , @NamedQuery(name = "OtroDescuento.findByFechaCreacion", query = "SELECT o FROM OtroDescuento o WHERE o.fechaCreacion = :fechaCreacion")
    , @NamedQuery(name = "OtroDescuento.findByUsuarioCreacion", query = "SELECT o FROM OtroDescuento o WHERE o.usuarioCreacion = :usuarioCreacion")
    , @NamedQuery(name = "OtroDescuento.findByFechaModificacion", query = "SELECT o FROM OtroDescuento o WHERE o.fechaModificacion = :fechaModificacion")
    , @NamedQuery(name = "OtroDescuento.findByUsuarioModifica", query = "SELECT o FROM OtroDescuento o WHERE o.usuarioModifica = :usuarioModifica")})
public class OtroDescuento implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "total_descuento")
    private BigDecimal totalDescuento;
    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @Size(max = 100)
    @Column(name = "usuario_creacion")
    private String usuarioCreacion;
    @Column(name = "fecha_modificacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaModificacion;
    @Size(max = 100)
    @Column(name = "usuario_modifica")
    private String usuarioModifica;
    @Column(name = "num_tramite")
    private BigInteger numTramite;
    @JoinColumn(name = "rol_pago", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private RolesDePago rolPago;
    @JoinColumn(name = "tipo_rol", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private TipoRol tipoRol;
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "otroDescuento")
    private List<DescuentoRubroValor> descuentoRubroValorList;

    public OtroDescuento() {
        this.estado = Boolean.TRUE;
    }

    public OtroDescuento(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getTotalDescuento() {
        return totalDescuento;
    }

    public void setTotalDescuento(BigDecimal totalDescuento) {
        this.totalDescuento = totalDescuento;
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

    public String getUsuarioCreacion() {
        return usuarioCreacion;
    }

    public void setUsuarioCreacion(String usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
    }

    public Date getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Date fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public String getUsuarioModifica() {
        return usuarioModifica;
    }

    public void setUsuarioModifica(String usuarioModifica) {
        this.usuarioModifica = usuarioModifica;
    }

    public RolesDePago getRolPago() {
        return rolPago;
    }

    public void setRolPago(RolesDePago rolPago) {
        this.rolPago = rolPago;
    }

    public TipoRol getTipoRol() {
        return tipoRol;
    }

    public void setTipoRol(TipoRol tipoRol) {
        this.tipoRol = tipoRol;
    }

    public List<DescuentoRubroValor> getDescuentoRubroValorList() {
        return descuentoRubroValorList;
    }

    public void setDescuentoRubroValorList(List<DescuentoRubroValor> descuentoRubroValorList) {
        this.descuentoRubroValorList = descuentoRubroValorList;
    }

    public BigInteger getNumTramite() {
        return numTramite;
    }

    public void setNumTramite(BigInteger numTramite) {
        this.numTramite = numTramite;
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
        if (!(object instanceof OtroDescuento)) {
            return false;
        }
        OtroDescuento other = (OtroDescuento) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.common.entities.OtroDescuento[ id=" + id + " ]";
    }

}
