/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

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
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Erwin
 */
@Entity
@Table(name = "constatacion_fisica_inventario", schema = "activos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ConstatacionFisicaInventario.findAll", query = "SELECT c FROM ConstatacionFisicaInventario c ORDER BY c.orden"),
    @NamedQuery(name = "ConstatacionFisicaInventario.findById", query = "SELECT c FROM ConstatacionFisicaInventario c WHERE c.id = :id"),
    @NamedQuery(name = "ConstatacionFisicaInventario.findByFechaConstatacion", query = "SELECT c FROM ConstatacionFisicaInventario c WHERE c.fechaConstatacion = :fechaConstatacion"),
    @NamedQuery(name = "ConstatacionFisicaInventario.findByCodigo", query = "SELECT c FROM ConstatacionFisicaInventario c WHERE c.codigo = :codigo"),
    @NamedQuery(name = "ConstatacionFisicaInventario.findByEstado", query = "SELECT c FROM ConstatacionFisicaInventario c WHERE c.estado = :estado"),
    @NamedQuery(name = "ConstatacionFisicaInventario.findByObservacion", query = "SELECT c FROM ConstatacionFisicaInventario c WHERE c.observacion = :observacion"),
    @NamedQuery(name = "ConstatacionFisicaInventario.findByOrdenConsta", query = "SELECT  MAX(c.orden)+1 FROM ConstatacionFisicaInventario c") //erwin
    ,
    @NamedQuery(name = "ConstatacionFisicaInventario.findByUsuarioCreacion", query = "SELECT c FROM ConstatacionFisicaInventario c WHERE c.usuarioCreacion = :usuarioCreacion"),
    @NamedQuery(name = "ConstatacionFisicaInventario.findByFechaCreacion", query = "SELECT c FROM ConstatacionFisicaInventario c WHERE c.fechaCreacion = :fechaCreacion"),
    @NamedQuery(name = "ConstatacionFisicaInventario.findByUsuarioModificacion", query = "SELECT c FROM ConstatacionFisicaInventario c WHERE c.usuarioModificacion = :usuarioModificacion"),
    @NamedQuery(name = "ConstatacionFisicaInventario.findByFechaModificacion", query = "SELECT c FROM ConstatacionFisicaInventario c WHERE c.fechaModificacion = :fechaModificacion")})
public class ConstatacionFisicaInventario implements Serializable {

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

    @Transient
    private Date fechaDesde;
    @Transient
    private Date fechaHasta;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "codigo")
    private String codigo;
    @Size(max = 255)
    @Column(name = "observacion")
    private String observacion;

    @Size(max = 255)
    @Column(name = "razon_const")
    private String razon;

    @Size(max = 255)
    @Column(name = "usuario_creacion")
    private String usuarioCreacion;
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "orden")
    private long orden;
    @JoinColumn(name = "area", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private GrupoNiveles area;
    @JoinColumn(name = "grupo", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private GrupoNiveles grupo;
    @JoinColumn(name = "sub_grupo", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private GrupoNiveles subGrupo;

    @Size(max = 255)
    @Column(name = "usuario_modificacion")
    private String usuarioModificacion;
    @Column(name = "fecha_modificacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaModificacion;
    @JoinColumn(name = "estado", referencedColumnName = "id", nullable = false)
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem estado;
    @Column(name = "estado_cons")
    private Boolean estadoCons;

    @Column(name = "ajustado")
    private Boolean ajustado;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "constatacionFisicaId")
    private List<DetalleConstFisicaInventario> detalleConstFisicaInventarioList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "constatacionFisica")
    private List<Inventario> inventariosList;

    public ConstatacionFisicaInventario() {
    }

    public ConstatacionFisicaInventario(Long id) {
        this.id = id;
    }

    public ConstatacionFisicaInventario(Long id, Date fechaConstatacion) {
        this.id = id;
        this.fechaConstatacion = fechaConstatacion;

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

    public List<Inventario> getInventariosList() {
        return inventariosList;
    }

    public void setInventariosList(List<Inventario> inventariosList) {
        this.inventariosList = inventariosList;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Boolean getEstadoCons() {
        return estadoCons;
    }

    public String getRazon() {
        return razon;
    }

    public void setRazon(String razon) {
        this.razon = razon;
    }

    public void setEstadoCons(Boolean estadoCons) {
        this.estadoCons = estadoCons;
    }

    public CatalogoItem getEstado() {
        return estado;
    }

    public void setEstado(CatalogoItem estado) {
        this.estado = estado;
    }

    public String getObservacion() {
        return observacion;
    }

    public Boolean getAjustado() {
        return ajustado;
    }

    public void setAjustado(Boolean ajustado) {
        this.ajustado = ajustado;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getUsuarioCreacion() {
        return usuarioCreacion;
    }

    public GrupoNiveles getArea() {
        return area;
    }

    public void setArea(GrupoNiveles area) {
        this.area = area;
    }

    public GrupoNiveles getGrupo() {
        return grupo;
    }

    public void setGrupo(GrupoNiveles grupo) {
        this.grupo = grupo;
    }

    public GrupoNiveles getSubGrupo() {
        return subGrupo;
    }

    public void setSubGrupo(GrupoNiveles subGrupo) {
        this.subGrupo = subGrupo;
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

    public Date getFechaDesde() {
        return fechaDesde;
    }

    public void setFechaDesde(Date fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    public Date getFechaHasta() {
        return fechaHasta;
    }

    public void setFechaHasta(Date fechaHasta) {
        this.fechaHasta = fechaHasta;
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

    
    public List<DetalleConstFisicaInventario> getDetalleConstFisicaInventarioList() {
        return detalleConstFisicaInventarioList;
    }

    public void setDetalleConstFisicaInventarioList(List<DetalleConstFisicaInventario> detalleConstFisicaInventarioList) {
        this.detalleConstFisicaInventarioList = detalleConstFisicaInventarioList;
    }

    public long getOrden() {
        return orden;
    }

    public void setOrden(long orden) {
        this.orden = orden;
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
        if (!(object instanceof ConstatacionFisicaInventario)) {
            return false;
        }
        ConstatacionFisicaInventario other = (ConstatacionFisicaInventario) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.common.entities.ConstatacionFisicaInventario[ id=" + id + " ]";
    }

}
