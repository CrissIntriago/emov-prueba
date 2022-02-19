/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.catastro.Entities;

import com.gestionTributaria.Entities.CatPredio;
import com.origami.sigef.common.entities.Canton;
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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
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
@Table(name = "cat_escritura", schema = Utils.SCHEMA_CATASTRO)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CatEscritura.findAll", query = "SELECT c FROM CatEscritura c"),
    @NamedQuery(name = "CatEscritura.findByIdEscritura", query = "SELECT c FROM CatEscritura c WHERE c.idEscritura = :idEscritura"),
    @NamedQuery(name = "CatEscritura.findBySecuencia", query = "SELECT c FROM CatEscritura c WHERE c.secuencia = :secuencia"),
    @NamedQuery(name = "CatEscritura.findByPredio", query = "SELECT c FROM CatEscritura c WHERE c.predio = :predio"),
    @NamedQuery(name = "CatEscritura.findByNumFicha", query = "SELECT c FROM CatEscritura c WHERE c.numFicha = :numFicha"),
    @NamedQuery(name = "CatEscritura.findByCanton", query = "SELECT c FROM CatEscritura c WHERE c.canton = :canton"),
    @NamedQuery(name = "CatEscritura.findByFecEscritura", query = "SELECT c FROM CatEscritura c WHERE c.fecEscritura = :fecEscritura"),
    @NamedQuery(name = "CatEscritura.findByNumRepertorio", query = "SELECT c FROM CatEscritura c WHERE c.numRepertorio = :numRepertorio"),
    @NamedQuery(name = "CatEscritura.findByFolioDesde", query = "SELECT c FROM CatEscritura c WHERE c.folioDesde = :folioDesde"),
    @NamedQuery(name = "CatEscritura.findByFolioHasta", query = "SELECT c FROM CatEscritura c WHERE c.folioHasta = :folioHasta"),
    @NamedQuery(name = "CatEscritura.findByAreaSolar", query = "SELECT c FROM CatEscritura c WHERE c.areaSolar = :areaSolar"),
    @NamedQuery(name = "CatEscritura.findByAreaConstruccion", query = "SELECT c FROM CatEscritura c WHERE c.areaConstruccion = :areaConstruccion"),
    @NamedQuery(name = "CatEscritura.findByAlicuota", query = "SELECT c FROM CatEscritura c WHERE c.alicuota = :alicuota"),
    @NamedQuery(name = "CatEscritura.findByLindEscrNorte", query = "SELECT c FROM CatEscritura c WHERE c.lindEscrNorte = :lindEscrNorte"),
    @NamedQuery(name = "CatEscritura.findByLindEscrNorteCon", query = "SELECT c FROM CatEscritura c WHERE c.lindEscrNorteCon = :lindEscrNorteCon"),
    @NamedQuery(name = "CatEscritura.findByLindEscrEste", query = "SELECT c FROM CatEscritura c WHERE c.lindEscrEste = :lindEscrEste"),
    @NamedQuery(name = "CatEscritura.findByLindEscrEsteCon", query = "SELECT c FROM CatEscritura c WHERE c.lindEscrEsteCon = :lindEscrEsteCon"),
    @NamedQuery(name = "CatEscritura.findByLindEscrSur", query = "SELECT c FROM CatEscritura c WHERE c.lindEscrSur = :lindEscrSur"),
    @NamedQuery(name = "CatEscritura.findByLindEscrSurCon", query = "SELECT c FROM CatEscritura c WHERE c.lindEscrSurCon = :lindEscrSurCon"),
    @NamedQuery(name = "CatEscritura.findByLindEscrOeste", query = "SELECT c FROM CatEscritura c WHERE c.lindEscrOeste = :lindEscrOeste"),
    @NamedQuery(name = "CatEscritura.findByLindEscrOesteCon", query = "SELECT c FROM CatEscritura c WHERE c.lindEscrOesteCon = :lindEscrOesteCon"),
    @NamedQuery(name = "CatEscritura.findByFecInscripcion", query = "SELECT c FROM CatEscritura c WHERE c.fecInscripcion = :fecInscripcion"),
    @NamedQuery(name = "CatEscritura.findByTraslDom", query = "SELECT c FROM CatEscritura c WHERE c.traslDom = :traslDom"),
    @NamedQuery(name = "CatEscritura.findByEstado", query = "SELECT c FROM CatEscritura c WHERE c.estado = :estado"),
    @NamedQuery(name = "CatEscritura.findByNumTramite", query = "SELECT c FROM CatEscritura c WHERE c.numTramite = :numTramite"),
    @NamedQuery(name = "CatEscritura.findByAnio", query = "SELECT c FROM CatEscritura c WHERE c.anio = :anio"),
    @NamedQuery(name = "CatEscritura.findByTaskId", query = "SELECT c FROM CatEscritura c WHERE c.taskId = :taskId"),
    @NamedQuery(name = "CatEscritura.findByNotaria", query = "SELECT c FROM CatEscritura c WHERE c.notaria = :notaria"),
    @NamedQuery(name = "CatEscritura.findByLindInferior", query = "SELECT c FROM CatEscritura c WHERE c.lindInferior = :lindInferior"),
    @NamedQuery(name = "CatEscritura.findByLindSuperior", query = "SELECT c FROM CatEscritura c WHERE c.lindSuperior = :lindSuperior"),
    @NamedQuery(name = "CatEscritura.findByLindInferiorCon", query = "SELECT c FROM CatEscritura c WHERE c.lindInferiorCon = :lindInferiorCon"),
    @NamedQuery(name = "CatEscritura.findByLindSuperiorCon", query = "SELECT c FROM CatEscritura c WHERE c.lindSuperiorCon = :lindSuperiorCon"),
    @NamedQuery(name = "CatEscritura.findByUnidadArea", query = "SELECT c FROM CatEscritura c WHERE c.unidadArea = :unidadArea"),
    @NamedQuery(name = "CatEscritura.findByFechaProtocolizacion", query = "SELECT c FROM CatEscritura c WHERE c.fechaProtocolizacion = :fechaProtocolizacion"),
    @NamedQuery(name = "CatEscritura.findByFecCre", query = "SELECT c FROM CatEscritura c WHERE c.fecCre = :fecCre"),
    @NamedQuery(name = "CatEscritura.findByTipoProtocolizacion", query = "SELECT c FROM CatEscritura c WHERE c.tipoProtocolizacion = :tipoProtocolizacion"),
    @NamedQuery(name = "CatEscritura.findByPrecioCompra", query = "SELECT c FROM CatEscritura c WHERE c.precioCompra = :precioCompra"),
    @NamedQuery(name = "CatEscritura.findByPropietario", query = "SELECT c FROM CatEscritura c WHERE c.propietario = :propietario"),
    @NamedQuery(name = "CatEscritura.findByLindPredioNorte", query = "SELECT c FROM CatEscritura c WHERE c.lindPredioNorte = :lindPredioNorte"),
    @NamedQuery(name = "CatEscritura.findByLindPredioSur", query = "SELECT c FROM CatEscritura c WHERE c.lindPredioSur = :lindPredioSur"),
    @NamedQuery(name = "CatEscritura.findByLindPredioEste", query = "SELECT c FROM CatEscritura c WHERE c.lindPredioEste = :lindPredioEste"),
    @NamedQuery(name = "CatEscritura.findByLindPredioOeste", query = "SELECT c FROM CatEscritura c WHERE c.lindPredioOeste = :lindPredioOeste"),
    @NamedQuery(name = "CatEscritura.findByLindPredioSuperior", query = "SELECT c FROM CatEscritura c WHERE c.lindPredioSuperior = :lindPredioSuperior"),
    @NamedQuery(name = "CatEscritura.findByLindPredioInferior", query = "SELECT c FROM CatEscritura c WHERE c.lindPredioInferior = :lindPredioInferior"),
    @NamedQuery(name = "CatEscritura.findByTipoPh", query = "SELECT c FROM CatEscritura c WHERE c.tipoPh = :tipoPh"),
    @NamedQuery(name = "CatEscritura.findByResolucion", query = "SELECT c FROM CatEscritura c WHERE c.resolucion = :resolucion"),
    @NamedQuery(name = "CatEscritura.findByFechaResolucion", query = "SELECT c FROM CatEscritura c WHERE c.fechaResolucion = :fechaResolucion"),
    @NamedQuery(name = "CatEscritura.findByCantonBloques", query = "SELECT c FROM CatEscritura c WHERE c.cantonBloques = :cantonBloques"),
    @NamedQuery(name = "CatEscritura.findByCantAlicuotas", query = "SELECT c FROM CatEscritura c WHERE c.cantAlicuotas = :cantAlicuotas"),
    @NamedQuery(name = "CatEscritura.findByProvincia", query = "SELECT c FROM CatEscritura c WHERE c.provincia = :provincia"),
    @NamedQuery(name = "CatEscritura.findByFecProtocolizacion", query = "SELECT c FROM CatEscritura c WHERE c.fecProtocolizacion = :fecProtocolizacion"),
    @NamedQuery(name = "CatEscritura.findByFechaAutorizacion", query = "SELECT c FROM CatEscritura c WHERE c.fechaAutorizacion = :fechaAutorizacion"),
    @NamedQuery(name = "CatEscritura.findByNombreNotario", query = "SELECT c FROM CatEscritura c WHERE c.nombreNotario = :nombreNotario"),
    @NamedQuery(name = "CatEscritura.findByNumeroActualizacion", query = "SELECT c FROM CatEscritura c WHERE c.numeroActualizacion = :numeroActualizacion")})
