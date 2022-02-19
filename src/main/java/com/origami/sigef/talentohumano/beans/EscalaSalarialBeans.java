package com.origami.sigef.talentohumano.beans;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.EscalaSalarial;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.talentohumano.services.EscalaSalarialService;
import java.io.Serializable;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 *
 * @author Luis Pozo
 */
@Named(value = "escalaSalarialView")
@ViewScoped
public class EscalaSalarialBeans implements Serializable {

    private static final long serialVersionUID = 1L;
    private LazyModel<EscalaSalarial> escalaLazy;
    private EscalaSalarial escalasalarial;
//    private Distributivo distributivo;
    @Inject
    private EscalaSalarialService escalaSalarialService;
    @Inject
    private ServletSession ss;
//    @Inject
//    private DistributivoService disService;
    @Inject
    private UserSession userSession;

    @PostConstruct
    public void inicializate() {
        this.escalasalarial = new EscalaSalarial();
        escalaLazy = new LazyModel<>(EscalaSalarial.class);
        escalaLazy.getFilterss().put("estado", true);
        escalaLazy.getSorteds().put("grupoOrganizacional", "ASC");
        escalaLazy.getSorteds().put("grado", "ASC");
//        this.distributivo = new Distributivo();
    }

    //editar
    public void form(EscalaSalarial escala) {

        if (escala != null) {
            this.escalasalarial = escala;
        } else {
            escalasalarial = new EscalaSalarial();

        }

        escalasalarial.setFechaModificacion(new Date());
        escalasalarial.setUsuarioModifica(userSession.getNameUser());
        escalasalarial.setEstado(true);
        PrimeFaces.current().executeScript("PF('escalaSalarialDialog').show()");
        PrimeFaces.current().ajax().update(":formEscala");
    }
    //guardar listo

    public void guardarEscala() {
        boolean edit = escalasalarial.getId() != null;
        if (!edit) {

            escalasalarial.setUsuarioCreacion(userSession.getNameUser());
            escalasalarial.setFechaCreacion(new Date());
            escalasalarial = escalaSalarialService.create(escalasalarial);
        } else {

            escalaSalarialService.edit(escalasalarial);

        }
        PrimeFaces.current().executeScript("PF('escalaSalarialDialog').hide()");
        PrimeFaces.current().ajax().update(":formMain");
        JsfUtil.addSuccessMessage("Información", escalasalarial.getGrupoOrganizacional() + (edit ? " editada" : " registrada") + " con éxito.");

    }

    public void eliminarEscala(EscalaSalarial escala) {
        if (escala.getId() == null) {
            JsfUtil.addErrorMessage("Error", "Escala no puedo ser actualizado.");
            return;
        }
        JsfUtil.addSuccessMessage("Información", escala.getGrupoOrganizacional() + " eliminada con éxito");
        escala.setEstado(Boolean.FALSE);
        escalaSalarialService.edit(escala);
        PrimeFaces.current().ajax().update(":formMain");
        PrimeFaces.current().ajax().update("messages");

    }

    public void printReport() {
        ss.setNombreReporte("escalaSalarial");
        ss.setNombreSubCarpeta("EscalaSalarial");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");

    }//<editor-fold defaultstate="collapsed" desc="getter and Setters">

    public EscalaSalarial getEscalasalarial() {
        return escalasalarial;
    }

    public void setEscalasalarial(EscalaSalarial escalasalarial) {
        this.escalasalarial = escalasalarial;
    }

    public LazyModel<EscalaSalarial> getEscalaLazy() {
        return escalaLazy;
    }

    public void setEscalaLazy(LazyModel<EscalaSalarial> escalaLazy) {
        this.escalaLazy = escalaLazy;
    }
//</editor-fold>

}
