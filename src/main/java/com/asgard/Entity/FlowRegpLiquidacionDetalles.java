/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.asgard.Entity;

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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "flow_regp_liquidacion_detalles", schema = Utils.SCHEMA_ASGARD)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FlowRegpLiquidacionDetalles.findAll", query = "SELECT f FROM FlowRegpLiquidacionDetalles f"),
    @NamedQuery(name = "FlowRegpLiquidacionDetalles.findById", query = "SELECT f FROM FlowRegpLiquidacionDetalles f WHERE f.id = :id"),
    @NamedQuery(name = "FlowRegpLiquidacionDetalles.findByActo", query = "SELECT f FROM FlowRegpLiquidacionDetalles f WHERE f.acto = :acto"),
    @NamedQuery(name = "FlowRegpLiquidacionDetalles.findByNumPredio", query = "SELECT f FROM FlowRegpLiquidacionDetalles f WHERE f.numPredio = :numPredio"),
    @NamedQuery(name = "FlowRegpLiquidacionDetalles.findByNomUrb", query = "SELECT f FROM FlowRegpLiquidacionDetalles f WHERE f.nomUrb = :nomUrb"),
    @NamedQuery(name = "FlowRegpLiquidacionDetalles.findByMzUrb", query = "SELECT f FROM FlowRegpLiquidacionDetalles f WHERE f.mzUrb = :mzUrb"),
    @NamedQuery(name = "FlowRegpLiquidacionDetalles.findBySlUrb", query = "SELECT f FROM FlowRegpLiquidacionDetalles f WHERE f.slUrb = :slUrb"),
    @NamedQuery(name = "FlowRegpLiquidacionDetalles.findByAvaluo", query = "SELECT f FROM FlowRegpLiquidacionDetalles f WHERE f.avaluo = :avaluo"),
    @NamedQuery(name = "FlowRegpLiquidacionDetalles.findByCuantia", query = "SELECT f FROM FlowRegpLiquidacionDetalles f WHERE f.cuantia = :cuantia"),
    @NamedQuery(name = "FlowRegpLiquidacionDetalles.findByCantidad", query = "SELECT f FROM FlowRegpLiquidacionDetalles f WHERE f.cantidad = :cantidad"),
    @NamedQuery(name = "FlowRegpLiquidacionDetalles.findByValorUnitario", query = "SELECT f FROM FlowRegpLiquidacionDetalles f WHERE f.valorUnitario = :valorUnitario"),
    @NamedQuery(name = "FlowRegpLiquidacionDetalles.findByDescuento", query = "SELECT f FROM FlowRegpLiquidacionDetalles f WHERE f.descuento = :descuento"),
    @NamedQuery(name = "FlowRegpLiquidacionDetalles.findByValorTotal", query = "SELECT f FROM FlowRegpLiquidacionDetalles f WHERE f.valorTotal = :valorTotal"),
    @NamedQuery(name = "FlowRegpLiquidacionDetalles.findByObservacion", query = "SELECT f FROM FlowRegpLiquidacionDetalles f WHERE f.observacion = :observacion"),
    @NamedQuery(name = "FlowRegpLiquidacionDetalles.findByInscripcion", query = "SELECT f FROM FlowRegpLiquidacionDetalles f WHERE f.inscripcion = :inscripcion"),
    @NamedQuery(name = "FlowRegpLiquidacionDetalles.findByFechaIngreso", query = "SELECT f FROM FlowRegpLiquidacionDetalles f WHERE f.fechaIngreso = :fechaIngreso"),
    @NamedQuery(name = "FlowRegpLiquidacionDetalles.findByDiferenciaPago", query = "SELECT f FROM FlowRegpLiquidacionDetalles f WHERE f.diferenciaPago = :diferenciaPago"),
    @NamedQuery(name = "FlowRegpLiquidacionDetalles.findByAplicaDescuento", query = "SELECT f FROM FlowRegpLiquidacionDetalles f WHERE f.aplicaDescuento = :aplicaDescuento"),
    @NamedQuery(name = "FlowRegpLiquidacionDetalles.findByReingreso", query = "SELECT f FROM FlowRegpLiquidacionDetalles f WHERE f.reingreso = :reingreso"),
    @NamedQuery(name = "FlowRegpLiquidacionDetalles.findByDescuentoPromedio", query = "SELECT f FROM FlowRegpLiquidacionDetalles f WHERE f.descuentoPromedio = :descuentoPromedio"),
    @NamedQuery(name = "FlowRegpLiquidacionDetalles.findByCantidadIntervinientes", query = "SELECT f FROM FlowRegpLiquidacionDetalles f WHERE f.cantidadIntervinientes = :cantidadIntervinientes"),
    @NamedQuery(name = "FlowRegpLiquidacionDetalles.findByAnioUltimaTrasnferencia", query = "SELECT f FROM FlowRegpLiquidacionDetalles f WHERE f.anioUltimaTrasnferencia = :anioUltimaTrasnferencia"),
    @NamedQuery(name = "FlowRegpLiquidacionDetalles.findByAnioAntecedenteSolicitado", query = "SELECT f FROM FlowRegpLiquidacionDetalles f WHERE f.anioAntecedenteSolicitado = :anioAntecedenteSolicitado"),
    @NamedQuery(name = "FlowRegpLiquidacionDetalles.findByRecargo", query = "SELECT f FROM FlowRegpLiquidacionDetalles f WHERE f.recargo = :recargo"),
    @NamedQuery(name = "FlowRegpLiquidacionDetalles.findByBaseImponible", query = "SELECT f FROM FlowRegpLiquidacionDetalles f WHERE f.baseImponible = :baseImponible")})
