/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.controller;

import com.origami.sigef.Entidad.Service.DatosGeneralesEntidadService;
import com.origami.sigef.activos.service.ProveedorService;
import com.origami.sigef.common.entities.Canton;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.entities.Provincia;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.CatalogoService;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.service.ClienteService;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 *
 * @author Criss Intriago
 */
@Named(value = "clienteView")
@ViewScoped
public class ClienteController implements Serializable {

    /*Inject*/
    @Inject
    private ClienteService clienteService;
    @Inject
    private CatalogoService catalogoService;
    @Inject
    private UserSession userSession;
    @Inject
    private DatosGeneralesEntidadService provinciaCantonService;
    @Inject
    private ProveedorService proveedorService;

    /*LazyModel*/
    private LazyModel<Cliente> clienteLazyModel;

    /*Objectos*/
    private Cliente cliente;
    private Provincia provinciaSelecionada;

    /*Listas*/
    private List<CatalogoItem> tiposDeIdentificacion;
    private List<CatalogoItem> tiposDeGenero;
    private List<Provincia> provincias;
    private List<Canton> cantones;

    /*Variables*/
    private Boolean ocultar;
    private Boolean noEditar;
    private Boolean discapacitado;
    private Boolean editar;
    private int tipo;

    @PostConstruct
    public void inicializate() {
        this.cliente = new Cliente();
        this.discapacitado = Boolean.FALSE;
        this.provinciaSelecionada = new Provincia();
        this.clienteLazyModel = new LazyModel<>(Cliente.class);
        this.clienteLazyModel.getSorteds().put("apellido", "ASC");
        this.clienteLazyModel.getFilterss().put("estado", true);
        this.clienteLazyModel.getFilterss().put("validado", true);
        this.tiposDeIdentificacion = catalogoService.getItemsByCatalogo("tipo_identificacion_beneficiario");
        this.tiposDeGenero = catalogoService.getItemsByCatalogo("tipo_genero");
        this.provincias = provinciaCantonService.getProvincias();
        this.ocultar = Boolean.FALSE;
        this.noEditar = Boolean.FALSE;
        this.tipo = 1;
    }

    public void form(Cliente clienteEnviado) {
        vaciarFormulario();
        this.cliente = new Cliente();
        if (clienteEnviado != null) {
            this.cliente = clienteEnviado;
            if (cliente.getCanton() != null) {
                provinciaSelecionada = cliente.getCanton().getIdProvincia();
                actualizarCantones();
            }
            actualizarFormulario();
            if (tipo == 1) {
                if (cliente.getIdentificacion().length() >= 13) {
                    cliente.setIdentificacion(cliente.getIdentificacionCompleta().substring(0, 13));
                } else {
                    cliente.setIdentificacion(cliente.getIdentificacionCompleta());
                }
            } else {
                cliente.setIdentificacion(cliente.getIdentificacion());
            }
            noEditar = true;
        } else {
            this.cliente = new Cliente();
            noEditar = false;
        }
        PrimeFaces.current().executeScript("PF('clienteDialog').show()");
        PrimeFaces.current().ajax().update("clienteDialog");
    }

    public void ver(Cliente c) {
        this.cliente = new Cliente();
        this.ocultar = Boolean.FALSE;
        this.cliente = c;
        if (cliente.getTipoIdentificacion().getTexto().equals("RUC")) {
            this.ocultar = Boolean.TRUE;
        }
        if (cliente.getCanton() != null) {
            provinciaSelecionada = cliente.getCanton().getIdProvincia();
            actualizarCantones();
        }
        discapacitado = cliente.getDiscapacidad();
        PrimeFaces.current().executeScript("PF('clienteDialogView').show()");
        PrimeFaces.current().ajax().update("formClienteView");
    }

    public void handleClose() {
        vaciarFormulario();
        this.cliente = new Cliente();
        PrimeFaces.current().ajax().update("clienteDialog");
    }

