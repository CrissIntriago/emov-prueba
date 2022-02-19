/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.talento_humano.controllers;

import com.origami.sigef.catalogoItem.service.CatalogoItemService;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.UnidadAdministrativa;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.service.CatalogoProformaPresupuestoService;
import com.origami.sigef.resource.talento_humano.entities.ThCargo;
import com.origami.sigef.resource.talento_humano.entities.ThCargoRubros;
import com.origami.sigef.resource.talento_humano.entities.ThEscalaSalarial;
import com.origami.sigef.resource.talento_humano.entities.ThRegimenLaboral;
import com.origami.sigef.resource.talento_humano.entities.ThRubro;
import com.origami.sigef.resource.talento_humano.interfaces.ThInterfaces;
import com.origami.sigef.resource.talento_humano.services.ThCargoRubrosService;
import com.origami.sigef.resource.talento_humano.services.ThCargoService;
import java.io.Serializable;
import java.math.BigDecimal;
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
@Named(value = "thActualizarCargoView")
@ViewScoped
public class ThActualizarCargoController implements Serializable {

    @Inject
    private ThInterfaces thInterfaces;
    @Inject
    private CatalogoItemService catalogoItemService;
    @Inject
    private CatalogoProformaPresupuestoService catalogoProformaPresupuestoService;
    @Inject
    private ThCargoService thCargoService;
    @Inject
    private ThCargoRubrosService thCargoRubrosService;

    private UnidadAdministrativa unidadSeleccionado, unidadFind;
    private ThEscalaSalarial thEscalaSeleccionado;
    private ThRegimenLaboral thRegimenSeleccionado;
    private CatalogoItem tipoContratoSeleccionado, clasificacionSeleccionado, filtroContrato, filtroClasificacion;
    private OpcionBusqueda opcionBusqueda;

    private List<ThCargo> thCargoList;
    private List<String> thCargosList;
    private List<CatalogoItem> listTipoCargo, tipoCargoList;
    private List<UnidadAdministrativa> unidadesAdministrativasList;
    private List<ThEscalaSalarial> thEscalaSalarialList;
    private List<ThRegimenLaboral> thRegimenLaboralList;
    private List<ThCargoRubros> thCargoRubrosInsert;
    private List<Short> listaPeriodo;

    private String thCargoSeleccionado;

    private LazyModel<ThRubro> thRubroLazyModel;

    @PostConstruct
    public void init() {
        thCargosList = thInterfaces.getCargosActivos();
        listTipoCargo = catalogoItemService.findByCatalogo("tipoCargo");
        tipoCargoList = catalogoItemService.findByCatalogo("th_tipo_contrato");
        unidadesAdministrativasList = thInterfaces.getUnidadesAdministrativas();
        thEscalaSalarialList = thInterfaces.getThEscalaSalarial();
        thRegimenLaboralList = thInterfaces.getThRegimenLaboral();
        //ThRubros
        thRubroLazyModel = new LazyModel<>(ThRubro.class);
        thRubroLazyModel.addSorted("ingreso", "DESC");
        thRubroLazyModel.addSorted("nombre", "ASC");
        thRubroLazyModel.getFilterss().put("estado", true);
        thRubroLazyModel.getFilterss().put("activo", true);
        listaPeriodo = thInterfaces.getPeriodos();
        clean();
    }

    public void clean() {
        thCargoSeleccionado = "";
        opcionBusqueda = new OpcionBusqueda();
        unidadSeleccionado = null;
        thEscalaSeleccionado = null;
        thRegimenSeleccionado = null;
        tipoContratoSeleccionado = null;
        clasificacionSeleccionado = null;
        thCargoList = new ArrayList<>();
        thCargoRubrosInsert = new ArrayList<>();
    }

    public void updateLazy() {
        if (thCargoSeleccionado == null || thCargoSeleccionado.equals("")) {
            thCargoList = null;
        } else {
            thCargoList = thInterfaces.findCargos(thCargoSeleccionado, filtroContrato, filtroClasificacion, unidadFind);
        }
    }

