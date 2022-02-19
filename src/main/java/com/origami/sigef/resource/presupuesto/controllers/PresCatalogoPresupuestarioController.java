/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.presupuesto.controllers;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.CatalogoService;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.resource.conf.entities.PlanCuentas;
import com.origami.sigef.resource.conf.services.PlanCuentasService;
import com.origami.sigef.resource.presupuesto.entities.PresCatalogoPresupuestario;
import com.origami.sigef.resource.presupuesto.services.PresCatalogoPresupuestarioService;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.component.datatable.DataTable;

/**
 *
 * @author Criss Intriago
 */
@Named(value = "presCatalogoPresupuestarioView")
@ViewScoped
public class PresCatalogoPresupuestarioController implements Serializable {

    @Inject
    private ServletSession servletSession;
    @Inject
    private PresCatalogoPresupuestarioService presCatalogoPresupuestarioService;
    @Inject
    private PlanCuentasService confCuentasService;
    @Inject
    private UserSession userSession;
    @Inject
    private CatalogoService catalogoService;

    private PresCatalogoPresupuestario presCatalogoPresupuestario;

    private List<PlanCuentas> confCuentasList;
    private List<CatalogoItem> clasificacionList;

    private LazyModel<PresCatalogoPresupuestario> presCatalogoPresupuestarioLazy;

    private boolean editView;
    
    //ENRIQUE
    private Boolean edititem;

    // Parametros de reporte //
    private int pActivo;
    private int pMovimiento;
    private Boolean bNivel;
    private PlanCuentas pNivel;
    private Boolean bCodigo;
    private String pCodigo;
    //////////////////////////

    @PostConstruct
    public void init() {
        this.presCatalogoPresupuestarioLazy = new LazyModel<>(PresCatalogoPresupuestario.class);
        this.presCatalogoPresupuestarioLazy.getFilterss().put("estado", true);
        this.presCatalogoPresupuestarioLazy.getSorteds().put("codigo", "ASC");
        confCuentasList = confCuentasService.getNivelesList(CONFIG.PLAN_ITEM_PRESUPUESTARIO,false);
        cleanForm();
        edititem = false;
    }
    
    public void clearAllFilters() {
        DataTable dataTable = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formMain:presCatalogoPresupuestarioTable");
        if (!dataTable.getFilters().isEmpty()) {
            dataTable.reset();
            PrimeFaces.current().ajax().update("formMain:presCatalogoPresupuestarioTable");
        }
    }

    public void cleanForm() {
        presCatalogoPresupuestario = new PresCatalogoPresupuestario();
        clasificacionList = catalogoService.getItemsByCatalogo("tipo_cuenta");
        editView = false;
    }

    public void form(PresCatalogoPresupuestario presCatalogoPresupuestario, boolean accion) {
        cleanForm();
        editView = accion;
        if (presCatalogoPresupuestario != null) {
            edititem = true;
            this.presCatalogoPresupuestario = presCatalogoPresupuestario;
            JsfUtil.executeJS("PF('presupuestarioDlg').show()");
            PrimeFaces.current().ajax().update("presupuestarioForm");
        } else {
            edititem = false;
            PlanCuentas aux = confCuentasService.getFindNext(CONFIG.PLAN_ITEM_PRESUPUESTARIO, 1, false);
            if (aux == null) {
                JsfUtil.addWarningMessage("AVISO!!!", "Debe agregar una nueva configuracion de nivel para el registro de una nueva cuenta contable");
                return;
            }
            this.presCatalogoPresupuestario = new PresCatalogoPresupuestario();
            this.presCatalogoPresupuestario.setConfId(aux);
            generarCodigo(null);
        }
    }

    public void insertPadre(PresCatalogoPresupuestario padre) {
        cleanForm();
        PlanCuentas aux = confCuentasService.getFindNext(CONFIG.PLAN_ITEM_PRESUPUESTARIO, (padre.getConfId().getNivel() + 1), false);
        if (aux == null) {
            JsfUtil.addWarningMessage("AVISO!!!", "Debe agregar una nueva configuracion de nivel para el registro de una nueva cuenta contable");
            return;
        }
        this.presCatalogoPresupuestario = new PresCatalogoPresupuestario();
        this.presCatalogoPresupuestario.setPadre(padre);
        this.presCatalogoPresupuestario.setClasificacion(padre.getClasificacion());
        this.presCatalogoPresupuestario.setConfId(aux);
        generarCodigo(padre);
    }

