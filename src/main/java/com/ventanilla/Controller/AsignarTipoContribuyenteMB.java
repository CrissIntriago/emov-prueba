/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ventanilla.Controller;

import com.origami.sigef.common.entities.UnidadAdministrativa;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import com.ventanilla.Entity.Servicio;
import com.ventanilla.Entity.ServicioTipo;
import com.ventanilla.Entity.TipoContribuyentes;
import com.ventanilla.Services.VentanillaService;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 *
 * @author Arturo
 */
@Named
@ViewScoped
public class AsignarTipoContribuyenteMB implements Serializable{
    
    @Inject
    private VentanillaService ventanillaService;
     
    @Inject
    private UserSession userSession;
    
    private List<Servicio> servicios;
    
    private List<UnidadAdministrativa> departamentos;
    
    private List<TipoContribuyentes> tiposContribuyentes;
     
    private ServicioTipo servicioTipo; 
    
    private LazyModel<ServicioTipo> lazyServicioTipo;
    
    private Boolean view;
     
    
     
    @PostConstruct
    public void init(){
        if (!JsfUtil.isAjaxRequest()) {
            loadModel();
        }  
    }
    
    public void loadModel() {
        servicios = ventanillaService.findAllQuery("SELECT u FROM Servicio u WHERE u.activo = TRUE ORDER BY u.nombre ASC", null);
        departamentos = ventanillaService.findAllQuery("SELECT u FROM UnidadAdministrativa u WHERE u.estado = TRUE ORDER BY u.nombre ASC", null);
        tiposContribuyentes = ventanillaService.findAllQuery("SELECT u FROM TipoContribuyentes u WHERE u.estado = TRUE ORDER BY u.nombre ASC", null);
        servicioTipo = new ServicioTipo();
       
        this.lazyServicioTipo = new LazyModel<>(ServicioTipo.class);
        this.lazyServicioTipo.getFilterss().put("estado", true);
    }

    public void vaciarFormulario() {
        servicioTipo = new ServicioTipo();
        JsfUtil.update("formNuevoItem");
    }

    public void abrirDlg(ServicioTipo data) {
        this.view = Boolean.FALSE;
        if (data != null) {
            this.servicioTipo = data;
        } else {
            this.servicioTipo = new ServicioTipo();
           
        }
        JsfUtil.executeJS("PF('dlgNuevoTipo').show();");
        JsfUtil.update("formNuevoItem");
    }
    
    public Boolean validarCampos() {
        
        if (servicioTipo.getServicio() == null ||  servicioTipo.getServicio().getId() == null) {
            JsfUtil.addWarningMessage("SERVICIO", "Seleccione un servicio");
            return false;
        }
        if (servicioTipo.getTipoContribuyentes() == null || servicioTipo.getTipoContribuyentes().getId() == null) {
            JsfUtil.addWarningMessage("TIPO", "Seleccione el tipo de contribuyente");
            return false;
        }
        
        if(servicioTipo.getServicio() != null ||  servicioTipo.getServicio().getId() != null && 
             (servicioTipo.getTipoContribuyentes() != null || servicioTipo.getTipoContribuyentes().getId() != null)){
            
            Map<String, Object> params = new HashMap<>();
            params.put("tipo", servicioTipo.getTipoContribuyentes().getId());
            params.put("servicio", servicioTipo.getServicio().getId());
            List<ServicioTipo> listRest = (List<ServicioTipo>) ventanillaService
                    .findAllQuery("SELECT s FROM ServicioTipo s  WHERE servicio.id=:servicio AND tipoContribuyentes.id =:tipo AND estado = true", params);

            
            if (!Utils.isEmpty(listRest)) {
                JsfUtil.addWarningMessage("Error",
                        "Ya existe el servicio " + servicioTipo.getServicio().getNombre() + " con el tipo de contribuyente "+ servicioTipo.getTipoContribuyentes().getNombre());
                return false;
            }
        }

        return true;
    }


    public void guardarItem() {
        try {
            boolean edit = servicioTipo.getId() != null;
           
            if (validarCampos()) {
                if (edit) {

                    ventanillaService.updateEntity(servicioTipo);

                    PrimeFaces.current().executeScript("PF('dlgNuevoTipo').hide()");
                    PrimeFaces.current().ajax().update("dtDatos");
                    JsfUtil.addSuccessMessage("SERVICIO", (edit ? "Editado" : " Registrado") + " con éxito.");
                    vaciarFormulario();
                } else {
                    
                    System.out.println("entro add");

                    servicioTipo.setUsuarioCreacion(userSession.getNameUser());
                    servicioTipo.setFechaCreacion(new Date());
                    servicioTipo.setEstado(true);

                    ventanillaService.updateEntity(servicioTipo);

                    PrimeFaces.current().executeScript("PF('dlgNuevoTipo').hide()");
                    PrimeFaces.current().ajax().update("dtDatos");
                    JsfUtil.addSuccessMessage("SERVICIO", (edit ? "Editado" : " Registrado") + " con éxito.");
                    vaciarFormulario();

                }
            }

        
        } catch (Exception e) {
            JsfUtil.addErrorMessage("Error.", "La Transacción no se pudo completar");
            e.printStackTrace();
        }

    }
    
    public void delete(ServicioTipo servicioTipo) {

        servicioTipo.setEstado(Boolean.FALSE);
        ventanillaService.save(servicioTipo);
        JsfUtil.addSuccessMessage("Servicio","Servicio: " +servicioTipo.getServicio().getNombre() + ", Tipo de contribuyente:" + servicioTipo.getTipoContribuyentes().getNombre() +" eliminada con éxito");
        PrimeFaces.current().ajax().update("dtDatos");
    }

    public void ver(ServicioTipo s) {
        this.servicioTipo = new ServicioTipo();
        this.view = Boolean.TRUE;
        this.servicioTipo = s;
        JsfUtil.executeJS("PF('dlgNuevoTipo').show();");
        JsfUtil.update("formNuevoItem");
    }

    public List<Servicio> getServicios() {
        return servicios;
    }

    public void setServicios(List<Servicio> servicios) {
        this.servicios = servicios;
    }

    public ServicioTipo getServicioTipo() {
        return servicioTipo;
    }

    public void setServicioTipo(ServicioTipo servicioTipo) {
        this.servicioTipo = servicioTipo;
    }
    
    public LazyModel<ServicioTipo> getLazyServicioTipo() {
        return lazyServicioTipo;
    }

    public void setLazyServicioTipo(LazyModel<ServicioTipo> lazyServicioTipo) {
        this.lazyServicioTipo = lazyServicioTipo;
    }

    public Boolean getView() {
        return view;
    }

    public void setView(Boolean view) {
        this.view = view;
    }

    public List<TipoContribuyentes> getTiposContribuyentes() {
        return tiposContribuyentes;
    }

    public void setTiposContribuyentes(List<TipoContribuyentes> tiposContribuyentes) {
        this.tiposContribuyentes = tiposContribuyentes;
    }

    public List<UnidadAdministrativa> getDepartamentos() {
        return departamentos;
    }

    public void setDepartamentos(List<UnidadAdministrativa> departamentos) {
        this.departamentos = departamentos;
    }
    
    
     
}
