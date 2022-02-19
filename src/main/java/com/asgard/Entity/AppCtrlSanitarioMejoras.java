/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.asgard.Entity;

import com.origami.sigef.common.util.Utils;
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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

/**
 *
 * @author DEVELOPER
 */
@Entity
@Table(name = "app_ctrl_sanitario_mejoras", schema = Utils.SCHEMA_ASGARD)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AppCtrlSanitarioMejoras.findAll", query = "SELECT a FROM AppCtrlSanitarioMejoras a"),
    @NamedQuery(name = "AppCtrlSanitarioMejoras.findById", query = "SELECT a FROM AppCtrlSanitarioMejoras a WHERE a.id = :id"),
    @NamedQuery(name = "AppCtrlSanitarioMejoras.findByViviendaFueraNegocio", query = "SELECT a FROM AppCtrlSanitarioMejoras a WHERE a.viviendaFueraNegocio = :viviendaFueraNegocio"),
    @NamedQuery(name = "AppCtrlSanitarioMejoras.findByRetiroObjetoAjeno", query = "SELECT a FROM AppCtrlSanitarioMejoras a WHERE a.retiroObjetoAjeno = :retiroObjetoAjeno"),
    @NamedQuery(name = "AppCtrlSanitarioMejoras.findByEsterilizador", query = "SELECT a FROM AppCtrlSanitarioMejoras a WHERE a.esterilizador = :esterilizador"),
    @NamedQuery(name = "AppCtrlSanitarioMejoras.findByCamaraSeguridad", query = "SELECT a FROM AppCtrlSanitarioMejoras a WHERE a.camaraSeguridad = :camaraSeguridad"),
    @NamedQuery(name = "AppCtrlSanitarioMejoras.findByRetiroAnimales", query = "SELECT a FROM AppCtrlSanitarioMejoras a WHERE a.retiroAnimales = :retiroAnimales"),
    @NamedQuery(name = "AppCtrlSanitarioMejoras.findByTrampaGrasas", query = "SELECT a FROM AppCtrlSanitarioMejoras a WHERE a.trampaGrasas = :trampaGrasas"),
    @NamedQuery(name = "AppCtrlSanitarioMejoras.findByInsonorizacion", query = "SELECT a FROM AppCtrlSanitarioMejoras a WHERE a.insonorizacion = :insonorizacion"),
    @NamedQuery(name = "AppCtrlSanitarioMejoras.findByGuardiaSeguridad", query = "SELECT a FROM AppCtrlSanitarioMejoras a WHERE a.guardiaSeguridad = :guardiaSeguridad"),
    @NamedQuery(name = "AppCtrlSanitarioMejoras.findByFechaReinspeccion", query = "SELECT a FROM AppCtrlSanitarioMejoras a WHERE a.fechaReinspeccion = :fechaReinspeccion"),
    @NamedQuery(name = "AppCtrlSanitarioMejoras.findByPermisoFuncionamiento", query = "SELECT a FROM AppCtrlSanitarioMejoras a WHERE a.permisoFuncionamiento = :permisoFuncionamiento"),
    @NamedQuery(name = "AppCtrlSanitarioMejoras.findByObservaciones", query = "SELECT a FROM AppCtrlSanitarioMejoras a WHERE a.observaciones = :observaciones")})
