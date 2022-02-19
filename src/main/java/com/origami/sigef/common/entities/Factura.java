/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

import com.origami.sigef.ats.entities.FacturaFormasPago;
import com.origami.sigef.ats.entities.PagoExterior;
import com.origami.sigef.resource.contabilidad.entities.ContDiarioGeneral;
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
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author OrigamiEc
 */
@Entity
@Table(name = "factura")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Factura.findAll", query = "SELECT f FROM Factura f"),
    @NamedQuery(name = "Factura.findById", query = "SELECT f FROM Factura f WHERE f.id = :id"),
    @NamedQuery(name = "Factura.findByIdDiario", query = "SELECT f FROM Factura f WHERE f.idConDiario = ?1 AND f.estado = true AND f.retenida = false ORDER BY f.numFactura ASC"),
    @NamedQuery(name = "Factura.findByNumFactura", query = "SELECT f FROM Factura f WHERE f.numFactura = ?1 AND f.estado = TRUE"),
    @NamedQuery(name = "Factura.findFacturasByRegistro", query = "SELECT f FROM Factura f WHERE f.inventarioRegistro = ?1"),
    @NamedQuery(name = "Factura.findByFechaFactura", query = "SELECT f FROM Factura f WHERE f.fechaFactura = :fechaFactura"),
    @NamedQuery(name = "Factura.findByProveedor", query = "SELECT f FROM Factura f WHERE f.proveedor = :proveedor"),
    @NamedQuery(name = "Factura.findByArchivo", query = "SELECT f FROM Factura f WHERE f.archivo = :archivo"),
    @NamedQuery(name = "Factura.findByTipoArchivo", query = "SELECT f FROM Factura f WHERE f.tipoArchivo = :tipoArchivo"),
    @NamedQuery(name = "Factura.findByTamanioArchivo", query = "SELECT f FROM Factura f WHERE f.tamanioArchivo = :tamanioArchivo"),
    @NamedQuery(name = "Factura.findByDiarioGeneral", query = "SELECT f FROM Factura f WHERE f.idConDiario = ?1 AND f.estado = true ORDER BY f.numFactura ASC"),
    @NamedQuery(name = "Factura.findByEstado", query = "SELECT f FROM Factura f WHERE f.estado = :estado")})
