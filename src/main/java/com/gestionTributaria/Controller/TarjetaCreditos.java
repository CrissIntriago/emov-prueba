/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Controller;

import com.asgard.Entity.FinaRenEntidadBancaria;
import com.asgard.Entity.FinaRenTipoEntidadBancaria;
import com.gestionTributaria.Commons.MessagesRentas;
import com.gestionTributaria.Lazys.RenEntidadBancariaLazy;
import com.gestionTributaria.Services.ManagerService;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.util.JsfUtil;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author DEVELOPER
 */
@Named
@ViewScoped
public class TarjetaCreditos implements Serializable {

    private static final Long serialVersionUID = 1L;

    private LazyModel<FinaRenEntidadBancaria> lazyBancos;

    private List<FinaRenEntidadBancaria> bancosList;
    private FinaRenEntidadBancaria banco;
    private Boolean nuevo = false;
    private String headerDLG;
    private final Long tipo = 2L;
    @Inject
    private ManagerService service;

    @PostConstruct
    public void initView() {
        lazyBancos = new LazyModel(FinaRenEntidadBancaria.class);
        //  lazyBancos.getFilterss().put("tipo.id", tipo);
        lazyBancos.getSorteds().put("descripcion", "ASC");
    }

    public void editar(FinaRenEntidadBancaria ban) {

        banco = new FinaRenEntidadBancaria();
        banco = ban;
        nuevo = false;
        headerDLG = "Editar Tarjeta Crédito";
        JsfUtil.update("frmDlg");
        JsfUtil.executeJS("PF('dlgTarjCred').show()");
    }

    public void nuevo() {

        banco = new FinaRenEntidadBancaria();
        banco.setEstado(true);
        nuevo = true;
        headerDLG = "Ingreso de Trajeta de Crédito";
        JsfUtil.update("frmDlg");
        JsfUtil.executeJS("PF('dlgTarjCred').show()");
    }

    public void guardar() {
        if (banco.getDescripcion() == null) {
            JsfUtil.addErrorMessage(MessagesRentas.advert, MessagesRentas.faltaNombreInst);
            return;
        }

        Long existeBanco = service.existeRenEntidadBancaria(banco.getDescripcion());
        if (existeBanco != null) {
            if (!nuevo) {
                if (banco.getId().compareTo(existeBanco) == 0) {

                } else {
                    JsfUtil.addErrorMessage(MessagesRentas.advert, MessagesRentas.existeInst);
                    return;
                }
            } else {
                JsfUtil.addErrorMessage(MessagesRentas.advert, MessagesRentas.existeInst);
                return;
            }
            banco.setFechaIngreso(new Date());
        }
        //banco.setTipo(new FinaRenTipoEntidadBancaria(tipo));
        banco = service.guardarBanco(banco);

        if (banco != null) {
            if (nuevo) {
                JsfUtil.addInformationMessage(MessagesRentas.info, MessagesRentas.institucionGuarda);
            } else {
                JsfUtil.addInformationMessage(MessagesRentas.info, MessagesRentas.institucionModificada);
            }
            JsfUtil.executeJS("PF('dlgTarjCred').hide()");
        } else {
            JsfUtil.addWarningMessage(MessagesRentas.advert, MessagesRentas.institucionGuarda);
        }
        lazyBancos = new LazyModel(FinaRenEntidadBancaria.class);
        //  lazyBancos.getFilterss().put("tipo.id", tipo);
        lazyBancos.getSorteds().put("descripcion", "ASC");
    }

    public List<FinaRenEntidadBancaria> getBancosList() {
        return bancosList;
    }

    public void setBancosList(List<FinaRenEntidadBancaria> bancosList) {
        this.bancosList = bancosList;
    }

    public FinaRenEntidadBancaria getBanco() {
        return banco;
    }

    public void setBanco(FinaRenEntidadBancaria banco) {
        this.banco = banco;
    }

    public Boolean getNuevo() {
        return nuevo;
    }

    public void setNuevo(Boolean nuevo) {
        this.nuevo = nuevo;
    }

    public String getHeaderDLG() {
        return headerDLG;
    }

    public void setHeaderDLG(String headerDLG) {
        this.headerDLG = headerDLG;
    }

    public LazyModel<FinaRenEntidadBancaria> getLazyBancos() {
        return lazyBancos;
    }

    public void setLazyBancos(LazyModel<FinaRenEntidadBancaria> lazyBancos) {
        this.lazyBancos = lazyBancos;
    }

}
