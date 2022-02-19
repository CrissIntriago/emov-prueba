/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Controller;

import com.asgard.Entity.*;
import com.gestionTributaria.Entities.*;
import com.gestionTributaria.Services.ManagerService;
import com.gestionTributaria.Services.RemisionInteresService;
import com.gestionTributaria.models.BusquedaPredios;
import com.gestionTributaria.models.CatPredioModel;
import com.gestionTributaria.models.CatPredioRusticoDTO;
import com.gestionTributaria.models.NombreContribuyenteModel;
import com.gestionTributaria.models.ValoracionPredialDTO;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.entities.Usuarios;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.tesoreria.comprobantelectronico.entities.Cajero;
import com.origami.sigef.tesoreria.comprobantelectronico.service.CajeroService;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.event.UnselectEvent;

/**
 *
 * @author Administrator
 */
@Named
@ViewScoped
public class PagoPrediales extends BusquedaPredios implements Serializable {

    @Inject
    private ManagerService manager;

    @Inject
    private ServletSession ss;

    @Inject
    private UserSession session;
    
    @Inject 
    private RemisionInteresService remisionInteresService;
    @Inject
    private CajeroService cajeroService;

    private Long anioEmision;
    private CtlgSalario salario;
    private List<FinaRenDetLiquidacion> rubrosEmision = new ArrayList<>();
    private FinaRenDetLiquidacion rubro;
    private FinaRenTipoLiquidacion tipoLiquidacion;

    private FinaRenLiquidacion emisionSeleccionada;
    private Cajero usuario;
    private List<FinaRenLiquidacion> emisionesACobrar;
    private List<FinaRenEntidadBancaria> bancos;
    private List<FinaRenEntidadBancaria> tarjetas;
    private FinaRenPago pagoCoactiva;
    private List<CatParroquia> parroquiasRurales;
    private CatPredioRusticoDTO predioRustico;

    private List<String> ciudadelas;

    private String numCertificado;
    private Long tipoCertificado;
    private String nombreComprador;
    private String nombreContribuyente;
    private List<NombreContribuyenteModel> modelNombresList;
    private Boolean liquidador = false;
    private CatPredioPropietario propietario;

    private Map<String, Object> paramt;

    private List<FinaRenLiquidacion> emisionesPredialesTemp;

    private Boolean renderContextMenu = Boolean.FALSE;

    private ValoracionPredialDTO valoracion;
    private Boolean normal;

    //public PagoTituloReporteModel modelPago ;
    /*PARA LAS NOTAS DE CREDITO*/
    private List<NotasCredito> listPagoNota;
    private NotasCredito nota;

    private FnConvenioPago convenioPago;
    private List<CatPredioRusticoDTO> prediosRusticoConsulta;
    private Boolean esPagoCuota, esPagoCuotaCoactiva;

