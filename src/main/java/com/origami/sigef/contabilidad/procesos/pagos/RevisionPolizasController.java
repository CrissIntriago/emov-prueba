/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.procesos.pagos;

import com.origami.sigef.activos.service.AdquisicionesService;
import com.origami.sigef.catalogoItem.service.CatalogoItemService;
import com.origami.sigef.common.config.RolUsuario;
import com.origami.sigef.common.entities.Adquisiciones;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.Garantias;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.models.Correo;
import com.origami.sigef.common.service.KatalinaService;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.talentohumano.UtilsTH.TalentoHumano;
import com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot;
import static com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot.LOG;
import com.origami.sigef.resource.procesos.entities.Observaciones;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.Email;
import org.primefaces.PrimeFaces;

/**
 *
 * @author jintr
 */
@Named(value = "revisionGarantiaView")
@ViewScoped
public class RevisionPolizasController extends BpmnBaseRoot implements Serializable {

    @Inject
    private CatalogoItemService catalogoService;
    @Inject
    private UserSession userSession;
    @Inject
    private AdquisicionesService adquisicionesService;
    @Inject
    private ClienteService clienteService;
    @Inject
    private KatalinaService katalinaService;

    /*Booleanos*/
    private boolean garantiaBol;
    private String observaciones;
    private Short dias;
    private String asunto;
    private String ccdestinat;
    private String mensaje;
    @Email(message = "Debe ser un correo electrónico válido")
    private String email;

    private Garantias garantia;
    private Adquisiciones adquisicion;
    private OpcionBusqueda busqueda;
    private LazyModel<Garantias> garantiaLazy;
    private List<CatalogoItem> tiposdoc;
    private List<Garantias> listGarantiasVigentes;
    private List<Garantias> listGarantiasVencidas;

