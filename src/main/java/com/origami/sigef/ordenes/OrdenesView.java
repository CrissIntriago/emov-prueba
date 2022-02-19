/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.ordenes;

import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.entities.OrdenTrabajo;
import com.origami.sigef.common.entities.Usuarios;
import com.origami.sigef.common.entities.transporte.Cooperativa;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.service.UsuarioService;
import com.origami.sigef.common.service.interfaces.FileUploadDoc;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.ordenes.services.OrdenService;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author Anyelo
 */
@Named
@ViewScoped
public class OrdenesView implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(OrdenesView.class.getName());

    @Inject
    private UserSession us;
    @Inject
    private ServletSession ss;
    @Inject
    private UsuarioService usuarioService;
    @Inject
    private FileUploadDoc fileSUploadDoc;
    @Inject
    private OrdenService ordenService;
    @Inject
    private ClienteService clienteService;

    private List<Usuarios> usuarios;
    private LazyModel<OrdenTrabajo> ordenesLazy;
    private OrdenTrabajo orden;
    private Cooperativa coop;
    private Boolean ver = true;

    @PostConstruct
    protected void iniView() {
        try {
            if (!JsfUtil.isAjaxRequest()) {
                ordenesLazy = new LazyModel<>(OrdenTrabajo.class);
                ordenesLazy.getSorteds().put("numOrden", "DESC");
                orden = new OrdenTrabajo();
                usuarios = this.usuarioService.findAllOrderActivos();
                coop = new Cooperativa();
                coop.setEmpresa(new Cliente());
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void nuevo(OrdenTrabajo orden) {
        if (orden == null) {
            this.orden = new OrdenTrabajo();
            this.orden.setSupervisor(new Usuarios(us.getUserId()));
            this.coop = new Cooperativa();
        } else {
            this.orden = orden;
            this.coop = this.orden.getCooperativa();
        }
        ver = false;
        JsfUtil.executeJS("PF('dlgOrden').show()");
        JsfUtil.update("frmOrden");
    }

    public void openDlg() {
        Utils.openDialog("/facelet/vehiculos/dlgCooperativas", null);
    }

    public void returnDialoProcess(SelectEvent evt) {
        Cooperativa c = (Cooperativa) evt.getObject();
        if (c != null) {
            orden.setCooperativa(c);
            coop = c;
        }
    }

    public void guardar() {
        if (orden.getCooperativa() == null) {
            JsfUtil.addErrorMessage("Cooperativa", "Debe buscar la cooperativa");
            return;
        }
        try {
            orden.setCooperativa(coop);
            orden.setEstado(true);
            orden = ordenService.save(orden, null);
            if (orden != null) {
                ver = true;
                JsfUtil.executeJS("PF('dlgOrden').hide()");
                JsfUtil.update("frmMain");
                coop = new Cooperativa();
                coop.setEmpresa(new Cliente());
                orden = new OrdenTrabajo();
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "", e);
        }
    }

    public void eliminar(OrdenTrabajo orden) {
        try {
            orden.setEstado(false);
            this.ordenService.edit(orden);
            JsfUtil.update("frmMain");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "", e);
        }
    }

    public void cerrarDialogo() {
        JsfUtil.executeJS("PF('dlgOrden').hide()");
        ver = true;
    }

    public void reporte() {

    }

    public void reporteIndividual(OrdenTrabajo orden) {

    }

    public void buscarCliente(Boolean tipoCliente) {
//        if (validaciones(clienteContacto)) {
//                JsfUtil.addWarningMessage("AVISO!!", "Revisar el No. de identificación y el tipo de identificación del contacto");
//                return;
//            }
//            Boolean accion = Boolean.FALSE;
//            if (clienteContacto.getTipoIdentificacion().getCodigo().equals("RUC")) {
//                accion = Boolean.TRUE;
//            }
//            clienteContacto = searchCliente(clienteContacto, accion);
//            if (clienteContacto.getCanton() != null) {
//                provinciaContacto = clienteContacto.getCanton().getIdProvincia();
//                actualizarCantones(false);
//            }
//            if (provinciaContacto.getId() == null && clienteContacto.getDireccion() != null) {
//                String[] split = clienteContacto.getDireccion().split("/");
//                provinciaContacto = proveedorService.findByNamedQuery1("Provincia.findByProvincia", split[0].replaceAll("[^\\p{ASCII}(N\u0303)(n\u0303)(\u00A1)(\u00BF)(\u00B0)(U\u0308)(u\u0308)]", "_"));
//                actualizarCantones(false);
//            }
//        PrimeFaces.current().ajax().update("fieldProveedor");
//        PrimeFaces.current().ajax().update("fieldContacto");
    }

    private Cliente searchCliente(Cliente cliente, Boolean tipoCliente) {
        Cliente resultado = new Cliente();
        try {
            Cliente c = clienteService.buscarCliente(cliente, tipoCliente);
            if (c.getIdentificacion() != null || c.getId() != null) {
                resultado = c;
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Busqueda de cliente", e);
        }
        return resultado;
    }

    public LazyModel<OrdenTrabajo> getOrdenesLazy() {
        return ordenesLazy;
    }

    public void setOrdenesLazy(LazyModel<OrdenTrabajo> ordenesLazy) {
        this.ordenesLazy = ordenesLazy;
    }

    public OrdenTrabajo getOrden() {
        return orden;
    }

    public void setOrden(OrdenTrabajo orden) {
        this.orden = orden;
    }

    public Boolean getVer() {
        return ver;
    }

    public void setVer(Boolean ver) {
        this.ver = ver;
    }

    public List<Usuarios> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuarios> usuarios) {
        this.usuarios = usuarios;
    }

    public Cooperativa getCoop() {
        return coop;
    }

    public void setCoop(Cooperativa coop) {
        this.coop = coop;
    }

}
