/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.procesos.controllers;

import com.asgard.Entity.FinaRenLocalComercial;
import com.catastro.Services.CatPredioServices;
import com.gestionTributaria.Entities.CatPredio;
import com.gestionTributaria.Services.SecuenciasServices;
import com.origami.sigef.activos.service.procesoService.ProcesoService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.entities.ProcedimientoRequisito;
import com.origami.sigef.common.entities.TipoTramiteRequisitoHistorial;
import com.origami.sigef.common.entities.Usuarios;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.TramiteService;
import com.origami.sigef.common.service.UsuarioService;
import com.origami.sigef.common.service.interfaces.FileUploadDoc;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.procesos.Model.ListarequisitosModel;
import static com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot.LOG;
import com.origami.sigef.resource.procesos.entities.HistoricoTramites;
import com.origami.sigef.resource.procesos.entities.Observaciones;
import com.origami.sigef.resource.procesos.entities.TipoTramite;
import com.origami.sigef.resource.procesos.entities.Tramite;
import com.origami.sigef.resource.procesos.services.TipoTramiteService;
import com.origami.sigef.resource.procesos.services.TramiteRequisitoHistorialService;
import com.origami.sigef.resource.talento_humano.entities.Servidor;
import java.io.IOException;
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
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author Angel Navarro
 */
@Named(value = "startProcessView")
@ViewScoped
public class StartProcessController extends BpmnBaseRoot implements Serializable {

    @Inject
    private TipoTramiteService tipotramiteService;
    @Inject
    private UsuarioService usuarioService;
    @Inject
    private ClienteService clienteService;
    @Inject
    private ProcesoService tipoTramiteService;
    @Inject
    private TramiteRequisitoHistorialService tramiteRequisitoHistorialService;
    @Inject
    private FileUploadDoc uploadDoc;
    @Inject
    private ServletSession ss;
    @Inject
    private TramiteService tramiteService;
    @Inject
    private CatPredioServices predioService;
    @Inject
    private SecuenciasServices secuenciasServices;

    private static final long serialVersionUID = 1L;

    private List<TipoTramite> tipoTramites;
    private List<TipoTramite> subTipoTramites;
    private List<ListarequisitosModel> listaRequisitosGlobal;
    private List<ProcedimientoRequisito> listaRequisitos;
    private Long idTipo;
    private TipoTramite tipoTramite;
    private UploadedFile file;
    private List<UploadedFile> files;
    private Usuarios usuario;
    private Boolean parameter;
    private ListarequisitosModel requisitosObjeto;
    private List<Tramite> tramites;
    private Tramite tramiteEntity;
    private String nombreCompania;
    private String rucCompania;
    private String direccionConpania;
    private String claveCatastral;
    private Cliente cliente;
    private String identificacion;
    private FinaRenLocalComercial local;
    private LazyModel<Cliente> clientes;
    private LazyModel<FinaRenLocalComercial> localesLazy;

    @PostConstruct

    public void initView() {
        if (!JsfUtil.isAjaxRequest()) {
            long currentTimeMillis = System.currentTimeMillis();
            System.out.println("isAjaxRequest >>  " + getTime(currentTimeMillis));
            //tipoTramites = tipotramiteService.findByUnidad(null);
            setTramite(new HistoricoTramites());
            getTramite().setTipoTramite(tipotramiteService.findByCodigo("C_US"));
            this.usuario = new Usuarios();
            if (this.usuario.getFuncionario() == null) {
                this.usuario.setFuncionario(new Servidor());
                this.usuario.getFuncionario().setPersona(new Cliente());
            }
            System.out.println("Buscar usuario " + getTime(currentTimeMillis));
            tramites = new ArrayList<>();
            tramites = tramiteService.findAllQuery("SELECT t FROM Tramite t WHERE estado=TRUE ORDER BY nombre ", null);
            System.out.println("Buscar tramites " + getTime(currentTimeMillis));
            cliente = new Cliente();
            local = new FinaRenLocalComercial();
            clientes = new LazyModel<>(Cliente.class);
            localesLazy = new LazyModel<>(FinaRenLocalComercial.class);
            System.out.println("Fin " + getTime(currentTimeMillis));
        }
    }

    public void getTipoTramiteByTramite() {
        try {
            if (tramiteEntity.getId() != null) {
                Map<String, Object> params = new HashMap<>();
                params.put("tramite", tramiteEntity.getId());
                tipoTramites = tramiteService.findAllQuery("SELECT tt FROM TipoTramite tt WHERE tt.estado=TRUE AND tt.tramite.id =:tramite ORDER BY tt.descripcion ASC", params);
            } else {
                tipoTramites = new ArrayList<>();
            }
        } catch (Exception e) {
            System.out.println("Error: al traer los servicios por departamento " + e);
        }
    }

