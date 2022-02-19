/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ventanilla.Controller;

import com.asgard.Entity.FinaRenLiquidacion;
import com.asgard.Entity.FinaRenPagoRubro;
import com.asgard.Entity.FinaRenTipoLiquidacion;
import com.gestionTributaria.Commons.SisVars;
import com.gestionTributaria.Entities.CatPredio;
import com.gestionTributaria.Entities.FnSolicitudExoneracion;
import com.gestionTributaria.Recaudacion.RecaudacionInteface;
import com.gestionTributaria.Services.*;
import com.google.gson.Gson;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.config.RolUsuario;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.entities.TipoTramiteRequisitoHistorial;
import com.origami.sigef.common.entities.UnidadAdministrativa;
import com.origami.sigef.common.entities.UsuarioRol;
import com.origami.sigef.common.entities.Usuarios;
import com.origami.sigef.common.models.Correo;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.interfaces.FileUploadDoc;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.procesos.Model.ListarequisitosModel;
import com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot;
import static com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot.LOG;
import com.origami.sigef.resource.procesos.entities.HistoricoTramites;
import com.origami.sigef.resource.procesos.entities.Observaciones;
import com.origami.sigef.resource.procesos.entities.TipoTramite;
import com.origami.sigef.resource.procesos.services.TramiteRequisitoHistorialService;
import com.origami.sigef.resource.talento_humano.entities.Servidor;
import com.ventanilla.Entity.PredioAutorizar;
import com.ventanilla.Entity.RegistroSolicitudRequisitos;
import com.ventanilla.Entity.Servicio;
import com.ventanilla.Entity.ServicioRequisito;
import com.ventanilla.Entity.ServicioTipo;
import com.ventanilla.Entity.SolicitudDepartamento;
import com.ventanilla.Entity.SolicitudServicios;
import com.ventanilla.Models.CatPredioModel;
import com.ventanilla.Models.DataPredioAutorizar;
import com.ventanilla.Models.FinaRenLiquidacionModel;
import com.ventanilla.Services.VentanillaService;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
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
 * @author Arturo
 */
@Named
@ViewScoped
public class IniciarTramiteVentanillaMB extends BpmnBaseRoot implements Serializable {

    @Inject
    private VentanillaService ventanillaService;
    @Inject
    private ClienteService clienteService;
    @Inject
    private FinaRenLiquidacionService liquidacionService;
    @Inject
    private FinaRenPagoService renPagoService;
    @Inject
    private TramiteRequisitoHistorialService tramiteRequisitoHistorialService;
    @Inject
    private RecaudacionInteface recaudacionService;
    @Inject
    private FileUploadDoc uploadDoc;
    @Inject
    private ServletSession ss;
    @Inject
    private FinaRenPagoRubroService finaRenPagoRubroService;
    @Inject
    private ServletSession servletSession;

    private static final long serialVersionUID = 1L;

    private List<Servicio> servicios;
    private SolicitudServicios solicitudServicios;
    private List<UnidadAdministrativa> departamentos;
    private UnidadAdministrativa departamento;

    private List<ListarequisitosModel> listaRequisitosGlobal;
    private List<ServicioRequisito> listRequisitoTasa;
    private List<ServicioRequisito> listaRequisitos;
    private Long idTipo;
    private TipoTramite tipoTramite;
    private Usuarios usuario;
    private Boolean parameter;
    private List<UsuarioRol> userList;
    private Boolean eleccionUsuarios, existeResponsable;
    private UsuarioRol userSelect;
    private List<ServicioTipo> serviciosTipos;
    private ServicioTipo servicioTipo;
    private List<ListarequisitosModel> requisitosSeleccionados;

    /*
    Para consulta del codigo de predio o catastral
     */
    private String codigoCatastral, tipoPredio, codigoVerificador, observacionAprobarPredio, identificacion, numeroComprobante;
    private List<FinaRenLiquidacion> emisionesPrediales;
    private FinaRenLiquidacion liquidacion;
    private BigDecimal totalEmisionesGeneral, totalEmisiones, totalCoactiva, totalRubros, totalDescuentoProntoPago, totalInteres, totalRecargo, totalAbonado;
    private FnSolicitudExoneracion exoneracion;
    private CatPredio catPredio;
    private Boolean loading, predioAutorizado, requiereAutorizacion;
    private PredioAutorizar predioAutorizarDB;

    //Para la subida de archivos
    private UploadedFile file;
    private File FILE;

    @PostConstruct
    public void initView() {
        loadModel();
    }

    public void loadModel() {
        departamentos = ventanillaService.findAllQuery("SELECT DISTINCT ua FROM Servicio s JOIN s.departamento ua WHERE ua.estado = true AND s.activo = true ORDER BY ua.nombre", null);
        servicios = ventanillaService.findAllQuery("SELECT s FROM Servicio s WHERE activo = true ORDER BY nombre", null);
        loadDataTramite();
    }

    private void loadDataTramite() {
        identificacion = "";
        solicitudServicios = new SolicitudServicios();
        liquidacion = new FinaRenLiquidacion();
        requisitosSeleccionados = new ArrayList<>();
        tramite = new HistoricoTramites();
        tramite.setSolicitante(new Cliente());
        tramite.getSolicitante().setIdentificacion("");
        loadData();
        departamento = new UnidadAdministrativa();
        requiereAutorizacion = Boolean.FALSE;
        userList = new ArrayList<>();
        this.usuario = new Usuarios();
        if (this.usuario.getFuncionario() == null) {
            this.usuario.setFuncionario(new Servidor());
            this.usuario.getFuncionario().setPersona(new Cliente());
        }
        eleccionUsuarios = Boolean.TRUE;
        existeResponsable = Boolean.FALSE;
        userSelect = new UsuarioRol();
        listRequisitoTasa = new ArrayList<>();
        servicioTipo = new ServicioTipo();
        listaRequisitosGlobal = new ArrayList<>();
        file = null;
        FILE = null;
    }

