/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.talento_humano.controllers;

import com.google.gson.Gson;
import com.origami.sigef.catalogoItem.service.CatalogoItemService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.resource.talento_humano.entities.Servidor;
import com.origami.sigef.resource.talento_humano.entities.ThAccionPersonal;
import com.origami.sigef.resource.talento_humano.models.ActasModel;
import com.origami.sigef.resource.talento_humano.models.InfoReemplazoModel;
import com.origami.sigef.resource.talento_humano.models.SituacionModel;
import com.origami.sigef.resource.talento_humano.services.ThAccionPersonalService;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author Criss Intriago
 */
@Named(value = "thAccionPersonalView")
@ViewScoped
public class ThAccionPersonalController implements Serializable {

    @Inject
    private ServletSession servlet;
    @Inject
    private UserSession userSession;
    @Inject
    private ThAccionPersonalService thAccionPersonalService;
    @Inject
    private CatalogoItemService catalogoItemService;

    private LazyModel<ThAccionPersonal> thAccionPersonalLazyModel;

    private ThAccionPersonal thAccionPersonal;
    private ThAccionPersonal reporte;
    private SituacionModel situacionActual;
    private SituacionModel situacionPropuesta;
    private ActasModel actaFinal;
    private ActasModel actaRecursoHumano;
    private ActasModel caucion;
    private ActasModel reemplazo;
    private InfoReemplazoModel infoReemplazoModel;

    private String identificacionServidor;

    private List<CatalogoItem> motivoList;

    @PostConstruct
    public void init() {
        thAccionPersonalLazyModel = new LazyModel<>(ThAccionPersonal.class);
        thAccionPersonalLazyModel.getFilterss().put("estado", true);
        reiniciarJSON();
    }

    private void reiniciarJSON() {
        this.situacionActual = new SituacionModel();
        this.situacionPropuesta = new SituacionModel();
        this.actaFinal = new ActasModel();
        this.actaRecursoHumano = new ActasModel();
        this.caucion = new ActasModel();
        this.reemplazo = new ActasModel();
        this.infoReemplazoModel = new InfoReemplazoModel();
        this.thAccionPersonal = new ThAccionPersonal();
        this.thAccionPersonal.setResponsableAdministrativo(new Servidor());
        this.thAccionPersonal.setRecursosHumano(new Servidor());
        this.reporte = new ThAccionPersonal();
        motivoList = catalogoItemService.findByNamedQuery("CatalogoItem.findCatalogoClasificacion1", "motivo_accion_personal");
    }

    public void form(ThAccionPersonal ap) {
        if (ap != null) {
            this.thAccionPersonal = ap;
            JsonObject(thAccionPersonal);
            if (thAccionPersonal.getCodigoList() != null) {
                thAccionPersonal.setCodMotivoApList(thAccionPersonal.getCodigoList().split(":"));
            }
        } else {
            thAccionPersonal = new ThAccionPersonal();
            Long orden = thAccionPersonalService.getOrderaccionPersonal();
            thAccionPersonal.setOrden(orden);
            String codigogenerado = Utils.completarCadenaConCeros(thAccionPersonal.getOrden() + "", 3);
            thAccionPersonal.setCodigo(CONFIG.SIGLAS_INSTITUCION + "-TH-" + codigogenerado + "-" + Utils.getAnio(new Date()).shortValue());
        }
        thAccionPersonal.setFechaModificacion(new Date());
        thAccionPersonal.setUsuarioModifica(userSession.getNameUser());
        thAccionPersonal.setEstado(true);
        PrimeFaces.current().executeScript("PF('accionDialog').show()");
        PrimeFaces.current().ajax().update("frmAccion");

    }

    public void openDlgSer() {
        Map<String, List<String>> params = new HashMap<>();
        params.put(CONFIG.PARAMETER_TIPO, Arrays.asList("true"));
        params.put(CONFIG.PARAMETER_RENDER, Arrays.asList("false"));
        params.put(CONFIG.ONE_TYPE, Arrays.asList("2"));
        Utils.openDialog("/facelet/view/commons/dlgBeneficiarios", "45%", "70%", params);
    }

    public void selectDataSer(SelectEvent evt) {
        thAccionPersonal.setRecursosHumano((Servidor) evt.getObject());
    }

    public void selectDataSer1(SelectEvent evt) {
        thAccionPersonal.setResponsableAdministrativo((Servidor) evt.getObject());
    }

    public void selectDataSer2(SelectEvent evt) {
        thAccionPersonal.setServidor((Servidor) evt.getObject());
        if (thAccionPersonal.getServidor() != null) {
            identificacionServidor = thAccionPersonal.getServidor().getPersona().getIdentificacion();
        }
    }

