package com.origami.sigef.resource.talento_humano.controllers;

import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.resource.talento_humano.entities.ThEscalaSalarial;
import com.origami.sigef.resource.talento_humano.services.ThEscalaSalarialService;
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
@Named(value = "thEscalaSalarialView")
@ViewScoped
public class ThEscalaSalarialController implements Serializable {

    @Inject
    private ThEscalaSalarialService thEscalaSalarialService;
    @Inject
    private UserSession userSession;

    private LazyModel<ThEscalaSalarial> thEscalaSalarialLazy;

    private ThEscalaSalarial thEscalaSalarial;

    private Boolean view;

    @PostConstruct
    public void init() {
        this.thEscalaSalarialLazy = new LazyModel<>(ThEscalaSalarial.class);
        this.thEscalaSalarialLazy.getSorteds().put("grado", "ASC");
        this.thEscalaSalarialLazy.getFilterss().put("estado", true);
        cleanForm();
    }

    public void form(ThEscalaSalarial thEscalaSalarial, Boolean view) {
        this.view = view;
        if (thEscalaSalarial != null) {
            this.thEscalaSalarial = thEscalaSalarial;
        } else {
            cleanForm();
        }
        JsfUtil.executeJS("PF('thEscalaSalarialDlg').show()");
        PrimeFaces.current().ajax().update("thEscalaSalarialForm");
    }

    public void save() {
        boolean edit = thEscalaSalarial.getId() != null;
        if (thEscalaSalarial.getGrupoOrganizacional() == null || thEscalaSalarial.getGrupoOrganizacional().equals("")) {
            JsfUtil.addWarningMessage("AVISO!!!", "Debe ingresar un grupo ocupacional");
            return;
        } else {
            if (thEscalaSalarial.getGrupoOrganizacional().length() > 250) {
                JsfUtil.addWarningMessage("AVISO!!!", "El campo de grupo ocupacional debe ser menor o igual a 250 caracteres");
                return;
            }
        }
        if (thEscalaSalarial.getRemuneracionDolares().doubleValue() <= 0) {
            JsfUtil.addWarningMessage("AVISO!!!", "Debe ingresar un valor de remuneración mayor a cero");
            return;
        }
        if (edit) {
            thEscalaSalarial.setUsuarioCreacion(userSession.getNameUser());
            thEscalaSalarial.setFechaModificacion(new Date());
            thEscalaSalarialService.edit(thEscalaSalarial);
        } else {
            thEscalaSalarial.setUsuarioModifica(userSession.getNameUser());
            thEscalaSalarial.setFechaCreacion(new Date());
            thEscalaSalarial = thEscalaSalarialService.create(thEscalaSalarial);
        }
        JsfUtil.executeJS("PF('thEscalaSalarialDlg').hide()");
        PrimeFaces.current().ajax().update("thEscalaSalarialForm");
        JsfUtil.addSuccessMessage("INFO!!!", (edit ? "Editado" : " Registrado") + " con éxito");
        closeForm();
    }

    public void delete(ThEscalaSalarial thEscalaSalarial) {
        thEscalaSalarial.setEstado(false);
        thEscalaSalarialService.edit(thEscalaSalarial);
        JsfUtil.addSuccessMessage("INFO!!", "Se ha eliminado correctamente");
    }

    public void closeForm() {
        thEscalaSalarial = new ThEscalaSalarial();
        cleanForm();
        JsfUtil.executeJS("PF('thEscalaSalarialDlg').hide()");
        PrimeFaces.current().ajax().update("thEscalaSalarialTable");
    }

    private void cleanForm() {
        thEscalaSalarial = new ThEscalaSalarial();
    }

    public LazyModel<ThEscalaSalarial> getThEscalaSalarialLazy() {
        return thEscalaSalarialLazy;
    }

    public void setThEscalaSalarialLazy(LazyModel<ThEscalaSalarial> thEscalaSalarialLazy) {
        this.thEscalaSalarialLazy = thEscalaSalarialLazy;
    }

    public ThEscalaSalarial getThEscalaSalarial() {
        return thEscalaSalarial;
    }

    public void setThEscalaSalarial(ThEscalaSalarial thEscalaSalarial) {
        this.thEscalaSalarial = thEscalaSalarial;
    }

    public Boolean getView() {
        return view;
    }

    public void setView(Boolean view) {
        this.view = view;
    }

}
