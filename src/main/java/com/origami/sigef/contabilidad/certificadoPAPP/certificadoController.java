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
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.model.Imagenes;
import com.origami.sigef.common.service.KatalinaService;
import com.origami.sigef.common.service.MasterCatalogoService;
import com.origami.sigef.common.service.PappProcesoService;
import com.origami.sigef.common.service.ProductoProcesoService;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.FilesUtil;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.contabilidad.service.PlanAnualProgramaProyectoService;
import com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot;
import com.origami.sigef.resource.procesos.entities.Observaciones;
import com.origami.sigef.ws.IrisService;
import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.extensions.event.ImageAreaSelectEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author ORIGAMI2
 */
@Named(value = "certificadoPAPPView")
@ViewScoped
public class certificadoController extends BpmnBaseRoot implements Serializable {

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
    private ServletSession serveltSession;
    @Inject
    private PappProcesoService pappService;
    @Inject
    private KatalinaService katalinaService;
    @Inject
    private ProductoProcesoService productoProcesoService;
    @Inject
    private IrisService irisService;

    private OpcionBusqueda busqueda;
    private Distributivo d;
    private List<UploadedFile> files;
    private CatalogoItem catalogoItem;
    private List<MasterCatalogo> periodos;
    private CatalogoItem catalogoItem2;
    private CatalogoItem catalogoItem3;
    private BigInteger numTramite;
    private MasterCatalogo planProgramaProyecto;
    private PappProceso pappProceso;
    private List<PappProceso> listaPlanPapp;
    private LazyModel<PappProceso> lazy;
    private List<ProductoProceso> listProductoPro;

    private UploadedFile uploadedFile;
    private boolean showOptionFirmar;
    private String clave;
    private Boolean existenImagenes;
    private List<Imagenes> imagenesPdfs;
    private FirmaElectronica firmaElectronica;
    private String nombreArchivo;
    private String rutaArchivoView;

    @PostConstruct
    public void init() {
        busqueda = new OpcionBusqueda();
        catalogoItem = catalogoItemService.getEstadoRol("PROCERTACT");
        catalogoItem2 = catalogoItemService.getEstadoRol("PROANAACT");
        catalogoItem3 = catalogoItemService.getEstadoRol("PROEMIACT");
        rutaArchivoView = "";
        if (this.session.getTaskID() != null) {
            if (!JsfUtil.isAjaxRequest()) {
                this.setTaskId(this.session.getTaskID());
                observacion = new Observaciones();
                numTramite = new BigInteger("" + this.tramite.getNumTramite());
            }
        }
        pappProceso = new PappProceso();
        periodos = masterCatalogoService.findByNamedQuery("MasterCatalogo.findByCatalogoAndTipo", new Object[]{"tipo_cuenta", "PA"});
        if (periodos != null) {
            int indice = periodos.size();
            if (!periodos.isEmpty() && indice == 1) {
                busqueda.setAnio(periodos.get(0).getAnio());
            } else {
                busqueda.setAnio(periodos.get(indice - 1).getAnio());
            }
        }
        cargarXanio();
    }

    public void cargarXanio() {
        if (busqueda.getAnio() != null) {
            planProgramaProyecto = masterCatalogoService.getMasterCatalogo("tipo_cuenta", "PA", busqueda.getAnio());
            d = clienteService.getuusuarioLogeado(userSession.getNameUser());
//            if (d != null) {
            listaPlanPapp = planAnualProgramaProyectoService.planProcesoSgte(catalogoItem2, numTramite, busqueda.getAnio());
//            System.out.println("lista plan >>>" + listaPlanPapp.size());
//            }
//        } else {
//            listaPlanPapp = new ArrayList<>();
        }
    }

    public BigDecimal getMontoDisponible(ProductoProceso da) {
        if (da.getProducto() == null) {
            return BigDecimal.ZERO;
        }
        BigDecimal valor = pappService.getValorAproabdo(da.getProducto());
        return da.getProducto().getMontoReformada().subtract(valor);
    }

    public void openDialogAmortizacion(PappProceso plan) {
        this.pappProceso = plan;
        listProductoPro = planAnualProgramaProyectoService.productoPropceso(catalogoItem3, numTramite, plan.getActividadOperativa());
        PrimeFaces.current().executeScript("PF('dlgAnt').show()");
        PrimeFaces.current().ajax().update("frmAnt");
    }

