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
import javax.persistence.Basic;
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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "predio_anio", schema = Utils.SCHEMA_CATASTRO)
@XmlRootElement
public class PredioAnio implements Serializable {

    private static final long serialVersionUID = 1L;
    @Size(max = 2147483647)
    @Column(name = "cod_catastral_predio")
    private String codCatastralPredio;
    @Size(max = 2147483647)
    @Column(name = "cod_anterior_predio")
    private String codAnteriorPredio;
    @Size(max = 2147483647)
    @Column(name = "calle_predio")
    private String callePredio;
    @Size(max = 2147483647)
    @Column(name = "numero_predio")
    private String numeroPredio;
    @Size(max = 2147483647)
    @Column(name = "barrio_predio")
    private String barrioPredio;
    @Size(max = 2147483647)
    @Column(name = "prop_anterior_predio")
    private String propAnteriorPredio;
    @Column(name = "area_total_predio")
    private BigInteger areaTotalPredio;
    @Size(max = 2147483647)
    @Column(name = "dominio_predio")
    private String dominioPredio;
    @Column(name = "frente_princ_predio")
    private BigInteger frentePrincPredio;
    @Column(name = "fondo_relat_predio")
    private BigInteger fondoRelatPredio;
    @Column(name = "valor_terreno_predio")
    private BigInteger valorTerrenoPredio;
    @Column(name = "valor_edific_predio")
    private BigInteger valorEdificPredio;
    @Column(name = "valor_elemento_valorizable")
    private BigInteger valorElementoValorizable;
    @Column(name = "valor_comercial_predio")
    private BigInteger valorComercialPredio;
    @Column(name = "base_imponible_predio")
    private BigInteger baseImponiblePredio;
    @Size(max = 2147483647)
    @Column(name = "tipo_predio")
    private String tipoPredio;
    @Column(name = "propied_hor_predio")
    private BigInteger propiedHorPredio;
    @Column(name = "pre_ph")
    private Integer prePh;
    @Column(name = "cod_div_pol")
    private Character codDivPol;
    @Column(name = "area_const_predio")
    private BigInteger areaConstPredio;
    @Column(name = "numero_bloques_predio")
    private Integer numeroBloquesPredio;
    @Size(max = 2147483647)
    @Column(name = "ced_ident_prop")
    private String cedIdentProp;
    @Size(max = 2147483647)
    @Column(name = "otros_prop")
    private String otrosProp;
    @Size(max = 2147483647)
    @Column(name = "apellidos_ciudadano")
    private String apellidosCiudadano;
    @Size(max = 2147483647)
    @Column(name = "nombres_ciudadano")
    private String nombresCiudadano;
    @Column(name = "ter_edad_prop")
    private Integer terEdadProp;
    @Column(name = "cod_finan_prestamo")
    private Integer codFinanPrestamo;
    @Column(name = "monto_prestamo")
    private BigInteger montoPrestamo;
    @Size(max = 2147483647)
    @Column(name = "plazo_prestamo")
    private String plazoPrestamo;
    @Column(name = "fecha_consecion_prestamo")
    @Temporal(TemporalType.DATE)
    private Date fechaConsecionPrestamo;
    @Column(name = "porcentaje_exon_prestamo")
    private Integer porcentajeExonPrestamo;
    @Size(max = 2147483647)
    @Column(name = "num_notaria_actual")
    private String numNotariaActual;
    @Size(max = 2147483647)
    @Column(name = "fecha_inscripcion_actual")
    private String fechaInscripcionActual;
    @Size(max = 2147483647)
    @Column(name = "lugar_inscripcion_actual")
    private String lugarInscripcionActual;
    @Size(max = 2147483647)
    @Column(name = "registro_prop_actual")
    private String registroPropActual;
    @Size(max = 2147483647)
    @Column(name = "fecha_registro_actual")
    private String fechaRegistroActual;
    @Column(name = "escritura_actual")
    private Integer escrituraActual;
    @Size(max = 2147483647)
    @Column(name = "nombre_notario")
    private String nombreNotario;
    @Size(max = 2147483647)
    @Column(name = "anio")
    private String anio;
    @Size(max = 2147483647)
    @Column(name = "condicion_propietario")
    private String condicionPropietario;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @JoinColumn(name = "id_predio", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatPredio idPredio;
    @Column(name = "valor_comercial_predio_2017")
    private BigDecimal valorComercialPredio2017;
    @Column(name = "base_imponible_predio_2017")
    private BigDecimal baseImponiblePredio2017;
    @Column(name = "avaluo_construccion")
    private BigDecimal avaluoConstruccion;
    @Column(name = "avaluo_solar")
    private BigDecimal avaluoSolar;
    @Column(name = "avaluo_municipal")
    private BigDecimal avaluoMunicipal;
    @Column(name = "tarifa_imp")
    private BigDecimal tarifaImpositiva;

    public PredioAnio() {
    }

    public PredioAnio(Long id) {
        this.id = id;
    }

    public String getCodCatastralPredio() {
        return codCatastralPredio;
    }

    public void setCodCatastralPredio(String codCatastralPredio) {
        this.codCatastralPredio = codCatastralPredio;
    }

    public String getCodAnteriorPredio() {
        return codAnteriorPredio;
    }

    public void setCodAnteriorPredio(String codAnteriorPredio) {
        this.codAnteriorPredio = codAnteriorPredio;
    }

    public String getCallePredio() {
        return callePredio;
    }

    public void setCallePredio(String callePredio) {
        this.callePredio = callePredio;
    }

    public String getNumeroPredio() {
        return numeroPredio;
    }

    public void setNumeroPredio(String numeroPredio) {
        this.numeroPredio = numeroPredio;
    }

    public String getBarrioPredio() {
        return barrioPredio;
    }

    public void setBarrioPredio(String barrioPredio) {
        this.barrioPredio = barrioPredio;
    }

    public String getPropAnteriorPredio() {
        return propAnteriorPredio;
    }

    public void setPropAnteriorPredio(String propAnteriorPredio) {
        this.propAnteriorPredio = propAnteriorPredio;
    }

    public BigInteger getAreaTotalPredio() {
        return areaTotalPredio;
    }

    public void setAreaTotalPredio(BigInteger areaTotalPredio) {
        this.areaTotalPredio = areaTotalPredio;
    }

    public String getDominioPredio() {
        return dominioPredio;
    }

    public void setDominioPredio(String dominioPredio) {
        this.dominioPredio = dominioPredio;
    }

    public BigInteger getFrentePrincPredio() {
        return frentePrincPredio;
    }

    public void setFrentePrincPredio(BigInteger frentePrincPredio) {
        this.frentePrincPredio = frentePrincPredio;
    }

    public BigInteger getFondoRelatPredio() {
        return fondoRelatPredio;
    }

    public void setFondoRelatPredio(BigInteger fondoRelatPredio) {
        this.fondoRelatPredio = fondoRelatPredio;
    }

    public BigInteger getValorTerrenoPredio() {
        return valorTerrenoPredio;
    }

    public void setValorTerrenoPredio(BigInteger valorTerrenoPredio) {
        this.valorTerrenoPredio = valorTerrenoPredio;
    }

    public BigInteger getValorEdificPredio() {
        return valorEdificPredio;
    }

    public void setValorEdificPredio(BigInteger valorEdificPredio) {
        this.valorEdificPredio = valorEdificPredio;
    }

    public BigInteger getValorElementoValorizable() {
        return valorElementoValorizable;
    }

    public void setValorElementoValorizable(BigInteger valorElementoValorizable) {
        this.valorElementoValorizable = valorElementoValorizable;
    }

    public BigInteger getValorComercialPredio() {
        return valorComercialPredio;
    }

    public void setValorComercialPredio(BigInteger valorComercialPredio) {
        this.valorComercialPredio = valorComercialPredio;
    }

    public BigInteger getBaseImponiblePredio() {
        return baseImponiblePredio;
    }

    public void setBaseImponiblePredio(BigInteger baseImponiblePredio) {
        this.baseImponiblePredio = baseImponiblePredio;
    }

    public String getTipoPredio() {
        return tipoPredio;
    }

    public void setTipoPredio(String tipoPredio) {
        this.tipoPredio = tipoPredio;
    }

    public BigInteger getPropiedHorPredio() {
        return propiedHorPredio;
    }

    public void setPropiedHorPredio(BigInteger propiedHorPredio) {
        this.propiedHorPredio = propiedHorPredio;
    }

    public Integer getPrePh() {
        return prePh;
    }

    public void setPrePh(Integer prePh) {
        this.prePh = prePh;
    }

    public Character getCodDivPol() {
        return codDivPol;
    }

    public void setCodDivPol(Character codDivPol) {
        this.codDivPol = codDivPol;
    }

    public BigInteger getAreaConstPredio() {
        return areaConstPredio;
    }

    public void setAreaConstPredio(BigInteger areaConstPredio) {
        this.areaConstPredio = areaConstPredio;
    }

    public Integer getNumeroBloquesPredio() {
        return numeroBloquesPredio;
    }

    public void setNumeroBloquesPredio(Integer numeroBloquesPredio) {
        this.numeroBloquesPredio = numeroBloquesPredio;
    }

    public String getCedIdentProp() {
        return cedIdentProp;
    }

    public void setCedIdentProp(String cedIdentProp) {
        this.cedIdentProp = cedIdentProp;
    }

    public String getOtrosProp() {
        return otrosProp;
    }

    public void setOtrosProp(String otrosProp) {
        this.otrosProp = otrosProp;
    }

    public String getApellidosCiudadano() {
        return apellidosCiudadano;
    }

    public void setApellidosCiudadano(String apellidosCiudadano) {
        this.apellidosCiudadano = apellidosCiudadano;
    }

    public String getNombresCiudadano() {
        return nombresCiudadano;
    }

    public void setNombresCiudadano(String nombresCiudadano) {
        this.nombresCiudadano = nombresCiudadano;
    }

    public Integer getTerEdadProp() {
        return terEdadProp;
    }

    public void setTerEdadProp(Integer terEdadProp) {
        this.terEdadProp = terEdadProp;
    }

    public Integer getCodFinanPrestamo() {
        return codFinanPrestamo;
    }

    public void setCodFinanPrestamo(Integer codFinanPrestamo) {
        this.codFinanPrestamo = codFinanPrestamo;
    }

    public BigInteger getMontoPrestamo() {
        return montoPrestamo;
    }

    public void setMontoPrestamo(BigInteger montoPrestamo) {
        this.montoPrestamo = montoPrestamo;
    }

    public String getPlazoPrestamo() {
        return plazoPrestamo;
    }

    public void setPlazoPrestamo(String plazoPrestamo) {
        this.plazoPrestamo = plazoPrestamo;
    }

    public Date getFechaConsecionPrestamo() {
        return fechaConsecionPrestamo;
    }

    public void setFechaConsecionPrestamo(Date fechaConsecionPrestamo) {
        this.fechaConsecionPrestamo = fechaConsecionPrestamo;
    }

    public Integer getPorcentajeExonPrestamo() {
        return porcentajeExonPrestamo;
    }

    public void setPorcentajeExonPrestamo(Integer porcentajeExonPrestamo) {
        this.porcentajeExonPrestamo = porcentajeExonPrestamo;
    }

    public String getNumNotariaActual() {
        return numNotariaActual;
    }

    public void setNumNotariaActual(String numNotariaActual) {
        this.numNotariaActual = numNotariaActual;
    }

    public String getFechaInscripcionActual() {
        return fechaInscripcionActual;
    }

    public void setFechaInscripcionActual(String fechaInscripcionActual) {
        this.fechaInscripcionActual = fechaInscripcionActual;
    }

    public String getLugarInscripcionActual() {
        return lugarInscripcionActual;
    }

    public void setLugarInscripcionActual(String lugarInscripcionActual) {
        this.lugarInscripcionActual = lugarInscripcionActual;
    }

    public String getRegistroPropActual() {
        return registroPropActual;
    }

    public void setRegistroPropActual(String registroPropActual) {
        this.registroPropActual = registroPropActual;
    }

    public String getFechaRegistroActual() {
        return fechaRegistroActual;
    }

    public void setFechaRegistroActual(String fechaRegistroActual) {
        this.fechaRegistroActual = fechaRegistroActual;
    }

    public Integer getEscrituraActual() {
        return escrituraActual;
    }

    public void setEscrituraActual(Integer escrituraActual) {
        this.escrituraActual = escrituraActual;
    }

    public String getNombreNotario() {
        return nombreNotario;
    }

    public void setNombreNotario(String nombreNotario) {
        this.nombreNotario = nombreNotario;
    }

    public String getAnio() {
        return anio;
    }

    public void setAnio(String anio) {
        this.anio = anio;
    }

    public String getCondicionPropietario() {
        return condicionPropietario;
    }

    public void setCondicionPropietario(String condicionPropietario) {
        this.condicionPropietario = condicionPropietario;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CatPredio getIdPredio() {
        return idPredio;
    }

    public void setIdPredio(CatPredio idPredio) {
        this.idPredio = idPredio;
    }

    public BigDecimal getValorComercialPredio2017() {
        return valorComercialPredio2017;
    }

    public void setValorComercialPredio2017(BigDecimal valorComercialPredio2017) {
        this.valorComercialPredio2017 = valorComercialPredio2017;
    }

    public BigDecimal getBaseImponiblePredio2017() {
        return baseImponiblePredio2017;
    }

    public void setBaseImponiblePredio2017(BigDecimal baseImponiblePredio2017) {
        this.baseImponiblePredio2017 = baseImponiblePredio2017;
    }

    public BigDecimal getAvaluoConstruccion() {
        return avaluoConstruccion;
    }

    public void setAvaluoConstruccion(BigDecimal avaluoConstruccion) {
        this.avaluoConstruccion = avaluoConstruccion;
    }

    public BigDecimal getAvaluoSolar() {
        return avaluoSolar;
    }

    public void setAvaluoSolar(BigDecimal avaluoSolar) {
        this.avaluoSolar = avaluoSolar;
    }

    public BigDecimal getAvaluoMunicipal() {
        return avaluoMunicipal;
    }

    public void setAvaluoMunicipal(BigDecimal avaluoMunicipal) {
        this.avaluoMunicipal = avaluoMunicipal;
    }

    public BigDecimal getTarifaImpositiva() {
        return tarifaImpositiva;
    }

    public void setTarifaImpositiva(BigDecimal tarifaImpositiva) {
        this.tarifaImpositiva = tarifaImpositiva;
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
        if (!(object instanceof PredioAnio)) {
            return false;
        }
        PredioAnio other = (PredioAnio) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gestionTributaria.Entities.PredioAnio[ id=" + id + " ]";
    }

}
