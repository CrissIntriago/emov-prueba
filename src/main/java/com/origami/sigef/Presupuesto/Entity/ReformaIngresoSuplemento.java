/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.Presupuesto.Entity;

import com.origami.sigef.common.entities.CatalogoItem;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

/**
 *
 * @author alexa
 */
@Entity
@Table(name = "reforma_ingreso_suplemento", schema = "presupuesto")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ReformaIngresoSuplemento.findAll", query = "SELECT r FROM ReformaIngresoSuplemento r")
    ,
    @NamedQuery(name = "ReformaIngresoSuplemento.findById", query = "SELECT r FROM ReformaIngresoSuplemento r WHERE r.id = :id")
    ,
    @NamedQuery(name = "ReformaIngresoSuplemento.findByNumeracion", query = "SELECT r FROM ReformaIngresoSuplemento r WHERE r.numeracion = :numeracion")
    ,
    @NamedQuery(name = "ReformaIngresoSuplemento.findByCodigo", query = "SELECT r FROM ReformaIngresoSuplemento r WHERE r.codigo = :codigo")
    ,
    @NamedQuery(name = "ReformaIngresoSuplemento.findByPeriodo", query = "SELECT r FROM ReformaIngresoSuplemento r WHERE r.periodo = :periodo")
    ,
    @NamedQuery(name = "ReformaIngresoSuplemento.findByFechaOficioReforma", query = "SELECT r FROM ReformaIngresoSuplemento r WHERE r.fechaOficioReforma = :fechaOficioReforma")
    ,
    @NamedQuery(name = "ReformaIngresoSuplemento.findByTipo", query = "SELECT r FROM ReformaIngresoSuplemento r WHERE r.tipo = :tipo")
    ,
    @NamedQuery(name = "ReformaIngresoSuplemento.findByEstado", query = "SELECT r FROM ReformaIngresoSuplemento r WHERE r.estado = :estado")
    ,
    @NamedQuery(name = "ReformaIngresoSuplemento.findByUsuarioCreacion", query = "SELECT r FROM ReformaIngresoSuplemento r WHERE r.usuarioCreacion = :usuarioCreacion")
    ,
    @NamedQuery(name = "ReformaIngresoSuplemento.findByFechaCreacion", query = "SELECT r FROM ReformaIngresoSuplemento r WHERE r.fechaCreacion = :fechaCreacion")
    ,
    @NamedQuery(name = "ReformaIngresoSuplemento.findByUsuarioModificacion", query = "SELECT r FROM ReformaIngresoSuplemento r WHERE r.usuarioModificacion = :usuarioModificacion")
    ,
    @NamedQuery(name = "ReformaIngresoSuplemento.findByFechaModificacion", query = "SELECT r FROM ReformaIngresoSuplemento r WHERE r.fechaModificacion = :fechaModificacion")})
public class ReformaIngresoSuplemento implements Serializable {

    @Size(max = 100)
    @Column(name = "codigo")
    private String codigo;
    @Size(max = 100)
    @Column(name = "usuario_creacion")
    private String usuarioCreacion;
    @Size(max = 100)
    @Column(name = "usuario_modificacion")
    private String usuarioModificacion;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "numeracion")
    private Integer numeracion;
    @Column(name = "periodo")
    private Short periodo;
    @Column(name = "fecha_oficio_reforma")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaOficioReforma;
    @Column(name = "fecha_aprobacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaAprobacion;
    @Column(name = "tipo")
    private Boolean tipo;
    @Column(name = "realizado")
    private Boolean realizado;
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @Column(name = "fecha_modificacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaModificacion;
    @Column(name = "ejecutado")
    private Boolean ejecutado;
    @Column(name = "num_tramite")
    private BigInteger numTramite;
    @Column(name = "informacion")
    private String informacion;
    @OneToMany(mappedBy = "reforma", fetch = FetchType.LAZY)
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<DetalleReformaIngresoSuplemento> detallereformaIngresosuplementoList;

    @JoinColumn(name = "estado", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem estado;

    public ReformaIngresoSuplemento() {
    }

    public ReformaIngresoSuplemento(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumeracion() {
        return numeracion;
    }

    public void setNumeracion(Integer numeracion) {
        this.numeracion = numeracion;
    }

    public Short getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Short periodo) {
        this.periodo = periodo;
    }

    public Date getFechaOficioReforma() {
        return fechaOficioReforma;
    }

    public void setFechaOficioReforma(Date fechaOficioReforma) {
        this.fechaOficioReforma = fechaOficioReforma;
    }

    public Boolean getTipo() {
        return tipo;
    }

    public void setTipo(Boolean tipo) {
        this.tipo = tipo;
    }

    public CatalogoItem getEstado() {
        return estado;
    }

    public void setEstado(CatalogoItem estado) {
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

    
    public List<DetalleReformaIngresoSuplemento> getDetallereformaIngresosuplementoList() {
        return detallereformaIngresosuplementoList;
    }

    public void setDetallereformaIngresosuplementoList(List<DetalleReformaIngresoSuplemento> detallereformaIngresosuplementoList) {
        this.detallereformaIngresosuplementoList = detallereformaIngresosuplementoList;
    }

    public Boolean getRealizado() {
        return realizado;
    }

    public void setRealizado(Boolean realizado) {
        this.realizado = realizado;
    }

    public Boolean getEjecutado() {
        return ejecutado;
    }

    public void setEjecutado(Boolean ejecutado) {
        this.ejecutado = ejecutado;
    }

    public Date getFechaAprobacion() {
        return fechaAprobacion;
    }

    public void setFechaAprobacion(Date fechaAprobacion) {
        this.fechaAprobacion = fechaAprobacion;
    }

    public BigInteger getNumTramite() {
        return numTramite;
    }

    public void setNumTramite(BigInteger numTramite) {
        this.numTramite = numTramite;
    }

    public String getInformacion() {
        return informacion;
    }

    public void setInformacion(String informacion) {
        this.informacion = informacion;
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
        if (!(object instanceof ReformaIngresoSuplemento)) {
            return false;
        }
        ReformaIngresoSuplemento other = (ReformaIngresoSuplemento) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.Presupuesto.Entity.ReformaIngresoSuplemento[ id=" + id + " ]";
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

}
