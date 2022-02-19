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
@Table(name = "ren_valor_inicial_liquidacion", schema = Utils.SCHEMA_SGM)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RenValorInicialLiquidacion.findAll", query = "SELECT r FROM RenValorInicialLiquidacion r")})
public class RenValorInicialLiquidacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 25)
    @Column(name = "id_liquidacion")
    private String idLiquidacion;
    @Column(name = "anio")
    private Integer anio;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "avaluo_construccion")
    private BigDecimal avaluoConstruccion;
    @Column(name = "avaluo_municipal")
    private BigDecimal avaluoMunicipal;
    @Column(name = "avaluo_solar")
    private BigDecimal avaluoSolar;
    @Column(name = "area_total")
    private BigDecimal areaTotal;
    @Column(name = "banda_impositiva")
    private BigDecimal bandaImpositiva;
    @Column(name = "base_imponible")
    private BigDecimal baseImponible;
    @Column(name = "bombero")
    private Boolean bombero;
    @Column(name = "coactiva")
    private Boolean coactiva;
    @Column(name = "comprador")
    private BigInteger comprador;
    @Column(name = "costo_adq")
    private BigDecimal costoAdq;
    @Column(name = "cuantia")
    private BigDecimal cuantia;
    @Column(name = "estado_coactiva")
    private Integer estadoCoactiva;
    @Column(name = "estado_liquidacion")
    private BigInteger estadoLiquidacion;
    @Size(max = 1)
    @Column(name = "estado_referencia")
    private String estadoReferencia;
    @Size(max = 2147483647)
    @Column(name = "exoneracion_descripcion")
    private String exoneracionDescripcion;
    @Column(name = "exonerado")
    private Boolean exonerado;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_ingreso")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaIngreso;
    @Column(name = "fecha_contrato_ant")
    @Temporal(TemporalType.DATE)
    private Date fechaContratoAnt;
    @Size(max = 500)
    @Column(name = "identificacion")
    private String identificacion;
    @Size(max = 450)
    @Column(name = "nombre_comprador")
    private String nombreComprador;
    @Column(name = "num_comprobante")
    private BigInteger numComprobante;
    @Column(name = "num_liquidacion")
    private BigInteger numLiquidacion;
    @Size(max = 2147483647)
    @Column(name = "observacion")
    private String observacion;
    @Column(name = "otros")
    private BigDecimal otros;
    @Column(name = "saldo")
    private BigDecimal saldo;
    @Column(name = "tipo_liquidacion")
    private BigInteger tipoLiquidacion;
    @Column(name = "total_adicionales")
    private BigDecimal totalAdicionales;
    @Column(name = "total_pago")
    private BigDecimal totalPago;
    @Size(max = 40)
    @Column(name = "usuario_ingreso")
    private String usuarioIngreso;
    @Column(name = "validada")
    private Boolean validada;
    @Column(name = "valor_comercial")
    private BigDecimal valorComercial;
    @Column(name = "valor_compra")
    private BigDecimal valorCompra;
    @Column(name = "valor_catastral")
    private BigDecimal valorCatastral;
    @Column(name = "valor_exoneracion")
    private BigDecimal valorExoneracion;
    @Column(name = "valor_hipoteca")
    private BigDecimal valorHipoteca;
    @Column(name = "valor_nominal")
    private BigDecimal valorNominal;
    @Column(name = "valor_mejoras")
    private BigDecimal valorMejoras;
    @Column(name = "valor_mora")
    private BigDecimal valorMora;
    @Column(name = "valor_venta")
    private BigDecimal valorVenta;
    @Column(name = "predio")
    private Long predio;
    @OneToMany(mappedBy = "liquidacion", fetch = FetchType.LAZY)
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<RenValorInicialDetLiquidacion> renValorInicialDetLiquidacionList;

    public RenValorInicialLiquidacion() {
    }

    public RenValorInicialLiquidacion(Long id) {
        this.id = id;
    }

    public RenValorInicialLiquidacion(Long id, Date fechaIngreso) {
        this.id = id;
        this.fechaIngreso = fechaIngreso;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdLiquidacion() {
        return idLiquidacion;
    }

    public void setIdLiquidacion(String idLiquidacion) {
        this.idLiquidacion = idLiquidacion;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public BigDecimal getAvaluoConstruccion() {
        return avaluoConstruccion;
    }

    public void setAvaluoConstruccion(BigDecimal avaluoConstruccion) {
        this.avaluoConstruccion = avaluoConstruccion;
    }

    public BigDecimal getAvaluoMunicipal() {
        return avaluoMunicipal;
    }

    public void setAvaluoMunicipal(BigDecimal avaluoMunicipal) {
        this.avaluoMunicipal = avaluoMunicipal;
    }

    public BigDecimal getAvaluoSolar() {
        return avaluoSolar;
    }

    public void setAvaluoSolar(BigDecimal avaluoSolar) {
        this.avaluoSolar = avaluoSolar;
    }

    public BigDecimal getAreaTotal() {
        return areaTotal;
    }

    public void setAreaTotal(BigDecimal areaTotal) {
        this.areaTotal = areaTotal;
    }

    public BigDecimal getBandaImpositiva() {
        return bandaImpositiva;
    }

    public void setBandaImpositiva(BigDecimal bandaImpositiva) {
        this.bandaImpositiva = bandaImpositiva;
    }

    public BigDecimal getBaseImponible() {
        return baseImponible;
    }

    public void setBaseImponible(BigDecimal baseImponible) {
        this.baseImponible = baseImponible;
    }

    public Boolean getBombero() {
        return bombero;
    }

    public void setBombero(Boolean bombero) {
        this.bombero = bombero;
    }

    public Boolean getCoactiva() {
        return coactiva;
    }

    public void setCoactiva(Boolean coactiva) {
        this.coactiva = coactiva;
    }

    public BigInteger getComprador() {
        return comprador;
    }

    public void setComprador(BigInteger comprador) {
        this.comprador = comprador;
    }

    public BigDecimal getCostoAdq() {
        return costoAdq;
    }

    public void setCostoAdq(BigDecimal costoAdq) {
        this.costoAdq = costoAdq;
    }

    public BigDecimal getCuantia() {
        return cuantia;
    }

    public void setCuantia(BigDecimal cuantia) {
        this.cuantia = cuantia;
    }

    public Integer getEstadoCoactiva() {
        return estadoCoactiva;
    }

    public void setEstadoCoactiva(Integer estadoCoactiva) {
        this.estadoCoactiva = estadoCoactiva;
    }

    public BigInteger getEstadoLiquidacion() {
        return estadoLiquidacion;
    }

    public void setEstadoLiquidacion(BigInteger estadoLiquidacion) {
        this.estadoLiquidacion = estadoLiquidacion;
    }

    public String getEstadoReferencia() {
        return estadoReferencia;
    }

    public void setEstadoReferencia(String estadoReferencia) {
        this.estadoReferencia = estadoReferencia;
    }

    public String getExoneracionDescripcion() {
        return exoneracionDescripcion;
    }

    public void setExoneracionDescripcion(String exoneracionDescripcion) {
        this.exoneracionDescripcion = exoneracionDescripcion;
    }

    public Boolean getExonerado() {
        return exonerado;
    }

    public void setExonerado(Boolean exonerado) {
        this.exonerado = exonerado;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public Date getFechaContratoAnt() {
        return fechaContratoAnt;
    }

    public void setFechaContratoAnt(Date fechaContratoAnt) {
        this.fechaContratoAnt = fechaContratoAnt;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public String getNombreComprador() {
        return nombreComprador;
    }

    public void setNombreComprador(String nombreComprador) {
        this.nombreComprador = nombreComprador;
    }

    public BigInteger getNumComprobante() {
        return numComprobante;
    }

    public void setNumComprobante(BigInteger numComprobante) {
        this.numComprobante = numComprobante;
    }

    public BigInteger getNumLiquidacion() {
        return numLiquidacion;
    }

    public void setNumLiquidacion(BigInteger numLiquidacion) {
        this.numLiquidacion = numLiquidacion;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public BigDecimal getOtros() {
        return otros;
    }

    public void setOtros(BigDecimal otros) {
        this.otros = otros;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public BigInteger getTipoLiquidacion() {
        return tipoLiquidacion;
    }

    public void setTipoLiquidacion(BigInteger tipoLiquidacion) {
        this.tipoLiquidacion = tipoLiquidacion;
    }

    public BigDecimal getTotalAdicionales() {
        return totalAdicionales;
    }

    public void setTotalAdicionales(BigDecimal totalAdicionales) {
        this.totalAdicionales = totalAdicionales;
    }

    public BigDecimal getTotalPago() {
        return totalPago;
    }

    public void setTotalPago(BigDecimal totalPago) {
        this.totalPago = totalPago;
    }

    public String getUsuarioIngreso() {
        return usuarioIngreso;
    }

    public void setUsuarioIngreso(String usuarioIngreso) {
        this.usuarioIngreso = usuarioIngreso;
    }

    public Boolean getValidada() {
        return validada;
    }

    public void setValidada(Boolean validada) {
        this.validada = validada;
    }

    public BigDecimal getValorComercial() {
        return valorComercial;
    }

    public void setValorComercial(BigDecimal valorComercial) {
        this.valorComercial = valorComercial;
    }

    public BigDecimal getValorCompra() {
        return valorCompra;
    }

    public void setValorCompra(BigDecimal valorCompra) {
        this.valorCompra = valorCompra;
    }

    public BigDecimal getValorCatastral() {
        return valorCatastral;
    }

    public void setValorCatastral(BigDecimal valorCatastral) {
        this.valorCatastral = valorCatastral;
    }

    public BigDecimal getValorExoneracion() {
        return valorExoneracion;
    }

    public void setValorExoneracion(BigDecimal valorExoneracion) {
        this.valorExoneracion = valorExoneracion;
    }

    public BigDecimal getValorHipoteca() {
        return valorHipoteca;
    }

    public void setValorHipoteca(BigDecimal valorHipoteca) {
        this.valorHipoteca = valorHipoteca;
    }

    public BigDecimal getValorNominal() {
        return valorNominal;
    }

    public void setValorNominal(BigDecimal valorNominal) {
        this.valorNominal = valorNominal;
    }

    public BigDecimal getValorMejoras() {
        return valorMejoras;
    }

    public void setValorMejoras(BigDecimal valorMejoras) {
        this.valorMejoras = valorMejoras;
    }

    public BigDecimal getValorMora() {
        return valorMora;
    }

    public void setValorMora(BigDecimal valorMora) {
        this.valorMora = valorMora;
    }

    public BigDecimal getValorVenta() {
        return valorVenta;
    }

    public void setValorVenta(BigDecimal valorVenta) {
        this.valorVenta = valorVenta;
    }

    public Long getPredio() {
        return predio;
    }

    public void setPredio(Long predio) {
        this.predio = predio;
    }

    
    public List<RenValorInicialDetLiquidacion> getRenValorInicialDetLiquidacionList() {
        return renValorInicialDetLiquidacionList;
    }

    public void setRenValorInicialDetLiquidacionList(List<RenValorInicialDetLiquidacion> renValorInicialDetLiquidacionList) {
        this.renValorInicialDetLiquidacionList = renValorInicialDetLiquidacionList;
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
        if (!(object instanceof RenValorInicialLiquidacion)) {
            return false;
        }
        RenValorInicialLiquidacion other = (RenValorInicialLiquidacion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.educacion.Models.RenValorInicialLiquidacion[ id=" + id + " ]";
    }
    
}
