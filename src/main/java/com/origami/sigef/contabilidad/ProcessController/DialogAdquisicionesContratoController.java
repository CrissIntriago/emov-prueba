/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.ProcessController;

import com.origami.sigef.activos.service.AdquisicionesService;
import com.origami.sigef.common.entities.Adquisiciones;
import com.origami.sigef.common.entities.ContratosReservaEjecuion;
import com.origami.sigef.common.entities.SolicitudReservaCompromiso;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.resource.procesos.entities.TipoTramite;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 *
 * @author Sandra Arroba
 */
@Named(value = "dlgAdquisicionView")
@ViewScoped
public class DialogAdquisicionesContratoController implements Serializable {
    
    private LazyModel<ContratosReservaEjecuion> contratosLazy;

//    private LazyModel<Adquisiciones> contratosLazy;
    Boolean bandera = Boolean.TRUE;
    private TipoTramite tipoTramiteTemp;
    private List<String> tipoAdquisicionList;

    @Inject
    private UserSession session;
    @Inject
    private AdquisicionesService adquisicionesService;

    @PostConstruct
    public void initView() {
        
        contratosLazy = new LazyModel<>(ContratosReservaEjecuion.class);
        contratosLazy.getSorteds().put("reserva.secuencial", "ASC");
        contratosLazy.getFilterss().put("reserva.estado.codigo", "APRO");
        contratosLazy.setDistinct(false);
        tipoAdquisicionList = new ArrayList<>();
        tipoTramiteTemp = new TipoTramite();
        tipoAdquisicionList.add("tipo_proceso_catalogo");
        tipoAdquisicionList.add("tipo_proceso_infima");
        if (this.session.getVarTemp() instanceof TipoTramite) {
            tipoTramiteTemp = (TipoTramite) this.session.getVarTemp();
        } else {
            tipoTramiteTemp = null;
        }
        if (tipoTramiteTemp != null) {
            if (tipoTramiteTemp.getAbreviatura() != null) {
                switch (tipoTramiteTemp.getAbreviatura()) {
                    case "PAG_ANTI_BIENES":
                        contratosLazy.getFilterss().put("contrato.tipoAdquisicion.codigo:equal", "tipo_adquisicion_bienes");
                        break;
                    case "PAG_ANTI_SERVICIOS":
                        contratosLazy.getFilterss().put("contrato.tipoAdquisicion.codigo:equal", "tipo_adquisicion_servicios");
                        break;
                    case "PAG_ANTI_OBRAPUBLICA":
                        contratosLazy.getFilterss().put("contrato.tipoAdquisicion.codigo:equal", "tipo_adquisicion_obras");
                        break;
                    case "PAG_ANTI_CONSULTORIA":
                        contratosLazy.getFilterss().put("contrato.tipoAdquisicion.codigo:equal", "tipo_adquisicion_consultoria");
                        break;
                    case "proceso_pca_bienes":
                        contratosLazy.getFilterss().put("reserva.contabilizado", false);
                        if (session.getUsuario().getFuncionario() != null) {
                            contratosLazy.getFilterss().put("contrato.responsableAdquisicionList.responsable.id", session.getUsuario().getFuncionario().getId());
                        }
                        contratosLazy.getFilterss().put("contrato.tipoAdquisicion.codigo:equal", "tipo_adquisicion_bienes");
                        contratosLazy.getFilterss().put("contrato.tipoProceso.codigo:notEqual", tipoAdquisicionList);
                        contratosLazy.getFilterss().put("contrato.responsableAdquisicionList.estado", true);

                        break;
                    case "proceso_pc_servicios":
                        contratosLazy.getFilterss().put("reserva.contabilizado", false);
                        if (session.getUsuario().getFuncionario() != null) {
                            contratosLazy.getFilterss().put("contrato.responsableAdquisicionList.responsable.id", session.getUsuario().getFuncionario().getId());
                        }
                        contratosLazy.getFilterss().put("contrato.tipoAdquisicion.codigo:equal", "tipo_adquisicion_servicios");
                        contratosLazy.getFilterss().put("contrato.tipoProceso.codigo:notEqual", tipoAdquisicionList);
                        contratosLazy.getFilterss().put("contrato.responsableAdquisicionList.estado", true);

                        break;
                    case "proceso_pco_publica":
                        contratosLazy.getFilterss().put("reserva.contabilizado", false);
                        if (session.getUsuario().getFuncionario() != null) {
                            contratosLazy.getFilterss().put("contrato.responsableAdquisicionList.responsable.id", session.getUsuario().getFuncionario().getId());
                        }
                        contratosLazy.getFilterss().put("contrato.tipoAdquisicion.codigo:equal", "tipo_adquisicion_obras");
                        contratosLazy.getFilterss().put("contrato.responsableAdquisicionList.estado", true);

                        break;
                    case "proceso_pc_consultoria":
                        contratosLazy.getFilterss().put("reserva.contabilizado", false);
                        if (session.getUsuario().getFuncionario() != null) {
                            contratosLazy.getFilterss().put("contrato.responsableAdquisicionList.responsable.id", session.getUsuario().getFuncionario().getId());
                        }
                        contratosLazy.getFilterss().put("contrato.tipoAdquisicion.codigo", "tipo_adquisicion_consultoria");
                        contratosLazy.getFilterss().put("contrato.responsableAdquisicionList.estado", true);

                        break;
                    case "proceso_pi_cuantia_bienes":
                        contratosLazy.getFilterss().put("reserva.contabilizado", false);
                        contratosLazy.getFilterss().put("contrato.tipoAdquisicion.codigo:equal", "tipo_adquisicion_bienes");
                        contratosLazy.getFilterss().put("contrato.tipoProceso.codigo:equal", "tipo_proceso_infima");
                        break;
                    case "proceso_pi_cuantia_servicios":
                        contratosLazy.getFilterss().put("reserva.contabilizado", false);
                        contratosLazy.getFilterss().put("contrato.tipoAdquisicion.codigo:equal", "tipo_adquisicion_servicios");
                        contratosLazy.getFilterss().put("contrato.tipoProceso.codigo:equal", "tipo_proceso_infima");
                        break;
                    case "PAG_INF_CUANT_OB_MENOR":
                        contratosLazy.getFilterss().put("reserva.contabilizado", false);
                        contratosLazy.getFilterss().put("contrato.tipoAdquisicion.codigo:equal", "tipo_adquisicion_obras");
                        contratosLazy.getFilterss().put("contrato.tipoProceso.codigo:equal", "tipo_proceso_infima");
                        break;
                    case "proceso_pago_ceb":
                        contratosLazy.getFilterss().put("reserva.contabilizado", false);
                        contratosLazy.getFilterss().put("contrato.tipoAdquisicion.codigo:equal", "tipo_adquisicion_bienes");
                        contratosLazy.getFilterss().put("contrato.tipoProceso.codigo:equal", "tipo_proceso_catalogo");
                        break;
                    case "proceso_pce_servicios":
                        contratosLazy.getFilterss().put("reserva.contabilizado", false);
                        contratosLazy.getFilterss().put("contrato.tipoAdquisicion.codigo:equal", "tipo_adquisicion_servicios");
                        contratosLazy.getFilterss().put("contrato.tipoProceso.codigo:equal", "tipo_proceso_catalogo");
                        break;
                    case "PPS_profesionales":
                        contratosLazy.getFilterss().put("reserva.contabilizado", false);
                        contratosLazy.getFilterss().put("contrato.tipoAdquisicion.codigo:equal", "tipo_adquisicion_otros");
                        break;

                }
            }
        }

//        String tipo = JsfUtil.getRequestParameter("TIPO");
    }

    public void close(Adquisiciones item) {
        List<SolicitudReservaCompromiso> reserva = adquisicionesService.verificarReservaByContrato(true, item);
        if (!reserva.isEmpty()) {
            PrimeFaces.current().dialog().closeDynamic(item);
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "AVISO!!", "No puede seleccionar el contrato, "
                    + "verifique que tenga Reserva de Compromiso."));
        }

    }

    public Boolean getBandera() {
        return bandera;
    }

    public void setBandera(Boolean bandera) {
        this.bandera = bandera;
    }

    public TipoTramite getTipoTramiteTemp() {
        return tipoTramiteTemp;
    }

    public void setTipoTramiteTemp(TipoTramite tipoTramiteTemp) {
        this.tipoTramiteTemp = tipoTramiteTemp;
    }

    public LazyModel<ContratosReservaEjecuion> getContratosLazy() {
        return contratosLazy;
    }

    public void setContratosLazy(LazyModel<ContratosReservaEjecuion> contratosLazy) {
        this.contratosLazy = contratosLazy;
    }

}
