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
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
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
@Table(name = "cat_predio_s6", schema = Utils.SCHEMA_CATASTRO)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CatPredioS6.findAll", query = "SELECT c FROM CatPredioS6 c"),
    @NamedQuery(name = "CatPredioS6.findById", query = "SELECT c FROM CatPredioS6 c WHERE c.id = :id"),
    @NamedQuery(name = "CatPredioS6.findByPredio", query = "SELECT c FROM CatPredioS6 c WHERE c.predio = :predio"),
    @NamedQuery(name = "CatPredioS6.findByTieneAguaPotable", query = "SELECT c FROM CatPredioS6 c WHERE c.tieneAguaPotable = :tieneAguaPotable"),
    @NamedQuery(name = "CatPredioS6.findByNumMedidoresAgua", query = "SELECT c FROM CatPredioS6 c WHERE c.numMedidoresAgua = :numMedidoresAgua"),
    @NamedQuery(name = "CatPredioS6.findByTieneAlcantarillado", query = "SELECT c FROM CatPredioS6 c WHERE c.tieneAlcantarillado = :tieneAlcantarillado"),
    @NamedQuery(name = "CatPredioS6.findByAbastAguaProviene", query = "SELECT c FROM CatPredioS6 c WHERE c.abastAguaProviene = :abastAguaProviene"),
    @NamedQuery(name = "CatPredioS6.findByEvacAguasServ", query = "SELECT c FROM CatPredioS6 c WHERE c.evacAguasServ = :evacAguasServ"),
    @NamedQuery(name = "CatPredioS6.findByTieneElectricidad", query = "SELECT c FROM CatPredioS6 c WHERE c.tieneElectricidad = :tieneElectricidad"),
    @NamedQuery(name = "CatPredioS6.findByNumMedElect", query = "SELECT c FROM CatPredioS6 c WHERE c.numMedElect = :numMedElect"),
    @NamedQuery(name = "CatPredioS6.findByTieneInternet", query = "SELECT c FROM CatPredioS6 c WHERE c.tieneInternet = :tieneInternet"),
    @NamedQuery(name = "CatPredioS6.findByAbasteElectrico", query = "SELECT c FROM CatPredioS6 c WHERE c.abasteElectrico = :abasteElectrico"),
    @NamedQuery(name = "CatPredioS6.findByTieneTelfFijo", query = "SELECT c FROM CatPredioS6 c WHERE c.tieneTelfFijo = :tieneTelfFijo"),
    @NamedQuery(name = "CatPredioS6.findByTelfFijo", query = "SELECT c FROM CatPredioS6 c WHERE c.telfFijo = :telfFijo"),
    @NamedQuery(name = "CatPredioS6.findByRecolBasura", query = "SELECT c FROM CatPredioS6 c WHERE c.recolBasura = :recolBasura"),
    @NamedQuery(name = "CatPredioS6.findByTieneAceras", query = "SELECT c FROM CatPredioS6 c WHERE c.tieneAceras = :tieneAceras"),
    @NamedQuery(name = "CatPredioS6.findByTieneBordillo", query = "SELECT c FROM CatPredioS6 c WHERE c.tieneBordillo = :tieneBordillo"),
    @NamedQuery(name = "CatPredioS6.findByTieneCunetas", query = "SELECT c FROM CatPredioS6 c WHERE c.tieneCunetas = :tieneCunetas"),
    @NamedQuery(name = "CatPredioS6.findByCanton", query = "SELECT c FROM CatPredioS6 c WHERE c.canton = :canton"),
    @NamedQuery(name = "CatPredioS6.findByFechaEscritura", query = "SELECT c FROM CatPredioS6 c WHERE c.fechaEscritura = :fechaEscritura"),
    @NamedQuery(name = "CatPredioS6.findByFechaInscripcion", query = "SELECT c FROM CatPredioS6 c WHERE c.fechaInscripcion = :fechaInscripcion"),
    @NamedQuery(name = "CatPredioS6.findByNumRegistro", query = "SELECT c FROM CatPredioS6 c WHERE c.numRegistro = :numRegistro"),
    @NamedQuery(name = "CatPredioS6.findByNumRepertorio", query = "SELECT c FROM CatPredioS6 c WHERE c.numRepertorio = :numRepertorio"),
    @NamedQuery(name = "CatPredioS6.findByFolioDesde", query = "SELECT c FROM CatPredioS6 c WHERE c.folioDesde = :folioDesde"),
    @NamedQuery(name = "CatPredioS6.findByFolioHasta", query = "SELECT c FROM CatPredioS6 c WHERE c.folioHasta = :folioHasta"),
    @NamedQuery(name = "CatPredioS6.findByAreaSolar", query = "SELECT c FROM CatPredioS6 c WHERE c.areaSolar = :areaSolar"),
    @NamedQuery(name = "CatPredioS6.findByAreaCons", query = "SELECT c FROM CatPredioS6 c WHERE c.areaCons = :areaCons"),
    @NamedQuery(name = "CatPredioS6.findByAlicuota", query = "SELECT c FROM CatPredioS6 c WHERE c.alicuota = :alicuota"),
    @NamedQuery(name = "CatPredioS6.findByLindLevNorte", query = "SELECT c FROM CatPredioS6 c WHERE c.lindLevNorte = :lindLevNorte"),
    @NamedQuery(name = "CatPredioS6.findByLindLevNorteCon", query = "SELECT c FROM CatPredioS6 c WHERE c.lindLevNorteCon = :lindLevNorteCon"),
    @NamedQuery(name = "CatPredioS6.findByLindLevSur", query = "SELECT c FROM CatPredioS6 c WHERE c.lindLevSur = :lindLevSur"),
    @NamedQuery(name = "CatPredioS6.findByLindLevSurCon", query = "SELECT c FROM CatPredioS6 c WHERE c.lindLevSurCon = :lindLevSurCon"),
    @NamedQuery(name = "CatPredioS6.findByLindLevEste", query = "SELECT c FROM CatPredioS6 c WHERE c.lindLevEste = :lindLevEste"),
    @NamedQuery(name = "CatPredioS6.findByLindLevEsteCon", query = "SELECT c FROM CatPredioS6 c WHERE c.lindLevEsteCon = :lindLevEsteCon"),
    @NamedQuery(name = "CatPredioS6.findByLindLevOeste", query = "SELECT c FROM CatPredioS6 c WHERE c.lindLevOeste = :lindLevOeste"),
    @NamedQuery(name = "CatPredioS6.findByLindLevOesteCon", query = "SELECT c FROM CatPredioS6 c WHERE c.lindLevOesteCon = :lindLevOesteCon"),
    @NamedQuery(name = "CatPredioS6.findByLindEscNorte", query = "SELECT c FROM CatPredioS6 c WHERE c.lindEscNorte = :lindEscNorte"),
    @NamedQuery(name = "CatPredioS6.findByLindEscNorteCon", query = "SELECT c FROM CatPredioS6 c WHERE c.lindEscNorteCon = :lindEscNorteCon"),
    @NamedQuery(name = "CatPredioS6.findByLindEscSur", query = "SELECT c FROM CatPredioS6 c WHERE c.lindEscSur = :lindEscSur"),
    @NamedQuery(name = "CatPredioS6.findByLindEscSurCon", query = "SELECT c FROM CatPredioS6 c WHERE c.lindEscSurCon = :lindEscSurCon"),
    @NamedQuery(name = "CatPredioS6.findByLindEscEste", query = "SELECT c FROM CatPredioS6 c WHERE c.lindEscEste = :lindEscEste"),
    @NamedQuery(name = "CatPredioS6.findByLindEscEsteCon", query = "SELECT c FROM CatPredioS6 c WHERE c.lindEscEsteCon = :lindEscEsteCon"),
    @NamedQuery(name = "CatPredioS6.findByLindEscOeste", query = "SELECT c FROM CatPredioS6 c WHERE c.lindEscOeste = :lindEscOeste"),
    @NamedQuery(name = "CatPredioS6.findByLindEscOesteCon", query = "SELECT c FROM CatPredioS6 c WHERE c.lindEscOesteCon = :lindEscOesteCon"),
    @NamedQuery(name = "CatPredioS6.findByFolioDesdeCad", query = "SELECT c FROM CatPredioS6 c WHERE c.folioDesdeCad = :folioDesdeCad"),
    @NamedQuery(name = "CatPredioS6.findByFolioHastaCad", query = "SELECT c FROM CatPredioS6 c WHERE c.folioHastaCad = :folioHastaCad"),
    @NamedQuery(name = "CatPredioS6.findByAlumbrado", query = "SELECT c FROM CatPredioS6 c WHERE c.alumbrado = :alumbrado"),
    @NamedQuery(name = "CatPredioS6.findByAseoCalles", query = "SELECT c FROM CatPredioS6 c WHERE c.aseoCalles = :aseoCalles"),
    @NamedQuery(name = "CatPredioS6.findByTvPag", query = "SELECT c FROM CatPredioS6 c WHERE c.tvPag = :tvPag"),
    @NamedQuery(name = "CatPredioS6.findByTpublico", query = "SELECT c FROM CatPredioS6 c WHERE c.tpublico = :tpublico"),
    @NamedQuery(name = "CatPredioS6.findByMedidorEe", query = "SELECT c FROM CatPredioS6 c WHERE c.medidorEe = :medidorEe"),
    @NamedQuery(name = "CatPredioS6.findByNotaria", query = "SELECT c FROM CatPredioS6 c WHERE c.notaria = :notaria"),
    @NamedQuery(name = "CatPredioS6.findByAlcantarilladoPluvial", query = "SELECT c FROM CatPredioS6 c WHERE c.alcantarilladoPluvial = :alcantarilladoPluvial"),
    @NamedQuery(name = "CatPredioS6.findByMedidorAgua", query = "SELECT c FROM CatPredioS6 c WHERE c.medidorAgua = :medidorAgua"),
    @NamedQuery(name = "CatPredioS6.findByMedidorElectrico", query = "SELECT c FROM CatPredioS6 c WHERE c.medidorElectrico = :medidorElectrico"),
    @NamedQuery(name = "CatPredioS6.findByTelefoniaSatelital", query = "SELECT c FROM CatPredioS6 c WHERE c.telefoniaSatelital = :telefoniaSatelital"),
    @NamedQuery(name = "CatPredioS6.findByAlarmasComunitarias", query = "SELECT c FROM CatPredioS6 c WHERE c.alarmasComunitarias = :alarmasComunitarias"),
    @NamedQuery(name = "CatPredioS6.findByRecoleccionBasura", query = "SELECT c FROM CatPredioS6 c WHERE c.recoleccionBasura = :recoleccionBasura"),
    @NamedQuery(name = "CatPredioS6.findByAbasAguaRecibe", query = "SELECT c FROM CatPredioS6 c WHERE c.abasAguaRecibe = :abasAguaRecibe"),
    @NamedQuery(name = "CatPredioS6.findByCoberturaCelular", query = "SELECT c FROM CatPredioS6 c WHERE c.coberturaCelular = :coberturaCelular"),
    @NamedQuery(name = "CatPredioS6.findByMetodoRiego", query = "SELECT c FROM CatPredioS6 c WHERE c.metodoRiego = :metodoRiego"),
    @NamedQuery(name = "CatPredioS6.findByDisponibilidadRiego", query = "SELECT c FROM CatPredioS6 c WHERE c.disponibilidadRiego = :disponibilidadRiego"),
    @NamedQuery(name = "CatPredioS6.findByAbastEnergiaProviene", query = "SELECT c FROM CatPredioS6 c WHERE c.abastEnergiaProviene = :abastEnergiaProviene"),
    @NamedQuery(name = "CatPredioS6.findByCercaniaPoblado", query = "SELECT c FROM CatPredioS6 c WHERE c.cercaniaPoblado = :cercaniaPoblado"),
    @NamedQuery(name = "CatPredioS6.findByUnidadmAreaEscritura", query = "SELECT c FROM CatPredioS6 c WHERE c.unidadmAreaEscritura = :unidadmAreaEscritura")})
