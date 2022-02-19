/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ventanilla.Controller;

import com.asgard.Entity.FinaRenTipoLiquidacion;
import com.gestionTributaria.Commons.SisVars;
import com.origami.sigef.common.entities.UnidadAdministrativa;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.FilesUtil;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.service.UnidadAdministrativaService;
import com.origami.sigef.resource.procesos.entities.TipoTramite;
import com.ventanilla.Entity.Servicio;
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

/**
 *
 * @author Arturo
 */
@ViewScoped
@Named
public class ServiciosMB implements Serializable {

    @Inject
    private UnidadAdministrativaService unidadAdministrativaService;
    @Inject
    private UserSession userSession;
    @Inject
    private VentanillaService ventanillaService;

    private Servicio servicio;
    private String abreviatura;
    private List<UnidadAdministrativa> departamentos;
    private LazyModel<Servicio> lazyServicios;
    private boolean view = false;
    private String imagenItem;
    private List<Servicio> listDocServicio;
    private List<FinaRenTipoLiquidacion> listTipoLiquidacion;
    private List<TipoTramite> listTipoTramite;
    
    @PostConstruct
    public void init() {
        loadModel();
        System.out.println("prueba");
    }

    public void loadModel() {
        departamentos = unidadAdministrativaService.findByNamedQuery("UnidadAdministrativa.findByEstado");
        listTipoLiquidacion = ventanillaService.findAllQuery("SELECT tl FROM FinaRenTipoLiquidacion tl where estado = TRUE AND tipo='ESP' ORDER BY nombre_titulo", null);
        listTipoTramite = ventanillaService.findAllQuery("SELECT t FROM TipoTramite t where estado = TRUE ORDER BY descripcion", null);
        this.lazyServicios = new LazyModel<>(Servicio.class);
        this.lazyServicios.getFilterss().put("activo", true);
    }

    public void vaciarFormulario() {
        servicio = new Servicio();
        servicio.setDepartamento(null);
        servicio.setEnLinea(Boolean.FALSE);
        abreviatura = "";
        JsfUtil.update("formNuevoItem");
    }

    public void abrirDlg(Servicio data) {

        this.view = Boolean.FALSE;
        this.listDocServicio = new ArrayList<>();
        if (data != null) {
            this.servicio = data;
            abreviatura = servicio.getAbreviatura();
            listDocServicio.add(data);
        } else {
            this.servicio = new Servicio();
            abreviatura = "";
        }
        JsfUtil.executeJS("PF('dlgNuevoServicio').show();");
        JsfUtil.update("formNuevoItem");
    }
    
    public Boolean validarCampos() {
        if(Utils.isEmptyString(servicio.getNombre())){
            JsfUtil.addWarningMessage("SERVICIO", "Debe ingresar un nombre al servicio");
            return false;
        }
        
        if( Utils.isEmptyString(servicio.getAbreviatura())){
            JsfUtil.addWarningMessage("ABREVIATURA", "Debe ingresar una abreviatura al servicio");
            return false;
        }
        
        if (servicio.getTipoTramite() == null || servicio.getTipoTramite().getId() == null) {
            JsfUtil.addWarningMessage("TIPO", "Seleccione el tipo de trámite");
            return false;
        }
       
        
        return true;
    }

