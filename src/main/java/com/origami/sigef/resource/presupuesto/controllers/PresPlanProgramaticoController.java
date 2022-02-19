/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.presupuesto.controllers;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.resource.conf.entities.PlanCuentas;
import com.origami.sigef.resource.conf.services.PlanCuentasService;
import com.origami.sigef.resource.presupuesto.entities.PresPlanProgramatico;
import com.origami.sigef.resource.presupuesto.services.PresPlanProgramaticoService;
import java.io.Serializable;
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
@Named(value = "presPlanProgramaticoView")
@ViewScoped
public class PresPlanProgramaticoController implements Serializable {

    @Inject
    private ServletSession servletSession;
    @Inject
    private PresPlanProgramaticoService presPlanProgramaticoService;
    @Inject
    private UserSession userSession;
    @Inject
    private PlanCuentasService planCuentasService;

    private PresPlanProgramatico presPlanProgramatico;

    private LazyModel<PresPlanProgramatico> presPlanProgramaticoLazy;

    private List<PlanCuentas> confCuentasList;

    private boolean editView;

    @PostConstruct
    public void init() {
        presPlanProgramaticoLazy = new LazyModel<>(PresPlanProgramatico.class);
        this.presPlanProgramaticoLazy.getSorteds().put("codigo", "ASC");
        this.presPlanProgramaticoLazy.getFilterss().put("estado", Boolean.TRUE);
        confCuentasList = planCuentasService.getNivelesList(CONFIG.PLAN_ITEM_PRESUPUESTARIO, true);
        cleanForm(false);
    }

    public void cleanForm(Boolean accion) {
        presPlanProgramatico = new PresPlanProgramatico();
        editView = false;
        if (accion) {
            JsfUtil.executeJS("PF('planProgramaticoDlg').hide()");
            PrimeFaces.current().ajax().update("planProgramaticoForm");
        }
    }

    public void form(PresPlanProgramatico presPlanProgramatico, Boolean view) {
        cleanForm(false);
        this.editView = view;
        if (presPlanProgramatico != null) {
            this.presPlanProgramatico = presPlanProgramatico;
            JsfUtil.executeJS("PF('planProgramaticoDlg').show()");
            PrimeFaces.current().ajax().update("planProgramaticoForm");
        } else {
            PlanCuentas aux = planCuentasService.getFindNext(CONFIG.PLAN_ITEM_PRESUPUESTARIO, 1, true);
            if (aux == null) {
                JsfUtil.addWarningMessage("AVISO!!!", "Debe agregar una nueva configuracion de nivel para el registro de una nueva cuenta contable");
                return;
            }
            this.presPlanProgramatico = new PresPlanProgramatico();
            this.presPlanProgramatico.setConfId(aux);
            generarCodigo(null);
        }
    }

    public void insertPadre(PresPlanProgramatico presPlanProgramatico) {
        cleanForm(false);
        this.presPlanProgramatico.setPadre(presPlanProgramatico);
        PlanCuentas aux = planCuentasService.getFindNext(CONFIG.PLAN_ITEM_PRESUPUESTARIO, (presPlanProgramatico.getConfId().getNivel() + 1), true);
        if (aux == null) {
            JsfUtil.addWarningMessage("AVISO!!!", "Debe agregar una nueva configuracion de nivel para el registro de una nueva cuenta contable");
            return;
        }
        this.presPlanProgramatico.setConfId(aux);
        generarCodigo(presPlanProgramatico);
    }

    private void generarCodigo(PresPlanProgramatico padre) {
        String code = presPlanProgramaticoService.getNextCode(presPlanProgramatico.getConfId(), padre);
        if (code.equals("1")) {
            if (presPlanProgramatico.getConfId().getNumDigito() > 1) {
                code = Utils.completarCadenaConCeros(code, presPlanProgramatico.getConfId().getNumDigito());
            }
            presPlanProgramatico.setCodIngreso(code);
            if (padre != null) {
                code = padre.getCodigo() + Utils.completarCadenaConCeros(code, presPlanProgramatico.getConfId().getNumDigito());
            }
            presPlanProgramatico.setCodigo(code);
        } else {
            presPlanProgramatico.setCodIngreso(code);
            if (presPlanProgramatico.getConfId().getNumDigito() > 1 && code.length() == 1) {
                code = padre.getCodigo() + Utils.completarCadenaConCeros(code, presPlanProgramatico.getConfId().getNumDigito());
            }
            this.presPlanProgramatico.setCodigo(code);
        }
        JsfUtil.executeJS("PF('planProgramaticoDlg').show()");
        PrimeFaces.current().ajax().update("planProgramaticoForm");
    }

