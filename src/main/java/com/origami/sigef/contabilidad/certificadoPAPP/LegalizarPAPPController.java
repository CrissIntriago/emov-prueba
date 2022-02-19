/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.certificadoPAPP;

import com.gestionTributaria.Commons.SisVars;
import com.origami.sigef.catalogoItem.service.CatalogoItemService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.config.RolUsuario;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.Distributivo;
import com.origami.sigef.common.entities.FirmaElectronica;
import com.origami.sigef.common.entities.MasterCatalogo;
import com.origami.sigef.common.entities.PappProceso;
import com.origami.sigef.common.entities.ProductoProceso;
import com.origami.sigef.common.entities.Usuarios;
import com.origami.sigef.common.model.Imagenes;
import com.origami.sigef.common.models.Correo;
import com.origami.sigef.common.service.KatalinaService;
import com.origami.sigef.common.service.MasterCatalogoService;
import com.origami.sigef.common.service.PappProcesoService;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.FilesUtil;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.contabilidad.service.PlanAnualProgramaProyectoService;
import com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot;
import com.origami.sigef.resource.procesos.entities.Observaciones;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author ORIGAMI2
 */
@Named(value = "legalizarCertificadoView")
@ViewScoped
public class LegalizarPAPPController extends BpmnBaseRoot implements Serializable {

    @Inject
    private MasterCatalogoService masterCatalogoService;
    @Inject
    private PlanAnualProgramaProyectoService planAnualProgramaProyectoService;
    @Inject
    private UserSession userSession;
    @Inject
    private ClienteService clienteService;
    @Inject
    private CatalogoItemService catalogoItemService;
    @Inject
    private PappProcesoService pappService;
    @Inject
    private ServletSession serveltSession;
    @Inject
    private KatalinaService katalinaService;

    private OpcionBusqueda busqueda;
    private Distributivo d;
    private List<UploadedFile> files;
    private UploadedFile uploadedFile;
    private CatalogoItem catalogoItem;
    private CatalogoItem catalogoItem2;
    private CatalogoItem catalogoItem3;
    private BigInteger numTramite;
    private MasterCatalogo planProgramaProyecto;
    private PappProceso pappProceso;
    private String rutaArchivoView;
    private List<PappProceso> listaPlanPapp;
    private List<ProductoProceso> listProductoPro;

    private String clave;
    private Boolean existenImagenes;
    private List<Imagenes> imagenesPdfs;
    private FirmaElectronica firmaElectronica;
    private String nombreArchivo;

    @PostConstruct
    public void init() {
        if (this.session.getTaskID() != null) {
            if (!JsfUtil.isAjaxRequest()) {
                this.setTaskId(this.session.getTaskID());
                observacion = new Observaciones();
                numTramite = new BigInteger("" + this.tramite.getNumTramite());
            }
        }
        pappProceso = new PappProceso();
        catalogoItem = catalogoItemService.getEstadoRol("PROLEGACT");
        catalogoItem2 = catalogoItemService.getEstadoRol("PROCERTACT");
        catalogoItem3 = catalogoItemService.getEstadoRol("PROEMIACT");
        busqueda = new OpcionBusqueda();
        planProgramaProyecto = masterCatalogoService.getMasterCatalogo("tipo_cuenta", "PA", busqueda.getAnio());
        d = clienteService.getuusuarioLogeado(userSession.getNameUser());
//        if (d != null) {
        listaPlanPapp = planAnualProgramaProyectoService.planProcesoSgte(catalogoItem2, numTramite, busqueda.getAnio());
//        }
    }

    public void completarTarea(PappProceso plan) {
        if (plan != null) {
            plan.setEstadoProceso(catalogoItem);
            plan.setFechaTramite(new Date());
            pappService.edit(plan);
//        getParamts().put("usuario_6", session.getNameUser());
//        getParamts().put("idServidor", session.getUserId());
            Correo c = new Correo();
            c.setDestinatario(pappProceso.getEmail());
            c.setAsunto("SOLICITUD DE CERTIFICACION PAPP");
            c.setMensaje("<html lang=\"es\">\n"
                    + "<head>\n"
                    + "<meta charset=\"utf-8\"/>\n"
                    + "</head>\n"
                    + "<body>\n"
                    + "<p style=\"width:200px;\">SR(a). " + pappProceso.getUsuarioSolicita() + "\n POR MEDIO DE LA PRESENTE SE LE INFORMA QUE LA JEFATURA APROBÓ SU SOLICITUD DE CERTIFICACION DE PAPP"
                    + " SEGÚN EL NUMERO DE TRÁMITE N° " + pappProceso.getNumeroTramite() + " </p>\n"
                    + "</body>\n"
                    + "</html>");
            katalinaService.enviarCorreo(c);
            JsfUtil.addSuccessMessage("Correo", "La notificacion fue enviada con exito a la direccion de email: " + pappProceso.getEmail() + " relacionada con: " + pappProceso.getUsuarioSolicita());
        }
        super.completeTask(this.session.getTaskID(), (HashMap<String, Object>) getParamts());
        this.continuar();
    }

    public void openDialogAmortizacion(PappProceso plan) {
        this.pappProceso = plan;
        listProductoPro = planAnualProgramaProyectoService.productoPropceso(catalogoItem3, numTramite, plan.getActividadOperativa());
        PrimeFaces.current().executeScript("PF('dlgAnt').show()");
        PrimeFaces.current().ajax().update("frmAnt");
    }

