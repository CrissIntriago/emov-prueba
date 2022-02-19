/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.asgard.Entity;

import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.entities.RenFactura;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.tesoreria.comprobantelectronico.entities.Cajero;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
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
@Table(name = "fina_ren_pago", schema = Utils.SCHEMA_ASGARD)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FinaRenPago.findAll", query = "SELECT f FROM FinaRenPago f"),
    @NamedQuery(name = "FinaRenPago.findById", query = "SELECT f FROM FinaRenPago f WHERE f.id = :id")})
public class FinaRenPago implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_pago")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaPago;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor")
    private BigDecimal valor;
    @Basic(optional = false)
    @NotNull
    @Column(name = "estado")
    private boolean estado;
    @Column(name = "pago_indebido")
    private Boolean pagoIndebido = Boolean.FALSE;
    @JoinColumn(name = "cajero", referencedColumnName = "id")
    @ManyToOne
    private Cajero cajero;
    @Column(name = "num_comprobante")
    private BigInteger numComprobante;
    @Column(name = "descuento")
    private BigDecimal descuento;
    @Column(name = "recargo")
    private BigDecimal recargo;
    @Column(name = "interes")
    private BigDecimal interes;
    @Column(name = "valor_coactiva")
    private BigDecimal valorCoactiva;
    @Size(max = 2147483647)
    @Column(name = "observacion")
    private String observacion;
    @OneToMany(mappedBy = "pago", fetch = FetchType.LAZY)
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<FinaRenPagoRubro> finaRenPagoRubroList;
    @OneToMany(mappedBy = "pago", fetch = FetchType.LAZY)
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<FinaRenPagoDetalle> finaRenPagoDetalleList;
    @JoinColumn(name = "liquidacion", referencedColumnName = "id")
    @ManyToOne
    private FinaRenLiquidacion liquidacion;
    @JoinColumn(name = "contribuyente", referencedColumnName = "id")
    @ManyToOne
    private Cliente contribuyente;
    @OneToMany(mappedBy = "pago", fetch = FetchType.LAZY)
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<FinaRenPagoDetalle> renPagoDetalles;
    @Column(name = "nombre_contribuyente")
    private String nombreContribuyente;
    @Column(name = "mac_addres_usuario_ingreso")
    private String macAddresUsuarioIngreso;
    @Column(name = "ip_user_session")
    private String ipUserSession;
    @Column(name = "usuario_anular")
    private String usuarioAnular;
    @OneToMany(mappedBy = "pago", fetch = FetchType.LAZY)
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<FinaRenPagoRubro> renPagoRubros;
    @Column(name = "fecha_anulacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaAnulacion;
    @JoinColumn(name = "factura", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private RenFactura factura;

    public FinaRenPago() {
    }

    public FinaRenPago(Long id) {
        this.id = id;
    }

    public FinaRenPago(Long id, Date fechaPago, boolean estado) {
        this.id = id;
        this.fechaPago = fechaPago;
        this.estado = estado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(Date fechaPago) {
        this.fechaPago = fechaPago;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public boolean getEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public Cajero getCajero() {
        return cajero;
    }

    public void setCajero(Cajero cajero) {
        this.cajero = cajero;
    }

    public BigInteger getNumComprobante() {
        return numComprobante;
    }

    public void setNumComprobante(BigInteger numComprobante) {
        this.numComprobante = numComprobante;
    }

    public BigDecimal getDescuento() {
        return descuento;
    }

    public void setDescuento(BigDecimal descuento) {
        this.descuento = descuento;
    }

    public BigDecimal getRecargo() {
        return recargo;
    }

    public void setRecargo(BigDecimal recargo) {
        this.recargo = recargo;
    }

    public BigDecimal getInteres() {
        return interes;
    }

    public void setInteres(BigDecimal interes) {
        this.interes = interes;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getUsuarioAnular() {
        return usuarioAnular;
    }

    public void setUsuarioAnular(String usuarioAnular) {
        this.usuarioAnular = usuarioAnular;
    }

    @XmlTransient
    public List<FinaRenPagoRubro> getFinaRenPagoRubroList() {
        return finaRenPagoRubroList;
    }

    public void setFinaRenPagoRubroList(List<FinaRenPagoRubro> finaRenPagoRubroList) {
        this.finaRenPagoRubroList = finaRenPagoRubroList;
    }

    public Cliente getContribuyente() {
        return contribuyente;
    }

    public void setContribuyente(Cliente contribuyente) {
        this.contribuyente = contribuyente;
    }

    @XmlTransient
    public List<FinaRenPagoDetalle> getFinaRenPagoDetalleList() {
        return finaRenPagoDetalleList;
    }

    public void setFinaRenPagoDetalleList(List<FinaRenPagoDetalle> finaRenPagoDetalleList) {
        this.finaRenPagoDetalleList = finaRenPagoDetalleList;
    }

    public FinaRenLiquidacion getLiquidacion() {
        return liquidacion;
    }

    public void setLiquidacion(FinaRenLiquidacion liquidacion) {
        this.liquidacion = liquidacion;
    }

    public Boolean isPagoIndebido() {
        return pagoIndebido;
    }

    public void setPagoIndebido(Boolean pagoIndebido) {
        this.pagoIndebido = pagoIndebido;
    }

    public List<FinaRenPagoDetalle> getRenPagoDetalles() {
        return renPagoDetalles;
    }

    public void setRenPagoDetalles(List<FinaRenPagoDetalle> renPagoDetalles) {
        this.renPagoDetalles = renPagoDetalles;
    }

    public String getNombreContribuyente() {
        return nombreContribuyente;
    }

    public void setNombreContribuyente(String nombreContribuyente) {
        this.nombreContribuyente = nombreContribuyente;
    }

    public String getMacAddresUsuarioIngreso() {
        return macAddresUsuarioIngreso;
    }

    public void setMacAddresUsuarioIngreso(String macAddresUsuarioIngreso) {
        this.macAddresUsuarioIngreso = macAddresUsuarioIngreso;
    }

    public String getIpUserSession() {
        return ipUserSession;
    }

    public void setIpUserSession(String ipUserSession) {
        this.ipUserSession = ipUserSession;
    }

    public List<FinaRenPagoRubro> getRenPagoRubros() {
        return renPagoRubros;
    }

    public void setRenPagoRubros(List<FinaRenPagoRubro> renPagoRubros) {
        this.renPagoRubros = renPagoRubros;
    }

    public boolean isEstado() {
        return estado;
    }

    public Boolean getPagoIndebido() {
        return pagoIndebido;
    }

    public Date getFechaAnulacion() {
        return fechaAnulacion;
    }

    public void setFechaAnulacion(Date fechaAnulacion) {
        this.fechaAnulacion = fechaAnulacion;
    }

    public BigDecimal getValorCoactiva() {
        return valorCoactiva;
    }

    public void setValorCoactiva(BigDecimal valorCoactiva) {
        this.valorCoactiva = valorCoactiva;
    }

    public RenFactura getFactura() {
        return factura;
    }

    public void setFactura(RenFactura factura) {
        this.factura = factura;
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
        if (!(object instanceof FinaRenPago)) {
            return false;
        }
        FinaRenPago other = (FinaRenPago) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "FinaRenPago{" + "id=" + id + ", liquidacion=" + liquidacion + '}';
    }

}
