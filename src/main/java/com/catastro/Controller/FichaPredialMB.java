/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.catastro.Controller;

import com.catastro.Entities.CatEscritura;
import com.catastro.Entities.CatPredioClasificRural;
import com.catastro.Entities.CatPredioEdificacion;
import com.catastro.Entities.CatPredioEdificacionProp;
import com.catastro.Entities.CatPredioFotos;
import com.catastro.Entities.CatPredioObraInterna;
import com.catastro.Entities.CatPredioS4;
import com.catastro.Entities.CatPredioS6;
import com.catastro.Models.BloquePredio;
import com.catastro.Models.EstadosPredio;
import com.catastro.Models.PredioAllService;
import com.catastro.Models.TipoProceso;
import com.catastro.Services.CatPredioFotosServices;
import com.catastro.Services.CatPredioS4Services;
import com.catastro.Services.CatPredioS6Services;
import com.catastro.Services.CatPredioServices;
import com.catastro.Utils.PredioUtil;
import com.gestionTributaria.Commons.SisVars;
import com.gestionTributaria.Entities.CatPredio;
import com.gestionTributaria.Entities.CatPredioPropietario;
import com.origami.sigef.common.entities.Canton;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author Administrator
 */
@Named
@ViewScoped
public class FichaPredialMB extends PredioUtil implements Serializable {

    @Inject
    protected AppConfig appconfig;
    @Inject
    private UserSession sess;
    @Inject
    private ServletSession ss;
    @Inject
    private CatPredioFotosServices catPredioFotosServices;
    @Inject
    private CatPredioS6Services catPredioS6Services;
    @Inject
    private CatPredioS4Services catPredioS4Services;
    @Inject
    private CatPredioServices catPredioServices;
    private static final Logger LOG = Logger.getLogger(FichaPredialMB.class.getName());
    private Short codProvincia = SisVars.PROVINCIA;
    private Cliente actualizadorPredio;
    private Cliente fiscalizadorPredio;
    private String predioId;
    private Long predioLink;
    private Boolean esTareaAp = false;
    private Boolean esTarea = false;
    private String taskKey;
    private String usr, obs, predioAnt, fichaEdifAnt, fichaEdifAct, fichaModelAnt, fichaModelAct;
    private String claveCat;
    private Boolean editable = false;
    private CatPredioS4 caracteristicas;
    private CatPredioS6 servicios;
    private CatEscritura escritura;
    private List<CatPredioEdificacion> bloques;
    private List<CatPredioPropietario> propietarios;
    private String jsTarea = null;
    private List<CatalogoItem> usosA = new ArrayList<>(), prototipos, vias, instalacionesEspeciales;
    private List<CatPredioObraInterna> obrasInternas;
    private List<CatPredioClasificRural> clasificaciones;
    private List<CatPredioFotos> fotos;
    private List<String> observacionRestricciones;
    private int tipoTarea = 1;
    private Boolean editarClave = false;
    private PredioAllService predioGraficoServ;
    private TipoProceso proceso;
    private Object objectoEliminar;
    private Integer tipoEliminar;
    private String observacionEliminar;
    private Boolean coopropietarios;
    private List<Canton> cantones;
    protected Boolean controlAddUp;
    private List<CatEscritura> escriturasConsulta;
    private final String keyGeoBloque = "geoBloque";
    private String tipoDocumentoAdj;
    private BloquePredio geoBloqueSeleccionado;
    @PostConstruct
    public void load() {
        try {
            codProvincia = SisVars.PROVINCIA;
            if (!JsfUtil.isAjaxRequest()) {
                cargarQueryPar();
                cargarDatos();
//                if (esProcesoActualizacionAreasLind()) {
//                    this.getSumaAreaPredioDesmenbraciones();
//                    this.getCalcularExcedenteDiferencia();
//                    this.getPorcentajeExcedenteDiferencia();
//                }
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, usr, e);
        }
    }

    public void showDialogObservaciones(int tipoTarea) {
        this.tipoTarea = tipoTarea;
        JsfUtil.executeJS("PF('obs').show();");
    }

    public Boolean esProcesoActualizacionAreasLind() {
        if (proceso == null) {
            if (ss.getParametros() != null && ss.getParametros().get("proceso") != null) {
                proceso = (TipoProceso) ss.getParametros().get("proceso");
            }
            if (proceso == null) {
                return false;
            }
        }
        return proceso.equals(TipoProceso.ACTIALIZAR_AREAS_LINDEROS);
    }

