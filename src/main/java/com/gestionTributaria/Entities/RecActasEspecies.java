/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Entities;

import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author DEVELOPER
 */
@Entity
@Table(name = "rec_actas_especies", schema = Utils.SCHEMA_SGM)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RecActasEspecies.findAll", query = "SELECT r FROM RecActasEspecies r"),
    @NamedQuery(name = "RecActasEspecies.findById", query = "SELECT r FROM RecActasEspecies r WHERE r.id = :id"),
    @NamedQuery(name = "RecActasEspecies.findByAnio", query = "SELECT r FROM RecActasEspecies r WHERE r.anio = :anio"),
    @NamedQuery(name = "RecActasEspecies.findByEstado", query = "SELECT r FROM RecActasEspecies r WHERE r.estado = :estado"),
    @NamedQuery(name = "RecActasEspecies.findByFechaIngreso", query = "SELECT r FROM RecActasEspecies r WHERE r.fechaIngreso = :fechaIngreso"),
    @NamedQuery(name = "RecActasEspecies.findByNumActa", query = "SELECT r FROM RecActasEspecies r WHERE r.numActa = :numActa"),
    @NamedQuery(name = "RecActasEspecies.findByObservacion", query = "SELECT r FROM RecActasEspecies r WHERE r.observacion = :observacion"),
    @NamedQuery(name = "RecActasEspecies.findByTotal", query = "SELECT r FROM RecActasEspecies r WHERE r.total = :total"),
    @NamedQuery(name = "RecActasEspecies.findByTesorero", query = "SELECT r FROM RecActasEspecies r WHERE r.tesorero = :tesorero"),
    @NamedQuery(name = "RecActasEspecies.findByUsuarioAsignado", query = "SELECT r FROM RecActasEspecies r WHERE r.usuarioAsignado = :usuarioAsignado"),
    @NamedQuery(name = "RecActasEspecies.findByUsuarioIngreso", query = "SELECT r FROM RecActasEspecies r WHERE r.usuarioIngreso = :usuarioIngreso")})
public class RecActasEspecies implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "anio")
    private Integer anio;
    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "fecha_ingreso")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaIngreso;
    @Column(name = "num_acta")
    private Integer numActa;
    @Size(max = 2147483647)
    @Column(name = "observacion")
    private String observacion;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "total")
    private BigDecimal total;
    @Column(name = "tesorero")
    private BigInteger tesorero;
    @Column(name = "usuario_asignado")
    private BigInteger usuarioAsignado;
    @Size(max = 100)
    @Column(name = "usuario_ingreso")
    private String usuarioIngreso;
    @OneToMany(mappedBy = "acta")
    private List<RecActasEspeciesDet> recActasEspeciesDetList;

    public RecActasEspecies() {
    }

    public RecActasEspecies(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
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

    public Integer getNumActa() {
        return numActa;
    }

    public void setNumActa(Integer numActa) {
        this.numActa = numActa;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigInteger getTesorero() {
        return tesorero;
    }

    public void setTesorero(BigInteger tesorero) {
        this.tesorero = tesorero;
    }

    public BigInteger getUsuarioAsignado() {
        return usuarioAsignado;
    }

    public void setUsuarioAsignado(BigInteger usuarioAsignado) {
        this.usuarioAsignado = usuarioAsignado;
    }

    public String getUsuarioIngreso() {
        return usuarioIngreso;
    }

    public void setUsuarioIngreso(String usuarioIngreso) {
        this.usuarioIngreso = usuarioIngreso;
    }

    
    public List<RecActasEspeciesDet> getRecActasEspeciesDetList() {
        return recActasEspeciesDetList;
    }

    public void setRecActasEspeciesDetList(List<RecActasEspeciesDet> recActasEspeciesDetList) {
        this.recActasEspeciesDetList = recActasEspeciesDetList;
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
        if (!(object instanceof RecActasEspecies)) {
            return false;
        }
        RecActasEspecies other = (RecActasEspecies) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.educacion.Models.RecActasEspecies[ id=" + id + " ]";
    }
    
}
