/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.commons.controllers;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.entities.Proveedor;
import com.origami.sigef.resource.talento_humano.entities.Servidor;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.util.JsfUtil;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 *
 * @author Criss Intriago
 */
@Named(value = "dlgBeneficiariosView")
@ViewScoped
public class dlgBeneficiariosController implements Serializable {

    private LazyModel<Servidor> servidorLazy;
    private LazyModel<Proveedor> proveedorLazy;

    private Boolean tipoListado;
    private Boolean renderSeleccion;
    private String code;

    @PostConstruct
    public void initView() {
        tipoListado = Boolean.parseBoolean(JsfUtil.getRequestParameter(CONFIG.PARAMETER_TIPO));
        renderSeleccion = Boolean.parseBoolean(JsfUtil.getRequestParameter(CONFIG.PARAMETER_RENDER));
        code = JsfUtil.getRequestParameter(CONFIG.ONE_TYPE);
        actualizarDlg();
    }

    public void actualizarDlg() {
        if (tipoListado) {
            servidorLazy = new LazyModel<>(Servidor.class);
            servidorLazy.getSorteds().put("persona.apellido", "ASC");
            servidorLazy.getFilterss().put("estado", true);
            servidorLazy.getFilterss().put("persona.estado", true);
            //servidorLazy.getFilterss().put("persona.validNodos", true);
            //servidorLazy.getFilterss().put("persona.validado", true);
            servidorLazy.setDistinct(false);
        } else {
            proveedorLazy = new LazyModel<>(Proveedor.class);
            proveedorLazy.getSorteds().put("cliente.nombre", "ASC");
            proveedorLazy.getFilterss().put("estado", true);
            proveedorLazy.setDistinct(false);
        }
    }

    public void closeDlg(Cliente cliente) {
        cliente.setTipoBeneficiario(tipoListado);
        PrimeFaces.current().dialog().closeDynamic(cliente);
    }

    public void closeDlgProveedor(Proveedor proveedor) {
        PrimeFaces.current().dialog().closeDynamic(proveedor);
    }

    public void closeDlgServidor(Servidor servidor) {
        PrimeFaces.current().dialog().closeDynamic(servidor);
    }

    public LazyModel<Servidor> getServidorLazy() {
        return servidorLazy;
    }

    public void setServidorLazy(LazyModel<Servidor> servidorLazy) {
        this.servidorLazy = servidorLazy;
    }

    public LazyModel<Proveedor> getProveedorLazy() {
        return proveedorLazy;
    }

    public void setProveedorLazy(LazyModel<Proveedor> proveedorLazy) {
        this.proveedorLazy = proveedorLazy;
    }

    public Boolean getTipoListado() {
        return tipoListado;
    }

    public void setTipoListado(Boolean tipoListado) {
        this.tipoListado = tipoListado;
    }

    public Boolean getRenderSeleccion() {
        return renderSeleccion;
    }

    public void setRenderSeleccion(Boolean renderSeleccion) {
        this.renderSeleccion = renderSeleccion;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
