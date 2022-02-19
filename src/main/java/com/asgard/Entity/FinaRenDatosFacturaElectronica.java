/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.asgard.Entity;

import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author DEVELOPER
 */
@Entity
@Table(name = "fina_ren_datos_factura_electronica", schema = Utils.SCHEMA_ASGARD)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FinaRenDatosFacturaElectronica.findAll", query = "SELECT f FROM FinaRenDatosFacturaElectronica f"),
    @NamedQuery(name = "FinaRenDatosFacturaElectronica.findById", query = "SELECT f FROM FinaRenDatosFacturaElectronica f WHERE f.id = :id"),
    @NamedQuery(name = "FinaRenDatosFacturaElectronica.findByCode", query = "SELECT f FROM FinaRenDatosFacturaElectronica f WHERE f.code = :code"),
    @NamedQuery(name = "FinaRenDatosFacturaElectronica.findByTipoComprobante", query = "SELECT f FROM FinaRenDatosFacturaElectronica f WHERE f.tipoComprobante = :tipoComprobante"),
    @NamedQuery(name = "FinaRenDatosFacturaElectronica.findByRuc", query = "SELECT f FROM FinaRenDatosFacturaElectronica f WHERE f.ruc = :ruc"),
    @NamedQuery(name = "FinaRenDatosFacturaElectronica.findByAmbiente", query = "SELECT f FROM FinaRenDatosFacturaElectronica f WHERE f.ambiente = :ambiente"),
    @NamedQuery(name = "FinaRenDatosFacturaElectronica.findBySerie", query = "SELECT f FROM FinaRenDatosFacturaElectronica f WHERE f.serie = :serie"),
    @NamedQuery(name = "FinaRenDatosFacturaElectronica.findByTipoEmision", query = "SELECT f FROM FinaRenDatosFacturaElectronica f WHERE f.tipoEmision = :tipoEmision"),
    @NamedQuery(name = "FinaRenDatosFacturaElectronica.findByValorMin", query = "SELECT f FROM FinaRenDatosFacturaElectronica f WHERE f.valorMin = :valorMin"),
    @NamedQuery(name = "FinaRenDatosFacturaElectronica.findByValorMax", query = "SELECT f FROM FinaRenDatosFacturaElectronica f WHERE f.valorMax = :valorMax"),
    @NamedQuery(name = "FinaRenDatosFacturaElectronica.findByRazonSocial", query = "SELECT f FROM FinaRenDatosFacturaElectronica f WHERE f.razonSocial = :razonSocial"),
    @NamedQuery(name = "FinaRenDatosFacturaElectronica.findByDireccionMatriz", query = "SELECT f FROM FinaRenDatosFacturaElectronica f WHERE f.direccionMatriz = :direccionMatriz"),
    @NamedQuery(name = "FinaRenDatosFacturaElectronica.findByTipoImpuesto", query = "SELECT f FROM FinaRenDatosFacturaElectronica f WHERE f.tipoImpuesto = :tipoImpuesto"),
    @NamedQuery(name = "FinaRenDatosFacturaElectronica.findByTipoPorcentajeImpuesto", query = "SELECT f FROM FinaRenDatosFacturaElectronica f WHERE f.tipoPorcentajeImpuesto = :tipoPorcentajeImpuesto"),
    @NamedQuery(name = "FinaRenDatosFacturaElectronica.findByMoneda", query = "SELECT f FROM FinaRenDatosFacturaElectronica f WHERE f.moneda = :moneda"),
    @NamedQuery(name = "FinaRenDatosFacturaElectronica.findByFormaPago", query = "SELECT f FROM FinaRenDatosFacturaElectronica f WHERE f.formaPago = :formaPago"),
    @NamedQuery(name = "FinaRenDatosFacturaElectronica.findByDiasPlazo", query = "SELECT f FROM FinaRenDatosFacturaElectronica f WHERE f.diasPlazo = :diasPlazo"),
    @NamedQuery(name = "FinaRenDatosFacturaElectronica.findByUnidadTiempo", query = "SELECT f FROM FinaRenDatosFacturaElectronica f WHERE f.unidadTiempo = :unidadTiempo"),
    @NamedQuery(name = "FinaRenDatosFacturaElectronica.findByCobroIva", query = "SELECT f FROM FinaRenDatosFacturaElectronica f WHERE f.cobroIva = :cobroIva")})