    public void buscarCliente(Boolean tipoCliente) {
        if (cliente.getId() == null) {
            if (cliente.getTipoIdentificacion() == null) {
                JsfUtil.addErrorMessage("Tipo Identificacion", "Debe seleccionar el tipo de identificacion");
                return;
            } else {
                if (cliente.getIdentificacion() == null) {
                    JsfUtil.addErrorMessage("Identificacion", "Debe ingresar el número identificacion");
                    return;
                } else {
                    if (tipoCliente) {
                        if (cliente.getIdentificacion().length() < 13) {
                            JsfUtil.addErrorMessage("Identificacion", "La cantidad de dígitos del RUC/RISE esta incompleto");
                            return;
                        }
                    } else {
                        if (cliente.getIdentificacion().length() < 10) {
                            JsfUtil.addErrorMessage("Identificacion", "La cantidad de dígitos del No. Identificación esta incompleto");
                            return;
                        }
                    }
                }
            }
            Cliente c = clienteService.buscarCliente(cliente, tipoCliente);
            if (c.getIdentificacion() != null || c.getId() != null) {
                cliente = c;
                if (!tipoCliente) {
                    if (cliente.getCanton() != null) {
                        provinciaSelecionada = c.getCanton().getIdProvincia();
                        actualizarCantones();
                    }
                    if (provinciaSelecionada.getId() == null && c.getDireccion() != null) {
                        String[] split = c.getDireccion().split("/");
                        provinciaSelecionada = proveedorService.findByNamedQuery1("Provincia.findByProvincia", split[0].replaceAll("[^\\p{ASCII}(N\u0303)(n\u0303)(\u00A1)(\u00BF)(\u00B0)(U\u0308)(u\u0308)]", "_"));
                        actualizarCantones();
                    }
                } else {
                    cliente.setIdentificacion(cliente.getIdentificacion().concat(cliente.getRuc()));
                }
                noEditar = true;
                PrimeFaces.current().ajax().update("gridMain");
                PrimeFaces.current().ajax().update("gridAdcionales");
            }
        } else {
            JsfUtil.addWarningMessage("Cliente", "Ya tiene un registro cargado, si quiere cargar otro dele clic en cancelar");
        }
    }

    private void Validaciones(Boolean accion) {
        String aux = cliente.getIdentificacion().substring(0, 10);
        if (accion) {
            if (clienteService.verificarIdentificacion(aux, cliente.getTipoIdentificacion())) {
                JsfUtil.addWarningMessage("CLIENTE", "Ya está registrado " + cliente.getNombre());
                return;
            }
            if (!Utils.validateCCRuc(cliente.getIdentificacion())) {
                JsfUtil.addWarningMessage("CLIENTE", "La identificacion es incorrecta");
                return;
            }
        }
        if (!Utils.validarEmailConExpresion(cliente.getEmail())) {
            JsfUtil.addWarningMessage("CLIENTE", "El correo ingresado es incorrecto");
            return;
        }

    }

    public void save() {
        boolean edit = cliente.getId() != null;
        if (edit) {
            Validaciones(false);
            if (tipo == 1) {
                cliente.setIdentificacion(cliente.getIdentificacionCompleta().substring(0, 10));
            }
            cliente.setUsuarioModificacion(userSession.getNameUser());
            cliente.setFechaModificacion(new Date());
            cliente.setValidado(true);
            cliente.setEstado(true);
            clienteService.edit(cliente);
        } else {
            Validaciones(true);
            if (tipo == 1) {
                String ruc = cliente.getRuc();
                if (cliente.getIdentificacion().length() == 13) {
                    cliente.setIdentificacion(cliente.getIdentificacion().substring(0, 10));
                    cliente.setRuc(cliente.getRuc());
                }
            }
            cliente.setValidado(true);
            cliente.setEstado(true);
            cliente.setUsuarioCreacion(userSession.getNameUser());
            cliente.setFechaCreacion(new Date());
            cliente = clienteService.create(cliente);
        }
        PrimeFaces.current().executeScript("PF('clienteDialog').hide()");
        PrimeFaces.current().ajax().update("clientes");
        JsfUtil.addSuccessMessage("CLIENTE", (edit ? "Editado" : " Registrado") + " con éxito.");
        vaciarFormulario();
    }

