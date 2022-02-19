package com.origami.sigef.common.entities;

import com.origami.sigef.resource.contabilidad.entities.ContDiarioGeneral;
import com.origami.sigef.resource.contabilidad.entities.ContDiarioGeneralDetalle;
import com.origami.sigef.tesoreria.entities.Rubro;
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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "ren_factura_detalle", schema = "tesoreria")
@NamedQueries({
    @NamedQuery(name = "RenFacturaDetalle.findAll", query = "SELECT r FROM RenFacturaDetalle r"),
    @NamedQuery(name = "RenFacturaDetalle.findById", query = "SELECT r FROM RenFacturaDetalle r WHERE r.id = :id"),
    @NamedQuery(name = "RenFacturaDetalle.findByCantidad", query = "SELECT r FROM RenFacturaDetalle r WHERE r.cantidad = :cantidad"),
    @NamedQuery(name = "RenFacturaDetalle.findByBaseImponible", query = "SELECT r FROM RenFacturaDetalle r WHERE r.baseImponible = :baseImponible"),
    @NamedQuery(name = "RenFacturaDetalle.findByImpuesto", query = "SELECT r FROM RenFacturaDetalle r WHERE r.impuesto = :impuesto"),
    @NamedQuery(name = "RenFacturaDetalle.findByIce", query = "SELECT r FROM RenFacturaDetalle r WHERE r.ice = :ice"),
    @NamedQuery(name = "RenFacturaDetalle.findByValor", query = "SELECT r FROM RenFacturaDetalle r WHERE r.valor = :valor"),
    @NamedQuery(name = "RenFacturaDetalle.findByValorRecaudado", query = "SELECT r FROM RenFacturaDetalle r WHERE r.valorRecaudado = :valorRecaudado"),
    @NamedQuery(name = "RenFacturaDetalle.findByValorDescuento", query = "SELECT r FROM RenFacturaDetalle r WHERE r.valorDescuento = :valorDescuento"),
    @NamedQuery(name = "RenFacturaDetalle.findByEstado", query = "SELECT r FROM RenFacturaDetalle r WHERE r.estado = :estado"),
    @NamedQuery(name = "RenFacturaDetalle.findByFactura", query = "SELECT r FROM RenFacturaDetalle r WHERE r.factura = :factura"),
    @NamedQuery(name = "RenFacturaDetalle.findByContabilizado", query = "SELECT r FROM RenFacturaDetalle r WHERE r.contabilizado = :contabilizado"),
    @NamedQuery(name = "RenFacturaDetalle.findByFechaContabilizado", query = "SELECT r FROM RenFacturaDetalle r WHERE r.fechaContabilizado = :fechaContabilizado")})
public class RenFacturaDetalle implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    private Integer cantidad;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "base_imponible", precision = 19, scale = 2)
    private BigDecimal baseImponible;
    @Column(precision = 19, scale = 2)
    private BigDecimal impuesto;
    @Column(precision = 19, scale = 2)
    private BigDecimal ice;
    @Column(precision = 19, scale = 2)
    private BigDecimal valor;
    @Column(name = "valor_recaudado", precision = 19, scale = 2)
    private BigDecimal valorRecaudado;
    @Column(name = "valor_descuento", precision = 19, scale = 2)
    private BigDecimal valorDescuento;
    @Column(name = "estado")
    private Boolean estado;
    @JoinColumn(name = "factura", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Factura factura;
    @Column(name = "contabilizado")
    private Boolean contabilizado;
    @Column(name = "fecha_contabilizado")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaContabilizado;
    @JoinColumn(name = "diario_general", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ContDiarioGeneral diarioGeneral;
    @JoinColumn(name = "cuenta_contable_retencion", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CuentaContableRetencion cuentaContableRetencion;
    @JoinColumn(name = "ren_factura", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private RenFactura renFactura;
    @JoinColumn(name = "rubro_ice", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Rubro rubroIce;
    @JoinColumn(name = "rubro", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Rubro rubro;
    @JoinColumn(name = "diario_general_detalle", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ContDiarioGeneralDetalle diarioGeneralDetalle;

    public RenFacturaDetalle() {
    }

    public RenFacturaDetalle(Long id, RenFactura renFactura, Rubro rubro, Integer cantidad, BigDecimal baseImponible,
            Factura factura, BigDecimal impuesto,
            BigDecimal ice, BigDecimal valor, BigDecimal valorRecaudado,
            CuentaContableRetencion cuentaContableRetencion) {
        this.id = id;
        this.renFactura = renFactura;
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

    public RenFacturaDetalle(Long id) {
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

    public BigDecimal getIce() {
        return ice;
    }

    public void setIce(BigDecimal ice) {
        this.ice = ice;
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

    public BigDecimal getValorDescuento() {
        return valorDescuento;
    }

    public void setValorDescuento(BigDecimal valorDescuento) {
        this.valorDescuento = valorDescuento;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
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

    public ContDiarioGeneral getDiarioGeneral() {
        return diarioGeneral;
    }

    public void setDiarioGeneral(ContDiarioGeneral diarioGeneral) {
        this.diarioGeneral = diarioGeneral;
    }

    public CuentaContableRetencion getCuentaContableRetencion() {
        return cuentaContableRetencion;
    }

    public void setCuentaContableRetencion(CuentaContableRetencion cuentaContableRetencion) {
        this.cuentaContableRetencion = cuentaContableRetencion;
    }

    public RenFactura getRenFactura() {
        return renFactura;
    }

    public void setRenFactura(RenFactura renFactura) {
        this.renFactura = renFactura;
    }

    public Rubro getRubroIce() {
        return rubroIce;
    }

    public void setRubroIce(Rubro rubroIce) {
        this.rubroIce = rubroIce;
    }

    public Rubro getRubro() {
        return rubro;
    }

    public void setRubro(Rubro rubro) {
        this.rubro = rubro;
    }

    public ContDiarioGeneralDetalle getDiarioGeneralDetalle() {
        return diarioGeneralDetalle;
    }

    public void setDiarioGeneralDetalle(ContDiarioGeneralDetalle diarioGeneralDetalle) {
        this.diarioGeneralDetalle = diarioGeneralDetalle;
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
        if (!(object instanceof RenFacturaDetalle)) {
            return false;
        }
        RenFacturaDetalle other = (RenFacturaDetalle) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.common.entities.RenFacturaDetalle[ id=" + id + " ]";
    }

}
