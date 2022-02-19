/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.certificacion_presupuesto_anual.controller;

import com.origami.sigef.certificacion_presupuesto_anual.service.ProcedimientoRequisitoService;
import com.origami.sigef.certificacion_presupuesto_anual.service.ProcedimientoService;
import com.origami.sigef.certificacion_presupuesto_anual.service.RequisitoService;
import com.origami.sigef.common.entities.Procedimiento;
import com.origami.sigef.common.entities.ProcedimientoRequisito;
import com.origami.sigef.common.entities.Requisito;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.talentohumano.services.TipoRolService;
import com.origami.sigef.resource.procesos.entities.TipoTramite;
import com.origami.sigef.resource.procesos.services.TipoTramiteService;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 *
 * @author Criss Intriago
 */
@Named(value = "mantRequisitoView")
@ViewScoped
public class MantenimientoRequisitoController implements Serializable {
    
    @Inject
    private ProcedimientoService procedimientoService;
    @Inject
    private UserSession userSession;
    @Inject
    private RequisitoService requisitoService;
    @Inject
    private ProcedimientoRequisitoService procedimientoRequisitoService;
    @Inject
    private TipoTramiteService tipoTramiteService;
    @Inject
    private TipoRolService tipoRolService;
    
    private Requisito requisitoSeleccionado;
    private Procedimiento procedimiento;
    private ProcedimientoRequisito procedimientoRequisito;
    private OpcionBusqueda opcionBusqueda;
    
    private LazyModel<Procedimiento> procedimientoLazy;
    
    private List<TipoTramite> procesoList;
    private List<Requisito> itemsRequisitos;
    private List<ProcedimientoRequisito> procedimientoRequisitoList;
    
    private Boolean soloLectura;
    
    private LazyModel<ProcedimientoRequisito> procedimientoRequisitoLazyModel;
    
    @PostConstruct
    public void initialize() {
        this.opcionBusqueda = new OpcionBusqueda();
        this.requisitoSeleccionado = new Requisito();
        this.procedimientoRequisito = new ProcedimientoRequisito();
        this.procedimientoRequisitoList = new ArrayList<>();
        this.procedimientoLazy = new LazyModel<>(Procedimiento.class);
        this.procedimientoLazy.getFilterss().put("estado", true);
        this.procedimientoLazy.getSorteds().put("id", "ASC");
        this.procedimientoLazy.setDistinct(false);
        procesoList = new ArrayList<>();
        this.procesoList = tipoTramiteService.getListTipoTramites();
        soloLectura = false;
    }
    
    public List<ProcedimientoRequisito> viewRequisitos(Procedimiento procedimiento) {
        List<ProcedimientoRequisito> result = procedimientoRequisitoService.getRequisitosDelProcedimiento(procedimiento);
        return result;
    }

    /*FUNCIONES PARA EL MANTENIMIENTO DE REQUISTO*/
    //Llama al formulario de registro de procedimiento es utilizado tanto para crear uno nuevo o editarlo
    public void formProcedimiento(Procedimiento procedimiento) {
        if (procedimiento != null) {
            //Carga los datos para la edicion y vizualizar la información en el formulario de procedimiento
            this.procedimiento = procedimiento;
            this.procedimientoRequisitoList = procedimientoRequisitoService.getRequisitosDelProcedimiento(procedimiento);
        } else {
            //Inicializa los datos requeridos para el previo registro del procedimiento
            this.procedimiento = new Procedimiento();
            procedimientoRequisitoList = new ArrayList<>();
            
        }
        PrimeFaces.current().ajax().update("formRegistroProcedimiento");
        PrimeFaces.current().executeScript("PF('DlgRegistroProcedimiento').show()");
    }
    
