/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ventanilla.Controller;

import com.asgard.Entity.FinaRenRubrosLiquidacion;
import com.gestionTributaria.Commons.SisVars;
import com.gestionTributaria.Services.FinaRenTipoLiquidacionService;
import com.origami.sigef.common.entities.UnidadAdministrativa;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.FilesUtil;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import com.ventanilla.Entity.Servicio;
import com.ventanilla.Entity.ServicioRequisito;
import com.ventanilla.Entity.ServicioTipo;
import com.ventanilla.Entity.TipoContribuyentes;
import com.ventanilla.Services.VentanillaService;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author Administrator
 */
@Named
@ViewScoped
public class ServicioRequisitosMB implements Serializable {

    @Inject
    private VentanillaService ventanillaService;
    @Inject
    private FinaRenTipoLiquidacionService tipoLiquidacionService;
    @Inject
    private UserSession userSession;

    private ServicioRequisito servicioRequisito;
    private ServicioTipo servicioTipo;
    private LazyModel<ServicioRequisito> lazy;
    private List<Servicio> servicios;
    private List<ServicioTipo> servicioTipos;
    private Servicio servicio;
    private boolean view = false;
    private String imagenRequisito;

    private UploadedFile file;
    private List<UnidadAdministrativa> departamentos;
    private String imagenItem;
    private TipoContribuyentes tipoContribuyente;
    private List<TipoContribuyentes> tipoContribuyentes;

    private List<ServicioRequisito> listDocServicioRequisito;

    /*PARA VINCULAR EL REQUISITO CON UN RUBRO*/
    private List<FinaRenRubrosLiquidacion> rubros;

    @PostConstruct
    public void init() {
        loadModel();
    }

    public void loadModel() {
        System.out.println("init");
        servicioRequisito = new ServicioRequisito();
        rubros = new ArrayList<>();
        departamentos = ventanillaService.findAllQuery(
                "SELECT u FROM UnidadAdministrativa u WHERE u.estado = TRUE AND u.estadoActivo = true AND u.padre = 27 ORDER BY nombre ASC",
                null);
        System.out.println("Departamentos:" + departamentos.size());
        this.lazy = new LazyModel<>(ServicioRequisito.class);
        this.lazy.getFilterss().put("activo", true);
        // this.lazy.getSorteds().put("servicioTipo.servicio.departamento.nombre",
        // "ASC");
        servicios = ventanillaService.findAllQuery("SELECT c FROM Servicio c WHERE c.activo = TRUE ORDER BY c.nombre",
                null);
        imagenRequisito = "";

    }

    public void loadServicioRequisitos() {
        this.view = Boolean.FALSE;
        lazy = new LazyModel<>(ServicioRequisito.class);
        lazy.getFilterss().put("activo", true);
        lazy.getSorteds().put("servicioTipo", "ASC");
        servicios = ventanillaService.findAllQuery("SELECT c FROM Servicio c ", null);
    }

    public void abrirDlg(ServicioRequisito re) {
        view = Boolean.FALSE;
        listDocServicioRequisito = new ArrayList<>();
        servicio = new Servicio();
        if (re != null) {
            servicioRequisito = re;
            servicioRequisito.setServicioTipo(re.getServicioTipo());
            servicio = servicioRequisito.getServicioTipo().getServicio();
            cargarServicioTipos();
            listDocServicioRequisito.add(re);
            buscarRubros();
        } else {
            servicioRequisito = new ServicioRequisito();
        }
        JsfUtil.executeJS("PF('dlgNuevoRequisito').show();");
        JsfUtil.update("formNuevoRequisito");
        //JsfUtil.update("panelServicioTipo");
    }

    public void vaciarFormulario() {
        servicioRequisito = new ServicioRequisito();
        servicio = new Servicio();
        JsfUtil.update("formNuevoRequisito");
    }

    public void cargarServicioTipos() {
        if (servicio != null) {
            Map<String, Object> params = new HashMap<>();
            params.put("servicio", servicio);
            servicioTipos = ventanillaService.findAllQuery(
                    "SELECT st FROM ServicioTipo st WHERE st.estado=true AND st.servicio=:servicio", params);
            buscarRubros();
        }
    }

    public Boolean validarCampos() {

       if (servicioRequisito.getNombre().equals("")){
            JsfUtil.addWarningMessage("NOMBRE", "Ingrese el nombre del requisito");
            return false;
        }
        if (servicio == null ||  servicio.getId() == null) {
            JsfUtil.addWarningMessage("SERVICIO", "Seleccione un servicio");
            return false;
        }
        if (servicioRequisito.getServicioTipo() == null || servicioRequisito.getServicioTipo().getId() == null) {
            JsfUtil.addWarningMessage("TIPO", "Seleccione el tipo de contribuyente");
            return false;
        }
        
        return true;
    }
    
    public void saveUpdate() {
        try {
            boolean edit = servicioRequisito.getId() != null;
            if (validarCampos()) {
                if (edit) {

                    servicioRequisito.setUsuarioModifica(userSession.getNameUser());
                    servicioRequisito.setFechaModifica(new Date());
                    ventanillaService.updateEntity(servicioRequisito);

                    PrimeFaces.current().executeScript("PF('dlgNuevoRequisito').hide()");
                    PrimeFaces.current().ajax().update("requisitosServiciosList");
                    JsfUtil.addSuccessMessage("Requisito", (edit ? "Editado" : " Registrado") + " con éxito.");
                    vaciarFormulario();
                } else {
                    servicioRequisito.setUsuarioCreacion(userSession.getNameUser());
                    servicioRequisito.setFechaCreacion(new Date());
                    servicioRequisito.setActivo(true);

                    ventanillaService.updateEntity(servicioRequisito);

                    JsfUtil.executeJS("PF('dlgNuevoRequisito').hide()");
                    PrimeFaces.current().ajax().update("requisitosServiciosList");
                    JsfUtil.addSuccessMessage("Requisito", (edit ? "Editado" : " Registrado") + " con éxito.");
                    vaciarFormulario();
                }
            } 
        } catch (Exception ex) {
            JsfUtil.addWarningMessage("", "La Transacción no se pudo completar");
        }
    }

