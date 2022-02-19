/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Entities;

import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

/**
 *
 * @author DEVELOPER
 */
@Entity
@Table(name = "esp_cementerio", schema = Utils.SCHEMA_SGM)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EspCementerio.findAll", query = "SELECT e FROM EspCementerio e"),
    @NamedQuery(name = "EspCementerio.findById", query = "SELECT e FROM EspCementerio e WHERE e.id = :id"),
    @NamedQuery(name = "EspCementerio.findByArea", query = "SELECT e FROM EspCementerio e WHERE e.area = :area"),
    @NamedQuery(name = "EspCementerio.findByEstado", query = "SELECT e FROM EspCementerio e WHERE e.estado = :estado"),
    @NamedQuery(name = "EspCementerio.findByFechaCreacion", query = "SELECT e FROM EspCementerio e WHERE e.fechaCreacion = :fechaCreacion"),
    @NamedQuery(name = "EspCementerio.findByNombreCementerio", query = "SELECT e FROM EspCementerio e WHERE e.nombreCementerio = :nombreCementerio"),
    @NamedQuery(name = "EspCementerio.findByUsuario", query = "SELECT e FROM EspCementerio e WHERE e.usuario = :usuario")})
public class EspCementerio implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "area")
    private BigDecimal area;
    @Column(name = "estado")
    private Boolean estado;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @Size(max = 100)
    @Column(name = "nombre_cementerio")
    private String nombreCementerio;
    @Size(max = 50)
    @Column(name = "usuario")
    private String usuario;
    @OneToMany(mappedBy = "cementerio", fetch = FetchType.LAZY)
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<EspCementerioBoveda> espCementerioBovedaList;

    public EspCementerio() {
    }

    public EspCementerio(Long id) {
        this.id = id;
    }

    public EspCementerio(Long id, Date fechaCreacion) {
        this.id = id;
        this.fechaCreacion = fechaCreacion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getArea() {
        return area;
    }

    public void setArea(BigDecimal area) {
        this.area = area;
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

    public String getNombreCementerio() {
        return nombreCementerio;
    }

    public void setNombreCementerio(String nombreCementerio) {
        this.nombreCementerio = nombreCementerio;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    
    public List<EspCementerioBoveda> getEspCementerioBovedaList() {
        return espCementerioBovedaList;
    }

    public void setEspCementerioBovedaList(List<EspCementerioBoveda> espCementerioBovedaList) {
        this.espCementerioBovedaList = espCementerioBovedaList;
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
        if (!(object instanceof EspCementerio)) {
            return false;
        }
        EspCementerio other = (EspCementerio) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.educacion.Models.EspCementerio[ id=" + id + " ]";
    }
    
}
