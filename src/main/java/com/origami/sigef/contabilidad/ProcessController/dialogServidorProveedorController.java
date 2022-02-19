/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.ProcessController;

import com.origami.sigef.common.entities.Proveedor;
import com.origami.sigef.resource.talento_humano.entities.Servidor;
import com.origami.sigef.common.lazy.LazyModel;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 *
 * @author Criss Intriago
 */
@Named(value = "dialogServidorProveedorView")
@ViewScoped
public class dialogServidorProveedorController implements Serializable {

    Boolean tipoBeneficiario;
    Boolean panelProveedor;
    Boolean panelServidor;

    private LazyModel<Proveedor> proveedorLazy;
    private LazyModel<Servidor> servidorLazy;

    
    @PostConstruct
    public void initView() {
        this.tipoBeneficiario = null;
    }

    public void CargarDatosBeneficiario() {
        if (tipoBeneficiario != null) {
            if (tipoBeneficiario) {
                panelProveedor = true;
                panelServidor = false;
                this.proveedorLazy = new LazyModel<>(Proveedor.class);
                this.proveedorLazy.getSorteds().put("cliente.nombre", "ASC");
                this.proveedorLazy.getFilterss().put("estado", true);
                this.proveedorLazy.setDistinct(false);
            } else {
                panelProveedor = false;
                panelServidor = true;
                this.servidorLazy = new LazyModel<>(Servidor.class);
                this.servidorLazy.getSorteds().put("persona.apellido", "ASC");
                this.servidorLazy.getFilterss().put("estado", true);
                this.servidorLazy.setDistinct(false);
            }
        } else {
            panelProveedor = false;
            panelServidor = false;
            proveedorLazy = null;
            servidorLazy = null;
        }
        PrimeFaces.current().ajax().update("formDlgBeneficiario");
    }

    public void CloseProveedor(Proveedor proveedor) {
        PrimeFaces.current().dialog().closeDynamic(proveedor);
    }

    public void CloseServidor(Servidor servidor) {
        PrimeFaces.current().dialog().closeDynamic(servidor);
    }

    public Boolean getTipoBeneficiario() {
        return tipoBeneficiario;
    }

    public void setTipoBeneficiario(Boolean tipoBeneficiario) {
        this.tipoBeneficiario = tipoBeneficiario;
    }

    public Boolean getPanelProveedor() {
        return panelProveedor;
    }

    public void setPanelProveedor(Boolean panelProveedor) {
        this.panelProveedor = panelProveedor;
    }

    public Boolean getPanelServidor() {
        return panelServidor;
    }

    public void setPanelServidor(Boolean panelServidor) {
        this.panelServidor = panelServidor;
    }

    public LazyModel<Proveedor> getProveedorLazy() {
        return proveedorLazy;
    }

    public void setProveedorLazy(LazyModel<Proveedor> proveedorLazy) {
        this.proveedorLazy = proveedorLazy;
    }

    public LazyModel<Servidor> getServidorLazy() {
        return servidorLazy;
    }

    public void setServidorLazy(LazyModel<Servidor> servidorLazy) {
        this.servidorLazy = servidorLazy;
    }

}