public class FinaRenDatosFacturaElectronica implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "code")
    private String code;
    @Size(max = 50)
    @Column(name = "tipo_comprobante")
    private String tipoComprobante;
    @Size(max = 50)
    @Column(name = "ruc")
    private String ruc;
    @Size(max = 50)
    @Column(name = "ambiente")
    private String ambiente;
    @Size(max = 50)
    @Column(name = "serie")
    private String serie;
    @Size(max = 50)
    @Column(name = "tipo_emision")
    private String tipoEmision;
    @Column(name = "valor_min")
    private Integer valorMin;
    @Column(name = "valor_max")
    private Integer valorMax;
    @Size(max = 500)
    @Column(name = "razon_social")
    private String razonSocial;
    @Size(max = 500)
    @Column(name = "direccion_matriz")
    private String direccionMatriz;
    @Size(max = 50)
    @Column(name = "tipo_impuesto")
    private String tipoImpuesto;
    @Size(max = 50)
    @Column(name = "tipo_porcentaje_impuesto")
    private String tipoPorcentajeImpuesto;
    @Size(max = 50)
    @Column(name = "moneda")
    private String moneda;
    @Size(max = 50)
    @Column(name = "forma_pago")
    private String formaPago;
    @Size(max = 50)
    @Column(name = "dias_plazo")
    private String diasPlazo;
    @Size(max = 50)
    @Column(name = "unidad_tiempo")
    private String unidadTiempo;
    @Size(max = 5)
    @Column(name = "cobro_iva")
    private String cobroIva;

    public FinaRenDatosFacturaElectronica() {
    }

    public FinaRenDatosFacturaElectronica(Long id) {
        this.id = id;
    }

    public FinaRenDatosFacturaElectronica(Long id, String code) {
        this.id = id;
        this.code = code;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTipoComprobante() {
        return tipoComprobante;
    }

    public void setTipoComprobante(String tipoComprobante) {
        this.tipoComprobante = tipoComprobante;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getAmbiente() {
        return ambiente;
    }

    public void setAmbiente(String ambiente) {
        this.ambiente = ambiente;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public String getTipoEmision() {
        return tipoEmision;
    }

    public void setTipoEmision(String tipoEmision) {
        this.tipoEmision = tipoEmision;
    }

    public Integer getValorMin() {
        return valorMin;
    }

    public void setValorMin(Integer valorMin) {
        this.valorMin = valorMin;
    }

    public Integer getValorMax() {
        return valorMax;
    }

    public void setValorMax(Integer valorMax) {
        this.valorMax = valorMax;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getDireccionMatriz() {
        return direccionMatriz;
    }

    public void setDireccionMatriz(String direccionMatriz) {
        this.direccionMatriz = direccionMatriz;
    }

    public String getTipoImpuesto() {
        return tipoImpuesto;
    }

    public void setTipoImpuesto(String tipoImpuesto) {
        this.tipoImpuesto = tipoImpuesto;
    }

    public String getTipoPorcentajeImpuesto() {
        return tipoPorcentajeImpuesto;
    }

    public void setTipoPorcentajeImpuesto(String tipoPorcentajeImpuesto) {
        this.tipoPorcentajeImpuesto = tipoPorcentajeImpuesto;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public String getFormaPago() {
        return formaPago;
    }

    public void setFormaPago(String formaPago) {
        this.formaPago = formaPago;
    }

    public String getDiasPlazo() {
        return diasPlazo;
    }

    public void setDiasPlazo(String diasPlazo) {
        this.diasPlazo = diasPlazo;
    }

    public String getUnidadTiempo() {
        return unidadTiempo;
    }

    public void setUnidadTiempo(String unidadTiempo) {
        this.unidadTiempo = unidadTiempo;
    }

    public String getCobroIva() {
        return cobroIva;
    }

    public void setCobroIva(String cobroIva) {
        this.cobroIva = cobroIva;
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
        if (!(object instanceof FinaRenDatosFacturaElectronica)) {
            return false;
        }
        FinaRenDatosFacturaElectronica other = (FinaRenDatosFacturaElectronica) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.educacion.Models.FinaRenDatosFacturaElectronica[ id=" + id + " ]";
    }
    
}
