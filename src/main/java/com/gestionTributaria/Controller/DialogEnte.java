/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Controller;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.util.JsfUtil;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 *
 * @author Administrator
 */
@Named
@ViewScoped
public class DialogEnte implements Serializable {

    private Boolean esPersona = true;
    private LazyModel<Cliente> entes;
    private Cliente ente;
    private String cedula;
    private String msg;

    @PostConstruct
    public void initView() {
        entes = new LazyModel<>(Cliente.class);

    }

    public Boolean buscarEnteDatoSeguro(Integer intentos) {
        try {
            //data = datoSeguroSeguro.getDatos(cedula, false, 0);
//            if(data==null){
//                msg = "No se ha encontrado al ente";
//                return false;
//            }
            // ente = datoSeguroSeguro.getEnteFromDatoSeguro(data);
            //JsfUti.messageInfo(null, "Info", "Se ha encontrado al ente "+ente.getNombres());
            // msg = "Se ha encontrado al cliente "+ente.getNombres() +" "+ente.getApellidos();
            JsfUtil.update("frmEnte");
            JsfUtil.executeJS("PF('dlgMsg').show()");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void cambioTipoPersona() {
        if (esPersona) {
            entes = new LazyModel<>(Cliente.class);
            entes.getFilterss().put("tipoPro", new CatalogoItem(10L));
        } else {
            entes = new LazyModel<>(Cliente.class);
            entes.getFilterss().put("tipoPro.codigo", new CatalogoItem(11L));
        }

    }

    public void agregar() {
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "/rentas/contribuyente");
    }

    public void returnEnte() {
        if (ente != null) {
            seleccionEnte(ente);
        }
    }

    public void seleccionEnte(Cliente ente) {
        PrimeFaces.current().dialog().closeDynamic(ente);
    }

    public LazyModel<Cliente> getEntes() {
        return entes;
    }

    public void setEntes(LazyModel<Cliente> entes) {
        this.entes = entes;
    }

    public Boolean getEsPersona() {
        return esPersona;
    }

    public void setEsPersona(Boolean esPersona) {
        this.esPersona = esPersona;
    }

    public DialogEnte() {
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public Cliente getEnte() {
        return ente;
    }

    public void setEnte(Cliente ente) {
        this.ente = ente;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
