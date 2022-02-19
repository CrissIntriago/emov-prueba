/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.activos.controller;

import com.origami.sigef.common.entities.DetalleItem;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.util.JsfUtil;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 *
 * @author OrigamiEc
 */
@Named(value = "dialogItemCtaView")
@ViewScoped
public class DialogItemCta implements Serializable {

    private static final long serialVersionUID = 1L;
    private LazyModel<DetalleItem> lazyItem;
    Boolean bandera = Boolean.TRUE;
//    private Short periodo;
    private String cons;
//    private String periodoCta;
//    private Short periodoCtaNum;
    private String subGrupo;
    private Long subGrupoNum;

    @PostConstruct
    public void initview() {
        String tipo = JsfUtil.getRequestParameter("TIPO");
        //constatacion fisica inventario
        cons = JsfUtil.getRequestParameter("CONST_TIP");
        if (cons != null) {
            cons = "CONSTATACION";
        } else {
            cons = "OTROS";
        }
//        periodoCta = JsfUtil.getRequestParameter("PERIODO_CTA");
//        if (periodoCta != null) {
//            periodoCtaNum = Short.valueOf(periodoCta);
//        }
        subGrupo = JsfUtil.getRequestParameter("SUBGRUPO");
        if (subGrupo != null) {
            subGrupoNum = Long.valueOf(subGrupo);
        }

        if (cons.equals("CONSTATACION")) {
            bandera = Boolean.FALSE;
//<editor-fold defaultstate="collapsed" desc="cambios realizados">
//            if ((tipoGastos != null) && (cuentaContables == null) && (area == null) && (grupo == null) && (subGrupo == null)) {
//                lazyItem = new LazyModel<>(DetalleItem.class);
//                lazyItem.getFilterss().put("estado", true);
//                lazyItem.getFilterss().put("periodo", periodoCtaNum);
//                lazyItem.getFilterss().put("cuentaContable:noEqual", null);
//                lazyItem.getFilterss().put("tiposGastos.codigo:equal", tipoGastos);
//                lazyItem.getSorteds().put("orden", "ASC");
//            }
//            else if ((tipoGastos != null) && (cuentaContables != null) && (area == null) && (grupo == null) && (subGrupo == null)) {
//                lazyItem = new LazyModel<>(DetalleItem.class);
//                lazyItem.getFilterss().put("estado", true);
//                lazyItem.getFilterss().put("periodo", periodoCtaNum);
//                lazyItem.getFilterss().put("cuentaContable.codigo:equal", cuentaContables);
//                lazyItem.getFilterss().put("tiposGastos.codigo:equal", tipoGastos);
//                lazyItem.getSorteds().put("orden", "ASC");
//            }
//            else if((tipoGastos != null) && (cuentaContables != null) && (area != null) && (grupo == null) && (subGrupo == null)){
//                lazyItem = new LazyModel<>(DetalleItem.class);
//                lazyItem.getFilterss().put("estado", true);
//                lazyItem.getFilterss().put("periodo", periodoCtaNum);
//                lazyItem.getFilterss().put("cuentaContable.codigo:equal", cuentaContables);
//                lazyItem.getFilterss().put("tiposGastos.codigo:equal", tipoGastos);
//                lazyItem.getFilterss().put("asignarGrupo.padre:",subGrupoNum);
//                lazyItem.getSorteds().put("orden", "ASC");
//            }
//            else if((tipoGastos != null) && (cuentaContables != null) && (area != null) && (grupo != null) && (subGrupo == null)){
//                lazyItem = new LazyModel<>(DetalleItem.class);
//                lazyItem.getFilterss().put("estado", true);
//                lazyItem.getFilterss().put("periodo", periodoCtaNum);
//                lazyItem.getFilterss().put("cuentaContable.codigo:equal", cuentaContables);
//                lazyItem.getFilterss().put("tiposGastos.codigo:equal", tipoGastos);
//                lazyItem.getFilterss().put("asignarGrupo.padre",grupoNum);
//                lazyItem.getSorteds().put("orden", "ASC");
//            }
//</editor-fold>
            if ((subGrupo != null)) {
                lazyItem = new LazyModel<>(DetalleItem.class);
                lazyItem.getFilterss().put("estado", true);
//                lazyItem.getFilterss().put("periodo", periodoCtaNum);
                lazyItem.getFilterss().put("cuentaContable:noEqual", null);
                lazyItem.getFilterss().put("asignarGrupo.id:equal", subGrupoNum);
                lazyItem.getSorteds().put("orden", "ASC");
            }

        } else {
            lazyItem = new LazyModel<>(DetalleItem.class);
            lazyItem.getFilterss().put("estado", true);
            lazyItem.getFilterss().put("cuentaContable:noEqual", null);
//            lazyItem.getFilterss().put("periodo", periodo);
            lazyItem.getSorteds().put("orden", "ASC");
            try {
                if (tipo.equals("INGRESO")) {
                    bandera = Boolean.FALSE;
                }
            } catch (NullPointerException ex) {
                bandera = Boolean.TRUE;
            }
        }

    }

    public void close(DetalleItem item) {
        if (bandera == false) {
            PrimeFaces.current().dialog().closeDynamic(item);
        }
        if (bandera == true) {
            if (item.getCantidadExistente() > 0) {
                PrimeFaces.current().dialog().closeDynamic(item);
            } else {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Información", "NO EXISTE STOCK DE " + item.getDescripcion());
                PrimeFaces.current().dialog().showMessageDynamic(message);
            }
        }
    }

    public LazyModel<DetalleItem> getLazyItem() {
        return lazyItem;
    }

    public void setLazyItem(LazyModel<DetalleItem> lazyItem) {
        this.lazyItem = lazyItem;
    }

    public Boolean getBandera() {
        return bandera;
    }

    public void setBandera(Boolean bandera) {
        this.bandera = bandera;
    }

    public String getCons() {
        return cons;
    }

    public void setCons(String cons) {
        this.cons = cons;
    }

    public String getSubGrupo() {
        return subGrupo;
    }

    public void setSubGrupo(String subGrupo) {
        this.subGrupo = subGrupo;
    }

    public Long getSubGrupoNum() {
        return subGrupoNum;
    }

    public void setSubGrupoNum(Long subGrupoNum) {
        this.subGrupoNum = subGrupoNum;
    }

}