public class Factura implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "num_factura")
    private String numFactura;
    @Column(name = "fecha_factura")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaFactura;
    @Column(name = "fecha_caducidad")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCaducidad;
    @Column(name = "num_autorizacion")
    private String numAutorizacion;
    @JoinColumn(name = "proveedor", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Proveedor proveedor;
    @JoinColumn(name = "inventario_registro", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private InventarioRegistro inventarioRegistro;
    @Column(name = "archivo")
    private String archivo;
    @Column(name = "tipo_archivo")
    private String tipoArchivo;
    @Column(name = "tamanio_archivo")
    private String tamanioArchivo;
    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "base_imponible_iva")
    private BigDecimal baseImponibleIva = BigDecimal.ZERO;
    @Column(name = "base_imponible_diferente")
    private BigDecimal baseImponibleDiferente = BigDecimal.ZERO;
    @Column(name = "retencion_iva10")
    private BigDecimal retencionIva10 = BigDecimal.ZERO;
    @Column(name = "retencion_iva20")
    private BigDecimal retencionIva20 = BigDecimal.ZERO;
    @Column(name = "retencion_iva30")
    private BigDecimal retencionIva30 = BigDecimal.ZERO;
    @Column(name = "retencion_iva50")
    private BigDecimal retencionIva50 = BigDecimal.ZERO;
    @Column(name = "retencion_iva70")
    private BigDecimal retencionIva70 = BigDecimal.ZERO;
    @Column(name = "retencion_iva100")
    private BigDecimal retencionIva100 = BigDecimal.ZERO;
    @Column(name = "monto_iva")
    private BigDecimal montoIva = BigDecimal.ZERO;
    @Column(name = "monto_ice")
    private BigDecimal montoIce = BigDecimal.ZERO;
    @OneToMany(mappedBy = "factura", fetch = FetchType.LAZY)
    private List<FacturaFormasPago> facturaFormasPago;
    @JoinColumn(name = "pago_exterior", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private PagoExterior pagoExterior;
    @Column(name = "retenida")
    private Boolean retenida = Boolean.FALSE;
    @Column(name = "factura_electronica")
    private Boolean facturaElectronica;
    @Column(name = "retencion_iva")
    private BigDecimal retencionIva;
    @Column(name = "retencion_renta")
    private BigDecimal retencionRenta;
    @JoinColumn(name = "id_cont_diario", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ContDiarioGeneral idConDiario;
    @Column(name = "cod_sustento")
    private String codSustento;
    @Column(name = "guia_remision")
    private Boolean guiaRemision;

    @Transient
    private Boolean isSeleccion;
    @Transient
    private String entidad;
    @Transient
    private String establecimiento;
    @Transient
    private String secuencia;
    @Transient
    private BigDecimal subtotalRenta = BigDecimal.ZERO;
    @Transient
    private List<RenFacturaDetalle> detalleRetencion;

    public Factura() {
        this.facturaElectronica = false;
        this.estado = true;
        this.montoIce = BigDecimal.ZERO;
        this.montoIva = BigDecimal.ZERO;
        this.retencionIva100 = BigDecimal.ZERO;
        this.retencionIva70 = BigDecimal.ZERO;
        this.retencionIva50 = BigDecimal.ZERO;
        this.retencionIva30 = BigDecimal.ZERO;
        this.retencionIva20 = BigDecimal.ZERO;
        this.retencionIva10 = BigDecimal.ZERO;
        this.baseImponibleIva = BigDecimal.ZERO;
        this.baseImponibleDiferente = BigDecimal.ZERO;
        this.retencionIva = BigDecimal.ZERO;
        this.retencionRenta = BigDecimal.ZERO;
    }

    public Factura(Long id) {
        this.id = id;
        this.facturaElectronica = false;
    }

    public Factura(Long id, String numFactura, Date fechaFactura) {
        this.id = id;
        this.numFactura = numFactura;
        this.fechaFactura = fechaFactura;
        this.facturaElectronica = false;
    }

    public Factura(String numFactura, String numAutorizacion, Date fechaFactura, Date fechaCaducidad) {
        this.numFactura = numFactura;
        this.fechaFactura = fechaFactura;
        this.numAutorizacion = numAutorizacion;
        this.fechaCaducidad = fechaCaducidad;
        this.facturaElectronica = false;
    }

    public Boolean getRetenida() {
        return retenida;
    }

    public void setRetenida(Boolean retenida) {
        this.retenida = retenida;
    }

    public BigDecimal getSubtotalRenta() {
        if (this.baseImponibleDiferente != null && this.baseImponibleIva != null) {
            return subtotalRenta = baseImponibleDiferente.add(baseImponibleIva);
        }
        return subtotalRenta;
    }

    public void setSubtotalRenta(BigDecimal subtotalRenta) {
        this.subtotalRenta = subtotalRenta;
    }

    public String getEntidad() {
        if (this.numFactura != null && !this.numFactura.isEmpty()) {
            String[] code = this.numFactura.split("-");
            return entidad = code[0];
        }
        return entidad;
    }

    public void setEntidad(String entidad) {
        this.entidad = entidad;
    }

    public String getEstablecimiento() {
        if (this.numFactura != null && !this.numFactura.isEmpty()) {
            String[] code = this.numFactura.split("-");
            return entidad = code[1];
        }
        return establecimiento;
    }

    public void setEstablecimiento(String establecimiento) {
        this.establecimiento = establecimiento;
    }

    public String getSecuencia() {
        if (this.numFactura != null && !this.numFactura.isEmpty()) {
            String[] code = this.numFactura.split("-");
            return entidad = code[2];
        }
        return secuencia;
    }

    public void setSecuencia(String secuencia) {
        this.secuencia = secuencia;
    }

    public PagoExterior getPagoExterior() {
        return pagoExterior;
    }

    public void setPagoExterior(PagoExterior pagoExterior) {
        this.pagoExterior = pagoExterior;
    }

    public List<FacturaFormasPago> getFacturaFormasPago() {
        return facturaFormasPago;
    }

    public void setFacturaFormasPago(List<FacturaFormasPago> facturaFormasPago) {
        this.facturaFormasPago = facturaFormasPago;
    }

    public BigDecimal getMontoIva() {
        return montoIva;
    }

    public void setMontoIva(BigDecimal montoIva) {
        this.montoIva = montoIva;
    }

    public BigDecimal getMontoIce() {
        return montoIce;
    }

    public void setMontoIce(BigDecimal montoIce) {
        this.montoIce = montoIce;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getBaseImponibleIva() {
        return baseImponibleIva;
    }

    public void setBaseImponibleIva(BigDecimal baseImponibleIva) {
        this.baseImponibleIva = baseImponibleIva;
    }

    public BigDecimal getBaseImponibleDiferente() {
        return baseImponibleDiferente;
    }

    public void setBaseImponibleDiferente(BigDecimal baseImponibleDiferente) {
        this.baseImponibleDiferente = baseImponibleDiferente;
    }

    public BigDecimal getRetencionIva10() {
        return retencionIva10;
    }

    public void setRetencionIva10(BigDecimal retencionIva10) {
        this.retencionIva10 = retencionIva10;
    }

    public BigDecimal getRetencionIva20() {
        return retencionIva20;
    }

    public void setRetencionIva20(BigDecimal retencionIva20) {
        this.retencionIva20 = retencionIva20;
    }

    public BigDecimal getRetencionIva30() {
        return retencionIva30;
    }

    public void setRetencionIva30(BigDecimal retencionIva30) {
        this.retencionIva30 = retencionIva30;
    }

    public BigDecimal getRetencionIva50() {
        return retencionIva50;
    }

    public void setRetencionIva50(BigDecimal retencionIva50) {
        this.retencionIva50 = retencionIva50;
    }

    public BigDecimal getRetencionIva70() {
        return retencionIva70;
    }

    public void setRetencionIva70(BigDecimal retencionIva70) {
        this.retencionIva70 = retencionIva70;
    }

    public BigDecimal getRetencionIva100() {
        return retencionIva100;
    }

    public void setRetencionIva100(BigDecimal retencionIva100) {
        this.retencionIva100 = retencionIva100;
    }

    public String getNumFactura() {
        return numFactura;
    }

    public void setNumFactura(String numFactura) {
        this.numFactura = numFactura;
    }

    public Date getFechaFactura() {
        return fechaFactura;
    }

    public void setFechaFactura(Date fechaFactura) {
        this.fechaFactura = fechaFactura;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    public String getArchivo() {
        return archivo;
    }

    public void setArchivo(String archivo) {
        this.archivo = archivo;
    }

    public String getTipoArchivo() {
        return tipoArchivo;
    }

    public void setTipoArchivo(String tipoArchivo) {
        this.tipoArchivo = tipoArchivo;
    }

    public String getTamanioArchivo() {
        return tamanioArchivo;
    }

    public void setTamanioArchivo(String tamanioArchivo) {
        this.tamanioArchivo = tamanioArchivo;
    }

    public Boolean getEstado() {
        return estado;
    }

    public InventarioRegistro getInventarioRegistro() {
        return inventarioRegistro;
    }

    public void setInventarioRegistro(InventarioRegistro inventarioRegistro) {
        this.inventarioRegistro = inventarioRegistro;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Date getFechaCaducidad() {
        return fechaCaducidad;
    }

    public void setFechaCaducidad(Date fechaCaducidad) {
        this.fechaCaducidad = fechaCaducidad;
    }

    public String getNumAutorizacion() {
        return numAutorizacion;
    }

    public void setNumAutorizacion(String numAutorizacion) {
        this.numAutorizacion = numAutorizacion;
    }

    public Boolean getIsSeleccion() {
        return isSeleccion;
    }

    public void setIsSeleccion(Boolean isSeleccion) {
        this.isSeleccion = isSeleccion;
    }

    public Boolean getFacturaElectronica() {
        return facturaElectronica;
    }

    public void setFacturaElectronica(Boolean facturaElectronica) {
        this.facturaElectronica = facturaElectronica;
    }

    public BigDecimal getRetencionIva() {
        return retencionIva;
    }

    public void setRetencionIva(BigDecimal retencionIva) {
        this.retencionIva = retencionIva;
    }

    public BigDecimal getRetencionRenta() {
        return retencionRenta;
    }

    public void setRetencionRenta(BigDecimal retencionRenta) {
        this.retencionRenta = retencionRenta;
    }

    public ContDiarioGeneral getIdConDiario() {
        return idConDiario;
    }

    public void setIdConDiario(ContDiarioGeneral idConDiario) {
        this.idConDiario = idConDiario;
    }

    public String getCodSustento() {
        return codSustento;
    }

    public void setCodSustento(String codSustento) {
        this.codSustento = codSustento;
    }

    public List<RenFacturaDetalle> getDetalleRetencion() {
        return detalleRetencion;
    }

    public void setDetalleRetencion(List<RenFacturaDetalle> detalleRetencion) {
        this.detalleRetencion = detalleRetencion;
    }

    public Boolean getGuiaRemision() {
        return guiaRemision;
    }

    public void setGuiaRemision(Boolean guiaRemision) {
        this.guiaRemision = guiaRemision;
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
        if (!(object instanceof Factura)) {
            return false;
        }
        Factura other = (Factura) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.common.entities.Factura[ id=" + id + " ]";
    }

}