public class CatEscritura implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_escritura")
    private Long idEscritura;
    @Column(name = "secuencia")
    private BigInteger secuencia;
    @JoinColumn(name = "predio", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatPredio predio;
    @Column(name = "num_ficha")
    private BigInteger numFicha;
    @Column(name = "canton")
    private Canton canton;
    @Column(name = "fec_escritura")
    private Date fecEscritura;
    @Column(name = "num_repertorio")
    private Integer numRepertorio;
    @Size(max = 2147483647)
    @Column(name = "folio_desde")
    private String folioDesde;
    @Size(max = 2147483647)
    @Column(name = "folio_hasta")
    private String folioHasta;
    @Column(name = "area_solar")
    private BigDecimal areaSolar;
    @Column(name = "area_construccion")
    private BigDecimal areaConstruccion;
    @Column(name = "alicuota")
    private BigDecimal alicuota;
    @Size(max = 2147483647)
    @Column(name = "lind_escr_norte")
    private String lindEscrNorte;
    @Column(name = "lind_escr_norte_con")
    private BigDecimal lindEscrNorteCon;
    @Size(max = 2147483647)
    @Column(name = "lind_escr_este")
    private String lindEscrEste;
    @Column(name = "lind_escr_este_con")
    private BigDecimal lindEscrEsteCon;
    @Size(max = 2147483647)
    @Column(name = "lind_escr_sur")
    private String lindEscrSur;
    @Column(name = "lind_escr_sur_con")
    private BigDecimal lindEscrSurCon;
    @Size(max = 50)
    @Column(name = "lind_escr_oeste")
    private String lindEscrOeste;
    @Column(name = "lind_escr_oeste_con")
    private BigDecimal lindEscrOesteCon;
    @Column(name = "fec_inscripcion")
    @Temporal(TemporalType.DATE)
    private Date fecInscripcion;
    @Column(name = "trasl_dom")
    private BigInteger traslDom;
    @Size(max = 1)
    @Column(name = "estado")
    private String estado;
    @Size(max = 2147483647)
    @Column(name = "num_tramite")
    private String numTramite;
    @Column(name = "anio")
    private BigInteger anio;
    @Size(max = 255)
    @Column(name = "task_id")
    private String taskId;
    @Size(max = 250)
    @Column(name = "notaria")
    private String notaria;
    @Size(max = 2147483647)
    @Column(name = "lind_inferior")
    private String lindInferior;
    @Size(max = 2147483647)
    @Column(name = "lind_superior")
    private String lindSuperior;
    @Column(name = "lind_inferior_con")
    private BigDecimal lindInferiorCon;
    @Column(name = "lind_superior_con")
    private BigDecimal lindSuperiorCon;
    @Column(name = "unidad_area")
    private BigInteger unidadArea;
    @Column(name = "fecha_protocolizacion")
    @Temporal(TemporalType.DATE)
    private Date fechaProtocolizacion;
    @Column(name = "fec_cre")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecCre;
    @Column(name = "tipo_protocolizacion")
    private BigInteger tipoProtocolizacion;
    @Column(name = "precio_compra")
    private BigDecimal precioCompra;
    @Column(name = "propietario")
    private BigInteger propietario;
    @Column(name = "lind_predio_norte")
    private BigInteger lindPredioNorte;
    @Column(name = "lind_predio_sur")
    private BigInteger lindPredioSur;
    @Column(name = "lind_predio_este")
    private BigInteger lindPredioEste;
    @Column(name = "lind_predio_oeste")
    private BigInteger lindPredioOeste;
    @Column(name = "lind_predio_superior")
    private BigInteger lindPredioSuperior;
    @Column(name = "lind_predio_inferior")
    private BigInteger lindPredioInferior;
    @Column(name = "tipo_ph")
    private Integer tipoPh;
    @Size(max = 100)
    @Column(name = "resolucion")
    private String resolucion;
    @Column(name = "fecha_resolucion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaResolucion;
    @Column(name = "canton_bloques")
    private Integer cantonBloques;
    @Column(name = "cant_alicuotas")
    private Integer cantAlicuotas;
    @Column(name = "provincia")
    private BigInteger provincia;
    @Column(name = "fec_protocolizacion")
    @Temporal(TemporalType.DATE)
    private Date fecProtocolizacion;
    @Column(name = "fecha_autorizacion")
    @Temporal(TemporalType.DATE)
    private Date fechaAutorizacion;
    @Size(max = 250)
    @Column(name = "nombre_notario")
    private String nombreNotario;
    @Column(name = "numero_actualizacion")
    private BigDecimal numeroActualizacion;

    public CatEscritura() {
    }

    public CatEscritura(Long idEscritura) {
        this.idEscritura = idEscritura;
    }

    public Long getIdEscritura() {
        return idEscritura;
    }

    public void setIdEscritura(Long idEscritura) {
        this.idEscritura = idEscritura;
    }

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public CatPredio getPredio() {
        return predio;
    }

    public void setPredio(CatPredio predio) {
        this.predio = predio;
    }

    public BigInteger getNumFicha() {
        return numFicha;
    }

    public void setNumFicha(BigInteger numFicha) {
        this.numFicha = numFicha;
    }

    public Canton getCanton() {
        return canton;
    }

    public void setCanton(Canton canton) {
        this.canton = canton;
    }

    public Date getFecEscritura() {
        return fecEscritura;
    }

    public void setFecEscritura(Date fecEscritura) {
        this.fecEscritura = fecEscritura;
    }

    public Integer getNumRepertorio() {
        return numRepertorio;
    }

    public void setNumRepertorio(Integer numRepertorio) {
        this.numRepertorio = numRepertorio;
    }

    public String getFolioDesde() {
        return folioDesde;
    }

    public void setFolioDesde(String folioDesde) {
        this.folioDesde = folioDesde;
    }

    public String getFolioHasta() {
        return folioHasta;
    }

    public void setFolioHasta(String folioHasta) {
        this.folioHasta = folioHasta;
    }

    public String getLindEscrNorte() {
        return lindEscrNorte;
    }

    public void setLindEscrNorte(String lindEscrNorte) {
        this.lindEscrNorte = lindEscrNorte;
    }

    public String getLindEscrEste() {
        return lindEscrEste;
    }

    public void setLindEscrEste(String lindEscrEste) {
        this.lindEscrEste = lindEscrEste;
    }

    public String getLindEscrSur() {
        return lindEscrSur;
    }

    public void setLindEscrSur(String lindEscrSur) {
        this.lindEscrSur = lindEscrSur;
    }

    public String getLindEscrOeste() {
        return lindEscrOeste;
    }

    public void setLindEscrOeste(String lindEscrOeste) {
        this.lindEscrOeste = lindEscrOeste;
    }

    public Date getFecInscripcion() {
        return fecInscripcion;
    }

    public void setFecInscripcion(Date fecInscripcion) {
        this.fecInscripcion = fecInscripcion;
    }

    public BigInteger getTraslDom() {
        return traslDom;
    }

    public void setTraslDom(BigInteger traslDom) {
        this.traslDom = traslDom;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getNumTramite() {
        return numTramite;
    }

    public void setNumTramite(String numTramite) {
        this.numTramite = numTramite;
    }

    public BigInteger getAnio() {
        return anio;
    }

    public void setAnio(BigInteger anio) {
        this.anio = anio;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getNotaria() {
        return notaria;
    }

    public void setNotaria(String notaria) {
        this.notaria = notaria;
    }

    public String getLindInferior() {
        return lindInferior;
    }

    public void setLindInferior(String lindInferior) {
        this.lindInferior = lindInferior;
    }

    public String getLindSuperior() {
        return lindSuperior;
    }

    public void setLindSuperior(String lindSuperior) {
        this.lindSuperior = lindSuperior;
    }

    public BigInteger getUnidadArea() {
        return unidadArea;
    }

    public void setUnidadArea(BigInteger unidadArea) {
        this.unidadArea = unidadArea;
    }

    public Date getFechaProtocolizacion() {
        return fechaProtocolizacion;
    }

    public void setFechaProtocolizacion(Date fechaProtocolizacion) {
        this.fechaProtocolizacion = fechaProtocolizacion;
    }

    public Date getFecCre() {
        return fecCre;
    }

    public void setFecCre(Date fecCre) {
        this.fecCre = fecCre;
    }

    public BigInteger getTipoProtocolizacion() {
        return tipoProtocolizacion;
    }

    public void setTipoProtocolizacion(BigInteger tipoProtocolizacion) {
        this.tipoProtocolizacion = tipoProtocolizacion;
    }

    public BigInteger getPropietario() {
        return propietario;
    }

    public void setPropietario(BigInteger propietario) {
        this.propietario = propietario;
    }

    public BigInteger getLindPredioNorte() {
        return lindPredioNorte;
    }

    public void setLindPredioNorte(BigInteger lindPredioNorte) {
        this.lindPredioNorte = lindPredioNorte;
    }

    public BigInteger getLindPredioSur() {
        return lindPredioSur;
    }

    public void setLindPredioSur(BigInteger lindPredioSur) {
        this.lindPredioSur = lindPredioSur;
    }

    public BigInteger getLindPredioEste() {
        return lindPredioEste;
    }

    public void setLindPredioEste(BigInteger lindPredioEste) {
        this.lindPredioEste = lindPredioEste;
    }

    public BigInteger getLindPredioOeste() {
        return lindPredioOeste;
    }

    public void setLindPredioOeste(BigInteger lindPredioOeste) {
        this.lindPredioOeste = lindPredioOeste;
    }

    public BigInteger getLindPredioSuperior() {
        return lindPredioSuperior;
    }

    public void setLindPredioSuperior(BigInteger lindPredioSuperior) {
        this.lindPredioSuperior = lindPredioSuperior;
    }

    public BigInteger getLindPredioInferior() {
        return lindPredioInferior;
    }

    public void setLindPredioInferior(BigInteger lindPredioInferior) {
        this.lindPredioInferior = lindPredioInferior;
    }

    public Integer getTipoPh() {
        return tipoPh;
    }

    public void setTipoPh(Integer tipoPh) {
        this.tipoPh = tipoPh;
    }

    public String getResolucion() {
        return resolucion;
    }

    public void setResolucion(String resolucion) {
        this.resolucion = resolucion;
    }

    public Date getFechaResolucion() {
        return fechaResolucion;
    }

    public void setFechaResolucion(Date fechaResolucion) {
        this.fechaResolucion = fechaResolucion;
    }

    public Integer getCantonBloques() {
        return cantonBloques;
    }

    public void setCantonBloques(Integer cantonBloques) {
        this.cantonBloques = cantonBloques;
    }

    public Integer getCantAlicuotas() {
        return cantAlicuotas;
    }

    public void setCantAlicuotas(Integer cantAlicuotas) {
        this.cantAlicuotas = cantAlicuotas;
    }

    public BigInteger getProvincia() {
        return provincia;
    }

    public void setProvincia(BigInteger provincia) {
        this.provincia = provincia;
    }

    public Date getFecProtocolizacion() {
        return fecProtocolizacion;
    }

    public void setFecProtocolizacion(Date fecProtocolizacion) {
        this.fecProtocolizacion = fecProtocolizacion;
    }

    public Date getFechaAutorizacion() {
        return fechaAutorizacion;
    }

    public void setFechaAutorizacion(Date fechaAutorizacion) {
        this.fechaAutorizacion = fechaAutorizacion;
    }

    public String getNombreNotario() {
        return nombreNotario;
    }

    public void setNombreNotario(String nombreNotario) {
        this.nombreNotario = nombreNotario;
    }

    public BigDecimal getAreaSolar() {
        return areaSolar;
    }

    public void setAreaSolar(BigDecimal areaSolar) {
        this.areaSolar = areaSolar;
    }

    public BigDecimal getAreaConstruccion() {
        return areaConstruccion;
    }

    public void setAreaConstruccion(BigDecimal areaConstruccion) {
        this.areaConstruccion = areaConstruccion;
    }

    public BigDecimal getAlicuota() {
        return alicuota;
    }

    public void setAlicuota(BigDecimal alicuota) {
        this.alicuota = alicuota;
    }

    public BigDecimal getLindEscrNorteCon() {
        return lindEscrNorteCon;
    }

    public void setLindEscrNorteCon(BigDecimal lindEscrNorteCon) {
        this.lindEscrNorteCon = lindEscrNorteCon;
    }

    public BigDecimal getLindEscrEsteCon() {
        return lindEscrEsteCon;
    }

    public void setLindEscrEsteCon(BigDecimal lindEscrEsteCon) {
        this.lindEscrEsteCon = lindEscrEsteCon;
    }

    public BigDecimal getLindEscrSurCon() {
        return lindEscrSurCon;
    }

    public void setLindEscrSurCon(BigDecimal lindEscrSurCon) {
        this.lindEscrSurCon = lindEscrSurCon;
    }

    public BigDecimal getLindEscrOesteCon() {
        return lindEscrOesteCon;
    }

    public void setLindEscrOesteCon(BigDecimal lindEscrOesteCon) {
        this.lindEscrOesteCon = lindEscrOesteCon;
    }

    public BigDecimal getLindInferiorCon() {
        return lindInferiorCon;
    }

    public void setLindInferiorCon(BigDecimal lindInferiorCon) {
        this.lindInferiorCon = lindInferiorCon;
    }

    public BigDecimal getLindSuperiorCon() {
        return lindSuperiorCon;
    }

    public void setLindSuperiorCon(BigDecimal lindSuperiorCon) {
        this.lindSuperiorCon = lindSuperiorCon;
    }

    public BigDecimal getPrecioCompra() {
        return precioCompra;
    }

    public void setPrecioCompra(BigDecimal precioCompra) {
        this.precioCompra = precioCompra;
    }

    public BigDecimal getNumeroActualizacion() {
        return numeroActualizacion;
    }

    public void setNumeroActualizacion(BigDecimal numeroActualizacion) {
        this.numeroActualizacion = numeroActualizacion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idEscritura != null ? idEscritura.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CatEscritura)) {
            return false;
        }
        CatEscritura other = (CatEscritura) object;
        if ((this.idEscritura == null && other.idEscritura != null) || (this.idEscritura != null && !this.idEscritura.equals(other.idEscritura))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.catastro.Entities.CatEscritura[ idEscritura=" + idEscritura + " ]";
    }

}
