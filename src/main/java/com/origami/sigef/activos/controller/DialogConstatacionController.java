/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.activos.controller;

import com.origami.sigef.common.entities.ConstatacionFisicaInventario;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.util.JsfUtil;
import java.io.Serializable;
import java.util.Arrays;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 *
 * @author erwin
 */
@Named(value = "dialogConstatacionController")
@ViewScoped
public class DialogConstatacionController implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(ProcesoIngresoInvController.class.getName());
    private LazyModel<ConstatacionFisicaInventario> lazyModel;

    @PostConstruct
    public void initView() {
        String tipo = JsfUtil.getRequestParameter("TIPO");
        lazyModel = new LazyModel<>(ConstatacionFisicaInventario.class);
        lazyModel.getFilterss().put("estado.texto:equal", "REGISTRADA");
        if (tipo.equals("INGRESO")) {
            lazyModel.getFilterss().put("razon:equal", Arrays.asList("INGRESO", "AMBOS"));
        } else {
            lazyModel.getFilterss().put("razon:equal", Arrays.asList("EGRESO", "AMBOS"));
        }
        lazyModel.getFilterss().put("ajustado", false);
    }

    public void close(ConstatacionFisicaInventario C) {
        PrimeFaces.current().dialog().closeDynamic(C);
    }

    public LazyModel<ConstatacionFisicaInventario> getLazyModel() {
        return lazyModel;
    }

    public void setLazyModel(LazyModel<ConstatacionFisicaInventario> lazyModel) {
        this.lazyModel = lazyModel;
    }

}
