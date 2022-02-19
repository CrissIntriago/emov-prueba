/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.asgard.Entity;

import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
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
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;
import com.gestionTributaria.Entities.DetRubroMejoras;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

/**
 *
 * @author DEVELOPER
 */
@Entity
@Table(name = "fina_ren_det_liquidacion", schema = Utils.SCHEMA_ASGARD)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FinaRenDetLiquidacion.findAll", query = "SELECT f FROM FinaRenDetLiquidacion f")
    ,
    @NamedQuery(name = "FinaRenDetLiquidacion.findById", query = "SELECT f FROM FinaRenDetLiquidacion f WHERE f.id = :id")})
public class FinaRenDetLiquidacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "cantidad")
    private Integer cantidad;
    @Column(name = "desde")
    private BigInteger desde;
    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "hasta")
    private BigInteger hasta;
    @Column(name = "rec_actas_especies_det")
    private Long recActasEspeciesDet;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor")
    private BigDecimal valor;
    @Column(name = "valor_recaudado")
    private BigDecimal valorRecaudado;
    @Column(name = "valor_sin_descuento")
    private BigDecimal valorSinDescuento;
    @JoinColumn(name = "liquidacion", referencedColumnName = "id")
    @ManyToOne
    private FinaRenLiquidacion liquidacion;
    @JoinColumn(name = "rubro", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private FinaRenRubrosLiquidacion rubro;
    @Transient
    private String descripcion;
    @Transient
    private Boolean cobrar;
    @Transient
    private Long codigoRubro;

    @OneToMany(mappedBy = "rubroMejora", fetch = FetchType.LAZY)
    @OrderBy("valor ASC")
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<DetRubroMejoras> mejDetRubroMejorasCollection;

    public FinaRenDetLiquidacion() {
        valorRecaudado = BigDecimal.ZERO;
    }

    public FinaRenDetLiquidacion(Long id) {
        this.id = id;
    }

    public FinaRenDetLiquidacion(BigDecimal valor, FinaRenLiquidacion liquidacion, FinaRenRubrosLiquidacion rubro) {
        this.valor = valor;
        this.liquidacion = liquidacion;
        this.rubro = rubro;
        this.estado = true;
    }

    public FinaRenDetLiquidacion(BigDecimal valor, FinaRenRubrosLiquidacion idRubro, String desc) {
        estado = true;
        this.valor = valor;
        rubro = idRubro;
        this.descripcion = desc;
    }

    public FinaRenDetLiquidacion(BigDecimal ipAct, FinaRenLiquidacion c, Long id) {
        this.valor = ipAct;
        this.liquidacion = c;
        this.rubro = new FinaRenRubrosLiquidacion(id);
        this.estado = true;
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

    public BigInteger getDesde() {
        return desde;
    }

    public void setDesde(BigInteger desde) {
        this.desde = desde;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public BigInteger getHasta() {
        return hasta;
    }

    public void setHasta(BigInteger hasta) {
        this.hasta = hasta;
    }

    public Long getRecActasEspeciesDet() {
        return recActasEspeciesDet;
    }

    public void setRecActasEspeciesDet(Long recActasEspeciesDet) {
        this.recActasEspeciesDet = recActasEspeciesDet;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public BigDecimal getValorRecaudado() {
        return valorRecaudado;
    }

    public void setValorRecaudado(BigDecimal valorRecaudado) {
        this.valorRecaudado = valorRecaudado;
    }

    public BigDecimal getValorSinDescuento() {
        return valorSinDescuento;
    }

    public void setValorSinDescuento(BigDecimal valorSinDescuento) {
        this.valorSinDescuento = valorSinDescuento;
    }

    public FinaRenLiquidacion getLiquidacion() {
        return liquidacion;
    }

    public void setLiquidacion(FinaRenLiquidacion liquidacion) {
        this.liquidacion = liquidacion;
    }

    public FinaRenRubrosLiquidacion getRubro() {
        return rubro;
    }

    public void setRubro(FinaRenRubrosLiquidacion rubro) {
        this.rubro = rubro;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Boolean getCobrar() {
        return cobrar;
    }

    public void setCobrar(Boolean cobrar) {
        this.cobrar = cobrar;
    }

    public Long getCodigoRubro() {
        return codigoRubro;
    }

    public void setCodigoRubro(Long codigoRubro) {
        this.codigoRubro = codigoRubro;
    }

    public List<DetRubroMejoras> getMejDetRubroMejorasCollection() {
        return mejDetRubroMejorasCollection;
    }

    public void setMejDetRubroMejorasCollection(List<DetRubroMejoras> mejDetRubroMejorasCollection) {
        this.mejDetRubroMejorasCollection = mejDetRubroMejorasCollection;
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
        if (!(object instanceof FinaRenDetLiquidacion)) {
            return false;
        }
        FinaRenDetLiquidacion other = (FinaRenDetLiquidacion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.educacion.Models.FinaRenDetLiquidacion[ id=" + id + " ]";
    }

}
