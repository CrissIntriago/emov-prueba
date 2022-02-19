/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Entities;

import com.asgard.Entity.FinaRenLocalCategoria;
import com.asgard.Entity.FinaRenRubrosLiquidacion;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author DEVELOPER
 */
@Entity
@Table(name = "ren_tasa_turismo", schema = Utils.SCHEMA_SGM)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RenTasaTurismo.findAll", query = "SELECT r FROM RenTasaTurismo r"),
    @NamedQuery(name = "RenTasaTurismo.findById", query = "SELECT r FROM RenTasaTurismo r WHERE r.id = :id"),
    @NamedQuery(name = "RenTasaTurismo.findByActividad", query = "SELECT r FROM RenTasaTurismo r WHERE r.actividad = :actividad"),
    @NamedQuery(name = "RenTasaTurismo.findByCategoria", query = "SELECT r FROM RenTasaTurismo r WHERE r.categoria = :categoria"),
    @NamedQuery(name = "RenTasaTurismo.findByDescripcion", query = "SELECT r FROM RenTasaTurismo r WHERE r.descripcion = :descripcion"),
    @NamedQuery(name = "RenTasaTurismo.findByEstado", query = "SELECT r FROM RenTasaTurismo r WHERE r.estado = :estado"),
    @NamedQuery(name = "RenTasaTurismo.findByFechaIngreso", query = "SELECT r FROM RenTasaTurismo r WHERE r.fechaIngreso = :fechaIngreso"),
    @NamedQuery(name = "RenTasaTurismo.findByMaximo", query = "SELECT r FROM RenTasaTurismo r WHERE r.maximo = :maximo"),
    @NamedQuery(name = "RenTasaTurismo.findByRubro", query = "SELECT r FROM RenTasaTurismo r WHERE r.rubro = :rubro"),
    @NamedQuery(name = "RenTasaTurismo.findByTipo", query = "SELECT r FROM RenTasaTurismo r WHERE r.tipo = :tipo"),
    @NamedQuery(name = "RenTasaTurismo.findByValor", query = "SELECT r FROM RenTasaTurismo r WHERE r.valor = :valor")})
public class RenTasaTurismo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "actividad")
    private BigInteger actividad;
    @JoinColumn(name = "categoria", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private FinaRenLocalCategoria categoria;
    @Size(max = 500)
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "fecha_ingreso")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaIngreso;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "maximo")
    private BigDecimal maximo;
    @JoinColumn(name = "rubro", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private FinaRenRubrosLiquidacion rubro;
    @Column(name = "tipo")
    private Integer tipo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "valor")
    private BigDecimal valor;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "renTasaTurismo1")
    private RenTasaTurismo renTasaTurismo;
    @JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private RenTasaTurismo renTasaTurismo1;
    @JoinColumn(name = "tipo_item", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private CatalogoItem tipoItem;
    @JoinColumn(name = "tipo_desglose", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private CatalogoItem tipoDesglose;

    public RenTasaTurismo() {
        valor = BigDecimal.ZERO;
    }

    public RenTasaTurismo(Long id) {
        this.id = id;
    }

    public RenTasaTurismo(Long id, BigDecimal valor) {
        this.id = id;
        this.valor = valor;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigInteger getActividad() {
        return actividad;
    }

    public void setActividad(BigInteger actividad) {
        this.actividad = actividad;
    }

    public FinaRenLocalCategoria getCategoria() {
        return categoria;
    }

    public void setCategoria(FinaRenLocalCategoria categoria) {
        this.categoria = categoria;
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

    public BigDecimal getMaximo() {
        return maximo;
    }

    public void setMaximo(BigDecimal maximo) {
        this.maximo = maximo;
    }

    public FinaRenRubrosLiquidacion getRubro() {
        return rubro;
    }

    public void setRubro(FinaRenRubrosLiquidacion rubro) {
        this.rubro = rubro;
    }

    public Integer getTipo() {
        return tipo;
    }

    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public RenTasaTurismo getRenTasaTurismo() {
        return renTasaTurismo;
    }

    public void setRenTasaTurismo(RenTasaTurismo renTasaTurismo) {
        this.renTasaTurismo = renTasaTurismo;
    }

    public RenTasaTurismo getRenTasaTurismo1() {
        return renTasaTurismo1;
    }

    public void setRenTasaTurismo1(RenTasaTurismo renTasaTurismo1) {
        this.renTasaTurismo1 = renTasaTurismo1;
    }

    public CatalogoItem getTipoItem() {
        return tipoItem;
    }

    public void setTipoItem(CatalogoItem tipoItem) {
        this.tipoItem = tipoItem;
    }

    public CatalogoItem getTipoDesglose() {
        return tipoDesglose;
    }

    public void setTipoDesglose(CatalogoItem tipoDesglose) {
        this.tipoDesglose = tipoDesglose;
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
        if (!(object instanceof RenTasaTurismo)) {
            return false;
        }
        RenTasaTurismo other = (RenTasaTurismo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.educacion.Models.RenTasaTurismo[ id=" + id + " ]";
    }

}
