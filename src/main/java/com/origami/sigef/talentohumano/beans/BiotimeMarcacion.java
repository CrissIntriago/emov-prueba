/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.talentohumano.beans;

import com.origami.sigef.common.entities.view.biotime.Marcacion;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.talentohumano.services.biotime.MarcacionService;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author ANGEL NAVARRO
 */
@Named
@ViewScoped
public class BiotimeMarcacion implements Serializable {

    private static final Logger LOG = Logger.getLogger(BiotimeMarcacion.class.getName());

    private LazyModel<Marcacion> marcacionLazy;
    @Inject
    private MarcacionService marcacionService;
    private Integer tipo;
    private Date desde;
    private Date hasta;

    @PostConstruct
    public void initView() {
        if (!JsfUtil.isAjaxRequest()) {
            try {
                marcacionLazy = new LazyModel<>(Marcacion.class);
                marcacionLazy.getSorteds().put("punchTime", "DESC");
                SimpleDateFormat f = new SimpleDateFormat("YYYY-mm-DD");
                marcacionLazy.setManager(marcacionService.getEntityManager());
                List<Date> fechas = Arrays.asList(f.parse(f.format(new Date())), f.parse(f.format(new Date())));
                marcacionLazy.getFilterss().put("punchTime", fechas);
            } catch (ParseException ex) {
                LOG.log(Level.SEVERE, null, ex);
            }
        }
    }

    public void buscar() {
        try {
            switch (tipo) {
                case 1:
                    marcacionLazy = new LazyModel<>(Marcacion.class);
                    marcacionLazy.getSorteds().put("punchTime", "DESC");
                    SimpleDateFormat f = new SimpleDateFormat("YYYY-mm-DD");
                    marcacionLazy.setManager(marcacionService.getEntityManager());
                    List<Date> fechas = Arrays.asList(f.parse(f.format(desde)), f.parse(f.format(hasta)));
                    marcacionLazy.getFilterss().put("punchTime", fechas);
                    break;
                case 2:

                    break;
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public LazyModel<Marcacion> getMarcacionLazy() {
        return marcacionLazy;
    }

    public void setMarcacionLazy(LazyModel<Marcacion> marcacionLazy) {
        this.marcacionLazy = marcacionLazy;
    }

    public Integer getTipo() {
        return tipo;
    }

    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }

    public Date getDesde() {
        return desde;
    }

    public void setDesde(Date desde) {
        this.desde = desde;
    }

    public Date getHasta() {
        return hasta;
    }

    public void setHasta(Date hasta) {
        this.hasta = hasta;
    }

}