    public void delete(ServicioRequisito servicioRequisito) {
        servicioRequisito.setActivo(Boolean.FALSE);
        ventanillaService.save(servicioRequisito);
        JsfUtil.addSuccessMessage("Requisito", servicioRequisito.getNombre() + " eliminado con éxito");
        PrimeFaces.current().ajax().update("requisitosServiciosList");
    }

    public void selectionServicio(ServicioTipo ser) {
        servicioRequisito.setServicioTipo(ser);
        JsfUtil.addInformationMessage("", "El Servicio se seleccion correctamente");
    }

    public void ver(ServicioRequisito s) {
        servicioRequisito = new ServicioRequisito();
        view = Boolean.TRUE;
        servicioRequisito = s;
        JsfUtil.executeJS("PF('dlgNuevoRequisito').show();");
        JsfUtil.update("formNuevoRequisito");
    }

    public void handleFileUploadItem(FileUploadEvent event) {
        try {
            imagenItem = event.getFile().getFileName();
            System.out.println("Imagen Item:" + imagenItem);
            File f = Utils.copyFileServer(event.getFile(), SisVars.RUTA_DOCUMENTOS_VENTANILLA_REQUISITOS);
            servicioRequisito.setUrlDocumento(SisVars.RUTA_DOCUMENTOS_VENTANILLA_REQUISITOS + f.getName());
            servicioRequisito.setNombreDocumento(imagenItem);
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addInformationMessage("Información", "El archivo se subió correctamente");
            // servicioRequisito.setUrlDocumento(SisVars.wsMedia + "resource/image/" +
            // f.getName());
        } catch (IOException ex) {
            JsfUtil.addErrorMessage("Requisitos", "Ocurrió un error al subir el archivo");
        }
    }

    public void eliminarDocumento(ServicioRequisito re) {
        FilesUtil.eliminarArchivoServer(re.getUrlDocumento());
        re.setUrlDocumento(null);
        re.setNombreDocumento("");
        ventanillaService.save(re);
        imagenItem = "";
        JsfUtil.addSuccessMessage("Requisito", "Documento eliminado con éxito");
        PrimeFaces.current().ajax().update("formNuevoRequisito");
    }

    public void buscarRubros() {
        if (servicio != null && servicio.getId() != null && servicioRequisito.getTasa()) {
            rubros = tipoLiquidacionService.obtenerRubros(servicio.getTipoLiquidacion() != null ? servicio.getTipoLiquidacion().getId() : null);
        } else {
            rubros = new ArrayList<>();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public List<FinaRenRubrosLiquidacion> getRubros() {
        return rubros;
    }

    public void setRubros(List<FinaRenRubrosLiquidacion> rubros) {
        this.rubros = rubros;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public List<UnidadAdministrativa> getDepartamentos() {
        return departamentos;
    }

    public void setDepartamentos(List<UnidadAdministrativa> departamentos) {
        this.departamentos = departamentos;
    }

    public String getImagenItem() {
        return imagenItem;
    }

    public void setImagenItem(String imagenItem) {
        this.imagenItem = imagenItem;
    }

    public ServicioTipo getServicioTipo() {
        return servicioTipo;
    }

    public void setServicioTipo(ServicioTipo servicioTipo) {
        this.servicioTipo = servicioTipo;
    }

    public TipoContribuyentes getTipoContribuyente() {
        return tipoContribuyente;
    }

    public void setTipoContribuyente(TipoContribuyentes tipoContribuyente) {
        this.tipoContribuyente = tipoContribuyente;
    }

    public List<TipoContribuyentes> getTipoContribuyentes() {
        return tipoContribuyentes;
    }

    public void setTipoContribuyentes(List<TipoContribuyentes> tipoContribuyentes) {
        this.tipoContribuyentes = tipoContribuyentes;
    }

    public List<ServicioRequisito> getListDocServicioRequisito() {
        return listDocServicioRequisito;
    }

    public void setListDocServicioRequisito(List<ServicioRequisito> listDocServicioRequisito) {
        this.listDocServicioRequisito = listDocServicioRequisito;
    }

    public ServicioRequisito getServicioRequisito() {
        return servicioRequisito;
    }

    public void setServicioRequisito(ServicioRequisito servicioRequisito) {
        this.servicioRequisito = servicioRequisito;
    }

    public LazyModel<ServicioRequisito> getLazy() {
        return lazy;
    }

    public void setLazy(LazyModel<ServicioRequisito> lazy) {
        this.lazy = lazy;
    }

    public List<Servicio> getServicios() {
        return servicios;
    }

    public void setServicios(List<Servicio> servicioRequisitos) {
        this.servicios = servicioRequisitos;
    }

    public boolean isView() {
        return view;
    }

    public void setView(boolean view) {
        this.view = view;
    }

    public String getImagenRequisito() {
        return imagenRequisito;
    }

    public List<ServicioTipo> getServicioTipos() {
        return servicioTipos;
    }

    public void setServicioTipos(List<ServicioTipo> servicioTipos) {
        this.servicioTipos = servicioTipos;
    }

    public Servicio getServicio() {
        return servicio;
    }

    public void setServicio(Servicio servicio) {
        this.servicio = servicio;
    }

    public void setImagenRequisito(String imagenRequisito) {
        this.imagenRequisito = imagenRequisito;
    }

    // </editor-fold>
}