    public void delete(Cliente cliente) {
        if (clienteService.verificarProveedor(cliente)) {
            JsfUtil.addErrorMessage("CLIENTE", "No se puede eliminar porque esta relacionado con Servidor Público");
            return;
        }
        if (clienteService.verificarServidor(cliente)) {
            JsfUtil.addErrorMessage("CLIENTE", "No se puede eliminar porque esta relacionado con Proveedor");
            return;
        }
        cliente.setEstado(Boolean.FALSE);
        clienteService.edit(cliente);
        JsfUtil.addSuccessMessage("Cliente", cliente.getIdentificacion() + " eliminada con éxito");
        PrimeFaces.current().ajax().update("clientes");
    }

    public void actualizarFormulario() {
        if (cliente.getTipoIdentificacion() != null) {
            switch (cliente.getTipoIdentificacion().getTexto()) {
                case "RUC":
                    tipo = 1;
                    break;
                case "CEDULA":
                    tipo = 2;
                    break;
                default:
                    tipo = 3;
                    break;
            }
        } else {
            tipo = 1;
        }
        PrimeFaces.current().ajax().update("formCliente");
    }

    private void vaciarFormulario() {
        this.tipo = 1;
        this.provinciaSelecionada = new Provincia();
        this.cliente = new Cliente();
        this.cantones = new ArrayList<>();
        this.ocultar = Boolean.FALSE;
        this.noEditar = Boolean.FALSE;
        this.discapacitado = Boolean.FALSE;
    }

    public void cancelar() {
        vaciarFormulario();
        actualizarFormulario();
        PrimeFaces.current().ajax().update("formCliente");
        PrimeFaces.current().ajax().update("gridMain");
        PrimeFaces.current().ajax().update("gridAdcionales");
    }

    public void requiredPorcentajeDiscapacidad() {
        if (cliente.getDiscapacidad()) {
            discapacitado = Boolean.TRUE;
        } else {
            discapacitado = Boolean.FALSE;
        }
    }

    public void actualizarCantones() {
        if (provinciaSelecionada != null) {
            this.cantones = provinciaCantonService.getCantones(provinciaSelecionada);
        } else {
            this.cantones = new ArrayList<>();
        }
    }

    //<editor-fold defaultstate="collapsed" desc="Get - Set">
    public Boolean getDiscapacitado() {
        return discapacitado;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public void setDiscapacitado(Boolean discapacitado) {
        this.discapacitado = discapacitado;
    }

    public LazyModel<Cliente> getClienteLazyModel() {
        return clienteLazyModel;
    }

    public void setClienteLazyModel(LazyModel<Cliente> clienteLazyModel) {
        this.clienteLazyModel = clienteLazyModel;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<CatalogoItem> getTiposDeIdentificacion() {
        return tiposDeIdentificacion;
    }

    public void setTiposDeIdentificacion(List<CatalogoItem> tiposDeIdentificacion) {
        this.tiposDeIdentificacion = tiposDeIdentificacion;
    }

    public List<CatalogoItem> getTiposDeGenero() {
        return tiposDeGenero;
    }

    public void setTiposDeGenero(List<CatalogoItem> tiposDeGenero) {
        this.tiposDeGenero = tiposDeGenero;
    }

    public Provincia getProvinciaSelecionada() {
        return provinciaSelecionada;
    }

    public void setProvinciaSelecionada(Provincia provinciaSelecionada) {
        this.provinciaSelecionada = provinciaSelecionada;
    }

    public List<Provincia> getProvincias() {
        return provincias;
    }

    public void setProvincias(List<Provincia> provincias) {
        this.provincias = provincias;
    }

    public List<Canton> getCantones() {
        return cantones;
    }

    public void setCantones(List<Canton> cantones) {
        this.cantones = cantones;
    }

    public Boolean getOcultar() {
        return ocultar;
    }

    public void setOcultar(Boolean ocultar) {
        this.ocultar = ocultar;
    }

    public Boolean getNoEditar() {
        return noEditar;
    }

    public void setNoEditar(Boolean noEditar) {
        this.noEditar = noEditar;
    }
//</editor-fold>
}
