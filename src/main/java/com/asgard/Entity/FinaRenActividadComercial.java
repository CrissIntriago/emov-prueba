/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.asgard.Entity;

import com.gestionTributaria.Entities.RenTasaTurismo;
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
import javax.persistence.ManyToMany;
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
@Table(name = "fina_ren_actividad_comercial", schema = Utils.SCHEMA_ASGARD)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FinaRenActividadComercial.findAll", query = "SELECT f FROM FinaRenActividadComercial f"),
    @NamedQuery(name = "FinaRenActividadComercial.findById", query = "SELECT f FROM FinaRenActividadComercial f WHERE f.id = :id"),
    @NamedQuery(name = "FinaRenActividadComercial.findByCiu", query = "SELECT f FROM FinaRenActividadComercial f WHERE f.ciu = :ciu"),
    @NamedQuery(name = "FinaRenActividadComercial.findByDescripcion", query = "SELECT f FROM FinaRenActividadComercial f WHERE f.descripcion = :descripcion"),
    @NamedQuery(name = "FinaRenActividadComercial.findByEstado", query = "SELECT f FROM FinaRenActividadComercial f WHERE f.estado = :estado"),
    @NamedQuery(name = "FinaRenActividadComercial.findByFechaIngreso", query = "SELECT f FROM FinaRenActividadComercial f WHERE f.fechaIngreso = :fechaIngreso"),
    @NamedQuery(name = "FinaRenActividadComercial.findByNivel", query = "SELECT f FROM FinaRenActividadComercial f WHERE f.nivel = :nivel"),
    @NamedQuery(name = "FinaRenActividadComercial.findByUsuarioIngreso", query = "SELECT f FROM FinaRenActividadComercial f WHERE f.usuarioIngreso = :usuarioIngreso")})
public class FinaRenActividadComercial implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 25)
    @Column(name = "ciu")
    private String ciu;
    @Size(max = 2147483647)
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "fecha_ingreso")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaIngreso;
    @Column(name = "nivel")
    private Integer nivel;
    @Column(name = "valor")
    private BigDecimal valor;
    @Size(max = 20)
    @Column(name = "usuario_ingreso")
    private String usuarioIngreso;
    @OneToMany(mappedBy = "actividadPrincipal", fetch = FetchType.LAZY)
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<FinaRenPatente> finaRenPatenteList;
    @ManyToMany(mappedBy = "actividad", fetch = FetchType.LAZY)
    private List<RenTasaTurismo> renTasaTurismosCollection;

    public FinaRenActividadComercial() {
    }

    public FinaRenActividadComercial(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCiu() {
        return ciu;
    }

    public void setCiu(String ciu) {
        this.ciu = ciu;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public Integer getNivel() {
        return nivel;
    }

    public void setNivel(Integer nivel) {
        this.nivel = nivel;
    }

    public String getUsuarioIngreso() {
        return usuarioIngreso;
    }

    public List<RenTasaTurismo> getRenTasaTurismosCollection() {
        return renTasaTurismosCollection;
    }

    public void setRenTasaTurismosCollection(List<RenTasaTurismo> renTasaTurismosCollection) {
        this.renTasaTurismosCollection = renTasaTurismosCollection;
    }

    public void setUsuarioIngreso(String usuarioIngreso) {
        this.usuarioIngreso = usuarioIngreso;
    }

    
    public List<FinaRenPatente> getFinaRenPatenteList() {
        return finaRenPatenteList;
    }

    public void setFinaRenPatenteList(List<FinaRenPatente> finaRenPatenteList) {
        this.finaRenPatenteList = finaRenPatenteList;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
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
        if (!(object instanceof FinaRenActividadComercial)) {
            return false;
        }
        FinaRenActividadComercial other = (FinaRenActividadComercial) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.educacion.Models.FinaRenActividadComercial[ id=" + id + " ]";
    }

}