    @PostConstruct
    public void inicializate() {
        try {
            if (this.session.getTaskID() != null) {
                this.setTaskId(this.session.getTaskID());
                observacion = new Observaciones();
                observacion.setIdTramite(tramite);

                busqueda = new OpcionBusqueda();
                adquisicion = new Adquisiciones();
                garantia = new Garantias();
                dias = 0;
                adquisicion = adquisicionesService.getAdquisicionById(tramite.getIdReferencia());
                garantiaLazy = new LazyModel<>(Garantias.class);
                garantiaLazy.getFilterss().put("estado", true);
                garantiaLazy.getSorteds().put("adquisicion.numeroContrato", "ASC");
                garantiaLazy.getFilterss().put("adquisicion.id:equal", tramite.getIdReferencia().longValue());
                garantiaLazy.setDistinct(false);
                listGarantiasVencidas = new ArrayList<>();
                listGarantiasVigentes = new ArrayList<>();
                tiposdoc = catalogoService.getTipoCuenta("tipo_doc", "ti_doc");
                verificarGarantia();
            } else {
                JsfUtil.redirectFaces("/procesos/bandeja-tareas");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public Boolean polizaVigente(Date fechaHasta) {
        return fechaHasta.compareTo(new Date()) > 0;
    }

    public void verificarGarantia() {
        garantiaBol = Boolean.FALSE;
        if (adquisicion.isGarantia()) {
            List<Garantias> temp = adquisicionesService.aplicaGarantiaTieneRegistrosEnPoliza(adquisicion);
            if (!temp.isEmpty()) {
                for (Garantias garantiasP : temp) {
                    boolean vigente;
                    vigente = polizaVigente(garantiasP.getFechaHasta());
                    if (vigente) {
                        listGarantiasVigentes.add(garantiasP);
                    } else {
                        listGarantiasVencidas.add(garantiasP);
                    }
                }
                if (listGarantiasVigentes.isEmpty()) {
                    if (!listGarantiasVencidas.isEmpty()) {
                        garantiaBol = Boolean.TRUE;
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "AVISO!!", "Contrato registra Garantías Vencidas, "
                                + "informar a TESORERIA"));
                    }
                }
            } else {
                garantiaBol = Boolean.TRUE;
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "AVISO!!", "No existen Garantías Ingresadas, "
                        + "Informe a TESORERIA: Registre Garantías"));
            }
        }
//        PrimeFaces.current().ajax().update("mensaje");
    }

    public void openDlgGarantia(Garantias gar) {
        garantia = new Garantias();
        garantia = gar;
        calculoDias();
        PrimeFaces.current().executeScript("PF('dialogViewGarantia').show()");
    }

    public void abriDlogo() {
        observacion.setEstado(true);
        observacion.setFecCre(new Date());
        observacion.setTarea(tarea.getName());
        observacion.setUserCre(session.getName());
        PrimeFaces.current().executeScript("PF('dlgObservaciones').show()");
        PrimeFaces.current().ajax().update(":frmDlgObser");

    }

    public void notificarCorreoTesoreria() {
        PrimeFaces.current().executeScript("PF('dlgNotificar').show()");
        PrimeFaces.current().ajax().update(":frmFormulario");

    }

    public void enviarCorreo() {
        Correo c = new Correo();
        c.setDestinatario(getEmail());
        c.setAsunto(getAsunto());
        c.setMensaje(getMensaje());
        katalinaService.enviarCorreo(c);
        PrimeFaces.current().executeScript("PF('dlgNotificar').hide()");
        PrimeFaces.current().ajax().update(":frmFormulario");
    }

    public void completarTarea() {
        try {
            observacion.setObservacion(observaciones);
            getParamts().put("usuarioPago", clienteService.getrolsUser(RolUsuario.autorizacionPago));
            if (saveTramite() == null) {
                return;
            }
            JsfUtil.executeJS("PF('dlgObservaciones').hide()");
            PrimeFaces.current().ajax().update(":frmDlgObser");
            this.session.setVarTemp(null);
            super.completeTask((HashMap<String, Object>) getParamts());
            JsfUtil.redirectFaces("/procesos/bandeja-tareas");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "completas tareas", e);
        }
    }

    public void calculoDias() {
        this.dias = (TalentoHumano.diasDiferencia(garantia.getFechaDesde(), garantia.getFechaHasta())).shortValue();
    }

    public boolean isGarantiaBol() {
        return garantiaBol;
    }

    public void setGarantiaBol(boolean garantiaBol) {
        this.garantiaBol = garantiaBol;
    }

    public Garantias getGarantia() {
        return garantia;
    }

    public void setGarantia(Garantias garantia) {
        this.garantia = garantia;
    }

    public OpcionBusqueda getBusqueda() {
        return busqueda;
    }

    public void setBusqueda(OpcionBusqueda busqueda) {
        this.busqueda = busqueda;
    }

    public LazyModel<Garantias> getGarantiaLazy() {
        return garantiaLazy;
    }

    public void setGarantiaLazy(LazyModel<Garantias> garantiaLazy) {
        this.garantiaLazy = garantiaLazy;
    }

    public List<CatalogoItem> getTiposdoc() {
        return tiposdoc;
    }

    public void setTiposdoc(List<CatalogoItem> tiposdoc) {
        this.tiposdoc = tiposdoc;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Adquisiciones getAdquisicion() {
        return adquisicion;
    }

    public void setAdquisicion(Adquisiciones adquisicion) {
        this.adquisicion = adquisicion;
    }

    public Short getDias() {
        return dias;
    }

    public void setDias(Short dias) {
        this.dias = dias;
    }

    public List<Garantias> getListGarantiasVigentes() {
        return listGarantiasVigentes;
    }

    public void setListGarantiasVigentes(List<Garantias> listGarantiasVigentes) {
        this.listGarantiasVigentes = listGarantiasVigentes;
    }

    public List<Garantias> getListGarantiasVencidas() {
        return listGarantiasVencidas;
    }

    public void setListGarantiasVencidas(List<Garantias> listGarantiasVencidas) {
        this.listGarantiasVencidas = listGarantiasVencidas;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getCcdestinat() {
        return ccdestinat;
    }

    public void setCcdestinat(String ccdestinat) {
        this.ccdestinat = ccdestinat;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

}
