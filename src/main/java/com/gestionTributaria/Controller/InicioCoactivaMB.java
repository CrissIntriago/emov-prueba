/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Controller;

import com.asgard.Entity.FinaRenLiquidacion;
import com.asgard.Entity.FinaRenPatente;
import com.asgard.Entity.FinaRenRubrosLiquidacion;
import com.asgard.Entity.FinaRenTipoLiquidacion;
import com.gestionTributaria.Entities.CatCiudadela;
import com.gestionTributaria.Entities.CatPredio;
import com.gestionTributaria.Entities.CatPredioPropietario;
import com.gestionTributaria.Entities.CatSectores;
import com.gestionTributaria.Entities.CoaAbogado;
import com.gestionTributaria.Entities.CoaEstadoJuicio;
import com.gestionTributaria.Entities.CoaJuicio;
import com.gestionTributaria.Entities.CoaJuicioPredio;
import com.gestionTributaria.Entities.FnConvenioPago;
import com.gestionTributaria.Entities.FnConvenioPagoDetalle;
import com.gestionTributaria.Services.CatCiudadelasService;
import com.gestionTributaria.Services.CatPredioPropietarioService;
import com.gestionTributaria.Services.CatPredioService;
import com.gestionTributaria.Services.CoaJuicioService;
import com.gestionTributaria.Services.FinaRenLiquidacionService;
import com.gestionTributaria.Services.InteresesService;
import com.gestionTributaria.Services.ManagerService;
import com.gestionTributaria.Services.RenRubroLiquidacionService;
import com.gestionTributaria.Services.SectoresService;
import com.gestionTributaria.Services.SecuenciasServices;
import com.gestionTributaria.models.Archivo;
import com.gestionTributaria.models.BusquedaPredios;
import com.gestionTributaria.models.CatPredioModel;
import com.gestionTributaria.models.PredioDTO;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.entities.SecuenciaGeneral;
import com.origami.sigef.common.entities.Usuarios;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot;
import com.origami.sigef.resource.procesos.entities.HistoricoTramites;
import com.origami.sigef.resource.procesos.entities.Observaciones;
import com.origami.sigef.resource.procesos.entities.TipoTramite;
import com.origami.sigef.resource.procesos.services.ObservacionesService;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.TransferEvent;
import org.primefaces.model.DualListModel;

/**
 *
 * @author Administrator
 */
@Named
@ViewScoped
public class InicioCoactivaMB extends BpmnBaseRoot implements Serializable {

    private static final Logger LOG = Logger.getLogger(InicioCoactivaMB.class.getName());

    @Inject
    private UserSession session;
    @Inject
    private ManagerService services;
    @Inject
    private ServletSession ss;
    @Inject
    private CoaJuicioService coactivaService;
    @Inject
    private ObservacionesService observacionesService;
    @Inject
    private CatPredioPropietarioService catPredioPropietarioService;
    @Inject
    private SecuenciasServices secuenciasServices;
    @Inject
    private FinaRenLiquidacionService finaRenLiquidacionService;
    @Inject
    private RenRubroLiquidacionService renRubroLiquidacionService;
    @Inject
    private InteresesService interesesService;
    @Inject
    private CatCiudadelasService catCiudadelasService;
    @Inject
    private CoaJuicioService coaJuicioService;
    @Inject
    private ClienteService clienteService;
    @Inject
    private SectoresService sectoresSevice;
    @Inject
    private CatPredioService prediosService;
    // contiene la lista de liquidaciones que aplican exoneracion
    private List<FinaRenLiquidacion> impuestos;
    //contiene la lista de liquidaciones  seleccionados
    private List<FinaRenLiquidacion> liquidacionesSeleccionadas;
    //secuencia para codigo
    private SecuenciaGeneral secuencia;
    //contiene los nombres de los propietariosLista de los predios
    private LazyModel<CatPredioPropietario> propietariosLista;
    //variable de seleccion de propietario del predio
    private CatPredioPropietario propietario;
    //lista del tipo de rubro para la liquidacion
    private FinaRenRubrosLiquidacion finaRenRubroLiquidacion;
    //Contiene el lista de las notificaciones //por prensa o boleta
    private List<FinaRenRubrosLiquidacion> finaRenRubrosLiquidacioness;
    //donde se guardan las dos listas
    private DualListModel<CatCiudadela> listaCiudadelas;
    //La lista de seleccion
    private List<CatCiudadela> cdlaTarget;
    private List<CatCiudadela> cdlaSource;
    private List<CatCiudadela> cdlaseleccionadas;
    //fin lista de selcciona
    //para lista de seleccion  predios rusticos
    private DualListModel<CatSectores> listaSectores;
    //para la lista de selecion
    private List<CatSectores> sectoresTarget;
    private List<CatSectores> sectoresSource;
    private List<CatSectores> sectoreSeleccionadas;
    //fin lista de selcciona
    private FinaRenTipoLiquidacion tipoLiquidacion;
    private List<FinaRenTipoLiquidacion> tipoLiquidaciones;
    private FinaRenLiquidacion finaRenLiquidacion;
    private List<String> ciudadelas;
    private Boolean urbanistico = true;
    private Boolean guardado = false;
    private BigDecimal totalJuicio = new BigDecimal("0.00");
    private LazyModel<CatPredioModel> predios;
    private CoaJuicio juicio;
    private Integer estado = 1;
    private String observacionDocumento;
    private String formatoArchivos;
    private HistoricoTramites ht = new HistoricoTramites();
    private List<Usuarios> users = new ArrayList<>();
    private List<CoaJuicioPredio> detalleJuicio = new ArrayList<>();
    private List<CoaAbogado> listAbogados = new ArrayList<>();
    private List<CoaEstadoJuicio> listEstados = new ArrayList<>();
    private CatPredio predio;
    private List<CatCiudadela> ciudadelasEntidad;
    private Map<String, Object> parametros;
    private Map<String, String> order;
    private CatPredioModel predioUrbanoConsulta;
    private List<CatPredioModel> prediosUrbanosConsultaSeleccionados;
    private List<CatPredioModel> prediosUrbanosConsulta;
    private List<CatPredioModel> prediosUrbanos = new ArrayList<>();
    private ArrayList<Archivo> files = new ArrayList<>();
    //para el tipo consulta
    private String consulta;
    private LazyModel<FnConvenioPago> convenios;
    private FnConvenioPago convenio, convenioSeleccionado;
    private List<FnConvenioPagoDetalle> convenidos;
    private String div1, div2, div3, div4;
    private SimpleDateFormat sdf;
    private Calendar cal;
    private LazyModel<FinaRenLiquidacion> titulosCredito;
    //datos por mandar
    private BusquedaPredios BusquedaPredios;
    //lisa de predios para coactiva
//    private List<CatPredio> prediosCoactiva;
    private List<PredioDTO> predioDTO;
    //TOTAL DEUDA PARA PREDIO
    private Double totalDeudaPredio;
    //LISTA DE PREDIOS
    private List<CatPredio> prediosAplicanCoactiva;
    //Lista de seleccion preidos coactiva masivos
    private List<CatPredio> predioSeleccionadosCoactiva;
    private String claveCatastralBuscar = "";
    //Lista de patentes
    private List<FinaRenPatente> listaPatente;
    private FinaRenPatente patente;
    private FinaRenLiquidacion liquidacion;
    private List<FinaRenPatente> patentesSeleccionadas;

