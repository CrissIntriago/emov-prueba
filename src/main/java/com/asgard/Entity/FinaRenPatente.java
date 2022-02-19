/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.asgard.Entity;

import com.gestionTributaria.models.CatPredioModel;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
import javax.persistence.criteria.Fetch;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.Where;

/**
 *
 * @author DEVELOPER
 */
@Entity
@Table(name = "fina_ren_patente", schema = Utils.SCHEMA_ASGARD)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FinaRenPatente.findAll", query = "SELECT f FROM FinaRenPatente f"),
    @NamedQuery(name = "FinaRenPatente.findById", query = "SELECT f FROM FinaRenPatente f WHERE f.id = :id"),
    @NamedQuery(name = "FinaRenPatente.findByContabilidad", query = "SELECT f FROM FinaRenPatente f WHERE f.contabilidad = :contabilidad"),
    @NamedQuery(name = "FinaRenPatente.findByFechaIngreso", query = "SELECT f FROM FinaRenPatente f WHERE f.fechaIngreso = :fechaIngreso"),
    @NamedQuery(name = "FinaRenPatente.findByFechaInicioActividad", query = "SELECT f FROM FinaRenPatente f WHERE f.fechaInicioActividad = :fechaInicioActividad"),
    @NamedQuery(name = "FinaRenPatente.findByFechaConstitucion", query = "SELECT f FROM FinaRenPatente f WHERE f.fechaConstitucion = :fechaConstitucion"),
    @NamedQuery(name = "FinaRenPatente.findByFechaInscripcion", query = "SELECT f FROM FinaRenPatente f WHERE f.fechaInscripcion = :fechaInscripcion"),
    @NamedQuery(name = "FinaRenPatente.findByFechaActualizacion", query = "SELECT f FROM FinaRenPatente f WHERE f.fechaActualizacion = :fechaActualizacion"),
    @NamedQuery(name = "FinaRenPatente.findByEstado", query = "SELECT f FROM FinaRenPatente f WHERE f.estado = :estado"),
    @NamedQuery(name = "FinaRenPatente.findByPropietario", query = "SELECT f FROM FinaRenPatente f WHERE f.propietario = :propietario"),
    @NamedQuery(name = "FinaRenPatente.findByRepresentanteLegal", query = "SELECT f FROM FinaRenPatente f WHERE f.representanteLegal = :representanteLegal"),
    @NamedQuery(name = "FinaRenPatente.findByUsuarioIngreso", query = "SELECT f FROM FinaRenPatente f WHERE f.usuarioIngreso = :usuarioIngreso")})
public class FinaRenPatente implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "contabilidad")
    private Boolean contabilidad;
    @Column(name = "fecha_ingreso")
    @Temporal(TemporalType.DATE)
    private Date fechaIngreso;
    @Column(name = "fecha_inicio_actividad")
    @Temporal(TemporalType.DATE)
    private Date fechaInicioActividad;
    @Column(name = "fecha_constitucion")
    @Temporal(TemporalType.DATE)
    private Date fechaConstitucion;
    @Column(name = "fecha_inscripcion")
    @Temporal(TemporalType.DATE)
    private Date fechaInscripcion;
    @Column(name = "fecha_actualizacion")
    @Temporal(TemporalType.DATE)
    private Date fechaActualizacion;
    @Column(name = "estado")
    private Boolean estado;
    @JoinColumn(name = "propietario", referencedColumnName = "id")
    @ManyToOne
    private Cliente propietario;
    @JoinColumn(name = "representante_legal", referencedColumnName = "id")
    @ManyToOne
    private Cliente representanteLegal;
    @Size(max = 20)
    @Column(name = "usuario_ingreso")
    private String usuarioIngreso;
    @OneToMany(mappedBy = "patente", fetch = FetchType.LAZY)
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<FinaRenLocalComercial> finaRenLocalComercialList;
    @JoinColumn(name = "actividad_principal", referencedColumnName = "id")
    @ManyToOne
    private FinaRenActividadComercial actividadPrincipal;
    @OneToMany(mappedBy = "patente", fetch = FetchType.LAZY)
    @Where(clause = "estado")
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<FinaRenLocalComercial> renLocalComercialList;
    @Column(name = "codigo_usuario")
    private String codigoUsuario;
   

    public FinaRenPatente() {
    }

    public FinaRenPatente(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getContabilidad() {
        return contabilidad;
    }

    public void setContabilidad(Boolean contabilidad) {
        this.contabilidad = contabilidad;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public Date getFechaInicioActividad() {
        return fechaInicioActividad;
    }

    public void setFechaInicioActividad(Date fechaInicioActividad) {
        this.fechaInicioActividad = fechaInicioActividad;
    }

    public Date getFechaConstitucion() {
        return fechaConstitucion;
    }

    public void setFechaConstitucion(Date fechaConstitucion) {
        this.fechaConstitucion = fechaConstitucion;
    }

    public Date getFechaInscripcion() {
        return fechaInscripcion;
    }

    public void setFechaInscripcion(Date fechaInscripcion) {
        this.fechaInscripcion = fechaInscripcion;
    }

    public Date getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(Date fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Cliente getPropietario() {
        return propietario;
    }

    public void setPropietario(Cliente propietario) {
        this.propietario = propietario;
    }

    public Cliente getRepresentanteLegal() {
        return representanteLegal;
    }

    public void setRepresentanteLegal(Cliente representanteLegal) {
        this.representanteLegal = representanteLegal;
    }

    public FinaRenLocalComercial localMatriz() {
        for (FinaRenLocalComercial l : renLocalComercialList) {
            if (l.getMatriz().equals(Boolean.TRUE)) {
                return l;
            }
        }
        return null;
    }

    public String getUsuarioIngreso() {
        return usuarioIngreso;
    }

    public void setUsuarioIngreso(String usuarioIngreso) {
        this.usuarioIngreso = usuarioIngreso;
    }

    
    public List<FinaRenLocalComercial> getFinaRenLocalComercialList() {
        return finaRenLocalComercialList;
    }

    public void setFinaRenLocalComercialList(List<FinaRenLocalComercial> finaRenLocalComercialList) {
        this.finaRenLocalComercialList = finaRenLocalComercialList;
    }

    public FinaRenActividadComercial getActividadPrincipal() {
        return actividadPrincipal;
    }

    public void setActividadPrincipal(FinaRenActividadComercial actividadPrincipal) {
        this.actividadPrincipal = actividadPrincipal;
    }

    public List<FinaRenLocalComercial> getRenLocalComercialList() {
        return renLocalComercialList;
    }

    public void setRenLocalComercialList(List<FinaRenLocalComercial> renLocalComercialList) {
        this.renLocalComercialList = renLocalComercialList;
    }

    public String getCodigoUsuario() {
        return codigoUsuario;
    }

    public void setCodigoUsuario(String codigoUsuario) {
        this.codigoUsuario = codigoUsuario;
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
        if (!(object instanceof FinaRenPatente)) {
            return false;
        }
        FinaRenPatente other = (FinaRenPatente) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.educacion.Models.FinaRenPatente[ id=" + id + " ]";
    }

}