public class CatPredioS6 implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @JoinColumn(name = "predio", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatPredio predio;
    @Column(name = "tiene_agua_potable")
    private Boolean tieneAguaPotable;
    @Column(name = "num_medidores_agua")
    private Short numMedidoresAgua;
    @Column(name = "tiene_alcantarillado")
    private Boolean tieneAlcantarillado;
    @JoinColumn(name = "abast_agua_proviene", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem abastAguaProviene;
    @JoinColumn(name = "evac_aguas_serv", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem evacAguasServ;
    @Column(name = "tiene_electricidad")
    private Boolean tieneElectricidad;
    @Size(max = 2147483647)
    @Column(name = "num_med_elect")
    private String numMedElect;
    @Column(name = "tiene_internet")
    private Boolean tieneInternet;
    @JoinColumn(name = "abaste_electrico", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem abasteElectrico;
    @Column(name = "tiene_telf_fijo")
    private Boolean tieneTelfFijo;
    @Size(max = 255)
    @Column(name = "telf_fijo")
    private String telfFijo;
    @JoinColumn(name = "recol_basura", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem recolBasura;
    @Column(name = "tiene_aceras")
    private Boolean tieneAceras;
    @Column(name = "tiene_bordillo")
    private Boolean tieneBordillo;
    @Column(name = "tiene_cunetas")
    private Boolean tieneCunetas;
    @Column(name = "canton")
    private BigInteger canton;
    @Column(name = "fecha_escritura")
    @Temporal(TemporalType.DATE)
    private Date fechaEscritura;
    @Column(name = "fecha_inscripcion")
    @Temporal(TemporalType.DATE)
    private Date fechaInscripcion;
    @Column(name = "num_registro")
    private BigInteger numRegistro;
    @Column(name = "num_repertorio")
    private Integer numRepertorio;
    @Column(name = "folio_desde")
    private Integer folioDesde;
    @Column(name = "folio_hasta")
    private Integer folioHasta;
    @Column(name = "area_solar")
    private BigDecimal areaSolar;
    @Column(name = "area_cons")
    private BigDecimal areaCons;
    @Column(name = "alicuota")
    private BigDecimal alicuota;
    @Size(max = 45)
    @Column(name = "lind_lev_norte")
    private String lindLevNorte;
    @Column(name = "lind_lev_norte_con")
    private BigDecimal lindLevNorteCon;
    @Size(max = 45)
    @Column(name = "lind_lev_sur")
    private String lindLevSur;
    @Column(name = "lind_lev_sur_con")
    private BigDecimal lindLevSurCon;
    @Size(max = 45)
    @Column(name = "lind_lev_este")
    private String lindLevEste;
    @Column(name = "lind_lev_este_con")
    private BigDecimal lindLevEsteCon;
    @Size(max = 45)
    @Column(name = "lind_lev_oeste")
    private String lindLevOeste;
    @Column(name = "lind_lev_oeste_con")
    private BigDecimal lindLevOesteCon;
    @Size(max = 45)
    @Column(name = "lind_esc_norte")
    private String lindEscNorte;
    @Column(name = "lind_esc_norte_con")
    private BigDecimal lindEscNorteCon;
    @Size(max = 45)
    @Column(name = "lind_esc_sur")
    private String lindEscSur;
    @Column(name = "lind_esc_sur_con")
    private BigDecimal lindEscSurCon;
    @Size(max = 45)
    @Column(name = "lind_esc_este")
    private String lindEscEste;
    @Column(name = "lind_esc_este_con")
    private BigDecimal lindEscEsteCon;
    @Size(max = 45)
    @Column(name = "lind_esc_oeste")
    private String lindEscOeste;
    @Column(name = "lind_esc_oeste_con")
    private BigDecimal lindEscOesteCon;
    @Size(max = 40)
    @Column(name = "folio_desde_cad")
    private String folioDesdeCad;
    @Size(max = 40)
    @Column(name = "folio_hasta_cad")
    private String folioHastaCad;
    @Column(name = "alumbrado")
    private Boolean alumbrado;
    @Column(name = "aseo_calles")
    private Boolean aseoCalles;
    @Column(name = "tv_pag")
    private Boolean tvPag;
    @Column(name = "tpublico")
    private Boolean tpublico;
    @Column(name = "medidor_ee")
    private Boolean medidorEe;
    @Size(max = 350)
    @Column(name = "notaria")
    private String notaria;
    @Column(name = "alcantarillado_pluvial")
    private Boolean alcantarilladoPluvial;
    @Size(max = 45)
    @Column(name = "medidor_agua")
    private String medidorAgua;
    @Size(max = 45)
    @Column(name = "medidor_electrico")
    private String medidorElectrico;
    @Column(name = "telefonia_satelital")
    private Boolean telefoniaSatelital;
    @Column(name = "alarmas_comunitarias")
    private Boolean alarmasComunitarias;
    @Column(name = "recoleccion_basura")
    private Boolean recoleccionBasura;
    @JoinColumn(name = "abas_agua_recibe", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem abasAguaRecibe;
    @Column(name = "cobertura_celular")
    private Boolean coberturaCelular;
    @JoinColumn(name = "metodo_riego", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem metodoRiego;
    @JoinColumn(name = "disponibilidad_riego", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem disponibilidadRiego;
    @JoinColumn(name = "abast_energia_proviene", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem abastEnergiaProviene;
    @JoinColumn(name = "cercania_poblado", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem cercaniaPoblado;
    @JoinColumn(name = "unidadm_area_escritura", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem unidadmAreaEscritura;
    @JoinTable(schema = Utils.SCHEMA_CATASTRO, name = "cat_predio_s6_has_usos", joinColumns = {
        @JoinColumn(name = "predio_s6", referencedColumnName = "id")},
            inverseJoinColumns = {
                @JoinColumn(name = "uso", referencedColumnName = "id")})
    @ManyToMany(fetch = FetchType.LAZY)
    private Collection<CatalogoItem> usosList;

    public CatPredioS6() {
    }

    public CatPredioS6(Long id) {
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

    public Boolean getTieneAguaPotable() {
        return tieneAguaPotable;
    }

    public void setTieneAguaPotable(Boolean tieneAguaPotable) {
        this.tieneAguaPotable = tieneAguaPotable;
    }

    public Short getNumMedidoresAgua() {
        return numMedidoresAgua;
    }

    public void setNumMedidoresAgua(Short numMedidoresAgua) {
        this.numMedidoresAgua = numMedidoresAgua;
    }

    public Boolean getTieneAlcantarillado() {
        return tieneAlcantarillado;
    }

    public void setTieneAlcantarillado(Boolean tieneAlcantarillado) {
        this.tieneAlcantarillado = tieneAlcantarillado;
    }

    public Boolean getTieneElectricidad() {
        return tieneElectricidad;
    }

    public void setTieneElectricidad(Boolean tieneElectricidad) {
        this.tieneElectricidad = tieneElectricidad;
    }

    public String getNumMedElect() {
        return numMedElect;
    }

    public void setNumMedElect(String numMedElect) {
        this.numMedElect = numMedElect;
    }

    public Boolean getTieneInternet() {
        return tieneInternet;
    }

    public void setTieneInternet(Boolean tieneInternet) {
        this.tieneInternet = tieneInternet;
    }

    public Boolean getTieneTelfFijo() {
        return tieneTelfFijo;
    }

    public void setTieneTelfFijo(Boolean tieneTelfFijo) {
        this.tieneTelfFijo = tieneTelfFijo;
    }

    public String getTelfFijo() {
        return telfFijo;
    }

    public void setTelfFijo(String telfFijo) {
        this.telfFijo = telfFijo;
    }

    public Boolean getTieneAceras() {
        return tieneAceras;
    }

    public void setTieneAceras(Boolean tieneAceras) {
        this.tieneAceras = tieneAceras;
    }

    public Boolean getTieneBordillo() {
        return tieneBordillo;
    }

    public void setTieneBordillo(Boolean tieneBordillo) {
        this.tieneBordillo = tieneBordillo;
    }

    public Boolean getTieneCunetas() {
        return tieneCunetas;
    }

    public void setTieneCunetas(Boolean tieneCunetas) {
        this.tieneCunetas = tieneCunetas;
    }

    public BigInteger getCanton() {
        return canton;
    }

    public void setCanton(BigInteger canton) {
        this.canton = canton;
    }

    public Date getFechaEscritura() {
        return fechaEscritura;
    }

    public void setFechaEscritura(Date fechaEscritura) {
        this.fechaEscritura = fechaEscritura;
    }

    public Date getFechaInscripcion() {
        return fechaInscripcion;
    }

    public void setFechaInscripcion(Date fechaInscripcion) {
        this.fechaInscripcion = fechaInscripcion;
    }

    public BigInteger getNumRegistro() {
        return numRegistro;
    }

    public void setNumRegistro(BigInteger numRegistro) {
        this.numRegistro = numRegistro;
    }

    public Integer getNumRepertorio() {
        return numRepertorio;
    }

    public void setNumRepertorio(Integer numRepertorio) {
        this.numRepertorio = numRepertorio;
    }

    public Integer getFolioDesde() {
        return folioDesde;
    }

    public void setFolioDesde(Integer folioDesde) {
        this.folioDesde = folioDesde;
    }

    public Integer getFolioHasta() {
        return folioHasta;
    }

    public void setFolioHasta(Integer folioHasta) {
        this.folioHasta = folioHasta;
    }

    public String getLindLevNorte() {
        return lindLevNorte;
    }

    public void setLindLevNorte(String lindLevNorte) {
        this.lindLevNorte = lindLevNorte;
    }

    public String getLindLevSur() {
        return lindLevSur;
    }

    public void setLindLevSur(String lindLevSur) {
        this.lindLevSur = lindLevSur;
    }

    public String getLindLevEste() {
        return lindLevEste;
    }

    public void setLindLevEste(String lindLevEste) {
        this.lindLevEste = lindLevEste;
    }

    public String getLindLevOeste() {
        return lindLevOeste;
    }

    public void setLindLevOeste(String lindLevOeste) {
        this.lindLevOeste = lindLevOeste;
    }

    public String getLindEscNorte() {
        return lindEscNorte;
    }

    public void setLindEscNorte(String lindEscNorte) {
        this.lindEscNorte = lindEscNorte;
    }

    public String getLindEscSur() {
        return lindEscSur;
    }

    public void setLindEscSur(String lindEscSur) {
        this.lindEscSur = lindEscSur;
    }

    public String getLindEscEste() {
        return lindEscEste;
    }

    public void setLindEscEste(String lindEscEste) {
        this.lindEscEste = lindEscEste;
    }

    public String getLindEscOeste() {
        return lindEscOeste;
    }

    public void setLindEscOeste(String lindEscOeste) {
        this.lindEscOeste = lindEscOeste;
    }

    public String getFolioDesdeCad() {
        return folioDesdeCad;
    }

    public void setFolioDesdeCad(String folioDesdeCad) {
        this.folioDesdeCad = folioDesdeCad;
    }

    public String getFolioHastaCad() {
        return folioHastaCad;
    }

    public void setFolioHastaCad(String folioHastaCad) {
        this.folioHastaCad = folioHastaCad;
    }

    public Boolean getAlumbrado() {
        return alumbrado;
    }

    public void setAlumbrado(Boolean alumbrado) {
        this.alumbrado = alumbrado;
    }

    public Boolean getAseoCalles() {
        return aseoCalles;
    }

    public void setAseoCalles(Boolean aseoCalles) {
        this.aseoCalles = aseoCalles;
    }

    public Boolean getTvPag() {
        return tvPag;
    }

    public void setTvPag(Boolean tvPag) {
        this.tvPag = tvPag;
    }

    public Boolean getTpublico() {
        return tpublico;
    }

    public void setTpublico(Boolean tpublico) {
        this.tpublico = tpublico;
    }

    public Boolean getMedidorEe() {
        return medidorEe;
    }

    public void setMedidorEe(Boolean medidorEe) {
        this.medidorEe = medidorEe;
    }

    public String getNotaria() {
        return notaria;
    }

    public void setNotaria(String notaria) {
        this.notaria = notaria;
    }

    public Boolean getAlcantarilladoPluvial() {
        return alcantarilladoPluvial;
    }

    public void setAlcantarilladoPluvial(Boolean alcantarilladoPluvial) {
        this.alcantarilladoPluvial = alcantarilladoPluvial;
    }

    public String getMedidorAgua() {
        return medidorAgua;
    }

    public void setMedidorAgua(String medidorAgua) {
        this.medidorAgua = medidorAgua;
    }

    public String getMedidorElectrico() {
        return medidorElectrico;
    }

    public void setMedidorElectrico(String medidorElectrico) {
        this.medidorElectrico = medidorElectrico;
    }

    public Boolean getTelefoniaSatelital() {
        return telefoniaSatelital;
    }

    public void setTelefoniaSatelital(Boolean telefoniaSatelital) {
        this.telefoniaSatelital = telefoniaSatelital;
    }

    public Boolean getAlarmasComunitarias() {
        return alarmasComunitarias;
    }

    public void setAlarmasComunitarias(Boolean alarmasComunitarias) {
        this.alarmasComunitarias = alarmasComunitarias;
    }

    public Boolean getRecoleccionBasura() {
        return recoleccionBasura;
    }

    public void setRecoleccionBasura(Boolean recoleccionBasura) {
        this.recoleccionBasura = recoleccionBasura;
    }

    public Boolean getCoberturaCelular() {
        return coberturaCelular;
    }

    public void setCoberturaCelular(Boolean coberturaCelular) {
        this.coberturaCelular = coberturaCelular;
    }

    public CatalogoItem getAbastAguaProviene() {
        return abastAguaProviene;
    }

    public void setAbastAguaProviene(CatalogoItem abastAguaProviene) {
        this.abastAguaProviene = abastAguaProviene;
    }

    public CatalogoItem getEvacAguasServ() {
        return evacAguasServ;
    }

    public void setEvacAguasServ(CatalogoItem evacAguasServ) {
        this.evacAguasServ = evacAguasServ;
    }

    public CatalogoItem getAbasteElectrico() {
        return abasteElectrico;
    }

    public void setAbasteElectrico(CatalogoItem abasteElectrico) {
        this.abasteElectrico = abasteElectrico;
    }

    public CatalogoItem getRecolBasura() {
        return recolBasura;
    }

    public void setRecolBasura(CatalogoItem recolBasura) {
        this.recolBasura = recolBasura;
    }

    public CatalogoItem getAbasAguaRecibe() {
        return abasAguaRecibe;
    }

    public void setAbasAguaRecibe(CatalogoItem abasAguaRecibe) {
        this.abasAguaRecibe = abasAguaRecibe;
    }

    public CatalogoItem getMetodoRiego() {
        return metodoRiego;
    }

    public void setMetodoRiego(CatalogoItem metodoRiego) {
        this.metodoRiego = metodoRiego;
    }

    public CatalogoItem getDisponibilidadRiego() {
        return disponibilidadRiego;
    }

    public void setDisponibilidadRiego(CatalogoItem disponibilidadRiego) {
        this.disponibilidadRiego = disponibilidadRiego;
    }

    public CatalogoItem getAbastEnergiaProviene() {
        return abastEnergiaProviene;
    }

    public void setAbastEnergiaProviene(CatalogoItem abastEnergiaProviene) {
        this.abastEnergiaProviene = abastEnergiaProviene;
    }

    public CatalogoItem getCercaniaPoblado() {
        return cercaniaPoblado;
    }

    public void setCercaniaPoblado(CatalogoItem cercaniaPoblado) {
        this.cercaniaPoblado = cercaniaPoblado;
    }

    public CatalogoItem getUnidadmAreaEscritura() {
        return unidadmAreaEscritura;
    }

    public void setUnidadmAreaEscritura(CatalogoItem unidadmAreaEscritura) {
        this.unidadmAreaEscritura = unidadmAreaEscritura;
    }

    public Collection<CatalogoItem> getUsosList() {
        return usosList;
    }

    public void setUsosList(Collection<CatalogoItem> usosList) {
        this.usosList = usosList;
    }

    public BigDecimal getAreaSolar() {
        return areaSolar;
    }

    public void setAreaSolar(BigDecimal areaSolar) {
        this.areaSolar = areaSolar;
    }

    public BigDecimal getAreaCons() {
        return areaCons;
    }

    public void setAreaCons(BigDecimal areaCons) {
        this.areaCons = areaCons;
    }

    public BigDecimal getAlicuota() {
        return alicuota;
    }

    public void setAlicuota(BigDecimal alicuota) {
        this.alicuota = alicuota;
    }

    public BigDecimal getLindLevNorteCon() {
        return lindLevNorteCon;
    }

    public void setLindLevNorteCon(BigDecimal lindLevNorteCon) {
        this.lindLevNorteCon = lindLevNorteCon;
    }

    public BigDecimal getLindLevSurCon() {
        return lindLevSurCon;
    }

    public void setLindLevSurCon(BigDecimal lindLevSurCon) {
        this.lindLevSurCon = lindLevSurCon;
    }

    public BigDecimal getLindLevEsteCon() {
        return lindLevEsteCon;
    }

    public void setLindLevEsteCon(BigDecimal lindLevEsteCon) {
        this.lindLevEsteCon = lindLevEsteCon;
    }

    public BigDecimal getLindLevOesteCon() {
        return lindLevOesteCon;
    }

    public void setLindLevOesteCon(BigDecimal lindLevOesteCon) {
        this.lindLevOesteCon = lindLevOesteCon;
    }

    public BigDecimal getLindEscNorteCon() {
        return lindEscNorteCon;
    }

    public void setLindEscNorteCon(BigDecimal lindEscNorteCon) {
        this.lindEscNorteCon = lindEscNorteCon;
    }

    public BigDecimal getLindEscSurCon() {
        return lindEscSurCon;
    }

    public void setLindEscSurCon(BigDecimal lindEscSurCon) {
        this.lindEscSurCon = lindEscSurCon;
    }

    public BigDecimal getLindEscEsteCon() {
        return lindEscEsteCon;
    }

    public void setLindEscEsteCon(BigDecimal lindEscEsteCon) {
        this.lindEscEsteCon = lindEscEsteCon;
    }

    public BigDecimal getLindEscOesteCon() {
        return lindEscOesteCon;
    }

    public void setLindEscOesteCon(BigDecimal lindEscOesteCon) {
        this.lindEscOesteCon = lindEscOesteCon;
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
        if (!(object instanceof CatPredioS6)) {
            return false;
        }
        CatPredioS6 other = (CatPredioS6) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.catastro.Entities.CatPredioS6[ id=" + id + " ]";
    }

}
