/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.talento_humano.entities;

import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.UnidadAdministrativa;
import java.io.Serializable;
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
import javax.persistence.Transient;
import javax.validation.constraints.Size;

/**
 *
 * @author Criss Intriago
 */
@Entity
@Table(name = "th_cargo", schema = "talento_humano")
@NamedQueries({
    @NamedQuery(name = "ThCargo.findAll", query = "SELECT t FROM ThCargo t"),
    @NamedQuery(name = "ThCargo.findById", query = "SELECT t FROM ThCargo t WHERE t.id = :id"),
    @NamedQuery(name = "ThCargo.findByNombreCargo", query = "SELECT t FROM ThCargo t WHERE t.nombreCargo = ?1 AND t.estado = true ORDER BY t.codigo"),
    @NamedQuery(name = "ThCargo.findByNombreCargoClasificacionContrato", query = "SELECT t FROM ThCargo t WHERE t.nombreCargo = ?1 AND t.estado = true AND t.idCatalogoItem = ?2 AND t.idContrato = ?3 ORDER BY t.codigo"),
    @NamedQuery(name = "ThCargo.findByNombreCargoClasificacion", query = "SELECT t FROM ThCargo t WHERE t.nombreCargo = ?1 AND t.estado = true AND t.idCatalogoItem = ?2 ORDER BY t.codigo"),
    @NamedQuery(name = "ThCargo.findByNombreCargoContrato", query = "SELECT t FROM ThCargo t WHERE t.nombreCargo = ?1 AND t.estado = true AND t.idContrato = ?2 ORDER BY t.codigo"),
    @NamedQuery(name = "ThCargo.findByEstado", query = "SELECT t FROM ThCargo t WHERE t.estado = :estado"),
    @NamedQuery(name = "ThCargo.findByFechaCreacion", query = "SELECT t FROM ThCargo t WHERE t.fechaCreacion = :fechaCreacion"),
    @NamedQuery(name = "ThCargo.findByUsuarioCreacion", query = "SELECT t FROM ThCargo t WHERE t.usuarioCreacion = :usuarioCreacion"),
    @NamedQuery(name = "ThCargo.findByFechaModificacion", query = "SELECT t FROM ThCargo t WHERE t.fechaModificacion = :fechaModificacion"),
    @NamedQuery(name = "ThCargo.findByUsuarioModifica", query = "SELECT t FROM ThCargo t WHERE t.usuarioModifica = :usuarioModifica"),
    @NamedQuery(name = "ThCargo.findByIdUnidad", query = "SELECT t FROM ThCargo t WHERE t.idUnidad = :idUnidad")})
public class ThCargo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 250)
    @Column(name = "nombre_cargo")
    private String nombreCargo;
    @Column(name = "codigo")
    private String codigo;
    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "activo")
    private Boolean activo;
    @Column(name = "asignado")
    private Boolean asignado;
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
    @JoinColumn(name = "id_unidad", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private UnidadAdministrativa idUnidad;
    @JoinColumn(name = "id_grupo", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ThEscalaSalarial idGrupo;
    @JoinColumn(name = "id_regimen", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ThRegimenLaboral idRegimen;
    @OneToMany(mappedBy = "idCargo", fetch = FetchType.LAZY)
    private List<ThCargoRubros> thCargoRubrosList;
    @JoinColumn(name = "id_catalogo_item", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem idCatalogoItem;
    @JoinColumn(name = "id_contrato", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem idContrato;

    @Transient
    private Date fechaAsignacion;
    @Transient
    private Date fechaFinalizacion;

    public ThCargo() {
        estado = true;
        activo = true;
        asignado = false;
    }

    public ThCargo(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreCargo() {
        return nombreCargo;
    }

    public void setNombreCargo(String nombreCargo) {
        this.nombreCargo = nombreCargo;
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

    public UnidadAdministrativa getIdUnidad() {
        return idUnidad;
    }

    public void setIdUnidad(UnidadAdministrativa idUnidad) {
        this.idUnidad = idUnidad;
    }

    public ThEscalaSalarial getIdGrupo() {
        return idGrupo;
    }

    public void setIdGrupo(ThEscalaSalarial idGrupo) {
        this.idGrupo = idGrupo;
    }

    public ThRegimenLaboral getIdRegimen() {
        return idRegimen;
    }

    public void setIdRegimen(ThRegimenLaboral idRegimen) {
        this.idRegimen = idRegimen;
    }

    public List<ThCargoRubros> getThCargoRubrosList() {
        return thCargoRubrosList;
    }

    public void setThCargoRubrosList(List<ThCargoRubros> thCargoRubrosList) {
        this.thCargoRubrosList = thCargoRubrosList;
    }

    public CatalogoItem getIdCatalogoItem() {
        return idCatalogoItem;
    }

    public void setIdCatalogoItem(CatalogoItem idCatalogoItem) {
        this.idCatalogoItem = idCatalogoItem;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Boolean getAsignado() {
        return asignado;
    }

    public void setAsignado(Boolean asignado) {
        this.asignado = asignado;
    }

    public Date getFechaAsignacion() {
        return fechaAsignacion;
    }

    public void setFechaAsignacion(Date fechaAsignacion) {
        this.fechaAsignacion = fechaAsignacion;
    }

    public Date getFechaFinalizacion() {
        return fechaFinalizacion;
    }

    public void setFechaFinalizacion(Date fechaFinalizacion) {
        this.fechaFinalizacion = fechaFinalizacion;
    }

    public CatalogoItem getIdContrato() {
        return idContrato;
    }

    public void setIdContrato(CatalogoItem idContrato) {
        this.idContrato = idContrato;
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
        if (!(object instanceof ThCargo)) {
            return false;
        }
        ThCargo other = (ThCargo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.resource.talento_humano.entities.ThCargo[ id=" + id + " ]";
    }

}