    private void loadData() {
        predioAutorizarDB = new PredioAutorizar();
        loading = Boolean.FALSE;
        predioAutorizado = Boolean.FALSE;
        tipoPredio = "";
        codigoVerificador = "";
        numeroComprobante = "";
        observacionAprobarPredio = "";
    }

    public void cargarTipoContribuyentes() {
        loadData();
        servicioTipo = null;
        listaRequisitos = new ArrayList<>();
        listRequisitoTasa = new ArrayList<>();
        listaRequisitosGlobal = new ArrayList<>();
        serviciosTipos = new ArrayList<>();
        if (tramite.getServicio() != null) {
            HashMap<String, Object> params = new HashMap<>();
            params.put("servicio", tramite.getServicio());
            if (departamento.getId() == null) {
                departamento = tramite.getServicio().getDepartamento();
            } else if (!departamento.getId().equals(tramite.getServicio().getDepartamento().getId())) {
                departamento = tramite.getServicio().getDepartamento();
            }
            if (tramite.getServicio() != null && tramite.getServicio().getVinculadoPredio() != null && tramite.getServicio().getVinculadoPredio()) {
                requiereAutorizacion = Boolean.TRUE;
            }
            serviciosTipos = ventanillaService.findAllQuery("SELECT st FROM ServicioTipo st WHERE st.servicio=:servicio", params);
        }
    }

    public void searchBeneficiario(Boolean parameter) {
        System.out.println("tramite.getSolicitante().getIdentificacion() " + tramite.getSolicitante().getIdentificacion());
        if (!Utils.isEmptyString(tramite.getSolicitante().getIdentificacion())) {
            Cliente c = clienteService.buscarCliente(tramite.getSolicitante().getIdentificacion());
            if (c != null && c.getId() != null) {
                tramite.setSolicitante(c);
                PrimeFaces.current().ajax().update("idBeneficiacio");
            } else if (c != null && c.getId() == null) {
                c = clienteService.create(c);
                tramite.setSolicitante(c);
                PrimeFaces.current().ajax().update("idBeneficiacio");
            } else {
                openDialogCliente(parameter);
            }
        } else {
            openDialogCliente(parameter);
        }
    }

    public void limpiarDatosSolicitante() {
        tramite.setSolicitante(new Cliente());
        identificacion = "";
    }

    private void openDialogCliente(Boolean parameter) {
        this.parameter = parameter;
        Map<String, List<String>> params = new HashMap<>();
        params.put(CONFIG.PARAMETER_TIPO, Arrays.asList("true"));
        params.put(CONFIG.PARAMETER_RENDER, Arrays.asList("true"));
        params.put(CONFIG.ONE_TYPE, Arrays.asList("1"));
        Utils.openDialog("/facelet/talentoHumano/dialogCliente", "45%", "70%", params);
    }

    public void selectBeneficiario(SelectEvent evt) {
        try {
            if (parameter) {
                tramite.setSolicitante((Cliente) evt.getObject());
            } else {
                tramite.setUsuarioRetiro((Cliente) evt.getObject());
            }
            PrimeFaces.current().ajax().update("@(.ui-panelgrid)");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error: seleccionar beneficiario", e);
        }
    }