    public void duplicarProcedimiento(Procedimiento procedimiento) {
        if (procedimiento != null) {
            this.procedimiento = Utils.clone(procedimiento);
            this.procedimiento.setFechaCreacion(new Date());
            this.procedimiento.setUsuarioCreacion(userSession.getNameUser());
            this.procedimiento.setId(null);
            this.procedimientoRequisitoList = Utils.clone(procedimientoRequisitoService.getRequisitosDelProcedimiento(procedimiento));
            if (Utils.isNotEmpty(procedimientoRequisitoList)) {
                for (ProcedimientoRequisito req1 : procedimientoRequisitoList) {
                    req1.setIdProcedimiento(this.procedimiento);
                    req1.setId(null);
                }
            }
        }
        PrimeFaces.current().ajax().update("formRegistroProcedimiento");
        PrimeFaces.current().executeScript("PF('DlgRegistroProcedimiento').show()");
    }

    //Llamar al formulario de requisitos para seleccionar los requisitos que tenemos previamente registrados
    public void formRequisito(Requisito requisito) {
        if (requisito != null) {
            this.requisitoSeleccionado = requisito;
        } else {
            this.requisitoSeleccionado = new Requisito();
            this.procedimientoRequisito = new ProcedimientoRequisito();
        }
        this.itemsRequisitos = requisitoService.getRequisitos();
        this.procedimientoRequisito.setObligatorio(Boolean.TRUE);
        PrimeFaces.current().ajax().update("DlgAniadirRequisito");
        PrimeFaces.current().executeScript("PF('DlgAniadirRequisito').show()");
    }

    //Añade el requisito a la lista que se va a guardar para la relacion entre el y el procedimiento
    public void addRequisito() {
        if (requisitoSeleccionado == null) {
            JsfUtil.addErrorMessage("Requisito", "Seleccione un requisito");
            return;
        }
        if (requisitoSeleccionado.getId() == null) {
            JsfUtil.addErrorMessage("Requisito", "Seleccione un requisito");
            return;
        }
        if (procedimiento.getId() != null) {
            ProcedimientoRequisito procedimientoRequisitoTemp = new ProcedimientoRequisito();
            procedimientoRequisitoTemp.setIdProcedimiento(procedimiento);
            procedimientoRequisitoTemp.setIdRequisito(requisitoSeleccionado);
            procedimientoRequisitoTemp.setObligatorio(procedimientoRequisito.getObligatorio());
            procedimientoRequisito = procedimientoRequisitoService.create(procedimientoRequisitoTemp);
            procedimientoRequisitoList = procedimientoRequisitoService.getRequisitosDelProcedimiento(procedimiento);
        } else {
            if (requisitoSeleccionado != null) {
                procedimientoRequisito.setIdRequisito(requisitoSeleccionado);
                procedimientoRequisitoList.add(procedimientoRequisito);
            } else {
                JsfUtil.addWarningMessage("Requisito", "Seleccione un requisito antes de guardar");
                return;
            }
        }
        PrimeFaces.current().executeScript("PF('DlgAniadirRequisito').hide()");
        PrimeFaces.current().ajax().update("mainForm");
        JsfUtil.addSuccessMessage("Requisito", " añadido con éxito");
    }

