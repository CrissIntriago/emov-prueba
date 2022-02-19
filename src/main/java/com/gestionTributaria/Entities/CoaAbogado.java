/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Entities;

import com.origami.sigef.common.entities.Usuarios;
import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import java.math.BigInteger;
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
@Table(name = "coa_abogado", schema = Utils.SCHEMA_SGM)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CoaAbogado.findAll", query = "SELECT c FROM CoaAbogado c")})
public class CoaAbogado implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "abreviatura")
    private String abreviatura;
    @JoinColumn(name = "auth_usuarios", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Usuarios authUsuarios;
    @Column(name = "codigo_sac")
    private Integer codigoSac;
    @Column(name = "detalle")
    private String detalle;
    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "fecha_ingreso")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaIngreso;
    @Column(name = "usuario_ingreso")
    private String usuarioIngreso;
    @OneToMany(mappedBy = "abogadoJuicio", fetch = FetchType.LAZY)
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<CoaJuicio> coaJuicioList;
    @OneToMany(mappedBy = "abogadoJuicio", fetch = FetchType.LAZY)
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<CoaJuicioPredio> coaJuicioPredioList;
    @Column(name = "abogado_interno")
    private Boolean abogadoInterno;

    public CoaAbogado() {
    }

    public CoaAbogado(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAbreviatura() {
        return abreviatura;
    }

    public void setAbreviatura(String abreviatura) {
        this.abreviatura = abreviatura;
    }

    public Usuarios getAuthUsuarios() {
        return authUsuarios;
    }

    public void setAuthUsuarios(Usuarios authUsuarios) {
        this.authUsuarios = authUsuarios;
    }

    public Integer getCodigoSac() {
        return codigoSac;
    }

    public void setCodigoSac(Integer codigoSac) {
        this.codigoSac = codigoSac;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
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

    public String getUsuarioIngreso() {
        return usuarioIngreso;
    }

    public void setUsuarioIngreso(String usuarioIngreso) {
        this.usuarioIngreso = usuarioIngreso;
    }

    public Boolean getAbogadoInterno() {
        return abogadoInterno;
    }

    public void setAbogadoInterno(Boolean abogadoInterno) {
        this.abogadoInterno = abogadoInterno;
    }

    @XmlTransient
    public List<CoaJuicio> getCoaJuicioList() {
        return coaJuicioList;
    }

    public void setCoaJuicioList(List<CoaJuicio> coaJuicioList) {
        this.coaJuicioList = coaJuicioList;
    }

    @XmlTransient
    public List<CoaJuicioPredio> getCoaJuicioPredioList() {
        return coaJuicioPredioList;
    }

    public void setCoaJuicioPredioList(List<CoaJuicioPredio> coaJuicioPredioList) {
        this.coaJuicioPredioList = coaJuicioPredioList;
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
        if (!(object instanceof CoaAbogado)) {
            return false;
        }
        CoaAbogado other = (CoaAbogado) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.educacion.Models.CoaAbogado[ id=" + id + " ]";
    }
    
}
