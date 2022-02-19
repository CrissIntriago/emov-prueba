/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Controller;

import com.asgard.Entity.FinaRenDetLiquidacion;
import com.asgard.Entity.FinaRenEstadoLiquidacion;
import com.asgard.Entity.FinaRenLiquidacion;
import com.asgard.Entity.FinaRenPago;
import com.asgard.Entity.FinaRenRubrosLiquidacion;
import com.asgard.Entity.FinaRenTipoLiquidacion;
import com.asgard.Entity.FinaRenTipoValor;
import com.gestionTributaria.Entities.CatPredio;
import com.gestionTributaria.Entities.CatPredioPropietario;
import com.gestionTributaria.Entities.CtlgSalario;
import com.gestionTributaria.Entities.FnEstadoExoneracion;
import com.gestionTributaria.Entities.FnExoneracionClase;
import com.gestionTributaria.Entities.FnExoneracionLiquidacion;
import com.gestionTributaria.Entities.FnExoneracionTipo;
import com.gestionTributaria.Entities.FnResolucion;
import com.gestionTributaria.Entities.FnSolicitudExoneracion;
import com.gestionTributaria.Entities.FnSolicitudExoneracionPredios;
import com.gestionTributaria.Entities.FnSolicitudTipoLiquidacionExoneracion;
import com.gestionTributaria.Recaudacion.RecaudacionInteface;
import com.gestionTributaria.Services.CatPredioPropietarioService;
import com.gestionTributaria.Services.FinaRenLiquidacionService;
import com.gestionTributaria.Services.FinaRenPagoService;
import com.gestionTributaria.Services.FinaRenTipoValorServices;
import com.gestionTributaria.Services.FnExoneracionClaseService;
import com.gestionTributaria.Services.FnSolicitudExoneracionServices;
import com.gestionTributaria.Services.FnTipoExoneracionService;
import com.gestionTributaria.Services.ManagerService;
import com.gestionTributaria.Services.ResolucionesService;
import com.gestionTributaria.Services.TipoLiquidacionService;
import com.gestionTributaria.models.BusquedaPredios;
import com.gestionTributaria.models.CatPredioModel;
import com.gestionTributaria.models.RubrosPorTipoLiq;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.resource.procesos.entities.Observaciones;
import com.origami.sigef.talentohumano.UtilsTH.TalentoHumano;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
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

/**
 *
 * @author Administrator
 */
@Named
@ViewScoped
public class AplicarExoneracionMB extends BusquedaPredios implements Serializable {

    private static final Logger LOG = Logger.getLogger(AplicarExoneracionMB.class.getName());

    //mis variables
    @Inject
    private FinaRenLiquidacionService renLiquidacionService;
    @Inject
    private TipoLiquidacionService tipoliquidacionService;
    @Inject
    private FnExoneracionClaseService exoneracionService;
    @Inject
    private FnTipoExoneracionService tipoExoneracionService;
    @Inject
    private ManagerService services;
    @Inject
    private UserSession session;
    @Inject
    private FinaRenTipoValorServices finaRenTipoValorServices;
    @Inject
    private FnSolicitudExoneracionServices exoservice;
    @Inject
    private CatPredioPropietarioService propietarioService;
    @Inject
    private ClienteService clienteService;
    @Inject
    private FinaRenPagoService finaRenPagoService;
    @Inject
    private ResolucionesService resolucionesService;
    @Inject
    private RecaudacionInteface recaudacionEjb;

    private FnExoneracionClase clase;
    private List<FnExoneracionClase> clases;
    private List<FnExoneracionLiquidacion> exoneraciones;
    private List<FinaRenTipoValor> tipoValores = new ArrayList<>();
    private FnSolicitudExoneracion solicitud;
    private FinaRenLiquidacion liquidacion;
    private List<FinaRenLiquidacion> liquidacionesSeleccionadas = new ArrayList<>();
    private FnExoneracionTipo tipo;
    private List<FinaRenLiquidacion> prediosExonerar;
    private CtlgSalario salario;
    private BigDecimal salarioMax;

