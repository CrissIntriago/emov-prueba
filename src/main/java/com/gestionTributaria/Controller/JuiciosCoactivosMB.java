/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Controller;

import com.asgard.Entity.FinaRenLiquidacion;
import com.asgard.Entity.FinaRenPago;
import com.asgard.Entity.FinaRenPagoDetalle;
import com.asgard.Entity.FinaRenTipoLiquidacion;
import com.gestionTributaria.Commons.SisVars;
import com.gestionTributaria.Entities.CatPredio;
import com.gestionTributaria.Entities.CoaAbogado;
import com.gestionTributaria.Entities.CoaEstadoJuicio;
import com.gestionTributaria.Entities.CoaJuicio;
import com.gestionTributaria.Entities.CoaJuicioPredio;
import com.gestionTributaria.Entities.CoaMedidasCautelares;
import com.gestionTributaria.Entities.Documentos;
import com.gestionTributaria.Entities.FnConvenioPago;
import com.gestionTributaria.Entities.FnLiquidacionConvenio;
import com.gestionTributaria.Services.CatCiudadelasService;
import com.gestionTributaria.Services.CatPredioPropietarioService;
import com.gestionTributaria.Services.CoaJuicioPredioServices;
import com.gestionTributaria.Services.CoaJuicioService;
import com.gestionTributaria.Services.FinaRenLiquidacionService;
import com.gestionTributaria.Services.FinaRenPagoService;
import com.gestionTributaria.Services.FinaRenTipoLiquidacionService;
import com.gestionTributaria.Services.FnConvenioPagoService;
import com.gestionTributaria.Services.FnLiquidacionConvenioService;
import com.gestionTributaria.Services.ManagerService;
import com.gestionTributaria.Services.MedidasCautelaresServices;
import com.gestionTributaria.models.CatPredioModel;
import com.origami.sigef.catalogoItem.service.CatalogoItemService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.Usuarios;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.service.interfaces.BpmBaseEngine;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.resource.procesos.entities.HistoricoTramites;
import com.origami.sigef.resource.procesos.entities.Observaciones;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.activiti.engine.history.HistoricTaskInstance;
import org.primefaces.event.FileUploadEvent;

/**
 *
 * @author Administrator
 */
@Named
@ViewScoped
public class JuiciosCoactivosMB implements Serializable {

    @Inject
    private ServletSession ss;
    @Inject
    private UserSession session;
    @Inject
    private ManagerService services;
    @Inject
    private CoaJuicioService coaJuicioService;
    @Inject
    private BpmBaseEngine baseEngine;
    @Inject
    private CoaJuicioPredioServices coaJuicioPredioServices;
    @Inject
    private FinaRenLiquidacionService finaRenLiquidacionServices;
    @Inject
    private FinaRenTipoLiquidacionService finaRenTipoLiquidacionService;
    @Inject
    private CatCiudadelasService ciudadelasService;
    @Inject
    private CatPredioPropietarioService catPredioPropietarioService;
    @Inject
    private CatalogoItemService catalogoService;
    @Inject
    private MedidasCautelaresServices cautelaresServices;
    @Inject
    private FnLiquidacionConvenioService convenioService;
    @Inject
    private FnConvenioPagoService convenioPagoService;
    @Inject
    private FinaRenPagoService pagoService;

    private Documentos documento;
    //para el filtro por fecha
    private Date fechaJuicioFiltro;
    
    private Date fechaJuicioFiltro2;

    public Date getFechaJuicioFiltro2() {
        return fechaJuicioFiltro2;
    }

    public void setFechaJuicioFiltro2(Date fechaJuicioFiltro2) {
        this.fechaJuicioFiltro2 = fechaJuicioFiltro2;
    }
    //para el criterio de búsuedad
    private String criterioBusquedad = "";
    //opcion seleccionada para el criterio de busqedad
    private String opcionBusquedad;
    //tiene los tipo de liquidacion que aplican coactiva
    private List<FinaRenTipoLiquidacion> finaRenTiposLiquidaciones;
    //guarda el tipo de liquidacion selecionado
    private FinaRenTipoLiquidacion tipoLiquidacion;
    private String noOficio = "";
    private LazyModel<CoaJuicio> juicios;
    private CoaJuicio juicio = new CoaJuicio();
    private List<CoaAbogado> listAbogados = new ArrayList<>();
    private List<CoaEstadoJuicio> listEstados = new ArrayList<>();
    private List<FinaRenLiquidacion> titulos = new ArrayList<>();
    private List<FinaRenLiquidacion> titulosConsulta = new ArrayList<>();
    private FinaRenLiquidacion liquidacionSeleccionada;
    private List<HistoricTaskInstance> tareas = new ArrayList<>();
    private CoaJuicioPredio juicioPredio;
    private Observaciones obs;
    private String observacion;
    private Long tipoReporte;
    private List<Usuarios> funcionarios;
    private List<Long> listAbogadosReporte = new ArrayList<>();
    private List<Long> listEstadosReporte = new ArrayList<>();
    private CoaEstadoJuicio estadoEscogido;
    private FnLiquidacionConvenio liquidacionConvenio;
    private List<FinaRenPago> pagos;
    private List<Long> funcionariosReporte;
    private Date fechaJuicioDesde;
    private Date fechaJuicioHasta;
    private Date fechaIngresoDesde;
    private Date fechaIngresoHasta;
    private Integer numDesde;
    private Integer numHasta;
    private Integer anioDesde;
    private Integer anioHasta;
    private Boolean porNumero = Boolean.FALSE;
    private Boolean porAnio = Boolean.FALSE;
    private Boolean porFechaJuicio = Boolean.FALSE;
    private Boolean porFechaIngreso = Boolean.FALSE;
    private Boolean porEstado = Boolean.FALSE;
    private Boolean porTipoReporte = Boolean.FALSE;
    private Observaciones observacionInactivo;
    private CatPredioModel predioConsulta;
    private List<CoaJuicio> juiciosConsulta;
    private CatPredioModel predioModel = new CatPredioModel();
    private Map<String, Object> parametros;
    private List<CatPredioModel> prediosUrbanosConsultaSeleccionados;
    private List<CatPredioModel> prediosUrbanosConsulta;
    private Boolean inactivar = Boolean.FALSE;
    private Boolean esPdf, esExcel;
    //lista de juicios del predio
    private List<CoaJuicio> historialJuicios;
    //PARA TRAER LA CLAVE CATASTRAL
    private CoaJuicioPredio coaJuicioPredio;
    //LISTA DE Paagos
    private List<FinaRenLiquidacion> liquidacionesPagos;
    //PARA EL TIPO DE FILTRO POR CLAVE CATASTRAL
    private String tipoPredio;
    //PARA BUSCAR LA CLAVE CATASTRAL
    private CatPredio predio;
    private String div1 = "", div2 = "", div3 = "", div4 = "";
    private Date fechaDesde;
    private Date fechaHasta;
    private CoaAbogado abogado;
    private List<CatalogoItem> medidasCautelaresAplicadas;
    private List<CatalogoItem> medidasCautelares;
    private List<Documentos> listaDocumentos;
    private FnConvenioPago convenioPago;
    private FinaRenPago finaRenPago;
    private FinaRenPagoDetalle finaRenPagoDetalle;