    public void getServicioByDpto() {
        try {
            loadData();
            if (departamento != null && departamento.getId() != null) {
                Map<String, Object> params = new HashMap<>();
                params.put("departamento", departamento.getId());
                servicios = ventanillaService.findAllQuery("SELECT s FROM Servicio s  WHERE departamento.id=:departamento AND activo = true ORDER BY nombre", params);
            } else {
                userList = new ArrayList<>();
                servicios = new ArrayList<>();
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error: al traer los servicios por departamento", e);
            userList = new ArrayList<>();
        }
    }

    public void getRequisitosByServicio() {
        if (tramite.getServicio().getId() != null && servicioTipo != null && servicioTipo.getId() != null) {
            listaRequisitos = new ArrayList<>();
            listRequisitoTasa = new ArrayList<>();
            loadData();
            initListUsuarios();
//            ServicioTipo st = (ServicioTipo) ventanillaService.find("SELECT st FROM ServicioTipo st WHERE st.servicio=:servicio AND st.tipoContribuyentes=:tc",
//                    new String[]{"servicio", "tc"},
//                    new Object[]{tramite.getServicio(), tipoContribuyente});
            if (servicioTipo != null && servicioTipo.getId() != null) {
                Map<String, Object> paramsReq = new HashMap<>();
                paramsReq.put("servicio", servicioTipo.getId());
                listaRequisitos = ventanillaService.findAllQuery("SELECT s FROM ServicioRequisito s  WHERE servicioTipo.id=:servicio ORDER BY s.posicion", paramsReq);
                listaRequisitosGlobal = new ArrayList<>();
                if (listaRequisitos != null && !listaRequisitos.isEmpty()) {
                    for (ServicioRequisito item : listaRequisitos) {
                        if (item.getTasa()) {
//                            item.setTasaValidada(Boolean.FALSE);
                            listRequisitoTasa.add(item);
                            codigoVerificador = "";
                        }
                        listaRequisitosGlobal.add(new ListarequisitosModel(item));
                    }
                }
            }
        }
    }

    /*Obtener los usuarios cuando tengan a un responsable según el dpto asignado*/
    public void initListUsuarios() {
        try {
            eleccionUsuarios = Boolean.FALSE;
            userList = new ArrayList<>();
            userSelect = new UsuarioRol();
            Map<String, Object> params = new HashMap<>();
            params.put("departamento", departamento.getId());
            List<UsuarioRol> users = ventanillaService.findAllUsuariosRolByDepartamento(departamento, Boolean.TRUE, null, null, null);
            if (!users.isEmpty()) {
                existeResponsable = Boolean.TRUE;
                userList.addAll(users);
                userSelect = userList.get(0);
                JsfUtil.update("dtUsuarios");
            } else {
                existeResponsable = Boolean.FALSE;
                eleccionUsuarios = Boolean.TRUE;
                userList = ventanillaService.findAllQuery("SELECT ur FROM UsuarioRol ur INNER JOIN ur.usuario u INNER JOIN ur.rol r  where r.unidadAdministrativa.id=:departamento", params);;
                JsfUtil.update("dtUsuariosSelect");
            }
            if (Utils.isEmpty(userList)) {
                JsfUtil.addErrorMessage("", "No existen usuarios al departamento por favor comuníquese con el administrador del sistema");
            }
        } catch (Exception e) {
            System.out.println("//Exception List Usuario " + e.getMessage());
        }
    }

    public void initTramite() {
        try {
            if (validarInitTramite()) {
                Boolean ok = false;
                String usuariosAsignados = userSelect.getId() != null ? userSelect.getUsuario().getUsuario() : "";
                /*Asignamos el tipo de tramite Ventanilla unica*/
                tramite.setTipoTramite(tramite.getServicio().getTipoTramite());
                String[] codes = {"usuario", session.getNameUser()};
                if (tramite.getTipoTramite().getUserDireccion() != null && tramite.getTipoTramite().getUserDireccion().trim().length() > 0) {
                    // Si solo existe un solo valor envia como variable
                    String[] temp = tramite.getTipoTramite().getUserDireccion().split(":");
                    if (temp.length == 1) {
                        codes[0] = temp[0];
                    } else {
                        codes = temp;
                        codes[1] = userSelect.getUsuario().getUsuario();
                    }
                }
                //para resoluciones editado por David Borbor
                //hay un caso especial, el tramite pasa directo a resoluciones si la exoneracion es por tercera edad o discapacdad
                // caso contrario para a financiero
                if (tramite.getTipoTramite().getAbreviatura().equals("PRORESOL")) {
                    String usuario = "";
                    if (tramite.getServicio().getAbreviatura().equals("SETE") || tramite.getServicio().getAbreviatura().equals("SED")) {
                        usuario = clienteService.getrolsUser(RolUsuario.abogadoResoucion1);
                        this.getParamts().put("resolucion", usuario.equals("") ? "admin_1" : usuario);
                        this.getParamts().put("usuario", session.getNameUser());
                        this.getParamts().put("tipo", 1);
                    } else {
                        usuario = clienteService.getrolsUser(RolUsuario.jefeFinanciero);
                        this.getParamts().put("financiero_revision", usuario.equals("") ? "admin_1" : usuario);
                        this.getParamts().put("usuario", session.getNameUser());
                        this.getParamts().put("tipo", 0);
                    }
                } else {
                    System.out.println("codes " + codes[0] + " " + codes[1]);
                    this.getParamts().put(codes[0], codes[1]);
                    this.getParamts().put("usuario", session.getNameUser());
                }
                tramite.setFechaIngreso(new Date());
                tramite.setPeriodo(Utils.getAnio(new Date()).shortValue());
                tramite.setFecha(new Date());
//                if (!Utils.isEmpty(listRequisitoTasa)) {
//                    tramite = saveTramiteVentanilla();
//                } else {
//                    tramite = this.saveTramite();
//                }
                tramite = this.saveTramite();
                if (!Utils.isEmpty(requisitosSeleccionados)) {
                    for (ListarequisitosModel data : requisitosSeleccionados) {
                        TipoTramiteRequisitoHistorial objeto = new TipoTramiteRequisitoHistorial();
                        objeto.setTipoTramite(tramite.getTipoTramite());
                        objeto.setServicioRequisito(data.getServicioRequisitos());
                        tramiteRequisitoHistorialService.create(objeto);
                    }
                }
                if (tramite != null) {
                    ok = true;
                    crearSolicitudesTramite(usuariosAsignados);
                    if (!Utils.isEmpty(listRequisitoTasa)) {
                        for (ServicioRequisito sr : listRequisitoTasa) {
                            if (sr.getPagoRubro() != null && sr.getPagoRubro().getId() != null) {
                                sr.getPagoRubro().setTramite(Boolean.TRUE);
                                sr.getPagoRubro().setNumTramite(tramite.getCodigo());
                                sr.getPagoRubro().setUsuarioTramite(session.getNameUser());
                                finaRenPagoRubroService.edit(sr.getPagoRubro());
                            }
                        }
                    }
                    enviarCorreo("Inicio de Trámite");
                    this.setObservacion(new Observaciones());
                    this.getObservacion().setObservacion("Inicio de Trámite");
                    this.getObservacion().setTarea("Inicio");
                    this.saveObs();
                }
                if (ok) {
                    uploadDoc.upload(tramite, file);
                    JsfUtil.executeJS("PF('continuarDlg').show()");
                    PrimeFaces.current().ajax().update("frmContinuar");
                } else {
                    JsfUtil.addErrorMessage("Error", "No se pudo generar el trámite");
                    JsfUtil.redirect(CONFIG.URL_APP + "procesos/bandeja-tareas");
                }
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    private void enviarCorreo(String asunto) {
        Correo c = new Correo();
        c.setDestinatario(solicitudServicios.getEnteSolicitante().getEmail());
        c.setAsunto(asunto);
        c.setMensaje(Utils.mailHtmlNotificacion("TRÁMITE N° " + tramite.getCodigo() + " - " + tramite.getTipoTramite().getDescripcion(),
                "Estimado(a) " + solicitudServicios.getEnteSolicitante().getNombreCompleto() + " se le notifica el inicio del trámite N° " + tramite.getCodigo(),
                "Gracias por la Atención Brindada", "Este correo fue enviado de forma automática y no requiere respuesta."));
        this.correoService.enviarCorreo(c);
        JsfUtil.addSuccessMessage("Correo", "La notificación fue enviada con éxito");
    }

    private Boolean validarInitTramite() {
        if (departamento == null || departamento.getId() == null) {
            JsfUtil.addErrorMessage("", "Debe escoger el departamento");
            return Boolean.FALSE;
        }
        if (tramite.getServicio() == null || tramite.getServicio().getId() == null) {
            JsfUtil.addErrorMessage("", "Debe escoger el servicio");
            return Boolean.FALSE;
        }
        if (servicioTipo == null || servicioTipo.getId() == null) {
            JsfUtil.addErrorMessage("", "Debe escoger el tipo de contribuyente");
            return Boolean.FALSE;
        }
        if (tramite.getServicio().getVinculadoPredio() != null && tramite.getServicio().getVinculadoPredio() && requiereAutorizacion) {
            JsfUtil.addErrorMessage("", "Debe validar el predio con deudas");
            return Boolean.FALSE;
        }
        if (!Utils.isEmpty(emisionesPrediales) && !predioAutorizado) {
            JsfUtil.addErrorMessage("", "Debe validar el predio con deudas");
            return Boolean.FALSE;
        }
//        if (!Utils.isEmpty(listRequisitoTasa) && (liquidacion == null || liquidacion.getId() == null)) {
//            JsfUtil.addErrorMessage("", "Debe validar la liquidación");
//            return Boolean.FALSE;
//        }
//        if (!Utils.isEmpty(listRequisitoTasa)) {
//            for (ServicioRequisito sr : listRequisitoTasa) {
//                if (!sr.getOpcional() && !sr.getTasaValidada()) {
//                    JsfUtil.addErrorMessage("", "Debe validar todas las tasas requeridas");
//                    return Boolean.FALSE;
//                }
//            }
//        }
        if (tramite.getSolicitante() == null || tramite.getSolicitante().getId() == null) {
            JsfUtil.addErrorMessage("", "Debe ingresar los datos el solicitante");
            return Boolean.FALSE;
        }
        if (tramite.getConcepto() == null || tramite.getConcepto().equals("")) {
            JsfUtil.addErrorMessage("", "Ingrese una observación");
            return Boolean.FALSE;
        }
        if (!Utils.isEmpty(listaRequisitosGlobal)) {
            if (Utils.isEmpty(requisitosSeleccionados)) {
                JsfUtil.addErrorMessage("", "Debe seleccionar los requisitos obligatorios");
                return Boolean.FALSE;
            }
            for (ListarequisitosModel req : listaRequisitosGlobal) {
                if (!req.getServicioRequisitos().getOpcional() && !requisitosSeleccionados.contains(req)) {
                    JsfUtil.addErrorMessage("", "Verifique que todos los requisitos obligatorios estén ingresados");
                    return Boolean.FALSE;
                }
            }
            if (file == null || FILE == null) {
                JsfUtil.addErrorMessage("", "Por favor adjunte un documento");
                return Boolean.FALSE;
            }
        }
        if (eleccionUsuarios) {
            if (Utils.isEmpty(userList)) {
                JsfUtil.addErrorMessage("", "No existen usuarios al departamento por favor comuníquese con el administrador del sistema");
                return Boolean.FALSE;
            }
            if (userSelect == null || userSelect.getId() == null) {
                JsfUtil.addErrorMessage("", "Debe escoger un usuario para el trámite");
                return Boolean.FALSE;
            }
        }
        return Boolean.TRUE;
    }

    public SolicitudServicios crearSolicitudServicio() {
        solicitudServicios = new SolicitudServicios();
        solicitudServicios.setSolicitudInterna(Boolean.TRUE);
        solicitudServicios.setEnLinea(Boolean.FALSE);
        solicitudServicios.setDescripcionInconveniente(tramite.getConcepto().toUpperCase());
        solicitudServicios.setFechaCreacion(new Date());
        solicitudServicios.setStatus("A");
        solicitudServicios.setServicioTipo(servicioTipo);
        solicitudServicios.setTramite(tramite);
        solicitudServicios.setEnteSolicitante(tramite.getSolicitante());
        solicitudServicios.setUsuarioIngreso(session.getUsuario());
        solicitudServicios.setReferenciaLiquidacion(codigoVerificador);
        solicitudServicios.setTipoContribuyente(servicioTipo.getTipoContribuyentes().getNombre());
        solicitudServicios.setFinalizado(Boolean.FALSE);
        solicitudServicios.setPredio(catPredio);
        solicitudServicios.setEnObservacion(Boolean.FALSE);
        solicitudServicios = (SolicitudServicios) ventanillaService.save(solicitudServicios);
        return solicitudServicios;
    }

    public void continuarProceso() {
        loadDataTramite();
        JsfUtil.update("formMain");
        JsfUtil.executeJS("PF('continuarDlg').hide()");
    }

    /*Subir el archivo y guardarlo en el repositorio de archivos*/
    public void handleFileUpload(FileUploadEvent event) {
        try {
            file = event.getFile();
            FILE = Utils.copyFileServer(file, SisVars.rutaRepositorioArchivo);
            JsfUtil.addInformationMessage("Información", "El archvio se subió correctamente");
        } catch (IOException e) {
            JsfUtil.addErrorMessage(null, "Ocurrió un error al subir el archivo");
        }
    }

//    public void viewFile(ListarequisitosModel modelFile) {
//        if (modelFile.getFile() != null) {
//            try {
//                ss.setContentType(requisitosObjeto.getFile().getContentType());
//                ss.setNombreDocumento(requisitosObjeto.getFile().getFileName());
//                ss.setTempData(requisitosObjeto.getFile().getInputstream());
//            } catch (IOException ex) {
//                Logger.getLogger(StartProcessController.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//    }
//    public void validarComprobante() {
//        if (Utils.isEmptyString(codigoVerificador)) {
//            JsfUtil.addErrorMessage("", "Ingrese el código de la liquidacion para validar el comprobante");
//            return;
//        }
//        liquidacion = liquidacionService.getFinaRenLiquidacionByIdLiquidacion(codigoVerificador);
//        if (liquidacion != null && liquidacion.getId() != null && (liquidacion.getCodigoVerificado() == null || !liquidacion.getCodigoVerificado())) {
//            JsfUtil.addSuccessMessage("", "Liquidación disponible");
//        } else {
//            liquidacion = new FinaRenLiquidacion();
//            JsfUtil.addErrorMessage("", "No existe ninguna liquidación o la liquidación ya ha sido usada");
//        }
//    }
    public void validarComprobante(ServicioRequisito tasa) {
        if (tasa.getRubro().getId() != null) {
            if (Utils.isEmptyString(codigoVerificador) && Utils.isEmptyString(numeroComprobante)) {
                JsfUtil.addErrorMessage("", "Ingrese los campos requeridos para verificar el comprobante");
                return;
            }
            FinaRenPagoRubro pagoRubro = renPagoService.getRubroByLiquidacionAndPagoAndRubro(tasa.getRubro().getId(), codigoVerificador, numeroComprobante);
            if (pagoRubro == null || pagoRubro.getId() == null) {
                JsfUtil.addErrorMessage("", "No existe pagos para el número de comprobante ingresado");
                return;
            }
            if (pagoRubro.getTramite() != null && pagoRubro.getTramite()) {
                JsfUtil.addErrorMessage("", "La tasa ya ha sido usada");
                return;
            }
            tasa.setTasaValidada(Boolean.TRUE);
            tasa.setPagoRubro(pagoRubro);
            JsfUtil.addSuccessMessage("", "Tasa disponible");
        } else {
            JsfUtil.addErrorMessage("", "La tasa no tiene ningún rubro relacionado, por favor comuníquise con el administrador");
        }
    }

    public void validarPredio() {
        if (Utils.isEmptyString(tipoPredio)) {
            JsfUtil.addErrorMessage("Debe escoger el tipo de predio", "");
            return;
        }
        if (Utils.isEmptyString(codigoCatastral)) {
            JsfUtil.addErrorMessage("Debe ingresar el código predio o catastral", "");
            return;
        }
        catPredio = codigoCatastral.contains(".")
                ? ventanillaService.findByNamedQuery1("CatPredio.findByClaveCatAndTipo", new Object[]{codigoCatastral, tipoPredio})
                : ventanillaService.findByNamedQuery1("CatPredio.findByNumPredio", new Object[]{new BigInteger(codigoCatastral), tipoPredio});
        if (catPredio == null) {
            JsfUtil.addErrorMessage("No existen datos", "");
            return;
        }
        emisionesPrediales = liquidacionService.liquidacionesByPredio(catPredio, new FinaRenTipoLiquidacion(2L));
        requiereAutorizacion = Boolean.TRUE;
        predioAutorizado = Boolean.FALSE;
        if (Utils.isEmpty(emisionesPrediales)) {
            requiereAutorizacion = Boolean.FALSE;
            predioAutorizado = Boolean.TRUE;
        }
        calculoTotalPago(emisionesPrediales, null);
        JsfUtil.executeJS("PF('dialogDeudas').show()");
        JsfUtil.update("formDeudasPredio");
    }

    public void calculoTotalPago(List<FinaRenLiquidacion> listado, Date fechaPago) {
        Boolean flag = true;
        totalEmisiones = new BigDecimal("0.00");
        totalCoactiva = new BigDecimal("0.00");
        totalRubros = new BigDecimal("0.00");
        totalDescuentoProntoPago = new BigDecimal("0.00");
        totalInteres = new BigDecimal("0.00");
        totalRecargo = new BigDecimal("0.00");
        totalAbonado = new BigDecimal("0.00");
        totalCoactiva = new BigDecimal("0.00");
        if (!Utils.isEmpty(listado)) {
            requiereAutorizacion = Boolean.TRUE;
            for (FinaRenLiquidacion e : listado) {
                // Pregunta por el año actual, si ya fue exonerado y si se encontró la solicitud de exoneración en el anterior método.
                if (Objects.equals(e.getAnio(), Utils.getAnio(new Date())) && e.getEstaExonerado() && exoneracion != null) {
                    exoneracion = null;
                }
                if (flag && e.getEstadoLiquidacion().getId().compareTo(2L) == 0) {
                    if (e.getEstadoCoactiva() == 2) {
                        flag = false;
                    }
                }
                if (e.getEstadoLiquidacion().getId().compareTo(2L) == 0) {
                    try {
                        //CALCULO DE DESCUENTO-RECARGO-INTERE
                        long totalSum = 0;
                        long startTime = System.currentTimeMillis();
                        if (e.getTipoLiquidacion().getId().equals(2L) || e.getTipoLiquidacion().getId().equals(3L)) {
                            e = recaudacionService.realizarDescuentoRecargaInteresPredial(e, fechaPago);
                        }
                        long endTime = System.currentTimeMillis() - startTime;
                        //imprime tiempo transcurrido en ms
                        System.out.println("despues de ejecutar el metodo de calculos>>>Duración " + endTime + " milisegundos.");
                        e.calcularPagoConCoactiva();
                        totalEmisiones = totalEmisiones.add(e.getPagoFinal());
                        totalEmisiones = totalEmisiones.setScale(2, RoundingMode.HALF_UP);
                    } catch (Exception ex) {
                        LOG.log(Level.SEVERE, null, e);
                    }
                }
                totalRubros = totalRubros.add(e.getTotalPago() != null ? e.getTotalPago() : BigDecimal.ZERO);
                totalCoactiva = totalCoactiva.add(e.getValorCoactiva() != null ? e.getValorCoactiva() : BigDecimal.ZERO);
                totalDescuentoProntoPago = totalDescuentoProntoPago.add(e.getDescuento() != null ? e.getDescuento() : BigDecimal.ZERO);
                totalInteres = totalInteres.add(e.getInteres() != null ? e.getInteres() : BigDecimal.ZERO);
                totalRecargo = totalRecargo.add(e.getRecargo() != null ? e.getRecargo() : BigDecimal.ZERO);
                totalAbonado = totalAbonado.add(e.abonado() != null ? e.abonado() : BigDecimal.ZERO);
            }
        }
    }

    public Cliente clientePredio() {
        if (catPredio != null && catPredio.getId() != null
                && catPredio.getCatPredioPropietarioList() != null
                && !catPredio.getCatPredioPropietarioList().isEmpty()) {
            return catPredio.getCatPredioPropietarioList().get(0).getEnte();
        }
        return null;
    }

    public void autorizarPredioConDeudas() {
        if (!Utils.isEmpty(emisionesPrediales)) {
            if (Utils.isEmptyString(observacionAprobarPredio)) {
                JsfUtil.addErrorMessage("", "Ingrese una observacion");
                return;
            }
            loading = Boolean.TRUE;
            Gson gson = Utils.getInstanceGson();
            String concepto = "Solicita una validación para un predio "
                    + (tipoPredio.equals("U") ? "Urbano" : "Rural")
                    + " con deuda con código " + codigoCatastral + " en un tipo de trámite "
                    + tramite.getServicio().getNombre()
                    + (clientePredio() != null
                            ? ". El contribuyente es " + clientePredio().getNombreCompleto()
                            + " con número de cédula " + clientePredio().getIdentificacion() + ". "
                            : ". ")
                    + "El predio tiene una deuda de $" + totalEmisiones;
            DataPredioAutorizar dato = new DataPredioAutorizar(tramite.getServicio().getNombre(), concepto, observacionAprobarPredio, initCatPredio());
            PredioAutorizar predioAut = new PredioAutorizar(gson.toJson(dato), new Date(), session.getNameUser(),
                    observacionAprobarPredio, Boolean.FALSE, Boolean.FALSE);
            predioAutorizarDB = (PredioAutorizar) ventanillaService.save(predioAut);
            JsfUtil.executeJS("PF('pollIncrement').start()");
            JsfUtil.update("formAutorizacion");
        }
    }

    private CatPredioModel initCatPredio() {
        List<FinaRenLiquidacionModel> emisiones = new ArrayList<>();
        if (!Utils.isEmpty(emisionesPrediales)) {
            for (FinaRenLiquidacion r : emisionesPrediales) {
                FinaRenLiquidacionModel model = new FinaRenLiquidacionModel(r.getId(), r.getAnio(), r.getTotalPago(),
                        r.getDescuento(), r.getInteres(), r.getRecargo(), r.getValorCoactiva(), r.getPagoFinal(), r.abonado());
                emisiones.add(model);
            }
        }
        if (catPredio != null && catPredio.getId() != null) {
            return new CatPredioModel(catPredio.getId(), catPredio.getClaveCat(), catPredio.getNumPredio(),
                    clientePredio() != null ? clientePredio().getIdentificacion() : "",
                    clientePredio() != null ? clientePredio().getNombreCompleto() : "",
                    catPredio.getDireccion(), totalEmisiones, totalRubros, totalDescuentoProntoPago, totalInteres,
                    totalRecargo, totalCoactiva, totalAbonado, emisiones);
        }
        return null;
    }

    public void predioConDeudaAutorizado() {
        if (predioAutorizarDB != null && predioAutorizarDB.getId() != null) {
            PredioAutorizar puDB = (PredioAutorizar) ventanillaService.find(PredioAutorizar.class,
                    predioAutorizarDB.getId());
            if (puDB != null && puDB.getId() != null && puDB.getAutorizado()) {
                predioAutorizarDB = puDB;
                loading = Boolean.FALSE;
                predioAutorizado = Boolean.TRUE;
                requiereAutorizacion = Boolean.FALSE;
                JsfUtil.executeJS("PF('pollIncrement').stop()");
                JsfUtil.executeJS("PF('dlgAutorizado').hide()");
                JsfUtil.update("formAutorizacion");
                JsfUtil.update("formDeudasPredio");
                JsfUtil.addSuccessMessage("Predio Autorizado", "");
            }
        }
    }

    public void cancelarAprobacion() {
        predioAutorizarDB.setCancelado(Boolean.TRUE);
        predioAutorizado = Boolean.FALSE;
        loading = Boolean.FALSE;
        predioAutorizarDB = (PredioAutorizar) ventanillaService.save(predioAutorizarDB);
        JsfUtil.executeJS("PF('pollIncrement').stop()");
        JsfUtil.executeJS("PF('dlgAutorizado').hide()");
        JsfUtil.update("formAutorizacion");
    }

    public void actualizarSolicitante() {
        if (tramite.getSolicitante() != null) {
            if (!Utils.validarEmailConExpresion(tramite.getSolicitante().getEmail())) {
                JsfUtil.addErrorMessage("", "Ingrese un correo correcto");
                return;
            }
            clienteService.edit(tramite.getSolicitante());
            JsfUtil.addSuccessMessage("", "Datos actualizados");
        }
    }

    private void crearSolicitudesTramite(String usuariosAsignados) {
        SolicitudDepartamento solicitudDepartamento = new SolicitudDepartamento();
        solicitudDepartamento.setEstado(Boolean.TRUE);
        solicitudDepartamento.setDepartamento(departamento);
        //CREANDO LA SOLICITUD SERVICIO
        solicitudDepartamento.setSolicitudServicio(crearSolicitudServicio());
        //CREANDO LOS REQUISITOS QUE ESTAN Y NO EN LA SOLICITUD
        crearRegistroSolicitudRequisitos(solicitudDepartamento.getSolicitudServicio());
        solicitudDepartamento.setResponsables(usuariosAsignados);
        solicitudDepartamento.setFecha(new Date());

        //ACTUALIZANDO LA LIQUIDACION SI ES QUE TIENE ALGUNA TASA QUE COMPRAR
        if (!Utils.isEmpty(listRequisitoTasa) && liquidacion != null && liquidacion.getId() != null) {
            liquidacion.setCodigoVerificado(Boolean.TRUE);
            liquidacionService.edit(liquidacion);
        }
    }

    private void crearRegistroSolicitudRequisitos(SolicitudServicios solicitudServicios) {
        if (!Utils.isEmpty(listaRequisitosGlobal) && !Utils.isEmpty(requisitosSeleccionados)) {
            List<RegistroSolicitudRequisitos> registroRequisito = new ArrayList<>();
            for (ListarequisitosModel lm : listaRequisitosGlobal) {
                RegistroSolicitudRequisitos rs = new RegistroSolicitudRequisitos(new SolicitudServicios(solicitudServicios.getId()),
                        new ServicioRequisito(lm.getServicioRequisitos().getId()), Boolean.FALSE,
                        null, Boolean.TRUE, Boolean.FALSE, "", new Date(), session.getNameUser());
                rs.setEntregado(requisitosSeleccionados.contains(lm) ? Boolean.TRUE : Boolean.FALSE);
                registroRequisito.add(rs);
            }
            if (!Utils.isEmpty(registroRequisito)) {
                ventanillaService.saveAll(registroRequisito);
            }
        }
    }

    public void imprimirTicket() {
        servletSession.borrarDatos();
        servletSession.borrarParametros();
        if (solicitudServicios != null && solicitudServicios.getId() != null) {
            List<SolicitudServicios> ssR = new ArrayList<>();
            ssR.add(solicitudServicios);
            servletSession.setDataSource(ssR);
            servletSession.setNombreSubCarpeta("ventanilla");
            servletSession.setNombreReporte("ticketTramite");
            JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
        } else {
            JsfUtil.addErrorMessage("", "Error al imprimir el reporte");
        }
    }

    //<editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public String getNumeroComprobante() {
        return numeroComprobante;
    }

    public void setNumeroComprobante(String numeroComprobante) {
        this.numeroComprobante = numeroComprobante;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public Boolean getPredioAutorizado() {
        return predioAutorizado;
    }

    public void setPredioAutorizado(Boolean predioAutorizado) {
        this.predioAutorizado = predioAutorizado;
    }

    public Boolean getRequiereAutorizacion() {
        return requiereAutorizacion;
    }

    public void setRequiereAutorizacion(Boolean requiereAutorizacion) {
        this.requiereAutorizacion = requiereAutorizacion;
    }

    public String getObservacionAprobarPredio() {
        return observacionAprobarPredio;
    }

    public void setObservacionAprobarPredio(String observacionAprobarPredio) {
        this.observacionAprobarPredio = observacionAprobarPredio;
    }

    public Boolean getLoading() {
        return loading;
    }

    public void setLoading(Boolean loading) {
        this.loading = loading;
    }

    public PredioAutorizar getPredioAutorizarDB() {
        return predioAutorizarDB;
    }

    public void setPredioAutorizarDB(PredioAutorizar predioAutorizarDB) {
        this.predioAutorizarDB = predioAutorizarDB;
    }

    public BigDecimal getTotalRubros() {
        return totalRubros;
    }

    public void setTotalRubros(BigDecimal totalRubros) {
        this.totalRubros = totalRubros;
    }

    public BigDecimal getTotalDescuentoProntoPago() {
        return totalDescuentoProntoPago;
    }

    public void setTotalDescuentoProntoPago(BigDecimal totalDescuentoProntoPago) {
        this.totalDescuentoProntoPago = totalDescuentoProntoPago;
    }

    public BigDecimal getTotalInteres() {
        return totalInteres;
    }

    public void setTotalInteres(BigDecimal totalInteres) {
        this.totalInteres = totalInteres;
    }

    public BigDecimal getTotalRecargo() {
        return totalRecargo;
    }

    public void setTotalRecargo(BigDecimal totalRecargo) {
        this.totalRecargo = totalRecargo;
    }

    public BigDecimal getTotalEmisionesGeneral() {
        return totalEmisionesGeneral;
    }

    public void setTotalEmisionesGeneral(BigDecimal totalEmisionesGeneral) {
        this.totalEmisionesGeneral = totalEmisionesGeneral;
    }

    public BigDecimal getTotalEmisiones() {
        return totalEmisiones;
    }

    public void setTotalEmisiones(BigDecimal totalEmisiones) {
        this.totalEmisiones = totalEmisiones;
    }

    public BigDecimal getTotalCoactiva() {
        return totalCoactiva;
    }

    public void setTotalCoactiva(BigDecimal totalCoactiva) {
        this.totalCoactiva = totalCoactiva;
    }

    public List<FinaRenLiquidacion> getEmisionesPrediales() {
        return emisionesPrediales;
    }

    public CatPredio getCatPredio() {
        return catPredio;
    }

    public void setCatPredio(CatPredio catPredio) {
        this.catPredio = catPredio;
    }

    public void setEmisionesPrediales(List<FinaRenLiquidacion> emisionesPrediales) {
        this.emisionesPrediales = emisionesPrediales;
    }

    public String getCodigoVerificador() {
        return codigoVerificador;
    }

    public void setCodigoVerificador(String codigoVerificador) {
        this.codigoVerificador = codigoVerificador;
    }

    public String getTipoPredio() {
        return tipoPredio;
    }

    public void setTipoPredio(String tipoPredio) {
        this.tipoPredio = tipoPredio;
    }

    public List<ListarequisitosModel> getRequisitosSeleccionados() {
        return requisitosSeleccionados;
    }

    public void setRequisitosSeleccionados(List<ListarequisitosModel> requisitosSeleccionados) {
        this.requisitosSeleccionados = requisitosSeleccionados;
    }

    public String getCodigoCatastral() {
        return codigoCatastral;
    }

    public void setCodigoCatastral(String codigoCatastral) {
        this.codigoCatastral = codigoCatastral;
    }

    public List<ServicioRequisito> getListRequisitoTasa() {
        return listRequisitoTasa;
    }

    public void setListRequisitoTasa(List<ServicioRequisito> listRequisitoTasa) {
        this.listRequisitoTasa = listRequisitoTasa;
    }

    public List<ServicioTipo> getServiciosTipos() {
        return serviciosTipos;
    }

    public void setServiciosTipos(List<ServicioTipo> serviciosTipos) {
        this.serviciosTipos = serviciosTipos;
    }

    public ServicioTipo getServicioTipo() {
        return servicioTipo;
    }

    public void setServicioTipo(ServicioTipo servicioTipo) {
        this.servicioTipo = servicioTipo;
    }

    public List<Servicio> getServicios() {
        return servicios;
    }

    public void setServicios(List<Servicio> servicios) {
        this.servicios = servicios;
    }

    public ClienteService getClienteService() {
        return clienteService;
    }

    public void setClienteService(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    public List<ListarequisitosModel> getListaRequisitosGlobal() {
        return listaRequisitosGlobal;
    }

    public void setListaRequisitosGlobal(List<ListarequisitosModel> listaRequisitosGlobal) {
        this.listaRequisitosGlobal = listaRequisitosGlobal;
    }

    public List<ServicioRequisito> getListaRequisitos() {
        return listaRequisitos;
    }

    public void setListaRequisitos(List<ServicioRequisito> listaRequisitos) {
        this.listaRequisitos = listaRequisitos;
    }

    public Long getIdTipo() {
        return idTipo;
    }

    public void setIdTipo(Long idTipo) {
        this.idTipo = idTipo;
    }

    public TipoTramite getTipoTramite() {
        return tipoTramite;
    }

    public void setTipoTramite(TipoTramite tipoTramite) {
        this.tipoTramite = tipoTramite;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public Boolean getParameter() {
        return parameter;
    }

    public void setParameter(Boolean parameter) {
        this.parameter = parameter;
    }

    public List<UnidadAdministrativa> getDepartamentos() {
        return departamentos;
    }

    public void setDepartamentos(List<UnidadAdministrativa> departamentos) {
        this.departamentos = departamentos;
    }

    public UnidadAdministrativa getDepartamento() {
        return departamento;
    }

    public void setDepartamento(UnidadAdministrativa departamento) {
        this.departamento = departamento;
    }

    public Boolean getEleccionUsuarios() {
        return eleccionUsuarios;
    }

    public void setEleccionUsuarios(Boolean eleccionUsuarios) {
        this.eleccionUsuarios = eleccionUsuarios;
    }

    public Boolean getExisteResponsable() {
        return existeResponsable;
    }

    public void setExisteResponsable(Boolean existeResponsable) {
        this.existeResponsable = existeResponsable;
    }

    public UsuarioRol getUserSelect() {
        return userSelect;
    }

    public void setUserSelect(UsuarioRol userSelect) {
        this.userSelect = userSelect;
    }

    public List<UsuarioRol> getUserList() {
        return userList;
    }

    public void setUserList(List<UsuarioRol> userList) {
        this.userList = userList;
    }

//</editor-fold>
}