    //Guardar nuevo procedimiento o los cambios realizado que se le realizaron al procedimiento
    public void saveProcedimiento() {
        if (procedimiento.getIdTipoTramite() != null && !procedimiento.getNombre().equals("") && !procedimiento.getDescripcion().equals("")) {
            if (procedimientoRequisitoList.size() >= 1) {
                boolean edit = procedimiento.getId() != null;
                if (edit) {
                    procedimiento.setNombre(procedimiento.getNombre().toUpperCase());
                    procedimiento.setDescripcion(procedimiento.getDescripcion().toUpperCase());
                    procedimiento.setUsuarioModificacion(userSession.getNameUser());
                    procedimiento.setFechaModificacion(new Date());
                    procedimientoService.edit(procedimiento);
                } else {
                    procedimiento.setNombre(procedimiento.getNombre().toUpperCase());
                    procedimiento.setDescripcion(procedimiento.getDescripcion().toUpperCase());
                    procedimiento.setEstado(Boolean.TRUE);
                    procedimiento.setUsuarioCreacion(userSession.getNameUser());
                    procedimiento.setFechaCreacion(new Date());
                    procedimiento.setUsuarioModificacion(userSession.getNameUser());
                    procedimiento.setFechaModificacion(new Date());
                    procedimiento = procedimientoService.create(procedimiento);
                    for (ProcedimientoRequisito object : procedimientoRequisitoList) {
                        procedimientoRequisito = new ProcedimientoRequisito();
                        procedimientoRequisito.setIdProcedimiento(procedimiento);
                        procedimientoRequisito.setIdRequisito(object.getIdRequisito());
                        procedimientoRequisito.setObligatorio(object.getObligatorio());
                        procedimientoRequisito = procedimientoRequisitoService.create(procedimientoRequisito);
                    }
                }
                PrimeFaces.current().executeScript("PF('DlgRegistroProcedimiento').hide()");
                PrimeFaces.current().ajax().update("mainForm");
                JsfUtil.addSuccessMessage("Procedimiento", procedimiento.getNombre() + (edit ? " editada" : " registrado") + " con éxito");
            } else {
                JsfUtil.addErrorMessage("Guardar", "Debe añadir al menos 1 requisito");
            }
        } else {
            JsfUtil.addErrorMessage("Guardar", "Ingrese todos los datos solicitados");
        }
    }
    
    public void eliminarProcedimiento(Procedimiento procedimiento) {
        Boolean consulta1 = procedimientoService.getConsultarRelacionCertificacion(procedimiento);
        if (consulta1) {
            JsfUtil.addErrorMessage("PROCEDIMIENTO", "No se puede eliminar debido a que esta relacionado a un proceso ");
        } else {
            Boolean consulta2 = procedimientoService.getTramiteAsociado(procedimiento);
            if (consulta2) {
                JsfUtil.addErrorMessage("PROCEDIMIENTO", "No se puede eliminar debido a que esta relacionado a un Tramite ");
            } else {
                procedimiento.setEstado(Boolean.FALSE);
                procedimientoService.edit(procedimiento);
                JsfUtil.addInformationMessage("PROCEDIMIENTO", "Se ha eliminado correctamente");
                PrimeFaces.current().ajax().update("prodecimientoRequisitoTable");
            }
        }
    }

    //REMOVER LOS REQUISITOS QUE YA ESTAN REGISTRADOS Y DE LOS QUE SE VAN A REGISTRAR
    public void eliminarRequisito(ProcedimientoRequisito procedimientoRequisito) {
        if (procedimientoRequisito.getId() != null) {
            Boolean consulta = procedimientoRequisitoService.getConsultarCertificacion(procedimientoRequisito, opcionBusqueda.getAnio());
            Boolean consulta_2 = procedimientoRequisitoService.getTramiteAsociado(procedimientoRequisito);
            if (consulta || consulta_2) {
                JsfUtil.addErrorMessage("REQUISITO", "No se puede ser eliminado porque esta relacionado con una Certificación Presupuestaria ó a un tramite");
                return;
            } else {
                procedimientoRequisitoService.remove(procedimientoRequisito);
                procedimientoRequisitoList = procedimientoRequisitoService.getRequisitosDelProcedimiento(procedimiento);
            }
        } else {
            int index = 0;
            for (ProcedimientoRequisito pReq : procedimientoRequisitoList) {
                if (pReq.getIdRequisito().equals(procedimientoRequisito.getIdRequisito())) {
                    break;
                }
                index++;
            }
            procedimientoRequisitoList.remove(index);
        }
        PrimeFaces.current().ajax().update(":requisitosAnadidos");
        JsfUtil.addSuccessMessage("REQUISITO", "Se elimino Correctamente");
    }

    /*FIN DE LAS FUNCIONES DE PROCEDIMIENTO*/
    public void acualizarDescripcion() {
        if (requisitoSeleccionado != null) {
            soloLectura = true;
        } else {
            soloLectura = false;
        }
    }

    //<editor-fold defaultstate="collapsed" desc="GET - SET">
    public Requisito getRequisitoSeleccionado() {
        return requisitoSeleccionado;
    }
    
