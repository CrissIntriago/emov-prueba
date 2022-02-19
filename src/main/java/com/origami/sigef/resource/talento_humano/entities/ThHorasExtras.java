/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.talento_humano.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

/**
 *
 * @author Criss Intriago
 */
@Entity
@Table(name = "th_horas_extras", schema = "talento_humano")
@NamedQueries({
    @NamedQuery(name = "ThHorasExtras.findAll", query = "SELECT t FROM ThHorasExtras t"),
    @NamedQuery(name = "ThHorasExtras.findById", query = "SELECT t FROM ThHorasExtras t WHERE t.id = :id"),
    @NamedQuery(name = "ThHorasExtras.findByServidor", query = "SELECT t FROM ThHorasExtras t WHERE t.estado = true AND t.idCargoServidor = ?1 AND t.idTipoRol = ?2"),
    @NamedQuery(name = "ThHorasExtras.findByValor", query = "SELECT t FROM ThHorasExtras t WHERE t.valor = :valor"),
    @NamedQuery(name = "ThHorasExtras.findByEstado", query = "SELECT t FROM ThHorasExtras t WHERE t.estado = :estado"),
    @NamedQuery(name = "ThHorasExtras.findByUsuarioCreacion", query = "SELECT t FROM ThHorasExtras t WHERE t.usuarioCreacion = :usuarioCreacion"),
    @NamedQuery(name = "ThHorasExtras.findByFechaCreacion", query = "SELECT t FROM ThHorasExtras t WHERE t.fechaCreacion = :fechaCreacion"),
    @NamedQuery(name = "ThHorasExtras.findByUsuarioModificacion", query = "SELECT t FROM ThHorasExtras t WHERE t.usuarioModificacion = :usuarioModificacion"),
    @NamedQuery(name = "ThHorasExtras.findByFechaModificacion", query = "SELECT t FROM ThHorasExtras t WHERE t.fechaModificacion = :fechaModificacion")})
public class ThHorasExtras implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor")
    private BigDecimal valor;
    @Column(name = "estado")
    private Boolean estado;
    @Size(max = 2147483647)
    @Column(name = "usuario_creacion")
    private String usuarioCreacion;
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.DATE)
    private Date fechaCreacion;
    @Size(max = 2147483647)
    @Column(name = "usuario_modificacion")
    private String usuarioModificacion;
    @Column(name = "fecha_modificacion")
    @Temporal(TemporalType.DATE)
    private Date fechaModificacion;
    @OneToMany(mappedBy = "idHorasExtras")
    private List<ThHorasExtrasDetalle> thHorasExtrasDetalleList;
    @JoinColumn(name = "id_rubro", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ThRubro idRubro;
    @JoinColumn(name = "id_cargo_servidor", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ThServidorCargo idCargoServidor;
    @JoinColumn(name = "id_tipo_rol", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ThTipoRol idTipoRol;
    @Column(name = "horas")
    private Short horas;
    @Column(name = "dias")
    private Integer dias;
    @Column(name = "rmu")
    private BigDecimal rmu;

    public ThHorasExtras() {
        this.estado = true;
        this.valor = BigDecimal.ZERO;
    }

    public ThHorasExtras(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public String getUsuarioCreacion() {
        return usuarioCreacion;
    }

    public void setUsuarioCreacion(String usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getUsuarioModificacion() {
        return usuarioModificacion;
    }

    public void setUsuarioModificacion(String usuarioModificacion) {
        this.usuarioModificacion = usuarioModificacion;
    }

    public Date getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Date fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public List<ThHorasExtrasDetalle> getThHorasExtrasDetalleList() {
        return thHorasExtrasDetalleList;
    }

    public void setThHorasExtrasDetalleList(List<ThHorasExtrasDetalle> thHorasExtrasDetalleList) {
        this.thHorasExtrasDetalleList = thHorasExtrasDetalleList;
    }

    public ThRubro getIdRubro() {
        return idRubro;
    }

    public void setIdRubro(ThRubro idRubro) {
        this.idRubro = idRubro;
    }

    public ThServidorCargo getIdCargoServidor() {
        return idCargoServidor;
    }

    public void setIdCargoServidor(ThServidorCargo idCargoServidor) {
        this.idCargoServidor = idCargoServidor;
    }

    public ThTipoRol getIdTipoRol() {
        return idTipoRol;
    }

    public void setIdTipoRol(ThTipoRol idTipoRol) {
        this.idTipoRol = idTipoRol;
    }

    public Short getHoras() {
        return horas;
    }

    public void setHoras(Short horas) {
        this.horas = horas;
    }

    public Integer getDias() {
        return dias;
    }

    public void setDias(Integer dias) {
        this.dias = dias;
    }

    public BigDecimal getRmu() {
        return rmu;
    }

    public void setRmu(BigDecimal rmu) {
        this.rmu = rmu;
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
        if (!(object instanceof ThHorasExtras)) {
            return false;
        }
        ThHorasExtras other = (ThHorasExtras) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.resource.talento_humano.entities.ThHorasExtras[ id=" + id + " ]";
    }

}
