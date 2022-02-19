/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.origami.sigef.tesoreria.comprobantelectronico.beans;

import com.gestionTributaria.Entities.NotasCredito;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.util.JsfUtil;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Administrator
 */
@Named(value = "nCreditoMovView")
@ViewScoped
public class NotaCreditoMovimientoBeans implements Serializable {

    private static final Logger LOG = Logger.getLogger(NotaCreditoMovimientoBeans.class.getName());

    @Inject
    private ServletSession ss;

    private LazyModel<NotasCredito> lazy;

    @PostConstruct
    public void init() {
        lazy = new LazyModel<>(NotasCredito.class);
        lazy.addFilter("estado", Short.parseShort("1"));
        lazy.addFilter("saldo:gt", BigDecimal.ZERO);
        lazy.getSorteds().put("id", "desc");
    }

    public void generarNotaCredito(NotasCredito nt) {
        ss.setNombreSubCarpeta("GestionTributatia/comprobantes");
        ss.setNombreReporte("sNotadeCredito");
        ss.addParametro("IDNOTA", nt.getId());
        ss.addParametro("LOGO_R", JsfUtil.getRealPath("resources/images/duranLogo.png"));
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
    }

    public LazyModel<NotasCredito> getLazy() {
        return lazy;
    }

    public void setLazy(LazyModel<NotasCredito> lazy) {
        this.lazy = lazy;
    }

}
