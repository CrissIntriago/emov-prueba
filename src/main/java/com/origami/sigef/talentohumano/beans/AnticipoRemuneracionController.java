/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.talentohumano.beans;

import com.origami.sigef.activos.service.procesoService.ProcesoService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.config.RolUsuario;
import com.origami.sigef.common.entities.AnticipoRemuneracion;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.entities.CuentaContable;
import com.origami.sigef.common.entities.CuotaAnticipo;
import com.origami.sigef.common.entities.Distributivo;
import com.origami.sigef.common.entities.ParametrosTalentoHumano;
import com.origami.sigef.common.entities.ProcedimientoRequisito;
import com.origami.sigef.resource.talento_humano.entities.Servidor;
import com.origami.sigef.common.entities.TipoTramiteRequisitoHistorial;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.service.interfaces.FileUploadDoc;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.contabilidad.service.ComprobantePagoService;
import com.origami.sigef.contabilidad.service.CuentaContableService;
import com.origami.sigef.procesos.Model.ListarequisitosModel;
import com.origami.sigef.talentohumano.services.AnticipoRemuneracionService;
import com.origami.sigef.talentohumano.services.CuotaAnticipoService;
import com.origami.sigef.talentohumano.services.ParametrizableService;
import com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot;
import com.origami.sigef.resource.procesos.entities.Observaciones;
import com.origami.sigef.resource.procesos.services.TramiteRequisitoHistorialService;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author ORIGAMI2
 */
@Named(value = "anticipoView")
@ViewScoped
public class AnticipoRemuneracionController extends BpmnBaseRoot implements Serializable {

    private static final Logger LOG = Logger.getLogger(AnticipoRemuneracionController.class.getName());

    @Inject
    private FileUploadDoc uploadDoc;
    @Inject
    private AnticipoRemuneracionService anticipoRemunercionService;
    @Inject
    private CuotaAnticipoService cuotaAnticipoService;
    @Inject
    private CuentaContableService cuentacontableService;
    @Inject
    private UserSession userSession;
    @Inject
    private ParametrizableService parametroService;
    @Inject
    private ClienteService clienteService;
    @Inject
    private TramiteRequisitoHistorialService tramiteRequisitoHistorialService;
    @Inject
    private ProcesoService tramiteServiceu;
    @Inject
    private ComprobantePagoService comprobantePagoService;

    private List<CuentaContable> cuentasContables;
    private short periodo;
    private LazyModel<AnticipoRemuneracion> lazy;
    private LazyModel<CuotaAnticipo> lazyCuotas;
    private List<AnticipoRemuneracion> listaAnticipo;
    private AnticipoRemuneracion anticipoRemuneracion;
    private AnticipoRemuneracion anticipoRemuneracionAdic;
    private CuotaAnticipo cuota;
    private CuentaContable cuentaContable;
    private BigDecimal sueldo;
    private BigDecimal tipoC;
    private BigDecimal cero;
    private BigDecimal uno;
    private BigDecimal menosUno;
    private BigDecimal totalPagado;
    private BigDecimal saldoPagar;
    private BigDecimal porcentaje;
    private BigDecimal cDec;
    private boolean collapsed;
    private boolean diciembreBoolean;
    private boolean variableNunCuota;
    private boolean ingresoForm;
    private Short cuostaAnteriore;
    private List<CuotaAnticipo> listaCuota;
    private CatalogoItem registrado, deuda, pagado, autorizado, incompleto, rechazado, completo, aprobado;
    private List<CatalogoItem> estadoList;
    private ParametrosTalentoHumano valoPrametro;
    private List<UploadedFile> files;
    private UploadedFile file;
    private boolean rendered;
    private BigInteger numTramite;
    private String montoAnticipo;
    private ListarequisitosModel requisitosObjeto;
    private List<ListarequisitosModel> requisitosTramite;
    private int aprobadoCodigo;
    private String observacionDlg;
    private AnticipoRemuneracion anticipoAuxiliar;
    private LazyModel<Servidor> servidorLazy;

