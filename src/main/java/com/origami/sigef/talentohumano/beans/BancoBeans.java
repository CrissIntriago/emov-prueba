/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.talentohumano.beans;

import com.origami.sigef.catalogoItem.service.CatalogoItemService;
import com.origami.sigef.common.entities.Banco;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.talentohumano.services.BancoService;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 *
 * @author Origami
 */
@Named(value = "bancoBeans")
@ViewScoped
public class BancoBeans implements Serializable {

    private Banco banco;
    private LazyModel<Banco> bancoLazy;
    @Inject
    private BancoService bancoService;
    @Inject
    private UserSession userSession;
    @Inject
    private CatalogoItemService catalogoService;
    private List<Banco> bancos;
    private List<CatalogoItem> tiposCuentas;

    @PostConstruct
    public void inicializate() {
        this.banco = new Banco();
        bancoLazy = new LazyModel<>(Banco.class);
        bancoLazy.getFilterss().put("estado", true);
        tiposCuentas = catalogoService.getTipoCuenta("tipo_cuenta_bancaria", "TIPO_CTA");
        bancos = bancoService.findAll();
    }

    public void nuevo(Banco b) {
        if (b != null) {
            this.banco = b;
        } else {
            banco = new Banco();
        }
        banco.setFechaModificacion(new Date());
        banco.setUsuarioModifica(userSession.getNameUser());
        banco.setEstado(true);
        PrimeFaces.current().executeScript("PF('bancoDialog').show()");
        PrimeFaces.current().ajax().update("frmBanco");
    }

    public void guardarBanco() {
        boolean edit = banco.getId() != null;
        if (banco.getNombreBanco() == null) {
            System.out.println("entro");
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addWarningMessage("Información", " Debe Ingresar un el nombre de la entidad bancaria");
        }
        if (edit) {
            banco.setUsuarioModifica(userSession.getNameUser());
            banco.setFechaModificacion(new Date());
            bancoService.edit(banco);
        } else {
            if (codigoRepetido(banco.getCuentaCorriente())) {
                JsfUtil.addWarningMessage("Banco", "Codigo de Banco se encuentra repetido");
                return;
            }
            banco.setUsuarioCreacion(userSession.getNameUser());
            banco.setFechaCreacion(new Date());
            banco = bancoService.create(banco);

        }
        PrimeFaces.current().executeScript("PF('bancoDialog').hide()");
        PrimeFaces.current().ajax().update("messages");
        PrimeFaces.current().ajax().update(":formMain");
        JsfUtil.addSuccessMessage("Información", banco.getNombreBanco() + (edit ? " editada" : " registrada") + " con éxito.");

    }

    public boolean codigoRepetido(String codigo) {
        return bancos.stream().anyMatch((cta) -> (cta.getCuentaCorriente().equals(codigo)));
    }

    public void eliminar(Banco b) {
        if (bancoService.getBancoUsadoBeneficiario(b) || bancoService.getBancoUsadoCuentaBancaria(b)) {
            JsfUtil.addErrorMessage("ERROR!!!", "No se puede eliminar el banco debido a que esta relacionado");
            return;
        }
        b.setEstado(Boolean.FALSE);
        bancoService.edit(b);
        PrimeFaces.current().ajax().update(":formMain");
        PrimeFaces.current().ajax().update("messages");
        JsfUtil.addSuccessMessage("Info", "Entidad Bancaria Eliminada Correctamente");

    }

    public boolean getReadonly() {
        boolean edit = banco.getId() != null;
        if (edit) {
            return true;
        }
        return false;
    }

    public Banco getBanco() {
        return banco;
    }

    public void setBanco(Banco banco) {
        this.banco = banco;
    }

    public LazyModel<Banco> getBancoLazy() {
        return bancoLazy;
    }

    public void setBancoLazy(LazyModel<Banco> bancoLazy) {
        this.bancoLazy = bancoLazy;
    }

    public List<CatalogoItem> getTiposCuentas() {
        return tiposCuentas;
    }

    public void setTiposCuentas(List<CatalogoItem> tiposCuentas) {
        this.tiposCuentas = tiposCuentas;
    }

}