    public void save() {
        Boolean edit = thAccionPersonal.getId() != null;
        if (thAccionPersonal.getServidor() == null) {
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addWarningMessage("Información", "Debe Ingresar un Funcionario");
            return;
        }
        if (thAccionPersonal.getResponsableAdministrativo() == null) {
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addWarningMessage("Información", "Debe Ingresar un Responsable Administrativo");
            return;
        }
        if (thAccionPersonal.getRecursosHumano() == null) {
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addWarningMessage("Información", "Debe Ingresar un Responsable de Recursos Humanos");
            return;
        }
        //Reemplazo
        Gson gson = new Gson();
        thAccionPersonal.setReemplazo(gson.toJson(reemplazo));
        //Datos funcionario
        Gson gson_1 = new Gson();
        thAccionPersonal.setInfoReemplazo(gson_1.toJson(infoReemplazoModel));
        //acta final
        Gson gson_2 = new Gson();
        thAccionPersonal.setActaFinal(gson_2.toJson(actaFinal));
        //acta recursos humanos
        Gson gson_3 = new Gson();
        thAccionPersonal.setActaRecursoHumano(gson_3.toJson(actaRecursoHumano));
        //caucion
        Gson gson_4 = new Gson();
        thAccionPersonal.setCaucion(gson_4.toJson(caucion));
        //situacion inicial
        Gson gson_5 = new Gson();
        thAccionPersonal.setSituacionActual(gson_5.toJson(situacionActual));
        //situacion propuesta
        Gson gson_6 = new Gson();
        thAccionPersonal.setSituacionPropuesta(gson_6.toJson(situacionPropuesta));
        /*Lista de codigos*/
        String temp = "";
        for (int i = 0; i < thAccionPersonal.getCodMotivoApList().length; i++) {
            temp = temp + thAccionPersonal.getCodMotivoApList()[i];
            if ((i + 1) != thAccionPersonal.getCodMotivoApList().length) {
                temp = temp + ":";
            }
        }
        thAccionPersonal.setCodigoList(temp);
        if (edit) {
            thAccionPersonal.setUsuarioModifica(userSession.getNameUser());
            thAccionPersonal.setFechaModificacion(new Date());
            thAccionPersonalService.edit(thAccionPersonal);
        } else {
            thAccionPersonal.setUsuarioCreacion(userSession.getNameUser());
            thAccionPersonal.setFechaCreacion(new Date());
            thAccionPersonal = thAccionPersonalService.create(thAccionPersonal);
        }
        PrimeFaces.current().executeScript("PF('accionDialog').hide()");
        JsfUtil.addSuccessMessage("Información", "Accion Personal" + (edit ? " editada" : " registrada") + " con éxito.");
        reporteThAccionPersonal(thAccionPersonal, false);
        reiniciarJSON();
        PrimeFaces.current().ajax().update("messages");
        PrimeFaces.current().ajax().update(":formMain");
    }

    public void delete(ThAccionPersonal a) {
        a.setEstado(Boolean.FALSE);
        thAccionPersonalService.edit(a);
        PrimeFaces.current().ajax().update(":formMain");
        PrimeFaces.current().ajax().update("messages");
        JsfUtil.addSuccessMessage("Accion Personal", " Eliminada con éxito");
    }

    public void reporteThAccionPersonal(ThAccionPersonal acc, Boolean accion) {
        servlet.addParametro("id", acc.getId());
        if (accion) {
            JsonObject(acc);
        }
        servlet.addParametro("URL_LOGO", JsfUtil.getRealPath("/reportes/IconosReportes/LogoMDT.png"));
        //situacion actual
        servlet.addParametro("situacionSI", situacionActual.getProceso());
        servlet.addParametro("situacionSI_1", situacionActual.getSubProceso());
        servlet.addParametro("situacionSI_2", situacionActual.getPuesto());
        servlet.addParametro("situacionSI_3", situacionActual.getLugarTrabajo());
        servlet.addParametro("situacionSI_4", situacionActual.getRemuneracion());
        servlet.addParametro("situacionSI_5", situacionActual.getCodPartida());
        servlet.addParametro("situacionSI_6", situacionActual.getNomPartida());
        //situacion propuesta
        servlet.addParametro("situacionSP", situacionPropuesta.getProceso());
        servlet.addParametro("situacionSP_1", situacionPropuesta.getSubProceso());
        servlet.addParametro("situacionSP_2", situacionPropuesta.getPuesto());
        servlet.addParametro("situacionSP_3", situacionPropuesta.getLugarTrabajo());
        servlet.addParametro("situacionSP_4", situacionPropuesta.getRemuneracion());
        servlet.addParametro("situacionSP_5", situacionPropuesta.getCodPartida());
        servlet.addParametro("situacionSP_6", situacionPropuesta.getNomPartida());
        //acta final
        servlet.addParametro("numAF", actaFinal.getNum());
        servlet.addParametro("fechaAF", actaFinal.getFecha());
        //acta talento humano
        servlet.addParametro("numTH", actaRecursoHumano.getNum());
        servlet.addParametro("fechaTH", actaRecursoHumano.getFecha());
        //caucion
        servlet.addParametro("numC", caucion.getNum());
        servlet.addParametro("fechaC", caucion.getFecha());
        //reemplazo
        servlet.addParametro("numRE", reemplazo.getNum());
        servlet.addParametro("fechaRE", reemplazo.getFecha());
        //info reemplazo
        servlet.addParametro("infoRE", infoReemplazoModel.getNombre());
        servlet.addParametro("infoRE_1", infoReemplazoModel.getPuesto());
        servlet.addParametro("infoRE_2", infoReemplazoModel.getFunciones());
        servlet.addParametro("infoRE_3", infoReemplazoModel.getAccion());
        servlet.addParametro("infoRE_4", infoReemplazoModel.getFecha());
        servlet.addParametro("infoRE_5", infoReemplazoModel.getColegio());
        /////////////////////
        servlet.setNombreReporte("accion_personal");
        servlet.setNombreSubCarpeta("_talento_humano");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
    }