    public void setRequisitoSeleccionado(Requisito requisitoSeleccionado) {
        this.requisitoSeleccionado = requisitoSeleccionado;
    }
    
    public Procedimiento getProcedimiento() {
        return procedimiento;
    }
    
    public void setProcedimiento(Procedimiento procedimiento) {
        this.procedimiento = procedimiento;
    }
    
    public List<TipoTramite> getProcesoList() {
        return procesoList;
    }
    
    public void setProcesoList(List<TipoTramite> procesoList) {
        this.procesoList = procesoList;
    }
    
    public List<Requisito> getItemsRequisitos() {
        return itemsRequisitos;
    }
    
    public void setItemsRequisitos(List<Requisito> itemsRequisitos) {
        this.itemsRequisitos = itemsRequisitos;
    }
    
    public Boolean getSoloLectura() {
        return soloLectura;
    }
    
    public void setSoloLectura(Boolean soloLectura) {
        this.soloLectura = soloLectura;
    }
    
    public ProcedimientoRequisito getProcedimientoRequisito() {
        return procedimientoRequisito;
    }
    
    public void setProcedimientoRequisito(ProcedimientoRequisito procedimientoRequisito) {
        this.procedimientoRequisito = procedimientoRequisito;
    }
    
    public LazyModel<ProcedimientoRequisito> getProcedimientoRequisitoLazyModel() {
        return procedimientoRequisitoLazyModel;
    }
    
    public void setProcedimientoRequisitoLazyModel(LazyModel<ProcedimientoRequisito> procedimientoRequisitoLazyModel) {
        this.procedimientoRequisitoLazyModel = procedimientoRequisitoLazyModel;
    }
    
    public List<ProcedimientoRequisito> getProcedimientoRequisitoList() {
        return procedimientoRequisitoList;
    }
    
    public void setProcedimientoRequisitoList(List<ProcedimientoRequisito> procedimientoRequisitoList) {
        this.procedimientoRequisitoList = procedimientoRequisitoList;
    }
    
    public ProcedimientoService getProcedimientoService() {
        return procedimientoService;
    }
    
    public void setProcedimientoService(ProcedimientoService procedimientoService) {
        this.procedimientoService = procedimientoService;
    }
    
    public UserSession getUserSession() {
        return userSession;
    }
    
    public void setUserSession(UserSession userSession) {
        this.userSession = userSession;
    }
    
    public RequisitoService getRequisitoService() {
        return requisitoService;
    }
    
    public void setRequisitoService(RequisitoService requisitoService) {
        this.requisitoService = requisitoService;
    }
    
    public ProcedimientoRequisitoService getProcedimientoRequisitoService() {
        return procedimientoRequisitoService;
    }
    
    public void setProcedimientoRequisitoService(ProcedimientoRequisitoService procedimientoRequisitoService) {
        this.procedimientoRequisitoService = procedimientoRequisitoService;
    }
    
    public TipoTramiteService getTipoTramiteService() {
        return tipoTramiteService;
    }
    
    public void setTipoTramiteService(TipoTramiteService tipoTramiteService) {
        this.tipoTramiteService = tipoTramiteService;
    }
    
    public TipoRolService getTipoRolService() {
        return tipoRolService;
    }
    
    public void setTipoRolService(TipoRolService tipoRolService) {
        this.tipoRolService = tipoRolService;
    }
    
    public OpcionBusqueda getOpcionBusqueda() {
        return opcionBusqueda;
    }
    
    public void setOpcionBusqueda(OpcionBusqueda opcionBusqueda) {
        this.opcionBusqueda = opcionBusqueda;
    }
    
    public LazyModel<Procedimiento> getProcedimientoLazy() {
        return procedimientoLazy;
    }
    
    public void setProcedimientoLazy(LazyModel<Procedimiento> procedimientoLazy) {
        this.procedimientoLazy = procedimientoLazy;
    }
//</editor-fold>
 
}
