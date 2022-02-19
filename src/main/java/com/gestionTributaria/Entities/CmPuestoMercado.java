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
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author DEVELOPER
 */
@Entity
@Table(name = "cm_puesto_mercado", schema = Utils.SCHEMA_SGM)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CmPuestoMercado.findAll", query = "SELECT c FROM CmPuestoMercado c"),
    @NamedQuery(name = "CmPuestoMercado.findById", query = "SELECT c FROM CmPuestoMercado c WHERE c.id = :id"),
    @NamedQuery(name = "CmPuestoMercado.findByArea", query = "SELECT c FROM CmPuestoMercado c WHERE c.area = :area"),
    @NamedQuery(name = "CmPuestoMercado.findByArrendatario", query = "SELECT c FROM CmPuestoMercado c WHERE c.arrendatario = :arrendatario"),
    @NamedQuery(name = "CmPuestoMercado.findByEstado", query = "SELECT c FROM CmPuestoMercado c WHERE c.estado = :estado"),
    @NamedQuery(name = "CmPuestoMercado.findByFechaIngreso", query = "SELECT c FROM CmPuestoMercado c WHERE c.fechaIngreso = :fechaIngreso"),
    @NamedQuery(name = "CmPuestoMercado.findByFechaUltimoPago", query = "SELECT c FROM CmPuestoMercado c WHERE c.fechaUltimoPago = :fechaUltimoPago"),
    @NamedQuery(name = "CmPuestoMercado.findByNombrePuesto", query = "SELECT c FROM CmPuestoMercado c WHERE c.nombrePuesto = :nombrePuesto"),
    @NamedQuery(name = "CmPuestoMercado.findByNumeroPuesto", query = "SELECT c FROM CmPuestoMercado c WHERE c.numeroPuesto = :numeroPuesto"),
    @NamedQuery(name = "CmPuestoMercado.findByTipoLocal", query = "SELECT c FROM CmPuestoMercado c WHERE c.tipoLocal = :tipoLocal"),
    @NamedQuery(name = "CmPuestoMercado.findByTipoAcividad", query = "SELECT c FROM CmPuestoMercado c WHERE c.tipoAcividad = :tipoAcividad"),
    @NamedQuery(name = "CmPuestoMercado.findByUsuarioIngreso", query = "SELECT c FROM CmPuestoMercado c WHERE c.usuarioIngreso = :usuarioIngreso")})
public class CmPuestoMercado implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "area")
    private BigDecimal area;
    @Column(name = "arrendatario")
    private BigInteger arrendatario;
    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "fecha_ingreso")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaIngreso;
    @Column(name = "fecha_ultimo_pago")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaUltimoPago;
    @Size(max = 250)
    @Column(name = "nombre_puesto")
    private String nombrePuesto;
    @Size(max = 50)
    @Column(name = "numero_puesto")
    private String numeroPuesto;
    @Column(name = "tipo_local")
    private BigInteger tipoLocal;
    @Column(name = "tipo_acividad")
    private BigInteger tipoAcividad;
    @Size(max = 20)
    @Column(name = "usuario_ingreso")
    private String usuarioIngreso;

    public CmPuestoMercado() {
    }

    public CmPuestoMercado(Long id) {
        this.id = id;
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

    public BigInteger getArrendatario() {
        return arrendatario;
    }

    public void setArrendatario(BigInteger arrendatario) {
        this.arrendatario = arrendatario;
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

    public Date getFechaUltimoPago() {
        return fechaUltimoPago;
    }

    public void setFechaUltimoPago(Date fechaUltimoPago) {
        this.fechaUltimoPago = fechaUltimoPago;
    }

    public String getNombrePuesto() {
        return nombrePuesto;
    }

    public void setNombrePuesto(String nombrePuesto) {
        this.nombrePuesto = nombrePuesto;
    }

    public String getNumeroPuesto() {
        return numeroPuesto;
    }

    public void setNumeroPuesto(String numeroPuesto) {
        this.numeroPuesto = numeroPuesto;
    }

    public BigInteger getTipoLocal() {
        return tipoLocal;
    }

    public void setTipoLocal(BigInteger tipoLocal) {
        this.tipoLocal = tipoLocal;
    }

    public BigInteger getTipoAcividad() {
        return tipoAcividad;
    }

    public void setTipoAcividad(BigInteger tipoAcividad) {
        this.tipoAcividad = tipoAcividad;
    }

    public String getUsuarioIngreso() {
        return usuarioIngreso;
    }

    public void setUsuarioIngreso(String usuarioIngreso) {
        this.usuarioIngreso = usuarioIngreso;
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
        if (!(object instanceof CmPuestoMercado)) {
            return false;
        }
        CmPuestoMercado other = (CmPuestoMercado) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.educacion.Models.CmPuestoMercado[ id=" + id + " ]";
    }
    
}
