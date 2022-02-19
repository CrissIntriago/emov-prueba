
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.controller;

import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.FuenteFinanciamiento;
import com.origami.sigef.common.entities.MasterCatalogo;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.CatalogoService;
import com.origami.sigef.common.service.MasterCatalogoService;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.contabilidad.lazy.FuenteFinanciamientoLazy;
import com.origami.sigef.contabilidad.service.FuenteFinanciamientoService;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 *
 * @author ORIGAMI2
 */
@Named(value = "fuenteView")
@ViewScoped
public class FuenteFinanciamientoController implements Serializable {

    /**
     *
     */
    @Inject
    private UserSession userSession;

    private static final long serialVersionUID = 1L;
    @Inject
    private FuenteFinanciamientoService fuenteSevice;
    @Inject
    private CatalogoService catalogoService;
    @Inject
    private MasterCatalogoService masterCatalogoService;

    private OpcionBusqueda opcion;
    private LazyModel<FuenteFinanciamiento> lazy;
    private FuenteFinanciamiento fuente;
    private List<MasterCatalogo> periodos;
    private List<CatalogoItem> clasificacion;

    @PostConstruct
    public void init() {
        this.opcion = new OpcionBusqueda();
        lazy = new LazyModel(FuenteFinanciamiento.class);
        lazy.getFilterss().put("estado", true);
        lazy.getSorteds().put("tipoFuente", "ASC");
        periodos = masterCatalogoService.findByNamedQuery("MasterCatalogo.findByCatalogoAndTipo", new Object[]{"tipo_cuenta", "CC"});
        fuente = new FuenteFinanciamiento();
        clasificacion = catalogoService.getItemsByCatalogo("tipo_fuente_financiamiento");
    }

    public void buscar() {
        lazy = new FuenteFinanciamientoLazy();
    }

    public void cancelar() {
        opcion = new OpcionBusqueda();
        lazy = new FuenteFinanciamientoLazy(opcion);
    }

    public void form(FuenteFinanciamiento f, boolean edit) {
        if (edit) {
            this.fuente = f;
        } else {
            this.fuente = new FuenteFinanciamiento();
            fuente.setPeriodo(opcion.getAnio());
            fuente.setFechaCreacion(fechaVigente());
        }
        PrimeFaces.current().executeScript("PF('cuentaDialog').show()");
        PrimeFaces.current().ajax().update(":formCuenta");
    }

    public void guardar() {

        boolean edit = fuente.getId() != null;
        try {
            if (!edit) {
                fuente.setUsuarioCreacion(userSession.getNameUser());
                fuente.setFechaCreacion(new Date());
                fuente = fuenteSevice.create(fuente);
            } else {
                fuente.setUsuarioModifica(userSession.getNameUser());
                fuente.setFechaModificacion(new Date());
                fuenteSevice.edit(fuente);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        PrimeFaces.current().executeScript("PF('cuentaDialog').hide()");
        PrimeFaces.current().ajax().update(":cuentas");
        PrimeFaces.current().ajax().update(":formMain");
        PrimeFaces.current().ajax().update("messages");
        JsfUtil.addSuccessMessage("Fuente", fuente.getNombre() + (edit ? " editada" : " registrada") + " con éxito.");

    }

    public void eliminar(FuenteFinanciamiento fuente) {
        JsfUtil.addSuccessMessage("Fuente", fuente.getNombre() + " eliminada con éxito");
        fuente.setEstado(Boolean.FALSE);
        fuenteSevice.edit(fuente);
        PrimeFaces.current().ajax().update("cuentas");
        PrimeFaces.current().ajax().update("messages");
    }

    public Date fechaVigente() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date fecha = new Date();
        Short anio = opcion.getAnio();
        String dia = "01/01/" + anio;
        try {
            fecha = sdf.parse(dia);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fecha;
    }

    public FuenteFinanciamientoService getFuenteSevice() {
        return fuenteSevice;
    }

    public void setFuenteSevice(FuenteFinanciamientoService fuenteSevice) {
        this.fuenteSevice = fuenteSevice;
    }

    public CatalogoService getCatalogoService() {
        return catalogoService;
    }

    public void setCatalogoService(CatalogoService catalogoService) {
        this.catalogoService = catalogoService;
    }

    public MasterCatalogoService getMasterCatalogoService() {
        return masterCatalogoService;
    }

    public void setMasterCatalogoService(MasterCatalogoService masterCatalogoService) {
        this.masterCatalogoService = masterCatalogoService;
    }

    public OpcionBusqueda getOpcion() {
        return opcion;
    }

    public void setOpcion(OpcionBusqueda opcion) {
        this.opcion = opcion;
    }

    public LazyModel<FuenteFinanciamiento> getLazy() {
        return lazy;
    }

    public void setLazy(LazyModel<FuenteFinanciamiento> lazy) {
        this.lazy = lazy;
    }

    public FuenteFinanciamiento getFuente() {
        return fuente;
    }

    public void setFuente(FuenteFinanciamiento fuente) {
        this.fuente = fuente;
    }

    public List<MasterCatalogo> getPeriodos() {
        return periodos;
    }

    public void setPeriodos(List<MasterCatalogo> periodos) {
        this.periodos = periodos;
    }

    public List<CatalogoItem> getClasificacion() {
        return clasificacion;
    }

    public void setClasificacion(List<CatalogoItem> clasificacion) {
        this.clasificacion = clasificacion;
    }

}
