/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.activos.controller;

import com.origami.sigef.activos.service.ProcesoIngresoService;
import com.origami.sigef.common.entities.Inventario;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.util.JsfUtil;
import java.io.Serializable;
import java.util.Arrays;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 *
 * @author OrigamiEc
 */
@Named(value = "DialogEgresoController")
@ViewScoped
public class DialogEgresoController implements Serializable {

    private static final long serialVersionUID = 1L;
    private String titulo1;
    private String titulo2;
    private String parametro;
    private Boolean ban;
    private LazyModel<Inventario> lazyEgreso;

    @Inject
    private ProcesoIngresoService procesoIngresoService;

    @PostConstruct
    public void initView() {
        String tipo = JsfUtil.getRequestParameter("TIPO");
        lazyEgreso = new LazyModel<>(Inventario.class);
        lazyEgreso.getFilterss().put("estado", true);
        lazyEgreso.getFilterss().put("tipoMovimiento", tipo);
        if (tipo.equals("EGRESO")) {
            lazyEgreso.getFilterss().put("motivoMovimiento.texto:notEqual", "ANULACIÓN");
            titulo1 = "EGRESOS REGISTRADOS";
            titulo2 = "LISTA DE EGRESOS REGISTRADOS";
            parametro = "EGRESO";
            ban = Boolean.TRUE;
        } else {
            lazyEgreso.getFilterss().put("motivoMovimiento.texto:notEqual", Arrays.asList("INGRESO POR DEVOLUCION","INGRESO POR ANULACION"));
            titulo1 = "INGRESOS REGISTRADOS";
            titulo2 = "LISTA DE INGRESOS REGISTRADOS";
            parametro = "INGRESO";
            ban = Boolean.FALSE;
        }
    }

    public void close(Inventario inv) {
        PrimeFaces.current().dialog().closeDynamic(inv);
    }

    public LazyModel<Inventario> getLazyEgreso() {
        return lazyEgreso;
    }

    public void setLazyEgreso(LazyModel<Inventario> lazyEgreso) {
        this.lazyEgreso = lazyEgreso;
    }

    public String getTitulo1() {
        return titulo1;
    }

    public void setTitulo1(String titulo1) {
        this.titulo1 = titulo1;
    }

    public String getTitulo2() {
        return titulo2;
    }

    public void setTitulo2(String titulo2) {
        this.titulo2 = titulo2;
    }

    public String getParametro() {
        return parametro;
    }

    public void setParametro(String parametro) {
        this.parametro = parametro;
    }

    public Boolean getBan() {
        return ban;
    }

    public void setBan(Boolean ban) {
        this.ban = ban;
    }

}
