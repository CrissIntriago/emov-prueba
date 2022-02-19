/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Entities;

import java.io.Serializable;
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
 * @author Administrator
 */
@Entity
@Table(name = "coa_abogado")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CoaAbogado_1.findAll", query = "SELECT c FROM CoaAbogado_1 c"),
    @NamedQuery(name = "CoaAbogado_1.findById", query = "SELECT c FROM CoaAbogado_1 c WHERE c.id = :id"),
    @NamedQuery(name = "CoaAbogado_1.findByAbreviatura", query = "SELECT c FROM CoaAbogado_1 c WHERE c.abreviatura = :abreviatura"),
    @NamedQuery(name = "CoaAbogado_1.findByAuthUsuarios", query = "SELECT c FROM CoaAbogado_1 c WHERE c.authUsuarios = :authUsuarios"),
    @NamedQuery(name = "CoaAbogado_1.findByCodigoSac", query = "SELECT c FROM CoaAbogado_1 c WHERE c.codigoSac = :codigoSac"),
    @NamedQuery(name = "CoaAbogado_1.findByDetalle", query = "SELECT c FROM CoaAbogado_1 c WHERE c.detalle = :detalle"),
    @NamedQuery(name = "CoaAbogado_1.findByEstado", query = "SELECT c FROM CoaAbogado_1 c WHERE c.estado = :estado"),
    @NamedQuery(name = "CoaAbogado_1.findByFechaIngreso", query = "SELECT c FROM CoaAbogado_1 c WHERE c.fechaIngreso = :fechaIngreso"),
    @NamedQuery(name = "CoaAbogado_1.findByUsuarioIngreso", query = "SELECT c FROM CoaAbogado_1 c WHERE c.usuarioIngreso = :usuarioIngreso"),
    @NamedQuery(name = "CoaAbogado_1.findByAbogadoInterno", query = "SELECT c FROM CoaAbogado_1 c WHERE c.abogadoInterno = :abogadoInterno")})
public class CoaAbogado_1 implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 40)
    @Column(name = "abreviatura")
    private String abreviatura;
    @Column(name = "auth_usuarios")
    private BigInteger authUsuarios;
    @Column(name = "codigo_sac")
    private Integer codigoSac;
    @Size(max = 2147483647)
    @Column(name = "detalle")
    private String detalle;
    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "fecha_ingreso")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaIngreso;
    @Size(max = 100)
    @Column(name = "usuario_ingreso")
    private String usuarioIngreso;
    @Column(name = "abogado_interno")
    private Boolean abogadoInterno;
    @OneToMany(mappedBy = "abogadoJuicio")
    private List<CoaJuicio> coaJuicioList;

    public CoaAbogado_1() {
    }

    public CoaAbogado_1(Long id) {
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

    public BigInteger getAuthUsuarios() {
        return authUsuarios;
    }

    public void setAuthUsuarios(BigInteger authUsuarios) {
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CoaAbogado_1)) {
            return false;
        }
        CoaAbogado_1 other = (CoaAbogado_1) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gestionTributaria.Entities.CoaAbogado_1[ id=" + id + " ]";
    }

}
