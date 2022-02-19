/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.activos.controllers;

import com.origami.sigef.activos.service.BienesItemService;
import com.origami.sigef.activos.service.BienesMovimientoService;
import com.origami.sigef.common.entities.BienesItem;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.resource.contabilidad.entities.ContCuentas;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author Sandra Arroba
 */
@Named(value = "bienClasificacionView")
@ViewScoped
public class BienClasificacionController implements Serializable {

    private BienesItem bienesItem; //Para Grupo
    private ContCuentas cuentaContable;
    private LazyModel<BienesItem> lazyGrupoBien;

    private List<ContCuentas> listCuentaContableClasifGrupo;
    private List<ContCuentas> listCuentaContable;
    private List<BienesItem> listBienesItem;
    private List<ContCuentas> listBienesItemFilterCta;

    private Boolean habilitar = Boolean.FALSE;

    @Inject
    private BienesItemService bienesItemService;

    @Inject
    private BienesMovimientoService bienesMovimientoService;

    @PostConstruct
    public void initView() {
        cuentaContable = new ContCuentas();
        /*Init Grupo Bien*/
        bienesItem = new BienesItem();
        bienesItem.setGrupoPadre(new BienesItem());
        lazyGrupoBien = new LazyModel<>(BienesItem.class);
        lazyGrupoBien.getFilterss().put("estado", Boolean.TRUE);
        lazyGrupoBien.getFilterss().put("itemBienBoolean:equal", Boolean.FALSE);
        lazyGrupoBien.getFilterss().put("componente:equal", Boolean.FALSE);
        lazyGrupoBien.getFilterss().put("grupoPadre:equal", null);
        lazyGrupoBien.getSorteds().put("codigoBienAgrupado", "ASC");
        listBienesItem = bienesItemService.getBienGrupo(); //Borrar si no se usa, verificar :v
        listBienesItemFilterCta = bienesItemService.getFilterCtaBienGrupo();
        listCuentaContableClasifGrupo = bienesMovimientoService.getClasificacionByTipoBien(3L, "14", "911", "6", "9", null, null);
        /* Termina init Grupo Bien*/

    }

    public void formNewGrupo(BienesItem itemGrupo) {
        Subject subject = SecurityUtils.getSubject();
        habilitar = Boolean.TRUE;
        if (itemGrupo != null) {
            habilitar = Boolean.TRUE;
            this.bienesItem = itemGrupo;
            bienesItem.setFechaModificacion(new Date());
            bienesItem.setUsuarioModifica(subject.getPrincipal().toString());
            PrimeFaces.current().executeScript("PF('dlgRegSubGrupo').show()");

        } else {
            bienesItem = new BienesItem();
            bienesItem.setCuentaContable(new ContCuentas());
            bienesItem.setUsuarioCreador(subject.getPrincipal().toString());
            bienesItem.setFechaCreacion(new Date());
            PrimeFaces.current().executeScript("PF('dlgRegSubGrupo').show()");
            PrimeFaces.current().ajax().update("frmRegClasificacion");
        }
    }

    public void buscarCuentaGrupo() {
        if (bienesItem.getClasificTipoBien() == null) {
            JsfUtil.addErrorMessage("ERROR", "Ingrese una Clasificación");
            return;
        }
        if (bienesItem.getCuentaContable().getCodigo() != null) {
            ContCuentas cta = bienesItemService.getCuentaContable(bienesItem.getCuentaContable().getCodigo(), bienesItem.getClasificTipoBien().getCodigo());
            if (cta != null) {
                this.bienesItem.setCuentaContable(cta);
                Long orden = bienesItemService.getNivelOfGrupo(bienesItem.getCuentaContable());
                bienesItem.setOrden(orden);
                String completarCadenaConCeros = Utils.completarCadenaConCeros(bienesItem.getOrden() + "", 2);
                bienesItem.setCodigoBien(completarCadenaConCeros);
                PrimeFaces.current().ajax().update("fsetCuentaGrupo");
                PrimeFaces.current().ajax().update("codigoBien");
            } else {
                Map<String, List<String>> params = new HashMap<>();
                params.put("TIPOACTIVO", Arrays.asList("BIENES"));
                params.put("CODIGO", Arrays.asList(bienesItem.getClasificTipoBien().getCodigo()));
                Utils.openDialog("/facelet/activos/inventario/Dialog/dialogCuentaContable", "65%", "55%", params);

            }
        } else {
            Map<String, List<String>> params = new HashMap<>();
            params.put("TIPOACTIVO", Arrays.asList("BIENES"));
            params.put("CODIGO", Arrays.asList(bienesItem.getClasificTipoBien().getCodigo()));
            Utils.openDialog("/facelet/activos/inventario/Dialog/dialogCuentaContable", "65%", "55%", params);
        }
    }

    public void limpiarCuentaGrupo() {
        bienesItem.setCuentaContable(new ContCuentas());
//        bienesItem.setOrden(null);
        bienesItem.setCodigoBien(null);
    }

