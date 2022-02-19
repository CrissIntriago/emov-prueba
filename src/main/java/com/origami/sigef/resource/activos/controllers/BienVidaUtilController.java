/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.activos.controllers;

import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.resource.activos.entities.BienVidaUtil;
import com.origami.sigef.resource.activos.services.BienVidaUtilService;
import com.origami.sigef.resource.contabilidad.entities.ContCuentas;
import java.io.Serializable;
import java.util.ArrayList;
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
@Named(value = "bienVidaUtilView")
@ViewScoped
public class BienVidaUtilController implements Serializable {

    private BienVidaUtil vidaUtil;
    private ContCuentas cuentaClasif;
    private ContCuentas cuentaContable;
    private List<ContCuentas> listCuentaContableClasif;
    private List<ContCuentas> listCuentasDepreciacion;
    private LazyModel<BienVidaUtil> lazyVidaUtil;
    private Boolean utpe;

    @Inject
    private BienVidaUtilService vidaUtilService;

    @PostConstruct
    public void initView() {
        vidaUtil = new BienVidaUtil();
        cuentaClasif = new ContCuentas();
        listCuentaContableClasif = vidaUtilService.getClasificacionByTipoBien(3, "14", "6", "9");
        listCuentasDepreciacion = new ArrayList<>();

        lazyVidaUtil = new LazyModel<>(BienVidaUtil.class);
        lazyVidaUtil.getFilterss().put("estado", Boolean.TRUE);
        lazyVidaUtil.getSorteds().put("cuentaContable.codigo", "ASC");
        lazyVidaUtil.setDistinct(false);
    }

    public void formNew(BienVidaUtil bien) {
        Subject subject = SecurityUtils.getSubject();

        if (bien != null) {
            this.vidaUtil = bien;
            vidaUtil.setFechaModificacion(new Date());
            vidaUtil.setUsuarioModifica(subject.getPrincipal().toString());
            cuentaClasif = getPadreOfCuenta(vidaUtil.getCuentaContable().getPadre());
            actualizarCuentaDepreciacion();
            PrimeFaces.current().executeScript("PF('dlgVidaUtil').show()");

        } else {
            vidaUtil = new BienVidaUtil();
            vidaUtil.setCuentaContable(new ContCuentas());
            cuentaClasif = new ContCuentas();
            vidaUtil.setUsuarioCreador(subject.getPrincipal().toString());
            vidaUtil.setFechaCreacion(new Date());
            utpe = Boolean.FALSE;
            PrimeFaces.current().executeScript("PF('dlgVidaUtil').show()");
            PrimeFaces.current().ajax().update("frmVidaUtil");
        }
    }

    public void buscarCuenta() {
        if (cuentaClasif == null) {
            JsfUtil.addErrorMessage("ERROR", "Ingrese una Clasificación");
            return;
        }
        if (vidaUtil.getCuentaContable().getCodigo() != null) {
            ContCuentas cta = vidaUtilService.getCuentaContable(vidaUtil.getCuentaContable().getCodigo(), cuentaClasif.getCodigo());
            if (cta != null) {
                this.vidaUtil.setCuentaContable(cta);
                actualizarCuentaDepreciacion();
                PrimeFaces.current().ajax().update("ccGrupo");
                PrimeFaces.current().ajax().update("ccDescripcion");
            } else {
                Map<String, List<String>> params = new HashMap<>();
                params.put("TIPOACTIVO", Arrays.asList("BIENES"));
                params.put("CODIGO", Arrays.asList(cuentaClasif.getCodigo()));
                Utils.openDialog("/facelet/activos/inventario/Dialog/dialogCuentaContable", "65%", "55%", params);

            }
        } else {
            Map<String, List<String>> params = new HashMap<>();
            params.put("TIPOACTIVO", Arrays.asList("BIENES"));
            params.put("CODIGO", Arrays.asList(cuentaClasif.getCodigo()));
            Utils.openDialog("/facelet/activos/inventario/Dialog/dialogCuentaContable", "65%", "55%", params);
        }
    }

    public void limpiarCuentaBien() {
        vidaUtil.setCuentaContable(new ContCuentas());
    }

    public void selectCuenta(SelectEvent event) {
        cuentaContable = (ContCuentas) event.getObject();
        vidaUtil.setCuentaContable(cuentaContable);
        cuentaContable = new ContCuentas();
        actualizarCuentaDepreciacion();
    }

    public void vidaUtilEstimada() {
        if (utpe) {
            vidaUtil.setVidaUtil(null);
        }
    }

    public void cancelarBien() {
        vidaUtil = new BienVidaUtil();
        cuentaContable = new ContCuentas();
        cuentaClasif = new ContCuentas();
        utpe = Boolean.FALSE;
        PrimeFaces.current().executeScript("PF('dlgVidaUtil').hide()");
    }

