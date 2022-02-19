/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSF/JSFManagedBean.java to edit this template
 */
package com.ventanilla.Controller;

import com.gestionTributaria.Entities.CheckList;
import com.gestionTributaria.Services.CheckListService;
import com.gestionTributaria.Services.SecuenciasServices;
import com.origami.sigef.common.config.RolUsuario;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.entities.SecuenciaGeneral;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.service.interfaces.FileUploadDoc;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot;
import static com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot.LOG;
import com.origami.sigef.resource.procesos.entities.Observaciones;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author Luis Pozo Gonzabay
 */
@Named(value = "informeComisariaPFNMB")
@ViewScoped
public class InformeComisariaPFNMB extends BpmnBaseRoot implements Serializable {

    @Inject
    private ClienteService clienteService;
    @Inject
    private FileUploadDoc uploadDoc;
    @Inject
    private SecuenciasServices secuecniaService;
    @Inject
    private CheckListService checkListService;
    @Inject
    private UserSession session;
    private int opc;
    private UploadedFile file;
    private CheckList memo;
    private String CumpleRequiistos;
    private List<CheckList> memos;
    private Cliente clienteD;
    private Cliente clienteC;
    private String cedulaD;
    private String cedulaC;
    private SecuenciaGeneral secuenciaGeneral;
    private Boolean editar;
    private String observacionesMemo;

