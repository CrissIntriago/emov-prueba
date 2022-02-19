/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

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
 * @author Origami
 */
@Entity
@Table(name = "tabla_impuesto_renta", schema = "talento_humano")
@NamedQueries({
    @NamedQuery(name = "TablaImpuestoRenta.findAll", query = "SELECT t FROM TablaImpuestoRenta t")
    , @NamedQuery(name = "TablaImpuestoRenta.findById", query = "SELECT t FROM TablaImpuestoRenta t WHERE t.id = :id")
    , @NamedQuery(name = "TablaImpuestoRenta.findByFraccionBasica", query = "SELECT t FROM TablaImpuestoRenta t WHERE t.fraccionBasica = :fraccionBasica")
    , @NamedQuery(name = "TablaImpuestoRenta.findByExcesoHasta", query = "SELECT t FROM TablaImpuestoRenta t WHERE t.excesoHasta = :excesoHasta")
    , @NamedQuery(name = "TablaImpuestoRenta.findByImpuestoFraccionBasica", query = "SELECT t FROM TablaImpuestoRenta t WHERE t.impuestoFraccionBasica = :impuestoFraccionBasica")
    , @NamedQuery(name = "TablaImpuestoRenta.findByPorcentajeFraccionExcedente", query = "SELECT t FROM TablaImpuestoRenta t WHERE t.porcentajeFraccionExcedente = :porcentajeFraccionExcedente")
    , @NamedQuery(name = "TablaImpuestoRenta.findByPeriodo", query = "SELECT t FROM TablaImpuestoRenta t WHERE t.periodo = :periodo")
    , @NamedQuery(name = "TablaImpuestoRenta.findByFechaCreacion", query = "SELECT t FROM TablaImpuestoRenta t WHERE t.fechaCreacion = :fechaCreacion")
    , @NamedQuery(name = "TablaImpuestoRenta.findByUsuarioCreacion", query = "SELECT t FROM TablaImpuestoRenta t WHERE t.usuarioCreacion = :usuarioCreacion")
    , @NamedQuery(name = "TablaImpuestoRenta.findByFechaModificacion", query = "SELECT t FROM TablaImpuestoRenta t WHERE t.fechaModificacion = :fechaModificacion")
    , @NamedQuery(name = "TablaImpuestoRenta.findByUsuarioModifica", query = "SELECT t FROM TablaImpuestoRenta t WHERE t.usuarioModifica = :usuarioModifica")
    , @NamedQuery(name = "TablaImpuestoRenta.findByEstado", query = "SELECT t FROM TablaImpuestoRenta t WHERE t.estado = :estado")})
public class TablaImpuestoRenta implements Serializable {

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
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @Size(max = 100)
    @Column(name = "usuario_creacion")
    private String usuarioCreacion;
    @Column(name = "fecha_modificacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaModificacion;
    @Size(max = 100)
    @Column(name = "usuario_modifica")
    private String usuarioModifica;
    @Column(name = "estado")
    private Boolean estado;


    public TablaImpuestoRenta() {
        this.estado = Boolean.TRUE;
        this.fraccionBasica = BigDecimal.ZERO;
        this.impuestoFraccionBasica = BigDecimal.ZERO;
        this.excesoHasta = BigDecimal.ZERO;
        this.porcentajeFraccionExcedente = (double) 0;
    }

    public TablaImpuestoRenta(Long id) {
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
        if (!(object instanceof TablaImpuestoRenta)) {
            return false;
        }
        TablaImpuestoRenta other = (TablaImpuestoRenta) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.mavenproject1.TablaImpuestoRenta[ id=" + id + " ]";
    }

}
