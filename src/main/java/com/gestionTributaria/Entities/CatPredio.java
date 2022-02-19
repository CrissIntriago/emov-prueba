/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Entities;

import com.catastro.Entities.CatEscritura;
import com.catastro.Entities.CatPredioClasificRural;
import com.catastro.Entities.CatPredioEdificacion;
import com.catastro.Entities.CatPredioLinderos;
import com.catastro.Entities.CatPredioObraInterna;
import com.catastro.Entities.CatPredioS4;
import com.catastro.Entities.CatPredioS6;
import com.catastro.Entities.CatTipoConjunto;
import com.google.gson.annotations.Expose;
import com.origami.sigef.common.entities.Canton;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.Provincia;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import org.hibernate.annotations.Where;

/**
 *
 * @author ORIGAMI2
 */
@Entity
@Table(name = "cat_predio", schema = "catastro")
@NamedQueries({
    @NamedQuery(name = "CatPredio.findAll", query = "SELECT c FROM CatPredio c"),
    @NamedQuery(name = "CatPredio.findById", query = "SELECT c FROM CatPredio c WHERE c.id = ?1"),
    @NamedQuery(name = "CatPredio.findBySector", query = "SELECT c FROM CatPredio c WHERE c.sector = ?1"),
    @NamedQuery(name = "CatPredio.findByMz", query = "SELECT c FROM CatPredio c WHERE c.mz = :mz"),
    @NamedQuery(name = "CatPredio.findByCiudadela", query = "SELECT c FROM CatPredio c WHERE c.ciudadela = ?1"),
    @NamedQuery(name = "CatPredio.findByMzdiv", query = "SELECT c FROM CatPredio c WHERE c.mzdiv = ?1"),
    @NamedQuery(name = "CatPredio.findBySolar", query = "SELECT c FROM CatPredio c WHERE c.solar = ?1"),
    @NamedQuery(name = "CatPredio.findByNumPredio", query = "SELECT c FROM CatPredio c WHERE c.numPredio = ?1 AND c.tipoPredio = ?2 AND c.estado = 'A'"),
    @NamedQuery(name = "CatPredio.findByCodigoPredioAndTipo", query = "SELECT c FROM CatPredio c WHERE c.codigoPredio = ?1 AND c.tipoPredio = ?2 AND c.estado = 'A'"),
    @NamedQuery(name = "CatPredio.findByNumPredioAndTipo", query = "SELECT c FROM CatPredio c WHERE c.numPredio = ?1 AND c.tipoPredio = ?2 AND c.estado = 'A'"),
    @NamedQuery(name = "CatPredio.findByPredialant", query = "SELECT c FROM CatPredio c WHERE c.predialant = ?1 AND c.tipoPredio = ?2 AND c.estado = 'A'"),
    @NamedQuery(name = "CatPredio.findByClaveCat", query = "SELECT c FROM CatPredio c WHERE c.claveCat = ?1"),
    @NamedQuery(name = "CatPredio.findByClaveCatAndTipo", query = "SELECT c FROM CatPredio c WHERE c.claveCat = ?1 AND c.tipoPredio = ?2  AND c.estado = 'A'")})
