/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.tesoreria.entities;

import com.origami.sigef.common.entities.CuentaContable;
import com.origami.sigef.common.entities.ItemTarifario;
import com.origami.sigef.common.entities.Recaudacion;
import java.io.Serializable;
import java.math.BigDecimal;
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

/**
 *
 * @author ORIGAMI2
 */
@Entity
@Table(name = "detalle_corte_orden_cobro", schema = "tesoreria")
@NamedQueries({
    @NamedQuery(name = "DetalleCorteOrdenCobro.findAll", query = "SELECT d FROM DetalleCorteOrdenCobro d"),
    @NamedQuery(name = "DetalleCorteOrdenCobro.findById", query = "SELECT d FROM DetalleCorteOrdenCobro d WHERE d.id = :id"),
    @NamedQuery(name = "DetalleCorteOrdenCobro.findByOrdenCobro", query = "SELECT d FROM DetalleCorteOrdenCobro d WHERE d.ordenCobro = :ordenCobro"),
    @NamedQuery(name = "DetalleCorteOrdenCobro.findByIdordenCobro", query = "SELECT d FROM DetalleCorteOrdenCobro d WHERE d.idordenCobro = :idordenCobro"),
    @NamedQuery(name = "DetalleCorteOrdenCobro.findByCodigoTarifa", query = "SELECT d FROM DetalleCorteOrdenCobro d WHERE d.codigoTarifa = :codigoTarifa"),
    @NamedQuery(name = "DetalleCorteOrdenCobro.findByConceptoTarifario", query = "SELECT d FROM DetalleCorteOrdenCobro d WHERE d.conceptoTarifario = :conceptoTarifario"),
    @NamedQuery(name = "DetalleCorteOrdenCobro.findByIdentificacion", query = "SELECT d FROM DetalleCorteOrdenCobro d WHERE d.identificacion = :identificacion"),
    @NamedQuery(name = "DetalleCorteOrdenCobro.findByNombre", query = "SELECT d FROM DetalleCorteOrdenCobro d WHERE d.nombre = :nombre"),
    @NamedQuery(name = "DetalleCorteOrdenCobro.findBySolicitante", query = "SELECT d FROM DetalleCorteOrdenCobro d WHERE d.solicitante = :solicitante"),
    @NamedQuery(name = "DetalleCorteOrdenCobro.findBySubtotal", query = "SELECT d FROM DetalleCorteOrdenCobro d WHERE d.subtotal = :subtotal"),
    @NamedQuery(name = "DetalleCorteOrdenCobro.findByTotal", query = "SELECT d FROM DetalleCorteOrdenCobro d WHERE d.total = :total"),
    @NamedQuery(name = "DetalleCorteOrdenCobro.findByNumeroPapeleta", query = "SELECT d FROM DetalleCorteOrdenCobro d WHERE d.numeroPapeleta = :numeroPapeleta"),
    @NamedQuery(name = "DetalleCorteOrdenCobro.findByFechaEmision", query = "SELECT d FROM DetalleCorteOrdenCobro d WHERE d.fechaEmision = :fechaEmision"),
    @NamedQuery(name = "DetalleCorteOrdenCobro.findByEstadoOrden", query = "SELECT d FROM DetalleCorteOrdenCobro d WHERE d.estadoOrden = :estadoOrden"),
    @NamedQuery(name = "DetalleCorteOrdenCobro.findByComprobanteInterno", query = "SELECT d FROM DetalleCorteOrdenCobro d WHERE d.comprobanteInterno = :comprobanteInterno"),
    @NamedQuery(name = "DetalleCorteOrdenCobro.findByBanco", query = "SELECT d FROM DetalleCorteOrdenCobro d WHERE d.banco = :banco")})
