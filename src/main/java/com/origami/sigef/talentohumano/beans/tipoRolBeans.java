/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.talentohumano.beans;

import com.origami.sigef.catalogoItem.service.CatalogoItemService;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.DiasLaborado;
import com.origami.sigef.common.entities.HoraExtraSuplementaria;
import com.origami.sigef.common.entities.MasterCatalogo;
import com.origami.sigef.common.entities.TipoRol;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.MasterCatalogoService;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.talentohumano.services.DiasLaboradoService;
import com.origami.sigef.talentohumano.services.HoraExtraSuplementariaService;
import com.origami.sigef.talentohumano.services.TipoRolService;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import org.primefaces.PrimeFaces;

/**
 *
 * @author Luis Pozo G
 */
@Named(value = "tipoRolBeans")
@ViewScoped
public class tipoRolBeans implements Serializable {

    @Inject
    private TipoRolService tipoRolService;
    private LazyModel<TipoRol> lazyTipoRol;
    private TipoRol tipoRol;
    private List<TipoRol> listaTipoRolCreados;

    @Inject
    private CatalogoItemService itemService;
    private List<CatalogoItem> listMeses;
    private List<CatalogoItem> listTipoRol;
    private CatalogoItem estadoTipoRol;

    @Inject
    private UserSession userSession;
    private List<MasterCatalogo> periodos;
    @Inject
    private MasterCatalogoService masterService;
    private Short anioAux;

    private boolean editBlock;
    private boolean deleteBlock;
    
    @Inject
    private DiasLaboradoService diaService;
    private List<DiasLaborado> listDiasLaborados;
    @Inject
    private HoraExtraSuplementariaService horaSuplemService;
    private List<HoraExtraSuplementaria> listHoraSuplementaria;

    @PostConstruct
    public void init() {
        anioAux = Utils.getAnio(new Date()).shortValue();
        estadoTipoRol = new CatalogoItem();
        tipoRol = new TipoRol();
        listMeses = itemService.findByNamedQuery("CatalogoItem.findCatalogoClasificacion1", "meses_anio");
        listTipoRol = itemService.findByNamedQuery("CatalogoItem.findCatalogoClasificacion1", "tipo_rol");
        periodos = masterService.findByNamedQuery("MasterCatalogo.findByCatalogoAndTipo", new Object[]{"tipo_cuenta", "D"});
        editBlock = false;
        lazyTipoRol = new LazyModel<>(TipoRol.class);
        lazyTipoRol.getFilterss().put("estado", true);
        lazyTipoRol.getFilterss().put("anio", anioAux);
        JsfUtil.addSuccessMessage("Información", "Selecciono el Período " +anioAux);
        if (lazyTipoRol.getRowCount() >= 1) {
            JsfUtil.addInformationMessage("Información", "Datos Cargados de este rol Correctamente.");
        }
    }

    public void form(TipoRol tipo) {
        if (anioAux == null) {
            JsfUtil.addWarningMessage("Información", "Debe ingresar el año del Período de Rol para Agregar Período de Rol.");
            return;
        }
        if (tipo != null) {
            setEditBlock(true);
            tipoRol = tipo;
        } else {
            setEditBlock(false);
            tipoRol = new TipoRol();
            tipoRol.setAnio(anioAux);
        }
        PrimeFaces.current().executeScript("PF('dlgCabeceraRol').show()");
        PrimeFaces.current().ajax().update(":panelCabeceraRol");
    }

    public void guardarRolxMes() {
        if ("".equals(tipoRol.getDescripcion())) {
            JsfUtil.addWarningMessage("Información", " Debe ingresar la Descripción del Período de Rol.");
            return;
        }
        if (tipoRol.getAnio() == null) {
            JsfUtil.addWarningMessage("Información", " Debe ingresar el año del Período de Rol.");
            return;
        }
        if (tipoRol.getTipoRol() == null) {
            JsfUtil.addWarningMessage("Información", " Debe ingresar el tipo de Rol del Periodo.");
            return;
        }
        if (tipoRol.getMes() == null) {
            JsfUtil.addWarningMessage("Información", " Debe ingresar el tipo de Rol.");
            return;
        }
        Boolean validar = false;
        listaTipoRolCreados = new ArrayList<>();
        listaTipoRolCreados = tipoRolService.findByNamedQuery("TipoRol.findAllTrue");
        if (!listaTipoRolCreados.isEmpty()) {
            for (int i = 0; i < listaTipoRolCreados.size(); i++) {
                if (Objects.equals(listaTipoRolCreados.get(i).getMes(), tipoRol.getMes()) && Objects.equals(listaTipoRolCreados.get(i).getTipoRol(), tipoRol.getTipoRol()) && Objects.equals(listaTipoRolCreados.get(i).getAnio(), tipoRol.getAnio())) {
                    validar = true;
                    break;
                }
            }
        }

        boolean edit = tipoRol.getId() != null;
        if (!edit) {
            if (validar == true) {
                JsfUtil.addWarningMessage("Información", " Este período para este Tipo ya Existe.");
                return;
            }
            tipoRol.setUsuarioCreacion(userSession.getNameUser());
            tipoRol.setUsuarioModifica(userSession.getNameUser());
            tipoRol.setFechaCreacion(new Date());
            tipoRol.setEstado(Boolean.TRUE);
            tipoRol.setEstadoAprobacion(getTipoEstado("registrado-rol"));
            tipoRol.setAprobacion(Boolean.FALSE);
            tipoRol.setFechaModificacion(new Date());
            tipoRol = tipoRolService.create(tipoRol);
        } else {
            tipoRol.setFechaModificacion(new Date());
            tipoRol.setUsuarioModifica(userSession.getNameUser());
            tipoRolService.edit(tipoRol);
        }
        PrimeFaces.current().executeScript("PF('dlgCabeceraRol').hide()");
        JsfUtil.addSuccessMessage("Información", tipoRol.getDescripcion() + (edit ? " editada" : " registrada") + " con éxito.");
    }

