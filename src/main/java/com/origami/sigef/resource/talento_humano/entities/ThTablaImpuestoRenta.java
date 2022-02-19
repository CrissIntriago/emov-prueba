/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.talento_humano.entities;

import java.io.Serializable;
import java.math.BigDecimal;
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

/**
 *
 * @author Criss Intriago
 * @author Jonathan Choez
 */
@Entity
@Table(name = "th_tabla_impuesto_renta",schema = "talento_humano")
@NamedQueries({
    @NamedQuery(name = "ThTablaImpuestoRenta.findAll", query = "SELECT t FROM ThTablaImpuestoRenta t"),
    @NamedQuery(name = "ThTablaImpuestoRenta.findById", query = "SELECT t FROM ThTablaImpuestoRenta t WHERE t.id = :id"),
    @NamedQuery(name = "ThTablaImpuestoRenta.findByFraccionBasica", query = "SELECT t FROM ThTablaImpuestoRenta t WHERE t.fraccionBasica = :fraccionBasica"),
    @NamedQuery(name = "ThTablaImpuestoRenta.findByExcesoHasta", query = "SELECT t FROM ThTablaImpuestoRenta t WHERE t.excesoHasta = :excesoHasta"),
    @NamedQuery(name = "ThTablaImpuestoRenta.findByImpuestoFraccionBasica", query = "SELECT t FROM ThTablaImpuestoRenta t WHERE t.impuestoFraccionBasica = :impuestoFraccionBasica"),
    @NamedQuery(name = "ThTablaImpuestoRenta.findByPorcentajeFraccionExcedente", query = "SELECT t FROM ThTablaImpuestoRenta t WHERE t.porcentajeFraccionExcedente = :porcentajeFraccionExcedente"),
    @NamedQuery(name = "ThTablaImpuestoRenta.findByPeriodo", query = "SELECT t FROM ThTablaImpuestoRenta t WHERE t.periodo = :periodo"),
    @NamedQuery(name = "ThTablaImpuestoRenta.findByFechaCreacion", query = "SELECT t FROM ThTablaImpuestoRenta t WHERE t.fechaCreacion = :fechaCreacion"),
    @NamedQuery(name = "ThTablaImpuestoRenta.findByUsuarioCreacion", query = "SELECT t FROM ThTablaImpuestoRenta t WHERE t.usuarioCreacion = :usuarioCreacion"),
    @NamedQuery(name = "ThTablaImpuestoRenta.findByFechaModificacion", query = "SELECT t FROM ThTablaImpuestoRenta t WHERE t.fechaModificacion = :fechaModificacion"),
    @NamedQuery(name = "ThTablaImpuestoRenta.findByUsuarioModifica", query = "SELECT t FROM ThTablaImpuestoRenta t WHERE t.usuarioModifica = :usuarioModifica"),
    @NamedQuery(name = "ThTablaImpuestoRenta.findByEstado", query = "SELECT t FROM ThTablaImpuestoRenta t WHERE t.estado = :estado")})
public class ThTablaImpuestoRenta implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "fraccion_basica")
    private BigDecimal fraccionBasica;
    @Column(name = "exceso_hasta")
    private BigDecimal excesoHasta;
    @Column(name = "impuesto_fraccion_basica")
    private BigDecimal impuestoFraccionBasica;
    @Column(name = "porcentaje_fraccion_excedente")
    private Double porcentajeFraccionExcedente;
    @Column(name = "periodo")
    private Short periodo;
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.DATE)
    private Date fechaCreacion;
    @Size(max = 100)
    @Column(name = "usuario_creacion")
    private String usuarioCreacion;
    @Column(name = "fecha_modificacion")
    @Temporal(TemporalType.DATE)
    private Date fechaModificacion;
    @Size(max = 100)
    @Column(name = "usuario_modifica")
    private String usuarioModifica;
    @Column(name = "estado")
    private Boolean estado;
    
    public ThTablaImpuestoRenta() {
        this.estado = true;
        this.fraccionBasica = BigDecimal.ZERO;
        this.impuestoFraccionBasica = BigDecimal.ZERO;
        this.porcentajeFraccionExcedente = Double.valueOf("0");
    }
    
    public ThTablaImpuestoRenta(Long id) {
        this.id = id;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public BigDecimal getFraccionBasica() {
        return fraccionBasica;
    }
    
    public void setFraccionBasica(BigDecimal fraccionBasica) {
        this.fraccionBasica = fraccionBasica;
    }
    
    public BigDecimal getExcesoHasta() {
        return excesoHasta;
    }
    
    public void setExcesoHasta(BigDecimal excesoHasta) {
        this.excesoHasta = excesoHasta;
    }
    
    public BigDecimal getImpuestoFraccionBasica() {
        return impuestoFraccionBasica;
    }
    
    public void setImpuestoFraccionBasica(BigDecimal impuestoFraccionBasica) {
        this.impuestoFraccionBasica = impuestoFraccionBasica;
    }
    
    public Double getPorcentajeFraccionExcedente() {
        return porcentajeFraccionExcedente;
    }
    
    public void setPorcentajeFraccionExcedente(Double porcentajeFraccionExcedente) {
        this.porcentajeFraccionExcedente = porcentajeFraccionExcedente;
    }
    
    public Short getPeriodo() {
        return periodo;
    }
    
    public void setPeriodo(Short periodo) {
        this.periodo = periodo;
    }
    
    public Date getFechaCreacion() {
        return fechaCreacion;
    }
    
    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    
    public String getUsuarioCreacion() {
        return usuarioCreacion;
    }
    
    public void setUsuarioCreacion(String usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
    }
    
    public Date getFechaModificacion() {
        return fechaModificacion;
    }
    
    public void setFechaModificacion(Date fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }
    
    public String getUsuarioModifica() {
        return usuarioModifica;
    }
    
    public void setUsuarioModifica(String usuarioModifica) {
        this.usuarioModifica = usuarioModifica;
    }
    
    public Boolean getEstado() {
        return estado;
    }
    
    public void setEstado(Boolean estado) {
        this.estado = estado;
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
        if (!(object instanceof ThTablaImpuestoRenta)) {
            return false;
        }
        ThTablaImpuestoRenta other = (ThTablaImpuestoRenta) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return "com.origami.sigef.resource.talento_humano.entities.ThTablaImpuestoRenta[ id=" + id + " ]";
    }
    
}
