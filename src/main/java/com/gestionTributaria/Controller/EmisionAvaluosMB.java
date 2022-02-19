/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Controller;

import com.asgard.Entity.FinaRenDetLiquidacion;
import com.asgard.Entity.FinaRenEstadoLiquidacion;
import com.asgard.Entity.FinaRenLiquidacion;
import com.asgard.Entity.FinaRenRubrosLiquidacion;
import com.catastro.Entities.AvalDetCobroImpuestoPredios;
import com.gestionTributaria.Commons.SisVars;
import com.gestionTributaria.Entities.*;
import com.gestionTributaria.Entities.AvalImpuestoPredios;
import com.gestionTributaria.Entities.CatPredio;
import com.gestionTributaria.Entities.CatPredioPropietario;
import com.gestionTributaria.Services.FinaRenLiquidacionService;
import com.gestionTributaria.Services.ManagerService;

import com.gestionTributaria.models.AniosEmision;
import com.gestionTributaria.models.AvalValorSueloDTO;
import com.gestionTributaria.models.CatParroquiaDTO;
import com.gestionTributaria.models.CatPredioModel;
import com.gestionTributaria.models.PrediosManzanaDTO;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.gestionTributaria.Services.AvalBandaImpositivaService;
import com.gestionTributaria.Services.AvalTasaSeguridadService;

import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.resource.contabilidad.interfaces.ContRegistroContable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Administrator
 */
@Named(value = "avaluosPrediales")
@ViewScoped
public class EmisionAvaluosMB implements Serializable {

    private static final Logger logx = Logger.getLogger(EmisionAvaluosMB.class.getName());
    @Inject
    private ManagerService services;
    @Inject
    private UserSession session;
    @Inject
    protected ServletSession ss;
    @Inject
    private FinaRenLiquidacionService liquidacionService;
    /*AGREGO UNA VAFRIABLE*/
    @Inject
    private AvalBandaImpositivaService avalservice;
    @Inject
    private AvalTasaSeguridadService tasase;
    @Inject
    private ContRegistroContable contableServices;

    private Integer tipoReporte;
    private LazyModel<CatPredio> predios;
    private List<CatPredio> prediosSeleccionados;
    private Integer anioEmisionFin;
    private Integer anioEmisionInicio;
    private AvalBandaImpositiva avalBandaImpositivaSeleccionada;
    private AvalTasaSeguridad avalTasaSeguridadSeleccionada;

    private CatPredio predioSeleccionado;
    private AvalImpuestoPredios avalImpuestoPredios;
    private AvalBandaImpositiva avalBandaImpositiva;
    private AvalBandaImpositiva elecBanda;

    public AvalBandaImpositiva getElecBanda() {
        return elecBanda;
    }

    private List<AvalBandaImpositiva> avalBandaImpositivaList;
    private List<AvalBandaImpositiva> avalBandaImpositivaTodas;