    @PostConstruct
    public void iniView() {
        finaRenPagoDetalle = new FinaRenPagoDetalle();
        finaRenPago = new FinaRenPago();
        convenioPago = new FnConvenioPago();
        liquidacionConvenio = new FnLiquidacionConvenio();
        listaDocumentos = new ArrayList<>();
        abogado = new CoaAbogado();
        //listado de jujicios
        fechaDesde = new Date();
        fechaHasta = new Date();
        tipoPredio = "U";
        juicios = new LazyModel<>(CoaJuicio.class);
        //historial de juicios
        historialJuicios = new ArrayList<>();
        coaJuicioPredio = new CoaJuicioPredio();
        fechaJuicioFiltro = new Date();
        fechaJuicioFiltro2 = new Date();
        tipoLiquidacion = new FinaRenTipoLiquidacion();
        opcionBusquedad = "1";
        documento = new Documentos();
        finaRenTiposLiquidaciones = finaRenTipoLiquidacionService.findTipoLiquidacionAplicaCoactiva();
        juicioPredio = new CoaJuicioPredio();
        esPdf = true;
        esExcel = false;
        juicios.getSorteds().put("anioJuicio", "desc");
        listAbogados = services.findAllQuery("Select a from CoaAbogado a where a.estado=true order by a.detalle", null);
        listEstados = services.findAllQuery("Select c from CoaEstadoJuicio c where c.estado = true order by c.secuencia", null);
        funcionarios = services.allUserFindById("200L"); /// ni idea cual sea el id 200
        juiciosConsulta = new ArrayList<>();
        liquidacionesPagos = new ArrayList<>();
        predio = new CatPredio();
        estadoEscogido = new CoaEstadoJuicio();
        medidasCautelares = catalogoService.findCatalogoItems("Medidas_Cautelares");

    }

    public void traerPagos(FinaRenLiquidacion liquidacion) {
        pagos = pagoService.findByPagoLiquidacion(liquidacion);
    }

    public void verDetallePago() {
        finaRenPagoDetalle = pagoService.findByFinaRenPago(finaRenPago);
        if (finaRenPagoDetalle == null) {
            finaRenPagoDetalle = new FinaRenPagoDetalle();
        }
    }

    public void limpiarFiltrosCriteriosBusquedad() {
        juicios.getFilterss().clear();
    }

    public void generarFiltro() {
        criterioBusquedad = "";
        juicios.getFilterss().put("fechaJuicio",Arrays.asList(getFechaJuicioFiltro2(),getFechaJuicioFiltro()));
    }

    public void cargarPorCriteriosJuiciosIngresados() {
        limpiarFiltrosCriteriosBusquedad();
//        if (!criterioBusquedad.isEmpty() || tipoLiquidacion.getId() != null) {
        switch (opcionBusquedad) {
            case "1":
                if (!criterioBusquedad.isEmpty()) {
                    juicios.getFilterss().put("coaJuicioPredioList.predio.codigoPredio", criterioBusquedad);
                }
                break;
            case "2":
                if (!criterioBusquedad.isEmpty()) {
                    juicios.getFilterss().put("coaJuicioPredioList.predio.catPredioPropietarioList.nombresCompletos", criterioBusquedad.trim().toUpperCase());
                }
                break;
            case "3":
                juicios.getFilterss().put("coaJuicioPredioList.liquidacion.tipoLiquidacion.id", tipoLiquidacion.getId());
                break;
            case "4":
                if (predio.getParroquia() != null) {
                    if (tipoPredio.equals("U")) {
                        criterioBusquedad = "";
                        //parroquia
                        criterioBusquedad = predio.getParroquia() == null ? criterioBusquedad + "1." : criterioBusquedad + predio.getParroquia() + ".";
                        //sector
                        criterioBusquedad = predio.getSector() == null ? criterioBusquedad + "0." : criterioBusquedad + predio.getSector() + ".";
                        //manzana
                        criterioBusquedad = predio.getMz() == null ? criterioBusquedad + "0." : criterioBusquedad + predio.getMz() + ".";
                        //solar
                        criterioBusquedad = predio.getSolar() == null ? criterioBusquedad + "0." : criterioBusquedad + predio.getSolar() + ".";
                        //div 1
                        criterioBusquedad = "".equals(div1) ? criterioBusquedad + "0." : criterioBusquedad + div1 + ".";
                        //div 2
                        criterioBusquedad = "".equals(div2) ? criterioBusquedad + "0." : criterioBusquedad + div2 + ".";
                        //div 3
                        criterioBusquedad = "".equals(div3) ? criterioBusquedad + "0." : criterioBusquedad + div3 + ".";
                        //div 4
                        criterioBusquedad = "".equals(div4) ? criterioBusquedad + "0." : criterioBusquedad + div4 + ".";
                        //div phv
                        criterioBusquedad = predio.getBloque() == null ? criterioBusquedad + "0." : criterioBusquedad + predio.getBloque() + ".";
                        //div phh
                        criterioBusquedad = predio.getPiso() == null ? criterioBusquedad + "0" : criterioBusquedad + predio.getPiso();
                        predio.setClaveCat(criterioBusquedad);
                    }
                    //clave catastral para rural
                    if (tipoPredio.equals("R")) {
                        criterioBusquedad = "";
                        //parroquia
                        criterioBusquedad = predio.getParroquia() == null ? criterioBusquedad + "1." : criterioBusquedad + predio.getParroquia() + ".";
                        //sector
                        criterioBusquedad = predio.getSector() == null ? criterioBusquedad + "0." : criterioBusquedad + predio.getSector() + ".";
                        //codigo
                        criterioBusquedad = "".equals(div3) ? criterioBusquedad + "0." : criterioBusquedad + div3 + ".";
                        //div 1
                        criterioBusquedad = "".equals(div1) ? criterioBusquedad + "0." : criterioBusquedad + div1 + ".";
                        //div 2
                        criterioBusquedad = "".equals(div2) ? criterioBusquedad + "0" : criterioBusquedad + div2;
                        predio.setClaveCat(criterioBusquedad);
                    }
                    if (!criterioBusquedad.isEmpty()) {
                        juicios.getFilterss().put("coaJuicioPredioList.predio.claveCat", predio.getClaveCat());
                    }
                    break;
                }
        }
        JsfUtil.update("mainForm:dtJuiciosCoa");
    }

