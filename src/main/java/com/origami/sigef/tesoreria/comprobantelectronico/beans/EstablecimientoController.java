/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.tesoreria.comprobantelectronico.beans;

import com.origami.sigef.catalogoItem.service.CatalogoItemService;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.tesoreria.comprobantelectronico.entities.Entidad;
import com.origami.sigef.tesoreria.comprobantelectronico.service.EntidadService;
import static com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot.LOG;
import java.io.Serializable;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 *
 * @author XndySxnchez
 */
@Named(value = "establecimientoView")
@ViewScoped
public class EstablecimientoController implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Inject
    private EntidadService entidadService;
    @Inject
    private ClienteService clienteService;
    @Inject
    private CatalogoItemService catalogoItemService;

    private LazyModel<Entidad> entidades;
    private Entidad establecimiento;
    private Boolean editar;
    private int mensaje;

    @PostConstruct
    public void init() {
        editar = Boolean.FALSE;
        establecimiento = new Entidad();
        entidades = new LazyModel<>(Entidad.class);
        entidades.getFilterss().put("estado", true);
    }

    public void guardarEstablecimiento() {

        if (establecimiento.getId() == null) {
            if (entidadService.existeEstablecimiento(establecimiento.getRucEntidad(),
                    establecimiento.getEstablecimiento()) == null) {
                establecimiento.setEstado(Boolean.TRUE);
                entidadService.create(establecimiento);
                establecimiento = new Entidad();
            } else {
                //EXISTE UN ESTABLECIMIENTO IGUAL
                JsfUtil.addErrorMessage("Advertencia", "Existe un establecimiento con los mismos datos");
            }
        } else {
            entidadService.edit(establecimiento);
            establecimiento = new Entidad();
            JsfUtil.addInformationMessage("Datos Actualizados Correctamente", "");
        }
    }

    public void eliminarEstablecimiento(Entidad e) {
        e.setEstado(Boolean.FALSE);
        entidadService.edit(e);
        PrimeFaces.current().ajax().update("datosEstablecimiento");
    }

    public void editarEstablecimiento(Entidad e) {
        editar = Boolean.TRUE;
        establecimiento = e;
        PrimeFaces.current().ajax().update("datosEstablecimiento");
    }

    public void cancelar() {
        editar = Boolean.FALSE;
        establecimiento = new Entidad();
    }

    public void buscarDatos() {
        if (establecimiento.getRucEntidad() == null) {
            JsfUtil.addErrorMessage("Identificacion", "Debe ingresar el número identificacion");
            return;
        }
        if (establecimiento.getRucEntidad().length() < 13) {
            JsfUtil.addWarningMessage("AVISO!!", "Debe ingresar la cantidad de dígitos correctos en la identificación del establecimiento");
            return;
        }
        try {
            Cliente c = new Cliente();
            c.setIdentificacion(establecimiento.getRucEntidad());
            c.setTipoIdentificacion(catalogoItemService.getTipoIdentificacion("RUC"));
            c = clienteService.buscarCliente(c, true);
            if (c.getIdentificacion() != null || c.getId() != null) {
                establecimiento.setRucEntidad(c.getIdentificacionCompleta());
                establecimiento.setNombreEntidad(c.getNombre());
                establecimiento.setNombreComercial(c.getApellido());
                establecimiento.setDireccion(c.getDireccion());
                establecimiento.setMail(c.getEmail());
                establecimiento.setTelefono(c.getTelefono());
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Busqueda de cliente", e);
        }
        PrimeFaces.current().ajax().update("datosEstablecimiento");
    }

    public LazyModel<Entidad> getEntidades() {
        return entidades;
    }

    public void setEntidades(LazyModel<Entidad> entidades) {
        this.entidades = entidades;
    }

    public Entidad getEstablecimiento() {
        return establecimiento;
    }

    public void setEstablecimiento(Entidad establecimiento) {
        this.establecimiento = establecimiento;
    }

    public Boolean getEditar() {
        return editar;
    }

    public void setEditar(Boolean editar) {
        this.editar = editar;
    }

}