    public void selectCuentaGrupo(SelectEvent event) {
        cuentaContable = (ContCuentas) event.getObject();
        bienesItem.setCuentaContable(cuentaContable);
        Long orden = bienesItemService.getNivelOfGrupo(bienesItem.getCuentaContable());
        bienesItem.setOrden(orden);
        String completarCadenaConCeros = Utils.completarCadenaConCeros(bienesItem.getOrden() + "", 2);
        bienesItem.setCodigoBien(completarCadenaConCeros);
        cuentaContable = new ContCuentas();
    }

    public void borrarBienGrupoLista(BienesItem grup) {
        grup.setEstado(Boolean.FALSE);
        bienesItemService.edit(grup);
        PrimeFaces.current().ajax().update(":dtGrupoBien");
    }

    public void cancelarGrupoBien() {
        bienesItem = new BienesItem();
        cuentaContable = new ContCuentas();
        habilitar = Boolean.FALSE;
        PrimeFaces.current().executeScript("PF('dlgRegSubGrupo').hide()");
    }

    public void saveGrupoBien() {
        if (bienesItem.getDescripcion() == null) {
            JsfUtil.addErrorMessage("Error", "Ingrese una descripción");
            return;
        }
        if (bienesItem.getCuentaContable() == null) {
            JsfUtil.addErrorMessage("ERROR", "Asigne una Cuenta Contable al Grupo");
            return;
        }

        if (bienesItemService.verificarClasificacionRepetida(bienesItem.getCuentaContable().getCodigo(), bienesItem.getDescripcion(), bienesItem.getPeriodo())) {
            JsfUtil.addWarningMessage("ADVERTENCIA", "El SubGrupo ya ha sido generado");
            return;
        }

        bienesItem.setDescripcion(bienesItem.getDescripcion().toUpperCase());
        if (bienesItem.getCodigoBienAgrupado() != null) {
            habilitar = Boolean.FALSE;
            bienesItemService.edit(bienesItem);
            JsfUtil.addInformationMessage("Editado", bienesItem.getCodigoBienAgrupado() + " Correctamente editado.");
            PrimeFaces.current().executeScript("PF('dlgRegSubGrupo').hide()");
        } else {
            bienesItem.setEstado(Boolean.TRUE);
            habilitar = Boolean.FALSE;
            bienesItem.setItemBienBoolean(Boolean.FALSE);
            bienesItem.setComponente(Boolean.FALSE);
            bienesItem.setCodigoBienAgrupado(bienesItem.getCuentaContable().getCodigo() + bienesItem.getCodigoBien());
            bienesItemService.create(bienesItem);
            JsfUtil.addInformationMessage("Guardado", bienesItem.getCodigoBienAgrupado() + " Correctamente Guardado.");
            PrimeFaces.current().executeScript("PF('dlgRegSubGrupo').hide()");
        }
    }

    //<editor-fold defaultstate="collapsed" desc="GETTER AND SETTER">
    public BienesItem getBienesItem() {
        return bienesItem;
    }

    public void setBienesItem(BienesItem bienesItem) {
        this.bienesItem = bienesItem;
    }

    public ContCuentas getCuentaContable() {
        return cuentaContable;
    }

    public void setCuentaContable(ContCuentas cuentaContable) {
        this.cuentaContable = cuentaContable;
    }

    public LazyModel<BienesItem> getLazyGrupoBien() {
        return lazyGrupoBien;
    }

    public void setLazyGrupoBien(LazyModel<BienesItem> lazyGrupoBien) {
        this.lazyGrupoBien = lazyGrupoBien;
    }   

    public List<ContCuentas> getListCuentaContableClasifGrupo() {
        return listCuentaContableClasifGrupo;
    }

    public void setListCuentaContableClasifGrupo(List<ContCuentas> listCuentaContableClasifGrupo) {
        this.listCuentaContableClasifGrupo = listCuentaContableClasifGrupo;
    }

    public List<ContCuentas> getListCuentaContable() {
        return listCuentaContable;
    }

    public void setListCuentaContable(List<ContCuentas> listCuentaContable) {
        this.listCuentaContable = listCuentaContable;
    }

    public List<BienesItem> getListBienesItem() {
        return listBienesItem;
    }

    public void setListBienesItem(List<BienesItem> listBienesItem) {
        this.listBienesItem = listBienesItem;
    }

    public List<ContCuentas> getListBienesItemFilterCta() {
        return listBienesItemFilterCta;
    }

    public void setListBienesItemFilterCta(List<ContCuentas> listBienesItemFilterCta) {
        this.listBienesItemFilterCta = listBienesItemFilterCta;
    }

    public Boolean getHabilitar() {
        return habilitar;
    }

    public void setHabilitar(Boolean habilitar) {
        this.habilitar = habilitar;
    }
//</editor-fold>
}
