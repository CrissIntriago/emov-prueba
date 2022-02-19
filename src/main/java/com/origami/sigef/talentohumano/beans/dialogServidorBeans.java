/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.talentohumano.beans;

import com.origami.sigef.common.entities.EscalaSalarial;
import com.origami.sigef.common.entities.TipoRol;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.resource.talento_humano.entities.Servidor;
import com.origami.sigef.resource.talento_humano.entities.ThServidorCargo;
import com.origami.sigef.talentohumano.UtilsTH.TalentoHumano;
import com.origami.sigef.talentohumano.services.EscalaSalarialService;
import com.origami.sigef.talentohumano.services.ServidorService;
import com.origami.sigef.talentohumano.services.TipoRolService;
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
 * @author OrigamiEC
 */
@Named(value = "dialogServidorBeans")
@ViewScoped
public class dialogServidorBeans implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
//    private LazyModel<Servidor> servidorMostrar;
    private LazyModel<ThServidorCargo> lazyServidorCargo;
    private Servidor servidor;
    @Inject
    private EscalaSalarialService escalaService;
    private List<EscalaSalarial> escalaList;
    private List<Servidor> listServidor;
    @Inject
    private ServidorService servidorService;
    @Inject
    private TipoRolService tipoRolService;
    private String fechaParam;
    private String fechaParamRol;
    private Date fechaFiltro;
    private Date fechaTipoRol;
    private String id;
    private TipoRol tipoRol;
    private int mes;

    @PostConstruct
    public void inicializate() {

        fechaParam = JsfUtil.getRequestParameter("FECHAGASTO");
        id = JsfUtil.getRequestParameter("ROL");
        servidor = new Servidor();
//        servidorMostrar = new LazyModel<>(Servidor.class);
        lazyServidorCargo = new LazyModel<>(ThServidorCargo.class);

//        servidorMostrar.getFilterss().put("estado", true);
        lazyServidorCargo.getFilterss().put("idServidor.estado", true);
        lazyServidorCargo.getFilterss().put("activo", true);
//        servidorMostrar.setDistinct(false);
        lazyServidorCargo.setDistinct(false);
        if (fechaParam != null) {
            fechaFiltro = new Date();
            fechaFiltro = TalentoHumano.ParseFecha(fechaParam, "dd/MM/yyyy");
//            servidorMostrar.getFilterss().put("fechaIngreso:lte", fechaFiltro);
            lazyServidorCargo.getFilterss().put("idServidor.fechaIngreso:lte", fechaFiltro);

        } else {
//            servidorMostrar.getFilterss().put("fechaIngreso:lte", new Date());
            lazyServidorCargo.getFilterss().put("idServidor.fechaIngreso:lte", new Date());
        }
        if (id != null) {
            fechaTipoRol = new Date();
            tipoRol = tipoRolService.tipoRolParam(Long.parseLong(id));
            mes = TalentoHumano.convertirMesLetraNum(tipoRol.getMes().getDescripcion()) + 1;
            fechaParamRol = "" + TalentoHumano.getUltimoDiaMes(tipoRol) + "/" + mes + "/" + tipoRol.getAnio();
            System.out.println(fechaParamRol);
            fechaTipoRol = TalentoHumano.ParseFecha(fechaParamRol, "dd/MM/yyyy");
//            servidorMostrar.getFilterss().put("fechaIngreso:lte", fechaTipoRol);
            lazyServidorCargo.getFilterss().put("idServidor.fechaIngreso:lte", fechaTipoRol);
        }
//        servidorMostrar.getSorteds().put("persona.apellido", "ASC");
        lazyServidorCargo.getSorteds().put("idServidor.persona.apellido", "ASC");
//        /*aqui tendremos que filtrar*/escalaMostrar.getFilterss().put("estado", true);
        escalaList = escalaService.findByNamedQuery("EscalaSalarial.findByEscalasVigentes");
//      vamos a agregar un filtro para llamar a datos a este Dialogo
        listServidor = servidorService.findServidorDistributivo();

    }

    public void closeSer(Servidor s) {
        PrimeFaces.current().dialog().closeDynamic(s);
    }

    public void closeEsc(EscalaSalarial e) {
        PrimeFaces.current().dialog().closeDynamic(e);
    }

    public void closeThSer(Servidor s) {
        PrimeFaces.current().dialog().closeDynamic(s);
    }

//<editor-fold defaultstate="collapsed" desc="Getters and Setters">
//    public LazyModel<Servidor> getServidorMostrar() {
//        return servidorMostrar;
//    }
//
//    public void setServidorMostrar(LazyModel<Servidor> servidorMostrar) {
//        this.servidorMostrar = servidorMostrar;
//    }
    public Servidor getServidor() {
        return servidor;
    }

    public void setServidor(Servidor servidor) {
        this.servidor = servidor;
    }

    public List<EscalaSalarial> getEscalaList() {
        return escalaList;
    }

    public void setEscalaList(List<EscalaSalarial> escalaList) {
        this.escalaList = escalaList;
    }

    public List<Servidor> getListServidor() {
        return listServidor;
    }

    public void setListServidor(List<Servidor> listServidor) {
        this.listServidor = listServidor;
    }

    public LazyModel<ThServidorCargo> getLazyServidorCargo() {
        return lazyServidorCargo;
    }

    public void setLazyServidorCargo(LazyModel<ThServidorCargo> lazyServidorCargo) {
        this.lazyServidorCargo = lazyServidorCargo;
    }
//</editor-fold>

}