    public CatalogoItem getTipoEstado(String codigo) {
        estadoTipoRol = new CatalogoItem();
        estadoTipoRol = itemService.getEstadoRol(codigo);
        return estadoTipoRol;
    }

    public void eliminar(TipoRol tipo) {
        JsfUtil.addSuccessMessage("Información", tipo.getDescripcion() + " eliminada con éxito");
        tipo.setEstado(Boolean.FALSE);
        tipoRolService.edit(tipo);
        PrimeFaces.current().ajax().update(":formMain");
    }

    public void buscarXPeriodo() {
        if (anioAux == null) {
            JsfUtil.addWarningMessage("Información", " Debe ingresar el Período para realizar una Busqueda.");
            return;
        }
        listaTipoRolCreados = new ArrayList<>();
        listaTipoRolCreados = tipoRolService.findByNamedQuery("TipoRol.findAllTrue");
        if (!listaTipoRolCreados.isEmpty()) {
            for (TipoRol item : listaTipoRolCreados) {
                listDiasLaborados = new ArrayList<>();
                listDiasLaborados = diaService.getDiasxTipoRol(item);
                listHoraSuplementaria = new ArrayList<>();
                listHoraSuplementaria = horaSuplemService.getHorasSuplemxTipoRol(item);
                if (!listaTipoRolCreados.isEmpty() || !listHoraSuplementaria.isEmpty()) {
                    item.setAprobacion(Boolean.TRUE);
                    tipoRolService.edit(item);
                }
                if (listDiasLaborados.isEmpty() && listHoraSuplementaria.isEmpty()) {
                    item.setAprobacion(Boolean.FALSE);
                    tipoRolService.edit(item);
                }
            }
        }
        tipoRol.setAnio(anioAux);
        lazyTipoRol.getFilterss().put("anio", tipoRol.getAnio());
        JsfUtil.addSuccessMessage("Información", "Selecciono el Período " + tipoRol.getAnio());
    }

//<editor-fold defaultstate="collapsed" desc="Get and Set">
    public LazyModel<TipoRol> getLazyTipoRol() {
        return lazyTipoRol;
    }

    public void setLazyTipoRol(LazyModel<TipoRol> lazyTipoRol) {
        this.lazyTipoRol = lazyTipoRol;
    }

    public TipoRol getTipoRol() {
        return tipoRol;
    }

    public void setTipoRol(TipoRol tipoRol) {
        this.tipoRol = tipoRol;
    }

    public List<CatalogoItem> getListMeses() {
        return listMeses;
    }

    public void setListMeses(List<CatalogoItem> listMeses) {
        this.listMeses = listMeses;
    }

    public List<CatalogoItem> getListTipoRol() {
        return listTipoRol;
    }

    public void setListTipoRol(List<CatalogoItem> listTipoRol) {
        this.listTipoRol = listTipoRol;
    }

    public List<MasterCatalogo> getPeriodos() {
        return periodos;
    }

    public void setPeriodos(List<MasterCatalogo> periodos) {
        this.periodos = periodos;
    }

    public Short getAnioAux() {
        return anioAux;
    }

    public void setAnioAux(Short anioAux) {
        this.anioAux = anioAux;
    }

    public boolean isEditBlock() {
        return editBlock;
    }

    public void setEditBlock(boolean editBlock) {
        this.editBlock = editBlock;
    }

    public boolean isDeleteBlock() {
        return deleteBlock;
    }

    public void setDeleteBlock(boolean deleteBlock) {
        this.deleteBlock = deleteBlock;
    }
//    public List<CatalogoItem> getListTipoEstadoRol() {
//        return listTipoEstadoRol;
//    }
//
//    public void setListTipoEstadoRol(List<CatalogoItem> listTipoEstadoRol) {
//        this.listTipoEstadoRol = listTipoEstadoRol;
//    }
//</editor-fold>

}