    public void limpiarCriterioBusquedad() {
        criterioBusquedad = "";
        predio = new CatPredio();
    }

    public void capturarLiquidacionDetalle(FinaRenLiquidacion liquidacion) {
        liquidacionSeleccionada = liquidacion;
        liquidacion.calcularPagoConCoactiva();
        JsfUtil.update("dlgDetalle");
        JsfUtil.executeJS("PF('dlgDetalle').show();");
    }

    public void generarMemo(Boolean esPdf) {
        try {
            ss.borrarDatos();
            ss.borrarParametros();
            switch (tipoReporte.intValue()) {
                case 1: {
                    System.out.println("CASO 1");
                    System.out.println("FECHA DESDE: " + fechaDesde);
                    System.out.println("FECHA DESDE: " + fechaHasta);
                    ss.setNombreReporte("JUICIOSINGRESADOS");
                    ss.setNombreSubCarpeta("/GestionTributatia/Coactiva/reporteJuicios");
                    ss.addParametro("FECHA_DESDE", fechaDesde);
                    ss.addParametro("FECHA_HASTA", fechaHasta);
                }
                break;
                case 2: {
                    System.out.println("CASO 2");
                    ss.setNombreReporte("JUICIOPORABOGADO");
                    ss.setNombreSubCarpeta("/GestionTributatia/Coactiva/reporteJuicios");
                    ss.addParametro("FECHA_DESDE", fechaDesde);
                    ss.addParametro("FECHA_HASTA", fechaHasta);
                    ss.addParametro("ABOGADO", abogado.getId());
                    ss.addParametro("NOMBRE_ABOGADO", abogado.getDetalle());
                    ss.addParametro("DESDE_TEXTO", Utils.getDia(fechaDesde).toString() + "/" + Utils.getMes(fechaDesde).toString() + "/" + Utils.getAnio(fechaDesde).toString());
                    ss.addParametro("HASTA_TEXTO", Utils.getDia(fechaHasta).toString() + "/" + Utils.getMes(fechaHasta).toString() + "/" + Utils.getAnio(fechaHasta).toString());
                }
                break;
                case 5:

                    switch (estadoEscogido.getAbreviatura()) {
                        /*CITACION INICIAL*/
                        case "CI":
                            System.out.println("pruebita");
                            System.out.println("estado_id:" + estadoEscogido.getId());
                            ss.addParametro("TITULO_REPORTE", "REPORTE DE JUICIO POR CITACIÓN INICIAL");
                            ss.addParametro("estado_id", estadoEscogido.getId());
                            ss.addParametro("IMG_URL", JsfUtil.getRealPath("resources/images/escudo_duran.png"));
                            ss.setNombreReporte("juiciosPorEstado");
                            ss.setNombreSubCarpeta("GestionTributatia/Coactiva/Juicios");

                            break;
                        /*EMISION DE MEDIDAS CAUTELARES*/
                        case "EMC":
                            System.out.println("estado_id:" + estadoEscogido.getId());
                            ss.addParametro("TITULO_REPORTE", "REPORTE DE JUICIO POR EMISIÓN DE MEDIDAS CAUTELARES");
                            ss.addParametro("estado_id", estadoEscogido.getId());
                            ss.addParametro("IMG_URL", JsfUtil.getRealPath("resources/images/escudo_duran.png"));
                            ss.setNombreReporte("juiciosPorEstado");
                            ss.setNombreSubCarpeta("GestionTributatia/Coactiva/Juicios");

                            break;

                        /*BAJA Y ARCHIVO DE LA CAUSA*/
                        case "BAC":

                            System.out.println("estado_id:" + estadoEscogido.getId());
                            ss.addParametro("TITULO_REPORTE", "REPORTE DE JUICIO POR  BAJA Y ARCHIVO DE LA CAUSA");
                            ss.addParametro("estado_id", estadoEscogido.getId());
                            ss.addParametro("IMG_URL", JsfUtil.getRealPath("resources/images/escudo_duran.png"));
                            ss.setNombreReporte("juiciosPorEstado");
                            ss.setNombreSubCarpeta("GestionTributatia/Coactiva/Juicios");

                            break;
                        /*CONVENIOS DE PAGO*/
                        case "CDP":
                            System.out.println("estado_id:" + estadoEscogido.getId());
                            ss.addParametro("TITULO_REPORTE", "REPORTE DE JUICIO POR CONVENIO DE PAGO");
                            ss.addParametro("estado_id", estadoEscogido.getId());
                            ss.addParametro("IMG_URL", JsfUtil.getRealPath("resources/images/escudo_duran.png"));
                            ss.setNombreReporte("juiciosPorEstado");
                            ss.setNombreSubCarpeta("GestionTributatia/Coactiva/Juicios");

                            break;

                    }

                    break;
            }

            ss.setImprimir(Boolean.FALSE);
            if (esPdf) {
                JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
            } else {
                JsfUtil.redirectNewTab(SisVars.urlbase + "DocumentoExcel");
            }
        } catch (Exception e) {
            Logger.getLogger(JuiciosCoactivosMB.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void imprimirAutoPago(CoaJuicio juicio) {
        try {
            System.out.println("JUICIO:" + juicio);
            List<CoaJuicioPredio> juiciosPredios = coaJuicioPredioServices.findCoaJuiciosPredios(juicio);
            String aniosDeudas = "";
            coaJuicioPredio = coaJuicioPredioServices.findCoaJuicioPredio(juicio);
//            CatPredioPropietario propietario = catPredioPropietarioService.findByPropietario(coaJuicioPredio.getPredio());
            juicio.setNotificaciones(Boolean.TRUE);
            coaJuicioService.edit(juicio);
            //recorremos los años de la deuda para enviar al resporte
            for (CoaJuicioPredio item : juiciosPredios) {
                aniosDeudas = item.getLiquidacion().getAnio().toString() + " - ";
            }
            ss.setNombreReporte("AUTOPAGO");
            ss.setNombreSubCarpeta("/GestionTributatia/Coactiva/Boletas");
            ss.addParametro("ANIOSDEUDA", aniosDeudas);
            System.out.println("el id del juicio es----> "+coaJuicioPredio.getId());
            ss.addParametro("TOTALJUICIO", "$ " + coaJuicioPredio.getJuicio().getTotalDeuda().toString());
            ss.addParametro("ABOGADO", coaJuicioPredio.getAbogadoJuicio() == null ? "" : coaJuicioPredio.getAbogadoJuicio().getDetalle());
            ss.addParametro("NROJUICIO", coaJuicioPredio.getJuicio().getCodigoJuicio());
            ss.addParametro("FECHA", " " + Utils.getDia(new Date()).toString() + " de " + mesLetra(Utils.getMes(new Date()) - 1) + " del " + Utils.getAnio(new Date()));
            ss.addParametro("HORA", "" + Utils.getHour(new Date()).toString() + ":" + Utils.getMinute(new Date()).toString() + ":" + Utils.getSecond(new Date()).toString());
            ss.addParametro("DIRECCION", coaJuicioPredio.getPredio().getDireccion());
            ss.addParametro("CONTRIBUYENTE", coaJuicioPredio.getPredio().getNombrePosesionario());
            ss.addParametro("CLAVECAT", coaJuicioPredio.getPredio().getClaveCat());
            ss.setImprimir(Boolean.FALSE);
            JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
        } catch (Exception ex) {
            Logger.getLogger(JuiciosCoactivosMB.class.getName()).log(Level.SEVERE, "Error al imprimir auto de pago inicial", ex);
        }
    }

    public void imprimirBoletas(CoaJuicio juicio) {
        try {
            System.out.println("JUICIO:" + juicio);
            List<CoaJuicioPredio> juiciosPredios = coaJuicioPredioServices.findCoaJuiciosPredios(juicio);
            String aniosDeudas = "";
            coaJuicioPredio = coaJuicioPredioServices.findCoaJuicioPredio(juicio);
            if (coaJuicioPredio != null) {
//            CatPredioPropietario propietario = catPredioPropietarioService.findByPropietario(coaJuicioPredio.getPredio());
                juicio.setNotificaciones(Boolean.TRUE);
                coaJuicioService.edit(juicio);
                System.out.println("LA  CLAVE CATASTRAL" + coaJuicioPredio.getPredio().getClaveCat());
                //recorremos los años de la deuda para enviar al resporte
                for (CoaJuicioPredio item : juiciosPredios) {
                    aniosDeudas = item.getLiquidacion().getAnio().toString() + " - ";
                }
                ss.setNombreReporte("BOLETAS");
                ss.setNombreSubCarpeta("/GestionTributatia/Coactiva/Boletas");
                ss.addParametro("NROJUICIO", juicio.getCodigoJuicio());
                ss.addParametro("ANIOSDEUDA", aniosDeudas);
                ss.addParametro("CLAVECATASTRAL", coaJuicioPredio.getPredio().getClaveCat());
                ss.addParametro("CONTRIBUYENTE", coaJuicioPredio.getLiquidacion().getComprador() == null ? "" : coaJuicioPredio.getLiquidacion().getComprador().getNombreCompleto());
                ss.addParametro("TOTALJUICIO", String.valueOf(juicio.getTotalDeuda()));
                System.out.println("juicio.getTotalDeuda() es---> "+juicio.getTotalDeuda());
                ss.addParametro("ABOGADOIMPULSADOR", juicio.getAbogadoJuicio().getDetalle());
                ss.addParametro("SECRETARIOCOACTIVA", "Ab. VICTORIA CALDERON CARRERA");
                ss.addParametro("JUEZCOACTIVA", "Ab. FREDDY FIENCO ZAMBRANO");
                ss.addParametro("DIRECCION", coaJuicioPredio.getPredio().getDireccion());
                ss.addParametro("FECHAHORA", "" + Utils.getDia(new Date()).toString() + " de " + mesLetra(Utils.getMes(new Date()) - 1) + " del " + Utils.getAnio(new Date()));
                ss.addParametro("NROBOLETA", "1");
                ss.setImprimir(Boolean.FALSE);
                JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
            } else {
                JsfUtil.addWarningMessage("", "Ocurrió un error al imprimir la boleta");
            }
        } catch (Exception ex) {
            Logger.getLogger(JuiciosCoactivosMB.class.getName()).log(Level.SEVERE, "Error al imprimir auto de pago inicial", ex);
        }
    }

    public String mesLetra(int num) {
        String month = "wrong";
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();
        if (num >= 0 && num <= 11) {
            month = months[num];
        }
        return month;
    }

    public void cambiarBoolean() {
        esExcel = !esPdf;
    }

    public void cambiarBoolean2() {
        esPdf = !esExcel;
    }

    public void showDlgInfJuicio(CoaJuicio ju) {
        try {
            juicio = ju;
            coaJuicioPredio = coaJuicioPredioServices.findCoaJuicioPredio(ju);
            historialJuicios = coaJuicioPredioServices.antecedentesJudiciales(ju);
            liquidacionesPagos = coaJuicioPredioServices.findHistorialPagos(coaJuicioPredio);
            JsfUtil.update("formInformCoa");
            JsfUtil.executeJS("PF('dlgVerInfoCoa').show();");
        } catch (Exception e) {
            Logger.getLogger(CoaJuicio.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void showDlgEditJuicio(CoaJuicio ju) {
        observacionInactivo = new Observaciones();
        juicio = ju;
        medidasCautelaresAplicadas = cautelaresServices.findMedidasCautelares(juicio);
        //traer documentos
        listaDocumentos = (List<Documentos>) services.documentoGestionTribtariaActivos(juicio.getClass().getPackage().getName() + "." + juicio.getClass().getSimpleName(), juicio.getId());
    }

    public void verDocumento(Documentos documentoX) {
        documento = (Documentos) services.documentoGestionTribtaria(juicio.getClass().getPackage().getName() + "." + juicio.getClass().getSimpleName(), juicio.getId());
//        documento=documentoX;
        if (documento != null) {
            JsfUtil.executeJS("PF('dowloadDoc').show()");
        }
    }

    public void saveObservacion(HistoricoTramites ht) {
        try {
            Observaciones ob = new Observaciones();
            ob.setFecCre(new Date());
            ob.setIdTramite(ht);
            ob.setObservacion((observacion != null && !observacion.trim().isEmpty()) ? observacion : "INGRESO DE JUICIO");
            ob.setEstado(Boolean.TRUE);
            ob.setUserCre(session.getNameUser());
            ob.setTarea("Ingreso de Juicio Coactivo: " + juicio.getAnioJuicio());
            services.save(ob);
        } catch (Exception e) {
            Logger.getLogger(InicioCoactivaMB.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void updateJuicio() {
        try {
            boolean bandera = false;
            if (juicio.getId() != null) {
                juicio.setFechaEdicion(new Date());
                juicio.setUsuarioEdicion(session.getNameUser());
                if ((juicio.getEstadoJuicio().getId().equals(5L)) || (juicio.getEstadoJuicio().getId().equals(9L))) {
                    for (CoaJuicioPredio jp : juicio.getCoaJuicioPredioList()) {
                        if (jp.getLiquidacion().getEstadoLiquidacion().getId().equals(2L)) {
                            JsfUtil.addInformationMessage("Existen Emisiones Pendientes de Pagos. El Juicio no puede Pasar a 'EXTINCION TOTAL DE LA DEUDA", "");
                            bandera = true;
                            break;
                        }
                    }
                }
                if (juicio.getEstadoJuicio().getDescripcion().equals("CONVENIOS DE PAGO")) {
                    for (CoaJuicioPredio jp : juicio.getCoaJuicioPredioList()) {
                        liquidacionConvenio = convenioService.findLiquidacionConvenioIdLiquidacion(jp.getLiquidacion());
                        if (liquidacionConvenio != null) {
                            bandera = Boolean.TRUE;
                        } else {
                            bandera = Boolean.FALSE;
                            break;
                        }
                    }
                    if (bandera == false) {
                        JsfUtil.addInformationMessage("NO TIENE  UN CONVENIO DE PAGO", "");
                    } else {
                        convenioPago = convenioPagoService.findbyConvenioPago(liquidacionConvenio.getConvenio());
                        if (convenioPago != null) {
                            JsfUtil.addWarningMessage("TIENE UN CONVENIO DE PAGO APROBADO", "");
                        } else {
                            JsfUtil.addWarningMessage("TIENE UN CONVENIO DE PAGO, PERO NO ESTÁ APROBADO", "");
                        }
                    }
                }
            }
        } catch (Exception e) {
            Logger.getLogger(JuiciosCoactivosMB.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void seleccionarJuicioPredio(CoaJuicioPredio juicioPredio) {
        try {
            if (juicioPredio != null) {
                this.juicioPredio = juicioPredio;
                observacion = "AB. PREVIO: " + (juicioPredio.getAbogadoJuicio() != null ? juicioPredio.getAbogadoJuicio().getDetalle() : "SIN AB.");
            } else {
                observacion = "AB. PREVIO: ";
                if (this.juicio != null && this.juicio.getCoaJuicioPredioList() != null && !this.juicio.getCoaJuicioPredioList().isEmpty() && this.predioModel != null) {
                    for (CoaJuicioPredio jl : this.juicio.getCoaJuicioPredioList()) {
                        this.predioModel.setNumPredio(jl.getLiquidacion().getPredio().getNumPredio());
                        this.consultarPredioUrbano();
                        break;
                    }
                }
            }
        } catch (Exception e) {
            Logger.getLogger(JuiciosCoactivosMB.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void consultarPredioUrbano() {
        predioConsulta = null;
        prediosUrbanosConsultaSeleccionados = null;
        prediosUrbanosConsulta = new ArrayList<>();
        parametros = new HashMap<>();
        try {
            switch (predioModel.getTipoConsultaUrbano().intValue()) {
                case 1://NUMERO PREDIAL
                    if (predioModel.getNumPredio() != null && predioModel.getNumPredio().compareTo(BigInteger.ZERO) > 0) {
                        parametros.put("numPredio", predioModel.getNumPredio());
                        predioConsulta = (CatPredioModel) services.findByParameter(CatPredioModel.class, parametros);
                    }
                    break;
                case 3://CODIGO PREDIAL
                    if (predioModel.getClaveCat() != null) {
                        parametros.put("estado", "A");
                        if (predioModel.getClaveCat() != null || predioModel.getClaveCat().trim().length() > 0) {
                            parametros.put("claveCat", predioModel.getClaveCat().trim());
                        }
                        prediosUrbanosConsulta = services.findByParameter(CatPredioModel.class, parametros);
                    } else {
                        JsfUtil.addErrorMessage("Error", "Codigo Predial no es valido.");
                    }
                    break;
                case 4://UBICACION
                    if (predioModel.getCiudadela() != null || predioModel.getMzUrb() != null || predioModel.getSlUrb() != null) {
                        if (predioModel.getCiudadela() != null) {
                            parametros.put("ciudadela", predioModel.getCiudadela());
                        }
                        if (predioModel.getMzUrb() != null) {
                            parametros.put("urbMz", predioModel.getMzUrb());
                        }
                        if (predioModel.getSlUrb() != null) {
                            parametros.put("urbSolarnew", predioModel.getSlUrb());
                        }
                        prediosUrbanosConsulta = services.findByParameter(CatPredioModel.class, parametros);
                    } else {
                        JsfUtil.addErrorMessage("Error", "Datos no validos para la Consulta");
                    }
                    break;
                default:
                    break;
            }
            if (prediosUrbanosConsulta != null && !prediosUrbanosConsulta.isEmpty() && prediosUrbanosConsulta.size() == 1) {
                parametros = new HashMap<>();
                parametros.put("numPredio", prediosUrbanosConsulta.get(0).getNumPredio());
                predioConsulta = (CatPredioModel) services.findByParameter(CatPredioModel.class, parametros);
            }
            if (predioConsulta != null) {

            } else {
                if (prediosUrbanosConsulta != null && prediosUrbanosConsulta.size() > 1) {
                    JsfUtil.update("frmPredios");
                    JsfUtil.executeJS("PF('dlgPrediosConsulta').show();");
                } else {
                    JsfUtil.addInformationMessage("Mensaje", "Predio no encontrado.");
                }
            }
        } catch (Exception e) {
            Logger.getLogger(JuiciosCoactivosMB.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void actualizarAbogadoPredio() {
        if (this.juicioPredio.getId() != null) {
            services.save(this.juicioPredio);;
        } else {
            if (this.juicio != null && this.juicio.getCoaJuicioPredioList() != null && !this.juicio.getCoaJuicioPredioList().isEmpty()) {
                for (CoaJuicioPredio jp : juicio.getCoaJuicioPredioList()) {
                    observacion = observacion + jp.getAbogadoJuicio().getDetalle() + ", ";
                    jp.setAbogadoJuicio(this.juicioPredio.getAbogadoJuicio());
                    services.save(jp);
                }
            }
        }
        observacion = observacion + "- AB. ACTUAL:" + this.juicioPredio.getAbogadoJuicio().getDetalle();
        if (this.juicio != null && this.juicio.getTramite() != null) {
            obs = new Observaciones();
            obs.setIdTramite(this.juicio.getTramite());
            obs.setTarea("Actualizacion de Abogado");
            obs.setObservacion(observacion);
            obs.setUserCre(session.getNameUser());
            obs.setFecCre(new Date());
            obs.setEstado(Boolean.TRUE);
            services.save(obs);
        }
    }

    public void desactivarJuicioPredio() {
        try {
            if (juicio != null && juicioPredio != null) {
                //SE INACTIVA LA EMISION DEL JUICIO
                //LA EMISION VUELVE A UN ESTADO INCIAL COACTIVA: FALSE ESATDO_COACTIVA:1
                juicioPredio.setEstado(Boolean.FALSE);
                coaJuicioPredioServices.edit(juicioPredio);
                juicioPredio.getLiquidacion().setCoactiva(Boolean.FALSE);
                juicioPredio.getLiquidacion().setEstadoCoactiva(1);
                finaRenLiquidacionServices.edit(juicioPredio.getLiquidacion());
//                CatPredioModel prediBuscar=services.buscarPredio( juicioPredio.getLiquidacion().getPredio()); // web services copnsumir
                String observacionInactivacion = (juicioPredio.getJuicio().getObservacion() == null ? "" : juicioPredio.getJuicio().getObservacion() + "") + "INACTIVACION TITULO: " + juicioPredio.getLiquidacion().getCodigoLocal() + "-" + juicioPredio.getLiquidacion().getAnio() + ". VALOR PREVIO:" + juicioPredio.getJuicio().getTotalDeuda();
                juicioPredio.getJuicio().setTotalDeuda(juicioPredio.getJuicio().getTotalDeuda().subtract(juicioPredio.getLiquidacion().getTotalPago()));
                observacionInactivacion = observacionInactivacion + "; VALOR ACTUAL:" + juicioPredio.getJuicio().getTotalDeuda();
                juicioPredio.getJuicio().setObservacion(observacionInactivacion);
                coaJuicioService.edit(juicioPredio.getJuicio());
            }
        } catch (Exception e) {
            Logger.getLogger(JuiciosCoactivosMB.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void seleccionarPredio(Long tipoPredio) {
        try {
            parametros = new HashMap<>();
            switch (tipoPredio.intValue()) {
                case 1:
                    if (prediosUrbanosConsultaSeleccionados != null && !prediosUrbanosConsultaSeleccionados.isEmpty()) {
                        for (CatPredioModel pucs : prediosUrbanosConsultaSeleccionados) {
                            parametros.put("numPredio", pucs.getNumPredio());
                            predioConsulta = (CatPredioModel) services.findByParameter(CatPredioModel.class, parametros);
                        }
                        JsfUtil.executeJS("PF('dlgPrediosConsulta').hide();");
                    } else {
                        JsfUtil.addInformationMessage("Mensaje", "Seleccione un predio, luego clic en Seleccionar");
                    }
                    break;
                default:
                    break;
            }

        } catch (Exception e) {
            Logger.getLogger(JuiciosCoactivosMB.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void limpiarConsultaJuicios() {
        juiciosConsulta = new ArrayList<>();
        predioConsulta = null;
        predioModel = new CatPredioModel();
    }

    public void consultarTitulosEnJuicios() {
        try {
            if (predioConsulta != null) {
                String query = "SELECT DISTINCT c.juicio FROM CoaJuicioPredio c WHERE c.liquidacion IN (SELECT l FROM RenLiquidacion l WHERE l.tipoLiquidacion.id=13 AND l.predio=:?1) AND c.estado=TRUE";
                Object[] objeto = new Object[]{predioConsulta};
                List<CoaJuicio> jConsulta = services.findNativeQueryList(CoaJuicio.class, query, objeto);
                if (jConsulta != null && !jConsulta.isEmpty()) {
                    for (CoaJuicio jc : jConsulta) {
                        if (!juiciosConsulta.contains(jc)) {
                            juiciosConsulta.add(jc);
                        }
                    }
                } else {
                    JsfUtil.addInformationMessage("Mensaje", "No se encontraron Juicios para el predio Consultado" + predioConsulta.getNumPredio() + " Cod:" + predioConsulta.getClaveCat());
                }
            } else {
                JsfUtil.addInformationMessage("Mensaje", "Seleccione un predio, luego clic en Consultar Juicio");
            }
        } catch (Exception e) {
            Logger.getLogger(CoaJuicio.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void removerEmisionJuicioPredio() {
        if (juicio != null && juicioPredio != null) {
            //SE INACTIVA LA EMISION DEL JUICIO
            //LA EMISION VUELVE A UN ESTADO INCIAL COACTIVA: FALSE ESATDO_COACTIVA:1
            juicioPredio.setEstado(Boolean.FALSE);
            services.save(juicioPredio);
        }
    }

    public void consultarTituloPorPredio() {
        try {
            if (predioConsulta != null) {
                List<FinaRenLiquidacion> tc = coaJuicioService.getEmisionesCoactivaAntigua(predioConsulta);
                if (tc != null && !tc.isEmpty()) {
                    for (FinaRenLiquidacion l : tc) {
                        if (!titulosConsulta.contains(l)) {
                            titulosConsulta.add(l);
                        }
                    }
                } else {
                    JsfUtil.addInformationMessage("Mensaje", "No se encontraron Emisiones para el predio Consultado" + predioConsulta.getNumPredio() + " Cod:" + predioConsulta.getClaveCat());
                }
            } else {
                JsfUtil.addInformationMessage("Mensaje", "Seleccione un predio, luego clic en Consultar Titulos");
            }
        } catch (Exception e) {
            Logger.getLogger(JuiciosCoactivosMB.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void deleteLiquidacion(int indice) {
        titulosConsulta.remove(indice);
    }

    public void agregarEmisionesJuicio() {
        try {
            if (titulosConsulta != null && !titulosConsulta.isEmpty()) {
                if (juicio != null) {
                    CoaAbogado ab = juicio.getAbogadoJuicio();
                    BigDecimal valorAdicional = new BigDecimal("0.00");
                    for (FinaRenLiquidacion t : titulosConsulta) {
                        juicioPredio = new CoaJuicioPredio();
                        juicioPredio.setEstado(Boolean.TRUE);
                        juicioPredio.setJuicio(juicio);
                        juicioPredio.setLiquidacion(t);
                        juicioPredio.setPredio(t.getPredio());
                        juicioPredio.setAnioDeuda(t.getAnio());
                        juicioPredio.setValorDeuda(t.getTotalPago());//****
                        juicioPredio.setAbogadoJuicio(ab);///***
                        valorAdicional = valorAdicional.add(t.getTotalPago());
                        //CREAR JUICIOPREDIO
                        services.save(juicioPredio);
                        //ACTUALIZA LA EMISION
                        t.setCoactiva(Boolean.TRUE);
                        t.setEstadoCoactiva(2);
                        services.save(t);
                    }
                    this.juicio.setTotalDeuda(this.juicio.getTotalDeuda().add(valorAdicional));
                    services.save(juicio);
                    titulosConsulta = new ArrayList<>();
                }
            } else {
                JsfUtil.addInformationMessage("Mensaje", "La lista de Titulos no debe estar Vacia");
            }
        } catch (Exception e) {
            Logger.getLogger(JuiciosCoactivosMB.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void handleFileDocumentBySave(FileUploadEvent event) throws ClassNotFoundException {
        try {
            listaDocumentos = new ArrayList<>();
            String ruta = SisVars.RUTA_EVIDENCIA + Utils.getAnio(new Date()) + Utils.getHour(new Date())
                    + Utils.getMinute(new Date()) + Utils.getSecond(new Date()) + Utils.getMiliSecond(new Date()) + "_" + event.getFile().getFileName();
            documento = new Documentos();
            documento.setDepartamento(session.getDepartamento());
            documento.setRutaDocumento(ruta);
            documento.setFechaCreacion(new Date());
            documento.setNombre(event.getFile().getFileName());
            documento.setDescripcion(event.getFile().getContentType());
            documento.setEstado(Boolean.TRUE);
            documento.setClaseNombre(juicio.getClass().getPackage().getName() + "." + juicio.getClass().getSimpleName());
            documento.setIdentificador(juicio.getId());
            listaDocumentos.add(documento);
            Utils.copyFileServerDoc(ruta, event.getFile().getInputstream());
            for (Documentos doc : listaDocumentos) {
                services.save(doc);
            }
            JsfUtil.addInformationMessage("Nota1", "Archivo cargado Satisfactoriamente");
            guardarMedidasCautelares();
        } catch (IOException e) {
            JsfUtil.addWarningMessage("", "Ocurrió un error al momento de subri el documento");
        }
    }

    public void guardarMedidasCautelares() {
        CoaMedidasCautelares medidasCautelare;
        System.out.println("RESULTADO" + cautelaresServices.actualizarMedidasCautelares(juicio.getId()));
        for (CatalogoItem medidas : medidasCautelaresAplicadas) {
            medidasCautelare = cautelaresServices.findMedidaCauleraExistente(juicio.getId(), medidas.getId());
            if (medidasCautelare.getId() != null) {
                //si es verdadera edita
                medidasCautelare.setEstado(Boolean.TRUE);
                cautelaresServices.edit(medidasCautelare);
            } else {
                //si es falso crea
                medidasCautelare = new CoaMedidasCautelares();
                medidasCautelare.setMedidaCautelar(medidas);
                medidasCautelare.setEstado(Boolean.TRUE);
                medidasCautelare.setJuicios(juicio);
                cautelaresServices.create(medidasCautelare);
            }
        }
    }

    //<editor-fold defaultstate="collapsed" desc="get and set">
    public ServletSession getSs() {
        return ss;
    }

    public void setSs(ServletSession ss) {
        this.ss = ss;
    }

    public String getTipoPredio() {
        return tipoPredio;
    }

    public void setTipoPredio(String tipoPredio) {
        this.tipoPredio = tipoPredio;
    }

    public List<CoaJuicio> getHistorialJuicios() {
        return historialJuicios;
    }

    public void setHistorialJuicios(List<CoaJuicio> historialJuicios) {
        this.historialJuicios = historialJuicios;
    }

    public Date getFechaJuicioFiltro() {
        return fechaJuicioFiltro;
    }

    public void setFechaJuicioFiltro(Date fechaJuicioFiltro) {
        this.fechaJuicioFiltro = fechaJuicioFiltro;
    }

    public FinaRenLiquidacion getLiquidacionSeleccionada() {
        return liquidacionSeleccionada;
    }

    public void setLiquidacionSeleccionada(FinaRenLiquidacion liquidacionSeleccionada) {
        this.liquidacionSeleccionada = liquidacionSeleccionada;
    }

    public FinaRenTipoLiquidacion getTipoLiquidacion() {
        return tipoLiquidacion;
    }

    public void setTipoLiquidacion(FinaRenTipoLiquidacion tipoLiquidacion) {
        this.tipoLiquidacion = tipoLiquidacion;
    }

    public String getOpcionBusquedad() {
        return opcionBusquedad;
    }

    public void setOpcionBusquedad(String opcionBusquedad) {
        this.opcionBusquedad = opcionBusquedad;
    }

    public UserSession getSession() {
        return session;
    }

    public void setSession(UserSession session) {
        this.session = session;
    }

    public ManagerService getServices() {
        return services;
    }

    public void setServices(ManagerService services) {
        this.services = services;
    }

    public String getNoOficio() {
        return noOficio;
    }

    public void setNoOficio(String noOficio) {
        this.noOficio = noOficio;
    }

    public LazyModel<CoaJuicio> getJuicios() {
        return juicios;
    }

    public void setJuicios(LazyModel<CoaJuicio> juicios) {
        this.juicios = juicios;
    }

    public CoaJuicio getJuicio() {
        return juicio;
    }

    public void setJuicio(CoaJuicio juicio) {
        this.juicio = juicio;
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

    public List<FinaRenLiquidacion> getTitulos() {
        return titulos;
    }

    public void setTitulos(List<FinaRenLiquidacion> titulos) {
        this.titulos = titulos;
    }

    public List<FinaRenLiquidacion> getTitulosConsulta() {
        return titulosConsulta;
    }

    public void setTitulosConsulta(List<FinaRenLiquidacion> titulosConsulta) {
        this.titulosConsulta = titulosConsulta;
    }

    public List<HistoricTaskInstance> getTareas() {
        return tareas;
    }

    public void setTareas(List<HistoricTaskInstance> tareas) {
        this.tareas = tareas;
    }

    public CoaJuicioPredio getJuicioPredio() {
        return juicioPredio;
    }

    public void setJuicioPredio(CoaJuicioPredio juicioPredio) {
        this.juicioPredio = juicioPredio;
    }

    public Observaciones getObs() {
        return obs;
    }

    public void setObs(Observaciones obs) {
        this.obs = obs;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Long getTipoReporte() {
        return tipoReporte;
    }

    public void setTipoReporte(Long tipoReporte) {
        this.tipoReporte = tipoReporte;
    }

    public List<Usuarios> getFuncionarios() {
        return funcionarios;
    }

    public void setFuncionarios(List<Usuarios> funcionarios) {
        this.funcionarios = funcionarios;
    }

    public List<Long> getListAbogadosReporte() {
        return listAbogadosReporte;
    }

    public void setListAbogadosReporte(List<Long> listAbogadosReporte) {
        this.listAbogadosReporte = listAbogadosReporte;
    }

    public List<Long> getListEstadosReporte() {
        return listEstadosReporte;
    }

    public void setListEstadosReporte(List<Long> listEstadosReporte) {
        this.listEstadosReporte = listEstadosReporte;
    }

    public List<Long> getFuncionariosReporte() {
        return funcionariosReporte;
    }

    public void setFuncionariosReporte(List<Long> funcionariosReporte) {
        this.funcionariosReporte = funcionariosReporte;
    }

    public Date getFechaJuicioDesde() {
        return fechaJuicioDesde;
    }

    public void setFechaJuicioDesde(Date fechaJuicioDesde) {
        this.fechaJuicioDesde = fechaJuicioDesde;
    }

    public Date getFechaJuicioHasta() {
        return fechaJuicioHasta;
    }

    public void setFechaJuicioHasta(Date fechaJuicioHasta) {
        this.fechaJuicioHasta = fechaJuicioHasta;
    }

    public Date getFechaIngresoDesde() {
        return fechaIngresoDesde;
    }

    public void setFechaIngresoDesde(Date fechaIngresoDesde) {
        this.fechaIngresoDesde = fechaIngresoDesde;
    }

    public Date getFechaIngresoHasta() {
        return fechaIngresoHasta;
    }

    public void setFechaIngresoHasta(Date fechaIngresoHasta) {
        this.fechaIngresoHasta = fechaIngresoHasta;
    }

    public Integer getNumDesde() {
        return numDesde;
    }

    public void setNumDesde(Integer numDesde) {
        this.numDesde = numDesde;
    }

    public Integer getNumHasta() {
        return numHasta;
    }

    public void setNumHasta(Integer numHasta) {
        this.numHasta = numHasta;
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

    public Boolean getPorNumero() {
        return porNumero;
    }

    public void setPorNumero(Boolean porNumero) {
        this.porNumero = porNumero;
    }

    public Boolean getPorAnio() {
        return porAnio;
    }

    public void setPorAnio(Boolean porAnio) {
        this.porAnio = porAnio;
    }

    public Boolean getPorFechaJuicio() {
        return porFechaJuicio;
    }

    public void setPorFechaJuicio(Boolean porFechaJuicio) {
        this.porFechaJuicio = porFechaJuicio;
    }

    public Boolean getPorFechaIngreso() {
        return porFechaIngreso;
    }

    public void setPorFechaIngreso(Boolean porFechaIngreso) {
        this.porFechaIngreso = porFechaIngreso;
    }

    public Observaciones getObservacionInactivo() {
        return observacionInactivo;
    }

    public void setObservacionInactivo(Observaciones observacionInactivo) {
        this.observacionInactivo = observacionInactivo;
    }

    public CatPredioModel getPredioConsulta() {
        return predioConsulta;
    }

    public void setPredioConsulta(CatPredioModel predioConsulta) {
        this.predioConsulta = predioConsulta;
    }

    public List<CoaJuicio> getJuiciosConsulta() {
        return juiciosConsulta;
    }

    public void setJuiciosConsulta(List<CoaJuicio> juiciosConsulta) {
        this.juiciosConsulta = juiciosConsulta;
    }

    public CatPredioModel getPredioModel() {
        return predioModel;
    }

    public void setPredioModel(CatPredioModel predioModel) {
        this.predioModel = predioModel;
    }

    public Map<String, Object> getParametros() {
        return parametros;
    }

    public void setParametros(Map<String, Object> parametros) {
        this.parametros = parametros;
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

    public Boolean getInactivar() {
        return inactivar;
    }

    public void setInactivar(Boolean inactivar) {
        this.inactivar = inactivar;
    }

    public Boolean getEsPdf() {
        return esPdf;
    }

    public void setEsPdf(Boolean esPdf) {
        this.esPdf = esPdf;
    }

    public Boolean getEsExcel() {
        return esExcel;
    }

    public void setEsExcel(Boolean esExcel) {
        this.esExcel = esExcel;
    }

    public Boolean getPorEstado() {
        return porEstado;
    }

    public void setPorEstado(Boolean porEstado) {
        this.porEstado = porEstado;
    }

    public Boolean getPorTipoReporte() {
        return porTipoReporte;
    }

    public void setPorTipoReporte(Boolean porTipoReporte) {
        this.porTipoReporte = porTipoReporte;
    }

    public List<FinaRenTipoLiquidacion> getFinaRenTiposLiquidaciones() {
        return finaRenTiposLiquidaciones;
    }

    public void setFinaRenTiposLiquidaciones(List<FinaRenTipoLiquidacion> finaRenTiposLiquidaciones) {
        this.finaRenTiposLiquidaciones = finaRenTiposLiquidaciones;
    }

    public String getCriterioBusquedad() {
        return criterioBusquedad;
    }

    public void setCriterioBusquedad(String criterioBusquedad) {
        this.criterioBusquedad = criterioBusquedad;
    }

    public CoaJuicioPredio getCoaJuicioPredio() {
        return coaJuicioPredio;
    }

    public void setCoaJuicioPredio(CoaJuicioPredio coaJuicioPredio) {
        this.coaJuicioPredio = coaJuicioPredio;
    }

    public List<FinaRenLiquidacion> getLiquidacionesPagos() {
        return liquidacionesPagos;
    }

    public void setLiquidacionesPagos(List<FinaRenLiquidacion> liquidacionesPagos) {
        this.liquidacionesPagos = liquidacionesPagos;
    }

    public CatPredio getPredio() {
        return predio;
    }

    public void setPredio(CatPredio predio) {
        this.predio = predio;
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

    public Date getFechaDesde() {
        return fechaDesde;
    }

    public void setFechaDesde(Date fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    public Date getFechaHasta() {
        return fechaHasta;
    }

    public void setFechaHasta(Date fechaHasta) {
        this.fechaHasta = fechaHasta;
    }

    public CoaAbogado getAbogado() {
        return abogado;
    }

    public void setAbogado(CoaAbogado abogado) {
        this.abogado = abogado;
    }

    public List<CatalogoItem> getMedidasCautelaresAplicadas() {
        return medidasCautelaresAplicadas;
    }

    public void setMedidasCautelaresAplicadas(List<CatalogoItem> medidasCautelaresAplicadas) {
        this.medidasCautelaresAplicadas = medidasCautelaresAplicadas;
    }

    public List<CatalogoItem> getMedidasCautelares() {
        return medidasCautelares;
    }

    public void setMedidasCautelares(List<CatalogoItem> medidasCautelares) {
        this.medidasCautelares = medidasCautelares;
    }

    public List<Documentos> getListaDocumentos() {
        return listaDocumentos;
    }

    public void setListaDocumentos(List<Documentos> listaDocumentos) {
        this.listaDocumentos = listaDocumentos;
    }

    public Documentos getDocumento() {
        return documento;
    }

    public void setDocumento(Documentos documento) {
        this.documento = documento;
    }

    public CoaEstadoJuicio getEstadoEscogido() {
        return estadoEscogido;
    }

    public void setEstadoEscogido(CoaEstadoJuicio estadoEscogido) {
        this.estadoEscogido = estadoEscogido;
    }

    public List<FinaRenPago> getPagos() {
        return pagos;
    }

    public void setPagos(List<FinaRenPago> pagos) {
        this.pagos = pagos;
    }

    public FinaRenPago getFinaRenPago() {
        return finaRenPago;
    }

    public void setFinaRenPago(FinaRenPago finaRenPago) {
        this.finaRenPago = finaRenPago;
    }

    public FinaRenPagoDetalle getFinaRenPagoDetalle() {
        return finaRenPagoDetalle;
    }

    public void setFinaRenPagoDetalle(FinaRenPagoDetalle finaRenPagoDetalle) {
        this.finaRenPagoDetalle = finaRenPagoDetalle;
    }
//</editor-fold>

}