    public String usuarioSolicita(PappProceso plan) {
        Usuarios user = clienteService.getUsuarioServidor(plan.getUsuarioSolicita());
        if (user != null && user.getFuncionario() != null && user.getFuncionario().getPersona() != null) {
            return user.getFuncionario().getPersona().getApellido() + " " + user.getFuncionario().getPersona().getNombre();
        } else {
            return plan.getUsuarioSolicita();
        }
    }

    public void generarCertificado(PappProceso plan) {
        pappProceso = plan;
        nombreArchivo = plan.getCodigo();
        serveltSession.addParametro("id_plan", plan.getId());
        serveltSession.addParametro("user_solicita", usuarioSolicita(plan));
        serveltSession.addParametro("num_tramite", plan.getNumeroTramite().intValue());
        Distributivo distributivo = clienteService.getuusuarioLogeado(clienteService.getrolsUser(RolUsuario.planificacion));
        serveltSession.addParametro("plan_identificacion", distributivo != null ? distributivo.getServidorPublico().getPersona().getIdentificacionCompleta() : "");
        serveltSession.addParametro("plan_nombre", distributivo != null ? distributivo.getServidorPublico().getPersona().getNombreCompleto() : "");
        serveltSession.addParametro("plan_cargo", distributivo != null ? distributivo.getCargo().getNombreCargo() : "");
        serveltSession.setNombreReporte("certificadoPAPP");
        serveltSession.setNombreSubCarpeta("CertificadoPAPP");
        serveltSession.addParametro("localSave", true);
        serveltSession.addParametro("dirLocalNameFile", SisVars.RUTA_FILES_CERTIFICACIONPAPP + nombreArchivo);
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
        plan.setRutaCertificado(SisVars.RUTA_FILES_CERTIFICACIONPAPP + nombreArchivo + ".pdf");
    }

    public void handleFileUpload(FileUploadEvent event) {
        try {
            if (pappProceso.getRutaCertificado() != null) {
                FilesUtil.eliminarArchivoServer(pappProceso.getRutaCertificado());
            }
//            uploadedFile = event.getFile();
            File copyFile = FilesUtil.copyFileServer1(event, SisVars.RUTA_FILES_CERTIFICACIONPAPP);
            pappProceso.setRutaCertificado(copyFile.getAbsolutePath());
            pappService.edit(pappProceso);
            JsfUtil.addInformationMessage("Información", "El archivo se subió correctamente.");
            PrimeFaces.current().executeScript("PF('DlgoDocumento').hide()");
            PrimeFaces.current().ajax().update("formDocumento", "messages");
        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.addErrorMessage("Ocurrió un error al subir el archivo", "");
        }
    }

    /*TRUE PARA DESCARGAR*/
    public void viewDocument(Boolean download) throws FileNotFoundException {
        if (pappProceso.getRutaCertificado() != null) {
            serveltSession.addParametro("download", download);
            serveltSession.setNombreDocumento(pappProceso.getRutaCertificado());
            JsfUtil.redirectNewTab(CONFIG.URL_APP + "ViewSystemDocs");
        } else {
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addWarningMessage("Información", "No existe ningun archivo.");
        }
    }

    public void previoFirmar(PappProceso s) {
        pappProceso = new PappProceso();
        pappProceso = s;
        rutaArchivoView = s.getRutaCertificado();
//        firmaElectronica.setArchivoFirmar(rutaArchivoView);
    }

    public OpcionBusqueda getBusqueda() {
        return busqueda;
    }

    public void setBusqueda(OpcionBusqueda busqueda) {
        this.busqueda = busqueda;
    }

    public MasterCatalogo getPlanProgramaProyecto() {
        return planProgramaProyecto;
    }

    public void setPlanProgramaProyecto(MasterCatalogo planProgramaProyecto) {
        this.planProgramaProyecto = planProgramaProyecto;
    }

    public PappProceso getPappProceso() {
        return pappProceso;
    }

    public void setPappProceso(PappProceso pappProceso) {
        this.pappProceso = pappProceso;
    }

    public List<PappProceso> getListaPlanPapp() {
        return listaPlanPapp;
    }

    public void setListaPlanPapp(List<PappProceso> listaPlanPapp) {
        this.listaPlanPapp = listaPlanPapp;
    }

    public List<ProductoProceso> getListProductoPro() {
        return listProductoPro;
    }

    public void setListProductoPro(List<ProductoProceso> listProductoPro) {
        this.listProductoPro = listProductoPro;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public String getRutaArchivoView() {
        return rutaArchivoView;
    }

    public void setRutaArchivoView(String rutaArchivoView) {
        this.rutaArchivoView = rutaArchivoView;
    }

    public List<UploadedFile> getFiles() {
        return files;
    }

    public void setFiles(List<UploadedFile> files) {
        this.files = files;
    }

    public Boolean getExistenImagenes() {
        return existenImagenes;
    }

    public void setExistenImagenes(Boolean existenImagenes) {
        this.existenImagenes = existenImagenes;
    }

    public List<Imagenes> getImagenesPdfs() {
        return imagenesPdfs;
    }

    public void setImagenesPdfs(List<Imagenes> imagenesPdfs) {
        this.imagenesPdfs = imagenesPdfs;
    }

    public FirmaElectronica getFirmaElectronica() {
        return firmaElectronica;
    }

    public void setFirmaElectronica(FirmaElectronica firmaElectronica) {
        this.firmaElectronica = firmaElectronica;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

}