    public void borrarBien(BienVidaUtil vUtil) {
        vUtil.setEstado(Boolean.FALSE);
        vidaUtilService.edit(vUtil);
        PrimeFaces.current().ajax().update(":dtVidaUtil");
    }

    public void saveVidaUTilBien() {
        if (vidaUtil.getDescripcion() == null) {
            JsfUtil.addErrorMessage("Error", "Ingrese una descripción");
            return;
        }
        if (vidaUtil.getCuentaContable() == null) {
            JsfUtil.addErrorMessage("ERROR", "Asigne una Cuenta Contable");
            return;
        }
        if (!utpe && (vidaUtil.getVidaUtil() == null || vidaUtil.getVidaUtil() <= 0)) {
            JsfUtil.addWarningMessage("Advertencia", "La vida útil debe ser mayor a 0");
            return;
        }

        if (vidaUtil.getEstado()) {
            vidaUtilService.edit(vidaUtil);
            JsfUtil.addInformationMessage("Editado", vidaUtil.getDescripcion() + " Correctamente editado.");
            PrimeFaces.current().executeScript("PF('dlgVidaUtil').hide()");
        } else {
            List<BienVidaUtil> listVerificadora = vidaUtilService.verificarListaVidaUtil(vidaUtil.getDescripcion(), vidaUtil.getCuentaContable());
            if (!listVerificadora.isEmpty()) {
                JsfUtil.addWarningMessage("Advertencia", "Este registro ya ha sido generado");
                return;
            }
            vidaUtil.setEstado(Boolean.TRUE);
            vidaUtilService.create(vidaUtil);
            JsfUtil.addInformationMessage("Guardado", vidaUtil.getDescripcion() + " Correctamente Guardado.");
            PrimeFaces.current().executeScript("PF('dlgVidaUtil').hide()");
        }
    }

    public void actualizarCuentaDepreciacion() {
        listCuentasDepreciacion = vidaUtilService.getCuentaContableDeActivosDepreciables("14199");
        if (vidaUtil.getCtaDepreciacion() == null) {
            for (ContCuentas depreciac : listCuentasDepreciacion) {
                if (depreciac.getCodigo().endsWith(vidaUtil.getCuentaContable().getCodigo().substring(6, 7))) {
                    vidaUtil.setCtaDepreciacion(depreciac);
                }
            }
        }
    }

    public ContCuentas getPadreOfCuenta(ContCuentas cta) {
        return vidaUtilService.getPadreOfCta(cta);
    }

//    public String getPadreOfCuentaDescripcion(ContCuentas cta) {
//        if (cta != null) {
//            if (cta.getPadre() != null) {
//                return "";
//            } else {
//                ContCuentas c = vidaUtilService.getPadreOfCta(cta.getPadre());
//                if (c != null) {
//                    return c.getDescripcion();
//                } else {
//                    return "";
//                }
//            }
//        } else {
//            return "";
//        }
//
//    }
    public BienVidaUtil getVidaUtil() {
        return vidaUtil;
    }

    public void setVidaUtil(BienVidaUtil vidaUtil) {
        this.vidaUtil = vidaUtil;
    }

    public ContCuentas getCuentaClasif() {
        return cuentaClasif;
    }

    public void setCuentaClasif(ContCuentas cuentaClasif) {
        this.cuentaClasif = cuentaClasif;
    }

    public ContCuentas getCuentaContable() {
        return cuentaContable;
    }

    public void setCuentaContable(ContCuentas cuentaContable) {
        this.cuentaContable = cuentaContable;
    }

    public LazyModel<BienVidaUtil> getLazyVidaUtil() {
        return lazyVidaUtil;
    }

    public void setLazyVidaUtil(LazyModel<BienVidaUtil> lazyVidaUtil) {
        this.lazyVidaUtil = lazyVidaUtil;
    }

    public List<ContCuentas> getListCuentaContableClasif() {
        return listCuentaContableClasif;
    }

    public void setListCuentaContableClasif(List<ContCuentas> listCuentaContableClasif) {
        this.listCuentaContableClasif = listCuentaContableClasif;
    }

    public Boolean getUtpe() {
        return utpe;
    }

    public void setUtpe(Boolean utpe) {
        this.utpe = utpe;
    }

    public List<ContCuentas> getListCuentasDepreciacion() {
        return listCuentasDepreciacion;
    }

    public void setListCuentasDepreciacion(List<ContCuentas> listCuentasDepreciacion) {
        this.listCuentasDepreciacion = listCuentasDepreciacion;
    }

}
