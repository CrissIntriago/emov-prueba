/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Dairon Freddy
 */
@Entity
@Table(name = "numero_meta_logrado")
@NamedQueries({
    @NamedQuery(name = "NumeroMetaLogrado.findAll", query = "SELECT n FROM NumeroMetaLogrado n"),
    @NamedQuery(name = "NumeroMetaLogrado.findById", query = "SELECT n FROM NumeroMetaLogrado n WHERE n.id = :id"),
    @NamedQuery(name = "NumeroMetaLogrado.findByCuadrante1", query = "SELECT n FROM NumeroMetaLogrado n WHERE n.cuadrante1 = :cuadrante1"),
    @NamedQuery(name = "NumeroMetaLogrado.findByCuadrante2", query = "SELECT n FROM NumeroMetaLogrado n WHERE n.cuadrante2 = :cuadrante2"),
    @NamedQuery(name = "NumeroMetaLogrado.findByCuadrante3", query = "SELECT n FROM NumeroMetaLogrado n WHERE n.cuadrante3 = :cuadrante3"),
    @NamedQuery(name = "NumeroMetaLogrado.findByTotalMetaLograda", query = "SELECT n FROM NumeroMetaLogrado n WHERE n.totalMetaLograda = :totalMetaLograda"),
    @NamedQuery(name = "NumeroMetaLogrado.findByTotalPresupuestoEjecutado", query = "SELECT n FROM NumeroMetaLogrado n WHERE n.totalPresupuestoEjecutado = :totalPresupuestoEjecutado"),
    @NamedQuery(name = "NumeroMetaLogrado.findByResultadoEficacia", query = "SELECT n FROM NumeroMetaLogrado n WHERE n.resultadoEficacia = :resultadoEficacia")})
public class NumeroMetaLogrado implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "cuadrante_1")
    private BigInteger cuadrante1;
    @Column(name = "cuadrante_2")
    private BigInteger cuadrante2;
    @Column(name = "cuadrante_3")
    private BigInteger cuadrante3;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "total_meta_lograda")
    private BigDecimal totalMetaLograda;
    @Column(name = "total_presupuesto_ejecutado")
    private BigDecimal totalPresupuestoEjecutado;
    @Column(name = "resultado_eficacia")
    private BigDecimal resultadoEficacia;
    @JoinColumn(name = "programacion_politica", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ProgramacionPoliticaPublica programacionPolitica;

    public NumeroMetaLogrado() {
    }

    public NumeroMetaLogrado(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigInteger getCuadrante1() {
        return cuadrante1;
    }

    public void setCuadrante1(BigInteger cuadrante1) {
        this.cuadrante1 = cuadrante1;
    }

    public BigInteger getCuadrante2() {
        return cuadrante2;
    }

    public void setCuadrante2(BigInteger cuadrante2) {
        this.cuadrante2 = cuadrante2;
    }

    public BigInteger getCuadrante3() {
        return cuadrante3;
    }

    public void setCuadrante3(BigInteger cuadrante3) {
        this.cuadrante3 = cuadrante3;
    }

    public BigDecimal getTotalMetaLograda() {
        return totalMetaLograda;
    }

    public void setTotalMetaLograda(BigDecimal totalMetaLograda) {
        this.totalMetaLograda = totalMetaLograda;
    }

    public BigDecimal getTotalPresupuestoEjecutado() {
        return totalPresupuestoEjecutado;
    }

    public void setTotalPresupuestoEjecutado(BigDecimal totalPresupuestoEjecutado) {
        this.totalPresupuestoEjecutado = totalPresupuestoEjecutado;
    }

    public BigDecimal getResultadoEficacia() {
        return resultadoEficacia;
    }

    public void setResultadoEficacia(BigDecimal resultadoEficacia) {
        this.resultadoEficacia = resultadoEficacia;
    }

    public ProgramacionPoliticaPublica getProgramacionPolitica() {
        return programacionPolitica;
    }

    public void setProgramacionPolitica(ProgramacionPoliticaPublica programacionPolitica) {
        this.programacionPolitica = programacionPolitica;
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
        if (!(object instanceof NumeroMetaLogrado)) {
            return false;
        }
        NumeroMetaLogrado other = (NumeroMetaLogrado) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.common.entities.NumeroMetaLogrado[ id=" + id + " ]";
    }

}
