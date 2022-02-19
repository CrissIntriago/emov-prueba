/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.catastro.Entities;

import com.gestionTributaria.Entities.CatPredio;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "cat_predio_s4", schema = Utils.SCHEMA_CATASTRO)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CatPredioS4.findAll", query = "SELECT c FROM CatPredioS4 c"),
    @NamedQuery(name = "CatPredioS4.findById", query = "SELECT c FROM CatPredioS4 c WHERE c.id = :id"),
    @NamedQuery(name = "CatPredioS4.findByPredio", query = "SELECT c FROM CatPredioS4 c WHERE c.predio = :predio"),
    @NamedQuery(name = "CatPredioS4.findByCerramientoCtlg", query = "SELECT c FROM CatPredioS4 c WHERE c.cerramientoCtlg = :cerramientoCtlg"),
    @NamedQuery(name = "CatPredioS4.findByTopografia", query = "SELECT c FROM CatPredioS4 c WHERE c.topografia = :topografia"),
    @NamedQuery(name = "CatPredioS4.findByTipoSuelo", query = "SELECT c FROM CatPredioS4 c WHERE c.tipoSuelo = :tipoSuelo"),
    @NamedQuery(name = "CatPredioS4.findByLocManzana", query = "SELECT c FROM CatPredioS4 c WHERE c.locManzana = :locManzana"),
    @NamedQuery(name = "CatPredioS4.findByFrente1", query = "SELECT c FROM CatPredioS4 c WHERE c.frente1 = :frente1"),
    @NamedQuery(name = "CatPredioS4.findByFrente2", query = "SELECT c FROM CatPredioS4 c WHERE c.frente2 = :frente2"),
    @NamedQuery(name = "CatPredioS4.findByFrente3", query = "SELECT c FROM CatPredioS4 c WHERE c.frente3 = :frente3"),
    @NamedQuery(name = "CatPredioS4.findByFrente4", query = "SELECT c FROM CatPredioS4 c WHERE c.frente4 = :frente4"),
    @NamedQuery(name = "CatPredioS4.findByFrenteTotal", query = "SELECT c FROM CatPredioS4 c WHERE c.frenteTotal = :frenteTotal"),
    @NamedQuery(name = "CatPredioS4.findByFondo1", query = "SELECT c FROM CatPredioS4 c WHERE c.fondo1 = :fondo1"),
    @NamedQuery(name = "CatPredioS4.findByFondo2", query = "SELECT c FROM CatPredioS4 c WHERE c.fondo2 = :fondo2"),
    @NamedQuery(name = "CatPredioS4.findByAreaCalculada", query = "SELECT c FROM CatPredioS4 c WHERE c.areaCalculada = :areaCalculada"),
    @NamedQuery(name = "CatPredioS4.findByNumHombres", query = "SELECT c FROM CatPredioS4 c WHERE c.numHombres = :numHombres"),
    @NamedQuery(name = "CatPredioS4.findByNumMujeres", query = "SELECT c FROM CatPredioS4 c WHERE c.numMujeres = :numMujeres"),
    @NamedQuery(name = "CatPredioS4.findByNumAdultos", query = "SELECT c FROM CatPredioS4 c WHERE c.numAdultos = :numAdultos"),
    @NamedQuery(name = "CatPredioS4.findByNumNinos", query = "SELECT c FROM CatPredioS4 c WHERE c.numNinos = :numNinos"),
    @NamedQuery(name = "CatPredioS4.findByCoberturaPredominante", query = "SELECT c FROM CatPredioS4 c WHERE c.coberturaPredominante = :coberturaPredominante"),
    @NamedQuery(name = "CatPredioS4.findByEcosistemaRelevante", query = "SELECT c FROM CatPredioS4 c WHERE c.ecosistemaRelevante = :ecosistemaRelevante"),
    @NamedQuery(name = "CatPredioS4.findByRiesgo", query = "SELECT c FROM CatPredioS4 c WHERE c.riesgo = :riesgo"),
    @NamedQuery(name = "CatPredioS4.findByErosion", query = "SELECT c FROM CatPredioS4 c WHERE c.erosion = :erosion"),
    @NamedQuery(name = "CatPredioS4.findByDrenaje", query = "SELECT c FROM CatPredioS4 c WHERE c.drenaje = :drenaje"),
    @NamedQuery(name = "CatPredioS4.findByTipoObraMejora", query = "SELECT c FROM CatPredioS4 c WHERE c.tipoObraMejora = :tipoObraMejora"),
    @NamedQuery(name = "CatPredioS4.findByMaterialMejora", query = "SELECT c FROM CatPredioS4 c WHERE c.materialMejora = :materialMejora"),
    @NamedQuery(name = "CatPredioS4.findByAreaMejora", query = "SELECT c FROM CatPredioS4 c WHERE c.areaMejora = :areaMejora"),
    @NamedQuery(name = "CatPredioS4.findByEstadoMejora", query = "SELECT c FROM CatPredioS4 c WHERE c.estadoMejora = :estadoMejora"),
    @NamedQuery(name = "CatPredioS4.findByAreaGraficaLote", query = "SELECT c FROM CatPredioS4 c WHERE c.areaGraficaLote = :areaGraficaLote"),
    @NamedQuery(name = "CatPredioS4.findByTieneHipoteca", query = "SELECT c FROM CatPredioS4 c WHERE c.tieneHipoteca = :tieneHipoteca"),
    @NamedQuery(name = "CatPredioS4.findByInstFinancieraHip", query = "SELECT c FROM CatPredioS4 c WHERE c.instFinancieraHip = :instFinancieraHip"),
    @NamedQuery(name = "CatPredioS4.findByLoteEnConflicto", query = "SELECT c FROM CatPredioS4 c WHERE c.loteEnConflicto = :loteEnConflicto"),
    @NamedQuery(name = "CatPredioS4.findByOpbservLoteEnConflicto", query = "SELECT c FROM CatPredioS4 c WHERE c.opbservLoteEnConflicto = :opbservLoteEnConflicto"),
    @NamedQuery(name = "CatPredioS4.findByTienePermisoConst", query = "SELECT c FROM CatPredioS4 c WHERE c.tienePermisoConst = :tienePermisoConst"),
    @NamedQuery(name = "CatPredioS4.findByTieneRetiros", query = "SELECT c FROM CatPredioS4 c WHERE c.tieneRetiros = :tieneRetiros"),
    @NamedQuery(name = "CatPredioS4.findByTieneAdosamiento", query = "SELECT c FROM CatPredioS4 c WHERE c.tieneAdosamiento = :tieneAdosamiento"),
    @NamedQuery(name = "CatPredioS4.findByAfectacionLote", query = "SELECT c FROM CatPredioS4 c WHERE c.afectacionLote = :afectacionLote"),
    @NamedQuery(name = "CatPredioS4.findBySuperficie", query = "SELECT c FROM CatPredioS4 c WHERE c.superficie = :superficie"),
    @NamedQuery(name = "CatPredioS4.findByAreaAccesoPriv", query = "SELECT c FROM CatPredioS4 c WHERE c.areaAccesoPriv = :areaAccesoPriv"),
    @NamedQuery(name = "CatPredioS4.findByAreaEscritura", query = "SELECT c FROM CatPredioS4 c WHERE c.areaEscritura = :areaEscritura"),
    @NamedQuery(name = "CatPredioS4.findByRecursosPropios", query = "SELECT c FROM CatPredioS4 c WHERE c.recursosPropios = :recursosPropios"),
    @NamedQuery(name = "CatPredioS4.findByNivelTerreno", query = "SELECT c FROM CatPredioS4 c WHERE c.nivelTerreno = :nivelTerreno"),
    @NamedQuery(name = "CatPredioS4.findByRodadura", query = "SELECT c FROM CatPredioS4 c WHERE c.rodadura = :rodadura"),
    @NamedQuery(name = "CatPredioS4.findByUnidadmAreaGrafica", query = "SELECT c FROM CatPredioS4 c WHERE c.unidadmAreaGrafica = :unidadmAreaGrafica")})