    public void aggCargo() {
        if (thCargoSeleccionado == null || thCargoSeleccionado.equals("")) {
            JsfUtil.addWarningMessage("AVISO!!!", "Debe seleccionar un cargo");
            return;
        }
        ThCargo temp = new ThCargo();
        temp.setNombreCargo(thCargoSeleccionado);
        if (unidadSeleccionado != null) {
            temp.setIdUnidad(unidadSeleccionado);
        }
        if (thRegimenSeleccionado != null) {
            temp.setIdRegimen(thRegimenSeleccionado);
        }
        if (thEscalaSeleccionado != null) {
            temp.setIdGrupo(thEscalaSeleccionado);
        }
        if (tipoContratoSeleccionado != null) {
            temp.setIdContrato(tipoContratoSeleccionado);
        }
        if (clasificacionSeleccionado != null) {
            temp.setIdCatalogoItem(clasificacionSeleccionado);
        }
        thCargoList.add(temp);
        JsfUtil.addSuccessMessage("INFO!!!", "Se ha registrado un nuevo cargo");
        PrimeFaces.current().ajax().update("thCargoTable");
    }

    public void delete(ThCargo thcargo, int index) {
        if (thcargo.getId() != null) {
            thCargoList.remove(thcargo);
        } else {
            thCargoList.remove(index);
        }
    }

    public void updateUnidad() {
        if (thCargoList != null && !thCargoList.isEmpty()) {
            for (ThCargo thCargo : thCargoList) {
                thCargo.setIdUnidad(unidadSeleccionado);
            }
        }
        PrimeFaces.current().ajax().update("thCargoTable");
    }

    public void updateRegimen() {
        if (thCargoList != null && !thCargoList.isEmpty()) {
            for (ThCargo thCargo : thCargoList) {
                thCargo.setIdRegimen(thRegimenSeleccionado);
            }
        }
        PrimeFaces.current().ajax().update("thCargoTable");
    }

    public void updateEscala() {
        if (thCargoList != null && !thCargoList.isEmpty()) {
            for (ThCargo thCargo : thCargoList) {
                thCargo.setIdGrupo(thEscalaSeleccionado);
            }
        }
        PrimeFaces.current().ajax().update("thCargoTable");
    }

    public void updateTipoContrato() {
        if (thCargoList != null && !thCargoList.isEmpty()) {
            for (ThCargo thCargo : thCargoList) {
                thCargo.setIdContrato(tipoContratoSeleccionado);
            }
        }
        PrimeFaces.current().ajax().update("thCargoTable");
    }

    public void updateClasificacion() {
        if (thCargoList != null && !thCargoList.isEmpty()) {
            for (ThCargo thCargo : thCargoList) {
                thCargo.setIdCatalogoItem(clasificacionSeleccionado);
            }
        }
        PrimeFaces.current().ajax().update("thCargoTable");
    }

    public void openDlg() {
        if (consultarAprobacion(opcionBusqueda.getAnio())) {
            JsfUtil.addWarningMessage("AVISO!!!", "No puede agregar un rubro en este periodo debido a que ya esta aprobado el presupuesto del periodo seleccionado");
            return;
        }
        JsfUtil.executeJS("PF('rubrosDlg').show()");
    }

    private Boolean consultarAprobacion(Short periodo) {
        return catalogoProformaPresupuestoService.presupuestoEgresoAprobado(periodo);
    }

    public void closeDlgRubro(ThRubro thRubro) {
        boolean accion = true;
        ThCargoRubros cargoRubro = new ThCargoRubros(thRubro, opcionBusqueda.getAnio());
        cargoRubro.setMonto(BigDecimal.ZERO);
        cargoRubro.setProyeccion(12);
        if (!thCargoRubrosInsert.isEmpty()) {
            for (ThCargoRubros th : thCargoRubrosInsert) {
                if (th.getIdRubro().equals(thRubro)) {
                    accion = false;
                }
            }
        }
        if (accion) {
            thCargoRubrosInsert.add(cargoRubro);
            JsfUtil.addSuccessMessage("INFO!!!", "Se ha agregado correctamente");
        } else {
            JsfUtil.addWarningMessage("AVISO!!!", "Ya se encuentra seleccionado");
        }
        PrimeFaces.current().ajax().update("thCargosRubroTable");
    }

