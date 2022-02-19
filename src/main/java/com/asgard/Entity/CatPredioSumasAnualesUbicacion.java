/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.asgard.Entity;

import com.gestionTributaria.Entities.Obra;
import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import java.math.BigDecimal;
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
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "cat_predio_sumas_anuales_ubicacion", schema = Utils.SCHEMA_ASGARD)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CatPredioSumasAnualesUbicacion.findAll", query = "SELECT c FROM CatPredioSumasAnualesUbicacion c")})
public class CatPredioSumasAnualesUbicacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "anio")
    private Integer anio;
    @JoinColumn(name = "ubicacion", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private CatUbicacion ubicacion;
    @Column(name = "avaluos_totales")
    private BigDecimal avaluosTotales;
    @Column(name = "area_solar_total")
    private BigDecimal areaSolarTotal;
    @Column(name = "pago_anual")
    private BigDecimal pagoAnual;
    @Column(name = "predio")
    private Integer predio;
    @Column(name = "porcentaje_pago")
    private BigDecimal porcentajePago;
    @JoinColumn(name = "obra", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Obra obra;

    public CatPredioSumasAnualesUbicacion() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public CatUbicacion getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(CatUbicacion ubicacion) {
        this.ubicacion = ubicacion;
    }

    public BigDecimal getAvaluosTotales() {
        return avaluosTotales;
    }

    public void setAvaluosTotales(BigDecimal avaluosTotales) {
        this.avaluosTotales = avaluosTotales;
    }

    public BigDecimal getAreaSolarTotal() {
        return areaSolarTotal;
    }

    public void setAreaSolarTotal(BigDecimal areaSolarTotal) {
        this.areaSolarTotal = areaSolarTotal;
    }

    public BigDecimal getPagoAnual() {
        return pagoAnual;
    }

    public void setPagoAnual(BigDecimal pagoAnual) {
        this.pagoAnual = pagoAnual;
    }

    public Integer getPredio() {
        return predio;
    }

    public void setPredio(Integer predio) {
        this.predio = predio;
    }

    public BigDecimal getPorcentajePago() {
        return porcentajePago;
    }

    public void setPorcentajePago(BigDecimal porcentajePago) {
        this.porcentajePago = porcentajePago;
    }

    public Obra getObra() {
        return obra;
    }

    public void setObra(Obra obra) {
        this.obra = obra;
    }

}
