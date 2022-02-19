/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "cat_sectores", schema = "catastro")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CatSectores.findAll", query = "SELECT c FROM CatSectores c"),
    @NamedQuery(name = "CatSectores.findByIdSector", query = "SELECT c FROM CatSectores c WHERE c.idSector = :idSector"),
    @NamedQuery(name = "CatSectores.findByZona", query = "SELECT c FROM CatSectores c WHERE c.zona = :zona"),
    @NamedQuery(name = "CatSectores.findBySector", query = "SELECT c FROM CatSectores c WHERE c.sector = :sector"),
    @NamedQuery(name = "CatSectores.findByNombre", query = "SELECT c FROM CatSectores c WHERE c.nombre = :nombre"),
    @NamedQuery(name = "CatSectores.findByUsuarioIngreso", query = "SELECT c FROM CatSectores c WHERE c.usuarioIngreso = :usuarioIngreso"),
    @NamedQuery(name = "CatSectores.findByFechaIngreso", query = "SELECT c FROM CatSectores c WHERE c.fechaIngreso = :fechaIngreso"),
    @NamedQuery(name = "CatSectores.findByUsuarioModificacion", query = "SELECT c FROM CatSectores c WHERE c.usuarioModificacion = :usuarioModificacion"),
    @NamedQuery(name = "CatSectores.findByFechaModificacion", query = "SELECT c FROM CatSectores c WHERE c.fechaModificacion = :fechaModificacion")})
public class CatSectores implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_sector")
    private Long idSector;
    @Column(name = "zona")
    private Short zona;
    @Column(name = "sector")
    private Short sector;
    @Size(max = 30)
    @Column(name = "nombre")
    private String nombre;
    @Size(max = 15)
    @Column(name = "usuario_ingreso")
    private String usuarioIngreso;
    @Column(name = "fecha_ingreso")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaIngreso;
    @Size(max = 15)
    @Column(name = "usuario_modificacion")
    private String usuarioModificacion;
    @Column(name = "fecha_modificacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaModificacion;

    public CatSectores() {
    }

    public CatSectores(Long idSector) {
        this.idSector = idSector;
    }

    public Long getIdSector() {
        return idSector;
    }

    public void setIdSector(Long idSector) {
        this.idSector = idSector;
    }

    public Short getZona() {
        return zona;
    }

    public void setZona(Short zona) {
        this.zona = zona;
    }

    public Short getSector() {
        return sector;
    }

    public void setSector(Short sector) {
        this.sector = sector;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUsuarioIngreso() {
        return usuarioIngreso;
    }

    public void setUsuarioIngreso(String usuarioIngreso) {
        this.usuarioIngreso = usuarioIngreso;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idSector != null ? idSector.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CatSectores)) {
            return false;
        }
        CatSectores other = (CatSectores) object;
        if ((this.idSector == null && other.idSector != null) || (this.idSector != null && !this.idSector.equals(other.idSector))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gestionTributaria.Entities.CatSectores[ idSector=" + idSector + " ]";
    }

}