    public void deteleCargoRubro(ThCargoRubros thCargoRubros, int index) {
        if (thCargoRubros.getId() != null) {
            thCargoRubrosInsert.remove(thCargoRubros);
        } else {
            thCargoRubrosInsert.remove(index);
        }
        PrimeFaces.current().ajax().update("thCargosRubroTable");
        JsfUtil.addInformationMessage("INFO!!!", "Se eliminado correctamente");
    }

    public void save() {
        if (thCargoList != null && !thCargoList.isEmpty()) {
            for (ThCargo cargo : thCargoList) {
                Boolean edit = cargo.getId() != null;
                if (edit) {
                    cargo.setUsuarioModifica(thInterfaces.getUser());
                    cargo.setFechaModificacion(new Date());
                    thCargoService.edit(cargo);
                } else {
                    cargo.setCodigo(thCargoService.getCode());
                    cargo.setUsuarioCreacion(thInterfaces.getUser());
                    cargo.setFechaCreacion(new Date());
                    cargo = thCargoService.create(cargo);
                }
                if (!edit) {
                    saveCargoRubro(cargo);
                } else {
                    List<ThCargoRubros> aux = thCargoRubrosService.findByCargoXanio(cargo, opcionBusqueda.getAnio());
                    if (aux == null || aux.isEmpty()) {
                        saveCargoRubro(cargo);
                    } else {
                        List<ThCargoRubros> temp = new ArrayList<>();
                        for (ThCargoRubros item : thCargoRubrosInsert) {
                            ThCargoRubros clone = Utils.clone(item);
                            for (ThCargoRubros _item : aux) {
                                if (clone.getIdRubro().getId().equals(_item.getIdRubro().getId())) {
                                    clone.setId(_item.getId());
                                    clone.setIdCargo(_item.getIdCargo());
                                }
                            }
                            temp.add(clone);
                        }
                        for (ThCargoRubros item : temp) {
                            if (item.getId() != null && item.getIdCargo() != null) {
                                thCargoRubrosService.edit(item);
                            } else {
                                ThCargoRubros clone = Utils.clone(item);
                                clone.setIdCargo(cargo);
                                clone.setId(null);
                                thCargoRubrosService.create(clone);
                            }
                        }
                    }
                }
            }
            clean();
            JsfUtil.addSuccessMessage("INFO!!!", "Se han modificado los cambios correctamente");
            PrimeFaces.current().ajax().update("formMain");
        } else {
            JsfUtil.addWarningMessage("AVISO!!!", "No hay datos generados");
        }
    }

    private void saveCargoRubro(ThCargo cargo) {
        if (thCargoRubrosInsert != null && !thCargoRubrosInsert.isEmpty()) {
            for (ThCargoRubros thCargoRubros : thCargoRubrosInsert) {
                thCargoRubros.setIdCargo(cargo);
                thCargoRubros.setId(null);
                thCargoRubrosService.create(thCargoRubros);
            }
        }
    }

    public List<ThCargo> getThCargoList() {
        return thCargoList;
    }

    public void setThCargoList(List<ThCargo> thCargoList) {
        this.thCargoList = thCargoList;
    }

    public List<String> getThCargosList() {
        return thCargosList;
    }

    public void setThCargosList(List<String> thCargosList) {
        this.thCargosList = thCargosList;
    }

    public String getThCargoSeleccionado() {
        return thCargoSeleccionado;
    }

    public void setThCargoSeleccionado(String thCargoSeleccionado) {
        this.thCargoSeleccionado = thCargoSeleccionado;
    }

    public List<CatalogoItem> getListTipoCargo() {
        return listTipoCargo;
    }

    public void setListTipoCargo(List<CatalogoItem> listTipoCargo) {
        this.listTipoCargo = listTipoCargo;
    }

    public List<CatalogoItem> getTipoCargoList() {
        return tipoCargoList;
    }

