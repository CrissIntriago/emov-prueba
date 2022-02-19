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
@Table(name = "ren_desvalorizacion", schema = Utils.SCHEMA_SGM)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RenDesvalorizacion.findAll", query = "SELECT r FROM RenDesvalorizacion r"),
    @NamedQuery(name = "RenDesvalorizacion.findById", query = "SELECT r FROM RenDesvalorizacion r WHERE r.id = :id"),
    @NamedQuery(name = "RenDesvalorizacion.findByAnio", query = "SELECT r FROM RenDesvalorizacion r WHERE r.anio = :anio"),
    @NamedQuery(name = "RenDesvalorizacion.findByFechaCreac", query = "SELECT r FROM RenDesvalorizacion r WHERE r.fechaCreac = :fechaCreac"),
    @NamedQuery(name = "RenDesvalorizacion.findByEstado", query = "SELECT r FROM RenDesvalorizacion r WHERE r.estado = :estado"),
    @NamedQuery(name = "RenDesvalorizacion.findByPorcentajeRebaja", query = "SELECT r FROM RenDesvalorizacion r WHERE r.porcentajeRebaja = :porcentajeRebaja"),
    @NamedQuery(name = "RenDesvalorizacion.findByUsuarioCreac", query = "SELECT r FROM RenDesvalorizacion r WHERE r.usuarioCreac = :usuarioCreac"),
    @NamedQuery(name = "RenDesvalorizacion.findByValor", query = "SELECT r FROM RenDesvalorizacion r WHERE r.valor = :valor")})
public class RenDesvalorizacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "anio")
    private int anio;
    @Column(name = "fecha_creac")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreac;
    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "porcentaje_rebaja")
    private Integer porcentajeRebaja;
    @Size(max = 20)
    @Column(name = "usuario_creac")
    private String usuarioCreac;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor")
    private BigDecimal valor;
    @OneToMany(mappedBy = "desvalorizacion", fetch = FetchType.LAZY)
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<RenValoresPlusvalia> renValoresPlusvaliaList;

    public RenDesvalorizacion() {
    }

    public RenDesvalorizacion(Long id) {
        this.id = id;
    }

    public RenDesvalorizacion(Long id, int anio) {
        this.id = id;
        this.anio = anio;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public Date getFechaCreac() {
        return fechaCreac;
    }

    public void setFechaCreac(Date fechaCreac) {
        this.fechaCreac = fechaCreac;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Integer getPorcentajeRebaja() {
        return porcentajeRebaja;
    }

    public void setPorcentajeRebaja(Integer porcentajeRebaja) {
        this.porcentajeRebaja = porcentajeRebaja;
    }

    public String getUsuarioCreac() {
        return usuarioCreac;
    }

    public void setUsuarioCreac(String usuarioCreac) {
        this.usuarioCreac = usuarioCreac;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    
    public List<RenValoresPlusvalia> getRenValoresPlusvaliaList() {
        return renValoresPlusvaliaList;
    }

    public void setRenValoresPlusvaliaList(List<RenValoresPlusvalia> renValoresPlusvaliaList) {
        this.renValoresPlusvaliaList = renValoresPlusvaliaList;
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
        if (!(object instanceof RenDesvalorizacion)) {
            return false;
        }
        RenDesvalorizacion other = (RenDesvalorizacion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.educacion.Models.RenDesvalorizacion[ id=" + id + " ]";
    }
    
}