    @PostConstruct
    public void initView() {
        try {
            if (session != null) {
                setSumaTotalConv(new BigDecimal("0.00"));
                //solicitaactualizarContribuyententes = new CatEnteLazy();
                setPropietarios(new LazyModel<>(Cliente.class));
                setPropietariosRustico(new LazyModel<>(CatPredioRusticoDTO.class));
                paramt = new HashMap<>();
                paramt.put("estado", Boolean.TRUE);
                paramt.put("tipo", new RenTipoEntidadBancaria(1L));
                bancos = manager.findObjectByParameterList(FinaRenEntidadBancaria.class, paramt);
                paramt.put("tipo", new RenTipoEntidadBancaria(2L));
                tarjetas = manager.findObjectByParameterList(FinaRenEntidadBancaria.class, paramt);
                //   parroquiasRurales = manager.findAllEntCopy(Querys.parroquiasRurales);
                anioEmision = new Long(Calendar.getInstance().get(Calendar.YEAR));
                paramt = new HashMap<>();
                paramt.put("usuario", session.getNameUser());
                usuario = cajeroService.findByCajero(session.getNameUser());
                ciudadelas = new ArrayList<>();
                habilitarRecalculo();
//                this.modelPago = new PagoTituloReporteModel();
//                this.modelPago.setNota(new NotasCredito());
                esPagoCuota = Boolean.FALSE;
                esPagoCuotaCoactiva = Boolean.FALSE;
//                tieneConvenio = Boolean.FALSE;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setNombreContribuyente() {
        String[] nombres;
        Integer cont = 0;
        NombreContribuyenteModel model = new NombreContribuyenteModel();
        modelNombresList = new ArrayList();

        if (emisionSeleccionada.getComprador() != null) {
            nombreContribuyente = emisionSeleccionada.getComprador().getNombreCompleto();
        } else {
            nombreContribuyente = emisionSeleccionada.getNombreComprador();
        }

        if (emisionSeleccionada.getNombreCompradorHistoric() != null) {
            nombres = emisionSeleccionada.getNombreCompradorHistoric().split(";");

            for (String temp : nombres) {
                if (cont % 3 == 0) {
                    model = new NombreContribuyenteModel();
                    model.setUsername(temp);
                }
                if (cont % 3 == 1) {
                    model.setFecha(temp);
                }
                if (cont % 3 == 2) {
                    model.setNombre(temp);
                    modelNombresList.add(model);
                }
                cont++;
            }
        }
    }

    public void actualizarContribuyente() {

        try {

            String nom = emisionSeleccionada.getComprador() == null ? emisionSeleccionada.getNombreComprador() == null ? "" : emisionSeleccionada.getNombreComprador().toUpperCase() : emisionSeleccionada.getComprador().getNombreCompleto().toUpperCase();
            if (nombreContribuyente != null) {

                emisionSeleccionada.setComprador(null);
                emisionSeleccionada.setNombreComprador(nombreContribuyente.toUpperCase());
                if (emisionSeleccionada.getNombreCompradorHistoric() == null) {
                    emisionSeleccionada.setNombreCompradorHistoric(session.getNameUser() + ";" + new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").format(new Date()) + ";" + nom + ";");
                } else {
                    emisionSeleccionada.setNombreCompradorHistoric(emisionSeleccionada.getNombreCompradorHistoric() + session.getNameUser() + ";" + new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").format(new Date()) + ";" + nombreContribuyente.toUpperCase() + ";");
                }
//                emisionSeleccionada = (FinaRenLiquidacion) manager.save(emisionSeleccionada);
                manager.update(emisionSeleccionada);
                for (FinaRenPago p : emisionSeleccionada.getRenPagoCollection()) {
                    p.setContribuyente(null);
                    p.setNombreContribuyente(nombreContribuyente.toUpperCase());
                }
                setControlDocumento(Boolean.FALSE);
            } else {
                JsfUtil.addInformationMessage("Info", "No ha ingresado el nombre del contribuyente o Subido el Documento");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void seleccionarPredio(Long tipoPredio) {
        try {
            emisionesPredialesTemp = null;
            switch (tipoPredio.intValue()) {
                case 1: // PREDIO URBANO INDIVIDUAL 
                    if (getPredioConsulta() != null) {
                        paramt = new HashMap<>();
                        paramt.put("tipoLiquidacion", new FinaRenTipoLiquidacion(13L));
                        paramt.put("predio", getPredioConsulta());
                        paramt.put("estadoLiquidacion", new FinaRenEstadoLiquidacion(2L));
                        /*emisionesPredialesTemp = manager.findObjectByParameterOrderList(RenLiquidacion.class, paramt, new String[]{"anio"}, Boolean.TRUE);
                        setEmisionesPrediales(emisionesPredialesTemp);*/
                        setEmisionesPrediales(manager.obtenerLiquidacionesPrediales(new FinaRenTipoLiquidacion(13L), getPredioConsulta().getNumPredio().longValue()));
                        calculoTotalPago(getEmisionesPrediales(), null);
                        JsfUtil.executeJS("PF('dlgPrediosConsulta').hide();");
                    } else {
                        JsfUtil.addInformationMessage("Mensaje", "Seleccione un predio, luego clic en Seleccionar");
                    }
                    break;
                case 2: // PREDIO RURAL INDIVIDUAL
                    if (getPredioRuralConsulta() != null) {
                        paramt = new HashMap<>();
                        paramt.put("tipoLiquidacion", new FinaRenTipoLiquidacion(7L));
                        paramt.put("predioRustico", getPredioRuralConsulta());
                        paramt.put("estadoLiquidacion", new FinaRenEstadoLiquidacion(2L));
                        emisionesPredialesTemp = manager.findAllQuery("Select l from FinaRenLiquidacion l order by l.id asc", null);
                        setEmisionesPrediales(emisionesPredialesTemp);
                        calculoTotalPago(getEmisionesPrediales(), null);
                        JsfUtil.executeJS("PF('dlgPrediosRuralConsulta').hide();");
                    } else {
                        JsfUtil.addInformationMessage("Mensaje", "Seleccione un predio, luego clic en Seleccionar");
                    }
                    break;
                case 3: // TODAS LA EMISIONES URBANAS QUE TENGA EL CONTRIBUYENTE 
                    List<FinaRenLiquidacion> tempEmisiones = new ArrayList<>();
                    for (CatPredio catPredio : this.getPrediosConsulta()) {
                        paramt = new HashMap<>();
                        paramt.put("tipoLiquidacion", new FinaRenTipoLiquidacion(13L));
                        paramt.put("predio", catPredio);
                        paramt.put("estadoLiquidacion", new FinaRenEstadoLiquidacion(2L));
                        emisionesPredialesTemp = manager.findAllQuery("Select l from FinaRenLiquidacion l order by l.id asc", null);
                        if (Utils.isNotEmpty(emisionesPredialesTemp)) {
                            tempEmisiones.addAll(emisionesPredialesTemp);
                        }
                    }
                    setEmisionesPrediales(tempEmisiones);
                    calculoTotalPago(getEmisionesPrediales(), null);
                    JsfUtil.executeJS("PF('dlgPrediosConsulta').hide();");
                    break;
                default:
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openDialogCertificado() {
        JsfUtil.executeJS("PF('dlgCertificado').show();");
        JsfUtil.update("formCertificado");
    }

    public void aplicarExoneracion() {
        List<FnExoneracionLiquidacion> exoneraciones = null;
        List parametros;
        FinaRenLiquidacion liqPost;
        BigDecimal posterior = BigDecimal.ZERO, original = BigDecimal.ZERO;
        try {
            //CONULTA SI EL PREDIO YA FUE EXONERADO (ESTADO LIQ: 4) 

            if (manager.find("SELECT r FROM FinaRenLiquidacion r WHERE r.predio = :predio AND r.anio = :anio AND r.tipoLiquidacion = :idTipoLiquidacion AND r.estadoLiquidacion = 4",
                    new String[]{"predio", "anio", "idTipoLiquidacion"},
                    new Object[]{getExoneracion().getPredio(), Utils.getAnio(new Date()), manager.find(FinaRenTipoLiquidacion.class, 13L)}) == null) {
                //A PARTIR DEL 2014 SE APLICAN LAS EXONERACIONES O UN MAXIMO DE 3 ANIOS???
                getExoneracion().setAnioInicio(getExoneracion().getAnioInicio() < 2014 ? 2014 : getExoneracion().getAnioInicio());
                getExoneracion().setAnioFin(Utils.getAnio(new Date())); //SE ACTUALIZA LA SOLICITUD ORIGINAL CON EL ANIO ACTUAL
                //METODO QUE REALIZA LA EXONERACION (EJB)
                exoneraciones = manager.aplicarExoneracion(null, this.getExoneracion(), session.getNameUser());
            }
            //SI NO INGRESA EN LA CONDICION ANTERIOR TAMPOCO INGRESA A ESTA --HENRY
            if (exoneraciones != null && !exoneraciones.isEmpty()) {
                FnExoneracionLiquidacion exo = exoneraciones.get(exoneraciones.size() - 1);
                parametros = new ArrayList<>();

                for (FnExoneracionLiquidacion t : exoneraciones) {
                    original = original.add(t.getLiquidacionOriginal().getTotalPago());
                    posterior = posterior.add(t.getLiquidacionPosterior().getTotalPago());
                    t.getLiquidacionPosterior().setComprador(t.getLiquidacionOriginal().getComprador());
                    t.getLiquidacionPosterior().setNombreComprador(t.getLiquidacionOriginal().getNombreComprador());
                    t.getLiquidacionPosterior().setObservacion(getMensajeExoneracion());
                    t.getLiquidacionPosterior().setTotalPago(t.getLiquidacionPosterior().getTotalPago().setScale(2, RoundingMode.HALF_UP));
                    t.getLiquidacionPosterior().setCoactiva(false);
                    t.getLiquidacionPosterior().setUsuarioIngreso(session.getNameUser());
                    this.emisionSeleccionada = t.getLiquidacionPosterior();
                    manager.save(t.getLiquidacionPosterior());
                }

                ss.instanciarParametros();

                //  ss.addParametro("LOGO", JsfUtil.getRealPath(SisVars.logoReportes));
                //    ss.addParametro("SUBREPORT_DIR", JsfUtil.getRealPath("/reportes/manageres/").concat("/"));
                ss.addParametro("FECHA", exo.getFechaIngreso());
                ss.addParametro("IMP_ORIG", original.setScale(2, BigDecimal.ROUND_HALF_UP));
                ss.addParametro("DIFERENCIA", original.subtract(posterior).setScale(2, BigDecimal.ROUND_HALF_UP));
                ss.addParametro("IMP_NEW", posterior.setScale(2, BigDecimal.ROUND_HALF_UP));
                ss.addParametro("ID_SOLICITUD", exo.getId());
                //    ss.setTieneDatasource(Boolean.TRUE);
                ss.setNombreSubCarpeta("manageres");
                ss.setNombreReporte("formulario_exoneracion_master");
            } else {
                JsfUtil.addInformationMessage("Info", "Ya tiene aplicada una exoneración");
                return;
            }
            setExoneracion(null);
            JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onChangeTab(TabChangeEvent event) {
        setTabName(event.getTab().getId().toString());
        emisionesPredialesTemp = new ArrayList<>();
        if (event.getTab().getId().equals("tabPagoPredial") || event.getTab().getId().equals("pagoPredialRural")) {
            setEmisionesPrediales(null);
            setPredioModel(new CatPredioModel());
        } else {
            setPredioModel(new CatPredioModel());
        }
    }

    @Override
    public void onChangeRadio() {
        setEmisionesPrediales(null);
        setPredioModel(new CatPredioModel());
        setPredioConsulta(null);
        setTotalEmisiones(new BigDecimal("0.00"));
    }

    /**
     * Tipo 1: Urbano, Tipo 2: Rural
     *
     * @param tipo
     */
    public void seleccionarEmision(Long tipo) {
        if (emisionesPredialesTemp == null || emisionesPredialesTemp.isEmpty()) {
            calculoTotalPago(getEmisionesPrediales(), null);
        } else {

            emisionesACobrar = new ArrayList<>();
            emisionesACobrar.clear();

            paramt = new HashMap<>();
            if (tipo == 1L) {
                for (FinaRenLiquidacion liq : emisionesPredialesTemp) {
                    paramt.put("anio", liq.getAnio());
                    for (FinaRenLiquidacion l : manager.getEmisionesByPredio(liq.getPredio().getId(), paramt)) {
                        if (l != null) {
                            emisionesACobrar.add(l);
                        }
                    }
                }
            }
            if (tipo == 2L) {
                for (FinaRenLiquidacion liq : emisionesPredialesTemp) {
                    paramt.put("anio", liq.getAnio());
                    for (FinaRenLiquidacion l : manager.getEmisionesByPredioRustico(liq.getPredioRustico(), paramt)) {
                        if (l != null) {
                            emisionesACobrar.add(l);
                        }
                    }
                }
            }

            if (emisionesPredialesTemp.size() == 1) {
                this.setEmisionSeleccionada(new FinaRenLiquidacion());
                this.setEmisionSeleccionada(emisionesPredialesTemp.get(0));
                //System.out.println("EMISION SELECCIONADA  " + emisionSeleccionada);
                renderContextMenu = true;
            }
            if (emisionesPredialesTemp.size() != 1) {
                this.setEmisionSeleccionada(null);
                renderContextMenu = false;
            }

            //elimina valores repetidos
            Set<FinaRenLiquidacion> liquidacionCobro = new HashSet<>();
            liquidacionCobro.addAll(emisionesACobrar);
            emisionesACobrar.clear();
            emisionesACobrar.addAll(liquidacionCobro);
            calculoTotalPago(emisionesACobrar, null);
        //    this.getModelPago().setValorCobrar(totalEmisiones);
            JsfUtil.update("mainForm");

        }
    }

    public void procesarPagosConv() {
//        try {
//            if (cuotasPredios.isEmpty() || cpd == null) {
//                JsfUtil.addWarningMessage("Advertencia", "No ha seleccionado deudas para procesar.");
//            } else {
//                procesarPagosCuotasCoactiva();
//            }
//        } catch (Exception e) {
//            JsfUtil.addErrorMessage("Error", "Error al procesar pago.");
//        }
    }

    public void procesarPagosConvCoactiva() {
//        if (cuotasPredios.isEmpty() || liquidacionCoactivaCuota == null) {
//            JsfUtil.addWarningMessage("Advertencia", "No ha seleccionado deudas para procesar.");
//        } else {
//            procesarPagosCuotasCoactiva();
//        }
    }

    public void procesarPagosCuotasCoactiva() {
//        emisionesACobrar = new ArrayList<>();
//        for (FinaRenLiquidacion rl : cuotasPredios) {
//            //VERIFIA SI EL PAGO QUESE HARA ES POR CUOTAS DE COACTIVA
//            if (rl.getTipoLiquidacion().getId().equals(281L)) {
//                esPagoCuotaCoactiva = Boolean.TRUE;
//            }
//            emisionesACobrar.add(rl);
//        }
//        if (cuotasPredios.size() == 1) {
//            this.setEmisionSeleccionada(new FinaRenLiquidacion());
//            this.setEmisionSeleccionada(cuotasPredios.get(0));
//            renderContextMenu = true;
//        }
//        if (cuotasPredios.size() != 1) {
//            this.setEmisionSeleccionada(null);
//            renderContextMenu = false;
//        }
//        Set<FinaRenLiquidacion> liquidacionCobro = new HashSet<>();
//        liquidacionCobro.addAll(emisionesACobrar);
//        emisionesACobrar.clear();
//        emisionesACobrar.addAll(liquidacionCobro);
//        if (esPagoCuotaCoactiva) {
//            totalEmisiones = sumaTotalCoactivaConv;
//            this.getModelPago().setValorCobrar(sumaTotalCoactivaConv);
//        } else {
//            esPagoCuota = Boolean.TRUE;
//            totalEmisiones = sumaTotalConv;
//            this.getModelPago().setValorCobrar(sumaTotalConv);
//        }
//
//        if (emisionesACobrar != null && !emisionesACobrar.isEmpty()) {
//            JsfUtil.update("formProcesar");
//            JsfUtil.executeJS("PF('dlgProcesar').show();");
//        } else {
//            JsfUtil.addInformationMessage("Mensaje", "No posee deuda.");
//
//        }

    }

    public void onRowSelect(SelectEvent event) {
        calculoTotalPago(emisionesPredialesTemp, null);
    }

    public void onRowUnselect(UnselectEvent event) {
        calculoTotalPago(emisionesPredialesTemp, null);
    }

    public void onRowToggle() {
        calculoTotalPago(emisionesPredialesTemp, null);
    }

    public void cleanValues() {
        emisionesPredialesTemp = new ArrayList();
        consultarEmisionesPendientesPago();
    }

    public void procesarPago() {
        try {
            if (getEmisionesPrediales() != null) {
                if (emisionesPredialesTemp != null) {
                    for (FinaRenLiquidacion renLiquidacion : emisionesACobrar) {
                        if (renLiquidacion.getTemporal()) {
                            JsfUtil.addInformationMessage("Info", "Realizar actualizacion de Datos previo a realizar el pago");
                            return;
                        }
                    }

                    if (!emisionesPredialesTemp.isEmpty()) {
                        for (FinaRenLiquidacion l : emisionesPredialesTemp) {
                            if (l.getAnio() > Utils.getAnio(new Date())) {
                                JsfUtil.addErrorMessage("Error", "Solo se puede realizar pago de emisión hasta el año actual.");
                                return;
                            }
                        }
                        emisionesACobrar = new ArrayList<>();
                        paramt = new HashMap<>();
                        for (FinaRenLiquidacion liq : emisionesPredialesTemp) {
                            paramt.put("anio", liq.getAnio());
                            if (liq.getTipoLiquidacion().getId() == 13L) {
                                for (FinaRenLiquidacion l : manager.getEmisionesByPredio(liq.getPredio().getId(), paramt)) {
                                    if (l != null) {
                                        emisionesACobrar.add(l);
                                        /*TRAIGO LAS NOTAS DE CREDITO*/
                                        listPagoNota = manager.findAll(NotasCredito.class, null);
                                        System.out.println("Cantidad A : " + listPagoNota.size());
                                    }
                                }
                            }
                            if (liq.getTipoLiquidacion().getId() == 7L) {
                                for (FinaRenLiquidacion l : manager.getEmisionesByPredioRustico(liq.getPredioRustico(), paramt)) {
                                    if (l != null) {
                                        emisionesACobrar.add(l);
                                    }
                                }
                            }
                        }
                        if (emisionesACobrar != null && !emisionesACobrar.isEmpty()) {

                            calculoTotalPago(emisionesACobrar, null);
                            JsfUtil.update("formProcesar");
                            JsfUtil.executeJS("PF('dlgProcesar').show();");
                        } else {
                            JsfUtil.addInformationMessage("Mensaje", "No posee deuda.");

                        }
                    }
                } else {
                    JsfUtil.addInformationMessage("Mensaje", "Debe seleccionar la emision a cancelar.");
                }
            } else {
                JsfUtil.addInformationMessage("Mensaje", "Debe realizar la Busqueda.");
            }
        } catch (Exception e) {
            Logger.getLogger(PagoPrediales.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void realizarPago() {
        List<FinaRenPago> pagos = new ArrayList<>();
        FinaRenPago pago;
        pagoCoactiva = null;
        try {
            if (emisionesACobrar.size() > 1) {
                //VERIFICAR QUE SOLO SE REALIZE UN TIPO DE PAGO DE LOS 5 QUE EXISTEN                
                if (getModelPago().cantidadTipoPagos() == 1) {//VERIFICAR QUE SE REALIZE TOTOAL PAGO DE LaS EMISIONES SELECCIONADAS
                    if (getModelPago().getValorTotal().compareTo(getTotalEmisiones()) == 0) {
                        FinaRenLiquidacion temp = manager.realizarPagosCoactiva(emisionesACobrar, session.getNameUser());

                        if (temp != null) {
                            temp.setPagoFinal(temp.getTotalPago());
                            pagoCoactiva = manager.realizarPago(temp, getModelPago().realizarPago(temp), usuario, true);
                        }
                        for (FinaRenLiquidacion e : emisionesACobrar) {
                            e.calcularPago();
                            pago = manager.realizarPago(e, getModelPago().realizarPago(e), usuario, true);

                            if (pago != null) {
                                pagos.add(pago);
                            }
                        }
                        if (pagoCoactiva != null) {
                            pagos.add(pagoCoactiva);
                        }
                    } else {
                        JsfUtil.addInformationMessage("Verifique el valor a cobrar", "El cobro de Emisiones multiples debe ser cancelado Totalmente");
                    }
                } else {
                    JsfUtil.addInformationMessage("Verifique Tipo de Pagos", "El cobro de Emisiones multiples solo permite un Tipo de Pago");
                }
            } else {
                if (getModelPago().getValorTotal().compareTo(getTotalEmisiones()) <= 0) {
                    if (getModelPago().getValorTotal().compareTo(BigDecimal.ZERO) > 0) {
                        for (FinaRenLiquidacion l : emisionesACobrar) {
                            //l = manager.realizarDescuentoRecargaInteresPredial(l,getModelPago().getPagoTransferencia().getFecha());
                            l.calcularPago();
                            //l=manager.realizarPago(l, getModelPago().realizarPago(l),usuario);
                            if (l.calculoMinimoPago(getModelPago().getValorTotal())) {
                                break;
                            }
                            if (l.getEstadoCoactiva() != null && l.getEstadoCoactiva() == 2) {
                                //modelPago.calculoValoresPago();
                                //RenLiquidacion temp = manager.realizarUnPagoCoactiva(l, modelPago.getValorCoactiva(), session.getNameUser());
                                FinaRenLiquidacion temp = manager.realizarUnPagoCoactiva(l, manager.valorRecaudarCoactiva(getModelPago().getValorTotal()), session.getNameUser());
                                if (temp != null) {
                                    temp.setPagoFinal(temp.getTotalPago());
                                    /*pagoCoactiva = manager.realizarPago(temp, getModelPago().realizarPago(temp), usuario);
                                     if (pagoCoactiva != null) {
                                     this.comprobanteCoactiva();
                                     }*/
                                    pagoCoactiva = manager.realizarPago(temp, getModelPago().realizarPago(temp), usuario, true);
                                }
                            }
                            pago = manager.realizarPago(l, getModelPago().realizarPago(l), usuario, true);
                            if (pago != null) {
                                pagos.add(pago);
                            }
                            if (pagoCoactiva != null) {
                                pagos.add(pagoCoactiva);
                            }
                        }
                        if (pagos.isEmpty()) {
                            JsfUtil.addInformationMessage("Verifique el valor a cobrar", "Los valores ingresados debe ser mayor al Recargo+Interes");
                        }
                    } else {
                        JsfUtil.addInformationMessage("Verifique el valor a cobrar", "Los valores ingresados debe ser mayor a 0.00");
                    }
                } else {
                    JsfUtil.addInformationMessage("Verifique el valor a cobrar", "Los valores ingresados no deben ser mayor al de la Recaudación");
                }
            }
            if (!pagos.isEmpty()) {
                setPagoRealizado(Boolean.TRUE);

                /*LO DE LAS NOTAS DE CREDITO*/
//                for (NotasCredito nc : modelPago.getListPagoNota()) {
//                    NotasCreditoHistorico ht = new NotasCreditoHistorico();
//                    ht.setNotaCredito(nc);
//                    ht.setLiquidacion(emisionSeleccionada);
//                    ht.setEstado((short) 0);
//                    ht.setFechaIngreso(new Date());
//                    ht.setUsuarioIngreso(session.getNameUser());
//
//                    manager.save(nc);
//                    manager.save(ht);
//                }
                generarComprobante(pagos);
            }
        } catch (Exception e) {
            Logger.getLogger(PagoPrediales.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    /*METODO QUE FUE MODIFICADO LOGO DEL REPORTE*/
    public void generarComprobante(List<FinaRenPago> pagos) {
        try {
//            String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");
//            ss.borrarParametros();
//            ss.instanciarParametros();
//            ss.addParametro("LOGO", path + SisVars.sisLogo);
//            ss.addParametro("SUBREPORT_DIR", JsfUtil.getRealPath("/reportes/"));
//        //    ss.setTieneDatasource(Boolean.TRUE);
//            /*PARA LOS ABONOS  EN EL COMBRE DE LOS PREDIOS*/
//            if (pagos.get(0).getLiquidacion().getTipoLiquidacion().getId().equals(13L)) {
//                ss.setNombreReporte("sEmisionPredioUrbanoGeneral");
//            } else {
//                if (pagos.get(0).getLiquidacion().getTipoLiquidacion().getNombreReporte() == null
//                        || pagos.get(0).getLiquidacion().getTipoLiquidacion().getNombreReporte().trim().length() == 0) {
//                    ss.setNombreReporte(pagos.get(0).getLiquidacion().getTipoLiquidacion().getNombreReporte());
//                } else {
//                    ss.setNombreReporte(pagos.get(0).getLiquidacion().getTipoLiquidacion().getNombreReporte());
//                }
//            }
//            ss.addParametro("liquidaciones", pagos);
//            JsfUtil.redirectNewTab(com.origami.config.SisVars.urlbase + "Comprobantes");
            setPagoRealizado(Boolean.FALSE);
        } catch (Exception e) {
            Logger.getLogger(PagoPrediales.class.getName()).log(Level.OFF, null, e);
        }
    }

    public void generarCertificado() {
//        GeDepartamento tesoreria;
        try {
//            String propietarioText = "";
//            ss.instanciarParametros();
//            ss.setTieneDatasource(Boolean.FALSE);
//            String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");
//            ss.setNombreSubCarpeta("certificados");
//            ss.setNombreReporte("certificadoNoAdeudarImpuestoPredial");
//            ss.addParametro("LOGO", path + SisVars.logoReportes);
//            ss.addParametro("NOMBRE_EMPRESA", "LA TESORERIA MUNICIPAL DEL CANTÓN MONTECRISTI.");
//            ss.addParametro("CANTON", (SisVars.NOMBRECANTON.toLowerCase().substring(0, 1).toUpperCase() + SisVars.NOMBRECANTON.toLowerCase().substring(1)));
//            ss.addParametro("CERTIFICADO", numCertificado);
//            tesoreria = manager.find(GeDepartamento.class, 20L);//ID: 20 Departamento de Tesoreria
//            for (AclRol rol : tesoreria.getAclRolCollection()) {
//                if (rol.getEsSubDirector()) {//ROL DE TESORERA
//                    for (AclUser user : rol.getAclUserCollection()) {
//                        ss.addParametro("TESORERA", user.getEnte() == null ? "REGISTRAR DATOS" : (Utils.isEmpty(user.getEnte().getTituloProf()) + " " + user.getEnte().getNombres() + " " + user.getEnte().getApellidos()));
//                        break;
//                    }
//                    break;
//                }
//            }
//            if (tipoCertificado.equals(1L)) {
//                List<CatPredioPropieatrioDTO> listaPropietarios = manager.buscarPredioPropietarios(getPredioConsulta().getId());
//
//                for (CatPredioPropieatrioDTO p : listaPropietarios) {
//                    propietarioText = propietarioText + p.getEnte() + " - ";
//                }
//                if (propietarioText != null && propietarioText.length() > 3) {
//                    propietarioText = propietarioText.substring(0, propietarioText.length() - 2);
//                }
//                //ss.addParametro("PROPIETARIO", p.getEnte().getEsPersona() ? p.getEnte().getApellidos() + " " + p.getEnte().getNombres() : p.getEnte().getRazonSocial());
//                ss.addParametro("PROPIETARIO", propietarioText);
//                ss.addParametro("NUM_PREDIO", getPredioConsulta().getNumPredio().toString());
//                ss.addParametro("CODIGO_CATASTRAL", getPredioConsulta().getCodigoPredialCompleto());
//                ss.addParametro("TITULO_REPORTE", 13L);
//            }
//            if (tipoCertificado.equals(2L)) {
//                ss.addParametro("PROPIETARIO", getPredioRuralConsulta().getPropietario() != null ? (getPredioRuralConsulta().getPropietario().getEsPersona() ? getPredioRuralConsulta().getPropietario().getApellidos() + " " + getPredioRuralConsulta().getPropietario().getNombres() : getPredioRuralConsulta().getPropietario().getRazonSocial()) : "");
//                ss.addParametro("CODIGO_CATASTRAL", getPredioRuralConsulta().getRegCatastral() + "-" + getPredioRuralConsulta().getIdPredial() + "-" + getPredioRuralConsulta().getParroquia().getDescripcion());
//                ss.addParametro("TITULO_REPORTE", 7L);
//            }

            JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
        } catch (Exception e) {
            Logger.getLogger(PagoPrediales.class.getName()).log(Level.OFF, null, e);
        }
    }

    public void comprobanteCoactiva() {
        try {
            if (pagoCoactiva.getId() != null) {
//                JsfUtil.redirectNewTab(com.origami.config.SisVars.urlbase + "Recibos?codigo=" + pagoCoactiva.getId());
                /*ss.instanciarParametros();
                 ss.addParametro("SUBREPORT_DIR", JsfUtil.getRealPath("/reportes/Emision/").concat("/"));
                 ss.addParametro("RUTA_FIRMAS", JsfUtil.getRealPath("/css/firmas/").concat("/"));
                 ss.setTieneDatasource(Boolean.TRUE);
                 ss.setNombreSubCarpeta("Emision");
                 ss.setNombreReporte("reciboCompleto");
                 ss.addParametro("ID_PAGO", pagoCoactiva.getId());
                 JsfUtil.redirectNewTab(com.origami.config.SisVars.urlbase + "Documento");*/
            }
        } catch (Exception e) {
            Logger.getLogger(PagoPrediales.class.getName()).log(Level.OFF, null, e);
        }
    }

    public void consultarPorNombreComprador(Long tipo) {
        try {
            if (nombreComprador != null) {
                setEmisionesPrediales(null);
                String[] nombreCompradorSegmentado = nombreComprador.trim().replaceAll(" ", "%").split("%");
                String nuevoNombre = "";
                if (nombreCompradorSegmentado.length > 2) {
                    for (int i = 2; i < nombreCompradorSegmentado.length; i++) {
                        nuevoNombre = nuevoNombre + "%" + nombreCompradorSegmentado[i];
                    }
                    for (int i = 0; i < 2; i++) {
                        nuevoNombre = nuevoNombre + "%" + nombreCompradorSegmentado[i];
                    }
                } else {
                    for (int i = nombreCompradorSegmentado.length; i > 0; i--) {
                        nuevoNombre = nuevoNombre + "%" + nombreCompradorSegmentado[i - 1];
                    }
                }
                if (tipo == 1L) {

                    List<Long> predios = new ArrayList<>();
                    predios = (List<Long>) manager.find("SELECT DISTINCT l.predio FROM FinaRenLiquidacion l WHERE l.tipoLiquidacion.id=13 AND l.nombreComprador ILIKE :nombreComprador",
                            new String[]{"nombreComprador"}, new Object[]{"%" + nombreComprador.trim().replaceAll(" ", "%") + "%"});

//                    prediosConsulta = manager.buscarPredioList(predios);
                    if (getPrediosConsulta() != null && !getPrediosConsulta().isEmpty()) {
                        if (getPrediosConsulta().size() == 1) {
                            //LLAMA AL PREDIO
                            paramt = new HashMap<>();
                            paramt.put("tipoLiquidacion", new FinaRenTipoLiquidacion(13L));
                            paramt.put("predio", getPrediosConsulta().get(0));
                            setPredioConsulta(getPrediosConsulta().get(0));
                            emisionesPredialesTemp = manager.findAllEasy("select f FinaRenLiquidacion f order by anio asc");
                            setEmisionesPrediales(emisionesPredialesTemp);
                            calculoTotalPago(getEmisionesPrediales(), null);
                            setTotalEmisionesGeneral(new BigDecimal(getTotalEmisiones().toString()));
                        } else {
                            //ABRIR CATALOGO PARA VARIOS PREDIOS LO HACE EL OTRO METODO
                            JsfUtil.update("frmPredios");
                            JsfUtil.executeJS("PF('dlgPrediosConsulta').show();");
                        }
                    } else {
                        //NUEVO INTENTO
                        predios = new ArrayList<>();
                        predios = (List<Long>) manager.find("SELECT DISTINCT l.predio FROM FinaRenLiquidacion l WHERE l.tipoLiquidacion.id=13 AND l.nombreComprador ILIKE :nombreComprador",
                                new String[]{"nombreComprador"}, new Object[]{"%" + nuevoNombre + "%"});

//                        prediosConsulta = manager.buscarPredioList(predios);
                        //  setPrediosConsulta(manager.findAll(manager.prediosUrbanosEnLiquidacionPorNombreContribuyente, new String[]{"nombreComprador"}, new Object[]{"%" + nuevoNombre + "%"}));
                        if (getPrediosConsulta() != null && !getPrediosConsulta().isEmpty()) {
                            if (getPrediosConsulta().size() == 1) {
                                //LLAMA AL PREDIO
                                paramt = new HashMap<>();
                                paramt.put("tipoLiquidacion", new FinaRenTipoLiquidacion(13L));
                                paramt.put("predio", getPrediosConsulta().get(0));
                                setPredioConsulta(getPrediosConsulta().get(0));
                                emisionesPredialesTemp = manager.findAllEasy("select f FinaRenLiquidacion f order by anio asc");
                                setEmisionesPrediales(emisionesPredialesTemp);
                                calculoTotalPago(getEmisionesPrediales(), null);
                                setTotalEmisionesGeneral(new BigDecimal(getTotalEmisiones().toString()));
                            } else {
                                //ABRIR CATALOGO PARA VARIOS PREDIOS LO HACE EL OTRO METODO
                                JsfUtil.update("frmPredios");
                                JsfUtil.executeJS("PF('dlgPrediosConsulta').show();");
                            }
                        } else {
                            JsfUtil.addInformationMessage("Mensaje", "No se encontraron coincidencias.");
                        }
                    }
                }
                if (tipo == 2L) {
                    emisionesPredialesTemp = new ArrayList<>();

                    List<Long> predios = new ArrayList<>();
                    predios = (List<Long>) manager.find("SELECT DISTINCT l.predioRustico FROM FinaRenLiquidacion l WHERE l.tipoLiquidacion.id=7 AND l.nombreComprador ILIKE :nombreComprador",
                            new String[]{"nombreComprador"}, new Object[]{"%" + nombreComprador.trim().replaceAll(" ", "%") + "%"});
                    prediosRusticoConsulta = manager.buscarPrediosRustico(predios);

                    if (getPrediosRusticoConsulta() != null && !getPrediosRusticoConsulta().isEmpty()) {
                        if (getPrediosRusticoConsulta().size() == 1) {
                            //LLAMA AL PREDIO
                            paramt = new HashMap<>();
                            paramt.put("tipoLiquidacion", new FinaRenTipoLiquidacion(7L));
                            paramt.put("predioRustico", getPrediosRusticoConsulta().get(0));
                            setPredioRuralConsulta(getPrediosRusticoConsulta().get(0));
                            emisionesPredialesTemp = manager.findAllEasy("select f FinaRenLiquidacion f order by anio asc");
                            setEmisionesPrediales(emisionesPredialesTemp);
                            calculoTotalPago(getEmisionesPrediales(), null);
                            setTotalEmisionesGeneral(new BigDecimal(getTotalEmisiones().toString()));
                        }
                    } else {
                        //NUEVO INTENTO

                        predios = new ArrayList<>();
                        predios = (List<Long>) manager.find("SELECT DISTINCT l.predioRustico FROM FinaRenLiquidacion l WHERE l.tipoLiquidacion.id=7 AND l.nombreComprador ILIKE :nombreComprador",
                                new String[]{"nombreComprador"}, new Object[]{"%" + nuevoNombre + "%"});
                        prediosRusticoConsulta = manager.buscarPrediosRustico(predios);

                        if (getPrediosRusticoConsulta() != null && !getPrediosRusticoConsulta().isEmpty()) {
                            if (getPrediosRusticoConsulta().size() == 1) {
                                //LLAMA AL PREDIO
                                paramt = new HashMap<>();
                                paramt.put("tipoLiquidacion", new FinaRenTipoLiquidacion(7L));
                                paramt.put("predioRustico", getPrediosRusticoConsulta().get(0));
                                setPredioRuralConsulta(getPrediosRusticoConsulta().get(0));
                                emisionesPredialesTemp = manager.findAllEasy("select f FinaRenLiquidacion f order by anio asc");
                                setEmisionesPrediales(emisionesPredialesTemp);
                                calculoTotalPago(getEmisionesPrediales(), null);
                                setTotalEmisionesGeneral(new BigDecimal(getTotalEmisiones().toString()));
                            }
                        } else {
                            JsfUtil.addInformationMessage("Mensaje", "No se encontraron coincidencias.");
                        }
                    }
                }
            } else {

            }
        } catch (Exception e) {
            Logger.getLogger(PagoPrediales.class.getName()).log(Level.OFF, null, e);
        }
    }

    public Boolean habilitarProcesar() {
        try {
            return session.getRoles().contains(73L) || session.getRoles().contains(201L) || session.getRoles().contains(75L)
                    || session.getRoles().contains(9L);
        } catch (Exception e) {
            Logger.getLogger(PagoPrediales.class.getName()).log(Level.OFF, null, e);
            return false;
        }
    }

    public void habilitarRecalculo() {
        if (session != null && session.getRoles() != null) {
            for (Long r : session.getRoles()) {
                if (r.equals(204L)) {
                    liquidador = true;
                }
            }
        }
    }

    public void preCalculo(Boolean normalx) throws InterruptedException, ExecutionException {
        normal = normalx;
        CatPredioModel predio = manager.buscarNumPredio(BigInteger.valueOf(emisionSeleccionada.getPredio().getId()));
        valoracion = manager.getEmisionPredial(session.getNameUser(), emisionSeleccionada.getAnio(), predio.getNumPredio(), normalx).get();
        if (valoracion != null) {
            JsfUtil.addInformationMessage("Nota!", "Predio recalculado!");
        }
    }

    public void recalcular() {
        try {
            setExoneracion((FnSolicitudExoneracion) manager.find("SELECT l FROM FnSolicitudExoneracion l WHERE l.predio = :predio AND l.exoneracionTipo.id IN (17, 18, 37, 44) AND l.estado.id IN(1, 2) ORDER BY l.anioInicio DESC",
                    new String[]{"predio"}, new Object[]{getPredioConsulta()}));
            if (getExoneracion() != null) {
                if (getExoneracion().getEstado().getId() == 3L || getExoneracion().getEstado().getId() == 4L || getExoneracion().getEstado().getId() == 2L) {
                    setExoneracion(null);
                    setMensajeExoneracion(null);
                } else {
                    setMensajeExoneracion("Tiene una exoneración de: " + getExoneracion().getExoneracionTipo().getDescripcion().toUpperCase()
                            + "\nNúmero de resolución: " + getExoneracion().getNumResolucionSac());
                    if (getExoneracion().getExoneracionTipo().getId() == 17L || getExoneracion().getExoneracionTipo().getId() == 18L || getExoneracion().getExoneracionTipo().getId() == 37L || getExoneracion().getExoneracionTipo().getId() == 44L) {
                        JsfUtil.update("formMensajeExo");
                        JsfUtil.executeJS("PF('dlgMensajeExo').show()");
                        return;
                    } else {
                        setExoneracion(null);
                        setMensajeExoneracion(null);
                    }
                }
            }
            if (manager.recalcular(session.getNameUser(), emisionSeleccionada.getId(), normal) != null) {
                JsfUtil.addInformationMessage("Nota!", "Liquidacion de predios urbanos recalculada satisfactoriamente");
            } else {
                JsfUtil.addWarningMessage("Advertencia!", "La emision actual esta exonerada, o realizo anticipos");
            }
        } catch (Exception e) {
            Logger.getLogger(PagoPrediales.class.getName()).log(Level.OFF, null, e);
        }
    }

    public void actualizarValorPago() {
        calculoTotalPago(emisionesACobrar, getModelPago().getPagoTransferencia().getFecha());
    }

    public void reversarPago(FinaRenPago pago) {
        //SOLO SE PUEDE REVERSAR EL ULTIMO PAGO
        try {
            Date fecha = new Date();
            if (pago != null && pago.getEstado()) {
                if (Utils.getDia(fecha).equals(Utils.getDia(pago.getFechaPago())) && Utils.getMes(fecha).equals(Utils.getMes(pago.getFechaPago())) && Utils.getAnio(fecha).equals(Utils.getAnio(pago.getFechaPago()))) {
                    if (usuario != null && usuario.getId().equals(pago.getCajero().getId())) {
                        if (pago.equals(manager.ultimoPago(pago.getLiquidacion())) && manager.ultimaEspecie(pago.getLiquidacion())) {
                            pago.setObservacion("Pago Anulado por: " + usuario.getUsuario());
                            pago = manager.reversarPago(pago);
                            if (pago != null) {
                                JsfUtil.addInformationMessage("Mensaje", "PAGO ANULADO EXITOSAMENTE");
                                JsfUtil.update("formDetPago");
                            } else {
                                JsfUtil.addErrorMessage("PROBLEMAS AL ANULAR EL PAGO", "VUELVA A REALIZAR LA ACCION");
                            }
                        } else {
                            JsfUtil.addInformationMessage("Mensaje", "SOLO SE PUEDE ANULAR EL ULTIMO PAGO PROCESADO");
                        }
                    } else {
                        JsfUtil.addInformationMessage("Mensaje", "SOLO SE ANULAN PAGOS REALIZADOS POR EL MISMO RECAUDADOR");
                    }
                } else {
                    JsfUtil.addInformationMessage("Mensaje", "SOLO SE ANULAN PAGOS DEL DIA VIGENTE");
                }
            } else {
                JsfUtil.addInformationMessage("Mensaje", "EL PAGO YA SE ENCUNETRA ANULADO");
            }
        } catch (Exception e) {
            Logger.getLogger(PagosRealizados.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void openDlgConvenio() {

        Map<String, List<String>> params = new HashMap<>();
        List<String> p = new ArrayList<>();

        p = new ArrayList<>();
        p.add("1");
        params.put("nuevo", p);

        p = new ArrayList<>();
        p.add(totalEmisiones.toString());
        params.put("deudaInicial", p);
        p = new ArrayList<>();
        p.add("1");
        params.put("calculaInteres", p);
        if (!emisionesPredialesTemp.isEmpty()) {
            p = new ArrayList<>();
            if (emisionesPredialesTemp.get(0).getComprador() != null) {
                p.add(emisionesPredialesTemp.get(0).getComprador().getId().toString());
            } else {
                JsfUtil.addErrorMessage("Error", "Debe Actualizar los Datos del Conribuyente de los Años Anteriores");
                return;
            }
            params.put("contribuyente", p);
        }

//        p = new ArrayList<>();
//        p.add(remisionInteresServices.aplicaRemisionPagoCuotaInicial(emisionesPredialesTemp).toString());
//        params.put("aplicaRemision", p);
        Map<String, Object> options = new HashMap<>();
        options.put("resizable", true);
        options.put("draggable", true);
        options.put("modal", true);
        options.put("closable", true);
        PrimeFaces.current().dialog().openDynamic("/resources/dialog/dlgConvenioPago", options, params);

    }

    public void procesarConvenio(SelectEvent event) {
        convenioPago = (FnConvenioPago) event.getObject();
        convenioPago.setConvenioAgua(Boolean.FALSE);
        convenioPago.setPredio(emisionesPredialesTemp.get(0).getPredio());
        convenioPago = (FnConvenioPago) manager.save(convenioPago);
        emisionesPredialesTemp.stream().map((l) -> {
            l.setValidada(Boolean.TRUE);
            return l;
        }).map((l) -> {
            l.setUsuarioValida(session.getNameUser());
            return l;
        }).map((l) -> {
            l.setConvenioPago(convenioPago);
            return l;
        }).map((l) -> {
            l.setEstadoLiquidacion(new FinaRenEstadoLiquidacion(7L));
            return l;
        }).forEachOrdered((l) -> {
            manager.save(l);
        });
        emisionesPrediales = new ArrayList();
        emisionesPredialesTemp = new ArrayList();
        JsfUtil.addInformationMessage("Info", "El convenio se ha elaborado con exito.");

    }

    public void realizarPagoCuotas() {

        List<FinaRenPago> pagos = new ArrayList<>();
        FinaRenPago pago;
        boolean terminado = emisionesACobrar.size() == convenidos.size();
        try {
            if (emisionesACobrar.size() > 1) {
                //VERIFICAR QUE SOLO SE REALIZE UN TIPO DE PAGO DE LOS 5 QUE EXISTEN                
                if (getModelPago().cantidadTipoPagos() == 1) {//VERIFICAR QUE SE REALIZE TOTOAL PAGO DE LaS EMISIONES SELECCIONADAS        
                    if (getModelPago().getValorTotal().compareTo(sumaTotalConv) == 0) {

                        for (FinaRenLiquidacion e : emisionesACobrar) {
                            //e = manager.realizarDescuentoRecargaInteresPredial(e,null);
                            e.calcularPago();
                            //e = manager.realizarPago(e, modelPago.realizarPago(e),usuario);
                            pago = manager.realizarPago(e, getModelPago().realizarPago(e), usuario, true);
                            if (pago != null) {
                                pagos.add(pago);
                            }
                        }

                    } else {
                        JsfUtil.addInformationMessage("Verifique el valor a cobrar", "El cobro de Emisiones multiples debe ser cancelado Totalmente");
                    }
                } else {
                    JsfUtil.addInformationMessage("Verifique Tipo de Pagos", "El cobro de Emisiones multiples solo permite un Tipo de Pago");
                }
            } else {
                if (getModelPago().getValorTotal().compareTo(sumaTotalConv) <= 0) {
                    if (getModelPago().getValorTotal().compareTo(BigDecimal.ZERO) > 0) {
                        for (FinaRenLiquidacion l : emisionesACobrar) {
                            //l = manager.realizarDescuentoRecargaInteresPredial(l,getModelPago().getPagoTransferencia().getFecha());
                            l.calcularPago();

                            pago = manager.realizarPago(l, getModelPago().realizarPago(l), usuario, true);
                            if (pago != null) {
                                pagos.add(pago);
                            }
                        }
                        if (pagos.isEmpty()) {
                            JsfUtil.addInformationMessage("Verifique el valor a cobrar", "Los valores ingresados debe ser mayor al Recargo+Interes");
                        }
                    } else {
                        JsfUtil.addInformationMessage("Verifique el valor a cobrar", "Los valores ingresados debe ser mayor a 0.00");
                    }
                } else {
                    JsfUtil.addInformationMessage("Verifique el valor a cobrar", "Los valores ingresados no deben ser mayor al de la Recaudación");
                }
            }
            if (!pagos.isEmpty()) {
                if (terminado) {
                    convenioPago.setEstado((short) 6);
                    FnConvenioPagoObservacion observacionConvenio = new FnConvenioPagoObservacion();
                    observacionConvenio.setConvenio(convenioPago);
                    observacionConvenio.setEstado(Boolean.TRUE);
                    observacionConvenio.setEstadoConvenio(convenioPago.getEstado());
                    observacionConvenio.setObservacion("Completado.");
                    observacionConvenio.setUsuarioIngreso(session.getNameUser());
                    observacionConvenio.setFechaIngreso(new Date());
                    manager.save(convenioPago);
                    manager.save(observacionConvenio);
                }
                /*LO DE LAS NOTAS DE CREDITO*/
//                for (NotasCredito nc : modelPago.getListPagoNota()) {
//                    NotasCreditoHistorico ht = new NotasCreditoHistorico();
//                    ht.setNotaCredito(nc);
//                    ht.setLiquidacion(emisionSeleccionada);
//                    ht.setEstado((short) 0);
//                    ht.setFechaIngreso(new Date());
//                    ht.setUsuarioIngreso(session.getNameUser());
//
//                    manager.save(nc);
//                    manager.save(ht);
//                }
                setPagoRealizado(Boolean.TRUE);
                generarComprobanteConvenio(pagos);
            }
        } catch (Exception e) {
            Logger.getLogger(PagoPrediales.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void generarComprobanteConvenio(List<FinaRenPago> pagos) {
        try {
//            String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");
//            ss.borrarParametros();
//            ss.instanciarParametros();
//    //        ss.setTieneDatasource(Boolean.TRUE);
//            ss.addParametro("SUBREPORT_DIR", JsfUtil.getRealPath("/reportes/"));
//            ss.addParametro("LOGO", path + SisVars.sisLogo);
//            ss.setNombreReporte("sComprobanteConvenioPagosPredios");
//            ss.addParametro("liquidaciones", pagos);
            JsfUtil.redirectNewTab(CONFIG.URL_APP + "Comprobantes");
        } catch (Exception e) {
            System.out.println("e: " + e.getStackTrace());
        }
    }

    //<editor-fold defaultstate="collapsed" desc="SETTER AND GETTER AND OTHERS METHOD">
    public UserSession getSession() {
        return session;
    }

    public void setSession(UserSession session) {
        this.session = session;
    }

    public Long getAnioEmision() {
        return anioEmision;
    }

    public void setAnioEmision(Long anioEmision) {
        this.anioEmision = anioEmision;
    }

    public CtlgSalario getSalario() {
        return salario;
    }

    public void setSalario(CtlgSalario salario) {
        this.salario = salario;
    }

    public void setBancos(List<FinaRenEntidadBancaria> bancos) {
        this.bancos = bancos;
    }

    public List<FinaRenEntidadBancaria> getTarjetas() {
        return tarjetas;
    }

    public void setTarjetas(List<FinaRenEntidadBancaria> tarjetas) {
        this.tarjetas = tarjetas;
    }

    public FinaRenPago getPagoCoactiva() {
        return pagoCoactiva;
    }

    public void setPagoCoactiva(FinaRenPago pagoCoactiva) {
        this.pagoCoactiva = pagoCoactiva;
    }

    public List<CatParroquia> getParroquiasRurales() {
        return parroquiasRurales;
    }

    public void setParroquiasRurales(List<CatParroquia> parroquiasRurales) {
        this.parroquiasRurales = parroquiasRurales;
    }

    public List<String> getCiudadelas() {
        return ciudadelas;
    }

    public void setCiudadelas(List<String> ciudadelas) {
        this.ciudadelas = ciudadelas;
    }

    public String getNumCertificado() {
        return numCertificado;
    }

    public void setNumCertificado(String numCertificado) {
        this.numCertificado = numCertificado;
    }

    public Long getTipoCertificado() {
        return tipoCertificado;
    }

    public void setTipoCertificado(Long tipoCertificado) {
        this.tipoCertificado = tipoCertificado;
    }

    public String getNombreComprador() {
        return nombreComprador;
    }

    public void setNombreComprador(String nombreComprador) {
        this.nombreComprador = nombreComprador;
    }

    public String getNombreContribuyente() {
        return nombreContribuyente;
    }

    public void setNombreContribuyente(String nombreContribuyente) {
        this.nombreContribuyente = nombreContribuyente;
    }

    public List<NombreContribuyenteModel> getModelNombresList() {
        return modelNombresList;
    }

    public void setModelNombresList(List<NombreContribuyenteModel> modelNombresList) {
        this.modelNombresList = modelNombresList;
    }

    public Boolean getLiquidador() {
        return liquidador;
    }

    public void setLiquidador(Boolean liquidador) {
        this.liquidador = liquidador;
    }

    public Map<String, Object> getParamt() {
        return paramt;
    }

    public void setParamt(Map<String, Object> paramt) {
        this.paramt = paramt;
    }

    public ValoracionPredialDTO getValoracion() {
        return valoracion;
    }

    public void setValoracion(ValoracionPredialDTO valoracion) {
        this.valoracion = valoracion;
    }

    public Boolean getNormal() {
        return normal;
    }

    public void setNormal(Boolean normal) {
        this.normal = normal;
    }

    public void setPropietario(CatPredioPropietario propietario) {
        try {
            if (propietario != null) {
                this.propietario = propietario;
                //    setContribuyenteConsulta(propietario.getEnte());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setPredioRustico(CatPredioRusticoDTO predioRustico) {
        try {
            if (predioRustico != null) {
                this.predioRustico = predioRustico;

                //setContribuyenteConsulta((Cliente) manager.find("Select c from Cliente c where c.nombre", new String[]{"ciru"}, new Object[]{predioRustico.getPropietario()}));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public CatPredioPropietario getPropietario() {
        return propietario;
    }

    public void emisionPredial() {
        if (getIsSanMiguel()) {
            getEmisionesPredialesTemp();
        } else {
            getEmisionSeleccionada();
        }
    }

    public Boolean getRenderContextMenu() {
        return renderContextMenu;
    }

    public void setRenderContextMenu(Boolean renderContextMenu) {
        this.renderContextMenu = renderContextMenu;
    }

    public List<NotasCredito> getListPagoNota() {
        return listPagoNota;
    }

    public void setListPagoNota(List<NotasCredito> listPagoNota) {
        this.listPagoNota = listPagoNota;
    }

    public NotasCredito getNota() {
        return nota;
    }

    public void setNota(NotasCredito nota) {
        this.nota = nota;
    }

    public Boolean getEsPagoCuota() {
        return esPagoCuota;
    }

    public void setEsPagoCuota(Boolean esPagoCuota) {
        this.esPagoCuota = esPagoCuota;
    }

    public FnConvenioPago getConvenioPago() {
        return convenioPago;
    }

    public void setConvenioPago(FnConvenioPago convenioPago) {
        this.convenioPago = convenioPago;
    }

    public boolean renderNoAdeudar() {

        int predio = predioConsulta != null ? 1 : 0;
        int predios = (prediosConsulta != null ? prediosConsulta.size() : 0);

        if ((predio + predios) > 0) {
            if (this.totalEmisiones != null) {
                if (totalEmisiones.compareTo(BigDecimal.ZERO) > 0) {
                    return false;
                } else {
                    return true;
                }
            } else {
                return false;
            }
        } else {

            return false;
        }

    }

    /*
    *
    *CALCULO DE REMISION DE INTERESES
     */
    public void openDlgRemisionInteres() {
        if (emisionesPrediales.get(emisionesPrediales.size() - 1).getComprador() != null) {

            Map<String, List<String>> params = new HashMap<>();
            List<String> p = new ArrayList<>();

            p = new ArrayList<>();
            p.add(remisionInteresService.saveFnRemisionSolicitudProceso(emisionesPrediales).getId().toString());
            params.put("fnRemisionSolicitudId", p);

            Map<String, Object> options = new HashMap<>();
            options.put("resizable", true);
            options.put("draggable", true);
            options.put("modal", true);
            options.put("closable", true);
            PrimeFaces.current().dialog().openDynamic("/resources/dialog/dlgRemisionInteres", options, params);
        } else {
            JsfUtil.addErrorMessage("Debe Actualizar los Datos del Propietario", null);
        }
    }

    public void procesarRemision(SelectEvent event) {
        FnExoneracionLiquidaciones frs = (FnExoneracionLiquidaciones) event.getObject();
        if (frs != null) {
            if (frs.getId() != null) {
                JsfUtil.addInformationMessage("Info", "Datos Guardados Correctamente");
                // RequestContext.getCurrentInstance().closeDialog(frs);
            } else {
                JsfUtil.addErrorMessage("Info", "Ocurrio un Problema Mientras Se saveian los datos");
            }
        } else {
            JsfUtil.addErrorMessage("Info", "Ocurrio un Problema Mientras Se saveian los datos");
        }
    }

    public ManagerService getManager() {
        return manager;
    }

    public void setManager(ManagerService manager) {
        this.manager = manager;
    }

    public ServletSession getSs() {
        return ss;
    }

    public void setSs(ServletSession ss) {
        this.ss = ss;
    }

    public List<FinaRenDetLiquidacion> getRubrosEmision() {
        return rubrosEmision;
    }

    public void setRubrosEmision(List<FinaRenDetLiquidacion> rubrosEmision) {
        this.rubrosEmision = rubrosEmision;
    }

    public FinaRenDetLiquidacion getRubro() {
        return rubro;
    }

    public void setRubro(FinaRenDetLiquidacion rubro) {
        this.rubro = rubro;
    }

    public FinaRenTipoLiquidacion getTipoLiquidacion() {
        return tipoLiquidacion;
    }

    public void setTipoLiquidacion(FinaRenTipoLiquidacion tipoLiquidacion) {
        this.tipoLiquidacion = tipoLiquidacion;
    }

    public FinaRenLiquidacion getEmisionSeleccionada() {
        return emisionSeleccionada;
    }

    public void setEmisionSeleccionada(FinaRenLiquidacion emisionSeleccionada) {
        this.emisionSeleccionada = emisionSeleccionada;
    }

    public List<FinaRenLiquidacion> getEmisionesACobrar() {
        return emisionesACobrar;
    }

    public void setEmisionesACobrar(List<FinaRenLiquidacion> emisionesACobrar) {
        this.emisionesACobrar = emisionesACobrar;
    }

    public List<FinaRenLiquidacion> getEmisionesPredialesTemp() {
        return emisionesPredialesTemp;
    }

    public void setEmisionesPredialesTemp(List<FinaRenLiquidacion> emisionesPredialesTemp) {
        this.emisionesPredialesTemp = emisionesPredialesTemp;
    }

    public Boolean getEsPagoCuotaCoactiva() {
        return esPagoCuotaCoactiva;
    }

    public void setEsPagoCuotaCoactiva(Boolean esPagoCuotaCoactiva) {
        this.esPagoCuotaCoactiva = esPagoCuotaCoactiva;
    }

 

    public List<FinaRenEntidadBancaria> getBancos() {
        return bancos;
    }

    public CatPredioRusticoDTO getPredioRustico() {
        return predioRustico;
    }





 
//</editor-fold>

    public void setRemisionInteresService(RemisionInteresService remisionInteresService) {
        this.remisionInteresService = remisionInteresService;
    }

  
    public RemisionInteresService getRemisionInteresService() {
        return remisionInteresService;
    }

  

}