public class CatPredioS4 implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @JoinColumn(name = "predio", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatPredio predio;
    @Column(name = "cerramiento_ctlg")
    private BigInteger cerramientoCtlg;
    @Column(name = "topografia")
    private BigInteger topografia;
    @Column(name = "tipo_suelo")
    private BigInteger tipoSuelo;
    @JoinColumn(name = "loc_manzana", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    //@ReportField(description = "Localizacion en Mz", type = FieldType.COLLECTION_ONE_TO_ONE)
    private CatalogoItem locManzana;
    @Column(name = "frente1")
    private BigDecimal frente1;
    @Column(name = "frente2")
    private BigDecimal frente2;
    @Column(name = "frente3")
    private BigDecimal frente3;
    @Column(name = "frente4")
    private BigDecimal frente4;
    @Column(name = "frente_total")
    private BigDecimal frenteTotal;
    @Column(name = "fondo1")
    private BigDecimal fondo1;
    @Column(name = "fondo2")
    private BigDecimal fondo2;
    @Column(name = "area_calculada")
    private BigDecimal areaCalculada;
    @Column(name = "num_hombres")
    private Short numHombres;
    @Column(name = "num_mujeres")
    private Short numMujeres;
    @Column(name = "num_adultos")
    private Short numAdultos;
    @Column(name = "num_ninos")
    private Short numNinos;
    @JoinColumn(name = "estado_solar", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem estadoSolar;
    @Column(name = "cobertura_predominante")
    private BigInteger coberturaPredominante;
    @Column(name = "ecosistema_relevante")
    private BigInteger ecosistemaRelevante;
    @Column(name = "riesgo")
    private BigInteger riesgo;
    @Column(name = "erosion")
    private BigInteger erosion;
    @Column(name = "drenaje")
    private BigInteger drenaje;
    @Column(name = "tipo_obra_mejora")
    private BigInteger tipoObraMejora;
    @Column(name = "material_mejora")
    private BigInteger materialMejora;
    @Column(name = "area_mejora")
    private BigDecimal areaMejora;
    @Column(name = "estado_mejora")
    private BigInteger estadoMejora;
    @Column(name = "area_grafica_lote")
    private BigDecimal areaGraficaLote;
    @Column(name = "tiene_hipoteca")
    private Boolean tieneHipoteca;
    @Size(max = 255)
    @Column(name = "inst_financiera_hip")
    private String instFinancieraHip;
    @Column(name = "lote_en_conflicto")
    private Boolean loteEnConflicto;
    @Size(max = 255)
    @Column(name = "opbserv_lote_en_conflicto")
    private String opbservLoteEnConflicto;
    @Column(name = "tiene_permiso_const")
    private Boolean tienePermisoConst;
    @Column(name = "tiene_retiros")
    private Boolean tieneRetiros;
    @Column(name = "tiene_adosamiento")
    private Boolean tieneAdosamiento;
    @Column(name = "afectacion_lote")
    private BigInteger afectacionLote;
    @Column(name = "superficie")
    private BigDecimal superficie;
    @Column(name = "area_acceso_priv")
    private BigDecimal areaAccesoPriv;
    @Column(name = "area_escritura")
    private BigDecimal areaEscritura;
    @Column(name = "recursos_propios")
    private Boolean recursosPropios;
    @Column(name = "nivel_terreno")
    private BigInteger nivelTerreno;
    @Column(name = "rodadura")
    private BigInteger rodadura;
    @Column(name = "unidadm_area_grafica")//valorkaskalklasdk
    private BigInteger unidadmAreaGrafica;

    public CatPredioS4() {
    }

    public CatPredioS4(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CatPredio getPredio() {
        return predio;
    }

    public void setPredio(CatPredio predio) {
        this.predio = predio;
    }

    public BigInteger getCerramientoCtlg() {
        return cerramientoCtlg;
    }

    public void setCerramientoCtlg(BigInteger cerramientoCtlg) {
        this.cerramientoCtlg = cerramientoCtlg;
    }

    public BigInteger getTopografia() {
        return topografia;
    }

    public void setTopografia(BigInteger topografia) {
        this.topografia = topografia;
    }

    public BigInteger getTipoSuelo() {
        return tipoSuelo;
    }

    public void setTipoSuelo(BigInteger tipoSuelo) {
        this.tipoSuelo = tipoSuelo;
    }

    public CatalogoItem getLocManzana() {
        return locManzana;
    }

    public void setLocManzana(CatalogoItem locManzana) {
        this.locManzana = locManzana;
    }

    public Short getNumHombres() {
        return numHombres;
    }

    public void setNumHombres(Short numHombres) {
        this.numHombres = numHombres;
    }

    public Short getNumMujeres() {
        return numMujeres;
    }

    public void setNumMujeres(Short numMujeres) {
        this.numMujeres = numMujeres;
    }

    public Short getNumAdultos() {
        return numAdultos;
    }

    public void setNumAdultos(Short numAdultos) {
        this.numAdultos = numAdultos;
    }

    public Short getNumNinos() {
        return numNinos;
    }

    public void setNumNinos(Short numNinos) {
        this.numNinos = numNinos;
    }

    public CatalogoItem getEstadoSolar() {
        return estadoSolar;
    }

    public void setEstadoSolar(CatalogoItem estadoSolar) {
        this.estadoSolar = estadoSolar;
    }

    public BigInteger getCoberturaPredominante() {
        return coberturaPredominante;
    }

    public void setCoberturaPredominante(BigInteger coberturaPredominante) {
        this.coberturaPredominante = coberturaPredominante;
    }

    public BigInteger getEcosistemaRelevante() {
        return ecosistemaRelevante;
    }

    public void setEcosistemaRelevante(BigInteger ecosistemaRelevante) {
        this.ecosistemaRelevante = ecosistemaRelevante;
    }

    public BigInteger getRiesgo() {
        return riesgo;
    }

    public void setRiesgo(BigInteger riesgo) {
        this.riesgo = riesgo;
    }

    public BigInteger getErosion() {
        return erosion;
    }

    public void setErosion(BigInteger erosion) {
        this.erosion = erosion;
    }

    public BigInteger getDrenaje() {
        return drenaje;
    }

    public void setDrenaje(BigInteger drenaje) {
        this.drenaje = drenaje;
    }

    public BigInteger getTipoObraMejora() {
        return tipoObraMejora;
    }

    public void setTipoObraMejora(BigInteger tipoObraMejora) {
        this.tipoObraMejora = tipoObraMejora;
    }

    public BigInteger getMaterialMejora() {
        return materialMejora;
    }

    public void setMaterialMejora(BigInteger materialMejora) {
        this.materialMejora = materialMejora;
    }

    public BigInteger getEstadoMejora() {
        return estadoMejora;
    }

    public void setEstadoMejora(BigInteger estadoMejora) {
        this.estadoMejora = estadoMejora;
    }

    public Boolean getTieneHipoteca() {
        return tieneHipoteca;
    }

    public void setTieneHipoteca(Boolean tieneHipoteca) {
        this.tieneHipoteca = tieneHipoteca;
    }

    public String getInstFinancieraHip() {
        return instFinancieraHip;
    }

    public void setInstFinancieraHip(String instFinancieraHip) {
        this.instFinancieraHip = instFinancieraHip;
    }

    public Boolean getLoteEnConflicto() {
        return loteEnConflicto;
    }

    public void setLoteEnConflicto(Boolean loteEnConflicto) {
        this.loteEnConflicto = loteEnConflicto;
    }

    public String getOpbservLoteEnConflicto() {
        return opbservLoteEnConflicto;
    }

    public void setOpbservLoteEnConflicto(String opbservLoteEnConflicto) {
        this.opbservLoteEnConflicto = opbservLoteEnConflicto;
    }

    public Boolean getTienePermisoConst() {
        return tienePermisoConst;
    }

    public void setTienePermisoConst(Boolean tienePermisoConst) {
        this.tienePermisoConst = tienePermisoConst;
    }

    public Boolean getTieneRetiros() {
        return tieneRetiros;
    }

    public void setTieneRetiros(Boolean tieneRetiros) {
        this.tieneRetiros = tieneRetiros;
    }

    public Boolean getTieneAdosamiento() {
        return tieneAdosamiento;
    }

    public void setTieneAdosamiento(Boolean tieneAdosamiento) {
        this.tieneAdosamiento = tieneAdosamiento;
    }

    public BigInteger getAfectacionLote() {
        return afectacionLote;
    }

    public void setAfectacionLote(BigInteger afectacionLote) {
        this.afectacionLote = afectacionLote;
    }

    public Boolean getRecursosPropios() {
        return recursosPropios;
    }

    public void setRecursosPropios(Boolean recursosPropios) {
        this.recursosPropios = recursosPropios;
    }

    public BigInteger getNivelTerreno() {
        return nivelTerreno;
    }

    public void setNivelTerreno(BigInteger nivelTerreno) {
        this.nivelTerreno = nivelTerreno;
    }

    public BigInteger getRodadura() {
        return rodadura;
    }

    public void setRodadura(BigInteger rodadura) {
        this.rodadura = rodadura;
    }

    public BigInteger getUnidadmAreaGrafica() {
        return unidadmAreaGrafica;
    }

    public void setUnidadmAreaGrafica(BigInteger unidadmAreaGrafica) {
        this.unidadmAreaGrafica = unidadmAreaGrafica;
    }

    public BigDecimal getFrente1() {
        return frente1;
    }

    public void setFrente1(BigDecimal frente1) {
        this.frente1 = frente1;
    }

    public BigDecimal getFrente2() {
        return frente2;
    }

    public void setFrente2(BigDecimal frente2) {
        this.frente2 = frente2;
    }

    public BigDecimal getFrente3() {
        return frente3;
    }

    public void setFrente3(BigDecimal frente3) {
        this.frente3 = frente3;
    }

    public BigDecimal getFrente4() {
        return frente4;
    }

    public void setFrente4(BigDecimal frente4) {
        this.frente4 = frente4;
    }

    public BigDecimal getFrenteTotal() {
        return frenteTotal;
    }

    public void setFrenteTotal(BigDecimal frenteTotal) {
        this.frenteTotal = frenteTotal;
    }

    public BigDecimal getFondo1() {
        return fondo1;
    }

    public void setFondo1(BigDecimal fondo1) {
        this.fondo1 = fondo1;
    }

    public BigDecimal getFondo2() {
        return fondo2;
    }

    public void setFondo2(BigDecimal fondo2) {
        this.fondo2 = fondo2;
    }

    public BigDecimal getAreaCalculada() {
        return areaCalculada;
    }

    public void setAreaCalculada(BigDecimal areaCalculada) {
        this.areaCalculada = areaCalculada;
    }

    public BigDecimal getAreaMejora() {
        return areaMejora;
    }

    public void setAreaMejora(BigDecimal areaMejora) {
        this.areaMejora = areaMejora;
    }

    public BigDecimal getAreaGraficaLote() {
        return areaGraficaLote;
    }

    public void setAreaGraficaLote(BigDecimal areaGraficaLote) {
        this.areaGraficaLote = areaGraficaLote;
    }

    public BigDecimal getSuperficie() {
        return superficie;
    }

    public void setSuperficie(BigDecimal superficie) {
        this.superficie = superficie;
    }

    public BigDecimal getAreaAccesoPriv() {
        return areaAccesoPriv;
    }

    public void setAreaAccesoPriv(BigDecimal areaAccesoPriv) {
        this.areaAccesoPriv = areaAccesoPriv;
    }

    public BigDecimal getAreaEscritura() {
        return areaEscritura;
    }

    public void setAreaEscritura(BigDecimal areaEscritura) {
        this.areaEscritura = areaEscritura;
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
        if (!(object instanceof CatPredioS4)) {
            return false;
        }
        CatPredioS4 other = (CatPredioS4) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.catastro.Entities.CatPredioS4[ id=" + id + " ]";
    }

}