    public void generarCodigo() {
        if (presPlanProgramatico.getPadre() != null) {
            if (presPlanProgramatico.getConfId().getNumDigito() > 1) {
                presPlanProgramatico.setCodIngreso(Utils.completarCadenaConCeros(presPlanProgramatico.returnIngreso(), presPlanProgramatico.getConfId().getNumDigito()));
            }
            presPlanProgramatico.setCodigo(presPlanProgramatico.getCodPadre().concat(presPlanProgramatico.returnIngreso()));
            if (presPlanProgramatico.getConfId().getSeparador()) {
                presPlanProgramatico.setCodigo(presPlanProgramatico.getCodigo().concat(presPlanProgramatico.getConfId().getCaracter()));
            }
        } else {
            presPlanProgramatico.setCodigo(presPlanProgramatico.getCodIngreso());
        }
    }

    public void save() {
        boolean edit = presPlanProgramatico.getId() != null;
        if (presPlanProgramatico.getCodigo() == null || presPlanProgramatico.getCodigo().equals("")) {
            JsfUtil.addWarningMessage("AVISO!!!", "Debe ingresar un codigo");
            return;
        }
        if (presPlanProgramatico.getDescripcion() == null || presPlanProgramatico.getDescripcion().equals("")) {
            JsfUtil.addWarningMessage("AVISO!!!", "Debe ingresar una descripción/nombre");
            return;
        }
        if (edit) {
            presPlanProgramatico.setUsuarioModifica(userSession.getNameUser());
            presPlanProgramatico.setFechaModificacion(new Date());
            presPlanProgramaticoService.edit(presPlanProgramatico);
        } else {
            if (presPlanProgramaticoService.findExiste(presPlanProgramatico.getCodigo())) {
                JsfUtil.addWarningMessage("AVISO!!!", "No se puede registrat porque ya existe un plan programatico con el codigo: " + presPlanProgramatico.getCodigo());
                return;
            }
            presPlanProgramatico.setUsuarioCreacion(userSession.getNameUser());
            presPlanProgramatico.setFechaCreacion(new Date());
            presPlanProgramatico = presPlanProgramaticoService.create(presPlanProgramatico);
        }
        JsfUtil.addSuccessMessage("INFO!!!", (edit ? "Editado" : " Registrado") + " con éxito");
        JsfUtil.executeJS("PF('planProgramaticoDlg').hide()");
        PrimeFaces.current().ajax().update("presPlanProgramaticoTable");
        cleanForm(false);
    }

    public void desactivar(PresPlanProgramatico presPlanProgramatico) {
        Boolean activo = presPlanProgramatico.getActivo();
        if (presPlanProgramatico.getActivo()) {
            presPlanProgramatico.setActivo(Boolean.FALSE);
        } else {
            presPlanProgramatico.setActivo(Boolean.TRUE);
        }
        presPlanProgramatico.setUsuarioModifica(userSession.getNameUser());
        presPlanProgramatico.setFechaModificacion(new Date());
        presPlanProgramaticoService.edit(presPlanProgramatico);
        JsfUtil.addInformationMessage("INFO!!!", "Se ha " + (activo ? "Desactivado" : "Activado") + " correctamente");
        PrimeFaces.current().ajax().update("presPlanProgramaticoTable");
    }

    public Boolean gethijos(PresPlanProgramatico presPlanProgramatico) {
        return presPlanProgramaticoService.poseeHijos(presPlanProgramatico);
    }

    public void eliminar(PresPlanProgramatico presPlanProgramatico) {
        presPlanProgramatico.setEstado(false);
        presPlanProgramatico.setUsuarioModifica(userSession.getNameUser());
        presPlanProgramatico.setFechaModificacion(new Date());
        presPlanProgramaticoService.edit(presPlanProgramatico);
        JsfUtil.addInformationMessage("INFO!!!", "plan programatico " + presPlanProgramatico.getDescripcion() + " eliminado correctamente...");
        PrimeFaces.current().ajax().update("presPlanProgramaticoTable");
    }

    public PresPlanProgramatico getPresPlanProgramatico() {
        return presPlanProgramatico;
    }

    public void setPresPlanProgramatico(PresPlanProgramatico presPlanProgramatico) {
        this.presPlanProgramatico = presPlanProgramatico;
    }

    public LazyModel<PresPlanProgramatico> getPresPlanProgramaticoLazy() {
        return presPlanProgramaticoLazy;
    }

    public void setPresPlanProgramaticoLazy(LazyModel<PresPlanProgramatico> presPlanProgramaticoLazy) {
        this.presPlanProgramaticoLazy = presPlanProgramaticoLazy;
    }

    public List<PlanCuentas> getConfCuentasList() {
        return confCuentasList;
    }

    public void setConfCuentasList(List<PlanCuentas> confCuentasList) {
        this.confCuentasList = confCuentasList;
    }

    public boolean isEditView() {
        return editView;
    }

    public void setEditView(boolean editView) {
        this.editView = editView;
    }

}