    private void generarCodigo(PresCatalogoPresupuestario padre) {
        String code = presCatalogoPresupuestarioService.getNextCode(presCatalogoPresupuestario.getConfId(), padre);
        if (code.equals("1")) {
            if (presCatalogoPresupuestario.getConfId().getNumDigito() > 1) {
                code = Utils.completarCadenaConCeros(code, presCatalogoPresupuestario.getConfId().getNumDigito());
            }
            presCatalogoPresupuestario.setCodIngreso(code);
            if (padre != null) {
                code = padre.getCodigo() + Utils.completarCadenaConCeros(code, presCatalogoPresupuestario.getConfId().getNumDigito());
            }
            presCatalogoPresupuestario.setCodigo(code);
        } else {
            presCatalogoPresupuestario.setCodIngreso(code);
            if (presCatalogoPresupuestario.getConfId().getNumDigito() > 1 && code.length() == 1) {
                code = padre.getCodigo() + Utils.completarCadenaConCeros(code, presCatalogoPresupuestario.getConfId().getNumDigito());
            }
            this.presCatalogoPresupuestario.setCodigo(code);
        }
        JsfUtil.executeJS("PF('presupuestarioDlg').show()");
        PrimeFaces.current().ajax().update("presupuestarioForm");
    }

    public void generarCodigo() {
        if (presCatalogoPresupuestario.getPadre() != null) {
            if (presCatalogoPresupuestario.getConfId().getNumDigito() > 1) {
                presCatalogoPresupuestario.setCodIngreso(Utils.completarCadenaConCeros(presCatalogoPresupuestario.returnIngreso(), presCatalogoPresupuestario.getConfId().getNumDigito()));
            }
            presCatalogoPresupuestario.setCodigo(presCatalogoPresupuestario.getCodPadre().concat(presCatalogoPresupuestario.returnIngreso()));
            if (presCatalogoPresupuestario.getConfId().getSeparador()) {
                presCatalogoPresupuestario.setCodigo(presCatalogoPresupuestario.getCodigo().concat(presCatalogoPresupuestario.getConfId().getCaracter()));
            }
        } else {
            presCatalogoPresupuestario.setCodigo(presCatalogoPresupuestario.getCodIngreso());
        }
    }

    public void save() {
        boolean edit = presCatalogoPresupuestario.getId() != null;
        if (presCatalogoPresupuestario.getConfId() == null) {
            JsfUtil.addWarningMessage("AVISO!!!", "Seleccione un nivel");
            return;
        }
        if (presCatalogoPresupuestario.getClasificacion() == null) {
            JsfUtil.addWarningMessage("AVISO!!!", "Seleccione una clasificacion");
            return;
        }
        if (presCatalogoPresupuestario.getDescripcion() == null || presCatalogoPresupuestario.getDescripcion().equals("")) {
            JsfUtil.addWarningMessage("AVISO!!!", "Debe ingresar una descripcion");
            return;
        }
        if (presCatalogoPresupuestario.getMovimiento()) {
            presCatalogoPresupuestario.setDescripcion(presCatalogoPresupuestario.getDescripcion().toUpperCase());
        }
        if (presCatalogoPresupuestario.getConfId().getNumDigito() > presCatalogoPresupuestario.returnIngreso().length()) {
            JsfUtil.addWarningMessage("AVISO!!!", "No la cantidad de digitos ingresado en el codigo es menor las requeridas en el nivel no." + presCatalogoPresupuestario.getConfId().getNivel());
            return;
        }
        if (presCatalogoPresupuestario.getCodigo() == null) {
            JsfUtil.addWarningMessage("AVISO!!!", "Debe ingresar el codigo de la cuenta");
            return;
        }
        if (edit) {
            presCatalogoPresupuestario.setUsuarioModifica(userSession.getNameUser());
            presCatalogoPresupuestario.setFechaModificacion(new Date());
            presCatalogoPresupuestarioService.edit(presCatalogoPresupuestario);
        } else {
            if (presCatalogoPresupuestarioService.findExiste(presCatalogoPresupuestario.getCodigo())) {
                JsfUtil.addWarningMessage("AVISO!!!", "No se puede registrat porque ya exite una cuenta contable con el codigo: " + presCatalogoPresupuestario.getCodigo());
                return;
            }
            presCatalogoPresupuestario.setUsuarioCreacion(userSession.getNameUser());
            presCatalogoPresupuestario.setFechaCreacion(new Date());
            presCatalogoPresupuestario = presCatalogoPresupuestarioService.create(presCatalogoPresupuestario);
        }
        JsfUtil.addSuccessMessage("INFO!!!", (edit ? "Editado" : " Registrado") + " con éxito");
        closeForm();
    }

    public void desactivar(PresCatalogoPresupuestario presCatalogoPresupuestario) {
        presCatalogoPresupuestario.setActivo(false);
        presCatalogoPresupuestario.setUsuarioModifica(userSession.getNameUser());
        presCatalogoPresupuestario.setFechaModificacion(new Date());
        presCatalogoPresupuestarioService.edit(presCatalogoPresupuestario);
        JsfUtil.addInformationMessage("INFO!!!", "Se ha desactivado correctamente");
        PrimeFaces.current().ajax().update("presCatalogoPresupuestarioTable");
    }