    @PostConstruct
    public void init() {
        try {
            if (this.session.getTaskID() != null) {
                this.setTaskId(this.session.getTaskID());
                observacion = new Observaciones();
                observacion.setIdTramite(tramite);
                clienteD = new Cliente();
                clienteC = new Cliente();
                memos = new ArrayList<>();
                memo = new CheckList();
                cedulaD = "";
                cedulaC = "";
                secuenciaGeneral = new SecuenciaGeneral();
                editar = false;
                buscarMemo();
                memo.setAsunto("REMISIÓN DE DOCUMENTACIÓN PARA SOLICITUD DE ORDEN DE PAGO DE TASA DE HABILITACIÓN Y PATENTE");
                memo.setDireccion("ABEL GILBERT PONTON III M< A46 SL 9.");
                clienteD = clienteService.getResponsableXRol("58").getPersona();
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void openDialogUpload() {
        PrimeFaces.current().executeScript("PF('dlgoUpload').show()");
        PrimeFaces.current().ajax().update("formUpload");
    }

    public void handleFileUpload(FileUploadEvent event) {
        file = event.getFile();
        uploadDoc.upload(tramite, file);
        PrimeFaces.current().ajax().update("messages");
        JsfUtil.addInformationMessage("Información", "El archvio se subió correctamente");
        PrimeFaces.current().executeScript("PF('dlgoUpload').hide()");
    }

    public void opendlg(int opc) {
        observacion.setEstado(true);
        observacion.setFecCre(new Date());
        observacion.setTarea(tarea.getName());
        observacion.setUserCre(session.getName());
        this.opc = opc;
        JsfUtil.executeJS("PF('dlgObservaciones').show()");
    }

    public void buscarMemo() {
        CheckList memoran = checkListService.findbyIdTramite(tramite.getNumTramite());
        if (memoran != null) {
            memos.add(memoran);
            clienteD = memoran.getDestinatario();
            clienteC = memoran.getUsuario();
        } else {
            memos = new ArrayList<>();
        }
    }

    public void completarTarea() {
        try {
            String usuario = clienteService.getrolsUser(RolUsuario.secretariaJusticiaVigilancia);
            switch (opc) {
                case 0://Corrección Documentos
                    getParamts().put("usuarioCorreccionDocumentos", usuario.equals("") ? "admin_1" : usuario);
                    getParamts().put("tipo", 1);
                    break;
                case 1://Registro del local
                    getParamts().put("usuarioCorreccionDocumentos", usuario.equals("") ? "admin_1" : usuario);
                    getParamts().put("tipo", 0);
                    break;
            }
            getParamts().put("correccion", opc);
            JsfUtil.executeJS("PF('dlgObservaciones').hide()");
            if (saveTramite() == null) {
                return;
            }
            this.session.setVarTemp(null);
            super.completeTask((HashMap<String, Object>) getParamts());
            JsfUtil.redirectFaces("/procesos/bandeja-tareas");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "completas tareas", e);
        }
    }

    public void buscarCliente() {
        String cedulaRuc = "";
        System.out.println("valor de cedula 1" + cedulaD);
        System.out.println("valor de cedula 2" + cedulaC);
        if (cedulaD.length() == 0) {
            cedulaRuc = cedulaC;
        } else {
            cedulaRuc = cedulaD;
        }
        System.out.println("CLIENTE tamaño " + cedulaRuc.length());
        if (cedulaRuc.length() == 0) {

        }
        //cedula
        if (cedulaRuc.length() == 10) {
            if (cedulaD.length() == 0) {
                clienteC = clienteService.getFindCliente(cedulaRuc);
                memo.setUsuario(clienteC);
            } else {
                clienteD = clienteService.getFindCliente(cedulaRuc);
                memo.setDestinatario(clienteD);
                System.out.println("destinatario: " + memo.getDestinatario());
            }
        }
        //RUC
        if (cedulaRuc.length() == 13) {
            if (cedulaD.length() == 0) {
                clienteC = clienteService.getFindCliente(cedulaRuc);
                System.out.println("CLIENTE " + clienteC);
            } else {
                clienteD = clienteService.getFindCliente(cedulaRuc);
                System.out.println("CLIENTE " + clienteD);
            }
        }
        cedulaD = "";
        cedulaC = "";

    }

    public void guardarMemo() {
        if (memo.getId() == null) {
            secuenciaGeneral = secuecniaService.findNumberByCodigo("SECUENCIA_MEMORAMDUM_COMISARIA_PN");
            memo.setMemorandum("GADMCD-CTM-2021-" + secuenciaGeneral.getSecuencia().toString() + "-M.");
            memo.setTramite(BigInteger.valueOf(tramite.getNumTramite()));
            memo.setFecha(new Date());
            memo = checkListService.create(memo);
            memo.setObservaciones(observacionesMemo);
            memos.add(memo);
        } else {
            checkListService.edit(memo);
        }
    }

    public void visualizarEditar(CheckList memoran, int opc) {
        memo = memoran;
        if (opc == 1) {
            editar = true;
        } else {
            editar = false;
        }
    }

    //<editor-fold defaultstate="collapsed" desc="GET AND SET">
    public CheckList getMemo() {
        return memo;
    }

    public void setMemo(CheckList memo) {
        this.memo = memo;
    }

    public String getCumpleRequiistos() {
        return CumpleRequiistos;
    }

    public void setCumpleRequiistos(String CumpleRequiistos) {
        this.CumpleRequiistos = CumpleRequiistos;
    }

    public List<CheckList> getMemos() {
        return memos;
    }

    public void setMemos(List<CheckList> memos) {
        this.memos = memos;
    }

    public String getCedulaD() {
        return cedulaD;
    }

    public void setCedulaD(String cedulaD) {
        this.cedulaD = cedulaD;
    }

    public String getCedulaC() {
        return cedulaC;
    }

    public void setCedulaC(String cedulaC) {
        this.cedulaC = cedulaC;
    }

    public Cliente getClienteD() {
        return clienteD;
    }

    public void setClienteD(Cliente clienteD) {
        this.clienteD = clienteD;
    }

    public Cliente getClienteC() {
        return clienteC;
    }

    public void setClienteC(Cliente clienteC) {
        this.clienteC = clienteC;
    }

    public Boolean getEditar() {
        return editar;
    }

    public void setEditar(Boolean editar) {
        this.editar = editar;
    }

    public String getObservacionesMemo() {
        return observacionesMemo;
    }

    public void setObservacionesMemo(String observacionesMemo) {
        this.observacionesMemo = observacionesMemo;
    }

//</editor-fold>
}