public class FlowRegpLiquidacionDetalles implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "acto")
    private long acto;
    @Column(name = "num_predio")
    private Integer numPredio;
    @Size(max = 2147483647)
    @Column(name = "nom_urb")
    private String nomUrb;
    @Size(max = 2147483647)
    @Column(name = "mz_urb")
    private String mzUrb;
    @Size(max = 2147483647)
    @Column(name = "sl_urb")
    private String slUrb;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "avaluo")
    private BigDecimal avaluo;
    @Column(name = "cuantia")
    private BigDecimal cuantia;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cantidad")
    private int cantidad;
    @Basic(optional = false)
    @NotNull
    @Column(name = "valor_unitario")
    private BigDecimal valorUnitario;
    @Basic(optional = false)
    @NotNull
    @Column(name = "descuento")
    private BigDecimal descuento;
    @Basic(optional = false)
    @NotNull
    @Column(name = "valor_total")
    private BigDecimal valorTotal;
    @Size(max = 2147483647)
    @Column(name = "observacion")
    private String observacion;
    @Column(name = "inscripcion")
    private Integer inscripcion;
    @Column(name = "fecha_ingreso")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaIngreso;
    @Column(name = "diferencia_pago")
    private BigDecimal diferenciaPago;
    @Column(name = "aplica_descuento")
    private Boolean aplicaDescuento;
    @Column(name = "reingreso")
    private Boolean reingreso;
    @Column(name = "descuento_promedio")
    private BigDecimal descuentoPromedio;
    @Column(name = "cantidad_intervinientes")
    private Integer cantidadIntervinientes;
    @Column(name = "anio_ultima_trasnferencia")
    private Integer anioUltimaTrasnferencia;
    @Column(name = "anio_antecedente_solicitado")
    private Integer anioAntecedenteSolicitado;
    @Column(name = "recargo")
    private BigDecimal recargo;
    @Column(name = "base_imponible")
    private BigDecimal baseImponible;
    @OneToMany(mappedBy = "detalle", fetch = FetchType.LAZY)
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<FlowRegpTareasTramite> flowRegpTareasTramiteList;
    @JoinColumn(name = "liquidacion", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private FlowRegpLiquidacion liquidacion;

    public FlowRegpLiquidacionDetalles() {
    }

    public FlowRegpLiquidacionDetalles(Long id) {
        this.id = id;
    }

    public FlowRegpLiquidacionDetalles(Long id, long acto, int cantidad, BigDecimal valorUnitario, BigDecimal descuento, BigDecimal valorTotal) {
        this.id = id;
        this.acto = acto;
        this.cantidad = cantidad;
        this.valorUnitario = valorUnitario;
        this.descuento = descuento;
        this.valorTotal = valorTotal;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getActo() {
        return acto;
    }

    public void setActo(long acto) {
        this.acto = acto;
    }

    public Integer getNumPredio() {
        return numPredio;
    }

    public void setNumPredio(Integer numPredio) {
        this.numPredio = numPredio;
    }

    public String getNomUrb() {
        return nomUrb;
    }

    public void setNomUrb(String nomUrb) {
        this.nomUrb = nomUrb;
    }

    public String getMzUrb() {
        return mzUrb;
    }

    public void setMzUrb(String mzUrb) {
        this.mzUrb = mzUrb;
    }

    public String getSlUrb() {
        return slUrb;
    }

    public void setSlUrb(String slUrb) {
        this.slUrb = slUrb;
    }

    public BigDecimal getAvaluo() {
        return avaluo;
    }

    public void setAvaluo(BigDecimal avaluo) {
        this.avaluo = avaluo;
    }

    public BigDecimal getCuantia() {
        return cuantia;
    }

    public void setCuantia(BigDecimal cuantia) {
        this.cuantia = cuantia;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getValorUnitario() {
        return valorUnitario;
    }

    public void setValorUnitario(BigDecimal valorUnitario) {
        this.valorUnitario = valorUnitario;
    }

    public BigDecimal getDescuento() {
        return descuento;
    }

    public void setDescuento(BigDecimal descuento) {
        this.descuento = descuento;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Integer getInscripcion() {
        return inscripcion;
    }

    public void setInscripcion(Integer inscripcion) {
        this.inscripcion = inscripcion;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public BigDecimal getDiferenciaPago() {
        return diferenciaPago;
    }

    public void setDiferenciaPago(BigDecimal diferenciaPago) {
        this.diferenciaPago = diferenciaPago;
    }

    public Boolean getAplicaDescuento() {
        return aplicaDescuento;
    }

    public void setAplicaDescuento(Boolean aplicaDescuento) {
        this.aplicaDescuento = aplicaDescuento;
    }

    public Boolean getReingreso() {
        return reingreso;
    }

    public void setReingreso(Boolean reingreso) {
        this.reingreso = reingreso;
    }

    public BigDecimal getDescuentoPromedio() {
        return descuentoPromedio;
    }

    public void setDescuentoPromedio(BigDecimal descuentoPromedio) {
        this.descuentoPromedio = descuentoPromedio;
    }

    public Integer getCantidadIntervinientes() {
        return cantidadIntervinientes;
    }

    public void setCantidadIntervinientes(Integer cantidadIntervinientes) {
        this.cantidadIntervinientes = cantidadIntervinientes;
    }

    public Integer getAnioUltimaTrasnferencia() {
        return anioUltimaTrasnferencia;
    }

    public void setAnioUltimaTrasnferencia(Integer anioUltimaTrasnferencia) {
        this.anioUltimaTrasnferencia = anioUltimaTrasnferencia;
    }

    public Integer getAnioAntecedenteSolicitado() {
        return anioAntecedenteSolicitado;
    }

    public void setAnioAntecedenteSolicitado(Integer anioAntecedenteSolicitado) {
        this.anioAntecedenteSolicitado = anioAntecedenteSolicitado;
    }

    public BigDecimal getRecargo() {
        return recargo;
    }

    public void setRecargo(BigDecimal recargo) {
        this.recargo = recargo;
    }

    public BigDecimal getBaseImponible() {
        return baseImponible;
    }

    public void setBaseImponible(BigDecimal baseImponible) {
        this.baseImponible = baseImponible;
    }

    
    public List<FlowRegpTareasTramite> getFlowRegpTareasTramiteList() {
        return flowRegpTareasTramiteList;
    }

    public void setFlowRegpTareasTramiteList(List<FlowRegpTareasTramite> flowRegpTareasTramiteList) {
        this.flowRegpTareasTramiteList = flowRegpTareasTramiteList;
    }

    public FlowRegpLiquidacion getLiquidacion() {
        return liquidacion;
    }

    public void setLiquidacion(FlowRegpLiquidacion liquidacion) {
        this.liquidacion = liquidacion;
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
        if (!(object instanceof FlowRegpLiquidacionDetalles)) {
            return false;
        }
        FlowRegpLiquidacionDetalles other = (FlowRegpLiquidacionDetalles) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.educacion.Models.FlowRegpLiquidacionDetalles[ id=" + id + " ]";
    }
    
}
