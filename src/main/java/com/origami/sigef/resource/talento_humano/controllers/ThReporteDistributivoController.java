/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.talento_humano.controllers;

import com.origami.sigef.catalogoItem.service.CatalogoItemService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.UnidadAdministrativa;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.contabilidad.service.UnidadAdministrativaService;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 *
 * @author Criss Intriago
 */
@Named(value = "thReporteDistributivoView")
@ViewScoped
public class ThReporteDistributivoController implements Serializable {

    @Inject
    private CatalogoItemService catalogoItemService;
    @Inject
    private ServletSession servletSession;
    @Inject
    private UnidadAdministrativaService unidadAdministrativaService;

    private List<Short> listaPeriodo;
    private List<UnidadAdministrativa> unidadesAdminitrativa;

    private OpcionBusqueda opcionBusqueda, opcionBusqueda2, opcionBusqueda3, opcionBusqueda4, opcionBusqueda5;
    private UnidadAdministrativa unidadAdministrativa, unidadAdministrativa2, unidadAdministrativa3;

    @PostConstruct
    public void init() {
        listaPeriodo = catalogoItemService.getPeriodo();
        unidadesAdminitrativa = unidadAdministrativaService.getlListaUnidades();
        cleanform();
    }

    public void cleanform() {
        opcionBusqueda = new OpcionBusqueda();
        opcionBusqueda2 = new OpcionBusqueda();
        opcionBusqueda3 = new OpcionBusqueda();
        opcionBusqueda4 = new OpcionBusqueda();
        opcionBusqueda5 = new OpcionBusqueda();
        unidadAdministrativa = null;
        unidadAdministrativa2 = null;
        unidadAdministrativa3 = null;
    }

    public void printReport(String tipoDocumento, int codigo) {
        switch (codigo) {
            case 1:
                if (opcionBusqueda.getAnio() == null) {
                    JsfUtil.addWarningMessage("AVISO!!!", "Debe seleccionar un periodo");
                    return;
                }
                servletSession.addParametro("periodo", opcionBusqueda.getAnio());
                if (unidadAdministrativa != null) {
                    servletSession.addParametro("unidad_administrativa", unidadAdministrativa.getId().toString());
                } else {
                    servletSession.addParametro("unidad_administrativa", "");
                }
                servletSession.setNombreReporte("distributivo_general");
                break;
            case 2:
                if (opcionBusqueda2.getAnio() == null) {
                    JsfUtil.addWarningMessage("AVISO!!!", "Debe seleccionar un periodo");
                    return;
                }
                servletSession.addParametro("periodo", opcionBusqueda2.getAnio());
                if (unidadAdministrativa2 != null) {
                    servletSession.addParametro("unidad_administrativa", unidadAdministrativa2.getId().toString());
                } else {
                    servletSession.addParametro("unidad_administrativa", "");
                }
                servletSession.setNombreReporte("distributivo_general_partidas");
                break;
            case 3:
                if (opcionBusqueda3.getAnio() == null) {
                    JsfUtil.addWarningMessage("AVISO!!!", "Debe seleccionar un periodo");
                    return;
                }
                servletSession.addParametro("periodo", opcionBusqueda3.getAnio());
                if (unidadAdministrativa3 != null) {
                    servletSession.addParametro("unidad_administrativa", unidadAdministrativa3.getId().toString());
                } else {
                    servletSession.addParametro("unidad_administrativa", "");
                }
                servletSession.setNombreReporte("distributivo_general_cuenta_contable");
                break;
            case 4:
                if (opcionBusqueda4.getAnio() == null) {
                    JsfUtil.addWarningMessage("AVISO!!!", "Debe seleccionar un periodo");
                    return;
                }
                servletSession.addParametro("periodo", opcionBusqueda4.getAnio());
                servletSession.setNombreReporte("distributivo_anexo");
                break;
            case 5:
                if (opcionBusqueda5.getAnio() == null) {
                    JsfUtil.addWarningMessage("AVISO!!!", "Debe seleccionar un periodo");
                    return;
                }
                servletSession.addParametro("periodo", opcionBusqueda5.getAnio());
                servletSession.setNombreReporte("distributivo_valores");
                break;
        }
        servletSession.setContentType(tipoDocumento);
        servletSession.setNombreSubCarpeta("_talento_humano");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
        cleanform();
        PrimeFaces.current().ajax().update("formMain");
    }

    public List<Short> getListaPeriodo() {
        return listaPeriodo;
    }

    public void setListaPeriodo(List<Short> listaPeriodo) {
        this.listaPeriodo = listaPeriodo;
    }

    public OpcionBusqueda getOpcionBusqueda() {
        return opcionBusqueda;
    }

    public void setOpcionBusqueda(OpcionBusqueda opcionBusqueda) {
        this.opcionBusqueda = opcionBusqueda;
    }

    public CatalogoItemService getCatalogoItemService() {
        return catalogoItemService;
    }

    public void setCatalogoItemService(CatalogoItemService catalogoItemService) {
        this.catalogoItemService = catalogoItemService;
    }

    public ServletSession getServletSession() {
        return servletSession;
    }

    public void setServletSession(ServletSession servletSession) {
        this.servletSession = servletSession;
    }

    public UnidadAdministrativaService getUnidadAdministrativaService() {
        return unidadAdministrativaService;
    }

    public void setUnidadAdministrativaService(UnidadAdministrativaService unidadAdministrativaService) {
        this.unidadAdministrativaService = unidadAdministrativaService;
    }

    public OpcionBusqueda getOpcionBusqueda2() {
        return opcionBusqueda2;
    }

    public void setOpcionBusqueda2(OpcionBusqueda opcionBusqueda2) {
        this.opcionBusqueda2 = opcionBusqueda2;
    }

    public OpcionBusqueda getOpcionBusqueda3() {
        return opcionBusqueda3;
    }

    public void setOpcionBusqueda3(OpcionBusqueda opcionBusqueda3) {
        this.opcionBusqueda3 = opcionBusqueda3;
    }

    public OpcionBusqueda getOpcionBusqueda4() {
        return opcionBusqueda4;
    }

    public void setOpcionBusqueda4(OpcionBusqueda opcionBusqueda4) {
        this.opcionBusqueda4 = opcionBusqueda4;
    }

    public OpcionBusqueda getOpcionBusqueda5() {
        return opcionBusqueda5;
    }

    public void setOpcionBusqueda5(OpcionBusqueda opcionBusqueda5) {
        this.opcionBusqueda5 = opcionBusqueda5;
    }

    public List<UnidadAdministrativa> getUnidadesAdminitrativa() {
        return unidadesAdminitrativa;
    }

    public void setUnidadesAdminitrativa(List<UnidadAdministrativa> unidadesAdminitrativa) {
        this.unidadesAdminitrativa = unidadesAdminitrativa;
    }

    public UnidadAdministrativa getUnidadAdministrativa() {
        return unidadAdministrativa;
    }

    public void setUnidadAdministrativa(UnidadAdministrativa unidadAdministrativa) {
        this.unidadAdministrativa = unidadAdministrativa;
    }

    public UnidadAdministrativa getUnidadAdministrativa2() {
        return unidadAdministrativa2;
    }

    public void setUnidadAdministrativa2(UnidadAdministrativa unidadAdministrativa2) {
        this.unidadAdministrativa2 = unidadAdministrativa2;
    }

    public UnidadAdministrativa getUnidadAdministrativa3() {
        return unidadAdministrativa3;
    }

    public void setUnidadAdministrativa3(UnidadAdministrativa unidadAdministrativa3) {
        this.unidadAdministrativa3 = unidadAdministrativa3;
    }

}
