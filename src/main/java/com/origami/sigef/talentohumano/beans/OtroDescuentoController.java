/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.talentohumano.beans;

import com.origami.sigef.catalogoItem.service.CatalogoItemService;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.entities.DescuentoRubroValor;
import com.origami.sigef.common.entities.MasterCatalogo;
import com.origami.sigef.common.entities.OtroDescuento;
import com.origami.sigef.common.entities.RolesDePago;
import com.origami.sigef.common.entities.TipoRol;
import com.origami.sigef.common.entities.ValoresRoles;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.MasterCatalogoService;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.talentohumano.model.ValoresRubroDAO;
import com.origami.sigef.talentohumano.services.DescuentoRubroValorService;
import com.origami.sigef.talentohumano.services.OtroDescuentoService;
import com.origami.sigef.talentohumano.services.RolesDePagoService;
import com.origami.sigef.talentohumano.services.TipoRolService;
import com.origami.sigef.talentohumano.services.ValoresRolesService;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author ORIGAMI2
 */
@Named(value = "otroDescuentoView")
@ViewScoped
public class OtroDescuentoController implements Serializable {

    @Inject
    private OtroDescuentoService otroDescService;
    @Inject
    private RolesDePagoService rolesService;
    @Inject
    private TipoRolService tipoRolService;
    @Inject
    private MasterCatalogoService masterCatalogoService;
    @Inject
    private CatalogoItemService catalogoItemService;
    @Inject
    private ValoresRolesService valorRolService;
    @Inject
    private UserSession userSession;
    @Inject
    private DescuentoRubroValorService descValoresRubroService;
    @Inject
    private ClienteService clienteService;

    private LazyModel<OtroDescuento> lazy;

    private List<TipoRol> rolesMensuales;
    private List<MasterCatalogo> periodos;
    private List<ValoresRoles> listaValores;
    private List<DescuentoRubroValor> listaDescuento;
    private List<ValoresRubroDAO> listaDao;

    private OpcionBusqueda busqueda;
    private TipoRol rolSeleccionado;
    private OtroDescuento descuento;
    private OtroDescuento descuentoSeleccionado;
    private CatalogoItem registrado;
    private String cedula;
    private ValoresRubroDAO valoresDAO;
    private DescuentoRubroValor descuentoValor;
    private double total;
    private List<Cliente> listaCliente;
    private Cliente beneficiarioSeleccionado;
//    private CatalogoItem rolGeneral;

    @PostConstruct
    public void init() {
        cedula = "";
        beneficiarioSeleccionado = new Cliente();
        busqueda = new OpcionBusqueda();
        rolSeleccionado = new TipoRol();
        descuento = new OtroDescuento();
        descuento.setRolPago(new RolesDePago());
        descuento.setTipoRol(new TipoRol());
        listaDescuento = new ArrayList<>();
        listaValores = new ArrayList<>();
        listaCliente = clienteService.listaClienteAndProvedor();
        valoresDAO = new ValoresRubroDAO();
        registrado = catalogoItemService.getEstadoRol("registrado-rol");
//        rolGeneral = catalogoItemService.getEstadoRol("rol_general");
        periodos = masterCatalogoService.findByNamedQuery("MasterCatalogo.findByCatalogoAndTipo", new Object[]{"tipo_cuenta", "CC"});
        rolesMensuales = tipoRolService.listaRolesXanio(busqueda.getAnio());
//        lazy = new LazyModel<>(OtroDescuento.class);
//        lazy.getFilterss().put("estado", true);
//        lazy.getFilterss().put("tipoRol", rolSeleccionado);
//        lazy.getSorteds().put("rolPago.servidor.persona.apellido", "ASC");
//        lazy.setDistinct(false);
    }