    public void guardarItem() {
        try {
            boolean edit = servicio.getId() != null;
            if (validarCampos()) {
                if (!existeAbreviatura()) {
                    if (edit) {
                        servicio.setUsuarioModifica(userSession.getNameUser());
                        servicio.setFechaModificacion(new Date());
                       
                        ventanillaService.updateEntity(servicio);

                        PrimeFaces.current().executeScript("PF('dlgNuevoServicio').hide()");
                        PrimeFaces.current().ajax().update("dtDatos");
                        JsfUtil.addSuccessMessage("SERVICIO", (edit ? "Editado" : " Registrado") + " con éxito.");
                        vaciarFormulario();
                    } else {

                        servicio.setUsuarioCreacion(userSession.getNameUser());
                        servicio.setFechaCreacion(new Date());
                        servicio.setActivo(true);
                        
                        ventanillaService.updateEntity(servicio);

                        PrimeFaces.current().executeScript("PF('dlgNuevoServicio').hide()");
                        PrimeFaces.current().ajax().update("dtDatos");
                        JsfUtil.addSuccessMessage("SERVICIO", (edit ? "Editado" : " Registrado") + " con éxito.");
                        vaciarFormulario();

                    }
                }
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage("Error.", "La Transacción no se pudo completar");
            e.printStackTrace();
        }

    }

    public Boolean existeAbreviatura() {
        if (abreviatura != null && !abreviatura.isEmpty()) {
            if (!abreviatura.equals(servicio.getAbreviatura())) {
                return validarExistente();
            }
        } else {
            return validarExistente();
        }
        return Boolean.FALSE;
    }

    private Boolean validarExistente() {

        Map<String, Object> params = new HashMap<>();
        params.put("abreviatura", servicio.getAbreviatura());

        List<Servicio> listRest = (List<Servicio>) ventanillaService
                .findAllQuery("SELECT s FROM Servicio s  WHERE abreviatura=:abreviatura AND activo = true", params);
        
        if (!Utils.isEmpty(listRest)) {
            JsfUtil.addErrorMessage("Error",
                    "Ya existe " + servicio.getAbreviatura() + ", debe ingresar otra abreviatura");
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    public void delete(Servicio servicio) {
        servicio.setActivo(Boolean.FALSE);
        ventanillaService.save(servicio);
        JsfUtil.addSuccessMessage("Servicio", servicio.getNombre() + " eliminada con éxito");
        PrimeFaces.current().ajax().update("dtDatos");
    }

    public void ver(Servicio s) {
        this.servicio = new Servicio();
        this.view = Boolean.TRUE;
        this.servicio = s;
        abreviatura = servicio.getAbreviatura();
        JsfUtil.executeJS("PF('dlgNuevoServicio').show();");
        JsfUtil.update("formNuevoItem");
    }

    public void handleFileUploadItem(FileUploadEvent event) {
        try {
            imagenItem = event.getFile().getFileName();

            File f = Utils.copyFileServer(event.getFile(), SisVars.RUTA_DOCUMENTOS_VENTANILLA_SERVICIOS);
            servicio.setUrlImagen(SisVars.RUTA_DOCUMENTOS_VENTANILLA_SERVICIOS + f.getName());
            servicio.setNombreImagen(imagenItem);
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addInformationMessage("Información", "El archivo se subió correctamente");
            JsfUtil.update("formNuevoItem:imagen-item");
        } catch (IOException ex) {
            JsfUtil.addErrorMessage("Servicio", "Ocurrió un error al subir el archivo");
        }
    }

    public void eliminarDocumento(Servicio s) {
        FilesUtil.eliminarArchivoServer(s.getUrlImagen());
        s.setUrlImagen(null);
        s.setNombreImagen("");
        ventanillaService.save(s);
        imagenItem = "";
        JsfUtil.addSuccessMessage("Requisito", "Documento eliminado con éxito");
        PrimeFaces.current().ajax().update("formNuevoItem");
    }

    public Servicio getItem() {
        return servicio;
    }

    public void setItem(Servicio item) {
        this.servicio = item;
    }

    public String getAbreviatura() {
        return abreviatura;
    }

    public void setAbreviatura(String abreviatura) {
        this.abreviatura = abreviatura;
    }

    public List<UnidadAdministrativa> getDepartamentos() {
        return departamentos;
    }

    public void setDepartamentos(List<UnidadAdministrativa> departamentos) {
        this.departamentos = departamentos;
    }

    public LazyModel<Servicio> getLazyServicios() {
        return lazyServicios;
    }

    public void setLazyServicios(LazyModel<Servicio> lazyServicios) {
        this.lazyServicios = lazyServicios;
    }

    public boolean isView() {
        return view;
    }

    public void setView(boolean view) {
        this.view = view;
    }

    public String getImagenItem() {
        return imagenItem;
    }

    public void setImagenItem(String imagenItem) {
        this.imagenItem = imagenItem;
    }

    public List<Servicio> getListDocServicio() {
        return listDocServicio;
    }

    public void setListDocServicio(List<Servicio> listDocServicio) {
        this.listDocServicio = listDocServicio;
    }

    public List<FinaRenTipoLiquidacion> getListTipoLiquidacion() {
        return listTipoLiquidacion;
    }

    public void setListTipoLiquidacion(List<FinaRenTipoLiquidacion> listTipoLiquidacion) {
        this.listTipoLiquidacion = listTipoLiquidacion;
    }

    public List<TipoTramite> getListTipoTramite() {
        return listTipoTramite;
    }

    public void setListTipoTramite(List<TipoTramite> listTipoTramite) {
        this.listTipoTramite = listTipoTramite;
    }

}
