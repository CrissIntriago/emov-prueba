/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.activos.controller;

import com.origami.sigef.activos.service.BienesMovimientoService;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.CatalogoMovimiento;
import com.origami.sigef.common.entities.CuentaContable;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.MasterCatalogoService;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.resource.contabilidad.entities.ContCuentas;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 *
 * @author Sandra Arroba
 */
@Named(value = "dialogCuentaContableView")
@ViewScoped
public class DialogCuentaContableController implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @Inject
    private MasterCatalogoService masterService;
    @Inject
    private BienesMovimientoService bienesMovimientoService;

    private static final Logger LOG = Logger.getLogger(DialogCuentaContableController.class.getName());
    private LazyModel<ContCuentas> lazyContCuentas;
    private CuentaContable cuentaContable;
    private ContCuentas contCuentas;
    private CatalogoMovimiento catalogoMovimiento;
    private CatalogoItem catalogoItem;
    private List<ContCuentas> listCC;

    private String codigo;
//    private String movimiento;
    private Long orden = 4L;
    private String nombre;

    @PostConstruct
    public void initView() {
        cuentaContable = new CuentaContable();
        contCuentas = new ContCuentas();
        lazyContCuentas = new LazyModel<>(ContCuentas.class);

        String tipoActivo = JsfUtil.getRequestParameter("TIPOACTIVO");
        String grupoBien = JsfUtil.getRequestParameter("GRUPOBIEN"); //Borrar
        String movBien = JsfUtil.getRequestParameter("MOVIMIENTO");
        codigo = JsfUtil.getRequestParameter("CODIGO");
        String codigoS = JsfUtil.getRequestParameter("CODSTART");
        String codigoE = JsfUtil.getRequestParameter("CODEND");
        String movimiento = JsfUtil.getRequestParameter("MOVIMIENTOCUENTA");//1 true 0false

        if (codigo.equals("911")) {
            orden = 3L;
            nombre = "Bienes";
        }
        if (codigo.equals("14")) {
            orden = 4L;
        }
        listCC = bienesMovimientoService.getFilterOfCuenta(codigo, orden, nombre);
        try {
            if (tipoActivo == null) {
                lazyContCuentas.addFilter("codigo:startsWith", codigo);
            } else {
                if (tipoActivo.equals("BIENES") && movBien == null) {
                    lazyContCuentas.addFilter("codigo:startsWith", codigo);
                    if (codigo.equals("911")) {
                        lazyContCuentas.addFilter("descripcion:startsWith", "Bienes");
                    } else {
                        if (codigo.startsWith("14")) {
                            codigoE = "98";
                        }
                        lazyContCuentas.addFilter("codigo:between", Arrays.asList(codigo + "01%", codigo + codigoE + "%"));
                    }
                }
                if (tipoActivo.equals("BIENES") && movBien != null) {
                    lazyContCuentas.addFilter("codigo:startsWith", codigo);
                    if (codigo.equals("911")) {
                        lazyContCuentas.addFilter("descripcion:startsWith", "Bienes");
                    } else {
                        lazyContCuentas.addFilter("codigo:between", Arrays.asList(codigo + codigoS + "%", codigo + codigoE + "%"));
                    }
                }

            }
            if (movimiento != null) {
                if (movimiento.equals("SI")) {
                    lazyContCuentas.addFilter("movimiento", true);
                } else if (movimiento.equals("NO")) {
                    lazyContCuentas.addFilter("movimiento", false);
                }

            }
        } catch (NullPointerException ex) {
            LOG.log(Level.SEVERE, codigo, ex);
        }
    }

    public void close(ContCuentas cta) {
        PrimeFaces.current().dialog().closeDynamic(cta);
    }
//<editor-fold defaultstate="collapsed" desc="Getters and Setters">

    public LazyModel<ContCuentas> getLazyContCuentas() {
        return lazyContCuentas;
    }

    public void setLazyContCuentas(LazyModel<ContCuentas> lazyContCuentas) {
        this.lazyContCuentas = lazyContCuentas;
    }

    public ContCuentas getContCuentas() {
        return contCuentas;
    }

    public void setContCuentas(ContCuentas contCuentas) {
        this.contCuentas = contCuentas;
    }

    public CuentaContable getCuentaContable() {
        return cuentaContable;
    }

    public void setCuentaContable(CuentaContable cuentaContable) {
        this.cuentaContable = cuentaContable;
    }

//    public List<MasterCatalogo> getListCuentaContable() {
//        return listCuentaContable;
//    }
//    
//    public void setListCuentaContable(List<MasterCatalogo> listCuentaContable) {
//        this.listCuentaContable = listCuentaContable;
//    }
//    
    public MasterCatalogoService getMasterService() {
        return masterService;
    }

    public void setMasterService(MasterCatalogoService masterService) {
        this.masterService = masterService;
    }

    public List<ContCuentas> getListCC() {
        return listCC;
    }

    public void setListCC(List<ContCuentas> listCC) {
        this.listCC = listCC;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public CatalogoMovimiento getCatalogoMovimiento() {
        return catalogoMovimiento;
    }

    public void setCatalogoMovimiento(CatalogoMovimiento catalogoMovimiento) {
        this.catalogoMovimiento = catalogoMovimiento;
    }

    public CatalogoItem getCatalogoItem() {
        return catalogoItem;
    }

    public void setCatalogoItem(CatalogoItem catalogoItem) {
        this.catalogoItem = catalogoItem;
    }

    public Long getOrden() {
        return orden;
    }

    public void setOrden(Long orden) {
        this.orden = orden;
    }

//    public String getMovimiento() {
//        return movimiento;
//    }
//
//    public void setMovimiento(String movimiento) {
//        this.movimiento = movimiento;
//    }
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
//</editor-fold>
}
