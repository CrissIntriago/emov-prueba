package com.gestionTributaria.models;

import com.gestionTributaria.Commons.SisVars;
import com.gestionTributaria.Entities.CatCiudadela;
import com.gestionTributaria.Entities.CatParroquia;
import com.gestionTributaria.Entities.CatPredioPropietario;
import com.origami.sigef.common.entities.Usuarios;
import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class CatPredioModel implements Serializable {

    private Long id;

    private Short provincia = SisVars.PROVINCIA;

    private Short canton = SisVars.CANTON;

    private Short parroquia;

    private Short zona;

    private Short sector;

    private Short mz;
    private Short mzdiv;

    private Short solar;

    private Short bloque;

    private Short piso;

    private Short unidad;

    private String claveCat;

    private String predialant;

    private String predialantAnt;

    private BigInteger numPredio;

    private String nombreUrb;

    private Short urbSec;

    private Short numHabitaciones;

    private Short numDormitorios;

    private Short numEspaciosBanios;

    private Integer habitantes;

    private Short numHogares;

    private Short numCelulares;

    private Boolean vivCencalPoseeTelfConvencional;

    private Boolean vivCencalServInternet;

    private Boolean soportaHipoteca;

    private String urbMz;

    private BigInteger numeroFicha;

    private Date instCreacion;

    private String urbSolarnew;

    private String urbSecnew;

    private String observaciones;

    private String predial97;

    private String calleAv;

    private String calle;

    private String calleS;

    private String numeroVivienda;

    private BigDecimal alicuotaUtil;

    private BigDecimal alicuotaConst;

    private BigDecimal areaAumentoCons;

    private BigDecimal alicuotaTerreno;

    private BigDecimal areaTerrenoAlicuota;

    private String numDepartamento;

    private String nombreEdificio;

    private String nomCompPago;

    private Boolean propiedadHorizontal = Boolean.FALSE;

    private BigInteger predioRaiz;

    private String estado;

    private String tipoPredio;

    private BigDecimal avaluoSolar;

    private BigDecimal avaluoConstruccion;

    private BigDecimal avaluoObraComplement;

    private BigDecimal baseImponible;

    private BigDecimal avaluoMunicipal;

    private BigDecimal avaluoCultivos;

    private String usoPh;

    private String divisionUrb;

    private BigInteger numPisos;

    private BigDecimal coordx;

    private BigDecimal coordy;

    private Date fecMod;

    private String usrMod;

    private Boolean revisado;

    private Boolean procesados;

    private Short lote;

    private Boolean nuevo;

    private Boolean tipoViviendaHorizontal;

    private Boolean ocupacionViviendaHorizontal;

    private String catPredioS4;

    private String catPredioS6;

    private String tipoConjunto;

    private Usuarios responsableActualizadorPredial;

    private Usuarios responsableFiscalizadorPredial;

    private Usuarios enteHorizontal;

    private String tenencia;

    private String propiedad;

    private String condicionMunicipal;

    private Integer condicionMunicipalAnio;

    private CatCiudadela ciudadela;

    private Usuarios usuarioCreador;

    private String catPredioS12;

    private BigDecimal areaSolar;

    private BigDecimal areaObras;

    private BigDecimal areaCultivos;

    private BigDecimal areaDeclaradaConst = BigDecimal.ZERO;

    private BigDecimal amri;

    private String zonaPu;

    private String formaSolar;

    private String codCategoria;

    private String topografiaSolar;

    private String tipovia;

    private String otroTipovia;

    private String unidadMedida;

    private String clasificacionViviendaHorizontal;

    private String tipoSuelo;

    private String tenenciaVivienda;

    private String usoSolar;

    private String constructividad;

    private String subsector;

    private BigDecimal areaConstPh;

    private Boolean requierePerfeccionamiento = false;

    private Integer aniosSinPerfeccionamiento;

    private Integer aniosPosesion;

    private String tipoPoseedor;

    private String nombrePuebloEtnia;

    private String clasificacionSuelo;

    private Boolean crear;

    private Short catParroquia;

    private String edificacion;

    private String escrituraLinderos;

    private String claveReordenada;

    private String informante;

    private Boolean fichaMadre = Boolean.FALSE;

    private Integer cantAlicuotas;

    private Boolean tieneEscritura;

    private String escritura;

    private BigDecimal avaluoPlussolar;

    private BigDecimal avaluoPlusconstruccion;

    private BigDecimal avaluoPlusmunicipal;

    private BigDecimal avaluoPluscultivos;

    private BigDecimal avaluoPluObraComplement;

    private String usoVia;

    private String formaAdquisicion;

    private String vivCencalAcabadoPiso;

    private String vivCencalEstadoAcabadoPiso;

    private String propAnteriorPredio;

    private String cedulaPropAnterior;

    private BigDecimal areaTotalCons;

    private String ciRucInformante;

    private String nombreInformante;

    private String apellidosInformante;

    private String ciuActualizador;

    private String ciuNombresActualizador;

    private String ciuFiscalizador;

    private String ciuNombresFiscalizador;

    private String ciuInformante;

    private String ciuNombresInformante;

    private String nombrePosesionario;

    private String cedulaPosesionario;

    private Boolean cambioNombreTitulo;

    private String nombreCambiado;

    private String clasificacionVivienda;

    private String tipoVivienda;

    private String condicionVivienda;

    private String ciuHorizontal;

    private String ciuTipoIdentificacion;

    private String ciuNombresHorizontal;

    private BigDecimal precioOferta;

    private String codigoInstNacPatCul;

    private String adminFullName;

    private String adminCedula;

    private String adminTelefono;

    private String adminCelular;

    private String adminCorreo;

    private String adminDireccion;

    private String notifTipoDireccion;

    private String adminCanton;

    private CatParroquia adminParroquia;

    private Boolean tieneAumentoph;
    //@Transient
    private String provinciaNot;

    private String direccion;
    
    private String idLiquidacion;

    private List<CatPredioPropieatrioDTO> catPredioPropietarioCollection;

    private Short cdla;
    private Short mzDiv;

    private Short div1;
    private Short div2;
    private Short div3;
    private Short div4;
    private Short div5;
    private Short div6;
    private Short div7;
    private Short div8;
    private Short div9;
    private Short phv;
    private Short phh;
    //para locales comerciales
    private Short numLocal;

    private Short parroquiaShort;
    private String codigoPredial;
    //////////////////
    private String regCatastral;
    private String nombreComprador;
    private String idPredial;

    private String mzUrb;
    private String slUrb;
    private String urbanizacion;
    private Long tipoConsultaUrbano = 2L;
    private Collection<DatosRenLiquidacion> datosLiquidacion;
    private String codAnt1;
    private String codAnt2;
    private String codAnt3;
    private String codAnt4;
    private String codAnt5;
    private String codAnt6;
    private String codAnt7;
    private String codAnt8;
    private Usuarios contribuyenteConsulta;
    private CatPredioPropietario propietarioUrbano;
    private Long tipoConsultaRural = 1L;
    private Usuarios contribuyenteConsultaRural;
    private Boolean habilitado = Boolean.FALSE;

    private String predialAnterior;
    //PREDIALANT
    private String provinciaAnt;
    private String cantonAnt;
    private String parroquiaAnt;
    private String manzanaAnt;
    private String zonaAnt;
    private String sectorAnt;
    private String propiedadHorizontalAnt;
    private String solarAnt;

    private String predialAnt;

    private BigDecimal areaConstruccion;
    private BigDecimal zonaGeo;
    private BigDecimal sectorGeo;
    private BigDecimal solarGeo;
    private BigDecimal loteGeo;
    private BigDecimal mzGeo;
    private String nombrePropietarios;

    private Boolean temporales = Boolean.FALSE;

    private Integer anioDesde = Utils.getAnio(new Date()) - 1;
    private Integer anioHasta = Utils.getAnio(new Date()) - 1;
    private Boolean esUrbano = Boolean.TRUE;

    public CatPredioModel() {
        this.provincia = SisVars.PROVINCIA;
        this.canton = SisVars.CANTON;
        this.parroquiaShort = 0;
        this.parroquia = 1;
        this.numPredio = BigInteger.ZERO;
        this.sector = 0;
        this.mz = 0;
        this.cdla = 0;
        this.mzDiv = 0;
        this.solar = 0;
        this.div1 = 0;
        this.div2 = 0;
        this.div3 = 0;
        this.div4 = 0;
        this.div5 = 0;
        this.div6 = 0;
        this.div7 = 0;
        this.div8 = 0;
        this.div9 = 0;
        this.phv = 0;
        this.phh = 0;
        this.numLocal = 0;
        this.codAnt1 = "0";
        this.codAnt2 = "0";
        this.codAnt3 = "0";
        this.codAnt4 = "0";
        this.codAnt5 = "0";
        this.codAnt6 = "0";
        this.codAnt7 = "0";
        this.codAnt8 = "0";

        claveCat = String.format("%02d%02d%02d", SisVars.PROVINCIA, SisVars.CANTON, parroquiaShort);
        predialAnt = String.format("%02d%02d%02d", SisVars.PROVINCIA, SisVars.CANTON, parroquiaShort);
    }

    public void cambiarTipo(Boolean esUrbano) {
        if (esUrbano) {
            claveCat = String.format("%02d%02d%02d", SisVars.PROVINCIA, SisVars.CANTON, parroquiaShort);
            predialAnt = String.format("%02d%02d%02d", SisVars.PROVINCIA, SisVars.CANTON, parroquiaShort);
        } else {
            claveCat = null;
            predialAnt = null;
        }
    }

    public CatPredioModel(BigDecimal areaSolar, BigDecimal areaConstruccion, BigDecimal zonaGeo, BigDecimal sectorGeo, BigDecimal solarGeo, BigDecimal loteGeo, BigDecimal mzGeo) {
        this.areaSolar = areaSolar;
        this.areaConstruccion = areaConstruccion;
        this.zonaGeo = zonaGeo;
        this.sectorGeo = sectorGeo;
        this.solarGeo = solarGeo;
        this.loteGeo = loteGeo;
        this.mzGeo = mzGeo;
    }

    public CatPredioModel(BigDecimal areaConstruccion) {
        this.areaConstruccion = areaConstruccion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Short getProvincia() {
        return provincia;
    }

    public void setProvincia(Short provincia) {
        this.provincia = provincia;
    }

    public Short getCanton() {
        return canton;
    }

    public void setCanton(Short canton) {
        this.canton = canton;
    }

    public Short getParroquia() {
        return parroquia;
    }

    public void setParroquia(Short parroquia) {
        this.parroquia = parroquia;
    }

    public Short getZona() {
        return zona;
    }

    public void setZona(Short zona) {
        this.zona = zona;
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

    public String getClaveCat() {
        return claveCat;
    }

    public void setClaveCat(String claveCat) {
        this.claveCat = claveCat;
    }

    public String getPredialant() {
        return predialant;
    }

    public void setPredialant(String predialant) {
        this.predialant = predialant;
    }

    public String getPredialantAnt() {
        return predialantAnt;
    }

    public void setPredialantAnt(String predialantAnt) {
        this.predialantAnt = predialantAnt;
    }

    public BigInteger getNumPredio() {
        return numPredio;
    }

    public void setNumPredio(BigInteger numPredio) {
        this.numPredio = numPredio;
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

    public Short getNumEspaciosBanios() {
        return numEspaciosBanios;
    }

    public void setNumEspaciosBanios(Short numEspaciosBanios) {
        this.numEspaciosBanios = numEspaciosBanios;
    }

    public Integer getHabitantes() {
        return habitantes;
    }

    public void setHabitantes(Integer habitantes) {
        this.habitantes = habitantes;
    }

    public Short getNumHogares() {
        return numHogares;
    }

    public void setNumHogares(Short numHogares) {
        this.numHogares = numHogares;
    }

    public Short getNumCelulares() {
        return numCelulares;
    }

    public void setNumCelulares(Short numCelulares) {
        this.numCelulares = numCelulares;
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

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getPredial97() {
        return predial97;
    }

    public void setPredial97(String predial97) {
        this.predial97 = predial97;
    }

    public String getCalleAv() {
        return calleAv;
    }

    public void setCalleAv(String calleAv) {
        this.calleAv = calleAv;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
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

    public BigDecimal getAlicuotaConst() {
        return alicuotaConst;
    }

    public void setAlicuotaConst(BigDecimal alicuotaConst) {
        this.alicuotaConst = alicuotaConst;
    }

    public BigDecimal getAreaAumentoCons() {
        return areaAumentoCons;
    }

    public void setAreaAumentoCons(BigDecimal areaAumentoCons) {
        this.areaAumentoCons = areaAumentoCons;
    }

    public BigDecimal getAlicuotaTerreno() {
        return alicuotaTerreno;
    }

    public void setAlicuotaTerreno(BigDecimal alicuotaTerreno) {
        this.alicuotaTerreno = alicuotaTerreno;
    }

    public BigDecimal getAreaTerrenoAlicuota() {
        return areaTerrenoAlicuota;
    }

    public void setAreaTerrenoAlicuota(BigDecimal areaTerrenoAlicuota) {
        this.areaTerrenoAlicuota = areaTerrenoAlicuota;
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

    public String getTipoPredio() {
        return tipoPredio;
    }

    public void setTipoPredio(String tipoPredio) {
        this.tipoPredio = tipoPredio;
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

    public BigDecimal getAvaluoObraComplement() {
        return avaluoObraComplement;
    }

    public void setAvaluoObraComplement(BigDecimal avaluoObraComplement) {
        this.avaluoObraComplement = avaluoObraComplement;
    }

    public BigDecimal getBaseImponible() {
        return baseImponible;
    }

    public void setBaseImponible(BigDecimal baseImponible) {
        this.baseImponible = baseImponible;
    }

    public BigDecimal getAvaluoMunicipal() {
        return avaluoMunicipal;
    }

    public void setAvaluoMunicipal(BigDecimal avaluoMunicipal) {
        this.avaluoMunicipal = avaluoMunicipal;
    }

    public BigDecimal getAvaluoCultivos() {
        return avaluoCultivos;
    }

    public void setAvaluoCultivos(BigDecimal avaluoCultivos) {
        this.avaluoCultivos = avaluoCultivos;
    }

    public String getUsoPh() {
        return usoPh;
    }

    public void setUsoPh(String usoPh) {
        this.usoPh = usoPh;
    }

    public String getDivisionUrb() {
        return divisionUrb;
    }

    public void setDivisionUrb(String divisionUrb) {
        this.divisionUrb = divisionUrb;
    }

    public BigInteger getNumPisos() {
        return numPisos;
    }

    public void setNumPisos(BigInteger numPisos) {
        this.numPisos = numPisos;
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

    public Boolean getProcesados() {
        return procesados;
    }

    public void setProcesados(Boolean procesados) {
        this.procesados = procesados;
    }

    public Boolean getNuevo() {
        return nuevo;
    }

    public void setNuevo(Boolean nuevo) {
        this.nuevo = nuevo;
    }

    public Boolean getTipoViviendaHorizontal() {
        return tipoViviendaHorizontal;
    }

    public void setTipoViviendaHorizontal(Boolean tipoViviendaHorizontal) {
        this.tipoViviendaHorizontal = tipoViviendaHorizontal;
    }

    public Boolean getOcupacionViviendaHorizontal() {
        return ocupacionViviendaHorizontal;
    }

    public void setOcupacionViviendaHorizontal(Boolean ocupacionViviendaHorizontal) {
        this.ocupacionViviendaHorizontal = ocupacionViviendaHorizontal;
    }

    public String getCatPredioS4() {
        return catPredioS4;
    }

    public void setCatPredioS4(String catPredioS4) {
        this.catPredioS4 = catPredioS4;
    }

    public String getCatPredioS6() {
        return catPredioS6;
    }

    public void setCatPredioS6(String catPredioS6) {
        this.catPredioS6 = catPredioS6;
    }

    public String getTipoConjunto() {
        return tipoConjunto;
    }

    public void setTipoConjunto(String tipoConjunto) {
        this.tipoConjunto = tipoConjunto;
    }

    public Usuarios getResponsableActualizadorPredial() {
        return responsableActualizadorPredial;
    }

    public void setResponsableActualizadorPredial(Usuarios responsableActualizadorPredial) {
        this.responsableActualizadorPredial = responsableActualizadorPredial;
    }

    public Usuarios getResponsableFiscalizadorPredial() {
        return responsableFiscalizadorPredial;
    }

    public void setResponsableFiscalizadorPredial(Usuarios responsableFiscalizadorPredial) {
        this.responsableFiscalizadorPredial = responsableFiscalizadorPredial;
    }

    public Usuarios getEnteHorizontal() {
        return enteHorizontal;
    }

    public void setEnteHorizontal(Usuarios enteHorizontal) {
        this.enteHorizontal = enteHorizontal;
    }

    public String getTenencia() {
        return tenencia;
    }

    public void setTenencia(String tenencia) {
        this.tenencia = tenencia;
    }

    public String getPropiedad() {
        return propiedad;
    }

    public void setPropiedad(String propiedad) {
        this.propiedad = propiedad;
    }

    public String getCondicionMunicipal() {
        return condicionMunicipal;
    }

    public void setCondicionMunicipal(String condicionMunicipal) {
        this.condicionMunicipal = condicionMunicipal;
    }

    public Integer getCondicionMunicipalAnio() {
        return condicionMunicipalAnio;
    }

    public void setCondicionMunicipalAnio(Integer condicionMunicipalAnio) {
        this.condicionMunicipalAnio = condicionMunicipalAnio;
    }

    public CatCiudadela getCiudadela() {
        return ciudadela;
    }

    public void setCiudadela(CatCiudadela ciudadela) {
        this.ciudadela = ciudadela;
    }

    public Usuarios getUsuarioCreador() {
        return usuarioCreador;
    }

    public void setUsuarioCreador(Usuarios usuarioCreador) {
        this.usuarioCreador = usuarioCreador;
    }

    public String getCatPredioS12() {
        return catPredioS12;
    }

    public void setCatPredioS12(String catPredioS12) {
        this.catPredioS12 = catPredioS12;
    }

    public BigDecimal getAreaSolar() {
        return areaSolar;
    }

    public void setAreaSolar(BigDecimal areaSolar) {
        this.areaSolar = areaSolar;
    }

    public BigDecimal getAreaObras() {
        return areaObras;
    }

    public void setAreaObras(BigDecimal areaObras) {
        this.areaObras = areaObras;
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

    public String getFormaSolar() {
        return formaSolar;
    }

    public void setFormaSolar(String formaSolar) {
        this.formaSolar = formaSolar;
    }

    public String getCodCategoria() {
        return codCategoria;
    }

    public void setCodCategoria(String codCategoria) {
        this.codCategoria = codCategoria;
    }

    public String getTopografiaSolar() {
        return topografiaSolar;
    }

    public void setTopografiaSolar(String topografiaSolar) {
        this.topografiaSolar = topografiaSolar;
    }

    public String getTipovia() {
        return tipovia;
    }

    public void setTipovia(String tipovia) {
        this.tipovia = tipovia;
    }

    public String getOtroTipovia() {
        return otroTipovia;
    }

    public void setOtroTipovia(String otroTipovia) {
        this.otroTipovia = otroTipovia;
    }

    public String getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    public String getClasificacionViviendaHorizontal() {
        return clasificacionViviendaHorizontal;
    }

    public void setClasificacionViviendaHorizontal(String clasificacionViviendaHorizontal) {
        this.clasificacionViviendaHorizontal = clasificacionViviendaHorizontal;
    }

    public String getTipoSuelo() {
        return tipoSuelo;
    }

    public void setTipoSuelo(String tipoSuelo) {
        this.tipoSuelo = tipoSuelo;
    }

    public String getTenenciaVivienda() {
        return tenenciaVivienda;
    }

    public void setTenenciaVivienda(String tenenciaVivienda) {
        this.tenenciaVivienda = tenenciaVivienda;
    }

    public String getUsoSolar() {
        return usoSolar;
    }

    public void setUsoSolar(String usoSolar) {
        this.usoSolar = usoSolar;
    }

    public String getConstructividad() {
        return constructividad;
    }

    public void setConstructividad(String constructividad) {
        this.constructividad = constructividad;
    }

    public String getSubsector() {
        return subsector;
    }

    public void setSubsector(String subsector) {
        this.subsector = subsector;
    }

    public BigDecimal getAreaConstPh() {
        return areaConstPh;
    }

    public void setAreaConstPh(BigDecimal areaConstPh) {
        this.areaConstPh = areaConstPh;
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

    public String getTipoPoseedor() {
        return tipoPoseedor;
    }

    public void setTipoPoseedor(String tipoPoseedor) {
        this.tipoPoseedor = tipoPoseedor;
    }

    public String getNombrePuebloEtnia() {
        return nombrePuebloEtnia;
    }

    public void setNombrePuebloEtnia(String nombrePuebloEtnia) {
        this.nombrePuebloEtnia = nombrePuebloEtnia;
    }

    public String getClasificacionSuelo() {
        return clasificacionSuelo;
    }

    public void setClasificacionSuelo(String clasificacionSuelo) {
        this.clasificacionSuelo = clasificacionSuelo;
    }

    public Boolean getCrear() {
        return crear;
    }

    public void setCrear(Boolean crear) {
        this.crear = crear;
    }

    public Short getCatParroquia() {
        return catParroquia;
    }

    public void setCatParroquia(Short catParroquia) {
        this.catParroquia = catParroquia;
    }

    public String getEdificacion() {
        return edificacion;
    }

    public void setEdificacion(String edificacion) {
        this.edificacion = edificacion;
    }

    public String getEscrituraLinderos() {
        return escrituraLinderos;
    }

    public void setEscrituraLinderos(String escrituraLinderos) {
        this.escrituraLinderos = escrituraLinderos;
    }

    public String getClaveReordenada() {
        return claveReordenada;
    }

    public void setClaveReordenada(String claveReordenada) {
        this.claveReordenada = claveReordenada;
    }

    public String getInformante() {
        return informante;
    }

    public void setInformante(String informante) {
        this.informante = informante;
    }

    public Boolean getFichaMadre() {
        return fichaMadre;
    }

    public void setFichaMadre(Boolean fichaMadre) {
        this.fichaMadre = fichaMadre;
    }

    public Integer getCantAlicuotas() {
        return cantAlicuotas;
    }

    public void setCantAlicuotas(Integer cantAlicuotas) {
        this.cantAlicuotas = cantAlicuotas;
    }

    public Boolean getTieneEscritura() {
        return tieneEscritura;
    }

    public void setTieneEscritura(Boolean tieneEscritura) {
        this.tieneEscritura = tieneEscritura;
    }

    public String getEscritura() {
        return escritura;
    }

    public void setEscritura(String escritura) {
        this.escritura = escritura;
    }

    public BigDecimal getAvaluoPlussolar() {
        return avaluoPlussolar;
    }

    public void setAvaluoPlussolar(BigDecimal avaluoPlussolar) {
        this.avaluoPlussolar = avaluoPlussolar;
    }

    public BigDecimal getAvaluoPlusconstruccion() {
        return avaluoPlusconstruccion;
    }

    public void setAvaluoPlusconstruccion(BigDecimal avaluoPlusconstruccion) {
        this.avaluoPlusconstruccion = avaluoPlusconstruccion;
    }

    public BigDecimal getAvaluoPlusmunicipal() {
        return avaluoPlusmunicipal;
    }

    public void setAvaluoPlusmunicipal(BigDecimal avaluoPlusmunicipal) {
        this.avaluoPlusmunicipal = avaluoPlusmunicipal;
    }

    public BigDecimal getAvaluoPluscultivos() {
        return avaluoPluscultivos;
    }

    public void setAvaluoPluscultivos(BigDecimal avaluoPluscultivos) {
        this.avaluoPluscultivos = avaluoPluscultivos;
    }

    public BigDecimal getAvaluoPluObraComplement() {
        return avaluoPluObraComplement;
    }

    public void setAvaluoPluObraComplement(BigDecimal avaluoPluObraComplement) {
        this.avaluoPluObraComplement = avaluoPluObraComplement;
    }

    public String getUsoVia() {
        return usoVia;
    }

    public void setUsoVia(String usoVia) {
        this.usoVia = usoVia;
    }

    public String getFormaAdquisicion() {
        return formaAdquisicion;
    }

    public void setFormaAdquisicion(String formaAdquisicion) {
        this.formaAdquisicion = formaAdquisicion;
    }

    public String getVivCencalAcabadoPiso() {
        return vivCencalAcabadoPiso;
    }

    public void setVivCencalAcabadoPiso(String vivCencalAcabadoPiso) {
        this.vivCencalAcabadoPiso = vivCencalAcabadoPiso;
    }

    public String getVivCencalEstadoAcabadoPiso() {
        return vivCencalEstadoAcabadoPiso;
    }

    public void setVivCencalEstadoAcabadoPiso(String vivCencalEstadoAcabadoPiso) {
        this.vivCencalEstadoAcabadoPiso = vivCencalEstadoAcabadoPiso;
    }

    public String getPropAnteriorPredio() {
        return propAnteriorPredio;
    }

    public void setPropAnteriorPredio(String propAnteriorPredio) {
        this.propAnteriorPredio = propAnteriorPredio;
    }

    public String getCedulaPropAnterior() {
        return cedulaPropAnterior;
    }

    public void setCedulaPropAnterior(String cedulaPropAnterior) {
        this.cedulaPropAnterior = cedulaPropAnterior;
    }

    public BigDecimal getAreaTotalCons() {
        return areaTotalCons;
    }

    public void setAreaTotalCons(BigDecimal areaTotalCons) {
        this.areaTotalCons = areaTotalCons;
    }

    public String getCiRucInformante() {
        return ciRucInformante;
    }

    public void setCiRucInformante(String ciRucInformante) {
        this.ciRucInformante = ciRucInformante;
    }

    public String getNombreInformante() {
        return nombreInformante;
    }

    public void setNombreInformante(String nombreInformante) {
        this.nombreInformante = nombreInformante;
    }

    public String getApellidosInformante() {
        return apellidosInformante;
    }

    public void setApellidosInformante(String apellidosInformante) {
        this.apellidosInformante = apellidosInformante;
    }

    public String getCiuActualizador() {
        return ciuActualizador;
    }

    public void setCiuActualizador(String ciuActualizador) {
        this.ciuActualizador = ciuActualizador;
    }

    public String getCiuNombresActualizador() {
        return ciuNombresActualizador;
    }

    public void setCiuNombresActualizador(String ciuNombresActualizador) {
        this.ciuNombresActualizador = ciuNombresActualizador;
    }

    public String getCiuFiscalizador() {
        return ciuFiscalizador;
    }

    public void setCiuFiscalizador(String ciuFiscalizador) {
        this.ciuFiscalizador = ciuFiscalizador;
    }

    public String getCiuNombresFiscalizador() {
        return ciuNombresFiscalizador;
    }

    public void setCiuNombresFiscalizador(String ciuNombresFiscalizador) {
        this.ciuNombresFiscalizador = ciuNombresFiscalizador;
    }

    public String getCiuInformante() {
        return ciuInformante;
    }

    public void setCiuInformante(String ciuInformante) {
        this.ciuInformante = ciuInformante;
    }

    public String getCiuNombresInformante() {
        return ciuNombresInformante;
    }

    public void setCiuNombresInformante(String ciuNombresInformante) {
        this.ciuNombresInformante = ciuNombresInformante;
    }

    public String getNombrePosesionario() {
        return nombrePosesionario;
    }

    public void setNombrePosesionario(String nombrePosesionario) {
        this.nombrePosesionario = nombrePosesionario;
    }

    public String getCedulaPosesionario() {
        return cedulaPosesionario;
    }

    public void setCedulaPosesionario(String cedulaPosesionario) {
        this.cedulaPosesionario = cedulaPosesionario;
    }

    public Boolean getCambioNombreTitulo() {
        return cambioNombreTitulo;
    }

    public void setCambioNombreTitulo(Boolean cambioNombreTitulo) {
        this.cambioNombreTitulo = cambioNombreTitulo;
    }

    public String getNombreCambiado() {
        return nombreCambiado;
    }

    public void setNombreCambiado(String nombreCambiado) {
        this.nombreCambiado = nombreCambiado;
    }

    public String getClasificacionVivienda() {
        return clasificacionVivienda;
    }

    public void setClasificacionVivienda(String clasificacionVivienda) {
        this.clasificacionVivienda = clasificacionVivienda;
    }

    public String getTipoVivienda() {
        return tipoVivienda;
    }

    public void setTipoVivienda(String tipoVivienda) {
        this.tipoVivienda = tipoVivienda;
    }

    public String getCondicionVivienda() {
        return condicionVivienda;
    }

    public void setCondicionVivienda(String condicionVivienda) {
        this.condicionVivienda = condicionVivienda;
    }

    public String getCiuHorizontal() {
        return ciuHorizontal;
    }

    public void setCiuHorizontal(String ciuHorizontal) {
        this.ciuHorizontal = ciuHorizontal;
    }

    public String getCiuTipoIdentificacion() {
        return ciuTipoIdentificacion;
    }

    public void setCiuTipoIdentificacion(String ciuTipoIdentificacion) {
        this.ciuTipoIdentificacion = ciuTipoIdentificacion;
    }

    public String getCiuNombresHorizontal() {
        return ciuNombresHorizontal;
    }

    public void setCiuNombresHorizontal(String ciuNombresHorizontal) {
        this.ciuNombresHorizontal = ciuNombresHorizontal;
    }

    public BigDecimal getPrecioOferta() {
        return precioOferta;
    }

    public void setPrecioOferta(BigDecimal precioOferta) {
        this.precioOferta = precioOferta;
    }

    public String getCodigoInstNacPatCul() {
        return codigoInstNacPatCul;
    }

    public void setCodigoInstNacPatCul(String codigoInstNacPatCul) {
        this.codigoInstNacPatCul = codigoInstNacPatCul;
    }

    public String getAdminFullName() {
        return adminFullName;
    }

    public void setAdminFullName(String adminFullName) {
        this.adminFullName = adminFullName;
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

    public String getNotifTipoDireccion() {
        return notifTipoDireccion;
    }

    public void setNotifTipoDireccion(String notifTipoDireccion) {
        this.notifTipoDireccion = notifTipoDireccion;
    }

    public String getAdminCanton() {
        return adminCanton;
    }

    public void setAdminCanton(String adminCanton) {
        this.adminCanton = adminCanton;
    }

    public CatParroquia getAdminParroquia() {
        return adminParroquia;
    }

    public void setAdminParroquia(CatParroquia adminParroquia) {
        this.adminParroquia = adminParroquia;
    }

    public Boolean getTieneAumentoph() {
        return tieneAumentoph;
    }

    public void setTieneAumentoph(Boolean tieneAumentoph) {
        this.tieneAumentoph = tieneAumentoph;
    }

    public String getProvinciaNot() {
        return provinciaNot;
    }

    public void setProvinciaNot(String provinciaNot) {
        this.provinciaNot = provinciaNot;
    }

    public Short getCdla() {
        return cdla;
    }

    public void setCdla(Short cdla) {
        this.cdla = cdla;
    }

    public Short getMzDiv() {
        return mzDiv;
    }

    public void setMzDiv(Short mzDiv) {
        this.mzDiv = mzDiv;
    }

    public Short getDiv1() {
        return div1;
    }

    public void setDiv1(Short div1) {
        this.div1 = div1;
    }

    public Short getDiv2() {
        return div2;
    }

    public void setDiv2(Short div2) {
        this.div2 = div2;
    }

    public Short getDiv3() {
        return div3;
    }

    public void setDiv3(Short div3) {
        this.div3 = div3;
    }

    public Short getDiv4() {
        return div4;
    }

    public void setDiv4(Short div4) {
        this.div4 = div4;
    }

    public Short getDiv5() {
        return div5;
    }

    public void setDiv5(Short div5) {
        this.div5 = div5;
    }

    public Short getDiv6() {
        return div6;
    }

    public void setDiv6(Short div6) {
        this.div6 = div6;
    }

    public Short getDiv7() {
        return div7;
    }

    public void setDiv7(Short div7) {
        this.div7 = div7;
    }

    public Short getDiv8() {
        return div8;
    }

    public void setDiv8(Short div8) {
        this.div8 = div8;
    }

    public Short getDiv9() {
        return div9;
    }

    public void setDiv9(Short div9) {
        this.div9 = div9;
    }

    public Short getPhv() {
        return phv;
    }

    public void setPhv(Short phv) {
        this.phv = phv;
    }

    public Short getPhh() {
        return phh;
    }

    public void setPhh(Short phh) {
        this.phh = phh;
    }

    public Short getParroquiaShort() {
        return parroquiaShort;
    }

    public void setParroquiaShort(Short parroquiaShort) {
        this.parroquiaShort = parroquiaShort;
    }

    public String getCodigoPredial() {
        return codigoPredial;
    }

    public void setCodigoPredial(String codigoPredial) {
        this.codigoPredial = codigoPredial;
    }

    public String getRegCatastral() {
        return regCatastral;
    }

    public void setRegCatastral(String regCatastral) {
        this.regCatastral = regCatastral;
    }

    public String getNombreComprador() {
        return nombreComprador;
    }

    public void setNombreComprador(String nombreComprador) {
        this.nombreComprador = nombreComprador;
    }

    public String getIdPredial() {
        return idPredial;
    }

    public void setIdPredial(String idPredial) {
        this.idPredial = idPredial;
    }

    public String getMzUrb() {
        return mzUrb;
    }

    public void setMzUrb(String mzUrb) {
        this.mzUrb = mzUrb;
    }

    public String getSlUrb() {
        return slUrb;
    }

    public void setSlUrb(String slUrb) {
        this.slUrb = slUrb;
    }

    public String getUrbanizacion() {
        return urbanizacion;
    }

    public void setUrbanizacion(String urbanizacion) {
        this.urbanizacion = urbanizacion;
    }

    public Long getTipoConsultaUrbano() {
        return tipoConsultaUrbano;
    }

    public void setTipoConsultaUrbano(Long tipoConsultaUrbano) {
        this.tipoConsultaUrbano = tipoConsultaUrbano;
    }

    public Collection<DatosRenLiquidacion> getDatosLiquidacion() {
        return datosLiquidacion;
    }

    public void setDatosLiquidacion(Collection<DatosRenLiquidacion> datosLiquidacion) {
        this.datosLiquidacion = datosLiquidacion;
    }

    public String getCodAnt1() {
        return codAnt1;
    }

    public void setCodAnt1(String codAnt1) {
        this.codAnt1 = codAnt1;
    }

    public String getCodAnt2() {
        return codAnt2;
    }

    public void setCodAnt2(String codAnt2) {
        this.codAnt2 = codAnt2;
    }

    public String getCodAnt3() {
        return codAnt3;
    }

    public void setCodAnt3(String codAnt3) {
        this.codAnt3 = codAnt3;
    }

    public String getCodAnt4() {
        return codAnt4;
    }

    public void setCodAnt4(String codAnt4) {
        this.codAnt4 = codAnt4;
    }

    public String getCodAnt5() {
        return codAnt5;
    }

    public void setCodAnt5(String codAnt5) {
        this.codAnt5 = codAnt5;
    }

    public String getCodAnt6() {
        return codAnt6;
    }

    public void setCodAnt6(String codAnt6) {
        this.codAnt6 = codAnt6;
    }

    public String getCodAnt7() {
        return codAnt7;
    }

    public void setCodAnt7(String codAnt7) {
        this.codAnt7 = codAnt7;
    }

    public String getCodAnt8() {
        return codAnt8;
    }

    public void setCodAnt8(String codAnt8) {
        this.codAnt8 = codAnt8;
    }

    public Usuarios getContribuyenteConsulta() {
        return contribuyenteConsulta;
    }

    public void setContribuyenteConsulta(Usuarios contribuyenteConsulta) {
        this.contribuyenteConsulta = contribuyenteConsulta;
    }

    public CatPredioPropietario getPropietarioUrbano() {
        return propietarioUrbano;
    }

    public void setPropietarioUrbano(CatPredioPropietario propietarioUrbano) {
        this.propietarioUrbano = propietarioUrbano;
    }

    public Long getTipoConsultaRural() {
        return tipoConsultaRural;
    }

    public void setTipoConsultaRural(Long tipoConsultaRural) {
        this.tipoConsultaRural = tipoConsultaRural;
    }

    public Usuarios getContribuyenteConsultaRural() {
        return contribuyenteConsultaRural;
    }

    public void setContribuyenteConsultaRural(Usuarios contribuyenteConsultaRural) {
        this.contribuyenteConsultaRural = contribuyenteConsultaRural;
    }

    public Boolean getHabilitado() {
        return habilitado;
    }

    public void setHabilitado(Boolean habilitado) {
        this.habilitado = habilitado;
    }

    public String getPredialAnterior() {
        return predialAnterior;
    }

    public void setPredialAnterior(String predialAnterior) {
        this.predialAnterior = predialAnterior;
    }

    public String getProvinciaAnt() {
        return provinciaAnt;
    }

    public void setProvinciaAnt(String provinciaAnt) {
        this.provinciaAnt = provinciaAnt;
    }

    public String getCantonAnt() {
        return cantonAnt;
    }

    public void setCantonAnt(String cantonAnt) {
        this.cantonAnt = cantonAnt;
    }

    public String getParroquiaAnt() {
        return parroquiaAnt;
    }

    public void setParroquiaAnt(String parroquiaAnt) {
        this.parroquiaAnt = parroquiaAnt;
    }

    public String getManzanaAnt() {
        return manzanaAnt;
    }

    public void setManzanaAnt(String manzanaAnt) {
        this.manzanaAnt = manzanaAnt;
    }

    public String getZonaAnt() {
        return zonaAnt;
    }

    public void setZonaAnt(String zonaAnt) {
        this.zonaAnt = zonaAnt;
    }

    public String getSectorAnt() {
        return sectorAnt;
    }

    public void setSectorAnt(String sectorAnt) {
        this.sectorAnt = sectorAnt;
    }

    public String getPropiedadHorizontalAnt() {
        return propiedadHorizontalAnt;
    }

    public void setPropiedadHorizontalAnt(String propiedadHorizontalAnt) {
        this.propiedadHorizontalAnt = propiedadHorizontalAnt;
    }

    public String getSolarAnt() {
        return solarAnt;
    }

    public void setSolarAnt(String solarAnt) {
        this.solarAnt = solarAnt;
    }

    public String getPredialAnt() {
        return predialAnt;
    }

    public void setPredialAnt(String predialAnt) {
        this.predialAnt = predialAnt;
    }

    public BigDecimal getAreaConstruccion() {
        return areaConstruccion;
    }

    public void setAreaConstruccion(BigDecimal areaConstruccion) {
        this.areaConstruccion = areaConstruccion;
    }

    public BigDecimal getZonaGeo() {
        return zonaGeo;
    }

    public void setZonaGeo(BigDecimal zonaGeo) {
        this.zonaGeo = zonaGeo;
    }

    public BigDecimal getSectorGeo() {
        return sectorGeo;
    }

    public void setSectorGeo(BigDecimal sectorGeo) {
        this.sectorGeo = sectorGeo;
    }

    public BigDecimal getSolarGeo() {
        return solarGeo;
    }

    public void setSolarGeo(BigDecimal solarGeo) {
        this.solarGeo = solarGeo;
    }

    public BigDecimal getLoteGeo() {
        return loteGeo;
    }

    public void setLoteGeo(BigDecimal loteGeo) {
        this.loteGeo = loteGeo;
    }

    public BigDecimal getMzGeo() {
        return mzGeo;
    }

    public void setMzGeo(BigDecimal mzGeo) {
        this.mzGeo = mzGeo;
    }

    public Boolean getTemporales() {
        return temporales;
    }

    public void setTemporales(Boolean temporales) {
        this.temporales = temporales;
    }

    public Integer getAnioDesde() {
        return anioDesde;
    }

    public void setAnioDesde(Integer anioDesde) {
        this.anioDesde = anioDesde;
    }

    public Integer getAnioHasta() {
        return anioHasta;
    }

    public void setAnioHasta(Integer anioHasta) {
        this.anioHasta = anioHasta;
    }

    public Boolean getEsUrbano() {
        return esUrbano;
    }

    public void setEsUrbano(Boolean esUrbano) {
        this.esUrbano = esUrbano;
    }

    public String getCodigoPredialCompleto() {
        return this.claveCat;
    }

    public Short getLote() {
        return lote;
    }

    public void setLote(Short lote) {
        this.lote = lote;
    }

    public String getNombrePropietarios() {
        return nombrePropietarios;
    }

    public void setNombrePropietarios(String nombrePropietarios) {
        this.nombrePropietarios = nombrePropietarios;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public List<CatPredioPropieatrioDTO> getCatPredioPropietarioCollection() {
        return catPredioPropietarioCollection;
    }

    public void setCatPredioPropietarioCollection(List<CatPredioPropieatrioDTO> catPredioPropietarioCollection) {
        this.catPredioPropietarioCollection = catPredioPropietarioCollection;
    }

    public String getIdLiquidacion() {
        return idLiquidacion;
    }

    public void setIdLiquidacion(String idLiquidacion) {
        this.idLiquidacion = idLiquidacion;
    }

    public Short getNumLocal() {
        return numLocal;
    }

    public void setNumLocal(Short numLocal) {
        this.numLocal = numLocal;
    }

    @Override
    public String toString() {
        return "CatPredioModel{" + "id=" + id + ", claveCat=" + claveCat + '}';
    }

}