    public void cargarDatos() {
        try {
            if (sess != null && ss.getParametros() != null) {
                if (ss.getParametros().get("taskId") != null && !Boolean.valueOf(ss.getParametros().get("edit").toString())) {
                    esTareaAp = true;
                }
                if (ss.getParametros().get("taskId") != null && Boolean.valueOf(ss.getParametros().get("edit").toString())) {
                    esTarea = true;
                    sess.setTaskID(ss.getParametros().get("taskId").toString());
                }
                if (ss.existeParametro("taskName")) {
                    taskKey = ss.getParametros().toString();
                } else {
                    taskKey = "Edicion Alfanumerico";
                }
                this.init();
                BigInteger numeroPredio = null;
                Long id = null;
                if (claveCat != null) {
                    if (claveCat.length() < 24) {
                        setPredio(getCatas().getPredioByClaveCatAnt(claveCat));
                    } else {
                        setPredio(getCatas().getPredioByClaveCat(claveCat));
                    }
                } else {
                    if (predioLink == null) {
                        if (ss.existeParametro("idPredio")) {
                            id = Long.parseLong(ss.getParametros().get("idPredio").toString());
                            System.out.println("entra a exite parametro id" + id);
                        }
                        if (ss.existeParametro("numPredio")) {
                            numeroPredio = new BigInteger(ss.getParametros().get("numPredio").toString());
                            System.out.println("numero predio existe" + numeroPredio);
                        }
                        if (ss.existeParametro("edit")) {
                            editable = Boolean.parseBoolean(ss.getParametros().get("edit").toString());
                        }
                    } else {
                        editable = false;
                    }
                    if (id == null) {
                        System.out.println("null" + id);
                        JsfUtil.redirectFaces("catastro/predios.xhtml");
                    } else if (numeroPredio != null) {
                        System.out.println("busca por numero predio" + numeroPredio);
                        setPredio(getCatas().getPredioNumPredio(numeroPredio));
                    } else {
                        System.out.println("busca por id");
                        setPredio(getCatas().getPredioId(id));
                    }
                }
                if (getPredio() != null) {
                    System.out.println("Usuario " + sess.getNameUser() + " editando Predio con id " + getPredio().getId()
                            + " y clave " + getPredio().getClaveCat() + ", numero Predio " + getPredio().getNumPredio());
                    if (getPredio().getCiudadela() != null && getPredio().getTipoConjunto() == null) {
                        if (getPredio().getCiudadela().getCodTipoConjunto() != null) {
                            getPredio().setTipoConjunto(getPredio().getCiudadela().getCodTipoConjunto());
                        }
                    }
//                    if (getPredio().getCatPredioS4() != null) {
//                        caracteristicas = getPredio().getCatPredioS4();
//                    } else {
//                        caracteristicas = new CatPredioS4();
//                        caracteristicas.setPredio(getPredio());
//                    }
//                    getPredio().setCatPredioS4(caracteristicas);
                    if (predio.getCatPredioS6() != null) {
                        servicios = predio.getCatPredioS6();
                        System.out.println("LOS S6: "+servicios);
                    } else {
                        servicios = new CatPredioS6();
                        servicios.setPredio(predio);
                    }
//                    getPredio().setCatPredioS6(servicios);

                    this.setUsr(sess.getNameUser());
//                    escritura = catEscrituraServices.getCatEscrituraByPredioUltima(getPredio());
//                    if (escritura == null) {
//                        escritura = new CatEscritura();
//                        escritura.setPredio(getPredio());
//                        escritura.setEstado("A");
//                        escritura.setSecuencia(new BigInteger("1"));
//                    }
                    bloques = (List<CatPredioEdificacion>) getPredio().getCatPredioEdificacionCollection();
                    if (bloques == null) {
                        bloques = new ArrayList<>();
                    }
                    propietarios = this.getCiudadano((List<CatPredioPropietario>) predio.getCatPredioPropietarioList());
                    clasificaciones = getPredio().getCatPredioClasificRuralCollection();
                    obrasInternas = (List<CatPredioObraInterna>) getPredio().getCatPredioObraInternaCollection();
                    this.cargarDoc();
                    this.getDatosBloques();
                    cargarPrestamos();
                    usosA = new ArrayList<>();
                    if (esPh()) {
//                            this.calcularAreaConsAlic();
//                            this.calcularAreaTerrAlic();
                    }
                    if (propietarios != null && propietarios.size() > 0) {
//                        for (CatPredioPropietario pp : propietarios) {
//                            if (pp.getEstado().equalsIgnoreCase("A")) {
//                                if (pp.getCopropietario() != null && pp.getCopropietario()) {
//                                    coopropietarios = true;
//                                    break;
//                                }
//                            }
//                        }
                    } else {
                        propietarios = new ArrayList<>();
                    }
                    getLinderosEscritura();
                    cargarFotos();
                    if (editable) {
                        this.defaultCatalogos();
                    }
                    listarUsos();
//                    if (getPredio().getTipoPredio().equals('U')) {
//                        this.verificarRestriccion();
//                    }
                    if (getPredio().getPropiedadHorizontal() == false || getPredio().getPropiedadHorizontal() == null) {
                        getPredio().setAreaDeclaradaConst(this.sumarAreaConst());
                    }
                    // Verificamos Si hay Bloque nuevo en la edicion de una tarea
                    if (ss.existeParametro("idTramite")) {
                        this.jsTarea = getPredioAnt();
                        BigDecimal areaTemp = BigDecimal.ZERO;
                        if (ss.existeParametro("areaGrafica")) {
                            areaTemp = new BigDecimal(ss.getParametros().get("areaGrafica").toString());
                            if (getPredio().getTipoPredio().equalsIgnoreCase("U")) {
                                getPredio().setAreaSolar(areaTemp.setScale(2, RoundingMode.HALF_UP));
                                this.caracteristicas.setAreaGraficaLote(areaTemp.setScale(2, RoundingMode.HALF_UP));
                            } else {
                                this.caracteristicas.setAreaGraficaLote(areaTemp.setScale(5, RoundingMode.HALF_UP).divide(BigDecimal.valueOf(10000.00)));
                                getPredio().setAreaSolar(areaTemp.divide(BigDecimal.valueOf(10000.00)).setScale(5, RoundingMode.HALF_UP));
                            }
                        }
                    }
                    this.cargarDatosEspaciales();
                    // cargamos los documentos
//                    documentos = new LazyModel<>(GeDocumentos.class);
//                    documentos.addFilter("raiz", BigInteger.valueOf(predio.getId()));
//                    documentos.addSorted("fechaCreacion", "DESC");
                } else {
                    JsfUtil.redirectFaces("catastro/predios.xhtml");
                }
            } else {
                JsfUtil.redirectFaces("catastro/predios.xhtml");
            }
        } catch (NumberFormatException e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public CatPredio getPredioRaiz() {
        if (getPredio() == null) {
            return null;
        }
        if (getPredio().getPredioRaiz() != null) {
            return catPredioServices.getPredioId(getPredio().getPredioRaiz().longValue());
        }
        return null;
    }

    private BigDecimal sumarAreaConst() {
        try {
            if (Utils.isEmpty(bloques)) {
                return BigDecimal.ZERO;
            }
            BigDecimal areaConst = BigDecimal.ZERO;
            for (CatPredioEdificacion b : bloques) {
                if (b.getAreaBloque() == null) {
                    b.setAreaBloque(BigDecimal.ZERO);
                }
                if (b.getEstado().equalsIgnoreCase(EstadosPredio.ACTIVO)) {
                    areaConst = areaConst.add(b.getAreaBloque());
                }
            }
            return areaConst;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
        return BigDecimal.ZERO;
    }

    public void cargarFotos() {
        if (getPredio() == null) {
            return;
        }
//        fotos = catPredioFotosServices.getFotosIdPredio(getPredio());
//        fotos.size();

    }

    public void getLinderosEscritura() {
        try {
            if (escritura != null) {
                getPredio().getPredioCollection().size();
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public Boolean esPh() {
        return getPredio().getPropiedadHorizontal();
    }

    private void getDatosBloques() {
        if (getPredio().getVivCencalEstadoAcabadoPiso() == null || getPredio().getVivCencalAcabadoPiso() == null) {
            if (Utils.isNotEmpty(getPredio().getCatPredioEdificacionCollection())) {
                CatPredioEdificacion get = Utils.get(getPredio().getCatPredioEdificacionCollection(), 0);
                if (getPredio().getVivCencalEstadoAcabadoPiso() == null) {
                    getPredio().setVivCencalEstadoAcabadoPiso(get.getEstadoConservacion());
                }
                if (getPredio().getVivCencalAcabadoPiso() == null) {
                    for (CatPredioEdificacionProp b : get.getCatPredioEdificacionPropList()) {
                        if (b.getProp().getCategoria().getGuiOrden() == 9) {
                            getPredio().setVivCencalAcabadoPiso(
                                    catalogoItemService.getItemByCatalagoOrder("predio.bloque.revestpiso", b.getProp().getOrden()));
                            break;
                        }
                    }
                }
            }
        }
    }

    public void listarUsos() {
        try {
            if (predio.getCatPredioS6()!= null) {
                if (predio.getCatPredioS6().getUsosList()!= null) {
                    if (Utils.isNotEmpty(predio.getCatPredioS6().getUsosList())) {
                        predio.getCatPredioS6().getUsosList().forEach((ci)->{
                            usosA.add(ci);
                        });
                    }
                    predio.getCatPredioS6().setUsosList(usosA);
                } else {
                    usosA = new ArrayList<>();
                }
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    /**
     * Carga los documentos adjuntados al predio
     */
    public void defaultCatalogos() {
        if (predio != null) {
            if (predio.getClasificacionSuelo() == null) {
                predio.setClasificacionSuelo(catalogoItemService.getDefaultItem("predio.clasificacion_suelo"));
            }
            if (predio.getConstructividad() == null) {
                predio.setConstructividad(new BigInteger(catalogoItemService.getDefaultItem("predio.constructividad_suelo").getId().toString()));
            }
            if (predio.getTipoVia() == null) {
                predio.setTipoVia(new BigInteger(catalogoItemService.getDefaultItem("predio.vias").getId().toString()));
            }

            if (this.caracteristicas != null) {
                if (this.caracteristicas.getCoberturaPredominante() == null) {
                    this.caracteristicas
                            .setCoberturaPredominante(new BigInteger(catalogoItemService.getDefaultItem("predio.cobertura_predominante").getId().toString()));
                }
                if (this.caracteristicas.getEcosistemaRelevante() == null) {
                    this.caracteristicas
                            .setEcosistemaRelevante(new BigInteger(catalogoItemService.getDefaultItem("predio.ecosistema_relevante").getId().toString()));
                }
                if (this.caracteristicas.getUnidadmAreaGrafica() == null) {
                    this.caracteristicas.setUnidadmAreaGrafica(new BigInteger(catalogoItemService.getDefaultItem("edif.unidad_medida").getId().toString()));
                }
            }
            if (this.servicios != null) {
                if (servicios.getAbastAguaProviene() == null) {
                    servicios.setAbastEnergiaProviene(catalogoItemService.getDefaultItem("predio.abastecimiento_agua"));
                }
                if (servicios.getAbasAguaRecibe() == null) {
                    servicios.setAbasAguaRecibe(catalogoItemService.getDefaultItem("predio.agua_recibe"));
                }
                if (servicios.getAbasteElectrico() == null) {
                    servicios.setAbasteElectrico(catalogoItemService.getDefaultItem("energia.electrica.proviene"));
                }
                if (servicios.getEvacAguasServ() == null) {
                    servicios.setEvacAguasServ(catalogoItemService.getDefaultItem("predio.evac_aguas_serv"));
                }
                if (servicios.getRecolBasura() == null) {
                    servicios.setRecolBasura(catalogoItemService.getDefaultItem("predio.recol_basura_medio"));
                }
                if (servicios.getUnidadmAreaEscritura() == null) {
                    servicios.setUnidadmAreaEscritura(catalogoItemService.getDefaultItem("edif.unidad_medida"));
                }
            }
        } else {
            System.out.println("No se puede cargar catalogos por default......");
        }
    }

    public void cargarDoc() {

    }

    public void cargarPrestamos() {

    }

    public void openDialog() {

    }

    public Boolean esPredioRural() {
        return predio.getTipoPredio().equalsIgnoreCase("R");
    }

    public void clasificacionSueloRural(CatPredioClasificRural cr) {

    }

    public void procesarClasificSueloRural() {

    }

    public void cargarDatosEspaciales() {
        try {
            if (predio.getEstado().equals(EstadosPredio.PENDIENTE)) {
//                if (ss.getParametros().containsKey("idPrediosTx")) {
//                    predioGraficoServ = dataBaseIb.getDatosGraficosPredio(Integer.valueOf(this.ss.getParametros().get("idPrediosTx").toString()));
//                } else if (this.predio.getPredioRaiz() != null) {
//                    CatPredio predioRaiz = getPredioRaiz();
//                    predioGraficoServ = dataBaseIb.getDatosGraficosPredio(predioRaiz);
//                }
            } else {
//                if (this.ss.getParametros().containsKey("idPrediosTx")) {
//                    predioGraficoServ = dataBaseIb.getDatosGraficosPredio(Integer.valueOf(this.ss.getParametros().get("idPrediosTx").toString()));
//                } else {
//                    predioGraficoServ = dataBaseIb.getDatosGraficosPredio(predio);
//                }
            }
            if (predioGraficoServ == null) {
                predioGraficoServ = new PredioAllService();
            }
            if (predioGraficoServ.getArea() != null) {
                predio.setAreaSolar(predioGraficoServ.getArea());
                caracteristicas.setAreaGraficaLote(predioGraficoServ.getArea());
                if (this.caracteristicas.getFrente1() != null) {
                    if (predioGraficoServ.getFrente() != null) {
                        if (caracteristicas.getFrente1().doubleValue() != predioGraficoServ.getFrente().doubleValue()) {
                            this.caracteristicas.setFrente1(predioGraficoServ.getFrente());
                        }
                    }
                } else {
                    this.caracteristicas.setFrente1(predioGraficoServ.getFrente());
                }
                if (this.caracteristicas.getFondo1() != null) {
                    BigDecimal fondR = predioGraficoServ.getArea().divide(caracteristicas.getFrente1()).setScale(3);
                    if (fondR.doubleValue() != fondR.doubleValue()) {
                        caracteristicas.setFondo1(fondR);
                    }
                } else {
                   caracteristicas.setFondo1((predioGraficoServ.getArea().divide(caracteristicas.getFrente1())).setScale(3));
                }
            }
        } catch (Exception e) {
            System.out.println("no se encontro grafico " + getPredio().getClaveCat() + " tipo predio " + getPredio().getTipoPredio());
        }

    }

    public String claveCatastralCompleta() {
        if (getPredio() == null) {
            return null;
        }
        String a = "";
        String prov = "";
        String clave = getPredio().getClaveCat();
        if (clave.length() < 22) {
            a = getPredio().getClaveCat();
        } else {
            prov = clave.substring(0, 2);
            if (prov.equals("99")) {
                prov = String.format("%02d", SisVars.PROVINCIA);
            }
            a = clave.substring(2, clave.length());
            a = prov + a;
        }
        return a;
    }

    public Boolean guardarDatosPredio(String observacion, Cliente informante) {
        try {
            if (observacion == null || observacion.equals("")) {
                observacion = "Actualizacion Informacion de Datos catastrales ";
            }
            if (getPredio() != null) {
//                try {
//                    getPredio().setClaveCat(claveCatastral(predio));
//                } catch (Exception e) {
//                }
                if (servicios != null) {
                    if (Utils.isNotEmpty(usosA)) {
                        servicios.setUsosList(usosA);
//                        getPredio().setCatPredioS6(catPredioS6Services.guardarPredioS6(servicios));
                    }
                }
                this.actualizarAreasBloques();
                if (getPredio() != null) {
                    getPredio().setCatPredioS4(catPredioS4Services.guardarPredioS4(caracteristicas));
//                    setPredio(catas.guardarPredio(getPredio()));
                    if (getPredio() != null) {
//                        getPredio().setCatPredioPropietarioCollection(propietarios);
//                        if (saveHistoric(getPredio(), observacion, null, null, null, null)) {
//                            servicios = getPredio().getCatPredioS6();
//                            this.propietarios = (List<CatPredioPropietario>) getPredio().getCatPredioPropietarioCollection();
//                            System.out.println("this.propietarios " + this.propietarios);
//                            return true;
//                        } else {
//                            JsfUtil.addWarningMessage("Advertencia", "Problemas al guardar Datos Historicos del Predio");
//                            return false;
//                        }
                    } else {
                        System.out.println("Ocurrio un error al guardar datos");
                    }
                } else {
                    JsfUtil.addWarningMessage("Advertencia!", "Ha ocurrido un error al actualizar la informacion predial, verifique que los campos esten ingresados correctamente");
                }
            }
        } catch (Exception e) {
            Logger.getLogger(FichaPredialMB.class.getName()).log(Level.SEVERE, null, e);
        }
        return false;
    }

    public void actualizarAreasBloques() {
        if (esPh()) {
            this.calcularAreaConsAlic();
        } else {
            getPredio().setAreaDeclaradaConst(sumarAreaConst());
        }
        if (Utils.isEmpty(bloques) && !getPredio().getPropiedadHorizontal()
                && !getPredio().getFichaMadre()) {
            getPredio().setAvaluoConstruccion(BigDecimal.ZERO);
            getPredio().setAvaluoMunicipal(getPredio().getAvaluoSolar());
        }
    }

    public void calcularAreaConsAlic() {
        if (esPh()) {
            if (getPredio().getAlicuotaConst() == null) {
                JsfUtil.addInformationMessage("Debe ingresar la alicuita de construccion.", "");
                return;
            }
            if (getPredio().getPredioRaiz() == null) {
                JsfUtil.addInformationMessage("No tiene asociado un predio matriz.", "");
                return;
            }
            CatPredio pr = catas.getPredioId(getPredio().getPredioRaiz().longValue());

            getPredio().setAreaDeclaradaConst(getAreaConstSegunAlicuota(pr));
            getPredio().setAvaluoConstruccion((getPredio().getAlicuotaConst().divide(BigDecimal.valueOf(100.00))).multiply(pr.getAvaluoConstruccion()));
            getPredio().setAvaluoMunicipal(getPredio().getAvaluoConstruccion().add(getPredio().getAvaluoSolar()));
            getPredio().setBaseImponible(getPredio().getAvaluoConstruccion().add(getPredio().getAvaluoSolar()));
            verificarAreaAumento();
        }
    }

    public void verificarAreaAumento() {
        BigDecimal areaAumento = BigDecimal.ZERO;
        if (this.esPh()) {
            areaAumento = this.sumarAreaConst();
            if (areaAumento.doubleValue() > 0) {
                getPredio().setAreaAumentoCons(areaAumento.subtract(getPredio().getAreaDeclaradaConst()));
                getPredio().setAreaTotalCons(areaAumento);
            } else {
                getPredio().setAreaTotalCons(getPredio().getAreaDeclaradaConst());
            }
        }
    }

    public BigDecimal getAreaConstSegunAlicuota(CatPredio pr) {
        return (getPredio().getAlicuotaConst().divide(BigDecimal.valueOf(100.00))).multiply(pr.getAreaDeclaradaConst());
    }

    public void observacionesEliminar(Integer tipoEliminar) {
        this.tipoEliminar = tipoEliminar;
        if (this.tipoEliminar == 1) {
            if (this.propietarios.size() == 1) {
                JsfUtil.addErrorMessage("Propietario", "No se pueden eliminar todos los propietarios.");
                return;
            }
        }
        observacionEliminar = null;
        JsfUtil.executeJS("PF('dlgConfirmarEliminacion').show()");
        JsfUtil.update("frmConfirmarEliminacion");
    }

    public void init() {
        predioAnt = "";
        fichaEdifAnt = "";
        fichaEdifAct = "";
        fichaModelAct = "";
        fichaModelAnt = "";
        this.mainConfig = new MainConfig();
    }

    public List<CatPredioPropietario> getCiudadano(List<CatPredioPropietario> propietariosTemp) {
        List<CatPredioPropietario> props = new ArrayList<>();
        for (CatPredioPropietario cpp : propietariosTemp) {
            props.add(cpp);
        }
        return props;

    }

    private boolean verificarPorcentajePosession() {
        if (Utils.isNotEmpty(propietarios)) {
            for (CatPredioPropietario pp : propietarios) {
                if (pp.getTipo().getOrden() == 1) {
                    return pp.getPorcentajePosecion().doubleValue() == 0;
                }
            }
        }
        return false;
    }

    public void propietario(CatPredioPropietario propietario) {
        if (!seccion1()) {
            return;
        }
        if (propietario == null) {
            if (verificarPorcentajePosession()) {
                JsfUtil.addErrorMessage("Info.", "No se puede agregar propietario ya esta completo el 100 % de acciones.");
                return;
            }
        }
        Map<String, List<String>> params = new HashMap<>();
        List<String> p = new ArrayList<>();
        p.add(predio.getId().toString());
        params.put("idPredio", p);
        p = new ArrayList<>();
        if (propietario != null) {
            if (propietario.getId() != null) {
                p.add(propietario.getId().toString());
            } else {
                JsfUtil.addErrorMessage("Error.", "Propietario no puede ser editado.");
                return;
            }
        }
        params.put("idCatPredioPro", p);
        p = new ArrayList<>();
        if (propietario == null) {
            p.add("true");
        } else {
            p.add("false");
        }
        params.put("nuevo", p);
        p = new ArrayList<>();
        if (propietario == null) {
            p.add("true");
        } else {
            p.add("false");
        }
        p = new ArrayList<>();
        p.add(editable.toString());
        params.put("editar", p);
        p = new ArrayList<>();
        p.add("false");
        params.put("persistir", p);
        ss.addParametro("propietarios", this.propietarios);
//        Utils.openDialog("/resources/dialog/propietarios_1","400", "80", params, null);
    }

    public void procesarPropietario(SelectEvent event) {
        CatPredioPropietario propietario = (CatPredioPropietario) event.getObject();
        if (propietario != null) {
            BigDecimal porcentaje = BigDecimal.valueOf(100.00);
            if (propietario.getId() != null) {
                if (!propietarios.contains(propietario)) {
                    propietarios.add(propietario);
                } else {
                    propietarios.set(propietarios.indexOf(propietario), propietario);
                }
            } else {
                propietarios.add(propietario);
            }
            for (CatPredioPropietario p : propietarios) {
                if (p.getTipo().getOrden() != 1) {
                    porcentaje = porcentaje.subtract(p.getPorcentajePosecion());
                }
            }
            for (CatPredioPropietario pp : propietarios) {
                if (pp.getTipo().getOrden() == 1) {
                    pp.setPorcentajePosecion(porcentaje);
                }
                if (pp.getCopropietario() != null) {
                    if (pp.getEstado().equalsIgnoreCase("A") && pp.getCopropietario()) {
                        coopropietarios = true;
                    }
                }
            }
            JsfUtil.update("tdatos:frmEscrituras:dtPropietarios");
        }
    }

    public void saveEscrituraControl() {
        cantones = cantonService.getCantones();
        controlAddUp = true;
    }

    public void saveEscrituraControl(CatEscritura propietario) {
        if (!seccion1()) {
            return;
        }
        Map<String, List<String>> params = new HashMap<>();
        List<String> p = new ArrayList<>();
        p.add(predio.getId().toString());
        params.put("idPredio", p);
        p = new ArrayList<>();
        if (propietario != null && propietario.getIdEscritura() != null) {
            p.add(propietario.getIdEscritura().toString());
        }
        params.put("idEscritura", p);
        p = new ArrayList<>();
        if (propietario == null) {
            p.add("true");
        } else {
            p.add("false");
        }
        params.put("nuevo", p);
        p = new ArrayList<>();
        if (propietario == null) {
            p.add("true");
        } else {
            p.add("false");
        }
        p = new ArrayList<>();
        p.add(editable.toString());
        params.put("editar", p);
//        Utils.openDialog("/resources/dialog/escritura", "350", "80",params);
    }

    public Boolean seccion1() {
        if (!this.ver) {
            if (Utils.isEmpty(usosA)) {
                JsfUtil.addWarningMessage("Advertencia!", "Debe seleccionar por lo menos un uso del predio.");
                return false;
            }
        }
        return true;
    }

    public void cargarQueryPar() {
        if (predioId != null) {
            predioLink = Long.valueOf(predioId);
        }
    }


    public void procesarEscritura(SelectEvent event) {
        escriturasConsulta = catEscrituraServices.getEscriturasByPredio(predio);
        this.predio.setCatEscrituraCollection(escriturasConsulta);
        escritura = (CatEscritura) event.getObject();
        if (saveHistoric(predio, "ACTUALIZACION DE IDENTIFICACION LEGAL", getFichaEdifAnt(), getFichaEdifAct(),
                getFichaModelAnt(), getFichaModelAct())) {
            JsfUtil.addInformationMessage("Exito", "Datos grabados Satisfactoriamente");
        } else {
            JsfUtil.addInformationMessage("Exito", "Datos grabados Satisfactoriamente");
        }
        if (esProcesoActualizacionAreasLind()) {
            this.getSumaAreaPredioDesmenbraciones();
            this.getCalcularExcedenteDiferencia();
            this.getPorcentajeExcedenteDiferencia();
//            JsfUtil.update("tdatos:frmDatosPredio:tvUbicacion:infAdcActAreasLinderos");
//            JsfUtil.update("tdatos:frmDatosPredio:tvUbicacion:infAdcActAreasLinderos");
//            JsfUtil.update("tdatos:frmDatosPredio:tvUbicacion:infAdcActAreasLinderos");
        }
    }


    public BigDecimal getCalcularExcedenteDiferencia() {
        if (escritura == null) {
            return BigDecimal.ZERO;
        }
        if (escritura.getAreaSolar() == null && !esPosesion()) {
            JsfUtil.addErrorMessage("Advertencia", "El area de escritura debe ser ingresado.");
            return BigDecimal.ZERO;
        }
        if (escritura.getAreaSolar() == null) {
            escritura.setAreaSolar(BigDecimal.ZERO);
        }
        // Validamos que el area de la escritura sea ingresada.
        // Restamos el area de escritura menos la suma de (area plano + desmenbraciones)
        BigDecimal total = getSumaAreaPredioDesmenbraciones().subtract(escritura.getAreaSolar());
        caracteristicas.setSuperficie(total);
        return caracteristicas.getSuperficie();
    }

    public BigDecimal getPorcentajeExcedenteDiferencia() {
        if (!esPosesion()) {
            if (escritura.getAreaSolar() == null) {
                JsfUtil.addErrorMessage("Advertencia", "El area de escritura debe ser ingresado.");
                escritura.setAreaSolar(BigDecimal.ZERO);
                return BigDecimal.ZERO;
            }
            // Validamos que el area de la escritura sea ingresada.
            if (escritura.getAreaSolar().compareTo(BigDecimal.ZERO) <= 0) {
                JsfUtil.addWarningMessage("Advertencia", "El area de escritura debe ser mayor a cero.");
                return BigDecimal.ZERO;
            }
        }
        if (caracteristicas.getSuperficie() == null) {
            caracteristicas.setSuperficie(BigDecimal.ZERO);
        }
        // Restamos el area de escritura menos la suma de (area plano + desmenbraciones)
        if (escritura.getAreaSolar().doubleValue() > 0 && caracteristicas.getSuperficie().doubleValue() > 0) {
//            caracteristicas.setFrenteTotal((caracteristicas.getSuperficie().divide(escritura.getAreaSolar(), 5, RoundingMode.HALF_UP)).multiply(BigDecimal.valueOf(100.00)));
        }
        if (caracteristicas.getFrenteTotal() == null) {
            return BigDecimal.ZERO;
        }
        return caracteristicas.getFrenteTotal();
    }

    private Boolean esPosesion() {
        return predio.getFormaAdquisicion() != null && (predio.getFormaAdquisicion().getCodigo().equals("posesion")
                || predio.getFormaAdquisicion().getCodigo().equals("mostrenco"));
    }

    public BigDecimal getSumaAreaPredioDesmenbraciones() {
        if (escritura.getAreaConstruccion() == null) {
            escritura.setAreaConstruccion(BigDecimal.ZERO);
        }
        return predio.getAreaSolar().add(escritura.getAreaConstruccion());
    }

    public void bloque(CatPredioEdificacion bloque) {
        if (!this.estaEdificado()) {
            if (caracteristicas.getEstadoSolar() == null) {
                if (!seccion3()) {
                    return;
                }
            }
            JsfUtil.addWarningMessage("Advertencia",
                    "El predio tiene Ocupacion: ." + caracteristicas.getEstadoSolar().getValor());
            return;
        }
        if (!seccion1()) {
            return;
        }
        if (!seccion2()) {
            return;
        }
        if (!seccion3()) {
            return;
        }

        Map<String, List<String>> params = new HashMap<>();
        List<String> p = new ArrayList<>();
        p.add(predio.getId().toString());
        params.put("idPredio", p);
        p = new ArrayList<>();
        if (bloque != null && bloque.getId() != null) {
            p.add(bloque.getId().toString());
            params.put("idCatPredioBloq", p);
            // Si el predio fue editado en el grafico buscamos el registro del bloque
            if (predioGraficoServ != null) {
                if (predioGraficoServ.getBloques() != null) {
                    predioGraficoServ.getBloques().stream().filter(bdg -> (Objects.equals(bdg.getNumBloq(), bloque.getNoEdificacion()))).forEachOrdered(bdg -> {
                        ss.addParametro(keyGeoBloque, bdg);
                    });
                }
            }
        } else {
            int num = 0;
            for (CatPredioEdificacion b : bloques) {
                if (b.getNoEdificacion() > num) {
                    num = b.getNoEdificacion();
                }
            }
            ss.addParametro(keyGeoBloque, this.predioGraficoServ.getBloque(num + 1));
        }

        p = new ArrayList<>();
        p.add(Boolean.toString((bloque == null)));
        params.put("nuevo", p);
        p = new ArrayList<>();
        p.add(ver.toString());
        params.put("ver", p);
        ver = false;
        Utils.openDialog("/resources/dialog/edificacionesPredio", "480", "480", params);
    }

    public Boolean seccion2() {
        if (Objects.isNull(predio.getPropiedad())) {
            JsfUtil.addWarningMessage("Advertencia!", "Debe seleccionar la Tenencia.");
            return false;
        }
        if (Objects.isNull(predio.getFormaAdquisicion())) {
            JsfUtil.addWarningMessage("Advertencia!", "Debe seleccionar la Forma de Adquisición o Tenencia.");
            return false;
        }
        if (Utils.isNotEmpty(propietarios)) {
            BigDecimal porcentajePosession = BigDecimal.ZERO;
            boolean existeTitular = false;
            int count = 0;
            for (CatPredioPropietario propietario : propietarios) {
                if (propietario.getEstado().equalsIgnoreCase("A")) {
                    porcentajePosession = porcentajePosession.add(propietario.getPorcentajePosecion());
                    if (propietario.getTipo().getOrden() == 1) {
                        existeTitular = true;
                        count++;
                    }
                }
            }
        }
        if (ss.getParametros() != null) {
            if (ss.getParametros().get("proceso") != null) {
                TipoProceso proceso = (TipoProceso) ss.getParametros().get("proceso");
                if (proceso.equals(TipoProceso.ACTIALIZAR_AREAS_LINDEROS)) {

                }
            }
        }
        return true;
    }

    public Boolean seccion3() {
        for (CatPredioPropietario propietario : propietarios) {
            if (propietario.getEstado().equalsIgnoreCase("A")) {
                if (propietario.getId() == null) {
                    JsfUtil.addWarningMessage("Advertencia!", "Debe guardar los datos de propietarios en la seccion 2.");
                    return false;
                }
            }
        }
        if (Objects.isNull(caracteristicas.getLocManzana())) {
            JsfUtil.addWarningMessage("Advertencia!", "Debe seleccionar la Localizacion en Mz.");
            return false;
        }
//        if (!this.estaEdificado() && Utils.isNotEmpty(predio.getCatPredioEdificacionCollection())) {
//            JsfUti.messageWarning(null, "Advertencia!", "Debe eliminar los bloques para continuar.");
//            return false;
//        }
        if (Objects.isNull(caracteristicas.getEstadoSolar())) {
            JsfUtil.addWarningMessage("Advertencia!", "Debe seleccionar la Ocupacion.");
            return false;
        }
//        if (Objects.isNull(predio.gett getTipoSuelo())) {
//            JsfUtil.addWarningMessage("Advertencia!", "Debe seleccionar la Caract. Suelo.");
//            return false;
//        }
        if (Objects.isNull(predio.getTopografiaSolar())) {
            JsfUtil.addWarningMessage("Advertencia!", "Debe seleccionar la Topografia.");
            return false;
        }
        if (Objects.isNull(predio.getFormaSolar())) {
            JsfUtil.addWarningMessage("Advertencia!", "Debe seleccionar la Forma del predio.");
            return false;
        }
        if (Objects.isNull(predio.getConstructividad())) {
            JsfUtil.addWarningMessage("Advertencia!", "Debe seleccionar la Constructividad.");
            return false;
        }
        if (Utils.isNotEmpty(predio.getCatPredioClasificRuralCollection())) {
            for (CatPredioClasificRural cpc : predio.getCatPredioClasificRuralCollection()) {
                if (cpc.getValorTerreno() == null) {
                    JsfUtil.addWarningMessage("Error!", "Debe Valorar la calidad del suelo del predio.");
                    return false;
                }
            }
        }
        return true;
    }

    public Boolean estaEdificado() {
        try {
            if (predio == null) {
                return false;
            }
            if (!predio.getPropiedadHorizontal()) {
                if (predio != null && caracteristicas != null) {
                    if (caracteristicas.getEstadoSolar() != null) {
                        return caracteristicas.getEstadoSolar().getId() == 58
                                || !caracteristicas.getEstadoSolar().getDescripcion().equals("EDIFICADO");
                    }
                }
            } else {
                return true;
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "", e);
        }
        return false;
    }

    public void obraInterna(CatPredioObraInterna obraInterna) {
        if (!seccion1()) {
            return;
        }
        if (!seccion2()) {
            return;
        }
        if (!seccion3()) {
            return;
        }
        Map<String, List<String>> params = new HashMap<>();
        List<String> p = new ArrayList<>();
        p.add(predio.getId().toString());
        params.put("idPredio", p);
        p = new ArrayList<>();
        if (obraInterna != null && obraInterna.getId() != null) {
            p.add(obraInterna.getId().toString());
            params.put("idCatPredioObraInterna", p);
        }

        p = new ArrayList<>();
        p.add(Boolean.toString((obraInterna == null)));
        params.put("nuevo", p);

        p = new ArrayList<>();
        p.add(ver.toString());
        params.put("ver", p);
        ver = false;
        Utils.openDialog("/resources/dialog/obraInterna", params);
    }

    public void procesarObraInterna(SelectEvent event) {
        CatPredioObraInterna obraInterna = (CatPredioObraInterna) event.getObject();
        if (Utils.isEmpty(obrasInternas)) {
            obrasInternas = new ArrayList<>();
        }
        if (obraInterna != null) {
            if (!obrasInternas.contains(obraInterna)) {
                obrasInternas.add(obraInterna);
            } else {
                obrasInternas.set(obrasInternas.indexOf(obraInterna), obraInterna);
            }
            if (this.predio.getAvaluoObraComplement() == null) {
                this.predio.setAvaluoObraComplement(BigDecimal.ZERO);
            }
            predio.setAreaObras(sumarObras());
            predio.setAvaluoObraComplement(sumarAvaluoObras());
            predio.setCatPredioObraInternaCollection(obrasInternas);
            if (this.saveHistoric(predio, "ACTUALIZACION CARACTERIZACION Y ELEMENTOS CONSTRUCTIVOS DE LA EDIFICACION",
                    null, null, null, null)) {
                JsfUtil.addInformationMessage("Nota!", "Obras Internas actualizadas satisfactoriamente");
            } else {
                JsfUtil.addInformationMessage("Nota!", "Ocurrio un error al guardar Obras Internas");
            }
        }
    }

    private BigDecimal sumarObras() {
        try {
            if (Utils.isEmpty(obrasInternas)) {
                return BigDecimal.ZERO;
            }
            BigDecimal area = BigDecimal.ZERO;
            for (CatPredioObraInterna obrasInterna : obrasInternas) {
                if (obrasInterna.getArea() == null) {
                    obrasInterna.setArea(BigDecimal.ZERO.toBigInteger());
                }
                area = area.add(new BigDecimal(obrasInterna.getArea()));
            }
            return area;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, usr, e);
        }
        return BigDecimal.ZERO;
    }

    private BigDecimal sumarAvaluoObras() {
        try {
            if (Utils.isEmpty(obrasInternas)) {
                return BigDecimal.ZERO;
            }
            BigDecimal avaluo = BigDecimal.ZERO;
            for (CatPredioObraInterna obrasInterna : obrasInternas) {
                if (obrasInterna.getAvaluo() == null) {
                    obrasInterna.setAvaluo(BigDecimal.ZERO.toBigInteger());
                }
                avaluo = avaluo.add(new BigDecimal(obrasInterna.getAvaluo()));
            }
            return avaluo;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, usr, e);
        }
        return BigDecimal.ZERO;
    }
    public void saveCensalService() {
        try {
            obs = "Vivienda censal.";
            if (predio.getCatPredioS6() != null) {
                servicios.setPredio(predio);
            } else {
                servicios = new CatPredioS6();
                servicios.setPredio(predio);
            }
        } catch (NumberFormatException ne) {
            LOG.log(Level.SEVERE, "Obtener datos ficha saveCensalService", ne);
        }
        try {
            this.guardarDatosPredio(obs, informante);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Guardar saveCensalService", e);
        }

    }
    public void guardarCaracteristicas(CatPredioS4 s4, CatPredio p, CatPredioS6 s6) {
        try {
            obs = "ACTUALIZACION INFORMACION DE CARACTERISTICAS";
            servicios = s6;
            if (this.esPh()) {
                this.calcularAreaConsAlic();
                // this.calcularAreaTerrAlic();
            }
            if (s4 != null) {
                caracteristicas = catas.guardarPredioS4(caracteristicas);
                predio.setCatPredioS4(caracteristicas);
            }
            if (this.guardarDatosPredio(obs, informante)) {
//                JsfUtil.update("tdatos:frmVivienda");
//                JsfUtil.update("tdatos:frmVivienda:fieldJH");
//                JsfUtil.update("tdatos:frmVivienda:fieldNV");
//                JsfUtil.update("tdatos:frmVivienda:pngObsVn");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }
     public void buscarCiudadano(ActionEvent event) {
        try {
            Cliente ciudadanoEnte = null;
            switch (event.getComponent().getId()) {
                case "btnFiscalizadorResponsable":
                    if (predio.getCiuFiscalizador() == null) {
                        JsfUtil.addErrorMessage("Error", "debe Ingresar el numero de identificacion");
                        return;
                    }
                    ciudadanoEnte = getCatEnteByParemt(predio.getCiuFiscalizador());
                    if (ciudadanoEnte == null) {
                        JsfUtil.addErrorMessage("Error", "No se encontro ciudadano");
                        return;
                    }
                    predio.setCiuFiscalizador(ciudadanoEnte.getIdentificacion());
                    predio.setCiuNombreFiscalizador(ciudadanoEnte.getNombreCompleto());
//                    JsfUtil.update(":tdatos:frmResponsables:actFisc");
                    break;
                case "btnActualizadorResponsable":
                    if (predio.getCiuActualizador() == null) {
                        JsfUtil.addErrorMessage("Error", "debe Ingresar el numero de identificacion");
                        return;
                    }
                    ciudadanoEnte = getCatEnteByParemt(predio.getCiuActualizador());
                    if (ciudadanoEnte == null) {
                        JsfUtil.addErrorMessage("Error", "No se encontro ciudadano");
                        return;
                    }
                    predio.setCiuActualizador(ciudadanoEnte.getIdentificacion());
                    predio.setCiuNombreActualizador(ciudadanoEnte.getNombreCompleto());
//                    JsfUtil.update(":tdatos:frmResponsables:actPredial");
                    break;
                case "btnEnteHorizontal":
                    if (predio.getCiuHorizontal() == null) {
                        JsfUtil.addErrorMessage("Error", "debe Ingresar el numero de identificacion");
                        return;
                    }
                    ciudadanoEnte = getCatEnteByParemt(predio.getCiuHorizontal());
                    if (ciudadanoEnte == null) {
                        JsfUtil.addErrorMessage("Error", "No se encontro ciudadano");
                        return;
                    }
                    predio.setCiuHorizontal(ciudadanoEnte.getIdentificacion());
                    predio.setCiuNombreHorizontal(ciudadanoEnte.getNombreCompleto());
//                    JsfUtil.update("tdatos:frmVivienda:pngJefeHogar");
                    break;
                case "pngObsVn":
                    if (predio.getCiuInformante() == null) {
                        JsfUtil.addErrorMessage("Error", "debe Ingresar el numero de identificacion");
                        return;
                    }
                    ciudadanoEnte =getCatEnteByParemt(predio.getCiuInformante());
                    if (ciudadanoEnte == null) {
                        JsfUtil.addErrorMessage("Error", "No se encontro ciudadano");
                        return;
                    }
                    predio.setCiuInformante(ciudadanoEnte.getIdentificacion());
                    predio.setCiuNombreInformante(ciudadanoEnte.getNombreCompleto());
//                    JsfUtil.update("tdatos:frmViviendaCensal:pngObsVn");
//                    JsfUtil.update("tdatos:frmViviendaCensal:pngObsVn");
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }
      public void limpiarJefeHogar() {
        this.predio.setCiuTipoIdentificacion(null);
        this.predio.setCiuHorizontal(null);
        this.predio.setCiuNombreHorizontal(null);
//        JsfUtil.update("frmVivienda:pngJefeHogar");
    }

    //<editor-fold defaultstate="collapsed" desc="get and set">
    public Short getCodProvincia() {
        return codProvincia;
    }

    public void setCodProvincia(Short codProvincia) {
        this.codProvincia = codProvincia;
    }

    public String getPredioId() {
        return predioId;
    }

    public void setPredioId(String predioId) {
        this.predioId = predioId;
    }

    public Long getPredioLink() {
        return predioLink;
    }

    public void setPredioLink(Long predioLink) {
        this.predioLink = predioLink;
    }

    public Boolean getEsTareaAp() {
        return esTareaAp;
    }

    public void setEsTareaAp(Boolean esTareaAp) {
        this.esTareaAp = esTareaAp;
    }

    public Boolean getEsTarea() {
        return esTarea;
    }

    public void setEsTarea(Boolean esTarea) {
        this.esTarea = esTarea;
    }

    @Override
    public void setMainConfig(MainConfig mainConfig) {
        super.setMainConfig(mainConfig); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public MainConfig getMainConfig() {
        return super.getMainConfig(); //To change body of generated methods, choose Tools | Templates.
    }

    public CatPredioFotosServices getCatPredioFotosServices() {
        return catPredioFotosServices;
    }

    public void setCatPredioFotosServices(CatPredioFotosServices catPredioFotosServices) {
        this.catPredioFotosServices = catPredioFotosServices;
    }

    public String getTaskKey() {
        return taskKey;
    }

    public void setTaskKey(String taskKey) {
        this.taskKey = taskKey;
    }

    public String getUsr() {
        return usr;
    }

    public void setUsr(String usr) {
        this.usr = usr;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

    public String getPredioAnt() {
        return predioAnt;
    }

    public void setPredioAnt(String predioAnt) {
        this.predioAnt = predioAnt;
    }

    public String getFichaEdifAnt() {
        return fichaEdifAnt;
    }

    public void setFichaEdifAnt(String fichaEdifAnt) {
        this.fichaEdifAnt = fichaEdifAnt;
    }

    public String getFichaModelAnt() {
        return fichaModelAnt;
    }

    public void setFichaModelAnt(String fichaModelAnt) {
        this.fichaModelAnt = fichaModelAnt;
    }

    public String getFichaModelAct() {
        return fichaModelAct;
    }

    public void setFichaModelAct(String fichaModelAct) {
        this.fichaModelAct = fichaModelAct;
    }

    public String getClaveCat() {
        return claveCat;
    }

    public void setClaveCat(String claveCat) {
        this.claveCat = claveCat;
    }

    public Boolean getEditable() {
        return editable;
    }

    public void setEditable(Boolean editable) {
        this.editable = editable;
    }

    public CatPredioS6 getServicios() {
        return servicios;
    }

    public void setServicios(CatPredioS6 servicios) {
        this.servicios = servicios;
    }

    public CatEscritura getEscritura() {
        return escritura;
    }

    public void setEscritura(CatEscritura escritura) {
        this.escritura = escritura;
    }

    public List<CatPredioEdificacion> getBloques() {
        return bloques;
    }

    public void setBloques(List<CatPredioEdificacion> bloques) {
        this.bloques = bloques;
    }

    public List<CatPredioPropietario> getPropietarios() {
        return propietarios;
    }

    public void setPropietarios(List<CatPredioPropietario> propietarios) {
        this.propietarios = propietarios;
    }

    public String getJsTarea() {
        return jsTarea;
    }

    public void setJsTarea(String jsTarea) {
        this.jsTarea = jsTarea;
    }

    public List<CatalogoItem> getUsosA() {
        return usosA;
    }

    public void setUsosA(List<CatalogoItem> usosA) {
        this.usosA = usosA;
    }

    public Boolean getControlAddUp() {
        return controlAddUp;
    }

    public void setControlAddUp(Boolean controlAddUp) {
        this.controlAddUp = controlAddUp;
    }

    public BloquePredio getGeoBloqueSeleccionado() {
        return geoBloqueSeleccionado;
    }

    public void setGeoBloqueSeleccionado(BloquePredio geoBloqueSeleccionado) {
        this.geoBloqueSeleccionado = geoBloqueSeleccionado;
    }


    public List<CatalogoItem> getPrototipos() {
        return prototipos;
    }

    public void setPrototipos(List<CatalogoItem> prototipos) {
        this.prototipos = prototipos;
    }

    public List<CatalogoItem> getVias() {
        return vias;
    }

    public void setVias(List<CatalogoItem> vias) {
        this.vias = vias;
    }

    public List<CatalogoItem> getInstalacionesEspeciales() {
        return instalacionesEspeciales;
    }

    public void setInstalacionesEspeciales(List<CatalogoItem> instalacionesEspeciales) {
        this.instalacionesEspeciales = instalacionesEspeciales;
    }

    public List<CatPredioObraInterna> getObrasInternas() {
        return obrasInternas;
    }

    public void setObrasInternas(List<CatPredioObraInterna> obrasInternas) {
        this.obrasInternas = obrasInternas;
    }

    public List<CatPredioClasificRural> getClasificaciones() {
        return clasificaciones;
    }

    public void setClasificaciones(List<CatPredioClasificRural> clasificaciones) {
        this.clasificaciones = clasificaciones;
    }

    public List<CatPredioFotos> getFotos() {
        return fotos;
    }

    public void setFotos(List<CatPredioFotos> fotos) {
        this.fotos = fotos;
    }

    public List<String> getObservacionRestricciones() {
        return observacionRestricciones;
    }

    public void setObservacionRestricciones(List<String> observacionRestricciones) {
        this.observacionRestricciones = observacionRestricciones;
    }

    public int getTipoTarea() {
        return tipoTarea;
    }

    public void setTipoTarea(int tipoTarea) {
        this.tipoTarea = tipoTarea;
    }

    public Boolean getEditarClave() {
        return editarClave;
    }

    public void setEditarClave(Boolean editarClave) {
        this.editarClave = editarClave;
    }

    public String getFichaEdifAct() {
        return fichaEdifAct;
    }

    public void setFichaEdifAct(String fichaEdifAct) {
        this.fichaEdifAct = fichaEdifAct;
    }

    public CatPredioS4 getCaracteristicas() {
        return caracteristicas;
    }

    public void setCaracteristicas(CatPredioS4 caracteristicas) {
        this.caracteristicas = caracteristicas;
    }

    public PredioAllService getPredioGraficoServ() {
        return predioGraficoServ;
    }

    public void setPredioGraficoServ(PredioAllService predioGraficoServ) {
        this.predioGraficoServ = predioGraficoServ;
    }

    public Object getObjectoEliminar() {
        return objectoEliminar;
    }

    public void setObjectoEliminar(Object objectoEliminar) {
        this.objectoEliminar = objectoEliminar;
    }

    public TipoProceso getProceso() {
        return proceso;
    }

    public void setProceso(TipoProceso proceso) {
        this.proceso = proceso;
    }

    public Integer getTipoEliminar() {
        return tipoEliminar;
    }

    public void setTipoEliminar(Integer tipoEliminar) {
        this.tipoEliminar = tipoEliminar;
    }

    public String getObservacionEliminar() {
        return observacionEliminar;
    }

    public void setObservacionEliminar(String observacionEliminar) {
        this.observacionEliminar = observacionEliminar;
    }

    public Boolean getCoopropietarios() {
        return coopropietarios;
    }

    public void setCoopropietarios(Boolean coopropietarios) {
        this.coopropietarios = coopropietarios;
    }

    public Cliente getActualizadorPredio() {
        return actualizadorPredio;
    }

    public void setActualizadorPredio(Cliente actualizadorPredio) {
        this.actualizadorPredio = actualizadorPredio;
    }

    public Cliente getFiscalizadorPredio() {
        return fiscalizadorPredio;
    }

    public void setFiscalizadorPredio(Cliente fiscalizadorPredio) {
        this.fiscalizadorPredio = fiscalizadorPredio;
    }

    public List<Canton> getCantones() {
        return cantones;
    }

    public void setCantones(List<Canton> cantones) {
        this.cantones = cantones;
    }

    public List<CatEscritura> getEscriturasConsulta() {
        return escriturasConsulta;
    }

    public void setEscriturasConsulta(List<CatEscritura> escriturasConsulta) {
        this.escriturasConsulta = escriturasConsulta;
    }
    public String getTipoDocumentoAdj() {
           return tipoDocumentoAdj;
       }

       public void setTipoDocumentoAdj(String tipoDocumentoAdj) {
           this.tipoDocumentoAdj = tipoDocumentoAdj;
       }


//</editor-fold>



}