    private void JsonObject(ThAccionPersonal thAccionPersonal) {
        Gson gson = new Gson();
        if (thAccionPersonal.getSituacionActual() != null) {
            situacionActual = gson.fromJson(thAccionPersonal.getSituacionActual(), SituacionModel.class);
        }
        if (thAccionPersonal.getSituacionPropuesta() != null) {
            situacionPropuesta = gson.fromJson(thAccionPersonal.getSituacionPropuesta(), SituacionModel.class);
        }
        if (thAccionPersonal.getCaucion() != null) {
            caucion = gson.fromJson(thAccionPersonal.getCaucion(), ActasModel.class);
        }
        if (thAccionPersonal.getActaFinal() != null) {
            actaFinal = gson.fromJson(thAccionPersonal.getActaFinal(), ActasModel.class);
        }
        if (thAccionPersonal.getActaRecursoHumano() != null) {
            actaRecursoHumano = gson.fromJson(thAccionPersonal.getActaRecursoHumano(), ActasModel.class);
        }
        if (thAccionPersonal.getInfoReemplazo() != null) {
            infoReemplazoModel = gson.fromJson(thAccionPersonal.getInfoReemplazo(), InfoReemplazoModel.class);
        }
        if (thAccionPersonal.getReemplazo() != null) {
            reemplazo = gson.fromJson(thAccionPersonal.getReemplazo(), ActasModel.class);
        }
    }

    public LazyModel<ThAccionPersonal> getThAccionPersonalLazyModel() {
        return thAccionPersonalLazyModel;
    }

    public void setThAccionPersonalLazyModel(LazyModel<ThAccionPersonal> thAccionPersonalLazyModel) {
        this.thAccionPersonalLazyModel = thAccionPersonalLazyModel;
    }

    public ThAccionPersonal getThAccionPersonal() {
        return thAccionPersonal;
    }

    public void setThAccionPersonal(ThAccionPersonal thAccionPersonal) {
        this.thAccionPersonal = thAccionPersonal;
    }

    public ThAccionPersonal getReporte() {
        return reporte;
    }

    public void setReporte(ThAccionPersonal reporte) {
        this.reporte = reporte;
    }

    public SituacionModel getSituacionActual() {
        return situacionActual;
    }

    public void setSituacionActual(SituacionModel situacionActual) {
        this.situacionActual = situacionActual;
    }

    public SituacionModel getSituacionPropuesta() {
        return situacionPropuesta;
    }

    public void setSituacionPropuesta(SituacionModel situacionPropuesta) {
        this.situacionPropuesta = situacionPropuesta;
    }

    public ActasModel getActaFinal() {
        return actaFinal;
    }

    public void setActaFinal(ActasModel actaFinal) {
        this.actaFinal = actaFinal;
    }

    public ActasModel getActaRecursoHumano() {
        return actaRecursoHumano;
    }

    public void setActaRecursoHumano(ActasModel actaRecursoHumano) {
        this.actaRecursoHumano = actaRecursoHumano;
    }

    public ActasModel getCaucion() {
        return caucion;
    }

    public void setCaucion(ActasModel caucion) {
        this.caucion = caucion;
    }

    public ActasModel getReemplazo() {
        return reemplazo;
    }

    public void setReemplazo(ActasModel reemplazo) {
        this.reemplazo = reemplazo;
    }

    public InfoReemplazoModel getInfoReemplazoModel() {
        return infoReemplazoModel;
    }

    public void setInfoReemplazoModel(InfoReemplazoModel infoReemplazoModel) {
        this.infoReemplazoModel = infoReemplazoModel;
    }

    public String getIdentificacionServidor() {
        return identificacionServidor;
    }

    public void setIdentificacionServidor(String identificacionServidor) {
        this.identificacionServidor = identificacionServidor;
    }

    public List<CatalogoItem> getMotivoList() {
        return motivoList;
    }

    public void setMotivoList(List<CatalogoItem> motivoList) {
        this.motivoList = motivoList;
    }

}
