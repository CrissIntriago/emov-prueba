/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.tesoreria.entities;

import com.origami.sigef.common.entities.CuentaContableRetencion;
import com.origami.sigef.common.entities.DiarioGeneral;
import com.origami.sigef.common.entities.Factura;
import com.origami.sigef.common.entities.ItemTarifario;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 * @author gutya
 */
@Entity
@Table(schema = "tesoreria", name = "liquidacion_detalle")
public class LiquidacionDetalle implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @JoinColumn(name = "liquidacion", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Liquidacion liquidacion;
    @JoinColumn(name = "exoneracion", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Exoneracion exoneracion;
    @JoinColumn(name = "rubro", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Rubro rubro;
    @JoinColumn(name = "rubro_ice", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Rubro rubroIce;
    private BigDecimal valor;
    private Integer cantidad;
    @Column(name = "valor_recaudado")
    private BigDecimal valorRecaudado = BigDecimal.ZERO;
    @Column(name = "base_imponible")
    private BigDecimal baseImponible; //PARA LAS notas de debito ES EL VALOR DE LA RAZON //TAMBIEN ES CONSIDERADO EL VALOR UNITARIO
    @Column(name = "valor_descuento")
    private BigDecimal valorDescuento;
    @Column(name = "impuesto")
    private BigDecimal impuesto = BigDecimal.ZERO; // PARA LAS notas de debito ES EL VALOR DEL IVA
    private BigDecimal ice = BigDecimal.ZERO; // PARA LAS notas de debito ES EL VALOR DEL ICE
    private Boolean estado;
    @JoinColumn(name = "factura", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Factura factura;
    @JoinColumn(name = "cuenta_contable_retencion", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CuentaContableRetencion cuentaContableRetencion;
    @Column(name = "contabilizado")
    private Boolean contabilizado;
    @Column(name = "fecha_contabilizado")
    @Temporal(TemporalType.DATE)
    private Date fechaContabilizado;
    @JoinColumn(name = "diario_general", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private DiarioGeneral diarioGeneral;
    @JoinColumn(name = "tarifario", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ItemTarifario itemTarifario;

    @Transient
    private String numFactura;

    public LiquidacionDetalle() {
    }

    public LiquidacionDetalle(Long id, Liquidacion liquidacion, Rubro rubro, Integer cantidad, BigDecimal baseImponible, BigDecimal impuesto,
            BigDecimal ice, BigDecimal valor, BigDecimal valorRecaudado, Factura factura,
            CuentaContableRetencion cuentaContableRetencion) {
        this.id = id;
        this.liquidacion = liquidacion;
        this.rubro = rubro;
        this.cantidad = cantidad;
        this.baseImponible = baseImponible;
        this.factura = factura;
        this.impuesto = impuesto;
        this.ice = ice;
        this.valor = valor;
        this.valorRecaudado = valorRecaudado;
        this.cuentaContableRetencion = cuentaContableRetencion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumFactura() {
        if (this.factura != null) {
            return numFactura = this.factura.getNumFactura();
        }
        return numFactura;
    }

    public void setNumFactura(String numFactura) {
        this.numFactura = numFactura;
    }

    public CuentaContableRetencion getCuentaContableRetencion() {
        return cuentaContableRetencion;
    }

    public void setCuentaContableRetencion(CuentaContableRetencion cuentaContableRetencion) {
        this.cuentaContableRetencion = cuentaContableRetencion;
    }

    public Liquidacion getLiquidacion() {
        return liquidacion;
    }

    public void setLiquidacion(Liquidacion liquidacion) {
        this.liquidacion = liquidacion;
    }

    public Exoneracion getExoneracion() {
//        if (exoneracion == null) {
//            exoneracion = new Exoneracion();
//        }
        return exoneracion;
    }

    public void setExoneracion(Exoneracion exoneracion) {
        this.exoneracion = exoneracion;
    }

    public Rubro getRubro() {
//        if (rubro == null) {
//            rubro = new Rubro();
//        }
        return rubro;
    }

    public void setRubro(Rubro rubro) {
        this.rubro = rubro;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public BigDecimal getBaseImponible() {
        return baseImponible;
    }

    public void setBaseImponible(BigDecimal baseImponible) {
        this.baseImponible = baseImponible;
    }

    public BigDecimal getImpuesto() {
        return impuesto;
    }

    public void setImpuesto(BigDecimal impuesto) {
        this.impuesto = impuesto;
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

    public BigDecimal getIce() {
        return ice;
    }

    public void setIce(BigDecimal ice) {
        this.ice = ice;
    }

    public Rubro getRubroIce() {
        return rubroIce;
    }

    public void setRubroIce(Rubro rubroIce) {
        this.rubroIce = rubroIce;
    }

    public BigDecimal getValorDescuento() {
        return valorDescuento;
    }

    public void setValorDescuento(BigDecimal valorDescuento) {
        this.valorDescuento = valorDescuento;
    }

    public Factura getFactura() {
        return factura;
    }

    public void setFactura(Factura factura) {
        this.factura = factura;
    }

    public Boolean getContabilizado() {
        return contabilizado;
    }

    public void setContabilizado(Boolean contabilizado) {
        this.contabilizado = contabilizado;
    }

    public Date getFechaContabilizado() {
        return fechaContabilizado;
    }

    public void setFechaContabilizado(Date fechaContabilizado) {
        this.fechaContabilizado = fechaContabilizado;
    }

    public DiarioGeneral getDiarioGeneral() {
        return diarioGeneral;
    }

    public void setDiarioGeneral(DiarioGeneral diarioGeneral) {
        this.diarioGeneral = diarioGeneral;
    }

    public ItemTarifario getItemTarifario() {
        return itemTarifario;
    }

    public void setItemTarifario(ItemTarifario itemTarifario) {
        this.itemTarifario = itemTarifario;
    }

}