    private List<PrediosManzanaDTO> prediosXManzana;
    private Boolean controlPredeterminada = Boolean.TRUE;
    private String tipoDefinicion = "M";
    private List<AvalImpuestoPredios> impuestoPrediosList;
    private List<Short> zonaList, mzList, sectorList, lotesList;
    private Short zona = null, mz = null, sector = null, lotes = null;
    protected CatParroquia parroquia;
    private List<FinaRenRubrosLiquidacion> rubrosList, rubrosSeleccionados;
    private Map<String, Object> parametros;
    private Integer anioMax;
    private LazyModel<AvalTasaSeguridad> lazyTasaSeguridad;
    private List<AniosEmision> aniosEmisionMax, aniosEmisionSeleccionados;
    private List<FinaRenLiquidacion> liquidacionesGeneradas;
    private Integer anioEmision;
    private Integer condicionDos;
    private Date fechaIngreso;

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }
    private static final String query = "select cp.parroquia, cp.zona, cp.sector, cp.mz, count(solar) cantidadsolar from catastro.cat_predio cp where cp.estado = :estado GROUP BY cp.parroquia, "
            + "cp.zona, cp.sector, cp.mz ORDER BY cp.parroquia, cp.zona, cp.sector, cp.mz asc ";

    @PostConstruct
    public void init() {
        elecBanda = null;
        condicionDos = 1;
        anioEmision = Utils.getAnio(new Date());
        aniosEmisionSeleccionados = new ArrayList<>();
        aniosEmisionMax = new ArrayList<>();
        zonaList = new ArrayList<>();
        mzList = new ArrayList<>();
        sectorList = new ArrayList<>();
        avalTasaSeguridadSeleccionada = new AvalTasaSeguridad();
        prediosSeleccionados = new ArrayList<>();
        avalBandaImpositivaList = services.getBandaImpositivaActivas("A");
        avalBandaImpositivaTodas = services.getBandaImpositivaTodas();
        predios = new LazyModel<>(CatPredio.class);
        //predios.addFilter("avaluoMunicipal:gt", new BigDecimal("0.00"));
        //prediosXManzana = services.getSqlQueryParametros(PrediosManzanaDTO.class, query, new String[]{"estado"}, new Object[]{"A"});
        avalBandaImpositiva = new AvalBandaImpositiva();
        avalBandaImpositivaSeleccionada = new AvalBandaImpositiva();
        avalImpuestoPredios = new AvalImpuestoPredios();
        impuestoPrediosList = services.getAvalImpuestoPrediosActivo("A");
        validatePredeterminada(false, null);
        parametros = new HashMap<>();
        // 13=> pagos de predios 
        parametros.put("rural", Arrays.asList(2L, 3L));

        rubrosList = new ArrayList<>();
        fechaIngreso = new Date();
//        rubrosList = services.findAllQuery("SELECT r FROM FinaRenRubrosLiquidacion r WHERE r.tipoLiquidacion.id in (:rural) AND r.estado = true AND r.codigoRubro IS NOT NULL ORDER BY r.codigoRubro ASC", parametros);
//        if (rubrosList.isEmpty()) {
//            rubrosList = new ArrayList<>();
//        }
        rubrosSeleccionados = new ArrayList();
        anioMax = (Utils.getAnio(new Date()) - 6);
        lazyTasaSeguridad = new LazyModel<>(AvalTasaSeguridad.class);
        lazyTasaSeguridad.getFilterss().put("estado", "A");
        loadAnios();
    }

    public void loadAnios() {
        AniosEmision aniosEmision = null;
        for (int i = anioMax; i <= (Utils.getAnio(new Date()) + 1); i++) {
            aniosEmision = new AniosEmision();
            aniosEmision.setId(i);
            aniosEmision.setAnioEmision(i);
            aniosEmisionMax.add(aniosEmision);
        }
    }

    public void saveAvalBandaImpositiva(Boolean control) {
        if (avalBandaImpositiva.getId() != null) {
            avalservice.edit(avalBandaImpositiva);
            JsfUtil.update("frmMain");
            avalBandaImpositiva = new AvalBandaImpositiva();
        } else {
            if (avalBandaImpositiva != null) {
                if (avalBandaImpositiva.getAnioFin() != null || avalBandaImpositiva.getAnioInicio() != null || avalBandaImpositiva.getMultiploImpuestoPredial() != null) {
                    if (validateAnio(avalBandaImpositiva.getAnioInicio(), avalBandaImpositiva.getAnioFin())) {
                        AvalBandaImpositiva bandaImpositiva;
                        if (control == Boolean.TRUE) {
                            this.avalBandaImpositiva.setPredeterminada(Boolean.TRUE);
                        }
                        System.out.println("no debería de entrar aquí");
                        bandaImpositiva = (AvalBandaImpositiva) avalservice.create(this.avalBandaImpositiva);
                        if (bandaImpositiva != null) {
                            mensajeCorrecto();
                            this.avalBandaImpositiva = new AvalBandaImpositiva();
                            avalBandaImpositivaList.add(bandaImpositiva);
                            if (control == Boolean.TRUE) {
                                saveBandaImpositivaPorParroquiaZonaSectorMz(bandaImpositiva);
                            }
                            validatePredeterminada(false, null);
                        } else {
                            mensajeError();
                        }
                    }
                } else {
                    mensajeAdvertencia();
                }
            } else {
                mensajeAdvertencia();
            }
        }
    }

    public void saveTasaSeguridad(Boolean control) {
        //avalTasaSeguridadSeleccionada
        if (avalTasaSeguridadSeleccionada.getId() != null) {
            tasase.edit(avalTasaSeguridadSeleccionada);
            JsfUtil.update("frmMain");
            avalTasaSeguridadSeleccionada = new AvalTasaSeguridad();
        } else {
            if (avalTasaSeguridadSeleccionada != null) {
                if (avalTasaSeguridadSeleccionada.getTarifa() != null || avalTasaSeguridadSeleccionada.getFechaIngreso() != null || avalTasaSeguridadSeleccionada.getPeriodo() != null) {
//                if (validateAnio(avalBandaImpositiva.getAnioInicio(), avalBandaImpositiva.getAnioFin())) {
                    AvalTasaSeguridad bandaImpositiva;
//                    if (control == Boolean.TRUE) {
//                        this.avalBandaImpositiva.setPredeterminada(Boolean.TRUE);
//                    }
                    System.out.println("no debería de entrar aquí");
                    bandaImpositiva = (AvalTasaSeguridad) services.save(this.avalTasaSeguridadSeleccionada);
                    if (bandaImpositiva != null) {
                        mensajeCorrecto();
                        this.avalTasaSeguridadSeleccionada = new AvalTasaSeguridad();
//                        lazyTasaSeguridad.add(avalTasaSeguridadSeleccionada);
//                        if (control == Boolean.TRUE) {                            
//                            saveBandaImpositivaPorParroquiaZonaSectorMz(bandaImpositiva);
//                        }
//                        validatePredeterminada(false, null);
                    } else {
                        mensajeError();
                    }

                }
            } else {
                mensajeAdvertencia();
            }
        }
    }

    public Boolean validateAnio(Integer anioInicio, Integer anioFin) {
        Boolean result = Boolean.TRUE;
        if (anioInicio == 0
                || anioInicio > Utils.getAnio(new Date()) || anioInicio < 2000
                || anioFin == 0
                || anioFin < Utils.getAnio(new Date()) || anioFin < 2000) {
            mensajeAdvertencia();
            return result.FALSE;
        }
        return result;
    }

    public void saveBandaImpositivaPorParroquiaZonaSectorMz(AvalBandaImpositiva bandaImpositiva) {
        AvalBandaImpositiva aipTemp = (AvalBandaImpositiva) services.save(this.avalImpuestoPredios);
    }

    /*REPORTE DE LAS EMISIONES PREDIOALES*/
    public void generarReporteEmision(Boolean excel, Integer tipoReporte) {
        try {
            ss.borrarDatos();
            ss.instanciarParametros();
            ss.setNombreSubCarpeta("recaudaciones");
            //// ss.setTieneDatasource(true);

            String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");
            ss.addParametro("TIPO_LIQUIDACION", 13L);
            ss.addParametro("ANIO", anioEmisionInicio);
            ss.addParametro("USUARIO", session.getNameUser());
            ss.addParametro("LOGO", path + SisVars.sisLogo);
            ss.setNombreReporte("saldoPorRubrosPrediales");
            if (excel) {
                JsfUtil.redirectNewTab(CONFIG.URL_APP + "DocumentoExcel");
            } else {
                JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
            }
        } catch (Exception e) {
            Logger.getLogger(EmisionAvaluosMB.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void reportePredioTitulosPrediales(Boolean excel) {
        try {
            ss.borrarDatos();
            ss.instanciarParametros();
            ss.setNombreSubCarpeta("recaudaciones");
            // ss.setTieneDatasource(true);
            ss.addParametro("SUBREPORT_DIR", JsfUtil.getRealPath("/") + "reportes/");
            String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");
            //ss.addParametro("TIPO_LIQUIDACION", 13L);
            ss.addParametro("ANIO", anioEmisionInicio);
            ss.addParametro("USUARIO", session.getNameUser());
            ss.addParametro("LOGO", path + SisVars.sisLogo);
            ss.setNombreReporte("detalleEmisionCatastral");
            if (excel) {
                JsfUtil.redirectNewTab(CONFIG.URL_APP + "DocumentoExcel");
            } else {
                JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
            }

        } catch (NumberFormatException e) {
            Logger.getLogger(EmisionAvaluosMB.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void validatePredeterminada(Boolean control, AvalBandaImpositiva impositiva) {
        if (avalBandaImpositivaList != null && !avalBandaImpositivaList.isEmpty()) {
            for (AvalBandaImpositiva abi : avalBandaImpositivaList) {
                if (abi.getPredeterminada()) {
                    this.controlPredeterminada = Boolean.FALSE;
                    if (control && impositiva != null) {
                        if (!abi.equals(impositiva)) {
                            abi.setPredeterminada(Boolean.FALSE);
                            services.save(abi);
                        }
                    }
                } else {
                    this.controlPredeterminada = Boolean.TRUE;
                }
            }
        }

    }

    public void definirPredeterminadaBandaImpositiva(AvalBandaImpositiva impositiva, Boolean predeterminada) {
        if (predeterminada) {
            impositiva.setPredeterminada(Boolean.TRUE);
        } else {
            impositiva.setPredeterminada(Boolean.FALSE);
        }
        impositiva = (AvalBandaImpositiva) services.save(impositiva);
        //QUITA EL VVALOR DE PREDETERMINADO EN LOS DEMAS  REGISTROS SI EN LTAL CASO EXISTEN
        validatePredeterminada(true, impositiva);
        //guarda registro en catastro.aval_impuesto_predios para establecer que esa base se queda predeterminada en para todos los predios:v
        setValuesBandaImpositivaSetAvalImpuestoPredio(null, Boolean.FALSE, impositiva);

        definirBaseImpositivaPorParroquiaSectorMz(impositiva);
    }

    //sirve para cuando se absre el. dialolgo de definir banda impositiva por la parroquias tambien funca para
    //
    public void setValuesBandaImpositivaSetAvalImpuestoPredio(PrediosManzanaDTO pmdto, Boolean control, AvalBandaImpositiva bandaImposi) {
        avalImpuestoPredios = new AvalImpuestoPredios();
        if (control) {
            this.avalImpuestoPredios.setParroquia(pmdto.getParroquia());
            this.avalImpuestoPredios.setSector(pmdto.getSector());
            this.avalImpuestoPredios.setZona(pmdto.getZona());
            this.avalImpuestoPredios.setMz(pmdto.getMz());
        } else {
            this.avalImpuestoPredios.setBandaImpositiva(bandaImposi);
            this.avalImpuestoPredios.setAnioFin(bandaImposi.getAnioFin());
            this.avalImpuestoPredios.setAnioInicio(bandaImposi.getAnioInicio());
            this.avalImpuestoPredios.setParroquia((short) -1);
            this.avalImpuestoPredios.setSector((short) -1);
            this.avalImpuestoPredios.setZona((short) -1);
            this.avalImpuestoPredios.setMz((short) -1);
        }

    }

    public void bajaPredios(boolean todo) {
        try {
            if (todo) {
                for (AniosEmision ae : aniosEmisionSeleccionados) {
                    contableServices.anularEmisionGeneral(2L, 3L, ae.getAnioEmision().shortValue());
                    int a = services.bajaTitualosPrediosUR(null, 4L, 2L, Arrays.asList(2L, 3L), ae.getAnioEmision(), true);
                }
            } else {
                for (CatPredio cp : prediosSeleccionados) {
                    for (AniosEmision ae : aniosEmisionSeleccionados) {
                        int a = services.bajaTitualosPrediosUR(cp, 4L, 2L, Arrays.asList(2L, 3L), ae.getAnioEmision(), false);
                        List<FinaRenLiquidacion> liquidaciones = liquidacionService.getObtenerLiquidacionPrediosUOR(cp, ae.getAnioEmision(), 2L);
                        for (FinaRenLiquidacion item : liquidaciones) {
                            if (contableServices.isEmisionContabilizada(item)) {
                                JsfUtil.update("La Emisión " + item.getIdLiquidacion() + " ya se encuentra contabilizada");
                                return;
                            } else {
                                contableServices.anularEmision(item);
                            }
                        }
                    }
                }
            }
            mensajeCorrecto();
        } catch (Exception e) {
            mensajeError();
        }
    }

    public void definirBaseImpositivaPorParroquiaSectorMz(AvalBandaImpositiva banda) {
        if (banda != null) {
            this.avalImpuestoPredios.setBandaImpositiva(banda);
        } else {
            this.avalImpuestoPredios.setBandaImpositiva(this.avalBandaImpositivaSeleccionada);
        }
        avalImpuestoPredios.setEstado("A");
        avalImpuestoPredios.setSolar((short) -1);
        switch (tipoDefinicion) {
            case "P":
                this.avalImpuestoPredios.setZona((short) -1);
                this.avalImpuestoPredios.setSector((short) -1);
                this.avalImpuestoPredios.setMz((short) -1);
                break;
            case "Z":
                this.avalImpuestoPredios.setSector((short) -1);
                this.avalImpuestoPredios.setMz((short) -1);
                break;
            case "S":
                this.avalImpuestoPredios.setMz((short) -1);
                break;
            default:
                break;

        }

        AvalImpuestoPredios aipResult = (AvalImpuestoPredios) services.save(this.avalImpuestoPredios);
        if (aipResult != null) {
            mensajeCorrecto();
        } else {
            mensajeError();
        }
    }

    public void saveTaxPaymentProperty() {
        if (this.avalBandaImpositivaSeleccionada != null) {
            List<AvalDetCobroImpuestoPredios> detCobroImpuestoPredios = null;
            if (this.avalBandaImpositivaSeleccionada.getId() != null) {
                this.avalImpuestoPredios.setBandaImpositiva(this.avalBandaImpositivaSeleccionada);
                avalImpuestoPredios.setEstado("A");
                this.validateSelection();

                switch (tipoDefinicion) {
                    case "":
                        this.avalImpuestoPredios.setParroquia((short) -1);
                        this.avalImpuestoPredios.setZona((short) -1);
                        this.avalImpuestoPredios.setSector((short) -1);
                        this.avalImpuestoPredios.setMz((short) -1);
                        this.avalImpuestoPredios.setSolar((short) -1);
                        break;
                    case "P":
                        this.avalImpuestoPredios.setParroquia(parroquia.getCodigoParroquia().shortValue());
                        this.avalImpuestoPredios.setZona((short) -1);
                        this.avalImpuestoPredios.setSector((short) -1);
                        this.avalImpuestoPredios.setMz((short) -1);
                        this.avalImpuestoPredios.setSolar((short) -1);
                        break;
                    case "Z":
                        this.avalImpuestoPredios.setParroquia(parroquia.getCodigoParroquia().shortValue());
                        this.avalImpuestoPredios.setZona(this.zona);
                        this.avalImpuestoPredios.setSector((short) -1);
                        this.avalImpuestoPredios.setMz((short) -1);
                        this.avalImpuestoPredios.setSolar((short) -1);
                        break;
                    case "S":
                        this.avalImpuestoPredios.setParroquia(parroquia.getCodigoParroquia().shortValue());
                        this.avalImpuestoPredios.setZona(this.zona);
                        this.avalImpuestoPredios.setSector(this.sector);
                        this.avalImpuestoPredios.setMz((short) -1);
                        this.avalImpuestoPredios.setSolar((short) -1);
                        break;
                    case "M":
                        this.avalImpuestoPredios.setParroquia(parroquia.getCodigoParroquia().shortValue());
                        this.avalImpuestoPredios.setZona(this.zona);
                        this.avalImpuestoPredios.setSector(this.sector);
                        this.avalImpuestoPredios.setMz(this.mz);
                        this.avalImpuestoPredios.setSolar((short) -1);
                        break;
                    case "L":
                        this.avalImpuestoPredios.setParroquia(parroquia.getCodigoParroquia().shortValue());
                        this.avalImpuestoPredios.setZona(this.zona);
                        this.avalImpuestoPredios.setSector(this.sector);
                        this.avalImpuestoPredios.setMz(this.mz);
                        this.avalImpuestoPredios.setSolar(this.lotes);
                        break;
                    default:
                        break;

                }
                if (this.rubrosSeleccionados != null) {
                    if (!this.rubrosSeleccionados.isEmpty()) {
                        AvalDetCobroImpuestoPredios cobroImpuestoPredios = new AvalDetCobroImpuestoPredios();
                        detCobroImpuestoPredios = new ArrayList<>();
                        for (FinaRenRubrosLiquidacion rubrosLiquidacion : this.rubrosSeleccionados) {
                            cobroImpuestoPredios = new AvalDetCobroImpuestoPredios();
                            cobroImpuestoPredios.setIdRubroCobrar(rubrosLiquidacion);
                            detCobroImpuestoPredios.add(cobroImpuestoPredios);
                        }
                        AvalImpuestoPredios aipResult = services.saveAvalImpuestoPrediosAndDetCobro(this.avalImpuestoPredios, detCobroImpuestoPredios);
                        if (aipResult != null) {
                            this.rubrosSeleccionados = new ArrayList();
                            this.avalBandaImpositivaSeleccionada = new AvalBandaImpositiva();
                            mensajeCorrecto();
                        } else {
                            mensajeAdvertencia();
                        }

                    } else {
                        mensajeAdvertencia();
                    }
                }

            }

        } else {
            mensajeAdvertencia();
        }
    }

    public void cleansVars() {
        this.zonaList = new ArrayList();
        this.sectorList = new ArrayList();
        this.mzList = new ArrayList();
        this.zona = null;
        this.sector = null;
        this.mz = null;
    }

    public void validateSelection() {
        //Definicion si es Por parroqquia - Zona - Sector - Mz - lotes
        if (parroquia != null) {
            //Parroquias
            if (parroquia.getCodigoParroquia() != null
                    && this.zona == null && this.sector == null && this.mz == null && this.lotes == null) {
                this.tipoDefinicion = "P";
            } else {
                //Zonas
                if (parroquia.getCodigoParroquia() != null
                        && this.zona != null && this.sector == null && this.mz == null && this.lotes == null) {
                    this.tipoDefinicion = "Z";
                }
                //Seector
                if (parroquia.getCodigoParroquia() != null
                        && this.zona != null && this.sector != null && this.mz == null && this.lotes == null) {
                    this.tipoDefinicion = "S";
                }
                //Manzanas
                if (parroquia.getCodigoParroquia() != null
                        && this.zona != null && this.sector != null && this.mz != null && this.lotes == null) {
                    this.tipoDefinicion = "M";
                }
                //Lote
                if (parroquia.getCodigoParroquia() != null
                        && this.zona != null && this.sector != null && this.mz != null && this.lotes != null) {
                    this.tipoDefinicion = "L";
                }
            }
        } else {
            this.tipoDefinicion = "";
        }

    }

    public void editarBaseImponible(AvalBandaImpositiva impo) {

        avalBandaImpositiva = impo;
        JsfUtil.update("frmMain");

    }

    public void editarTasaSeguridad(AvalTasaSeguridad tasa) {
        avalTasaSeguridadSeleccionada = tasa;
        System.out.println("el id de tasa es--> " + tasa.getId());
//        System.out.println("el id de avalTasa es--> "+avalTasaSeguridadSeleccionada.getId());
        JsfUtil.update("frmMain");
    }

    public void inactivaBandaImpositviva(AvalBandaImpositiva impositiva) {
        if (impositiva.getPredeterminada()) {
            JsfUtil.addErrorMessage("Error !", "Las Bases impositivas predeterminadas no se pueden Eliminar");
            return;
        }

        impositiva.setEstado("I");
        services.save(impositiva);
        avalBandaImpositivaList.remove(impositiva);

    }

    public void inactivaImpuestoPredial(AvalImpuestoPredios impuestoPredios) {
        impuestoPredios.setEstado("I");
        services.save(impuestoPredios);
        impuestoPrediosList.remove(impuestoPredios);
    }

    public CatPredioModel validarEmisionExitente(List<CatPredioModel> cps, Boolean control) {
        CatPredioModel cp = null;
        try {
            FinaRenLiquidacion emision;
            for (CatPredioModel predio : cps) {
                cp = new CatPredioModel();
                parametros = new HashMap<>();
                parametros.put("idPredio", predio.getId());
                parametros.put("anio", this.anioEmisionInicio);

                emision = (FinaRenLiquidacion) services.find("SELECT l FROM FinaRenLiquidacion l WHERE l.predio.id=:idPredio AND l.anio=:anio AND l.tipoLiquidacion.id=13", new String[]{"idPredio", "anio"},
                        new Object[]{predio.getId(), this.anioEmisionInicio});
                if (emision != null) {
                    cp = predio;
                    break;
                }
            }
            return cp;
        } catch (Exception e) {
            Logger.getLogger(EmisionAvaluosMB.class.getName()).log(Level.SEVERE, null, e);
        }
        return cp;
    }

    public void startEmisionPredial(Boolean control, Integer condicionUno) {
        Object o;
        if (control) {
            System.out.println("entrando todo caatro");
            for (AniosEmision ae : aniosEmisionSeleccionados) {

                o = services.generateEmisionPredial(null, ae.getAnioEmision(), ae.getAnioEmision(), control, session.getNameUser(), ae.getConsideraConstruccion(), condicionUno, condicionDos);
                JsfUtil.addInformationMessage("", "La Emisión se realizo con exito");
            }
        } else {
            System.out.println("predios Selecciondados");
            if (Utils.isEmpty(aniosEmisionSeleccionados)) {
                JsfUtil.addErrorMessage("Debe seleccionar el año a emitir", "Emisión");
                return;
            }

            Boolean poseeDeuda = Boolean.FALSE;
            for (CatPredio cp : prediosSeleccionados) {
                List<CatPredioPropietario> listaPreidosPropietarios = services.buscarPropietarios(cp.getId()); //consmir web services los predios de los propiestarios

                System.out.println("listaPreidosPropietarios " + listaPreidosPropietarios.size());
                if (listaPreidosPropietarios.isEmpty()) {
                    JsfUtil.addErrorMessage("Predio No Tiene Registrado Propietarios", "CLAVE " + cp.getClaveCat());
                    poseeDeuda = Boolean.TRUE;
                    break;
                }
                AvalValorSueloDTO acvsResult = null;
                for (AniosEmision ae : aniosEmisionSeleccionados) {
                    if (poseeDeudas(cp, ae.getAnioEmision())) {
                        JsfUtil.addErrorMessage("Predio Posee Emisiones Al Año Seleccionado", "");
                        poseeDeuda = Boolean.TRUE;
                        break;
                    }
                }
            }
            if (!poseeDeuda) {
                for (AniosEmision ae : aniosEmisionSeleccionados) {

                    o = services.generateEmisionPredial(prediosSeleccionados, ae.getAnioEmision(), ae.getAnioEmision(), control, session.getNameUser(), ae.getConsideraConstruccion(), condicionUno, condicionDos);
                }
                mensajeCorrecto();
            } else {
                return;
            }
            System.out.println("paso ");
            List<FinaRenDetLiquidacion> detLiquidacions = new ArrayList();
            liquidacionesGeneradas = new ArrayList();
            FinaRenLiquidacion liquidacion = null;
            FinaRenDetLiquidacion detLiquidacion = null;

            for (CatPredio cp : prediosSeleccionados) {
                for (AniosEmision ae : aniosEmisionSeleccionados) {
                    System.out.println("entrando 1");
                    liquidacion = (FinaRenLiquidacion) services.find("SELECT r FROM FinaRenLiquidacion r where r.predio = :idPredio AND r.anio = :anio AND r.estadoLiquidacion.id = :estado",
                            new String[]{"idPredio", "anio", "estado"},
                            new Object[]{cp, ae.getAnioEmision(), 2L});
                    if (liquidacion != null) {
                        liquidacionesGeneradas.add(liquidacion);
                        System.out.println("entrando 2");
                        detLiquidacions = new ArrayList();
                        for (FinaRenDetLiquidacion rdl : liquidacion.getRenDetLiquidacionCollection()) {
                            detLiquidacions.add(rdl);

                        }

                        liquidacion.setRenDetLiquidacionCollection(detLiquidacions);
                    }
                }
            }

            JsfUtil.addInformationMessage("", "La Emisión se realizo con exito");
            JsfUtil.executeJS("PF('dlgEmisiones').show()");
            JsfUtil.update("frmEmisiones");
        }
    }

    public Boolean poseeDeudas(CatPredio cp, Integer anio) {
        FinaRenLiquidacion liq = services.getDeudasPredioAnioActualByAnio(cp, anio);
        if (liq != null) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    public List<CatParroquia> getParroquias() {
        Long idCanton = 81L; //buscar los cantones con web services
        /*(Long) services.find("SELECT c.id FROM Canton WHERE c.codigo = :codigoNacional AND p.codNac=:codNac",
                new String[]{"codigoNacional", "codNac"}, new Object[]{Utils.CANTON, Utils.PROVINCIA});*/
        return (List<CatParroquia>) services.getCanton(idCanton);
    }

    public void loadZonas() {
        this.zonaList = new ArrayList();
        this.sectorList = new ArrayList();
        this.mzList = new ArrayList();
        this.lotesList = new ArrayList();
        this.lotes = null;
        this.zona = null;
        this.sector = null;
        this.mz = null;

        if (parroquia != null) {
            if (parroquia.getCodigoParroquia() != null) {
                //SELECT DISTINCT p.zona FROM CatPredio p WHERE p.parroquia =:parroquia AND p.estado = 'A'
                //   this.zonaList = services.findAll(Querys.getZonasByParroquia, new String[]{"parroquia"}, new Object[]{parroquia.getCodigoParroquia().shortValue()});
                // Collections.sort(this.zonaList);
                this.zonaList = (List<Short>) services.listBuscarZona(parroquia);
            }
        }
    }

    public void loadSectorByZonas() {
        this.sectorList = new ArrayList();
        this.mzList = new ArrayList();
        this.sector = null;
        this.mz = null;
        this.lotes = null;
        this.lotesList = new ArrayList();
        if (this.zona != null) {
//SELECT DISTINCT p.sector FROM CatPredio p WHERE p.parroquia =:parroquia AND p.zona =:zona AND p.estado = 'A'
            //  this.sectorList = services.findAll(Querys.getSectorByZonas, new String[]{"parroquia", "zona"}, new Object[]{parroquia.getCodigoParroquia().shortValue(), this.zona});
            //  Collections.sort(this.sectorList);
            this.sectorList = (List<Short>) services.listBuscarSector(parroquia.getCodigoParroquia().toString(), this.zona.toString());
        }
    }

    public void loadMzBySector() {
        this.mz = null;
        this.mzList = new ArrayList();
        this.lotes = null;
        this.lotesList = new ArrayList();
        if (this.sector != null) {
            //"SELECT DISTINCT p.mz FROM CatPredio p WHERE p.parroquia =:parroquia AND p.zona =:zona AND p.sector =:sector AND p.estado = 'A'";
            //   this.mzList = services.findAll(Querys.getMzBySector, new String[]{"parroquia", "zona", "sector"}, new Object[]{parroquia.getCodigoParroquia().shortValue(), this.zona, this.sector});
            // Collections.sort(this.mzList);
            this.mzList = (List<Short>) services.listBuscarMZSector(parroquia.getCodigoParroquia().toString(), this.zona.toString(), this.sector.toString());
        }
    }

    public void loadLotesByMz() {
        this.lotesList = new ArrayList();
        this.lotes = null;
        if (this.mz != null) {
            //"SELECT  p.solar FROM CatPredio p WHERE p.parroquia =:parroquia AND p.zona =:zona AND p.sector =:sector AND p.mz =:mz AND p.estado = 'A'";
//            this.lotesList = services.findAll(Querys.getLotesByMz, new String[]{"parroquia", "zona", "sector", "mz"}, new Object[]{parroquia.getCodigoParroquia().shortValue(), this.zona, this.sector, this.mz});
//            Collections.sort(this.lotesList);
            this.lotesList = (List<Short>) services.listBuscarLotesMz(parroquia.getCodigoParroquia().toString(), this.zona.toString(), this.sector.toString(), this.mz.toString());
        } else {
            JsfUtil.addWarningMessage("Advertencia!", "La parroquia seleccionada no tiene definido su codigo.");
        }
    }

    public void mensajeCorrecto() {
        JsfUtil.addInformationMessage("Éxito !", "La Transacción se realizó Correctamente");
    }

    public void mensajeError() {
        JsfUtil.addErrorMessage("Error !", "Ocurrio un error mientras se persistian los datos");
    }

    public void mensajeAdvertencia() {
        JsfUtil.addErrorMessage("Advertencia !", "Verifique que los datos esten seleccionados Correctamente");
    }

    public void emitirAvaluo() {
        try {

        } catch (Exception e) {
            logx.log(Level.SEVERE, null, e);
        }
    }

    public AvalBandaImpositiva getAvalBandaImpositiva() {
        avalBandaImpositiva.setAnioInicio(Utils.getAnio(new Date()));
//        avalBandaImpositiva.setAnioFin(Utils.getAnio(new Date()));
        avalBandaImpositiva.setEstado("A");
        return avalBandaImpositiva;
    }

    public AvalImpuestoPredios getAvalImpuestoPredios() {
        avalImpuestoPredios.setAnioFin(Utils.getAnio(new Date()));
        avalImpuestoPredios.setAnioInicio(Utils.getAnio(new Date()));
        return avalImpuestoPredios;
    }

    //<editor-fold defaultstate="collapsed" desc="SETTER AND GETTER">
    public ManagerService getServices() {
        return services;
    }

    public void setServices(ManagerService services) {
        this.services = services;
    }

    public List<AvalBandaImpositiva> getAvalBandaImpositivaTodas() {
        return avalBandaImpositivaTodas;
    }

    public void setAvalBandaImpositivaTodas(List<AvalBandaImpositiva> avalBandaImpositivaTodas) {
        this.avalBandaImpositivaTodas = avalBandaImpositivaTodas;
    }

    public Integer getCondicionDos() {
        return condicionDos;
    }

    public void setCondicionDos(Integer condicionDos) {
        this.condicionDos = condicionDos;
    }

    public UserSession getSession() {
        return session;
    }

    public void setSession(UserSession session) {
        this.session = session;
    }

    public ServletSession getSs() {
        return ss;
    }

    public void setSs(ServletSession ss) {
        this.ss = ss;
    }

    public Integer getTipoReporte() {
        return tipoReporte;
    }

    public void setTipoReporte(Integer tipoReporte) {
        this.tipoReporte = tipoReporte;
    }

    public Integer getAnioEmisionFin() {
        return anioEmisionFin;
    }

    public void setAnioEmisionFin(Integer anioEmisionFin) {
        this.anioEmisionFin = anioEmisionFin;
    }

    public Integer getAnioEmisionInicio() {
        return anioEmisionInicio;
    }

    public void setAnioEmisionInicio(Integer anioEmisionInicio) {
        this.anioEmisionInicio = anioEmisionInicio;
    }

    public AvalBandaImpositiva getAvalBandaImpositivaSeleccionada() {
        return avalBandaImpositivaSeleccionada;
    }

    public void setAvalBandaImpositivaSeleccionada(AvalBandaImpositiva avalBandaImpositivaSeleccionada) {
        this.avalBandaImpositivaSeleccionada = avalBandaImpositivaSeleccionada;
    }

    public List<AvalBandaImpositiva> getAvalBandaImpositivaList() {
        return avalBandaImpositivaList;
    }

    public void setAvalBandaImpositivaList(List<AvalBandaImpositiva> avalBandaImpositivaList) {
        this.avalBandaImpositivaList = avalBandaImpositivaList;
    }

    public List<PrediosManzanaDTO> getPrediosXManzana() {
        return prediosXManzana;
    }

    public void setPrediosXManzana(List<PrediosManzanaDTO> prediosXManzana) {
        this.prediosXManzana = prediosXManzana;
    }

    public Boolean getControlPredeterminada() {
        return controlPredeterminada;
    }

    public void setControlPredeterminada(Boolean controlPredeterminada) {
        this.controlPredeterminada = controlPredeterminada;
    }

    public String getTipoDefinicion() {
        return tipoDefinicion;
    }

    public void setTipoDefinicion(String tipoDefinicion) {
        this.tipoDefinicion = tipoDefinicion;
    }

    public List<Short> getZonaList() {
        return zonaList;
    }

    public void setZonaList(List<Short> zonaList) {
        this.zonaList = zonaList;
    }

    public List<Short> getMzList() {
        return mzList;
    }

    public void setMzList(List<Short> mzList) {
        this.mzList = mzList;
    }

    public List<Short> getSectorList() {
        return sectorList;
    }

    public void setSectorList(List<Short> sectorList) {
        this.sectorList = sectorList;
    }

    public List<Short> getLotesList() {
        return lotesList;
    }

    public void setLotesList(List<Short> lotesList) {
        this.lotesList = lotesList;
    }

    public Short getZona() {
        return zona;
    }

    public void setZona(Short zona) {
        this.zona = zona;
    }

    public Short getMz() {
        return mz;
    }

    public void setMz(Short mz) {
        this.mz = mz;
    }

    public Short getSector() {
        return sector;
    }

    public void setSector(Short sector) {
        this.sector = sector;
    }

    public Short getLotes() {
        return lotes;
    }

    public void setLotes(Short lotes) {
        this.lotes = lotes;
    }

    public CatParroquia getParroquia() {
        return parroquia;
    }

    public void setParroquia(CatParroquia parroquia) {
        this.parroquia = parroquia;
    }

    public List<FinaRenRubrosLiquidacion> getRubrosList() {
        return rubrosList;
    }

    public void setRubrosList(List<FinaRenRubrosLiquidacion> rubrosList) {
        this.rubrosList = rubrosList;
    }

    public List<FinaRenRubrosLiquidacion> getRubrosSeleccionados() {
        return rubrosSeleccionados;
    }

    public void setRubrosSeleccionados(List<FinaRenRubrosLiquidacion> rubrosSeleccionados) {
        this.rubrosSeleccionados = rubrosSeleccionados;
    }

    public Map<String, Object> getParametros() {
        return parametros;
    }

    public void setParametros(Map<String, Object> parametros) {
        this.parametros = parametros;
    }

    public Integer getAnioMax() {
        return anioMax;
    }

    public void setAnioMax(Integer anioMax) {
        this.anioMax = anioMax;
    }

    public List<AniosEmision> getAniosEmisionMax() {
        return aniosEmisionMax;
    }

    public void setAniosEmisionMax(List<AniosEmision> aniosEmisionMax) {
        this.aniosEmisionMax = aniosEmisionMax;
    }

    public List<AniosEmision> getAniosEmisionSeleccionados() {
        return aniosEmisionSeleccionados;
    }

    public void setAniosEmisionSeleccionados(List<AniosEmision> aniosEmisionSeleccionados) {
        this.aniosEmisionSeleccionados = aniosEmisionSeleccionados;
    }

    public List<FinaRenLiquidacion> getLiquidacionesGeneradas() {
        return liquidacionesGeneradas;
    }

    public void setLiquidacionesGeneradas(List<FinaRenLiquidacion> liquidacionesGeneradas) {
        this.liquidacionesGeneradas = liquidacionesGeneradas;
    }

    public Integer getAnioEmision() {
        return anioEmision;
    }

    public void setAnioEmision(Integer anioEmision) {
        this.anioEmision = anioEmision;
    }

    public LazyModel<CatPredio> getPredios() {
        return predios;
    }

    public void setPredios(LazyModel<CatPredio> predios) {
        this.predios = predios;
    }

    public List<CatPredio> getPrediosSeleccionados() {
        return prediosSeleccionados;
    }

    public void setPrediosSeleccionados(List<CatPredio> prediosSeleccionados) {
        this.prediosSeleccionados = prediosSeleccionados;
    }

    public CatPredio getPredioSeleccionado() {
        return predioSeleccionado;
    }

    public void setPredioSeleccionado(CatPredio predioSeleccionado) {
        this.predioSeleccionado = predioSeleccionado;
    }

    public List<AvalImpuestoPredios> getImpuestoPrediosList() {
        return impuestoPrediosList;
    }

    public void setImpuestoPrediosList(List<AvalImpuestoPredios> impuestoPrediosList) {
        this.impuestoPrediosList = impuestoPrediosList;
    }

    public LazyModel<AvalTasaSeguridad> getLazyTasaSeguridad() {
        return lazyTasaSeguridad;
    }

    public void setLazyTasaSeguridad(LazyModel<AvalTasaSeguridad> lazyTasaSeguridad) {
        this.lazyTasaSeguridad = lazyTasaSeguridad;
    }

    public void setElecBanda(AvalBandaImpositiva elecBanda) {
        this.elecBanda = elecBanda;
    }

    public AvalTasaSeguridad getAvalTasaSeguridadSeleccionada() {
        return avalTasaSeguridadSeleccionada;
    }

    public void setAvalTasaSeguridadSeleccionada(AvalTasaSeguridad avalTasaSeguridadSeleccionada) {
        this.avalTasaSeguridadSeleccionada = avalTasaSeguridadSeleccionada;
    }

//</editor-fold>
}