    @PostConstruct
    public void init() {
        if (this.session.getTaskID() != null) {
            if (!JsfUtil.isAjaxRequest()) {
                this.setTaskId(this.session.getTaskID());
                observacion = new Observaciones();
                numTramite = new BigInteger("" + tramite.getNumTramite());
            }
        }
        estadoList = new ArrayList<>();
        valoPrametro = parametroService.valorParametroTipo("ANT_RMU");
        registrado = anticipoRemunercionService.getEstadoAnticipo("EST_ANTI", (short) 1);
        deuda = anticipoRemunercionService.getEstadoAnticipo("EST_ANTI", (short) 2);
        pagado = anticipoRemunercionService.getEstadoAnticipo("EST_ANTI", (short) 3);
        incompleto = anticipoRemunercionService.getEstadoAnticipo("EST_ANTI", (short) 4);
        rechazado = anticipoRemunercionService.getEstadoAnticipo("EST_ANTI", (short) 5);
        autorizado = anticipoRemunercionService.getEstadoAnticipo("EST_ANTI", (short) 6);
        completo = anticipoRemunercionService.getEstadoAnticipo("EST_ANTI", (short) 7);
        aprobado = anticipoRemunercionService.getEstadoAnticipo("EST_ANTI", (short) 8);
        totalPagado = BigDecimal.ZERO;
        saldoPagar = BigDecimal.ZERO;
        cDec = BigDecimal.ZERO;
        tipoC = new BigDecimal(3);
        porcentaje = new BigDecimal(0.70);
        cero = new BigDecimal(0.00);
        uno = new BigDecimal(1);
        menosUno = new BigDecimal(-1);
        listaCuota = new ArrayList<>();
        collapsed = Boolean.TRUE;
        variableNunCuota = Boolean.FALSE;
        anticipoRemuneracion = new AnticipoRemuneracion();
        anticipoRemuneracionAdic = new AnticipoRemuneracion();
        calcularnumeroCuota();
        anticipoRemuneracion.setValorDiciembre(cero);
        anticipoRemuneracion.setServidor(new Servidor());
        anticipoRemuneracion.getServidor().setPersona(new Cliente());
        anticipoRemuneracion.setDistributivo(new Distributivo());
        anticipoRemuneracion.setValorParametrizado(new ParametrosTalentoHumano());
        lazy = new LazyModel<>(AnticipoRemuneracion.class);
        lazy.getFilterss().put("estado", true);
        lazy.setDistinct(false);
        if (this.session.getTaskID() != null) {
            lazy.getFilterss().put("numTramite", numTramite);
            switch (tarea.getTaskDefinitionKey()) {
                case "ingresoSolicitud":
                    estadoList.add(registrado);
                    estadoList.add(incompleto);
                    lazy.getFilterss().put("estadoAnticipo", estadoList);
                    break;
                case "autoSolicitud":
                    lazy.getFilterss().put("estadoAnticipo", completo);
                    break;
                case "infoTecnico":
                    estadoList.add(registrado);
                    estadoList.add(incompleto);
                    lazy.getFilterss().put("estadoAnticipo", estadoList);
                    break;
                case "controlPrevio":
                    estadoList.add(incompleto);
                    estadoList.add(aprobado);
                    estadoList.add(registrado);
                    lazy.getFilterss().put("estadoAnticipo", estadoList);
                    break;
                case "autPago":
                    estadoList.add(aprobado);
                    estadoList.add(completo);
                    estadoList.add(autorizado);
                    lazy.getFilterss().put("estadoAnticipo", estadoList);
                    break;
            }
            rendered = true;
        } else {
            rendered = false;
        }
        lazy.getSorteds().put("servidor.persona.apellido", "ASC");
        periodo = Utils.getAnio(new Date()).shortValue();
        lazy.getFilterss().put("periodo", periodo);
        diciembreBoolean = Boolean.TRUE;
        cuentasContables = cuentacontableService.getCuentasAnticipo(periodo);
        listaAnticipo = anticipoRemunercionService.getLista(periodo);
        cuota = new CuotaAnticipo();
        cuota.setAnticipoRemuneracion(new AnticipoRemuneracion());
        listRequisitos();
        if (anticipoRemunercionService.findAnticipoByNTramite(numTramite) != null) {
            ingresoForm = true;
        } else {
            ingresoForm = false;
        }
    }

    private void listRequisitos() {
        List<ProcedimientoRequisito> listaRequisitos = tramiteServiceu.getListaRequisito(tramite.getTipoTramite().getId());
        if (listaRequisitos != null) {
            requisitosTramite = new ArrayList<>(listaRequisitos.size() + 1);
            for (ProcedimientoRequisito tipoTramite : listaRequisitos) {
                ListarequisitosModel req = new ListarequisitosModel();
                req.setRequisitos(tipoTramite);
                requisitosTramite.add(req);
            }
        }
    }

    public void openDlgObservacion() {
        PrimeFaces.current().executeScript("PF('dlgObservaciones').show()");
        PrimeFaces.current().ajax().update(":frmDlgObser");
    }

    public void completarTarea(AnticipoRemuneracion anticipo) {
//        getParamts().put("usuarioAutoriza", session.getUsuario().getFuncionario().getDistributivo().getUnidadAdministrativa().getCodigo()== "");
        getParamts().put("usuarioTTHH", clienteService.getrolsUser(RolUsuario.titularTH));
        getParamts().put("idServidor", session.getUserId());
        super.completeTask(this.session.getTaskID(), (HashMap<String, Object>) getParamts());
        JsfUtil.redirect(CONFIG.URL_APP + "procesos/bandeja-tareas");
    }

    public void completarTareaAutoriza(int aprobado) {
        try {
            if (aprobado == 1) {
                anticipoRemuneracion.setEstadoAnticipo(this.aprobado);
                getParamts().put("usuarioDir", clienteService.getrolsUser(RolUsuario.directorFinanciero));
            } else {
                anticipoRemuneracion.setEstadoAnticipo(rechazado);
            }
            anticipoRemunercionService.edit(anticipoRemuneracion);
            getParamts().put("aprobado", aprobado);
            if (saveTramite() == null) {
                return;
            }
            super.completeTask(this.session.getTaskID(), (HashMap<String, Object>) getParamts());
            JsfUtil.redirect(CONFIG.URL_APP + "procesos/bandeja-tareas");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Select ", e);
        }
    }