    public void searchBeneficiario(Boolean parameter) {
        this.parameter = parameter;
        Map<String, List<String>> params = new HashMap<>();
        params.put(CONFIG.PARAMETER_TIPO, Arrays.asList("true"));
        params.put(CONFIG.PARAMETER_RENDER, Arrays.asList("true"));
        params.put(CONFIG.ONE_TYPE, Arrays.asList("1"));
        Utils.openDialog("/facelet/view/commons/dlgBeneficiarios", "45%", "70%", params);
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

    public void getListRequisitos() {
//        subTipoTramites = tipotramiteService.findBySubtipo(tramite.getTipoTramite());
        listaRequisitos = new ArrayList<>();
        System.out.println("tramite id:" + tramite.getTipoTramite().getId());
        if (tramite.getTipoTramite().getId() == 120L) {
            this.listaRequisitos = new ArrayList<>();
            JsfUtil.addWarningMessage("AVISO!!!", "Los documentos se receptarán una vez iniciado el trámite");
            PrimeFaces.current().executeScript("PF('dtRequisitos').hide()");
        } else {
            this.listaRequisitos = tipoTramiteService.getListaRequisito(tramite.getTipoTramite().getId());
        }
        System.out.println("lista requisitos" + listaRequisitos.size());
        listaRequisitosGlobal = new ArrayList<>();
        if (listaRequisitos != null && !listaRequisitos.isEmpty()) {
            for (ProcedimientoRequisito item : listaRequisitos) {
                listaRequisitosGlobal.add(new ListarequisitosModel(item, null));
            }
        }
        presentarModalPermisoFuncionamientoJuridico();
    }

    public void presentarModalPermisoFuncionamientoJuridico() {
        if (tramite.getTipoTramite().getActivitykey().equals("procesoPermisoFunionamientoJuridico")) {
            JsfUtil.executeJS("PF('dlgRentas').show()");
        }
    }

    public void anadirFile() {
        if (file != null) {
            requisitosObjeto.setFile(file);
            this.listaRequisitosGlobal.add(this.listaRequisitosGlobal.indexOf(requisitosObjeto), requisitosObjeto);
            this.listaRequisitosGlobal.remove(this.listaRequisitosGlobal.indexOf(requisitosObjeto));
        }
        file = null;
        PrimeFaces.current().executeScript("PF('DlgoDocumento').hide()");
        PrimeFaces.current().ajax().update("formDocumento");
    }

    public void abrirDlgArchivos(ListarequisitosModel model) {
        this.requisitosObjeto = model;
        PrimeFaces.current().executeScript("PF('DlgoDocumento').show()");
        PrimeFaces.current().ajax().update("formDocumento");
    }

    public void testRepo() {
        if (!listaRequisitosGlobal.isEmpty()) {
            if (files != null) {
                uploadDoc.upload(tramite, files);
            }
        }
    }

    public void initTramite() {
        if (tramite.getPeriodo() == null || tramite.getPeriodo() == 0) {
            JsfUtil.addWarningMessage("AVISO!!!", "Ingrese un período");
            return;
        }
        if (tramite.getFechaIngreso() == null) {
            JsfUtil.addWarningMessage("AVISO!!!", "Seleccione la fecha de inicio del trámite");
            return;
        }
        if (tramite.getFecha() == null) {
            JsfUtil.addWarningMessage("AVISO!!!", "Seleccione la fecha de referencia del trámite");
            return;
        }
        if (tramite.getTipoTramite() == null) {
            JsfUtil.addWarningMessage("AVISO!!!", "Seleccione un tipo de trámite");
            return;
        }
        if (tramite.getSubTipoTramite() == null) {
            if (subTipoTramites != null) {
                if (!subTipoTramites.isEmpty()) {
                    JsfUtil.addWarningMessage("AVISO!!!", "Seleccione un subtipo de trámite");
                    return;
                }
            }
        }
        if (tramite.getConcepto() == null || tramite.getConcepto().equals("")) {
            JsfUtil.addWarningMessage("AVISO!!!", "Ingrese un concepto");
            return;
        }
        boolean ok = false;
        try {
            if (tramite.getSubTipoTramite() != null) {
                tramite.setTipoTramite(tramite.getSubTipoTramite());
            }
            String[] codes = {"usuario", session.getNameUser()};
            if (tramite.getTipoTramite().getUserDireccion() != null && tramite.getTipoTramite().getUserDireccion().trim().length() > 0) {
                // Si solo existe un solo valor envia como variable
                String[] temp = tramite.getTipoTramite().getUserDireccion().split(":");
                if (temp.length == 1) {
                    codes[0] = temp[0];
                } else {
                    codes = temp;
                    codes[1] = clienteService.getrolsUser(temp[1]);
                }
            }
            this.getParamts().put(codes[0], Utils.isEmptyString(codes[1]) ? session.getNameUser() : codes[1]);
            this.getParamts().put("usuario", session.getNameUser());
            this.tramite = this.saveTramite();

            //*Guardamos los requisitos que vamos utilizando*//
            if (!listaRequisitosGlobal.isEmpty()) {
                for (ListarequisitosModel data : listaRequisitosGlobal) {
                    if (data.getRequisitos().getObligatorio() == true) {
                        if (data.getFile() != null) {
                            TipoTramiteRequisitoHistorial objeto = new TipoTramiteRequisitoHistorial();
                            objeto.setTipoTramite(tipoTramite);
                            objeto.setProcedimientoRequisito(data.getRequisitos());
                            tramiteRequisitoHistorialService.create(objeto);
                        }
                    }
                }
            }

            if (this.tramite != null) {
                ok = true;
                this.setObservacion(new Observaciones());
                this.getObservacion().setObservacion("Inicio de Tramite");
                this.getObservacion().setTarea("Inicio");
                this.saveObs();
            }
            if (ok) {
                testRepo();
                JsfUtil.executeJS("PF('continuarDlg').show()");
                PrimeFaces.current().ajax().update("frmContinuar");
            } else {
                JsfUtil.addErrorMessage("Error", "No se pudo generar el trámite");
                JsfUtil.redirectFaces("/procesos/bandeja-tareas");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void continuarProceso() {
        JsfUtil.redirectFaces("/procesos/bandeja-tareas");
    }

    public void openddddd() {
        JsfUtil.executeJS("PF('continuarDlg').show()");
        PrimeFaces.current().ajax().update("frmContinuar");
    }

    public void handleFileUpload(FileUploadEvent event) {
        if (files == null) {
            files = new ArrayList<>();
        }
        file = event.getFile();
        files.add(file);
        PrimeFaces.current().ajax().update("messages");
        JsfUtil.addInformationMessage("Información", "El archvio se subió correctamente");
        anadirFile();
    }

    public void viewFile(ListarequisitosModel modelFile) {
        requisitosObjeto = modelFile;
        if (modelFile.getFile() != null) {
            try {
                ss.setContentType(requisitosObjeto.getFile().getContentType());
                ss.setNombreDocumento(requisitosObjeto.getFile().getFileName());
                ss.setTempData(requisitosObjeto.getFile().getInputstream());
            } catch (IOException ex) {
                Logger.getLogger(StartProcessController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void subirMemoRentas() {
        ss.setNombreReporte("memorandoRentas");
        ss.setNombreSubCarpeta("GestionTributatia/Recaudacion");
        ss.addParametro("compania", local.getNombreLocal());
        ss.addParametro("ruc", local.getPropietario().getIdentificacionCompleta());
        ss.addParametro("direccion", direccionConpania);
        ss.addParametro("clave", local.getClavePreial());
        ss.addParametro("fecha", new Date());
        ss.addParametro("secuencia", secuenciasServices.findNumberByCodigo("SECUENCIA_MEMORANDUM_RENTAS").getSecuencia().toString());
        ss.setImprimir(Boolean.FALSE);
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
    }

    public void seleccionarCliente(Cliente cl) {
        this.cliente = cl;
        JsfUtil.addInformationMessage("", "Cliente seleccionado" + cliente.getNombreCompleto());
        JsfUtil.executeJS("PF('dlgClientes').hide()");
    }

    public void seleccionaPredio(FinaRenLocalComercial loca) {
        this.local = loca;
        CatPredio predio = new CatPredio();
        predio = predioService.getPredioId(local.getPredio());
        direccionConpania = predio.getDireccion();
        rucCompania = local.getPropietario().getIdentificacionCompleta();
        JsfUtil.addInformationMessage("", "Local seleccionado" + loca.getClavePreial());
        JsfUtil.executeJS("PF('dlgPredios').hide()");
    }

    public void buscarClienteCiRuc() {
        if (identificacion.isEmpty()) {
            JsfUtil.executeJS("PF('dlgClientes').show()");
        } else {
            cliente = (Cliente) clienteService.buscarCliente(identificacion);
        }

        if (cliente == null) {
            cliente = new Cliente();
            JsfUtil.addInformationMessage("", "NO SE ENCUENTRA REGISTRADO");
        } else {
            JsfUtil.addInformationMessage("", "Cliente Seleccionado " + cliente.getNombreCompleto());
        }
    }

    public void buscarPredio() {
        if (claveCatastral.isEmpty()) {
            JsfUtil.executeJS("PF('dlgPredios').show()");
        }
//        else {
//            local = (CatPredio) predioService.getPredioByClaveCat(claveCatastral);
//        }
//        if (predio == null) {
//            predio = new CatPredio();
//            JsfUtil.addInformationMessage("", "Predio No encontrado, comunicarse con catastro para registrar el predio");
//        } else {
//            JsfUtil.addInformationMessage("", "Predio Seleccionado" + predio.getClaveCat());
//        }
    }
    
    private long getTime(long currentTimeMillis) {
        long cm = System.currentTimeMillis();
        return cm - currentTimeMillis;
    }

    //<editor-fold defaultstate="collapsed" desc="SETTER AND GETTER">
    public List<TipoTramite> getTipoTramites() {
        return tipoTramites;
    }

    public void setTipoTramites(List<TipoTramite> tipoTramites) {
        this.tipoTramites = tipoTramites;
    }

    public List<ListarequisitosModel> getListaRequisitosGlobal() {
        return listaRequisitosGlobal;
    }

    public void setListaRequisitosGlobal(List<ListarequisitosModel> listaRequisitosGlobal) {
        this.listaRequisitosGlobal = listaRequisitosGlobal;
    }

    public List<ProcedimientoRequisito> getListaRequisitos() {
        return listaRequisitos;
    }

    public void setListaRequisitos(List<ProcedimientoRequisito> listaRequisitos) {
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

    public List<UploadedFile> getFiles() {
        return files;
    }

    public void setFiles(List<UploadedFile> files) {
        this.files = files;
    }

    public Boolean getParameter() {
        return parameter;
    }

    public void setParameter(Boolean parameter) {
        this.parameter = parameter;
    }

    public ListarequisitosModel getRequisitosObjeto() {
        return requisitosObjeto;
    }

    public void setRequisitosObjeto(ListarequisitosModel requisitosObjeto) {
        this.requisitosObjeto = requisitosObjeto;
    }

    public List<TipoTramite> getSubTipoTramites() {
        return subTipoTramites;
    }

    public void setSubTipoTramites(List<TipoTramite> subTipoTramites) {
        this.subTipoTramites = subTipoTramites;
    }

    public List<Tramite> getTramites() {
        return tramites;
    }

    public void setTramites(List<Tramite> tramites) {
        this.tramites = tramites;
    }

    public Tramite getTramiteEntity() {
        return tramiteEntity;
    }

    public void setTramiteEntity(Tramite tramiteEntity) {
        this.tramiteEntity = tramiteEntity;
    }

    public String getNombreCompania() {
        return nombreCompania;
    }

    public void setNombreCompania(String nombreCompania) {
        this.nombreCompania = nombreCompania;
    }

    public String getRucCompania() {
        return rucCompania;
    }

    public void setRucCompania(String rucCompania) {
        this.rucCompania = rucCompania;
    }

    public String getDireccionConpania() {
        return direccionConpania;
    }

    public void setDireccionConpania(String direccionConpania) {
        this.direccionConpania = direccionConpania;
    }

    public String getClaveCatastral() {
        return claveCatastral;
    }

    public void setClaveCatastral(String claveCatastral) {
        this.claveCatastral = claveCatastral;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public Usuarios getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuarios usuario) {
        this.usuario = usuario;
    }

    public FinaRenLocalComercial getLocal() {
        return local;
    }

    public void setLocal(FinaRenLocalComercial local) {
        this.local = local;
    }

    public LazyModel<Cliente> getClientes() {
        return clientes;
    }

    public void setClientes(LazyModel<Cliente> clientes) {
        this.clientes = clientes;
    }

    public LazyModel<FinaRenLocalComercial> getLocalesLazy() {
        return localesLazy;
    }

    public void setLocalesLazy(LazyModel<FinaRenLocalComercial> localesLazy) {
        this.localesLazy = localesLazy;
    }

//</editor-fold>
}