    public void generarCertificado(PappProceso plan) {
        nombreArchivo = plan.getCodigo();
        serveltSession.addParametro("id_plan", plan.getId());
        serveltSession.addParametro("user_solicita", usuarioSolicita(plan));
        serveltSession.addParametro("num_tramite", plan.getNumeroTramite().intValue());
        Distributivo distributivo = clienteService.getuusuarioLogeado(clienteService.getrolsUser(RolUsuario.planificacion));
        serveltSession.addParametro("plan_identificacion", distributivo != null ? distributivo.getServidorPublico().getPersona().getIdentificacion() : "");
        serveltSession.addParametro("plan_nombre", distributivo != null ? distributivo.getServidorPublico().getPersona().getNombre() + " " + distributivo.getServidorPublico().getPersona().getApellido() : "");
        serveltSession.addParametro("plan_cargo", distributivo != null ? distributivo.getCargo().getNombreCargo() + " DE LA " + distributivo.getUnidadAdministrativa().getNombre() : "");
        serveltSession.setNombreReporte("certificadoPAPP");
        serveltSession.setNombreSubCarpeta("CertificadoPAPP");
        serveltSession.addParametro("localSave", true);
        serveltSession.addParametro("dirLocalNameFile", SisVars.RUTA_FILES_CERTIFICACIONPAPP + nombreArchivo);
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
        plan.setRutaCertificado(SisVars.RUTA_FILES_CERTIFICACIONPAPP + nombreArchivo + ".pdf");
        pappService.edit(plan);
    }

    public String usuarioSolicita(PappProceso plan) {
        Usuarios user = clienteService.getUsuarioServidor(plan.getUsuarioSolicita());
        if (user != null && user.getFuncionario() != null && user.getFuncionario().getPersona() != null) {
            return user.getFuncionario().getPersona().getApellido() + " " + user.getFuncionario().getPersona().getNombre();
        } else {
            return plan.getUsuarioSolicita();
        }
    }

