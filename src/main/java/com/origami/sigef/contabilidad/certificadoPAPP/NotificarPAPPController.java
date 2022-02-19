/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.certificadoPAPP;

import com.origami.sigef.catalogoItem.service.CatalogoItemService;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.Distributivo;
import com.origami.sigef.common.entities.MasterCatalogo;
import com.origami.sigef.common.entities.PappProceso;
import com.origami.sigef.common.entities.ProductoProceso;
import com.origami.sigef.common.models.Correo;
import com.origami.sigef.common.service.KatalinaService;
import com.origami.sigef.common.service.MasterCatalogoService;
import com.origami.sigef.common.service.PappProcesoService;
import com.origami.sigef.common.service.ProductoProcesoService;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.contabilidad.service.PlanAnualProgramaProyectoService;
import com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot;
import com.origami.sigef.resource.procesos.entities.Observaciones;
import java.io.Serializable;
import java.math.BigInteger;
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
import org.primefaces.model.UploadedFile;

/**
 *
 * @author ORIGAMI2
 */
@Named(value = "notificarLegalView")
@ViewScoped
public class NotificarPAPPController extends BpmnBaseRoot implements Serializable {

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
    private KatalinaService katalinaService;
    @Inject
    private PappProcesoService pappService;
    @Inject
    private ProductoProcesoService productoProcesoService;

    private OpcionBusqueda busqueda;
    private Distributivo d;
    private List<UploadedFile> files;
    private CatalogoItem catalogoItem;
    private CatalogoItem catalogoItem2;
    private CatalogoItem catalogoItem3;
    private String email;
    private String asunto;
    private String detalle;
    private BigInteger numTramite;
    private MasterCatalogo planProgramaProyecto;
    private PappProceso pappProceso;
    private List<PappProceso> listaPlanPapp;
    private List<ProductoProceso> listProductoPro;
    private static final Logger LOG = Logger.getLogger(PlanAnualProgramaProyectoService.class.getName());

    @PostConstruct
    public void init() {
        if (this.session.getTaskID() != null) {
            if (!JsfUtil.isAjaxRequest()) {
                this.setTaskId(this.session.getTaskID());
                observacion = new Observaciones();
                numTramite = new BigInteger("" + this.tramite.getNumTramite());
            }
        }
        busqueda = new OpcionBusqueda();
        catalogoItem = catalogoItemService.getEstadoRol("PROLEGACT");
        catalogoItem2 = catalogoItemService.getEstadoRol("PROEMIACT");
        catalogoItem3 = catalogoItemService.getEstadoRol("PROTERMACT");
        planProgramaProyecto = masterCatalogoService.getMasterCatalogo("tipo_cuenta", "PA", busqueda.getAnio());
        d = clienteService.getuusuarioLogeado(userSession.getNameUser());
//        if (d != null) {
        listaPlanPapp = planAnualProgramaProyectoService.planProcesoSgte(catalogoItem, numTramite, busqueda.getAnio());
//        }
    }

    public void openDialogAmortizacion(PappProceso plan) {
        this.pappProceso = plan;
        listProductoPro = planAnualProgramaProyectoService.productoPropceso(catalogoItem2, numTramite, plan.getActividadOperativa());
        PrimeFaces.current().executeScript("PF('dlgAnt').show()");
        PrimeFaces.current().ajax().update("frmAnt");
    }

    public void openDialogoCorreo(PappProceso plan) {
        this.pappProceso = plan;
        listProductoPro = planAnualProgramaProyectoService.productoPropceso(catalogoItem2, numTramite, plan.getActividadOperativa());
        PrimeFaces.current().executeScript("PF('dlgcorreo').show()");
        PrimeFaces.current().ajax().update("frmcorreo");
    }

    public void enviarCorreo(PappProceso plan) throws Throwable {
        try {
            this.pappProceso = plan;
            listProductoPro = planAnualProgramaProyectoService.productoPropceso(catalogoItem2, numTramite, plan.getActividadOperativa());
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
//            List<CorreoArchivo> archivos = new ArrayList();
//            Map<String, Object> parametros = new HashMap();
//            parametros.put("tramite", impresion.getNumeroTramite());
//            String rutaArchivo = katalinaService.buildJasper(impresion.getId(), "\\activos\\actaEntregaRecepInventario", parametros);
//            CorreoArchivo archivo = new CorreoArchivo("Reporte", Utils.encodeFileToBase64Binary(rutaArchivo), "pdf");
//            archivos.add(archivo);
//            c.setArchivos(archivos);
            katalinaService.enviarCorreo(c);
            JsfUtil.addSuccessMessage("Correo", "La notificacion fue enviada con exito a la direccion de email: " + pappProceso.getEmail() + " relacionada con: " + pappProceso.getUsuarioSolicita());
            this.pappProceso.setEstadoProceso(catalogoItem3);
            this.pappProceso.setFechaTramite(new Date());
            pappService.edit(pappProceso);
            productoCertificado();
            super.completeTask(this.session.getTaskID(), (HashMap<String, Object>) getParamts());
            this.continuar();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "completas tareas", e);
        }
        email = "";
        asunto = "";
        detalle = "";
        PrimeFaces.current().executeScript("PF('dlgcorreo').hide()");
    }

    public void productoCertificado() {
        for (ProductoProceso p : listProductoPro) {
            p.setEstadoProceso(catalogoItem3);
            productoProcesoService.edit(p);
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
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

}
