/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.activos.entities;

import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.UnidadAdministrativa;
import com.origami.sigef.resource.talento_humano.entities.Servidor;
import java.io.Serializable;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Sandra Arroba
 */
@Entity
@Table(name = "bien_constatacion_fisica", schema = "activos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "BienConstatacionFisica.findAll", query = "SELECT c FROM BienConstatacionFisica c"),
    @NamedQuery(name = "BienConstatacionFisica.findById", query = "SELECT c FROM BienConstatacionFisica c WHERE c.id = :id"),
    @NamedQuery(name = "BienConstatacionFisica.findByFechaConstatacion", query = "SELECT c FROM BienConstatacionFisica c WHERE c.fechaConstatacion = :fechaConstatacion"),
    @NamedQuery(name = "BienConstatacionFisica.findByfindByOrdenConstatacionBien", query = "SELECT  MAX(c.orden)+1 FROM BienConstatacionFisica c WHERE c.estado = true"),
    @NamedQuery(name = "BienConstatacionFisica.findByObservacion", query = "SELECT c FROM BienConstatacionFisica c WHERE c.observacion = :observacion"),
    @NamedQuery(name = "BienConstatacionFisica.findByEstadoConstatacion", query = "SELECT c FROM BienConstatacionFisica c WHERE c.estadoConstatacion = :estadoConstatacion"),
    @NamedQuery(name = "BienConstatacionFisica.findByFechaCreacion", query = "SELECT c FROM BienConstatacionFisica c WHERE c.fechaCreacion = :fechaCreacion"),
    @NamedQuery(name = "BienConstatacionFisica.findByUsuarioCreacion", query = "SELECT c FROM BienConstatacionFisica c WHERE c.usuarioCreacion = :usuarioCreacion"),
    @NamedQuery(name = "BienConstatacionFisica.findByCodigo", query = "SELECT c FROM BienConstatacionFisica c WHERE c.codigo = :codigo"),
    @NamedQuery(name = "BienConstatacionFisica.findByOrden", query = "SELECT c FROM BienConstatacionFisica c WHERE c.orden = :orden"),
    @NamedQuery(name = "BienConstatacionFisica.findByEstado", query = "SELECT c FROM BienConstatacionFisica c WHERE c.estado = :estado")})
public class BienConstatacionFisica implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_constatacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaConstatacion;
    @Size(max = 255)
    @Column(name = "observacion")
    private String observacion;
    @NotNull
    @JoinColumn(name = "estado_constatacion", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem estadoConstatacion;
//    @JoinColumn(name = "direccion", referencedColumnName = "id")
//    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
//    private UnidadAdministrativa direccion;
//    @JoinColumn(name = "departamento", referencedColumnName = "id")
//    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
//    private UnidadAdministrativa departamento;
    @JoinColumn(name = "unidad", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private UnidadAdministrativa unidad;
    @JoinColumn(name = "custodio", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Servidor custodio;
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @Size(max = 255)
    @Column(name = "usuario_creacion")
    private String usuarioCreacion;
    @Column(name = "fecha_modificacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaModificacion;
    @Size(max = 255)
    @Column(name = "usuario_modificacion")
    private String usuarioModificacion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "codigo")
    private String codigo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "orden")
    private long orden;
    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "custodio_boolean")
    private Boolean custodioBoolean;
    @Column(name = "cargo")
    private String cargo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "constatacionFisica")
    private List<BienConstatacionFisicaDetalle> constatacionFisicaDetalleBienesList;

    public BienConstatacionFisica() {
    }

    public BienConstatacionFisica(Long id) {
        this.id = id;
    }

    public BienConstatacionFisica(Long id, Date fechaConstatacion, String codigo, long orden) {
        this.id = id;
        this.fechaConstatacion = fechaConstatacion;
        this.codigo = codigo;
        this.orden = orden;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getFechaConstatacion() {
        return fechaConstatacion;
    }

    public void setFechaConstatacion(Date fechaConstatacion) {
        this.fechaConstatacion = fechaConstatacion;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public CatalogoItem getEstadoConstatacion() {
        return estadoConstatacion;
    }

    public void setEstadoConstatacion(CatalogoItem estadoConstatacion) {
        this.estadoConstatacion = estadoConstatacion;
    }

//    public UnidadAdministrativa getDireccion() {
//        return direccion;
//    }
//
//    public void setDireccion(UnidadAdministrativa direccion) {
//        this.direccion = direccion;
//    }
//
//    public UnidadAdministrativa getDepartamento() {
//        return departamento;
//    }
//
//    public void setDepartamento(UnidadAdministrativa departamento) {
//        this.departamento = departamento;
//    }

    public UnidadAdministrativa getUnidad() {
        return unidad;
    }

    public void setUnidad(UnidadAdministrativa unidad) {
        this.unidad = unidad;
    }

    public Servidor getCustodio() {
        return custodio;
    }

    public void setCustodio(Servidor custodio) {
        this.custodio = custodio;
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

    public String getUsuarioModificacion() {
        return usuarioModificacion;
    }

    public void setUsuarioModificacion(String usuarioModificacion) {
        this.usuarioModificacion = usuarioModificacion;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public long getOrden() {
        return orden;
    }

    public void setOrden(long orden) {
        this.orden = orden;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Boolean getCustodioBoolean() {
        return custodioBoolean;
    }

    public void setCustodioBoolean(Boolean custodioBoolean) {
        this.custodioBoolean = custodioBoolean;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    @XmlTransient
    public List<BienConstatacionFisicaDetalle> getConstatacionFisicaDetalleBienesList() {
        return constatacionFisicaDetalleBienesList;
    }

    public void setConstatacionFisicaDetalleBienesList(List<BienConstatacionFisicaDetalle> constatacionFisicaDetalleBienesList) {
        this.constatacionFisicaDetalleBienesList = constatacionFisicaDetalleBienesList;
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
        if (!(object instanceof BienConstatacionFisica)) {
            return false;
        }
        BienConstatacionFisica other = (BienConstatacionFisica) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.resource.activos.entities.BienConstatacionFisica[ id=" + id + " ]";
    }

}
