/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.tesoreria.comprobantelectronico.beans;

import com.origami.sigef.common.entities.Usuarios;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.SeqGenMan;
import com.origami.sigef.common.service.UsuarioService;
import com.origami.sigef.common.service.ValoresService;
import com.origami.sigef.common.util.FilesUtil;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.resource.contabilidad.entities.ContCuentas;
import com.origami.sigef.tesoreria.comprobantelectronico.entities.Cajero;
import com.origami.sigef.tesoreria.comprobantelectronico.entities.Entidad;
import com.origami.sigef.tesoreria.comprobantelectronico.service.CajeroService;
import com.origami.sigef.tesoreria.comprobantelectronico.service.EntidadService;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author XndySxnchez
 */
@Named(value = "cajerosView")
@ViewScoped
public class CajerosController implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Inject
    private CajeroService cajeroService;
    @Inject
    private EntidadService entidadService;
    @Inject
    private ValoresService valoresService;
    @Inject
    private SeqGenMan seqGenManService;
    @Inject
    private UsuarioService usuarioService;

    private LazyModel<Cajero> cajeros;
    private Cajero cajero;
    private Usuarios usuario;
    private Boolean editar;

    private List<Entidad> entidades;
    private Entidad entidad;
    private Integer idEntidad;
    private UploadedFile firmaElectronica;

    private List<Usuarios> usuariosList;
    private LazyModel<ContCuentas> contCuentasLazy;

    @PostConstruct
    public void init() {
        entidades = entidadService.getListEntidades();
        editar = Boolean.FALSE;
        cajero = new Cajero();
        cajeros = new LazyModel<>(Cajero.class);
        cajeros.getFilterss().put("estado", true);
        actualizarListadoUsuarios();
    }

    private void actualizarListadoUsuarios() {
        usuariosList = cajeroService.getListUsuarios();
    }

    public void guardarCajero() {
        if (cajero.getArchivo() == null || cajero.getArchivo().isEmpty()) {
            JsfUtil.addErrorMessage("Advertencia", "Debe subir un archivo de Firma Electronica (.p12)");
            return;
        }
        if (cajero.getId() == null) {
            if (cajeroService.findByCajero(cajero.getUsuario()) == null) {
                cajero.setEntidad(entidadService.find(idEntidad));
                cajero.setEstado(Boolean.TRUE);
                cajeroService.create(cajero);
                loadSecuencias();
                cajero = new Cajero();
                idEntidad = null;
            } else {
                //EXISTE UN CAJERO IGUAL
                JsfUtil.addErrorMessage("Advertencia", "Existe un Cajero con los mismos datos");
            }
        } else {
            cajero.setEntidad(entidadService.find(idEntidad));
            cajeroService.edit(cajero);
            loadSecuencias();
            JsfUtil.addInformationMessage("Datos Actualizados Correctamente", "");
            editar = Boolean.FALSE;
            cajero = new Cajero();
            idEntidad = null;
        }
        actualizarListadoUsuarios();
    }

    private void loadSecuencias() {
        seqGenManService.crearSecuenciaAmbinete(cajero.getVariableSecuenciaFacturas());
        seqGenManService.crearSecuenciaAmbinete(cajero.getVariableSecuenciaNotaDebito());
        seqGenManService.crearSecuenciaAmbinete(cajero.getVariableSecuenciaNotaCredito());
        seqGenManService.crearSecuenciaAmbinete(cajero.getVariableSecuenciaRetencion());
        seqGenManService.crearSecuenciaAmbinete(cajero.getVariableSecuenciaGuiaRemision());
    }

    public void usuarioSeleccionado() {
        usuario = usuarioService.findByUsuario(cajero.getUsuario());
        PrimeFaces.current().ajax().update("panelDatosPersonales");
    }

    public void eliminarCajero(Cajero c) {
        c.setEstado(Boolean.FALSE);
        cajeroService.edit(c);
        actualizarListadoUsuarios();
    }

    public void editarCajero(Cajero c) {
        editar = Boolean.TRUE;
        cajero = c;
        idEntidad = cajero.getEntidad().getId();
        usuarioSeleccionado();
    }

    public void cancelar() {
        editar = Boolean.FALSE;
        cajero = new Cajero();
        entidad = null;
        idEntidad = null;
        usuario = new Usuarios();
        actualizarListadoUsuarios();
    }

    public void upload(FileUploadEvent file) {
        try {
            String archivoFD = valoresService.findByCodigo("RUTA_FIRMA_ELECTRONICA");
            FilesUtil.copyFileServer(file, archivoFD);
            cajero.setArchivo(archivoFD + Utils.reemplazarEspacionEnBlanco(file.getFile().getFileName(), "_"));
        } catch (IOException e) {
            JsfUtil.addErrorMessage("Ocurrió un error al subir el archivo de la Firma Electrónica", "");
        }
    }

    public void openDlgCuenta(Boolean accion) {
        contCuentasLazy = new LazyModel<>(ContCuentas.class);
        contCuentasLazy.getSorteds().put("codigo", "ASC");
        contCuentasLazy.getFilterss().put("estado", true);
        contCuentasLazy.getFilterss().put("activo", true);
        contCuentasLazy.getFilterss().put("movimiento", true);
        if (accion) {
            JsfUtil.executeJS("PF('dlgCuentaContable').show()");
        }
        PrimeFaces.current().ajax().update("contCuentasTable");
        PrimeFaces.current().ajax().update("dlgCuentaContableForm");
    }

    public void closeDlgCuenta(ContCuentas contCuentas) {
        cajero.setContCuentas(contCuentas);
        JsfUtil.executeJS("PF('dlgCuentaContable').hide()");
        JsfUtil.addSuccessMessage("INFO!!!", "Se ha seleccionado correctamente la cuenta contable");
        PrimeFaces.current().ajax().update("gridCtaContable");
    }

    public LazyModel<Cajero> getCajeros() {
        return cajeros;
    }

    public void setCajeros(LazyModel<Cajero> cajeros) {
        this.cajeros = cajeros;
    }

    public Cajero getCajero() {
        return cajero;
    }

    public void setCajero(Cajero cajero) {
        this.cajero = cajero;
    }

    public Usuarios getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuarios usuario) {
        this.usuario = usuario;
    }

    public Boolean getEditar() {
        return editar;
    }

    public void setEditar(Boolean editar) {
        this.editar = editar;
    }

    public List<Entidad> getEntidades() {
        return entidades;
    }

    public void setEntidades(List<Entidad> entidades) {
        this.entidades = entidades;
    }

    public Entidad getEntidad() {
        return entidad;
    }

    public void setEntidad(Entidad entidad) {
        this.entidad = entidad;
    }

    public Integer getIdEntidad() {
        return idEntidad;
    }

    public void setIdEntidad(Integer idEntidad) {
        this.idEntidad = idEntidad;
    }

    public UploadedFile getFirmaElectronica() {
        return firmaElectronica;
    }

    public void setFirmaElectronica(UploadedFile firmaElectronica) {
        this.firmaElectronica = firmaElectronica;
    }

    public List<Usuarios> getUsuariosList() {
        return usuariosList;
    }

    public void setUsuariosList(List<Usuarios> usuariosList) {
        this.usuariosList = usuariosList;
    }

    public LazyModel<ContCuentas> getContCuentasLazy() {
        return contCuentasLazy;
    }

    public void setContCuentasLazy(LazyModel<ContCuentas> contCuentasLazy) {
        this.contCuentasLazy = contCuentasLazy;
    }

}