public class AppCtrlSanitarioMejoras implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "vivienda_fuera_negocio")
    private Boolean viviendaFueraNegocio;
    @Column(name = "retiro_objeto_ajeno")
    private Boolean retiroObjetoAjeno;
    @Column(name = "esterilizador")
    private Boolean esterilizador;
    @Column(name = "camara_seguridad")
    private Boolean camaraSeguridad;
    @Column(name = "retiro_animales")
    private Boolean retiroAnimales;
    @Column(name = "trampa_grasas")
    private Boolean trampaGrasas;
    @Column(name = "insonorizacion")
    private Boolean insonorizacion;
    @Column(name = "guardia_seguridad")
    private Boolean guardiaSeguridad;
    @Column(name = "fecha_reinspeccion")
    @Temporal(TemporalType.DATE)
    private Date fechaReinspeccion;
    @Column(name = "permiso_funcionamiento")
    private Boolean permisoFuncionamiento;
    @Size(max = 500)
    @Column(name = "observaciones")
    private String observaciones;
    @OneToMany(mappedBy = "ctrlSanitarioMejoras", fetch = FetchType.LAZY)
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<AppCtrlSanitarioReinspeccion> appCtrlSanitarioReinspeccionList;
    @JoinColumn(name = "proteccion_alimento", referencedColumnName = "id")
    @ManyToOne
    private AppProteccionAlimentos proteccionAlimento;

    public AppCtrlSanitarioMejoras() {
    }

    public AppCtrlSanitarioMejoras(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getViviendaFueraNegocio() {
        return viviendaFueraNegocio;
    }

    public void setViviendaFueraNegocio(Boolean viviendaFueraNegocio) {
        this.viviendaFueraNegocio = viviendaFueraNegocio;
    }

    public Boolean getRetiroObjetoAjeno() {
        return retiroObjetoAjeno;
    }

    public void setRetiroObjetoAjeno(Boolean retiroObjetoAjeno) {
        this.retiroObjetoAjeno = retiroObjetoAjeno;
    }

    public Boolean getEsterilizador() {
        return esterilizador;
    }

    public void setEsterilizador(Boolean esterilizador) {
        this.esterilizador = esterilizador;
    }

    public Boolean getCamaraSeguridad() {
        return camaraSeguridad;
    }

    public void setCamaraSeguridad(Boolean camaraSeguridad) {
        this.camaraSeguridad = camaraSeguridad;
    }

    public Boolean getRetiroAnimales() {
        return retiroAnimales;
    }

    public void setRetiroAnimales(Boolean retiroAnimales) {
        this.retiroAnimales = retiroAnimales;
    }

    public Boolean getTrampaGrasas() {
        return trampaGrasas;
    }

    public void setTrampaGrasas(Boolean trampaGrasas) {
        this.trampaGrasas = trampaGrasas;
    }

    public Boolean getInsonorizacion() {
        return insonorizacion;
    }

    public void setInsonorizacion(Boolean insonorizacion) {
        this.insonorizacion = insonorizacion;
    }

    public Boolean getGuardiaSeguridad() {
        return guardiaSeguridad;
    }

    public void setGuardiaSeguridad(Boolean guardiaSeguridad) {
        this.guardiaSeguridad = guardiaSeguridad;
    }

    public Date getFechaReinspeccion() {
        return fechaReinspeccion;
    }

    public void setFechaReinspeccion(Date fechaReinspeccion) {
        this.fechaReinspeccion = fechaReinspeccion;
    }

    public Boolean getPermisoFuncionamiento() {
        return permisoFuncionamiento;
    }

    public void setPermisoFuncionamiento(Boolean permisoFuncionamiento) {
        this.permisoFuncionamiento = permisoFuncionamiento;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    
    public List<AppCtrlSanitarioReinspeccion> getAppCtrlSanitarioReinspeccionList() {
        return appCtrlSanitarioReinspeccionList;
    }

    public void setAppCtrlSanitarioReinspeccionList(List<AppCtrlSanitarioReinspeccion> appCtrlSanitarioReinspeccionList) {
        this.appCtrlSanitarioReinspeccionList = appCtrlSanitarioReinspeccionList;
    }

    public AppProteccionAlimentos getProteccionAlimento() {
        return proteccionAlimento;
    }

    public void setProteccionAlimento(AppProteccionAlimentos proteccionAlimento) {
        this.proteccionAlimento = proteccionAlimento;
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
        if (!(object instanceof AppCtrlSanitarioMejoras)) {
            return false;
        }
        AppCtrlSanitarioMejoras other = (AppCtrlSanitarioMejoras) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.educacion.Models.AppCtrlSanitarioMejoras[ id=" + id + " ]";
    }
    
}