    private Map<String, Object> parametros;
    private Observaciones obs;
    private List<RubrosPorTipoLiq> detList1, detList2;
    private Integer aplicacionEstado;
    private List<FnExoneracionTipo> tipos;
    private List<FnSolicitudTipoLiquidacionExoneracion> tipoLiquidacionesEnSolitud = new ArrayList<>();
    private BigDecimal baseImponible;
    private FinaRenTipoLiquidacion tipoLiquidacion;
    private FinaRenRubrosLiquidacion rubroLiquidacion;
    private List<FnSolicitudExoneracionPredios> prediosEnSolicitud = new ArrayList<>();
    private Boolean revision500Exoneraciones = Boolean.TRUE;
    private List<FinaRenDetLiquidacion> detLiq;
    private List<CatPredio> prediosList;
    private Cliente propietario;
    //resoluciones
    private FnResolucion resolucion;

    @PostConstruct
    public void init() {
        try {
            prediosList = new ArrayList<>();
            liquidacion = new FinaRenLiquidacion();
            obs = new Observaciones();
            clases = exoneracionService.findAllExoneraciones();
            tipoValores = finaRenTipoValorServices.getRenTipovalor("exo");
            traerSalarioAño();
            if (session.esLogueado()) {
                resolucion = new FnResolucion();
                solicitud = new FnSolicitudExoneracion();
                solicitud.setFechaAprobacion(new Date());
                solicitud.setPredio(new CatPredio());
                clase = exoneracionService.findAllExoneraciones().get(0);
                tipos = tipoExoneracionService.findAllExoneracionesByIdTipo(clase.getId());
                if (tipo == null) {
                    tipo = tipos.get(0);
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Error" + e.getMessage());
        }
    }

    private void traerSalarioAño() {
        parametros = new HashMap<>();
        parametros.put("anio", new BigInteger(Utils.getAnio(new Date()).toString()));
        salario = (CtlgSalario) services.findByParameter(CtlgSalario.class, parametros);
        if (salario != null) {
            salarioMax = salario.getValor().multiply(new BigDecimal(500));
        } else {
            salarioMax = BigDecimal.ZERO;
            JsfUtil.addErrorMessage("Error", "No se encontro registro de Salario para el año actual.");
        }
    }

    public void consultarEmi() {
        System.out.println("consultando");
        this.consultarPrediosEmisiones();
        int anios = 0;
        Date fechaActual = new Date();
        if (Utils.isNotEmpty(prediosConsulta)) {
            CatPredioPropietario prop = propietarioService.findByPropietario(prediosConsulta.get(0));
            System.out.println("propietario>>" + prop.getEnte());
            propietario = new Cliente();
            if (prop != null) {
                propietario = prop.getEnte();
            }
            if (propietario.getIdentificacion() != null) {
                System.out.println("identificacion>>" + propietario.getIdentificacion());
                propietario = clienteService.buscarClienteData(propietario, Boolean.TRUE);
                if (this.propietario.getCondicionCiudadano() == null) {
                    propietario.setCondicionCiudadano("");
                }
            }
            System.out.println("condicion ciudadano>>>" + this.propietario.getCondicionCiudadano());
        } else {
            JsfUtil.addWarningMessage("Información", "Criterios de Busqueda no encontrado...");
        }
//        System.out.println("año>>" + anios + " fecha nac>>" + Utils.dateFormatPattern("dd/MM/yyyy", propietario.getFechaNacimiento()));
    }

    private void verificarSeleccionTipoValor() {
        if (tipo == null) {
            JsfUtil.addInformationMessage("Info", "Debe seleccionar el tipo de exoneracion");
        }
        if ((solicitud.getValor() == null)) {
            JsfUtil.addInformationMessage("Info", "Debe ingresar el valor correspondiente a su tipo");
        }
    }

    public Boolean validarBaseImponible() {
        if ((patrimonioImponibleTotal().compareTo(salarioMax)) > 0) {
            JsfUtil.addInformationMessage("Información", "El patrimonio neto de la base imponible total supera las 500 remuneraciones basicas");
            return true;
        } else {
            return false;
        }
    }

    public Boolean verificarPredioExonerado() {
        boolean bandera = true;
        if (!liquidacionesSeleccionadas.isEmpty()) {
            for (FinaRenLiquidacion liquidacion : liquidacionesSeleccionadas) {
                if (!this.verificarPredioExonerado(liquidacion, tipo, solicitud.getAnioInicio())) {
                    JsfUtil.addInformationMessage("Información", "Solicitud Registrada para el Predio en el Periodo2");
                    bandera = false;
                    break;
                }
            }
        } else {
            bandera = false;
        }
        return bandera;
    }

    /*SE VERIFICA SI LA EMISION ESTA PAGADA Y ABONADA*/
    public Boolean verificarEmisionPago() {
        boolean bandera = true;
        List<FinaRenPago> pagos;
        if (!liquidacionesSeleccionadas.isEmpty()) {
            liquidacion = liquidacionesSeleccionadas.get(0);
            if (this.tipo.getId() != 34L) { // el id 34 es BAJA DE TITULOS
                for (FinaRenLiquidacion liquidacion : liquidacionesSeleccionadas) {
                    pagos = finaRenPagoService.getPagosByPredioTipoLiquidacionAnio(liquidacion.getPredio().getId(), null, new FinaRenTipoLiquidacion("IMPUESTO A LOS PREDIOS URBANOS"), solicitud.getAnioInicio(), solicitud.getAnioFin());
                    if (pagos != null && !pagos.isEmpty()) {
                        JsfUtil.addInformationMessage("Información", "Solo procede las Emisiones Pendientes de Pago");
                        bandera = false;
                        break;
                    }
                }
            } else {
                for (FinaRenLiquidacion liquidacion : liquidacionesSeleccionadas) {
                    pagos = finaRenPagoService.getPagosByPredioTipoLiquidacionAnioPagada(liquidacion.getId(), null, new FinaRenTipoLiquidacion("IMPUESTO A LOS PREDIOS URBANOS"), solicitud.getAnioInicio(), solicitud.getAnioFin());
                    if (pagos != null && !pagos.isEmpty()) {
                        JsfUtil.addInformationMessage("Información", "Solo procede las Emisiones Pendientes de Pago");
                        bandera = false;
                        break;
                    }
                }
            }
        } else {
            bandera = false;
        }
        return bandera;
    }

    public Boolean verificarLeyCiego() {
        Boolean bandera = true;
        if (tipo.getId().intValue() == 31 || tipo.getId().intValue() == 37) {
            if (liquidacionesSeleccionadas.size() > 1) {
                JsfUtil.addInformationMessage("Para las exoneraciones de " + tipo.getDescripcion(), "Solo se pueden realizar sobre un solo bien "
                        + "inmueble con un avalúo máximo de quinientas (500) remuneraciones básicas");
                bandera = false;
            }
        } else {
            return bandera;
        }
        return true;
    }

    public void aplicarExoneracion() {
        BigDecimal valorExonerado = BigDecimal.ZERO;;
        Boolean exonerado = Boolean.FALSE;
        Boolean excedente = Boolean.FALSE;
        solicitud.setExoneracionTipo(tipo);
        solicitud.setFechaIngreso(new Date());
        solicitud.setUsuarioCreacion(session.getNameUser());
        solicitud.setSolicitante(null);
        solicitud.setPredio(prediosList.get(0));
        solicitud.setEstado(new FnEstadoExoneracion(2L));
        solicitud = exoservice.create(solicitud);
        resolucion.setSolicitudExoneracion(exoneracion);
        resolucion.setClaseExoneracion(solicitud.getExoneracionTipo().getExoneracionClase());
        resolucion.setEstado("A");
        resolucion = resolucionesService.create(resolucion);
        solicitud.setResolucion(resolucion);
        exoservice.edit(solicitud);
        registrarSolicudExoneracion();
//        System.out.println("cedula>>>" + this.propietario.getIdentificacion());
        if (propietario.getIdentificacion() == null) {
            JsfUtil.addWarningMessage("AVISO!!!", "El Sr(a). " + propietario.getNombreCompleltoSql() + " presenta inconsistencia en los datos, deben ser actualizados...");
            return;
        }
        if (this.patrimonioTotal().compareTo(salarioMax) > 0) {
            excedente = Boolean.TRUE;
            JsfUtil.addSuccessMessage("Información.", "El Patrimonio neto de la base imponible total supera las 500 Remuneraciones basicas...");
        }
        if (!this.propietario.getCondicionCiudadano().equals("FALLECIDO")) {
//        if (true) {
            paramtr = new HashMap();
            paramtr.put("SALARIOMAX", salarioMax);
            paramtr.put("PROPIETARIO", propietario);
            paramtr.put("EXCEDENTE", excedente);
            paramtr.put("VALOREXCEDENTE", this.patrimonioTotal());
            exoneraciones = recaudacionEjb.aplicarExoneracion(prediosConsulta, solicitud, paramtr);
            JsfUtil.addSuccessMessage("Aviso..", "Exoneración aplicada con exito...");
            exoneraciones = new ArrayList();
            solicitud = new FnSolicitudExoneracion();
            this.init();
        } else {
            JsfUtil.addWarningMessage("AVISO!!!", "El Sr(a). " + propietario.getNombreCompleltoSql() + " presenta ACTA DE DEFUNCIÓN");
        }
    }

    public void registrarSolicudExoneracion() {
        FnSolicitudExoneracionPredios predioSolicitud;
        for (CatPredio cp : prediosList) {
            predioSolicitud = new FnSolicitudExoneracionPredios();
            predioSolicitud.setPredio(cp);
            predioSolicitud.setSolicitudExoneracion(solicitud);
            services.save(predioSolicitud);
        }
    }

    public void registrarExoneracionLiquidacion(FinaRenLiquidacion liquidacion) {
        FnExoneracionLiquidacion exoneracionLiquidacion = new FnExoneracionLiquidacion();
        try {
            exoneracionLiquidacion.setFechaIngreso(new Date());
            exoneracionLiquidacion.setLiquidacionOriginal(liquidacion);
            exoneracionLiquidacion.setLiquidacionPosterior(liquidacion);
            exoneracionLiquidacion.setExoneracion(solicitud);
            exoneracionLiquidacion.setUsuarioIngreso(session.getNameUser());
            exoneracionLiquidacion.setEstado(Boolean.TRUE);
            exoneracionLiquidacion.setEsUrbano(Boolean.TRUE);
            services.save(exoneracionLiquidacion);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    /*MODIFICACION JC 2 */
    public Boolean exoneracionA500Remuneraciones(BigDecimal salarioMax) {
        BigDecimal totalesAvaluos = BigDecimal.ZERO;

        for (FinaRenLiquidacion liquidacion : liquidacionesSeleccionadas) {
            if (liquidacion.getAvaluoMunicipal() != null) {
                totalesAvaluos = totalesAvaluos.add(liquidacion.getAvaluoMunicipal() != null ? liquidacion.getAvaluoMunicipal() : BigDecimal.ZERO);
            }
        }
        int res = totalesAvaluos.compareTo(salarioMax);
        if (res == 1) {
            JsfUtil.addErrorMessage(null, "La sumatoria de los Avaluos Supera las 500 remuneraciones Basicas");
            return true;
        } else {
            List<FinaRenLiquidacion> prediosUrbanosExoneracion = new ArrayList();
            for (FinaRenLiquidacion liquidacion : liquidacionesSeleccionadas) {
                liquidacion.setAvaluoMunicipal(BigDecimal.ZERO);
                prediosUrbanosExoneracion.add(liquidacion);
            }
            prediosExonerar = new ArrayList<>();
            prediosExonerar = prediosUrbanosExoneracion;
        }
        return false;
    }

    /*MODIFICACION JC 3 */
    public Boolean validarExoneracion() {
        Boolean exonera = Boolean.TRUE;
        if (tipo.getId().intValue() == 29 || tipo.getId().intValue() == 35) {
            if (solicitud.getTipoValor().getId() == 7) {
                for (FinaRenLiquidacion liquidacion : liquidacionesSeleccionadas) {
                    if (liquidacion.getAvaluoMunicipal().compareTo(solicitud.getValor()) < 0) {
                        JsfUtil.addErrorMessage("El Valor Ingresado no Puede ser Mayor al  Avalùo del Predio", "");
                        exonera = Boolean.FALSE;
                    }
                }
                if (solicitud.getValor().compareTo(new BigDecimal(100)) < 0) {
                    JsfUtil.addErrorMessage("El Valor Ingresado no Puede ser Mayor al 100%", "");
                    exonera = Boolean.FALSE;
                }
            }
        }
        return exonera;
    }

    public Boolean verificarPredioExonerado(FinaRenLiquidacion liquidacion, FnExoneracionTipo exoneracionTipo, Integer anio) {
        FnSolicitudExoneracion solicitudConsulta = null;
        if (liquidacion != null) {
            solicitudConsulta = (FnSolicitudExoneracion) services.find("SELECT f FROM FnSolicitudExoneracion f WHERE f.predio = :predio AND f.exoneracionTipo = :tipo AND f.anioInicio<=:anio AND "
                    + "f.anioFin>=:anio AND f.estado IN (1,2)", new String[]{"predio", "tipo", "anio"}, new Object[]{liquidacion.getPredio(), exoneracionTipo, Utils.getAnio(new Date())});
            if (solicitudConsulta != null) {
                return false;
            }
        }
        return true;
    }

    public void eliminarTipoLiquidacionSolicitud(FnSolicitudTipoLiquidacionExoneracion tipoLiqSol) {
        for (FnSolicitudTipoLiquidacionExoneracion tls : this.tipoLiquidacionesEnSolitud) {
            if (tls.getTipoLiquidacion().equals(tipoLiqSol.getTipoLiquidacion())) {
                this.tipoLiquidacionesEnSolitud.remove(tls);
                return;
            }
        }
    }

    public void eliminarPredioUrbano(CatPredio predio) {
        this.prediosConsulta.remove(predio);
    }

    public List<FinaRenLiquidacion> getLiquidacionesByPredio(CatPredio cp) {
        FinaRenTipoLiquidacion tipo = null;
        if (cp.getTipoPredio().equals("U")) {
            tipo = new FinaRenTipoLiquidacion(2L);
        } else {
            tipo = new FinaRenTipoLiquidacion(3L);
        }
        List<FinaRenLiquidacion> listaLiquidacion = renLiquidacionService.liquidacionesConsultaByTipoPredio(cp, new FinaRenEstadoLiquidacion(2L), tipo);
        return listaLiquidacion;
    }

    public void openDlgSolicitante() {
        JsfUtil.executeJS("PF('dlgSolicitante').show();");
    }

    //funcion validar solo numeros
    public boolean validar(String cadena) {
        if (cadena.matches("[0-9]{10}")) {
            return true;
        } else {
            return false;
        }
    }

    public void selClase() {
        tipos = tipoExoneracionService.findAllExoneracionesByIdTipo(clase.getId());
        if (tipo.getPorcentaje() != null) {
            solicitud.setValor(tipo.getPorcentaje());
        } else {
            solicitud.setValor(BigDecimal.ZERO);
        }
    }

    public boolean verificarPago(CatPredioModel p) {
        parametros = new HashMap<>();
        parametros.put("tipoLiquidacion", new FinaRenTipoLiquidacion("IMPUESTO A LOS PREDIOS URBANOS"));
        parametros.put("predio", p);
        parametros.put("anio", Utils.getAnio(new Date()));
        List<FinaRenLiquidacion> emisionesPrediales = services.findAllQuery("SELECT r FROM RenLiquidacion r WHERE r.tipoLiquidacion = :tipoLiquidacion AND r.predio = :predio  AND r.anio = :anio AND r.estadoLiquidacion IN (2) ORDER BY r.anio ASC", parametros);
        if (emisionesPrediales != null) {
            if (!emisionesPrediales.isEmpty()) {
                if (emisionesPrediales.get(0).getEstadoLiquidacion().getId() == 1L) {
                    return true;
                }
            }
        }
        return false;
    }

    /*PATRIMONIO*/
    public BigDecimal patrimonioTotal() {
        BigDecimal patrimonio = BigDecimal.ZERO;
        if (Utils.isNotEmpty(this.prediosConsulta)) {
            prediosList.clear();
            for (CatPredio pre : this.prediosConsulta) {
                if (!prediosList.contains(pre)) {
                    prediosList.add(pre);
                    patrimonio = patrimonio.add(pre.getAvaluoMunicipal() != null ? pre.getAvaluoMunicipal() : BigDecimal.ZERO);
                }
            }
            predioConsulta = prediosConsulta.get(0);
        }
        return patrimonio;
    }

    public BigDecimal patrimonioImponibleTotal() {
        BigDecimal patrimonio = BigDecimal.ZERO;
        if (Utils.isNotEmpty(this.prediosConsulta)) {
            prediosList.clear();
            for (CatPredio pre : this.prediosConsulta) {
                if (!prediosList.contains(pre)) {
                    prediosList.add(pre);
                    patrimonio = patrimonio.add(pre.getAvaluoMunicipal() != null ? pre.getAvaluoMunicipal() : BigDecimal.ZERO);
                }
            }
            predioConsulta = prediosConsulta.get(0);
        }
        return patrimonio;
    }

    public Boolean getAplicaExoneracion(FinaRenLiquidacion liq) {
        int anionacimiento = Utils.getAnio(propietario.getFechaNacimiento());
        int aniodiferen = liq.getAnio() - anionacimiento;
        if (aniodiferen > TalentoHumano.ANIOS_MAYOR_EDAD) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    public void addResolucion() {
        System.out.println("numero tramite>>" + resolucion.getTramite());
        this.solicitud.setResolucion(new FnResolucion());
        this.solicitud.setNumResolucionSac(resolucion.getNumeroOficio());
//        this.solicitud.setResolucion(resolucion);
        System.out.println("resolucion solicitud>>" + solicitud.getNumResolucionSac());
    }

//<editor-fold defaultstate="collapsed" desc="set ans get">
    public List<FinaRenTipoValor> getTipoValores() {
        return tipoValores;
    }

    public void setTipoValores(List<FinaRenTipoValor> tipoValores) {
        this.tipoValores = tipoValores;
    }

    public FnResolucion getResolucion() {
        return resolucion;
    }

    public void setResolucion(FnResolucion resolucion) {
        this.resolucion = resolucion;
    }

    public TipoLiquidacionService getTipoliquidacionService() {
        return tipoliquidacionService;
    }

    public void setTipoliquidacionService(TipoLiquidacionService tipoliquidacionService) {
        this.tipoliquidacionService = tipoliquidacionService;
    }

    public FnExoneracionClaseService getExoneracionService() {
        return exoneracionService;
    }

    public void setExoneracionService(FnExoneracionClaseService exoneracionService) {
        this.exoneracionService = exoneracionService;
    }

    public FnTipoExoneracionService getTipoExoneracionService() {
        return tipoExoneracionService;
    }

    public void setTipoExoneracionService(FnTipoExoneracionService tipoExoneracionService) {
        this.tipoExoneracionService = tipoExoneracionService;
    }

    public FinaRenTipoValorServices getFinaRenTipoValorServices() {
        return finaRenTipoValorServices;
    }

    public void setFinaRenTipoValorServices(FinaRenTipoValorServices finaRenTipoValorServices) {
        this.finaRenTipoValorServices = finaRenTipoValorServices;
    }

    public List<FnExoneracionClase> getClases() {
        return clases;
    }

    public void setClases(List<FnExoneracionClase> clases) {
        this.clases = clases;
    }

    public Map<String, Object> getParametros() {
        return parametros;
    }

    public void setParametros(Map<String, Object> parametros) {
        this.parametros = parametros;
    }

    public BigDecimal getSalarioMax() {
        return salarioMax;
    }

    public void setSalarioMax(BigDecimal salarioMax) {
        this.salarioMax = salarioMax;
    }

    public FnSolicitudExoneracion getSolicitud() {
        return solicitud;
    }

    public void setSolicitud(FnSolicitudExoneracion solicitud) {
        this.solicitud = solicitud;
    }

    public FnExoneracionClase getClase() {
        return clase;
    }

    public void setClase(FnExoneracionClase clase) {
        this.clase = clase;
    }

    public Observaciones getObs() {
        return obs;
    }

    public void setObs(Observaciones obs) {
        this.obs = obs;
    }

    public FnExoneracionTipo getTipo() {
        return tipo;
    }

    public void setTipo(FnExoneracionTipo tipo) {
        this.tipo = tipo;
    }

    public List<RubrosPorTipoLiq> getDetList1() {
        return detList1;
    }

    public void setDetList1(List<RubrosPorTipoLiq> detList1) {
        this.detList1 = detList1;
    }

    public List<RubrosPorTipoLiq> getDetList2() {
        return detList2;
    }

    public void setDetList2(List<RubrosPorTipoLiq> detList2) {
        this.detList2 = detList2;
    }

    public Integer getAplicacionEstado() {
        return aplicacionEstado;
    }

    public void setAplicacionEstado(Integer aplicacionEstado) {
        this.aplicacionEstado = aplicacionEstado;
    }

    public List<FnExoneracionTipo> getTipos() {
        return tipos;
    }

    public void setTipos(List<FnExoneracionTipo> tipos) {
        this.tipos = tipos;
    }

    public List<FnSolicitudTipoLiquidacionExoneracion> getTipoLiquidacionesEnSolitud() {
        return tipoLiquidacionesEnSolitud;
    }

    public void setTipoLiquidacionesEnSolitud(List<FnSolicitudTipoLiquidacionExoneracion> tipoLiquidacionesEnSolitud) {
        this.tipoLiquidacionesEnSolitud = tipoLiquidacionesEnSolitud;
    }

    public BigDecimal getBaseImponible() {
        return baseImponible;
    }

    public void setBaseImponible(BigDecimal baseImponible) {
        this.baseImponible = baseImponible;
    }

    public FinaRenTipoLiquidacion getTipoLiquidacion() {
        return tipoLiquidacion;
    }

    public void setTipoLiquidacion(FinaRenTipoLiquidacion tipoLiquidacion) {
        this.tipoLiquidacion = tipoLiquidacion;
    }

    public FinaRenRubrosLiquidacion getRubroLiquidacion() {
        return rubroLiquidacion;
    }

    public void setRubroLiquidacion(FinaRenRubrosLiquidacion rubroLiquidacion) {
        this.rubroLiquidacion = rubroLiquidacion;
    }

    public List<FnSolicitudExoneracionPredios> getPrediosEnSolicitud() {
        return prediosEnSolicitud;
    }

    public void setPrediosEnSolicitud(List<FnSolicitudExoneracionPredios> prediosEnSolicitud) {
        this.prediosEnSolicitud = prediosEnSolicitud;
    }

    public List<FnExoneracionLiquidacion> getExoneraciones() {
        return exoneraciones;
    }

    public void setExoneraciones(List<FnExoneracionLiquidacion> exoneraciones) {
        this.exoneraciones = exoneraciones;
    }

    public Boolean getRevision500Exoneraciones() {
        return revision500Exoneraciones;
    }

    public void setRevision500Exoneraciones(Boolean revision500Exoneraciones) {
        this.revision500Exoneraciones = revision500Exoneraciones;
    }

    public List<FinaRenDetLiquidacion> getDetLiq() {
        return detLiq;
    }

    public void setDetLiq(List<FinaRenDetLiquidacion> detLiq) {
        this.detLiq = detLiq;
    }

    public Cliente getPropietario() {
        return propietario;
    }

    public void setPropietario(Cliente propietario) {
        this.propietario = propietario;
    }

    public List<FinaRenLiquidacion> getLiquidacionesSeleccionadas() {
        return liquidacionesSeleccionadas;
    }

    public void setLiquidacionesSeleccionadas(List<FinaRenLiquidacion> liquidacionesSeleccionadas) {
        this.liquidacionesSeleccionadas = liquidacionesSeleccionadas;
    }

//</editor-fold>
}