    public void completarTarea() throws Throwable {
        try {
//            this.pappProceso = plan;
            listProductoPro = planAnualProgramaProyectoService.productoPropceso(catalogoItem2, numTramite, pappProceso.getActividadOperativa());
//            Correo c = new Correo();
//            c.setDestinatario(pappProceso.getEmail());
//            c.setAsunto("SOLICITUD DE CERTIFICACION PAPP");
//            c.setMensaje("<html lang=\"es\">\n"
//                    + "<head>\n"
//                    + "<meta charset=\"utf-8\"/>\n"
//                    + "</head>\n"
//                    + "<body>\n"
//                    + "<p style=\"width:200px;\">SR(a). " + pappProceso.getUsuarioSolicita() + "\n POR MEDIO DE LA PRESENTE SE LE INFORMA QUE LA JEFATURA APROBÓ SU SOLICITUD DE CERTIFICACION DE PAPP"
//                    + " SEGÚN EL NUMERO DE TRÁMITE N° " + pappProceso.getNumeroTramite() + " </p>\n"
//                    + "</body>\n"
//                    + "</html>");
//            katalinaService.enviarCorreo(c);
//            JsfUtil.addSuccessMessage("Correo", "La notificacion fue enviada con exito a la direccion de emai: " + pappProceso.getEmail() + " relacionada con: " + pappProceso.getUsuarioSolicita());
            this.pappProceso.setEstadoProceso(catalogoItem);
            this.pappProceso.setFechaTramite(new Date());
            pappService.edit(pappProceso);
//            productoCertificado();
            getParamts().put("usuario_5", clienteService.getrolsUser(RolUsuario.planificacion));
            super.completeTask(this.session.getTaskID(), (HashMap<String, Object>) getParamts());
            this.continuar();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "completas tareas", e);
        }
    }

    public void productoCertificado() {
        for (ProductoProceso p : listProductoPro) {
            p.setEstadoProceso(catalogoItem3);
            productoProcesoService.edit(p);
        }
    }

    public void handleFileUpload(FileUploadEvent event) {
        try {
            uploadedFile = event.getFile();
            existenImagenes = Boolean.TRUE;
            File file = FilesUtil.copyFileServer1(event, SisVars.RUTA_FILES_CERTIFICACIONPAPP);
            System.out.println("File: >> " + file.getAbsolutePath());
            firmaElectronica.setArchivoFirmar(file.getAbsolutePath());
            System.out.println("Arch. Firmar: >> " + firmaElectronica.getArchivoFirmar());
            JsfUtil.addSuccessMessage("INFORMACIÓN", "Valide su firma para firmar el Documento");

        } catch (Exception e) {
            JsfUtil.addErrorMessage("Ocurrió un error al subir el archivo", "");
        }
    }

    public FirmaElectronica generarFirmaElectronica(FirmaElectronica firmaElectrica, String tipo) {
        FirmaElectronica archivoGenerado = new FirmaElectronica();
        if (!firmaElectrica.getClave().isEmpty()) {
            if (Utils.encriptaEnMD5(firmaElectrica.getClave()).equals(userSession.getFirmaElectronica().getClave())) {
                firmaElectrica.setIsuser(userSession.getUsuario().getNameUsuario());
                firmaElectrica.setClave(firmaElectrica.getClave());
                //LLAMANDO DIRECTAMENTE AL METODO GENERAR FIRMA AL WS
                archivoGenerado = (FirmaElectronica) irisService.methodPOST(firmaElectrica, tipo + "firmaElectronica/generar", FirmaElectronica.class);
                firmaElectrica.setClave(Utils.encriptaEnMD5(firmaElectrica.getClave()));
                if (archivoGenerado != null) {
                    JsfUtil.addInformationMessage("", "Elemento guardado con éxito.");
                } else {
                    JsfUtil.addSuccessMessage("", "No se pudo firmar el documento");
                }

            } else {
                JsfUtil.addErrorMessage("", "Clave incorrecta");
            }
        } else {
            JsfUtil.addErrorMessage("", "Ingrese su clave ");
        }

        return archivoGenerado;
    }

    public void previoFirmar(PappProceso s) {
        pappProceso = new PappProceso();
        pappProceso = s;
        rutaArchivoView = s.getRutaCertificado();
//        firmaElectronica.setArchivoFirmar(rutaArchivoView);
    }

    private Boolean validarFirma() {
        if (!clave.isEmpty()) {
            if (userSession.getFirmaElectronica() != null) {
                if (new Date().before(userSession.getFirmaElectronica().getFechaExpiracion())) {
                    if (Utils.encriptaEnMD5(clave).equals(userSession.getFirmaElectronica().getClave())) {
                        System.out.println("Clave correcta");
                        return Boolean.TRUE;
                    } else {
                        JsfUtil.addErrorMessage("", "Clave incorrecta.");
                        return Boolean.FALSE;
                    }
                } else {
                    JsfUtil.addErrorMessage("", "Su firma electrónica esta caducada");
                    return Boolean.FALSE;
                }
            } else {
                JsfUtil.addErrorMessage("", "Suba su firma electrónica, primero");
                return Boolean.FALSE;
            }
        } else {
            JsfUtil.addErrorMessage("", "Ingrese su clave ");
            return Boolean.FALSE;
        }
    }

    public Void selectEndListener(ImageAreaSelectEvent e) {
        if (e.getWidth() == 150 && e.getHeight() == 50) {
            String pagina = e.getComponent().getClientId().replace("dtPdfs:", "").replace(":areaSelect", "");
            firmaElectronica.setClave(clave);
            firmaElectronica.setNumeroPagina(Integer.valueOf(pagina) + 1);
            firmaElectronica.setPosicionX1("" + (e.getX1()));
            firmaElectronica.setPosicionX2("" + (e.getX2()));
            firmaElectronica.setPosicionY1(((e.getImgHeight() - e.getY1())) + "");
            firmaElectronica.setPosicionY2(((e.getImgHeight() - e.getY2())) + "");
            FirmaElectronica archivoGenerado = firmarElectronicamente(firmaElectronica);
            firmaElectronica.setClave(Utils.encriptaEnMD5(clave));//SE LA ENCRIPTA NUEVAMENTE
            //Eliminar Archivos
            if (archivoGenerado != null) {

                String rutaTmp = archivoGenerado.getArchivoFirmado().replace("\\", "/");
                pappProceso.setRutaCertificado(rutaTmp);
                pappService.edit(pappProceso);
//                switch (tarea.getTaskDefinitionKey()) {
//                    case "documentoReservaLegalizacion":
//                        solicitudReservaCompromiso.setRutaReserva(rutaTmp);
//                        break;
//                    case "documento_legalizacion":
//                        solicitudReservaCompromiso.setRutaModificada(rutaTmp);
//                        break;
//                    case "documento_a":
//                        solicitudReservaCompromiso.setRutaAnulada(rutaTmp);
//                        break;
//                    case "documento_l":
//                        solicitudReservaCompromiso.setRutaLiquidada(rutaTmp);
//                        break;
//                    case "documento_liber":
//                        solicitudReservaCompromiso.setRutaLiberada(rutaTmp);
//                        break;
//                    default:
//                        solicitudReservaCompromiso.setRutaOtros(rutaTmp);
//                        break;
//
//                }
//
//                solicitudService.edit(solicitudReservaCompromiso);
                previoFirmar(pappProceso);
            }
            existenImagenes = false;
            imagenesPdfs = new ArrayList<>();
            clave = "";
            PrimeFaces.current().ajax().update("mainForm");
        } else {
            JsfUtil.addErrorMessage("Error", "Debe seleccionar el tamaño completo del rectángulo para la firma electrónica");
        }
        return null;
    }

    public FirmaElectronica firmarElectronicamente(FirmaElectronica firmaElectronica) {
        return (FirmaElectronica) irisService.methodPOST(firmaElectronica, SisVars.wsFirmaEC + "firmaElectronica/generar", FirmaElectronica.class);
    }

    public void validarFirmaImagenPDF() {
        if (validarFirma()) {
            System.out.println("Archivo Firmar: " + firmaElectronica.getArchivoFirmar());
            imagenesPdfs = obtenerImagenesDesdePDF(firmaElectronica.getArchivoFirmar());
            System.out.println("imagenesPdfs: " + imagenesPdfs);
            if (Utils.isNotEmpty(imagenesPdfs)) {
                existenImagenes = Boolean.TRUE;
            }
        }
    }

    public List<Imagenes> obtenerImagenesDesdePDF(String ruta) {
        String urlFile = Utils.getFilterRuta(ruta);
        return (List<Imagenes>) irisService.methodListGET(SisVars.appQrService + "resource/pdfImagenes/" + urlFile, Imagenes[].class);
    }

    public void enviar(PappProceso plan) {
        pappProceso = plan;
        observacion.setEstado(true);
        observacion.setFecCre(new Date());
        observacion.setTarea(tarea.getName());
        observacion.setUserCre(session.getName());
        PrimeFaces.current().executeScript("PF('dlgObservaciones').show()");
        PrimeFaces.current().ajax().update(":frmDlgObser");
    }

    public OpcionBusqueda getBusqueda() {
        return busqueda;
    }

    public void setBusqueda(OpcionBusqueda busqueda) {
        this.busqueda = busqueda;
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

    public MasterCatalogo getPlanProgramaProyecto() {
        return planProgramaProyecto;
    }

    public void setPlanProgramaProyecto(MasterCatalogo planProgramaProyecto) {
        this.planProgramaProyecto = planProgramaProyecto;
    }

    public List<UploadedFile> getFiles() {
        return files;
    }

    public void setFiles(List<UploadedFile> files) {
        this.files = files;
    }

    public LazyModel<PappProceso> getLazy() {
        return lazy;
    }

    public void setLazy(LazyModel<PappProceso> lazy) {
        this.lazy = lazy;
    }

    public List<MasterCatalogo> getPeriodos() {
        return periodos;
    }

    public void setPeriodos(List<MasterCatalogo> periodos) {
        this.periodos = periodos;
    }

    public boolean isShowOptionFirmar() {
        return showOptionFirmar;
    }

    public void setShowOptionFirmar(boolean showOptionFirmar) {
        this.showOptionFirmar = showOptionFirmar;
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

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

}
