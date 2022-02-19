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
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author DEVELOPER
 */
@Entity
@Table(name = "rec_actas_especies_det", schema = Utils.SCHEMA_SGM)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RecActasEspeciesDet.findAll", query = "SELECT r FROM RecActasEspeciesDet r"),
    @NamedQuery(name = "RecActasEspeciesDet.findById", query = "SELECT r FROM RecActasEspeciesDet r WHERE r.id = :id"),
    @NamedQuery(name = "RecActasEspeciesDet.findByCantidad", query = "SELECT r FROM RecActasEspeciesDet r WHERE r.cantidad = :cantidad"),
    @NamedQuery(name = "RecActasEspeciesDet.findByEstado", query = "SELECT r FROM RecActasEspeciesDet r WHERE r.estado = :estado"),
    @NamedQuery(name = "RecActasEspeciesDet.findByDesde", query = "SELECT r FROM RecActasEspeciesDet r WHERE r.desde = :desde"),
    @NamedQuery(name = "RecActasEspeciesDet.findByDisponibles", query = "SELECT r FROM RecActasEspeciesDet r WHERE r.disponibles = :disponibles"),
    @NamedQuery(name = "RecActasEspeciesDet.findByHasta", query = "SELECT r FROM RecActasEspeciesDet r WHERE r.hasta = :hasta"),
    @NamedQuery(name = "RecActasEspeciesDet.findByUltimoVendido", query = "SELECT r FROM RecActasEspeciesDet r WHERE r.ultimoVendido = :ultimoVendido"),
    @NamedQuery(name = "RecActasEspeciesDet.findByValorUni", query = "SELECT r FROM RecActasEspeciesDet r WHERE r.valorUni = :valorUni"),
    @NamedQuery(name = "RecActasEspeciesDet.findByValorTotal", query = "SELECT r FROM RecActasEspeciesDet r WHERE r.valorTotal = :valorTotal")})
public class RecActasEspeciesDet implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "cantidad")
    private Integer cantidad;
    @Size(max = 2147483647)
    @Column(name = "estado")
    private String estado;
    @Column(name = "desde")
    private Long desde;
    @Column(name = "disponibles")
    private Integer disponibles;
    @Column(name = "hasta")
    private Long hasta;
    @Column(name = "ultimo_vendido")
    private Long ultimoVendido;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor_uni")
    private BigDecimal valorUni;
    @Column(name = "valor_total")
    private BigDecimal valorTotal;
    @JoinColumn(name = "acta", referencedColumnName = "id")
    @ManyToOne
    private RecActasEspecies acta;
    @JoinColumn(name = "especie", referencedColumnName = "id")
    @ManyToOne
    private RecEspecies especie;
    @Transient
    private Long desdeTemp;
    @Transient
    private Long hastaTemp;
    @Transient
    private Integer disponiblesTemp;
 
    public RecActasEspeciesDet() {
    }

    public RecActasEspeciesDet(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Long getDesde() {
        return desde;
    }

    public void setDesde(Long desde) {
        this.desde = desde;
    }

    public Integer getDisponibles() {
        return disponibles;
    }

    public void setDisponibles(Integer disponibles) {
        this.disponibles = disponibles;
    }

    public Long getHasta() {
        return hasta;
    }

    public void setHasta(Long hasta) {
        this.hasta = hasta;
    }

    public Long getUltimoVendido() {
        return ultimoVendido;
    }

    public void setUltimoVendido(Long ultimoVendido) {
        this.ultimoVendido = ultimoVendido;
    }

    public BigDecimal getValorUni() {
        return valorUni;
    }

    public void setValorUni(BigDecimal valorUni) {
        this.valorUni = valorUni;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public RecActasEspecies getActa() {
        return acta;
    }

    public void setActa(RecActasEspecies acta) {
        this.acta = acta;
    }

    public RecEspecies getEspecie() {
        return especie;
    }

    public void setEspecie(RecEspecies especie) {
        this.especie = especie;
    }

    public Long getDesdeTemp() {
        return desdeTemp;
    }

    public void setDesdeTemp(Long desdeTemp) {
        this.desdeTemp = desdeTemp;
    }

    public Long getHastaTemp() {
        return hastaTemp;
    }

    public void setHastaTemp(Long hastaTemp) {
        this.hastaTemp = hastaTemp;
    }

    public Integer getDisponiblesTemp() {
        return disponiblesTemp;
    }

    public void setDisponiblesTemp(Integer disponiblesTemp) {
        this.disponiblesTemp = disponiblesTemp;
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
        if (!(object instanceof RecActasEspeciesDet)) {
            return false;
        }
        RecActasEspeciesDet other = (RecActasEspeciesDet) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.educacion.Models.RecActasEspeciesDet[ id=" + id + " ]";
    }

}