    @PostConstruct
    public void initView() {
        try {
            liquidacion = new FinaRenLiquidacion();
            patente = new FinaRenPatente();
            listaPatente = new ArrayList<>();
            propietariosLista = new LazyModel<>(CatPredioPropietario.class);
            consulta = "3";
            juicio = new CoaJuicio();
            juicio.setTipoJuicio(new FinaRenTipoLiquidacion(2L));
            predioDTO = new ArrayList<>();
            secuencia = new SecuenciaGeneral();
            finaRenLiquidacion = new FinaRenLiquidacion();
            tipoLiquidaciones = (List<FinaRenTipoLiquidacion>) services.findAllEasy("select a from FinaRenTipoLiquidacion a where a.aplicaCoativa=true");
            predio = new CatPredio();
            ciudadelasEntidad = services.ordenamientoNombreCiudadela();
            juicio.setFechaJuicio(new Date());
            juicio.setEstado(Boolean.TRUE);
            juicio.setAnioJuicio(Utils.getAnio(new Date()));
            ciudadelas = services.findAllQuery("SELECT cp1.nombre FROM CatCiudadela cp1 ORDER BY cp1.nombre ASC", null);
            listAbogados = services.findAllQuery("SELECT c from CoaAbogado c where c.estado = true and c.detalle!=null order by c.detalle", null);
            listEstados = services.findAllQuery("SELECT c from CoaEstadoJuicio c where c.estado = true order by c.secuencia", null);
            users = services.allUserFindById("ASISTENTE DE COACTIVA");
            consultarCiudadelas();
            consultarSectores();
            prediosAplicanCoactiva = new ArrayList<>();
            predioSeleccionadosCoactiva = new ArrayList<>();
            patentesSeleccionadas = new ArrayList<>();
            this.tramite = new HistoricoTramites();
        } catch (Exception e) {
            Logger.getLogger(InicioCoactivaMB.class.getName()).log(Level.ALL.SEVERE, null, e);
        }
    }