public class DetalleCorteOrdenCobro implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 2147483647)
    @Column(name = "orden_cobro")
    private String ordenCobro;
    @Column(name = "idorden_cobro")
    private Long idordenCobro;
    @Size(max = 2147483647)
    @Column(name = "codigo_tarifa")
    private String codigoTarifa;
    @Size(max = 2147483647)
    @Column(name = "concepto_tarifario")
    private String conceptoTarifario;
    @Size(max = 2147483647)
    @Column(name = "identificacion")
    private String identificacion;
    @Size(max = 2147483647)
    @Column(name = "nombre")
    private String nombre;
    @Size(max = 2147483647)
    @Column(name = "solicitante")
    private String solicitante;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "subtotal")
    private BigDecimal subtotal;
    @Column(name = "total")
    private BigDecimal total;
    @Size(max = 2147483647)
    @Column(name = "numero_papeleta")
    private String numeroPapeleta;
    @Size(max = 2147483647)
    @Column(name = "fecha_emision")
    private String fechaEmision;
    @Size(max = 2147483647)
    @Column(name = "estado_orden")
    private String estadoOrden;
    @Size(max = 2147483647)
    @Column(name = "comprobante_interno")
    private String comprobanteInterno;
    @Size(max = 2147483647)
    @Column(name = "banco")
    private String banco;
    @Size(max = 2147483647)
    @Column(name = "placa")
    private String placa;
    @Column(name = "id_banco")
    private Long id_banco;
    @Column(name = "verificado")
    private Boolean verificado;
    @Transient
    private Boolean auxVerificar;
    
    @JoinColumn(name = "corte_orden_cobro", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CorteOrdenCobro corteOrdenCobro;
    @JoinColumn(name = "item_tarifa", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ItemTarifario itemTarifa;
    @JoinColumn(name = "cuenta_caja", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CuentaContable cuentaCaja;
    @JoinColumn(name = "cobro_ajuste", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Recaudacion cobroAjuste;

    @Column(name = "valor_ajuste")
    private Boolean valorAjuste;
    
    public DetalleCorteOrdenCobro() {
        this.verificado = Boolean.FALSE;
        this.auxVerificar = Boolean.FALSE;
        this.valorAjuste=Boolean.FALSE;
    }

    public DetalleCorteOrdenCobro(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrdenCobro() {
        return ordenCobro;
    }

    public void setOrdenCobro(String ordenCobro) {
        this.ordenCobro = ordenCobro;
    }

    public Long getIdordenCobro() {
        return idordenCobro;
    }

    public void setIdordenCobro(Long idordenCobro) {
        this.idordenCobro = idordenCobro;
    }

    public String getCodigoTarifa() {
        return codigoTarifa;
    }

    public void setCodigoTarifa(String codigoTarifa) {
        this.codigoTarifa = codigoTarifa;
    }

    public String getConceptoTarifario() {
        return conceptoTarifario;
    }

    public void setConceptoTarifario(String conceptoTarifario) {
        this.conceptoTarifario = conceptoTarifario;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getSolicitante() {
        return solicitante;
    }

    public void setSolicitante(String solicitante) {
        this.solicitante = solicitante;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getNumeroPapeleta() {
        return numeroPapeleta;
    }

    public void setNumeroPapeleta(String numeroPapeleta) {
        this.numeroPapeleta = numeroPapeleta;
    }

    public String getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(String fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public String getEstadoOrden() {
        return estadoOrden;
    }

    public void setEstadoOrden(String estadoOrden) {
        this.estadoOrden = estadoOrden;
    }

    public String getComprobanteInterno() {
        return comprobanteInterno;
    }

    public void setComprobanteInterno(String comprobanteInterno) {
        this.comprobanteInterno = comprobanteInterno;
    }

    public String getBanco() {
        return banco;
    }

    public void setBanco(String banco) {
        this.banco = banco;
    }

    public CorteOrdenCobro getCorteOrdenCobro() {
        return corteOrdenCobro;
    }

    public void setCorteOrdenCobro(CorteOrdenCobro corteOrdenCobro) {
        this.corteOrdenCobro = corteOrdenCobro;
    }

    public ItemTarifario getItemTarifa() {
        return itemTarifa;
    }

    public void setItemTarifa(ItemTarifario itemTarifa) {
        this.itemTarifa = itemTarifa;
    }

    public CuentaContable getCuentaCaja() {
        return cuentaCaja;
    }

    public void setCuentaCaja(CuentaContable cuentaCaja) {
        this.cuentaCaja = cuentaCaja;
    }

    public Long getId_banco() {
        return id_banco;
    }

    public void setId_banco(Long id_banco) {
        this.id_banco = id_banco;
    }

    public Boolean getVerificado() {
        return verificado;
    }

    public void setVerificado(Boolean verificado) {
        this.verificado = verificado;
    }

    public Boolean getAuxVerificar() {
        return auxVerificar;
    }

    public void setAuxVerificar(Boolean auxVerificar) {
        this.auxVerificar = auxVerificar;
    }

    public Recaudacion getCobroAjuste() {
        return cobroAjuste;
    }

    public void setCobroAjuste(Recaudacion cobroAjuste) {
        this.cobroAjuste = cobroAjuste;
    }

    public Boolean getValorAjuste() {
        return valorAjuste;
    }

    public void setValorAjuste(Boolean valorAjuste) {
        this.valorAjuste = valorAjuste;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DetalleCorteOrdenCobro)) {
            return false;
        }
        DetalleCorteOrdenCobro other = (DetalleCorteOrdenCobro) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.tesoreria.entities.DetalleCorteOrdenCobro[ id=" + id + " ]";
    }
    
}