    public void setTipoCargoList(List<CatalogoItem> tipoCargoList) {
        this.tipoCargoList = tipoCargoList;
    }

    public List<UnidadAdministrativa> getUnidadesAdministrativasList() {
        return unidadesAdministrativasList;
    }

    public void setUnidadesAdministrativasList(List<UnidadAdministrativa> unidadesAdministrativasList) {
        this.unidadesAdministrativasList = unidadesAdministrativasList;
    }

    public List<ThEscalaSalarial> getThEscalaSalarialList() {
        return thEscalaSalarialList;
    }

    public void setThEscalaSalarialList(List<ThEscalaSalarial> thEscalaSalarialList) {
        this.thEscalaSalarialList = thEscalaSalarialList;
    }

    public List<ThRegimenLaboral> getThRegimenLaboralList() {
        return thRegimenLaboralList;
    }

    public void setThRegimenLaboralList(List<ThRegimenLaboral> thRegimenLaboralList) {
        this.thRegimenLaboralList = thRegimenLaboralList;
    }

    public UnidadAdministrativa getUnidadSeleccionado() {
        return unidadSeleccionado;
    }

    public void setUnidadSeleccionado(UnidadAdministrativa unidadSeleccionado) {
        this.unidadSeleccionado = unidadSeleccionado;
    }

    public ThEscalaSalarial getThEscalaSeleccionado() {
        return thEscalaSeleccionado;
    }

    public void setThEscalaSeleccionado(ThEscalaSalarial thEscalaSeleccionado) {
        this.thEscalaSeleccionado = thEscalaSeleccionado;
    }

    public ThRegimenLaboral getThRegimenSeleccionado() {
        return thRegimenSeleccionado;
    }

    public void setThRegimenSeleccionado(ThRegimenLaboral thRegimenSeleccionado) {
        this.thRegimenSeleccionado = thRegimenSeleccionado;
    }

    public CatalogoItem getTipoContratoSeleccionado() {
        return tipoContratoSeleccionado;
    }

    public void setTipoContratoSeleccionado(CatalogoItem tipoContratoSeleccionado) {
        this.tipoContratoSeleccionado = tipoContratoSeleccionado;
    }

    public CatalogoItem getClasificacionSeleccionado() {
        return clasificacionSeleccionado;
    }

    public void setClasificacionSeleccionado(CatalogoItem clasificacionSeleccionado) {
        this.clasificacionSeleccionado = clasificacionSeleccionado;
    }

    public OpcionBusqueda getOpcionBusqueda() {
        return opcionBusqueda;
    }

    public void setOpcionBusqueda(OpcionBusqueda opcionBusqueda) {
        this.opcionBusqueda = opcionBusqueda;
    }

    public CatalogoItem getFiltroContrato() {
        return filtroContrato;
    }

    public void setFiltroContrato(CatalogoItem filtroContrato) {
        this.filtroContrato = filtroContrato;
    }

    public CatalogoItem getFiltroClasificacion() {
        return filtroClasificacion;
    }

    public void setFiltroClasificacion(CatalogoItem filtroClasificacion) {
        this.filtroClasificacion = filtroClasificacion;
    }

    public List<ThCargoRubros> getThCargoRubrosInsert() {
        return thCargoRubrosInsert;
    }

    public void setThCargoRubrosInsert(List<ThCargoRubros> thCargoRubrosInsert) {
        this.thCargoRubrosInsert = thCargoRubrosInsert;
    }

    public LazyModel<ThRubro> getThRubroLazyModel() {
        return thRubroLazyModel;
    }

    public void setThRubroLazyModel(LazyModel<ThRubro> thRubroLazyModel) {
        this.thRubroLazyModel = thRubroLazyModel;
    }

    public List<Short> getListaPeriodo() {
        return listaPeriodo;
    }

    public void setListaPeriodo(List<Short> listaPeriodo) {
        this.listaPeriodo = listaPeriodo;
    }

    public UnidadAdministrativa getUnidadFind() {
        return unidadFind;
    }

    public void setUnidadFind(UnidadAdministrativa unidadFind) {
        this.unidadFind = unidadFind;
    }

}