    public void consultarCiudadelas() {
        try {
            cdlaSource = catCiudadelasService.getAllCiudadelas();
            cdlaTarget = new ArrayList<>();
            listaCiudadelas = new DualListModel<>(cdlaSource, cdlaTarget);
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "error al traer sectores", ex.getMessage());
        }
    }

    public void consultarSectores() {
        try {
            sectoresSource = sectoresSevice.getAllSectores();
            sectoresTarget = new ArrayList<>();
            listaSectores = new DualListModel<>(sectoresSource, sectoresTarget);
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error al consultar sectores", ex.getMessage());
        }
    }

    public SelectItem[] getLisUrbanizaciones() {
        int cantRegis = ciudadelas.size();
        SelectItem[] options = new SelectItem[cantRegis + 1];
        options[0] = new SelectItem("", "Seleccione");
        for (int i = 0; i < cantRegis; i++) {
            options[i + 1] = new SelectItem(ciudadelas.get(i), ciudadelas.get(i));
        }
        return options;
    }

    public void showDlgPredios() {
        predios = new LazyModel<>(CatPredioModel.class);
        JsfUtil.update("formPredSel");
        JsfUtil.executeJS("PF('selPredio').show();");
    }

    public void closeDialog() {
        JsfUtil.addInformationMessage("Correcto", "Archivo Agregados Correctamente");
    }

    public void toogleEvent() {
        if (!liquidacionesSeleccionadas.isEmpty()) {
            for (FinaRenLiquidacion item : liquidacionesSeleccionadas) {
                item.calcularPago();
                totalJuicio = totalJuicio.add(item.getPagoFinal());
            }
        } else {
            totalJuicio = totalJuicio.ZERO;
        }
        JsfUtil.update("mainForm:dtCoactiva");
        JsfUtil.update("mainForm:groupCoactivaTotales");
    }

    public void actualizarValorAnterior() {
        totalJuicio = totalJuicio.ZERO;
        if (liquidacionesSeleccionadas.size() > 0) {
            int anio = liquidacionesSeleccionadas.get(0).getAnio();
            liquidacionesSeleccionadas.clear();
            for (FinaRenLiquidacion liquidaciones : impuestos) {
                if (liquidaciones.getAnio() <= anio) {
                    liquidacionesSeleccionadas.add(liquidaciones);
                    liquidaciones.calcularPago();
                    totalJuicio = totalJuicio.add(liquidaciones.getPagoFinal());
                }
            }
        }
        JsfUtil.update("mainForm:dtCoactiva");
        JsfUtil.update("mainForm:groupCoactivaTotales");
    }

    public void seleccionLiquidacion(FinaRenLiquidacion liquidacion) {
        finaRenLiquidacion = liquidacion;
        ArrayList lista = new ArrayList<>();
        lista.add(liquidacion);
        BusquedaPredios.calculoTotalPago(lista, null);
        JsfUtil.update("dlgDetalle");
        JsfUtil.executeJS("PF('dlgDetalle').show();");
    }

    public void consultarPredioUrbano() {
        impuestos = new ArrayList<>();
        liquidacionesSeleccionadas = new ArrayList<>();
        impuestos.clear();
        liquidacionesSeleccionadas.clear();
        if (validaCampos()) {
            try {
                //urbano y rustico
                if (juicio.getTipoJuicio().getId().intValue() == 2 || juicio.getTipoJuicio().getId().intValue() == 3) {
                    switch (consulta) {
                        case "1":
                            prediosAplicanCoactiva = finaRenLiquidacionService.getPrediosLiquidacion(consulta, juicio.getTipoJuicio(), predio);
                            break;
                        case "2":
                            //contribuyente
                            if (predio.getNombrePosesionario().isEmpty() && predio.getCedulaPosesionario().isEmpty()) {
                                JsfUtil.executeJS("PF('dlgContribuyente').show();");
                            } else {
                                CatPredio buscarPredioLiquidacion;
                                //si el campo nombre completo está lleno
                                if (!predio.getNombrePosesionario().isEmpty()) {
                                    buscarPredioLiquidacion = catPredioPropietarioService.findByPropietarioNombreCompleto(predio.getNombrePosesionario());
                                    if (buscarPredioLiquidacion != null) {
                                        prediosAplicanCoactiva = finaRenLiquidacionService.getPrediosLiquidacion(consulta, juicio.getTipoJuicio(), buscarPredioLiquidacion);
                                    } else {
                                        JsfUtil.addInformationMessage("Sin resultados", "");
                                    }
                                } else {
                                    //busquedad por cedula o ruc
                                    buscarPredioLiquidacion = catPredioPropietarioService.findByPropietarioCiuRuc(predio.getCedulaPosesionario());
                                    if (buscarPredioLiquidacion != null) {
                                        prediosAplicanCoactiva = finaRenLiquidacionService.getPrediosLiquidacion(consulta, juicio.getTipoJuicio(), buscarPredioLiquidacion);
                                    } else {
                                        JsfUtil.addInformationMessage("Sin resultados", "");
                                    }
                                }
                            }
                            break;
                        case "4":
                            //clave catastral para urbano
                            if (juicio.getTipoJuicio().getId().intValue() == 2) {
                                claveCatastralBuscar = "";
                                //parroquia
                                claveCatastralBuscar = predio.getParroquia() == null ? claveCatastralBuscar + "1." : claveCatastralBuscar + predio.getParroquia() + ".";
                                //sector
                                claveCatastralBuscar = predio.getSector() == null ? claveCatastralBuscar + "0." : claveCatastralBuscar + predio.getSector() + ".";
                                //manzana
                                claveCatastralBuscar = predio.getMz() == null ? claveCatastralBuscar + "0." : claveCatastralBuscar + predio.getMz() + ".";
                                //solar
                                claveCatastralBuscar = predio.getSolar() == null ? claveCatastralBuscar + "0." : claveCatastralBuscar + predio.getSolar() + ".";
                                //div 1
                                claveCatastralBuscar = "".equals(div1) ? claveCatastralBuscar + "0." : claveCatastralBuscar + div1 + ".";
                                //div 2
                                claveCatastralBuscar = "".equals(div2) ? claveCatastralBuscar + "0." : claveCatastralBuscar + div2 + ".";
                                //div 3
                                claveCatastralBuscar = "".equals(div3) ? claveCatastralBuscar + "0." : claveCatastralBuscar + div3 + ".";
                                //div 4
                                claveCatastralBuscar = "".equals(div4) ? claveCatastralBuscar + "0." : claveCatastralBuscar + div4 + ".";
                                //div phv
                                claveCatastralBuscar = predio.getBloque() == null ? claveCatastralBuscar + "0." : claveCatastralBuscar + predio.getBloque() + ".";
                                //div phh
                                claveCatastralBuscar = predio.getPiso() == null ? claveCatastralBuscar + "0" : claveCatastralBuscar + predio.getPiso();
                                predio.setClaveCat(claveCatastralBuscar);
                                prediosAplicanCoactiva = finaRenLiquidacionService.getPrediosLiquidacion(consulta, juicio.getTipoJuicio(), predio);
                            }
                            //clave catastral para rural
                            if (juicio.getTipoJuicio().getId().intValue() == 3) {
                                claveCatastralBuscar = "";
                                //parroquia
                                claveCatastralBuscar = predio.getParroquia() == null ? claveCatastralBuscar + "1." : claveCatastralBuscar + predio.getParroquia() + ".";
                                //sector
                                claveCatastralBuscar = predio.getSector() == null ? claveCatastralBuscar + "0." : claveCatastralBuscar + predio.getSector() + ".";
                                //codigo
                                claveCatastralBuscar = "".equals(div3) ? claveCatastralBuscar + "0." : claveCatastralBuscar + div3 + ".";
                                //div 1
                                claveCatastralBuscar = "".equals(div1) ? claveCatastralBuscar + "0." : claveCatastralBuscar + div1 + ".";
                                //div 2
                                claveCatastralBuscar = "".equals(div2) ? claveCatastralBuscar + "0" : claveCatastralBuscar + div2;
                                predio.setClaveCat(claveCatastralBuscar);
                                prediosAplicanCoactiva = finaRenLiquidacionService.getPrediosLiquidacion(consulta, juicio.getTipoJuicio(), predio);
                            }
                            break;
                        case "5":
                            if (juicio.getTipoJuicio().getId().intValue() == 2) {
                                prediosAplicanCoactiva.clear();
                                if (cdlaseleccionadas.size() > 0) {
                                    for (CatCiudadela ciudadela : cdlaseleccionadas) {
                                        predio.setCiudadela(ciudadela);
                                        List<CatPredio> aux = finaRenLiquidacionService.getPrediosLiquidacion(consulta, juicio.getTipoJuicio(), predio);
                                        prediosAplicanCoactiva.addAll(aux);
                                    }
                                }
                            }
                            if (juicio.getTipoJuicio().getId().intValue() == 3) {
                                prediosAplicanCoactiva.clear();
                                if (sectoreSeleccionadas.size() > 0) {
                                    for (CatSectores sector : sectoreSeleccionadas) {
                                        predio.setSector(sector.getSector().shortValue());
                                        List<CatPredio> aux = finaRenLiquidacionService.getPrediosLiquidacion("6", juicio.getTipoJuicio(), predio);
                                        prediosAplicanCoactiva.addAll(aux);
                                    }
                                }
                            }
                            break;
                    }
                    if (prediosAplicanCoactiva.isEmpty()) {
                        JsfUtil.addInformationMessage("No tiene liquidaciones pendientes de pago para el año anterior", "");
                    }
                }
                if (juicio.getTipoJuicio().getId().intValue() == 9) {
                    switch (consulta) {
                        case "6":
                            claveCatastralBuscar = "";
                            //parroquia
                            claveCatastralBuscar = predio.getParroquia() == null ? claveCatastralBuscar + "1." : claveCatastralBuscar + predio.getParroquia() + ".";
                            //sector
                            claveCatastralBuscar = predio.getSector() == null ? claveCatastralBuscar + "0." : claveCatastralBuscar + predio.getSector() + ".";
                            //manzana
                            claveCatastralBuscar = predio.getMz() == null ? claveCatastralBuscar + "0." : claveCatastralBuscar + predio.getMz() + ".";
                            //solar
                            claveCatastralBuscar = predio.getSolar() == null ? claveCatastralBuscar + "0." : claveCatastralBuscar + predio.getSolar() + ".";
                            //div 1
                            claveCatastralBuscar = "".equals(div1) ? claveCatastralBuscar + "0." : claveCatastralBuscar + div1 + ".";
                            //div 2
                            claveCatastralBuscar = "".equals(div2) ? claveCatastralBuscar + "0." : claveCatastralBuscar + div2 + ".";
                            //div 3
                            claveCatastralBuscar = "".equals(div3) ? claveCatastralBuscar + "0." : claveCatastralBuscar + div3 + ".";
                            //div 4
                            claveCatastralBuscar = "".equals(div4) ? claveCatastralBuscar + "0." : claveCatastralBuscar + div4 + ".";
                            //div phv
                            claveCatastralBuscar = predio.getBloque() == null ? claveCatastralBuscar + "0." : claveCatastralBuscar + predio.getBloque() + ".";
                            //div phh
                            claveCatastralBuscar = predio.getPiso() == null ? claveCatastralBuscar + "0" : claveCatastralBuscar + predio.getPiso();
//                            listaPatente = finaRenLiquidacionService.getPatenteLiquidacion(consulta, juicio.getTipoJuicio(), liquidacion);
                            impuestos = finaRenLiquidacionService.listaLiquidacionesPatente(claveCatastralBuscar, liquidacion.getCodigoLocal(), juicio.getTipoJuicio().getId());
                            break;
                        case "7":
                            if (predio.getNombrePosesionario().isEmpty() && predio.getCedulaPosesionario().isEmpty()) {
                                JsfUtil.executeJS("PF('dlgContribuyente').show();");
                            } else {
                                Cliente propietario;
                                //si el campo nombre completo está lleno
                                if (!predio.getNombrePosesionario().isEmpty()) {
                                    propietario = finaRenLiquidacionService.findByPropietarioNombreCompleto(predio.getNombrePosesionario());
                                    if (propietario != null) {
                                        liquidacion.setComprador(propietario);
                                        listaPatente = finaRenLiquidacionService.getPatenteLiquidacion(consulta, juicio.getTipoJuicio(), liquidacion);
                                    } else {
                                        JsfUtil.addInformationMessage("Sin resultados", "");
                                    }
                                } else {
                                    //busquedad por cedula o ruc
                                    propietario = finaRenLiquidacionService.findByPropietarioCiuRuc(predio.getCedulaPosesionario());
                                    if (propietario != null) {
                                        liquidacion.setComprador(propietario);
                                        listaPatente = finaRenLiquidacionService.getPatenteLiquidacion(consulta, juicio.getTipoJuicio(), liquidacion);
                                    } else {
                                        JsfUtil.addInformationMessage("Sin resultados", "");
                                    }
                                }
                            }
                            break;
                    }
                    if (impuestos.isEmpty()) {
                        JsfUtil.addInformationMessage("No tiene patentes pendientes de pago", "");
                    }
                }
                //todo en liquidaciones
                if (juicio.getTipoJuicio().getId().intValue() != 9 && juicio.getTipoJuicio().getId().intValue() != 3 && juicio.getTipoJuicio().getId().intValue() != 2) {
                    impuestos.clear();
                    if (predio.getNombrePosesionario().isEmpty() && predio.getCedulaPosesionario().isEmpty()) {
                        JsfUtil.executeJS("PF('dlgContribuyente').show();");
                    } else {
                        Cliente propietario;
                        if (!predio.getNombrePosesionario().isEmpty()) {
                            propietario = finaRenLiquidacionService.findByPropietarioNombreCompleto(predio.getNombrePosesionario());
                            if (propietario != null) {
                                //por nombre
                                liquidacion.setComprador(propietario);
                                impuestos = finaRenLiquidacionService.getLiquidacionesCoactiva(juicio.getTipoJuicio(), liquidacion);
                            } else {
                                JsfUtil.addInformationMessage("Sin resultados", "");
                            }
                        } else {
                            //busquedad por cedula o ruc
                            propietario = finaRenLiquidacionService.findByPropietarioCiuRuc(predio.getCedulaPosesionario());
                            if (propietario != null) {
                                liquidacion.setComprador(propietario);
                                impuestos = finaRenLiquidacionService.getLiquidacionesCoactiva(juicio.getTipoJuicio(), liquidacion);
                            } else {
                                JsfUtil.addInformationMessage("Sin resultados", "");
                            }
                        }
                    }
                    if (listaPatente.isEmpty()) {
                        JsfUtil.addInformationMessage("Sin resultados", "");
                    }
                }
                //calculos para total del juicio
                updateValorJuicio(impuestos);
            } catch (Exception e) {
                LOG.log(Level.SEVERE, "Error búsquedad: ", e);
            }
        }
    }

    public void updateValorJuicio(List<FinaRenLiquidacion> liquidaciones) {
        Double total = 0.00;
        for (FinaRenLiquidacion liq : liquidaciones) {
            total = total + liq.getTotalPago().doubleValue();
        }
        totalJuicio = BigDecimal.valueOf(total);
    }

    public void onAlterarPickList(TransferEvent event) {
        cdlaseleccionadas = listaCiudadelas.getTarget();
    }

    public void onAlterarPickListRural(TransferEvent event) {
        sectoreSeleccionadas = listaSectores.getTarget();
    }

    public Boolean validaCampos() {
        if (juicio.getAbogadoJuicio() == null) {
            JsfUtil.addErrorMessage("Error", "Debe seleccionar un abogado para el juicio.");
            return false;
        }
        if (juicio.getTipoJuicio() == null) {
            JsfUtil.addErrorMessage("Error", "Debe seleccionar un tipo de juicio.");
            return false;
        }
        if (juicio.getObservacion().isEmpty()) {
            JsfUtil.addErrorMessage("Error", "La observacion es necesaria.");
            return false;
        }
        if (coactivaService.consultaJuicioByNumeroYanio(juicio.getNumeroJuicio(), juicio.getAnioJuicio())) {
            JsfUtil.addErrorMessage("Error", "Ya se encuentra ingresado un juicio coactivo con el mismo numero y mismo anio.");
            return false;
        }

        return true;
    }

    public void BuscarPredioContribuyenteSeleccionado() {
        predio.setId(propietario.getPredio().getId());
        impuestos = finaRenLiquidacionService.getLiquidacionesByLocal(null, null, predio, 2, juicio.getTipoJuicio().getId());
        liquidacionesSeleccionadas = impuestos;
        if (impuestos.isEmpty()) {
            JsfUtil.addInformationMessage("Info", "El contribuyente no tiene liquidaciones pendientes de pago");
        } else {
            toogleEvent();
        }
        JsfUtil.update("mainForm");
        JsfUtil.executeJS("PF('dlgContribuyente').hide();");
    }

    public void deleteLiquidacion(int indice) {
        impuestos.remove(indice);
//        this.updateValorJuicio();
        JsfUtil.update("mainForm");
    }

    public void deleteLiquidacionByConvenios(int indice) {
        convenidos.remove(indice);
        impuestos = new ArrayList<>();
        for (FnConvenioPagoDetalle l : convenidos) {
            impuestos.add(l.getLiquidacion());
        }
//        this.updateValorJuicio();
    }

    public Boolean validaPredioExistente(Long idPredio) {
        for (FinaRenLiquidacion l : impuestos) {
            CatPredioModel predio = services.buscarPredio(l.getPredio().getId());
            if (predio.getId().equals(idPredio)) {
                return false;
            }
        }
        return true;
    }

    public String nomenclaturaJuicio(FinaRenTipoLiquidacion tipoLiquidacion) {
        String abreviatura = "";
        //URBANO Y RUSTICO
        if (tipoLiquidacion.getId().intValue() == 2 || tipoLiquidacion.getId().intValue() == 3) {
            abreviatura = "-PC";
        }
        //ACTIVO TOTAL
        if (tipoLiquidacion.getId().intValue() == 7) {
            abreviatura = "-UPCPM-PC";
        }
        //PATENTE
        if (tipoLiquidacion.getId().intValue() == 9) {
            abreviatura = "-P-PC";
        }
        //TASA DE HABILITACION
        if (tipoLiquidacion.getId().intValue() == 517) {
            abreviatura = "-PFU-PC-PC";
        }
        //EX FUNCIONARIO//INFRACCIÓN A LA ORDENANZA MUNICIPAL
        if (tipoLiquidacion.getId().intValue() == 660 || tipoLiquidacion.getId().intValue() == 379) {
            abreviatura = "-LQ";
        }
        //IMCUMPLIMIENTO DE CONVENIO
        if (tipoLiquidacion.getId().intValue() == 893) {
            abreviatura = "-ICP";
        }
        return abreviatura;
    }

    public void guardarJuicioCoactivoNotificacion() {
        try {
            //por un predio
            Double totalPagar = 0.00;
            if (juicio.getTipoJuicio().getId().intValue() != 2 && juicio.getTipoJuicio().getId().intValue() != 3) {
                if (this.validaEmisiones()) {
                    secuencia = secuenciasServices.findNumberByCodigo("SECUENCIA_NUMERO_JUICIO");
                    juicio.setNumeroJuicio(secuencia.getSecuencia().intValue());
                    juicio.setFechaIngreso(new Date());
                    juicio.setCodigoJuicio("GADMCD-DGF-T-JC-" + Utils.getAnio(new Date()).toString() + "-" + juicio.getNumeroJuicio() + nomenclaturaJuicio(juicio.getTipoJuicio()));
                    juicio.setTotalDeuda(totalJuicio);// revisar actualizar cada seleccion
                    juicio.setUsuarioIngreso(session.getUsuario().getNameUsuario());
                    juicio.setNotificaciones(Boolean.FALSE);
                    juicio.setAnioJuicio(Utils.getAnio(new Date()));
                    ingresarTramite();
                    juicio.setTramite(tramite);
                    juicio.setEstadoJuicio(new CoaEstadoJuicio(2L)); //citacion inicial
                    juicio = coactivaService.saveJuicioCoactivoNuevo(juicio, liquidacionesSeleccionadas);
                    if (juicio != null) {
                        JsfUtil.redirectFaces("/moduloGestionTributario/coactiva/ingresoCoactiva.xhtml");
                        JsfUtil.addInformationMessage("Info:", "Juicio guardado correctamente!!!!! \n Numero de Juicio: " + secuencia.getSecuencia());
                    }
                    JsfUtil.update("mainForm");
                }
            } else {
                //masivo //predios
                CoaJuicio juicio = null;
                List<FinaRenLiquidacion> liquidaciones;
                if (this.juicio.getTipoJuicio().getId().intValue() == 2 || this.juicio.getTipoJuicio().getId().intValue() == 3) {
                    for (CatPredio predio : predioSeleccionadosCoactiva) {
                        liquidaciones = new ArrayList<>();
                        juicio = new CoaJuicio();
                        juicio.setTipoJuicio(this.juicio.getTipoJuicio());
                        secuencia = secuenciasServices.findNumberByCodigo("SECUENCIA_NUMERO_JUICIO");
                        juicio.setFechaJuicio(this.juicio.getFechaJuicio());
                        juicio.setFechaIngreso(new Date());
                        juicio.setEstado(this.juicio.getEstado());
                        juicio.setAnioJuicio(this.juicio.getAnioJuicio());
                        juicio.setNumeroJuicio(secuencia.getSecuencia().intValue());
                        juicio.setCodigoJuicio("GADMCD-DGF-T-JC-" + Utils.getAnio(new Date()).toString() + "-" + juicio.getNumeroJuicio() + nomenclaturaJuicio(juicio.getTipoJuicio()));
                        juicio.setUsuarioIngreso(session.getUsuario().getNameUsuario());
                        juicio.setNotificaciones(Boolean.FALSE);
                        juicio.setAnioJuicio(Utils.getAnio(new Date()));
                        juicio.setEstadoJuicio(new CoaEstadoJuicio(2L)); //citacion inicial
                        juicio.setAbogadoJuicio(this.juicio.getAbogadoJuicio());
                        //aqui se añade las liquidaciones al juicio
                        for (FinaRenLiquidacion x : finaRenLiquidacionService.getLiquidacionesByLocal(null, null, predio, 5, juicio.getTipoJuicio().getId())) {
                            liquidaciones.add(x);
                        }
                        juicio.setTotalDeuda(BigDecimal.valueOf(totalPagar));
                        ingresarTramite();
                        juicio.setTramite(tramite);
                        coactivaService.saveJuicioCoactivoNuevo(juicio, liquidaciones);
                    }
                }
//                //masivo //patente
//                if (this.juicio.getTipoJuicio().getId() == 9) {
//                    for (FinaRenPatente patente : patentesSeleccionadas) {
//                        liquidaciones = new ArrayList<>();
//                        juicio = new CoaJuicio();
//                        juicio.setTipoJuicio(this.juicio.getTipoJuicio());
//                        secuencia = secuenciasServices.findNumberByCodigo("SECUENCIA_NUMERO_JUICIO");
//                        juicio.setFechaJuicio(this.juicio.getFechaJuicio());
//                        juicio.setFechaIngreso(new Date());
//                        juicio.setEstado(this.juicio.getEstado());
//                        juicio.setAnioJuicio(this.juicio.getAnioJuicio());
//                        juicio.setNumeroJuicio(secuencia.getSecuencia().intValue());
//                        juicio.setCodigoJuicio("GADMCD-DGF-T-JC-" + Utils.getAnio(new Date()).toString() + "-" + juicio.getNumeroJuicio() + nomenclaturaJuicio(juicio.getTipoJuicio()));
//                        juicio.setUsuarioIngreso(session.getUsuario().getNameUsuario());
//                        juicio.setNotificaciones(Boolean.FALSE);
//                        juicio.setAnioJuicio(Utils.getAnio(new Date()));
//                        juicio.setEstadoJuicio(new CoaEstadoJuicio(2L)); //citacion inicial
//                        juicio.setAbogadoJuicio(this.juicio.getAbogadoJuicio());
//                        //aqui se añade las liquidaciones al juicio
//                        for (FinaRenLiquidacion x : finaRenLiquidacionService.findbyLiquidacionesPatente(patente, juicio.getTipoJuicio())) {
//                            liquidaciones.add(x);
//                        }
//                        juicio.setTotalDeuda(BigDecimal.valueOf(totalPagar));
//                        ingresarTramite();
//                        juicio.setTramite(tramite);
//                        coactivaService.saveJuicioCoactivoNuevo(juicio, liquidaciones);
//                        totalPagar = 0.00;
//                    }
//
//                }
            }
            JsfUtil.addInformationMessage("Se Generó con éxtio los jucios", "");
            limpiarPantalla();
        } catch (Exception e) {
            JsfUtil.addErrorMessage("Error", e.getMessage());
            Logger.getLogger(InicioCoactivaMB.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void limpiarPantalla() {
        juicio = new CoaJuicio();
        juicio.setAnioJuicio(Utils.getAnio(new Date()));
        juicio.setFechaJuicio(new Date());
        juicio.setAbogadoJuicio(new CoaAbogado());
        juicio.setTipoJuicio(new FinaRenTipoLiquidacion(2L));
        consulta = "3";
        prediosAplicanCoactiva.clear();
        listaPatente.clear();
        impuestos.clear();
        cdlaTarget.clear();
        listaCiudadelas.getSource().clear();
        JsfUtil.update("mainForm");

    }

    public void actualizarValorTitulo() {
        for (FinaRenLiquidacion item : impuestos) {
            item.setTotalPago(item.getTotalPago().add(finaRenRubroLiquidacion.getValor()));
            item.setSaldo(item.getSaldo().add(finaRenRubroLiquidacion.getValor()));
            item = interesesService.realizarDescuentoRecargaInteresPredial(item, null);
            item.calcularCoactiva();
        }
        JsfUtil.update("mainForm:dtCoactiva");

    }

    public Boolean validaEmisiones() {
        if (liquidacionesSeleccionadas.isEmpty()) {
            JsfUtil.addErrorMessage("Error", "No hay titulos de credito ingresados para el juicio.");
            return false;
        }
        if (liquidacionesSeleccionadas.get(0).getComprador() == null && liquidacionesSeleccionadas.get(0).getNombreComprador() == null) {
            JsfUtil.addErrorMessage("Error", "Debe Actualizar los Datos del Conribuyente de los años Anteriores");
            return false;
        }
        return true;
    }

    public void ingresarTramite() {
        try {
            tramite = new HistoricoTramites();
            TipoTramite tipo = (TipoTramite) services.find(TipoTramite.class, 124L);
            tramite.setFechaIngreso(new Date());
            tramite.setPeriodo(Utils.getAnio(new Date()).shortValue());
            tramite.setFecha(new Date());
            tramite.setTipoTramite(tipo);
            tramite.setConcepto(observacionDocumento);
            this.getParamts().put("juezcoactiva", session.getNameUser());
            this.getParamts().put("prioridad", 50);
            this.tramite = this.saveTramite();
            if (this.tramite != null && this.tramite.getId() != null) {
                this.setObservacion(new Observaciones());
                this.getObservacion().setObservacion(this.juicio.getObservacion());

                this.saveObs();
            }
            //   this.completeTask((HashMap<String, Object>) this.getParamts());
        } catch (Exception e) {
            Logger.getLogger(InicioCoactivaMB.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void ingresarCoactiva() {
        services.save(juicio);
        guardado = true;
        JsfUtil.addInformationMessage("Juicio guardado correctamente!!!", "");
    }

    public void redirectIngresoJuicio() {
        Date fechaJuicio;
        Integer anioJuicio;
        CoaAbogado abJuicio;
        Usuarios userJuicio;
        if (juicio != null) {
            fechaJuicio = juicio.getFechaJuicio();
            anioJuicio = juicio.getAnioJuicio();
            abJuicio = juicio.getAbogadoJuicio();
            userJuicio = juicio.getUsuarioAsignado();
            juicio = new CoaJuicio();
            juicio.setFechaJuicio(fechaJuicio);
            juicio.setAnioJuicio(anioJuicio);
            juicio.setAbogadoJuicio(abJuicio);
//            juicio.setUsuarioAsignado(userJuicio);
            juicio.setUsuarioAsignado(session.getUsuario());
            guardado = false;
            estado = 1;
            totalJuicio = new BigDecimal("0.00");
            impuestos = new ArrayList<>();
            prediosUrbanos = new ArrayList<>();
            //lecturas = new ArrayList<>();
            convenidos = new ArrayList<>();
            convenioSeleccionado = new FnConvenioPago();
            convenio = new FnConvenioPago();
        }
    }

    public List<CatalogoItem> valoresByCatalogo(String codigo) {
        //'coactiva-tipo_jucio'
        String Query = "Select c from CatalogoItem c where c.catalogo.codigo=:codigo";
        parametros = new HashMap<>();
        parametros.put("codigo", codigo);
        List<CatalogoItem> ctlgItem = (List<CatalogoItem>) services.findAllQuery(Query, parametros);
        return ctlgItem;
    }

    //<editor-fold defaultstate="collapsed" desc="get and set">
    public FinaRenLiquidacion getFinaRenLiquidacion() {
        return finaRenLiquidacion;
    }

    public void setFinaRenLiquidacion(FinaRenLiquidacion finaRenLiquidacion) {
        this.finaRenLiquidacion = finaRenLiquidacion;
    }

    public List<CatPredio> getPredioSeleccionadosCoactiva() {
        return predioSeleccionadosCoactiva;
    }

    public void setPredioSeleccionadosCoactiva(List<CatPredio> predioSeleccionadosCoactiva) {
        this.predioSeleccionadosCoactiva = predioSeleccionadosCoactiva;
    }

    public List<FinaRenPatente> getListaPatente() {
        return listaPatente;
    }

    public void setListaPatente(List<FinaRenPatente> listaPatente) {
        this.listaPatente = listaPatente;
    }

    public FinaRenPatente getPatente() {
        return patente;
    }

    public void setPatente(FinaRenPatente patente) {
        this.patente = patente;
    }

    public List<CatCiudadela> getCdlaseleccionadas() {
        return cdlaseleccionadas;
    }

    public void setCdlaseleccionadas(List<CatCiudadela> cdlaseleccionadas) {
        this.cdlaseleccionadas = cdlaseleccionadas;
    }

    public List<PredioDTO> getPredioDTO() {
        return predioDTO;
    }

    public void setPredioDTO(List<PredioDTO> predioDTO) {
        this.predioDTO = predioDTO;
    }

    public CatPredio getPredio() {
        return predio;
    }

    public void setPredio(CatPredio predio) {
        this.predio = predio;
    }

//    public List<CatPredio> getPrediosCoactiva() {
//        return prediosCoactiva;
//    }
//
//    public void setPrediosCoactiva(List<CatPredio> prediosCoactiva) {
//        this.prediosCoactiva = prediosCoactiva;
//    }
    public FinaRenRubrosLiquidacion getFinaRenRubroLiquidacion() {
        return finaRenRubroLiquidacion;
    }

    public void setFinaRenRubroLiquidacion(FinaRenRubrosLiquidacion finaRenRubroLiquidacion) {
        this.finaRenRubroLiquidacion = finaRenRubroLiquidacion;
    }

    public DualListModel<CatCiudadela> getListaCiudadelas() {
        return listaCiudadelas;
    }

    public void setListaCiudadelas(DualListModel<CatCiudadela> listaCiudadelas) {
        this.listaCiudadelas = listaCiudadelas;
    }

    public List<FinaRenRubrosLiquidacion> getFinaRenRubrosLiquidacioness() {
        return finaRenRubrosLiquidacioness;
    }

    public void setFinaRenRubrosLiquidacioness(List<FinaRenRubrosLiquidacion> finaRenRubrosLiquidacioness) {
        this.finaRenRubrosLiquidacioness = finaRenRubrosLiquidacioness;
    }

    public SecuenciaGeneral getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(SecuenciaGeneral secuencia) {
        this.secuencia = secuencia;
    }

    public ObservacionesService getObservacionesService() {
        return observacionesService;
    }

    public void setObservacionesService(ObservacionesService observacionesService) {
        this.observacionesService = observacionesService;
    }

    public CatPredioPropietarioService getCatPredioPropietarioService() {
        return catPredioPropietarioService;
    }

    public void setCatPredioPropietarioService(CatPredioPropietarioService catPredioPropietarioService) {
        this.catPredioPropietarioService = catPredioPropietarioService;
    }

    public SecuenciasServices getSecuenciasServices() {
        return secuenciasServices;
    }

    public void setSecuenciasServices(SecuenciasServices secuenciasServices) {
        this.secuenciasServices = secuenciasServices;
    }

    public FinaRenLiquidacionService getFinaRenLiquidacionService() {
        return finaRenLiquidacionService;
    }

    public void setFinaRenLiquidacionService(FinaRenLiquidacionService finaRenLiquidacionService) {
        this.finaRenLiquidacionService = finaRenLiquidacionService;
    }

    public LazyModel<CatPredioPropietario> getPropietariosLista() {
        return propietariosLista;
    }

    public void setPropietariosLista(LazyModel<CatPredioPropietario> propietariosLista) {
        this.propietariosLista = propietariosLista;
    }

    public List<FinaRenLiquidacion> getLiquidacionesSeleccionadas() {
        return liquidacionesSeleccionadas;
    }

    public void setLiquidacionesSeleccionadas(List<FinaRenLiquidacion> liquidacionesSeleccionadas) {
        this.liquidacionesSeleccionadas = liquidacionesSeleccionadas;
    }

    public ManagerService getServices() {
        return services;
    }

    public void setServices(ManagerService services) {
        this.services = services;
    }

    public ServletSession getSs() {
        return ss;
    }

    public void setSs(ServletSession ss) {
        this.ss = ss;
    }

    public List<String> getCiudadelas() {
        return ciudadelas;
    }

    public void setCiudadelas(List<String> ciudadelas) {
        this.ciudadelas = ciudadelas;
    }

    public Boolean getUrbanistico() {
        return urbanistico;
    }

    public void setUrbanistico(Boolean urbanistico) {
        this.urbanistico = urbanistico;
    }

    public Boolean getGuardado() {
        return guardado;
    }

    public void setGuardado(Boolean guardado) {
        this.guardado = guardado;
    }

    public BigDecimal getTotalJuicio() {
        return totalJuicio;
    }

    public void setTotalJuicio(BigDecimal totalJuicio) {
        this.totalJuicio = totalJuicio;
    }

    public LazyModel<CatPredioModel> getPredios() {
        return predios;
    }

    public void setPredios(LazyModel<CatPredioModel> predios) {
        this.predios = predios;
    }

    public CoaJuicio getJuicio() {
        return juicio;
    }

    public void setJuicio(CoaJuicio juicio) {
        this.juicio = juicio;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    public String getObservacionDocumento() {
        return observacionDocumento;
    }

    public void setObservacionDocumento(String observacionDocumento) {
        this.observacionDocumento = observacionDocumento;
    }

    public String getFormatoArchivos() {
        return formatoArchivos;
    }

    public void setFormatoArchivos(String formatoArchivos) {
        this.formatoArchivos = formatoArchivos;
    }

    public HistoricoTramites getHt() {
        return ht;
    }

    public void setHt(HistoricoTramites ht) {
        this.ht = ht;
    }

    public List<Usuarios> getUsers() {
        return users;
    }

    public void setUsers(List<Usuarios> users) {
        this.users = users;
    }

    public List<CoaJuicioPredio> getDetalleJuicio() {
        return detalleJuicio;
    }

    public void setDetalleJuicio(List<CoaJuicioPredio> detalleJuicio) {
        this.detalleJuicio = detalleJuicio;
    }

    public List<CoaAbogado> getListAbogados() {
        return listAbogados;
    }

    public void setListAbogados(List<CoaAbogado> listAbogados) {
        this.listAbogados = listAbogados;
    }

    public List<CoaEstadoJuicio> getListEstados() {
        return listEstados;
    }

    public void setListEstados(List<CoaEstadoJuicio> listEstados) {
        this.listEstados = listEstados;
    }

    public List<FinaRenLiquidacion> getImpuestos() {
        return impuestos;
    }

    public void setImpuestos(List<FinaRenLiquidacion> impuestos) {
        this.impuestos = impuestos;
    }

    public List<CatCiudadela> getCiudadelasEntidad() {
        return ciudadelasEntidad;
    }

    public void setCiudadelasEntidad(List<CatCiudadela> ciudadelasEntidad) {
        this.ciudadelasEntidad = ciudadelasEntidad;
    }

    public Map<String, Object> getParametros() {
        return parametros;
    }

    public void setParametros(Map<String, Object> parametros) {
        this.parametros = parametros;
    }

    public Map<String, String> getOrder() {
        return order;
    }

    public void setOrder(Map<String, String> order) {
        this.order = order;
    }

    public CatPredioModel getPredioUrbanoConsulta() {
        return predioUrbanoConsulta;
    }

    public void setPredioUrbanoConsulta(CatPredioModel predioUrbanoConsulta) {
        this.predioUrbanoConsulta = predioUrbanoConsulta;
    }

    public String getConsulta() {
        return consulta;
    }

    public void setConsulta(String consulta) {
        this.consulta = consulta;
    }

    public List<CatPredioModel> getPrediosUrbanosConsultaSeleccionados() {
        return prediosUrbanosConsultaSeleccionados;
    }

    public void setPrediosUrbanosConsultaSeleccionados(List<CatPredioModel> prediosUrbanosConsultaSeleccionados) {
        this.prediosUrbanosConsultaSeleccionados = prediosUrbanosConsultaSeleccionados;
    }

    public List<CatPredioModel> getPrediosUrbanosConsulta() {
        return prediosUrbanosConsulta;
    }

    public void setPrediosUrbanosConsulta(List<CatPredioModel> prediosUrbanosConsulta) {
        this.prediosUrbanosConsulta = prediosUrbanosConsulta;
    }

    public List<CatPredioModel> getPrediosUrbanos() {
        return prediosUrbanos;
    }

    public void setPrediosUrbanos(List<CatPredioModel> prediosUrbanos) {
        this.prediosUrbanos = prediosUrbanos;
    }

    public LazyModel<FnConvenioPago> getConvenios() {
        return convenios;
    }

    public void setConvenios(LazyModel<FnConvenioPago> convenios) {
        this.convenios = convenios;
    }

    public FnConvenioPago getConvenio() {
        return convenio;
    }

    public void setConvenio(FnConvenioPago convenio) {
        this.convenio = convenio;
    }

    public FnConvenioPago getConvenioSeleccionado() {
        return convenioSeleccionado;
    }

    public void setConvenioSeleccionado(FnConvenioPago convenioSeleccionado) {
        this.convenioSeleccionado = convenioSeleccionado;
    }

    public List<FnConvenioPagoDetalle> getConvenidos() {
        return convenidos;
    }

    public void setConvenidos(List<FnConvenioPagoDetalle> convenidos) {
        this.convenidos = convenidos;
    }

    public SimpleDateFormat getSdf() {
        return sdf;
    }

    public void setSdf(SimpleDateFormat sdf) {
        this.sdf = sdf;
    }

    public Calendar getCal() {
        return cal;
    }

    public void setCal(Calendar cal) {
        this.cal = cal;
    }

    public LazyModel<FinaRenLiquidacion> getTitulosCredito() {
        return titulosCredito;
    }

    public void setTitulosCredito(LazyModel<FinaRenLiquidacion> titulosCredito) {
        this.titulosCredito = titulosCredito;
    }

    public CoaJuicioService getCoactivaService() {
        return coactivaService;
    }

    public void setCoactivaService(CoaJuicioService coactivaService) {
        this.coactivaService = coactivaService;
    }

    public FinaRenTipoLiquidacion getTipoLiquidacion() {
        return tipoLiquidacion;
    }

    public void setTipoLiquidacion(FinaRenTipoLiquidacion tipoLiquidacion) {
        this.tipoLiquidacion = tipoLiquidacion;
    }

    public List<FinaRenTipoLiquidacion> getTipoLiquidaciones() {
        return tipoLiquidaciones;
    }

    public void setTipoLiquidaciones(List<FinaRenTipoLiquidacion> tipoLiquidaciones) {
        this.tipoLiquidaciones = tipoLiquidaciones;
    }

    public CatPredioPropietario getPropietario() {
        return propietario;
    }

    public void setPropietario(CatPredioPropietario propietario) {
        this.propietario = propietario;
    }

    public ArrayList<Archivo> getFiles() {
        return files;
    }

    public void setFiles(ArrayList<Archivo> files) {
        this.files = files;
    }

    public String getDiv1() {
        return div1;
    }

    public void setDiv1(String div1) {
        this.div1 = div1;
    }

    public String getDiv2() {
        return div2;
    }

    public void setDiv2(String div2) {
        this.div2 = div2;
    }

    public String getDiv3() {
        return div3;
    }

    public void setDiv3(String div3) {
        this.div3 = div3;
    }

    public String getDiv4() {
        return div4;
    }

    public void setDiv4(String div4) {
        this.div4 = div4;
    }

    public List<CatCiudadela> getCdlaTarget() {
        return cdlaTarget;
    }

    public void setCdlaTarget(List<CatCiudadela> cdlaTarget) {
        this.cdlaTarget = cdlaTarget;
    }

    public List<CatCiudadela> getCdlaSource() {
        return cdlaSource;
    }

    public void setCdlaSource(List<CatCiudadela> cdlaSource) {
        this.cdlaSource = cdlaSource;
    }

    public SectoresService getSectoresSevice() {
        return sectoresSevice;
    }

    public void setSectoresSevice(SectoresService sectoresSevice) {
        this.sectoresSevice = sectoresSevice;
    }

    public List<CatSectores> getSectoresTarget() {
        return sectoresTarget;
    }

    public void setSectoresTarget(List<CatSectores> sectoresTarget) {
        this.sectoresTarget = sectoresTarget;
    }

    public List<CatSectores> getSectoresSource() {
        return sectoresSource;
    }

    public void setSectoresSource(List<CatSectores> sectoresSource) {
        this.sectoresSource = sectoresSource;
    }

    public List<CatSectores> getSectoreSeleccionadas() {
        return sectoreSeleccionadas;
    }

    public void setSectoreSeleccionadas(List<CatSectores> sectoreSeleccionadas) {
        this.sectoreSeleccionadas = sectoreSeleccionadas;
    }

    public DualListModel<CatSectores> getListaSectores() {
        return listaSectores;
    }

    public void setListaSectores(DualListModel<CatSectores> listaSectores) {
        this.listaSectores = listaSectores;
    }

    public List<CatPredio> getPrediosAplicanCoactiva() {
        return prediosAplicanCoactiva;
    }

    public void setPrediosAplicanCoactiva(List<CatPredio> prediosAplicanCoactiva) {
        this.prediosAplicanCoactiva = prediosAplicanCoactiva;
    }

    public FinaRenLiquidacion getLiquidacion() {
        return liquidacion;
    }

    public void setLiquidacion(FinaRenLiquidacion liquidacion) {
        this.liquidacion = liquidacion;
    }

    public List<FinaRenPatente> getPatentesSeleccionadas() {
        return patentesSeleccionadas;
    }

    public void setPatentesSeleccionadas(List<FinaRenPatente> patentesSeleccionadas) {
        this.patentesSeleccionadas = patentesSeleccionadas;
    }

}
//</editor-fold>