    public void closeForm() {
        cleanForm();
        JsfUtil.executeJS("PF('presupuestarioDlg').hide()");
    }

    public void openDlgReporte() {
        pActivo = 1;
        pMovimiento = 1;
        bNivel = true;
        bCodigo = true;
        pCodigo = "";
        pNivel = new PlanCuentas();
        JsfUtil.executeJS("PF('reporteDlg').show()");
        PrimeFaces.current().ajax().update("reporteForm");
    }

    public void printReporte(String type) {
        if (!bCodigo) {
            if (pCodigo.equals("") || pCodigo == null) {
                JsfUtil.addWarningMessage("AVISO!!!", "Debe ingresar el codigo de busqueda");
                return;
            }
        }
        if (!bNivel) {
            if (pNivel == null || pNivel.getId() == null) {
                JsfUtil.addWarningMessage("AVISO!!!", "Debe seleccionar un nivel");
                return;
            }
            servletSession.addParametro("pNivel", pNivel.getId());
        } else {
            servletSession.addParametro("pNivel", null);
        }
        servletSession.addParametro("pActivo", pActivo);
        servletSession.addParametro("pMovimiento", pMovimiento);
        servletSession.addParametro("bNivel", bNivel);
        servletSession.addParametro("bCodigo", bCodigo);
        servletSession.addParametro("pCodigo", pCodigo + "%");
        servletSession.setNombreReporte("plan_presupuestario");
        servletSession.setNombreSubCarpeta("_presupuesto");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
        JsfUtil.executeJS("PF('reporteDlg').hide()");
        PrimeFaces.current().ajax().update("reporteForm");
    }
    public void delete(PresCatalogoPresupuestario presCatalogoPresupuestario){
        if (presCatalogoPresupuestarioService.findHijos(presCatalogoPresupuestario)) {
            JsfUtil.addWarningMessage("AVISO!!!", "No se puede eliminar la partida, debido a las relaciones que tiene");
            return;
        }
        presCatalogoPresupuestario.setEstado(Boolean.FALSE);
        presCatalogoPresupuestarioService.edit(presCatalogoPresupuestario);
        JsfUtil.addSuccessMessage("INFO!!!", "Se ha eliminado correctamente");
        PrimeFaces.current().ajax().update("presCatalogoPresupuestarioTable");
    }

    public PresCatalogoPresupuestario getPresCatalogoPresupuestario() {
        return presCatalogoPresupuestario;
    }

    public Boolean getEdititem() {
        return edititem;
    }

    public void setEdititem(Boolean edititem) {
        this.edititem = edititem;
    }
    
    public void setPresCatalogoPresupuestario(PresCatalogoPresupuestario presCatalogoPresupuestario) {
        this.presCatalogoPresupuestario = presCatalogoPresupuestario;
    }

    public List<PlanCuentas> getConfCuentasList() {
        return confCuentasList;
    }

    public void setConfCuentasList(List<PlanCuentas> confCuentasList) {
        this.confCuentasList = confCuentasList;
    }

    public List<CatalogoItem> getClasificacionList() {
        return clasificacionList;
    }

    public void setClasificacionList(List<CatalogoItem> clasificacionList) {
        this.clasificacionList = clasificacionList;
    }

    public LazyModel<PresCatalogoPresupuestario> getPresCatalogoPresupuestarioLazy() {
        return presCatalogoPresupuestarioLazy;
    }

    public void setPresCatalogoPresupuestarioLazy(LazyModel<PresCatalogoPresupuestario> presCatalogoPresupuestarioLazy) {
        this.presCatalogoPresupuestarioLazy = presCatalogoPresupuestarioLazy;
    }

    public boolean isEditView() {
        return editView;
    }

    public void setEditView(boolean editView) {
        this.editView = editView;
    }

    public int getpActivo() {
        return pActivo;
    }

    public void setpActivo(int pActivo) {
        this.pActivo = pActivo;
    }

    public int getpMovimiento() {
        return pMovimiento;
    }

    public void setpMovimiento(int pMovimiento) {
        this.pMovimiento = pMovimiento;
    }

    public Boolean getbNivel() {
        return bNivel;
    }

    public void setbNivel(Boolean bNivel) {
        this.bNivel = bNivel;
    }

    public PlanCuentas getpNivel() {
        return pNivel;
    }

    public void setpNivel(PlanCuentas pNivel) {
        this.pNivel = pNivel;
    }

    public Boolean getbCodigo() {
        return bCodigo;
    }

    public void setbCodigo(Boolean bCodigo) {
        this.bCodigo = bCodigo;
    }

    public String getpCodigo() {
        return pCodigo;
    }

    public void setpCodigo(String pCodigo) {
        this.pCodigo = pCodigo;
    }

}