    public void guardar() {
        boolean edit = descuento.getId() != null;
        if (edit) {
            descuento.setFechaModificacion(new Date());
            descuento.setUsuarioModifica(userSession.getNameUser());
            otroDescService.edit(descuento);
            editarValores(descuento);
        } else {
            if (validarExiste(descuento)) {
                JsfUtil.addWarningMessage("Información", "Servidor ya se encuentra registrado");
                return;
            }
            if (listaDao == null || listaDao.isEmpty()) {
                JsfUtil.addWarningMessage("Información", "Servidor no cuenta con Rubros de descuento");
                return;
            }
            descuento.setFechaCreacion(new Date());
            descuento.setUsuarioCreacion(userSession.getNameUser());
            descuento.setTipoRol(rolSeleccionado);
            descuento = otroDescService.create(descuento);
            setearValores(descuento);
        }
        resetValue();
        PrimeFaces.current().ajax().update("formMain");
    }

    public boolean validarExiste(OtroDescuento des) {
        List<OtroDescuento> lista = otroDescService.getListaDwsc(rolSeleccionado);
        if (lista != null) {
            if (!lista.isEmpty()) {
                for (OtroDescuento o : lista) {
                    if (o.getRolPago().equals(des.getRolPago())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void editar(OtroDescuento desc) {
        listaDao = new ArrayList<>();
        descuento = new OtroDescuento();
        rolSeleccionado = desc.getTipoRol();
        descuento = desc;
        cedula = desc.getRolPago().getServidor().getPersona().getIdentificacion();
        listaDescuento = descValoresRubroService.getListaXservidorOtroDesc(desc);
        for (DescuentoRubroValor d : listaDescuento) {
            valoresDAO = new ValoresRubroDAO();
            valoresDAO.setOtroDescuento(new OtroDescuento());
            valoresDAO.setValoresRoles(new ValoresRoles());
            valoresDAO.setBeneficiario(new Cliente());
            valoresDAO.setOtroDescuento(descuento);
            valoresDAO.setValoresRoles(d.getValorRol());
            valoresDAO.setValor(d.getValorDescuento());
            valoresDAO.setBeneficiario(d.getBeneficiario());
            listaDao.add(valoresDAO);
        }
    }

    public void editarValores(OtroDescuento d) {
        listaDescuento = new ArrayList<>();
        listaDescuento = descValoresRubroService.getListaXservidorOtroDesc(d);
        if (!listaDescuento.isEmpty()) {
            for (DescuentoRubroValor dr : listaDescuento) {
                for (ValoresRubroDAO v : listaDao) {
                    if (dr.getValorRol().equals(v.getValoresRoles())) {
                        dr.setBeneficiario(v.getBeneficiario());
                        dr.setValorDescuento(v.getValor());
                        descValoresRubroService.edit(dr);
                    }
                }
            }
        }
    }

    public void setearValores(OtroDescuento d) {
        if (!listaDao.isEmpty()) {
            for (ValoresRubroDAO v : listaDao) {
                descuentoValor = new DescuentoRubroValor();
                descuentoValor.setBeneficiario(new Cliente());
                descuentoValor.setOtroDescuento(new OtroDescuento());
                descuentoValor.setValorRol(new ValoresRoles());
                descuentoValor.setOtroDescuento(d);
                descuentoValor.setBeneficiario(v.getBeneficiario());
                descuentoValor.setValorRol(v.getValoresRoles());
                descuentoValor.setValorDescuento(v.getValor());
                descuentoValor = descValoresRubroService.create(descuentoValor);

            }
        }
    }

    public void actulizarRoles() {
        rolesMensuales = tipoRolService.listaRolesXanio(busqueda.getAnio());
    }

    public void buscarServ() {
        if (rolSeleccionado.getId() == null) {
            JsfUtil.addWarningMessage("Información", "Debe de seleccionar un Rol");
            return;
        }
        if (!"registrado-rol".equals(rolSeleccionado.getEstadoAprobacion().getCodigo())) {
            JsfUtil.addWarningMessage("Información", "No se puede Agregar Servidores en Este Rol Seleccionado. ");
            return;
        }
        RolesDePago rol = rolesService.rolPagoXservidor(rolSeleccionado.getAnio(), cedula);
        if (rol != null) {
            this.descuento.setRolPago(rol);
            listaValores = valorRolService.valorRolDescuento(rol);
            if (listaValores.isEmpty()) {
                JsfUtil.addWarningMessage("Información", "Servidor Seleccionado no cuenta con rubros de descuentro.");
            } else {
                listaDao = new ArrayList<>();
                for (ValoresRoles v : listaValores) {
                    descuentoValor = new DescuentoRubroValor();
                    valoresDAO = new ValoresRubroDAO();
                    valoresDAO.setValoresRoles(new ValoresRoles());
                    valoresDAO.setValoresRoles(v);
                    valoresDAO.setValor(BigDecimal.ZERO);
                    if (v.getCuentaContable() == null) {
                        JsfUtil.addWarningMessage("Información", "Rubro " + v.getValorParametrizable().getNombre() + " no Registra una Cuenta Contable");
                    } else {
                        listaDao.add(valoresDAO);
                    }
                }
            }
        } else {
            Map<String, List<String>> params = new HashMap<>();
            String tipoRolid, anio;
            anio = rolSeleccionado.getAnio() + "";
            tipoRolid = rolSeleccionado.getId() + "";
            params.put("ANIO", Arrays.asList(anio));
            params.put("ROL", Arrays.asList(tipoRolid));
            Utils.openDialog("/facelet/talentoHumano/dialgo/dlgServidporCtaAsig", "800", "400", params);
        }

    }

    public void buscarRol() {
        lazy = new LazyModel<>(OtroDescuento.class);
        lazy.getFilterss().put("estado", true);
        lazy.getFilterss().put("tipoRol", rolSeleccionado);
        lazy.getSorteds().put("rolPago.servidor.persona.apellido", "ASC");
        lazy.setDistinct(false);
        PrimeFaces.current().ajax().update("formMain:dataTableFondos");
    }

    public void selectData(SelectEvent evt) {
        descuento = new OtroDescuento();
        descuento.setRolPago(new RolesDePago());
        descuento.setRolPago((RolesDePago) evt.getObject());
        cedula = descuento.getRolPago().getServidor().getPersona().getIdentificacion();
        listaDao = new ArrayList<>();
        listaValores = valorRolService.valorRolDescuento(descuento.getRolPago());
        if (listaValores.isEmpty()) {
            JsfUtil.addWarningMessage("Información", "Servidor Seleccionado no cuenta con rubros de descuentro.");
        } else {
            for (ValoresRoles v : listaValores) {
                cedula = "";
                descuentoValor = new DescuentoRubroValor();
                valoresDAO = new ValoresRubroDAO();
                valoresDAO.setValoresRoles(new ValoresRoles());
                valoresDAO.setValoresRoles(v);
                valoresDAO.setValor(BigDecimal.ZERO);
                if (v.getCuentaContable() == null) {
                    JsfUtil.addWarningMessage("Información", "Rubro " + v.getValorParametrizable().getNombre() + " no Registra una Cuenta Contable");
                } else {
                    listaDao.add(valoresDAO);
                }
            }
        }
        PrimeFaces.current().ajax().update("formMain:dtRubro");
    }

    public List<DescuentoRubroValor> getListaRubros(OtroDescuento desc) {
        listaDescuento = new ArrayList<>();
        return listaDescuento = descValoresRubroService.getListaXservidorOtroDesc(desc);
    }

    public void resetValue() {
        cedula = "";
        descuento = new OtroDescuento();
        listaDescuento = new ArrayList<>();
        listaValores = new ArrayList<>();
        listaDao = new ArrayList<>();
        valoresDAO = new ValoresRubroDAO();
        descuentoValor = new DescuentoRubroValor();
    }

    public void canelar() {
        cedula = "";
        rolSeleccionado = new TipoRol();
        descuento = new OtroDescuento();
        descuento.setRolPago(new RolesDePago());
        descuento.setTipoRol(new TipoRol());
        descuentoValor = new DescuentoRubroValor();
        listaDescuento = new ArrayList<>();
        listaValores = new ArrayList<>();
        listaDao = new ArrayList<>();
        valoresDAO = new ValoresRubroDAO();
        lazy = null;
    }

    public void eliminar(OtroDescuento desc) {
        listaDescuento = new ArrayList<>();
        desc.setEstado(Boolean.FALSE);
        otroDescService.edit(desc);
        listaDescuento = descValoresRubroService.getListaXservidorOtroDesc(desc);
        for (DescuentoRubroValor d : listaDescuento) {
            d.setEstado(Boolean.FALSE);
            descValoresRubroService.edit(d);
        }
    }
    
    public void eliminarRubro(ValoresRubroDAO rubro){
        listaDao.remove(rubro);
    }
    
    //<editor-fold defaultstate="collapsed" desc="Getter and Setter">
    public Cliente getBeneficiarioSeleccionado() {
        return beneficiarioSeleccionado;
    }

    public void setBeneficiarioSeleccionado(Cliente beneficiarioSeleccionado) {
        this.beneficiarioSeleccionado = beneficiarioSeleccionado;
    }

    public List<Cliente> getListaCliente() {
        return listaCliente;
    }

    public void setListaCliente(List<Cliente> listaCliente) {
        this.listaCliente = listaCliente;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public List<DescuentoRubroValor> getListaDescuento() {
        return listaDescuento;
    }

    public void setListaDescuento(List<DescuentoRubroValor> listaDescuento) {
        this.listaDescuento = listaDescuento;
    }

    public List<ValoresRubroDAO> getListaDao() {
        return listaDao;
    }

    public void setListaDao(List<ValoresRubroDAO> listaDao) {
        this.listaDao = listaDao;
    }

    public ValoresRubroDAO getValoresDAO() {
        return valoresDAO;
    }

    public void setValoresDAO(ValoresRubroDAO valoresDAO) {
        this.valoresDAO = valoresDAO;
    }

    public DescuentoRubroValor getDescuentoValor() {
        return descuentoValor;
    }

    public void setDescuentoValor(DescuentoRubroValor descuentoValor) {
        this.descuentoValor = descuentoValor;
    }

    public LazyModel<OtroDescuento> getLazy() {
        return lazy;
    }

    public void setLazy(LazyModel<OtroDescuento> lazy) {
        this.lazy = lazy;
    }

    public List<TipoRol> getRolesMensuales() {
        return rolesMensuales;
    }

    public void setRolesMensuales(List<TipoRol> rolesMensuales) {
        this.rolesMensuales = rolesMensuales;
    }

    public List<MasterCatalogo> getPeriodos() {
        return periodos;
    }

    public void setPeriodos(List<MasterCatalogo> periodos) {
        this.periodos = periodos;
    }

    public OpcionBusqueda getBusqueda() {
        return busqueda;
    }

    public void setBusqueda(OpcionBusqueda busqueda) {
        this.busqueda = busqueda;
    }

    public TipoRol getRolSeleccionado() {
        return rolSeleccionado;
    }

    public void setRolSeleccionado(TipoRol rolSeleccionado) {
        this.rolSeleccionado = rolSeleccionado;
    }

    public OtroDescuento getDescuento() {
        return descuento;
    }

    public void setDescuento(OtroDescuento descuento) {
        this.descuento = descuento;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public OtroDescuento getDescuentoSeleccionado() {
        return descuentoSeleccionado;
    }

    public void setDescuentoSeleccionado(OtroDescuento descuentoSeleccionado) {
        this.descuentoSeleccionado = descuentoSeleccionado;
    }

    public List<ValoresRoles> getListaValores() {
        return listaValores;
    }

    public void setListaValores(List<ValoresRoles> listaValores) {
        this.listaValores = listaValores;
    }
//</editor-fold>
}