    public void openDlgObservacion(int aux, AnticipoRemuneracion ant) {
        aprobadoCodigo = aux;
        observacionDlg = "";
        anticipoAuxiliar = ant;
        PrimeFaces.current().executeScript("PF('dlgObservaciones').show()");
    }

    public void completarTarea() {
        try {
            getParamts().put("aprobado", aprobadoCodigo);
            observacion.setObservacion(observacionDlg);
            observacion.setEstado(true);
            observacion.setFecCre(new Date());
            observacion.setTarea(tarea.getName());
            observacion.setUserCre(session.getName());
            switch (aprobadoCodigo) {
                case 1:
                    getParamts().put("usuarioCtl", clienteService.getrolsUser(RolUsuario.contador));
                    anticipoAuxiliar.setEstadoAnticipo(aprobado);
                    anticipoRemunercionService.edit(anticipoAuxiliar);
                    break;
                case 0:
                    anticipoAuxiliar.setEstadoAnticipo(rechazado);
                    anticipoRemunercionService.edit(anticipoAuxiliar);
                    break;
            }
            if (saveTramite() == null) {
                return;
            }
            super.completeTask(this.session.getTaskID(), (HashMap<String, Object>) getParamts());
            PrimeFaces.current().executeScript("PF('dlgObservaciones').hide()");
            JsfUtil.redirect(CONFIG.URL_APP + "procesos/bandeja-tareas");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "completas tareas", e);
        }
    }

    public void completarTareaControl(int aprobar) throws Exception {
        if (aprobar == 1) {
            anticipoRemuneracion.setEstadoAnticipo(completo);
            getParamts().put("usuarioMax", clienteService.getrolsUser(RolUsuario.maximaAutoridad));
        } else {
            anticipoRemuneracion.setEstadoAnticipo(incompleto);
            getParamts().put("usuarioTTHH", clienteService.getrolsUser(RolUsuario.titularTH));
        }
        anticipoRemunercionService.edit(anticipoRemuneracion);
        getParamts().put("aprobado", aprobar);
        if (saveTramite() == null) {
            return;
        }
        super.completeTask(this.session.getTaskID(), (HashMap<String, Object>) getParamts());
        JsfUtil.redirect(CONFIG.URL_APP + "procesos/bandeja-tareas");
    }

    public void tareaAutorizaPago(AnticipoRemuneracion a) {
        a.setEstadoAnticipo(autorizado);
        anticipoRemunercionService.edit(a);
        getParamts().put("usuarioCon", clienteService.getrolsUser(RolUsuario.contador));
        super.completeTask(this.session.getTaskID(), (HashMap<String, Object>) getParamts());
        JsfUtil.redirect(CONFIG.URL_APP + "procesos/bandeja-tareas");
    }

    public boolean validarAnticipoServidor() {
        listaAnticipo = anticipoRemunercionService.getLista(periodo);
        for (AnticipoRemuneracion a : listaAnticipo) {
            if (anticipoRemuneracion.getServidor().equals(a.getServidor())) {
                if (!a.getEstadoAnticipo().equals(rechazado)) {
                    if (!cuotaAnticipoService.getCuotasDeuda(a)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void guardar() throws ParseException {
        boolean edit = anticipoRemuneracion.getId() != null;
        //validaciones
        if (anticipoRemuneracion.getValorDiciembre().shortValue() == 0) {
            anticipoRemuneracion.setValorDiciembre(cero);
            if (diciembreBoolean) {
                anticipoRemuneracion.setValorDiciembre(cero);
            }
        }
        if ((anticipoRemuneracion.getFechaAnticipo() == null) || (anticipoRemuneracion.getFechaAnticipo().toString().equals(""))) {
            JsfUtil.addWarningMessage("Anticipo Remuneración", "Favor ingrese fecha para proceder");
            return;
        } else if ((anticipoRemuneracion.getDescripcion() == null) || (anticipoRemuneracion.getDescripcion().equals(""))) {
            JsfUtil.addWarningMessage("Anticipo Remuneración", "Favor ingrese descripción  para proceder");
            return;
        }
        //fin validaciones
        if (edit) {
            anticipoRemuneracion.setFechaModificacion(new Date());
            anticipoRemuneracion.setUsuarioModifica(userSession.getNameUser());
            anticipoRemunercionService.edit(anticipoRemuneracion);
            eliminarCuota(anticipoRemuneracion);
            generarCuotas();
        } else {
            anticipoRemuneracion.setFechaCreacion(new Date());
            anticipoRemuneracion.setUsuarioCreacion(userSession.getNameUser());
            anticipoRemuneracion.setPeriodo(periodo);
            anticipoRemuneracion.setNumTramite(numTramite);
            anticipoRemuneracion.setValorParametrizado(valoPrametro);
            anticipoRemuneracion.setEstadoAnticipo(registrado);
            anticipoRemuneracion = anticipoRemunercionService.create(anticipoRemuneracion);
            generarCuotas();
        }
        anticipoRemuneracion = new AnticipoRemuneracion();
        collapsed = Boolean.TRUE;
        montoAnticipo = "";
        cancelar();
        JsfUtil.addInformationMessage("Anticipo de Remuneración", "Información " + (edit ? "Editados" : "Registrados") + " Correctamente");
        ingresoForm = true;
        PrimeFaces.current().ajax().update("formMain");
    }

    public void buscarServidor() {
        Utils.openDialog("/facelet/talentoHumano/dialogServidor", "840", "400");
    }

    public void selectData(SelectEvent evt) {
        cancelar();
        anticipoRemuneracion.setServidor((Servidor) evt.getObject());
        if (validarAnticipoServidor()) {
            JsfUtil.addWarningMessage("Anticipo Remuneración", "El Servidor ya cuenta con un Anticipo en proceso.");
            cancelar();
            return;
        }
        if (anticipoRemuneracion.getServidor() != null) {
            anticipoRemuneracion.setSueldoServidor(anticipoRemunercionService.sueldoServidor(periodo, anticipoRemuneracion.getServidor().getPersona()));
            anticipoRemuneracion.setDistributivo(anticipoRemunercionService.getDistributivo(periodo, anticipoRemuneracion.getServidor()));
        }
    }

    private void calcularnumeroCuota() {
        int meses = 12;
        anticipoRemuneracion.setNumeroCuota((short) (meses - Utils.getMes(new Date())));
    }

    public Boolean validarCuotas() {
        AnticipoRemuneracion antici = new AnticipoRemuneracion();
        List<CuotaAnticipo> cuotaList = new ArrayList<>();
        listaAnticipo = anticipoRemunercionService.getLista(periodo);
        for (AnticipoRemuneracion a : listaAnticipo) {
            if (anticipoRemuneracion.getServidor().equals(a.getServidor())) {
                if (a.getEstadoAnticipo().equals(pagado)) {
                    antici = a;
                    break;
                }
            }
        }
        cuotaList = cuotaAnticipoService.getCuotaList(antici);
        int cuotasPaga = 0;
        if (!cuotaList.isEmpty()) {
            for (CuotaAnticipo c : cuotaList) {
                if (c.getEstadoCuota() == true) {

                }
            }
        }
        return false;
    }

    public static Date formatearCalendar(Calendar c) throws ParseException {
        DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat formato = new SimpleDateFormat("yyyy/MM/dd");
        String date = df.format(c.getTime());
        return formato.parse(date);
    }

    public void generarCuotas() throws ParseException {
        BigDecimal cuotaspagar, cuotaDec, aux, menos;
        menos = new BigDecimal(1);
        int mes;
        BigDecimal numCuota;
        if (anticipoRemuneracion.getValorDiciembre().shortValue() > 0) {
            cuotaDec = anticipoRemuneracion.getValorDiciembre();
            aux = anticipoRemuneracion.getMontoAnticipo().subtract(cuotaDec);
            Calendar fechaReg = Calendar.getInstance();
            fechaReg.setTime(anticipoRemuneracion.getFechaAnticipo());
            if (anticipoRemuneracion.getAnticipoExistente()) {
                numCuota = new BigDecimal(getCuostaAnteriore());
                int auxCuotaAnt = getCuostaAnteriore().intValue() - anticipoRemuneracion.getNumeroCuota().intValue();
                fechaReg.add(Calendar.MONTH, auxCuotaAnt);
                BigDecimal resta = numCuota.subtract(menos);
                cuotaspagar = BigDecimal.valueOf(aux.doubleValue() / resta.doubleValue()).setScale(2, BigDecimal.ROUND_HALF_EVEN);
            } else {
                numCuota = new BigDecimal(anticipoRemuneracion.getNumeroCuota());
                BigDecimal resta = numCuota.subtract(menos);
                cuotaspagar = BigDecimal.valueOf(aux.doubleValue() / resta.doubleValue()).setScale(2, BigDecimal.ROUND_HALF_EVEN);
            }
            for (int i = 0; i < anticipoRemuneracion.getNumeroCuota().intValue(); i++) {
                fechaReg.set(Calendar.DAY_OF_MONTH, fechaReg.getActualMaximum(Calendar.DAY_OF_MONTH));
                mes = fechaReg.get(Calendar.MONTH);
                cuota.setFechaCuota(formatearCalendar(fechaReg));
                cuota.setAnticipoRemuneracion(anticipoRemuneracion);
                cuota.setMes(getmesString(mes + 1));
                cuota.setCuota((short) (i + 1));
                if (mes == 11) {
                    cuota.setValorCuota(cuotaDec);
                } else {
                    cuota.setValorCuota(cuotaspagar);
                }
                //listaCuota.add(cuota);
                cuota = cuotaAnticipoService.create(cuota);
                cuota = new CuotaAnticipo();
                fechaReg.add(Calendar.MONTH, 1);
            }
        } else {
            aux = anticipoRemuneracion.getMontoAnticipo();
            Calendar fechaReg = Calendar.getInstance();
            fechaReg.setTime(anticipoRemuneracion.getFechaAnticipo());
            if (anticipoRemuneracion.getAnticipoExistente()) {
                numCuota = new BigDecimal(getCuostaAnteriore());
                int auxCuotaAnt = getCuostaAnteriore().intValue() - anticipoRemuneracion.getNumeroCuota().intValue();
                fechaReg.add(Calendar.MONTH, auxCuotaAnt);
                BigDecimal resta = numCuota.subtract(menos);
                cuotaspagar = BigDecimal.valueOf(aux.doubleValue() / resta.doubleValue()).setScale(2, BigDecimal.ROUND_HALF_EVEN);
            } else {
                numCuota = new BigDecimal(anticipoRemuneracion.getNumeroCuota());
                BigDecimal resta = numCuota;
                cuotaspagar = BigDecimal.valueOf(aux.doubleValue() / resta.doubleValue()).setScale(2, BigDecimal.ROUND_HALF_EVEN);
            }
            for (int i = 0; i < anticipoRemuneracion.getNumeroCuota().intValue(); i++) {
                fechaReg.set(Calendar.DAY_OF_MONTH, fechaReg.getActualMaximum(Calendar.DAY_OF_MONTH));
                mes = fechaReg.get(Calendar.MONTH);
                cuota.setFechaCuota(formatearCalendar(fechaReg));
                cuota.setAnticipoRemuneracion(anticipoRemuneracion);
                cuota.setMes(getmesString(mes + 1));
                cuota.setCuota((short) (i + 1));
                cuota.setValorCuota(cuotaspagar);
                cuota = cuotaAnticipoService.create(cuota);
                cuota = new CuotaAnticipo();
                fechaReg.add(Calendar.MONTH, 1);
            }

        }
        //sumar todas las cuotas y dejar igual valor al prestamo realizado
        listaCuota = cuotaAnticipoService.getCuotaList(anticipoRemuneracion);
        Integer aa = listaCuota.size();
        BigDecimal sumaTotal = BigDecimal.ZERO;
        BigDecimal diferencia = BigDecimal.ZERO;
        for (int i = 0; i < aa; i++) {
            sumaTotal = sumaTotal.add(listaCuota.get(i).getValorCuota());
        }
        diferencia = sumaTotal.subtract(anticipoRemuneracion.getMontoAnticipo());
        //diferencia mandar a la ultima cuota si la ultima cuota es diciembre a la penultima
        if ((diferencia.compareTo(cero)) > 0) {
            if (listaCuota.get(0).getMes().equals("Diciembre")) {
                listaCuota.get(1).setValorCuota(listaCuota.get(1).getValorCuota().subtract(diferencia));
                cuotaAnticipoService.edit(listaCuota.get(1));
            } else {
                listaCuota.get(0).setValorCuota(listaCuota.get(0).getValorCuota().subtract(diferencia));
                cuotaAnticipoService.edit(listaCuota.get(0));
            }
        } else if (diferencia.compareTo(cero) < 0) {
            if (listaCuota.get(0).getMes().equals("Diciembre")) {
                listaCuota.get(1).setValorCuota(listaCuota.get(1).getValorCuota().add(diferencia.multiply(menosUno)));
                cuotaAnticipoService.edit(listaCuota.get(1));
            } else {
                listaCuota.get(0).setValorCuota(listaCuota.get(0).getValorCuota().add(diferencia.multiply(menosUno)));
                cuotaAnticipoService.edit(listaCuota.get(0));
            }
        }
    }

    public void requisitoDucumento() {
        PrimeFaces.current().executeScript("PF('requisitoDialog').show()");
    }

    public void adjuntarDucumento() {
        PrimeFaces.current().executeScript("PF('requisitoDialog').show()");
    }

    public void handleFileUploadInformeTec(FileUploadEvent event) {
        try {
            if (files == null) {
                files = new ArrayList<>();
            }
            file = event.getFile();
            files.add(file);
            addArchivo();
        } catch (Exception e) {
            System.out.println("error al subir el archivo " + e);
        }
    }

    public void addArchivo() {
        if (files != null) {
            uploadDoc.upload(this.tramite, files);
            anticipoRemuneracion = new AnticipoRemuneracion();
            PrimeFaces.current().executeScript("PF('requisitoDialog').hide()");
            PrimeFaces.current().ajax().update("formMain");
            JsfUtil.addInformationMessage("Información", "Su archivo se subio exitosamente");
        }
    }

    public void dlogoObservaciones(AnticipoRemuneracion ant) {
        try {
            anticipoRemuneracion = ant;
            observacion.setEstado(true);
            observacion.setFecCre(new Date());
            observacion.setTarea(tarea.getName());
            observacion.setUserCre(session.getName());
            PrimeFaces.current().executeScript("PF('dlgObservaciones').show()");
            PrimeFaces.current().ajax().update(":frmDlgObser");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Select ", e);
        }
    }

    public void addReqTramite(ListarequisitosModel requisito) {
        requisitosObjeto = requisito;
    }

    public void handleFileUploadCertificadoGerente(FileUploadEvent event) {
        try {
            uploadDoc.upload(this.tramite, Arrays.asList(event.getFile()));
            if (requisitosObjeto != null) {
                TipoTramiteRequisitoHistorial objeto = new TipoTramiteRequisitoHistorial();
                objeto.setTipoTramite(this.tramite.getTipoTramite());
                objeto.setProcedimientoRequisito(requisitosObjeto.getRequisitos());
                tramiteRequisitoHistorialService.edit(objeto);
                requisitosObjeto = null;
            }
            JsfUtil.addInformationMessage("Información", "El archvio se subió correctamente");
        } catch (Exception e) {
            JsfUtil.addErrorMessage("Archivo", "Ocurrió un error al subir archivo.");
            LOG.log(Level.SEVERE, "Error al subir archivo", e);
        }
    }

    public void handleFileUpload(FileUploadEvent event) {
        try {
            if (files == null) {
                files = new ArrayList<>();
            }
            file = event.getFile();
            files.add(file);
            if (files != null) {
                uploadDoc.upload(this.tramite, files);
                JsfUtil.addInformationMessage("Requisitos", "Datos almacenados correctamente");
            }
            files = null;
            PrimeFaces.current().executeScript("PF('requisitoDialog').hide()");
            PrimeFaces.current().ajax().update("requisitoDialogForm");
            PrimeFaces.current().ajax().update("formMain:idDocumentos:dtArchivosTramites");
            JsfUtil.addInformationMessage("Información", "Su archivo se subio exitosamente");
        } catch (Exception e) {
            System.out.println("error al subir el archivo " + e);
        }
    }

    public void generaMontoAnticipo() {
        if (anticipoRemuneracion.getSueldoServidor() != null) {
            switch (montoAnticipo) {
                case "1SB":
                    anticipoRemuneracion.setMontoAnticipo(anticipoRemuneracion.getSueldoServidor().multiply(new BigDecimal(1)));
                    break;
                case "2SB":
                    anticipoRemuneracion.setMontoAnticipo(anticipoRemuneracion.getSueldoServidor().multiply(new BigDecimal(2)));
                    break;
                case "3SB":
                    anticipoRemuneracion.setMontoAnticipo(anticipoRemuneracion.getSueldoServidor().multiply(new BigDecimal(3)));
                    break;
            }
            validarRMUnuevo();
        } else {
            montoAnticipo = "";
        }
    }

    public void validarRMUnuevo() {
        BigDecimal antiRMU = anticipoRemuneracion.getSueldoServidor().multiply(tipoC);
        if ((anticipoRemuneracion.getMontoAnticipo() == null) || (anticipoRemuneracion.getMontoAnticipo().toString().equals(""))) {
            JsfUtil.addWarningMessage("Anticipo Remuneración", "Monto no valido ingrese un valor para proceder");
            return;
        }
        if (anticipoRemuneracion.getMontoAnticipo().doubleValue() > antiRMU.doubleValue()) {
            JsfUtil.addWarningMessage("Anticipo Remuneración", "El monto solicitado supera los 3 RMU");
            PrimeFaces.current().focus("formMain:monto");
            PrimeFaces.current().executeScript("document.getElementById('formMain:monto').select();");
        }
        // para ver si las cuotas son hasta dos meses o doce meses
        if (anticipoRemuneracion.getMontoAnticipo().compareTo(anticipoRemuneracion.getSueldoServidor()) == (-1)) {
            variableNunCuota = Boolean.TRUE;
        } else {
            variableNunCuota = Boolean.FALSE;
        }
        calcularnumeroCuota();
        PrimeFaces.current().ajax().update("formMain:cuota");
        anticipoRemuneracion.setValorDiciembre(cero);
        diciembreBoolean = Boolean.TRUE;
        PrimeFaces.current().ajax().update("formMain:valorDic");
    }

    public void validarDecDisabled() {
        int mes, condicion = 0;
        Calendar fechaReg = Calendar.getInstance();
        if (anticipoRemuneracion.getFechaAnticipo() != null) {  //para usar el mismo meotodo tanto para nuevo y editar
            fechaReg.setTime(anticipoRemuneracion.getFechaAnticipo());
        } else {
            fechaReg.setTime(new Date());
        }
        for (int i = 0; i < anticipoRemuneracion.getNumeroCuota().intValue(); i++) {
            fechaReg.add(Calendar.MONTH, 1);
            mes = fechaReg.get(Calendar.MONTH);
            if (mes == 11) {
                condicion = condicion + 1;
            }
        }
        if (condicion > 0) {
            diciembreBoolean = Boolean.FALSE;
        } else {
            diciembreBoolean = Boolean.TRUE;
            anticipoRemuneracion.setValorDiciembre(cero);
        }
        if ((Utils.getMes(new Date()) == 11) || (Utils.getMes(new Date()) == 12)) {
            diciembreBoolean = Boolean.TRUE;
        }
        PrimeFaces.current().ajax().update("formMain:valorDic");
    }

    public void validarDecMenor70RMUnuevo() {
        if (anticipoRemuneracion.getValorDiciembre() == null) {
            JsfUtil.addWarningMessage("Anticipo Remuneración", "Valor no válido, favor ingrese un valor");
            PrimeFaces.current().focus("formMain:valorDic");
            return;
        }
        BigDecimal nuCuota = new BigDecimal(anticipoRemuneracion.getNumeroCuota());
        BigDecimal valCuota = BigDecimal.valueOf(anticipoRemuneracion.getMontoAnticipo().doubleValue() / nuCuota.doubleValue()).setScale(2, BigDecimal.ROUND_HALF_EVEN);
        BigDecimal menor70RMU = anticipoRemuneracion.getSueldoServidor().multiply(porcentaje);
        if (anticipoRemuneracion.getValorDiciembre().doubleValue() > menor70RMU.doubleValue()) {
            JsfUtil.addWarningMessage("Anticipo Remuneración", "Valor de Diciembre debe ser menor al 70% de RMU del servidor");
            PrimeFaces.current().focus("formMain:valorDic");
            PrimeFaces.current().executeScript("document.getElementById('formMain:valorDic').select();");
        } else if ((anticipoRemuneracion.getValorDiciembre().compareTo(cero) == 1)
                && (anticipoRemuneracion.getValorDiciembre().compareTo(valCuota) == (-1))) {
            JsfUtil.addWarningMessage("Anticipo Remuneración", "Valor Diciembre no debe ser menor a valor de cuota: $ " + valCuota + " , dejar en $0"
                    + " si desea valor de cuota o ingrese otro valor");
            PrimeFaces.current().focus("formMain:valorDic");
            PrimeFaces.current().executeScript("document.getElementById('formMain:valorDic').select();");
        } else if ((anticipoRemuneracion.getValorDiciembre().compareTo(cero) == -1)) {
            JsfUtil.addWarningMessage("Anticipo Remuneración", "Valor Diciembre no puede ser valor negativo, corrija");
            PrimeFaces.current().focus("formMain:valorDic");
            PrimeFaces.current().executeScript("document.getElementById('formMain:valorDic').select();");
        }
    }

    public String getmesString(int mes) {
        String mesString;
        switch (mes) {
            case 1:
                mesString = "Enero";
                break;
            case 2:
                mesString = "Febrero";
                break;
            case 3:
                mesString = "Marzo";
                break;
            case 4:
                mesString = "Abril";
                break;
            case 5:
                mesString = "Mayo";
                break;
            case 6:
                mesString = "Junio";
                break;
            case 7:
                mesString = "Julio";
                break;
            case 8:
                mesString = "Agosto";
                break;
            case 9:
                mesString = "Septiembre";
                break;
            case 10:
                mesString = "Octubre";
                break;
            case 11:
                mesString = "Noviembre";
                break;
            case 12:
                mesString = "Diciembre";
                break;
            default:
                mesString = "Invalid month";
                break;
        }
        return mesString;
    }

    public void editar(AnticipoRemuneracion anticipo) {
        this.anticipoRemuneracion = anticipo;
        this.collapsed = Boolean.FALSE;
        this.ingresoForm = Boolean.FALSE;
        if (anticipoRemuneracion.getMontoAnticipo().compareTo(anticipoRemuneracion.getSueldoServidor()) == (-1)) {
            variableNunCuota = Boolean.TRUE;
        } else {
            variableNunCuota = Boolean.FALSE;
        }
        validarDecDisabled();
    }

    public void eliminar(AnticipoRemuneracion anticipo) {
        if (anticipo.getEstadoAnticipo().equals(rechazado) || anticipo.getEstadoAnticipo().equals(registrado)) {
            anticipo.setEstado(false);
            eliminarCuota(anticipo);
            anticipoRemunercionService.edit(anticipo);
            JsfUtil.addInformationMessage("Anticipo de Remuneración", "Anticipo eliminado con éxito");
        } else {
            JsfUtil.addWarningMessage("Anticipo de Remuneración", "No puede eliminar Anticipo ");
        }
        listaCuota = new ArrayList<>();
    }

    public void eliminarCuota(AnticipoRemuneracion anticipo) {
        int executeUpdate = cuotaAnticipoService.deleteCuotas(anticipo);
    }

    public void openDialogAmortizacion(AnticipoRemuneracion anticipo) {
        //sumar total pagado ; saldo a pagar
        listaCuota = cuotaAnticipoService.getCuotaList(anticipo);
        Integer aa = listaCuota.size();
        BigDecimal sumaTotal = BigDecimal.ZERO;
        for (int i = 0; i < aa; i++) {
            if (listaCuota.get(i).getEstadoCuota()) {
                sumaTotal = sumaTotal.add(listaCuota.get(i).getValorCuota());
            }
        }
        totalPagado = sumaTotal;
        saldoPagar = (anticipo.getMontoAnticipo()).subtract(totalPagado);
        //llenar datatable de amortizacion
        lazyCuotas = new LazyModel<>(CuotaAnticipo.class);
        lazyCuotas.setDistinct(false);
        lazyCuotas.getFilterss().put("anticipoRemuneracion", anticipo);
        anticipoRemuneracionAdic = anticipo;
        PrimeFaces.current().executeScript("PF('dlgAnt').show()");
        PrimeFaces.current().ajax().update("frmAnt");
    }

    public void cancelar() {
        anticipoRemuneracion = new AnticipoRemuneracion();
        calcularnumeroCuota();
        anticipoRemuneracion.setValorDiciembre(cero);
        anticipoRemuneracion.setServidor(new Servidor());
        anticipoRemuneracion.setDistributivo(new Distributivo());
        anticipoRemuneracion.getServidor().setPersona(new Cliente());
        variableNunCuota = Boolean.FALSE;
        diciembreBoolean = Boolean.TRUE;
        file = null;
        setCuostaAnteriore(null);
    }

    //<editor-fold defaultstate="collapsed" desc="getter and setter">
    public String getObservacionDlg() {
        return observacionDlg;
    }

    public void setObservacionDlg(String observacionDlg) {
        this.observacionDlg = observacionDlg;
    }

    public boolean isIngresoForm() {
        return ingresoForm;
    }

    public void setIngresoForm(boolean ingresoForm) {
        this.ingresoForm = ingresoForm;
    }

    public List<ListarequisitosModel> getRequisitosTramite() {
        return requisitosTramite;
    }

    public void setRequisitosTramite(List<ListarequisitosModel> requisitosTramite) {
        this.requisitosTramite = requisitosTramite;
    }

    public String getMontoAnticipo() {
        return montoAnticipo;
    }

    public void setMontoAnticipo(String montoAnticipo) {
        this.montoAnticipo = montoAnticipo;
    }

    public List<CuentaContable> getCuentasContables() {
        return cuentasContables;
    }

    public void setCuentasContables(List<CuentaContable> cuentasContables) {
        this.cuentasContables = cuentasContables;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public boolean isRendered() {
        return rendered;
    }

    public void setRendered(boolean rendered) {
        this.rendered = rendered;
    }

    public short getPeriodo() {
        return periodo;
    }

    public void setPeriodo(short periodo) {
        this.periodo = periodo;
    }

    public LazyModel<AnticipoRemuneracion> getLazy() {
        return lazy;
    }

    public void setLazy(LazyModel<AnticipoRemuneracion> lazy) {
        this.lazy = lazy;
    }

    public AnticipoRemuneracion getAnticipoRemuneracion() {
        return anticipoRemuneracion;
    }

    public void setAnticipoRemuneracion(AnticipoRemuneracion anticipoRemuneracion) {
        this.anticipoRemuneracion = anticipoRemuneracion;
    }

    public CuentaContable getCuentaContable() {
        return cuentaContable;
    }

    public void setCuentaContable(CuentaContable cuentaContable) {
        this.cuentaContable = cuentaContable;
    }

    public boolean isCollapsed() {
        return collapsed;
    }

    public void setCollapsed(boolean collapsed) {
        this.collapsed = collapsed;
    }

    public BigDecimal getSueldo() {
        return sueldo;
    }

    public void setSueldo(BigDecimal sueldo) {
        this.sueldo = sueldo;
    }

    public CuotaAnticipo getCuota() {
        return cuota;
    }

    public void setCuota(CuotaAnticipo cuota) {
        this.cuota = cuota;
    }

    public BigDecimal getTipoC() {
        return tipoC;
    }

    public void setTipoC(BigDecimal tipoC) {
        this.tipoC = tipoC;
    }

    public BigDecimal getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(BigDecimal porcentaje) {
        this.porcentaje = porcentaje;
    }

    public Short getCuostaAnteriore() {
        return cuostaAnteriore;
    }

    public void setCuostaAnteriore(Short cuostaAnteriore) {
        this.cuostaAnteriore = cuostaAnteriore;
    }

    public AnticipoRemuneracion getAnticipoRemuneracionAdic() {
        return anticipoRemuneracionAdic;
    }

    public void setAnticipoRemuneracionAdic(AnticipoRemuneracion anticipoRemuneracionAdic) {
        this.anticipoRemuneracionAdic = anticipoRemuneracionAdic;
    }

    public LazyModel<CuotaAnticipo> getLazyCuotas() {
        return lazyCuotas;
    }

    public void setLazyCuotas(LazyModel<CuotaAnticipo> lazyCuotas) {
        this.lazyCuotas = lazyCuotas;
    }

    public BigDecimal getTotalPagado() {
        return totalPagado;
    }

    public BigDecimal getMenosUno() {
        return menosUno;
    }

    public void setMenosUno(BigDecimal menosUno) {
        this.menosUno = menosUno;
    }

    public BigDecimal getCero() {
        return cero;
    }

    public BigDecimal getcDec() {
        return cDec;
    }

    public void setcDec(BigDecimal cDec) {
        this.cDec = cDec;
    }

    public void setCero(BigDecimal cero) {
        this.cero = cero;
    }

    public void setTotalPagado(BigDecimal totalPagado) {
        this.totalPagado = totalPagado;
    }

    public BigDecimal getSaldoPagar() {
        return saldoPagar;
    }

    public void setSaldoPagar(BigDecimal saldoPagar) {
        this.saldoPagar = saldoPagar;
    }

    public boolean isDiciembreBoolean() {
        return diciembreBoolean;
    }

    public void setDiciembreBoolean(boolean diciembreBoolean) {
        this.diciembreBoolean = diciembreBoolean;
    }

    public boolean isVariableNunCuota() {
        return variableNunCuota;
    }

    public void setVariableNunCuota(boolean variableNunCuota) {
        this.variableNunCuota = variableNunCuota;
    }

//</editor-fold>
}