public class CatPredio implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Long id;
    @Basic(optional = false)
    @Column(name = "sector", nullable = false)
    private Short sector;
    @Basic(optional = false)
    @Column(name = "mz", nullable = false)
    private Short mz;
    @JoinColumn(name = "ciudadela", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatCiudadela ciudadela;
    @Column(name = "mzdiv")
    private Short mzdiv;
    @Basic(optional = false)
    @Column(name = "solar", nullable = false)
    private Short solar;
    @JoinColumn(name = "tipo_conjunto", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatTipoConjunto tipoConjunto;
    @Size(max = 250)
    @Column(name = "nombre_urb", length = 250)
    private String nombreUrb;
    @Column(name = "urb_sec")
    private Short urbSec;
    @Column(name = "num_predio")
    private BigInteger numPredio;
    @Column(name = "tenencia")
    private BigInteger tenencia;
    @Column(name = "soporta_hipoteca")
    private Boolean soportaHipoteca;
    @Size(max = 100)
    @Column(name = "urb_mz", length = 100)
    private String urbMz;
    @Column(name = "numero_ficha")
    private BigInteger numeroFicha;
    @Basic(optional = false)
    @Column(name = "inst_creacion", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date instCreacion;
    @Column(name = "usuario_creador")
    private BigInteger usuarioCreador;
    @Column(name = "cdla")
    private Short cdla;
    @Size(max = 100)
    @Column(name = "urb_solarnew", length = 100)
    private String urbSolarnew;
    @Size(max = 50)
    @Column(name = "urb_secnew", length = 50)
    private String urbSecnew;
    @Size(max = 200)
    @Column(name = "nom_comp_pago", length = 200)
    private String nomCompPago;
    @Column(name = "propiedad_horizontal")
    private Boolean propiedadHorizontal;
    @Column(name = "predio_raiz")
    private BigInteger predioRaiz;
    @Size(max = 1)
    @Column(name = "estado", length = 1)
    private String estado;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "avaluo_solar", precision = 15, scale = 4)
    private BigDecimal avaluoSolar;
    @Column(name = "avaluo_construccion", precision = 15, scale = 4)
    private BigDecimal avaluoConstruccion;
    @Column(name = "avaluo_municipal", precision = 15, scale = 4)
    private BigDecimal avaluoMunicipal;
    @Size(max = 2147483647)
    @Column(name = "calle", length = 2147483647)
    private String calle;
    @JoinColumn(name = "forma_solar", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem formaSolar;
    @Column(name = "area_solar", precision = 12, scale = 5)
    private BigDecimal areaSolar;
    @JoinColumn(name = "topografia_solar", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem topografiaSolar;
    @Size(max = 50)
    @Column(name = "num_departamento", length = 50)
    private String numDepartamento;
    @Size(max = 150)
    @Column(name = "nombre_edificio", length = 150)
    private String nombreEdificio;
    @Size(max = 200)
    @Column(name = "division_urb", length = 200)
    private String divisionUrb;
    @Size(max = 50)
    @Column(name = "uso_ph", length = 50)
    private String usoPh;
    @Column(name = "num_pisos")
    private BigInteger numPisos;
    @Size(max = 150)
    @Column(name = "predial97", length = 150)
    private String predial97;
    @Size(max = 150)
    @Column(name = "predialant", length = 150)
    private String predialant;
    @Size(max = 5000)
    @Column(name = "observaciones", length = 5000)
    private String observaciones;
    @Column(name = "amri", precision = 12, scale = 2)
    private BigDecimal amri;
    @Size(max = 150)
    @Column(name = "zona_pu", length = 150)
    private String zonaPu;
    @Size(max = 2147483647)
    @Column(name = "calle_av", length = 2147483647)
    private String calleAv;
    @Size(max = 2147483647)
    @Column(name = "calle_s", length = 2147483647)
    private String calleS;
    @Size(max = 150)
    @Column(name = "numero_vivienda", length = 150)
    private String numeroVivienda;
    @Column(name = "alicuota_util", precision = 12, scale = 4)
    private BigDecimal alicuotaUtil;
    @Column(name = "uso_solar")
    private BigInteger usoSolar;
    @Size(max = 6)
    @Column(name = "cod_categoria", length = 6)
    private String codCategoria;
    @Column(name = "coordx", precision = 14, scale = 4)
    private BigDecimal coordx;
    @Column(name = "coordy", precision = 14, scale = 4)
    private BigDecimal coordy;
    @Column(name = "constructividad")
    private BigInteger constructividad;
    @Column(name = "fec_mod")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecMod;
    @Size(max = 50)
    @Column(name = "usr_mod", length = 50)
    private String usrMod;
    @Column(name = "revisado")
    private Boolean revisado;
    @Column(name = "subsector")
    private BigInteger subsector;
    @Size(max = 100)
    @Column(name = "clave_cat", length = 100)
    private String claveCat;
    @Column(name = "zona")
    private Short zona;
    @Basic(optional = false)
    @Column(name = "lote", nullable = false)
    private Short lote;
    @Basic(optional = false)
    @Column(name = "bloque", nullable = false)
    private Short bloque;
    @Basic(optional = false)
    @Column(name = "piso", nullable = false)
    private Short piso;
    @Basic(optional = false)
    @Column(name = "unidad", nullable = false)
    private Short unidad;
    @Column(name = "habitantes")
    private Integer habitantes;
    @Column(name = "nuevo")
    private Boolean nuevo;
    @Column(name = "procesados")
    private Boolean procesados;
    @Column(name = "alicuota_const", precision = 12, scale = 4)
    private BigDecimal alicuotaConst;
    @Column(name = "canton")
    private Short canton;
    @Column(name = "provincia")
    private Short provincia;
    @Size(max = 2147483647)
    @Column(name = "lind_inferior", length = 2147483647)
    private String lindInferior;
    @Size(max = 2147483647)
    @Column(name = "lind_superior", length = 2147483647)
    private String lindSuperior;
    @Column(name = "parroquia")
    private Short parroquia;
    @Size(max = 2147483647)
    @Column(name = "tipo_predio", length = 2147483647)
    private String tipoPredio;
    @Column(name = "alarmas_comunitarias")
    private Boolean alarmasComunitarias;
    @Column(name = "telefonia_satelital")
    private Boolean telefoniaSatelital;
    @Column(name = "area_cultivos", precision = 12, scale = 2)
    private BigDecimal areaCultivos;
    @Column(name = "area_declarada_const", precision = 12, scale = 2)
    private BigDecimal areaDeclaradaConst;
    @Column(name = "area_obras", precision = 12, scale = 2)
    private BigDecimal areaObras;
    @Column(name = "avaluo_cultivos", precision = 15, scale = 4)
    private BigDecimal avaluoCultivos;
    @Column(name = "area_const_ph", precision = 12, scale = 2)
    private BigDecimal areaConstPh;
    @Column(name = "tipo_via")
    private BigInteger tipoVia;
    @Column(name = "cobertura_predominante")
    private BigInteger coberturaPredominante;
    @Size(max = 40)
    @Column(name = "nombre_pueblo_etnia", length = 40)
    private String nombrePuebloEtnia;
    @Column(name = "unidad_medida")
    private BigInteger unidadMedida;
    @Column(name = "ente_horizontal")
    private BigInteger enteHorizontal;
    @Column(name = "clasif_horizontal")
    private BigInteger clasifHorizontal;
    @Column(name = "num_hogares")
    private Short numHogares;
    @Column(name = "num_habitaciones")
    private Short numHabitaciones;
    @Column(name = "num_dormitorios")
    private Short numDormitorios;
    @Column(name = "ocupacion_viv_horizontal")
    private Boolean ocupacionVivHorizontal;
    @Column(name = "tipo_vivienda_horizontal")
    private Boolean tipoViviendaHorizontal;
    @Column(name = "num_espacios_banios")
    private Short numEspaciosBanios;
    @Column(name = "num_celulares")
    private Short numCelulares;
    @Column(name = "otro_tipo_via")
    private BigInteger otroTipoVia;
    @Column(name = "responsable_actualizador_predial")
    private BigInteger responsableActualizadorPredial;
    @Column(name = "responsable_fiscalizador_predial")
    private BigInteger responsableFiscalizadorPredial;
    @JoinColumn(name = "clasificacion_suelo", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem clasificacionSuelo;
    @Column(name = "requiere_perfeccionamiento")
    private Boolean requierePerfeccionamiento;
    @Column(name = "anios_sin_perfeccionamiento")
    private Integer aniosSinPerfeccionamiento;
    @Column(name = "anios_posesion")
    private Integer aniosPosesion;
    @Column(name = "tenencia_vivienda")
    private BigInteger tenenciaVivienda;
    @Column(name = "ficha_madre")
    private Boolean fichaMadre;
    @Size(max = 500)
    @Column(name = "admin_nombres_apellidos", length = 500)
    private String adminNombresApellidos;
    @Size(max = 20)
    @Column(name = "admin_cedula", length = 20)
    private String adminCedula;
    @Size(max = 2147483647)
    @Column(name = "admin_telefono", length = 2147483647)
    private String adminTelefono;
    @Size(max = 2147483647)
    @Column(name = "admin_celular", length = 2147483647)
    private String adminCelular;
    @Column(name = "informante")
    private BigInteger informante;
    @Column(name = "tipo_poseedor")
    private BigInteger tipoPoseedor;
    @Column(name = "cant_alicuotas")
    private Integer cantAlicuotas;
    @Column(name = "componente_dpto")
    private Boolean componenteDpto;
    @Column(name = "componente_bodegas")
    private Boolean componenteBodegas;
    @Column(name = "componente_parqueos")
    private Boolean componenteParqueos;
    @Column(name = "tiene_escritura")
    private Boolean tieneEscritura;
    @Column(name = "componente_bodegas_area", precision = 19, scale = 2)
    private BigDecimal componenteBodegasArea;
    @Column(name = "componente_dpto_area", precision = 19, scale = 2)
    private BigDecimal componenteDptoArea;
    @Size(max = 200)
    @Column(name = "componente_bodegas_numero", length = 200)
    private String componenteBodegasNumero;
    @Size(max = 200)
    @Column(name = "componente_parqueos_numero", length = 200)
    private String componenteParqueosNumero;
    @Column(name = "componente_parqueos_area", precision = 19, scale = 2)
    private BigDecimal componenteParqueosArea;
    @Column(name = "parroquia_anterior")
    private Short parroquiaAnterior;
    @Column(name = "alicuota_terreno", precision = 12, scale = 2)
    private BigDecimal alicuotaTerreno;
    @Size(max = 2147483647)
    @Column(name = "apellidos_informante", length = 2147483647)
    private String apellidosInformante;
    @Column(name = "area_aumento_cons", precision = 12, scale = 2)
    private BigDecimal areaAumentoCons;
    @Column(name = "area_terreno_alicuota", precision = 12, scale = 2)
    private BigDecimal areaTerrenoAlicuota;
    @Column(name = "area_total_cons", precision = 12, scale = 2)
    private BigDecimal areaTotalCons;
    @Column(name = "avaluo_obra_complement", precision = 15, scale = 4)
    private BigDecimal avaluoObraComplement;
    @Column(name = "avaluo_plu_obra_complement", precision = 15, scale = 4)
    private BigDecimal avaluoPluObraComplement;
    @Column(name = "avaluo_plusconstruccion", precision = 15, scale = 4)
    private BigDecimal avaluoPlusconstruccion;
    @Column(name = "avaluo_pluscultivos", precision = 15, scale = 4)
    private BigDecimal avaluoPluscultivos;
    @Column(name = "avaluo_plusmunicipal", precision = 15, scale = 4)
    private BigDecimal avaluoPlusmunicipal;
    @Column(name = "avaluo_plussolar", precision = 15, scale = 4)
    private BigDecimal avaluoPlussolar;
    @Column(name = "base_imponible", precision = 15, scale = 4)
    private BigDecimal baseImponible;
    @Size(max = 2147483647)
    @Column(name = "cedula_prop_anterior", length = 2147483647)
    private String cedulaPropAnterior;
    @Size(max = 2147483647)
    @Column(name = "ci_ruc_informante", length = 2147483647)
    private String ciRucInformante;
    @Size(max = 2147483647)
    @Column(name = "ciu_actualizador", length = 2147483647)
    private String ciuActualizador;
    @Size(max = 2147483647)
    @Column(name = "ciu_fiscalizador", length = 2147483647)
    private String ciuFiscalizador;
    @Size(max = 255)
    @Column(name = "ciu_horizontal", length = 255)
    private String ciuHorizontal;
    @Size(max = 255)
    @Column(name = "ciu_informante", length = 255)
    private String ciuInformante;
    @Size(max = 255)
    @Column(name = "ciu_nombre_actualizador", length = 255)
    private String ciuNombreActualizador;
    @Size(max = 255)
    @Column(name = "ciu_nombre_fiscalizador", length = 255)
    private String ciuNombreFiscalizador;
    @Size(max = 255)
    @Column(name = "ciu_nombre_horizontal", length = 255)
    private String ciuNombreHorizontal;
    @Size(max = 255)
    @Column(name = "ciu_nombre_informante", length = 255)
    private String ciuNombreInformante;
    @Size(max = 255)
    @Column(name = "nombre_informante", length = 255)
    private String nombreInformante;
    @Size(max = 100)
    @Column(name = "predialant_ant", length = 100)
    private String predialantAnt;
    @Size(max = 255)
    @Column(name = "prop_anterior_predio", length = 255)
    private String propAnteriorPredio;
    @Column(name = "viv_cencal_posee_telf_convencional")
    private Boolean vivCencalPoseeTelfConvencional;
    @Column(name = "viv_cencal_serv_internet")
    private Boolean vivCencalServInternet;
    @JoinColumn(name = "clasificacion_vivienda", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem clasificacionVivienda;
    @JoinColumn(name = "condicion_vivienda", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem condicionVivienda;
    @JoinColumn(name = "forma_adquisicion", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem formaAdquisicion;
    @JoinColumn(name = "tipo_vivienda", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem tipoVivienda;
    @JoinColumn(name = "uso_via", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem usoVia;
    @JoinColumn(name = "viv_cencal_acabado_piso", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem vivCencalAcabadoPiso;
    @JoinColumn(name = "viv_cencal_estado_acabado_piso", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem vivCencalEstadoAcabadoPiso;
    @Column(name = "fecha_actualizador")
    @Temporal(TemporalType.DATE)
    private Date fechaActualizador;
    @Column(name = "fecha_fiscalizador")
    @Temporal(TemporalType.DATE)
    private Date fechaFiscalizador;
    @Size(max = 8000)
    @Column(name = "firma_actualizador", length = 8000)
    private String firmaActualizador;
    @Size(max = 8000)
    @Column(name = "firma_fiscalizador", length = 8000)
    private String firmaFiscalizador;
    @Column(name = "costo_directo", precision = 19, scale = 2)
    private BigDecimal costoDirecto;
    @Column(name = "cambio_nombre")
    private Boolean cambioNombre;
    @Size(max = 4000)
    @Column(name = "nombre_cambiado", length = 4000)
    private String nombreCambiado;
    @Size(max = 255)
    @Column(name = "nombre_predio", length = 255)
    private String nombrePredio;
    @Column(name = "predialante_migrado")
    private Boolean predialanteMigrado;
    @Column(name = "valor_m2", precision = 12, scale = 2)
    private BigDecimal valorM2;
    @Column(name = "area_grafica", precision = 12, scale = 2)
    private BigDecimal areaGrafica;
    @Column(name = "esta_dibujado")
    private Boolean estaDibujado;
    @Column(name = "es_avaluo_especial")
    private Boolean esAvaluoEspecial;
    @Column(name = "valor_base_m2", precision = 12, scale = 2)
    private BigDecimal valorBaseM2;
    @Column(name = "num_predio_respaldo")
    private BigInteger numPredioRespaldo;
    @Column(name = "es_calculo_cuantia")
    private Boolean esCalculoCuantia;
    @Size(max = 30)
    @Column(name = "predialant_test", length = 30)
    private String predialantTest;
    @Column(name = "es_avaluo_verificado")
    private Boolean esAvaluoVerificado;
    @Size(max = 50)
    @Column(name = "mac_addres_usuario_ingreso", length = 50)
    private String macAddresUsuarioIngreso;
    @Size(max = 50)
    @Column(name = "ip_user_session", length = 50)
    private String ipUserSession;
    @Column(name = "ubicacion_manzana")
    private BigInteger ubicacionManzana;
    @Column(name = "es_tributario")
    private Boolean esTributario;
    @Column(name = "esta_exonerado")
    private Boolean estaExonerado;
    @Size(max = 2147483647)
    @Column(name = "admin_correo", length = 2147483647)
    private String adminCorreo;
    @Size(max = 2147483647)
    @Column(name = "admin_direccion", length = 2147483647)
    private String adminDireccion;
    @Size(max = 2147483647)
    @Column(name = "cedula_posesionario", length = 2147483647)
    private String cedulaPosesionario;
    @Size(max = 255)
    @Column(name = "codigo_inst_nac_pat_cul", length = 255)
    private String codigoInstNacPatCul;
    @Column(name = "condicion_municipal_anio")
    private Integer condicionMunicipalAnio;
    @Size(max = 255)
    @Column(name = "nombre_posesionario", length = 255)
    private String nombrePosesionario;
    @Column(name = "precio_oferta", precision = 19, scale = 2)
    private BigDecimal precioOferta;
    @Column(name = "tiene_aumentoph")
    private Boolean tieneAumentoph;
    @JoinColumn(name = "admin_canton", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Canton adminCanton;
    @Column(name = "admin_parroquia")
    private BigInteger adminParroquia;
    @Column(name = "ciu_tipo_identificacion")
    private BigInteger ciuTipoIdentificacion;
    @Column(name = "condicion_municipal")
    private BigInteger condicionMunicipal;
    @JoinColumn(name = "notif_tipo_direccion", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem notifTipoDireccion;
    @JoinColumn(name = "propiedad", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem propiedad;
    @Size(max = 2147483647)
    @Column(name = "codigo_predio")
    private String codigoPredio;
    @Column(name = "bloque_phh")
    private Short phh;
    @Column(name = "bloque_phv")
    private Short phv;
    @Column(name = "div1")
    private BigInteger div1;
    @Column(name = "div2")
    private BigInteger div2;
    @Column(name = "div3")
    private BigInteger div3;
    @Column(name = "div4")
    private BigInteger div4;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "predio")
    private List<CatPredioPropietario> catPredioPropietarioList;
    @OneToOne(mappedBy = "predio", fetch = FetchType.LAZY)
    //@ReportField(description = "Caracteristicas", type = FieldType.COLLECTION_ONE_TO_ONE)
    private CatPredioS4 catPredioS4;
    @OneToOne(mappedBy = "predio", fetch = FetchType.LAZY)
    @Expose
    //@ReportField(description = "Servicios", type = FieldType.COLLECTION_ONE_TO_ONE)
    private CatPredioS6 catPredioS6;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "predio", fetch = FetchType.LAZY)
    @Where(clause = "estado='A'")
    @Expose
    private List<CatPredioEdificacion> catPredioEdificacionCollection;
    @Where(clause = "estado = 'A'")
    @OneToMany(mappedBy = "predio", fetch = FetchType.LAZY)
    @Expose
    private List<CatPredioObraInterna> catPredioObraInternaCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "predio", fetch = FetchType.LAZY)
    @Where(clause = "estado = 'A'")
    //@ReportField(description = "Colindantes", type = FieldType.COLLECTION_ONE_TO_MANY)
    private List<CatPredioLinderos> predioCollection;
    @OneToMany(mappedBy = "predio", fetch = FetchType.LAZY)
    @Where(clause = "estado = 'True'")
    private List<CatPredioClasificRural> catPredioClasificRuralCollection;
    @OneToMany(mappedBy = "predio", fetch = FetchType.LAZY)
    @Expose
    private List<CatEscritura> catEscrituraCollection;
    @Where(clause = "estado = 'A'")
    @OneToMany(mappedBy = "predio", fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @Expose
    //@ReportField(description = "Propietarios", type = FieldType.COLLECTION_ONE_TO_MANY)
    private Collection<CatPredioPropietario> catPredioPropietarioCollection;
    @Transient
    private Provincia provinciaNot;
    @Transient
    private String nombrePropietarios;
    @Column(name = "es_activo")
    private String esActivo;
    @Column(name = "cem_2008")
    private BigInteger calculoCem0403;
    @Column(name = "tarifa_impositiva")
    private BigDecimal tarifaImpositiva;

    public CatPredio() {
    }

    public CatPredio(Long id) {
        this.id = id;
    }

    public CatPredio(Long id, Short sector, Short mz, Short solar,
            Date instCreacion, Short lote, Short bloque, Short piso, Short unidad) {
        this.id = id;
        this.sector = sector;//valor
        this.mz = mz;
        this.solar = solar;
        this.instCreacion = instCreacion;
        this.lote = lote;
        this.bloque = bloque;
        this.piso = piso;
        this.unidad = unidad;
    }

    public CatPredio(Long id, String claveCatastral, String tipoPredio) {
        this.id = id;
        this.claveCat = claveCatastral;
        this.tipoPredio = tipoPredio;
    }

    public String getNombrePropietarios() {
        String nombres = "";
        StringBuilder sb = new StringBuilder();

        if (this.catPredioPropietarioList != null && !this.catPredioPropietarioList.isEmpty()) {
            for (CatPredioPropietario cpp : catPredioPropietarioList) {
                if (cpp.getEnte() != null) {
                    if (cpp.getEnte().getEstado()) {
                        nombres = cpp.getEnte().getNombreCompleltoSql();
                    } else {
                        nombres = cpp.getNombresCompletos();
                    }
                    sb.append(nombres).append(" - ");
                } else {
                    if (cpp.getNombresCompletos() != null) {
                        nombres = cpp.getNombresCompletos();
                    }
                }
            }
        }
        if (sb.length() >= 3) {
            sb.delete(sb.length() - 3, sb.length() - 1);
        }
        return sb.toString().toUpperCase();
    }

    public String getDireccion() {

        return (this.ciudadela == null ? "" : this.ciudadela.getNombre())
                + (calle == null ? "" : " Calle principal: " + calle)
                + (calleS == null ? "" : " Calle Secudaria: " + calleS);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Short getSector() {
        return sector;
    }

    public void setSector(Short sector) {
        this.sector = sector;
    }

    public Short getMz() {
        return mz;
    }

    public void setMz(Short mz) {
        this.mz = mz;
    }

    public CatCiudadela getCiudadela() {
        return ciudadela;
    }

    public void setCiudadela(CatCiudadela ciudadela) {
        this.ciudadela = ciudadela;
    }

    public Short getMzdiv() {
        return mzdiv;
    }

    public void setMzdiv(Short mzdiv) {
        this.mzdiv = mzdiv;
    }

    public Short getSolar() {
        return solar;
    }

    public void setSolar(Short solar) {
        this.solar = solar;
    }

    public CatTipoConjunto getTipoConjunto() {
        return tipoConjunto;
    }

    public void setTipoConjunto(CatTipoConjunto tipoConjunto) {
        this.tipoConjunto = tipoConjunto;
    }

    public String getNombreUrb() {
        return nombreUrb;
    }

    public void setNombreUrb(String nombreUrb) {
        this.nombreUrb = nombreUrb;
    }

    public Short getUrbSec() {
        return urbSec;
    }

    public void setUrbSec(Short urbSec) {
        this.urbSec = urbSec;
    }

    public BigInteger getNumPredio() {
        return numPredio;
    }

    public void setNumPredio(BigInteger numPredio) {
        this.numPredio = numPredio;
    }

    public BigInteger getTenencia() {
        return tenencia;
    }

    public void setTenencia(BigInteger tenencia) {
        this.tenencia = tenencia;
    }

    public Boolean getSoportaHipoteca() {
        return soportaHipoteca;
    }

    public void setSoportaHipoteca(Boolean soportaHipoteca) {
        this.soportaHipoteca = soportaHipoteca;
    }

    public String getUrbMz() {
        return urbMz;
    }

    public void setUrbMz(String urbMz) {
        this.urbMz = urbMz;
    }

    public BigInteger getNumeroFicha() {
        return numeroFicha;
    }

    public void setNumeroFicha(BigInteger numeroFicha) {
        this.numeroFicha = numeroFicha;
    }

    public Date getInstCreacion() {
        return instCreacion;
    }

    public void setInstCreacion(Date instCreacion) {
        this.instCreacion = instCreacion;
    }

    public BigInteger getUsuarioCreador() {
        return usuarioCreador;
    }

    public void setUsuarioCreador(BigInteger usuarioCreador) {
        this.usuarioCreador = usuarioCreador;
    }

    public Short getCdla() {
        return cdla;
    }

    public void setCdla(Short cdla) {
        this.cdla = cdla;
    }

    public String getUrbSolarnew() {
        return urbSolarnew;
    }

    public void setUrbSolarnew(String urbSolarnew) {
        this.urbSolarnew = urbSolarnew;
    }

    public String getUrbSecnew() {
        return urbSecnew;
    }

    public void setUrbSecnew(String urbSecnew) {
        this.urbSecnew = urbSecnew;
    }

    public String getNomCompPago() {
        return nomCompPago;
    }

    public void setNomCompPago(String nomCompPago) {
        this.nomCompPago = nomCompPago;
    }

    public Boolean getPropiedadHorizontal() {
        return propiedadHorizontal;
    }

    public void setPropiedadHorizontal(Boolean propiedadHorizontal) {
        this.propiedadHorizontal = propiedadHorizontal;
    }

    public BigInteger getPredioRaiz() {
        return predioRaiz;
    }

    public void setPredioRaiz(BigInteger predioRaiz) {
        this.predioRaiz = predioRaiz;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public BigDecimal getAvaluoSolar() {
        return avaluoSolar;
    }

    public void setAvaluoSolar(BigDecimal avaluoSolar) {
        this.avaluoSolar = avaluoSolar;
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

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public CatalogoItem getFormaSolar() {
        return formaSolar;
    }

    public void setFormaSolar(CatalogoItem formaSolar) {
        this.formaSolar = formaSolar;
    }

    public BigDecimal getAreaSolar() {
        return areaSolar;
    }

    public void setAreaSolar(BigDecimal areaSolar) {
        this.areaSolar = areaSolar;
    }

    public String getNumDepartamento() {
        return numDepartamento;
    }

    public void setNumDepartamento(String numDepartamento) {
        this.numDepartamento = numDepartamento;
    }

    public String getNombreEdificio() {
        return nombreEdificio;
    }

    public void setNombreEdificio(String nombreEdificio) {
        this.nombreEdificio = nombreEdificio;
    }

    public String getDivisionUrb() {
        return divisionUrb;
    }

    public void setDivisionUrb(String divisionUrb) {
        this.divisionUrb = divisionUrb;
    }

    public String getUsoPh() {
        return usoPh;
    }

    public void setUsoPh(String usoPh) {
        this.usoPh = usoPh;
    }

    public BigInteger getNumPisos() {
        return numPisos;
    }

    public void setNumPisos(BigInteger numPisos) {
        this.numPisos = numPisos;
    }

    public String getPredial97() {
        return predial97;
    }

    public void setPredial97(String predial97) {
        this.predial97 = predial97;
    }

    public String getPredialant() {
        return predialant;
    }

    public void setPredialant(String predialant) {
        this.predialant = predialant;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public BigDecimal getAmri() {
        return amri;
    }

    public void setAmri(BigDecimal amri) {
        this.amri = amri;
    }

    public String getZonaPu() {
        return zonaPu;
    }

    public void setZonaPu(String zonaPu) {
        this.zonaPu = zonaPu;
    }

    public String getCalleAv() {
        return calleAv;
    }

    public void setCalleAv(String calleAv) {
        this.calleAv = calleAv;
    }

    public String getCalleS() {
        return calleS;
    }

    public void setCalleS(String calleS) {
        this.calleS = calleS;
    }

    public String getNumeroVivienda() {
        return numeroVivienda;
    }

    public void setNumeroVivienda(String numeroVivienda) {
        this.numeroVivienda = numeroVivienda;
    }

    public BigDecimal getAlicuotaUtil() {
        return alicuotaUtil;
    }

    public void setAlicuotaUtil(BigDecimal alicuotaUtil) {
        this.alicuotaUtil = alicuotaUtil;
    }

    public BigInteger getUsoSolar() {
        return usoSolar;
    }

    public void setUsoSolar(BigInteger usoSolar) {
        this.usoSolar = usoSolar;
    }

    public String getCodCategoria() {
        return codCategoria;
    }

    public void setCodCategoria(String codCategoria) {
        this.codCategoria = codCategoria;
    }

    public BigDecimal getCoordx() {
        return coordx;
    }

    public void setCoordx(BigDecimal coordx) {
        this.coordx = coordx;
    }

    public BigDecimal getCoordy() {
        return coordy;
    }

    public void setCoordy(BigDecimal coordy) {
        this.coordy = coordy;
    }

    public BigInteger getConstructividad() {
        return constructividad;
    }

    public void setConstructividad(BigInteger constructividad) {
        this.constructividad = constructividad;
    }

    public Date getFecMod() {
        return fecMod;
    }

    public void setFecMod(Date fecMod) {
        this.fecMod = fecMod;
    }

    public String getUsrMod() {
        return usrMod;
    }

    public void setUsrMod(String usrMod) {
        this.usrMod = usrMod;
    }

    public Boolean getRevisado() {
        return revisado;
    }

    public void setRevisado(Boolean revisado) {
        this.revisado = revisado;
    }

    public BigInteger getSubsector() {
        return subsector;
    }

    public void setSubsector(BigInteger subsector) {
        this.subsector = subsector;
    }

    public String getClaveCat() {
        return claveCat;
    }

    public void setClaveCat(String claveCat) {
        this.claveCat = claveCat;
    }

    public Short getZona() {
        return zona;
    }

    public void setZona(Short zona) {
        this.zona = zona;
    }

    public Short getLote() {
        return lote;
    }

    public void setLote(Short lote) {
        this.lote = lote;
    }

    public Short getBloque() {
        return bloque;
    }

    public void setBloque(Short bloque) {
        this.bloque = bloque;
    }

    public Short getPiso() {
        return piso;
    }

    public void setPiso(Short piso) {
        this.piso = piso;
    }

    public Short getUnidad() {
        return unidad;
    }

    public void setUnidad(Short unidad) {
        this.unidad = unidad;
    }

    public Integer getHabitantes() {
        return habitantes;
    }

    public void setHabitantes(Integer habitantes) {
        this.habitantes = habitantes;
    }

    public Boolean getNuevo() {
        return nuevo;
    }

    public void setNuevo(Boolean nuevo) {
        this.nuevo = nuevo;
    }

    public Boolean getProcesados() {
        return procesados;
    }

    public void setProcesados(Boolean procesados) {
        this.procesados = procesados;
    }

    public BigDecimal getAlicuotaConst() {
        return alicuotaConst;
    }

    public void setAlicuotaConst(BigDecimal alicuotaConst) {
        this.alicuotaConst = alicuotaConst;
    }

    public Short getCanton() {
        return canton;
    }

    public void setCanton(Short canton) {
        this.canton = canton;
    }

    public Short getProvincia() {
        return provincia;
    }

    public void setProvincia(Short provincia) {
        this.provincia = provincia;
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

    public Short getParroquia() {
        return parroquia;
    }

    public void setParroquia(Short parroquia) {
        this.parroquia = parroquia;
    }

    public String getTipoPredio() {
        return tipoPredio;
    }

    public void setTipoPredio(String tipoPredio) {
        this.tipoPredio = tipoPredio;
    }

    public Boolean getAlarmasComunitarias() {
        return alarmasComunitarias;
    }

    public void setAlarmasComunitarias(Boolean alarmasComunitarias) {
        this.alarmasComunitarias = alarmasComunitarias;
    }

    public Boolean getTelefoniaSatelital() {
        return telefoniaSatelital;
    }

    public void setTelefoniaSatelital(Boolean telefoniaSatelital) {
        this.telefoniaSatelital = telefoniaSatelital;
    }

    public BigDecimal getAreaCultivos() {
        return areaCultivos;
    }

    public void setAreaCultivos(BigDecimal areaCultivos) {
        this.areaCultivos = areaCultivos;
    }

    public BigDecimal getAreaDeclaradaConst() {
        return areaDeclaradaConst;
    }

    public void setAreaDeclaradaConst(BigDecimal areaDeclaradaConst) {
        this.areaDeclaradaConst = areaDeclaradaConst;
    }

    public BigDecimal getAreaObras() {
        return areaObras;
    }

    public void setAreaObras(BigDecimal areaObras) {
        this.areaObras = areaObras;
    }

    public BigDecimal getAvaluoCultivos() {
        return avaluoCultivos;
    }

    public void setAvaluoCultivos(BigDecimal avaluoCultivos) {
        this.avaluoCultivos = avaluoCultivos;
    }

    public BigDecimal getAreaConstPh() {
        return areaConstPh;
    }

    public void setAreaConstPh(BigDecimal areaConstPh) {
        this.areaConstPh = areaConstPh;
    }

    public BigInteger getTipoVia() {
        return tipoVia;
    }

    public void setTipoVia(BigInteger tipoVia) {
        this.tipoVia = tipoVia;
    }

    public BigInteger getCoberturaPredominante() {
        return coberturaPredominante;
    }

    public void setCoberturaPredominante(BigInteger coberturaPredominante) {
        this.coberturaPredominante = coberturaPredominante;
    }

    public String getNombrePuebloEtnia() {
        return nombrePuebloEtnia;
    }

    public void setNombrePuebloEtnia(String nombrePuebloEtnia) {
        this.nombrePuebloEtnia = nombrePuebloEtnia;
    }

    public BigInteger getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(BigInteger unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    public BigInteger getEnteHorizontal() {
        return enteHorizontal;
    }

    public void setEnteHorizontal(BigInteger enteHorizontal) {
        this.enteHorizontal = enteHorizontal;
    }

    public BigInteger getClasifHorizontal() {
        return clasifHorizontal;
    }

    public void setClasifHorizontal(BigInteger clasifHorizontal) {
        this.clasifHorizontal = clasifHorizontal;
    }

    public Short getNumHogares() {
        return numHogares;
    }

    public void setNumHogares(Short numHogares) {
        this.numHogares = numHogares;
    }

    public Short getNumHabitaciones() {
        return numHabitaciones;
    }

    public void setNumHabitaciones(Short numHabitaciones) {
        this.numHabitaciones = numHabitaciones;
    }

    public Short getNumDormitorios() {
        return numDormitorios;
    }

    public void setNumDormitorios(Short numDormitorios) {
        this.numDormitorios = numDormitorios;
    }

    public Boolean getOcupacionVivHorizontal() {
        return ocupacionVivHorizontal;
    }

    public void setOcupacionVivHorizontal(Boolean ocupacionVivHorizontal) {
        this.ocupacionVivHorizontal = ocupacionVivHorizontal;
    }

    public Boolean getTipoViviendaHorizontal() {
        return tipoViviendaHorizontal;
    }

    public void setTipoViviendaHorizontal(Boolean tipoViviendaHorizontal) {
        this.tipoViviendaHorizontal = tipoViviendaHorizontal;
    }

    public Short getNumEspaciosBanios() {
        return numEspaciosBanios;
    }

    public void setNumEspaciosBanios(Short numEspaciosBanios) {
        this.numEspaciosBanios = numEspaciosBanios;
    }

    public Short getNumCelulares() {
        return numCelulares;
    }

    public void setNumCelulares(Short numCelulares) {
        this.numCelulares = numCelulares;
    }

    public BigInteger getOtroTipoVia() {
        return otroTipoVia;
    }

    public void setOtroTipoVia(BigInteger otroTipoVia) {
        this.otroTipoVia = otroTipoVia;
    }

    public BigInteger getResponsableActualizadorPredial() {
        return responsableActualizadorPredial;
    }

    public void setResponsableActualizadorPredial(BigInteger responsableActualizadorPredial) {
        this.responsableActualizadorPredial = responsableActualizadorPredial;
    }

    public BigInteger getResponsableFiscalizadorPredial() {
        return responsableFiscalizadorPredial;
    }

    public void setResponsableFiscalizadorPredial(BigInteger responsableFiscalizadorPredial) {
        this.responsableFiscalizadorPredial = responsableFiscalizadorPredial;
    }

    public CatalogoItem getClasificacionSuelo() {
        return clasificacionSuelo;
    }

    public void setClasificacionSuelo(CatalogoItem clasificacionSuelo) {
        this.clasificacionSuelo = clasificacionSuelo;
    }

    public Boolean getRequierePerfeccionamiento() {
        return requierePerfeccionamiento;
    }

    public void setRequierePerfeccionamiento(Boolean requierePerfeccionamiento) {
        this.requierePerfeccionamiento = requierePerfeccionamiento;
    }

    public Integer getAniosSinPerfeccionamiento() {
        return aniosSinPerfeccionamiento;
    }

    public void setAniosSinPerfeccionamiento(Integer aniosSinPerfeccionamiento) {
        this.aniosSinPerfeccionamiento = aniosSinPerfeccionamiento;
    }

    public Integer getAniosPosesion() {
        return aniosPosesion;
    }

    public void setAniosPosesion(Integer aniosPosesion) {
        this.aniosPosesion = aniosPosesion;
    }

    public BigInteger getTenenciaVivienda() {
        return tenenciaVivienda;
    }

    public void setTenenciaVivienda(BigInteger tenenciaVivienda) {
        this.tenenciaVivienda = tenenciaVivienda;
    }

    public Boolean getFichaMadre() {
        return fichaMadre;
    }

    public void setFichaMadre(Boolean fichaMadre) {
        this.fichaMadre = fichaMadre;
    }

    public String getAdminNombresApellidos() {
        return adminNombresApellidos;
    }

    public void setAdminNombresApellidos(String adminNombresApellidos) {
        this.adminNombresApellidos = adminNombresApellidos;
    }

    public String getAdminCedula() {
        return adminCedula;
    }

    public void setAdminCedula(String adminCedula) {
        this.adminCedula = adminCedula;
    }

    public String getAdminTelefono() {
        return adminTelefono;
    }

    public void setAdminTelefono(String adminTelefono) {
        this.adminTelefono = adminTelefono;
    }

    public String getAdminCelular() {
        return adminCelular;
    }

    public void setAdminCelular(String adminCelular) {
        this.adminCelular = adminCelular;
    }

    public BigInteger getInformante() {
        return informante;
    }

    public void setInformante(BigInteger informante) {
        this.informante = informante;
    }

    public BigInteger getTipoPoseedor() {
        return tipoPoseedor;
    }

    public void setTipoPoseedor(BigInteger tipoPoseedor) {
        this.tipoPoseedor = tipoPoseedor;
    }

    public Integer getCantAlicuotas() {
        return cantAlicuotas;
    }

    public void setCantAlicuotas(Integer cantAlicuotas) {
        this.cantAlicuotas = cantAlicuotas;
    }

    public Boolean getComponenteDpto() {
        return componenteDpto;
    }

    public void setComponenteDpto(Boolean componenteDpto) {
        this.componenteDpto = componenteDpto;
    }

    public Boolean getComponenteBodegas() {
        return componenteBodegas;
    }

    public void setComponenteBodegas(Boolean componenteBodegas) {
        this.componenteBodegas = componenteBodegas;
    }

    public Boolean getComponenteParqueos() {
        return componenteParqueos;
    }

    public void setComponenteParqueos(Boolean componenteParqueos) {
        this.componenteParqueos = componenteParqueos;
    }

    public Boolean getTieneEscritura() {
        return tieneEscritura;
    }

    public void setTieneEscritura(Boolean tieneEscritura) {
        this.tieneEscritura = tieneEscritura;
    }

    public BigDecimal getComponenteBodegasArea() {
        return componenteBodegasArea;
    }

    public void setComponenteBodegasArea(BigDecimal componenteBodegasArea) {
        this.componenteBodegasArea = componenteBodegasArea;
    }

    public BigDecimal getComponenteDptoArea() {
        return componenteDptoArea;
    }

    public void setComponenteDptoArea(BigDecimal componenteDptoArea) {
        this.componenteDptoArea = componenteDptoArea;
    }

    public String getComponenteBodegasNumero() {
        return componenteBodegasNumero;
    }

    public void setComponenteBodegasNumero(String componenteBodegasNumero) {
        this.componenteBodegasNumero = componenteBodegasNumero;
    }

    public String getComponenteParqueosNumero() {
        return componenteParqueosNumero;
    }

    public void setComponenteParqueosNumero(String componenteParqueosNumero) {
        this.componenteParqueosNumero = componenteParqueosNumero;
    }

    public BigDecimal getComponenteParqueosArea() {
        return componenteParqueosArea;
    }

    public void setComponenteParqueosArea(BigDecimal componenteParqueosArea) {
        this.componenteParqueosArea = componenteParqueosArea;
    }

    public Short getParroquiaAnterior() {
        return parroquiaAnterior;
    }

    public void setParroquiaAnterior(Short parroquiaAnterior) {
        this.parroquiaAnterior = parroquiaAnterior;
    }

    public BigDecimal getAlicuotaTerreno() {
        return alicuotaTerreno;
    }

    public void setAlicuotaTerreno(BigDecimal alicuotaTerreno) {
        this.alicuotaTerreno = alicuotaTerreno;
    }

    public String getApellidosInformante() {
        return apellidosInformante;
    }

    public void setApellidosInformante(String apellidosInformante) {
        this.apellidosInformante = apellidosInformante;
    }

    public BigDecimal getAreaAumentoCons() {
        return areaAumentoCons;
    }

    public void setAreaAumentoCons(BigDecimal areaAumentoCons) {
        this.areaAumentoCons = areaAumentoCons;
    }

    public BigDecimal getAreaTerrenoAlicuota() {
        return areaTerrenoAlicuota;
    }

    public void setAreaTerrenoAlicuota(BigDecimal areaTerrenoAlicuota) {
        this.areaTerrenoAlicuota = areaTerrenoAlicuota;
    }

    public BigDecimal getAreaTotalCons() {
        return areaTotalCons;
    }

    public void setAreaTotalCons(BigDecimal areaTotalCons) {
        this.areaTotalCons = areaTotalCons;
    }

    public BigDecimal getAvaluoObraComplement() {
        return avaluoObraComplement;
    }

    public void setAvaluoObraComplement(BigDecimal avaluoObraComplement) {
        this.avaluoObraComplement = avaluoObraComplement;
    }

    public BigDecimal getAvaluoPluObraComplement() {
        return avaluoPluObraComplement;
    }

    public void setAvaluoPluObraComplement(BigDecimal avaluoPluObraComplement) {
        this.avaluoPluObraComplement = avaluoPluObraComplement;
    }

    public BigDecimal getAvaluoPlusconstruccion() {
        return avaluoPlusconstruccion;
    }

    public void setAvaluoPlusconstruccion(BigDecimal avaluoPlusconstruccion) {
        this.avaluoPlusconstruccion = avaluoPlusconstruccion;
    }

    public BigDecimal getAvaluoPluscultivos() {
        return avaluoPluscultivos;
    }

    public void setAvaluoPluscultivos(BigDecimal avaluoPluscultivos) {
        this.avaluoPluscultivos = avaluoPluscultivos;
    }

    public BigDecimal getAvaluoPlusmunicipal() {
        return avaluoPlusmunicipal;
    }

    public void setAvaluoPlusmunicipal(BigDecimal avaluoPlusmunicipal) {
        this.avaluoPlusmunicipal = avaluoPlusmunicipal;
    }

    public BigDecimal getAvaluoPlussolar() {
        return avaluoPlussolar;
    }

    public void setAvaluoPlussolar(BigDecimal avaluoPlussolar) {
        this.avaluoPlussolar = avaluoPlussolar;
    }

    public BigDecimal getBaseImponible() {
        return baseImponible;
    }

    public void setBaseImponible(BigDecimal baseImponible) {
        this.baseImponible = baseImponible;
    }

    public String getCedulaPropAnterior() {
        return cedulaPropAnterior;
    }

    public void setCedulaPropAnterior(String cedulaPropAnterior) {
        this.cedulaPropAnterior = cedulaPropAnterior;
    }

    public String getCiRucInformante() {
        return ciRucInformante;
    }

    public void setCiRucInformante(String ciRucInformante) {
        this.ciRucInformante = ciRucInformante;
    }

    public String getCiuActualizador() {
        return ciuActualizador;
    }

    public void setCiuActualizador(String ciuActualizador) {
        this.ciuActualizador = ciuActualizador;
    }

    public String getCiuFiscalizador() {
        return ciuFiscalizador;
    }

    public void setCiuFiscalizador(String ciuFiscalizador) {
        this.ciuFiscalizador = ciuFiscalizador;
    }

    public String getCiuHorizontal() {
        return ciuHorizontal;
    }

    public void setCiuHorizontal(String ciuHorizontal) {
        this.ciuHorizontal = ciuHorizontal;
    }

    public String getCiuInformante() {
        return ciuInformante;
    }

    public void setCiuInformante(String ciuInformante) {
        this.ciuInformante = ciuInformante;
    }

    public String getCiuNombreActualizador() {
        return ciuNombreActualizador;
    }

    public void setCiuNombreActualizador(String ciuNombreActualizador) {
        this.ciuNombreActualizador = ciuNombreActualizador;
    }

    public String getCiuNombreFiscalizador() {
        return ciuNombreFiscalizador;
    }

    public void setCiuNombreFiscalizador(String ciuNombreFiscalizador) {
        this.ciuNombreFiscalizador = ciuNombreFiscalizador;
    }

    public String getCiuNombreHorizontal() {
        return ciuNombreHorizontal;
    }

    public void setCiuNombreHorizontal(String ciuNombreHorizontal) {
        this.ciuNombreHorizontal = ciuNombreHorizontal;
    }

    public String getCiuNombreInformante() {
        return ciuNombreInformante;
    }

    public void setCiuNombreInformante(String ciuNombreInformante) {
        this.ciuNombreInformante = ciuNombreInformante;
    }

    public String getNombreInformante() {
        return nombreInformante;
    }

    public void setNombreInformante(String nombreInformante) {
        this.nombreInformante = nombreInformante;
    }

    public String getPredialantAnt() {
        return predialantAnt;
    }

    public void setPredialantAnt(String predialantAnt) {
        this.predialantAnt = predialantAnt;
    }

    public String getPropAnteriorPredio() {
        return propAnteriorPredio;
    }

    public void setPropAnteriorPredio(String propAnteriorPredio) {
        this.propAnteriorPredio = propAnteriorPredio;
    }

    public Boolean getVivCencalPoseeTelfConvencional() {
        return vivCencalPoseeTelfConvencional;
    }

    public void setVivCencalPoseeTelfConvencional(Boolean vivCencalPoseeTelfConvencional) {
        this.vivCencalPoseeTelfConvencional = vivCencalPoseeTelfConvencional;
    }

    public Boolean getVivCencalServInternet() {
        return vivCencalServInternet;
    }

    public void setVivCencalServInternet(Boolean vivCencalServInternet) {
        this.vivCencalServInternet = vivCencalServInternet;
    }

    public CatalogoItem getClasificacionVivienda() {
        return clasificacionVivienda;
    }

    public void setClasificacionVivienda(CatalogoItem clasificacionVivienda) {
        this.clasificacionVivienda = clasificacionVivienda;
    }

    public CatalogoItem getCondicionVivienda() {
        return condicionVivienda;
    }

    public void setCondicionVivienda(CatalogoItem condicionVivienda) {
        this.condicionVivienda = condicionVivienda;
    }

    public Date getFechaActualizador() {
        return fechaActualizador;
    }

    public CatalogoItem getFormaAdquisicion() {
        return formaAdquisicion;
    }

    public void setFormaAdquisicion(CatalogoItem formaAdquisicion) {
        this.formaAdquisicion = formaAdquisicion;
    }

    public void setFechaActualizador(Date fechaActualizador) {
        this.fechaActualizador = fechaActualizador;
    }

    public Date getFechaFiscalizador() {
        return fechaFiscalizador;
    }

    public void setFechaFiscalizador(Date fechaFiscalizador) {
        this.fechaFiscalizador = fechaFiscalizador;
    }

    public String getFirmaActualizador() {
        return firmaActualizador;
    }

    public void setFirmaActualizador(String firmaActualizador) {
        this.firmaActualizador = firmaActualizador;
    }

    public String getFirmaFiscalizador() {
        return firmaFiscalizador;
    }

    public void setFirmaFiscalizador(String firmaFiscalizador) {
        this.firmaFiscalizador = firmaFiscalizador;
    }

    public BigDecimal getCostoDirecto() {
        return costoDirecto;
    }

    public void setCostoDirecto(BigDecimal costoDirecto) {
        this.costoDirecto = costoDirecto;
    }

    public Boolean getCambioNombre() {
        return cambioNombre;
    }

    public void setCambioNombre(Boolean cambioNombre) {
        this.cambioNombre = cambioNombre;
    }

    public String getNombreCambiado() {
        return nombreCambiado;
    }

    public void setNombreCambiado(String nombreCambiado) {
        this.nombreCambiado = nombreCambiado;
    }

    public String getNombrePredio() {
        return nombrePredio;
    }

    public void setNombrePredio(String nombrePredio) {
        this.nombrePredio = nombrePredio;
    }

    public Boolean getPredialanteMigrado() {
        return predialanteMigrado;
    }

    public void setPredialanteMigrado(Boolean predialanteMigrado) {
        this.predialanteMigrado = predialanteMigrado;
    }

    public BigDecimal getValorM2() {
        return valorM2;
    }

    public void setValorM2(BigDecimal valorM2) {
        this.valorM2 = valorM2;
    }

    public BigDecimal getValorBaseM2() {
        return valorBaseM2;
    }

    public void setValorBaseM2(BigDecimal valorBaseM2) {
        this.valorBaseM2 = valorBaseM2;
    }

    public BigDecimal getAreaGrafica() {
        return areaGrafica;
    }

    public void setAreaGrafica(BigDecimal areaGrafica) {
        this.areaGrafica = areaGrafica;
    }

    public Boolean getEstaDibujado() {
        return estaDibujado;
    }

    public void setEstaDibujado(Boolean estaDibujado) {
        this.estaDibujado = estaDibujado;
    }

    public Boolean getEsAvaluoEspecial() {
        return esAvaluoEspecial;
    }

    public void setEsAvaluoEspecial(Boolean esAvaluoEspecial) {
        this.esAvaluoEspecial = esAvaluoEspecial;
    }

    public BigInteger getNumPredioRespaldo() {
        return numPredioRespaldo;
    }

    public void setNumPredioRespaldo(BigInteger numPredioRespaldo) {
        this.numPredioRespaldo = numPredioRespaldo;
    }

    public Boolean getEsCalculoCuantia() {
        return esCalculoCuantia;
    }

    public void setEsCalculoCuantia(Boolean esCalculoCuantia) {
        this.esCalculoCuantia = esCalculoCuantia;
    }

    public String getPredialantTest() {
        return predialantTest;
    }

    public void setPredialantTest(String predialantTest) {
        this.predialantTest = predialantTest;
    }

    public Boolean getEsAvaluoVerificado() {
        return esAvaluoVerificado;
    }

    public void setEsAvaluoVerificado(Boolean esAvaluoVerificado) {
        this.esAvaluoVerificado = esAvaluoVerificado;
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

    public BigInteger getUbicacionManzana() {
        return ubicacionManzana;
    }

    public void setUbicacionManzana(BigInteger ubicacionManzana) {
        this.ubicacionManzana = ubicacionManzana;
    }

    public Boolean getEsTributario() {
        return esTributario;
    }

    public void setEsTributario(Boolean esTributario) {
        this.esTributario = esTributario;
    }

    public Boolean getEstaExonerado() {
        return estaExonerado;
    }

    public void setEstaExonerado(Boolean estaExonerado) {
        this.estaExonerado = estaExonerado;
    }

    public String getAdminCorreo() {
        return adminCorreo;
    }

    public void setAdminCorreo(String adminCorreo) {
        this.adminCorreo = adminCorreo;
    }

    public String getAdminDireccion() {
        return adminDireccion;
    }

    public void setAdminDireccion(String adminDireccion) {
        this.adminDireccion = adminDireccion;
    }

    public String getCedulaPosesionario() {
        return cedulaPosesionario;
    }

    public void setCedulaPosesionario(String cedulaPosesionario) {
        this.cedulaPosesionario = cedulaPosesionario;
    }

    public String getCodigoInstNacPatCul() {
        return codigoInstNacPatCul;
    }

    public void setCodigoInstNacPatCul(String codigoInstNacPatCul) {
        this.codigoInstNacPatCul = codigoInstNacPatCul;
    }

    public Integer getCondicionMunicipalAnio() {
        return condicionMunicipalAnio;
    }

    public void setCondicionMunicipalAnio(Integer condicionMunicipalAnio) {
        this.condicionMunicipalAnio = condicionMunicipalAnio;
    }

    public String getNombrePosesionario() {
        return nombrePosesionario;
    }

    public void setNombrePosesionario(String nombrePosesionario) {
        this.nombrePosesionario = nombrePosesionario;
    }

    public BigDecimal getPrecioOferta() {
        return precioOferta;
    }

    public void setPrecioOferta(BigDecimal precioOferta) {
        this.precioOferta = precioOferta;
    }

    public Boolean getTieneAumentoph() {
        return tieneAumentoph;
    }

    public void setTieneAumentoph(Boolean tieneAumentoph) {
        this.tieneAumentoph = tieneAumentoph;
    }

    public Canton getAdminCanton() {
        return adminCanton;
    }

    public void setAdminCanton(Canton adminCanton) {
        this.adminCanton = adminCanton;
    }

    public BigInteger getAdminParroquia() {
        return adminParroquia;
    }

    public void setAdminParroquia(BigInteger adminParroquia) {
        this.adminParroquia = adminParroquia;
    }

    public BigInteger getCiuTipoIdentificacion() {
        return ciuTipoIdentificacion;
    }

    public void setCiuTipoIdentificacion(BigInteger ciuTipoIdentificacion) {
        this.ciuTipoIdentificacion = ciuTipoIdentificacion;
    }

    public BigInteger getCondicionMunicipal() {
        return condicionMunicipal;
    }

    public void setCondicionMunicipal(BigInteger condicionMunicipal) {
        this.condicionMunicipal = condicionMunicipal;
    }

    public CatalogoItem getNotifTipoDireccion() {
        return notifTipoDireccion;
    }

    public void setNotifTipoDireccion(CatalogoItem notifTipoDireccion) {
        this.notifTipoDireccion = notifTipoDireccion;
    }

    public CatalogoItem getPropiedad() {
        return propiedad;
    }

    public void setPropiedad(CatalogoItem propiedad) {
        this.propiedad = propiedad;
    }

    public CatalogoItem getTopografiaSolar() {
        return topografiaSolar;
    }

    public void setTopografiaSolar(CatalogoItem topografiaSolar) {
        this.topografiaSolar = topografiaSolar;
    }

    public CatalogoItem getTipoVivienda() {
        return tipoVivienda;
    }

    public void setTipoVivienda(CatalogoItem tipoVivienda) {
        this.tipoVivienda = tipoVivienda;
    }

    public CatalogoItem getUsoVia() {
        return usoVia;
    }

    public void setUsoVia(CatalogoItem usoVia) {
        this.usoVia = usoVia;
    }

    public CatalogoItem getVivCencalAcabadoPiso() {
        return vivCencalAcabadoPiso;
    }

    public void setVivCencalAcabadoPiso(CatalogoItem vivCencalAcabadoPiso) {
        this.vivCencalAcabadoPiso = vivCencalAcabadoPiso;
    }

    public String getCodigoPredio() {
        return codigoPredio;
    }

    public CatalogoItem getVivCencalEstadoAcabadoPiso() {
        return vivCencalEstadoAcabadoPiso;
    }

    public void setVivCencalEstadoAcabadoPiso(CatalogoItem vivCencalEstadoAcabadoPiso) {
        this.vivCencalEstadoAcabadoPiso = vivCencalEstadoAcabadoPiso;
    }

    public void setCodigoPredio(String codigoPredio) {
        this.codigoPredio = codigoPredio;
    }

    public List<CatPredioPropietario> getCatPredioPropietarioList() {
        return catPredioPropietarioList;
    }

    public void setCatPredioPropietarioList(List<CatPredioPropietario> catPredioPropietarioList) {
        this.catPredioPropietarioList = catPredioPropietarioList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    public CatPredioS4 getCatPredioS4() {
        return catPredioS4;
    }

    public void setCatPredioS4(CatPredioS4 catPredioS4) {
        this.catPredioS4 = catPredioS4;
    }

    public CatPredioS6 getCatPredioS6() {
        return catPredioS6;
    }

    public void setCatPredioS6(CatPredioS6 catPredioS6) {
        this.catPredioS6 = catPredioS6;
    }

    public List<CatPredioEdificacion> getCatPredioEdificacionCollection() {
        return catPredioEdificacionCollection;
    }

    public void setCatPredioEdificacionCollection(List<CatPredioEdificacion> catPredioEdificacionCollection) {
        this.catPredioEdificacionCollection = catPredioEdificacionCollection;
    }

    public List<CatPredioLinderos> getPredioCollection() {
        return predioCollection;
    }

    public void setPredioCollection(List<CatPredioLinderos> predioCollection) {
        this.predioCollection = predioCollection;
    }

    public List<CatPredioClasificRural> getCatPredioClasificRuralCollection() {
        return catPredioClasificRuralCollection;
    }

    public void setCatPredioClasificRuralCollection(List<CatPredioClasificRural> catPredioClasificRuralCollection) {
        this.catPredioClasificRuralCollection = catPredioClasificRuralCollection;
    }

    public List<CatEscritura> getCatEscrituraCollection() {
        return catEscrituraCollection;
    }

    public void setCatEscrituraCollection(List<CatEscritura> catEscrituraCollection) {
        this.catEscrituraCollection = catEscrituraCollection;
    }

    public List<CatPredioObraInterna> getCatPredioObraInternaCollection() {
        return catPredioObraInternaCollection;
    }

    public void setCatPredioObraInternaCollection(List<CatPredioObraInterna> catPredioObraInternaCollection) {
        this.catPredioObraInternaCollection = catPredioObraInternaCollection;
    }

    public void setProvinciaNot(Provincia provinciaNot) {
        this.provinciaNot = provinciaNot;
    }

    public Provincia getProvinciaNot() {
        if (adminCanton != null) {
            provinciaNot = adminCanton.getIdProvincia();
        }
        return provinciaNot;
    }

    public Collection<CatPredioPropietario> getCatPredioPropietarioCollection() {
        return catPredioPropietarioCollection;
    }

    public void setCatPredioPropietarioCollection(Collection<CatPredioPropietario> catPredioPropietarioCollection) {
        this.catPredioPropietarioCollection = catPredioPropietarioCollection;
    }

    public BigInteger getCalculoCem0403() {
        return calculoCem0403;
    }

    public void setCalculoCem0403(BigInteger calculoCem0403) {
        this.calculoCem0403 = calculoCem0403;
    }

    public String getEsActivo() {
        return esActivo;
    }

    public void setEsActivo(String esActivo) {
        this.esActivo = esActivo;
    }

    public Short getPhh() {
        return phh;
    }

    public void setPhh(Short phh) {
        this.phh = phh;
    }

    public Short getPhv() {
        return phv;
    }

    public void setPhv(Short phv) {
        this.phv = phv;
    }

    public BigInteger getDiv1() {
        return div1;
    }

    public void setDiv1(BigInteger div1) {
        this.div1 = div1;
    }

    public BigInteger getDiv2() {
        return div2;
    }

    public void setDiv2(BigInteger div2) {
        this.div2 = div2;
    }

    public BigInteger getDiv3() {
        return div3;
    }

    public void setDiv3(BigInteger div3) {
        this.div3 = div3;
    }

    public BigInteger getDiv4() {
        return div4;
    }

    public void setDiv4(BigInteger div4) {
        this.div4 = div4;
    }

    public BigDecimal getTarifaImpositiva() {
        return tarifaImpositiva;
    }

    public void setTarifaImpositiva(BigDecimal tarifaImpositiva) {
        this.tarifaImpositiva = tarifaImpositiva;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CatPredio)) {
            return false;
        }
        CatPredio other = (CatPredio) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gestionTributaria.Entities.CatPredio[ id=" + id + " ]";
    }

}
